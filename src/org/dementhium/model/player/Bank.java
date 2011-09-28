package org.dementhium.model.player;

import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.net.ActionSender;

public class Bank {

	public static int SIZE = 516;
	public static int TAB_SIZE = 11;

	private final Container bank = new Container(SIZE, true);
	private final Player player;
	private final int[] tabStartSlot = new int[TAB_SIZE];

	private boolean checkingBank = false;
	public Bank(Player player) {
		this.player = player;
	}

	public void openBank() {
		if (player.getTradeSession() != null) {
			player.getTradeSession().tradeFailed();
		}
		if (player.getAttribute("cantMove") == Boolean.TRUE) {
            return;
        }
		checkingBank = false;
		player.setAttribute("inBank", Boolean.TRUE);
		player.setAttribute("bankScreen", 2);
		player.setAttribute("currentTab", 10);
		ActionSender.sendItems(player, 31, player.getInventory().getContainer(), false);
		ActionSender.sendItems(player, 95, bank, false);
		ActionSender.sendAMask(player, 0, 516, 762, 93, 40, 1278);
		ActionSender.sendAMask(player, 0, 27, 763, 0, 37, 1150);
		ActionSender.sendConfig(player, 563, 4194304);
		//ActionSender.sendConfig(player, 1248, -2013265920);
		//Amask 194: 27 195: 4261378 196: 50003970 197: 0
		/*Amask 194: 516 195: 2622718 196: 49938525 197: 0
		Amask 194: 27 195: 2361214 196: 50003968 197: 0
		Amask 194: 14 195: 4261378 196: 43712519 197: 0
		Amask 194: 27 195: 4261378 196: 50003970 197: 0*/

		/*
		 * bldr.writeInt2(interfaceId2 << 16 | childId2);
		bldr.writeShortA(set2);
		bldr.writeShortA(set1);
		bldr.writeLEInt(interfaceId1 << 16 | childId1);
		player.write(bldr.toMessage());

		 */
		ActionSender.sendBlankClientScript(player, 1451);
		ActionSender.sendInterface(player, 762);
		ActionSender.sendInventoryInterface(player, 763);
		ActionSender.sendString(player, 762, 31, bank.size()+"");
		ActionSender.sendString(player, 762, 32, SIZE+"");
		ActionSender.sendString(player, 762, 45, "Bank of Dementhium");
		sendTabConfig();
		//ActionSender.sendConfig(player, 1248, player.getAttribute("currentTabConfig", -2013265920)); //Sends the currently viewed tab <3
	}

	public void openPlayerBank(Player victim) {
		if (victim == null) {
			return;
		}
		if (player.getTradeSession() != null) {
			player.getTradeSession().tradeFailed();
		}
		checkingBank = true;
		player.setAttribute("inBank", Boolean.TRUE);
		player.setAttribute("bankScreen", 2);
		player.setAttribute("currentTab", 10);
		ActionSender.sendItems(player, 31, victim.getInventory().getContainer(), false);
		ActionSender.sendItems(player, 95, victim.getBank().getContainer(), false);
		ActionSender.sendAMask(player, 0, 516, 762, 93, 40, 1278);
		ActionSender.sendAMask(player, 0, 27, 763, 0, 37, 1150);
		ActionSender.sendConfig(player, 563, 4194304);
		ActionSender.sendConfig(player, 1248, -2013265920);
		ActionSender.sendBlankClientScript(player, 1451);
		ActionSender.sendInterface(player, 762);
		ActionSender.sendInventoryInterface(player, 763);
		sendTabConfig();
	}

	public void addItem(int slot, int amount) {
		if (checkingBank) {
			return;
		}
		addItem(slot, amount, true);
	}
	
