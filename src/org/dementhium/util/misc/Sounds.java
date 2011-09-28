package org.dementhium.util.misc;

import java.util.List;

import org.dementhium.model.Location;
import org.dementhium.model.map.Region;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

public class Sounds {


    /**
     * @author mgi125
     */
    public static final int NexNoEscape = 3292, NexBloodSacrifice = 3293,
            NexThereIs = 3294, NexAtLast = 3295, NexVirus = 3296,
            NexCrourDontFail = 3298, NexCrour = 3299, NexDarken = 3302,
            NexInfuseMeIce = 3303, NexGlacies = 3304, NexFloodBlood = 3306,
            NexUmbraDontFail = 3307, NexDieNowInPrison = 3308,
            NexFillSmoke = 3310, NexPowerOfZaros = 3312, NexUmbra = 3313,
            NexFearTheShadow = 3314, NexContainThis = 3316, NexSiphon = 3317,
            NexFumusDontFail = 3321, NexEmbraceDarkness = 3322,
            NexTasteWrath = 3323, NexFumus = 3325, NexGlaciesDontFail = 3327;

    /**
     * Sends a certain sound to all players in distance of the center location.
     * @param center The center location.
     * @param sound The sound to play.
     * @param distance The distance.
     */
    public static void playSound(Location center, int sound, int distance) {
    	List<Player> players = Region.getLocalPlayers(center, distance);
        for (Player player : players) {
        	ActionSender.sendSound(player, sound, 100, 255, true);
        }
    }
}
