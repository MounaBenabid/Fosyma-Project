package eu.su.mas.dedaleEtu.mas.agents.dummies.explo;

import java.util.ArrayList;
import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedale.mas.agent.behaviours.startMyBehaviours;
import eu.su.mas.dedaleEtu.mas.behaviours.CommunicationBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ExploMultiBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.FirstPartExploBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.LastStateBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceiveMapBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.ReceiveMsgBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.SendMapBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.SendMsgBehaviour;
import eu.su.mas.dedaleEtu.mas.behaviours.TransitionBehaviour;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.AID;
import jade.core.behaviours.Behaviour;
import jade.core.behaviours.FSMBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;

/**
 * <pre>
 * ExploreSolo agent. 
 * It explore the map using a DFS algorithm.
 * It stops when all nodes have been visited.
 *  </pre>
 *  
 * @author hc
 *
 */

public class ExploreMultiAgent extends AbstractDedaleAgent {

	private static final long serialVersionUID = -6431752665590433727L;
	private MapRepresentation myMap;
	private boolean waiting = false;
	private List<Couple<String, String>> otherAgentsPos;
	private String nextNode;
	private List<String> nodesStench;
	private List<String> obsNodes;
	private List<Couple<Couple<String,String>, List<String>>> othNodesStench;
	private String lastPosition;
	private String positionGolem;
	private Couple<List<String>, String> givenPosGolem;
	

	/**
	 * This method is automatically called when "agent".start() is executed.
	 * Consider that Agent is launched for the first time. 
	 * 			1) set the agent attributes 
	 *	 		2) add the behaviours
	 *          
	 */
	protected void setup(){

		super.setup();
		
		this.initializeOtherAgentsPos();
		
		lastPosition = null;
		
		List<Behaviour> lb=new ArrayList<Behaviour>();
	
		List <String> agentsNames = new ArrayList<String>();
		
		List <String> agents_ams = this.getAgentsList();
		for (int i=0; i<agents_ams.size(); i++) {
			String agentName = agents_ams.get(i);
			if (!agentName.equals(this.getLocalName()) && agentName.contains("Explo")) {
				agentsNames.add(agentName);
			}
		}
		
		FirstPartExploBehaviour firstExploSB = new FirstPartExploBehaviour(this,this.myMap);
		ExploMultiBehaviour exploSB = new ExploMultiBehaviour(this,this.myMap);
		TransitionBehaviour lastState = new TransitionBehaviour(this);
		
		FSMBehaviour fsmCallB = new FSMBehaviour(this);				
		CommunicationBehaviour com = new CommunicationBehaviour(this, agentsNames, this.myMap);
		
		fsmCallB.registerFirstState(firstExploSB, "firstExplo");
		fsmCallB.registerState(com, "communication");
		fsmCallB.registerState(exploSB, "explo");
		fsmCallB.registerLastState(lastState, "end");
		
		fsmCallB.registerDefaultTransition("firstExplo", "communication");
		fsmCallB.registerTransition("communication", "communication", 0);
		fsmCallB.registerTransition("communication", "explo", 1);
		fsmCallB.registerTransition("explo", "firstExplo", 0);
		fsmCallB.registerTransition("explo", "end", 1);
		
		lb.add(fsmCallB);
		
		/***
		 * MANDATORY TO ALLOW YOUR AGENT TO BE DEPLOYED CORRECTLY
		 */
		addBehaviour(new startMyBehaviours(this,lb));
		
		System.out.println("the  agent "+this.getLocalName()+ " is started");

	}
	
	public MapRepresentation getMyMap() {
		return myMap;
	}
	
	public void setMyMap(MapRepresentation myMap) {
		this.myMap = myMap;
	}
	
	public boolean isWaiting() {
		return waiting;
	}

	public void setWaiting(boolean waiting) {
		this.waiting = waiting;
	}
	
	public void initializeOtherAgentsPos() {
		otherAgentsPos = new ArrayList<Couple<String, String>>();
	}
	
	public void addOtherAgentsPos(String name, String pos) {
		for (Couple<String,String> o : otherAgentsPos) {
			if (o.getLeft().equals(name)) {
				otherAgentsPos.remove(o);
				break;
			}
		}
		otherAgentsPos.add(new Couple<String, String>(name, pos));
	}
	
	public List<Couple<String, String>> getOtherAgentsPos() {
		return otherAgentsPos;
	}
	
	public void setNextNode(String node) {
		nextNode = node;
	}
	
	public String getNextNode() {
		return nextNode;
	}
	
	public void setNodesStench(List<String> nodes) {
		nodesStench = nodes;
	}
	
	public List<String> getNodesStench() {
		return nodesStench;
	}
	
	public void setObsNodes(List<String> nodes) {
		obsNodes = nodes;
	}
	
	public List<String> getObsNodes() {
		return obsNodes;
	}
	
	public void setOthNodesStench(List<Couple<Couple<String,String>, List<String>>> nodes) {
		othNodesStench = nodes;
	}
	
	public void addOthNodesStench(Couple<Couple<String,String>, List<String>> nodes) {
		othNodesStench.add(nodes);
	}
	
	public List<Couple<Couple<String,String>, List<String>>> getOthNodesStench() {
		return othNodesStench;
	}
	
	public void setLastPosition(String p) {
		lastPosition = p;
	}
	
	public String getLastPosition() {
		return lastPosition;
	}
	
	public void setPositionGolem(String p) {
		positionGolem = p;
	}
	
	public String getPositionGolem() {
		return positionGolem;
	}
	
	public void setGivenPosGolem(Couple<List<String>, String> p) {
		givenPosGolem = p;
	}
	
	public Couple<List<String>, String> getGivenPosGolem() {
		return givenPosGolem;
	}
	
	/**
	 * @return The list of the ( local )names of the agents currently within the platform
	 *          
	 */
	public List <String> getAgentsList(){
		AMSAgentDescription [] agentsDescriptionCatalog = null ;
		List <String> agentsNames= new ArrayList<String>();
		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults ( Long.valueOf(-1) );
			agentsDescriptionCatalog = AMSService.search(this, new AMSAgentDescription (), c );
		}
		catch (Exception e) {
			System.out. println ( "Problem searching AMS: " + e );
			e . printStackTrace () ;
		}
	
		for ( int i =0; i<agentsDescriptionCatalog. length ; i ++){
			AID agentID = agentsDescriptionCatalog[i ]. getName();
			agentsNames.add(agentID.getLocalName());
		}
		return agentsNames;
	
	}
}