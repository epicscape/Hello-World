package org.dementhium.content;

import org.dementhium.model.Item;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;

public class SkillCapes {

	private static int masterNpcId;

	public static int[][] skillCapeId = {
		{9747, 9748},
		{9753, 9754}, 
		{9750, 9751}, 
		{9768, 9769}, 
		{9756, 9757},
		{9759, 9760}, 
		{9762, 9763},
		{9801, 9802}, 
		{9807, 9808}, 
		{9783, 9784},
		{9798, 9799},
		{9804, 9805}, 
		{9780, 9781}, 
		{9795, 9796}, 
		{9792, 9793},
		{9774, 9775}, 
		{9771, 9772}, 
		{9777, 9778}, 
		{9786, 9787}, 
		{9810, 9811},
		{9765, 9766}, 
		{9948, 9949}, 
		{9789, 9790}, 
		{12169, 12170}, 
		{18508, 18509}};


	public static int getNumberOfSkillCapes(Player player) {
		int count = 0;
		for (int i = 0; i < 25; i++) {
			if (player.getSkills().getLevelForExperience(i) >= 99) {
				count++;
			}
		}
		return count;
	}

	private enum Masters {
		AJJAT(4288, Skills.ATTACK, new Item[] {new Item(9747), new Item(9748)}, new Item(9749)),
		SLOANE(4297, Skills.STRENGTH, new Item[] {new Item(9750), new Item(9751)}, new Item(9752)),
		MELEE_INSTRUCTOR(705, Skills.DEFENCE, new Item[] {new Item(9753), new Item(9754)}, new Item(9755)),
		ARMOUR_SALESMAN(682, Skills.RANGE, new Item[] {new Item(9756), new Item(9757)}, new Item(9758)),
		BROTHER_JERED(802, Skills.PRAYER, new Item[] {new Item(9759), new Item(9760)}, new Item(9761)),
		ROBE_STORE_OWNER(1658, Skills.MAGIC, new Item[] {new Item(9762), new Item(9763)}, new Item(9764)),
		SURGEON_GENERAL(961, Skills.HITPOINTS, new Item[]{new Item(9768), new Item(9769)}, new Item(9770)),
		CAPTION_IZZY(437, Skills.AGILITY, new Item[] {new Item(9771), new Item(9772)}, new Item(9773)),
		KAQEMEEX(455, Skills.HERBLORE, new Item[]{new Item(9774), new Item(9775)}, new Item(9776)),
		MARTIN_THWAIT(2270, Skills.THIEVING, new Item[] {new Item(9777), new Item(9778)}, new Item(9779)),
		MASTER_CRAFTER(805, Skills.CRAFTING, new Item[]{new Item(9780), new Item(9781)}, new Item(9782)),
		HICKTON(575, Skills.FLETCHING, new Item[]{new Item(9763), new Item(9764)}, new Item(9765)),
		DWARF(3295, Skills.MINING, new Item[]{new Item(9792), new Item(9793)}, new Item(9794)),
		THURGO(604, Skills.SMITHING, new Item[]{new Item(9795), new Item(9795)}, new Item(9796)),
		MASTER_FISHER(308, Skills.FISHING, new Item[]{new Item(9798), new Item(9799)}, new Item(9800)),
		HEAD_CHEF(847, Skills.COOKING, new Item[]{new Item(9801), new Item(9802)}, new Item(9803)),
		IGNATIUS_VULCAN(4946, Skills.FIREMAKING, new Item[]{new Item(9804), new Item(9805)}, new Item(9806)),
		WILFRED(4906, Skills.WOODCUTTING, new Item[]{new Item(9807), new Item(9808)}, new Item(9809)),
		MARTIN_MASTER_GARDENER(3299, Skills.FARMING, new Item[]{new Item(9789), new Item(9790)}, new Item(9791));

		/**
		 * The NPC ID of the master
		 */
		 private int npcId;

		/**
		 * The Skill associated with the master
		 */
		 private int skillId;
		/**
		 * The cape items associated with this master
		 */
		 private Item[] capes;
		 /**
		  * The hood item associated with this master
		  */
		 private Item hood;

		 private Masters(int npcId, int skillId, Item[] capes, Item hood){
			 this.npcId = npcId;
			 this.skillId = skillId;
			 this.capes = capes;
			 this.hood = hood;
		 }

		 public int getNpcId(){
			 return npcId;
		 }

		 public int getSkillId(){
			 return skillId;
		 }

		 public Item[] getCape(){
			 return capes;
		 }

		 public Item getHood(){
			 return hood;
		 }

		 public static Masters getMaster(int npcId){
			 for(Masters master : Masters.values()){
				 if(master.getNpcId() == npcId){
					 return master;
				 }
			 }
			 return null;
		 }
	}

	public static boolean handleDialogue(Player player, int stage) {
		Masters master = Masters.getMaster(masterNpcId);
		switch (stage) {
		case 579:
			if(player.getSkills().getLevel(master.skillId) < 98){
				DialogueManager.sendDialogue(player, DialogueManager.SAD, master.getNpcId(), -1, "You do not have 99 "+Skills.SKILL_NAME[master.getSkillId()]);
				return true;
			}else{
				DialogueManager.sendDialogue(player, DialogueManager.HAPPY_TALKING, master.getNpcId(), 580, "Most certainly, unfortunately being such a prestigious","Item, they are appropriately expensive. I'm afraid I","must ask you for the princely sum of 99000 gold!");
			}
			return true;
		case 580:
			DialogueManager.sendOptionDialogue(player, new int[] {581, 582}, "99000 Gold! Are you mad?", "I would gladly pay such a paltry sum for a splendid cape.");
			return true;
		case 581:
			DialogueManager.sendDialogue(player, DialogueManager.DEPRESSED, -1, -1, "99000 Gold! Are you mad? No thank you.");
			return true;
		case 582:
			DialogueManager.sendDialogue(player, DialogueManager.HAPPY_TALKING, -1, 583, "I would gladly pay such a paltry sum for a splendid","cape.");
			return true;
		case 583:
			if (player.getInventory().contains(995, 99000)) {
				if(player.getInventory().getContainer().getFreeSlots() < 2){
					DialogueManager.sendDialogue(player, DialogueManager.SAD, master.getNpcId(), -1, "You do not have enough inventory space.");
					return true;
				}
				player.getInventory().deleteItem(995, 99000);
				if(getNumberOfSkillCapes(player) > 1){
					player.getInventory().addDropable(new Item(master.getCape()[1]));
					player.getInventory().addDropable(new Item(master.getHood()));
				}else{
					player.getInventory().addDropable(new Item(master.getCape()[0]));
					player.getInventory().addDropable(new Item(master.getHood()));
				}
				DialogueManager.sendDialogue(player, DialogueManager.HAPPY_TALKING, master.getNpcId(), -1, "Excellent! Wear that cape with pride my friend.");
			} else {
				DialogueManager.sendDialogue(player, DialogueManager.SAD, master.getNpcId(), -1, "Sorry but these capes are 99,000 coins a piece.");
			}
			return true;
		}
		return false; // then option is 12 and I don't think they send anything if you click option 2
	}

	public static boolean handleFirstOption(Player player, int npcId){
		Masters master = Masters.getMaster(npcId);
		if(master == null){
			return false;
		}
		masterNpcId = npcId;
		DialogueManager.sendDialogue(player, DialogueManager.HAPPY_TALKING, -1, 579, "May I buy a Skillcape of "+Skills.SKILL_NAME[master.skillId]);
		return true;
	}
}