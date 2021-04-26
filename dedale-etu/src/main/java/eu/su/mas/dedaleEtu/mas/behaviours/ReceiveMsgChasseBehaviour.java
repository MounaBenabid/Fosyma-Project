package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedaleEtu.mas.agents.dummies.explo.ExploreMultiAgent;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ReceiveMsgChasseBehaviour extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5936080618888488533L;

	public ReceiveMsgChasseBehaviour (final Agent myagent) {
		super(myagent);
	}
	
	@Override
	public void action() {
		MessageTemplate msgTemplate = MessageTemplate.and(
				MessageTemplate.MatchProtocol("CHASSE-STENCH"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));
											
		ACLMessage msg = this.myAgent.receive(msgTemplate);
				
		if (msg != null) {	
			try {
				Couple<Couple<String,String>, List<String>> o = (Couple<Couple<String,String>, List<String>>) msg.getContentObject();					
				((ExploreMultiAgent)this.myAgent).addOthNodesStench(o);
			} catch (UnreadableException e) {
				e.printStackTrace();
			}
		}
	}

}
