package org.dementhium.model.combat.impl.spells.ancient;

import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MagicFormulae;
import org.dementhium.model.combat.MagicSpell;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 * Handles the Ice blitz ancient spell.
 * @author Emperor
 *
 */
public class IceBlitz extends MagicSpell {

	@Override
	public boolean castSpell(Interaction interaction) {
		MagicFormulae.setDamage(interaction);
		interaction.getSource().animate(1978);
		interaction.getSource().graphics(366, 124 << 16);
		if (interaction.getDamage().getHit() > -1 && interaction.getVictim().getAttribute("freezeImmunity", -1) < World.getTicks()) {
			int delay = 25;
			interaction.getVictim().getCombatExecutor().reset();
			if (interaction.getVictim().isPlayer()) {
				ActionSender.sendMessage(interaction.getVictim().getPlayer(), "You have been frozen.");
				if (interaction.isDeflected() || interaction.getVictim().getPlayer().getPrayer().usingPrayer(0, 17)) {
					delay *= 0.5;
				}
			}
			interaction.getVictim().getWalkingQueue().reset();
			interaction.getVictim().setAttribute("freezeTime", World.getTicks() + delay);
			interaction.getVictim().setAttribute("freezeImmunity", World.getTicks() + delay + 4);
		}
		interaction.setEndGraphic(Graphic.create(367));
		return true;
	}

	@Override
	public double getExperience(Interaction interaction) {
		double xp = 46;
		if (interaction.getDamage().getHit() > 0) {
			xp += interaction.getDamage().getHit() * 0.2;
		}
		return xp;
	}

	@Override
	public int getStartDamage(Player source, Mob victim) {
		return 220 + getBaseDamage();
	}

	@Override
	public int getNormalDamage() {
		return 18;
	}

	@Override
	public int getBaseDamage() {
		return 40;
	}
	
	@Override
	public int getAutocastConfig() {
		return 85;
	}
	
	@Override
	public int getExtraDelay() {
		return 2;
	}

	@Override
	public Item[] getRequiredRunes() {
		return new Item[] { new Item(555, 3), new Item(565, 2), new Item(560, 2) };
	}
}