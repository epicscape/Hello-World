package org.dementhium.content.cutscenes.actions;

import org.dementhium.content.cutscenes.CutsceneAction;
import org.dementhium.model.Location;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 * @author Steve
 */
public class CameraRotateAction extends CutsceneAction {

    private final int x, y, speed;
    private int angle;

    public CameraRotateAction(Player p, int delay, int x, int y, int speed, int angle, int height) {
        super(p, delay);
        this.x = x;
        this.y = y;
        this.speed = speed;
        this.angle = angle;
    }

    @Override
    public void execute(Player[] players) {
        if (players == null)
            ActionSender.rotateCamera(getMob().getPlayer(), Location.locate(x, y, getMob().getPlayer().getLocation().getZ()).getViewportX(getMob().getPlayer().getLocation(), getMob().getViewportDepth()), Location.locate(x, y, getMob().getPlayer().getLocation().getZ()).getViewportY(getMob().getPlayer().getLocation(), getMob().getViewportDepth()), speed, angle);
        else {
            for (Player p : players) {
                ActionSender.rotateCamera(p, Location.locate(x, y, p.getLocation().getZ()).getViewportX(p.getLocation(), p.getViewportDepth()), Location.locate(x, y, p.getLocation().getZ()).getViewportY(p.getLocation(), p.getViewportDepth()), speed, angle);
            }
        }
    }

}
