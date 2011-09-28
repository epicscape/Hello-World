package org.dementhium.content.activity.impl.castlewars;

import org.dementhium.content.activity.impl.CastleWarsActivity;
import org.dementhium.content.activity.impl.castlewars.DynamicObject.ActionType;
import org.dementhium.content.activity.impl.castlewars.TeamManager.FlagState;
import org.dementhium.content.areas.Area;
import org.dementhium.event.EventListener.ClickOption;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.map.ObjectManager;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.misc.IconManager;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.tickable.Tick;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Handles all castlewars related objects (I fucking hate hardcoding, but meh...).
 *
 * @author Emperor
 */
public class CastleWarsObjects {

    /**
     * The random instance used.
     */
    private static final Random RANDOM = new Random();

    /**
     * A list of dynamic objects.
     */
    private static List<DynamicObject> dynamicObjects = new ArrayList<DynamicObject>();

    /**
     * Handles an object option.
     *
     * @param player The player.
     * @param object The object.
     * @param option The option.
     * @param team   The team the player is in.
     * @return {@code True} if the object action got handled, {@code false} if not.
     */
    public static boolean handleObject(Player player, GameObject object, ClickOption option, TeamManager team) {
        if (handleEnergyBarrier(player, object, team)) {
            return true;
        } else if (handleLadder(player, object, team)) {
            return true;
        } else if (handleTrapdoor(player, object, team)) {
            return true;
        } else if (handleDoor(player, object, team, option)) {
            return true;
        } else if (handleSupplyTable(player, object, team, option)) {
            return true;
        } else if (handleSteppingStone(player, object)) {
            return true;
        } else if (handleFlagObject(player, object, team)) {
            return true;
        } else if (object.getId() == 4448 || object.getId() == 4437 || object.getId() == 4438) {
            Area area = World.getWorld().getAreaManager().getAreaByName("NorthRocks");
            if (player.getLocation().getX() < 2394) {
                area = World.getWorld().getAreaManager().getAreaByName("WestRocks");
            } else if (player.getLocation().getX() > 2407) {
                area = World.getWorld().getAreaManager().getAreaByName("EastRocks");
            } else if (player.getLocation().getY() < 9497) {
                area = World.getWorld().getAreaManager().getAreaByName("SouthRocks");
            }
            Location l = Location.locate(area.swX, area.swY, 0);
            if (object.getId() == 4448 && (l.hasObjects() && (l.getGameObject(4437) != null || l.getGameObject(4438) != null))) {
                player.sendMessage("These rocks have already collapsed.");
                return true;
            }
            player.registerAction(new RockMiningAction(object, l, area));
            return true;
        }
        return false;
    }

    /**
     * Handles a trapdoor.
     *
     * @param player The player.
     * @param object The object.
     * @param team   The team manager.
     * @return {@code True} if the object was a trapdoor, {@code false} if not.
     */
    private static boolean handleTrapdoor(Player player, GameObject object, TeamManager team) {
        if (object.getId() == 4472) {
            if (team.getTeamType() != TeamType.ZAMORAK) {
                player.sendMessage("Only players from the zamorakian team can climb down this trapdoor.");
                return true;
            }
            player.teleport(player.getLocation().transform(0, 0, -1));
            return true;
        } else if (object.getId() == 4471) {
            if (team.getTeamType() != TeamType.SARADOMIN) {
                player.sendMessage("Only players from the saradomin team can climb down this trapdoor.");
                return true;
            }
            player.teleport(player.getLocation().transform(0, 0, -1));
            return true;
        }
        return false;
    }

    /**
     * Handles the opening of a door.
     *
     * @param player The player.
     * @param object The object.
     * @param team   The team manager.
     * @param option The option.
     * @return {@code True} if the object was a door, {@code false} if not.
     */
    private static boolean handleDoor(Player player, GameObject object, TeamManager team, ClickOption option) {
        if (object.getId() == 4467) {
            if (team.getTeamType() == TeamType.ZAMORAK) {
                team.setLockedDoor(false);
            } else if (RANDOM.nextInt(120) < player.getSkills().getLevel(Skills.THIEVING)) {
                team.getTeamType().getOppositeTeam().setLockedDoor(false);
                player.sendMessage("You succesfully pick the lock.");
            } else {
                player.sendMessage("You fail to pick the lock.");
                return true;
            }
            removeObject(object.getLocation().getX(), object.getLocation().getY(), 0, object.getType());
            addObject(new GameObject(4468, object.getLocation().transform(-1, 0, 0), 0, 1));
            return true;
        } else if (object.getId() == 4468) {
            if (team.getTeamType() == TeamType.ZAMORAK) {
                team.setLockedDoor(true);
            } else {
                team.getTeamType().getOppositeTeam().setLockedDoor(true);
            }
            removeObject(object.getLocation().getX(), object.getLocation().getY(), 0, object.getType());
            addObject(new GameObject(4467, object.getLocation().transform(1, 0, 0), 0, 0));
            return true;
        } else if (object.getId() == 4465) {
            if (team.getTeamType() == TeamType.SARADOMIN) {
                team.setLockedDoor(false);
            } else if (RANDOM.nextInt(120) < player.getSkills().getLevel(Skills.THIEVING)) {
                team.getTeamType().getOppositeTeam().setLockedDoor(false);
                player.sendMessage("You succesfully pick the lock.");
            } else {
                player.sendMessage("You fail to pick the lock.");
                return true;
            }
            removeObject(object.getLocation().getX(), object.getLocation().getY(), 0, object.getType());
            addObject(new GameObject(4466, object.getLocation().transform(0, 0, 0), 0, 1));
            return true;
        } else if (object.getId() == 4466) {
            if (team.getTeamType() == TeamType.SARADOMIN) {
                team.setLockedDoor(true);
            } else {
                team.getTeamType().getOppositeTeam().setLockedDoor(true);
            }
            removeObject(object.getLocation().getX(), object.getLocation().getY(), 0, object.getType());
            addObject(new GameObject(4465, object.getLocation().transform(0, 0, 0), 0, 0));
            return true;
        }
        return handleGates(player, object, team, option);
    }

