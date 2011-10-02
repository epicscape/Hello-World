package org.dementhium.model.player;


import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.net.ActionSender;
import org.dementhium.util.Logger;
import org.dementhium.util.Misc;

/**
 * Represents a trade session
 *
 * @author Stephen
 */
public class TradeSession {

    private final Player trader, partner;
    private TradeState currentState = TradeState.STATE_ONE;
    private Container traderItemsOffered = new Container(28, false);
    private Container partnerItemsOffered = new Container(28, false);
    private boolean traderDidAccept, partnerDidAccept;
    private boolean tradeModifiedPartner;
    private boolean tradeModifiedTrader;

    /*
      * Some info for the future,
      * 44 = wealth transfer
      * 43 = left limit
      * 45 = right limit
      */

	public void tradeWarning(Player player, int slot) {
		Object[] opt = new Object[]{slot, 7, 4, 21954593};
		ActionSender.sendClientScript(player, 143, opt, "Iiii");
	}
	
    public TradeSession(Player trader, Player partner) {
        this.trader = trader;
        this.partner = partner;
        trader.setAttribute("didRequestTrade", Boolean.FALSE);
        partner.setAttribute("didRequestTrade", Boolean.FALSE);
    }

    public void start() {
        refreshScreen();
        openFirstTradeScreen(trader);
        openFirstTradeScreen(partner);
    }

    public Player getPartner() {
        return partner;
    }

    public void openFirstTradeScreen(Player p) {
        ActionSender.sendTradeOptions(p);
        ActionSender.sendInterface(p, 335);
        ActionSender.sendInventoryInterface(p, 336);
        ActionSender.sendItems(p, 90, traderItemsOffered, false);
        ActionSender.sendItems(p, 90, partnerItemsOffered, true);
        ActionSender.sendString(p, "", 335, 37);
        String name = trader.getUsername();
        String name1 = partner.getUsername();
        ActionSender.sendString(partner, "Trading with: " + Misc.formatPlayerNameForDisplay(name), 335, 15);
        ActionSender.sendString(trader, "Trading with: " + Misc.formatPlayerNameForDisplay(name1), 335, 15);
        ActionSender.sendString(partner, Misc.formatPlayerNameForDisplay(name), 335, 22);
        ActionSender.sendString(trader, Misc.formatPlayerNameForDisplay(name1), 335, 22);
    }

    public void openSecondTradeScreen(Player p) {
        currentState = TradeState.STATE_TWO;
        partnerDidAccept = false;
        traderDidAccept = false;
        ActionSender.sendInterface(p, 334);
        ActionSender.sendString(p, "<col=00FFFF>Trading with:<br><col=00FFFF>" + Misc.formatPlayerNameForDisplay(p.equals(trader) ? partner.getUsername() : trader.getUsername()), 334, 54);
        ActionSender.sendString(p, "Are you sure you want to make this trade?", 334, 34);
    }
    
	public boolean isTradeModifiedPartner() {
		return tradeModifiedPartner;
	}
	
	public boolean isTradeModifiedTrader() {
		return tradeModifiedTrader;
	}

