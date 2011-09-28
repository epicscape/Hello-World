package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.Mob;
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
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.ProjectileManager;

/**
 * Executes the Dark bow's special attack: Descent of Dragons/Darkness.
 * @author Emperor
 *
 */
public class DescentOfDragons extends SpecialAttack {
	
	/**
	 * The special attack projectile GFX id.
	 */
	private static final int DRAGON_PROJECTILE = 1099;
	
	/**
	 * The special attack projectile GFX id.
	 */
	private static final int DARKNESS_PROJECTILE = 1101;
	
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
		boolean dragonArrows = ItemDefinition.forId(data.getAmmo().getItemId()).getName().contains("Dragon arrow");
		int minimum = dragonArrows ? 80 : 50;
		int maximum = RangeFormulae.getRangeDamage(interaction.getSource(), dragonArrows ? 1.5 : 1.3);
		interaction.getSource().setAttribute("dragonArrows", dragonArrows);
		int hit = RangeFormulae.getDamage(interaction.getSource(), interaction.getVictim(), 1.25, dragonArrows ? 1.5 : 1.3, 1);
		data.setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.RANGE, hit < minimum ? minimum : hit));
		hit = RangeFormulae.getDamage(interaction.getSource(), interaction.getVictim(), 1.25, dragonArrows ? 1.5 : 1.3, 1);
		data.setDamage2(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.RANGE, hit < minimum ? minimum : hit));
		data.getDamage().setMaximum(maximum);
		data.getDamage2().setMaximum(maximum);
		interaction.setTicks((int) Math.floor(interaction.getSource().getLocation().distance(interaction.getVictim().getLocation()) * 0.3));
		interaction.getSource().animate(data.getWeapon().getAnimationId());
		interaction.getSource().graphics(data.getAmmo().getDarkBowGraphics());
		sendProjectiles(interaction.getSource(), interaction.getVictim(), data.getAmmo());
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
	public boolean endSpecialAttack(Interaction interaction) {
		interaction.getVictim().graphics(
				interaction.getSource().getAttribute("dragonArrows") ? 1100 : 1103, 96);
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
		int delay = 18;
		interaction.getVictim().getDamageManager().damage(
				interaction.getSource(), interaction.getRangeData().getDamage2(), 
				DamageType.RANGE, delay);
		if (interaction.getRangeData().getDamage2().getDeflected() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(),
					interaction.getRangeData().getDamage2().getDeflected(), 
					interaction.getRangeData().getDamage2().getDeflected(), 
					DamageType.DEFLECT, delay);
		}
		if (interaction.getRangeData().getDamage2().getRecoiled() > 0) {
			interaction.getSource().getDamageManager().damage(interaction.getVictim(),
					interaction.getRangeData().getDamage2().getRecoiled(), 
					interaction.getRangeData().getDamage2().getRecoiled(), 
					DamageType.DEFLECT, delay);
		}
		interaction.getVictim().retaliate(interaction.getSource());
		return true;
	}

	/**
	 * Sends the projectiles.
	 * @param source The attacking player.
	 * @param victim The mob being attacked.
	 * @return {@code True}.
	 */
	private static boolean sendProjectiles(Mob source, Mob victim, Ammunition ammo) {
		int speed = (int) (46 + (source.getLocation().distance(victim.getLocation()) * 5));
		int speed2 = (int) (55 + (source.getLocation().distance(victim.getLocation()) * 10));
		if (source.getAttribute("dragonArrows")) {
			ProjectileManager.sendProjectile(Projectile.create(source, victim, DRAGON_PROJECTILE, 40, 36, 41, speed, 5, 11));
			ProjectileManager.sendProjectile(Projectile.create(source, victim, DRAGON_PROJECTILE, 40, 36, 41, speed2, 25, 11));
		} else {
			ProjectileManager.sendProjectile(Projectile.create(source, victim, ammo.getProjectileId(), 40, 36, 41, speed, 5, 11));
			ProjectileManager.sendProjectile(Projectile.create(source, victim, DARKNESS_PROJECTILE, 40, 36, 41, speed, 5, 11));
			ProjectileManager.sendProjectile(Projectile.create(source, victim, ammo.getProjectileId(), 40, 36, 41, speed2, 25, 11));
			ProjectileManager.sendProjectile(Projectile.create(source, victim, DARKNESS_PROJECTILE + 1, 40, 36, 41, speed2, 25, 11));
		}
		return true;
	}
	
	@Override
	public CombatType getCombatType() {
		return CombatType.RANGE;
	}

	@Override
	public int getSpecialEnergyAmount() {
		return 650;
	}

	@Override
	public int getCooldownTicks() {
		return 9;
	}

}