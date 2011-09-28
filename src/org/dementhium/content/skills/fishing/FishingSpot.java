package org.dementhium.content.skills.fishing;

import org.dementhium.model.mask.Animation;

import java.util.HashMap;
import java.util.Map;

public enum FishingSpot {

    NET_NET_AND_BAIT(1, 316, Animation.create(621), 303, -1, Fish.SHRIMP, Fish.ANCHOVIES),
    BAIT_NET_AND_BAIT(2, 316, Animation.create(622), 307, 313, Fish.SARDINE, Fish.HERRING),
    LURE_LURE_AND_BAIT(1, 317, Animation.create(622), 309, 314, Fish.TROUT, Fish.SALMON),
    BAIT_LURE_AND_BAIT(2, 317, Animation.create(622), 307, 313, Fish.PIKE, Fish.CAVE_FISH),
    CAGE_CAGE_AND_HARPOON(1, 321, Animation.create(619), 301, -1, Fish.LOBSTER),
    HARPOON_CAGE_AND_HARPOON(2, 321, Animation.create(618), 311, -1, Fish.TUNA, Fish.SWORDFISH, Fish.MONKFISH),
    BIG_NET_NET_AND_HARPOON(1, 322, Animation.create(621), 305, -1, Fish.MACKEREL, Fish.COD, Fish.BASS),
    HARPOON_NET_AND_HARPOON(2, 322, Animation.create(618), 311, -1, Fish.SHARK);

    private static final Map<Integer, FishingSpot> fishingSpot = new HashMap<Integer, FishingSpot>();

    static {
        for (FishingSpot fishSpot : FishingSpot.values()) {
            fishingSpot.put(fishSpot.getNpcId() | (fishSpot.getClick() << 24),
                    fishSpot);
        }
    }

    public static FishingSpot forId(int object) {
        return fishingSpot.get(object);
    }

    private final int npcId;
    private final int item;
    private final int bait;
    private final int click;
    private final Animation animation;
    private final Fish[] fish;

    private FishingSpot(int click, int npcId, Animation animation, int item,
                        int bait, Fish... fish) {
        this.npcId = npcId;
        this.item = item;
        this.bait = bait;
        this.fish = fish;
        this.animation = animation;
        this.click = click;
    }

    public int getNpcId() {
        return npcId;
    }

    public int getItem() {
        return item;
    }

    public int getBait() {
        return bait;
    }

    public Animation getAnimation() {
        return animation;
    }

    public int getClick() {
        return click;
    }

    public Fish[] getHarvest() {
        return fish;
    }

}