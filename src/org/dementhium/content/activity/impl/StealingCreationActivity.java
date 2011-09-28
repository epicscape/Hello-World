package org.dementhium.content.activity.impl;

import org.dementhium.content.activity.Activity;
import org.dementhium.model.player.Player;

/**
 * Handles the activity: {@code Stealing} {@code Creation}.
 *
 * @author Emperor
 */
public class StealingCreationActivity extends Activity<Player> {

    /**
     * Constructs a new {@code StealingCreationActivity}.
     */
    public StealingCreationActivity() {
        super();
    }

    @Override
    public boolean initializeActivity() {
        /*
           * As this is a global, continuing activity, it will initialize regardless.
           */
        return true;
    }

    @Override
    public boolean commenceSession() {
        /*
           * As this is a global, continuing activity, it will commence regardless.
           */
        return true;
    }

    @Override
    public boolean updateSession() {
        return true;
    }

    @Override
    public boolean endSession() {
        // TODO Auto-generated method stub
        return false;
    }
}