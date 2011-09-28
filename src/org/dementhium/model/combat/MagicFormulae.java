package org.dementhium.model.combat;

import org.dementhium.model.Mob;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.npc.impl.MetalDragon;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;

/**
 * Holds all the magic combat related formulae.
 *
 * @author Emperor
 */
public class MagicFormulae {

    /**
     * Gets the current magic damage.
     *
     * @param source             The attacking NPC.
     * @param victim             The entity being attacked.
     * @param accuracyMultiplier The amount to increase the accuracy with.
     * @param hitMultiplier      The amount to increase the hit with.
     * @param defenceMultiplier  The amount to increase the defence with.
     * @return The amount to hit.
     */
    public static int getDamage(NPC source, Mob victim, double accuracyMultiplier,
                                double hitMultiplier, double defenceMultiplier) {
        double accuracy = CombatExecutor.getGaussian(0.5, source.getRandom(), getMaximumMagicAccuracy(source, accuracyMultiplier));
        double defence = CombatExecutor.getGaussian(0.5, victim.getRandom(), getMaximumMagicDefence(victim, defenceMultiplier));
        double mod = accuracy / (accuracy + defence);
        if (accuracy > defence) {
            return (int) CombatExecutor.getGaussian(mod, source.getRandom(), getMaximumMagicDamage(source, hitMultiplier));
        }
        return -1;
    }

    /**
     * Gets the current magic damage.
     *
     * @param source             The attacking NPC.
     * @param victim             The entity being attacked.
     * @param accuracyMultiplier The amount to increase the accuracy with.
     * @param hitMultiplier      The amount to increase the hit with.
     * @param defenceMultiplier  The amount to increase the defence with.
     * @return The amount to hit.
     */
    public static int getDamage(NPC source, Mob victim, double accuracyMultiplier,
                                int damage, double defenceMultiplier) {
        double accuracy = CombatExecutor.getGaussian(0.5, source.getRandom(), getMaximumMagicAccuracy(source, accuracyMultiplier));
        double defence = CombatExecutor.getGaussian(0.5, victim.getRandom(), getMaximumMagicDefence(victim, defenceMultiplier));
        double mod = accuracy / (accuracy + defence);
        if (accuracy > defence) {
            return (int) CombatExecutor.getGaussian(mod, source.getRandom(), damage);
        }
        return -1;
    }

    /**
     * Gets the maximum magic accuracy.
     *
     * @param source             The attacking NPC.
     * @param accuracyMultiplier The accuracy multiplier.
     * @return The maximum magic accuracy.
     */
    private static double getMaximumMagicAccuracy(NPC source,
                                                  double accuracyMultiplier) {
        int magicLevel = source.getDefinition().getMagicLevel() + 1;
        int magicBonus = source.getDefinition().getBonuses()[4];
        double accuracy = ((magicLevel + (magicBonus * 2)) + 45) * accuracyMultiplier;
        return accuracy < 1 ? 1 : accuracy;
    }

    /**
     * Gets the maximum magic defence.
     *
     * @param source            The attacking NPC.
     * @param victim            The entity being attacked.
     * @param defenceMultiplier The defence multiplier.
     * @return The maximum magic defence.
     */
    private static double getMaximumMagicDefence(Mob victim, double defenceMultiplier) {
        int style = 0;
        if (victim.isPlayer()) {
            if (victim.getPlayer().getSettings().getCombatStyle() == WeaponInterface.STYLE_DEFENSIVE) {
                style = 3;
            } else if (victim.getPlayer().getSettings().getCombatStyle() == WeaponInterface.STYLE_CONTROLLED) {
                style = 1;
            }
        }
        double defLvl = (victim.isNPC() ? victim.getNPC().getDefinition().getDefenceLevel() : victim.getPlayer().getSkills().getLevel(1)) * 0.3;
        defLvl += (victim.isNPC() ? victim.getNPC().getDefinition().getMagicLevel() : victim.getPlayer().getSkills().getLevel(6)) * 0.7;
        int defBonus = victim.isNPC() ? victim.getNPC().getDefinition().getBonuses()[8] : victim.getPlayer().getBonuses().getBonus(8);
        double defMult = 1.0;
        defMult += victim.isPlayer() ? victim.getPlayer().getPrayer().getDefenceModifier() : victim.getNPC().getDefenceModifier();
        double defence = (((defLvl + (defBonus * 2)) + style) * defMult) * defenceMultiplier;
        return defence < 1 ? 1 : defence;
    }

