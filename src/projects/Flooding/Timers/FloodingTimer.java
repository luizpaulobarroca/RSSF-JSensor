package projects.Flooding.Timers;

import jsensor.runtime.Jsensor;
import jsensor.nodes.Node;
import jsensor.nodes.events.TimerEvent;
import jsensor.utils.GenerateFilesOmnet;
import projects.Flooding.Messages.FloodingMessage;


/**
 *
 * @author Danniel & Matheus
 */
public class FloodingTimer extends TimerEvent{

	public static Node sink;

    @Override
    public void fire() {
    	
    	Node destination = sink;
        
        FloodingMessage message = new FloodingMessage(this.node, destination, 0, "", this.node.getChunk());
        
        String messageText = "Searching";
        message.setMsg(messageText);

		Jsensor.log("time: "+ Jsensor.currentTime +"\t sensorID: "+this.node.getID()+ "\t searching next node");
//    	Jsensor.log("time: "+ Jsensor.currentTime +"\t sensorID: "+this.node.getID()+ "\t sendTo: " +destination.getID());
		

        GenerateFilesOmnet.addStartNode(this.node.getID(), destination.getID(), Jsensor.currentTime);

        ReceivingTimer rt = new ReceivingTimer();
        rt.startRelative(20, this.node);

	    this.node.multicast(message);
    }    
}
