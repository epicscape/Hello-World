package org.dementhium.net.codec.handshake;

import org.dementhium.net.codec.login.RS2LoginDecoder;
import org.dementhium.net.codec.ondemand.OnDemandDecoder;
import org.dementhium.net.message.MessageBuilder;
import org.dementhium.util.Constants;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.frame.FrameDecoder;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class HandshakeDecoder extends FrameDecoder {

	public static int[] DATA = {
		56, 79325, 55568, 46770, 24563, 299978, 44375, 0, 4176, 3589, 109125, 604031, 176138, 292288,
		350498, 686783, 18008, 20836, 16339, 1244, 8142, 743, 119, 699632, 932831, 3931, 2974,
	};

	@Override
	protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer) throws Exception {
		if (ctx.getPipeline().get(HandshakeDecoder.class) != null) {
			ctx.getPipeline().remove(this);
		}
		int opcode = buffer.readByte() & 0xFF;
		MessageBuilder response = new MessageBuilder();
		switch (opcode) {
		case HandshakeConstants.JS5_REQUEST:
			int version = buffer.readInt();
			if (version != Constants.REVISION) {
				response.writeByte((byte) 6);
			} else {
				response.writeByte((byte) 0);
				for (int i = 0; i < 27; i++) {
					response.writeInt(DATA[i]);
				}
				ctx.getPipeline().addBefore("handler", "decoder", new OnDemandDecoder());
			}
			break;
		case HandshakeConstants.LOGIN_REQUEST:
			ctx.getPipeline().addBefore("handler", "decoder", new RS2LoginDecoder());
			response.writeByte((byte) 0);
			break;
		}
		return new HandshakeMessage(opcode, response.toMessage());
	}

}
