package org.dementhium.model.player;

import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.definition.NPCDefinition;
import org.dementhium.model.misc.DamageManager.DamageHit;
import org.dementhium.net.ActionSender;
import org.dementhium.net.message.Message.PacketType;
import org.dementhium.net.message.MessageBuilder;
import org.dementhium.util.Misc;

import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author 'Mystic Flow
 */
public class PlayerUpdate {

    public static final int MAX_ADD = 10;

    /*public static Comparator<Player> LOCAL_ORDER = new Comparator<Player>() {
         @Override
         public int compare(Player first, Player second) {
             if(first.getIndex() > second.getIndex()) {
                 return 1;
             } else if(first.getIndex() < second.getIndex()) {
                 return -1;
             } else {
                 return 0;
             }
         }
     };*/

    private final Player player;

    private final LinkedList<Player> localPlayers = new LinkedList<Player>();
    private final boolean[] added = new boolean[2048];

    public PlayerUpdate(Player player) {
        this.player = player;
    }

    public void loginData(MessageBuilder stream) {
        stream.startBitAccess();
        stream.writeBits(30, player.getLocation().getX() << 14 | player.getLocation().getY() & 0x3fff | player.getLocation().getZ() << 28);
        short playerIndex = player.getIndex();
        for (int index = 1; index < 2048; index++) {
            if (index == playerIndex)
                continue;
            Player other = World.getWorld().getPlayers().get(index);
            if (other == null || !other.isOnline()) {
                stream.writeBits(18, 0);
                continue;
            }
            if (!player.getLocation().withinDistance(other.getLocation(), player.getViewDistance())) {
                stream.writeBits(18, 0);
                continue;
            }
            stream.writeBits(18, other.getLocation().get18BitsHash());
        }
        stream.finishBitAccess();
    }

    public void sendUpdate() {
        if (!player.isOnline() || player.destroyed()) {
            return;
        }
        if (player.getRegion().isDidMapRegionChange()) {
        	player.updateMap();
        }

        boolean chatUpdate = player.getMask().getLastChatMessage() != null;

        MessageBuilder packet = new MessageBuilder(70, PacketType.VAR_SHORT);
        MessageBuilder updateBlock = new MessageBuilder();
        appendPlayerUpdateBlock(updateBlock, player, false);
        packet.startBitAccess();
        if (player.getMask().requiresUpdate()) {
            packet.writeBits(1, 1);
            applyLocalUpdate(packet, player);
        } else {
            packet.writeBits(1, 0);
        }
        if (chatUpdate) {
            ActionSender.sendPublicChatMessage(player, player.getIndex(), player.getRights(), player.getMask().getLastChatMessage());
        }
        if (localPlayers.size() > 0) {
            Iterator<Player> it = localPlayers.iterator();
            while (it.hasNext()) {
                Player other = it.next();
                if (other != null && other.isOnline() && other.getLocation().withinDistance(player.getLocation(), player.getViewDistance())) {
                    if (chatUpdate) {
                        ActionSender.sendPublicChatMessage(other, player.getIndex(), player.getRights(), player.getMask().getLastChatMessage());
                    }
                    appendPlayerUpdateBlock(updateBlock, other, false);
                    if (other.getMask().requiresUpdate()) {
                        packet.writeBits(1, 1);
                        packet.writeBits(11, other.getIndex());
                        applyLocalUpdate(packet, other);
                        continue;
                    }
                    packet.writeBits(1, 0);
                } else {
                    added[other.getIndex()] = false;
                    packet.writeBits(1, 1);
                    packet.writeBits(11, other.getIndex());
                    removeLocalPlayer(packet, other);
                    it.remove();
                }
            }
        }
        if (player.getViewDistance() > 0 && player.isActive()) {
            int addedCount = 0;
            for (Player other : World.getWorld().getPlayers()) {
                if (addedCount >= 5 || localPlayers.size() >= 255) {
                    break;
                }
                if (other == null || !other.isOnline()) {
                    continue;
                }
                if (other == player || added[other.getIndex()] || !player.getLocation().withinDistance(other.getLocation(), player.getViewDistance()) || !other.isOnline()) {
                    continue;
                }
                addedCount++;
                packet.writeBits(1, 1);
                packet.writeBits(11, other.getIndex());
                addLocalPlayer(packet, other);
                appendPlayerUpdateBlock(updateBlock, other, true);
                added[other.getIndex()] = true;
            }
        }
        packet.writeBits(1, 0);
        packet.finishBitAccess();
        packet.writeBytes(updateBlock.getBuffer());
        player.write(packet.toMessage());
    }

