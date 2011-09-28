package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.Location;
import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.map.Directions;
import org.dementhium.model.map.Directions.WalkingDirection;
import org.dementhium.model.map.path.PrimitivePathFinder;

/**
 * Executes the Dragon spear/Zamorakian spear special attack: Shove.
 * @author Emperor
 *
 */
public class Shove extends SpecialAttack {

	/**
	 * The special attack animation used.
	 */
	private static final short ANIMATION = 12017;
	
	/**
	 * The graphics id.
	 */
	private static final short GRAPHICS = 253;
	
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		if (interaction.getVictim().size() > 1) {
			interaction.getSource().getPlayer().sendMessage("This monster is too large to stun.");
			return false;
		}
		int vx = interaction.getVictim().getLocation().getX();
		int vy = interaction.getVictim().getLocation().getY();
		int sx = interaction.getSource().getLocation().getX();
		int sy = interaction.getSource().getLocation().getY();
		int x = 0;
		int y = 0;
		if (vx == sx && vy > sy) {
			y++;
		} else if (vx == sx && vy < sy) {
			y--;
		} else if (vx > sx && vy == sy) {
			x++;
		} else if (vx < sx && vy == sy) {
			x--;
		} else if (vx > sx && vy > sy) {
			x++;
			y++;
		} else if (vx < sx && vy > sy) {
			x--;
			y++;
		} else if (vx > sx && vy < sy) {
			x++;
			y--;
		} else if (vx < sx && vy < sy) {
			x--;
			y--;
		}
		interaction.getSource().animate(ANIMATION);
		interaction.getSource().graphics(GRAPHICS, 96 << 16);
		interaction.getVictim().stun(5, "You have been stunned.", true);
		interaction.getSource().getCombatExecutor().reset();
		interaction.getVictim().getCombatExecutor().reset();
		interaction.getSource().turnTo(interaction.getVictim());
		interaction.getVictim().turnTo(interaction.getSource());
		WalkingDirection direction = Directions.directionFor(x, y);
		System.out.println("X: " + x + ", y: " + y + ", dir: " + direction);
		if (PrimitivePathFinder.canMove(interaction.getVictim().getLocation(), direction, false)) {
			Location loc = interaction.getVictim().getLocation().getLocation(direction);
			interaction.getVictim().requestWalk(loc.getX(), loc.getY());
			System.out.println("Victim could move to " + loc);
		}
		return true;
	}

	@Override
	public boolean tick(Interaction interaction) {
		interaction.getVictim().turnTo(interaction.getSource());
		return true;
	}

	@Override
	public boolean endSpecialAttack(Interaction interaction) {
		interaction.getVictim().turnTo(interaction.getSource());
		return true;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MELEE;
	}

	@Override
	public int getSpecialEnergyAmount() {
		return 250;
	}

	@Override
	public int getCooldownTicks() {
		return 6;
	}

}
