package org.dementhium.model.npc;

import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.misc.DamageManager.DamageHit;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.player.Player;
import org.dementhium.net.message.Message;
import org.dementhium.net.message.Message.PacketType;
import org.dementhium.net.message.MessageBuilder;

import java.util.Iterator;
import java.util.LinkedList;

public class NpcUpdate {

	private final Player player;

	private final LinkedList<NPC> localNpcs = new LinkedList<NPC>();

	public NpcUpdate(Player player) {
		this.player = player;
	}

	public void sendUpdate() {
		if (!player.isActive()) {
			return;
		}
		Message message = createPacket();
		if (message != null) {
			player.write(message);
		}
	}

	public Message createPacket() {
		MessageBuilder packet = new MessageBuilder(6, PacketType.VAR_SHORT);
		MessageBuilder updateBlock = new MessageBuilder();
		packet.startBitAccess();
		packet.writeBits(8, localNpcs.size());
		Iterator<NPC> it = localNpcs.iterator();
		while (it.hasNext()) {
			NPC npc = it.next();
			if (npc == null || npc.destroyed() || npc.isTeleporting() || (npc.isHidden() && !npc.isPrivate(player)) || !player.getLocation().withinDistance(npc.getLocation())) {
				packet.writeBits(1, 1);
				packet.writeBits(2, 3);
				it.remove();
				continue;
			}
			int sprite = npc.getWalkingQueue().getWalkDir();
			if (sprite == -1) {
				if (npc.getMask().requiresUpdate()) {
					packet.writeBits(1, 1);
					packet.writeBits(2, 0);
				} else {
					packet.writeBits(1, 0);
				}
			} else {
				packet.writeBits(1, 1);
				packet.writeBits(2, 1);
				packet.writeBits(3, sprite);
				packet.writeBits(1, npc.getMask().requiresUpdate() ? 1 : 0);
			}
			if (npc.getMask().requiresUpdate()) {
				appendUpdateBlock(player, npc, updateBlock);
			}
		}
		if (localNpcs.size() < 255 && player.getViewDistance() > 0) {
			int added = 0;
			for (NPC npc : World.getWorld().getNpcs()) {
				if (added >= 5 || localNpcs.size() >= 255 || player.getRegion().isDidTeleport()) {
					break;
				}
				if (npc == null || npc.destroyed() || (npc.isHidden() && !npc.isPrivate(player)) || !player.getLocation().withinDistance(npc.getLocation(), player.getViewDistance())) {
					continue;
				}
				if (localNpcs.contains(npc)) {
					continue;
				}
				packet.writeBits(15, npc.getIndex());
				packet.writeBits(14, npc.getId());
				packet.writeBits(1, 1);
				int y = npc.getLocation().getY() - player.getLocation().getY();
				int x = npc.getLocation().getX() - player.getLocation().getX();
				if (x < 0) {
					x += 32;
				}
				if (y < 0) {
					y += 32;
				}
				packet.writeBits(5, x);
				packet.writeBits(5, y);
				packet.writeBits(2, npc.getLocation().getZ());
				packet.writeBits(1, npc.getMask().requiresUpdate() ? 1 : 0);
				packet.writeBits(3, npc.getFaceDir());
				localNpcs.add(npc);
				if (npc.getMask().requiresUpdate()) {
					appendUpdateBlock(player, npc, updateBlock);
				}
				added++;
			}
		}
		packet.writeBits(15, 32767);
		packet.finishBitAccess();
		packet.writeBytes(updateBlock.getBuffer());
		return packet.toMessage();
	}



	private static void appendUpdateBlock(Player player, NPC npc, MessageBuilder out) {
		int maskData = 0x0;
		if (npc.getMask().isForceMovementUpdate()) {
			maskData |= 0x2000;
		}
		if (npc.getDamageManager().getHits().size() > 0) {
			maskData |= 0x4;
		}
		if (npc.getMask().getLastGraphics() != null) {
			maskData |= 0x2;
		}
		if (npc.getMask().isFaceEntityUpdate()) {
			maskData |= 0x8;
		}
		if (npc.getMask().getSwitchId() > -1) {
			maskData |= 0x80;
		}
		if (npc.getMask().getLastAnimation() != null) {
			maskData |= 0x10;
		}
		if (npc.getMask().getFacePosition() != null) {
			maskData |= 0x40;
		}
		if (npc.getMask().isForceTextUpdate()) {
			maskData |= 0x1;
		}
		if (maskData > 128)
			maskData |= 0x20;
		out.writeByte(maskData);
		if (maskData > 128)
			out.writeByte(maskData >> 8);
		if (npc.getMask().isForceMovementUpdate()) {
			applyForceMovementMask(npc, out);
		}
		if (npc.getDamageManager().getHits().size() > 0) {
			applyHitUpdate(player, npc, out);
		}
		if (npc.getMask().getLastGraphics() != null) {
			applyGraphicMask(npc, out);
		}
		if (npc.getMask().isFaceEntityUpdate()) {
			applyFaceEntity(npc, out);
		}
		if (npc.getMask().getSwitchId() > -1) {
			applySwitchMask(npc, out);
		}
		if (npc.getMask().getLastAnimation() != null) {
			applyAnimationMask(npc, out);
		}
		if (npc.getMask().getFacePosition() != null) {
			out.writeLEShortA(npc.getMask().getFacePosition().getX() * 2);
			out.writeLEShortA(npc.getMask().getFacePosition().getY() * 2);
		}
		if (npc.getMask().isForceTextUpdate()) {
			applyForceText(npc, out);
		}
	}

