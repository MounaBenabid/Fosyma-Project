package eu.su.mas.dedaleEtu.mas.behaviours;

import java.io.IOException;
import java.util.List;

import dataStructures.tuple.Couple;
import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import eu.su.mas.dedaleEtu.mas.agents.dummies.explo.ExploreMultiAgent;

public class CommunicationChasseBehaviour extends OneShotBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8141118043424030960L;
	private int end;
	private List<String> receiversNames;
	
	public CommunicationChasseBehaviour(final Agent myagent, List<String> agentsNames) {
		super(myagent);
		receiversNames = agentsNames;

	}
	
	public void action() {
		
		end = 0;
		
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		String positionGolem = ((ExploreMultiAgent)this.myAgent).getPositionGolem();
		
		List<String> nodesStench = ((ExploreMultiAgent)this.myAgent).getNodesStench();
		
		final ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
		msg.setProtocol("CHASSE-STENCH");
		msg.setSender(this.myAgent.getAID());
		
		for (int i=0; i<receiversNames.size(); i++) {
			String agentName = receiversNames.get(i);
			try {
				Couple<String,String> couple = new Couple<String,String>(myPosition, positionGolem);
				msg.setContentObject(new Couple<Couple<String,String>,List<String>>(couple, nodesStench));
				msg.addReceiver(new AID(agentName, AID.ISLOCALNAME));  
				((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Override
	public int onEnd() {
		this.reset();
		return end;
	}

}
