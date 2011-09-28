package org.dementhium.content.misc;

import org.dementhium.model.Item;
import org.dementhium.model.player.Player;

import java.util.ArrayList;

/**
 * @author Lumby <lumbyjr@hotmail.com>
 */
public class RepairItem {

    private static ArrayList<Item> repairItems = new ArrayList<Item>();

    private static int totalCost = 0;

    public static ArrayList<Item> getRepairItems() {
        return repairItems;
    }

    public static int getCost() {
        return totalCost;
    }

    public static void checkPlayerForRepair(Player player) {
        for (int i = 4856; i < 5000; i++) {
            if (player.getInventory().getContainer().contains(new Item(i))
                    && !player.getInventory().getContainer().get(player.getInventory().getContainer()
                    .getThisItemSlot(new Item(i))).getDefinition().isNoted()) {
                getRepairItems().add(
                        new Item(i, player.getInventory().getContainer()
                                .getNumberOf(new Item(i))));
            }
        }
    }

    public static int getRepairCost(Player player) {
        if (repairItems == null) {
            return -1;
        }
        for (Item item : repairItems) {
            if (item.getId() < 4886 && item.getId() > 4879
                    || item.getId() < 4862 && item.getId() > 4855
                    || item.getId() > 4903 && item.getId() < 4910
                    || item.getId() > 4927 && item.getId() < 4934
                    || item.getId() > 4951 && item.getId() < 4958
                    || item.getId() > 4975 && item.getId() < 4982) {
                if (item.getAmount() > 1) {
                    totalCost += 60000 * item.getAmount();
                } else {
                    totalCost += 60000;// Helm repair
                }
            }
            if (item.getId() < 4898 && item.getId() > 4891//Dharok
                    || item.getId() < 4874 && item.getId() > 4867//Ahrims
                    || item.getId() > 4915 && item.getId() < 4922//Guthans
                    || item.getId() > 4939 && item.getId() < 4946//Karil
                    || item.getId() > 4963 && item.getId() < 4970//Torag
                    || item.getId() > 4987 && item.getId() < 4994//Verac
                    ) {
                if (item.getAmount() > 1) {
                    totalCost += 90000 * item.getAmount();
                } else {
                    totalCost += 90000;// Body repair
                }
            }
            if (item.getId() < 4904 && item.getId() > 4897//Dharok
                    || item.getId() < 4880 && item.getId() > 4873//Ahrims
                    || item.getId() > 4921 && item.getId() < 4928//Guthans
                    || item.getId() > 4945 && item.getId() < 4952//Karil
                    || item.getId() > 4969 && item.getId() < 4976//Torag
                    || item.getId() > 4993 && item.getId() < 5000//Verac
                    ) {
                if (item.getAmount() > 1) {
                    totalCost += 80000 * item.getAmount();
                } else {
                    totalCost += 80000;// Legs repair
                }
            }
            if (item.getId() < 4892 && item.getId() > 4885//Dharok
                    || item.getId() < 4868 && item.getId() > 4861//Ahrims
                    || item.getId() > 4909 && item.getId() < 4916//Guthans
                    || item.getId() > 4933 && item.getId() < 4940//Karil
                    || item.getId() > 4957 && item.getId() < 4964//Torag
                    || item.getId() > 4981 && item.getId() < 4988//Verac
                    ) {
                if (item.getAmount() > 1) {
                    totalCost += 100000 * item.getAmount();
                } else {
                    totalCost += 100000;// Weapon repair
                }
            }
        }
        return totalCost;
    }

    public static void repairItems(Player player) {
        if (player.getInventory().getContainer().getItemCount(995) < totalCost) {
            player.sendMessage("You don't have enough coins to repair these items.");
            return;
        }
        for (Item item : repairItems) {
            player.getInventory().getContainer().remove(item);
            if (item.getId() < 4886 && item.getId() > 4879) {
                player.getInventory().getContainer().add(new Item(4716, item.getAmount()));
            }
            if (item.getId() < 4862 && item.getId() > 4855) {
                player.getInventory().getContainer().add(new Item(4708, item.getAmount()));
            }
            if (item.getId() > 4903 && item.getId() < 4910) {
                player.getInventory().getContainer().add(new Item(4724, item.getAmount()));
            }
            if (item.getId() > 4927 && item.getId() < 4934) {
                player.getInventory().getContainer().add(new Item(4732, item.getAmount()));
            }
            if (item.getId() > 4951 && item.getId() < 4958) {
                player.getInventory().getContainer().add(new Item(4745, item.getAmount()));
            }
            if (item.getId() > 4975 && item.getId() < 4982) {
                player.getInventory().getContainer().add(new Item(4753, item.getAmount()));
            }
            if (item.getId() < 4898 && item.getId() > 4891) {
                player.getInventory().getContainer().add(new Item(4720, item.getAmount()));
            }
            if (item.getId() < 4874 && item.getId() > 4867) {
                player.getInventory().getContainer().add(new Item(4712, item.getAmount()));
            }
            if (item.getId() > 4915 && item.getId() < 4922) {
                player.getInventory().getContainer().add(new Item(4728, item.getAmount()));
            }
            if (item.getId() > 4939 && item.getId() < 4946) {
                player.getInventory().getContainer().add(new Item(4736, item.getAmount()));
            }
            if (item.getId() > 4963 && item.getId() < 4970) {
                player.getInventory().getContainer().add(new Item(4749, item.getAmount()));
            }
            if (item.getId() > 4987 && item.getId() < 4994) {
                player.getInventory().getContainer().add(new Item(4757, item.getAmount()));
            }
            if (item.getId() < 4904 && item.getId() > 4897) {
                player.getInventory().getContainer().add(new Item(4722, item.getAmount()));
            }
            if (item.getId() < 4880 && item.getId() > 4873) {
                player.getInventory().getContainer().add(new Item(4714, item.getAmount()));
            }
            if (item.getId() > 4921 && item.getId() < 4928) {
                player.getInventory().getContainer().add(new Item(4730, item.getAmount()));
            }
            if (item.getId() > 4945 && item.getId() < 4952) {
                player.getInventory().getContainer().add(new Item(4738, item.getAmount()));
            }
            if (item.getId() > 4969 && item.getId() < 4976) {
                player.getInventory().getContainer().add(new Item(4751, item.getAmount()));
            }
            if (item.getId() > 4993 && item.getId() < 5000) {
                player.getInventory().getContainer().add(new Item(4759, item.getAmount()));
            }
            if (item.getId() < 4892 && item.getId() > 4885) {
                player.getInventory().getContainer().add(new Item(4718, item.getAmount()));
            }
            if (item.getId() < 4868 && item.getId() > 4861) {
                player.getInventory().getContainer().add(new Item(4710, item.getAmount()));
            }
            if (item.getId() > 4909 && item.getId() < 4916) {
                player.getInventory().getContainer().add(new Item(4726, item.getAmount()));
            }
            if (item.getId() > 4933 && item.getId() < 4940) {
                player.getInventory().getContainer().add(new Item(4734, item.getAmount()));
            }
            if (item.getId() > 4957 && item.getId() < 4964) {
                player.getInventory().getContainer().add(new Item(4747, item.getAmount()));
            }
            if (item.getId() > 4981 && item.getId() < 4988) {
                player.getInventory().getContainer().add(new Item(4755, item.getAmount()));
            }

        }
        player.getInventory().getContainer().remove(new Item(995, totalCost));
        player.getInventory().refresh();
        resetRepair();
    }

    public static void resetRepair() {
        getRepairItems().clear();
        totalCost = 0;
    }

}
