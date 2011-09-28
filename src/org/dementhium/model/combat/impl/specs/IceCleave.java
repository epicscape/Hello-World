package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.World;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;

/**
 * Executes the Zamorak godsword special attack - Ice cleave.
 * @author Emperor
 *
 */
public class IceCleave extends SpecialAttack {

	/**
	 * The special attack animation used.
	 */
	private static final short ANIMATION = 7070;
	
	/**
	 * The graphics id.
	 */
	private static final short GRAPHICS = 2110;
	
	/**
	 * The end graphics id.
	 */
	private static final short END_GRAPHICS = 2111;
	
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		interaction.setDamage(Damage.getDamage(interaction.getSource(), 
				interaction.getVictim(), CombatType.MELEE, 
				MeleeFormulae.getDamage(interaction.getSource(), 
						interaction.getVictim(), 1.175, 1.0, 0.998)));
		interaction.getDamage().setMaximum(MeleeFormulae.getMeleeDamage(interaction.getSource(), 1.0));
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
		}
		if (interaction.getDamage().getHit() > 0) {
			if (interaction.getVictim().isPlayer()) {
				interaction.getVictim().getPlayer().sendMessage("You have been frozen.");
			}
			interaction.getVictim().getCombatExecutor().reset();
			interaction.getVictim().getWalkingQueue().reset();
			interaction.getVictim().setAttribute("freezeTime", World.getTicks() + 33);
		}
		interaction.getSource().animate(ANIMATION);
		interaction.getSource().graphics(GRAPHICS);
		interaction.getVictim().animate(interaction.isDeflected() ? 12573 : interaction.getVictim().getDefenceAnimation());
		if (interaction.isDeflected()) {
			interaction.getVictim().graphics(2230);
		}
		interaction.getVictim().graphics(END_GRAPHICS);
		return true;
	}
	
	@Override
	public boolean endSpecialAttack(Interaction interaction) {
		return super.endSpecialAttack(interaction);
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}
	
	@Override
	public int getSpecialEnergyAmount() {
		return 600;
	}

	@Override
	public int getCooldownTicks() {
		return 6;
	}

}