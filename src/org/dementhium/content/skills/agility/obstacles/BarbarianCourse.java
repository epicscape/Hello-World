package org.dementhium.content.skills.agility.obstacles;

import org.dementhium.content.skills.agility.AbstractCourse;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class BarbarianCourse extends AbstractCourse {

	public static final Location PIPE_OBJECT_LOCATION = Location.locate(2552, 3559, 0);

	public static final Location[] PIPE_LOCATIONS = {
		Location.locate(2552, 3561, 0), Location.locate(2552, 3558, 0)
	};

	private Location logBalanceLocation = Location.locate(2551, 3546, 0);

	@Override
	public boolean handleObject(final Player player, GameObject object) {
		Tick tick;
		switch (object.getId()) {
		case 20210: // entrance pipe thing
			if (player.getSkills().getLevel(Skills.AGILITY) < 35) {
				player.sendMessage("You need an Agility level of 35 or above to enter this course!");
				return true;
			}
			final int index = player.getLocation().getY() > 3559 ? 0 : 1;
			final Location location = PIPE_LOCATIONS[index];
			tick = new Tick(player.getLocation() == location ? 0 : 1) {
				public void execute() {
					if (player.getLocation() == location) {
						switch (index) {
						case 0:
							forceWalk(player, Animation.create(10580), player.getLocation().getX(), player.getLocation().getY() - 3, 2, new int[]{10, 60, -1}, null, 10.0);
							break;
						case 1:
							forceWalk(player, Animation.create(10580), player.getLocation().getX(), player.getLocation().getY() + 3, 2, new int[]{10, 60, -1}, null, 0.0);
							break;
						}
						stop();
					}
				}
			};
			if (tick.getTime() < 1) {
				tick.execute();
			} else {
				player.requestWalk(location.getX(), location.getY());
				player.submitTick("agility_tick", tick);
			}
			return true;
		case 43526: // swing rope
			if (player.getSkills().getLevel(Skills.AGILITY) < 35) {
				player.sendMessage("You need an Agility level of 35 or above to do this!");
				return true;
			}
			player.getMask().setFacePosition(object.getLocation().transform(0, -1, 0), 1, 1);
			forceWalk(player, Animation.create(751), player.getLocation().getX(), player.getLocation().getY() - 5, 3, new int[]{30, 90, -1}, null, 22.0);
			ActionSender.sendAnimateObject(player, object, 497);
			advanceCourseStage(player, "barbarian_course", 0, 1);
			return true;
		case 43595: // log balance
			if (player.getSkills().getLevel(Skills.AGILITY) < 35) {
				player.sendMessage("You need an Agility level of 35 or above to do this!");
				return true;
			}
			tick = new Tick(player.getLocation() == logBalanceLocation ? 0 : 1) {
				public void execute() {
					if (player.getLocation() == logBalanceLocation) {
						player.sendMessage("You walk carefully across the slippery log...");
						forceWalk(player, CROSS_ANIMATION, player.getLocation().getX() - 10, player.getLocation().getY(), 12, null, ".. and make it safely to the other side.", 13.7);
						advanceCourseStage(player, "barbarian_course", 1, 2);
						stop();
					}
				}
			};
			if (tick.getTime() < 1) {
				tick.execute();
			} else {
				player.requestWalk(logBalanceLocation.getX(), logBalanceLocation.getY());
				player.submitTick("agility_tick", tick);
			}
			return true;
		case 20211: // Obstacle net
			climb(player, player.getLocation().transform(-2, 0, 1), null, null, 8.2);
			advanceCourseStage(player, "barbarian_course", 2, 3);
			return true;
		case 2302: // Balancing ledge
			//player.animate(Animation.create(753));
			forceWalk(player, null, player.getLocation().getX() - 4, player.getLocation().getY(), 5, null, null, 22.0);
			advanceCourseStage(player, "barbarian_course", 3, 4);
			World.getWorld().submit(new Tick(1) {
				public void execute() {
					stop();
					player.setRenderAnimation(157);
				}
			});
			return true;
		case 1948:
			if (player.getSkills().getLevel(Skills.AGILITY) < 35) {
				player.sendMessage("You need an Agility level of 35 or above to do this!");
				return true;
			}
			final Location to = object.getLocation().transform(-1, 0, 0);
			tick = new Tick(player.getLocation() == to ? 0 : 1) {
				public void execute() {
					if (player.getLocation() == to) {
						double xp = 13.7;
						if (player.getLocation().getX() + 2 == 2543) {
							if (player.<Integer>getAttribute("barbarian_course", 0) == 4) {
								player.removeAttribute("barbarian_course");
								xp += 46.2;
							}
						}
						forceWalk(player, Animation.create(4853), player.getLocation().getX() + 2, player.getLocation().getY(), 2, new int[] {0, 60, 1}, null, xp);
						stop();
					}
				}
			};
			if (tick.getTime() < 1) {
				tick.execute();
			} else {
				int diffX = player.getLocation().getX() - object.getLocation().getX();
				if (diffX > 0) {
					player.sendMessage("You can't do that from here!");
					return true;
				}
				player.requestWalk(to.getX(), to.getY());
				player.submitTick("agility_tick", tick);
			}
			return true;
		}
		return false;
	}

}
