package org.dementhium.model.combat.impl.specs;

import org.dementhium.model.Projectile;
import org.dementhium.model.SpecialAttack;
import org.dementhium.model.World;
import org.dementhium.model.combat.Ammunition;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.RangeData;
import org.dementhium.model.combat.RangeFormulae;
import org.dementhium.model.combat.RangeWeapon;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.tickable.Tick;

/**
 * Executes the Saradomin bow's special attack Restorative shot.
 * @author Emperor
 * 
 */
public class RestorativeShot extends SpecialAttack {
	
	/**
	 * The end graphics.
	 */
	private static final short END_GRAPHICS = 128;
	
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		RangeData data = new RangeData(true);
		data.setWeapon(RangeWeapon.get(interaction.getSource().getPlayer().getEquipment().getSlot(3)));
		data.setAmmo(Ammunition.get(interaction.getSource().getPlayer().getEquipment().getSlot(13)));
		if (data.getAmmo() == null || !data.getWeapon().getAmmunition().contains(data.getAmmo().getItemId())) {
			interaction.getSource().getPlayer().sendMessage("You do not have enough ammo left.");
			interaction.getSource().getCombatExecutor().reset();
			return false;
		}
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 8));
		}
		int maximum = RangeFormulae.getRangeDamage(interaction.getSource(), 1);
		data.setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.RANGE,RangeFormulae.getDamage(interaction.getSource(), interaction.getVictim(), 1.2, 1.0, 1.0)));
		data.getDamage().setMaximum(maximum);
		int speed = (int) (46 + (interaction.getSource().getLocation().distance(interaction.getVictim().getLocation()) * 5));
		ProjectileManager.sendProjectile(Projectile.create(interaction.getSource(), interaction.getVictim(), data.getAmmo().getProjectileId(), 40, 36, 41, speed, 5, 11));
		interaction.setTicks((int) Math.floor(interaction.getSource().getLocation().distance(interaction.getVictim().getLocation()) * 0.3));
		interaction.getSource().animate(data.getWeapon().getAnimationId());
		interaction.getSource().graphics(data.getAmmo().getStartGraphics());
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
		interaction.getVictim().graphics(END_GRAPHICS);
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
		if (interaction.getDamage().getHit() > 0) {
			World.getWorld().submit(new Tick(10) {
				private int count = 5;
				@Override
				public void execute() {
					if (--count == -1) {
						this.stop();
						return;
					}
					interaction.getSource().getPlayer().getSkills().heal(interaction.getDamage().getHit() / 5);
				}				
			});
		}
		interaction.getVictim().retaliate(interaction.getSource());
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
		return 6;
	}

}