package org.dementhium.content.activity.impl;

import org.dementhium.content.activity.Activity;
import org.dementhium.content.activity.impl.duel.DuelConfigurations;
import org.dementhium.content.activity.impl.duel.DuelConfigurations.Rules;
import org.dementhium.content.activity.impl.duel.DuelConfigurations.TeleportLocations;
import org.dementhium.content.activity.impl.duel.Stakes;
import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.mask.ForceText;
import org.dementhium.model.misc.IconManager;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;
import org.dementhium.util.Misc;

/**
 * Handles the activity: <code>Duel Arena</code>.
 *
 * @author Emperor
 */
public class DuelActivity extends Activity<Player> {

    /**
     * The current duel arena state.
     *
     * @author Emperor
     */
    public static enum State {
        FIRST_SCREEN, SECOND_SCREEN, FIGHTING, FIRST_FRIENDLY_SCREEN, SECOND_FRIEDNLY_SCREEN
    }
    
    /**
     * The duel rules friendly interface id.
     */
    public static final short DUEL_RULES_FRIENDLY_INTERFACE = 637;

    /**
     * The second duel friendly interface id.
     */
    public static final short DUEL_SECOND_FRIENDLY_INTERFACE = 639;
    
    /**
     * The duel rules interface id.
     */
    public static final short DUEL_RULES_INTERFACE = 631;

    /**
     * The second duel interface id.
     */
    public static final short DUEL_SECOND_INTERFACE = 626;

    /**
     * Represents the other player in a duel.
     */
    private final Player otherPlayer;

    /**
     * Represents the duel configurations used during this session.
     */
    private DuelConfigurations duelConfigurations;

    /**
     * Represents the total stake, which will be received by the winner of this duel.
     */
    private Container stakes;

    /**
     * If the activity has commenced.
     */
    private boolean commenced = false;

    /**
     * If the activity is finished.
     */
    private boolean finished = false;

    /**
     * The current dueling state.
     */
    private State currentState;

    /**
     * Constructs a new {@code Duel activity} instance.
     *
     * @param challenger  The player challenging.
     * @param otherPlayer The other player.
     * @param stake       The rewards for the player who wins the duel.
     */
    public DuelActivity(Player challenger, Player otherPlayer) {
        super(challenger);
        this.otherPlayer = otherPlayer;
        this.stakes = new Container(56, false);
        reset(otherPlayer, getPlayer());
        getPlayer().setActivity(Mob.DEFAULT_ACTIVITY);
        otherPlayer.setActivity(Mob.DEFAULT_ACTIVITY);
        getPlayer().setAttribute("hasWonDuel", false);
        otherPlayer.setAttribute("hasWonDuel", false);
        getPlayer().setAttribute("droppedAmmo", null);
        otherPlayer.setAttribute("droppedAmmo", null);
        addEntity(otherPlayer);
    }

