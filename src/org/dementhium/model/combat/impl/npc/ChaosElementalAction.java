package org.dementhium.model.combat.impl.npc;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.content.areas.Area;
import org.dementhium.model.Item;
import org.dementhium.model.Projectile;
import org.dementhium.model.World;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatTask;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.MagicFormulae;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.combat.RangeFormulae;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;

/**
 * Handles the chaos elemental's combat action.
 * @author Emperor
 *
 */
public class ChaosElementalAction extends CombatAction {

	/**
	 * The combat area.
	 */
	public static final Area COMBAT_AREA = World.getWorld().getAreaManager().getAreaByName("chaosElemental");

	/**
	 * An enum holding the 3 different attacks data.
	 * @author Emperor
	 *
	 */
	private static enum Attack {
		
		/**
		 * The default attack.
		 */
		PRIMARY(Graphic.create(556, 96 << 16), 
				Projectile.create(null, null, 557, 30, 32, 52, 75, 3, 11), 
				Graphic.create(558, 96 << 16), new CombatTask() {
					@Override
					public boolean execute(Interaction interaction) {
						return true;
					}
				}
		),
		
		/**
		 * The teleporting attack.
		 */
		TELEOTHER(Graphic.create(553, 96 << 16), 
				Projectile.create(null, null, 554, 30, 32, 52, 75, 3, 11), 
				Graphic.create(555, 96 << 16), new CombatTask() {
					@Override
					public boolean execute(Interaction interaction) {
						COMBAT_AREA.randomTeleport(interaction.getVictim());
						return false;
					}
				}
		),
		
		/**
		 * The disarming attack.
		 */
		DISARM(Graphic.create(550, 96 << 16), 
				Projectile.create(null, null, 551, 30, 32, 52, 75, 3, 11), 
				Graphic.create(552, 96 << 16), new CombatTask() {
					@Override
					public boolean execute(Interaction interaction) {
						if (!interaction.getVictim().isPlayer()) {
							return false;
						}
						Player p = interaction.getVictim().getPlayer();
						List<Item> items = new ArrayList<Item>();
						for (Item item : p.getEquipment().getContainer().toArray()) {
							if (item != null) {
								items.add(item);
							}
						}
						if (items.size() < 1) {
							return false;
						}
						Item item = items.get(p.getRandom().nextInt(items.size()));
						int targetSlot = Equipment.getItemType(item.getId());
				        if (item.getDefinition().getName().toLowerCase().contains("cape")) {
				            targetSlot = Equipment.SLOT_CAPE;
				        }
				        if (item.getDefinition().getName().toLowerCase().contains("mask")) {
				            targetSlot = Equipment.SLOT_HAT;
				        }
						p.getEquipment().unEquip(p, item.getId(), 0, targetSlot);
						return false;
					}
				}
		);
		
		/**
		 * The start graphic.
		 */
		private final Graphic start;
		
		/**
		 * The projectile to send.
		 */
		private final Projectile projectile;
		
		/**
		 * The end graphic.
		 */
		private final Graphic end;
		
		/**
		 * The combat task to execute.
		 */
		private final CombatTask task;
		
		/**
		 * Constructs a new {@code Attack} {@code Object}.
		 * @param start The start graphic.
		 * @param projectile The projectile to send.
		 * @param end The end graphic.
		 * @param task The combat task to execute.
		 */
		private Attack(Graphic start, Projectile projectile, Graphic end, CombatTask task) {
			this.start = start;
			this.projectile = projectile;
			this.end = end;
			this.task = task;
		}
	}
	
	/**
	 * The current combat type used.
	 */
	private CombatType type = CombatType.MAGIC;
	
	/**
	 * The current attack used.
	 */
	private Attack attack = Attack.PRIMARY;
	
	/**
	 * Constructs a new {@code ChaosElementalAction} {@code Object}.
	 */
	public ChaosElementalAction() {
		super(false);
	}

	@Override
	public boolean commenceSession() {
		attack = Attack.PRIMARY;
		interaction.getSource().getCombatExecutor().setTicks(5);
		if (interaction.getSource().getRandom().nextInt(10) < 5) {
			attack = Attack.values()[interaction.getSource().getRandom().nextInt(Attack.values().length)];
		}
		if (attack == Attack.PRIMARY) {
			int arg = interaction.getSource().getRandom().nextInt(10);
			int hit;
			if (arg < 3) {
				type = CombatType.RANGE;
				hit = RangeFormulae.getDamage(interaction.getSource(), interaction.getVictim());
			} else if (arg > 7) {
				type = CombatType.MELEE;
				hit = MeleeFormulae.getDamage(interaction.getSource(), interaction.getVictim());
			} else {
				type = CombatType.MAGIC;
				hit = MagicFormulae.getDamage(interaction.getSource().getNPC(), interaction.getVictim(), 1.0, 1.0, 1.0);
			}
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, type.getDeflectCurse()));
			interaction.setDamage(Damage.getDamage(interaction.getSource(), 
					interaction.getVictim(), type, hit));
			interaction.getDamage().setMaximum(284);
		}
		ProjectileManager.sendProjectile(attack.projectile.transform(interaction.getSource(), interaction.getVictim()));
		interaction.getSource().animate(interaction.getSource().getAttackAnimation());
		interaction.getSource().graphics(attack.start);
        int ticks = (int) Math.floor(attack.projectile.getSourceLocation().distance(interaction.getVictim().getLocation()) * 0.3);
		interaction.setTicks(ticks);
		return true;
	}

	@Override
	public boolean executeSession() {
		if (interaction.getTicks() < 2) {
			if (interaction.isDeflected()) {
				interaction.getVictim().graphics(2230 - type.ordinal());
			}
			interaction.getVictim().animate(interaction.isDeflected() ? 12573 : interaction.getVictim().getDefenceAnimation());
		}
		interaction.setTicks(interaction.getTicks() - 1);
		return interaction.getTicks() < 1;
	}

	@Override
	public boolean endSession() {
		interaction.getVictim().graphics(attack.end);
		if (attack.task.execute(interaction) && interaction.getDamage() != null) {
			if (interaction.getDamage().getHit() > -1) {
				interaction.getVictim().getDamageManager().damage(
						interaction.getSource(), interaction.getDamage(), type.getDamageType());
			} else {
				interaction.getVictim().graphics(85, 96 << 16);
			}
			if (interaction.getDamage().getVenged() > 0) {
				interaction.getVictim().submitVengeance(interaction.getSource(), interaction.getDamage().getVenged());
			}
			if (interaction.getDamage().getDeflected() > 0) {
				interaction.getSource().getDamageManager().damage(interaction.getVictim(), 
						interaction.getDamage().getDeflected(), 
						interaction.getDamage().getDeflected(), DamageType.DEFLECT);
			}
			if (interaction.getDamage().getRecoiled() > 0) {
				interaction.getSource().getDamageManager().damage(interaction.getVictim(), 
						interaction.getDamage().getRecoiled(), 
						interaction.getDamage().getRecoiled(), DamageType.DEFLECT);
			}
			interaction.getVictim().retaliate(interaction.getSource());
		}
		return true;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}

}