	public void addItem(int slot, int amount, boolean refresh) {
		if (checkingBank) {
			return;
		}
		if (player.getAttribute("inBank", Boolean.FALSE) == Boolean.TRUE) {
			ActionSender.sendCloseChatBox(player);
			Item item = player.getInventory().get(slot);
			if (item == null) {
				return;
			}
			int playerAmount = player.getInventory().getContainer().getNumberOf(item);
			int currentTab = (Integer) player.getAttribute("currentTab", 1);
			if (playerAmount < amount) {
				amount = playerAmount;
			}
			if (item.getDefinition().isNoted()) {
				item = new Item(item.getId() == 10843 ? 10828 : item.getId() - 1, item.getAmount());
				player.getInventory().deleteItem(item.getId() == 10828 ? 10843 : item.getId() + 1, amount, slot, refresh);
			} else {
				player.getInventory().deleteItem(item.getId(), amount, slot, refresh);
			}
			int index = bank.indexOf(item);
			if (index > -1) {
				Item item2 = bank.get(index);
				if (item2 != null) {
					if (item2.getId() == item.getId()) {
						bank.set(index, new Item(item.getId(), amount + item2.getAmount()));
					}
				}
			} else {
				int freeSlot;
				if (currentTab == 10) {
					freeSlot = bank.getFreeSlot();
				} else {
					freeSlot = tabStartSlot[currentTab] + getItemsInTab(currentTab);
				}
				if (item.getAmount() > 0) {
					if (currentTab != 10) {
						insert(bank.getFreeSlot(), freeSlot);
						increaseTabStartSlots(currentTab);
					}
					bank.set(freeSlot, new Item(item.getId(), amount));
				}
			}
			if (refresh) {
				refresh();
			}
		}
	}

	public void refresh() {
		ActionSender.sendItems(player, 95, bank, false);
		ActionSender.sendString(player, 762, 31, bank.size()+"");
		sendTabConfig();
	}


	public void removeItem(int slot, int amount) {
		if (checkingBank) {
			return;
		}
		if (player.getAttribute("inBank", Boolean.FALSE) == Boolean.TRUE) {
			ActionSender.sendCloseChatBox(player);
			if (slot < 0 || slot > Bank.SIZE || amount <= 0) {
				return;
			}
			Item item = bank.get(slot);
			Item item2 = bank.get(slot);
			Item item3 = bank.get(slot);
			int tabId = getTabByItemSlot(slot);
			if (item == null) {
				return;
			}
			if (amount > item.getAmount()) {
				item = new Item(item.getId(), item.getAmount());
				item2 = new Item(item.getId() == 10828 ? 10843 : item.getId() + 1, item.getAmount());
				item3 = new Item(item.getId(), item.getAmount());
				if (noting()) {
					if (item2.getDefinition().isNoted() && item2.getDefinition().getName().equals(item.getDefinition().getName()) && !item.getDefinition().isStackable()) {
						item = new Item(item.getId() == 10828 ? 10843 : item.getId() + 1, item.getAmount());
					} else {
						player.sendMessage("You cannot withdraw this item as a note.");
						item = new Item(item.getId(), item.getAmount());
					}
				}
			} else {
				item = new Item(item.getId(), amount);
				item2 = new Item(item.getId(), amount);
				item3 = new Item(item.getId(), amount);
				if (noting()) {
					item2 = new Item(item.getId() == 10828 ? 10843 : item.getId() + 1, item.getAmount());
					if (item2.getDefinition().isNoted() && item2.getDefinition().getName().equals(item.getDefinition().getName()) && !item.getDefinition().isStackable()) {
						item = new Item(item.getId() == 10828 ? 10843 : item.getId() + 1, item.getAmount());
					} else {
						player.sendMessage("You cannot withdraw this item as a note.");
						item = new Item(item.getId(), item.getAmount());
						return;
					}
				}
			}
			if (amount > player.getInventory().getFreeSlots() && !item3.getDefinition().isStackable() && !noting()) {
				item = new Item(item.getId(), player.getInventory().getFreeSlots());
				item2 = new Item(item2.getId(), player.getInventory().getFreeSlots());
				item3 = new Item(item3.getId(), player.getInventory().getFreeSlots());
			}
			if (bank.contains(item3)) {
				if (player.getInventory().getFreeSlots() <= 0) {
					player.sendMessage("Not enough space in your inventory.");
				} else {
					if (noting() && !item.getDefinition().isNoted()) {
						player.getInventory().addItem(item.getId(), item.getAmount());
						bank.remove(item3);
					} else {
						player.getInventory().addItem(item.getId(), item.getAmount());
						bank.remove(item3);
					}
				}
			}
			if (get(slot) == null) {
				decreaseTabStartSlots(tabId);
			}
			bank.shift();
			refresh();
		}
	}

