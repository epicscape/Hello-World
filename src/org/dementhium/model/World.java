package org.dementhium.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.dementhium.DementhiumShutdownHook;
import org.dementhium.RS2ServerBootstrap;
import org.dementhium.content.activity.ActivityManager;
import org.dementhium.content.activity.impl.CastleWarsActivity;
import org.dementhium.content.activity.impl.GodwarsActivity;
import org.dementhium.content.activity.impl.ImpetuousImpulses;
import org.dementhium.content.areas.AreaManager;
import org.dementhium.content.areas.CoordinateEvent;
import org.dementhium.content.clans.ClanManager;
import org.dementhium.content.dialogue.DialogueManager;
import org.dementhium.content.misc.PunishHandler;
import org.dementhium.content.skills.agility.Agility;
import org.dementhium.content.skills.crafting.LeatherCrafting;
import org.dementhium.identifiers.IdentifierManager;
import org.dementhium.model.combat.SpellContainer;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.definition.NPCDefinition;
import org.dementhium.model.definition.PlayerDefinition;
import org.dementhium.model.map.ObjectManager;
import org.dementhium.model.map.Position;
import org.dementhium.model.map.path.PathFinder;
import org.dementhium.model.map.path.PathState;
import org.dementhium.model.map.region.RegionBuilder;
import org.dementhium.model.misc.GroundItemManager;
import org.dementhium.model.npc.NPC;
import org.dementhium.model.npc.NPCDropLoader;
import org.dementhium.model.npc.NPCLoader;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.ShopManager;
import org.dementhium.mysql.impl.Highscores;
import org.dementhium.mysql.impl.PlayerCount;
import org.dementhium.mysql.impl.PlayerLoader;
import org.dementhium.mysql.impl.PlayerLoader.PlayerLoadResult;
import org.dementhium.net.GameSession;
import org.dementhium.net.PacketManager;
import org.dementhium.net.message.MessageBuilder;
import org.dementhium.net.packethandlers.WalkingHandler;
import org.dementhium.task.SequentialTaskExecutor;
import org.dementhium.task.Task;
import org.dementhium.task.impl.SessionLoginTask;
import org.dementhium.task.impl.SessionLogoutTask;
import org.dementhium.tickable.Tick;
import org.dementhium.tickable.impl.GroundItemUpdateTick;
import org.dementhium.tickable.impl.WGuildTick;
import org.dementhium.util.Constants;
import org.dementhium.util.DementhiumThreadFactory;
import org.dementhium.util.EntityList;
import org.dementhium.util.Misc;
import org.jboss.netty.channel.ChannelFutureListener;

/**
 * @author Emperor
 * @author Lumby
 * @author Steve
 * @author 'Mystic Flow
 * @author Khaled
 * @author `Discardedx2
 */
public final class World implements Runnable {

	private static final World INSTANCE = new World();

	public static World getWorld() {
		return INSTANCE;
	}

	/**
	 * The amount of ticks passed, used for checking how long ago a player did a certain execution.
	 * Eg. for food, burrying bones, ...
	 */
	private static int ticksPassed;

	private final ScheduledExecutorService gameService = Executors.newScheduledThreadPool(1, new DementhiumThreadFactory("GameLogic", Thread.MAX_PRIORITY));

	private EntityList<Player> players = new EntityList<Player>(Constants.MAX_AMT_OF_PLAYERS);
	private EntityList<Player> lobbyPlayers = new EntityList<Player>(Constants.MAX_AMT_OF_PLAYERS);
	private EntityList<NPC> npcs = new EntityList<NPC>(Constants.MAX_AMT_OF_NPCS);
	private ShopManager shopManager = new ShopManager();
	private NPCDropLoader npcDropLoader = new NPCDropLoader();
	private LinkedList<Tick> ticksToAdd = new LinkedList<Tick>();
	private LinkedList<Tick> ticks = new LinkedList<Tick>();