    private void addLocalPlayer(MessageBuilder packet, Player other) {
        packet.writeBits(2, 0);
        boolean updateHash = false;
        packet.writeBits(1, updateHash ? 0 : 1);
        if (!updateHash) {
            packet.writeBits(2, 3);
            packet.writeBits(18, other.getLocation().get18BitsHash());
        }
        packet.writeBits(6, other.getLocation().getX() - (other.getLocation().getRegionX() << 6));
        packet.writeBits(6, other.getLocation().getY() - (other.getLocation().getRegionY() << 6));
        packet.writeBits(1, 1);
        added[other.getIndex()] = true;
        localPlayers.add(other);
    }

    private void applyLocalUpdate(MessageBuilder packet, Player other) {
        if (other.getRegion().isDidTeleport()) {
            sendLocalPlayerTeleport(packet, other);
            return;
        }
        int walkDir = other.getWalkingQueue().getWalkDir();
        int runDir = other.getWalkingQueue().getRunDir();
        sendLocalPlayerStatus(packet, walkDir > -1 ? 1 : runDir > -1 ? 2 : 0, true);
        if (walkDir < 0 && runDir < 0)
            return;
        packet.writeBits(walkDir > -1 ? 3 : 4, walkDir > -1 ? walkDir : runDir);
    }

    private void sendLocalPlayerTeleport(MessageBuilder packet, Player other) {
        sendLocalPlayerStatus(packet, 3, true);
        packet.writeBits(1, 1);
        packet.writeBits(30, other.getLocation().getY() | other.getLocation().getZ() << 28 | other.getLocation().getX() << 14);
    }

    private void sendLocalPlayerStatus(MessageBuilder packet, int type, boolean status) {
        packet.writeBits(1, status ? 1 : 0);
        packet.writeBits(2, type);
    }

    private void removeLocalPlayer(MessageBuilder packet, Player other) {
        sendLocalPlayerStatus(packet, 0, false);
        packet.writeBits(1, 0);
    }

    private void appendPlayerUpdateBlock(MessageBuilder updateBlock, Player other, boolean forceAppearance) {
        if (!other.getMask().requiresUpdate() && !forceAppearance) {
            return;
        }
        int maskData = 0;
        if (other.getMask().getLastGraphics() != null) {
            maskData |= 0x4000;
        }
        if (other.getRegion().isDidTeleport()) {
            maskData |= 0x2000;
        }
        if (other.getMask().isForceMovementUpdate()) {
            maskData |= 0x200;
        }
        if (other.getMask().getFacePosition() != null) {
            maskData |= 0x4;
        }
        if (other.getMask().isFaceEntityUpdate()) {
            maskData |= 0x2;
        }
        if (other.getDamageManager().getHits().size() > 0) {
            maskData |= 0x8;
        }
        if (other.getMask().getLastAnimation() != null) {
            maskData |= 0x10;
        }
        if (other.getMask().isApperanceUpdate() || forceAppearance) {
            maskData |= 0x40;
        }
        if (other.getMask().isForceTextUpdate()) {
            maskData |= 0x8000;
        }
        if (other.getWalkingQueue().getWalkDir() != -1 || other.getWalkingQueue().getRunDir() != -1) {
            maskData |= 0x1;
        }
        //		if (other.getMask().getLastHeal() != null) {
        //			maskData |= 0x100;
        //		}
        if (maskData > 128)
            maskData |= 0x20;
        if (maskData > 32768)
            maskData |= 0x800;
        updateBlock.writeByte(maskData);
        if (maskData > 128)
            updateBlock.writeByte(maskData >> 8);
        if (maskData > 32768)
            updateBlock.writeByte(maskData >> 16);
        if (other.getMask().getLastGraphics() != null) {
            applyGraphicMask(other, updateBlock);
        }
        if (other.getRegion().isDidTeleport()) {
            applyTeleTypeMask(updateBlock);
        }
        if (other.getMask().isForceMovementUpdate()) {
            applyForceMovementMask(other, updateBlock);
        }
        if (other.getMask().getFacePosition() != null) {
            applyTurnToCoordMask(other, updateBlock);
        }
        if (other.getMask().isFaceEntityUpdate()) {
            applyTurnToMask(other, updateBlock);
        }
        if (other.getDamageManager().getHits().size() > 0) {
            applyHitMask(player, other, updateBlock);
        }
        if (other.getMask().getLastAnimation() != null) {
            applyAnimationMask(other, updateBlock);
        }
        if (other.getMask().isApperanceUpdate() || forceAppearance) {
            applyAppearanceMask(other, updateBlock);
        }
        if (other.getMask().isForceTextUpdate()) {
            applyForceText(other, updateBlock);
        }
        if (other.getWalkingQueue().getWalkDir() != -1 || other.getWalkingQueue().getRunDir() != -1) {
            applyMovementMask(other, updateBlock);
        }
        //		if (other.getMask().getLastHeal() != null) {
        //			applyHealMask(other, updateBlock);
        //		}
    }

