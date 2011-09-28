package org.dementhium.model;

import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.misc.DamageManager.DamageType;

/**
 * Represents a special attack to execute.
 * @author Emperor
 *
 */
public abstract class SpecialAttack {

	/**
	 * Commences the special attack combat cycle.
	 * @param interaction The current interaction.
	 * @return {@code True} if the special attack could commence,
	 * <br>		{@code false} if the combat cycle should stop.
	 */
	public abstract boolean commenceSpecialAttack(Interaction interaction);
	
	/**
	 * Ticks the combat cycle.
	 * @param interaction The current interaction.
	 * @return {@code True} if the special attack can end,
	 * <br>		{@code false} if we should keep ticking.
	 */
	public boolean tick(Interaction interaction) {
		return true;
	}
	
	/**
	 * Ends the special attack combat cycle.
	 * @param interaction The current interaction.
	 * @return {@code True} if the special attack could end,
	 * <br>		{@code false} if we should re-try.
	 */
	public boolean endSpecialAttack(Interaction interaction) {
		interaction.getVictim().getDamageManager().damage(
				interaction.getSource(), interaction.getDamage(), DamageType.MELEE);
		if (interaction.getDamage().getVenged() > 0) {
			interaction.getVictim().submitVengeance(
					interaction.getSource(), interaction.getDamage().getVenged());
		}
		if (interaction.getDamage().getDeflected() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(),
					interaction.getDamage().getDeflected(), interaction.getDamage().getDeflected(), DamageType.DEFLECT);
		}
		if (interaction.getDamage().getRecoiled() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(),
					interaction.getDamage().getRecoiled(), interaction.getDamage().getRecoiled(), DamageType.DEFLECT);
		}
		interaction.getVictim().retaliate(interaction.getSource());
		return true;
	}
	
	/**
	 * Gets the combat type.
	 * @return The combat type.
	 */
	public abstract CombatType getCombatType();
	
	/**
	 * Gets the special energy draining amount.
	 * @return The amount to drain.
	 */
	public abstract int getSpecialEnergyAmount();
	
	/**
	 * Gets the cooldown ticks for this combat session.
	 * @return The amount of ticks.
	 */
	public abstract int getCooldownTicks();
	
	/**
	 * Checks if the special attack is an instant special attack (Granite maul, ...)
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isInstant() {
		return false;
	}
	
}