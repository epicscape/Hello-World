package org.dementhium.content.clans;

import org.dementhium.model.player.Player;
import org.dementhium.net.message.Message.PacketType;
import org.dementhium.net.message.MessageBuilder;
import org.dementhium.util.Misc;
import org.dementhium.util.TextUtils;

import java.util.Random;


/**
 * @author 'Mystic Flow
 */
public class ClanMessage {

    public static int messageCounter = 1;
    public static final Random r = new Random();
    public static int id = 0;

    public static void sendClanChatMessage(Player from, Player pl, String roomName, String user, String message) {
        long id = (long) (++messageCounter + ((Math.random() * Long.MAX_VALUE) + (Math.random() * Long.MIN_VALUE)));
        byte[] bytes = new byte[256];
        bytes[0] = (byte) message.length();
        int length = 1 + TextUtils.huffmanCompress(message, bytes, 1);
        MessageBuilder bldr = new MessageBuilder(35, PacketType.VAR_BYTE);
        bldr.writeByte(0);
        bldr.writeRS2String(Misc.formatPlayerNameForDisplay(from.getUsername()));
        bldr.writeLong(Misc.stringToLong(roomName));
        bldr.writeShort((int) (id >> 32));
        bldr.writeMediumInt((int) (id - ((id >> 32) << 32)));
        bldr.writeByte(from.getRights());
        bldr.writeBytes(bytes, 0, length);
        if (pl != null) {
            pl.write(bldr.toMessage());
        } else {
            from.write(bldr.toMessage());
        }
        /*int messageCounter = getNextUniqueId();
          MessageBuilder out = new MessageBuilder(35, PacketType.VAR_BYTE);
          out.writeByte(0);
          out.writeRS2String(Misc.formatPlayerNameForDisplay((from.getUsername())));
          out.writeLong(Misc.stringToLong(roomName));
          out.writeShort(r.nextInt());
          byte[] bytes = new byte[256];
          bytes[0] = (byte) message.length();
          int len = 1 + TextUtils.huffmanCompress(message, bytes, 1);
          out.writeBytes(new byte[] { (byte) ((messageCounter << 16) & 0xFF),
                  (byte) ((messageCounter << 8) & 0xFF),
                  (byte) (messageCounter & 0xFF) });
          out.writeByte((byte) from.getRights());
          out.writeBytes(bytes, 0, len);
          //TODO: Check if this is required using this MessageBuilder system? out.endPacketVarByte();
          if (pl != null)
              pl.getConnection().write(out.toMessage());
          else
              from.getConnection().write(out.toMessage());*/
    }

    public static int getNextUniqueId() {
        if (messageCounter >= 16000000) {
            messageCounter = 0;
        }
        return messageCounter++;
    }
    /*public static void sendClanChatMessage(Player from, Player pl, String roomName, String user, String message) {
         int messageCounter = getNextUniqueId();
         MessageBuilder bldr = new MessageBuilder(64, PacketType.VAR_BYTE);
         bldr.writeByte(0);
         bldr.writeRS2String(Misc.formatPlayerNameForDisplay((from.getUsername())));
         bldr.writeLong(Misc.stringToLong(roomName));
         bldr.writeShort(r.nextInt());
         byte[] bytes = new byte[256];
         bytes[0] = (byte) message.length();
         int len = 1 + TextUtils.huffmanCompress(message, bytes, 1);
         bldr.writeMediumInt(messageCounter);
         bldr.writeByte((byte) from.getRights());
         bldr.writeBytes(bytes, 0, len);
         if (pl != null)
             pl.getConnection().write(bldr.toMessage());
         else
             from.getConnection().write(bldr.toMessage());*/
    //TODO
    /* CODE FOR 639!!! WORKING!!!
          * 		long id = (long) (++messageCounter + ((Math.random() * Long.MAX_VALUE) + (Math.random() * Long.MIN_VALUE)));
         byte[] bytes = new byte[256];
         bytes[0] = (byte) message.length();
         int length = 1 + TextUtils.huffmanCompress(message, bytes, 1);
         MessageBuilder bldr = new MessageBuilder(35, PacketType.VAR_BYTE);
         bldr.writeByte(0);
         bldr.writeRS2String(Username);
         bldr.writeLong(Misc.stringToLong("Teh Noob"));
         bldr.writeShort((int) (id));
         bldr.writeSomeInt2((int) (id - ((id))));
         bldr.writeByte(rights);
         bldr.writeBytes(bytes, 0, length);
         player.write(bldr.toMessage());
          */
    //}
}