    public void applyForceMovementMask(Player p, MessageBuilder updateBlock) {
        Location myLocation = p.getLocation();
        Location fromLocation = p.getLocation();
        Location toLocation = Location.locate(p.getForceWalk()[0], p.getForceWalk()[1], 0);

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
        updateBlock.writeByteA(positiveFromY ? distfromy : -distfromy);
        updateBlock.writeByteC(positiveToX ? distanceToX : -distanceToX);
        updateBlock.writeByteC(positiveToY ? distanceToY : -distanceToY);

        updateBlock.writeLEShort(p.getForceWalk()[2]);
        updateBlock.writeLEShortA(p.getForceWalk()[3]);
        updateBlock.writeByteA(p.getForceWalk()[4]);
    }

    private static void applyTurnToCoordMask(Player p, MessageBuilder updateBlock) {
        int dX = p.getLocation().getX() - p.getMask().getFacePosition().getX();
        int dY = p.getLocation().getY() - p.getMask().getFacePosition().getY();
        updateBlock.writeLEShort(((int) (Math.atan2(dX, dY) * 2607.5945876176133)) & 0x3fff);
        //updateBlock.writeLEShort(Misc.getFacingDirection(p.getLocation().getX(), p.getLocation().getY(), p.getMask().getFacePosition().getX(), p.getMask().getFacePosition().getY()));
    }

    public static void applyForceText(Player p, MessageBuilder updateBlock) {
        updateBlock.writeRS2String(p.getMask().getForceText().getLastForceText());
    }

    public static void applyTurnToMask(Player p, MessageBuilder bldr) {
        if (p.getMask().getInteractingEntity() != null) {
            bldr.writeLEShort(p.getMask().getInteractingEntity().getClientIndex());
        } else {
            bldr.writeLEShort(-1);
        }
    }

    public static void applyTeleTypeMask(MessageBuilder bldr) {
        bldr.writeByteA(127);
    }

