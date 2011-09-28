package org.dementhium.content.activity.impl.castlewars;

import org.dementhium.content.activity.impl.CastleWarsActivity;
import org.dementhium.content.areas.Area;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * Handles the castle wars item-related actions.
 *
 * @author Emperor
 */
public final class CastleWarsItemActions {

    /**
     * Handles an item on object interaction.
     *
     * @param player The player.
     * @param item   The item.
     * @param object The game object.
     * @param team   The team the player is in.
     * @return {@code True} if the action was castle wars-related, {@code false} if not.
     */
    public static boolean handleItemObjectInteraction(Player player, Item item, GameObject object, TeamManager team) {
        if (item.getId() == 4045) {
            if (object.getId() == 4448 || object.getId() == 4437 || object.getId() == 4438) {
                Area area = World.getWorld().getAreaManager().getAreaByName("NorthRocks");
                if (player.getLocation().getX() < 2394) {
                    area = World.getWorld().getAreaManager().getAreaByName("WestRocks");
                } else if (player.getLocation().getX() > 2407) {
                    area = World.getWorld().getAreaManager().getAreaByName("EastRocks");
                } else if (player.getLocation().getY() < 9497) {
                    area = World.getWorld().getAreaManager().getAreaByName("SouthRocks");
                }
                Location l = Location.locate(area.swX, area.swY, 0);
                if (object.getId() == 4448) {
                    if (l.hasObjects() && (l.getGameObject(4437) != null || l.getGameObject(4438) != null)) {
                        player.sendMessage("These rocks have already collapsed.");
                        return true;
                    }
                    player.getInventory().removeItems(item);
                    player.sendMessage("You've collapsed the tunnel!");
                    for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                        if (area.contains(p.getLocation())) {
                            p.getWalkingQueue().reset();
                            p.getDamageManager().miscDamage(p.getSkills().getHitPoints(), DamageType.RED_DAMAGE);
                        }
                        ActionSender.sendPositionedGraphic(p, player.getLocation(), 2739);
                    }
                    for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                        if (area.contains(p.getLocation())) {
                            p.getWalkingQueue().reset();
                            p.getDamageManager().miscDamage(p.getSkills().getHitPoints(), DamageType.RED_DAMAGE);
                        }
                        ActionSender.sendPositionedGraphic(p, player.getLocation(), 2739);
                    }
                    int rotation = (area.getName().equals("WestRocks") || area.getName().equals("EastRocks")) ? 1 : 0;
                    CastleWarsObjects.addObject(new GameObject(4437, l, 10, rotation));
                    if (area.getName().equals("NorthRocks")) {
                        CastleWarsActivity.getSingleton().getZamorakTeam().setRockCleared(false);
                    } else if (area.getName().equals("WestRocks")) {
                        CastleWarsActivity.getSingleton().getZamorakTeam().setRockCleared2(false);
                    } else if (area.getName().equals("SouthRocks")) {
                        CastleWarsActivity.getSingleton().getSaradominTeam().setRockCleared(false);
                    } else if (area.getName().equals("EastRocks")) {
                        CastleWarsActivity.getSingleton().getSaradominTeam().setRockCleared2(false);
                    }
                } else {
                    player.getInventory().removeItems(item);
                    for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                        ActionSender.sendPositionedGraphic(p, object.getLocation(), 2739);
                    }
                    for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                        ActionSender.sendPositionedGraphic(p, object.getLocation(), 2739);
                    }
                    CastleWarsObjects.removeObject(l, object.getType());
                    if (object.getId() == 4437) {
                        CastleWarsObjects.addObject(new GameObject(4438, object.getLocation(), object.getType(), object.getRotation()));
                        return true;
                    }
                    if (area.getName().equals("NorthRocks")) {
                        CastleWarsActivity.getSingleton().getZamorakTeam().setRockCleared(true);
                    } else if (area.getName().equals("WestRocks")) {
                        CastleWarsActivity.getSingleton().getZamorakTeam().setRockCleared2(true);
                    } else if (area.getName().equals("SouthRocks")) {
                        CastleWarsActivity.getSingleton().getSaradominTeam().setRockCleared(true);
                    } else if (area.getName().equals("EastRocks")) {
                        CastleWarsActivity.getSingleton().getSaradominTeam().setRockCleared2(true);
                    }
                }
                return true;
            }
        } else if (item.getId() == 954) {
            System.out.println(object.getId() + ", " + object.getType());
            if (object.getId() == 4446 || object.getId() == 4447) {
                if (!player.getInventory().removeItems(item)) {
                    return true;
                }
                CastleWarsObjects.removeObject(object.getLocation(), object.getType());
                GameObject climbingRope = new GameObject(36312, object.getLocation(), 4, object.getRotation());
                CastleWarsObjects.addObject(climbingRope);
                CastleWarsObjects.addObject(new GameObject(object.getId() == 4446 ? 36313 : 36314,
                        object.getLocation(), object.getType(), object.getRotation()));
                object.getLocation().addObject(climbingRope);
                return true;
            }
        }
        return false;
    }

    /**
     * Handles an item on NPC action.
     *
     * @param player The player.
     * @param item   The item.
     * @param npc    The npc.
     * @param team   The team manager.
     * @return {@code True} if the action was castle wars-related, {@code false} if not.
     */
    public static boolean handleItemNPCInteraction(Player player, Item item, final NPC npc, TeamManager team) {
        if (item.getId() == 4045) {
            if (npc.getId() == 1532) {
                player.setAttribute("instantNPCAction", 1532);
                for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                    ActionSender.sendPositionedGraphic(p, npc.getLocation(), 2739);
                }
                for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                    ActionSender.sendPositionedGraphic(p, npc.getLocation(), 2739);
                }
                player.getInventory().removeItems(item);
                CastleWarsActivity.getSingleton().getSaradominTeam().removeBarricade(npc);
            } else if (npc.getId() == 1534) {
                player.setAttribute("instantNPCAction", 1534);
                for (Player p : CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()) {
                    ActionSender.sendPositionedGraphic(p, npc.getLocation(), 2739);
                }
                for (Player p : CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()) {
                    ActionSender.sendPositionedGraphic(p, npc.getLocation(), 2739);
                }
                player.getInventory().removeItems(item);
                CastleWarsActivity.getSingleton().getZamorakTeam().removeBarricade(npc);
            } else {
                return false;
            }
            return true;
        } else if (item.getId() == 590) {
            if (npc.getId() == 1532 || npc.getId() == 1534) {
                npc.getMask().setSwitchId(npc.getId() + 1);
                npc.getMask().setApperanceUpdate(true);
                final int hitpoints = npc.getHitPoints();
                World.getWorld().submit(new Tick(1) {
                    int hp = hitpoints;

                    @Override
                    public void execute() {
                        if (hp > 200 && hp < 301) {
                            npc.animate(Animation.create(9658));
                        }
                        hp -= 100;
                        if (hp < 1) {
                            if (npc.getId() == 1533) {
                                CastleWarsActivity.getSingleton().getSaradominTeam().removeBarricade(npc);
                            } else {
                                CastleWarsActivity.getSingleton().getZamorakTeam().removeBarricade(npc);
                            }
                            stop();
                        }
                    }
                });
                return true;
            }
        }
        return false;
    }
}
