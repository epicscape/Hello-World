package org.dementhium.model.combat.impl;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.combat.impl.specs.Disrupt;
import org.dementhium.net.ActionSender;

/**
 * Executes a special attack cycle.
 * @author Emperor
 *
 */
public class SpecialAction extends CombatAction {

	/**
	 * The special acion singleton.
	 */
	private static final SpecialAction SINGLETON = new SpecialAction();
	
	/**
	 * The special attack to use.
	 */
	private SpecialAttack specialAttack;
	
	/**
	 * Constructs a new {@code SpecialAction} {@code Object}.
	 */
	private SpecialAction() {
		super(false);
	}

	@Override
	public boolean commenceSession() {
		interaction.setSpecialAttack(specialAttack);
		interaction.getSource().getPlayer().getSettings().setUsingSpecial(false);
		ActionSender.sendConfig(interaction.getSource().getPlayer(), 301, 0);
		int amount = interaction.getSource().getPlayer().getSpecialAmount() - interaction.getSpecialAttack().getSpecialEnergyAmount();
		if (amount < 0) {
			ActionSender.sendMessage(interaction.getSource().getPlayer(), "You do not have enough power left.");
			return false;
		}
		if (interaction.getSpecialAttack().getCooldownTicks() > 0) {
			interaction.getSource().getCombatExecutor().setTicks(interaction.getSpecialAttack().getCooldownTicks());
		}
		if (specialAttack.commenceSpecialAttack(interaction)) {
			interaction.getSource().getPlayer().setSpecialAmount(amount);
			if (interaction.getSource().isPlayer() && interaction.getDamage() != null && !(specialAttack instanceof Disrupt)) {
				CombatUtils.appendExperience(interaction.getSource().getPlayer(), 
						interaction.getDamage().getHit(), specialAttack.getCombatType().getDamageType());
			}
			return true;
		}
		return false;
	}

	@Override
	public boolean executeSession() {
		return interaction.getSpecialAttack().tick(interaction);
	}

	@Override
	public boolean endSession() {
		return interaction.getSpecialAttack().endSpecialAttack(interaction);
	}

	@Override
	public CombatType getCombatType() {
		return specialAttack.getCombatType();
	}

	/**
	 * @return the singleton
	 */
	public static SpecialAction getSingleton() {
		return SINGLETON;
	}

	/**
	 * @return the specialAttack
	 */
	public SpecialAttack getSpecialAttack() {
		return specialAttack;
	}

	/**
	 * @param specialAttack the specialAttack to set
	 */
	public void setSpecialAttack(SpecialAttack specialAttack) {
		this.specialAttack = specialAttack;
	}

}
