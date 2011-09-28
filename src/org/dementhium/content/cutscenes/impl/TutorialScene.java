package org.dementhium.content.cutscenes.impl;

import org.dementhium.content.DialogueManager;
import org.dementhium.content.cutscenes.Cutscene;
import org.dementhium.content.cutscenes.CutsceneAction;
import org.dementhium.content.cutscenes.actions.*;
import org.dementhium.model.World;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;

import java.util.ArrayList;

/**
 * @author 'Lumby <lumbyjr@hotmail.com>
 */
public class TutorialScene {

    private Cutscene scene;

    public TutorialScene(Player p) {
        scene = new Cutscene(p, constructActions(p));
    }

    /*
      * tips for making scenes, if you're going to have camera movement and dialogue at the same time,
      * ALWAYS do the camera first, because the next action will not advance until you click the continue button
      * on a dialogue action.
      */
    private CutsceneAction[] constructActions(final Player p) {
        ArrayList<CutsceneAction> actions = new ArrayList<CutsceneAction>();
        actions.add(new AnimationAction(p, 0, Animation.create(4367)));
        actions.add(new InterfaceAction(p, 0, 177));
        actions.add(new InterfaceAction(p, 0, -1));
        actions.add(new MovingRotationCamera(p, 0, new CameraMoveAction(p, 0, p.getLocation().getX()+2, p.getLocation().getY(), 50, 1), new CameraRotateAction(p, 0, p.getLocation().getX()-1, p.getLocation().getY(), 2, 85, 5)));
        actions.add(new DialogueAction(p, 6537, DialogueManager.HAPPY_TALKING, false, "Welcome to EpicScape my son!"));
        actions.add(new FaceToAction(p, 2, World.getWorld().getNpcs().getById(6537)));
        actions.add(new DialogueAction(p, -1, DialogueManager.CONFUSED, false, "Ouch.. Who are you?"));
        actions.add(new DialogueAction(p, 6537, DialogueManager.HAPPY_TALKING, false, "My name is Mandrith."));
        actions.add(new DialogueAction(p, -1, DialogueManager.CONFUSED, false, "Why hello Mandrith. But why drop me here?"));
        actions.add(new DialogueAction(p, 6537, DialogueManager.HAPPY_TALKING, false, "Because you've requested me to!"));
        actions.add(new DialogueAction(p, -1, DialogueManager.CONFUSED, false, "Joining this server means drop on the floor?"));
        actions.add(new DialogueAction(p, 6537, DialogueManager.HAPPY_TALKING, false, "To me - a strong warrior - it does!"));
        /*DialogueAction[] links = new DialogueAction[]{new DialogueAction(p, -1, DialogueManager.TALKING_ALOT, true, "No thanks") {
            @Override
            public void execute(Player[] players) {
                super.execute(players);
                p.removeTick("cutscene");
            }
        }, new DialogueAction(p, -1, DialogueManager.HAPPY_TALKING, false, "Yes please!")};
        actions.add(new DialogueOptionAction(p, links, "Yes please!", "No thanks"));
        for (DialogueAction act : links) {
            actions.add(act);
        }*/
        //actions.add(new MovingRotationCamera(p, 0, new CameraMoveAction(p, 0, 3223, 3431, 254, 2), new CameraRotateAction(p, 0, 3229, 3436, 2, 90, 2)));
        actions.add(new DialogueAction(p, 6537, DialogueManager.HAPPY_TALKING, false, "Let me show you some places in Edgeville."));
        actions.add(new MovingRotationCamera(p, 0, new CameraMoveAction(p, 0, 3091, 3489, 70, 2), new CameraRotateAction(p, 0, 3094, 3493, 20, 50, 2)));
        actions.add(new DialogueAction(p, 6537, DialogueManager.HAPPY_TALKING, false, "This is the Edgeville bank. Over here, you can store", "your valuable goods so you won't lose", "them during a battle."));
        actions.add(new MovingRotationCamera(p, 0, new CameraMoveAction(p, 0, 3103, 3498, 70, 1), new CameraRotateAction(p, 0, 3108, 3499, 20, 90, 2)));
        actions.add(new DialogueAction(p, 6537, DialogueManager.HAPPY_TALKING, false, "Over here is a furnace. You can smelt", "and smith your ores to weapons for fighting."));
        actions.add(new MovingRotationCamera(p, 0, new CameraMoveAction(p, 0, 3077, 3508, 70, 2), new CameraRotateAction(p, 0, 3081, 3511, 20, 50, 2)));
        actions.add(new DialogueAction(p, 6537, DialogueManager.HAPPY_TALKING, false, "To the north, you will find the store. Great ", "warriors will sell their goods here for others", "to buy. So can you!"));
        actions.add(new DialogueAction(p, -1, DialogueManager.CONFUSED, false, "But eh... Are there commands?"));
        actions.add(new DialogueAction(p, 6537, DialogueManager.HAPPY_TALKING, false, "All of our commands can be found in the command book", "that you will be given at the end of this tutorial."));
        actions.add(new DialogueAction(p, -1, DialogueManager.HAPPY_TALKING, false, "So how do I get started fighting?"));
        actions.add(new DialogueAction(p, 6537, DialogueManager.HAPPY_TALKING, false, "We have various places to train. For novice", "players, you're adviced to train on Goblins.","When you're stronger, go train at Rock Crabs."));
        actions.add(new DialogueAction(p, -1, DialogueManager.THINKING, false, "What if I have any more questions?"));
        actions.add(new DialogueAction(p, 6537, DialogueManager.HAPPY_TALKING, false, "In that case, feel free to go to our forums ", "located at www.epicscape.org."));
        actions.add(new DialogueAction(p, 6537, DialogueManager.HAPPY_TALKING, true, "I think I told you enough now, go ahead","and enjoy this beautiful world! There's alot "," to do for you, fellow adventurer. ","I wish you luck in the world of EpicScape!"));
        return actions.toArray(new CutsceneAction[0]);
    }

