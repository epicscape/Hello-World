package org.dementhium.model.combat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.util.misc.CycleState;

/**
 * The combat executing class.
 * @author Emperor
 *
 */
public class CombatExecutor {

	/**
	 * The attacking mob.
	 */
	private final Mob mob;
	
	/**
	 * The mob being attacked.
	 */
	private Mob victim;
	
	/**
	 * The current combat action used.
	 */
	private CombatAction combatAction;
		
	/**
	 * A list holding all current combat actions to execute.
	 */
	private List<Runnable> currentActions = new ArrayList<Runnable>();
	
	/**
	 * The amount of ticks left before next combat action tick.
	 */
	private int ticks = 3;
	
	/**
	 * The last attacker.
	 */
	private Mob lastAttacker;
	
	/**
	 * Constructs a new {@code CombatExecutor} {@code Object}.
	 * @param mob The source mob.
	 */
	public CombatExecutor(Mob mob) {
		this.mob = mob;
	}
	
	/**
	 * Updates the combat.
	 */
	public void tick() {
		if (ticks > 0) {
			ticks--;
		}
		if (victim != null) {
			try {
			if (mob.isDead() || victim.isDead()) {
				reset();
				return;
			}
			mob.turnTo(victim);
			combatAction = getCombatAction(mob);
			if (CombatMovement.combatFollow(mob, victim, combatAction.getCombatType()) && ticks < 1) {
				if (!victim.isAttackable(mob)) {
					reset();
				} else {
					currentActions.add(new Runnable() {
						private final CombatAction action = combatAction;
						private final Interaction interaction = new Interaction(mob, victim);
						@Override
						public void run() {
							action.setInteraction(interaction);
							action.execute();
							if (interaction.getState() == CycleState.FINISHED) {
								currentActions.remove(this);
							}
						}					
					});
				}
			}
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
		List<Runnable> actions = new ArrayList<Runnable>(currentActions);
		for (Runnable r : actions) {
			try {
			r.run();
			} catch (Throwable t) {
				t.printStackTrace();
			}
		}
	}

	/**
	 * Gets the mob's combat action.
	 * @param mob The mob.
	 * @return The combat action to use.
	 */
	public static CombatAction getCombatAction(Mob mob) {
		return mob.getCombatAction();
	}
	
	/**
	 * Resets the combat.
	 */
	public void reset() {
		victim = null;
		mob.getWalkingQueue().reset();
		mob.setAttribute("spellId", -1);
		mob.turnTo(null);
		
	}
	
	/**
	 * Gets a gaussian distributed randomized value between 0 and the {@code maximum} value.
	 * <br>The mean (average) is maximum / 2.
	 * @param meanModifier The modifier used to determine the mean.
	 * @param r The random instance.
	 * @param maximum The maximum value.
	 * @return The randomized value.
	 */
	public static double getGaussian(double meanModifier, Random r, double maximum) {
		double mean = maximum * meanModifier;
		double deviation = mean * 1.79;
		double value = 0;
		do {
			value = Math.floor(mean + r.nextGaussian() * deviation);
		} while (value < 0 || value > maximum);
		return value;
	}

	/**
	 * @return the mob
	 */
	public Mob getMob() {
		return mob;
	}

	/**
	 * @return the victim
	 */
	public Mob getVictim() {
		return victim;
	}

	/**
	 * @param victim the victim to set
	 */
	public void setVictim(Mob victim) {
		this.victim = victim;
	}

	/**
	 * @return the combatAction
	 */
	public CombatAction getCombatAction() {
		return combatAction;
	}

	/**
	 * @param combatAction the combatAction to set
	 */
	public void setCombatAction(CombatAction combatAction) {
		this.combatAction = combatAction;
	}

	/**
	 * Sets the current amount of ticks.
	 * @param cooldownTicks The ticks to cool down.
	 */
	public void setTicks(int cooldownTicks) {
		setTicks(cooldownTicks, mob.getAttribute("miasamicTime", -1) > World.getTicks() 
				&& combatAction.getCombatType() != CombatType.MAGIC);
	}
	
	/**
	 * Sets the current amount of ticks.
	 * @param cooldownTicks The ticks to cool down.
	 */
	public void setTicks(int cooldownTicks, boolean miasmic) {
		this.ticks = cooldownTicks;
		if (miasmic) {
			this.ticks *= 1.5;
		}
	}

	/**
	 * Gets the amount of cooldown ticks left.
	 * @return The amount of cooldown ticks.
	 */
	public int getTicks() {
		return ticks;
	}

	/**
	 * @return the lastAttacker
	 */
	public Mob getLastAttacker() {
		if (mob.getAttribute("combatTicks", -1) < World.getTicks()) {
			lastAttacker = null;
		}
		return lastAttacker;
	}

	/**
	 * @param lastAttacker the lastAttacker to set
	 */
	public void setLastAttacker(Mob lastAttacker) {
		mob.setAttribute("combatTicks", World.getTicks() + 16);
		this.lastAttacker = lastAttacker;
	}
}