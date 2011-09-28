package org.dementhium.content.activity.impl;

import org.dementhium.content.DialogueManager;
import org.dementhium.content.activity.Activity;
import org.dementhium.model.Item;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 * @author Steve <golden_32@live.com>
 */
/*
 * info: GFX FOR CATAPULT: 679, 
 * Animation: 4170+
 */
public abstract class WarriorsGuildMinigame extends Activity<Player> {

    protected Player player;
    private boolean canExecute = false;


    public WarriorsGuildMinigame(Player p) {
        super(p); //getPlayer() calls the player in the super class.
        this.player = p;
    }

    public boolean canExecute() {
        return canExecute;
    }

    public void setCanExecute(boolean b) {
        this.canExecute = b;
    }

    @Override
    public boolean initializeActivity() {
        if (getPlayer() == null) {
            this.stop();
            return false;
        } else if (canExecute()) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean handleDialogue(Player player, int stage) {
        switch (stage) {
            case 256:
                DialogueManager.sendDialogue(player, DialogueManager.CONFUSED, -1, -1, "Umm...thank you, I think.");
                return true;
            case 257:
                DialogueManager.sendOptionDialogue(player, new int[]{258, 310, 317}, "Quite a place you've got here. Tell me more about it", "You any good with a sword?", "Bye!");
                return true;
            case 258://"Quite a place you've got here. Tell me more about it"
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 259, "Quite a place you've got here. Tell me more about it.");
                return true;
            case 259:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 260, "Yes indeed. What would you like to know?");
                return true;
            case 260:
                DialogueManager.sendOptionDialogue(player, new int[]{261, 267, 275, 289, 298}, "Tell me about the Strength training area.", "Tell me about the Attack training area.", "Tell me about the Defence training area.", "Tell me about the Combat training area.", "Tell me about tokens.");
                return true;
            case 261:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 262, "Tell me about the Strength training area.");
                return true;
            case 262:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 263, "Ahh, the mighty warrior, Sloane, guards the Strength",
                        "training area. This intriguing little area consists of two",
                        "shotput lanes for different weights of shot. It's fairly",
                        "simple, the referee or Sloane can explain more. There's");
                return true;
            case 263:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 264, "also the store room next door where Jimmy might share",
                        "his talents with you, but don't tell him that I know he's",
                        "not on guard duty!");
                return true;
            case 264:
                DialogueManager.sendDialogue(player, DialogueManager.CONFUSED, -1, 265, "Oh? Why?");
                return true;
            case 265:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 266, "Well, he's doing no harm and let's face it, with all these",
                        "warriors around, the guild is hardly unguarded You",
                        "can find the strength area just up the stairs behind the bank.");
                return true;
            case 266:
                DialogueManager.sendOptionDialogue(player, new int[]{267, 275, 289, 298, 317}, "Tell me about the Attack training area.", "Tell me about the Defence training area.", "Tell me about the Combat training area.", "Tell me about tokens.", "Bye!");
                return true;
            case 267:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 268, "Tell me about the Attack training area.");
                return true;
            case 268:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 269, "Ahhh, dummies.");
                return true;
            case 269:
                DialogueManager.sendDialogue(player, DialogueManager.SAD, -1, 270, "I'm no dummy, I just want to know what is there!");
                return true;
            case 270:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 271, "Oh no, my dear man, I did not mean you at all! The",
                        "training area has mechanical dummies that pop up out",
                        "of holes in the floor. The noble dwarf, Gamfred,",
                        "invented the mechanism and Ajjat can explain more");
                return true;
            case 271:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 272, "about what to do there.");
                return true;
            case 272:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 273, "Oh, okay, I'll have to try it out.");
                return true;
            case 273:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 274, "You can find it just down the corridor and on your", "right.");
                return true;
            case 274:
                DialogueManager.sendOptionDialogue(player, new int[]{261, 275, 289, 298, 317}, "Tell me about the Strength training area.", "Tell me about the Defence training area.", "Tell me about the Combat training area.", "Tell me about tokens.", "Bye!");
                return true;
            case 275:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 276, "Tell me about the Defence training area.");
                return true;
            case 276:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 277, "To polish your defensive skills to the very highest level,",
                        "we've employed a most inventive dwarf and a catapult.");
                return true;
            case 277:
                DialogueManager.sendDialogue(player, DialogueManager.SCARED, -1, 278, "You're going to throw dwarves at me?");
                return true;
            case 278:
                DialogueManager.sendDialogue(player, DialogueManager.LAUGH_EXCITED, 4286, 279, "Oh my, no! I think Gamfred would object to that most", "strnogly.");
                return true;
            case 279:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 280, "He's an inventor, you see, and has built a marvellous",
                        "contraption that can throw all sorts of things at you.",
                        "Things such as magic missiles...");
                return true;
            case 280:
                DialogueManager.sendDialogue(player, DialogueManager.CALM_TALK, -1, 281, "Mmmm?");
                return true;
            case 281:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 282, "...spiked iron balls...");
                return true;
            case 282:
                DialogueManager.sendDialogue(player, DialogueManager.CALM_TALK, -1, 283, "Er...");
                return true;
            case 283:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 284, "...spinning, slashing blades...");
                return true;
            case 284:
                DialogueManager.sendDialogue(player, DialogueManager.WORRIED, -1, 285, "Ummm...");
                return true;
            case 285:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 286, "...and anvils.");
                return true;
            case 286:
                DialogueManager.sendDialogue(player, DialogueManager.WORRIED, -1, 287, "ANVILS?");
                return true;
            case 287:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 288, "No need to be afraid, it's all under very controlled",
                        "conditions! You can find it just up the stairs behind the", "bank.");
                return true;
            case 288:
                DialogueManager.sendOptionDialogue(player, new int[]{261, 267, 289, 298, 317}, "Tell me about the Strength training area.", "Tell me about the Attack training area.", "Tell me about the Combat training area.", "Tell me about tokens.", "Bye!");
                return true;
            case 289:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 290, "Tell me about the Combat training area.");
                return true;
            case 290:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 291, "Ahh, yes, our resident magician from foreign lands",
                        "created a most amazing gadget that can turn your own",
                        "armour against you! It's really quite intriguing.");
                return true;
            case 291:
                DialogueManager.sendDialogue(player, DialogueManager.CONFUSED, -1, 292, "That sounds dangerous. What if I'm wearing it at the", "time?");
                return true;
            case 292:
                DialogueManager.sendDialogue(player, DialogueManager.LAUGH_EXCITED, 4286, 293, "So far, that's not happened. You need to speak to",
                        "Shanomi about the specifics of the process, but as I",
                        "understand it, putting a suit of armour in one of these",
                        "devices will make it come to life somehow. The better");
                return true;
            case 293:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 294, "the armour, the harder it is to defeat.");
                return true;
            case 294:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 295, "Fighting my own armour sounds weird. I could be", "killed by it...");
                return true;
            case 295:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 296, "Indeed, we have had a few fatalities from warriors",
                        "overstretching themselves and not knowing their limits.",
                        "Start small and work up, is my motto! That and go see",
                        "Lidio for some food if you need it.");
                return true;
            case 296:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 297, "Okay, thanks for the warning.");
                return true;
            case 297:
                DialogueManager.sendOptionDialogue(player, new int[]{261, 267, 275, 298, 317}, "Tell me about the Strength training area.", "Tell me about the Attack training area.", "Tell me about the Defence training area.", "Tell me about tokens.", "Bye!");
                return true;
            case 298:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 299, "Tell me about tokens.");
                return true;
            case 299:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 300, "Ahh, yes! The tokens allow you to spend an amount of",
                        "time with my 'discovery', located on the top floor of the",
                        "guild. Now, the amount of tokens you collect from the",
                        "five activities around the guild will dictate how long");
                return true;
            case 300:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 301, "Kamfreena will allow you in the enclosure on the very",
                        "top floor. More tokens equals more time. There are also",
                        "some bonuses available should you take part in all of the",
                        "activities around the guild.");
                return true;
            case 301:
                DialogueManager.sendDialogue(player, DialogueManager.CONFUSED, -1, 302, "Okay, okay. So, how do i earn these tokens?");
                return true;
            case 302:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 303, "You can earn them by simply using the training", "exercises around the guild. The staff will enter your", "token earnings into a ledger as you play.");
                return true;
            case 303:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 304, "Sounds easy enough. What are the bonuses?");
                return true;
            case 304:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 305, "Should you take part in all five activities around the",
                        "guild you can choose to pay for your time on the top",
                        "floor with tokens of all types. Should you do this then",
                        "you'll find you spend  less tokens overall and have a");
                return true;
            case 305:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 306, "better chance of getting the dragon defender, amongst", "other things.");
                return true;
            case 306:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 307, "Excellent, sounds good. So, what's up on the top floor?");
                return true;
            case 307:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 308, "Well, without giving too much away, they're big and", "mean, and you get to fight them for defenders. If",
                        "you're really lucky they'll summon a", "cyclossus...although that might be unlucky. Still, if you");
                return true;
            case 308:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 309, "manage to defeat him, you could win his hat.");
                return true;
            case 309:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 260, "Interesting...I will have to explore the top floor then!");
                return true;
            case 310:
                DialogueManager.sendDialogue(player, DialogueManager.CONFUSED, -1, 311, "You any good with a sword?");
                return true;
            case 311:
                DialogueManager.sendDialogue(player, DialogueManager.WHAT_THE_CRAP, 4286, 312, "'Am I any good with a sword'? Have you any clue who", "I am?");
                return true;
            case 312:
                DialogueManager.sendDialogue(player, DialogueManager.CALM_TALK, -1, 313, "Not really, no.");
                return true;
            case 313:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 314, "Why, I could best any person alive in a rapier duel!");
                return true;
            case 314:
                DialogueManager.sendDialogue(player, DialogueManager.CONFUSED, -1, 315, "Try me, then!");
                return true;
            case 315:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 316, "My dear man, I couldn't possibly duel you, I might",
                        "hurt you and then what would happen to my",
                        "reputation! Besides I have this wonderful guild to run.",
                        "Why don't you take a look at the various activites we");
                return true;
            case 316:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, 260, "have. You might even collect enough tokens to be", "allowed in the kill the strange beasts from the east!");
                return true;
            case 317:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 318, "Bye!");
                return true;
            case 318:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4286, -1, "Farewell, brave warrior, I do hope you enjoy my guild.");
                return true;
            case 420:
                DialogueManager.sendOptionDialogue(player, new int[]{421, 423, 441, 443}, "That's not a catapult, it's a large crossbow.", "Yes, beautiful piece of engineering.", "No, where is it?", "Bye!");
                return true;
            case 421:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 422, "That's not a catapult, it's a large crossbow.");
                return true;
            case 422:
                DialogueManager.sendDialogue(player, DialogueManager.MEAN_FACE, 4287, -1, "WHAT!? I'll have you know that is the finest piece of", "dwarven engineering for miles around! How DARE", "you insult my work!");
                return true;
            case 423:
                //"Yes, beautiful piece of engineering."
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 424, "Yes, beautiful piece of engineering.");
                return true;
            case 424:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4287, 425, "Nice to meet someone who appreciates fine work, have", "you tried it out yet?");
                return true;
            case 425:
                DialogueManager.sendOptionDialogue(player, new int[]{426, 438}, "Yes.", "No, how do I do that?");
                return true;
            case 426:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 427, "Yes.");
                return true;
            case 427:
                DialogueManager.sendDialogue(player, DialogueManager.CONFUSED, 4287, 428, "What did you think?");
                return true;
            case 428:
                DialogueManager.sendOptionDialogue(player, new int[]{429, 431, 433, 435}, "It was ok I guess.", "It was fun!", "I didn't like it.", "May I have a shield please?");
                return true;
            case 429:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 430, "It was ok I guess.");
                return true;
            case 430:
                DialogueManager.sendDialogue(player, DialogueManager.SAD, 4287, -1, "Well I guess not everyone will like it.");
                return true;
            case 431:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 432, "It was fun!");
                return true;
            case 432:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4287, -1, "Glad ta hear it. Try it again sometime. We have more", "tests to run.");
                return true;
            case 433:
                DialogueManager.sendDialogue(player, DialogueManager.SAD, -1, 434, "I didn't like it.");
                return true;
            case 434:
                DialogueManager.sendDialogue(player, DialogueManager.SAD, 4287, -1, "Well I guess not everyone will like it. But give it", "another chance before you go.");
                return true; //May I have a shield please?
            case 435:
                DialogueManager.sendDialogue(player, DialogueManager.CONFUSED, -1, 436, "May I have a shield please?");
                return true;
            case 436:
                if (player.getInventory().contains(8856) || player.getBank().contains(8856) || player.getEquipment().contains(8856)) {
                    DialogueManager.sendDialogue(player, DialogueManager.LAUGH_EXCITED, 4287, -1, "Silly muffin, you have one already!");
                } else {
                    DialogueManager.sendDialogue(player, DialogueManager.HAPPY_TALKING, 4287, 437, "Of course!");
                }
                return true;
            case 437:
                ActionSender.sendChatboxInterface(player, 511);
                ActionSender.sendItemOnInterface(player, 511, 0, -1, 8856);
                ActionSender.sendString(player, 511, 1, "The dwarf hands you a large shield.");
                player.getInventory().addDropable(new Item(8856));
                return true;
            case 438:
                DialogueManager.sendDialogue(player, DialogueManager.CONFUSED, -1, 439, "No, how do I do that?");
                return true;
            case 439:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4287, 440, "Well it's simple! First you need to get a shield from me,", "and stand on that target over there. Then, the catapult", "will start hurling missles at you which you need to dodge.", "You can look at the scroll on the wall over there");
                return true;
            case 440:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4287, -1, "for any dodging tips. Anyway, goodluck warrior!");
                return true;
            case 441:
                DialogueManager.sendDialogue(player, DialogueManager.CONFUSED, -1, 442, "No, where is it?");
                return true;
            case 442:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4287, -1, "Well it's right through that door behind me!", "Be sure to test it out, we still need more test subjects.");
                return true;
            case 443:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, -1, 444, "Bye!");
                return true;
            case 444:
                DialogueManager.sendDialogue(player, DialogueManager.TALKING_ALOT, 4287, -1, "Farewell, I hope you enjoy the guild!");
                return true;
        }
        return false;
    }


}
