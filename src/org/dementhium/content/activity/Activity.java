package org.dementhium.content.activity;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.event.EventListener.ClickOption;
import org.dementhium.model.Entity;
import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.player.Player;
import org.dementhium.tickable.Tick;

/**
 * The {@code Activity abstract class} is representing a minigame/activity session,
 * and is used to ease updating and handeling of this session.
 *
 * @param <E> The entity type involved in this minigame (usually Player).
 * @author Emperor
 */
public abstract class Activity<E extends Entity> extends Tick {

    /**
     * The default cycle time constant (1 tick).
     */
    public static final short DEFAULT_CYCLE_TIME = 1;

    /**
     * Represents the session state flags.
     *
     * @author Emperor
     */
    public static enum SessionStates {
        PAUSE_STATE,
        INIT_STATE,
        COMMENCE_STATE,
        UPDATE_STATE,
        END_STATE;
    }

    /**
     * The player.
     */
    protected final Player player;

    /**
     * A {@link List} containing all the other players in this minigame/activity session.
     */
    private final List<E> entities;

    /**
     * The state the minigame is in.
     */
    private SessionStates activityState = SessionStates.INIT_STATE;

    /**
     * The activity's identification digit.
     */
    private int activityId;

    /**
     * A constructor setting the player instance to {@code null} <br>
     * and cycle time will be 600 milliseconds per tick.<p>
     * <b><u>Warning: sets the Player to null.</u></b>
     */
    public Activity() {
        this(null, DEFAULT_CYCLE_TIME);
    }

    /**
     * A constructor setting a player instance <br>
     * and cycle time will be 600 milliseconds per tick.
     *
     * @param player The player.
     */
    public Activity(Player player) {
        this(player, DEFAULT_CYCLE_TIME);
    }

    /**
     * The constructor.
     *
     * @param player        The "source" player.
     * @param cycleDuration The sleep time between each cycle tick.
     */
    public Activity(Player player, int cycleDuration) {
        super(cycleDuration);
        this.player = player;
        this.entities = new ArrayList<E>();
    }

    /**
     * Initializes the minigame/activity.
     *
     * @return {@code True} if the initializing succesfully occured, {@code false} if not.
     */
    public abstract boolean initializeActivity();

    /**
     * Commences the minigame/activity, <u><b>only</b> if the initializing returned {@code true}.</u>
     *
     * @return {@code True} if the session could be commenced, {@code false} if not.
     */
    public abstract boolean commenceSession();

    /**
     * Ends the activity/minigame session.
     *
     * @return {@code True} if the session ended succesfully, <br>
     *         {@code false} if the session wasn't ready to end yet <br>
     *         <i>(this method will keep being called instead of the update session method until the session has ended).</i>
     */
    public abstract boolean endSession();

    /**
     * This method gets called when the player teleports.
     *
     * @param player The player.
     * @return {@code True} if the player can continue its teleport,
     *         <br>		{@code false} if not.
     */
    public boolean onTeleport(Player player) {
        return true;
    }

    /**
     * This method gets called when the player dies.
     *
     * @param player The player dying.
     * @return {@code True} if the player keeps his stuff,
     *         <br>		{@code false} if the player drops his stuff (like a normal death event).
     */
    public boolean onDeath(Player player) {
        return false;
    }

    /**
     * Updates when walking.
     *
     * @param player The player walking.
     * @return {@code True} if the player can walk, {@code false} if not.
     */
    public boolean walkingUpdate(Player player) {
        return true;
    }

    /**
     * Checks if the player can logout.
     *
     * @param player       The player.
     * @param logoutButton If the player used the logout button.
     * @return {@code True} if so, {@code false} if not.
     */
    public boolean canLogout(Player player, boolean logoutButton) {
        return true;
    }

    /**
     * Checks if the minigame is a combat minigame.
     *
     * @param mob    The attacking mob.
     * @param victim The victim.
     * @return {@code True} if so, {@code false} if not.
     */
    public boolean isCombatActivity(Mob mob, Mob victim) {
        return true;
    }

    /**
     * The {@code updateSession} method will update the session (if overridden)
     * <br>only if the session has succesfully commenced.
     *
     * @return {@code True} if the session succesfully updated, or if this method didn't get {@code Override}, <br>
     *         {@code false} if not.
     */
    public boolean updateSession() {
        return true;
    }

    /**
     * Forces this activity to shut down for this player.
     * <br>(should only be called in the shutdown hook/uncaught exception handler).
     *
     * @param player The player.
     * @return {@code True}.
     */
    public boolean forceEnd(Player player) {
        return true;
    }

    /**
     * Attempts to add a entity to the {@code entities} {@link List}.
     *
     * @param entity The entity to add.
     * @return {@code True} if the entity got succesfully added, <br>
     *         {@code false} if the {@code Entity List} already contained the player's index.
     */
    public boolean addEntity(E entity) {
        if (entity == null) {
            System.out.println("[" + this + "]: Session already contained entity " + entity + ".");
            return false;
        }
        entities.add(entity);
        return true;
    }

    @Override
    public String toString() {
        return this.getClass().getSimpleName();
    }

    @Override
    public void stop() {
        stop(true);
    }

    /**
     * Stops the {@code Activity}.
     *
     * @param endSession If the {@code Activity} session should also be ended.
     */
    public void stop(boolean endSession) {
        super.stop();
        ActivityManager.getSingleton().unregister(this, endSession);
    }

    @Override
    public void execute() {
        switch (activityState) {
            case PAUSE_STATE:
                return;
            case INIT_STATE:
                if (this.initializeActivity()) {
                    this.setActivityState(SessionStates.COMMENCE_STATE);
                }
                return;
            case COMMENCE_STATE:
                if (this.commenceSession()) {
                    this.setActivityState(SessionStates.UPDATE_STATE);
                }
                return;
            case UPDATE_STATE:
                this.updateSession();
                return;
            case END_STATE:
                if (this.endSession()) {
                    this.stop(false);
                }
        }
    }

    /**
     * Attempts to do an object action.
     *
     * @param player   The player.
     * @param object   The object.
     * @param actionId The option id.
     * @return {@code True} if the object got executed, {@code false} if not.
     */
    public boolean objectAction(Player player, GameObject object, ClickOption actionId) {
        return false;
    }

    /**
     * Attempts to do an item action.
     *
     * @param player   The player.
     * @param item     The item.
     * @param actionId The action id.
     * @param action   The action performed. (ex. "unequip", "equip", "drop", "operate", ...)
     * @param params   an object array of possible parameters.
     * @return {@code True} if the item action got executed, {@code false} if not.
     */
    public boolean itemAction(Player player, Item item, int actionId, String action, Object... params) {
        return false;
    }

    /**
     * @return the player
     */
    public Player getPlayer() {
        return player;
    }

    /**
     * Gets an entity (other than the "source" player) from the {@code entities} {@link List}.
     *
     * @param id The entity index.
     * @return The entity, or null if the entity didn't enter this activity.
     */
    public E getEntity(int id) {
        return entities.get((short) id);
    }

    /**
     * @return the entities
     */
    public List<E> getEntities() {
        return entities;
    }

    /**
     * @param activityState the activityState to set
     */
    public void setActivityState(SessionStates activityState) {
        this.activityState = activityState;
    }

    /**
     * @return the activityState
     */
    public SessionStates getActivityState() {
        return activityState;
    }

    /**
     * @param activityId the activityId to set
     */
    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    /**
     * @return the activityId
     */
    public int getActivityId() {
        return activityId;
    }
}