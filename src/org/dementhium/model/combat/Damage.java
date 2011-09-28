package org.dementhium.model.combat;

import org.dementhium.model.Mob;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 * Represents a damage to hit.
 * @author Emperor
 * @author CjayII (aka Mystic Flow/Steven) <font size="2" color="red"><b>Did pretty much nothing</b></font>.
 */
public class Damage {
	
	/**
	 * The hit to be dealt to the opponent.
	 */
	private int hit;
	
	/**
	 * The amount of soaked damage.
	 */
	private int soaked;
	
	/**
	 * The deflected hit to be dealt to the attacker.
	 */
	private int deflected;
	
	/**
	 * The venged hit to be dealt to the attacker.
	 */
	private int venged;
	
	/**
	 * The recoiled hit to be dealt to the attacker.
	 */
	private int recoiled;
	
	/**
	 * The maximum hit of the mob.
	 */
	private int maximum;
	
	/**
	 * Constructs a new {@code Damage} {@code Object}.
	 * @param hit The standard hit.
	 */
	public Damage(int hit) {
		this.hit = hit;
	}
	
	/**
	 * Returns the damage instance of this hit, keeping damage modifiers in mind.
	 * @param source The attacking entity.
	 * @param victim The entity being hit.
	 * @param hit The hit.
	 * @return The damage.
	 */
	public static Damage getDamage(Mob source, Mob victim, CombatType type, int hit) {
		Damage damage = victim.updateHit(source, hit, type);
		if (victim.isPlayer() && damage.getHit() > 200) {
			damage.soaked = getSoaked(victim, damage.getHit(), type);
			damage.setHit(damage.getHit() - damage.soaked);
		}
		int lifepoints = victim.getHitPoints();
		if (hit > lifepoints) {
			hit = lifepoints;
		}
		damage.setRecoiled(getRecoilDamage(source, victim, damage.getHit()));
		damage.setVenged(getVengDamage(victim, damage.getHit()));
		return damage;
	}
	
	/**
	 * Gets the recoiled damage.
	 * @param victim The victim.
	 * @param hit The hit.
	 * @return The recoiled damage if the victim is recoiling, or -1 if not.
	 */
	private static int getRecoilDamage(Mob source, Mob victim, int hit) {
		int recoiled = (int) Math.floor(hit * 0.1);
		if (recoiled < 1) {
			return -1;
		}
		if (victim.isPlayer()) {
			Player p = victim.getPlayer();
			if (p.getEquipment().getSlot(Equipment.SLOT_RING) != 2550) {
				return -1;
			}
			if (recoiled > p.getSettings().getRecoilDamage()) {
				recoiled = p.getSettings().getRecoilDamage();
			}
			int hitpoints = source.getHitPoints();
			if (recoiled > hitpoints) {
				recoiled = hitpoints;
			}
			p.getSettings().setRecoilDamage(p.getSettings().getRecoilDamage() - recoiled);
			if (p.getSettings().getRecoilDamage() < 1) {
				ActionSender.sendMessage(p, "Your ring of recoil has turned to dust.");
				p.getEquipment().set(Equipment.SLOT_RING, null);
				p.getSettings().setRecoilDamage(400);
			}
			return recoiled;
		}
		//TODO: NPC recoiling.
		return -1;
	}

	/**
	 * Gets the vengeance damage.
	 * @param victim The victim.
	 * @param hit The hit.
	 * @return The vengeance damage if the spell is casted and the victim is a player, or -1 if not.
	 */
	private static int getVengDamage(Mob victim, int hit) {
		if (victim.isNPC()) {
			return -1;
		}
		if (hit > 0 && victim.getAttribute("vengeance", false)) {
			return (int) (hit * 0.75);
		}
		return -1;
	}
	
	/**
	 * Gets the soaked damage amount.
	 * @param victim The victim.
	 * @param hit The hit to soak.
	 * @param type The combat type used.
	 * @return The amount of absorbed damage.
	 */
    private static int getSoaked(Mob victim, int hit, CombatType type) {
    	if (type.getAbsorbtion() < 0 || type.getAbsorbtion() > 2) {
    		return 0;
    	}
    	int absorptionId = type.getAbsorbtion();
    	int excess = hit - 200;
    	double bonus = victim.getPlayer().getBonuses().getAbsorptionBonus(absorptionId) / 100D;
    	return (int) (excess * bonus);
    }

	/**
	 * @param hit the hit
	 */
	public void setHit(int hit) {
		this.hit = hit;
	}
	
	/**
	 * @return the hit
	 */
	public int getHit() {
		return hit;
	}

	/**
	 * @param deflected the deflected to set
	 * @return The Damage instance.
	 */
	public Damage setDeflected(int deflected) {
		this.deflected = deflected;
		return this;
	}

	/**
	 * @return the deflected
	 */
	public int getDeflected() {
		return deflected;
	}

	/**
	 * @param venged the venged to set
	 */
	public void setVenged(int venged) {
		this.venged = venged;
	}

	/**
	 * @return the venged
	 */
	public int getVenged() {
		return venged;
	}

	/**
	 * @param recoiled the recoiled to set
	 */
	public void setRecoiled(int recoiled) {
		this.recoiled = recoiled;
	}

	/**
	 * @return the recoiled
	 */
	public int getRecoiled() {
		return recoiled;
	}

	/**
	 * @return the soaked
	 */
	public int getSoaked() {
		return soaked;
	}

	/**
	 * @param soaked the soaked to set
	 */
	public void setSoaked(int soaked) {
		this.soaked = soaked;
	}

	/**
	 * @return the maximum
	 */
	public int getMaximum() {
		return maximum;
	}

	/**
	 * @param maximum the maximum to set
	 */
	public void setMaximum(int maximum) {
		this.maximum = maximum;
	}
	
}