    /**
     * Gets the maximum magic damage.
     *
     * @param source        The attacking NPC.
     * @param hitMultiplier The hit multiplier.
     * @return The maximum magic damage.
     */
    public static double getMaximumMagicDamage(NPC source, double hitMultiplier) {
        int mageLvl = source.getDefinition().getMagicLevel() + 1;
        int magicBonus = source.getDefinition().getBonuses()[13];
        return (14 + mageLvl + (magicBonus / 8) + ((mageLvl * magicBonus) / 64)) * hitMultiplier;
    }
	
	/**
	 * Sets the current damage on the interaction.
	 * @param interaction The interaction.
	 */
	public static void setDamage(Interaction interaction) {
		double accuracy = CombatExecutor.getGaussian(0.5, interaction.getSource().getRandom(), getMaximumAccuracy(interaction.getSource().getPlayer(), interaction.getSpell()) + 5);
		double defence = CombatExecutor.getGaussian(0.5, interaction.getVictim().getRandom(), getMaximumDefence(interaction.getSource().getPlayer(), interaction.getVictim(), interaction.getSpell()) + 5);
		double maximum = getMaximumDamage(interaction.getSource().getPlayer(), interaction.getVictim(), interaction.getSpell());
		double mod = accuracy / (accuracy + defence);
		if (accuracy > defence) {
			int hit = (int) CombatExecutor.getGaussian(mod, interaction.getSource().getRandom(), maximum);
			interaction.setDamage(Damage.getDamage(interaction.getSource(), interaction.getVictim(), CombatType.MAGIC, hit));
		} else {
			interaction.setDamage(new Damage(-1));
		}
		interaction.getDamage().setMaximum((int) maximum);
	}
	
	/**
	 * Gets the current magic damage.
	 * @param source The attacking player.
	 * @param victim The mob being attacked.
	 * @param spell The spell used.
	 * @return The current damage.
	 */
	public static int getDamage(Player source, Mob victim, MagicSpell spell) {
		double accuracy = CombatExecutor.getGaussian(0.5, source.getRandom(), getMaximumAccuracy(source, spell) + 5);
		double defence = CombatExecutor.getGaussian(0.5, victim.getRandom(), getMaximumDefence(source, victim, spell) + 5);
		double maximum = getMaximumDamage(source, victim, spell);
		double mod = accuracy / (accuracy + defence);
		if (accuracy > defence) {
			return (int) CombatExecutor.getGaussian(mod, source.getRandom(), maximum);
		}
		return -1;
	}

	/**
	 * Gets the maximum magic accuracy.
	 * @param source The attacking player.
	 * @param accuracyMultiplier The accuracy multiplier.
	 * @return The maximum magic accuracy.
	 */
	private static double getMaximumAccuracy(Player source, MagicSpell spell) {
		int magicLevel = source.getSkills().getLevel(Skills.MAGIC);
		int magicBonus = source.getBonuses().getBonus(3);
		double prayerBonus = 1 + source.getPrayer().getMagicModifier();
		double accuracy = ((magicLevel + (magicBonus * 4)) * 1.2) * prayerBonus;
		accuracy += (spell.getNormalDamage() + spell.getBaseDamage()) / 2;
		if (accuracy < 1) {
			accuracy = 1;
		}
		if (source.getEquipment().voidSet(3)) {
			return accuracy * 1.1;
		}
		return accuracy;
	}

