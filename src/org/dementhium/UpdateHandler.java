package org.dementhium;

import org.dementhium.model.World;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

/**
 * Handles the updating of the server.
 *
 * @author Emperor
 */
public class UpdateHandler extends Tick {

    /**
     * The singleton instance.
     */
    private static final UpdateHandler SINGLETON = new UpdateHandler();

    /**
     * The update seconds.
     */
    private int updateSeconds;

    /**
     * Constructs a new {@code UpdateHandler} {@code Object},
     * <br>with a cycle of {@code 1}.
     */
    private UpdateHandler() {
        super(1);
        stop();
    }

    @Override
    public void execute() {
        updateSeconds--;
        if (updateSeconds < 0) {
            stop();
            RS2ServerBootstrap.restart(null, 1);
        }
    }

    /**
     * Refreshes the system update.
     */
    public void refresh() {
        for (Player p : World.getWorld().getPlayers()) {
            ActionSender.sendSystemUpdate(p, updateSeconds);
        }
    }

    /**
     * @return the singleton
     */
    public static UpdateHandler getSingleton() {
        return SINGLETON;
    }

    /**
     * @return the updateSeconds
     */
    public int getUpdateSeconds() {
        return updateSeconds;
    }

    /**
     * @param updateSeconds the updateSeconds to set
     */
    public void setUpdateSeconds(int updateSeconds) {
        this.updateSeconds = (int) (updateSeconds * 1.7);
        System.out.println("Test 2.");
    }
}