    public void start() {
        scene.start();
    }
    
    
    
    
    /**
     * package org.dementhium.content.cutscenes.impl;

import org.dementhium.content.DialogueManager;
import org.dementhium.content.cutscenes.Cutscene;
import org.dementhium.content.cutscenes.CutsceneAction;
import org.dementhium.content.cutscenes.actions.*;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;

import java.util.ArrayList;

public class TutorialScene {

    private Cutscene scene;

    public TutorialScene(Player p) {
        scene = new Cutscene(p, constructActions(p));
    }


    private CutsceneAction[] constructActions(final Player p) {
        ArrayList<CutsceneAction> actions = new ArrayList<CutsceneAction>();
        actions.add(new AnimationAction(p, 0, Animation.create(4367)));
        actions.add(new InterfaceAction(p, 0, 177));
        actions.add(new InterfaceAction(p, 0, -1));
        actions.add(new DialogueAction(p, -1, DialogueManager.CONFUSED, false, "Wha.. What the heck? Where am I?!"));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "Well hello there!"));
        actions.add(new DialogueAction(p, -1, DialogueManager.CONFUSED, false, "Who.. Who's there?"));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "Allow me to introduce myself, my name is Roddeck."));
        actions.add(new DialogueAction(p, -1, DialogueManager.CONFUSED, false, "Erm, ok. But can you tell me where I am?"));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "Dementhium of course!"));
        actions.add(new DialogueAction(p, -1, DialogueManager.CONFUSED, false, "Ok so... What can I do here?"));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "Just about EVERYTHING ha!", "","Here let me show you around!"));
        /*DialogueAction[] links = new DialogueAction[]{new DialogueAction(p, -1, DialogueManager.TALKING_ALOT, true, "No thanks") {
            @Override
            public void execute(Player[] players) {
                super.execute(players);
                p.removeTick("cutscene");
            }
        }, new DialogueAction(p, -1, DialogueManager.HAPPY_TALKING, false, "Yes please!")};
        actions.add(new DialogueOptionAction(p, links, "Yes please!", "No thanks"));
        for (DialogueAction act : links) {
            actions.add(act);
        }
        actions.add(new MovingRotationCamera(p, 0, new CameraMoveAction(p, 0, 2387, 4464, 254, 6), new CameraRotateAction(p, 0, 2387, 4473, 2, 90, 2)));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "North of the bank you will find Aubury. He is the man", "to go to for all of your magic runes."));
        actions.add(new MovingRotationCamera(p, 0, new CameraMoveAction(p, 0, 2387, 4464, 254, 6), new CameraRotateAction(p, 0, 2380, 4468, 2, 90, 2)));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "Right next door to Aubury you will find", "Bob. Here you can buy your basic woodcutting hatchets", "and mining pickaxes."));
        actions.add(new MovingRotationCamera(p, 0, new CameraMoveAction(p, 0, 2390, 4449, 254, 6), new CameraRotateAction(p, 0, 2379, 4461, 2, 90, 2)));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "Over here you will find our main bank. You can store", "all of your items here for safe keeping."));
        actions.add(new MovingRotationCamera(p, 0, new CameraMoveAction(p, 0, 2390, 4449, 254, 6), new CameraRotateAction(p, 0, 2376, 4448, 2, 50, 2)));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "Right next door to our main bank you will find the", "general store. Here you can sell all of your wares for coins."));
        actions.add(new MovingRotationCamera(p, 0, new CameraMoveAction(p, 0, 2390, 4449, 254, 6), new CameraRotateAction(p, 0, 2382, 4440, 2, 30, 2)));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "Moving along, you can cook your meals at the range", "located in this building next to the general store. I hear that","the Fairy Chef is cooking up some good meals these days."));
        actions.add(new MovingRotationCamera(p, 0, new CameraMoveAction(p, 0, 2408, 4468, 254, 6), new CameraRotateAction(p, 0, 2401, 4470, 2, 50, 2)));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "To the North-East you can use the furnace to smelt ores", "into bars, which later can be turned into armour."));
        actions.add(new MovingRotationCamera(p, 0, new CameraMoveAction(p, 0, 2408, 4468, 254, 6), new CameraRotateAction(p, 0, 2416, 4469, 4, 50, 2)));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "In the field across from the furnace, you will find fungi.", "Be careful when you pick them as you may not find what","you are expecting to see! You can also practice your","woodcutting skills here."));
        actions.add(new MovingRotationCamera(p, 0, new CameraMoveAction(p, 0, 2412, 4439, 254, 6), new CameraRotateAction(p, 0, 2412, 4435, 2, 50, 2)));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "Fairy rings are the main method of transportation around", "the world of Dementhium."));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "Please read the gray book in your inventory for all of", "the avaliable fairy ring codes."));
        actions.add(new DialogueAction(p, -1, DialogueManager.HAPPY_TALKING, false, "So wait... where are the codes?"));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "All of the codes can be found in the gray guide book", "that you will be given at the end of this tutorial."));
        actions.add(new MovingRotationCamera(p, 0, new CameraMoveAction(p, 0, 2390, 4444, 254, 6), new CameraRotateAction(p, 0, 2390, 4449, 2, 90, 2)));
        actions.add(new DialogueAction(p, -1, DialogueManager.HAPPY_TALKING, false, "How do I train my combat skills?"));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "There are various dungeons around Dementhium that you", "can use inorder to train combat. You can access them by","fairy ring, or their normal Runescape way."));
        actions.add(new DialogueAction(p, -1, DialogueManager.HAPPY_TALKING, false, "Is there a website I can learn more from?"));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "You can visit the Guide section of the Forums or", "you can visit http://dementhium.wikia.com"));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, false, "Before I set you off on your journey, please take a look in", "your inventory for a blue rule book. It is recommended that","you read this book carefully and understand what not","to do while playing Dementhium."));
        actions.add(new DialogueAction(p, 8863, DialogueManager.HAPPY_TALKING, true, "There simply isn't enough time to show you everything","Dementhium has to offer. After we finish our tour"," you will see that you have been given some starter items. ","I wish you luck in the world of Dementhium!"));
        return actions.toArray(new CutsceneAction[0]);
    }

    public void start() {
        scene.start();
    }
}

     */
}
