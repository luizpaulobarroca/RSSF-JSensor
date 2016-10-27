package projects.Flooding.Timers;

import jsensor.nodes.events.TimerEvent;
import projects.Flooding.Messages.FloodingMessage;
import projects.Flooding.Sensors.FloodingNode;


public class ReceivingTimer extends TimerEvent {

    public static int sendingHops = 0;

    @Override
    public void fire() {



        FloodingNode node = (FloodingNode)this.node;
        FloodingNode dest = node.chooseBest();
        FloodingMessage message = new FloodingMessage(this.node, FloodingTimer.sink, sendingHops++, "", this.node.getChunk());
        message.setMsg("Sending");

        node.unicast(message, dest);
    }
}
