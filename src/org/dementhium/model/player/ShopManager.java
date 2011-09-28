package org.dementhium.model.player;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.HashMap;

public class ShopManager {

    public HashMap<Integer, Shop> shops = new HashMap<Integer, Shop>();

    public void load() {
        try {
            RandomAccessFile shopFile = new RandomAccessFile("data/shops.bin", "r");
            int shopsAmt = shopFile.readShort();
            for (int shopId = 0; shopId < shopsAmt; shopId++) {
                int npcId = shopFile.readShort();
                int[] items = new int[shopFile.readByte()];
                int[] amounts = new int[items.length];
                boolean isGeneral = shopFile.read() == 1;
                for (int itemData = 0; itemData < items.length; itemData++) {
                    items[itemData] = shopFile.readShort();
                    amounts[itemData] = shopFile.readInt();
                }
                shops.put(npcId, new Shop(npcId, isGeneral, items, amounts, true));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded " + shops.size() + " Shops");
    }

    public boolean openShop(Player player, int id) {
        if (shops.get(id) != null) {
            player.setAttribute("shopId", id);
            shops.get(id).open(player);
            shops.get(id).addPlayer(player);
            return true;
        } else {
            return false;
        }
    }

    public Shop getShop(int id) {
        return shops.get(id);
    }


}