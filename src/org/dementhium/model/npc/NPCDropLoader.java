package org.dementhium.model.npc;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 * @author Steve
 */
public class NPCDropLoader {

    RandomAccessFile dropFile;

    private Map<Integer, ArrayList<Drop>> dropMap = new LinkedHashMap<Integer, ArrayList<Drop>>();

    public NPCDropLoader() {
        try {
            dropFile = new RandomAccessFile("data/npcs/drops.bin", "r");
        } catch (FileNotFoundException e) {
            System.out.println("No drop file!");
        }
    }

    public void load() {
        System.out.println("Loading drops...");
        try {
            FileChannel channel = dropFile.getChannel();
            if (channel.size() > 0) {
                ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
                ArrayList<Drop> drops = null;
                int dropSize = buffer.getShort();
                for (int i = 0; i < dropSize; i++) {
                    int npcId = buffer.getShort();
                    short dropAmt = buffer.getShort();
                    drops = new ArrayList<Drop>(dropAmt);
                    for (int x = 0; x < dropAmt; x++) {
                        if (buffer.get() == 0)
                            drops.add(new Drop(buffer.getShort(), buffer.getDouble(), buffer.getInt(), buffer.getInt(), false));
                        else
                            drops.add(new Drop((short) 0, 0, 0, 0, true));
                    }
                    dropMap.put(npcId, drops);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        System.out.println("Loaded " + dropMap.size() + " drops");
    }

    public Map<Integer, ArrayList<Drop>> getDropMap() {
        return dropMap;
    }

    public static class Drop {

        public static Drop create(int itemId, double rate, int minAmount, int maxAmount, boolean rare) {
            return new Drop((short) itemId, rate, minAmount, maxAmount, rare);
        }

        private short itemId;
        private double rate;
        private int minAmount;
        private int maxAmount;
        private boolean rareTable;

        public Drop(short itemId, double rate, int minAmount, int maxAmount, boolean rare) {
            this.itemId = itemId;
            this.rate = rate;
            this.minAmount = minAmount;
            this.maxAmount = maxAmount;
            this.rareTable = rare;
        }

        public short getItemId() {
            return itemId;
        }

        public double getRate() {
            return rate;
        }

        public void setItemId(short itemId) {
            this.itemId = itemId;
        }

        public void setRate(double rate) {
            this.rate = rate;
        }

        public int getMinAmount() {
            return minAmount;
        }

        public int getMaxAmount() {
            return maxAmount;
        }

        public void setMinAmount(int amount) {
            this.minAmount = amount;
        }

        public void setMaxAmount(int amount) {
            this.maxAmount = amount;
        }

        public boolean isFromRareTable() {
            return rareTable;
        }
    }
}
