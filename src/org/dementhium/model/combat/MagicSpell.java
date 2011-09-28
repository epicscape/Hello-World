package org.dementhium.model.combat;

import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.player.Player;

/**
 * Represents a single Magic spell.
 * @author Emperor
 *
 */
public abstract class MagicSpell {

	/**
	 * Starts the casting of a spell.
	 * @param interaction The interaction settings.
	 * @return {@code True}.
	 */
	public abstract boolean castSpell(Interaction interaction);

	/**
	 * Ticks the spell.
	 * @param interaction The interaction settings.
	 * @return {@code True}.
	 */
	public boolean tick(Interaction interaction) {
		if (interaction.getTicks() < 2) {
			if (interaction.isDeflected()) {
				interaction.getVictim().graphics(2228);
			}
			interaction.getVictim().animate(interaction.isDeflected() ? 12573 : interaction.getVictim().getDefenceAnimation());
		}
		return true;
	}

	/**
	 * Ends the spell.
	 * @param interaction The interaction settings.
	 * @return {@code True} if the spell can be stopped, <br>
	 * 		   {@code false} if not.
	 */
	public boolean endSpell(Interaction interaction) {
		if (interaction.getDamage().getHit() > -1) {
			interaction.getVictim().graphics(interaction.getEndGraphic());
			interaction.getVictim().getDamageManager().damage(
					interaction.getSource(), interaction.getDamage(), DamageType.MAGE);
		} else {
			interaction.getVictim().graphics(85, 96 << 16);
		}
		if (interaction.getDamage().getVenged() > 0) {
			interaction.getVictim().submitVengeance(interaction.getSource(), interaction.getDamage().getVenged());
		}
		if (interaction.getDamage().getDeflected() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(), 
					interaction.getDamage().getDeflected(), 
					interaction.getDamage().getDeflected(), DamageType.DEFLECT);
		}
		if (interaction.getDamage().getRecoiled() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(), 
					interaction.getDamage().getRecoiled(), 
					interaction.getDamage().getRecoiled(), DamageType.DEFLECT);
		}
		interaction.getVictim().retaliate(interaction.getSource());
		return true;
	}

	/**
	 * Gets the experience to add.
	 * @param interaction The interaction used.
	 * @return The experience to add.
	 */
	public abstract double getExperience(Interaction interaction);
	
	/**
	 * Gets the start damage for maximum hit calculation.
	 * @param source The attacking player.
	 * @param victim The victim.
	 * @return The start damage.
	 */
	public abstract int getStartDamage(Player source, Mob victim);

	/**
	 * Gets the normal damage.
	 * @return The normal damage.
	 */
	public abstract int getNormalDamage();

	/**
	 * Gets the spell's base damage.
	 * @return The base damage.
	 */
	public abstract int getBaseDamage();
	
	/**
	 * Gets the required runes used for this spell.
	 * @return The array of runes.
	 */
	public abstract Item[] getRequiredRunes();
	
	/**
	 * Gets the extra delay between casting and hitting.
	 * @return The extra delay amount in ticks.
	 */
	public int getExtraDelay() {
		return 0;
	}

	/**
	 * The autocasting config value.
	 * @return The value.
	 */
	public int getAutocastConfig() {
		return -1;
	}
}