    public void offerItem(Player pl, int slot, int amt) {
        if (currentState.equals(TradeState.STATE_ONE)) {
            if (pl.equals(trader)) {
                if (pl.getInventory().getContainer().get(slot) == null) {
                    return;
                }
                Item item = new Item(pl.getInventory().getContainer().get(slot).getId(), amt);
                if (item != null) {
                    if (pl.getRights() != 2 && !item.getDefinition().isTradeable() && item.getId() != 995) {
                        pl.sendMessage("You cannot trade this item!");
                        return;
                    }
                    if (pl.getInventory().getContainer().getItemCount(item.getId()) < amt) {
                        if (ItemDefinition.forId(item.getId()).isNoted()
                                || ItemDefinition.forId(item.getId()).isStackable()) {
                            amt = pl.getInventory().lookup(item.getId()).getAmount();
                        } else {
                            amt = pl.getInventory().getContainer().getItemCount(item.getId());
                        }
                        item.setAmount(amt);

                    }
                    if (0 >= amt) {
                        return;
                    }
                    if (traderItemsOffered.getFreeSlots() < amt && !pl.getInventory().getContainer().get(slot).getDefinition().isNoted() && !pl.getInventory().getContainer().get(slot).getDefinition().isStackable()) {
                        item.setAmount(traderItemsOffered.getFreeSlots());
                    }
                    traderItemsOffered.add(item);
                    pl.getInventory().getContainer().remove(new Item(pl.getInventory().getContainer().get(slot).getId(), amt));
                    pl.getInventory().refresh();
                    tradeModifiedTrader = false;
                }
            } else if (pl.equals(partner)) {
                Item inventoryItem = pl.getInventory().getContainer().get(slot);
                Item item = inventoryItem != null ? new Item(inventoryItem.getId(), amt) : null;
                if (item != null) {
                    if (!item.getDefinition().isTradeable() && item.getId() != 995 && pl.getRights() != 2) {
                        pl.sendMessage("You cannot trade this item!");
                        return;
                    }
                    if (pl.getInventory().getContainer().getItemCount(item.getId()) < amt) {
                        if (ItemDefinition.forId(item.getId()).isNoted()
                                || ItemDefinition.forId(item.getId()).isStackable()) {
                            amt = pl.getInventory().lookup(item.getId()).getAmount();
                        } else {
                            amt = pl.getInventory().getContainer().getItemCount(item.getId());
                        }
                        item.setAmount(amt);

                    }
                    if (0 >= amt) {
                        return;
                    }
                    if (partnerItemsOffered.getFreeSlots() < amt && !pl.getInventory().getContainer().get(slot).getDefinition().isNoted() && !pl.getInventory().getContainer().get(slot).getDefinition().isStackable()) {
                        item.setAmount(partnerItemsOffered.getFreeSlots());
                    }
                    partnerItemsOffered.add(item);
                    pl.getInventory().getContainer().remove(item);
                    pl.getInventory().refresh();
                    tradeModifiedPartner = false;
                }
            }
            refreshScreen();
        }
    }

    public void removeItem(Player pl, int slot, int amt) {
        if (currentState.equals(TradeState.STATE_ONE)) {
            if (pl.equals(trader)) {
                Item item = new Item(traderItemsOffered.get(slot).getId(), amt);
                if (item != null) {
                    if (traderItemsOffered.getItemCount(item.getId()) < amt) {
                        if (ItemDefinition.forId(item.getId()).isNoted()
                                || ItemDefinition.forId(item.getId()).isStackable()) {
                            amt = traderItemsOffered.lookup(item.getId()).getAmount();
                        } else {
                            amt = traderItemsOffered.getItemCount(item.getId());
                        }
                        item.setAmount(amt);

                    }
                    if (0 >= amt) {
                        return;
                    }
                    if (pl.getInventory().getFreeSlots() < amt && !traderItemsOffered.get(slot).getDefinition().isNoted() && !traderItemsOffered.get(slot).getDefinition().isStackable()) {
                        item.setAmount(pl.getInventory().getFreeSlots());
                    }
                    trader.getInventory().getContainer().add(new Item(traderItemsOffered.get(slot).getId(), item.getAmount()));
                    trader.getInventory().refresh();
                    partner.getTradeSession().tradeWarning(partner, slot);
                    trader.getTradeSession().tradeWarning(partner, slot);
                    traderItemsOffered.remove(item);
                    tradeModifiedTrader = true;
                    resetAccept();
                }
            } else if (pl.equals(partner)) {
            	Item item = new Item(partnerItemsOffered.get(slot).getId(), amt);
                if (item != null) {
                    if (partnerItemsOffered.getItemCount(item.getId()) < amt) {
                        if (ItemDefinition.forId(item.getId()).isNoted()
                                || ItemDefinition.forId(item.getId()).isStackable()) {
                            amt = partnerItemsOffered.lookup(item.getId()).getAmount();
                        } else {
                            amt = partnerItemsOffered.getItemCount(item.getId());
                        }
                        item.setAmount(amt);

                    }
                    if (0 >= amt) {
                        return;
                    }
                    if (pl.getInventory().getFreeSlots() < amt && !partnerItemsOffered.get(slot).getDefinition().isNoted() && !partnerItemsOffered.get(slot).getDefinition().isStackable()) {
                        item.setAmount(pl.getInventory().getFreeSlots());
                    }
                    partner.getInventory().getContainer().add(new Item(partnerItemsOffered.get(slot).getId(), item.getAmount()));
                    partner.getInventory().refresh();
                    partnerItemsOffered.remove(item);
                    trader.getTradeSession().tradeWarning(trader, slot);
                    partner.getTradeSession().tradeWarning(trader, slot);
                    tradeModifiedPartner = true;
                    resetAccept();
                }
            }
            refreshScreen();
        }
    }