	public static void applyForceMovementMask(NPC npc, MessageBuilder updateBlock) {
		Location myLocation = npc.getLocation();
		Location fromLocation = npc.getLocation();
		Location toLocation = Location.locate(npc.getForceWalk()[0], npc.getForceWalk()[1], 0);

		int distfromx = 0;
		int distfromy = 0;
		boolean positiveFromX = false;
		boolean positiveFromY = false;
		int distanceToX = 0;
		int distanceToY = 0;
		boolean positiveToX = false;
		boolean positiveToY = false;

		if (myLocation.getX() < fromLocation.getX()) {
			positiveFromX = true;
		}
		if (myLocation.getY() < fromLocation.getY()) {
			positiveFromY = true;
		}
		if (fromLocation.getX() < toLocation.getX()) {
			positiveToX = true;
		}
		if (fromLocation.getY() < toLocation.getY()) {
			positiveToY = true;
		}

		if (positiveFromX) {
			distfromx = fromLocation.getX() - myLocation.getX();
		} else {
			distfromx = myLocation.getX() - fromLocation.getX();
		}
		if (positiveFromY) {
			distfromy = fromLocation.getY() - myLocation.getY();
		} else {
			distfromy = myLocation.getY() - fromLocation.getY();
		}
		if (positiveToX) {
			distanceToX = toLocation.getX() - fromLocation.getX();
		} else {
			distanceToX = fromLocation.getX() - toLocation.getX();
		}

		if (positiveToY) {
			distanceToY = toLocation.getY() - fromLocation.getY();
		} else {
			distanceToY = fromLocation.getY() - toLocation.getY();
		}
		updateBlock.writeByteC(positiveFromX ? distfromx : -distfromx);
		updateBlock.writeByteC(positiveFromY ? distfromy : -distfromy);
		updateBlock.writeByteC(positiveToX ? distanceToX : -distanceToX);
		updateBlock.writeByteC(positiveToY ? distanceToY : -distanceToY);

		updateBlock.writeShort(npc.getForceWalk()[2]);
		updateBlock.writeLEShort(npc.getForceWalk()[3]);
		updateBlock.writeByteC(npc.getForceWalk()[4]);
	}

	/*
	 * 	out.writeLEShortA(-15);
	 *	out.writeLEShortA(-120); some weird turnto stuff
	 *
	 *0x8 - 		out.writeShort(15); //more turnto?
	 */
	 public static void testMask(NPC npc, MessageBuilder out) {
		 out.writeShortA(856);
		 out.writeShortA(0);
		 out.writeShortA(0);
		 out.writeShortA(0);
		 out.writeByteS(0);
		 npc.testingmask = false;
	 }

	 public static void applySwitchMask(NPC npc, MessageBuilder out) {
		 out.writeLEShort(npc.getMask().getSwitchId());
	 }

	 public static void applyFaceEntity(NPC npc, MessageBuilder out) {
		 if (npc.getMask().getInteractingEntity() != null) {
			 out.writeShort(npc.getMask().getInteractingEntity().getClientIndex());
		 } else {
			 out.writeShort(-1);
		 }
	 }

	 private static void applyHitUpdate(Player player, NPC other, MessageBuilder bldr) {
		 bldr.writeByteC(other.getDamageManager().getHits().size());
		 for (DamageHit hit : other.getDamageManager().getHits()) {
			 int type = hit.getType().toInteger();
			 if (hit.isMax() && hit.getType() != DamageType.RED_DAMAGE && hit.getType() != DamageType.HEAL) {
				 type += 10;
			 }
			 if (hit.getDamage() < 1) {
				 type = 8;//If there is no damage display a shield!
			 }
			 if (hit.getAttacker() == player || hit.getVictim() == player) {
				 bldr.writeSmart(type);
			 } else {
				 bldr.writeSmart(type + 14);
			 }
			 bldr.writeSmart(hit.getDamage());
			 bldr.writeSmart(hit.getDelay());//Delay TODO
			 bldr.writeByte(hit.getCurrentHealth());
		 }
	 }

	 private static void applyForceText(NPC npc, MessageBuilder out) { //Converted for 639.
		 out.writeRS2String(npc.getMask().getForceText().getLastForceText());
	 }

	 private static void applyAnimationMask(NPC npc, MessageBuilder out) {
		 for (int i = 0; i < 4; i++) {
			 out.writeShortA(npc.getMask().getLastAnimation().getId());
		 }
		 out.writeByteS(npc.getMask().getLastAnimation().getDelay());
	 }

	 private static void applyGraphicMask(NPC npc, MessageBuilder out) {//Converted for 639
		 out.writeShortA(npc.getMask().getLastGraphics().getId());
		 out.writeLEInt(npc.getMask().getLastGraphics().getDelay());
		 out.writeByteC(npc.getMask().getLastGraphics().getHeight());
		 /*
		  * int i_25_ = Class48_Sub1_Sub2.aClass98_Sub22_Sub1_5514
                              .readShortA(65);
                      if ((i_25_ ^ 0xffffffff) == -65536)
                          i_25_ = -1;
                      int i_26_ = Class48_Sub1_Sub2.aClass98_Sub22_Sub1_5514
                              .readIntReverse(true);
                      int i_27_ = Class48_Sub1_Sub2.aClass98_Sub22_Sub1_5514
                              .readByteC((byte) 119);
		  */
	 }

}
