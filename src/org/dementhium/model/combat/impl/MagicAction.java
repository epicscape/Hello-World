package org.dementhium.model.combat.impl;

import java.util.ArrayList;
import java.util.List;

import org.dementhium.content.misc.MagicStaff;
import org.dementhium.model.Item;
import org.dementhium.model.Mob;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.MagicSpell;
import org.dementhium.model.combat.SpellContainer;
import org.dementhium.model.combat.impl.spells.NPCSpell;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;

/**
 * Represents a magic combat action cycle.
 * @author Emperor
 *
 */
public class MagicAction extends CombatAction {

	/**
	 * Constructs a new {@code MagicAction} {@code Object}.
	 */
	public MagicAction() {
		super(false);
	}

	@Override
	public boolean commenceSession() {
		interaction.getSource().getCombatExecutor().setTicks(getCooldownTicks());
		MagicSpell spell = NPCSpell.getSingleton();
		if (interaction.getSource().isPlayer()) {
			boolean autocast = false;
			int spellId = interaction.getSource().getAttribute("spellId", -1);
			if (spellId == -1) {
				spellId = interaction.getSource().getAttribute("autocastId", -1);
				autocast = true;
			}
			spell = SpellContainer.grabSpell(interaction.getSource().getPlayer(), spellId);
			if (!autocast) {
				interaction.getSource().removeAttribute("spellId");
				interaction.getSource().getCombatExecutor().reset();
			}
			if (spell == null) {
				return false;
			}
			if (!checkRunes(interaction.getSource(), spell.getRequiredRunes(), true)) {
				interaction.getSource().getPlayer().sendMessage("You do not have the runes required to cast this spell.");
				interaction.getSource().getCombatExecutor().reset();
				return false;
			}
		}
		if (interaction.getVictim().isPlayer()) {
			interaction.setDeflected(interaction.getVictim().getPlayer().getPrayer().usingPrayer(1, 7));
		}
		interaction.setSpell(spell);
		interaction.setTicks((int) Math.floor(interaction.getSource().getLocation().distance(interaction.getVictim().getLocation()) * 0.5) 
				+ spell.getExtraDelay());
		if (!spell.castSpell(interaction)) {
			return false;
		}
		if (interaction.getSource().isPlayer()) {
			if (interaction.getDamage() != null) {
				interaction.getSource().getPlayer().getSkills().addExperience(Skills.HITPOINTS, interaction.getDamage().getHit() * 0.133);
			}
			if (interaction.getSource().getAttribute("defensiveCast", false)) {
				double experience = spell.getExperience(interaction);
				interaction.getSource().getPlayer().getSkills().addExperience(Skills.MAGIC, experience / 2);
				interaction.getSource().getPlayer().getSkills().addExperience(Skills.DEFENCE, experience / 2);
			} else {
				interaction.getSource().getPlayer().getSkills().addExperience(Skills.MAGIC, spell.getExperience(interaction));
			}
		}
		return true;
	}

	/**
	 * Checks if the player has the runes required.
	 * @param mob The mob.
	 * @param runes The runes.
	 * @param remove If the runes should be removed.
	 * @return {@code True} if the player had all the runes, {@code false} if not.
	 */
	public static boolean checkRunes(Mob mob, Item[] runes, boolean remove) {
		if (mob.isNPC() || runes == null) {
			return true;
		}
		List<Item> toRemove = new ArrayList<Item>();
		for (Item item : runes) {
			if (!hasStaff(mob.getPlayer(), item.getId())) {
				if (!mob.getPlayer().getInventory().contains(item)) {
					return false;
				}
				toRemove.add(item);
			}
		}
		if (remove) {
			Item weapon = mob.getPlayer().getEquipment().get(3);
			if (weapon != null && weapon.getDefinition().getName().startsWith("Staff of light")) {
				if (mob.getRandom().nextInt(8) == 0) {
					mob.getPlayer().sendMessage("Your spell draws its power completely from your staff.");
					return true;
				}
			}
			for (Item item : toRemove) {
				mob.getPlayer().getInventory().deleteItem(item.getId(), item.getAmount());
			}
		}
		return true;
	}
	
	/**
	 * Checks if the player is wearing the correct staff.
	 * @param player The player.
	 * @param rune The rune item id.
	 * @return {@code True} if so, {@code false} if not.
	 */
	private static boolean hasStaff(Player player, int rune) {
		Item weapon = player.getEquipment().get(3);
		if (weapon == null) {
			return false;
		}
		MagicStaff staff = MagicStaff.forId(rune);
		if (staff == null) {
			return false;
		}
		int[] staves = staff.getStaves();
		for (int id : staves) {
			if (weapon.getId() == id) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean executeSession() {
		interaction.getSpell().tick(interaction);
		interaction.setTicks(interaction.getTicks() - 1);
		return interaction.getTicks() < 1;
	}

	@Override
	public boolean endSession() {
		return interaction.getSpell().endSpell(interaction);
	}

	@Override
	public CombatType getCombatType() {
		return CombatType.MAGIC;
	}

	@Override
	public int getCooldownTicks() {
		return 5;
	}

}
