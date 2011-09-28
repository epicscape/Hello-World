package org.dementhium.content.activity.impl;

import org.dementhium.content.activity.Activity;
import org.dementhium.content.activity.impl.castlewars.*;
import org.dementhium.content.activity.impl.castlewars.DynamicObject.ActionType;
import org.dementhium.content.activity.impl.castlewars.TeamManager.FlagState;
import org.dementhium.event.EventListener.ClickOption;
import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.map.ObjectManager;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.misc.IconManager;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;

import java.util.LinkedList;
import java.util.List;

/**
 * The castle wars activity.
 *
 * @author Emperor
 */
public class CastleWarsActivity extends Activity<Player> {
	//2129 id animation, 2690 is the projectile id

    /**
     * The singleton.
     */
    private static final CastleWarsActivity SINGLETON = new CastleWarsActivity();

    /**
     * The zamorakian team.
     */
    private final TeamManager zamorakTeam;

    /**
     * The saradomin team.
     */
    private final TeamManager saradominTeam;

    /**
     * The amount of waiting ticks until next game starts.
     */
    private int waitingTicks = 500;

    /**
     * The amount of minutes left.
     */
    private int minutesLeft = 5;

    /**
     * The amount of minutes left last update.
     */
    private int lastMinute = 5;

    /**
     * Constructs a new {@code CastleWarsActivity} {@code Object}.
     */
    private CastleWarsActivity() {
        this.zamorakTeam = new TeamManager(TeamType.ZAMORAK);
        this.saradominTeam = new TeamManager(TeamType.SARADOMIN);
        TeamType.ZAMORAK.setOppositeTeam(saradominTeam);
        TeamType.SARADOMIN.setOppositeTeam(zamorakTeam);
    }

    @Override
    public boolean initializeActivity() {
        /*
         * As this is a global, continuing activity, it will initialize regardless.
         */
        return true;
    }

    @Override
    public boolean commenceSession() {
        /*
         * As this is a global, continuing activity, it will commence regardless.
         */
        return true;
    }

    @Override
    public boolean updateSession() {
        minutesLeft = waitingTicks / 100;
        updateWaitingRooms();
        updatePlayers();
        waitingTicks--;
        return true;
    }