    @Override
    public boolean initializeActivity() {
        if (getPlayer() == null || otherPlayer == null) {
            this.stop();
            return false;
        }
        if (player.setAttribute("isStaking", Boolean.TRUE)) {
	        duelConfigurations = new DuelConfigurations();
	        getPlayer().setActivity(this);
	        otherPlayer.setActivity(this);
	        ActionSender.sendConfig(getPlayer(), 286, 0);
	        ActionSender.sendConfig(otherPlayer, 286, 0);
	        ActionSender.sendString(getPlayer(), "", 631, 28);
	        ActionSender.sendString(otherPlayer, "", 631, 28);
	        String name = getPlayer().getUsername();
	        String name1 = otherPlayer.getUsername();
	        ActionSender.sendString(getPlayer(), 631, 23, "" + Misc.formatPlayerNameForDisplay(name1));
	        ActionSender.sendString(otherPlayer, 631, 23, "" + Misc.formatPlayerNameForDisplay(name));
	        ActionSender.sendString(getPlayer(), "" + otherPlayer.getSkills().getCombatLevel(), 631, 25);
	        ActionSender.sendString(otherPlayer, "" + getPlayer().getSkills().getCombatLevel(), 631, 25);
	        ActionSender.sendInventoryInterface(getPlayer(), 628);
	        ActionSender.sendInventoryInterface(otherPlayer, 628);
	        ActionSender.sendInterface(getPlayer(), DUEL_RULES_INTERFACE);
	        ActionSender.sendInterface(otherPlayer, DUEL_RULES_INTERFACE);
	        ActionSender.sendDuelOptions(getPlayer());
	        ActionSender.sendDuelOptions(otherPlayer);
	        getPlayer().setAttribute("duelStakes", new Stakes(getPlayer()));
	        otherPlayer.setAttribute("duelStakes", new Stakes(otherPlayer));
	        ((Stakes) getPlayer().getAttribute("duelStakes")).refresh();
	        ((Stakes) otherPlayer.getAttribute("duelStakes")).refresh();
	        setCurrentState(State.FIRST_SCREEN);
	        setActivityState(SessionStates.PAUSE_STATE);
	        return false;
        //Started on "Friendly" dueling
         } else {
        	 if (player.setAttribute("isFriendly", Boolean.TRUE)) {
        		duelConfigurations = new DuelConfigurations();
    	        getPlayer().setActivity(this);
    	        otherPlayer.setActivity(this);
    	        ActionSender.sendConfig(getPlayer(), 286, 0);
    	        ActionSender.sendConfig(otherPlayer, 286, 0);
    	        ActionSender.sendString(getPlayer(), "", 631, 44);
    	        ActionSender.sendString(otherPlayer, "", 631, 44);
    	        String name = getPlayer().getUsername();
    	        String name1 = otherPlayer.getUsername();
    	        ActionSender.sendString(getPlayer(), Misc.formatPlayerNameForDisplay(name1), 631, 23);
    	        ActionSender.sendString(otherPlayer, Misc.formatPlayerNameForDisplay(name), 631, 23);
    	        ActionSender.sendString(getPlayer(), "Test", 631, 23);
    	        ActionSender.sendString(otherPlayer, "Test", 631, 23);
    	        ActionSender.sendString(getPlayer(), "" + otherPlayer.getSkills().getCombatLevel(), 637, 17);
    	        ActionSender.sendString(otherPlayer, "" + getPlayer().getSkills().getCombatLevel(), 637, 15);
    	        ActionSender.sendInventoryInterface(getPlayer(), 628);
    	        ActionSender.sendInventoryInterface(otherPlayer, 628);
    	        ActionSender.sendInterface(getPlayer(), DUEL_RULES_FRIENDLY_INTERFACE);
    	        ActionSender.sendInterface(otherPlayer, DUEL_RULES_FRIENDLY_INTERFACE);
    	        setCurrentState(State.FIRST_FRIENDLY_SCREEN);
    	        setActivityState(SessionStates.PAUSE_STATE);
    	        return false;
        	}
        }
    }

    @Override
    public boolean commenceSession() {
        ActionSender.sendCloseInterface(getPlayer());
        ActionSender.sendCloseInterface(otherPlayer);
        ActionSender.sendCloseChatBox(getPlayer());
        ActionSender.closeInventoryInterface(getPlayer());
        ActionSender.sendCloseChatBox(otherPlayer);
        ActionSender.closeInventoryInterface(otherPlayer);
        ActionSender.sendPlayerOption(getPlayer(), "Attack", 1, false);
        ActionSender.sendPlayerOption(otherPlayer, "Attack", 1, false);
        transferStake(getPlayer());
        transferStake(otherPlayer);
        getPlayer().setAttribute("duelingWith", otherPlayer);
        otherPlayer.setAttribute("duelingWith", getPlayer());
        TeleportLocations loc = TeleportLocations.NORMAL_ARENA;
        if (duelConfigurations.getRule(Rules.SUMMONING)) {
            loc = TeleportLocations.SUMMONING_ARENA;
        } else if (duelConfigurations.getRule(Rules.OBSTACLES)) {
            loc = TeleportLocations.OBSTACLES_ARENA;
        }
        DuelConfigurations.teleport(getPlayer(), loc, false);
        DuelConfigurations.teleport(otherPlayer, loc, duelConfigurations.getRule(Rules.MOVEMENT));
        getPlayer().setAttribute("cantMove", Boolean.TRUE);
        otherPlayer.setAttribute("cantMove", Boolean.TRUE);
        ActionSender.sendPlayerOption(getPlayer(), "Attack", 1, false);
        ActionSender.sendPlayerOption(otherPlayer, "Attack", 1, false);
        World.getWorld().submit(new Tick(2) {
            int count = 3;

            @Override
            public void execute() {
                if (count == 0) {
                    getPlayer().getMask().setForceText(new ForceText("FIGHT!"));
                    otherPlayer.getMask().setForceText(new ForceText("FIGHT!"));
                    getPlayer().removeAttribute("cantMove");
                    otherPlayer.removeAttribute("cantMove");
                    this.stop();
                    commenced = true;
                    return;
                }
                ActionSender.sendPlayerOption(getPlayer(), "Attack", 1, false); //weird bug, so we send it 3 times :D
                ActionSender.sendPlayerOption(otherPlayer, "Attack", 1, false); //It's probably your AreaUpdateTick or w/e it's called.
                getPlayer().getMask().setForceText(new ForceText(count + ""));
                otherPlayer.getMask().setForceText(new ForceText((count--) + ""));
            }
        });
        IconManager.iconOnMob(getPlayer(), otherPlayer, 1, 65535);
        IconManager.iconOnMob(otherPlayer, getPlayer(), 1, 65535);
        return true;
    }

