package org.dementhium.content.skills.cooking;

import org.dementhium.action.ProduceAction;
import org.dementhium.model.Item;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;

/**
 * @author Steve <golden_32@live.com>
 */
public class ItemCombining extends ProduceAction {

    public enum ItemCombination {
        BREAD_DOUGH(1, 15, new Item[]{new Item(1)}, new Item(2307));

        private ItemCombination(int level, int exp, Item[] ingredients, Item producedItem) {

        }
    }

    public ItemCombining(int cycles, int productionAmount) {
        super(cycles, productionAmount);
        // TODO Auto-generated constructor stub
    }

    @Override
    public int getAnimationDelay() {
        return 0;
    }

    @Override
    public Item getFailItem() {
        return null;
    }

    @Override
    public Item produceItem() {
        return null;
    }

    @Override
    public Item[] getRequiredItems() {
        return null;
    }

    @Override
    public int getRequiredLevel() {
        return 0;
    }

    @Override
    public int getSkill() {
        return 0;
    }

    @Override
    public double getExperience() {
        return 0;
    }

    @Override
    public String getMessage(int type) {
        return null;
    }

    @Override
    public Animation getAnimation() {
        return null;
    }

    @Override
    public Graphic getGraphic() {
        return null;
    }

    @Override
    public boolean isSuccessfull() {
        return true;
    }

}
