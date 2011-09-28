package org.dementhium.content.cutscenes.impl;

import org.dementhium.content.cutscenes.Cutscene;
import org.dementhium.content.cutscenes.CutsceneAction;
import org.dementhium.content.cutscenes.actions.*;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * @author Steve <golden_32@live.com>
 */
public class TestScene {

    public TestScene(Player p) {
        Cutscene scene = new Cutscene(p, constructActions(p));
        scene.start();
    }

    private CutsceneAction[] constructActions(final Player p) {
        final NPC evilGuy = new NPC(2909);
        World.getWorld().getNpcs().add(evilGuy);
        ActionSender.updateMinimap(p, ActionSender.BLACKOUT_ORB_AND_MAP);
        CutsceneAction[] actions = new CutsceneAction[29];
        actions[0] = new InterfaceAction(p, 4, 115);
        actions[1] = new TeleportAction(p, 5, 2379, 4679, 0, 0, false) {
            @Override
            public void execute(Player[] players) {
                super.execute(players);
                p.getMask().setFacePosition(Location.locate(2378, 4679, 0), 0, 0);
            }
        };
        actions[2] = new AnimationAction(p, 1, Animation.create(618));
        actions[3] = new CameraMoveAction(p, 1, 2372, 4700, 254, 6);
        actions[4] = new CameraRotateAction(p, 1, 2379, 4679, 254, 70, 6);
        actions[5] = new InterfaceAction(p, 3, 170);
        actions[6] = new InterfaceAction(p, 1, -1);
        actions[7] = new CameraMoveAction(p, 1, 2373, 4679, 6, 6);
        actions[8] = new CameraRotateAction(p, 5, 2379, 4679, 1, 85, 6);
        actions[9] = new MessageAction(p, 5, "Oh man, what a great day to be fishing!");
        actions[10] = new MessageAction(p, 5, "After all this fishing, my arms have gotten huge!");
        actions[11] = new MessageAction(p, 5, "Let's see how big they've gotten, just need to make sure no ones around.");
        actions[12] = new AnimationAction(p, 5, Animation.create(9063));
        actions[13] = new MessageAction(p, 6, "Alright we're good, let's see how big these babies are!");
        actions[14] = new AnimationAction(p, 5, Animation.create(8996));
        actions[15] = new MessageAction(p, 4, "Golly, these muscles are HUGEEE-");
        actions[16] = new MessageAction(p, 5, "Wait what was that?! Oh no, he's back already!");
        actions[17] = new MessageAction(p, 3, "I gotta hide quick!");
        actions[18] = new MovingRotationCamera(p, 1, new CameraMoveAction(p, 0, 2387, 4676, 254, 7), new CameraRotateAction(p, 0, 2382, 4680, 254, 55, 8));
        actions[19] = new TeleportAction(evilGuy, 1, 2383, 4681, 0, 0, false);
        actions[20] = new FaceToAction(evilGuy, 1, p);
        actions[21] = new MessageAction(evilGuy, 2, "What are you doing in my EVIL cave?!");
        actions[22] = new FaceToAction(p, 1, evilGuy);
        actions[23] = new MessageAction(p, 3, "Uhh, sorry Sir, I was just fishing!");
        actions[24] = new MessageAction(evilGuy, 2, "In my EVIL cave?!");
        actions[25] = new MessageAction(p, 3, "Sorry... I just wasn't thinking!");
        actions[26] = new MessageAction(evilGuy, 5, "This is unexcusable! You must PAY!") {
            @Override
            public void execute(Player[] players) {
                super.execute(players);
                evilGuy.animate(6128);
                evilGuy.graphics(247, 100 << 16);
                ProjectileManager.sendDelayedProjectile(evilGuy, p, 395, false);
                World.getWorld().submit(new Tick(2) {
                    @Override
                    public void execute() {
                        p.graphics(287, 100 << 16);
                        p.getDamageManager().damage(evilGuy, p.getHitPoints(), p.getHitPoints(), DamageType.MAGE);
                        this.stop();
                    }

                });
            }
        };
        actions[27] = new MessageAction(p, 2, "Noooooooooooooo!!");
        actions[28] = new MessageAction(evilGuy, 4, "Muahauahauahauhauha!!") {
            @Override
            public void execute(Player[] players) {
                super.execute(players);
                World.getWorld().getNpcs().remove(evilGuy);
                ActionSender.updateMinimap(p, ActionSender.NO_BLACKOUT);
            }
        };
        return actions;
    }

}
