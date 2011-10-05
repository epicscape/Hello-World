package org.dementhium.model.player;


import org.dementhium.action.Action;
import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;
import org.dementhium.util.Logger;
import org.dementhium.util.Misc;

/**
 * Represents a trade session
 *
 * @author Stephen
 */
public class TradeSession {

<<<<<<< HEAD
	private final Player trader, partner;
	private TradeState currentState = TradeState.STATE_ONE;
	public Container traderItemsOffered = new Container(28, false);
	public Container partnerItemsOffered = new Container(28, false);
	private Container traderItemsLoaned = new Container(1, false);
	private Container partnerItemsLoaned = new Container(1, false);
	private boolean traderDidAccept, partnerDidAccept;
	public boolean tradeModifiedPartner, tradeModifiedTrader;
	private boolean hasLentItem;

	/*
	 * Some info for the future,
	 * 44 = wealth transfer
	 * 43 = left limit
	 * 45 = right limit
	 */

	/**
	 * Trade Flashing "!" Stoof.
	 */
=======
    private final Player trader, partner;
    private TradeState currentState = TradeState.STATE_ONE;
    private Container traderItemsOffered = new Container(28, false);
    private Container partnerItemsOffered = new Container(28, false);
    private Container traderItemsLoaned = new Container(1, false);
    private Container partnerItemsLoaned = new Container(1, false);
    private boolean traderDidAccept, partnerDidAccept;
    public boolean tradeModifiedPartner, tradeModifiedTrader;
    private boolean hasLentItem;
    
    /*
      * Some info for the future,
      * 44 = wealth transfer
      * 43 = left limit
      * 45 = right limit
      */

