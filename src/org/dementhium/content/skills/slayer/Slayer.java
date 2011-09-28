package org.dementhium.content.skills.slayer;

import org.dementhium.content.skills.slayer.SlayerTask.Master;
import org.dementhium.model.World;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;

import static org.dementhium.content.DialogueManager.*;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class Slayer {

    //Great, you're doing great. Your new task is to kill
    //60 ghouls.

    /**
     * Tips
     * Ghouls - Ghouls aren't undead, but they are stronger
     * and tougher then they look. They're also very cowardly and
     * will run if they're losing a fight. They wait to ambush
     * those entering Morytania, close to the River Salve.
     */

    private Player player;
    private SlayerTask task;

    public Slayer(Player player) {
        this.player = player;
    }


    public boolean handleDialouge(int stage) {
        if (!(stage >= 2000 && stage <= 3000)) {
            return false;
        }
        NPC talkingTo = player.getSettings().getSpeakingTo() == null ? null : player.getSettings().getSpeakingTo().getNPC();
        Master master = player.getSettings().getSpeakingTo() == null ? null : Master.forId(talkingTo.getId());
        switch (stage) {
            case 2000:
                if (master == Master.VANNAKA) {
                    sendDialogue(player, CALM_TALK, talkingTo.getId(), 2001, "'Ello and what are you after then?");
                }
                return true;
            case 2001:
                sendOptionDialogue(player, new int[]{2002, 2003, 2004, 2005, 2006}, "I need another assignment.", "Do you have anything for trade?", "About the task system...", "I am here to discuss any reward I might be eligble for.", "Er...nothing...");
                return true;
            case 2002:
                sendDialogue(player, CALM_TALK, -1, 2012, "I need another assignment.");
                return true;
            case 2003:
                sendDialogue(player, HAPPY_TALKING, -1, 2007, "Do you have anything for trade?");
                return true;
            case 2004:
                sendOptionDialogue(player, new int[]{2009, 2010}, "Tell me about the Task System.", "Sorry I was just leaving.");
                return true;
            case 2006:
                sendDialogue(player, CALM_TALK, -1, -1, "Er...nothing...");
                return true;
            case 2007:
                sendDialogue(player, HAPPY_TALKING, talkingTo.getId(), 2008, "I have a wide selection of Slayer equipment; take a look!");
                return true;
            case 2008:
                World.getWorld().getShopManager().openShop(player, Master.VANNAKA.getId());
                return false;
            case 2009:
                sendDialogue(player, CALM_TALK, -1, 2011, "Tell me about the task system.");
                return true;
            case 2010:
                sendDialogue(player, HAPPY_TALKING, -1, -1, "Sorry I was just leaving.");
                return true;
            case 2011:
                sendDialogue(player, CALM_TALK, talkingTo.getId(), -1, "There isn't much information on it now, come back later.");
                return true;
            case 2012:
                if (task != null) {
                    sendDialogue(player, CONFUSED, talkingTo.getId(), -1, "You're still hunting " + task.getName() + "; come back when you've", "finished your task.");
                } else {
                    double slayerExperience = player.getSkills().getXp(Skills.SLAYER);
                    SlayerTask newTask = null;
                    if (slayerExperience == 0) {
                        newTask = new SlayerTask(master, 0, 30);
                        sendDialogue(player, CALM_TALK, talkingTo.getId(), -1, "For your first task I'm assigning you to", "kill 30 bats.");
                    } else {
                        newTask = SlayerTask.random(player, master);
                        sendDialogue(player, CALM_TALK, talkingTo.getId(), -1, "Great, you're doing great. Your new task is to kill", newTask.getTaskAmount() + " " + newTask.getName() + "s");
                    }
                    this.task = newTask;
                }
                return true;
            case 2013:
                sendDialogue(player, HAPPY_TALKING, Master.VANNAKA.getId(), 2014, "Hello there, @PLAYER_NAME@, what can I help you with?");
                return true;
            case 2014:
                sendOptionDialogue(player, new int[]{2015, 2016, 2017, 2018, 2019}, "How am I doing so far?", "Who are you?", "Where are you?", "Got any tips for me?", "Nothing really.");
                return true;
            case 2015:
                sendDialogue(player, CALM_TALK, -1, 2020, "How am I doing so far?");
                return true;
            case 2016:
                sendDialogue(player, CALM_TALK, -1, 2021, "Who are you?");
                return true;
            case 2017:
                sendDialogue(player, CALM_TALK, -1, 2022, "Where are you?");
                return true;
            case 2018:
                sendDialogue(player, CALM_TALK, -1, 2023, "Got any tips for me?");
                return true;
            case 2019:
                sendDialogue(player, CALM_TALK, -1, -1, "Nothing really.");
                return true;
            case 2020:
                if (task != null) {
                    sendDialogue(player, HAPPY_TALKING, Master.VANNAKA.getId(), 2040, "You're current assigned to kill " + task.getName().toLowerCase() + "; only " + task.getTaskAmount() + " more", "to go.");
                } else {
                    sendDialogue(player, HAPPY_TALKING, Master.VANNAKA.getId(), 2040, "You currently have no task, come to me so I can assign you one.");
                }
                return true;
            case 2021:
                //TODO Support for different masters.
                sendDialogue(player, HAPPY_TALKING, Master.VANNAKA.getId(), 2041, "My name's Vannaka; I'm a Slayer Master.");
                return true;
            case 2022:
                sendDialogue(player, HAPPY_TALKING, Master.VANNAKA.getId(), 2042, "You'll find me in the default city used for Dementhium.", "I'll be here when you need a new task.");
                return true;
            case 2023:
                sendDialogue(player, CALM_TALK, Master.VANNAKA.getId(), 2043, "At the moment, no.");
                return true;
            case 2040:
                sendOptionDialogue(player, new int[]{2016, 2017, 2018, 2019}, "Who are you?", "Where are you?", "Got any tips for me?", "Nothing really.");
                return true;
            case 2041:
                sendOptionDialogue(player, new int[]{2015, 2017, 2018, 2019}, "How am I doing so far?", "Where are you?", "Got any tips for me?", "Nothing really.");
                return true;
            case 2042:
                sendOptionDialogue(player, new int[]{2015, 2016, 2018, 2019}, "How am I doing so far?", "Who are you?", "Got any tips for me?", "Nothing really.");
                return true;
            case 2043:
                sendOptionDialogue(player, new int[]{2015, 2016, 2017, 2019}, "How am I doing so far?", "Who are you?", "Where are you?", "Nothing really.");
                return true;
        }
        return true;
    }


    public SlayerTask getSlayerTask() {
        return task;
    }

    public void killedTask() {
        player.getSkills().addExperience(Skills.SLAYER, task.getXPAmount());
        task.decreaseAmount();
        if (task.getTaskAmount() < 1) {
            player.sendMessage("You have finished your slayer task, talk to a slayer master for a new one.");
            task = null;
        }
    }


    public void setSlayerTask(SlayerTask slayerTask) {
        this.task = slayerTask;
    }

}