    /**
     * Updates the players in game.
     */
    private void updatePlayers() {
        int gameTicks = (waitingTicks - 500) / 100;
        if (gameTicks < 1) {
            List<Player> players = new LinkedList<Player>(getSaradominTeam().getPlayers());
            players.addAll(getZamorakTeam().getPlayers());
            for (Player player : players) {
                if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4037 || player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4039) {
                    for (Player p : players) {
                        IconManager.removeIcon(player, p);
                    }
                    player.getEquipment().set(Equipment.SLOT_WEAPON, null);
                }
                IconManager.removeIcon(player, saradominTeam.getFlagNPC());
                IconManager.removeIcon(player, zamorakTeam.getFlagNPC());
                removeItems(player);
                player.fullRestore();
                player.teleport(2440 + Misc.random(4), 3083 + Misc.random(12), 0);
                ActionSender.sendCloseOverlay(player);
                player.setActivity(Mob.DEFAULT_ACTIVITY);
            }
            getZamorakTeam().getPlayers().clear();
            getSaradominTeam().getPlayers().clear();
            getZamorakTeam().getDisconnectedPlayers().clear();
            getSaradominTeam().getDisconnectedPlayers().clear();
            resetGameField();
            return;
        }
        for (Player player : zamorakTeam.getPlayers()) {
            updateCastleWarsScreen(player, zamorakTeam);
            ActionSender.sendConfig(player, 380, gameTicks);
        }
        for (Player player : saradominTeam.getPlayers()) {
            updateCastleWarsScreen(player, saradominTeam);
            ActionSender.sendConfig(player, 380, gameTicks);
        }
    }

    /**
     * Resets the game field to prepare for the next game.
     */
    private void resetGameField() {
        zamorakTeam.setCatapultActive(true);
        zamorakTeam.setFlagState(FlagState.SAFE);
        zamorakTeam.setLockedDoor(true);
        zamorakTeam.setDoorHealth(100);
        zamorakTeam.setPoints(0);
        zamorakTeam.setRockCleared(false);
        zamorakTeam.setRockCleared2(false);
        zamorakTeam.setFlagNPC(null);
        List<NPC> barricades = new LinkedList<NPC>(zamorakTeam.getBarricades());
        for (NPC npc : barricades) {
            zamorakTeam.removeBarricade(npc);
        }
        saradominTeam.setCatapultActive(true);
        saradominTeam.setFlagState(FlagState.SAFE);
        saradominTeam.setLockedDoor(true);
        saradominTeam.setDoorHealth(100);
        saradominTeam.setPoints(0);
        saradominTeam.setRockCleared(false);
        saradominTeam.setRockCleared2(false);
        saradominTeam.setFlagNPC(null);
        barricades = new LinkedList<NPC>(saradominTeam.getBarricades());
        for (NPC npc : barricades) {
            saradominTeam.removeBarricade(npc);
        }
        for (int i = CastleWarsObjects.getDynamicObjects().size() - 1; i >= 0; i--) {
            DynamicObject object = CastleWarsObjects.getDynamicObjects().get(i);
            if (object != null) {
                GameObject obj = object.getGameObject();
                if (obj == null) {
                    continue;
                }
                if (object.getActionType().getOther() == ActionType.REMOVE) {
                    ObjectManager.removeCustomObject(obj.getLocation(), obj.getType());
                } else {
                    ObjectManager.addCustomObject(obj);
                }
            }
        }
        CastleWarsObjects.getDynamicObjects().clear();
    }

    /**
     * Updates the castlewars gamescreen.
     *
     * @param player The player.
     * @param team   The team the player is in.
     */
    private void updateCastleWarsScreen(Player player, TeamManager team) {
        int config = team.getDoorHealth();
        config += !team.isLockedDoor() ? 128 : 0;
        config += team.isRockCleared() ? 256 : 0;
        config += team.isRockCleared2() ? 512 : 0;
        config += !team.isCatapultActive() ? 1024 : 0;
        config += team.getFlagState().getValue() * 2097152;
        config += 16777216 * team.getPoints();
        ActionSender.sendConfig(player, team.getTeamType() == TeamType.ZAMORAK ? 377 : 378, config);
        config = team.getTeamType().getOppositeTeam().getDoorHealth();
        config += !team.getTeamType().getOppositeTeam().isLockedDoor() ? 128 : 0;
        config += team.getTeamType().getOppositeTeam().isRockCleared() ? 256 : 0;
        config += team.getTeamType().getOppositeTeam().isRockCleared2() ? 512 : 0;
        config += !team.getTeamType().getOppositeTeam().isCatapultActive() ? 1024 : 0;
        config += team.getTeamType().getOppositeTeam().getFlagState().getValue() * 2097152;
        config += 16777216 * team.getTeamType().getOppositeTeam().getPoints();
        ActionSender.sendConfig(player, team.getTeamType() == TeamType.ZAMORAK ? 378 : 377, config);
    }

    /**
     * Updates the waiting rooms.
     */
    private void updateWaitingRooms() {
        if (minutesLeft < 1) {
            if (zamorakTeam.getWaitingPlayers().size() < 1 || saradominTeam.getWaitingPlayers().size() < 1) {
                waitingTicks = 95;
                for (Player player : zamorakTeam.getWaitingPlayers()) {
                    ActionSender.sendConfig(player, 380, 0);
                }
                for (Player player : saradominTeam.getWaitingPlayers()) {
                    ActionSender.sendConfig(player, 380, 0);
                }
                //return;
            }
            waitingTicks = 2500;
            lastMinute = 26;
            for (Player player : zamorakTeam.getWaitingPlayers()) {
                ActionSender.sendOverlay(player, 58);
                player.teleport(2369 + Misc.random(6), 3128 + Misc.random(6), 1);
                ActionSender.sendPlayerOption(player, "Attack", 1, true);
                player.getInventory().addItem(590, 1);
            }
            for (Player player : saradominTeam.getWaitingPlayers()) {
                ActionSender.sendOverlay(player, 58);
                player.teleport(2424 + Misc.random(6), 3073 + Misc.random(6), 1);
                ActionSender.sendPlayerOption(player, "Attack", 1, true);
                player.getInventory().addItem(590, 1);
            }
            zamorakTeam.getPlayers().addAll(zamorakTeam.getWaitingPlayers());
            zamorakTeam.getWaitingPlayers().clear();
            saradominTeam.getPlayers().addAll(saradominTeam.getWaitingPlayers());
            saradominTeam.getWaitingPlayers().clear();
            return;
        }
        if (minutesLeft < lastMinute) {
            lastMinute = minutesLeft;
            for (Player player : zamorakTeam.getWaitingPlayers()) {
                ActionSender.sendConfig(player, 380, minutesLeft);
            }
            for (Player player : saradominTeam.getWaitingPlayers()) {
                ActionSender.sendConfig(player, 380, minutesLeft);
            }
        }
    }

    @Override
    public boolean endSession() {
        /*
           * This method should never get called.
           */
        throw new IllegalStateException("Castle wars activity shut down.");
    }

    /**
     * Registers a player.
     *
     * @param player   The player.
     * @param objectId The object id (used for determining which team the player wants to go in).
     */
    public synchronized void register(Player player, int objectId) {
    	if (player.hasTick("teleport_tick")) {
            return;
        }
    	player.getActionManager().stopAction();
        if (player.getEquipment().get(Equipment.SLOT_HAT) != null || player.getEquipment().get(Equipment.SLOT_CAPE) != null) {
            player.sendMessage("Please remove your helmet and cape before entering the minigame.");
            return;
        }
        if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4037 || player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4039
                || player.getInventory().contains(4037) || player.getInventory().contains(4039)) {
            player.sendMessage("You can't take your flag in this activity.");
            return;
        }
        for (Item item : player.getInventory().getContainer().toArray()) {
            String name = null;
            if (item == null || (name = item.getDefinition().getName()) == null) {
                continue;
            }
            if (Equipment.getItemType(item.getId()) == -1 && !name.endsWith(" rune")
                    && !name.contains("potion") && !name.contains("aradomin brew")
                    && !name.startsWith("Super ") && !name.contains("Overload")
                    && !name.startsWith("Extreme ") && !name.equals("Tinderbox")) {
                player.sendMessage("You can't take non-combat items in this activity.");
                return;
            }
        }
        if (objectId == 4388) {
            if (zamorakTeam.getWaitingPlayers().size() > saradominTeam.getWaitingPlayers().size()) {
                player.sendMessage("This team is already strong enough, join the Saradomin team instead.");
                return;
            }
            player.getEquipment().set(Equipment.SLOT_CAPE, new Item(4042, 1));
            player.teleport(2420 + Misc.random(4), 9517 + Misc.random(8), 0);
            zamorakTeam.getWaitingPlayers().add(player);
        } else if (objectId == 4387) {
            if (zamorakTeam.getWaitingPlayers().size() < saradominTeam.getWaitingPlayers().size()) {
                player.sendMessage("This team is already strong enough, join the Zamorak team instead.");
                return;
            }
            player.getEquipment().set(Equipment.SLOT_CAPE, new Item(4041, 1));
            player.teleport(2376 + Misc.random(6), 9485 + Misc.random(4), 0);
            saradominTeam.getWaitingPlayers().add(player);
        } else if (objectId == 4408) {
            if (zamorakTeam.getWaitingPlayers().size() > saradominTeam.getWaitingPlayers().size()) {
                player.getEquipment().set(Equipment.SLOT_CAPE, new Item(4041, 1));
                player.teleport(2376 + Misc.random(6), 9485 + Misc.random(4), 0);
                saradominTeam.getWaitingPlayers().add(player);
            } else {
                player.getEquipment().set(Equipment.SLOT_CAPE, new Item(4042, 1));
                player.teleport(2420 + Misc.random(4), 9517 + Misc.random(8), 0);
                zamorakTeam.getWaitingPlayers().add(player);
            }
        }
        ActionSender.sendConfig(player, 380, lastMinute);
        ActionSender.sendOverlay(player, 57);
        player.setActivity(this);
    }

    /**
     * Removes a player from the activitiy.
     *
     * @param player   The player to remove.
     * @param objectId The object id (used for determining which team the player is in).
     */
    public void unregister(Player player, int objectId) {
        if (objectId == 4406) {
            getSaradominTeam().getPlayers().remove(player);
        } else if (objectId == 4407) {
            getZamorakTeam().getPlayers().remove(player);
        } else if (objectId == 4390) {
            getZamorakTeam().getWaitingPlayers().remove(player);
        } else if (objectId == 4389) {
            getSaradominTeam().getWaitingPlayers().remove(player);
        }
        if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4037 || player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4039) {
            CastleWarsObjects.createDroppedFlag(player);
        }
        removeItems(player);
        player.teleport(2440 + Misc.random(4), 3083 + Misc.random(12), 0);
        ActionSender.sendCloseOverlay(player);
        player.setActivity(Mob.DEFAULT_ACTIVITY);
    }

    /**
     * Removes the player's castle wars related items.
     *
     * @param player The player.
     */
    public void removeItems(Player player) {
        ActionSender.sendPlayerOption(player, "null", 1, true);
        player.getEquipment().set(Equipment.SLOT_CAPE, null);
        player.getInventory().deleteItem(954, 28);
        player.getInventory().deleteItem(4053, 28);
        player.getInventory().deleteItem(4051, 28);
        player.getInventory().deleteItem(4045, 28);
        player.getInventory().deleteItem(4043, 28);
        player.getInventory().deleteItem(18704, 28);
        player.getInventory().deleteItem(4049, 28);
    }

    @Override
    public boolean objectAction(Player player, GameObject object, ClickOption actionId) {
        return CastleWarsObjects.handleObject(player, object, actionId, zamorakTeam.getPlayers().contains(player) ?
                zamorakTeam : saradominTeam);
    }

    @Override
    public boolean itemAction(Player player, Item item, int actionId, String action, Object... params) {
        TeamManager team = zamorakTeam.getPlayers().contains(player) ? zamorakTeam : saradominTeam;
        if (action.equals("ItemOption") && actionId == 1) {
            if (item.getId() == 4053) {
                if (World.getWorld().getAreaManager().getAreaByName("NorthRocks").contains(player.getLocation())
                        || World.getWorld().getAreaManager().getAreaByName("WestRocks").contains(player.getLocation())
                        || World.getWorld().getAreaManager().getAreaByName("EastRocks").contains(player.getLocation())
                        || World.getWorld().getAreaManager().getAreaByName("SouthRocks").contains(player.getLocation())) {
                    player.sendMessage("You can't setup a barricade here.");
                    return true;
                }
                player.animate(Animation.create(9648));
                return team.addBarricade(player);
            } else if (item.getId() == 4049) {
                if (!player.getInventory().removeItems(item)) {
                    return true;
                }
                player.getSkills().heal(player.getSkills().getMaxHitpoints() / 10);
                player.getPoisonManager().removePoison();
                int energy = player.getWalkingQueue().getRunEnergy() + 30;
                if (energy > 100) {
                    energy = 100;
                }
                player.getWalkingQueue().setRunEnergy(energy);
                return true;
            }
        } else if (action.equals("ItemOnObject")) {
            GameObject object = (GameObject) params[0];
            return CastleWarsItemActions.handleItemObjectInteraction(player, item, object, team);
        } else if (action.equals("ItemOnNPC")) {
            NPC npc = (NPC) params[0];
            return CastleWarsItemActions.handleItemNPCInteraction(player, item, npc, team);
        }
        return false;
    }

    @Override
    public boolean onDeath(Player player) {
        if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4037 || player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4039) {
            CastleWarsObjects.createDroppedFlag(player);
        }
        if (zamorakTeam.getPlayers().contains(player)) {
            player.teleport(2369 + Misc.random(6), 3128 + Misc.random(6), 1);
        } else if (saradominTeam.getPlayers().contains(player)) {
            player.teleport(2424 + Misc.random(6), 3073 + Misc.random(6), 1);
        }
        return true;
    }

    @Override
    public boolean isCombatActivity(Mob mob, Mob victim) {
        if (mob.isNPC() || victim.isNPC()) {
            return true;
        }
        if (!zamorakTeam.getPlayers().contains(victim.getPlayer())
                && !saradominTeam.getPlayers().contains(victim.getPlayer())) {
            return false;
        }
        if (zamorakTeam.getPlayers().contains(mob.getPlayer()) && zamorakTeam.getPlayers().contains(victim.getPlayer())) {
            return false;
        }
        if (saradominTeam.getPlayers().contains(mob.getPlayer()) && saradominTeam.getPlayers().contains(victim.getPlayer())) {
            return false;
        }
        return zamorakTeam.getPlayers().contains(mob.getPlayer()) || saradominTeam.getPlayers().contains(mob.getPlayer());
    }

    @Override
    public boolean onTeleport(Player player) {
        player.sendMessage("You can't teleport out of this activity.");
        return false;
    }

    @Override
    public boolean canLogout(Player player, boolean logoutButton) {
        if (logoutButton) {
            return true;
        }
        if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4039 || player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4037) {
            CastleWarsObjects.createDroppedFlag(player);
        }
        TeamManager team = zamorakTeam.getPlayers().contains(player) ? zamorakTeam :
                saradominTeam.getPlayers().contains(player) ? saradominTeam : null;
        if (team != null) {
            team.getPlayers().remove(player);
            team.getDisconnectedPlayers().add(player.getUsername());
            return true;
        }
        removeItems(player);
        player.teleport(2440 + Misc.random(4), 3083 + Misc.random(12), 0);
        zamorakTeam.getWaitingPlayers().remove(player);
        saradominTeam.getWaitingPlayers().remove(player);
        return true;
    }

    /**
     * @return the singleton
     */
    public static CastleWarsActivity getSingleton() {
        return SINGLETON;
    }

    /**
     * Gets the zamorakian team.
     *
     * @return The zamorak team.
     */
    public TeamManager getZamorakTeam() {
        return zamorakTeam;
    }

    /**
     * Gets the saradomin team.
     *
     * @return The saradomin team.
     */
    public TeamManager getSaradominTeam() {
        return saradominTeam;
    }

}
