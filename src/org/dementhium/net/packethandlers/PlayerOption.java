package org.dementhium.net.packethandlers;

import org.dementhium.content.activity.ActivityManager;
import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.content.areas.CoordinateEvent;
import org.dementhium.content.misc.Following;
import org.dementhium.model.World;
import org.dementhium.model.map.path.DefaultPathFinder;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.TradeSession;
import org.dementhium.net.ActionSender;
import org.dementhium.net.PacketHandler;
import org.dementhium.net.message.Message;
import org.dementhium.util.Constants;

public class PlayerOption extends PacketHandler {

    public static final int FIRST_OPTION = 70;
    public static final int TRADE_PLAYER = 27; // RIGHT!
    public static final int FOLLOW_PLAYER = 80;

    @Override
    public void handlePacket(Player player, Message packet) {
        switch (packet.getOpcode()) {
            case FIRST_OPTION:
                handleFirstOption(player, packet);
                break;
            case TRADE_PLAYER:
                handleTradeRequest(player, packet);
                break;
            case FOLLOW_PLAYER:
                Following.playerFollow(player, World.getWorld().getPlayers().get(packet.readLEShortA()));
                break;
        }
    }

    private void handleFirstOption(final Player player, Message packet) {
        int playerIndex = packet.readLEShort();
        final Player other = World.getWorld().getPlayers().get(playerIndex);
        if (other == null || !other.isOnline() || player.getIndex() == other.getIndex()) {
            return;
        }
        player.getActionManager().stopAction();
        if (World.getWorld().getAreaManager().getAreaByName("ChallengeRoom").contains(player.getLocation())) {
            player.turnTo(other);
            if (!World.getWorld().doPath(new DefaultPathFinder(), player, other.getLocation().getX(), other.getLocation().getY(), false, false).isRouteFound()) {
                player.sendMessage("I can't reach that!");
                return;
            } else {
                Following.combatFollow(player, other);
            }

            World.getWorld().submitAreaEvent(player, new CoordinateEvent(player, other.getLocation().getX(), other.getLocation().getY(), other.size(), other.size()) {

                @Override
                public void execute() {
                    if (other.getAttribute("didRequestDuel") == Boolean.TRUE && ((Short) other.getAttribute("duelWithIndex") == player.getIndex())) {
                        ActivityManager.getSingleton().register(new DuelActivity(player, other));
                    } else {
                        ActionSender.sendInterface(player, 640);
                        ActionSender.sendConfig(player, 283, 67108864);
                        player.setAttribute("isStaking", Boolean.FALSE);
                        player.setAttribute("isFriendly", Boolean.FALSE);
                        player.setAttribute("duelWithIndex", other.getIndex());
                    }
                }
            });
        } else if (player.getActivity().isCombatActivity(player, other) || player.getPlayerArea().inWilderness() || (player.getAttribute("isInDuelArena", Boolean.FALSE) == Boolean.TRUE && World.getWorld().getAreaManager().getAreaByName("Duel").contains(player.getLocation()))) {
            player.turnTo(other);
            player.getCombatExecutor().setVictim(other);
        }
    }

    private void handleTradeRequest(final Player player, Message packet) {
        int partnerIndex = packet.readShort();
        packet.readByte();
        if (partnerIndex < 0 || partnerIndex >= Constants.MAX_AMT_OF_PLAYERS) {
            return;
        }
        final Player partner = World.getWorld().getPlayers().get(partnerIndex);
        if (partner == null || !partner.isOnline() || player.getIndex() == partner.getIndex()) {
            return;
        }
        if(player.hasStarter() == false || partner.hasStarter() == false){//Can't trade people during Tutorial cutscene!
        	return;
        }
        player.getActionManager().stopAction();
        player.turnTo(partner);
        if (!World.getWorld().doPath(new DefaultPathFinder(), player, partner.getLocation().getX(), partner.getLocation().getY(), false, false).isRouteFound()) {
            player.sendMessage("I can't reach that!");
            return;
        } else {
            Following.combatFollow(player, partner);
        }
        World.getWorld().submitAreaEvent(player, new CoordinateEvent(player, partner.getLocation().getX(), partner.getLocation().getY(), partner.size(), partner.size()) {

            @Override
            public void execute() {
                if (partner.getTradeSession() != null) {
                    ActionSender.sendMessage(player, "The other player is busy.");
                    return;
                }
                if (partner.getAttribute("didRequestTrade") == Boolean.TRUE && ((Short) partner.getAttribute("tradeWithIndex") == player.getIndex())) {
                    TradeSession session = new TradeSession(player, partner);
                    player.setTradeSession(session);
                    partner.setTradePartner(player);
                    session.start();
                } else {
                    ActionSender.sendMessage(player, "Sending trade request...");
                    ActionSender.sendTradeReq(partner, player.getUsername(), "wishes to trade with you.");
                    player.setAttribute("tradeWithIndex", partner.getIndex());
                    player.setAttribute("didRequestTrade", Boolean.TRUE);
                }
            }
        });
    }
}
