package org.dementhium.model;


/**
 * Represents a projectile to send.
 * @author Emperor
 *
 */
public class Projectile {

	/**
	 * The source node.
	 */
	private Mob source;
	
	/**
	 * The source's centered location.
	 */
	private Location sourceLocation;
	
	/**
	 * The victim.
	 */
	private Mob victim;
	
	/**
	 * The projectile's gfx id.
	 */
	private int projectileId;
	
	/**
	 * The start height.
	 */
	private int startHeight;
	
	/**
	 * The ending height.
	 */
	private int endHeight;
	
	/**
	 * The type.
	 */
	private int type;
	
	/**
	 * The speed.
	 */
	private int speed;
	
	/**
	 * The angle.
	 */
	private int angle;
	
	/**
	 * The distance to start.
	 */
	private int distance;
	
	/**
	 * The end location (used for location based projectiles).
	 */
	private Location endLocation;
	
	/**
	 * Creates a new projectile.
	 * @param source The source entity.
	 * @param victim The victim.
	 * @param projectileId The projectile's gfx id;
	 * @return The created projectile.
	 */
	public static Projectile create(Mob source, Mob victim, int projectileId) {
		int speed = (int) (46 + (getLocation(source).distance(victim.getLocation()) * 5));
		return new Projectile(source, victim, projectileId, 40, 36, 41, speed, 5, source.size() << 6);
	}
	
	/**
	 * Creates a new projectile.
	 * @param source The source node.
	 * @param victim The victim.
	 * @param projectileId The projectile's gfx id;
	 * @param startHeight The starting height.
	 * @param endHeight The ending height.
	 * @return The created projectile.
	 */
	public static Projectile create(Mob source, Mob victim, int projectileId, int startHeight, int endHeight) {
		int speed = (int) (46 + (getLocation(source).distance(victim.getLocation()) * 5));
		return new Projectile(source, victim, projectileId, startHeight, endHeight, 41, speed, 5, source.size() << 6);
	}
	
	/**
	 * Creates a new projectile.
	 * @param source The source node.
	 * @param victim The victim.
	 * @param projectileId The projectile's gfx id;
	 * @param startHeight The starting height.
	 * @param endHeight The ending height.
	 * @param type The projectile type.
	 * @return The created projectile.
	 */
	public static Projectile create(Mob source, Mob victim, int projectileId, int startHeight, int endHeight, int type) {
		int speed = (int) (46 + (getLocation(source).distance(victim.getLocation()) * 5));
		return new Projectile(source, victim, projectileId, startHeight, endHeight, type, speed, 5, source.size() << 6);
	}
	
	/**
	 * Creates a new projectile.
	 * @param source The source node.
	 * @param victim The victim.
	 * @param projectileId The projectile's gfx id;
	 * @param startHeight The starting height.
	 * @param endHeight The ending height.
	 * @param type The projectile type.
	 * @param speed The projectile speed.
	 * @return The created projectile.
	 */
	public static Projectile create(Mob source, Mob victim, int projectileId, int startHeight, int endHeight, int type, int speed) {
		return new Projectile(source, victim, projectileId, startHeight, endHeight, type, speed, 5, source.size() << 6);
	}
	
	/**
	 * Creates a new projectile.
	 * @param source The source node.
	 * @param victim The victim.
	 * @param projectileId The projectile's gfx id;
	 * @param startHeight The starting height.
	 * @param endHeight The ending height.
	 * @param type The projectile type.
	 * @param speed The projectile speed.
	 * @param angle The angle.
	 * @return The created projectile.
	 */
	public static Projectile create(Mob source, Mob victim, int projectileId, int startHeight, int endHeight, int type, int speed, int angle) {
		return new Projectile(source, victim, projectileId, startHeight, endHeight, type, speed, angle, source.size() << 6);
	}
	
