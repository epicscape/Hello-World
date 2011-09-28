package org.dementhium.model;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

import org.dementhium.action.Action;
import org.dementhium.action.ActionManager;
import org.dementhium.content.activity.Activity;
import org.dementhium.content.activity.impl.DefaultActivity;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatExecutor;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.RangeData;
import org.dementhium.model.combat.impl.npc.ChaosElementalAction;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.mask.Mask;
import org.dementhium.model.mask.Sprites;
import org.dementhium.model.misc.DamageManager;
import org.dementhium.model.misc.DamageManager.DamageType;
import org.dementhium.model.misc.PoisonManager;
import org.dementhium.model.misc.WalkingQueue;
import org.dementhium.model.npc.NPC;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

public abstract class Mob extends Entity {
	
    public final static int[] VIEWPORT_SIZES = {104, 120, 136, 168};

    public static final Location DEFAULT = Location.locate(3087, 3491, 0);

    /**
     * The mob's default activity.
     */
    public static final Activity<?> DEFAULT_ACTIVITY = new DefaultActivity();

    /**
     * The mob's combat executor.
     */
    private final CombatExecutor combatExecutor;
    
    private final Random random = new Random();
    
    private int index;
    private boolean hidden;
    private boolean teleporting = false;

    private final Map<String, Object> attributes = new HashMap<String, Object>();
    private final ActionManager actionManager = new ActionManager(this);
    private final Sprites sprite = new Sprites();

    protected final Mask mask = new Mask(this);

    private final DamageManager damageManager = new DamageManager(this);
    private final PoisonManager poisonManager = new PoisonManager(this);

    /**
     * The mob's current activity.
     */
    private Activity<?> activity;

    protected WalkingQueue walkingQueue;

    private int[] forceWalk;

    private Map<String, Tick> ticks = new HashMap<String, Tick>();

    private boolean canAnimate = true;

    public Mob() {
        setLocation(DEFAULT);
        setActivity(DEFAULT_ACTIVITY);
        this.combatExecutor = new CombatExecutor(this);
    }

    public boolean isMulti() {
        if (World.getWorld().getAreaManager().getAreaByName("CastleWarsArea").contains(getLocation()) && activity != null && activity.getActivityId() == 0) {
            return true;
        }
        if (World.getWorld().getAreaManager().getAreaByName("TormentedDemons").contains(getLocation())) {
            return true;
        }
        if (World.getWorld().getAreaManager().getAreaByName("KingBlackDragonLair").contains(getLocation())) {
            return true;
        }
        if (World.getWorld().getAreaManager().getAreaByName("KalphiteQueenHive").contains(getLocation())) {
            return true;
        }
        if (World.getWorld().getAreaManager().getAreaByName("CorporealBeast").contains(getLocation())) {
            return true;
        }
        if (World.getWorld().getAreaManager().getAreaByName("SkeleHorror").contains(getLocation())) {
            return true;
        }
        if (ChaosElementalAction.COMBAT_AREA.contains(getLocation())) {
        	return true;
        }
        if (!World.getWorld().getAreaManager().getAreaByName("Edgeville").contains(getLocation())
                && !World.getWorld().getAreaManager().getAreaByName("Magebank").contains(getLocation())
                && !World.getWorld().getAreaManager().getAreaByName("Greendrags").contains(getLocation())
                && !World.getWorld().getAreaManager().getAreaByName("Greendragseast").contains(getLocation())
                && !World.getWorld().getAreaManager().getAreaByName("LavaMaze").contains(getLocation())
                && World.getWorld().getAreaManager().getAreaByName("Wilderness").contains(getLocation())) {
            return true;
        } else if (World.getWorld().getAreaManager().getAreaByName("Godwars").contains(getLocation())) {
            return true;
        }
        return false;
    }