    @Override
    public boolean endSession() {
        if (finished || getPlayer().getActivity() != this || otherPlayer.getActivity() != this) {
            return true;
        }
        finished = true;
        if (getPlayer().getAttribute("duellingForfeit") == Boolean.TRUE) {
            otherPlayer.setAttribute("hasWonDuel", true);
            getPlayer().setAttribute("hasWonDuel", false);
        } else if (otherPlayer.getAttribute("duellingForfeit") == Boolean.TRUE) {
            otherPlayer.setAttribute("hasWonDuel", false);
            getPlayer().setAttribute("hasWonDuel", true);
        }
        ActionSender.sendPlayerOption(getPlayer(), "Challenge", 1, false);
        ActionSender.sendPlayerOption(otherPlayer, "Challenge", 1, false);
        DuelConfigurations.teleport(getPlayer(), TeleportLocations.CHALLENGE_ROOM, false);
        DuelConfigurations.teleport(otherPlayer, TeleportLocations.CHALLENGE_ROOM, false);
        getPlayer().setAttribute("duelingWith", null);
        otherPlayer.setAttribute("duelingWith", null);
        IconManager.removeIcon(getPlayer(), otherPlayer);
        IconManager.removeIcon(otherPlayer, getPlayer());
        getPlayer().setAttribute("isStaking", Boolean.FALSE);
        otherPlayer.setAttribute("isStaking", Boolean.FALSE);
        final Container spoils = (getPlayer().getAttribute("hasWonDuel") == Boolean.TRUE ?
                ((Stakes) otherPlayer.getAttribute("duelStakes")).getContainer() :
                ((Stakes) getPlayer().getAttribute("duelStakes")).getContainer());
        reset(getPlayer(), otherPlayer);
        setActivityState(SessionStates.PAUSE_STATE);
        World.getWorld().submit(new Tick(1) {
            @Override
            public void execute() {
                checkAmmunition(getPlayer());
                checkAmmunition(otherPlayer);
                if (getPlayer().getAttribute("hasWonDuel") == Boolean.TRUE) {
                    otherPlayer.setAttribute("hasWonDuel", false);
                    getPlayer().setAttribute("hasWonDuel", false);
                    getPlayer().fullRestore();
                    addSpoils(getPlayer(), spoils);
                } else if (otherPlayer.getAttribute("hasWonDuel") == Boolean.TRUE) {
                    getPlayer().setAttribute("hasWonDuel", false);
                    otherPlayer.setAttribute("hasWonDuel", false);
                    otherPlayer.fullRestore();
                    addSpoils(otherPlayer, spoils);
                }
                getPlayer().setActivity(Mob.DEFAULT_ACTIVITY);
                otherPlayer.setActivity(Mob.DEFAULT_ACTIVITY);
                getPlayer().setAttribute("isStaking", Boolean.FALSE);
                otherPlayer.setAttribute("isStaking", Boolean.FALSE);
                stop();
                DuelActivity.this.stop(false); //Incase the endSession() method gets called in the SessionLogoutTask.
            }
        });
        return false;
    }

    @Override
    public boolean forceEnd(Player player) {
        if (getActivityState() != SessionStates.UPDATE_STATE) {
            return true;
        }
        Stakes stake = player.getAttribute("duelStakes");
        if (stake == null) {
            return true;
        }
        for (Item item : stake.getContainer().toArray()) {
            player.getInventory().addItem(item);
        }
        DuelConfigurations.teleport(getPlayer(), TeleportLocations.CHALLENGE_ROOM, false);
        player.setActivity(Mob.DEFAULT_ACTIVITY);
        getPlayer().setAttribute("isStaking", Boolean.FALSE);
        otherPlayer.setAttribute("isStaking", Boolean.FALSE);
        return true;
    }

