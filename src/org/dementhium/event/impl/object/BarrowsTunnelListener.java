package org.dementhium.event.impl.object;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Location;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.player.Player;

/**
 * @author Luke132
 * @author 'Mystic Flow
 */
public class BarrowsTunnelListener extends EventListener {

    public static final int[][] COMMON_REWARDS = {
            {558, 0, 1795}, {562, 0, 773}, {560, 0, 391}, {565, 0, 164},
            {995, 800, 5000}, {4740, 50, 150}
    };

    public static final int[] RARE_REWARDS = {
            1149, 985, 987
    };

    protected static final int[] BARROW_REWARDS = {
            4757, 4759, 4753, 4755, // Verac's
            4736, 4738, 4734, 4732, // Karil's
            4745, 4747, 4749, 4751, // Torag's
            4708, 4710, 4712, 4714, // Ahrim's
            4716, 4718, 4720, 4722, // Dharok's
            4724, 4726, 4728, 4730, // Guthan's
    };


    protected static final int[] DOORS = {
            6747, 6741, 6735, 6739, 6746, 6745, 6737, 6735,
            6728, 6722, 6716, 6720, 6727, 6726, 6718, 6716,
            6731, 6728, 6722, 6720, 6727, 6731, 6726, 6718,
            6750, 6747, 6741, 6739, 6746, 6750, 6745, 6737,
            6742, 6749, 6748, 6743, 6744, 6740, 6742, 6738,
            6723, 6730, 6729, 6724, 6725, 6723, 6721, 6719,
            6749, 6748, 6736, 6743, 6744, 6740, 6738, 6736,
            6730, 6729, 6717, 6724, 6725, 6721, 6719, 6717,
    };

    protected static final int[][] DOOR_LOCATION = {
            {3569, 9684}, {3569, 9701}, {3569, 9718}, {3552, 9701}, {3552, 9684},
            {3535, 9684}, {3535, 9701}, {3535, 9718}, {3568, 9684}, {3568, 9701},
            {3568, 9718}, {3551, 9701}, {3551, 9684}, {3534, 9684}, {3534, 9701},
            {3534, 9718}, {3569, 9671}, {3569, 9688}, {3569, 9705}, {3552, 9705},
            {3552, 9688}, {3535, 9671}, {3535, 9688}, {3535, 9705}, {3568, 9671},
            {3568, 9688}, {3568, 9705}, {3551, 9705}, {3551, 9688}, {3534, 9671},
            {3534, 9688}, {3534, 9705}, {3575, 9677}, {3558, 9677}, {3541, 9677},
            {3541, 9694}, {3558, 9694}, {3558, 9711}, {3575, 9711}, {3541, 9711},
            {3575, 9678}, {3558, 9678}, {3541, 9678}, {3541, 9695}, {3558, 9695},
            {3575, 9712}, {3558, 9712}, {3541, 9712}, {3562, 9678}, {3545, 9678},
            {3528, 9678}, {3545, 9695}, {3562, 9695}, {3562, 9712}, {3545, 9712},
            {3528, 9712}, {3562, 9677}, {3545, 9677}, {3528, 9677}, {3545, 9694},
            {3562, 9694}, {3562, 9711}, {3545, 9711}, {3528, 9711},
    };

    protected static final int[][] DOOR_OPEN_DIRECTION = {
            {6732, 2, 4}, {6732, 2, 4}, {6732, 2, 4}, {6732, 2, 4}, {6732, 2, 4}, {6732, 2, 4}, {6732, 2, 4}, {6732, 2, 4},
            {6713, 0, 4}, {6713, 0, 4}, {6713, 0, 4}, {6713, 0, 4}, {6713, 0, 4}, {6713, 0, 4}, {6713, 0, 4}, {6713, 0, 4},
            {6713, 2, 2}, {6713, 2, 2}, {6713, 2, 2}, {6713, 2, 2}, {6713, 2, 2}, {6713, 2, 2}, {6713, 2, 2}, {6713, 2, 2},
            {6732, 4, 2}, {6732, 4, 2}, {6732, 4, 2}, {6732, 4, 2}, {6732, 4, 2}, {6732, 4, 2}, {6732, 4, 2}, {6732, 4, 2},
            {6732, 3, 3}, {6732, 3, 3}, {6732, 3, 3}, {6732, 3, 3}, {6732, 3, 3}, {6732, 3, 3}, {6732, 3, 3}, {6732, 3, 3},
            {6713, 1, 3}, {6713, 1, 3}, {6713, 1, 3}, {6713, 1, 3}, {6713, 1, 3}, {6713, 1, 3}, {6713, 1, 3}, {6713, 1, 3},
            {6732, 1, 1}, {6732, 1, 1}, {6732, 1, 1}, {6732, 1, 1}, {6732, 1, 1}, {6732, 1, 1}, {6732, 1, 1}, {6732, 1, 1},
            {6713, 3, 1}, {6713, 3, 1}, {6713, 3, 1}, {6713, 3, 1}, {6713, 3, 1}, {6713, 3, 1}, {6713, 3, 1}, {6713, 3, 1},
    };