    public static void applyHitMask(Player player, Player other, MessageBuilder bldr) {
        bldr.writeByte(other.getDamageManager().getHits().size());
        for (DamageHit hit : other.getDamageManager().getHits()) {
            if (hit.getPartner() != null) {
                bldr.writeSmart(32767);
            }
            int type = hit.getType().toInteger();
            if (type != 9) {
                if (hit.isMax()) {
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
            } else {
                bldr.writeSmart(9);
            }
            bldr.writeSmart(hit.getDamage());
            if (hit.getPartner() != null) {
                int type2 = hit.getPartner().getType().toInteger();
                if (hit.getAttacker() == player || hit.getVictim() == player) {
                    bldr.writeSmart(type2);
                } else {
                    bldr.writeSmart(type2 + 14);
                }
                bldr.writeSmart(hit.getPartner().getDamage());
            }
            bldr.writeSmart(hit.getDelay());
            bldr.writeByte(hit.getCurrentHealth());
        }
    }

    public static void applyHealMask(Player p, MessageBuilder outStream) {
        outStream.writeShort(p.getMask().getLastHeal().getHealDelay());
        outStream.writeByteS(p.getMask().getLastHeal().getBarDelay());
        outStream.writeByteS(p.getMask().getLastHeal().getHealSpeed());
    }

    public static void applyAnimationMask(Player p, MessageBuilder updateBlock) {
        for (int i = 0; i < 4; i++) {
            updateBlock.writeLEShortA(p.getMask().getLastAnimation().getId());
        }
        updateBlock.writeByteC(p.getMask().getLastAnimation().getDelay());
    }

    public static void applyGraphicMask(Player p, MessageBuilder outStream) {
        outStream.writeLEShortA(p.getMask().getLastGraphics().getId());
        outStream.writeInt2(p.getMask().getLastGraphics().getDelay());
        outStream.writeByte(p.getMask().getLastGraphics().getHeight());
    }

    public static void applyMovementMask(Player p, MessageBuilder outStream) {
//		if(p.getWalkingQueue().getWalkDir() != -1 && p.getWalkingQueue().getRunEnergy() > 0 && p.getWalkingQueue().isRunning() && !p.hasTick("following_mob")) {
//			outStream.writeByteA(2);
//		} else {
        outStream.writeByteA(p.getWalkingQueue().getWalkDir() != -1 ? 1 : 2);
        //	}
    }

    public void applyAppearanceMask(Player p, MessageBuilder updateMessage) {
    	// todo cache this shit yoooo
        MessageBuilder appearanceData = new MessageBuilder();
        // Do we even need this, smh
        NPCDefinition def = p.getAppearance().getNpcType() != -1 ? NPCDefinition.forId(p.getAppearance().getNpcType()) : null;
        int hash = 0;
        hash |= p.getAppearance().getGender() & 0x1;
        hash |= 0x4; //enables combat colouring ^.^
        appearanceData.writeByte(hash);
        appearanceData.writeByte(0); // titles
        appearanceData.writeByte(p.getSkullManager().isSkulled() ? 0 : -1);
        appearanceData.writeByte(p.getPrayer().getHeadIcon());
        appearanceData.writeByte(0); //TODO Identimify this shit yoooo
        if (p.getAppearance().getNpcType() == -1) {
            for (int i = 0; i < 4; i++) {
            	Item item =  p.getEquipment().get(i);
                if (item == null)
                    appearanceData.writeByte(0);
                else
                    appearanceData.writeShort(32768 + item.getDefinition().getEquipId());
            }
            if (p.getEquipment().get(Equipment.SLOT_CHEST) != null) {
                appearanceData.writeShort(32768 + p.getEquipment().get(Equipment.SLOT_CHEST).getDefinition().getEquipId());
            } else {
                appearanceData.writeShort(0x100 + p.getAppearance().getLook()[2]);
            }
            if (p.getEquipment().get(Equipment.SLOT_SHIELD) != null) {
                appearanceData.writeShort(32768 + p.getEquipment().get(Equipment.SLOT_SHIELD).getDefinition().getEquipId());
            } else {
                appearanceData.writeByte((byte) 0);
            }
            Item chest = p.getEquipment().get(Equipment.SLOT_CHEST);
            if (chest != null) {
                if (!Equipment.isFullBody(chest.getDefinition())) {
                    appearanceData.writeShort(0x100 + p.getAppearance().getLook()[3]);
                } else {
                    appearanceData.writeByte((byte) 0);
                }
            } else {
                appearanceData.writeShort(0x100 + p.getAppearance().getLook()[3]);
            }
            if (p.getEquipment().get(Equipment.SLOT_LEGS) != null) {
                appearanceData.writeShort(32768 + p.getEquipment().get(Equipment.SLOT_LEGS).getDefinition().getEquipId());
            } else {
                appearanceData.writeShort(0x100 + p.getAppearance().getLook()[5]);
            }
            Item cape = p.getEquipment().get(Equipment.SLOT_CAPE);
            if (cape == null) {
                cape = new Item(-1, 0);
            }
            Item hat = p.getEquipment().get(Equipment.SLOT_HAT);
            if (hat != null) {
                if (!Equipment.isFullHat(hat.getDefinition()) && !Equipment.isFullMask(hat.getDefinition())
                        && cape.getId() != 4042 && cape.getId() != 4041) {
                    appearanceData.writeShort(0x100 + p.getAppearance().getLook()[0]);
                } else {
                    appearanceData.writeByte((byte) 0);
                }
            } else {
                appearanceData.writeShort(0x100 + p.getAppearance().getLook()[0]);
            }
            if (p.getEquipment().get(Equipment.SLOT_HANDS) != null) {
                appearanceData.writeShort(32768 + p.getEquipment().get(Equipment.SLOT_HANDS).getDefinition().getEquipId());
            } else {
                appearanceData.writeShort(0x100 + p.getAppearance().getLook()[4]);
            }
            if (p.getEquipment().get(Equipment.SLOT_FEET) != null) {
                appearanceData.writeShort(32768 + p.getEquipment().get(Equipment.SLOT_FEET).getDefinition().getEquipId());
            } else {
                appearanceData.writeShort(0x100 + p.getAppearance().getLook()[6]);
            }
            if (hat != null) {
                if (!Equipment.isFullMask(hat.getDefinition())) {
                    appearanceData.writeShort(0x100 + p.getAppearance().getLook()[1]);
                } else {
                    appearanceData.writeByte((byte) 0);
                }
            } else {
                appearanceData.writeShort(0x100 + p.getAppearance().getLook()[1]);
            }
        } else {
            appearanceData.writeShort(-1);
            appearanceData.writeShort(def == null ? p.getAppearance().getNpcType() :def.getId());
            appearanceData.writeByte(0); //lol wtf is this
        }
        for (int j = 0; j < 5; j++) {
            appearanceData.writeByte(p.getAppearance().getColour()[j]);
        }
        // TODO Check if this shit work yo
        appearanceData.writeShort(p.getRenderAnimation() > -1 ? p.getRenderAnimation() : p.getAppearance().getNpcType() != -1 && def != null ? def.getCacheDefinition().renderEmote : p.getEquipment().getRenderAnim());
        appearanceData.writeRS2String(Misc.formatPlayerNameForDisplay(p.getUsername()));
        appearanceData.writeByte(p.getSkills().getCombatLevel());
        appearanceData.writeShort(0); // >_>
        appearanceData.writeByte(0); // <_<
        updateMessage.writeByteA(appearanceData.position());
        updateMessage.writeBytes(appearanceData.getBuffer());
    }

}