	public boolean noting() {
		return player.getAttribute("noting") == Boolean.TRUE;
	}

	/**
	 * Banks the inventory items.
	 */
	public void bankInv() {
		if (checkingBank) {
			return;
		}
		if (player.getInventory().getContainer().size() < 1) {
			player.sendMessage("You don't have any items to bank.");
			return;
		}
		player.getInventory();
		for (int i = 0; i < Inventory.SIZE; i++) {
			Item item = player.getInventory().get(i);
			if (item != null) {
				addItem(i, item.getAmount(), false);
			}
		}
		refresh();
		player.getInventory().refresh();
	}

	/**
	 * Banks all the equipped items.
	 */
	public void bankEquip() {
		if (checkingBank) {
			return;
		}
		if (player.getEquipment().getContainer().size() < 1) {
			player.sendMessage("You're not wearing anything to bank.");
			return;
		}
		bankItems(player.getEquipment().getContainer());
		refresh();
		player.getEquipment().refresh();
	}

	/**
	 * Banks all of the beast of burden's items.
	 */
	public void bankBob() {
		if (checkingBank) {
			return;
		}
		/*if (!player.getFamiliar().isBurdenBeast()) {
			player.sendMessage("You do not have a beast of burden.");
			return;
		} else if (player.getFamiliar().getBurdenBeast().getContainer().size() < 1) {
			player.sendMessage("Your beast of burden is not wearing any items.");
			return;
		}
		bankItems(player.getFamiliar().getBurdenBeast().getContainer());*/
		refresh();
	}

	/**
	 * Banks all the items from a certain container.
	 * @param container The container.
	 * @return {@code True}.
	 */
	private boolean bankItems(Container container) {
		int currentTab = (Integer) player.getAttribute("currentTab", 1);
		try {
		for (int i = 0; i < container.getSize(); i++) {
			Item item = container.get(i);
			if (item == null) {
				continue;
			}
			Item toBank = item;
			if (item.getDefinition().isNoted()) {
				toBank = new Item(item.getId() == 10843 ? 10828 : item.getId() - 1, item.getAmount());
			}
			container.set(i, null);
			int index = bank.indexOf(item);
			if (index > -1) {
				Item item2 = bank.get(index);
				if (item2 != null) {
					if (item2.getId() == item.getId()) {
						bank.set(index, new Item(toBank.getId(), item.getAmount() + item2.getAmount()));
					}
				}
			} else {
				int freeSlot;
				if (currentTab == 10) {
					freeSlot = bank.getFreeSlot();
				} else {
					freeSlot = tabStartSlot[currentTab] + getItemsInTab(currentTab);
				}
				if (item.getAmount() > 0) {
					if (currentTab != 10) {
						insert(bank.getFreeSlot(), freeSlot);
						increaseTabStartSlots(currentTab);
					}
					bank.set(freeSlot, new Item(toBank.getId(), item.getAmount()));
				}
			}
		}
		} catch (Throwable t) {
			t.printStackTrace();
		}
		return true;
	}
	
	public boolean contains(int item, int amount) {
		return bank.contains(new Item(item, amount));
	}

	public boolean contains(int item) {
		return bank.contains(new Item(item));
	}

	public Container getContainer() {
		return bank;
	}

	public Item get(int slot) {
		return bank.get(slot);
	}

	public void set(int slot, Item item) {
		bank.set(slot, item);
	}