    /**
     * Adds any used ammunition in the duel arena.
     *
     * @param player The player.
     */
    private void checkAmmunition(Player player) {
        int[][] ammoRecord = (int[][]) player.getAttribute("droppedAmmo");
        if (ammoRecord == null) {
            return;
        }
        for (int i = 0; i < ammoRecord.length; i++) {
            if (ammoRecord[i][0] > 0) {
                player.getInventory().addDropable(new Item(ammoRecord[i][0], ammoRecord[i][1]));
            }
        }
    }

    @Override
    public boolean walkingUpdate(Player player) {
        boolean isInChallengeRoom = TeleportLocations.CHALLENGE_ROOM.getArea().contains(player.getLocation());
        if (!isInChallengeRoom && duelConfigurations.getRule(Rules.MOVEMENT)) {
            player.sendMessage("Movement has been disabled during this duel.");
            return false;
        } else if (isInChallengeRoom) {
            decline(player);
        }
        return true;
    }

    @Override
    public boolean canLogout(Player player, boolean logoutButton) {
        if (!finished) {
            if (logoutButton) {
                player.sendMessage("You can't logout in a duel.");
                return false;
            }
            player.setAttribute("duellingForfeit", true);
            setActivityState(SessionStates.END_STATE);
        }
        return false;
    }

    @Override
    public boolean onTeleport(Player player) {
        player.sendMessage("You can't teleport out of a duel!");
        return false;
    }

    @Override
    public boolean onDeath(Player player) {
        Player killer = getOpponent(player);
        player.setAttribute("hasWonDuel", false);
        killer.setAttribute("hasWonDuel", true);
        player.setAttribute("duellingForfeit", false);
        killer.setAttribute("duellingForfeit", false);
        endSession();
        stop(false);
        return true;
    }

    @Override
    public boolean isCombatActivity(Mob mob, Mob victim) {
        if (mob.isNPC() || victim.isNPC()) {
            return false;
        }
        if (mob.getPlayer() != getPlayer() && mob.getPlayer() != otherPlayer) {
            return false;
        }
        if (!commenced) {
            mob.getPlayer().sendMessage("The duel hasn't started yet!");
            return false;
        }
        return victim.getPlayer() == getPlayer() || victim.getPlayer() == otherPlayer;
    }

    /**
     * Declines the duel.
     *
     * @param player The player declining.
     * @return {@code True}.
     */
    public boolean decline(Player player) {
        Player other = getOpponent(player);
        ActionSender.sendCloseInterface(player);
        ActionSender.sendCloseInterface(other);
        ActionSender.closeInventoryInterface(player);
        ActionSender.closeInventoryInterface(other);
        player.getInventory().refresh();
        other.getInventory().refresh();
        reset(player, other);
        player.setActivity(Mob.DEFAULT_ACTIVITY);
        other.setActivity(Mob.DEFAULT_ACTIVITY);
        getPlayer().setAttribute("isStaking", Boolean.FALSE);
        otherPlayer.setAttribute("isStaking", Boolean.FALSE);
        player.sendMessage("You've declined the duel.");
        other.sendMessage("Other player has declined the duel.");
        stop(false);
        return true;
    }

    /**
     * Resets all the attributes.
     *
     * @param player The first player.
     * @param other  The second player.
     * @return {@code True}.
     */
    private boolean reset(Player player, Player other) {
        player.setAttribute("duelStake", null);
        other.setAttribute("duelStake", null);
        player.setAttribute("duelingWith", null);
        other.setAttribute("duelingWith", null);
        player.setAttribute("acceptedDuel", false);
        other.setAttribute("acceptedDuel", false);
        player.setAttribute("duellingForfeit", false);
        other.setAttribute("duellingForfeit", false);
        getPlayer().setAttribute("isStaking", Boolean.FALSE);
        otherPlayer.setAttribute("isStaking", Boolean.FALSE);
        return true;
    }

