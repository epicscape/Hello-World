package org.dementhium.model.combat.impl;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.model.Item;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.ExtraTarget;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.RangeFormulae;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.net.ActionSender;

/**
 * Handles a chinchoma range combat cycle.
 * @author Emperor
 *
 */
public class ChinchompaAction extends CombatAction {

	/**
	 * The singleton.
	 */
	private static final ChinchompaAction SINGLETON = new ChinchompaAction();
	
	/**
	 * The end graphic.
	 */
	private static final Graphic END_GRAPHIC = Graphic.create(2739, 96 << 16);
	
	/**
	 * Constructs a new {@code ChinchompaAction} {@code Object}.
	 */
	private ChinchompaAction() {
		super(false);
	}

	@Override
	public boolean commenceSession() {
		interaction.setDamage(null);
		int maximum = RangeFormulae.getRangeDamage(interaction.getSource(), 1.0);
		List<ExtraTarget> targets;
		if (!interaction.getSource().isMulti() || !interaction.getVictim().isMulti()) {
			targets = new ArrayList<ExtraTarget>();
			targets.add(new ExtraTarget(interaction.getVictim()));
		} else {
			targets = CombatUtils.getTargetList(interaction.getSource(), interaction.getVictim(), 1, 9);
		}
		for (ExtraTarget e : targets) {
			if (e.getVictim().isPlayer()) {
				e.setDeflected(e.getVictim().getPlayer().getPrayer().usingPrayer(1, 8));
			}
			e.setDamage(Damage.getDamage(interaction.getSource(), e.getVictim(), CombatType.RANGE, RangeFormulae.getDamage(interaction.getSource(), e.getVictim())));
			e.getDamage().setMaximum(maximum);
			Interaction inter = new Interaction(interaction.getSource(), e.getVictim());
			inter.setDamage(e.getDamage());
			interaction.getSource().preCombatTick(inter);
			CombatUtils.appendExperience(interaction.getSource().getPlayer(), 
						e.getDamage().getHit(), DamageType.RANGE);
		}
		interaction.getSource().getPlayer().getEquipment().getContainer().remove(new Item(interaction.getRangeData().getAmmo().getItemId(), 1));
		interaction.getSource().getPlayer().getEquipment().refresh();
		if (interaction.getSource().getPlayer().getEquipment().getSlot(3, -1) == -1) {
			ActionSender.sendMessage(interaction.getSource().getPlayer(), "You've ran out of ammo.");
		}
		ProjectileManager.sendProjectile(interaction.getRangeData().getProjectile());
		interaction.setTicks((int) Math.floor(getInteraction().getSource().getLocation().distance(getInteraction().getVictim().getLocation()) * 0.3));
		interaction.getSource().animate(getInteraction().getRangeData().getAnimation());
		interaction.getSource().graphics(getInteraction().getRangeData().getGraphics());
		interaction.setTargets(targets);
		return true;
	}

	@Override
	public boolean executeSession() {
		if (interaction.getTicks() < 2) {
			for (ExtraTarget e : interaction.getTargets()) {
				e.getVictim().animate(e.isDeflected() ? 12573 
						: e.getVictim().getDefenceAnimation());
				if (e.isDeflected()) {
					e.getVictim().graphics(2229);
				}
			}
		}
		interaction.setTicks(interaction.getTicks() - 1);
		return interaction.getTicks() < 1;
	}

	@Override
	public boolean endSession() {
		for (ExtraTarget e : interaction.getTargets()) {
			e.getVictim().graphics(END_GRAPHIC);
			e.getVictim().getDamageManager().damage(
					interaction.getSource(), e.getDamage(), DamageType.RANGE);
			if (e.getDamage().getVenged() > 0) {
				e.getVictim().submitVengeance(
						interaction.getSource(), e.getDamage().getVenged());
			}
			if (e.getDamage().getDeflected() > 0) {
				interaction.getSource().getDamageManager().damage(e.getVictim(),
						e.getDamage().getDeflected(), e.getDamage().getDeflected(), DamageType.DEFLECT);
			}
			if (e.getDamage().getRecoiled() > 0) {
				interaction.getSource().getDamageManager().damage(e.getVictim(),
						e.getDamage().getRecoiled(), e.getDamage().getRecoiled(), DamageType.DEFLECT);
			}
			e.getVictim().retaliate(interaction.getSource());
		}
		return true;
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.RANGE;
	}

	/**
	 * @return the singleton
	 */
	public static ChinchompaAction getSingleton() {
		return SINGLETON;
	}

}
