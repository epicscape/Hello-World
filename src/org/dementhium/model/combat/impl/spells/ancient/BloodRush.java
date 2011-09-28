package org.dementhium.model.combat.impl.spells.ancient;

import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MagicFormulae;
import org.dementhium.model.combat.MagicSpell;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/** 
 * Handles the Blood rush ancient spell.
 * @author Emperor
 *
 */
public class BloodRush extends MagicSpell {

	@Override
	public boolean castSpell(Interaction interaction) {
		MagicFormulae.setDamage(interaction);
		interaction.getSource().animate(1978);
		if (interaction.getDamage().getHit() > 0) {
			if (interaction.getVictim().isPlayer()) {
				ActionSender.sendMessage(interaction.getVictim().getPlayer(), "Your lifepoints have been drained.");
			}
			interaction.getSource().heal((int) (interaction.getDamage().getHit() * 0.25));
			ActionSender.sendMessage(interaction.getSource().getPlayer(), "You drain some of your opponents' lifepoints.");
		}
		interaction.setEndGraphic(Graphic.create(373));
		return true;
	}

	@Override
	public double getExperience(Interaction interaction) {
		double xp = 33;
		if (interaction.getDamage().getHit() > 0) {
			xp += interaction.getDamage().getHit() * 0.2;
		}
		return xp;
	}

	@Override
	public int getStartDamage(Player source, Mob victim) {
		return 140 + getBaseDamage();
	}

	@Override
	public int getNormalDamage() {
		return 9;
	}

	@Override
	public int getBaseDamage() {
		return 30;
	}
	
	@Override
	public int getAutocastConfig() {
		return 67;
	}

	@Override
	public Item[] getRequiredRunes() {
		return new Item[] { new Item(565, 1), new Item(562, 2), new Item(560, 2) };
	}
}