    /**
     * Trade Flashing "!" Stoof.
     */
>>>>>>> d9616223ef4c9f16b6274d636e789bf214d81346
	public void tradeWarningPartner(Player player, int slot) {
		Object[] opt = new Object[]{slot, 7, 4, 21954594};
		ActionSender.sendClientScript(player, 143, opt, "Iiii");
	}
<<<<<<< HEAD

=======
	
>>>>>>> d9616223ef4c9f16b6274d636e789bf214d81346
	public void tradeWarningMySide(Player player, int slot) {
		Object[] opt = new Object[]{slot, 7, 4, 21954591};
		ActionSender.sendClientScript(player, 143, opt, "Iiii");
	}
<<<<<<< HEAD

=======
	
>>>>>>> d9616223ef4c9f16b6274d636e789bf214d81346
	public void lendWarningMySide(Player player, int slot) {
		Object[] opt = new Object[]{slot, 7, 4, 714, 21954607};
		ActionSender.sendClientScript(player, 146, opt, "Iiii");
	}
<<<<<<< HEAD

=======
	
>>>>>>> d9616223ef4c9f16b6274d636e789bf214d81346
	public void lendWarningPartner(Player player, int slot) {
		Object[] opt = new Object[]{slot, 7, 4, 714, 21954608};
		ActionSender.sendClientScript(player, 146, opt, "Iiii");
	}
<<<<<<< HEAD

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
		ActionSender.sendString(partner, "Trading with: " + Misc.formatPlayerNameForDisplay(trader.getUsername()), 335, 15);
		ActionSender.sendString(partner, Misc.formatPlayerNameForDisplay(trader.getUsername()), 335, 22);
		ActionSender.sendString(trader, "Trading with: " + Misc.formatPlayerNameForDisplay(partner.getUsername()), 335, 15);
		ActionSender.sendString(trader, Misc.formatPlayerNameForDisplay(partner.getUsername()), 335, 22);
		System.out.println("Derp? "+partner.getUsername());
		System.out.println("Derpus "+trader.getUsername());
	}

	public Player getPartner() {
		return partner;
	}

	public void openFirstTradeScreen(Player p) {
		ActionSender.sendString(trader, "" + Misc.formatPlayerNameForDisplay(partner.getUsername()), 335, 22);
		ActionSender.sendString(partner, "" + Misc.formatPlayerNameForDisplay(trader.getUsername()), 335, 22);
		ActionSender.sendTradeOptions(p);
		ActionSender.sendInterface(p, 335);
		ActionSender.sendInventoryInterface(p, 336);
		ActionSender.sendItems(p, 90, traderItemsOffered, false);
		ActionSender.sendItems(p, 90, partnerItemsOffered, true);
		ActionSender.sendItems(p, 541, traderItemsLoaned, false);
		ActionSender.sendItems(p, 541, partnerItemsLoaned, true);
		ActionSender.sendString(p, "", 335, 37);
		String name = p.equals(trader) ? partner.getUsername() : trader.getUsername();
		ActionSender.sendString(p, "Trading with: " + Misc.formatPlayerNameForDisplay(name), 335, 15);
		ActionSender.sendString(p, Misc.formatPlayerNameForDisplay(name), 335, 22);
		ActionSender.sendString(trader, "" + Misc.formatPlayerNameForDisplay(partner.getUsername()), 335, 22);
		ActionSender.sendString(partner, "" + Misc.formatPlayerNameForDisplay(trader.getUsername()), 335, 22);
		sendDelayedAction(1, new Action(0) {
			@Override
			public void execute() {
				ActionSender.sendString(trader, "" + Misc.formatPlayerNameForDisplay(partner.getUsername()), 335, 22);
				ActionSender.sendString(partner, "" + Misc.formatPlayerNameForDisplay(trader.getUsername()), 335, 22);
			}
		});
	}

	public void openSecondTradeScreen(Player p) {
		currentState = TradeState.STATE_TWO;
		partnerDidAccept = false;
		traderDidAccept = false;
		ActionSender.sendInterface(p, 334);
		ActionSender.sendString(p, "<col=00FFFF>Trading with:<br><col=00FFFF>" + Misc.formatPlayerNameForDisplay(p.equals(trader) ? partner.getUsername() : trader.getUsername()), 334, 54);
		ActionSender.sendString(p, "Are you sure you want to make this trade?", 334, 34);
	}

	public void removeLoanedItem(Player pl, int slot, int amt) {
		if (currentState.equals(TradeState.STATE_ONE)) {
			if (pl.equals(trader)) {
				Item item = new Item(traderItemsLoaned.get(slot).getId(), amt);
				if (item != null) {
					if (pl.getInventory().getFreeSlots() < amt && !traderItemsLoaned.get(slot).getDefinition().isNoted() && !traderItemsLoaned.get(slot).getDefinition().isStackable()) {
						item.setAmount(pl.getInventory().getFreeSlots());
					}
					trader.getInventory().getContainer().add(new Item(traderItemsLoaned.get(slot).getId(), item.getAmount()));
					trader.getInventory().refresh();
					tradeWarningPartner(partner, slot);
					tradeWarningMySide(trader, slot);
					refreshScreen();
					traderItemsLoaned.remove(item);
					resetAccept();
					tradeModifiedTrader = true;
				}
			} else if (pl.equals(partner)) {
				Item item = new Item(partnerItemsLoaned.get(slot).getId(), amt);
				if (item != null) {
					if (pl.getInventory().getFreeSlots() < amt && !partnerItemsLoaned.get(slot).getDefinition().isNoted() && !partnerItemsLoaned.get(slot).getDefinition().isStackable()) {
						item.setAmount(pl.getInventory().getFreeSlots());
					}
					partner.getInventory().getContainer().add(new Item(partnerItemsLoaned.get(slot).getId(), item.getAmount()));
					partner.getInventory().refresh();
					refreshScreen();
					tradeWarningPartner(trader, slot);
					tradeWarningMySide(partner, slot);
					partnerItemsLoaned.remove(item);
					tradeModifiedPartner = true;
					resetAccept();
				}
			}
		}
	}

	public void lendItem(Player pl, int slot, int amt) {
		if (currentState.equals(TradeState.STATE_ONE)) {
			if (pl.equals(trader)) {
				//ActionSender.sendItems(pl, 541, pl.getInventory().getContainer(), false);
				if (pl.getInventory().getContainer().get(slot) == null) {
					return;
				}
				Item item = new Item(pl.getInventory().getContainer().get(slot).getId(), amt);
				if (item != null) {
					if (pl.getRights() != 2 && !item.getDefinition().isTradeable() && item.getId() != 995) {
						pl.sendMessage("You cannot loan this item!");
						hasLentItem = false;
						return;
					}
					//if (pl.getTradeSession().hasLentItem = true) {
					//	pl.sendMessage("You have already lent an item.");
					//	return;
					//}
					if (pl.getInventory().getContainer().getItemCount(item.getId()) > 1) {
						pl.sendMessage("You cannot lend more than one item at a time");
						hasLentItem = false;
						return;
					}
					if (traderItemsLoaned.getFreeSlots() < amt && !pl.getInventory().getContainer().get(slot).getDefinition().isNoted() && !pl.getInventory().getContainer().get(slot).getDefinition().isStackable()) {
						item.setAmount(traderItemsLoaned.getFreeSlots());
					}
					traderItemsLoaned.add(item);
					pl.getInventory().getContainer().remove(new Item(pl.getInventory().getContainer().get(slot).getId(), amt));
					pl.getInventory().refresh();
					tradeModifiedPartner = false;
					hasLentItem = true;
				}
			} else if (pl.equals(partner)) {
				Item inventoryItem = pl.getInventory().getContainer().get(slot);
				Item item = inventoryItem != null ? new Item(inventoryItem.getId(), amt) : null;
				if (item != null) {
					if (pl.getRights() != 2 && !item.getDefinition().isTradeable() && item.getId() == 995) {
						pl.sendMessage("You cannot loan this item!");
						hasLentItem = false;
						return;
					}
					// if (pl.getTradeSession().hasLentItem = true) {
					// 	pl.sendMessage("You have already lent an item.");
					// 	hasLentItem = false;
					//  	return;
					// }
					if (pl.getInventory().getContainer().getItemCount(item.getId()) > 1) {
						pl.sendMessage("You cannot lend more than one item at a time");
						hasLentItem = false;
						return;
					}
					if (partnerItemsLoaned.getFreeSlots() < amt && !pl.getInventory().getContainer().get(slot).getDefinition().isNoted() && !pl.getInventory().getContainer().get(slot).getDefinition().isStackable()) {
						item.setAmount(partnerItemsLoaned.getFreeSlots());
					}
					partnerItemsLoaned.add(item);
					pl.getInventory().getContainer().remove(item);
					pl.getInventory().refresh();
					tradeModifiedTrader = false;
					hasLentItem = true;
				}
			}
			refreshScreen();
		}
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
					tradeModifiedPartner = false;
					traderItemsOffered.add(item);
					pl.getInventory().getContainer().remove(new Item(pl.getInventory().getContainer().get(slot).getId(), amt));
					pl.getInventory().refresh();
					refreshScreen();
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
					refreshScreen();
					tradeModifiedTrader = false;
				}
			}

		}
	}

	public void removeItem(Player pl, int slot, int amt) {
		if (currentState.equals(TradeState.STATE_ONE)) {
			if (pl.equals(trader)) {
				System.out.println("Fag is trader");
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
					tradeWarningPartner(trader, slot);
					tradeWarningMySide(partner, slot);
					//refreshScreen(slot);
					traderItemsOffered.set(slot, item);
					traderItemsOffered.remove(item);
					resetAccept();
					//refreshScreen();
					tradeModifiedTrader = true;
					refreshScreen(slot);
				}
			} else if (pl.equals(partner)) {
				System.out.println("Fag is partner");
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
					//refreshScreen(slot);
					tradeWarningPartner(trader, slot);
					tradeWarningMySide(partner, slot);
					partnerItemsOffered.set(slot, item);
					partnerItemsOffered.remove(item);
					tradeModifiedPartner = true;
					resetAccept();
					refreshScreen(slot);
					//refreshScreen();
				}
			}
			// refreshScreen(slot);
		}

	}

	private void refreshScreen() {
		System.out.println("Refresh...\r\n"+Thread.currentThread().getStackTrace()[2]);
		ActionSender.sendItems(trader, 90, traderItemsOffered, false);
		ActionSender.sendItems(partner, 90, partnerItemsOffered, false);
		ActionSender.sendItems(trader, 90, partnerItemsOffered, true);
		ActionSender.sendItems(partner, 90, traderItemsOffered, true);
		ActionSender.sendItems(trader, 541, traderItemsLoaned, false);
		ActionSender.sendItems(partner, 541, partnerItemsLoaned, false);
		ActionSender.sendItems(trader, 541, partnerItemsLoaned, true);
		ActionSender.sendItems(partner, 541, traderItemsLoaned, true);
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
		if (partner.getTradeSession().tradeModifiedPartner == true) {
=======
	
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
        ActionSender.sendItems(p, 541, traderItemsLoaned, false);
        ActionSender.sendItems(p, 541, partnerItemsLoaned, true);
        ActionSender.sendString(p, "", 335, 37);
        String name = p.equals(trader) ? partner.getUsername() : trader.getUsername();
        ActionSender.sendString(p, "Trading with: " + Misc.formatPlayerNameForDisplay(name), 335, 15);
        //ActionSender.sendString(p, Misc.formatPlayerNameForDisplay(name), 335, 22);
        ActionSender.sendString(trader, "" + Misc.formatPlayerNameForDisplay(partner.getUsername()), 335, 22);
        ActionSender.sendString(partner, "" + Misc.formatPlayerNameForDisplay(trader.getUsername()), 335, 22);
    }

    public void openSecondTradeScreen(Player p) {
        currentState = TradeState.STATE_TWO;
        partnerDidAccept = false;
        traderDidAccept = false;
        ActionSender.sendInterface(p, 334);
        ActionSender.sendString(p, "<col=00FFFF>Trading with:<br><col=00FFFF>" + Misc.formatPlayerNameForDisplay(p.equals(trader) ? partner.getUsername() : trader.getUsername()), 334, 54);
        ActionSender.sendString(p, "Are you sure you want to make this trade?", 334, 34);
    }

    public void removeLoanedItem(Player pl, int slot, int amt) {
        if (currentState.equals(TradeState.STATE_ONE)) {
            if (pl.equals(trader)) {
                Item item = new Item(traderItemsLoaned.get(slot).getId(), amt);
                if (item != null) {
                    if (pl.getInventory().getFreeSlots() < amt && !traderItemsLoaned.get(slot).getDefinition().isNoted() && !traderItemsLoaned.get(slot).getDefinition().isStackable()) {
                        item.setAmount(pl.getInventory().getFreeSlots());
                    }
                    trader.getInventory().getContainer().add(new Item(traderItemsLoaned.get(slot).getId(), item.getAmount()));
                    trader.getInventory().refresh();
                    tradeWarningPartner(partner, slot);
                    tradeWarningMySide(trader, slot);
                    refreshScreen();
                    traderItemsLoaned.remove(item);
                    resetAccept();
                    tradeModifiedTrader = true;
                }
            } else if (pl.equals(partner)) {
            	Item item = new Item(partnerItemsLoaned.get(slot).getId(), amt);
                if (item != null) {
                	if (pl.getInventory().getFreeSlots() < amt && !partnerItemsLoaned.get(slot).getDefinition().isNoted() && !partnerItemsLoaned.get(slot).getDefinition().isStackable()) {
                        item.setAmount(pl.getInventory().getFreeSlots());
                     }
                    partner.getInventory().getContainer().add(new Item(partnerItemsLoaned.get(slot).getId(), item.getAmount()));
                    partner.getInventory().refresh();
                    refreshScreen();
                    tradeWarningPartner(trader, slot);
                    tradeWarningMySide(partner, slot);
                    partnerItemsLoaned.remove(item);
                    tradeModifiedPartner = true;
                    resetAccept();
                }
            }
        }
    }
    
    public void lendItem(Player pl, int slot, int amt) {
        if (currentState.equals(TradeState.STATE_ONE)) {
            if (pl.equals(trader)) {
                //ActionSender.sendItems(pl, 541, pl.getInventory().getContainer(), false);
                if (pl.getInventory().getContainer().get(slot) == null) {
                    return;
                }
                Item item = new Item(pl.getInventory().getContainer().get(slot).getId(), amt);
                if (item != null) {
                    if (pl.getRights() != 2 && !item.getDefinition().isTradeable() && item.getId() != 995) {
                        pl.sendMessage("You cannot loan this item!");
                        hasLentItem = false;
                        return;
                    }
                    //if (pl.getTradeSession().hasLentItem = true) {
                    //	pl.sendMessage("You have already lent an item.");
                    //	return;
                    //}
                    if (pl.getInventory().getContainer().getItemCount(item.getId()) > 1) {
                    	pl.sendMessage("You cannot lend more than one item at a time");
                    	hasLentItem = false;
                    	return;
                    }
                    if (traderItemsLoaned.getFreeSlots() < amt && !pl.getInventory().getContainer().get(slot).getDefinition().isNoted() && !pl.getInventory().getContainer().get(slot).getDefinition().isStackable()) {
                        item.setAmount(traderItemsLoaned.getFreeSlots());
                    }
                    traderItemsLoaned.add(item);
                    pl.getInventory().getContainer().remove(new Item(pl.getInventory().getContainer().get(slot).getId(), amt));
                    pl.getInventory().refresh();
                    tradeModifiedPartner = false;
                    hasLentItem = true;
                }
            } else if (pl.equals(partner)) {
                Item inventoryItem = pl.getInventory().getContainer().get(slot);
                Item item = inventoryItem != null ? new Item(inventoryItem.getId(), amt) : null;
                if (item != null) {
                    if (pl.getRights() != 2 && !item.getDefinition().isTradeable() && item.getId() == 995) {
                        pl.sendMessage("You cannot loan this item!");
                        hasLentItem = false;
                        return;
                    }
                   // if (pl.getTradeSession().hasLentItem = true) {
                   // 	pl.sendMessage("You have already lent an item.");
                   // 	hasLentItem = false;
                  //  	return;
                   // }
                    if (pl.getInventory().getContainer().getItemCount(item.getId()) > 1) {
                    	pl.sendMessage("You cannot lend more than one item at a time");
                    	hasLentItem = false;
                    	return;
                    }
                    if (partnerItemsLoaned.getFreeSlots() < amt && !pl.getInventory().getContainer().get(slot).getDefinition().isNoted() && !pl.getInventory().getContainer().get(slot).getDefinition().isStackable()) {
                        item.setAmount(partnerItemsLoaned.getFreeSlots());
                    }
                    partnerItemsLoaned.add(item);
                    pl.getInventory().getContainer().remove(item);
                    pl.getInventory().refresh();
                    tradeModifiedTrader = false;
                    hasLentItem = true;
                }
            }
            refreshScreen();
        }
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
                    tradeModifiedPartner = false;
                    traderItemsOffered.add(item);
                    pl.getInventory().getContainer().remove(new Item(pl.getInventory().getContainer().get(slot).getId(), amt));
                    pl.getInventory().refresh();
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
                    tradeModifiedTrader = false;
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
                    tradeWarningPartner(partner, slot);
                    tradeWarningMySide(trader, slot);
                    //refreshScreen(slot);
                    traderItemsOffered.remove(item);
                    resetAccept();
                    tradeModifiedTrader = true;
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
                    //refreshScreen(slot);
                    tradeWarningPartner(trader, slot);
                    tradeWarningMySide(partner, slot);
                    partnerItemsOffered.remove(item);
                    tradeModifiedPartner = true;
                    resetAccept();
                }
            }
            //refreshScreen(slot);
        }
    }

    private void refreshScreen() {
        ActionSender.sendItems(trader, 90, traderItemsOffered, false);
        ActionSender.sendItems(partner, 90, partnerItemsOffered, false);
        ActionSender.sendItems(trader, 90, partnerItemsOffered, true);
        ActionSender.sendItems(partner, 90, traderItemsOffered, true);
        ActionSender.sendItems(trader, 541, traderItemsLoaned, false);
        ActionSender.sendItems(partner, 541, partnerItemsLoaned, false);
        ActionSender.sendItems(trader, 541, partnerItemsLoaned, true);
        ActionSender.sendItems(partner, 541, traderItemsLoaned, true);
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
		if (partner.getTradeSession().tradeModifiedPartner == true) {
			//tradeWarningPartner(trader, slot);
			//tradeWarningMySide(partner, slot);
>>>>>>> d9616223ef4c9f16b6274d636e789bf214d81346
			ActionSender.sendConfig(trader, 1043, 1);
			ActionSender.sendConfig(partner, 1042, 1);
		}
		if (trader.getTradeSession().tradeModifiedTrader == true) {
<<<<<<< HEAD
			ActionSender.sendConfig(trader, 1042, 1);
			ActionSender.sendConfig(partner, 1043, 1);
		}
	}

	private void refreshScreen(final int modifiedSlot) {
		System.out.println("Refresh2...\r\n"+Thread.currentThread().getStackTrace()[2]);
		ActionSender.sendItems(trader, 90, traderItemsOffered, false);
		ActionSender.sendItems(partner, 90, partnerItemsOffered, false);
		ActionSender.sendItems(trader, 90, partnerItemsOffered, true);
		ActionSender.sendItems(partner, 90, traderItemsOffered, true);
		ActionSender.sendItems(trader, 541, traderItemsLoaned, false);
		ActionSender.sendItems(partner, 541, partnerItemsLoaned, false);
		ActionSender.sendItems(trader, 541, partnerItemsLoaned, true);
		ActionSender.sendItems(partner, 541, traderItemsLoaned, true);
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
		tradeWarningPartner(trader, modifiedSlot);
		tradeWarningMySide(partner, modifiedSlot);
		ActionSender.sendConfig(trader, 1043, 1);
		ActionSender.sendConfig(partner, 1042, 1);
		World.getWorld().submit(new Tick(1) {
			@Override
			public void execute() {
				tradeWarningPartner(trader, modifiedSlot);
        		tradeWarningMySide(partner, modifiedSlot);
        		if (partner.getTradeSession().tradeModifiedPartner == true) {
        			ActionSender.sendConfig(trader, 1043, 1);
        			ActionSender.sendConfig(partner, 1042, 1);
        		}
        		if (trader.getTradeSession().tradeModifiedTrader == true) {
        			ActionSender.sendConfig(trader, 1042, 1);
        			ActionSender.sendConfig(partner, 1043, 1);
        		}
				this.stop();
			}
		});
	}

	public int getTradersItemsValue() {
		int initialPrice = 0;
		for (Item item : traderItemsOffered.toArray()) {
			if (item != null) {
				initialPrice += item.getDefinition().getExchangePrice();
			}
		}
		return initialPrice;
	}

	public int getPartnersItemsValue() {
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
		trader.getInventory().getContainer().addAll(traderItemsLoaned);
		partner.getInventory().getContainer().addAll(partnerItemsLoaned);
		endSession();
		trader.getInventory().refresh();
		partner.getInventory().refresh();
	}

	public void endSession() {
		traderItemsOffered = partnerItemsOffered = null;
		traderItemsLoaned = partnerItemsLoaned= null;
		trader.setTradeSession(null);
		partner.setTradePartner(null);
		ActionSender.sendCloseInterface(trader);
		ActionSender.sendCloseInterface(partner);
		ActionSender.closeInventoryInterface(trader);
		ActionSender.closeInventoryInterface(partner);
	}
	
	public void sendDelayedAction(int delay, final Action e){
		World.getWorld().submit(new Tick(delay) {
			@Override
			public void execute() {
				e.run();
				e.stop();
				this.stop();
			}
		});
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

	public Container getPlayerItemsLoaned(Player p) {
		return (p.equals(trader) ? traderItemsLoaned : partnerItemsLoaned);
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
=======
			//tradeWarningPartner(partner, slot); Didnt work :S
			//tradeWarningMySide(trader, slot);
			ActionSender.sendConfig(trader, 1042, 1);
			ActionSender.sendConfig(partner, 1043, 1);
		}
    }

    public int getTradersItemsValue() {
        int initialPrice = 0;
        for (Item item : traderItemsOffered.toArray()) {
            if (item != null) {
                initialPrice += item.getDefinition().getExchangePrice();
            }
        }
        return initialPrice;
    }

    public int getPartnersItemsValue() {
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
        trader.getInventory().getContainer().addAll(traderItemsLoaned);
        partner.getInventory().getContainer().addAll(partnerItemsLoaned);
        endSession();
        trader.getInventory().refresh();
        partner.getInventory().refresh();
    }

    public void endSession() {
        traderItemsOffered = partnerItemsOffered = null;
        traderItemsLoaned = partnerItemsLoaned= null;
        trader.setTradeSession(null);
        partner.setTradePartner(null);
        ActionSender.sendCloseInterface(trader);
        ActionSender.sendCloseInterface(partner);
        ActionSender.closeInventoryInterface(trader);
        ActionSender.closeInventoryInterface(partner);
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
    
    public Container getPlayerItemsLoaned(Player p) {
        return (p.equals(trader) ? traderItemsLoaned : partnerItemsLoaned);
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
>>>>>>> d9616223ef4c9f16b6274d636e789bf214d81346
}
