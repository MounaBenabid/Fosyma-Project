package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.domain.AMSService;
import jade.domain.FIPAAgentManagement.AMSAgentDescription;
import jade.domain.FIPAAgentManagement.SearchConstraints;
import jade.lang.acl.ACLMessage;

public class SendSignalBehaviour extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2826250928810907469L;
	private String myPosition;
	private List<String> agentNames;
	
	public SendSignalBehaviour(Agent a) {
		super(a);
	}
	
	private List<String> getAgentsList(){
		AMSAgentDescription [] agentsDescriptionCatalog = null;
		List<String> agentsNames= new ArrayList<String>();
		try {
			SearchConstraints c = new SearchConstraints();
			c.setMaxResults ( Long.valueOf(-1) );
			agentsDescriptionCatalog = AMSService.search(this.myAgent, new AMSAgentDescription(), c );
		}
		catch (Exception e) {
			e. printStackTrace();
		}
		for (int i=0; i<agentsDescriptionCatalog.length; i++){
			AID agentID = agentsDescriptionCatalog[i].getName();
			if (agentID.getLocalName().compareTo(this.myAgent.getLocalName()) != 0 && agentID.getLocalName().contains("Explo")) {
				agentsNames.add(agentID.getLocalName());
			}
		}
		return agentsNames;
	}
	
	@Override
	public void action() {
		this.myPosition = ((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		this.agentNames = this.getAgentsList();
		ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setProtocol("POSITION");
		msg.setSender(this.myAgent.getAID());
		for (String agentName : agentNames) {
			msg.addReceiver(new AID(agentName,AID.ISLOCALNAME));
			System.out.println(this.myAgent.getLocalName() + " sent his position to " + agentName);
		}				
		msg.setContent(myPosition);
		((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
	}

}

