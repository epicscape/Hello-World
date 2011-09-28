package org.dementhium.model.combat.impl.spells.modern;

import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MagicFormulae;
import org.dementhium.model.combat.MagicSpell;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.npc.impl.Impling;
import org.dementhium.model.player.Player;

/**
 * Handles the bind spell.
 * @author Emperor
 *
 */
public class Bind extends MagicSpell {

	@Override
	public boolean castSpell(Interaction interaction) {
		MagicFormulae.setDamage(interaction);
		interaction.getSource().turnTo(interaction.getVictim());
		interaction.getSource().animate(710);
		interaction.getSource().graphics(177, 96 << 16);
		interaction.setEndGraphic(Graphic.create(181, 96 << 16));
		if (interaction.getDamage().getHit() > -1 && interaction.getVictim().getAttribute("freezeImmunity", -1) < World.getTicks()) {
			interaction.getVictim().setAttribute("freezeTime", World.getTicks() + 8);
			interaction.getVictim().setAttribute("freezeImmunity", World.getTicks() + 13);
		}
		return true;
	}

	@Override
	public boolean endSpell(Interaction interaction) {
		if (interaction.getVictim() instanceof Impling) {
			interaction.getVictim().graphics(interaction.getEndGraphic());
			return true;
		}
		return super.endSpell(interaction);
	}
	
	@Override
	public double getExperience(Interaction interaction) {
		if (interaction.getVictim() instanceof Impling) {
			return 30;
		}
		if (interaction.getDamage() != null && interaction.getDamage().getHit() > -1) {
			return 30 + interaction.getDamage().getHit();
		}
		return 30;
	}

	@Override
	public int getStartDamage(Player source, Mob victim) {
		return 10;
	}

	@Override
	public int getNormalDamage() {
		return 9;
	}

	@Override
	public int getBaseDamage() {
		return 5;
	}

	@Override
	public Item[] getRequiredRunes() {
		return new Item[] { new Item(555, 3), new Item(557, 3), new Item(561, 2) };
	}

}
