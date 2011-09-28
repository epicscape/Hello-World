package org.dementhium.content.interfaces;

import org.dementhium.content.skills.Prayer;
import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;
import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Inventory;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class ItemsKeptOnDeath {

    public static class EquipmentInterfaceListener extends EventListener {

        @Override
        public void register(EventManager manager) {
            manager.registerInterfaceListener(387, this);
        }

        public boolean interfaceOption(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
            if (buttonId == 45) {
                displayInterface(player);
                return true;
            }
            return false;
        }

    }

    /*private static final Comparator<Item> PRICE_ORDER = new Comparator<Item>() {
        @Override
        public int compare(Item o1, Item o2) {
            if (o1 != null && o2 == null) {
                return 1;
            } else if (o1 == null && o2 != null) {
                return -1;
            } else if (o1 == null && o2 == null) {
                return 0;
            }
            if (o1.getDefinition().getStorePrice() > o2.getDefinition().getStorePrice()) {
                return 1;
            } else if (o1.getDefinition().getStorePrice() < o2.getDefinition().getStorePrice()) {
                return -1;
            }
            return 0;
        }
    };*/

    public static void displayInterface(Player player) {
        int allowedAmount = allowedAmount(player);
        int carriedWealth = getCarriedWealth(player);
        Container[] itemData = getDeathContainers(player);
        int riskedWealth = getRiskedWealth(itemData[1]);

        int displayType = 0;

        //I had this wrong =p
        //		if(player.inWilderness()) {
        //			displayType = 0;
        //		}
        Container keptItems = new Container(itemData[0].size(), false, true);
        for (Item item : itemData[0].toArray()) {
            if (item != null) {
                keptItems.add(item);
            }
        }
        keptItems.shift();
        ItemsKeptOnDeath.sendPacket(player, allowedAmount, riskedWealth, carriedWealth, false/*player.getFamiliar() instanceof BeastOfBurden*/, false, displayType, keptItems);
    }

    /**
     * @return Two containers, one being the items you keep on death, and the second being the lost items.
     * @author Michael (Scu11)
     * Gets two containers, one being the items you keep on death, and the second being the lost items.
     */
    /*public static Container[] getKeptItems(Player player, int allowed) {
        Container topItems = new Container(allowed, false, true);
        Container clonedInventory = player.getInventory().getContainer().clone();
        Container clonedEquipment = player.getEquipment().getContainer().clone();
        for (int i = 0; i < Inventory.SIZE; i++) {
            Item item = clonedInventory.get(i);
            if (item != null) {
                item = new Item(clonedInventory.get(i).getId(), 1);
                for (int k = 0; k < allowed; k++) {
                    Item topItem = topItems.get(k);
                    if (topItem == null || item.getDefinition().getStorePrice() > topItem.getDefinition().getStorePrice()) {
                        if (topItem != null) {
                            topItems.remove(topItem);
                        }
                        topItems.add(item);
                        clonedInventory.remove(item);
                        if (topItem != null) {
                            clonedInventory.add(topItem);
                        }
                        if (item.getDefinition().isStackable() && clonedInventory.getItemCount(item.getId()) > 0) {
                            i--;
                        }
                        break;
                    }
                }
            }
        }
        for (int i = 0; i < Equipment.SIZE; i++) {
            Item item = clonedEquipment.get(i);
            if (item != null) {
                item = new Item(clonedEquipment.get(i).getId(), 1);
                for (int k = 0; k < allowed; k++) {
                    int lowest = -1;
                    int lowestSlot = -1;
                    //This fixes the bug with inv
                    for (int j = 0; j < topItems.size(); j++) {
                        if (topItems.get(j) != null) {
                            if (lowest == -1 || lowest > topItems.get(j).getDefinition().getStorePrice()) {
                                lowest = topItems.get(j).getDefinition().getStorePrice();
                                lowestSlot = j;
                            }
                        } else {
                            lowest = -1;
                            lowestSlot = j;
                        }
                    }
                    Item topItem = topItems.get(lowestSlot);
                    if (topItem == null || item.getDefinition().getStorePrice() > topItem.getDefinition().getStorePrice()) {
                        if (topItem != null) {
                            topItems.remove(topItem);
                        }
                        topItems.add(item);
                        clonedEquipment.remove(item);
                        if (topItem != null) {
                            clonedEquipment.add(topItem);
                        }
                        if (item.getDefinition().isStackable() && clonedEquipment.getItemCount(item.getId()) > 0) {
                            i--;
                        }
                        break;
                    }
                }
            }
        }
        Container lostItems = new Container(Inventory.SIZE + Equipment.SIZE, false);
        for (Item lostItem : clonedInventory.toArray()) {
            if (lostItem != null) {
                lostItems.add(lostItem);
            }
        }
        for (Item lostItem : clonedEquipment.toArray()) {
            if (lostItem != null) {
                lostItems.add(lostItem);
            }
        }
        topItems.sort(PRICE_ORDER);
        return new Container[]{topItems, lostItems};
    }*/
    
	/**
	 * Gets the death containers.<br>
	 * 1 is items kept, 2 is items lost.
	 * 
	 * @return The 2 containers.
	 */
	public static Container[] getDeathContainers(Player player) {
		int count = 3;
		if (player.getPrayer().usingPrayer(0, Prayer.PROTECT_ITEM) || player.getPrayer().usingPrayer(1, Prayer.CURSE_PROTECT_ITEM)) {
			count++;
		}
		if (player.getSkullManager().isSkulled()) {
			count -= 3;
		}
		Container itemsKept = new Container(count, false);
		Container itemsLost = new Container(36, false);
		itemsLost.addAll(player.getInventory().getContainer());
		itemsLost.addAll(player.getEquipment().getContainer());
		Item toRemove = null;
		Item toAdd = null;
		Item[] toReplace = null;
		for (int i = 0; i < count; i++) {
			for (Item item : itemsLost.toArray()) {
				if (item == null) {
					continue;
				}
				ItemDefinition id;
				if (!(id = item.getDefinition()).isStackable()) {
					if (itemsKept.get(i) == null) {
						itemsKept.set(i, item);
						toRemove = item;
					} else if (id.getHighAlchPrice() > itemsKept.get(i)
							.getDefinition().getHighAlchPrice()) {
						if (itemsKept.get(i).getDefinition().isStackable()) {
							toAdd = itemsKept.get(i);
						}
						toRemove = item;
						itemsKept.set(i, item);
					}
				} else {
					if (itemsKept.get(i) == null) {
						if (item.getAmount() > 0) {
							itemsKept.set(i, new Item(item.getId(), item.getAmount() - (item.getAmount() - 1)));
							toReplace = new Item[2];
							toReplace[0] = item;
							toReplace[1] = new Item(item.getId(),
									item.getAmount() - 1);
						}
					} else if (id.getHighAlchPrice() > itemsKept.get(i)
							.getDefinition().getHighAlchPrice()) {
						toReplace = new Item[2];
						toReplace[0] = item;
						toReplace[1] = new Item(item.getId(),
								item.getAmount() - 1);
						itemsKept.set(i, new Item(item.getId(), 1));
					}
				}
			}
			if (toAdd != null) {
				itemsLost.add(toAdd);
				toAdd = null;
			}
			if (toRemove != null) {
				itemsLost.remove(toRemove);
				toRemove = null;
			}
			if (toReplace != null) {
				itemsLost.remove(toReplace[0]);
				if (toReplace[1].getAmount() > 0) {
					itemsLost.add(toReplace[1]);
				}
				toReplace = null;
			}
		}
		return new Container[] { itemsKept, itemsLost };
	}

    public static void sendPacket(Player player, int allowedItems, int riskedWealth, int carriedWealth, boolean hasFamiliar, boolean skulled, int type, Container keptItems) {
        ActionSender.sendAMask(player, 211, 0, 2, 102, 18, 4);
        ActionSender.sendAMask(player, 212, 0, 2, 102, 21, 42);
        Object[] params = new Object[]{riskedWealth, carriedWealth, "", hasFamiliar ? 1 : 0, skulled ? 1 : 0, keptItems.getItemSlot(3), keptItems.getItemSlot(2), keptItems.getItemSlot(1), keptItems.getItemSlot(0), allowedItems, type};
        ActionSender.sendClientScript(player, 118, params, "noooooobsll");
        ActionSender.sendBConfig(player, 199, 442);
        ActionSender.sendInterface(player, 102);
    }

    public static int allowedAmount(Player player) {
        int allowedItems = 3;
        if (player.getPrayer().usingPrayer(0, Prayer.PROTECT_ITEM) || player.getPrayer().usingPrayer(1, Prayer.CURSE_PROTECT_ITEM)) {
            allowedItems++;
        }
        if (player.getSkullManager().isSkulled()) {
        	allowedItems -= 3;
        }
        return allowedItems;
    }

    public static int getCarriedWealth(Player player) {
        long amount = 0;
        for (int i = 0; i < Inventory.SIZE; i++) {
            Item item = player.getInventory().get(i);
            if (item != null) {
                amount += item.getDefinition().getExchangePrice() * item.getAmount();
            }
        }
        for (int i = 0; i < Equipment.SIZE; i++) {
            Item item = player.getEquipment().get(i);
            if (item != null) {
                amount += item.getDefinition().getExchangePrice() * item.getAmount();
            }
        }
        if (amount < 0 || amount > Integer.MAX_VALUE) {
            amount = Integer.MAX_VALUE;
        }
        return (int) amount;
    }

    public static int getRiskedWealth(Container lostItems) {
        long amount = 0;
        for (Item item : lostItems.toArray()) {
            if (item != null) {
                amount += item.getDefinition().getExchangePrice() * item.getAmount();
            }
        }
        if (amount < 0 || amount > Integer.MAX_VALUE) {
            amount = Integer.MAX_VALUE;
        }
        return (int) amount;
    }

    //types, 1=safe area, 2=poh, 3=in castlewars 4=in trouble brewing 5=barbie assualt

}