    protected static final int[][] DB = {
            {3532, 9665, 3570, 9671},
            {3575, 9676, 3581, 9714},
            {3534, 9718, 3570, 9723},
            {3523, 9675, 3528, 9712},
            {3541, 9711, 3545, 9712},
            {3558, 9711, 3562, 9712},
            {3568, 9701, 3569, 9705},
            {3551, 9701, 3552, 9705},
            {3534, 9701, 3535, 9705},
            {3541, 9694, 3545, 9695},
            {3558, 9694, 3562, 9695},
            {3568, 9684, 3569, 9688},
            {3551, 9684, 3552, 9688},
            {3534, 9684, 3535, 9688},
            {3541, 9677, 3545, 9678},
            {3558, 9677, 3562, 9678},
    };


    @Override
    public void register(EventManager manager) {
        List<Integer> doors = new ArrayList<Integer>();
        for (int i : DOORS) {
            if (!doors.contains(i)) {
                doors.add(i);
            }
        }
        for (int i : doors) {
            manager.registerObjectListener(i, this);
        }
        manager.registerObjectListener(6775, this);
        manager.registerObjectListener(10284, this);
    }

    public boolean objectOption(Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
       /* if (option != ClickOption.FIRST) {
            return false;
        }
        if (objectId == 10284) {
            if (player.getCombatExecutor().getLastAttacker() != null) {
                player.sendMessage("You can't open this while being under attack!");
                return true;
            }
            player.getMask().setFacePosition(gameObject.getLocation(), gameObject.getDefinition().getSizeX(), gameObject.getDefinition().getSizeY());
            ActionSender.sendObject(player, 6775, 3551, 9695, 0, 10, 0);
            return true;
        } else if (objectId == 6775) {
            if (player.getAttribute("canLoot") == Boolean.TRUE) {
                player.removeAttribute("canLoot");
                for (int[] data : COMMON_REWARDS) {
                    if (player.getRandom().nextDouble() > 0.40) {
                        int id = data[0];
                        int amount = Misc.random(data[1], data[2]);
                        player.getInventory().addDropable(new Item(id, amount));
                    }
                }
                int chance = 1;
                for (int i = 0; i < player.getSettings().getKilledBrothers().length; i++) {
                    if (player.getSettings().getKilledBrothers()[i]) {
                        player.getSettings().getKilledBrothers()[i] = false;
                        chance += 3;
                    }
                }
                if (player.getRandom().nextInt(100) <= (chance > 15 ? 15 : chance)) {
                    int item = BARROW_REWARDS[player.getRandom().nextInt(BARROW_REWARDS.length)];
                    player.getInventory().addDropable(new Item(item, 1));
                }
                player.setAttribute("looted_barrows", Boolean.TRUE);
            } else {
               /* if (player.getAttribute(Barrows.FIGHTING_ATTRIBUTE) == null) {
                    Brother brother = player.getAttribute(Barrows.TUNNEL_CRYPT);
                    NPC spawnedBrother = new BarrowBrother(brother.getNpcId(), player.getLocation().transform(-1, 0, 0));
                    spawnedBrother.loadEntityVariables();
                    World.getWorld().getNpcs().add(spawnedBrother);
                    spawnedBrother.getCombatExecutor().setVictim(player);
                    player.setAttribute(Barrows.FIGHTING_ATTRIBUTE, spawnedBrother);
                }*/
        /*    }
            return true;
        }
        final int x = gameObject.getLocation().getX(), y = gameObject.getLocation().getY();
        final Player p = player;
        final int index = getDoorIndex(objectId, x, y);
        final int index2 = getOtherDoor(x, y); // index of the door next to the one you clicked.
        if (index == -1 || index2 == -1) {
            return false;
        }
        final boolean betweenDoors = p.getAttribute("betweenDoors") != null;
        final Location clickedDoor = Location.locate(DOOR_LOCATION[index][0], DOOR_LOCATION[index][1], 0);
        final Location otherDoor = Location.locate(DOOR_LOCATION[index2][0], DOOR_LOCATION[index2][1], 0);
        final int openDoorId = DOOR_OPEN_DIRECTION[index][0];
        final int openDoorId2 = DOOR_OPEN_DIRECTION[index2][0];
        final int openDirection = DOOR_OPEN_DIRECTION[index][2];
        final int newX = openDirection == 1 ? x + 1 : openDirection == 2 ? x : openDirection == 3 ? x - 1 : openDirection == 4 ? x : x;
        final int newY = openDirection == 1 ? y : openDirection == 2 ? y + 1 : openDirection == 3 ? y : openDirection == 4 ? y - 1 : y;
        final int newX2 = openDirection == 1 ? DOOR_LOCATION[index2][0] + 1 : openDirection == 2 ? DOOR_LOCATION[index2][0] : openDirection == 3 ? DOOR_LOCATION[index2][0] - 1 : openDirection == 4 ? DOOR_LOCATION[index2][0] : DOOR_LOCATION[index2][0];
        final int newY2 = openDirection == 1 ? DOOR_LOCATION[index2][1] : openDirection == 2 ? DOOR_LOCATION[index2][1] + 1 : openDirection == 3 ? DOOR_LOCATION[index2][1] : openDirection == 4 ? DOOR_LOCATION[index2][1] - 1 : DOOR_LOCATION[index2][1];
        final int[] walkDirections = getWalkDirections(p, index, index2, betweenDoors);
        p.getMask().setFacePosition(clickedDoor, 1, 1);
        World.getWorld().submit(new Tick(1) {
            @Override
            public void execute() {
                stop();
                p.getWalkingQueue().reset();
                ObjectManager.removeCustomObject(clickedDoor.getX(), clickedDoor.getY(), 0, 0);
                ObjectManager.removeCustomObject(otherDoor.getX(), otherDoor.getY(), 0, 0);
                ObjectManager.addCustomObject(p, openDoorId, newX, newY, 0, 0, DOOR_OPEN_DIRECTION[index][1]);
                ObjectManager.addCustomObject(p, openDoorId2, newX2, newY2, 0, 0, DOOR_OPEN_DIRECTION[index2][1]);
                p.requestWalk(p.getLocation().getX() + walkDirections[0], p.getLocation().getY() + walkDirections[1]);
                p.setAttribute("cantMove", Boolean.TRUE);
            }
        });
        World.getWorld().submit(new Tick(2) {

            @Override
            public void execute() {
                p.removeAttribute("cantMove");
                int face = openDirection == 3 ? 0 : openDirection == 4 ? 3 : openDirection == 2 ? 1 : 2;
                ObjectManager.removeCustomObject(newX, newY, 0, 0);
                ObjectManager.removeCustomObject(newX2, newY2, 0, 0);
                ObjectManager.addCustomObject(p, DOORS[index], clickedDoor.getX(), clickedDoor.getY(), 0, 0, face);
                ObjectManager.addCustomObject(p, DOORS[index2], otherDoor.getX(), otherDoor.getY(), 0, 0, face);
                if (!betweenDoors) {
                    p.setAttribute("betweenDoors", true);
                    ActionSender.sendConfig(p, 1270, 1);
                } else {
                    p.removeAttribute("betweenDoors");
                    ActionSender.sendConfig(p, 1270, 0);
                }
                stop();
            }
        });
        return true;*/
    	return false;
    }

