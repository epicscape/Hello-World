package org.dementhium.model.map;

import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 'Mystic Flow
 */
public class ObjectManager {


	private static List<GameObject> customObjects = new ArrayList<GameObject>();

	private static List<GameObject> removedObjects = new ArrayList<GameObject>();

	public static GameObject addCustomObject(int objectId, int x, int y, int height, int type, int direction) {
		return addCustomObject(objectId, x, y, height, type, direction, true);
	}

	public static GameObject removeCustomObject(Location l, int type) {
		return removeCustomObject(l.getX(), l.getY(), l.getZ(), type, true);
	}

	public static GameObject removeCustomObject(int x, int y, int height, int type) {
		return removeCustomObject(x, y, height, type, true);
	}

	public static GameObject addCustomObject(Player owner, int objectId, int x, int y, int height, int type, int direction) {
		return addCustomObject(objectId, x, y, height, type, direction, true);
	}

	public static GameObject addCustomObject(GameObject object) {
		return addCustomObject(object.getId(), object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getZ(), object.getType(), object.getRotation());
	}

	public static GameObject addCustomObject(int objectId, int x, int y, int height, int type, int direction, boolean refresh) {
		GameObject objectAdded = Region.addObject(objectId, x, y, height, type, direction, false);
		if (objectAdded != null && removedObjects.contains(objectAdded)) {
			removedObjects.remove(objectAdded);
		}
		if (objectAdded != null) {
			customObjects.add(objectAdded);
			if (refresh) {
				refresh(objectAdded);
			}
		}
		return objectAdded;
	}

	public static GameObject removeCustomObject(int x, int y, int height, int type, boolean refresh) {
		GameObject objectRemoved = null;
		for (int i = type - 1; i <= type + 2; i++) {
			objectRemoved = Region.removeObject(x, y, height, i);
			if (objectRemoved != null) {
				customObjects.remove(objectRemoved);
			}
			if (refresh) {
				if (objectRemoved != null) {
					if (objectRemoved.getLocation() == null)
						objectRemoved.setLocation(Location.locate(x, y, height));
					for (Player player : World.getWorld().getPlayers()) {//TODO: Check if this works >.< Region.getLocalPlayers(objectRemoved.getLocation())) {
						if (player != null && player.getLocation().withinDistance(objectRemoved.getLocation())) {
							ActionSender.deleteObject(player, objectRemoved.getId(), objectRemoved.getLocation().getX(), objectRemoved.getLocation().getY(), objectRemoved.getLocation().getZ(), objectRemoved.getType(), objectRemoved.getRotation());
						}
					}
				}
			}
			if (objectRemoved != null) {
				break;
			}
		}
		return objectRemoved;
	}

	public static void addCustomObject(Player owner, int objectId, int x, int y, int height, int type, int direction, boolean refresh) {
		GameObject objectAdded = Region.addObject(objectId, x, y, height, type, direction, false);
		if (objectAdded != null) {
			customObjects.add(objectAdded);
			if (refresh) {
				refresh(objectAdded);
			}
		}
	}

	public static void replaceObjectTemporarily(Location location, int newId, int delay) {
		replaceObjectTemporarily(location.getX(), location.getY(), location.getZ(), newId, delay);
	}

	public static void replaceObjectTemporarily(final int x, final int y, final int height, final int newId, final int delay) {
		final GameObject objectRemoved = removeCustomObject(x, y, height, 10, false);
		if (objectRemoved != null) { //nothing to replace if it's null
			final int oldId = objectRemoved.getId();
			addCustomObject(newId, x, y, height, 10, objectRemoved.getRotation());
			World.getWorld().submit(new Tick(delay) {
				public void execute() {
					removeCustomObject(x, y, height, 10);
					refresh(addCustomObject(oldId, x, y, height, 10, objectRemoved.getRotation()));
					stop();
				}
			});
		}
	}

	public static void replaceObject(Location location, int newId) {
		replaceObject(location.getX(), location.getY(), location.getZ(), newId);
	}

	public static void replaceObject(final int x, final int y, final int height, final int newId) {
		GameObject objectRemoved = removeCustomObject(x, y, height, 10, false);
		if (objectRemoved != null) {
			addCustomObject(newId, x, y, height, 10, 0);
		}
	}