    /**
     * Accepts the duel.
     *
     * @param player The player accepting.
     * @return {@code True}.
     */
    public boolean accept(Player player) {
        if (duelConfigurations.canAccept(player)) {
            Player other = getOpponent(player);
            player.setAttribute("acceptedDuel", true);
            ActionSender.sendString(other, "Other player has accepted.", 631, 26);
            ActionSender.sendString(player, "Accepted duel rules and stakes.", 631, 26);
            if (other.getAttribute("acceptedDuel") == Boolean.TRUE) {
                player.setAttribute("acceptedDuel", false);
                other.setAttribute("acceptedDuel", false);
                setCurrentState(State.SECOND_SCREEN);
                duelConfigurations.sendSecondInterface(player, other);
                duelConfigurations.sendSecondInterface(other, player);
            }
        }
        return true;
    }

    /**
     * Accepts the second duel interface.
     *
     * @param player The plater accepting.
     */
    public void acceptSecond(Player player) {
        Player other = getOpponent(player);
        player.setAttribute("acceptedDuel", true);
        ActionSender.sendString(other, "Other player has accepted.", 626, 45);
        ActionSender.sendString(player, "Accepted duel rules and stakes.", 626, 45);
        if (other.getAttribute("acceptedDuel") == Boolean.TRUE) {
            setActivityState(SessionStates.COMMENCE_STATE);
            setCurrentState(State.FIGHTING);
            player.fullRestore();
            other.fullRestore();
            duelConfigurations.removeEquipment(player);
            duelConfigurations.removeEquipment(other);
        }
    }

    /**
     * Adds the stakes to the player's inventory.
     *
     * @param player The player.
     * @return {@code True} if spoils got added, {@code false} if not.
     */
    private boolean addSpoils(Player player, Container spoils) {
        ActionSender.sendInterface(player, 634);
        Player other = getOpponent(player);
        Container stake = ((Stakes) other.getAttribute("duelStakes")).getContainer();
        other.getInventory().removeItems(stake.getData());
        player.getInventory().removeItems(((Stakes) player.getAttribute("duelStakes")).getContainer().getData());
        ActionSender.sendAMask(player, 1026, 634, 28, 0, 35);
        ActionSender.sendClientScript(player, 149, new Object[]{"", "", "", "", "", -1, 0, 6, 6, 136, 634 << 16 | 28}, "noooobsssss");
        ActionSender.sendItems(player, 136, stake, false);
        ActionSender.sendString(player, 634, 33, Misc.formatPlayerNameForDisplay(other.getUsername()));
        ActionSender.sendString(player, 634, 32, Integer.toString(other.getSkills().getCombatLevel()));
        for (Item item : stakes.toArray()) {
            if (item != null) {
                player.getInventory().addDropable(item);
            }
        }
        return true;
    }

    /**
     * Removes the stake from the player's inventory, and adds it to the stakes {@link Container}.
     *
     * @param player The player.
     * @return Always {@code true}.
     */
    private boolean transferStake(Player player) {
        Stakes stake = (Stakes) player.getAttribute("duelStakes");
        if (stake == null) {
            return false;
        }
        stakes.addAll(stake.getContainer());
        return true;
    }

    /**
     * Gets the opponent.
     *
     * @param thisPlayer The player who's looking for the opponent.
     * @return The opponent player.
     */
    public Player getOpponent(Player thisPlayer) {
        return getPlayer().equals(thisPlayer) ? otherPlayer : getPlayer();
    }

    /**
     * Gets one of both players.
     *
     * @return A player.
     */
    public Player getOtherPlayer() {
        return otherPlayer;
    }

    /**
     * @param duelConfigurations the duelConfigurations to set
     */
    public void setDuelConfigurations(DuelConfigurations duelConfigurations) {
        this.duelConfigurations = duelConfigurations;
    }

    /**
     * @return the duelConfigurations
     */
    public DuelConfigurations getDuelConfigurations() {
        return duelConfigurations;
    }

    /**
     * Sets the stakes.
     *
     * @param stake The stake {@code Item Container}.
     */
    public void setStakes(Container stake) {
        this.stakes = stake;
    }

    /**
     * Gets the {@code Item} {@link Container} of stakes.
     *
     * @return The stakes
     */
    public Container getStakes() {
        return stakes;
    }

    /**
     * @return the commenced
     */
    public boolean isCommenced() {
        return commenced;
    }

    public State getCurrentState() {
        return currentState;
    }

    public void setCurrentState(State currentState) {
        this.currentState = currentState;
    }
}
