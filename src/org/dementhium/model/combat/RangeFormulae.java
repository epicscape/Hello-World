package org.dementhium.model.combat;

import org.dementhium.model.Mob;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.model.player.Bonuses;
import org.dementhium.model.player.Skills;

/**
 * Holds the range combat related formulae.
 *
 * @author Emperor
 */
public class RangeFormulae {

    /**
     * Gets the current damage to be dealt to the victim.
     *
     * @param source The attacking mob.
     * @param victim The entity being attacked.
     * @return The amount to hit.
     */
    public static final int getDamage(Mob source, Mob victim) {
        return getDamage(source, victim, 1.0, 1.0, 1.0);
    }

    /**
     * Gets the current range damage.
     *
     * @param source             The attacking mob.
     * @param victim             The mob being attacked.
     * @param accuracyMultiplier The amount to increase the accuracy with.
     * @param hitMultiplier      The amount to increase the hit with.
     * @param defenceMultiplier  The amount to increase the defence with.
     * @return The amount to hit.
     */
    public static int getDamage(Mob source, Mob victim, double accuracyMultiplier,
                                double hitMultiplier, double defenceMultiplier) {
        double accuracy = CombatExecutor.getGaussian(0.5, source.getRandom(), getAccuracy(source, accuracyMultiplier));
        double defence = CombatExecutor.getGaussian(0.5, victim.getRandom(), getDefence(source, victim, defenceMultiplier));
        double mod = accuracy / (accuracy + defence);
                if (accuracy > defence) {
            return (int) CombatExecutor.getGaussian(mod, source.getRandom(), getRangeDamage(source, hitMultiplier));
        }
        return 0;
    }

    /**
     * Gets the current range damage.
     *
     * @param source             The attacking mob.
     * @param victim             The mob being attacked.
     * @param accuracyMultiplier The amount to increase the accuracy with.
     * @param damage             The amount of damage.
     * @param defenceMultiplier  The amount to increase the defence with.
     * @return The amount to hit.
     */
    public static int getDamage(Mob source, Mob victim, double accuracyMultiplier,
                                int damage, double defenceMultiplier) {
        double accuracy = CombatExecutor.getGaussian(0.5, source.getRandom(), getAccuracy(source, accuracyMultiplier));
        double defence = CombatExecutor.getGaussian(0.5, victim.getRandom(), getDefence(source, victim, defenceMultiplier));
        double mod = accuracy / (accuracy + defence);
                if (accuracy > defence) {
            return (int) CombatExecutor.getGaussian(mod, source.getRandom(), damage);
        }
        return 0;
    }

    /**
     * Gets the maximum range damage.
     *
     * @param source        The attacking mob.
     * @param hitMultiplier The hit multiplier.
     * @return The maximum range damage.
     */
    public static int getRangeDamage(Mob source, double hitMultiplier) {
        int style = 0;
        if (source.isPlayer() && source.getPlayer().getSettings().getCombatStyle() == WeaponInterface.STYLE_ACCURATE) {
            style = 3;
        }
        int strLvl = source.isPlayer() ? source.getPlayer().getSkills().getLevel(Skills.RANGE) : source.getNPC().getDefinition().getRangeLevel();
        int strBonus = source.isPlayer() ? source.getPlayer().getBonuses().getBonus(Bonuses.RANGED) : source.getNPC().getDefinition().getBonuses()[12];
        double strMult = 1.0;
        strMult += source.isPlayer() ? source.getPlayer().getPrayer().getRangeModifier() : source.getNPC().getRangeModifier();
        if (source.isPlayer() && source.getPlayer().getEquipment().voidSet(2)) {
            hitMultiplier += 0.2;
        }
        double cumulativeStr = strLvl * strMult + style;
        return (int) ((14 + cumulativeStr + (strBonus / 8) + ((cumulativeStr * strBonus) / 64)) * hitMultiplier);
    }

    /**
     * Gets the maximum range accuracy.
     *
     * @param source             The attacking mob.
     * @param accuracyMultiplier The accuracy multiplier.
     * @return The maximum range accuracy.
     */
    public static double getAccuracy(Mob source, double accuracyMultiplier) {
        int style = 0;
        if (source.isPlayer() && source.getPlayer().getSettings().getCombatStyle() == WeaponInterface.STYLE_ACCURATE) {
            style = 3;
        }
        int attLvl = source.isPlayer() ? source.getPlayer().getSkills().getLevel(Skills.RANGE) : source.getNPC().getDefinition().getRangeLevel();
        int attBonus = source.isPlayer() ? source.getPlayer().getBonuses().getBonus(Bonuses.RANGED_ATTACK) : source.getNPC().getDefinition().getBonuses()[4];
        double attMult = 1.0;
        attMult += source.isPlayer() ? source.getPlayer().getPrayer().getRangeModifier() : source.getNPC().getRangeModifier();
        if (source.isPlayer() && source.getPlayer().getEquipment().voidSet(2)) {
            accuracyMultiplier += 0.1;
        }
        double cumulativeAtt = attLvl * attMult + style;
        return ((14 + cumulativeAtt + (attBonus / 8) + ((cumulativeAtt * attBonus) / 64)) * 1.2) * accuracyMultiplier;
    }

    /**
     * Gets the maximum range defence.
     *
     * @param source            The attacking mob.
     * @param victim            The mob being attacked.
     * @param defenceMultiplier The defence multiplier.
     * @return The maximum range defence.
     */
    public static double getDefence(Mob source, Mob victim, double defenceMultiplier) {
        int style = 0;
        if (victim.isPlayer()) {
            if (victim.getPlayer().getSettings().getCombatStyle() == WeaponInterface.STYLE_DEFENSIVE) {
                style = 3;
            } else if (victim.getPlayer().getSettings().getCombatStyle() == WeaponInterface.STYLE_CONTROLLED) {
                style = 1;
            }
        }
        int type = Bonuses.RANGED_ATTACK;
        int defLvl = victim.isNPC() ? victim.getNPC().getDefinition().getDefenceLevel() : victim.getPlayer().getSkills().getLevel(Skills.DEFENCE);
        int defBonus = victim.isNPC() ? victim.getNPC().getDefinition().getDefenceBonus(type) : victim.getPlayer().getBonuses().getDefence(type);
        double defMult = 1.0;
        defMult += victim.isPlayer() ? victim.getPlayer().getPrayer().getDefenceModifier() : victim.getNPC().getDefenceModifier();
        double cumulativeDef = defLvl * defMult + style;
        return (14 + cumulativeDef + (defBonus / 8) + ((cumulativeDef * defBonus) / 64)) * defenceMultiplier;
    }
}