package org.dementhium.net.codec.login;

import org.dementhium.model.World;
import org.dementhium.model.definition.PlayerDefinition;
import org.dementhium.net.GameSession;
import org.dementhium.net.codec.DefaultGameDecoder;
import org.dementhium.net.message.MessageBuilder;
import org.dementhium.util.BufferUtils;
import org.dementhium.util.Constants;
import org.dementhium.util.Misc;
import org.dementhium.util.XTEA;
import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.buffer.ChannelBuffers;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.handler.codec.replay.ReplayingDecoder;

import java.io.IOException;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
@SuppressWarnings("unused")
public final class RS2LoginDecoder extends ReplayingDecoder<LoginState> {

    public RS2LoginDecoder() {
        checkpoint(LoginState.PRE_STAGE);
    }

    private GameSession session;

    /**
     * A char array of invalid characters.
     * TODO: Either find the real way of formatting usernames, or add in all invalid characters possible.
     */
    //private final char[] invalidCharacter = new char[] { '/', '*', '-', '+', '|', '(', ')','.', '?', '!', '=', ';', ',', ':', '#' };
    @Override
    protected Object decode(ChannelHandlerContext ctx, Channel channel, ChannelBuffer buffer, LoginState state) throws Exception {
        if (state == LoginState.LOBBY_FINALIZATION || state == LoginState.LOGIN_FINALIZATION) {
            session = new GameSession(channel);
        }
        switch (state) {
            /*case READ_NAME_HASH:
               nameHash = buffer.readByte() & 0xFF;
               System.out.println(nameHash);
               serverKey = serverKeyGenerator.nextLong();
               MessageBuilder response = new MessageBuilder();
               response.writeByte((byte) 0);
               response.writeLong(serverKey);
               channel.write(response);
               checkpoint(LoginState.PRE_STAGE);
               break;*/
            case PRE_STAGE:
                if (readableBytes(buffer) >= 3) {
                    int loginType = buffer.readByte() & 0xFF;
                    int loginPacketSize = buffer.readShort() & 0xFFFF;
                    if (loginPacketSize != readableBytes(buffer)) {
                        throw new IOException("Mismatched login packet size.");
                    }
                    int clientVersion = buffer.readInt();
                    if (clientVersion != Constants.REVISION) {
                        throw new IOException("Incorrect revision read");
                    }
                    if (loginType == 16 || loginType == 18) {
                        checkpoint(LoginState.LOGIN_FINALIZATION);
                    } else if (loginType == 19) {
                        checkpoint(LoginState.LOBBY_FINALIZATION);
                    } else {
                        throw new IOException("Incorrect login type");
                    }
                }
                break;
            case LOBBY_FINALIZATION:
                if (buffer.readable()) {
                    /*buffer.readByte();
                     buffer.readByte(); // display
                     for (int i = 0; i < 24; i++) {
                         buffer.readByte();
                     }
                     BufferUtils.readRS2String(buffer); // settings
                     buffer.readInt();
                     for (int i = 0; i < 34; i++) {
                         buffer.readInt();
                     }*/
                    buffer.readShort();
                    int rsaHeader = buffer.readByte();
                    if (rsaHeader != 10) {
                        throw new IOException("Invalid RSA header.");
                    }
                    int[] keys = new int[4];
                    for (int i = 0; i < keys.length; i++) {
                        keys[i] = buffer.readInt();
                    }
                    buffer.readLong();
                    String password = BufferUtils.readRS2String(buffer);

                    buffer.readLong(); // client key
                    buffer.readLong(); // other client key

                    byte[] block = new byte[readableBytes(buffer)];

                    buffer.readBytes(block);

                    ChannelBuffer decryptedPayload = ChannelBuffers.wrappedBuffer(XTEA.decrypt(keys, block, 0, block.length));
                    String name = BufferUtils.readRS2String(decryptedPayload).toLowerCase();
                    for (char c : name.toCharArray()) {
                        if (!Misc.allowed(c)) {
                            session.write(new MessageBuilder().writeByte(3).toMessage()).addListener(ChannelFutureListener.CLOSE);
                            return null;
                        }
                    }
                    decryptedPayload.readByte(); // screen settings?
                    decryptedPayload.readByte();
                    for (int i = 0; i < 24; i++) {
                        decryptedPayload.readByte();
                    }
                    BufferUtils.readRS2String(decryptedPayload); // settings
                    decryptedPayload.readInt();
                    for (int i = 0; i < 34; i++) {
                        decryptedPayload.readInt();
                    }
                    session.setInLobby(true);

                    //	System.out.println("Login request  name=" + name + " password=" + password);

                    World.getWorld().load(session, new PlayerDefinition(name, password));

                    ctx.getPipeline().replace("decoder", "decoder", new DefaultGameDecoder(session));
                }
                break;
            case LOGIN_FINALIZATION:
                if (buffer.readable()) {
                    buffer.readShort();
                    int rsaHeader = buffer.readByte();
                    if (rsaHeader != 10) {
                        throw new IOException("Invalid RSA header.");
                    }
                    int[] keys = new int[4];
                    for (int i = 0; i < keys.length; i++) {
                        keys[i] = buffer.readInt();
                    }
                    buffer.readLong();
                    String password = BufferUtils.readRS2String(buffer);

                    buffer.readLong(); // client key
                    buffer.readLong(); // other client key

                    byte[] block = new byte[readableBytes(buffer)];

                    buffer.readBytes(block);

                    ChannelBuffer decryptedPayload = ChannelBuffers.wrappedBuffer(XTEA.decrypt(keys, block, 0, block.length));
                    String name = BufferUtils.readRS2String(decryptedPayload).toLowerCase();
                    for (char c : name.toCharArray()) {
                        if (!Misc.allowed(c)) {
                            session.write(new MessageBuilder().writeByte(3).toMessage()).addListener(ChannelFutureListener.CLOSE);
                            return null;
                        }
                    }
                    decryptedPayload.readByte();
                    int mode = decryptedPayload.readByte();
                    int width = decryptedPayload.readShort();
                    int height = decryptedPayload.readShort();
                    decryptedPayload.readByte();
                    //	System.out.println("Width: "+width+" Height: "+height+" Mode: "+mode);
                    for (int i = 0; i < 24; i++) {
                        decryptedPayload.readByte();
                    }
                    BufferUtils.readRS2String(decryptedPayload);
                    decryptedPayload.readInt();
                    decryptedPayload.skipBytes(decryptedPayload.readByte() & 0xff);
                    session.setInLobby(false);
                    //	System.out.println("Full Login request  name=" + name + " password=" + password);
                    World.getWorld().load(session, new PlayerDefinition(name, password));
                    ctx.getPipeline().replace("decoder", "decoder", new DefaultGameDecoder(session));
                    //InterfaceDecoder.switchWindow(session.getPlayer(), mode);
                    session.setDisplayMode(mode);
                }
                break;
        }

        return null;
    }

    public int readableBytes(ChannelBuffer buffer) {
        return buffer.writerIndex() - buffer.readerIndex();
    }

}
