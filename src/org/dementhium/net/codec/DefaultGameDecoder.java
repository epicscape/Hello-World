package org.dementhium.net.codec;

import org.dementhium.net.GameSession;
import org.dementhium.net.message.Message;
import org.dementhium.net.message.Message.PacketType;
import org.dementhium.util.Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class DefaultGameDecoder extends FrameDecoder {

    public DefaultGameDecoder(GameSession session) {
        session.getChannel().getPipeline().getContext("handler").setAttachment(session);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
    	if (buffer.readableBytes() > 1000) {
    		channel.close();
    		return null;
    	}
    	if (buffer.readable()) {
            int opcode = buffer.readUnsignedByte();
            int length = Constants.PACKET_SIZES[opcode];
            if (opcode < 0 || opcode > 255) {
                buffer.discardReadBytes();
                return null;
            }
            if (length == -1) {
                if (buffer.readable()) {
                    length = buffer.readUnsignedByte();
                }
            }
            if (length <= buffer.readableBytes() && length > 0) {
                byte[] payload = new byte[length];
                buffer.readBytes(payload, 0, length);
                return new Message(opcode, PacketType.STANDARD, ChannelBuffers.wrappedBuffer(payload));
            }
        }
        return null;
    }

}