    /**
     * Handles one of the large gates.
     *
     * @param player The player.
     * @param object The object.
     * @param team   The team manager.
     * @param option The option clicked.
     * @return {@code True} if the object clicked was a large gate, {@code false} if not.
     */
    private static boolean handleGates(final Player player, final GameObject object, final TeamManager team, ClickOption option) {
        if (object.getId() == 4423 || object.getId() == 4424) {
            if (option == ClickOption.FIRST) {
                if (player.getLocation().getY() > 3088 && team.getTeamType() == TeamType.ZAMORAK && team.getTeamType().getOppositeTeam().getDoorHealth() > 0) {
                    player.sendMessage("The doors won't open, they're too strong.");
                    return true;
                }
                removeObject(2426, 3089, 0, object.getType());
                removeObject(2427, 3089, 0, object.getType());
                addObject(new GameObject(4425, 2427, 3088, 0, 0, 2));
                addObject(new GameObject(4426, 2426, 3088, 0, 0, 0));
            } else if (team.getTeamType() == TeamType.ZAMORAK) {
                player.animate(Animation.create(9649));
                player.submitTick("attack_cw_door", new Tick(5) {
                    @Override
                    public void execute() {
                        player.animate(Animation.create(9649));
                        int health = team.getTeamType().getOppositeTeam().getDoorHealth() - (RANDOM.nextInt(30));
                        if (health < 1) {
                            health = 0;
                            stop();
                            removeObject(2426, 3089, 0, object.getType());
                            removeObject(2427, 3089, 0, object.getType());
                            addObject(new GameObject(4432, 2426, 3088, 0, 0, 0));
                            addObject(new GameObject(4431, 2427, 3088, 0, 0, 2));
                        }
                        team.getTeamType().getOppositeTeam().setDoorHealth(health);
                    }
                });
            } else {
                player.sendMessage("You don't want to damage your own doors.");
            }
            return true;
        } else if (object.getId() == 4425 || object.getId() == 4426) {
            removeObject(2426, 3088, 0, object.getType());
            removeObject(2427, 3088, 0, object.getType());
            addObject(new GameObject(4424, 2426, 3088, 0, 0, 1));
            addObject(new GameObject(4423, 2427, 3088, 0, 0, 1));
            return true;
        } else if (object.getId() == 4431 || object.getId() == 4432) {
            if (player.getAttribute("repairDelay", 0) > World.getTicks()) {
                return true;
            }
            player.setAttribute("repairDelay", World.getTicks() + 5);
            if (!player.getInventory().contains(4051)) {
                player.sendMessage("You need a toolbox to repair these doors.");
                return true;
            }
            int repaired = 30;
            int currentHealth = CastleWarsActivity.getSingleton().getSaradominTeam().getDoorHealth();
            if (repaired > (100 - currentHealth)) {
                repaired = 100 - CastleWarsActivity.getSingleton().getSaradominTeam().getDoorHealth();
            }
            player.sendMessage("You repair " + repaired + "% of the door.");
            CastleWarsActivity.getSingleton().getSaradominTeam().setDoorHealth(currentHealth + repaired);
            player.animate(Animation.create(10102));
            if (repaired < 30) {
                removeObject(2426, 3088, 0, object.getType());
                removeObject(2427, 3088, 0, object.getType());
                addObject(new GameObject(4425, 2427, 3088, 0, 0, 2));
                addObject(new GameObject(4426, 2426, 3088, 0, 0, 0));
            }
            return true;
        } else if (object.getId() == 4427 || object.getId() == 4428) {
            if (option == ClickOption.FIRST) {
                if (player.getLocation().getY() < 3119 && team.getTeamType() == TeamType.SARADOMIN && team.getTeamType().getOppositeTeam().getDoorHealth() > 0) {
                    player.sendMessage("The doors won't open, they're too strong.");
                    return true;
                }
                removeObject(2373, 3119, 0, object.getType());
                removeObject(2372, 3119, 0, object.getType());
                addObject(new GameObject(4429, 2373, 3119, 0, 0, 2));
                addObject(new GameObject(4430, 2372, 3119, 0, 0, 0));
                return true;
            } else if (team.getTeamType() == TeamType.SARADOMIN) {
                //TODO: Zamorak doors
                player.animate(Animation.create(9649));
                player.submitTick("attack_cw_door", new Tick(5) {
                    @Override
                    public void execute() {
                        player.animate(Animation.create(9649));
                        int health = team.getTeamType().getOppositeTeam().getDoorHealth() - (RANDOM.nextInt(30));
                        if (health < 1) {
                            health = 0;
                            stop();
                            removeObject(2373, 3119, 0, object.getType());
                            removeObject(2372, 3119, 0, object.getType());
                            addObject(new GameObject(4434, 2373, 3119, 0, 0, 2));
                            addObject(new GameObject(4433, 2372, 3119, 0, 0, 0));
                        }
                        team.getTeamType().getOppositeTeam().setDoorHealth(health);
                    }
                });
            } else {
                player.sendMessage("You don't want to damage your own doors.");
            }
            return true;
        } else if (object.getId() == 4429 || object.getId() == 4430) {
            removeObject(2373, 3119, 0, object.getType());
            removeObject(2372, 3119, 0, object.getType());
            addObject(new GameObject(4428, 2373, 3119, 0, 0, 3));
            addObject(new GameObject(4427, 2372, 3119, 0, 0, 3));
            return true;
        } else if (object.getId() == 4433 || object.getId() == 4434) {
            if (player.getAttribute("repairDelay", 0) > World.getTicks()) {
                return true;
            }
            player.setAttribute("repairDelay", World.getTicks() + 5);
            if (!player.getInventory().contains(4051)) {
                player.sendMessage("You need a toolbox to repair these doors.");
                return true;
            }
            int repaired = 30;
            int currentHealth = CastleWarsActivity.getSingleton().getZamorakTeam().getDoorHealth();
            if (repaired > (100 - currentHealth)) {
                repaired = 100 - CastleWarsActivity.getSingleton().getZamorakTeam().getDoorHealth();
            }
            player.sendMessage("You repair " + repaired + "% of the door.");
            CastleWarsActivity.getSingleton().getZamorakTeam().setDoorHealth(currentHealth + repaired);
            player.animate(Animation.create(10102));
            if (repaired < 30) {
                removeObject(2373, 3119, 0, object.getType());
                removeObject(2372, 3119, 0, object.getType());
                addObject(new GameObject(4429, 2373, 3119, 0, 0, 2));
                addObject(new GameObject(4430, 2372, 3119, 0, 0, 0));
            }
            return true;
        }
        return false;
    }

