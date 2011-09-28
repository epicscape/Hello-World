package org.dementhium.content.activity.impl.duel;

import org.dementhium.content.activity.impl.DuelActivity;
import org.dementhium.model.Container;
import org.dementhium.model.Item;
import org.dementhium.model.player.Inventory;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;

/**
 * The player's stake containers.
 *
 * @author Emperor
 */
public class Stakes {

    /**
     * The container.
     */
    private Container stake;

    /**
     * The player.
     */
    private final Player player;

    /**
     * Constructs a new {@code Stakes} {@code Object}.
     *
     * @param player The player.
     */
    public Stakes(Player player) {
        this.stake = new Container(9, false);
        this.player = player;
    }

    /**
     * Stakes a new item.
     *
     * @param itemId The item id.
     * @param slot   The slot.
     * @param amount The amount.
     * @return {@code True} if the items could be staked, {@code false} if not.
     */
    public boolean stake(int itemId, int slot, int amount) {
        Item item = player.getInventory().getContainer().get(slot);
        if (item == null || item.getId() != itemId) {
            return false;
        }
        /*TODO: if(player.hasFakeInventory()) {
              return false;
          }*/
        int amt = player.getInventory().getContainer().getNumberOf(item);
        if (amount > amt) {
            amount = amt;
        }
        if (amount == 0) {
            return false;
        }
        if (stake.getTakenSlots() + amount > 9) {
            if (!item.getDefinition().isStackable()) {
                amount = 9 - stake.getTakenSlots();
            }
        }
        if (stake.getTakenSlots() == 9) {
            if ((item.getDefinition().isStackable() && !stake.contains(item)) && !item.getDefinition().isStackable()) {
                player.sendMessage("Your stake is full!");
                return false;
            }
        }
        item = new Item(itemId, amount);
        if (item.getDefinition().isStackable()) {
            if (player.getInventory().removeItems(item)) {
                stake.add(item);
            }
        } else {
            item = new Item(itemId, 1);
            for (int i = 0; i < amount; i++) {
                if (player.getInventory().removeItems(item)) {
                    stake.add(item);
                }
            }
            player.getInventory().refresh();
        }
        refresh();
        Player other = ((DuelActivity) player.getActivity()).getOpponent(player);
        ((Stakes) other.getAttribute("duelStakes")).refresh();
        return true;
    }

    /**
     * Refreshes the stake.
     *
     * @return {@code True}.
     */
    public boolean refresh() {
        Player other = ((DuelActivity) player.getActivity()).getOpponent(player);
        ActionSender.sendItems(player, 134, stake, false);
        ActionSender.sendItems(player, 134, ((Stakes) other.getAttribute("duelStakes")).getContainer(), true);
        ActionSender.sendItems(player, 93, player.getInventory().getContainer(), false);
        return true;
    }

    /**
     * Removes a staked item.
     *
     * @param item   The item.
     * @param amount The amount.
     * @return {@code True} if the item could be removed, {@code false} if not.
     */
    public boolean remove(int item, int amount) {
        int amt = stake.getNumberOf(new Item(item));
        if (amount > amt) {
            amount = amt;
        }
        if (amount < 1) {
            return false;
        }
        Item i = new Item(item, amount);
        stake.remove(i);
        player.getInventory().addItem(i);
        Container reArranged = new Container(Inventory.SIZE, false);
        reArranged.addAll(stake);
        stake = reArranged;
        refresh();
        Player other = ((DuelActivity) player.getActivity()).getOpponent(player);
        ((Stakes) other.getAttribute("duelStakes")).refresh();
        return true;
    }

    public Container getContainer() {
        return stake;
    }
}