package org.dementhium.model.combat.impl;

import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatType;

/**
 * The default action for NPCs without combat.
 * @author Emperor
 *
 */
public class DefaultAction extends CombatAction {

	/**
	 * Constructs a new {@code DefaultAction} {@code Object}.
	 */
	public DefaultAction() {
		super(true);
	}

	@Override
	public boolean commenceSession() {
		getInteraction().getSource().getCombatExecutor().reset();
		return false;
	}

	@Override
	public boolean executeSession() {
		return false;
	}

	@Override
	public boolean endSession() {
		return true;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.DEFAULT;
	}

	@Override
	public int getCooldownTicks() {
		return 4;
	}

}
