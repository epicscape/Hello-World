package org.dementhium.model.combat.impl.npc;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.content.skills.Prayer;
import org.dementhium.model.Location;
import org.dementhium.model.Projectile;
import org.dementhium.model.World;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.MagicFormulae;
import org.dementhium.model.combat.MeleeFormulae;
import org.dementhium.model.combat.RangeFormulae;
import org.dementhium.model.map.Region;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.npc.impl.TormentedDemon;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * Handles a Tormented demon's combat action.
 * @author Emperor
 *
 */
public class TormentedDemonAction extends CombatAction {
	
	/**
	 * The current combat type used.
	 */
	private CombatType type = CombatType.RANGE;
	
	/**
	 * The tormented demon's attacks.
	 * @author Emperor
	 *
	 */
	private static enum Attack {
		MELEE(Animation.create(10922), Graphic.create(1886), null, null),
		RANGE(Animation.create(10919), Graphic.create(1888), 
				Projectile.create(null, null, 1887, 46, 32, 56, 75, 3, 11), Graphic.create(-1)),
		MAGIC(Animation.create(10918), Graphic.create(-1), 
				Projectile.create(null, null, 1884, 46, 32, 56, 75, 3, 11), Graphic.create(1883, 96 << 16));
		
		/**
		 * The attack animation.
		 */
		private final Animation anim;
		
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
		 * Constructs a new {@code Attack} {@code Object}.
		 * @param anim The attack animation.
		 * @param start The start graphic.
		 * @param projectile The projectile.
		 * @param end The end graphic.
		 */
		private Attack(Animation anim, Graphic start, Projectile projectile, Graphic end) {
			this.anim = anim;
			this.start = start;
			this.projectile = projectile;
			this.end = end;
		}
	}
	
	/**
	 * The current attack.
	 */
	private Attack attack;
	
	/**
	 * Constructs a new {@code TormentedDemonAction} {@code Object}.
	 * @param demon The tormented demon using this combat action.
	 */
	public TormentedDemonAction() {
		super(false);
	}

	@Override
	public boolean commenceSession() {
		interaction.getSource().getCombatExecutor().setTicks(interaction.getSource().getAttackDelay());
		attack = Attack.values()[type.ordinal()];
		int currentHit = 0;
		int maximum = 269;
		if (attack == Attack.MELEE) {
			maximum = 189;
			currentHit = MeleeFormulae.getDamage(interaction.getSource(), interaction.getVictim());
		} else if (attack == Attack.RANGE) {
			currentHit = RangeFormulae.getDamage(interaction.getSource(), interaction.getVictim());
		} else {
			currentHit = MagicFormulae.getDamage(interaction.getSource().getNPC(), interaction.getVictim(), 1.0, 1.0, 1.0);
		}
		interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, type.getDeflectCurse()));
		interaction.setDamage(Damage.getDamage(interaction.getSource(), 
				interaction.getVictim(), type, currentHit));
		interaction.getDamage().setMaximum(maximum);
		if (attack.projectile != null) {
			ProjectileManager.sendProjectile(attack.projectile.transform(interaction.getSource(), interaction.getVictim()));
		}
		interaction.getSource().animate(attack.anim);
		interaction.getSource().graphics(attack.start);
        int ticks = attack.projectile != null ? (int) Math.floor(attack.projectile.getSourceLocation().distance(interaction.getVictim().getLocation()) * 0.3)
        		: 1;
		interaction.setTicks(ticks);
		return true;
	}

	@Override
	public boolean executeSession() {
		if (interaction.getTicks() < 2 || type == CombatType.MELEE) {
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
		if (attack != Attack.MELEE) {
			interaction.getVictim().graphics(attack.end);
		}
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
		return true;
	}

	/**
	 * Sends the magic-based location attack.
	 * @param npc The tormented demon.
	 */
	public static void sendLocationAttack(final TormentedDemon demon) {
		List<Player> players = Region.getLocalPlayers(demon.getLocation(), 12);
		if (players.size() < 1) {
			return;
		}
		Player player = players.get(demon.getRandom().nextInt(players.size()));
		List<Location> locations = new ArrayList<Location>();
		int clippingMask;
		for (int x = player.getLocation().getX() - 5; x < player.getLocation().getX() + 6; x++) {
			for (int y = player.getLocation().getY() - 5; y < player.getLocation().getY() + 6; y++) {
				clippingMask = Region.getClippingMask(x, y, 0);
		        if ((clippingMask & 0x1280180) == 0 && (clippingMask & 0x1280108) == 0
		        		&& (clippingMask & 0x1280120) == 0 && (clippingMask & 0x1280102) == 0) {
		        	locations.add(Location.locate(x, y, 0));
		        }
			}
		}
		if (locations.size() < 1) {
			return;
		}
		final Location l = locations.get(demon.getRandom().nextInt(locations.size()));
		ProjectileManager.sendProjectile(demon.getProjectile().transform(demon, l, true, 46, 10));
		int ticks = (int) Math.floor(demon.getProjectile().getSourceLocation().distance(l) * 0.5);
		World.getWorld().submit(new Tick(ticks + 1) {
			@Override
			public void execute() {
				stop();
				List<Player> players = Region.getLocalPlayers(l, 5);
				for (Player p : players) {
					ActionSender.sendPositionedGraphic(p, l, 1883);
					int damage = 269;
					if (p.getPrayer().usingPrayer(0, Prayer.PROTECT_FROM_MAGIC)) {
						damage = 1;
						p.animate(p.getDefenceAnimation());
					} else if (p.getPrayer().usingPrayer(1, Prayer.DEFLECT_MAGIC)) {
						damage = 1;
						p.graphics(2228);
						p.animate(12573);
					}
					p.getDamageManager().damage(demon, p.getRandom().nextInt(damage), 269, DamageType.MAGE);
				}
			}			
		});
	}
	
	@Override
	public CombatType getCombatType() {
		return type;
	}

	/**
	 * Sets the current combat type.
	 * @param combatType The combat type.
	 */
	public void setType(CombatType combatType) {
		this.type = combatType;
	}

}