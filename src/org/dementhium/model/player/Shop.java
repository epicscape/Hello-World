package org.dementhium.model.player;

import org.dementhium.content.SkillCapes;
import org.dementhium.content.interfaces.ItemInfo;
import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;

import java.text.NumberFormat;
import java.util.ArrayList;


public class Shop {

	public static final int[] unsellableItems = new int[]{995};

	private static final int RESTOCK_TIME = 70;

	private int STORE_SIZE = 40;
	private Container shop = new Container(STORE_SIZE, true);
	private ArrayList<Player> playersViewing = new ArrayList<Player>();
	private boolean generalStore = false;

	public boolean isGeneralStore() {
		return generalStore;
	}

	public void setGeneral(boolean b) {
		this.generalStore = b;
	}


	public static final Object[] params = new Object[]{"Sell 50", "Sell 10", "Sell 5", "Sell 1", "Value", -1, 1, 7, 4, 93, 40697856};

	private int shopId = 0;

	private int[] origAmounts;
	private int[] origItems;

	public Shop(int id, boolean isGeneralStore, int[] items, int[] amounts, boolean inGame) {
		this.shopId = id;
		this.generalStore = isGeneralStore;
		if (!inGame) {
			shop = new Container(STORE_SIZE, false);
		}
		if (items != null && amounts != null) {
			for (int itemSlot = 0; itemSlot < items.length; itemSlot++) {
				shop.set(shop.getFreeSlot(), new Item(items[itemSlot], amounts[itemSlot]));
			}
		}
		origItems = items;
		origAmounts = amounts;
		if (inGame)
			startRestocking();
	}

	private void startRestocking() {
		World.getWorld().submit(new Tick(RESTOCK_TIME) {

			@Override
			public void execute() {
				for (int i = 0; i < shop.getSize(); i++) {
					if (i < origAmounts.length) {
						if (shop.get(i) != null) {
							if (shop.get(i).getAmount() < origAmounts[i]) {
								shop.add(new Item(shop.get(i).getId(), 1));
							} else if (shop.get(i).getAmount() > origAmounts[i]) {
								shop.remove(new Item(shop.get(i).getId(), 1));
							}
						}
					}
				}
				for (int i = origAmounts.length; i < shop.getSize(); i++) {
					if (shop.get(i) != null) {
						shop.remove(new Item(shop.get(i).getId(), 1));
					}
				}
				update();
			}
		});

	}
/*
 * ActionSender.sendConfig(player, 118, 3);
		ActionSender.sendConfig(player, 1496, 553);
		ActionSender.sendConfig(player, 532, 995);
		//ActionSender.sendBConfig(player, 199, -1);
		ActionSender.sendInterface(player, 620);
		sendInventory(player);
		ActionSender.sendAMask(player, 0, 12, 620, 26, 0, 1150);
		ActionSender.sendAMask(player, 0, 240, 620, 25, 0, 1150);
		ActionSender.sendItems(player, 3, shop, false);
 */
	public void open(Player player) {
		ActionSender.sendConfig(player, 118, 4);
		ActionSender.sendConfig(player, 1496, -1);//has a value if free items are avaliable example, senditems 555 would be value 555
		ActionSender.sendConfig(player, 532, 995);
		//ActionSender.sendBConfig(player, 199, -1);
		ActionSender.sendInterface(player, 620);
		sendInventory(player);
		ActionSender.sendAMask(player, 0, 12, 620, 26, 0, 1150);
		ActionSender.sendAMask(player, 0, 240, 620, 25, 0, 1150);

		ActionSender.sendItems(player, 4, shop, false);
	}


	public static void sendInventory(Player player) {
		ActionSender.sendInventoryInterface(player, 621);
		ActionSender.sendClientScript(player, 149, params, "IviiiIsssss");
		ActionSender.sendAMask(player, 0, 27, 621, 0, 36, 1086);
		ActionSender.sendItems(player, 93, player.getInventory().getContainer(), false);
	}

	public void addPlayer(Player player) {
		playersViewing.add(player);
	}

	public void removePlayer(Player player) {
		playersViewing.remove(player);
	}

