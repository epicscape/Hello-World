package org.dementhium.model.npc.impl;

import org.dementhium.content.activity.impl.ImpetuousImpulses;
import org.dementhium.content.areas.Area;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.npc.NPC;
import org.dementhium.tickable.Tick;
import org.dementhium.util.Misc;

/**
 * Represents an impling.
 * @author Emperor
 *
 */
public class Impling extends NPC {

	/**
	 * Constructs a new {@code Impling} {@code Object}.
	 * @param id The npc id.
	 */
	public Impling(int id) {
		super(id);
	}
	
	@Override
    public void tick() {
		boolean inPuroPuro = getAttribute("puroPuro", false);
		if (getRandom().nextInt(100) < 4) {
			forceText("Tee hee!");
		}
		int nextTeleport = getAttribute("nextTeleport", -1);
		if (nextTeleport > -1 && World.getTicks() > nextTeleport) {
			setAttribute("nextTeleport", World.getTicks() + 600);
			graphics(590);
			World.getWorld().submit(new Tick(1) {
				@Override
				public void execute() {
					stop();
					teleport(ImpetuousImpulses.LOCATIONS[getRandom().nextInt(
							ImpetuousImpulses.LOCATIONS.length)]);
				}
			});
			return;
		}
        if (getAttribute("freezeTime", -1) < World.getTicks() && getRandom().nextDouble() > 0.85) {
        	if (inPuroPuro) {
        		Area area = World.getWorld().getAreaManager().getAreaByName("Puro-Puro");
        		int x = area.swX + getRandom().nextInt(area.nwX - 3 - area.swX) + 3;
        		int y = area.swY + getRandom().nextInt(area.nwY - 3 - area.swY) + 3;
        		requestWalk(x, y);
        	} else {
	            int moveX = getLocation().getX() + Misc.random(-8, 8), moveY = getLocation().getY() + Misc.random(-8, 8);
	            requestWalk(moveX, moveY);
        	}
        }
    }
	
	@Override
	public boolean isAttackable(Mob other) {
		return true;
	}
	
	@Override
	public void instantDeath() {
        setHidden(true);
        setDead(true);
        setAttribute("freezeTime", -1);
		World.getWorld().submit(new Tick(getAttribute("respawnDelay", 500)) {
			@Override
			public void execute() {
				ImpetuousImpulses.getCaughtImplings().remove(Impling.this);
		        setHidden(false);
		        setDead(false);
				stop();
			}			
		});
	}

}
