package org.dementhium.content.activity.impl.castlewars;

import org.dementhium.model.World;
import org.dementhium.model.map.Region;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;

import java.util.LinkedList;
import java.util.List;

/**
 * The Castle wars team manager.
 *
 * @author Emperor
 */
public class TeamManager {

    /**
     * The current flag state.
     *
     * @author Emperor
     */
    public static enum FlagState {

        /**
         * The flag is safe.
         */
        SAFE(0),

        /**
         * The flag has been taken.
         */
        TAKEN(1),

        /**
         * The flag has been dropped.
         */
        DROPPED(2);

        /**
         * The value of the state.
         */
        private final byte value;

        /**
         * constructs a new {@code FlagState} {@code Object}.
         *
         * @param value The value.
         */
        private FlagState(int value) {
            this.value = (byte) value;
        }

        /**
         * @return the value
         */
        public byte getValue() {
            return value;
        }
    }

    /**
     * The team-type of this team.
     */
    private final TeamType teamType;

    /**
     * The list of players in this team.
     */
    private final List<Player> players = new LinkedList<Player>();

    /**
     * A list of players in the waiting room.
     */
    private final List<Player> waitingPlayers = new LinkedList<Player>();

    /**
     * A list holding the names of disconnected players.
     */
    private final List<String> disconnectedPlayers = new LinkedList<String>();

    /**
     * A list of barricades.
     */
    private final List<NPC> barricades = new LinkedList<NPC>();

    /**
     * The health of the main gate.
     */
    private int doorHealth = 100;

    /**
     * If the door has been locked.
     */
    private boolean lockedDoor = true;

    /**
     * If the first rock has collapsed.
     */
    private boolean rockCleared = false;

    /**
     * If the second rock has collapsed.
     */
    private boolean rockCleared2 = false;

    /**
     * If the catapult is active.
     */
    private boolean catapultActive = true;

    /**
     * If this team's flag is secure.
     */
    private FlagState flagState = FlagState.SAFE;

    /**
     * The flag NPC used for sending the hint icon to the dropped flag.
     * FIXME
     */
    private NPC flagNPC;

    /**
     * The amount of points gained.
     */
    private int points;

    /**
     * Constructs a new {@code TeamManager} {@code Object}.
     *
     * @param teamType The team type.
     */
    public TeamManager(TeamType teamType) {
        this.teamType = teamType;
    }

    /**
     * Adds a barricade to this team.
     *
     * @param player The player.
     * @return {@code True}.
     */
    public boolean addBarricade(Player player) {
        if (!player.getInventory().contains(4053)) {
            return true;
        }
        if (barricades.size() == 10) {
            player.sendMessage("Your team has already set 10 barricades up.");
            return true;
        }
        for (NPC npc : barricades) {
            if (npc.getLocation().equals(player.getLocation())) {
                player.sendMessage("You can't set up a barricade here.");
                return true;
            }
        }
        player.getInventory().deleteItem(4053, 1);
        NPC barricade = World.getWorld().register(teamType == TeamType.SARADOMIN ? 1532 : 1534, player.getLocation());
        barricade.setUnrespawnable(true);
        barricades.add(barricade);
        int clipping = 256;
        clipping |= 0x20000;
        clipping |= 0x40000000;
        Region.addClipping(barricade.getLocation().getX(), barricade.getLocation().getY(), barricade.getLocation().getZ(), clipping);
        return true;
    }

    /**
     * Removes a barricade.
     *
     * @param npc The barricade.
     * @return {@code True} if succesful, {@code false} if not..
     */
    public boolean removeBarricade(NPC npc) {
        if (!barricades.remove(npc)) {
            return false;
        }
        World.getWorld().getNpcs().remove(npc);
        int clipping = 256;
        clipping |= 0x20000;
        clipping |= 0x40000000;
        Region.removeClipping(npc.getLocation().getX(), npc.getLocation().getY(), npc.getLocation().getZ(), clipping);
        return true;
    }

    /**
     * @return the teamType
     */
    public TeamType getTeamType() {
        return teamType;
    }

    /**
     * @return the players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * @return the waitingPlayers
     */
    public synchronized List<Player> getWaitingPlayers() {
        return waitingPlayers;
    }

    /**
     * @return the disconnectedPlayers
     */
    public List<String> getDisconnectedPlayers() {
        return disconnectedPlayers;
    }

    /**
     * @return the barricades
     */
    public List<NPC> getBarricades() {
        return barricades;
    }

    /**
     * @param health the health to set
     */
    public void setDoorHealth(int health) {
        this.doorHealth = health;
    }

    /**
     * @return the health
     */
    public int getDoorHealth() {
        return doorHealth;
    }

    /**
     * @param lockedDoor the lockedDoor to set
     */
    public void setLockedDoor(boolean lockedDoor) {
        this.lockedDoor = lockedDoor;
    }

    /**
     * @return the lockedDoor
     */
    public boolean isLockedDoor() {
        return lockedDoor;
    }

    /**
     * @param rockCleared the rockCollapsed to set
     */
    public void setRockCleared(boolean rockCleared) {
        this.rockCleared = rockCleared;
    }

    /**
     * @return the rockCollapsed
     */
    public boolean isRockCleared() {
        return rockCleared;
    }

    /**
     * @param rockCleared2 the rockCollapsed2 to set
     */
    public void setRockCleared2(boolean rockCleared2) {
        this.rockCleared2 = rockCleared2;
    }

    /**
     * @return the rockCollapsed2
     */
    public boolean isRockCleared2() {
        return rockCleared2;
    }

    /**
     * @param catapultActive the catapultActive to set
     */
    public void setCatapultActive(boolean catapultActive) {
        this.catapultActive = catapultActive;
    }

    /**
     * @return the catapultActive
     */
    public boolean isCatapultActive() {
        return catapultActive;
    }

    /**
     * @param flagState the flagState to set
     */
    public void setFlagState(FlagState flagState) {
        this.flagState = flagState;
    }

    /**
     * @return the flagState
     */
    public FlagState getFlagState() {
        return flagState;
    }

    /**
     * @param flagNPC the flagNPC to set
     */
    public void setFlagNPC(NPC flagNPC) {
        this.flagNPC = flagNPC;
    }

    /**
     * @return the flagNPC
     */
    public NPC getFlagNPC() {
        return flagNPC;
    }

    /**
     * @param points the points to set
     */
    public void setPoints(int points) {
        this.points = points;
    }

    /**
     * @return the points
     */
    public int getPoints() {
        return points;
    }

}