    public boolean inWilderness() {
        if (isPlayer()) {
            return getPlayer().getPlayerArea().inWilderness();
        }
        return World.getWorld().getAreaManager().getAreaByName("Wilderness").contains(getLocation());
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public short getIndex() {
        return (short) index;
    }

    public void resetCombat() {
        resetTurnTo();
        combatExecutor.reset();
    }

    public int getClientIndex() {
        if (isPlayer()) {
            return this.index + 32768;
        } else {
            return this.index;
        }
    }

    public boolean isDead() {
        if (isPlayer()) {
            return getPlayer().getSkills().isDead();
        } else {
            return getNPC().isDead();
        }
    }

    /**
     * Checks if this Mob is attackable by another mob.
     * @param mob The attacking mob.
     * @return {@code True} if so, {@code false} if not.
     */
    public abstract boolean isAttackable(Mob mob);
    
    public void turnTo(Mob mob) {
        mask.setInteractingEntity(mob);
    }

    public void resetTurnTo() {
        mask.setInteractingEntity(null);
    }

    public void loadEntityVariables() {
        this.setHidden(false);
        this.setLocation(getLocation());
    }

    public void setHidden(boolean hidden) {
        this.hidden = hidden;
    }

    public boolean isHidden() {
        return hidden;
    }

    public boolean isAutoRetaliating() {
        if (this instanceof NPC)
            return true; //?
        else
            return getPlayer().getSettings().isAutoRetaliate();
    }

    public void setAttribute(String string, Object object) {
        attributes.put(string, object);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String string) {
        return (T) attributes.get(string);
    }

    @SuppressWarnings("unchecked")
    public <T> T getAttribute(String string, T fail) {
        T object = (T) attributes.get(string);
        if (object != null) {
            return object;
        }
        return fail;
    }

    public void removeAttribute(String string) {
        attributes.remove(string);
    }

    public Mask getMask() {
        return mask;
    }

    public boolean usingSpecial() {
        if (isPlayer()) {
            return getPlayer().getSettings().isUsingSpecial();
        }
        return false;
    }

    public void teleport(int x, int y, int z) {
        if (isPlayer()) {
            getPlayer().getRegion().teleport(x, y, z);
        } else {
            walkingQueue.reset();
            setLocation(Location.locate(x, y, z));
            setTeleporting(true);
        }
    }

    public void teleport(Location loc) {
        teleport(loc.getX(), loc.getY(), loc.getZ());
    }

    public boolean isTeleporting() {
        return teleporting;
    }

    public void setTeleporting(boolean teleporting) {
        this.teleporting = teleporting;
    }

    public Sprites getDir() {
        return sprite;
    }

    public void animate(int... args) {
        switch (args.length) {
            case 1:
                mask.setLastAnimation(Animation.create(args[0]));
                break;
            case 2:
                mask.setLastAnimation(Animation.create(args[0], args[1]));
                break;
            default:
                throw new IllegalArgumentException("Animation arguments can't be greater then 2");
        }

    }

    public void graphics(int... args) {
        switch (args.length) {
            case 1:
                mask.setLastGraphics(Graphic.create(args[0]));
                break;
            case 2:
                mask.setLastGraphics(Graphic.create(args[0], args[1]));
                break;
            case 3:
                mask.setLastGraphics(Graphic.create(args[0], args[1], args[2]));
                break;
            default:
                throw new IllegalArgumentException("Graphic arguments can't be greater then 3");
        }
    }

    public void graphics(Graphic graphic) {
        mask.setLastGraphics(graphic);
    }

    public void animate(Animation animation) {
        mask.setLastAnimation(animation);
    }

    public void animate(Animation animation, boolean ignoreFlag) {
        mask.setLastAnimation(animation, ignoreFlag);
    }

    public boolean isAnimating() {
        return mask.getLastAnimation() != null;
    }

    public boolean destroyed() {
        if (isPlayer()) {
            return !World.getWorld().getPlayers().contains(this);
        }
        return !World.getWorld().getNpcs().contains(this);
    }

    public void addPoint(int x, int y) {
        int firstX = x - (getLocation().getRegionX() - 6) * 8;
        int firstY = y - (getLocation().getRegionY() - 6) * 8;
        walkingQueue.addToWalkingQueue(firstX, firstY);
    }

    public void requestWalk(int x, int y) {
        int firstX = x - (getLocation().getRegionX() - 6) * 8;
        int firstY = y - (getLocation().getRegionY() - 6) * 8;
        walkingQueue.reset();
        walkingQueue.addToWalkingQueue(firstX, firstY);
    }

    public void requestClippedWalk(int x, int y) {
        int firstX = x - (getLocation().getRegionX() - 6) * 8;
        int firstY = y - (getLocation().getRegionY() - 6) * 8;
        walkingQueue.reset();
        walkingQueue.addClippedWalkingQueue(firstX, firstY);
    }


    public ActionManager getActionManager() {
        return actionManager;
    }

    public void registerAction(Action action) {
        actionManager.appendAction(action);
    }

    public abstract int getDefenceAnimation();

    public abstract int getAttackAnimation();

    public abstract int getAttackDelay();
    
    /**
     * Gets the current combat action.
     * @return The combat action to execute.
     */
    public abstract CombatAction getCombatAction();

    public abstract void forceText(String string);

    public int size() {
        if (isPlayer()) {
            return 1;
        }
        /*if (getNPC().isNex() || getNPC().getId() >= Nex.FUMUS && getNPC().getId() <= Nex.GLACIES || getNPC().getId() == 13458) {
            return 2;
        }*/
        return getNPC().getDefinition().getCacheDefinition().size;
    }

    public WalkingQueue getWalkingQueue() {
        if (walkingQueue == null) {
            walkingQueue = new WalkingQueue(this);
        }
        return walkingQueue;
    }

    public int[] getForceWalk() {
        return forceWalk;
    }

    public void forceMovement(Animation animation, int x, int y, int speed1, int speed2, int dir, int cycles, boolean removeAttribute) {
        forceMovement(animation, x, y, speed1, speed2, dir, cycles, removeAttribute, true);
    }

    public void forceMovement(Animation animation, int x, int y, int speed1, int speed2, int dir, int cycles, boolean removeAttribute, boolean teleport) {
        if (dir == -1) {
            if (location.getX() > x)
                dir = 3;
            if (location.getX() < x)
                dir = 4;
            if (location.getY() > y)
                dir = 2;
            if (location.getY() < y)
                dir = 0;
        }
        if (animation != null) {
            animate(animation);
        }
        setForceWalk(x, y, speed1, speed2, dir, cycles, removeAttribute, teleport);
        setCanAnimate(false);
        mask.setForceMovementUpdate(true);
        walkingQueue.reset();
        setAttribute("cantMove", Boolean.TRUE);
    }

    public void setForceWalk(final int x, final int y, final int speed1, final int speed2, final int dir, final int cycles, final boolean removeAttribute, final boolean teleport) {
        this.forceWalk = new int[]{x, y, speed1, speed2, dir, cycles};
        World.getWorld().submit(new Tick(forceWalk[5]) {
            @Override
            public void execute() {
                teleport(Location.locate(forceWalk[0], forceWalk[1], location.getZ()));
                setCanAnimate(true);
                if (removeAttribute) {
                    removeAttribute("busy");
                    removeAttribute("cantMove");
                }
                stop();
            }
        });
    }

    public void stun(int cycles, String stunMessage, boolean performGraphics) {
        if (isPlayer() && stunMessage != null) {
            getPlayer().sendMessage(stunMessage);
        }
        if (performGraphics) {
            graphics(Graphic.STUNNED_GRAPHIC);
        }
        setAttribute("stunned", Boolean.TRUE);
        setAttribute("cantMove", Boolean.TRUE);
        World.getWorld().submit(new Tick(cycles) {
            @Override
            public void execute() {
                stop();
                removeAttribute("stunned");
                removeAttribute("cantMove");
            }
        });
    }
    
	/**
	 * Submits the vengeance hit.
	 * @param source The mob who will be receiving the hit.
	 */
	public void submitVengeance(final Mob source, final int hit) {
		if (isNPC()) {
			//TODO: NPC vengeance.
			return;
		}
		if (!getAttribute("vengeance", false)) {
			return;
		}
		setAttribute("vengeance", false);
		forceText("Taste vengeance!");
		World.getWorld().submit(new Tick(1) {
			@Override
			public void execute() {
				source.getDamageManager().damage(getPlayer(), hit, -1, DamageType.RED_DAMAGE);
				stop();
			}			
		});
	}

    public DamageManager getDamageManager() {
        return damageManager;
    }

    public PoisonManager getPoisonManager() {
        return poisonManager;
    }

    public void submitTick(String identifier, Tick tick, boolean replace) {
        if (ticks.containsKey(identifier) && !replace) {
            return;
        }
        ticks.put(identifier, tick);
    }

    public void submitTick(String identifier, Tick tick) {
        submitTick(identifier, tick, false);
    }

    public void removeTick(String identifier) {
        if (hasTick("following_mob") && identifier.equals("following_mob")) {
            resetTurnTo();
        }
        Tick tick = ticks.get(identifier);
        if (tick != null) {
            tick.stop();
            ticks.remove(identifier);
        }
    }
    
    public Tick getTick(String identifier) {
    	return ticks.get(identifier);
    }

    public boolean hasTick(String string) {
        return ticks.containsKey(string);
    }

    public void processTicks() {
        if (ticks.isEmpty()) {
            return;
        }
        Map<String, Tick> ticks = new HashMap<String, Tick>(this.ticks);
        Iterator<Map.Entry<String, Tick>> it = ticks.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<String, Tick> entry = it.next();
            if (entry.getKey().equals("following_mob")) {
            	continue;
            }
            if (!entry.getValue().run()) {
                this.ticks.remove(entry.getKey());
            }
        }
    }