	public void handleOption(Player p, int interfaceId, int buttonId,
			int buttonId2, int packetId, int itemIdent) {
		switch (interfaceId) {
		case 620:
			switch (buttonId) {
			case 18:
				removePlayer(p);
				p.removeAttribute("shopId");
				break;
			case 25:
			case 26:
				if (buttonId2 > 0) {
					buttonId2 /= 6;
				}
				switch (packetId) {
				case 58:
					ItemDefinition def = ItemDefinition.forId(shop.get(buttonId2).getId());
					if (def == null) {
						return;
					}
					p.sendMessage(def.getExamine());
					break;
				case 13:
					buyItem(p, shop.get(buttonId2).getId(), 1);
					break;
				case 0:
					buyItem(p, shop.get(buttonId2).getId(), 5);
					break;
				case 15:
					buyItem(p, shop.get(buttonId2).getId(), 10);
					break;
				case 46:
					buyItem(p, shop.get(buttonId2).getId(), 50);
					break;
				case 67:
					buyItem(p, shop.get(buttonId2).getId(), 500);
					break;
				case 6:
					ItemInfo.sendItemInfo(p, shop.get(buttonId2), buttonId2);
				}
				break;
			}
			break;
		case 621:
			Item definition = p.getInventory().getContainer().get(buttonId2);
			if (definition == null) {
				return;
			}
			int itemId = definition.getId();
			switch (buttonId) {
			case 0:
				switch (packetId) {
				case 13:
					sellItem(p, itemId, 1);
					break;
				case 0:
					sellItem(p, itemId, 5);
					break;
				case 15:
					sellItem(p, itemId, 10);
					break;
				case 46:
					sellItem(p, itemId, 50);
					break;
				case 67:
					sellItem(p, itemId, 500);
					break;
				case 58: {
					ItemDefinition def = p.getInventory().getContainer().get(buttonId2).getDefinition();
					p.sendMessage(def.getExamine());
				}
				break;
				case 6:
					ItemDefinition def = p.getInventory().getContainer().get(buttonId2).getDefinition();
					if (def.isNoted()) {
						def = ItemDefinition.forId(itemId - 1);
					}
					p.sendMessage(def.getName() + " costs " + formatPrice(def.getStorePrice()) + " coins.");
					break;
				}
			}
			break;
		}
	}

	private void sellItem(Player p, int itemId, int amount) {
		if (amount < 1) {
			return;
		}
		if (amount > p.getInventory().getContainer().getItemCount(itemId)) {
			amount = p.getInventory().getContainer().getItemCount(itemId);
		}
		Item item = new Item(itemId, amount);
		if (item.getDefinition().isNoted()) {
			item = new Item(item.getId() - 1, item.getAmount());
		}
		ItemDefinition def = item.getDefinition();
		if (def.getStorePrice() > 0) {
			int price = (int) (def.getStorePrice() * amount);
			for (int z = 0; z < 25; z++) {
				for (int x = 0; x < 2; x++) {
					if (SkillCapes.skillCapeId[z][x] == item.getId()) {
						price = 99000;
					}
				}
			}
			if (price < 0) {
				p.sendMessage("The price is too high to sell!");
				return;
			}
			if (!p.getInventory().contains(itemId)) {
				return;
			}
			if (!p.getInventory().hasRoomFor(995, price)) {
				ActionSender.sendMessage(p, "Not enough space in your inventory.");
				return;
			}
			if (!shop.contains(new Item(itemId, 1)) && !generalStore || itemId == 995) {
				ActionSender.sendMessage(p, "You cannot sell that item to this shop.");
				return;
			}
			if (p.getInventory().getContainer().getItemCount(itemId) < amount) {
				if (ItemDefinition.forId(itemId).isNoted()
						|| ItemDefinition.forId(itemId).isStackable()) {
					amount = p.getInventory().lookup(itemId).getAmount();
				} else {
					amount = p.getInventory().getContainer().getItemCount(itemId);
				}
				price = (int) ((int) def.getStorePrice() / 1.6 * amount);
				ActionSender.sendMessage(p, "You don't have enough of that item!");
				if (this.shopId > 100) {
					price = (int) ((int) def.getStorePrice() * amount);
				}

			}
			if (!hasRoomFor(itemId, amount)) {
				ActionSender.sendMessage(p, "The shop is full.");
				return;
			}
			for (int i = 1038; i < 1059; i++) {
				if (itemId == i) {
					p.sendMessage("You cannot sell a rare to a shop!");
					return;
				}
			}
			shop.add(item);
			p.getInventory().deleteItem(itemId, amount);
			p.getInventory().addItem(995, price);
			update();
		} else {
			ActionSender.sendMessage(p, "You cannot sell that item to this shop.");
		}
	}

