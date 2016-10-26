package projects.Flooding.Timers;

import jsensor.nodes.Node;
import jsensor.nodes.events.TimerEvent;
import jsensor.runtime.Jsensor;
import jsensor.utils.GenerateFilesOmnet;
import projects.Flooding.Messages.FloodingMessage;
import projects.Flooding.Sensors.FloodingNode;


public class ReceivingTimer extends TimerEvent {
    @Override
    public void fire() {
        FloodingNode node = (FloodingNode)this.node;
        FloodingNode dest = node.chooseBest();
        FloodingMessage message = new FloodingMessage(this.node, FloodingTimer.sink, 0, "", this.node.getChunk());
        message.setMsg("Sending");

        FloodingTimer ft = new FloodingTimer();
        ft.startRelative(5, dest);

        node.unicast(message, dest);
    }
}
