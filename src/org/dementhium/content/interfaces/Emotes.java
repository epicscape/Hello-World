package org.dementhium.content.interfaces;

import org.dementhium.model.Item;
import org.dementhium.model.World;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.tickable.Tick;

/**
 * @author Lumby <lumbyjr@hotmail.com>
 */
public class Emotes {

	//12418 - curtsey
	private enum Emote{
		YES(2, null, Animation.create(0x357), null, 3),
		NO(3, null, Animation.create(0x358), null, 2),
		BOW(4, null,Animation.create(0x35A), null, 2),
		ANGRY(5, null, Animation.create(0x35B), null, 3),
		THINK(6, null,  Animation.create(0x359), null, 3),
		WAVE(7, null, Animation.create(0x35F), null, 3),
		SHRUG(8, null, Animation.create(0x841), null, 2),
		CHEER(9, null, Animation.create(0x35E), null, 3),
		BECKON(10, null, Animation.create(0x360), null, 3),
		LAUGH(11, null, Animation.create(0x83D), null, 3),
		JUMP_FOR_JOY(12, null, Animation.create(0x35D), null, 1),
		YAWN(13, null, Animation.create(0x83F), null, 4),
		DANCE(14, null, Animation.create(0x362), null, 7),
		JIG(15, null, Animation.create(0x83A), null, 6),
		TWIRL(16, null, Animation.create(0x83B), null, 1),
		HEADBANG(17, null, Animation.create(0x83C), null, 5),
		CRY(18, null, Animation.create(0x35C), null, 3),
		BLOW_KISS(19, null, Animation.create(0x55E), Graphic.create(1702), 3),
		PANIC(20, null, Animation.create(0x839), null, 2),
		RASPBERRY(21, null, Animation.create(0x83E), null, 3),
		CLAP(22, null,  Animation.create(0x361), null, 3),
		SALUTE(23, null, Animation.create(0x840), null, 1),
		GOBLIN_BOW(24, null, Animation.create(0x84F), null, 2),
		GOBLIN_SALUTE(25, null, Animation.create(0x850), null, 3),
		GLASS_BOX(26, null,  Animation.create(0x46B), null, 3),
		CLIMB_ROPE(27, null, Animation.create(0x46A), null, 5),
		LEAN(28, null, Animation.create(0x469), null, 5),
		GLASS_WALL(29, null, Animation.create(0x468), null, 6),
		IDEA(30, null, Animation.create(0x10B3), Graphic.create(712), 3),
		STOMP(31, null, Animation.create(4278), Graphic.create(713), 1),
		FLAP(32, null, Animation.create(0x10B8), null, 2),
		SLAP_HEAD(33, null, Animation.create(0x10B4), null, 1),
		ZOMBIE_WALK(34, null, Animation.create(0xDD8), null, 8),
		ZOMBIE_DANCE(35, null, Animation.create(0xDD7), null, 6),
		ZOMBIE_HAND(36, null, Animation.create(0x1C68), Graphic.create(1244), 3),
		SCARED(37, null, Animation.create(0xB14), null, 4),
		BUNNY_HOP(38, null, Animation.create(0x17DF), null, 5),
		SNOWMAN_DANCE(40, null,Animation.create(0x1D6B), null, 4),
		AIR_GUITAR(41, null,  Animation.create(0x96E), Graphic.create(1537), 6),
		SAFETY_FIRST(42, null, Animation.create(0x2242), Graphic.create(1553), 4),
		EXPLORE(43, null, Animation.create(0x2706), Graphic.create(1734), 7),
		TRICK(44, null, Animation.create(0x2922), Graphic.create(1864), 5),
		FREEZE(45, null, Animation.create(0x2B24), Graphic.create(1973), 6),
		TURKEY(46, null, null, null, 0),
		AROUND_THE_WORLD(47, null, Animation.create(0x2D16),Graphic.create(2037), 6),
		DRAMATIC_POINT(48, null, Animation.create(0x3172), Graphic.create(780), 4),
		FAINT(49, null, Animation.create(14165), null, 7),
		PUPPET_MASTER(50, null, Animation.create(0x3A15), Graphic.create(2837), 6),
		TASKMASTER(51, null, Animation.create(0x3ABA), Graphic.create(2930), 15),
		SEAL(52, null, null, null, 0),

