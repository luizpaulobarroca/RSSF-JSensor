package projects.Flooding.Sensors;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.sun.scenario.effect.Flood;
import jsensor.runtime.Jsensor;
import jsensor.nodes.Node;
import jsensor.nodes.messages.Inbox;
import jsensor.nodes.messages.Message;
import jsensor.utils.Position;
import projects.Flooding.Messages.FloodingMessage;
import projects.Flooding.Timers.FloodingTimer;


/**
 *
 * @author danniel & Matheus
 */
public class FloodingNode extends Node{
    public LinkedList<Long> messagesIDs;
    private ArrayList<FloodingNode> neighboors = new ArrayList<>();
    private Long time0 = null, timef = null;

    @Override
    public void handleMessages(Inbox inbox) {
        if (Jsensor.currentTime > timef) {
            time0 = null;
            timef = null;
            this.chooseBest();
        }
       while(inbox.hasMoreMessages()) {
           Message message = inbox.getNextMessage();
           if(message instanceof FloodingMessage) {
        	   FloodingMessage floodingMessage = (FloodingMessage) message;
               if(this.messagesIDs.contains(floodingMessage.getID())) {
                   continue;
               }
               this.messagesIDs.add(floodingMessage.getID());
               if(floodingMessage.getDestination().equals(this)) {
                   Jsensor.log("time: "+ Jsensor.currentTime +
                           "\t sensorID: " +this.ID+
                           "\t receivedFrom: " +floodingMessage.getSender().getID()+
                           "\t hops: "+ floodingMessage.getHops() +
                           "\t msg: " +floodingMessage.getMsg().concat(this.ID+""));
               } else if(floodingMessage.getMsg().equals("Searching")) {
                   int n = 999999;
                   int cont=0;
                   for (int i=1;i<=n;i++ ){
                       if(n%i == 0)
                           cont=cont+1;
                   }

                   if (cont > 0){
                       floodingMessage.setMsg("Found");
                       floodingMessage.setDestination(floodingMessage.getSender());
                       floodingMessage.setSender(this);
                       this.unicast(floodingMessage, floodingMessage.getDestination());
//                       this.multicast(message);
                   }
               } else if(floodingMessage.getMsg().equals("Found")) {
                   this.neighboors.add((FloodingNode)floodingMessage.getSender());
                   Jsensor.log("time: "+ Jsensor.currentTime +
                           "\t sensorID: " +this.ID+
                           "\t receivedFrom: " +floodingMessage.getSender().getID()+
                           "\t Dist" + this.dist(floodingMessage.getSender()));
               } else if(floodingMessage.getMsg().equals("Sending")) {
                   Jsensor.log("time: "+ Jsensor.currentTime +
                           "\t sensorID: " +this.ID+
                           "\t receivedFrom: " +floodingMessage.getSender().getID());
                   FloodingTimer ft = new FloodingTimer();
                   ft.startRelative(5, this);
               }
           }        
       }
    }

    @Override
    public void onCreation() 
    {
    	//initializes the list of messages received by the node.
        this.messagesIDs = new LinkedList<Long>();
 
        //sends the first messages if is one of the selected nodes
        if(this.ID == 1) {
            FloodingTimer.sink = this;
        }
        if(this.ID == 2)
        {
        	int time = 10 + this.ID * 10;
        	FloodingTimer ft = new FloodingTimer();
            ft.startRelative(time, this);
        }
    }

    public double dist(Node node) {
        Position position0 = this.getPosition();
        double x0 = position0.getPosX(), y0 = position0.getPosY();
        Position positionf = node.getPosition();
        double xf = positionf.getPosX(), yf = positionf.getPosY();
        return Math.sqrt((Math.pow(x0 - xf, 2) + Math.pow(y0 - yf, 2)));
    }

    public FloodingNode chooseBest () {
        FloodingNode bestNode = this;
        double bestDist = 999999999;
        for (Node e : neighboors) {
            double actualDist = this.dist(e);
            if(actualDist < bestDist) {
                bestDist = actualDist;
                bestNode = (FloodingNode) e;
            }
        }
        if(bestNode == this)
            return null;
        return bestNode;
    }
}
