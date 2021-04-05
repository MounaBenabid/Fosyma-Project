package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.Iterator;
import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.explo.ExploreCoopAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import jade.core.behaviours.OneShotBehaviour;

public class ExploCoopOneBehaviour extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1283803818197639690L;

	private MapRepresentation myMap;
	
	public ExploCoopOneBehaviour(final AbstractDedaleAgent myagent) {
		super(myagent);
	}
	
	@Override
	public void action() {
		
		if (((ExploreCoopAgent)this.myAgent).getMyMap() == null) {
			((ExploreCoopAgent)this.myAgent).changeMyMap(new MapRepresentation());
			((ExploreCoopAgent)this.myAgent).initializeOtherAgentsPos();
		}
		
		
		this.myMap = ((ExploreCoopAgent)this.myAgent).getMyMap();
		//0) Retrieve the current position
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		
		System.out.println(this.myAgent.getLocalName() + " is in ExploCoopOne !");
		((ExploreCoopAgent)this.myAgent).initializeOtherAgentsPos();
		
		if (myPosition!=null){
			//List of observable from the agent's current position
			List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();//myPosition

			//1) remove the current node from openlist and add it to closedNodes.
			this.myMap.addNode(myPosition, MapAttribute.closed);

			//2) get the surrounding nodes and, if not in closedNodes, add them to open nodes.
			((ExploreCoopAgent)this.myAgent).setNextNode(null);
			String nextNode=null;
			Iterator<Couple<String, List<Couple<Observation, Integer>>>> iter=lobs.iterator();
			while(iter.hasNext()){
				String nodeId=iter.next().getLeft();
				boolean isNewNode=this.myMap.addNewNode(nodeId);
				//the node may exist, but not necessarily the edge
				if (myPosition!=nodeId) {
					this.myMap.addEdge(myPosition, nodeId);
					if (nextNode==null && isNewNode) {
						nextNode=nodeId;
						((ExploreCoopAgent)this.myAgent).setNextNode(nodeId);
					}
				}
			}
			((ExploreCoopAgent)this.myAgent).changeMyMap(this.myMap);
		}
	}
	
}
