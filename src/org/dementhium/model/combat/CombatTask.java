package org.dementhium.model.combat;


/**
 * A combat task that can be used for end-effects for several attacks.
 * @author Emperor
 *
 */
public interface CombatTask {

	/**
	 * Executes the task.
	 * @param interaction The current interaction.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	boolean execute(Interaction interaction);
	
}