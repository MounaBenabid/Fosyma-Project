package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.explo.ExploreCoopAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.behaviours.OneShotBehaviour;

public class ExploCoopTwoBehaviour extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1283803818197639690L;

	private MapRepresentation myMap;
	private int end;
	
	public ExploCoopTwoBehaviour(final AbstractDedaleAgent myagent) {
		super(myagent);
	}

	@Override
	public void action() {
		System.out.println(this.myAgent.getLocalName() +" is in ExploCoopTwo 'cause he wants to move !");
		this.myMap = ((ExploreCoopAgent)this.myAgent).getMyMap();
		List<String> otherAgentsPos = ((ExploreCoopAgent)this.myAgent).getOtherAgentsPos();
		String nextNode = ((ExploreCoopAgent)this.myAgent).getNextNode();
		String myPosition = ((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		end = 0;
		
		/**
		 * Just added here to let you see what the agent is doing, otherwise he will be too quick
		 */
		try {
			this.myAgent.doWait(250);
		} catch (Exception e) {
			e.printStackTrace();
		}

		//3) while openNodes is not empty, continues.
		if (!this.myMap.hasOpenNode()){
			//Explo finished
			end=1;
			System.out.println(this.myAgent.getLocalName()+" - Exploration successufully done, behaviour removed.");
		}else{
			//4) select next move.
			//4.1 If there exist one open node directly reachable, go for it,
			//	 otherwise choose one from the openNode list, compute the shortestPath and go for it
			if (nextNode==null){
				//no directly accessible openNode
				//chose one, compute the path and take the first step.
				List<Couple<String,Integer>> nodeUs = this.myMap.getDistanceOpenNodes(myPosition);
				if (otherAgentsPos.isEmpty() || nodeUs.size()==1) {
					nextNode=this.myMap.getShortestPathToClosestOpenNode(myPosition).get(0);
					System.out.println(this.myAgent.getLocalName() + " will go to " + nextNode);
				}
				else {
					int i = 0;
					System.out.println(this.myAgent.getLocalName() + " can go to " + nodeUs);
					while (nextNode == null) {
						int j = 0;
						//we compare our path with the other agents
						for (String pos : otherAgentsPos) {
							System.out.println("The other agent is at position " + pos);
							List<Couple<String,Integer>> nodeThem = this.myMap.getDistanceOpenNodes(pos);
							System.out.println("The other agent can go to " + nodeThem);
							if (nodeUs.get(i).getLeft().compareTo(nodeThem.get(0).getLeft())==0) {
								if (nodeUs.get(i).getRight() > nodeThem.get(0).getRight()) {
									i++;
									break;
								}
							}
							j++;
						
						}
						if (j == otherAgentsPos.size()) {
							nextNode=this.myMap.getShortestPath(myPosition,nodeUs.get(i).getLeft()).get(0);//getShortestPath(myPosition,this.openNodes.get(0)).get(0);
							System.out.println(this.myAgent.getLocalName() + " will go to " + nextNode);
						}
						else if (i == otherAgentsPos.size()) {
							nextNode=this.myMap.getShortestPathToClosestOpenNode(myPosition).get(0);
						}
					}
				
				}
			
				//System.out.println(this.myAgent.getLocalName()+"-- list= "+this.myMap.getOpenNodes()+"| nextNode: "+nextNode);
			}else {
				//System.out.println("nextNode notNUll - "+this.myAgent.getLocalName()+"-- list= "+this.myMap.getOpenNodes()+"\n -- nextNode: "+nextNode);
			}

//			}
			((AbstractDedaleAgent)this.myAgent).moveTo(nextNode);
	}
	}
	
	public int onEnd() {
		return end;
	}
}
