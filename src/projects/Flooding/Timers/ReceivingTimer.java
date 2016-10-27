package projects.Flooding.Timers;

import jsensor.nodes.events.TimerEvent;
import projects.Flooding.Messages.FloodingMessage;
import projects.Flooding.Sensors.FloodingNode;


public class ReceivingTimer extends TimerEvent {
    @Override
    public void fire() {
        FloodingNode node = (FloodingNode)this.node;
        FloodingNode dest = node.chooseBest();
        FloodingMessage message = new FloodingMessage(this.node, FloodingTimer.sink, 0, "", this.node.getChunk());
        message.setMsg("Sending");

        node.unicast(message, dest);
    }
}
