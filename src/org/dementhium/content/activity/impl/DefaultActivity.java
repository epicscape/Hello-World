package org.dementhium.content.activity.impl;

import org.dementhium.content.activity.Activity;
import org.dementhium.model.Mob;

/**
 * The default activity.
 *
 * @author Emperor
 */
public class DefaultActivity extends Activity<Mob> {

    /**
     * Constructs a new {@code DefaultActivity} {@code Object}.
     */
    public DefaultActivity() {
        stop(false);
        setActivityId(-1);
    }

    @Override
    public boolean initializeActivity() {
        return false;
    }

    @Override
    public boolean commenceSession() {
        return false;
    }

    @Override
    public boolean endSession() {
        return false;
    }

}