	private PacketManager packetManager = new PacketManager();
	private PlayerLoader playerLoader = new PlayerLoader();
	private Highscores hiscores = new Highscores();

	private SequentialTaskExecutor sequentialExecutor = new SequentialTaskExecutor();

	private ClanManager clanManager;
	private AreaManager areaManager;

	private final ExecutorService backgroundLoader = Executors.newFixedThreadPool(1, new DementhiumThreadFactory("BackgroundLoader"));

	private PunishHandler punishHandler = new PunishHandler();


	public static boolean print;

	private World() {

	}

	public void load() throws Exception {
		System.out.println("Loading World...");
		clanManager = new ClanManager();
		areaManager = new AreaManager();
		npcDropLoader.load();
		GroundItemManager.load();
		SpellContainer.initialize();
		SpecialAttackContainer.initialize();
		DialogueManager.init();
		shopManager.load();
		punishHandler.load();
		ObjectManager.init();
		ItemDefinition.init();
		NPCDefinition.init();
		NPCLoader.load();
		Agility.init();
		LeatherCrafting.init();
		RegionBuilder.init();
		IdentifierManager.registerIdentifiers();
		registerEvents();
		registerGlobalActivities();
		gameService.scheduleAtFixedRate(this, 600, 600, TimeUnit.MILLISECONDS);
		if (Misc.isVPS()) {
			gameService.scheduleAtFixedRate(new PlayerCount(), 10, 10, TimeUnit.SECONDS);
		}
	}

