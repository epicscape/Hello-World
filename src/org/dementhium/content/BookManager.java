package org.dementhium.content;

import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 * @author Lumby <lumbyjr@hotmail.com>
 */
public class BookManager {

    public static boolean proceedBook(Player player, int stage) {
        /*
           * Start and end all books from here! First value is the previous button
           * stage Second value is next button stage First Boolean is enable back
           * button Second Boolean is enable next button
           */
        switch (stage) {
            case 1:
                sendBook(player, -1, 2, false, true, "EpicScape Rule Book",
                        "<shad=6081134>EpicScape Rules</shad>", "",
                        "In this book you will find the",
                        "rules that need to be followed",
                        "when playing the server.", "", "FAILURE TO FOLLOW",
                        "THESE RULES WILL", "RESULT IN YOUR", "ACCOUNT BEING",
                        "BANNED OR RESET.", "~RULE 1~", "Offensive Language",
                        "You are in no way allowed", "to use offensive language",
                        "towards other players ingame.", "", "~RULE 2~",
                        "Hacking/Duping", "Anyone partaking in a dupe",
                        "or hack will be IP banned.", "");
                return true;
            case 2:
                sendBook(player, 1, 3, false, false, "EpicScape Rule Book",
                        "~RULE 3 - Spamming~", "Autotypers must be set with",
                        "an interval of 10 seconds.", "Anyone using them more",
                        "frequently will recieve a", "temporary mute.", "",
                        "~RULE 4 - Botting/Macro~", "Anyone using a bot to gain",
                        "an unfair advantage will be", "IP banned.",
                        "~RULE 5 - Bug Abuse~", "If you find a bug please",
                        "report it on the forums.", "Anyone who is abusing a bug",
                        "will be banned.", "", "~RULE 6 - Scamming~",
                        "You may not trick or scam", "a player in any way. You",
                        "will recieve a permanent ban.");
                return true;
            case 3:
                sendBook(player, 2, -1, true, false, "EpicScape Rule Book",
                        "~RULE 7 Disrespecting Staff~",
                        "You may not disrespect any", "staff member in any way.",
                        "You are to treat the staff", "with the upmost respect.",
                        "", "~RULE 8 Aggravating Users~",
                        "You may not Aggravate a", "user and pick on them to",
                        "make them mad. This is", "immature and will result",
                        "in a mute.", "", "~RULE 8 - Trolling~",
                        "This doesn't need to be", "explained, simply anyone who",
                        "is trolling will recieve a mute.", "",
                        "Thank you for reading and", "we hope you enjoy playing",
                        "EpicScape safely.");
                return false;
            case 4:
            	sendBook(player, -1, -1, true, true, "EpicScape Commands List",
                        "<col=7A0000>::Barrows</col>",
                        "Teleports you to barrows.",
                        "<col=7A0000>::Rocks</col>",
                        "Teleports you to rock crabs.",
                        "<col=7A0000>::Trees</col>",
                        "Shows up a list of available", "tree spot teleports.",
                        "<col=7A0000>::Shops</col>",
                        "Brings you to the shops.",
                        "<col=7A0000>::Market</col>",
                        "Brings you to the marketplace.",
                        "<col=7A0000>::Donate</col>",
                        "Shows you information about", 
                        "Donating for EpicScape.",
                        "<col=7A0000>::Help</col>", 
                        "Shows you information on", 
                        "how to obtain game help."
                       // "[<col=F5C507>$</col>] <col=7A0000>::Magics</col>",
                        );
            	return true;
            default:
                return false;
        }
    }

    public static void processNextPage(Player player) {
        int stage;
        Object attribute = player.getAttribute("nextBookStage");
        stage = (Integer) attribute;
        if (stage == -1) {
            resetBook(player, false);
            return;
        }
        if (!proceedBook(player, stage)) {
            resetBook(player, false);
        }
    }

    public static void processPreviousPage(Player player) {
        int stage;
        Object attribute = player.getAttribute("previousBookStage");
        stage = (Integer) attribute;
        if (stage == -1) {
            resetBook(player, true);
            return;
        }
        if (!proceedBook(player, stage)) {
            resetBook(player, true);
        }
    }

    public static void sendBook(Player player, int previousBookStage,
                                int nextBookStage, boolean hideNext, boolean hideBack,
                                String title, String... content) {
        if (content.length == 0 || content.length > 22) {
            return;
        }
        ActionSender.sendString(player, 959, 5, title);
        int index = 30;
        for (String s : content) {
            ActionSender.sendString(player, 959, index, s);
            ActionSender.sendInterfaceConfig(player, 959, index, true);
            index++;
        }
        if (content.length < 22) {
            int blankContentNumber = 22 - content.length;
            int blankIndex = 30 + content.length;
            for (int i = 0; i < blankContentNumber; i++) {
                ActionSender.sendInterfaceConfig(player, 959, blankIndex + i,
                        false);
            }
        }
        ActionSender.sendString(player, 959, 53, "Next Page");
        ActionSender.sendString(player, 959, 52, "Previous Page");
        if (hideNext) {
            ActionSender.sendInterfaceConfig(player, 959, 29, false);
            ActionSender.sendInterfaceConfig(player, 959, 53, false);
        } else {
            ActionSender.sendInterfaceConfig(player, 959, 29, true);
            ActionSender.sendInterfaceConfig(player, 959, 53, true);
        }
        if (hideBack) {
            ActionSender.sendInterfaceConfig(player, 959, 28, false);
            ActionSender.sendInterfaceConfig(player, 959, 52, false);
        } else {
            ActionSender.sendInterfaceConfig(player, 959, 28, true);
            ActionSender.sendInterfaceConfig(player, 959, 52, true);
        }
        ActionSender.sendInterface(player, 959);
        player.removeAttribute("nextBookStage");
        player.removeAttribute("previousBookStage");
        player.setAttribute("nextBookStage", nextBookStage);
        player.setAttribute("previousBookStage", previousBookStage);
    }

    public static void resetBook(Player player, boolean resetPrevious) {
        if (!resetPrevious)
            player.removeAttribute("nextBookStage");
        else
            player.removeAttribute("previousBookStage");
    }
}
