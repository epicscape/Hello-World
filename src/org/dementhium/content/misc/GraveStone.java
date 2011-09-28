package org.dementhium.content.misc;

import org.dementhium.model.World;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.misc.GroundItem;
import org.dementhium.model.misc.IconManager;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;
import org.dementhium.util.Misc;

import java.util.LinkedList;
import java.util.List;

/**
 * Represents a player's grave stone.
 *
 * @author Emperor
 */
public class GraveStone extends Tick {

    /**
     * A list holding the gravestone's items.
     */
    private final List<GroundItem> items = new LinkedList<GroundItem>();

    /**
     * The grave NPC.
     */
    private final NPC grave;

    /**
     * The player's name.
     */
    private final String owner;

    /**
     * The gravestone inscription.
     */
    private final String inscription;

    /**
     * The maximum time left.
     */
    private final int maximumTicks;

    /**
     * The amount of ticks for this grave.
     */
    private int ticks;

    /**
     * The current gravestone state.
     */
    private GraveStoneState currentState = GraveStoneState.CREATED;

    /**
     * Constructs a new {@code GraveStone} {@code Object}.
     *
     * @param owner The name of the player.
     * @param grave The NPC.
     */
    public GraveStone(String owner, int graveStone, NPC grave) {
        super(1);
        this.grave = grave;
        this.owner = owner;
        this.maximumTicks = getMaximumTicks(graveStone);
        this.ticks = maximumTicks;
        this.inscription = getInscription(owner, graveStone);
    }

    @Override
    public void execute() {
        Player player = World.getWorld().getPlayerInServer(owner);
        if (player != null) {
            int id = player.getConnection().getDisplayMode() < 2 ? 548 : 746;
            int minutes = ticks / 100;
            int seconds = (int) ((ticks - (minutes * 100)) * 0.6);
            String time = (minutes < 10 ? "0" + minutes : minutes) + ":" + (seconds < 10 ? "0" + seconds : seconds);
            ActionSender.sendString(player, id, id == 548 ? 13 : 166, time);
        }
        if (--ticks == 50) {
            if (currentState != GraveStoneState.BLESSED) {
                currentState = GraveStoneState.BROKEN;
            }
            grave.getMask().setSwitchId(grave.getId() + 1);
        }
        if (ticks == 100) {
            if (currentState != GraveStoneState.BLESSED) {
                currentState = GraveStoneState.BROKEN;
            }
            grave.getMask().setSwitchId(grave.getId() + 1);
            if (player != null) {
                player.sendMessage("Your grave is about to collapse.");
            }
        } else if (ticks < 1) {
            demolish(player, "Your grave has collapsed.");
            stop();
        }
    }

    /**
     * Demolishes this gravestone.
     */
    public void demolish(Player player, String message) {
        currentState = GraveStoneState.COLLAPSED;
        if (player != null) {
            player.sendMessage(message);
            IconManager.removeIcon(player, grave.getLocation());
            int id = player.getConnection().getDisplayMode() < 2 ? 548 : 746;
            ActionSender.sendInterfaceConfig(player, id, id == 548 ? 12 : 164, false);
            ActionSender.sendInterfaceConfig(player, id, id == 548 ? 13 : 165, false);
            ActionSender.sendInterfaceConfig(player, id, id == 548 ? 14 : 166, false);
        }
        for (GroundItem item : items) {
            item.setUpdateTicks(3);
        }
        grave.animate(Animation.create(7489));//TODO: Animation..
        World.getWorld().submit(new Tick(2) {
            @Override
            public void execute() {
                World.getWorld().getNpcs().remove(grave);
                stop();
            }
        });
        items.clear();
        GraveStoneManager.getGravestones().remove(owner);
    }

    /**
     * Gets the grave's NPC id.
     *
     * @param graveStone The grave stone id.
     * @return The NPC id.
     */
    public static int getNPCId(int graveStone) {
        if (graveStone == 13) {
            return 13296;
        }
        return 6565 + (graveStone * 3);
    }

    /**
     * Gets the gravestone's inscription.
     *
     * @param graveStone The gravestone id.
     * @return The inscription String.
     */
    private static String getInscription(String owner, int graveStone) {
        owner = Misc.formatPlayerNameForDisplay(owner);
        switch (graveStone) {
            case 0:
            case 1:
                return "In memory of <i>" + owner + "</i>,<br>who died here.";
            case 2:
            case 3:
                return "In loving memory of our dear friend <i>" + owner + "</i>,who <br>died in this place @X@ minutes ago.";
            case 4:
            case 5:
                return "In your travels, pause awhile to remember <i>" + owner + "</i>,<br>who passed away at this spot.";
            case 6:
                return "<i>" + owner + "</i>, <br>an enlightened servant of Saradomin,<br>perished in this place.";
            case 7:
                return "<i>" + owner + "</i>, <br>a most bloodthirsty follower of Zamorak,<br>perished in this place.";
            case 8:
                return "<i>" + owner + "</i>, <br>who walked with the Balance of Guthix,<br>perished in this place.";
            case 9:
                return "<i>" + owner + "</i>, <br>a vicious warrior dedicated to Bandos,<br>perished in this place.";
            case 10:
                return "<i>" + owner + "</i>, <br>a follower of the Law of Armadyl,<br>perished in this place.";
            case 11:
                return "<i>" + owner + "</i>, <br>servant of the Unknown Power,<br>perished in this place.";
            case 12:
                return "Ye frail mortals who gaze upon this sight, forget not<br>the fate of <i>" + owner + "</i>, once mighty, now surrendered to the inescapable grasp of destiny.<br><i>Requiescat in pace.</i>";
            case 13:
                return "Here lies <i>" + owner + "</i>, friend of dwarves. Great in<br>life, glorious in death. His/Her name lives on in<br>song and story.";
        }
        return "Cabbage";
    }

    /**
     * Gets the maximum amount of ticks.
     *
     * @param graveStone The gravestone id.
     * @return The maximum amount of ticks.
     */
    private static int getMaximumTicks(int graveStone) {
        switch (graveStone) {
            case 0:
                return 500;
            case 1:
            case 2:
                return 600;
            case 3:
                return 800;
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                return 1000;
            case 12:
                return 1200;
            case 13:
                return 1500;
        }
        return 500;
    }

    /**
     * @return the owner
     */
    public String getOwner() {
        return owner;
    }

    /**
     * @return the grave
     */
    public NPC getGrave() {
        return grave;
    }

    /**
     * @return the items
     */
    public List<GroundItem> getItems() {
        return items;
    }

    /**
     * @return the ticks
     */
    public int getTicks() {
        return ticks;
    }

    /**
     * @param ticks the ticks to set
     */
    public void setTicks(int ticks) {
        this.ticks = ticks;
    }

    /**
     * Gets the current gravestone state.
     *
     * @return The gravestone state.
     */
    public GraveStoneState getCurrentState() {
        return currentState;
    }

    /**
     * Sets the current gravestone state.
     *
     * @param currentState The current state.
     */
    public void setCurrentState(GraveStoneState currentState) {
        this.currentState = currentState;
    }

    /**
     * Gets the maximum amount of ticks.
     *
     * @return The maximum amount.
     */
    public int getMaximumTicks() {
        return maximumTicks;
    }

    /**
     * Gets the inscription on the grave.
     *
     * @return The inscription.
     */
    public String getInscription() {
        return inscription.replace("@X@", "" + (ticks / 100));
    }
}