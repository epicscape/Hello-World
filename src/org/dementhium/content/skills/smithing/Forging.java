package org.dementhium.content.skills.smithing;

import org.dementhium.action.ProduceAction;
import org.dementhium.content.DialogueManager;
import org.dementhium.content.skills.smithing.SmithingUtils.ForgingBar;
import org.dementhium.model.Item;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Skills;
import org.dementhium.util.Misc;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class Forging extends ProduceAction {

    public static final Animation FORGING_ANIMATION = Animation.create(898);

    private ForgingBar bar;
    private int index;

    public Forging(int ticks, int productionAmount, ForgingBar bar, int index) {
        super(ticks, productionAmount);
        this.bar = bar;
        this.index = index;
    }

    @Override
    public Animation getAnimation() {
        return FORGING_ANIMATION;
    }

    @Override
    public double getExperience() {
        int levelRequired = bar.getBaseLevel() + SmithingUtils.getLevelIncrement(bar, bar.getItems()[index]);
        int barAmount = SmithingUtils.getBarAmount(levelRequired, bar, bar.getItems()[index]);

        return bar.getExperience()[barAmount == 5 ? 3 : barAmount - 1];
    }

    @Override
    public Item getFailItem() {
        return null;
    }

    @Override
    public Graphic getGraphic() {
        return null;
    }

    @Override
    public String getMessage(int type) {
        switch (type) {
            case SUCCESSFULLY_PRODUCED:
                return "You make " + Misc.withPrefix(ItemDefinition.forId(bar.getItems()[index]).getName().toLowerCase()) + ".";
            case NOT_ENOUGH_ITEMS:
                String barName = bar.toString().toLowerCase();
                DialogueManager.sendDisplayBox(mob.getPlayer(), -1, "You begin Smithing " + Misc.withPrefix(ItemDefinition.forId(bar.getItems()[index]).getName().toLowerCase()) + ", but quickly discover that you do", "not have sufficient " + barName + " bars.");
                return null;
        }
        return "null";
    }

    @Override
    public Item[] getRequiredItems() {
        return new Item[]{new Item(bar.getBarId(), SmithingUtils.getBarAmount(bar.getBaseLevel() + SmithingUtils.getLevelIncrement(bar, bar.getItems()[index]), bar, bar.getItems()[index]))};
    }

    @Override
    public int getRequiredLevel() {
        return 0;
    }

    @Override
    public int getSkill() {
        return Skills.SMITHING;
    }

    @Override
    public boolean isSuccessfull() {
        return true;
    }

    @Override
    public Item produceItem() {
        return new Item(bar.getItems()[index], SmithingUtils.getItemAmount(bar.getItems()[index]));
    }

    @Override
    public int getAnimationDelay() {
        return 0;
    }

}
