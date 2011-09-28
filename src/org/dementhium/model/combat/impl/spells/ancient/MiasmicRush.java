package org.dementhium.model.combat.impl.spells.ancient;

import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.Projectile;
import org.dementhium.model.World;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MagicFormulae;
import org.dementhium.model.combat.MagicSpell;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 * Handles the Miasmic rush ancient spell.
 * @author Emperor
 *
 */
public class MiasmicRush extends MagicSpell {
	
	@Override
	public boolean castSpell(Interaction interaction) {
		int weaponId = interaction.getSource().getPlayer().getEquipment().getSlot(3);
		if (weaponId != 13867 && weaponId != 13869 && weaponId != 13941 && weaponId != 13943) {
			ActionSender.sendMessage(interaction.getSource().getPlayer(), "You need Zuriel's staff to cast this spell.");
			interaction.getVictim().getCombatExecutor().reset();
			return false;
		}
		MagicFormulae.setDamage(interaction);
		int speed = (int) (46 + interaction.getSource().getLocation().getDistance(interaction.getVictim().getLocation()) * 10);
		ProjectileManager.sendProjectile(Projectile.create(interaction.getSource(), interaction.getVictim(), 1846, 43, 22, 51, speed, 16, 64));
		interaction.getSource().animate(10513);
		interaction.getSource().graphics(1845);
		if (interaction.getDamage().getHit() > -1 && interaction.getVictim().getAttribute("miasmicImmunity", -1) < World.getTicks()) {
			if (interaction.getVictim().isPlayer()) {
				ActionSender.sendMessage(interaction.getVictim().getPlayer(), "You feel slowed down.");
			}
			interaction.getVictim().setAttribute("miasmicTime", World.getTicks() + 20);
			interaction.getVictim().setAttribute("miasmicImmunity", World.getTicks() + 35);
		}
		interaction.setEndGraphic(Graphic.create(1847));
		return true;
	}

	@Override
	public double getExperience(Interaction interaction) {
		double xp = 35;
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
		return 60;
	}
	
	@Override
	public int getAutocastConfig() {
		return 95;
	}

	@Override
	public Item[] getRequiredRunes() {
		return new Item[] { new Item(566, 1), new Item(562, 2), new Item(557, 1) };
	}
}