package org.dementhium;

import org.dementhium.content.misc.PunishHandler;
import org.dementhium.io.XMLHandler;
import org.dementhium.model.World;
import org.dementhium.model.player.Player;


/**
 * Our shutdown hook.LOL
 *
 * @author Emperor
 */
public class DementhiumShutdownHook extends Thread {

    /**
     * The singleton.
     */
    private static final DementhiumShutdownHook SINGLETON = new DementhiumShutdownHook();

    /**
     * Constructs a new {@code DementhiumShutdownHook} {@code Object}.
     */
    private DementhiumShutdownHook() {
        System.out.println("Shutdown hook initialized!");
    }

    /**
     * If the shutdown hook is/has running/runned.
     */
    public boolean activated = false;

    @Override
    public void run() {
        activated = true;
        System.out.println("Shutting down Dementhium...");
        int failCount = 0;
        System.out.println("Preparing players for shutdown...");
        for (Player player : World.getWorld().getPlayers()) {
            if (player == null) {
                continue;
            }
            try {
                if (player.getActivity() != null) {
                    player.getActivity().forceEnd(player);
                }
                if (player.getTradeSession() != null) {
                    player.getTradeSession().tradeFailed();
                }
                if (player.getPriceCheck() != null && player.getPriceCheck().isOpen()) {
                    player.getPriceCheck().close();
                }
                failCount = 0;
                while (!World.getWorld().getPlayerLoader().save(player)) {
                    if (failCount++ > 2) {
                        System.out.println("Player " + player.getUsername() + " could not be saved!");
                        break;
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
        System.out.println("Players saved.");
        System.out.println("Saving clans...");
        try {
            XMLHandler.toXML(PunishHandler.DIRECTORY + "clans.xml",
                    World.getWorld().getClanManager().getClans());
            System.out.println("Clans succesfully saved.");
        } catch (Throwable e) {
            e.printStackTrace();
        }
        System.out.println("Saving punishments...");
        World.getWorld().getPunishHandler().save();
        System.out.println("Saved punishments.");
    }

    /**
     * @return the singleton
     */
    public static DementhiumShutdownHook getSingleton() {
        return SINGLETON;
    }
}
