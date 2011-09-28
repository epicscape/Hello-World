package org.dementhium.content.skills.agility;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.content.skills.agility.obstacles.BarbarianCourse;
import org.dementhium.content.skills.agility.obstacles.GnomeStrongholdCourse;
import org.dementhium.content.skills.agility.obstacles.WorldObstacles;
import org.dementhium.content.skills.agility.obstacles.WildernessCourse;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.player.Player;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class Agility {

	private static List<AbstractCourse> courses = new ArrayList<AbstractCourse>();

	static {
		courses.add(new GnomeStrongholdCourse());
		courses.add(new BarbarianCourse());
		courses.add(new WildernessCourse());
		courses.add(new WorldObstacles());
	}

	public static void init() {

	}

	public static boolean handleObject(Player player, GameObject gameObject) {
		for (AbstractCourse course : courses) {
			if (course.handleObject(player, gameObject)) {
				return true;
			}
		}
		return false;
	}
}