	public boolean hasRoomFor(int id, int itemAmount) {
		return shop.getFreeSlots() >= 1 || shop.contains(new Item(id));
	}

	public void update() {
		for (Player player : playersViewing) {
			if (player == null) {
				playersViewing.remove(player);
			}
			ActionSender.sendItems(player, 93, player.getInventory().getContainer(),
					false);
			ActionSender.sendItems(player, 4, shop, false);
			for (int index = 0; index < 40; index++) {
				if (shop.get(index) != null) {
					//	ActionSender.sendBConfig(player, 946 + index, 0);
				}
			}
		}
	}

	private void buyItem(Player p, int id, int amount) {
		Item item = new Item(id, 1);
		if (item.getDefinition().getStorePrice() < 0) {
			item.getDefinition().setStorePrice(10);
		}
		long price = item.getDefinition().getStorePrice() * amount;
		for (int z = 0; z < 25; z++) {
			for (int x = 0; x < 2; x++) {
				if (SkillCapes.skillCapeId[z][x] == item.getId()) {
					price = 99000 * amount;
				}
			}
		}
		if (price < 0 || price >= Integer.MAX_VALUE) {
			p.sendMessage("Too expensive.");
			return;
		}
		if (p.getInventory().getFreeSlots() < amount && !(item.getDefinition().isNoted() || item.getDefinition().isStackable())) {
			ActionSender.sendMessage(p, "Not enough space in your inventory.");
			if (p.getInventory().getFreeSlots() == 0) {
				return;
			}
			amount = p.getInventory().getFreeSlots();
			price = ItemDefinition.forId(id).getStorePrice() * amount;
			for (int z = 0; z < 25; z++) {
				for (int x = 0; x < 2; x++) {
					if (SkillCapes.skillCapeId[z][x] == item.getId()) {
						price = 99000 * amount;
					}
				}
			}
		} else if (p.getInventory().getFreeSlots() < amount && (item.getDefinition().isNoted() || item.getDefinition().isStackable())) {
			if (!p.getInventory().contains(id) && p.getInventory().getFreeSlots() == 0) {
				ActionSender.sendMessage(p, "Not enough space in your inventory.");
				return;
			}
		}
		if (shop.getItemCount(id) < amount) {
			ActionSender.sendMessage(p, "The shop has run out of stock of that item.");
			amount = shop.getItemCount(id);
			price = ItemDefinition.forId(id).getStorePrice() * amount;
			for (int z = 0; z < 25; z++) {
				for (int x = 0; x < 2; x++) {
					if (SkillCapes.skillCapeId[z][x] == item.getId()) {
						price = 99000 * amount;
					}
				}
			}
		}
		if (p.getInventory().getContainer().getItemCount(995) < price) {
			ActionSender.sendMessage(p, "You do not have enough coins for that many.");
			amount = p.getInventory().getContainer().getItemCount(995) / ItemDefinition.forId(id).getStorePrice();
			price = ItemDefinition.forId(id).getStorePrice() * amount;
			if (price < ItemDefinition.forId(id).getStorePrice()) {
				return;
			}
		}
		p.getInventory().deleteItem(995, (int) price);
		p.getInventory().addItem(id, amount);
		System.out.println(shop.get(shop.lookupSlot(id)).getAmount()+" "+amount);
		if (origItems.length > shop.lookupSlot(id) && (shop.get(shop.lookupSlot(id)).getAmount() - amount == 0)) {
			shop.get(shop.lookupSlot(id)).setAmount(0);
		} else {
			shop.remove(new Item(id, amount));
		}
		update();
		//} else {
		//ActionSender.sendMessage(p, "You cannot buy that item from this shop");
		//}
	}

	public static String formatPrice(int price) {
		return NumberFormat.getInstance().format(price);
	}

	public Container getShop() {
		return shop;
	}

	public void addItem(int id, int amount) {
		shop.add(new Item(id, amount));
	}

	public void removeItem(int slot) {
		shop.set(slot, null);
	}

	public void set(int slot, Item i) {
		shop.set(slot, i);
	}


	public Integer getId() {
		return shopId;
	}

	public void setId(int parseInt) {
		this.shopId = parseInt;

	}

	public void clear() {
		shop.clear();

	}


}