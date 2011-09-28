package org.dementhium.net.codec.handshake;

import org.dementhium.net.message.Message;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public final class HandshakeMessage {

    private int opcode;

    private Message message;

    public HandshakeMessage(int opcode, Message buffer) {
        this.opcode = opcode;
        this.message = buffer;
    }

    public int getOpcode() {
        return opcode;
    }

    public Message getMessage() {
        return message;
    }
}
