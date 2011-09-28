package org.dementhium.model.combat;

import org.dementhium.model.Mob;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.model.player.Bonuses;
import org.dementhium.model.player.Skills;

/**
 * Holds all the melee-related formulae.
 *
 * @author Emperor
 */
public class MeleeFormulae {

    /**
     * Gets the current damage to be dealt to the victim.
     *
     * @param source The attacking mob.
     * @param victim The mob being attacked.
     * @return The amount to hit.
     */
    public static final int getDamage(Mob source, Mob victim) {
        return getDamage(source, victim, 1.0, 1.0, 1.0);
    }

    /**
     * Gets the current melee damage.
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
        double accuracy = CombatExecutor.getGaussian(0.5, source.getRandom(), getMeleeAccuracy(source, accuracyMultiplier));
        double defence = CombatExecutor.getGaussian(0.5, victim.getRandom(), getMeleeDefence(source, victim, defenceMultiplier));
        double mod = accuracy / (accuracy + defence);
        if (accuracy > defence) {
            return (int) CombatExecutor.getGaussian(mod, source.getRandom(), getMeleeDamage(source, hitMultiplier));
        }
        return 0;
    }

    /**
     * Gets the current melee damage.
     *
     * @param source             The attacking player.
     * @param victim             The entity being attacked.
     * @param accuracyMultiplier The amount to increase the accuracy with.
     * @param damage             The amount of maximum damage.
     * @param defenceMultiplier  The amount to increase the defence with.
     * @return The amount to hit.
     */
    public static int getDamage(Mob source, Mob victim, double accuracyMultiplier,
                                int damage, double defenceMultiplier) {
        double accuracy = CombatExecutor.getGaussian(0.5, source.getRandom(), getMeleeAccuracy(source, accuracyMultiplier));
        double defence = CombatExecutor.getGaussian(0.5, victim.getRandom(), getMeleeDefence(source, victim, defenceMultiplier));
        double mod = accuracy / (accuracy + defence);
                if (accuracy > defence) {
            return (int) CombatExecutor.getGaussian(mod, source.getRandom(), damage);
        }
        return 0;
    }

    /**
     * Gets the maximum melee damage.
     *
     * @param source        The attacking mob.
     * @param hitMultiplier The hit multiplier.
     * @return The maximum melee damage.
     */
    public static int getMeleeDamage(Mob source, double hitMultiplier) {
        int style = 0;
        if (source.isPlayer()) {
            if (source.getPlayer().getSettings().getCombatStyle() == WeaponInterface.STYLE_AGGRESSIVE) {
                style = 3;
            } else if (source.getPlayer().getSettings().getCombatStyle() == WeaponInterface.STYLE_CONTROLLED) {
                style = 1;
            }
        }
        int strLvl = source.isPlayer() ? source.getPlayer().getSkills().getLevel(Skills.STRENGTH) : source.getNPC().getDefinition().getStrengthLevel();
        int strBonus = source.isPlayer() ? source.getPlayer().getBonuses().getBonus(Bonuses.STRENGTH) : source.getNPC().getDefinition().getBonuses()[11];
        double strMult = 1.0;
        int dhp = 0;
        double dharokMod = 1.0;
        strMult += source.isPlayer() ? source.getPlayer().getPrayer().getStrengthModifier() : source.getNPC().getStrengthModifier();
        if (source.isPlayer() && source.getPlayer().getEquipment().barrowsSet(2)) {
            dhp = source.getPlayer().getSkills().getMaxHitpoints() - source.getPlayer().getSkills().getHitPoints();
            dharokMod = (dhp * 0.001) + 1;
        }
        if (source.isPlayer() && source.getPlayer().getEquipment().voidSet(1)) {
            hitMultiplier += 0.1;
        }
        double cumulativeStr = (strLvl * strMult + style) * dharokMod;
        return (int) ((14 + cumulativeStr + (strBonus / 8) + ((cumulativeStr * strBonus) / 64)) * hitMultiplier);
    }

    /**
     * Gets the maximum melee accuracy.
     *
     * @param source             The attacking mob.
     * @param accuracyMultiplier The accuracy multiplier.
     * @return The maximum melee accuracy.
     */
    public static double getMeleeAccuracy(Mob source, double accuracyMultiplier) {
        int style = 0;
        if (source.isPlayer()) {
            if (source.getPlayer().getSettings().getCombatStyle() == WeaponInterface.STYLE_ACCURATE) {
                style = 3;
            } else if (source.getPlayer().getSettings().getCombatStyle() == WeaponInterface.STYLE_CONTROLLED) {
                style = 1;
            }
        }
        int type = getBonusType(source);
        int attLvl = source.isPlayer() ? source.getPlayer().getSkills().getLevel(Skills.ATTACK) : source.getNPC().getDefinition().getAttackLevel();
        int attBonus = source.isPlayer() ? source.getPlayer().getBonuses().getBonus(type) : source.getNPC().getDefinition().getBonuses()[type];
        double attMult = 1.0;
        attMult += source.isPlayer() ? source.getPlayer().getPrayer().getAttackModifier() : source.getNPC().getAttackModifier();
        if (source.isPlayer() && source.getPlayer().getEquipment().voidSet(1)) {
            accuracyMultiplier += 0.15;
        }
        double cumulativeAtt = attLvl * attMult + style;
        return ((14 + cumulativeAtt + (attBonus / 8) + ((cumulativeAtt * attBonus) / 64)) * 1.2) * accuracyMultiplier;
    }

    /**
     * Gets the maximum melee defence.
     *
     * @param source            The attacking mob.
     * @param victim            The mob being attacked.
     * @param defenceMultiplier The defence multiplier.
     * @return The maximum melee defence.
     */
    public static double getMeleeDefence(Mob source, Mob victim, double defenceMultiplier) {
        int style = 0;
        if (victim.isPlayer()) {
            if (victim.getPlayer().getSettings().getCombatStyle() == WeaponInterface.STYLE_DEFENSIVE) {
                style = 3;
            } else if (victim.getPlayer().getSettings().getCombatStyle() == WeaponInterface.STYLE_CONTROLLED) {
                style = 1;
            }
        }
        int type = getBonusType(source);
        int defLvl = victim.isNPC() ? victim.getNPC().getDefinition().getDefenceLevel() : victim.getPlayer().getSkills().getLevel(Skills.DEFENCE);
        int defBonus = victim.isNPC() ? victim.getNPC().getDefinition().getDefenceBonus(type) : victim.getPlayer().getBonuses().getDefence(type);
        double defMult = 1.0;
        defMult += victim.isPlayer() ? victim.getPlayer().getPrayer().getDefenceModifier() : victim.getNPC().getDefenceModifier();
        double cumulativeDef = defLvl * defMult + style;
        return (14 + cumulativeDef + (defBonus / 8) + ((cumulativeDef * defBonus) / 64)) * defenceMultiplier;
    }
    
    /**
     * Gets the bonus type used.
     *
     * @param source The attacking mob.
     * @return The bonus type.
     */
    public static int getBonusType(Mob source) {
        if (source.isPlayer()) {
        	return source.getPlayer().getSettings().getCombatType();
        }
        int type = 0;
        int bonus = 0;
        for (int i = 0; i < 3; i++) {
            if (source.getNPC().getDefinition().getBonuses()[i] > bonus) {
                bonus = source.getNPC().getDefinition().getBonuses()[i];
                type = i;
            }
        }
        return type;
    }
}