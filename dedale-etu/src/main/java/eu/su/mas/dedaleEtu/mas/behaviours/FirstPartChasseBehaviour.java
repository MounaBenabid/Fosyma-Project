package eu.su.mas.dedaleEtu.mas.behaviours;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.explo.ExploreMultiAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import dataStructures.tuple.Couple;
import eu.su.mas.dedale.env.Observation;

public class FirstPartChasseBehaviour extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8120296833499796359L;

	private MapRepresentation myMap;
	
	private int end;
	
	public FirstPartChasseBehaviour(final Agent agent) {
		super(agent);
		this.myMap = ((ExploreMultiAgent) this.myAgent).getMyMap();
	}
	
	@Override
	public void action() {
		//0) Retrieve the current position
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		
		end = 0;
		
		if (myPosition!=null){
			//List of observable from the agent's current position
			List<Couple<String,List<Couple<Observation,Integer>>>> lobs=((AbstractDedaleAgent)this.myAgent).observe();//myPosition
					
			/**
			 * Just added here to let you see what the agent is doing, otherwise he will be too quick
			 */
			try {
				this.myAgent.doWait(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			List<String> nodesStench = new ArrayList<String>();
			List<String> obsNodes = new ArrayList<String>();
			
			Iterator<Couple<String, List<Couple<Observation, Integer>>>> iter=lobs.iterator();
			while(iter.hasNext()){
				Couple<String, List<Couple<Observation, Integer>>> obs_node = iter.next();
				obsNodes.add(obs_node.getLeft());
				if (containsStench(obs_node.getRight())){
					nodesStench.add(obs_node.getLeft());
					end = 1;
				}
			}
			
			((ExploreMultiAgent)this.myAgent).setNodesStench(nodesStench);
			((ExploreMultiAgent)this.myAgent).setObsNodes(obsNodes);
			
			List<Couple<String, List<String>>> n = new ArrayList<Couple<String, List<String>>>();
			((ExploreMultiAgent)this.myAgent).setOthNodesStench(n);
		}
	}

	public boolean containsStench(List<Couple<Observation, Integer>> L){
        for (Couple<Observation, Integer> c:L){
            if(c.getLeft().getName().equals("Stench"))
                return true;
        }
        return false;
    }
	
	public int onEnd() {
		return end;
	}
}
