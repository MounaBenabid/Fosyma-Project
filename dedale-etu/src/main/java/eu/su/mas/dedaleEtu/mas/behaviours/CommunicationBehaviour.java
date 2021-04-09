package eu.su.mas.dedaleEtu.mas.behaviours;

import eu.su.mas.dedale.mas.AbstractDedaleAgent;
import eu.su.mas.dedaleEtu.mas.agents.dummies.ExploreSoloAgent;
import eu.su.mas.dedaleEtu.mas.knowledge.MapRepresentation;
import jade.core.AID;
import jade.core.behaviours.FSMBehaviour;
import jade.core.behaviours.ParallelBehaviour;
import jade.lang.acl.ACLMessage;
import jade.lang.acl.MessageTemplate;

public class CommunicationBehaviour extends ParallelBehaviour {

	/**
	 * 
	 */
	private static final long serialVersionUID = -9105455214162907106L;
	private int end;
	
	
	public CommunicationBehaviour(final AbstractDedaleAgent myagent, String receiverName, MapRepresentation myMap) {
		super();
		
		
		SendMsgBehaviour sendMsgB = new SendMsgBehaviour(myagent, receiverName);
		ReceiveMsgBehaviour	receiveMsgB = new ReceiveMsgBehaviour(myagent);
		SendMapBehaviour sendMapB = new SendMapBehaviour(myagent, receiverName, myMap);
		ReceiveMapBehaviour receiveMapB = new ReceiveMapBehaviour(myagent, myMap);
		LastStateBehaviour lastState = new LastStateBehaviour();
		
		FSMBehaviour fsmSend = new FSMBehaviour(myagent);
		fsmSend.registerFirstState(sendMsgB, "ping");
		fsmSend.registerLastState(sendMapB, "shareMap");
		fsmSend.registerLastState(lastState, "end");
		fsmSend.registerTransition("ping", "shareMap", 1);
		fsmSend.registerTransition("ping", "end", 0);
		
		FSMBehaviour fsmReceive = new FSMBehaviour(myagent);
		fsmReceive.registerFirstState(receiveMsgB, "getPing");
		fsmReceive.registerLastState(receiveMapB, "receiveMap");
		fsmReceive.registerLastState(lastState, "end");
		fsmReceive.registerTransition("getPing", "receiveMap", 1);
		fsmReceive.registerTransition("getPing", "end", 0);
		
		addSubBehaviour(fsmSend);
		addSubBehaviour(fsmReceive);
		
		if(sendMsgB.onEnd() == 1) {
			if(sendMapB.onEnd() == 0)
				end = 0;
		}
		else if(receiveMsgB.onEnd() == 1) {
			if(receiveMapB.onEnd() == 0)
				end = 0;
		}
		else {
			end = 1;
		}
	}

	@Override
	public int onEnd() {
		this.reset();
		return end;
	}

}
