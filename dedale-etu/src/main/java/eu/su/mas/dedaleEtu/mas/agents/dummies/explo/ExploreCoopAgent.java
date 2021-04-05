package eu.su.mas.dedaleEtu.mas.agents.dummies.explo;

import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;

import eu.su.mas.dedaleEtu.mas.behaviours.ExploCoopOneBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploCoopTwoBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.LastStateBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceiveMapBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.SendSignalBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.SignalReceptionBehaviour;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;

/**
 * <pre>
 * ExploreCoop agent. 
 * Basic example of how to "collaboratively" explore the map
 *  - It explore the map using a DFS algorithm and blindly tries to share the topology with the agents within reach.
 *  - The shortestPath computation is not optimized
 *  - Agents do not coordinate themselves on the node(s) to visit, thus progressively creating a single file. It's bad.
 *  - The agent sends all its map, periodically, forever. Its bad x3.
 *  
 * It stops when all nodes have been visited.
 * 
 * 
 *  </pre>
 *  
 * @author hc
 *
 */


public class ExploreCoopAgent extends AbstractDedaleAgent {

	private static final long serialVersionUID = -7969469610241668140L;
	private MapRepresentation myMap;
	private List<String> otherAgentsPos;
	private String nextNode;
	
	public void changeMyMap(MapRepresentation map) {
		myMap = map;
	}
	
	public MapRepresentation getMyMap() {
		return myMap;
	}
	
	public void initializeOtherAgentsPos() {
		otherAgentsPos = new ArrayList<String>();
	}
	
	public void addOtherAgentsPos(String pos) {
		otherAgentsPos.add(pos);
	}
	
	public void emptyOtherAgentsPos() {
		for (String pos : otherAgentsPos) {
			otherAgentsPos.remove(pos);
		}
	}
	
	public List<String> getOtherAgentsPos(){
		return otherAgentsPos;
	}
	
	public void setNextNode(String node) {
		nextNode = node;
	}
	
	public String getNextNode() {
		return nextNode;
	}
	
	/*
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1) set the agent attributes 
	 *	 		2) add the behaviours
	 *          
	 */
	protected void setup(){

		super.setup();
		
		nextNode = null;
		
		final Object[] args = getArguments();
		System.out.println("Arguments of agent: " +args);
		
		List<String> list_agentNames=new ArrayList<String>();
		otherAgentsPos = new ArrayList<String>();
		
		if(args.length==0){
			System.err.println("Error while creating the agent, names of agent to contact expected");
			System.exit(-1);
		}else{
			int i=2;// WARNING YOU SHOULD ALWAYS START AT 2. This will be corrected in the next release.
			while (i<args.length) {
				list_agentNames.add((String)args[i]);
				i++;
			}
		}

		List<Behaviour> lb=new ArrayList<Behaviour>();
		
		/************************************************
		 * 
		 * ADD the behaviours of the Dummy Moving Agent
		 * 
		 ************************************************/
		
		FSMBehaviour fsm = new FSMBehaviour(this);
		fsm.registerFirstState(new ExploCoopOneBehaviour(this), "A");
		fsm.registerState(new SendSignalBehaviour(this), "B");
		fsm.registerState(new SignalReceptionBehaviour(this), "C");
		fsm.registerState(new ReceiveMapBehaviour(this), "D");
		fsm.registerState(new ExploCoopTwoBehaviour(this), "E");
		fsm.registerLastState(new LastStateBehaviour(), "F");
		fsm.registerDefaultTransition("A", "B");
		fsm.registerDefaultTransition("B", "C");
		fsm.registerDefaultTransition("C", "E");
		fsm.registerTransition("C", "D", 1);
		fsm.registerDefaultTransition("D", "E");
		fsm.registerDefaultTransition("E", "A");
		fsm.registerTransition("E", "F", 1);
		
		lb.add(fsm);
		
		
		/***
		 * MANDATORY TO ALLOW YOUR AGENT TO BE DEPLOYED CORRECTLY
		 */
		
		
		addBehaviour(new startMyBehaviours(this,lb));
		
		System.out.println("the  agent "+this.getLocalName()+ " is started");

	}
	
}

