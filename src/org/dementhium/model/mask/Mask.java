package org.dementhium.model.mask;

import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.definition.NPCDefinition;
import org.dementhium.model.player.Player;

public class Mask {

    private Mob mob;

    private ChatMessage lastChatMessage;
    private Graphic lastGraphics;
    private Animation lastAnimation;
    private Heal lastHeal;
    private Location facePosition;

    private Mob interactingEntity;

    private ForceText forceText;

    //These need to be flagged since they can be reset
    private boolean forceTextUpdate, appearanceUpdate, teleport, faceEntityUpdate, forceMovementUpdate;

    private int switchId = -1;

    public Mask(Mob mob) {
        this.mob = mob;
        this.setApperanceUpdate(true);
    }

    public void reset() {
        switchId = -1;
        lastChatMessage = null;
        lastGraphics = null;
        lastAnimation = null;
        lastHeal = null;
        appearanceUpdate = false;
        faceEntityUpdate = false;
        forceText = null;
        forceMovementUpdate = false;
        forceTextUpdate = false;
        setTeleport(false);
        facePosition = null;
    }

    public Mob getPlayer() {
        return mob;
    }

    public boolean requiresUpdate() {
        if (mob.getDamageManager().getHits().size() > 0 || facePosition != null || forceMovementUpdate || forceTextUpdate || switchId > -1 || forceText != null || appearanceUpdate || faceEntityUpdate || lastChatMessage != null || lastGraphics != null || lastAnimation != null || lastHeal != null) {
            return true;
        }
        if (mob.isNPC()) {
            return mob.getNPC().testingmask;
        }
        if (mob.isPlayer()) {
            Player player = mob.getPlayer();
            return player.getWalkingQueue().getWalkDir() != -1 || player.getWalkingQueue().getRunDir() != -1 || player.getWalkingQueue().isDidTele();
        }
        return false;
    }

    public void setLastChatMessage(ChatMessage lastChatMessage) {
        this.lastChatMessage = lastChatMessage;
    }

    public ChatMessage getLastChatMessage() {
        return lastChatMessage;
    }

    public void setLastGraphics(Graphic lastGraphics) {
        this.lastGraphics = lastGraphics;
    }

    public Graphic getLastGraphics() {
        return lastGraphics;
    }

    public void setLastAnimation(Animation lastAnimation) {
    //	System.out.println(lastAnimation.getId() + ", " + mob.canAnimate());
    //	new Throwable().printStackTrace(System.out);
        if (mob.canAnimate()) {
            this.lastAnimation = lastAnimation;
        }
    }

    public void setLastAnimation(Animation lastAnimation, boolean ignoreFlag) {
        if (!mob.canAnimate() && !ignoreFlag) {
            return;
        }
        this.lastAnimation = lastAnimation;
    }

    public Animation getLastAnimation() {
        return lastAnimation;
    }

    public void setLastHeal(Heal lastHeal) {
        this.lastHeal = lastHeal;
    }

    public Heal getLastHeal() {
        return lastHeal;
    }

    public void setLastForceText(ForceText text) {
        setForceText(text);
    }

    public boolean isForceTextUpdate() {
        return forceTextUpdate;
    }

    public void setForceText(ForceText forceText) {
        this.forceText = forceText;
        forceTextUpdate = true;
    }

    public void setForceTextUpdate(boolean forceText) {
        this.forceTextUpdate = forceText;
    }

    public ForceText getForceText() {
        return forceText;
    }

    public void setTeleport(boolean teleport) {
        this.teleport = teleport;
    }

    public boolean isTeleport() {
        return teleport;
    }

    public void setApperanceUpdate(boolean appearanceUpdate) {
        this.appearanceUpdate = appearanceUpdate;
    }

    public boolean isApperanceUpdate() {
        return appearanceUpdate;
    }

    public void setInteractingEntity(Mob interactingEntity) {
        this.interactingEntity = interactingEntity;
        this.faceEntityUpdate = true;
    }

    public Mob getInteractingEntity() {
        return interactingEntity;
    }

    public boolean isFaceEntityUpdate() {
        return faceEntityUpdate;
    }

    public void setFacePosition(Location facePosition, int sizeX, int sizeY) {
        if (sizeX <= 1 && sizeY <= 1) {
            this.facePosition = facePosition;
        } else {
            int faceX = facePosition.getX() + Math.round(sizeX / 2);
            int faceY = facePosition.getY() + Math.round(sizeY / 2);
            //			int objectCalcX = (-48 + 8 * facePosition.getRegionX());
            //            int objectCalcY = (-48 + 8 * facePosition.getRegionY());
            //            int offsetY = ((faceY + sizeY) - (objectCalcY + objectCalcY) / 64) + ((objectCalcY + objectCalcY) / 64);
            //            int offsetX = ((faceX + sizeX) - (objectCalcX + objectCalcX) / 64) + ((objectCalcX + objectCalcX) / 64);
            //            if (sizeX < sizeY)
            //                offsetX += 1;
            //            else if (sizeX > sizeY)
            //                offsetX -= 1;
            //            faceY = offsetY;
            //            faceX = offsetX;
            this.facePosition = Location.locate(faceX, faceY, 0);
        }
    }

    public Location getFacePosition() {
        return facePosition;
    }

    public void setSwitchId(int switchId) {
        this.switchId = switchId;
        mob.getNPC().setId(switchId);
        mob.getNPC().setDefinition(NPCDefinition.forId(switchId));
    }

    public int getSwitchId() {
        return switchId;
    }

    public void setForceMovementUpdate(boolean forceMovementUpdate) {
        this.forceMovementUpdate = forceMovementUpdate;
    }

    public boolean isForceMovementUpdate() {
        return forceMovementUpdate;
    }

}
