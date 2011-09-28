package org.dementhium.tickable.impl;

import java.util.LinkedList;
import java.util.List;

import org.dementhium.io.FileUtilities;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.misc.GroundItem;
import org.dementhium.model.misc.GroundItemManager;
import org.dementhium.model.misc.RespawnableGroundItem;
import org.dementhium.tickable.Tick;

/**
 * The gorund item updating tick task.
 *
 * @author Emperor
 */
public class GroundItemUpdateTick extends Tick {

	/**
	 * The singleton of this class.
	 */
	private static final GroundItemUpdateTick SINGLETON = new GroundItemUpdateTick();

	/**
	 * Constructs a new {@code GroundItemUpdateTick} {@code Object}.
	 */
	private GroundItemUpdateTick() {
		super(1);
		try {
			boolean ignore = false;
			for (String string : FileUtilities.readFile("data/worldItems.txt")) {
				if (string.startsWith("//") || string.equals("")) {
					continue;
				}
				if (string.contains("/*")) {
					ignore = true;
					continue;
				}
				if (ignore) {
					if (string.contains("*/")) {
						ignore = false;
					}
					continue;
				}
				String[] data = string.split(" ");
				int id = Integer.parseInt(data[0]), x = Integer.parseInt(data[1]), y = Integer.parseInt(data[2]), z = Integer.parseInt(data[3]), delay = Integer.parseInt(data[4]);
				GroundItemManager.createGroundItem(new RespawnableGroundItem(new Item(id), Location.locate(x, y, z), delay));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void execute() {
		try {
			List<Runnable> toExecute = new LinkedList<Runnable>();
			for (int i = 0; i < GroundItemManager.getGroundItems().size(); i++) {
				final GroundItem item = GroundItemManager.getGroundItems().get(i);
				if (item == null || item instanceof RespawnableGroundItem) {
					continue;
				}
				item.setUpdateTicks(item.getUpdateTicks() - 1);
				if (item.getUpdateTicks() < 1) {
					if (item.isPublic()) {
						toExecute.add(new Runnable() {
							@Override
							public void run() {
								GroundItemManager.removePublicGroundItem(item);
							}
						});
					} else {
						toExecute.add(new Runnable() {
							@Override
							public void run() {
								if (item.getItem() != null && item.getItem().getDefinition().isTradeable()) {
									GroundItemManager.setPublic(item);
								} else {
									GroundItemManager.removeGroundItem(item);
								}
							}
						});
					}
				}
			}
			for (Runnable execute : toExecute) {
				execute.run();
			}
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return the singleton
	 */
	public static GroundItemUpdateTick getSingleton() {
		return SINGLETON;
	}

}