    public abstract int getHitPoints();

    public abstract int getMaximumHitPoints();

    public void setCanAnimate(boolean canAnimate) {
        this.canAnimate = canAnimate;
    }

    public boolean canAnimate() {
        return canAnimate;
    }

    public void heal(int amount) {
        if (isNPC()) {
            getNPC().heal(amount);
        } else if (isPlayer()) {
            getPlayer().getSkills().heal(amount);
        }
    }

    /**
     * @param activity the activity to set
     */
    public void setActivity(Activity<?> activity) {
        this.activity = activity;
    }

    /**
     * @return the activity
     */
    public Activity<?> getActivity() {
        return activity;
    }

    public int getViewportX() {
        return getViewportX(location);
    }

    public int getViewportX(Location base) {
        int depth = VIEWPORT_SIZES[getViewportDepth()];
        return location.getX() - Location.SECTORS_PER_REGION * (base.getRegionX() - (depth >> Location.SECTOR_LENGTH));
    }

    public int getViewportY() {
        return getViewportY(location);
    }

    public int getViewportY(Location base) {
        int depth = VIEWPORT_SIZES[getViewportDepth()];
        return location.getY() - Location.SECTORS_PER_REGION * (base.getRegionY() - (depth >> Location.SECTOR_LENGTH));
    }

