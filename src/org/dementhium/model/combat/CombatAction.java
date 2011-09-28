package org.dementhium.model.combat;

import org.dementhium.util.misc.CycleState;

/**
 * Represents a combat action to execute.
 * @author Emperor
 *
 */
public abstract class CombatAction {
	
	/**
	 * The current interaction.
	 */
	protected Interaction interaction;
	
	/**
	 * If the combat action should be executed instantly (i.e melee combat).
	 */
	private final boolean instant;
	
	/**
	 * Constructs a new {@code CombatAction} {@code Object}.
	 * @param instant If the action should be executed instantly.
	 * @see #instant
	 */
	public CombatAction(boolean instant) {
		this.instant = instant;
	}

	/**
	 * Updates the combat action.
	 * @param stop If the interaction should be reset.
	 */
	public void execute() {
		switch (interaction.getState()) {
		case COMMENCE:
			if (commenceSession()) {
				interaction.getSource().preCombatTick(interaction);
				if (instant || getCombatType() == CombatType.MELEE) {
					interaction.setState(CycleState.FINALIZE);
					return;
				}
				interaction.setState(CycleState.EXECUTE);
				return;
			}
			stop();
			return;
		case EXECUTE:
			if (executeSession()) {
				interaction.setState(CycleState.FINALIZE);
				return;
			}
			return;
		case FINALIZE:
			if (instant || getCombatType() == CombatType.MELEE) {
				executeSession();
			}
			if (endSession()) {
				interaction.getSource().postCombatTick(interaction);
				stop();
			}
			return;
		}
	}
	
	/**
	 * Commences the attack cycle.
	 * @return {@code True} if succesfull, <br>
	 * 			{@code false} if not (will end the combat session).
	 */
	public abstract boolean commenceSession();
	
	/**
	 * Executes the attack cycle.
	 * @return  {@code True} if succesfull, <br>
	 * 			{@code false} if not (will keep trying to execute).
	 */
	public abstract boolean executeSession();
	
	/**
	 * Ends the attack cycle.
	 * @return {@code True} if succesfull, <br>
	 * 			{@code false} if not (will keep trying to end the session).
	 */
	public abstract boolean endSession();
	
	/**
	 * Gets the {@code CombatType} used.
	 * @return The combat type.
	 */
	public abstract CombatType getCombatType();
	
	/**
	 * Gets the cooldown ticks used for this cycle.
	 * @return The amount of ticks to cool down.
	 */
	public int getCooldownTicks() {
		return interaction.getSource().getAttackDelay();
	}
	
	/**
	 * Stops the combat.
	 */
	public void stop() {
		//interaction.getSource().getCombatExecutor().setInteraction(
			//	new Interaction(interaction.getSource(), interaction.getVictim()));
		interaction.setState(CycleState.FINISHED);
	}

	/**
	 * @return the interaction
	 */
	public Interaction getInteraction() {
		return interaction;
	}

	/**
	 * @param interaction the interaction to set
	 */
	public void setInteraction(Interaction interaction) {
		this.interaction = interaction;
	}
	
	/**
	 * @return the instant
	 */
	public boolean isInstant() {
		return instant;
	}
}