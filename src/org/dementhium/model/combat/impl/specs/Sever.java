package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MeleeFormulae;

/**
 * Dragon scimitar - Sever<p>
 * 
 * A slash with increased accuracy that, if successful, <br>
 * prevents the target from using protection prayers for five seconds.<br>
 * Extra: Requires the completion of Monkey Madness to equip
 * 
 * @author Emperor
 * 
 */
public class Sever extends SpecialAttack {

	/**
	 * The special attack animation used.
	 */
	private static final short ANIMATION = 12031;
	
	/**
	 * The graphics id.
	 */
	private static final short GRAPHICS = 2118;
	
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		interaction.setDamage(Damage.getDamage(interaction.getSource(), 
				interaction.getVictim(), CombatType.MELEE, 
				MeleeFormulae.getDamage(interaction.getSource(), 
						interaction.getVictim(), 1.124, 1.0, 1.0)));
		interaction.getDamage().setMaximum(MeleeFormulae.getMeleeDamage(interaction.getSource(), 1));
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 9));
		}
		interaction.getSource().animate(ANIMATION);
		interaction.getSource().graphics(GRAPHICS, 96 << 16);
		if (interaction.getVictim().isPlayer() && interaction.getDamage().getHit() > 0) {
			interaction.getVictim().getPlayer().getPrayer().closeOnPrayers(0, new int[] {16, 17, 18, 19});
			interaction.getVictim().getPlayer().getPrayer().closeOnPrayers(1, new int[] {6, 7, 8, 9});
			interaction.getVictim().getPlayer().getPrayer().recalculatePrayer();
			interaction.getVictim().getPlayer().getMask().setApperanceUpdate(true);
			interaction.getVictim().getPlayer().setAttribute("restrict protection", 20);
		}
		interaction.getVictim().animate(interaction.isDeflected() ? 12573 : interaction.getVictim().getDefenceAnimation());
		if (interaction.isDeflected()) {
			interaction.getVictim().graphics(2230);
		}
		return true;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}
	
	@Override
	public int getSpecialEnergyAmount() {
		return 550;
	}

	@Override
	public int getCooldownTicks() {
		return 4;
	}

}