	/**
	 * Creates a new projectile.
	 * @param source The source node.
	 * @param victim The victim.
	 * @param projectileId The projectile's gfx id;
	 * @param startHeight The starting height.
	 * @param endHeight The ending height.
	 * @param type The projectile type.
	 * @param speed The projectile speed.
	 * @param angle The angle.
	 * @param distance The distance to start from.
	 * @return The created projectile.
	 */
	public static Projectile create(Mob source, Mob victim, int projectileId, int startHeight, int endHeight, int type, int speed, int angle, int distance) {
		return new Projectile(source, victim, projectileId, startHeight, endHeight, type, speed, angle, distance);
	}
	
	/**
	 * Creates a new magic-speed based projectile.
	 * @param source The source node.
	 * @param victim The victim.
	 * @param projectileId The projectile's gfx id;
	 * @param startHeight The starting height.
	 * @param endHeight The ending height.
	 * @param type The projectile type.
	 * @param angle The angle.
	 * @return The created projectile.
	 */
	public static Projectile magic(Mob source, Mob victim, int projectileId, int startHeight, int endHeight, int type, int angle) {
		int speed = (int) (46 + (getLocation(source).distance(victim.getLocation()) * 10));
		return new Projectile(source, victim, projectileId, startHeight, endHeight, type, speed, angle, 11);
	}
	
	/**
	 * Creates a new range-speed based projectile.
	 * @param source The source node.
	 * @param victim The victim.
	 * @param projectileId The projectile's gfx id;
	 * @param startHeight The starting height.
	 * @param endHeight The ending height.
	 * @param type The projectile type.
	 * @param angle The angle.
	 * @return The created projectile.
	 */
	public static Projectile ranged(Mob source, Mob victim, int projectileId, int startHeight, int endHeight, int type, int angle) {
		int speed = (int) (46 + (getLocation(source).distance(victim.getLocation()) * 5));
		return new Projectile(source, victim, projectileId, startHeight, endHeight, type, speed, angle, 11);
	}
	
	/**
	 * Constructs a new {@code Projectile} {@code Object}.
	 */
	public Projectile() {
		/*
		 * empty.
		 */
	}
	
	/**
	 * Constructs a new projectile.
	 * @param source The source node.
	 * @param victim The entity victim.
	 * @param projectileId The projectile gfx id.
	 * @param startHeight The start height.
	 * @param endHeight The end height.
	 * @param type The type of the projectile.
	 * @param speed The projectile speed.
	 * @param angle The projectile angle.
	 * @param distance The distance.
	 */
	private Projectile(Mob source, Mob victim, int projectileId, int startHeight, int endHeight, int type, int speed, int angle, int distance) {
		this.source = source;
		this.sourceLocation = getLocation(source);
		this.victim = victim;
		this.projectileId = projectileId;
		this.startHeight = startHeight;
		this.endHeight = endHeight;
		this.type = type;
		this.speed = speed;
		this.angle = angle;
		this.distance = distance;
	}
	
	/**
	 * Gets the source location on construction.
	 * @param n The node.
	 * @return The centered location.
	 */
	public static Location getLocation(Mob n) {
		if (n == null) {
			return null;
		}
		if (n.isNPC()) {
			int size = n.size() / 2;
			return n.getNPC().getLocation().transform(size, size, 0);
		}
		return n.getLocation();
	}
	
	/**
	 * Changes the projectile so it sends from the source mob to the victim mob given.
	 * @param source The source mob.
	 * @param victim The victim mob.
	 * @return The projectile instance.
	 */
	public Projectile transform(Mob source, Mob victim) {
		return transform(source, victim, source.isNPC(), 46, 5);
	}
	
