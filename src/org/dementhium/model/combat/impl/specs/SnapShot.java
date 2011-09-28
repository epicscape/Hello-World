package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.Projectile;
import org.dementhium.model.SpecialAttack;
import org.dementhium.model.combat.Ammunition;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.RangeData;
import org.dementhium.model.combat.RangeFormulae;
import org.dementhium.model.combat.RangeWeapon;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.ProjectileManager;

/**
 * Executes the magic shortbow special attack - Snap-shot.
 * @author Emperor
 * 
 */
public class SnapShot extends SpecialAttack {
	
	/**
	 * The graphics.
	 */
	private static final Graphic GRAPHICS = Graphic.create(256, 96 << 16);
	
	/**
	 * The animation id.
	 */
	private static final short ANIMATION = 1074;
	
	/**
	 * The special attack projectile GFX id.
	 */
	private static final short PROJECTILE_ID = 249;
	
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		RangeData data = new RangeData(true);
		data.setWeapon(RangeWeapon.get(interaction.getSource().getPlayer().getEquipment().getSlot(3)));
		data.setAmmo(Ammunition.get(interaction.getSource().getPlayer().getEquipment().getSlot(13)));
		if (data.getAmmo() == null || !data.getWeapon().getAmmunition().contains(data.getAmmo().getItemId())
				|| interaction.getSource().getPlayer().getEquipment().get(13).getAmount() < 2) {
			interaction.getSource().getPlayer().sendMessage("You do not have enough ammo left.");
			interaction.getSource().getCombatExecutor().reset();
			return false;
		}
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 8));
		}
		int maximum = RangeFormulae.getRangeDamage(interaction.getSource(), 1.0);
		data.setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.RANGE,RangeFormulae.getDamage(interaction.getSource(), interaction.getVictim(), 0.95, 1.0, 1.02)));
		data.getDamage().setMaximum(maximum);
		Damage secondHit = Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.RANGE,RangeFormulae.getDamage(interaction.getSource(), interaction.getVictim(), 0.95, 1.0, 1.02));
		secondHit.setMaximum(maximum);
		CombatUtils.appendExperience(interaction.getSource().getPlayer(), 
				secondHit.getHit(), DamageType.RANGE);
		interaction.getSource().setAttribute("secondHit", secondHit);
		int speed = (int) (27 + (interaction.getSource().getLocation().distance(interaction.getVictim().getLocation()) * 5));
		int speed2 = (int) (32 + (interaction.getSource().getLocation().distance(interaction.getVictim().getLocation()) * 10));
		ProjectileManager.sendProjectile(Projectile.create(interaction.getSource(), interaction.getVictim(), PROJECTILE_ID, 40, 36, 20, speed, 15, 11));
		ProjectileManager.sendProjectile(Projectile.create(interaction.getSource(), interaction.getVictim(), PROJECTILE_ID, 40, 36, 50, speed2, 15, 11));
		interaction.setTicks((int) Math.floor(interaction.getSource().getLocation().distance(interaction.getVictim().getLocation()) * 0.3));
		interaction.getSource().animate(ANIMATION);
		interaction.getSource().graphics(GRAPHICS);
		CombatUtils.dropArrows(interaction.getSource().getPlayer(), interaction.getVictim(), interaction.getRangeData());
		interaction.setRangeData(data);
		return true;
	}

	@Override
	public boolean tick(Interaction interaction) {
		if (interaction.getTicks() < 2) {
			interaction.getVictim().animate(interaction.isDeflected() ? 12573 
					: interaction.getVictim().getDefenceAnimation());
			if (interaction.isDeflected()) {
				interaction.getVictim().graphics(2229);
			}
		}
		interaction.setTicks(interaction.getTicks() - 1);
		return interaction.getTicks() < 1;
	}

	@Override
	public boolean endSpecialAttack(final Interaction interaction) {
		interaction.getVictim().getDamageManager().damage(
				interaction.getSource(), interaction.getRangeData().getDamage(), 
				DamageType.RANGE);
		if (interaction.getRangeData().getDamage().getVenged() > 0) {
			interaction.getVictim().submitVengeance(
					interaction.getSource(), interaction.getRangeData().getDamage().getVenged());
		}
		if (interaction.getRangeData().getDamage().getDeflected() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(),
					interaction.getRangeData().getDamage().getDeflected(), 
					interaction.getRangeData().getDamage().getDeflected(), DamageType.DEFLECT);
		}
		if (interaction.getRangeData().getDamage().getRecoiled() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(),
					interaction.getRangeData().getDamage().getRecoiled(), 
					interaction.getRangeData().getDamage().getRecoiled(), DamageType.DEFLECT);
		}
		interaction.getVictim().retaliate(interaction.getSource());
		Damage d = interaction.getSource().getAttribute("secondHit");
		if (d == null) {
			return true;
		}
		int delay = 1;
		interaction.getVictim().getDamageManager().damage(
				interaction.getSource(), d, 
				DamageType.RANGE, delay);
		if (d.getVenged() > 0) {
			interaction.getVictim().submitVengeance(
					interaction.getSource(), d.getVenged());
		}
		if (d.getDeflected() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(),
					d.getDeflected(), d.getDeflected(), DamageType.DEFLECT, delay);
		}
		if (d.getRecoiled() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(),
					d.getRecoiled(), d.getRecoiled(), DamageType.DEFLECT, delay);
		}
		return true;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGE;
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