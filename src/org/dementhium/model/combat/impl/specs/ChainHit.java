package org.dementhium.model.combat.impl.specs;

import java.util.List;

import org.dementhium.model.Projectile;
import org.dementhium.model.SpecialAttack;
import org.dementhium.model.World;
import org.dementhium.model.combat.Ammunition;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.ExtraTarget;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.RangeData;
import org.dementhium.model.combat.RangeFormulae;
import org.dementhium.model.combat.RangeWeapon;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.tickable.Tick;

/**
 * Executes the Rune thrownaxe special attack: -Chain hit.
 * @author Emperor
 *
 */
public class ChainHit extends SpecialAttack {
	
	/**
	 * The graphics.
	 */
	private static final Graphic GRAPHICS = Graphic.create(257, 96 << 16);
	
	/**
	 * The special attack projectile GFX id.
	 */
	private static final short ANIMATION = 1068;
	
	/**
	 * The special attack projectile GFX id.
	 */
	private static final short PROJECTILE_ID = 258;
		
	@Override
	public boolean commenceSpecialAttack(Interaction interaction) {
		RangeData data = new RangeData(true);
		data.setWeapon(RangeWeapon.get(interaction.getSource().getPlayer().getEquipment().getSlot(3)));
		data.setAmmo(Ammunition.get(interaction.getSource().getPlayer().getEquipment().getSlot(3)));
		if (data.getAmmo() == null || !data.getWeapon().getAmmunition().contains(data.getAmmo().getItemId())) {
			interaction.getSource().getPlayer().sendMessage("You do not have enough ammo left.");
			interaction.getSource().getCombatExecutor().reset();
			return false;
		}
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 8));
		}
		int maximum = RangeFormulae.getRangeDamage(interaction.getSource(), 1.0);
		if (interaction.getSource().isMulti() && interaction.getVictim().isMulti()) {
			List<ExtraTarget> targets = CombatUtils.getTargetList(interaction.getSource(), interaction.getVictim(), 14, 10);
			ExtraTarget toRemove = null;
			for (ExtraTarget e : targets) {
				if (e.getVictim() == interaction.getVictim()) {
					toRemove = e;
					continue;
				}
				e.setDamage(Damage.getDamage(interaction.getSource(), e.getVictim(), CombatType.RANGE, RangeFormulae.getDamage(interaction.getSource(), e.getVictim())));
				e.getDamage().setMaximum(maximum);
				CombatUtils.appendExperience(interaction.getSource().getPlayer(), e.getDamage().getHit(), DamageType.RANGE);
			}
			if (toRemove != null) {
				targets.remove(toRemove);
			}
			interaction.setTargets(targets);
		}
		data.setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.RANGE,RangeFormulae.getDamage(interaction.getSource(), interaction.getVictim())));
		data.getDamage().setMaximum(maximum);
		int speed = (int) (46 + (interaction.getSource().getLocation().distance(interaction.getVictim().getLocation()) * 5));
		ProjectileManager.sendProjectile(Projectile.create(interaction.getSource(), interaction.getVictim(), PROJECTILE_ID, 40, 36, 56, speed, 0, 11));
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
		if (interaction.getTargets() != null && interaction.getTargets().size() > 0) {
			final ExtraTarget first = interaction.getTargets().get(0);
			if (first != null && interaction.getSource().getPlayer().getSpecialAmount() >= getSpecialEnergyAmount()) {
				//int speed = (int) (46 + (interaction.getVictim().getLocation().distance(first.getVictim().getLocation()) * 5));
				ProjectileManager.sendProjectile(Projectile.create(interaction.getVictim(), first.getVictim(), PROJECTILE_ID, 40, 36, 32, 46, 5, 0));
				World.getWorld().submit(new Tick(1) {
					private ExtraTarget last = first;
					private int index = 1;
					@Override
					public void execute() {
						last.getVictim().getDamageManager().damage(
								interaction.getSource(), last.getDamage(), 
								DamageType.RANGE);
						if (last.getDamage().getVenged() > 0) {
							last.getVictim().submitVengeance(
									interaction.getSource(), last.getDamage().getVenged());
						}
						if (last.getDamage().getDeflected() > 0) {
							interaction.getSource().getDamageManager().damage(last.getVictim(),
									last.getDamage().getDeflected(), 
									last.getDamage().getDeflected(), DamageType.DEFLECT);
						}
						if (last.getDamage().getRecoiled() > 0) {
							interaction.getSource().getDamageManager().damage(last.getVictim(),
									last.getDamage().getRecoiled(), 
									last.getDamage().getRecoiled(), DamageType.DEFLECT);
						}
						//TODO: enable last.getVictim().retaliate(interaction.getSource());
						interaction.getSource().getPlayer().setSpecialAmount(interaction.getSource().getPlayer().getSpecialAmount() - getSpecialEnergyAmount());
						if (index == interaction.getTargets().size() || interaction.getSource().getPlayer().getSpecialAmount() < getSpecialEnergyAmount()) {
							stop();
							return;
						}
						ExtraTarget e = interaction.getTargets().get(index++);
						if (e == null) {
							stop();
							return;
						}
						//int speed = (int) (46 + (last.getVictim().getLocation().distance(e.getVictim().getLocation()) * 5));
						ProjectileManager.sendProjectile(Projectile.create(last.getVictim(), e.getVictim(), PROJECTILE_ID, 40, 36, 32, 46, 5, 0));
						last = e;
					}			
				});
			}
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
		return 100;
	}

	@Override
	public int getCooldownTicks() {
		return 5;
	}
	
}