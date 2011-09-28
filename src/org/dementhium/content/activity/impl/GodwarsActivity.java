package org.dementhium.content.activity.impl;

import org.dementhium.content.activity.Activity;
import org.dementhium.model.npc.NPC;

/**
 * Handles the godwars activity.
 * @author Emperor
 *
 */
public class GodwarsActivity extends Activity<NPC> {
	
	/**
	 * The singleton of this class.
	 */
	private static final GodwarsActivity SINGLETON = new GodwarsActivity();
	
	/**
	 * Constructs a new {@code GodwarsActivity} {@code Object}.
	 */
	private GodwarsActivity() {
		super();
	}
	
	@Override
	public boolean initializeActivity() {
		//Here we basically spawn all godwars dungeon NPCs, 
		//handing out appropriate attribute to classify them as saradomin, zamorak, bandos or armadyl followers.
		return true;
	}

	@Override
	public boolean commenceSession() {
		//Here we initialize extra aspects of the activity.
		return true;
	}

	@Override
	public boolean updateSession() {
		//Here, we basically handle NvN aggressiveness, player killcount interface updating, ...
		return false;
	}
	
	@Override
	public boolean endSession() {
        /*
         * This method should never get called, as this is a global activity.
         */
		throw new IllegalStateException("Godwars dungeon activity shut down.");
	}

	/**
	 * @return the singleton
	 */
	public static GodwarsActivity getSingleton() {
		return SINGLETON;
	}

}