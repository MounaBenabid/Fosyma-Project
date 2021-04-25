package eu.su.mas.dedaleEtu.mas.behaviours;

import java.util.List;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.ExploreSoloAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.explo.ExploreMultiAgent;
import jade.core.AID;
import jade.core.behaviours.SimpleBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;


public class SendMsgBehaviour extends SimpleBehaviour{
	
	private static final long serialVersionUID = 9088209402507795289L;

	private boolean finished = false;
	private boolean getAnswer = false;
	
	/**
	 * Name of the agent that should receive the values
	 */
	private List<String> receiversNames;
	
	
	public SendMsgBehaviour(final AbstractDedaleAgent myagent, List<String> receiversNames) {
		super(myagent);
		this.receiversNames=receiversNames;
		
	}

	public void ping() {
		
		String myPosition=((AbstractDedaleAgent)this.myAgent).getCurrentPosition();
		
		for (int i=0; i<receiversNames.size(); i++) {
			String agentName = receiversNames.get(i);
			final ACLMessage msg = new ACLMessage(ACLMessage.INFORM);
			msg.setProtocol("PING");
			msg.setSender(this.myAgent.getAID());
			
			//System.out.println("<---- test sendping ");
			msg.setContent(myPosition);
			//msg.setContent(((ExploreMultiAgent)this.myAgent).getNextNode());
			msg.addReceiver(new AID(agentName, AID.ISLOCALNAME));  
			((AbstractDedaleAgent)this.myAgent).sendMessage(msg);
		}
	}
	
	
	public void getAnswer() {
		final MessageTemplate msgTemplate = MessageTemplate.MatchPerformative(ACLMessage.CONFIRM);
		
		final ACLMessage msg = this.myAgent.receive(msgTemplate);
		
		if (msg != null) {		
			System.out.println(this.myAgent.getLocalName()+ "<---- Got answer from "+msg.getSender().getLocalName()+", content= "+msg.getContent());
			for (String receiverName:receiversNames) {
				if (msg.getSender().getLocalName().equals(receiverName))
					this.getAnswer = true;
					SendMapBehaviour sendMapB = new SendMapBehaviour(((AbstractDedaleAgent)this.myAgent), receiverName);
					((ExploreMultiAgent)this.myAgent).addBehaviour(sendMapB);
			}
		}
		else {
			this.getAnswer = false;
		}
	}
	
	public void action() {
		ping();
		getAnswer();
		this.finished=true; 
	}

	public boolean done() {
		return finished;
	}
	
	@Override
	public int onEnd() {
		if (this.getAnswer) return 1;
		else return 0;
	}

}