		ATTACK(39, new Item[] {new Item(9747), new Item(9748)}, Animation.create(4959), Graphic.create(823), 5),
		STRENGTH(39, new Item[] {new Item(9750), new Item(9751)},  Animation.create(4981), Graphic.create(828), 16),
		DEFENCE(39, new Item[] {new Item(9753), new Item(9754)},  Animation.create(4961), Graphic.create(824), 6),
		CONSTITUTION(39, new Item[] {new Item(9768), new Item(9769)},  Animation.create(14242), Graphic.create(2745), 7),
		RANGED(39, new Item[] {new Item(9756), new Item(9757)},  Animation.create(4973), Graphic.create(832), 8),
		MAGIC(39, new Item[] {new Item(9762), new Item(9763)},  Animation.create(4939), Graphic.create(813), 5),
		PRAYER(39, new Item[] {new Item(9759), new Item(9760)},  Animation.create(4979), Graphic.create(829), 10),
		COOKING(39, new Item[] {new Item(9801), new Item(9802)},  Animation.create(4955), Graphic.create(821), 23),
		WOODCUTTING(39, new Item[] {new Item(9807), new Item(9808)},  Animation.create(4957), Graphic.create(822), 19),
		FLETCHING(39, new Item[] {new Item(9783), new Item(9784)},  Animation.create(4937), Graphic.create(812), 12),
		FISHING(39, new Item[] {new Item(9798), new Item(9799)},  Animation.create(4951), Graphic.create(819), 11),
		FIREMAKING(39, new Item[] {new Item(9804), new Item(9805)},  Animation.create(4975), Graphic.create(831), 8),
		CRAFTING(39, new Item[] {new Item(9780), new Item(9781)},  Animation.create(4949), Graphic.create(818), 13),
		SMITHING(39, new Item[] {new Item(9795), new Item(9796)},  Animation.create(4943), Graphic.create(815), 17),
		MINING(39, new Item[] {new Item(9792), new Item(9793)},  Animation.create(4941), Graphic.create(814), 6),
		HERBLORE(39, new Item[] {new Item(9774), new Item(9775)},  Animation.create(4969), Graphic.create(835), 14),
		AGILITY(39, new Item[] {new Item(9771), new Item(9772)},  Animation.create(4977), Graphic.create(830), 7),
		THIEVING(39, new Item[] {new Item(9777), new Item(9778)},  Animation.create(4965), Graphic.create(826), 13),
		SLAYER(39, new Item[] {new Item(9786), new Item(9787)},  Animation.create(4967), Graphic.create(1656), 5),
		FARMING(39, new Item[] {new Item(9810), new Item(9811)},  Animation.create(4963), Graphic.create(825), 11),
		RUNECRAFTING(39, new Item[] {new Item(9765), new Item(9766)},  Animation.create(4947), Graphic.create(817), 10),
		CONSTRUCTION(39, new Item[] {new Item(9789), new Item(9790)},  Animation.create(4953), Graphic.create(820), 11),
		SUMMONING(39, new Item[] {new Item(12169), new Item(12170)},  Animation.create(8525), Graphic.create(1515), 8),
		HUNTER(39, new Item[] {new Item(9948), new Item(9949)},  Animation.create(5158), Graphic.create(907), 10),
		QUEST(39, new Item[] {new Item(9813), new Item(10662)},  Animation.create(4945), Graphic.create(816), 13);
		//Dungeoneering is handled below

		/**
		 * The button Id for the emote
		 */
		private int buttonId;
		/**
		 * The item if the emote requires one
		 */
		private Item[] item;
		/**
		 * The animation of the emote
		 */
		private Animation animation;
		/**
		 * The graphic of the emote
		 */
		private Graphic graphic;
		/**
		 * The amount of ticks it takes to complete the emote
		 */
		private int tick;

		private Emote(int buttonId, Item[] item, Animation animation, Graphic graphic, int tick){
			this.buttonId = buttonId;
			this.item = item;
			this.animation = animation;
			this.graphic = graphic;
			this.tick = tick;
		}

		public int getButtonId(){
			return buttonId;
		}

		public Item[] getItem(){
			return item;
		}

		public Animation getAnimation(){
			return animation;
		}

		public Graphic getGraphic(){
			return graphic;
		}

		public int getTick(){
			return tick;
		}

		public static Emote getEmote(int buttonId){
			for(Emote emote : Emote.values()){
				if(emote.getButtonId() == buttonId){
					return emote;
				}
			}
			return null;
		}

		public static Emote getEmoteBySkillCape(int itemId){
			for(Emote emote : Emote.values()){
				try{
					if(emote.getButtonId() == 39 && emote.getItem()[0].getId() == itemId || emote.getButtonId() == 39 && emote.getItem()[1].getId() == itemId){
						return emote;
					}
				}catch(NullPointerException e){
					return null;
				}
			}
			return null;
		}
	}