	public static void clearArea(Location loc, int depth) {
		List<GameObject> toRemove = new ArrayList<GameObject>();
		for (GameObject object : customObjects) {
			if (object.getLocation().distance(loc) <= depth) {
				toRemove.add(object);
			}
		}
		for (GameObject object : toRemove) {
			ObjectManager.removeCustomObject(object.getLocation().getX(), object.getLocation().getY(), object.getLocation().getZ(), object.getType(), true);
			customObjects.remove(object);
		}
	}

	public static void refresh() {
		for (GameObject object : customObjects) {
			if (object.getOwner() == null) {
				for (Player player : Region.getLocalPlayers(object.getLocation())) {
					ActionSender.sendObject(player, object);
				}
			} else {
				ActionSender.sendObject(object.getOwner(), object);
			}
		}
	}

	public static void refresh(GameObject object) {
		if (object.getOwner() == null) {
			for (Player player : Region.getLocalPlayers(object.getLocation())) {
				ActionSender.sendObject(player, object);
			}
		} else {
			ActionSender.sendObject(object.getOwner(), object);
		}
	}

	public static void refresh(Player player) {
		//		for (GameObject object : removedObjects) {
			//		//	ActionSender.deleteObject(player, object.getId(), object.getLocation().getX(), object.getLocation().getY(),
					//			//		object.getLocation().getZ(), object.getType(), object.getRotation());
			//		}
		for (GameObject object : customObjects) {
			if (object.getOwner() == null) {
				ActionSender.sendObject(player, object);
			} else {
				ActionSender.sendObject(object.getOwner(), object);
			}
		}
	}

	public static void init() {
		System.out.println("Loading objects...");
		ObjectManager.addCustomObject(2352, 3568, 9677, 0, 10, 0, false); //climbing rope @ barrows
		ObjectManager.addCustomObject(2352, 3568, 9711, 0, 10, 0, false); //climbing rope @ barrows
		ObjectManager.addCustomObject(2352, 3568, 9694, 0, 10, 0, false);  //climbing rope @ barrows
		ObjectManager.addCustomObject(2352, 3551, 9711, 0, 10, 0, false);  //climbing rope @ barrows
		ObjectManager.addCustomObject(1293, 2540, 2849, 0, 10, 0, false); //spirit tree
		ObjectManager.addCustomObject(2273, 2648, 9562, 0, 10, 0, false);
		ObjectManager.addCustomObject(2274, 2648, 9557, 0, 10, 0, false);
		ObjectManager.addCustomObject(2465, 2683, 9504, 0, 10, 0, false);
		ObjectManager.addCustomObject(2466, 2687, 9508, 0, 10, 0, false);
		ObjectManager.addCustomObject(12128, 3198, 3425, 0, 22, 0, false);
		ObjectManager.addCustomObject(12129, 3199, 3425, 0, 22, 0, false);
		ObjectManager.addCustomObject(12130, 3199, 3424, 0, 22, 0, false);
		ObjectManager.addCustomObject(2782, 2401, 4470, 0 , 10, 0, false);
		System.out.println("Loaded " + customObjects.size() + " objects.");

		//		Region.addObject(4411, 2418, 3123, 0, 22, 0, true);
		//		Region.addObject(4411, 2418, 3125, 0, 22, 0, true);
		//		Region.addObject(4411, 2419, 3125, 0, 22, 0, true);
		//		Region.addObject(4411, 2419, 3123, 0, 22, 0, true);
		//
		//		Region.addObject(36691, 2400, 3108, 0, 22, 0, true);
	}

	public static void removeObjectTemporarily(Location location, int delay, final int type, final int dir) {
		final int x = location.getX();
		final int y = location.getY();
		final int height = location.getZ();
		GameObject objectRemoved = removeCustomObject(x, y, height, type, true);
		if (objectRemoved != null) { //nothing to replace if it's null
			final int oldId = objectRemoved.getId();
			World.getWorld().submit(new Tick(delay) {
				public void execute() {
					addCustomObject(oldId, x, y, height, type, dir);
					stop();
				}
			});
		}

	}

	public static void addObjectTemporarily(final int x, final int y, final int height, int rotation, final int type, int id, int ticks) {
		addCustomObject(id, x, y, height, type, rotation);
		World.getWorld().submit(new Tick(ticks) {
			public void execute() {
				stop();
				removeCustomObject(x, y, height, type, true);
			}
		});

	}
}