	/**
	 * Changes the projectile so it sends from the source mob to the victim mob given.
	 * @param source The source mob.
	 * @param victim The victim mob.
	 * @param npc If the source should be handled as an NPC.
	 * @param baseSpeed The base speed.
	 * @param modifiedSpeed The modified speed.
	 * @return The projectile instance.
	 */
	public Projectile transform(Mob source, Mob victim, boolean npc, int baseSpeed, int modifiedSpeed) {
		this.source = source;
		this.sourceLocation = getLocation(source);
		this.victim = victim;
		this.speed = (int) (baseSpeed + sourceLocation.distance(victim.getLocation()) * modifiedSpeed);
		if (npc) {
			this.distance = source.size() << 6;
		}
		return this;
	}

	/**
	 * Transforms the projectile so it is location based and it sends to the location.
	 * @param source The source mob.
	 * @param l The location.
	 * @return The projectile instance.
	 */
	public Projectile transform(Mob source, Location l) {
		return transform(source, l, source.isNPC(), 46, 5);
	}
	
	/**
	 * Transforms a projectile to be location based, with updated parameters.
	 * @param source The mob sending this projectile.
	 * @param l The end location.
	 * @param npc If the mob should be handled as an npc.
	 * @param baseSpeed The base speed.
	 * @param modifiedSpeed The modified speed.
	 * @return The projectile instance.
	 */
	public Projectile transform(Mob source, Location l, boolean npc, int baseSpeed, int modifiedSpeed) {
		this.source = source;
		this.sourceLocation = getLocation(source);
		this.endLocation = l;
		this.speed = (int) (baseSpeed + sourceLocation.distance(l) * modifiedSpeed);
		if (npc) {
			this.distance = source.size() << 6;
		}
		return this;
	}

	/**
	 * @param source The source node.
	 */
	public void setSource(Mob source) {
		this.source = source;
	}

	/**
	 * @return The source node.
	 */
	public Mob getSource() {
		return source;
	}

	/**
	 * @param sourceLocation the sourceLocation to set
	 */
	public void setSourceLocation(Location sourceLocation) {
		this.sourceLocation = sourceLocation;
	}

	/**
	 * @return the sourceLocation
	 */
	public Location getSourceLocation() {
		return sourceLocation;
	}

	/**
	 * @param victim The entity victim.
	 */
	public void setVictim(Mob victim) {
		this.victim = victim;
	}

	/**
	 * @return The entity victim.
	 */
	public Mob getVictim() {
		return victim;
	}

	/**
	 * @param projectileId the projectileId to set
	 */
	public void setProjectileId(int projectileId) {
		this.projectileId = projectileId;
	}

	/**
	 * @return the projectileId
	 */
	public int getProjectileId() {
		return projectileId;
	}

	/**
	 * @param startHeight the startHeight to set
	 */
	public void setStartHeight(int startHeight) {
		this.startHeight = startHeight;
	}

	/**
	 * @return the startHeight
	 */
	public int getStartHeight() {
		return startHeight;
	}

	/**
	 * @param endHeight the endHeight to set
	 */
	public void setEndHeight(int endHeight) {
		this.endHeight = endHeight;
	}

	/**
	 * @return the endHeight
	 */
	public int getEndHeight() {
		return endHeight;
	}

	/**
	 * @param type the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param speed the speed to set
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * @return the speed
	 */
	public int getSpeed() {
		return speed;
	}

	/**
	 * @param angle the angle to set
	 */
	public void setAngle(int angle) {
		this.angle = angle;
	}

	/**
	 * @return the angle
	 */
	public int getAngle() {
		return angle;
	}

	/**
	 * @param distance the distance to set
	 */
	public void setDistance(int distance) {
		this.distance = distance;
	}

	/**
	 * @return the distance
	 */
	public int getDistance() {
		return distance;
	}

	/**
	 * Checks if the projectile is location based.
	 * @return {@code True} if so, {@code false} if not.
	 */
	public boolean isLocationBased() {
		return endLocation != null;
	}
	
	/**
	 * @return the endLocation
	 */
	public Location getEndLocation() {
		return endLocation;
	}

	/**
	 * @param endLocation the endLocation to set
	 */
	public void setEndLocation(Location endLocation) {
		this.endLocation = endLocation;
	}

}