	@Override
	public void run() {
		try {
			long start = System.currentTimeMillis();
			if (ticksToAdd.size() > 0) {
				ticks.addAll(ticksToAdd);
				ticksToAdd = new LinkedList<Tick>();
			}
			for (Iterator<Tick> it = ticks.iterator(); it.hasNext(); ) {
				if (!it.next().run()) {
					it.remove();
				}
			}
			List<Task> tickTasks = new ArrayList<Task>();
			List<Task> updateTasks = new ArrayList<Task>();
			List<Task> resetTasks = new ArrayList<Task>();
			Iterator<Player> worldPlayers = players.iterator();
			while (worldPlayers.hasNext()) {
				Player player = worldPlayers.next();
				if (player.isOnline() && !player.destroyed() && !player.getConnection().isDisconnected()) {
					tickTasks.add(player.tickTask);
					updateTasks.add(player.updateTask);
					resetTasks.add(player.resetTask);
				} else {
					submitTask(new SessionLogoutTask(player));
				}
			}
			Iterator<Player> lobbyIterator = lobbyPlayers.iterator();
			while (lobbyIterator.hasNext()) {
				Player player = lobbyIterator.next();
				if (player.getConnection().isInLobby() && !player.getConnection().isDisconnected()) {
					tickTasks.add(player.tickTask);
				} else {
					submitTask(new SessionLogoutTask(player));
				}
			}
			for (NPC npc : npcs) {
				tickTasks.add(npc.getNPCTasks()[0]);
				resetTasks.add(npc.getNPCTasks()[1]);
			}
			sequentialExecutor.performTasks(tickTasks);
			sequentialExecutor.performTasks(updateTasks);
			sequentialExecutor.performTasks(resetTasks);
			long elapsed = System.currentTimeMillis() - start;
			if (print) {
				System.out.println("Benchmark " + elapsed);
			}
			ticksPassed++;
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void registerEvents() {
		submit(GroundItemUpdateTick.getSingleton());
		submit(new WGuildTick(5));
	}

	/**
	 * Registers the global activities.
	 */
	private void registerGlobalActivities() {
		/*
		 * Please add new activities after castle wars, it has to be the first one.
		 */
		ActivityManager.getSingleton().register(CastleWarsActivity.getSingleton());
		ActivityManager.getSingleton().register(ImpetuousImpulses.getSingleton());
		ActivityManager.getSingleton().register(GodwarsActivity.getSingleton());
	}

	public void load(final GameSession connection, final PlayerDefinition definition) {
		if (connection != null && definition != null) {
			backgroundLoader.submit(new Runnable() {
				public void run() {
					PlayerLoadResult result = playerLoader.load(connection, definition);
					int code = result.getReturnCode();
					if (code != 2) {
						connection.write(new MessageBuilder().writeByte(code).toMessage()).addListener(ChannelFutureListener.CLOSE);
					} else {
						connection.setPlayer(result.getPlayer());
						if (RS2ServerBootstrap.sqlDisabled || playerLoader.load(result.getPlayer())) {
							submitTask(new SessionLoginTask(result.getPlayer()));
						} else {
							connection.write(new MessageBuilder().writeByte(Constants.ERROR_LOADING_PROFILE).toMessage()).addListener(ChannelFutureListener.CLOSE);
						}
					}
				}
			});
		}
	}

	public void register(final Player player) {
		int code = 2;
		if (player.getConnection().isInLobby()) {
			if (!lobbyPlayers.add(player)) {
				code = 7;
			}
		} else {
			if (!players.add(player)) {
				code = 7;
			}
		}
		player.getConnection().write(new MessageBuilder().writeByte(code).toMessage());
		if (code == 2) {
			player.loadPlayer();
		}
	}

	public NPC register(int npcId, Location spawnLoc) {
		NPC npc = NPCLoader.getNPC(npcId);
		npc.setDoesWalk(true);
		npc.setLocation(spawnLoc);
		npc.setOriginalLocation(spawnLoc);
		npc.loadEntityVariables();
		npcs.add(npc);
		return npc;
	}

	public void unregister(final Player player) {
		if (DementhiumShutdownHook.getSingleton().activated) {
			//			TODO try {
			//				ChannelBuffer saveBuffer = ChannelBuffers.dynamicBuffer();
			//				player.save(saveBuffer);
			//				FileUtilities.writeBufferToFile("./data/backup games/" + player.getUsername() + PlayerLoader.EXTENSION, saveBuffer.toByteBuffer());
			//			} catch (Throwable e) {
			//				e.printStackTrace();
			//			}
			return;
		}
		/*if (player.getFamiliar() != null) {
			player.getFamiliar().dismiss();
		}*/
		if (player.getTradeSession() != null) {
			player.getTradeSession().tradeFailed();
		}
		if (player.getPriceCheck().isOpen()) {
			player.getPriceCheck().close();
		}
		clanManager.leaveClan(player, true);
		player.setOnline(false);
		if (player.getConnection().isInLobby()) {
			lobbyPlayers.remove(player);
		} else {
			players.remove(player);
		}
		backgroundLoader.submit(new Runnable() {
			@Override
			public void run() {
				playerLoader.save(player);
				if (!player.getConnection().isInLobby()) {
					hiscores.saveHighscore(player);
				}
			}
		});
		String name = Misc.formatPlayerNameForDisplay(player.getUsername());
		for (Player p : World.getWorld().getPlayers()) {
			if (p.getFriendManager().getFriends().contains(name)) {
				p.getFriendManager().updateFriend(name);
			}
		}
	}

	public boolean isOnList(String name) {
		name = Misc.formatPlayerNameForProtocol(name);
		for (Player p : players) {
			if (Misc.formatPlayerNameForProtocol(p.getUsername()).equals(name)) {
				return true;
			}
		}
		for (Player p : lobbyPlayers) {
			if (Misc.formatPlayerNameForProtocol(p.getUsername()).equals(name)) {
				return true;
			}
		}
		return false;
	}

	public Player getPlayerInServer(String name) {
		name = Misc.formatPlayerNameForProtocol(name);
		for (Player p : players) {
			if (Misc.formatPlayerNameForProtocol(p.getUsername()).equals(name)) {
				return p;
			}
		}
		for (Player p : lobbyPlayers) {
			if (Misc.formatPlayerNameForProtocol(p.getUsername()).equals(name)) {
				return p;
			}
		}
		return null;
	}

	public Player getPlayerOutOfLobby(String name) {
		name = Misc.formatPlayerNameForProtocol(name);
		for (Player pl : lobbyPlayers) {
			if (pl.getUsername().equalsIgnoreCase(name)) {
				return pl;
			}
		}
		return null;
	}

	public void submitAreaEvent(final Mob mob, final CoordinateEvent coordinateEvent) {
		mob.submitTick("area_event", new Tick(1) {

			private int attempts;

			@Override
			public void execute() {
				if (++attempts >= 20) {
					stop();
					return;
				}
				if ((coordinateEvent.inArea() || !mob.getWalkingQueue().isMoving()) && mob.getAttribute("freezeTime", 0) < getTicks()) {
					stop();
					mob.getWalkingQueue().reset();
					coordinateEvent.execute();
				}
			}
		});
	}

	public void submit(Tick tick) {
		ticksToAdd.add(tick);
	}

	public void submitTask(final Task task) {
		gameService.submit(new Runnable() {
			@Override
			public void run() {
				try {
					task.execute();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public ScheduledExecutorService getService() {
		return gameService;
	}

	public PathState doPath(PathFinder pathFinder, Mob mob, int x, int y) {
		return doPath(pathFinder, mob, x, y, false, true, true);
	}

	public PathState doPath(final PathFinder pathFinder, final Mob mob, final int x, final int y, final boolean ignoreLastStep, boolean addToWalking) {
		return doPath(pathFinder, mob, x, y, ignoreLastStep, addToWalking, true);
	}

	public PathState doPath(final PathFinder pathFinder, final Mob mob, final int x, final int y, final boolean ignoreLastStep, boolean addToWalking, boolean nullOnFail) {
		Location destination = Location.locate(x, y, mob.getLocation().getZ());
		Location base = mob.getLocation();
		int srcX = mob.getViewportX();
		int srcY = mob.getViewportY();
		int destX = destination.getViewportX(base, mob.getViewportDepth());
		int destY = destination.getViewportY(base, mob.getViewportDepth());
		PathState state = pathFinder.findPath(mob, mob.getLocation(), srcX, srcY, destX, destY, mob.getLocation().getZ(), 0, mob.getWalkingQueue().isRunning(), ignoreLastStep, nullOnFail);
		if (state != null && addToWalking) {
			//TODO: Check if this works as planned.
			if (!WalkingHandler.canMove(mob)) {
				return state;
			}
			mob.getWalkingQueue().reset();
			for (Position step : state.getPoints()) {
				mob.addPoint(step.getX(), step.getY());
			}
		}
		return state;
	}

	public void doPath(Mob mob, PathState state) {
		if (state != null) {
			//TODO: Check if this works as planned.
			if (!WalkingHandler.canMove(mob)) {
				return;
			}
			mob.getWalkingQueue().reset();
			for (Position step : state.getPoints()) {
				mob.addPoint(step.getX(), step.getY());
			}
		}
	}

	public EntityList<NPC> getNpcs() {
		return npcs;
	}

	public EntityList<Player> getPlayers() {
		return players;
	}

	public PacketManager getPacketManager() {
		return packetManager;
	}

	public PlayerLoader getPlayerLoader() {
		return playerLoader;
	}

	public ClanManager getClanManager() {
		return clanManager;
	}

	public EntityList<Player> getLobbyPlayers() {
		return lobbyPlayers;
	}

	public AreaManager getAreaManager() {
		return areaManager;
	}

	public ShopManager getShopManager() {
		return shopManager;
	}


	public NPCDropLoader getNpcDropLoader() {
		return npcDropLoader;
	}

	public ExecutorService getBackgroundLoader() {
		return backgroundLoader;
	}

	/**
	 * @return the ticksPassed
	 */
	public static int getTicks() {
		return ticksPassed;
	}

	public PunishHandler getPunishHandler() {
		return punishHandler;

	}

}
