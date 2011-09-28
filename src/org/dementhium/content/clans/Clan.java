package org.dementhium.content.clans;

import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Misc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author 'Mystic Flow
 */
public class Clan {

    private String roomName;
    private String roomOwner;
    private int joinReq = 0;
    private int talkReq = 0;
    private int kickReq = 7;
    private int lootReq = 0;
    private HashMap<String, Byte> ranks;
    private HashMap<String, Long> banned;
    private transient List<Player> members;
    private transient boolean lootsharing;
    private boolean coinSharing;

    public Clan(String owner, String name) {
        this.roomName = name;
        this.roomOwner = owner;
        setTransient();
    }

    public void setTransient() {
        setLootsharing(false);
        if (kickReq == 0) {
            kickReq = 7;
        }
        if (members == null) {
            this.members = new ArrayList<Player>();
        }
        if (ranks == null) {
            this.ranks = new HashMap<String, Byte>();
        }
        if (banned == null) {
            this.banned = new HashMap<String, Long>();
        }
    }

    public String getName() {
        return roomName;
    }

    public String getOwner() {
        return roomOwner;
    }

    public void rankUser(String name, int rank) {
        ranks.put(name, (byte) rank);
    }

    /**
     * Gets a player's rank.
     *
     * @param username The username.
     * @return The rank.
     */
    public int getRank(String username) {
        username = Misc.formatPlayerNameForProtocol(username);
        if (ranks.containsKey(username)) {
            return ranks.get(username);
        }
        return 0;
    }

    public Byte getRank(Player player) {
        if (Misc.formatPlayerNameForProtocol(player.getUsername()).equals(roomOwner)) {
            return 7;
        } else if (player.getRights() == 2) {
            return 127;
        } else if (ranks.containsKey(player.getUsername())) {
            return ranks.get(player.getUsername());
        }
        return -1;
    }

    public boolean canJoin(Player player) {
        byte rank = 0;
        if (members.contains(player)) {
            return false;
        }
        if (banned.containsKey(player.getUsername())) {
            if (System.currentTimeMillis() - banned.get(player.getUsername()) < 3600000) {
                player.sendMessage("You have been banned from this channel.");
                return false;
            } else {
                banned.remove(player.getUsername());
            }
        }
        if (ranks.containsKey(player.getUsername())) {
            rank = (byte) (ranks.get(player.getUsername()) + 1);
            return rank >= joinReq;
        }
        return true;
    }

    public boolean canTalk(Player player) {
        byte rank = 0;
        if (ranks.containsKey(player.getUsername())) {
            rank = (byte) (ranks.get(player.getUsername()) + 1);
        }
        return rank >= talkReq;
    }

    public void toggleLootshare() {
        lootsharing = !lootsharing;
        String message = "";
        if (lootsharing) {
            message = "Lootshare has been enabled.";
        } else {
            message = "Lootshare has been disabled.";
        }
        for (Player pl : members) {
            ActionSender.sendMessage(pl, message);
            ActionSender.sendConfig(pl, 1083, lootsharing ? 1 : 0);
        }
    }

    public void toggleCoinsharing() {
        this.coinSharing = !coinSharing;
        String message = "";
        if (coinSharing) {
            message = "Coinshare has been enabled.";
        } else {
            message = "Coinshare has been disabled.";
        }
        for (Player pl : members) {
            ActionSender.sendMessage(pl, message);
        }
    }

    public void addMember(Player member) {
        members.add(member);
    }

    public void setName(String name) {
        this.roomName = name;
    }

    public List<Player> getMembers() {
        return members;
    }

    public void removeMember(Player player) {
        members.remove(player);
    }

    public HashMap<String, Byte> getRanks() {
        return ranks;
    }

    public void setLootsharing(boolean lootsharing) {
        this.lootsharing = lootsharing;
    }

    public boolean isLootsharing() {
        return lootsharing;
    }

    public void setTalkReq(int talkReq) {
        this.talkReq = talkReq;
    }

    public int getTalkReq() {
        return talkReq;
    }

    public void setJoinReq(int joinReq) {
        this.joinReq = joinReq;
    }

    public int getJoinReq() {
        return joinReq;
    }

    public int getKickReq() {
        return kickReq;
    }

    public boolean isCoinSharing() {
        return coinSharing;
    }

    public void banMember(String playerName) {
        banned.put(playerName, System.currentTimeMillis());

    }

    public void handleOption(int buttonOption, int menuIndex) {
        switch (buttonOption) {
            case 4:
                joinReq = menuIndex;
            case 3:
                talkReq = menuIndex;
            case 2:
                kickReq = menuIndex;
            case 1:
                lootReq = menuIndex;
        }

    }


    public void setLootReq(int i) {
        lootReq = i;
    }

    public int getLootReq() {
        return lootReq;
    }

    public boolean canShareLoot(Player pl) {
        if (getRank(pl) >= lootReq) {
            return true;
        }
        return false;
    }

    public boolean canBan(Player player) {
        return getRank(player) > kickReq;
    }

}
