package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;

import dataStructures.serializableGraph.SerializableSimpleGraph;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.explo.ExploreCoopAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class SignalReceptionBehaviour extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -230890405825465975L;
	private MapRepresentation myMap;
	private int end;
	
	public SignalReceptionBehaviour(Agent a) {
		super(a);
	}
	
	@Override
	public void action() {
		this.myMap = ((ExploreCoopAgent)this.myAgent).getMyMap();
		MessageTemplate msgTemplate=MessageTemplate.and(
				MessageTemplate.MatchProtocol("POSITION"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		ACLMessage msgReceived=this.myAgent.receive(msgTemplate);
		if (msgReceived!=null) {
			System.out.println(this.myAgent.getLocalName() +" is in SignalReception and he got a message !");
			end = 1;  
			((ExploreCoopAgent)this.myAgent).addOtherAgentsPos(msgReceived.getContent());
			
			System.out.println(this.myAgent.getLocalName() + " has learned that " + msgReceived.getSender().getLocalName() + " is at position " + (String) msgReceived.getContent());
			
			ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setProtocol("SHARE-TOPO");
			msg.setSender(this.myAgent.getAID());
			msg.addReceiver(msgReceived.getSender());
			System.out.println(this.myAgent.getLocalName() + " sends his map to " + msgReceived.getSender().getLocalName());
			
			
			SerializableSimpleGraph<String, MapAttribute> sg=this.myMap.getSerializableGraph();
			try {					
				msg.setContentObject(sg);
			} catch (IOException e) {
				e.printStackTrace();
			}
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
			
			System.out.println(this.myAgent.getLocalName() + " received the map of " + msgReceived.getSender().getLocalName());
		}
		else {
			end = 0;
			System.out.println(this.myAgent.getLocalName() +" is in SignalReception and he didn't get a message !");
		}
	}
	
	public int onEnd() {
		return end;
	}

}