    /**
     * Handles a flag object.
     *
     * @param player The player.
     * @param object The object.
     * @param team   The team manager.
     * @return {@code True} if the object was a flag, {@code false} if not.
     */
    private static boolean handleFlagObject(Player player, GameObject object, TeamManager team) {
        if (object.getId() == 4902) {
            team.getTeamType().getOppositeTeam().setFlagState(FlagState.SAFE);
            if (team.getTeamType() == TeamType.SARADOMIN) {
                if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4039) {
                    player.getEquipment().set(Equipment.SLOT_WEAPON, null);
                    team.setPoints(team.getPoints() + 1);
                    for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                        IconManager.removeIcon(p, player);
                    }
                    for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                        IconManager.removeIcon(p, player);
                    }
                    team.getTeamType().getOppositeTeam().setFlagState(FlagState.SAFE);
                    removeObject(2370, 3133, 3, object.getType());
                    addObject(new GameObject(4903, 2370, 3133, 3, object.getType(), object.getRotation()));
                    return true;
                }
                player.sendMessage("You can't take your own team's flag.");
                return true;
            } else if (team.getTeamType().getOppositeTeam().getFlagState() != FlagState.SAFE) {
                player.sendMessage("The flag has already been taken.");
                return true;
            }
            Item item = player.getEquipment().get(Equipment.SLOT_WEAPON);
            if (item != null) {
                player.getEquipment().unEquip(player, item.getId(), -1, Equipment.SLOT_WEAPON);
            }
            item = player.getEquipment().get(Equipment.SLOT_SHIELD);
            if (item != null) {
                player.getEquipment().unEquip(player, item.getId(), -1, Equipment.SLOT_SHIELD);
            }
            if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null || player.getEquipment().get(Equipment.SLOT_SHIELD) != null) {
                return true;
            }
            team.getTeamType().getOppositeTeam().setFlagState(FlagState.TAKEN);
            removeObject(object.getLocation().getX(), object.getLocation().getY(), 3, object.getType());
            addObject(new GameObject(4377, object.getLocation(), object.getType(), object.getRotation()));
            player.getEquipment().set(Equipment.SLOT_WEAPON, new Item(4037, 1));
            for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                IconManager.iconOnMob(p, player, 1, 65535);
            }
            for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                IconManager.iconOnMob(p, player, 1, 65535);
            }
        } else if (object.getId() == 4377) {
            if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4037) {
                player.getEquipment().set(Equipment.SLOT_WEAPON, null);
                removeObject(object.getLocation().getX(), object.getLocation().getY(), 3, object.getType());
                addObject(new GameObject(4902, object.getLocation(), object.getType(), object.getRotation()));
                for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                    IconManager.removeIcon(p, player);
                }
                for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                    IconManager.removeIcon(p, player);
                }
            } else if (team.getTeamType() == TeamType.SARADOMIN) {
                if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4039) {
                    player.getEquipment().set(Equipment.SLOT_WEAPON, null);
                    team.setPoints(team.getPoints() + 1);
                    for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                        IconManager.removeIcon(p, player);
                    }
                    for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                        IconManager.removeIcon(p, player);
                    }
                    team.getTeamType().getOppositeTeam().setFlagState(FlagState.SAFE);
                    removeObject(2370, 3133, 3, object.getType());
                    addObject(new GameObject(4903, 2370, 3133, 3, object.getType(), object.getRotation()));
                    return true;
                }
                player.sendMessage("You can't take your own team's flag.");
                return true;
            }
            player.sendMessage("The flag has already been taken.");
            return true;
        } else if (object.getId() == 4900) {
            if (CastleWarsActivity.getSingleton().getSaradominTeam().getFlagState() != FlagState.DROPPED) {
                return true;
            }
            if (team.getTeamType() == TeamType.SARADOMIN &&
                    World.getWorld().getAreaManager().getAreaByName("SaradominBase").contains(player.getLocation())) {
                for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                    IconManager.removeIcon(p, CastleWarsActivity.getSingleton().getSaradominTeam().getFlagNPC());
                }
                for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                    IconManager.removeIcon(p, CastleWarsActivity.getSingleton().getSaradominTeam().getFlagNPC());
                }
                World.getWorld().getNpcs().remove(CastleWarsActivity.getSingleton().getSaradominTeam().getFlagNPC());
                CastleWarsActivity.getSingleton().getSaradominTeam().setFlagNPC(null);
                removeObject(object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getZ(), object.getType());
                team.setFlagState(FlagState.SAFE);
                removeObject(2429, 3074, 3, object.getType());
                addObject(new GameObject(4902, 2429, 3074, 3, object.getType(), object.getRotation()));
                return true;
            }
            Item item = player.getEquipment().get(Equipment.SLOT_WEAPON);
            if (item != null) {
                player.getEquipment().unEquip(player, item.getId(), -1, Equipment.SLOT_WEAPON);
            }
            item = player.getEquipment().get(Equipment.SLOT_SHIELD);
            if (item != null) {
                player.getEquipment().unEquip(player, item.getId(), -1, Equipment.SLOT_SHIELD);
            }
            if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null || player.getEquipment().get(Equipment.SLOT_SHIELD) != null) {
                return true;
            }
            for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                IconManager.removeIcon(p, CastleWarsActivity.getSingleton().getSaradominTeam().getFlagNPC());
                IconManager.iconOnMob(p, player, 1, 65535);
            }
            for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                IconManager.removeIcon(p, CastleWarsActivity.getSingleton().getSaradominTeam().getFlagNPC());
                IconManager.iconOnMob(p, player, 1, 65535);
            }
            World.getWorld().getNpcs().remove(CastleWarsActivity.getSingleton().getSaradominTeam().getFlagNPC());
            CastleWarsActivity.getSingleton().getSaradominTeam().setFlagNPC(null);
            removeObject(object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getZ(), object.getType());
            player.getEquipment().set(Equipment.SLOT_WEAPON, new Item(4037));
            CastleWarsActivity.getSingleton().getSaradominTeam().setFlagState(FlagState.TAKEN);
        } else if (object.getId() == 4903) {
            team.getTeamType().getOppositeTeam().setFlagState(FlagState.SAFE);
            if (team.getTeamType() == TeamType.ZAMORAK) {
                if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4037) {
                    player.getEquipment().set(Equipment.SLOT_WEAPON, null);
                    team.setPoints(team.getPoints() + 1);
                    for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                        IconManager.removeIcon(p, player);
                    }
                    for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                        IconManager.removeIcon(p, player);
                    }
                    team.getTeamType().getOppositeTeam().setFlagState(FlagState.SAFE);
                    removeObject(2429, 3074, 3, object.getType());
                    addObject(new GameObject(4902, 2429, 3074, 3, object.getType(), object.getRotation()));
                    return true;
                }
                player.sendMessage("You can't take your own team's flag.");
                return true;
            } else if (team.getTeamType().getOppositeTeam().getFlagState() != FlagState.SAFE) {
                player.sendMessage("The flag has already been taken.");
                return true;
            }
            Item item = player.getEquipment().get(Equipment.SLOT_WEAPON);
            if (item != null) {
                player.getEquipment().unEquip(player, item.getId(), -1, Equipment.SLOT_WEAPON);
            }
            item = player.getEquipment().get(Equipment.SLOT_SHIELD);
            if (item != null) {
                player.getEquipment().unEquip(player, item.getId(), -1, Equipment.SLOT_SHIELD);
            }
            if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null || player.getEquipment().get(Equipment.SLOT_SHIELD) != null) {
                return true;
            }
            team.getTeamType().getOppositeTeam().setFlagState(FlagState.TAKEN);
            removeObject(object.getLocation().getX(), object.getLocation().getY(), 3, object.getType());
            addObject(new GameObject(4378, object.getLocation(), object.getType(), object.getRotation()));
            player.getEquipment().set(Equipment.SLOT_WEAPON, new Item(4039, 1));
            for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                IconManager.iconOnMob(p, player, 1, 65535);
            }
            for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                IconManager.iconOnMob(p, player, 1, 65535);
            }
        } else if (object.getId() == 4378) {
            if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4039) {
                player.getEquipment().set(Equipment.SLOT_WEAPON, null);
                removeObject(object.getLocation().getX(), object.getLocation().getY(), 3, object.getType());
                addObject(new GameObject(4903, object.getLocation(), object.getType(), object.getRotation()));
                for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                    IconManager.removeIcon(p, player);
                }
                for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                    IconManager.removeIcon(p, player);
                }
            } else if (team.getTeamType() == TeamType.ZAMORAK) {
                if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4037) {
                    player.getEquipment().set(Equipment.SLOT_WEAPON, null);
                    team.setPoints(team.getPoints() + 1);
                    for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                        IconManager.removeIcon(p, player);
                    }
                    for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                        IconManager.removeIcon(p, player);
                    }
                    team.getTeamType().getOppositeTeam().setFlagState(FlagState.SAFE);
                    removeObject(2429, 3074, 3, object.getType());
                    addObject(new GameObject(4902, 2429, 3074, 3, object.getType(), object.getRotation()));
                    return true;
                }
                player.sendMessage("You can't take your own team's flag.");
                return true;
            }
            player.sendMessage("The flag has already been taken.");
            return true;
        } else if (object.getId() == 4901) {
            if (CastleWarsActivity.getSingleton().getZamorakTeam().getFlagState() != FlagState.DROPPED) {
                return true;
            }
            if (team.getTeamType() == TeamType.ZAMORAK &&
                    World.getWorld().getAreaManager().getAreaByName("ZamorakBase").contains(player.getLocation())) {
                for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                    IconManager.removeIcon(p, CastleWarsActivity.getSingleton().getZamorakTeam().getFlagNPC());
                }
                for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                    IconManager.removeIcon(p, CastleWarsActivity.getSingleton().getZamorakTeam().getFlagNPC());
                }
                World.getWorld().getNpcs().remove(CastleWarsActivity.getSingleton().getZamorakTeam().getFlagNPC());
                CastleWarsActivity.getSingleton().getZamorakTeam().setFlagNPC(null);
                removeObject(object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getZ(), object.getType());
                team.setFlagState(FlagState.SAFE);
                removeObject(2370, 3133, 3, object.getType());
                addObject(new GameObject(4903, 2370, 3133, 3, object.getType(), object.getRotation()));
                return true;
            }
            Item item = player.getEquipment().get(Equipment.SLOT_WEAPON);
            if (item != null) {
                player.getEquipment().unEquip(player, item.getId(), -1, Equipment.SLOT_WEAPON);
            }
            item = player.getEquipment().get(Equipment.SLOT_SHIELD);
            if (item != null) {
                player.getEquipment().unEquip(player, item.getId(), -1, Equipment.SLOT_SHIELD);
            }
            if (player.getEquipment().get(Equipment.SLOT_WEAPON) != null || player.getEquipment().get(Equipment.SLOT_SHIELD) != null) {
                return true;
            }
            for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                IconManager.removeIcon(p, CastleWarsActivity.getSingleton().getZamorakTeam().getFlagNPC());
                IconManager.iconOnMob(p, player, 1, 65535);
            }
            for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                IconManager.removeIcon(p, CastleWarsActivity.getSingleton().getZamorakTeam().getFlagNPC());
                IconManager.iconOnMob(p, player, 1, 65535);
            }
            World.getWorld().getNpcs().remove(CastleWarsActivity.getSingleton().getZamorakTeam().getFlagNPC());
            CastleWarsActivity.getSingleton().getZamorakTeam().setFlagNPC(null);
            removeObject(object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getZ(), object.getType());
            player.getEquipment().set(Equipment.SLOT_WEAPON, new Item(4039));
            CastleWarsActivity.getSingleton().getZamorakTeam().setFlagState(FlagState.TAKEN);
        }
        return false;
    }

    /**
     * Handles the taking supplies from a table.
     *
     * @param player The player.
     * @param object The object.
     * @param team   The team manager.
     * @param option The option.
     * @return {@code True} if the object was a supply table, {@code false} if not.
     */
    private static boolean handleSupplyTable(Player player, GameObject object, TeamManager team, ClickOption option) {
        if (object.getId() == 36575 || object.getId() == 36582) {
            player.animate(Animation.create(832));
            player.getInventory().addItem(new Item(4053, option == ClickOption.FIRST ? 1 : 5));
            return true;
        } else if (object.getId() == 36573 || object.getId() == 36580) {
            player.animate(Animation.create(832));
            player.getInventory().addItem(new Item(4051, 1));
            return true;
        } else if (object.getId() == 36576 || object.getId() == 36583) {
            player.animate(Animation.create(832));
            player.getInventory().addItem(new Item(954, option == ClickOption.FIRST ? 1 : 5));
            return true;
        } else if (object.getId() == 36584 || object.getId() == 36577) {
            player.animate(Animation.create(832));
            player.getInventory().addItem(new Item(4045, option == ClickOption.FIRST ? 1 : 5));
            return true;
        } else if (object.getId() == 36585 || object.getId() == 36578) {
            player.animate(Animation.create(832));
            player.getInventory().addItem(new Item(1265, 1));
            return true;
        } else if (object.getId() == 36581 || object.getId() == 36574) {
            player.animate(Animation.create(832));
            player.getInventory().addItem(new Item(4043, option == ClickOption.FIRST ? 1 : 5));
            return true;
        } else if (object.getId() == 2207 || object.getId() == 2203) {
            player.animate(Animation.create(832));
            player.getInventory().addItem(new Item(18704, option == ClickOption.FIRST ? 1 : 5));
            return true;
        } else if (object.getId() == 36586 || object.getId() == 36579) {
            player.animate(Animation.create(832));
            player.getInventory().addItem(new Item(4049, option == ClickOption.FIRST ? 1 : 5));
        }
        return false;
    }

    /**
     * Handles a stepping stone action.
     *
     * @param player The player.
     * @param object The object.
     * @return {@code True} if the object was a stepping stone, {@code false} if not.
     */
    private static boolean handleSteppingStone(Player player, GameObject object) {
        if (object.getId() != 4411) {
            return false;
        }
        int x = object.getLocation().getX();
        int y = object.getLocation().getY();
        int dir = 0;
        if (x > player.getLocation().getX()) {
            dir = 1;
        } else if (y < player.getLocation().getY()) {
            dir = 2;
        } else if (x < player.getLocation().getX()) {
            dir = 3;
        }
        player.forceMovement(Animation.create(741), x, y, 15, 30, dir, 1, true);
        return true;
    }

    /**
     * Handles a ladder.
     *
     * @param player The player.
     * @param object The object.
     * @param team   The team manager.
     * @return {@code True} if the object was a ladder, {@code false} if not.
     */
    private static boolean handleLadder(Player player, GameObject object, TeamManager team) {
        if (object.getId() == 6280 || object.getId() == 6281) {
            player.teleport(player.getLocation().transform(0, 0, 1));
            return true;
        } else if (object.getId() == 4415) {
            if (player.getLocation().getX() == 2373 && player.getLocation().getY() == 3133) {
                player.teleport(2374, 3130, 2);
            } else if (player.getLocation().getX() == 2369 && player.getLocation().getY() == 3127) {
                player.teleport(2372, 3126, 1);
            } else if (player.getLocation().getX() == 2430 && player.getLocation().getY() == 3080) {
                player.teleport(2427, 3081, 1);
            } else if (player.getLocation().getX() == 2426 && player.getLocation().getY() == 3074) {
                player.teleport(2425, 3077, 2);
            } else if (player.getLocation().getX() == 2379 && player.getLocation().getY() == 3127) {
                player.teleport(2380, 3130, 0);
            } else if (player.getLocation().getX() == 2383 && player.getLocation().getY() == 3132) {
                player.teleport(2382, 3129, 0);
            } else if (player.getLocation().getX() == 2416 && player.getLocation().getY() == 3075) {
                player.teleport(2417, 3078, 0);
            } else if (player.getLocation().getX() == 2420 && player.getLocation().getY() == 3080) {
                player.teleport(2419, 3077, 0);
            }
            return true;
        } else if (object.getId() == 36365) {
            if (object.getLocation().getX() <= player.getLocation().getX()) {
                player.teleport(player.getLocation().transform(0, 0, -1));
            }
            return true;
        } else if (object.getId() == 36363) {
            if (object.getLocation().getX() <= player.getLocation().getX()) {
                player.teleport(player.getLocation().transform(0, 0, 1));
            }
            return true;
        } else if (object.getId() == 36349) {
            if (object.getLocation().getX() >= player.getLocation().getX()) {
                player.teleport(player.getLocation().transform(0, 0, -1));
            }
            return true;
        } else if (object.getId() == 36347) {
            if (object.getLocation().getX() >= player.getLocation().getX()) {
                player.teleport(player.getLocation().transform(0, 0, 1));
            }
            return true;
        } else if (object.getId() == 36523) {
            if (player.getLocation().getX() == 2374 && player.getLocation().getY() == 3130) {
                player.teleport(2373, 3133, 3);
            }
            return true;
        } else if (object.getId() == 36521) {
            if (player.getLocation().getX() == 2372 && player.getLocation().getY() == 3126) {
                player.teleport(2369, 3127, 2);
            }
            return true;
        } else if (object.getId() == 36481) {
            if (player.getLocation().getX() == 2417 && player.getLocation().getY() == 3078) {
                player.teleport(2416, 3075, 0);
            }
            return true;
        } else if (object.getId() == 36495) {
            if (player.getLocation().getX() == 2419 && player.getLocation().getY() == 3077) {
                player.teleport(2420, 3080, 1);
            }
            return true;
        } else if (object.getId() == 36480) {
            if (player.getLocation().getX() == 2427 && player.getLocation().getY() == 3081) {
                player.teleport(2430, 3080, 2);
            }
            return true;
        } else if (object.getId() == 36484) {
            if (player.getLocation().getX() == 2425 && player.getLocation().getY() == 3077) {
                player.teleport(2426, 3074, 3);
            }
            return true;
        } else if (object.getId() == 36646) {
            if (player.getLocation().getX() == 2369 && player.getLocation().getY() == 9526) {
                player.teleport(2369, 3126, 0);
            }
            player.setAttribute("freezeTime", 0);
            player.setAttribute("freezeImmunity", 0);
            return true;
        } else if (object.getId() == 36694) {
            if (player.getLocation().getX() == 2369 && player.getLocation().getY() == 3126) {
                player.teleport(2369, 9526, 0);
            }
            player.setAttribute("freezeTime", 0);
            player.setAttribute("freezeImmunity", 0);
            return true;
        } else if (object.getId() == 36645) {
            if (player.getLocation().getX() == 2430 && player.getLocation().getY() == 9481) {
                player.teleport(2430, 3081, 0);
            }
            player.setAttribute("freezeTime", 0);
            player.setAttribute("freezeImmunity", 0);
            return true;
        } else if (object.getId() == 36693) {
            if (player.getLocation().getX() == 2430 && player.getLocation().getY() == 3081) {
                player.teleport(2430, 9481, 0);
            }
            player.setAttribute("freezeTime", 0);
            player.setAttribute("freezeImmunity", 0);
            return true;
        } else if (object.getId() == 36644) {
            if (player.getLocation().getX() == 2400 && player.getLocation().getY() > 9506) {
                player.teleport(2400, 3107 + (player.getLocation().getY() - 9507), 0);
            } else if (player.getLocation().getX() == 2399 && player.getLocation().getY() < 9501) {
                player.teleport(2399, 3100 + (player.getLocation().getY() - 9500), 0);
            }
            player.setAttribute("freezeTime", 0);
            player.setAttribute("freezeImmunity", 0);
            return true;
        } else if (object.getId() == 36691) {
            if (player.getLocation().getX() == 2400 && player.getLocation().getY() > 3106) {
                player.teleport(2400, player.getLocation().getY() + 6400, 0);
            } else if (player.getLocation().getX() == 2399 && player.getLocation().getY() < 3101) {
                player.teleport(2399, player.getLocation().getY() + 6400, 0);
            }
            player.setAttribute("freezeTime", 0);
            player.setAttribute("freezeImmunity", 0);
            return true;
        } else if (object.getId() == 36532) {
            if (player.getLocation().getX() == 2380 && player.getLocation().getY() == 3130) {
                player.teleport(2379, 3127, 1);
            }
            return true;
        } else if (object.getId() == 36540) {
            if (player.getLocation().getX() == 2382 && player.getLocation().getY() == 3129) {
                player.teleport(2383, 3132, 0);
            }
            return true;
        } else if (object.getId() == 36312) {
            int x = object.getRotation() == 0 ? -1 : object.getRotation() == 2 ? 1 : 0;
            int y = object.getRotation() == 3 ? -1 : object.getRotation() == 1 ? 1 : 0;
            if (object.getRotation() == 0 && x < 0 && player.getLocation().getX() < object.getLocation().getX()
                    || object.getRotation() == 2 && x > 0 && player.getLocation().getX() > object.getLocation().getX()) {
                x *= -1;
            }
            if (object.getRotation() == 3 && y < 0 && player.getLocation().getY() < object.getLocation().getY()
                    || object.getRotation() == 1 && y > 0 && player.getLocation().getY() > object.getLocation().getY()) {
                y *= -1;
            }
            player.teleport(player.getLocation().transform(x, y, 0));
        }
        return false;
    }

    /**
     * Handles an energy barrier in the prepare rooms.
     *
     * @param player The player.
     * @param object The object.
     * @param team   The team manager.
     * @return {@code True} if the object was an energy barrier, {@code false} if not.
     */
    private static boolean handleEnergyBarrier(Player player, GameObject object, TeamManager team) {
        if (object.getId() == 4470) {
            if (team.getTeamType() != TeamType.ZAMORAK) {
                player.sendMessage("A magical force stops you from entering.");
                return true;
            }
            if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4037 || player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4039) {
                createDroppedFlag(player);
            }
            player.getMask().setFacePosition(object.getLocation(), 1, 1);
            if (object.getRotation() == 1 || object.getRotation() == 3) {
                if (object.getLocation().getY() > player.getLocation().getY()) {
                    player.teleport(object.getLocation().getX(), object.getLocation().getY(), 1);
                } else {
                    player.teleport(object.getLocation().getX(), object.getLocation().getY() - 1, 1);
                }
                return true;
            }
            if (object.getLocation().getX() >= player.getLocation().getX()) {
                player.teleport(object.getLocation().getX() + 1, object.getLocation().getY(), 1);
            } else {
                player.teleport(object.getLocation().getX(), object.getLocation().getY(), 1);
            }
            return true;
        } else if (object.getId() == 4469) {
            if (team.getTeamType() != TeamType.SARADOMIN) {
                player.sendMessage("A magical force stops you from entering.");
                return true;
            }
            if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4037 || player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4039) {
                createDroppedFlag(player);
            }
            player.getMask().setFacePosition(object.getLocation(), 1, 1);
            if (object.getRotation() == 1 || object.getRotation() == 3) {
                if (object.getLocation().getY() >= player.getLocation().getY()) {
                    player.teleport(object.getLocation().getX(), object.getLocation().getY() + 1, 1);
                } else {
                    player.teleport(object.getLocation().getX(), object.getLocation().getY(), 1);
                }
                return true;
            }
            if (object.getLocation().getX() > player.getLocation().getX()) {
                player.teleport(object.getLocation().getX(), object.getLocation().getY(), 1);
            } else {
                player.teleport(object.getLocation().getX() - 1, object.getLocation().getY(), 1);
            }
            return true;
        } else if (object.getId() == 4390 || object.getId() == 4389 || object.getId() == 4407 || object.getId() == 4406) {
            CastleWarsActivity.getSingleton().unregister(player, object.getId());
        }
        return false;
    }

    public static void createDroppedFlag(Player player) {
        NPC test = null;
        if (player.getEquipment().getSlot(Equipment.SLOT_WEAPON) == 4037) {
            if (CastleWarsActivity.getSingleton().getSaradominTeam().getFlagNPC() == null) {
                test = World.getWorld().register(13487, player.getLocation());
            } else {
                test = CastleWarsActivity.getSingleton().getSaradominTeam().getFlagNPC();
                test.teleport(player.getLocation());
            }
            addObject(new GameObject(4900, player.getLocation(), 10, 0));
            CastleWarsActivity.getSingleton().getSaradominTeam().setFlagState(FlagState.DROPPED);
            CastleWarsActivity.getSingleton().getSaradominTeam().setFlagNPC(test);
        } else {
            if (CastleWarsActivity.getSingleton().getZamorakTeam().getFlagNPC() == null) {
                test = World.getWorld().register(13487, player.getLocation());
            } else {
                test = CastleWarsActivity.getSingleton().getZamorakTeam().getFlagNPC();
                test.teleport(player.getLocation());
            }
            addObject(new GameObject(4901, player.getLocation(), 10, 0));
            CastleWarsActivity.getSingleton().getZamorakTeam().setFlagState(FlagState.DROPPED);
            CastleWarsActivity.getSingleton().getZamorakTeam().setFlagNPC(test);
        }
        player.getEquipment().set(Equipment.SLOT_WEAPON, null);
        for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
            IconManager.removeIcon(p, player);
            IconManager.iconOnMob(p, test, 1, 65535);
        }
        for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
            IconManager.removeIcon(p, player);
            IconManager.iconOnMob(p, test, 1, 65535);
        }
    }

    synchronized static void addObject(GameObject gameObject) {
        dynamicObjects.add(new DynamicObject(ObjectManager.addCustomObject(gameObject), ActionType.ADD));
    }


    public synchronized static void removeObject(Location l, int type) {
        removeObject(l.getX(), l.getY(), l.getZ(), type);
    }

    private synchronized static void removeObject(int x, int y, int z, int type) {
        dynamicObjects.add(new DynamicObject(ObjectManager.removeCustomObject(x, y, z, type, true), ActionType.REMOVE));
    }

    /**
     * @return the addedObjects
     */
    public static List<DynamicObject> getDynamicObjects() {
        return dynamicObjects;
    }
}
