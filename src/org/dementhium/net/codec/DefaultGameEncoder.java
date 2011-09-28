package org.dementhium.net.codec;

import org.dementhium.net.message.Message;
import org.dementhium.net.message.Message.PacketType;
import org.dementhium.net.message.MessageBuilder;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.oneone.OneToOneEncoder;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class DefaultGameEncoder extends OneToOneEncoder {

    @Override
    protected Object encode(ChannelHandlerContext ctx, Channel channel, Object message) throws Exception {
        Message packetMessage;
        if (message instanceof MessageBuilder) {
            packetMessage = ((MessageBuilder) message).toMessage();
        } else {
            packetMessage = (Message) message;
        }
        if (!packetMessage.isRaw()) {
            int packetLength = 1 + packetMessage.getLength() + packetMessage.getType().getSize();

            ChannelBuffer response = ChannelBuffers.buffer(packetLength);

            response.writeByte((byte) packetMessage.getOpcode());
            if (packetMessage.getType() == PacketType.VAR_BYTE) {
                response.writeByte((byte) packetMessage.getLength());
            } else if (packetMessage.getType() == PacketType.VAR_SHORT) {
                response.writeShort((short) packetMessage.getLength());
            }
            response.writeBytes(packetMessage.getBuffer());

            return response;
        }
        return packetMessage.getBuffer();
    }

}
