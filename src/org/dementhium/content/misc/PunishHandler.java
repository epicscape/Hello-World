package org.dementhium.content.misc;

import org.dementhium.io.XMLHandler;
import org.dementhium.model.player.Player;
import org.dementhium.util.Misc;

import java.io.IOException;
import java.util.ArrayList;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 * @author Lumby <lumbyjr@hotmail.com>
 * @author Emperor <black_dragon_686@hotmail.com>
 */
public class PunishHandler {

    private ArrayList<String> mutedPlayers = new ArrayList<String>();
    private ArrayList<String> mutedIps = new ArrayList<String>();
    private ArrayList<String> bannedPlayers = new ArrayList<String>();
    private ArrayList<String> bannedIps = new ArrayList<String>();

    public boolean isMuted(Player p) {
        boolean muted = false;
        if (mutedPlayers.contains(p.getUsername())) {
            muted = true;
        } else if (mutedIps.contains(formatIp(p.getConnection().getChannel().getRemoteAddress().toString()))) {
            muted = true;
        }
        return muted;
    }

    public boolean isBanned(Player p) {
        boolean banned = false;
        if (bannedPlayers.contains(p.getUsername())) {
            banned = true;
        } else if (bannedIps.contains(formatIp(p.getConnection().getChannel().getRemoteAddress().toString()))) {
            banned = true;
        }
        return banned;
    }

    public void addMuted(Player p, boolean ipMute) {
        if (mutedPlayers == null) {
            mutedPlayers = new ArrayList<String>();
        }
        if (mutedIps == null) {
            mutedIps = new ArrayList<String>();
        }
        if (ipMute) {
            mutedIps.add(formatIp(p.getConnection().getChannel().getRemoteAddress().toString()));
        } else {
            mutedPlayers.add(p.getUsername());
        }
    }

    public void addBan(Player p, boolean ipBan) {
        if (bannedPlayers == null) {
            bannedPlayers = new ArrayList<String>();
        }
        if (bannedIps == null) {
            bannedIps = new ArrayList<String>();
        }
        if (ipBan) {
            bannedIps.add(formatIp(p.getConnection().getChannel().getRemoteAddress().toString()));
        } else {
            bannedPlayers.add(p.getUsername());
        }
    }

    public void unMute(Player p, boolean ipMute) {
        if (ipMute) {
            String ip = formatIp(p.getConnection().getChannel().getRemoteAddress().toString());
            for (String s : mutedIps) {
                if (mutedIps.equals(ip)) {
                    mutedIps.remove(s);
                }
            }
        } else {
            mutedPlayers.remove(p.getUsername());
        }
    }

    public void unMute(String user, boolean ipMute) {
        if (ipMute) {
            mutedIps.remove(user);
        } else {
            mutedPlayers.remove(user);
        }
    }

    public void unBan(Player p, boolean ipBan) {
        if (ipBan) {
            String ip = p.getLastConnectIp();
            for (String s : bannedIps) {
                if (bannedIps.equals(ip)) {
                    bannedIps.remove(s);
                }
            }
        } else {
            bannedPlayers.remove(p.getUsername());
        }
    }

    public void unBan(String p, boolean ipBan) {
        if (ipBan) {
            bannedIps.remove(p);
        } else {
            bannedPlayers.remove(p);
        }
    }

    public static final String DIRECTORY = Misc.isVPS() ? "data/xml/" : "root/xml/";

    public void save() {
        try {
            XMLHandler.toXML(DIRECTORY + "ipmutes.xml", mutedIps);
            XMLHandler.toXML(DIRECTORY + "mutes.xml", mutedPlayers);
            XMLHandler.toXML(DIRECTORY + "banned_users.xml", bannedPlayers);
            XMLHandler.toXML(DIRECTORY + "banned_ips.xml", bannedIps);
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    public void load() {
        try {
            mutedIps = XMLHandler.fromXML(DIRECTORY + "ipmutes.xml");
            mutedPlayers = XMLHandler.fromXML(DIRECTORY + "mutes.xml");
            bannedIps = XMLHandler.fromXML(DIRECTORY + "banned_ips.xml");
            bannedPlayers = XMLHandler.fromXML(DIRECTORY + "banned_users.xml");
        } catch (IOException e) {
        }

    }

    /**
     * Formats the IP-Address.
     *
     * @param unformatted The unformatted IP.
     * @return The formatted IP.
     */
    public static final String formatIp(String unformatted) {
        String ipAddress = unformatted;
        ipAddress = ipAddress.replaceAll("/", "");
        ipAddress = ipAddress.replaceAll(" ", "");
        ipAddress = ipAddress.substring(0, ipAddress.indexOf(":"));
        return ipAddress;
    }

    public void addBan(String name) {
        bannedPlayers.add(name);
    }
}