	/**
	 * Gets the maximum magic defence.
	 * @param source The attacking player.
	 * @param spellType The spell type used.
	 * @param victim The entity being attacked.
	 * @param defenceMultiplier The defence multiplier.
	 * @return The maximum magic defence.
	 */
	private static double getMaximumDefence(Player source, Mob victim, MagicSpell spell) {
		int style = 0;
        if (victim.isPlayer()) {
            if (victim.getPlayer().getSettings().getCombatStyle() == WeaponInterface.STYLE_DEFENSIVE) {
                style = 3;
            } else if (victim.getPlayer().getSettings().getCombatStyle() == WeaponInterface.STYLE_CONTROLLED) {
                style = 1;
            }
        }
		double defLvl = (victim.isNPC() ? victim.getNPC().getDefinition().getDefenceLevel() : victim.getPlayer().getSkills().getLevel(1)) * 0.3;
		defLvl += (victim.isNPC() ? victim.getNPC().getDefinition().getMagicLevel() : victim.getPlayer().getSkills().getLevel(6)) * 0.7;
		int defBonus = victim.isNPC() ? victim.getNPC().getDefinition().getBonuses()[8] : victim.getPlayer().getBonuses().getBonus(8);
		double defMult = 1.0;
		defMult += victim.isPlayer() ? victim.getPlayer().getPrayer().getDefenceModifier() : victim.getNPC().getDefenceModifier();
        if (victim instanceof MetalDragon && spell.getClass().getSimpleName().contains("Fire")) {
        	defMult -= 0.5;
        }
		double defence = ((defLvl + (defBonus * 4)) + style) * defMult;
		return defence < 1 ? 1 : defence;
	}

	/**
	 * Gets the maximum magic damage.
	 * @param source The attacking player.
	 * @param victim The entity being attacked.
	 * @param spellType The spell type used.
	 * @param hitMultiplier The hit multiplier.
	 * @return The maximum magic damage.
	 */
	public static double getMaximumDamage(Player source, Mob victim, MagicSpell spell) {
		int damage = spell.getStartDamage(source, victim);
		double multiplier = 1;
		multiplier *= (source.getBonuses().getBonus(14) * 0.01) + 1;
		if (source.getSkills().getLevel(Skills.MAGIC) > source.getSkills().getLevelForExperience(Skills.MAGIC)) {
			multiplier *= 1 + ((source.getSkills().getLevel(Skills.MAGIC) - source.getSkills().getLevelForExperience(Skills.MAGIC)) * 0.03);
		}
		//TODO: Slayer helm/HexCreft helm multiplier on slayer tasks.
		if (victim.isNPC() && victim.getNPC().getId() == 9463) {
			boolean isFireSpell = spell.getClass().getSimpleName().contains("Fire");
			if (source.getEquipment().getSlot(Equipment.SLOT_CAPE) == 6570 && isFireSpell) {
				damage += 40;
				multiplier *= 2.0;
			} else if (isFireSpell) {
				multiplier *= 1.5;
			} else if (source.getEquipment().getSlot(Equipment.SLOT_CAPE) == 6570) {
				damage += 40;
			}
		}
		if (source.getEquipment().voidSet(3)) {
			multiplier += .1;
		}
		return damage * multiplier;
	}
	
	/**
	 * Gets the normal damage from the spell?
	 * @param source The attacking player.
	 * @param victim The entity being attacked.
	 * @param spellType The spell type used.
	 * @return The normal damage.
	 */
	/*private static int getNormalDamage(Player source, Entity victim, SpellType spellType) {
		int t = spellType.getBaseDamage();
		String name = spellType.name();
		if (name.endsWith("STRIKE")) {
			return victim instanceof Npc && ((Npc) victim).getId() == 205 ? (80 + t) : (2 * t);
		} else if (name.endsWith("BOLT")) {
			return source.getEquipment().getItem(Equipment.SLOT_HANDS).getId() == 777 ? 110 + t : 80 + t;
		} else if (name.endsWith("BLAST")) {
			return 120 + t;
		} else if (name.endsWith("WAVE")) {
			return 160 + t;
		} else if (name.endsWith("SURGE")) {
			return 200 + (2 * t);
		} else if (name.equals("CRUMBLE_UNDEAD")) {
			return 150;
		} else if (name.equals("MAGIC_DART")) {
			return 100 + source.getSkills().getLevel(Skills.MAGIC);
		} else if (name.equals("IBAN_BLAST")) {
			return 250;
		} else if (name.endsWith("GODSPELL")) {
			return source.getIntegerAttribute("godSpellCharge") > /*GameEngine.getTicks()*1 ? 300 : 200;
		/*} else if (name.endsWith("RUSH")) {
			return 140 + t;
		} else if (name.endsWith("BURST")) {
			return 180 + t;
		} else if (name.endsWith("BLITZ")) {
			return 220 + t;
		} else if (name.endsWith("BARRAGE")) {
			return 260 + t;
		}
		return t;
	}*/
}