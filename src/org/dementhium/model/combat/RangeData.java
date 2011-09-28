package org.dementhium.model.combat;

import org.dementhium.model.Projectile;
import org.dementhium.model.mask.Graphic;

/**
 * Represents the range data for a ranged combat action cycle.
 * @author Emperor
 *
 */
public class RangeData {

	/**
	 * If the mob is a player.
	 */
	private final boolean player;
	
	/**
	 * The range weapon used.
	 */
	private RangeWeapon weapon;
	
	/**
	 * The ammunition used.
	 */
	private Ammunition ammo;
	
	/**
	 * The damage to deal.
	 */
	private Damage damage;
	
	/**
	 * The second damage to deal (dark bow).
	 */
	private Damage damage2;

	/**
	 * The projectile to send.
	 */
	private Projectile projectile;

	/**
	 * The second projectile to send (dark bow).
	 */
	private Projectile projectile2;
	
	/**
	 * The weapon type.
	 */
	private int weaponType;
	
	/**
	 * The animation id.
	 */
	private int animation;
	
	/**
	 * The graphics id.
	 */
	private Graphic graphics;
	
	/**
	 * If the ammunition needs to be dropped.
	 */
	private boolean dropAmmo;
	
	/**
	 * Constructs a new {@code RangeData} {@code Object}.
	 * @param player If the mob is a player.
	 */
	public RangeData(boolean player) {
		this.player = player;
	}

	/**
	 * @return the player
	 */
	public boolean isPlayer() {
		return player;
	}

	/**
	 * @return the weapon
	 */
	public RangeWeapon getWeapon() {
		return weapon;
	}

	/**
	 * @param weapon the weapon to set
	 */
	public void setWeapon(RangeWeapon weapon) {
		this.weapon = weapon;
	}

	/**
	 * @return the ammo
	 */
	public Ammunition getAmmo() {
		return ammo;
	}

	/**
	 * @param ammo the ammo to set
	 */
	public void setAmmo(Ammunition ammo) {
		this.ammo = ammo;
	}

	/**
	 * @return the damage
	 */
	public Damage getDamage() {
		return damage;
	}

	/**
	 * @param damage the damage to set
	 */
	public void setDamage(Damage damage) {
		this.damage = damage;
	}

	/**
	 * @return the damage2
	 */
	public Damage getDamage2() {
		return damage2;
	}

	/**
	 * @param damage2 the damage2 to set
	 */
	public void setDamage2(Damage damage2) {
		this.damage2 = damage2;
	}

	/**
	 * @return the projectile
	 */
	public Projectile getProjectile() {
		return projectile;
	}

	/**
	 * @param projectile the projectile to set
	 */
	public void setProjectile(Projectile projectile) {
		this.projectile = projectile;
	}

	/**
	 * @return the projectile2
	 */
	public Projectile getProjectile2() {
		return projectile2;
	}

	/**
	 * @param projectile2 the projectile2 to set
	 */
	public void setProjectile2(Projectile projectile2) {
		this.projectile2 = projectile2;
	}

	/**
	 * @return the animation
	 */
	public int getAnimation() {
		return animation;
	}

	/**
	 * @param animation the animation to set
	 */
	public void setAnimation(int animation) {
		this.animation = animation;
	}

	/**
	 * @return the graphics
	 */
	public Graphic getGraphics() {
		return graphics;
	}

	/**
	 * @param graphics the graphics to set
	 */
	public void setGraphics(Graphic graphics) {
		this.graphics = graphics;
	}

	/**
	 * @return the weaponType
	 */
	public int getWeaponType() {
		return weaponType;
	}

	/**
	 * @param weaponType the weaponType to set
	 */
	public void setWeaponType(int weaponType) {
		this.weaponType = weaponType;
	}

	/**
	 * @return the dropAmmo
	 */
	public boolean isDropAmmo() {
		return dropAmmo;
	}

	/**
	 * @param dropAmmo the dropAmmo to set
	 */
	public void setDropAmmo(boolean dropAmmo) {
		this.dropAmmo = dropAmmo;
	}
}