	public void increaseTabStartSlots(int startId) {
		for (int i = startId + 1; i < tabStartSlot.length; i++) {
			tabStartSlot[i]++;
		}
	}

	public void decreaseTabStartSlots(int startId) {
		if (startId == 10)
			return;
		for (int i = startId + 1; i < tabStartSlot.length; i++) {
			tabStartSlot[i]--;
		}
		if (getItemsInTab(startId) == 0) {
			collapseTab(startId);
		}
	}

	public void insert(int fromId, int toId) {
		Item temp = bank.toArray()[fromId];
		if (toId > fromId) {
			for (int i = fromId; i < toId; i++) {
				set(i, get(i + 1));
			}
		} else if (fromId > toId) {
			for (int i = fromId; i > toId; i--) {
				set(i, get(i - 1));
			}
		}
		set(toId, temp);
	}

	public int getItemsInTab(int tabId) {
		return tabStartSlot[tabId + 1] - tabStartSlot[tabId];
	}

	public int getTabByItemSlot(int itemSlot) {
		int tabId = 0;
		for (int i = 0; i < tabStartSlot.length; i++) {
			if (itemSlot >= tabStartSlot[i]) {
				tabId = i;
			}
		}
		return tabId;
	}

	public void collapseTab(int tabId) {
		if (checkingBank) {
			return;
		}
		int size = getItemsInTab(tabId);
		Item[] tempTabItems = new Item[size];
		for (int i = 0; i < size; i++) {
			tempTabItems[i] = get(tabStartSlot[tabId] + i);
			set(tabStartSlot[tabId] + i, null);
		}
		bank.shift();
		for (int i = tabId; i < tabStartSlot.length - 1; i++) {
			tabStartSlot[i] = tabStartSlot[i + 1] - size;
		}
		tabStartSlot[10] = tabStartSlot[10] - size;
		for (int i = 0; i < size; i++) {
			int slot = bank.getFreeSlot();
			set(slot, tempTabItems[i]);
		}
	}

	public void sendTabConfig() {
		int config = 0;
		config += getItemsInTab(2);
		config += getItemsInTab(3) << 10;
		config += getItemsInTab(4) << 20;
		ActionSender.sendConfig(player, 1246, config);
		config = 0;
		config += getItemsInTab(5);
		config += getItemsInTab(6) << 10;
		config += getItemsInTab(7) << 20;
		ActionSender.sendConfig(player, 1247, config);
		int tab = (Integer) player.getAttribute("currentTab", 10);
		config = -2013265920;
		config += (134217728 * (tab == 10 ? 0 : tab - 1));
		config += getItemsInTab(8);
		config += getItemsInTab(9) << 10;
		ActionSender.sendConfig(player, 1248, config);
	}

	public static int getArrayIndex(int tabId) {
		if (tabId == 62 || tabId == 74) {
			return 10;
		}
		int base = 60;
		for (int i = 2; i < 10; i++) {
			if (tabId == base) {
				return i;
			}
			base -= 2;
		}
		base = 74;
		for (int i = 2; i < 10; i++) {
			if (tabId == base) {
				return i;
			}
			base++;
		}
		//Should not happen
		return -1;
	}
	
	/**
	 * Gets the config value for setting the currently viewed tab.
	 * @param buttonId The button id.
	 * @return The config value.
	 */
	public static int getViewedTabConfig(int buttonId) {
		if (buttonId == 46) {
			return -939499493;
		} else if (buttonId == 48) {
			return -1073717221;
		} else if (buttonId == 50) {
			return -1207934949;
		} else if (buttonId == 52) {
			return -1342152677;
		} else if (buttonId == 54) {
			return -1476370405;
		} else if (buttonId == 56) {
			return -1610588133;
		} else if (buttonId == 58) {
			return -1744805861;
		} else if (buttonId == 60) {
			return -1879023589;
		}
		return -2013241317;
	}

	public int[] getTab() {
		return tabStartSlot;
	}

	public boolean isCheckingBank() {
		return checkingBank;
	}
}