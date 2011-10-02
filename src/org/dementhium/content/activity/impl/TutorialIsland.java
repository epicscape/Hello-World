package org.dementhium.content.activity.impl;

import org.dementhium.content.activity.Activity;
import org.dementhium.content.activity.Activity.SessionStates;
import org.dementhium.model.Mob;
import org.dementhium.model.World;
import org.dementhium.model.misc.IconManager;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.player.Player;
import org.dementhium.net.ActionSender;
import org.dementhium.tickable.Tick;
import org.dementhium.util.InterfaceSettings;

/**
 * Handles the godwars activity.
 * @author Emperor
 *
 */
public class TutorialIsland extends Activity<NPC> {
	
	private Player player;
	private int stage, lastStage;
	public boolean needStageUpdate;
	
	/**
	 * Constructs a new {@code TutorialIsland} {@code Object}.
	 */
	public TutorialIsland(Player player) {
		super();
		this.player = player;
	}
	
	@Override
	public boolean initializeActivity() {
		System.out.println("Init act");
		stage = 0;
		lastStage = -1;
		player.setAttribute("inTutorial", Boolean.TRUE);
		return true;
	}

	@Override
	public boolean commenceSession() {
		hideTabs();
		System.out.println("Showing screen");
		player.sendMessage("Welcome to the tutorial island.");
		ActionSender.sendOverlay(player, 371);
		ActionSender.sendInterfaceConfig(player, 371, 4, false);
		ActionSender.sendChatboxInterface(player, 372);
		IconManager.iconOnMob(player, World.getWorld().getNpcs().getById(945), 4, -1);
		lastStage = stage;
		stage++;
		needStageUpdate = true;
		updateSession();
		return true;
	}

	private void hideTabs() {
		for (int i=202; i<218; i++)
			ActionSender.sendCloseInterface(player, 548, i); //Close tab
	}

	@Override
	public boolean updateSession() {
		//System.out.println("Update act"+stage);
		if (!needStageUpdate)
			return false;
		if (stage == 1){
			ActionSender.sendBConfig(player, 168, 4);
			sendInstructionText("Getting started", "", "To start the tutorial use your left mouse button to click on the", "EpicScape Guide in this room. He is indicated by a flashing yellow", "arrow above his head. If you can't see him, use your keyboard's arrow", "keys to rotate the view.", "");
		} else if (stage == 2){
			sendInstructionText("Game Options", "", "", "Please click on the flashing tools icon found at the bottom-right of", "your screen. This will display your game options.", "", "");
			ActionSender.sendBConfig(player, 168, 4);
			ActionSender.sendConfig(player, 1021, 13);
			ActionSender.sendInterface(player, 1, 548, 214, 261);
			ActionSender.sendBConfig(player, 168, 4);
		}
		needStageUpdate = false;
		return false;
	}
	
	@Override
	public boolean canLogout(Player player, boolean logoutButton) {
		stop(true);
		return true;
	}
	
	@Override
	public boolean endSession() {
		System.out.println("Shutty");
		player.setAttribute("inTutorial", Boolean.FALSE);
		ActionSender.sendCloseChatBox(player);
		setActivityState(SessionStates.PAUSE_STATE);
		IconManager.removeIcon(player, World.getWorld().getNpcs().getById(945));
        World.getWorld().submit(new Tick(1) {
            @Override public void execute() {
                stop();
                TutorialIsland.this.stop(false); //Incase the endSession() method gets called in the SessionLogoutTask.
            }
        });
        return false;
	}

	private void sendInstructionText(String... instructions){
		for (int i=0; i<instructions.length; i++){
			ActionSender.sendString(player, 372, i, instructions[i]);
		}
	}

	public void nextStage() {
		lastStage = stage;
		stage++;
	}

}