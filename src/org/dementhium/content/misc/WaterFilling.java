package org.dementhium.content.misc;

import org.dementhium.content.skills.SkillAction;
import org.dementhium.model.Item;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.player.Player;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 * @author Lumby <lumbyjr@hotmail.com>
 * @author Emperor <black_dragon_686@hotmail.com>
 */
public class WaterFilling extends SkillAction {
	
	private int item;
	/*
	 * First value - Empty Item
	 * Second value - water filled item
	 */
	private static int waterItems[][] = {
		{229, 227},//Vial to Vial of water
		{1925, 1929},//Bucket to Bucket of water
		{1935, 1937},//Jug to Jug of Water
		{3734, 3735}//Vase to Vase of Water
		};
	
	
	private Animation FILLING_ANIMATION = Animation.create(832);
	
	private int amount;
	
	public int getItem(){
		return item;
	}

	public WaterFilling(Player player, int item) {
		super(player);
		this.item = item;
	}

	@Override
	public boolean commence(Player player) {
		if(item == -1){
			return false;
		}
		amount = player.getInventory().getContainer().getNumberOf(new Item(waterItems[getIndex(item)][0]));
		return true;
	}

	@Override
	public boolean execute(Player player) {
		if(!player.getInventory().getContainer().contains(new Item(waterItems[getIndex(item)][0]))){
			return false;
		}
		return true;
	}

	@Override
	public boolean finish(Player player) {
		if(!player.getInventory().getContainer().contains(new Item(waterItems[getIndex(item)][0]))){
			return false;
		}
		amount--;
		player.animate(FILLING_ANIMATION);
		player.getInventory().getContainer().remove(new Item(waterItems[getIndex(item)][0]));
		player.getInventory().getContainer().add(new Item(waterItems[getIndex(item)][1]));
		player.getInventory().refresh();
		return amount < 1;
	}

	
	public static boolean isWaterItem(int item){
		for(int i = 0; i < waterItems.length; i++){
			if(waterItems[i][0] == item){
				return true;
			}
		}
		return false;
	}
	
	private int getIndex(int item){
		for(int i = 0; i < waterItems.length; i++){
			if(waterItems[i][0] == item){
				return i;
			}
		}
		return -1;
	}
}
