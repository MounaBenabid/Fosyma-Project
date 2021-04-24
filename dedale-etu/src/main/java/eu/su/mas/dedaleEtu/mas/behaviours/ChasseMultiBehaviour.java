package eu.su.mas.dedaleEtu.mas.behaviours;
import eu.su.mas.dedaleEtu.mas.agents.dummies.explo.ExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import dataStructures.tuple.Couple;

public class ChasseMultiBehaviour extends OneShotBehaviour {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1087655301033262353L;

	private MapRepresentation myMap;
	
	private int end;
	
	public ChasseMultiBehaviour(final Agent myagent) {
		super(myagent);
		this.myMap = ((ExploreMultiAgent) this.myAgent).getMyMap();
	}

	@Override
	public void action() {
		
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		
		
		if (myPosition!=null){
			
			String nextNode = null;
			
			boolean givenGolem = false;
			boolean golem = false;
			boolean givenPGolem = false;
			
			for (Couple<Couple<String,String>, List<String>> c : ((ExploreMultiAgent)this.myAgent).getOthNodesStench()) {
				if (c.getLeft().getRight() != null) {
					givenGolem = true;
				}
			}
			
			if (((ExploreMultiAgent)this.myAgent).getPositionGolem() != null) {
				golem = false;
				
				for (String node : ((ExploreMultiAgent)this.myAgent).getNodesStench()) {
					if (((ExploreMultiAgent)this.myAgent).getPositionGolem().equals(node))
						golem = true;
				}
				
				for (Couple<Couple<String,String>, List<String>> c : ((ExploreMultiAgent)this.myAgent).getOthNodesStench()) {
					if (c.getLeft().getLeft().equals(((ExploreMultiAgent)this.myAgent).getPositionGolem())) {
						golem = false;
						((ExploreMultiAgent)this.myAgent).setPositionGolem(null);
					}
				}
			}
			
			if (((ExploreMultiAgent)this.myAgent).getGivenPosGolem() != null) {
				givenPGolem = true;
				
				for (String s : ((ExploreMultiAgent)this.myAgent).getGivenPosGolem().getLeft()) {
					if (((ExploreMultiAgent)this.myAgent).getGivenPosGolem().equals(s)) {
						givenPGolem = false;
						((ExploreMultiAgent)this.myAgent).setGivenPosGolem(null);
					}
				}
				
				for (Couple<Couple<String,String>, List<String>> c : ((ExploreMultiAgent)this.myAgent).getOthNodesStench()) {
					if (c.getLeft().getLeft().equals(((ExploreMultiAgent)this.myAgent).getPositionGolem())) {
						givenPGolem = false;
						((ExploreMultiAgent)this.myAgent).setGivenPosGolem(null);
					}
				}
			}
			
			if (((ExploreMultiAgent)this.myAgent).getNodesStench().isEmpty() && ((ExploreMultiAgent)this.myAgent).getOthNodesStench().isEmpty()
					&& !givenPGolem) {
				
				List<String> nodes = ((ExploreMultiAgent)this.myAgent).getObsNodes();
				List<String> remNodes = new ArrayList<String>();
				
				for (String n : nodes) {
					Couple<Integer, List<String>> l = this.myMap.sendNodeEdges(n);
					if (l.getLeft() == 1) {
						remNodes.add(n);
					}
				}
				
				for (String n : remNodes) {
					nodes.remove(n);
				}
				
				if (nodes.size() != 1) {
					remNodes = new ArrayList<String>();
					
					for (String n : nodes) {
						if (n.equals(((ExploreMultiAgent)this.myAgent).getLastPosition())) {
							remNodes.add(n);
						}
					}
					
					for (String n : remNodes) {
						nodes.remove(n);
					}
				}

				int size = nodes.size();
				
				Random rand = new Random();
				
				int na = rand.nextInt(size);
				
				nextNode = nodes.get(na);
			}
			
			else if (golem) {
				nextNode = ((ExploreMultiAgent)this.myAgent).getPositionGolem();
			}
			
			else if (givenGolem) {
				
			}
			
			else if (givenPGolem) {
				
			}
			
			else if (((ExploreMultiAgent)this.myAgent).getNodesStench().isEmpty()) {
				
				List<Couple<Couple<String,String>, List<String>>> othNodesStench = ((ExploreMultiAgent)this.myAgent).getOthNodesStench();
				List<String> nodesStench = new ArrayList<String>();
				
				for (Couple<Couple<String,String>, List<String>> l : othNodesStench) {
					for (String s : l.getRight()) {
						if (!nodesStench.contains(s)) 
							nodesStench.add(s);
					}
				}
				
				int smallest = Integer.MAX_VALUE;
				
				for (String node : nodesStench) {
					int size = this.myMap.getShortestPath(myPosition, node).size();
					
					if (size < smallest) {
						smallest = size;
						nextNode = this.myMap.getShortestPath(myPosition, node).get(0);
					}
				}
			}
			
			else {
				
				List<String> obsNodes = ((ExploreMultiAgent)this.myAgent).getObsNodes();
				List<String> nodesStench = ((ExploreMultiAgent)this.myAgent).getNodesStench();
				
				int f = 0;
				
				// on compte le nombre de feuilles observables
				for (String node : obsNodes) {
					Couple<Integer, List<String>> l = this.myMap.sendNodeEdges(node);
					if (l.getLeft() == 1) {
						f += 1;
					}
				}
				
				int fs = 0;
				
				// on compte le nombre de feuilles avec une odeur
				for (String node : nodesStench) {
					Couple<Integer, List<String>> l = this.myMap.sendNodeEdges(node);
					if (l.getLeft() == 1) {
						fs += 1;
					}
				}
				
				// si on n'a qu'une feuille avec une odeur mais plus d'une feuille observables, le golem est peut-être dans la feuille
				// on essaye d'aller dans la feuille
				if (fs == 1 && f > 1) {
					for (String node : obsNodes) {
						Couple<Integer, List<String>> l = this.myMap.sendNodeEdges(node);
						if (l.getLeft() == 1) {
							nextNode = node;
						}
					}
				}
					
				else {
					if (nodesStench.size() != 1) {
						
						List<String> remNodes = new ArrayList<String>();
						
						for (String n : nodesStench) {
							if (n.equals(((ExploreMultiAgent)this.myAgent).getLastPosition())) {
								remNodes.add(n);
							}
						}
						
						for (String n : remNodes) {
							nodesStench.remove(n);
						}
					}
					
					int size = nodesStench.size();
					
					Random rand = new Random();
					
					int na = rand.nextInt(size);
					
					nextNode = nodesStench.get(na);
				}

			}
			
			((ExploreMultiAgent)this.myAgent).setLastPosition(myPosition);
			((ExploreMultiAgent)this.myAgent).setNextNode(nextNode);
			((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
			
		}
		
	}
}
