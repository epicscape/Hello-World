package org.dementhium.model.player;

import org.dementhium.model.World;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;

import java.util.ArrayList;
import java.util.List;

/**
 * @author 'Mystic Flow
 */
public final class FriendManager {

    private final List<String> friends = new ArrayList<String>(200);
    private final List<String> ignores = new ArrayList<String>(100);
    private final Player player;

    public FriendManager(Player player) {
        this.player = player;
    }

    public void loadFriendList() {
        if (friends.size() > 0) {
            for (String friend : friends) {
                Player other = World.getWorld().getPlayerInServer(friend);
                boolean isOnline = other != null;
                ActionSender.sendFriend(player, friend, friend, isOnline ? 0 : 1, isOnline, false, other != null && other.getConnection().isInLobby());
            }
        } else {
            ActionSender.sendUnlockFriendList(player);
        }
    }

    public void loadIgnoreList() {
        for (String Ignore : ignores) {
            ActionSender.sendIgnore(player, Ignore, Ignore);
        }
    }

    public void updateFriend(String friend) {
        Player other = World.getWorld().getPlayerInServer(friend);
        boolean isOnline = other != null;
        ActionSender.sendFriend(player, friend, friend, isOnline ? 0 : 1, isOnline, true, other != null && other.getConnection().isInLobby());
    }

    public void updateFriend(String friend, Player other) {
        boolean isOnline = other != null;
        ActionSender.sendFriend(player, friend, friend, isOnline ? 0 : 1, isOnline, true, other != null && other.getConnection().isInLobby());
    }


    public void addFriend(String name) {
        if (friends.size() >= 200
                || name == null
                || name.equals("")
                || friends.contains(name)
                || ignores.contains(name)
                || name.equals(Misc.formatPlayerNameForDisplay(player.getUsername())
        ))
            return;
        friends.add(Misc.formatPlayerNameForDisplay(name));
        updateFriend(Misc.formatPlayerNameForDisplay(name));
    }

    public void addIgnore(String ignore) {
        if (ignores.size() >= 100 || ignore == null || friends.contains(ignore) || ignores.contains(ignore))
            return;
        ignores.add(Misc.formatPlayerNameForDisplay(ignore));
        ActionSender.sendIgnore(player, ignore, Misc.formatPlayerNameForDisplay(ignore));
    }

    public void removeIgnore(String ignore) {
        if (ignore == null || !ignores.contains(ignore))
            return;
        ignores.remove(Misc.formatPlayerNameForDisplay(ignore));
    }

    public void removeFriend(String friend) {
        if (friend == null)
            return;
        friends.remove(Misc.formatPlayerNameForDisplay(friend));
    }

    public List<String> getFriends() {
        return friends;
    }

    public List<String> getIgnores() {
        return ignores;
    }

}