    private static int[] getWalkDirections(Player p, int index, int index2, boolean betweenDoors) {
        int openDirection = DOOR_OPEN_DIRECTION[index][2];
        int[] direction = new int[2];
        if (openDirection == 0) {
            /*Nothing*/
        } else if (openDirection == 1) { // doors open east.
            direction[0] = betweenDoors ? +1 : -1;
            direction[1] = 0;
        } else if (openDirection == 2) { // doors open north.
            direction[0] = 0;
            direction[1] = betweenDoors ? +1 : -1;
        } else if (openDirection == 3) { // doors open west.
            direction[0] = betweenDoors ? -1 : +1;
            direction[1] = 0;
        } else if (openDirection == 4) { // doors open south.
            direction[0] = 0;
            direction[1] = betweenDoors ? -1 : +1;
        }
        return direction;
    }

    private static int getOtherDoor(int x, int y) {
        for (int i = 0; i < DOOR_LOCATION.length; i++) {
            if ((x == DOOR_LOCATION[i][0] && y + 1 == DOOR_LOCATION[i][1]) ||
                    (x + 1 == DOOR_LOCATION[i][0] && y == DOOR_LOCATION[i][1]) ||
                    (x == DOOR_LOCATION[i][0] && y - 1 == DOOR_LOCATION[i][1]) ||
                    (x - 1 == DOOR_LOCATION[i][0] && y == DOOR_LOCATION[i][1])) {
                return i;
            }
        }
        return -1;
    }

    private static int getDoorIndex(int doorId, int x, int y) {
        for (int i = 0; i < DOORS.length; i++) {
            if (doorId == DOORS[i]) {
                if (x == DOOR_LOCATION[i][0] && y == DOOR_LOCATION[i][1]) {
                    return i;
                }
            }
        }
        return -1;
    }

}
