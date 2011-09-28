package org.dementhium.net.handler;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.net.GameSession;
import org.dementhium.net.codec.handshake.HandshakeMessage;
import org.dementhium.net.message.Message;
import org.dementhium.task.impl.SessionLogoutTask;
import org.jboss.netty.channel.*;

import java.util.ArrayDeque;
import java.util.Queue;

/**
 * @author 'Mystic Flow
 */
public class DementhiumHandler extends SimpleChannelHandler {

    private final Queue<Message> packetQueue = new ArrayDeque<Message>();

    private boolean receivingPackets;

    public void processPackets(Player player) {
        synchronized (this) {
            Message message;
            while ((message = packetQueue.poll()) != null) {
                try {
                    World.getWorld().getPacketManager().handlePacket(player, message);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) throws Exception {
        //System.out.println("Channel connected: " + ctx.getChannel());
    }

    @Override
    public void channelDisconnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
        GameSession session = (GameSession) ctx.getAttachment();
        if (session != null) {
            Player player = session.getPlayer();
            if (player != null) {
                World.getWorld().submitTask(new SessionLogoutTask(player));
            }
            ctx.setAttachment(null);
        }
    }


    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) throws Exception {
       // e.getCause().printStackTrace();
    }

    @Override
    public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
        Object message = e.getMessage();
        if (message == null) {
            return;
        }
        if (receivingPackets) {
            synchronized (this) {
                packetQueue.add((Message) message);
            }
        } else if (message instanceof HandshakeMessage) {
            HandshakeMessage handshakeMessage = (HandshakeMessage) message;
            ctx.getChannel().write(handshakeMessage.getMessage());
            receivingPackets = true;
        }
    }

}
