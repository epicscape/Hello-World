package org.dementhium.content.skills.agility;

import org.dementhium.content.misc.ClimbingHandler;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.tickable.Tick;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public abstract class AbstractCourse {

	public static final Animation CLIMB_ANIMATION = ClimbingHandler.CLIMB_ANIMATION, CROSS_ANIMATION = Animation.create(9908, 40);

	public abstract boolean handleObject(Player player, GameObject object);

	public void forceWalk(final Player player, Animation animation, int toX, int toY, int ticks, int[] forceMovement, final String finishMessage) {
		forceWalk(player, animation, toX, toY, ticks, forceMovement, finishMessage, 0.0);
	}

	public void forceWalk(final Player player, Animation animation, int toX, int toY, int ticks, int[] forceMovement, final String finishMessage, final double experience) {
		final boolean running;
		if (forceMovement != null) {
			player.forceMovement(animation, toX, toY, forceMovement[0], forceMovement[1], forceMovement[2], ticks, forceMovement.length < 4);
			reverseActions(player, false);
			running = player.getWalkingQueue().isRunning();
		} else {
			running = player.getWalkingQueue().isRunning();
			player.animate(animation);
			player.getWalkingQueue().setRunToggled(false);
			player.getWalkingQueue().setIsRunning(false);
			player.requestWalk(toX, toY);
			reverseActions(player, false);
		}
		if (experience > 0) {
			player.submitTick("restoreAgility", new Tick(ticks) {
				@Override
				public void execute() {
					stop();
					player.getSkills().addExperience(Skills.AGILITY, experience);
					reverseActions(player, true);
					if (player.getRenderAnimation() > 0) {
						player.resetRenderAnimation();
						player.getMask().setApperanceUpdate(true);
					}
					player.sendMessage(finishMessage);
					player.animate(Animation.RESET);
					player.getWalkingQueue().setRunToggled(running);
				}
			}, true);
		}
	}

	public void delayForceWalk(final Player player, Animation animation, int toX, int toY, int ticks, int[] forceMovement, final String finishMessage, final int delay) {
		delayForceWalk(player, animation, toX, toY, ticks, forceMovement, finishMessage, delay, 0.0);
	}

	public void delayForceWalk(final Player player, final Animation animation, final int toX, final int toY, final int ticks, final int[] forceMovement, final String finishMessage, final int delay, final double experience) {
		World.getWorld().submit(new Tick(delay) {
			@Override
			public void execute() {
				stop();
				forceWalk(player, animation, toX, toY, ticks, forceMovement, finishMessage, experience);
			}
		});
	}

	public void reverseActions(Player player, boolean allowed) {
		if (!allowed) {
			player.setAttribute("cantMove", Boolean.TRUE);
			player.setAttribute("busy", Boolean.TRUE);
		} else {
			player.removeAttribute("cantMove");
			player.removeAttribute("busy");
		}
	}

	public void climb(final Player player, final Location location, final String climbMessage, final String arrivalMessage, final double experience) {
		player.animate(CLIMB_ANIMATION);
		player.sendMessage(climbMessage);
		reverseActions(player, false);
		player.submitTick("agility_tick", new Tick(1) {
			private boolean done = false;

			@Override
			public void execute() {
				if (done) {
					player.getSkills().addExperience(Skills.AGILITY, experience);
					player.sendMessage(arrivalMessage);
					stop();
					return;
				}
				done = true;
				player.teleport(location);
				reverseActions(player, true);
			}
		});
	}

	public void advanceCourseStage(Player player, String attributeIdentifier, int expectedStage, int nextStage) {
		int currentStage = player.getAttribute(attributeIdentifier, 0);
		if (currentStage == expectedStage) {
			player.setAttribute(attributeIdentifier, nextStage);
		}
	}

}
