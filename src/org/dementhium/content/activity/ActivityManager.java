package org.dementhium.content.activity;

import org.dementhium.model.World;

import java.util.ArrayList;
import java.util.List;

/**
 * The activity managing class.
 *
 * @author Emperor
 */
public class ActivityManager {

    /**
     * The {@code ActivityManager} instance.
     */
    private static final ActivityManager SINGLETON = new ActivityManager();

    /**
     * A {@link List} holding all the currently occuring player activities.
     */
    private List<Activity<?>> activities = new ArrayList<Activity<?>>();

    /**
     * The constructor.
     */
    public ActivityManager() {
        /*
           * empty.
           */
    }

    /**
     * Registers a new activity.
     *
     * @param activity The activity.
     */
    public boolean register(Activity<?> activity) {
        if (activity == null) {
            return false;
        }
        activities.add(activity);
        activity.setActivityId(activities.indexOf(activity));
        World.getWorld().submit(activity);
        return true;
    }

    /**
     * Unregisters an activity.
     *
     * @param key        The activity's key to remove.
     * @param endSession If the session should be ended as well.
     */
    public boolean unregister(int key, boolean endSession) {
        if (key >= activities.size()) {
            return false;
        }
        Activity<?> a = activities.get(key);
        if (a != null) {
            if (endSession) {
                a.endSession();
            }
            activities.remove(key);
            return true;
        }
        return false;
    }

    /**
     * Unregisters an activity.
     *
     * @param activity The activity to remove.
     */
    public boolean unregister(Activity<?> activity) {
        return unregister(activity.getActivityId(), true);
    }

    /**
     * Unregisters an activity.
     *
     * @param activity   The activity to remove.
     * @param endSession If the session should also be ended.
     */
    public boolean unregister(Activity<?> activity, boolean endSession) {
        return unregister(activity.getActivityId(), endSession);
    }

    /**
     * Resets all activities.
     *
     * @return {@code True} if all activities were succesfully removed, <br>
     *         {@code false} if one or more activities couldn't be removed.
     */
    public boolean reset() {
        for (Activity<?> a : activities) {
            if (a != null && !unregister(a)) {
                return false;
            }
        }
        return true;
    }

    /**
     * @param playerActivities the playerActivities to set
     */
    public void setActivities(List<Activity<?>> activities) {
        this.activities = activities;
    }

    /**
     * @return the playerActivities
     */
    public List<Activity<?>> getActivities() {
        return activities;
    }

    /**
     * Gets the {@code ActivityManager} instance.
     *
     * @return The instance
     */
    public static ActivityManager getSingleton() {
        return SINGLETON;
    }

}