package eu.su.mas.dedaleEtu.mas.behaviours;

import dataStructures.serializableGraph.SerializableSimpleGraph;
import eu.su.mas.dedaleEtu.mas.agents.dummies.explo.ExploreCoopAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation.MapAttribute;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;
import jade.lang.acl.UnreadableException;

public class ReceiveMapBehaviour extends OneShotBehaviour{
	
	private MapRepresentation myMap;

	/**
	 * The agent periodically share its map.
	 * It blindly tries to send all its graph to its friend(s)  	
	 * If it was written properly, this sharing action would NOT be in a ticker behaviour and only a subgraph would be shared.

	 * @param a the agent
	 * @param period the periodicity of the behaviour (in ms)
	 * @param mymap (the map to share)
	 * @param receivers the list of agents to send the map to
	 */
	public ReceiveMapBehaviour(Agent a) {
		super(a);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = -568863390879327961L;


	@Override
	public void action() {
		System.out.println(this.myAgent.getLocalName() + " is in ReceiveMap for some reason lol");
		this.myMap = ((ExploreCoopAgent)this.myAgent).getMyMap();
		MessageTemplate msgTemplate=MessageTemplate.and(
				MessageTemplate.MatchProtocol("SHARE-TOPO"),
				MessageTemplate.MatchPerformative(ACLMessage.INFORM));
		ACLMessage msgReceived=this.myAgent.blockingReceive(msgTemplate);
		SerializableSimpleGraph<String, MapAttribute> sgreceived=null;
		try {
			sgreceived = (SerializableSimpleGraph<String, MapAttribute>)msgReceived.getContentObject();
		} catch (UnreadableException e) {
			e.printStackTrace();
		}
		this.myMap.mergeMap(sgreceived);
		((ExploreCoopAgent)this.myAgent).changeMyMap(this.myMap);
		System.out.println(this.myAgent.getLocalName() + " received the map of " + msgReceived.getSender().getLocalName());
	}
	
}