    public int getViewportDepth() {
        return 0;
    }
    
    public Random getRandom() {
    	return random;
    }
    
    /**
     * Retaliates to the other mob.
     * @param other The other mob.
     * @return 
     */
    public abstract void retaliate(Mob other);

	/**
	 * @return the combatExecutor
	 */
	public CombatExecutor getCombatExecutor() {
		return combatExecutor;
	}

	/**
	 * Updates a hit (increase/decrease depending on the mob).
	 * @param source The mob dealing the hit.
	 * @param hit The hit to deal.
	 * @return The increased/decreased hit.
	 */
	public abstract Damage updateHit(Mob source, int hit, CombatType type);

	/**
	 * Gets the range data for a ranged combat action.
	 * @param victim The victim mob.
	 * @return The range data.
	 */
	public abstract RangeData getRangeData(Mob victim);

	/**
	 * Executes the tick before combat execution.
	 * @param interaction The interaction.
	 */
	public void preCombatTick(Interaction interaction) {
		interaction.getVictim().getCombatExecutor().setLastAttacker(this);
		if (interaction.getVictim().isPlayer()) {
			if (interaction.getSource().isPlayer() && interaction.getSource().inWilderness()) {
				interaction.getSource().getPlayer().getSkullManager().appendSkull(interaction.getVictim().getPlayer());
			}
			if (interaction.getVictim().getPlayer().getTradeSession() != null) {
				interaction.getVictim().getPlayer().getTradeSession().tradeFailed();
			}
			if (interaction.getVictim().getPlayer().getConnection() != null) {
				interaction.getVictim().getPlayer().getPriceCheck().close();
				ActionSender.sendCloseInterface(interaction.getVictim().getPlayer());
				ActionSender.sendCloseInventoryInterface(interaction.getVictim().getPlayer());
				ActionSender.sendCloseChatBox(interaction.getVictim().getPlayer());
			}
		}
	}

	/**
	 * Executes the tick after combat execution.
	 * @param interaction The interaction.
	 */
	public void postCombatTick(Interaction interaction) {
	}

	/**
	 * Checks if the mob is a familiar.
	 * @return {@code True} if so.
	 */
	public boolean isFamiliar() {
		return false;
	}
	
}