    private void refreshScreen() {
        ActionSender.sendItems(trader, 90, traderItemsOffered, false);
        ActionSender.sendItems(partner, 90, partnerItemsOffered, false);
        ActionSender.sendItems(trader, 90, partnerItemsOffered, true);
        ActionSender.sendItems(partner, 90, traderItemsOffered, true);
        String name = trader.getUsername();
        ActionSender.sendString(partner, Misc.formatPlayerNameForDisplay(name), 335, 22);
        String name1 = partner.getUsername();
        ActionSender.sendString(trader, Misc.formatPlayerNameForDisplay(name1), 335, 22);
        ActionSender.sendString(trader, 335, 21, " has " + partner.getInventory().getFreeSlots() + " free inventory slots.");
        ActionSender.sendString(partner, 335, 21, " has " + trader.getInventory().getFreeSlots() + " free inventory slots.");
        ActionSender.sendBConfig(trader, 729, getTradersItemsValue());
        ActionSender.sendBConfig(trader, 697, getPartnersItemsValue());
        ActionSender.sendBConfig(partner, 729, getPartnersItemsValue());
        ActionSender.sendBConfig(partner, 697, getTradersItemsValue());
		if (partner.getTradeSession().isTradeModifiedPartner()) {
			ActionSender.sendConfig(trader, 1043, 1);
			//ActionSender.sendConfig(partner, 1043, 1);
		}
		if (trader.getTradeSession().isTradeModifiedTrader()) {
			//ActionSender.sendConfig(trader, 1043, 1);
			ActionSender.sendConfig(partner, 1043, 1);
		}
    }

    private int getTradersItemsValue() {
        int initialPrice = 0;
        for (Item item : traderItemsOffered.toArray()) {
            if (item != null) {
                initialPrice += item.getDefinition().getExchangePrice();
            }
        }
        return initialPrice;
    }

    private int getPartnersItemsValue() {
        int initialPrice = 0;
        for (Item item : partnerItemsOffered.toArray()) {
            if (item != null) {
                initialPrice += item.getDefinition().getExchangePrice();
            }
        }
        return initialPrice;
    }

    @SuppressWarnings("unused")
    private void flashSlot(Player player, int slot) {
        ActionSender.sendClientScript(player, 143, new Object[]{slot, 7, 4, player.equals(trader) ? 21954591 : 21954593}, "Iiii"); //Guess this wouldn't work for both screens.
    }