	public static void handleButton(final Player player, final int buttonId, int buttonId2, int buttonId3) {
		if (buttonId < 0 || buttonId > 52) {
			return;
		}
		Emote emote = Emote.getEmote(buttonId);
		if(emote == null){
			return;
		}
		if(player.getCombatExecutor().getLastAttacker() != null){
    		player.sendMessage("You can not perform an emote while in combat.");
    		return;
    	}
		if(player.getAttribute("doingEmote") != null){
			player.sendMessage("You are already performing an emote!");
			return;
		}
		if(player.getSettings().isResting()){
			player.sendMessage("You cannot perform an emote while resting!");
			return;
		}
		if (buttonId == 46) {
			player.setAttribute("doingEmote", Boolean.TRUE);
			player.animate(Animation.create(10994));
			player.graphics(Graphic.create(86));
			player.setAttribute("cantMove", Boolean.TRUE);
			World.getWorld().submit(new Tick(1) {

				public void execute() {
					player.animate(Animation.create(10996));
					player.getAppearance().setNpcType((short) 8499);
					player.getMask().setApperanceUpdate(true);
					this.stop();
				}
			});
			World.getWorld().submit(new Tick(8) {

				public void execute() {
					player.animate(Animation.create(10995));
					player.graphics(Graphic.create(86));
					player.getAppearance().setNpcType((short) -1);
					player.getAppearance().resetAppearence();
					player.getMask().setApperanceUpdate(true);
					this.stop();
				}
			});
			World.getWorld().submit(new Tick(10) {

				public void execute() {
					player.removeAttribute("cantMove");
					player.removeAttribute("doingEmote");
					this.stop();
				}
			});
		} else if(buttonId == 52){
			player.setAttribute("doingEmote", Boolean.TRUE);
			player.animate(Animation.create(15104));
			player.graphics(Graphic.create(1287));
			player.setAttribute("cantMove", Boolean.TRUE);
			final int rand = (int) (Math.random() * (2 + 1));
			World.getWorld().submit(new Tick(2) {

				public void execute() {
					player.animate(Animation.create(15106));
					player.getAppearance().setNpcType((short) (rand == 0 ? 13255 : (rand == 1 ? 13256 : 13257)));
					player.getMask().setApperanceUpdate(true);
					this.stop();
				}
			});
			World.getWorld().submit(new Tick(4) {

				public void execute() {
					player.animate(Animation.create(15108));
					this.stop();
				}
			});
			World.getWorld().submit(new Tick(7) {

				public void execute() {
					player.animate(Animation.create(15105));
					player.graphics(Graphic.create(1287));
					player.getAppearance().setNpcType((short) -1);
					player.getAppearance().resetAppearence();
					player.getMask().setApperanceUpdate(true);
					this.stop();
				}
			});
			World.getWorld().submit(new Tick(9) {

				public void execute() {
					player.removeAttribute("cantMove");
					player.removeAttribute("doingEmote");
					this.stop();
				}
			});
		} else if(buttonId == 39){
			if(player.getEquipment().getContainer().get(Equipment.SLOT_CAPE) == null){
				player.sendMessage("You are not wearing a skillcape.");
				return;
			}
			if (player.getEquipment().getSlot(1) == 15706 || player.getEquipment().getSlot(1) == 18508 || player.getEquipment().getSlot(1) == 18509 || player.getEquipment().getSlot(1) == 19709 || player.getEquipment().getSlot(1) == 19710) {
				player.animate(Animation.create(13190));
				player.graphics(Graphic.create(2442));
				player.setAttribute("doingEmote", Boolean.TRUE);
				player.setAttribute("cantMove", Boolean.TRUE);
				final int rand = (int) (Math.random() * (2 + 1));
				World.getWorld().submit(new Tick(1) {

					public void execute() {
						player.getAppearance().setNpcType((short) (rand == 0 ? 11227 : (rand == 1 ? 11228 : 11229)));
						player.getMask().setApperanceUpdate(true);
						player.animate(Animation.create(rand == 0 ? 13192 : (rand == 1 ? 13193 : 13194)));
						this.stop();
					}
				});
				World.getWorld().submit(new Tick(6) {

					public void execute() {
						player.getAppearance().setNpcType((short) -1);
						player.removeAttribute("cantMove");
						player.removeAttribute("doingEmote");
						player.getMask().setApperanceUpdate(true);
						this.stop();
					}
				});

			}else{
				Emote capeEmote = Emote.getEmoteBySkillCape(player.getEquipment().getContainer().get(Equipment.SLOT_CAPE).getId());
				if(capeEmote == null){
					player.sendMessage("You are not wearing a skillcape.");
					return;
				}
				player.setAttribute("cantMove", Boolean.TRUE);
				player.setAttribute("doingEmote", Boolean.TRUE);
				player.animate(capeEmote.getAnimation());
				player.graphics(capeEmote.getGraphic());
				World.getWorld().submit(new Tick(capeEmote.getTick()) {
					public void execute() {
						player.removeAttribute("cantMove");
						player.removeAttribute("doingEmote");
						this.stop();
					}
				});
			}
		} else {
			if(buttonId == 52){
			
			}
			player.setAttribute("cantMove", Boolean.TRUE);
			player.setAttribute("doingEmote", Boolean.TRUE);
			player.animate(emote.getAnimation());
			if(emote.getGraphic() != null)
				player.graphics(emote.getGraphic());
			World.getWorld().submit(new Tick(emote.getTick()) {
				public void execute() {
					player.removeAttribute("cantMove");
					player.removeAttribute("doingEmote");
					this.stop();
				}
			});

		}
	}

}