    public void acceptPressed(Player pl) {
        if (!traderDidAccept && pl.equals(trader)) {
            traderDidAccept = true;
        } else if (!partnerDidAccept && pl.equals(partner)) {
            partnerDidAccept = true;
        }
        switch (currentState) {
            case STATE_ONE:
                if (pl.equals(trader)) {
                    if (partnerDidAccept && traderDidAccept) {
                        openSecondTradeScreen(trader);
                        openSecondTradeScreen(partner);
                    } else {
                        ActionSender.sendString(trader, "Waiting for other player...", 335, 37);
                        ActionSender.sendString(partner, "The other player has accepted", 335, 37);
                    }
                } else if (pl.equals(partner)) {
                    if (partnerDidAccept && traderDidAccept) {
                        openSecondTradeScreen(trader);
                        openSecondTradeScreen(partner);
                    } else {
                        ActionSender.sendString(partner, "Waiting for other player...", 335, 37);
                        ActionSender.sendString(trader, "The other player has accepted", 335, 37);
                    }
                }
                break;

            case STATE_TWO:
                if (pl.equals(trader)) {
                    if (partnerDidAccept && traderDidAccept) {
                        giveItems();
                    } else {
                        ActionSender.sendString(trader, "Waiting for other player...", 334, 34);
                        ActionSender.sendString(partner, "The other player has accepted", 334, 34);
                    }
                } else if (pl.equals(partner)) {
                    if (partnerDidAccept && traderDidAccept) {
                        giveItems();
                    } else {
                        ActionSender.sendString(partner, "Waiting for other player...", 334, 34);
                        ActionSender.sendString(trader, "The other player has accepted", 334, 34);
                    }
                }
                break;
        }

    }

    public void tradeFailed() {
        trader.getInventory().getContainer().addAll(traderItemsOffered);
        partner.getInventory().getContainer().addAll(partnerItemsOffered);
        endSession();
        trader.getInventory().refresh();
        partner.getInventory().refresh();
    }

    public void endSession() {
        traderItemsOffered = partnerItemsOffered = null;
        trader.setTradeSession(null);
        partner.setTradePartner(null);
        resetTradeWarning(trader);
        resetTradeWarning(partner);
        ActionSender.sendCloseInterface(trader);
        ActionSender.sendCloseInterface(partner);
        ActionSender.closeInventoryInterface(trader);
        ActionSender.closeInventoryInterface(partner);
    }
    
    public void resetTradeWarning(Player player) {
		ActionSender.sendConfig(player, 1043, 0);
		ActionSender.sendConfig(player, 1042, 0);
		tradeModifiedPartner = false;
		tradeModifiedTrader = false;
    }

    private void giveItems() {
        if (!trader.getInventory().getContainer().hasSpaceFor(partnerItemsOffered)) {
            ActionSender.sendMessage(partner, "The other player does not have enough space in their inventory.");
            ActionSender.sendMessage(trader, "You do not have enough space in your inventory.");
            tradeFailed();
            return;
        } else if (!partner.getInventory().getContainer().hasSpaceFor(traderItemsOffered)) {
            ActionSender.sendMessage(trader, "The other player does not have enough space in their inventory.");
            ActionSender.sendMessage(partner, "You do not have enough space in your inventory.");
            tradeFailed();
            return;
        }
        for (Item itemAtIndex : traderItemsOffered.toArray()) {
            if (itemAtIndex != null) {
                partner.getInventory().addItem(itemAtIndex.getId(), itemAtIndex.getAmount());
            }
        }
        for (Item itemAtIndex : partnerItemsOffered.toArray()) {
            if (itemAtIndex != null) {
                trader.getInventory().addItem(itemAtIndex.getId(), itemAtIndex.getAmount());
            }
        }
        Logger.writeTradeLog(trader, partner, traderItemsOffered, partnerItemsOffered);
        endSession();
        partner.getInventory().refresh();
        trader.getInventory().refresh();
    }

    public Container getPlayerItemsOffered(Player p) {
        return (p.equals(trader) ? traderItemsOffered : partnerItemsOffered);
    }

    public enum TradeState {

        STATE_ONE,
        STATE_TWO
    }

    public void resetAccept() {
        partnerDidAccept = traderDidAccept = false;
        switch (currentState) {
            case STATE_ONE:
                ActionSender.sendString(partner, "", 335, 37);
                ActionSender.sendString(trader, "", 335, 37);
                break;
            case STATE_TWO:
                ActionSender.sendString(partner, "", 334, 34);
                ActionSender.sendString(trader, "", 334, 34);
                break;
        }
    }

    public TradeState getState() {
        return currentState;
    }
}
