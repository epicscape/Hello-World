package org.dementhium.model.player;

import java.nio.ByteBuffer;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dementhium.RS2ServerBootstrap;
import org.dementhium.UpdateHandler;
import org.dementhium.content.activity.impl.CastleWarsActivity;
import org.dementhium.content.cutscenes.impl.TutorialScene;
import org.dementhium.content.misc.PriceCheck;
import org.dementhium.content.misc.PunishHandler;
import org.dementhium.content.skills.Prayer;
import org.dementhium.content.skills.runecrafting.Talisman;
import org.dementhium.content.skills.slayer.Slayer;
import org.dementhium.content.skills.slayer.SlayerTask;
import org.dementhium.content.skills.slayer.SlayerTask.Master;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.Projectile;
import org.dementhium.model.SpecialAttack;
import org.dementhium.model.SpecialAttackContainer;
import org.dementhium.model.World;
import org.dementhium.model.combat.Ammunition;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.Interaction;
import org.dementhium.model.combat.RangeData;
import org.dementhium.model.combat.RangeFormulae;
import org.dementhium.model.combat.RangeWeapon;
import org.dementhium.model.combat.impl.SpecialAction;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.definition.PlayerDefinition;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.model.map.ObjectManager;
import org.dementhium.model.map.region.RegionBuilder;
import org.dementhium.model.mask.Animation;
import org.dementhium.model.mask.Appearance;
import org.dementhium.model.mask.ForceText;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.ProjectileManager;
import org.dementhium.model.misc.SkullManager;
import org.dementhium.model.npc.NpcUpdate;
import org.dementhium.net.ActionSender;
import org.dementhium.net.GameSession;
import org.dementhium.net.handler.DementhiumHandler;
import org.dementhium.net.message.Message;
import org.dementhium.task.Task;
import org.dementhium.task.impl.PlayerResetTask;
import org.dementhium.task.impl.PlayerTickTask;
import org.dementhium.task.impl.PlayerUpdateTask;
import org.dementhium.tickable.Tick;
import org.dementhium.tickable.impl.PlayerAreaTick;
import org.dementhium.tickable.impl.PlayerRestorationTick;
import org.dementhium.util.BufferUtils;
import org.dementhium.util.Constants;
import org.dementhium.util.InterfaceSettings;
import org.dementhium.util.Misc;

/**
 * @author 'Mystic Flow
 * @author `Discardedx2
 * @author Steve
 * @author Lumby
 */
public final class Player extends Mob {

	private final PlayerDefinition definition;

	private final Appearance appearance = new Appearance();
	private final FriendManager friendManager = new FriendManager(this);
	private final Inventory inventory = new Inventory(this);
	private final Equipment equipment = new Equipment(this);
	private final Skills skills = new Skills(this);
	private final Bank bank = new Bank(this);
	private final Bonuses bonuses = new Bonuses(this);
	private final PlayerUpdate gpi = new PlayerUpdate(this);
	private final NpcUpdate gni = new NpcUpdate(this);
	private final Settings settings = new Settings();
	private final Prayer prayer = new Prayer(this);
	private final RegionData region = new RegionData(this);
	private final PriceCheck priceCheck = new PriceCheck(this);
	private final ItemDefinition itemDef = new ItemDefinition(this);
	private final Notes notes = new Notes(this);
	private final Slayer slayer = new Slayer(this);
	private final PlayerAreaTick playerAreaTick = new PlayerAreaTick(this);

	private List<Integer> mapRegionIds;
	private boolean isAtDynamicRegion;

	/**
	 * Player Dueling
	 */
	public boolean isStaking;
	public boolean isFriendly;
	/**
	 * The skull manager used.
	 */
	private final SkullManager skullManager = new SkullManager(this);

	/**
	 * Loan Timer
	 */
	public int loanTimer = 0;
	
	/**
	 * The quest storage used.
	 */
	private final QuestStorage questStorage = new QuestStorage();

	private GameSession connection;
	private TradeSession currentTradeSession;
	private Player tradePartner;

	private DementhiumHandler handler;

	private boolean isOnline;
	private boolean starter;
	private boolean active;

	private int viewDistance = 0;

	public Task tickTask = new PlayerTickTask(this),
			updateTask = new PlayerUpdateTask(this),
			resetTask = new PlayerResetTask(this);

	private String lastConnectIp = "null";

	private int viewportDepth;
	private int renderAnimation = -1;

	private long doubleXPTimer;

	private long lastPing = System.currentTimeMillis();

	private static int[] emptyLot = RegionBuilder.findEmptyMap(40, 40); // 16x16
	private final static Location houseLocation = Location.locate(emptyLot[0],
			emptyLot[1], 0);
	
	private int tutorialStage = 0;

	public Location getHouseLocation() {
		return houseLocation;
	}

	public void setHouseLocation() {

	}

	public Player(GameSession connection, PlayerDefinition definition) {
		super();
		this.definition = definition;
		this.connection = connection;
	}
	
	public void loadPlayer() {
		handler = null;
		if (!connection.isInLobby()) {
			setOnline(true);
			loadEntityVariables();
			ActionSender.loginResponse(this);
			World.getWorld().submit(playerAreaTick);
			World.getWorld().submit(new PlayerRestorationTick(this));
			initPackets();
			if (getUsername().equalsIgnoreCase("emperor")) {
				getAppearance().setNpcType(1684);
				getMask().setApperanceUpdate(true);
			}
		} else {
			ActionSender.sendLobbyResponse(this);
		}
		if (World.getWorld().getAreaManager().getAreaByName("Nex")
				.contains(getLocation())) {
			teleport(DEFAULT);
		}
		String name = Misc.formatPlayerNameForDisplay(getUsername());
		for (Player player : World.getWorld().getPlayers()) {
			if (player.getFriendManager().getFriends().contains(name)) {
				player.getFriendManager().updateFriend(name, this);
			}
		}
		for (Player player : World.getWorld().getLobbyPlayers()) {
			if (player.getFriendManager().getFriends().contains(name)) {
				player.getFriendManager().updateFriend(name, this);
			}
		}
		if (getAttribute("clanToJoin") != null) {
			World.getWorld().getClanManager()
					.joinClan(this, (String) getAttribute("clanToJoin"));
			removeAttribute("clanToJoin");
		}
		if (getEquipment().getSlot(Equipment.SLOT_SHIELD) == 8856
				&& !getAttribute("disabledTabs", false)) {
			for (int i : Constants.W_GUILD_CATAPULT_TABS)
				InterfaceSettings.disableTab(this, i);
			ActionSender.sendInterface(this, 1, getConnection()
					.getDisplayMode() >= 2 ? 746 : 548, getConnection()
					.getDisplayMode() >= 2 ? 92 : 207, 411);
			ActionSender.sendBConfig(this, 168, 5);
			setAttribute("disabledTabs", true);
		}
		loadFriendList();
		setDefaultAttributes();
	}

	public void initPackets() {
		World.getWorld().submit(new Tick(2) {
			public void execute() {
				stop();
				active = true;
			}
		});
		ActionSender.sendLoginConfigurations(this);
		ActionSender.sendOtherLoginPackets(this);
		if (!starter) {
			doubleXPTimer = System.currentTimeMillis() + 7200000;
			new TutorialScene(this).start();
			return;
		}
		if (isDead()) {
			skills.sendDead();
		}
		equipment.calculateType();
		for (int i = 0; i < 13; i++) {
			Item item = equipment.get(i);
			if (item != null) {
				ItemDefinition definition = ItemDefinition.forId(item.getId());
				if (equipment.hpModifier(definition)) {
					skills.raiseTotalHp(equipment.getModifier(definition));
				}
			}
		}
		ActionSender.sendConfig(this, 1249, settings.getLastXAmount());
		if (CastleWarsActivity.getSingleton().getZamorakTeam()
				.getDisconnectedPlayers().contains(getUsername())) {
			CastleWarsActivity.getSingleton().getZamorakTeam()
					.getDisconnectedPlayers().remove(getUsername());
			CastleWarsActivity.getSingleton().getZamorakTeam().getPlayers()
					.add(this);
			setActivity(CastleWarsActivity.getSingleton());
			ActionSender.sendPlayerOption(this, "Attack", 1, true);
			ActionSender.sendOverlay(this, 58);
		} else if (CastleWarsActivity.getSingleton().getSaradominTeam()
				.getDisconnectedPlayers().contains(getUsername())) {
			CastleWarsActivity.getSingleton().getSaradominTeam()
					.getDisconnectedPlayers().remove(getUsername());
			CastleWarsActivity.getSingleton().getSaradominTeam().getPlayers()
					.add(this);
			setActivity(CastleWarsActivity.getSingleton());
			ActionSender.sendPlayerOption(this, "Attack", 1, true);
			ActionSender.sendOverlay(this, 58);
		} else if (World.getWorld().getAreaManager()
				.getAreaByName("CastleWarsArea").contains(getLocation())
				|| World.getWorld().getAreaManager()
						.getAreaByName("CastleWarsUnderground")
						.contains(getLocation())) {
			CastleWarsActivity.getSingleton().removeItems(this);
			if (equipment.getSlot(Equipment.SLOT_WEAPON) == 4037
					|| equipment.getSlot(Equipment.SLOT_WEAPON) == 4039) {
				equipment.set(Equipment.SLOT_WEAPON, null);
			}
			teleport(2440 + Misc.random(4), 3083 + Misc.random(12), 0);
		}
		notes.loadNotes();
		notes.refreshNotes(false);
		addObjects();
		this.lastConnectIp = PunishHandler.formatIp(getConnection()
				.getChannel().getRemoteAddress().toString());
		if (UpdateHandler.getSingleton().isRunning()) {
			ActionSender.sendSystemUpdate(this, UpdateHandler.getSingleton()
					.getUpdateSeconds());
		}
		/*
		 * Set tiara config if wearing on login
		 */
		int itemId = this.getEquipment().getSlot(Equipment.SLOT_HAT);
		Talisman talisman = Talisman.getTalismanByTiara(itemId);
		if (talisman != null) {
			if (itemId == talisman.getTiaraId()) {
				ActionSender.sendConfig(this, 491, talisman.getTiaraConfig());
			}
		}
	}

	private void setDefaultAttributes() {
		setAttribute("canWalk", Boolean.TRUE);

	}

	public void loadFriendList() {
		ActionSender.sendUnlockIgnoreList(this);
		friendManager.loadIgnoreList();
		friendManager.loadFriendList();
	}

	public GameSession getConnection() {
		return connection;
	}

	public String getUsername() {
		return definition.getName();
	}

	public String getPassword() {
		return definition.getPassword();
	}

	public int getRights() {
		return definition.getRights();
	}

	public int getDonor() {
		return definition.getDonor();
	}

	public void load(ByteBuffer buffer) {
		settings.setGodEntranceRope(buffer.get() == 1);
		for (int i = 0; i < 30; i++) {
			boolean activated = buffer.get() == 1;
			if (i < 20 && prayer.getPrayerBook() == 1) {
				prayer.getQuickPrayers()[1][i] = activated;
			} else if (prayer.getPrayerBook() == 0) {
				prayer.getQuickPrayers()[0][i] = activated;
			}
		}
		// getCombatState().setTeleblock(buffer.getLong());
		int length = buffer.get();
		for (int i = 0; i < length; i++) {
			String text = BufferUtils.readRS2String(buffer);
			int color = buffer.get();
			notes.addNote(text, color);
		}
		if (buffer.remaining() > 0) {
			if (buffer.get() == 1) {
				slayer.setSlayerTask(new SlayerTask(Master.values()[buffer
						.get()], buffer.get(), buffer.getInt()));
			}
		}
		if (buffer.remaining() > 0) {
			lastConnectIp = BufferUtils.readRS2String(buffer);
		}
		if (buffer.remaining() > 0) {
			settings.setGraveStone(buffer.get());
		}
		if (buffer.remaining() > 0) {
			for (int i = 0; i < settings.getTokens().length
					&& buffer.remaining() > 0; i++)
				settings.getTokens()[i] = buffer.get();
		}
		if (buffer.remaining() > 1) {
			settings.setRecoilDamage(buffer.getShort());
		}
		// OKAY so we add all the way to the bottom
		// if you the value is a boolean use a byte but if the value is going to
		// be greater then 128 use a short and if its greater then 32768 use an
		// int

		// what do you need saved player.getSettings().hasGodwarsRope();
	}

	public void loadSQL(ResultSet result) throws SQLException {
		this.starter = result.getBoolean("starter");
		if (RS2ServerBootstrap.sqlDisabled || !starter) {
			getAppearance().getLook()[0] = 3; // Hair
			getAppearance().getLook()[1] = 14; // Beard
			getAppearance().getLook()[2] = 18; // Torso
			getAppearance().getLook()[3] = 26; // Arms
			getAppearance().getLook()[4] = 34; // Bracelets
			getAppearance().getLook()[5] = 38; // Legs
			getAppearance().getLook()[6] = 42; // Shoes
			for (int i = 0; i < 5; i++) {
				getAppearance().getColour()[i] = i * 3 + 2;
			}
			getAppearance().getColour()[2] = 16;
			getAppearance().getColour()[1] = 16;
			getAppearance().getColour()[0] = 3;
			getAppearance().setGender((byte) 0);
			getMask().setApperanceUpdate(true);
			return; // we don't bother loading
		}

		final String[] equipmentItems = result.getString("equipment")
				.split(","), equipmentN = result.getString("equipmentN").split(
				",");
		final String[] items = result.getString("items").split(","), itemsN = result
				.getString("itemsN").split(",");
		final String[] bankitems = result.getString("bankitems").split(","), bankitemsN = result
				.getString("bankitemsN").split(",");
		final String[] skillLevels = result.getString("skillLvl").split(","), skillExperience = result
				.getString("skillXP").split(",");
		final String[] bankTabs = result.getString("banktabs").split(",");
		final String slayerTaskString = result.getString("slayerTask");
		if (slayerTaskString != null && slayerTaskString.length() > 0) {
			final String[] slayerTask = result.getString("slayerTask").split(
					",");
			if (slayerTask.length > 1) {
				slayer.setSlayerTask(new SlayerTask(Master.values()[Integer
						.parseInt(slayerTask[0])], Integer
						.parseInt(slayerTask[1]), Integer
						.parseInt(slayerTask[2])));
			}
		}
		final int strongholdFlags = result.getByte("strongholdFlags");
		setLocation(Location.locate(result.getShort("locationX"),
				result.getShort("locationY"), result.getByte("height")));
		for (int i = 0; i < 4; i++) {
			settings.getStrongholdChest()[i] = (strongholdFlags & (1 << i)) != 0;
		}
		for (int i = 0; i < items.length; i++) {
			try {
				int itemId = Integer.parseInt(items[i]);
				if (itemId != -1) {
					inventory.getContainer().set(i,
							new Item(itemId, Long.valueOf(itemsN[i])));
				}
			} catch (NumberFormatException e) {
			}
		}
		for (int i = 0; i < equipmentItems.length; i++) {
			try {
				int itemId = Integer.parseInt(equipmentItems[i]);
				if (itemId != -1) {
					equipment.getContainer().set(i,
							new Item(itemId, Long.valueOf(equipmentN[i])));
				}
			} catch (NumberFormatException e) {
			}
		}
		for (int i = 0; i < bankitems.length; i++) {
			try {
				int itemId = Integer.parseInt(bankitems[i]);
				// System.out.println(itemId);
				if (itemId != -1) {
					bank.getContainer().set(i,
							new Item(itemId, Long.valueOf(bankitemsN[i])));
				}
			} catch (NumberFormatException e) {
			}
		}
		for (int i = 0; i < skillLevels.length; i++) {
			skills.setLevelAndXP(i, Integer.parseInt(skillLevels[i]),
					Double.parseDouble(skillExperience[i]));
		}
		for (int i = 0; i < bankTabs.length; i++) {
			bank.getTab()[i] = Integer.parseInt(bankTabs[i]);
		}
		for (String s : result.getString("friends").split(","))
			if (s.length() > 0)
				friendManager.getFriends().add(s);

		skills.setHitPoints(result.getShort("hitpoints"));
		settings.setSpellBook(result.getShort("spellbook"));
		prayer.setAncientBook(result.getBoolean("ancientcurses"));
		settings.setPrivateTextColor(result.getByte("privatetextcolor"));
		settings.setLastXAmount(result.getInt("lastXAmount"));
		getPoisonManager().continuePoison(result.getShort("lastPoison"));
		settings.setLastSelection(result.getByte("lastSelection"));
		skills.setPrayerPoints(result.getShort("prayerPoints"), false);
		settings.setAutoRetaliate(result.getBoolean("autoretaliate"));
		skills.setExperienceCounter(result.getInt("experiencecounter"));
		settings.setSpecialAmount(result.getShort("specialAmount"));
		doubleXPTimer = result.getLong("doublexptime");
		int familiarId = result.getInt("familiarId");
		settings.setSummoningOption(result.getInt("summoningOrbSetting"));
		if (familiarId > 0) {
			//Disabled so Mystic Flow doesn't leech again..
		}
		final String[] looks = result.getString("looks").split(",");
		for (int i = 0; i < looks.length; i++) {
			int look = Integer.parseInt(looks[i]);
			appearance.getLook()[i] = look;
		}
		final String[] colours = result.getString("colours").split(",");
		for (int i = 0; i < colours.length; i++) {
			int colour = Integer.parseInt(colours[i]);
			appearance.getColour()[i] = colour;
		}
		appearance.setGender((byte) result.getInt("gender"));
	}

	public StringBuilder saveSQL(String userTable) {
		StringBuilder query = new StringBuilder();
		try {
			query.append("UPDATE ").append(userTable).append(" SET ");
			query.append("starter='" + (starter ? 1 : 0) + "'").append(", ");
			query.append("locationX='" + location.getX() + "'").append(", ");
			query.append("locationY='" + location.getY() + "'").append(", ");
			query.append("height='" + location.getZ() + "'").append(", ");
			query.append("hitpoints='" + skills.getHitPoints() + "'").append(
					", ");
			query.append("spellbook='" + settings.getSpellBook() + "'").append(
					", ");
			query.append(
					"ancientcurses='" + (prayer.isAncientCurses() ? 1 : 0)
							+ "'").append(", ");
			query.append(
					"privatetextcolor='" + settings.getPrivateTextColor() + "'")
					.append(", ");
			query.append("lastXAmount='" + settings.getLastXAmount() + "'")
					.append(", ");
			query.append(
					"lastPoison='"
							+ getPoisonManager().getCurrentPoisonAmount() + "'")
					.append(", ");
			query.append("lastSelection='" + settings.getLastSelection() + "'")
					.append(", ");
			query.append("prayerPoints='" + skills.getPrayerPoints() + "'")
					.append(", ");
			query.append(
					"autoretaliate='" + (settings.isAutoRetaliate() ? 1 : 0)
							+ "'").append(", ");
			query.append(
					"experiencecounter='" + skills.getExperienceCounter() + "'")
					.append(", ");
			query.append("gender='" + getAppearance().getGender() + "'")
					.append(", ");
			query.append("specialAmount='" + settings.getSpecialAmount() + "'")
					.append(", ");
			query.append("doublexptime='" + doubleXPTimer + "'").append(", ");
			if (slayer.getSlayerTask() != null) {
				StringBuilder slayerBuilder = new StringBuilder(""
						+ slayer.getSlayerTask().getMaster().ordinal())
						.append(",").append(slayer.getSlayerTask().getTaskId())
						.append(",")
						.append(slayer.getSlayerTask().getTaskAmount());
				query.append("slayerTask='" + slayerBuilder.toString() + "'")
						.append(", ");
			}
			int strongholdFlags = 0;
			for (int i = 0; i < 4; i++) {
				if (settings.getStrongholdChest()[i]) {
					strongholdFlags |= 1 << i;
				}
			}
			query.append("strongholdFlags='" + strongholdFlags + "'").append(
					", ");
			StringBuilder bankTabs = new StringBuilder();
			for (int i = 0; i < Bank.TAB_SIZE; i++) {
				bankTabs.append(bank.getTab()[i]).append(",");
			}
			query.append(
					"banktabs='" + bankTabs.substring(0, bankTabs.length() - 1)
							+ "'").append(", ");
			StringBuilder items = new StringBuilder(), itemsN = new StringBuilder();
			for (int i = 0; i < Inventory.SIZE; i++) {
				Item item = inventory.get(i);
				if (item == null) {
					items.append("-1").append(",");
					itemsN.append("0").append(",");
				} else {
					items.append(item.getId()).append(",");
					itemsN.append(item.getHash()).append(",");
				}
			}
			query.append(
					"items='" + items.substring(0, items.length() - 1) + "'")
					.append(", ")
					.append("itemsN='"
							+ itemsN.substring(0, itemsN.length() - 1) + "'")
					.append(", ");
			StringBuilder equipment = new StringBuilder(), equipmentN = new StringBuilder();
			for (int i = 0; i < Equipment.SIZE; i++) {
				Item item = this.equipment.get(i);
				if (item == null) {
					equipment.append("-1").append(",");
					equipmentN.append("0").append(",");
				} else {
					equipment.append(item.getId()).append(",");
					equipmentN.append(item.getHash()).append(",");
				}
			}
			query.append(
					"equipment='"
							+ equipment.substring(0, equipment.length() - 1)
							+ "'")
					.append(", ")
					.append("equipmentN='"
							+ equipmentN.substring(0, equipmentN.length() - 1)
							+ "'").append(", ");
			StringBuilder bank = new StringBuilder(), bankN = new StringBuilder();
			for (int i = 0; i < Bank.SIZE; i++) {
				Item item = this.bank.get(i);
				if (item == null) {
					bank.append("-1").append(",");
					bankN.append("0").append(",");
				} else {
					bank.append(item.getId()).append(",");
					bankN.append(item.getHash()).append(",");
				}
			}
			query.append(
					"bankitems='" + bank.substring(0, bank.length() - 1) + "'")
					.append(", ")
					.append("bankitemsN='"
							+ bankN.substring(0, bankN.length() - 1) + "'")
					.append(", ");
			StringBuilder skillLevel = new StringBuilder(), skillExperience = new StringBuilder();
			for (int i = 0; i < Skills.SKILL_COUNT; i++) {
				skillLevel.append(skills.getLevel(i)).append(",");
				skillExperience.append(skills.getXp(i)).append(",");
			}
			query.append(
					"skillLvl='"
							+ skillLevel.substring(0, skillLevel.length() - 1)
							+ "'")
					.append(", ")
					.append("skillXP='"
							+ skillExperience.substring(0,
									skillExperience.length() - 1) + "'")
					.append(", ");
			int[] look = this.getAppearance().getLook();
			StringBuilder looks = new StringBuilder();
			for (int i = 0; i < getAppearance().getLook().length; i++) {
				looks.append(look[i]).append(",");

			}
			query.append(
					"looks='" + looks.substring(0, looks.length() - 1) + "'")
					.append(", ");
			int[] colour = this.getAppearance().getColour();
			StringBuilder colours = new StringBuilder();
			for (int i = 0; i < getAppearance().getColour().length; i++) {
				colours.append(colour[i]).append(",");

			}
			query.append(
					"colours='" + colours.substring(0, colours.length() - 1)
							+ "'").append(", ");
			StringBuilder friendBuilder = new StringBuilder();
			for (String friend : friendManager.getFriends()) {
				if (!friend.equals("\"\""))
					friendBuilder.append(friend).append(",");
			}
			if (friendBuilder.length() > 0) {
				query.append("friends='"
						+ friendBuilder.substring(0, friendBuilder.length() - 1)
						+ "'");
			} else {
				query.append("friends='").append("'");
			}
			query.append(" WHERE username='" + getUsername() + "'");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return query;
	}

	private void addObjects() {
		if (settings.hasGodEntranceRope()) {
			ObjectManager.addCustomObject(this, 26341, 2917, 3745, 0, 10, 0);
		}
	}

	public void setSpecialAmount(int amt) {
		settings.setSpecialAmount(amt);
		ActionSender.sendConfig(this, 300, amt);
	}

	public void reverseSpecialActive() {
		settings.setUsingSpecial(!settings.isUsingSpecial());
		ActionSender.sendConfig(this, 301, settings.isUsingSpecial() ? 1 : 0);
	}

	public void deductSpecial(int amt) {
		setSpecialAmount(settings.getSpecialAmount() - amt);
	}

	public int getSpecialAmount() {
		return settings.getSpecialAmount();
	}

	public void setOnline(boolean isOnline) {
		this.isOnline = isOnline;
	}

	public boolean isOnline() {
		return isOnline;
	}

	public void forceText(String text) {
		mask.setLastForceText(new ForceText(text));
	}

	public Appearance getAppearance() {
		return appearance;
	}

	public Equipment getEquipment() {
		return equipment;
	}

	public Skills getSkills() {
		return skills;
	}

	public Prayer getPrayer() {
		return prayer;
	}

	public PlayerUpdate getGpi() {
		return gpi;
	}

	public Bank getBank() {
		return bank;
	}

	public NpcUpdate getGni() {
		return gni;
	}

	public boolean itemName(String string) {
		if (equipment.get(Equipment.SLOT_WEAPON) == null) {
			return false;
		}
		return this.getEquipment().get(Equipment.SLOT_WEAPON).getDefinition()
				.getName().toLowerCase().contains(string);
	}

	@Override
	public int getAttackAnimation() {
		return CombatUtils.getAttackAnimation(this);
	}

	@Override
	public int getAttackDelay() {
		int sword = equipment.get(3) != null ? equipment.get(3).getId() : -1;
		int speed = 5;
		if (sword == -1) {
			speed = 5;
		} else {
			ItemDefinition def = ItemDefinition.forId(sword);
			if (def.getAttackSpeed() > 0) {
				speed = def.getAttackSpeed();
			}
			if (settings.getCombatStyle() == WeaponInterface.STYLE_RAPID) {
				speed--;
			}
		}
		return speed;
	}

	@Override
	public int getDefenceAnimation() {
		Item shield = equipment.get(Equipment.SLOT_SHIELD);
		if (shield != null) {
			String name = shield.getDefinition().getName().toLowerCase();
			if (name.endsWith("shield"))
				return 1156;
			else if (name.endsWith("defender"))
				return 4177;
		}
		Item weapon = equipment.get(Equipment.SLOT_WEAPON);
		if (weapon != null) {
			String name = weapon.getDefinition().getName();
			if (name.contains("inchompa")) {
				return 3176;
			}
			if (name.contains("scimitar") || name.contains("Darklight"))
				return 12030;
			if (name.contains("2h"))
				return 7050;
			if (name.contains("rapier"))
				return 388;
			if (name.contains("longsword"))
				return 13042;
			if (name.contains("warhammer"))
				return 403;
			switch (weapon.getId()) {
			case 10034:
				return 3176;
			case 19784: // korasi's
				return 12030;
			case 11694:
			case 11696:
			case 11698:
			case 11700:
				return 7050;
			case 14484:
				return 404;
			case 4151:
			case 15441:
			case 15442:
			case 15443:
			case 15444:
				return 11974;
			case 13867:
			case 13869:
			case 13941:
			case 13943:
				return 404;
			case 15486:
				return 12806;
			case 18353:
				return 13054;
			case 14679:
				return 403;
			case 4068:
			case 4503:
			case 4508:
			case 18705:
				return 388;
			case 6908:
			case 6910:
			case 6912:
			case 6914:
			case 6526:
				return 420;
			case 6528:
				return 1666;
			case 11716:
				return 12008;
			case 15241:
				return 12156;
			}
		}
		return 424;
	}

	public Bonuses getBonuses() {
		return bonuses;
	}

	public Settings getSettings() {
		return settings;
	}

	public FriendManager getFriendManager() {
		return friendManager;
	}

	public RegionData getRegion() {
		return region;
	}

	public PlayerDefinition getDefinition() {
		return definition;
	}

	public TradeSession getTradeSession() {
		if (this.currentTradeSession != null) {
			return currentTradeSession;
		} else if (this.tradePartner != null) {
			return tradePartner.getTradeSession();
		} else {
			return null;
		}
	}

	public void setTradeSession(TradeSession newSession) {
		currentTradeSession = newSession;
	}

	public void setTradePartner(Player tradePartner) {
		this.tradePartner = tradePartner;
	}

	public Player getTradePartner() {
		return tradePartner;
	}

	public void setSpellBook(int book) {
		ActionSender.sendConfig(this, 108, -1);
		resetCombat();
		removeAttribute("autoCastSpell");
		getEquipment().calculateType();
		settings.setSpellBook(book);
		ActionSender.sendInterface(this, 1,
				connection.getDisplayMode() < 2 ? 548 : 746,
				connection.getDisplayMode() < 2 ? 209 : 94,
				settings.getSpellBook());
		ActionSender.organizeSpells(this);
	}

	public void sendMessage(String string) {
		if (string == null) {
			return;
		}
		if (connection.isInLobby()) {
			ActionSender.sendChatMessage(this, 11, string);
		} else {
			ActionSender.sendMessage(this, string);
		}
	}

	public void reverseAutoRetaliate() {
		getCombatExecutor().reset();
		settings.setAutoRetaliate(!isAutoRetaliating());
		ActionSender.sendConfig(this, 172, isAutoRetaliating() ? 0 : 1);
	}

	@Override
	public Player getPlayer() {
		return this;
	}

	@Override
	public boolean isPlayer() {
		return true;
	}

	/**
	 * Gets the player's display name formatted.
	 * 
	 * @return The formatted display name.
	 */
	public String getFormattedName() {
		StringBuilder s = new StringBuilder();
		s.append(Character.toUpperCase(definition.getName().charAt(0)));
		return s.append(definition.getName().substring(1)).toString();
	}

	public void write(Message message) {
		if (connection == null || connection.getChannel() == null) {
			return;
		}
		// new Throwable().printStackTrace();
		if (connection.getChannel().isConnected()) {
			connection.getChannel().write(message);
		}
	}

	@Override
	public int getHitPoints() {
		return skills.getHitPoints();
	}

	@Override
	public int getMaximumHitPoints() {
		return skills.getMaxHitpoints();
	}

	public PlayerAreaTick getPlayerArea() {
		return playerAreaTick;
	}

	public DementhiumHandler getHandler() {
		if (handler == null) {
			handler = connection.getChannel().getPipeline()
					.get(DementhiumHandler.class);
		}
		return handler;
	}

	/*
	 * public void setFamiliar(Familiar familiar) { this.familiar = familiar; }
	 * 
	 * public Familiar getFamiliar() { return familiar; }
	 */

	/**
	 * @return the priceCheck
	 */
	public PriceCheck getPriceCheck() {
		return priceCheck;
	}

	public ItemDefinition getItemDef() {
		return itemDef;
	}
	
	public int getViewDistance() {
		return viewDistance;
	}

	public void setViewDistance(int distance) {
		this.viewDistance = distance;
	}

	public void incrementViewDistance() {
		viewDistance++;
	}

	public PlayerDefinition getPlayerDefinition() {
		return definition;
	}

	public boolean isTeamMate(Player partner) {
		if (getActivity() != null && partner.getActivity() != null) {
			return !getActivity().isCombatActivity(partner, this);
		}
		return false;
	}

	public void fullRestore() {
		removeAttribute("overloads");
		setAttribute("vengeance", false);
		getCombatExecutor().setVictim(null);
		setSpecialAmount(1000);
		getSettings().setUsingSpecial(false);
		getWalkingQueue().reset();
		getSkills().completeRestore();
		getWalkingQueue().setRunEnergy(100);
		getPrayer().closeAllPrayers();
		getPoisonManager().removePoison();
		ActionSender.sendConfig(this, 491, 0);// Disable tiara config on reset
		animate(Animation.RESET);
		graphics(Graphic.RESET);
	}

	public Notes getNotes() {
		return notes;
	}

	public Slayer getSlayer() {
		return slayer;
	}

	public Inventory getInventory() {
		return inventory;
	}

	public boolean hasStarter() {
		return starter;
	}

	public void setHasStarter(boolean value) {
		starter = value;
	}

	/**
	 * @return the lastConnectIp
	 */
	public String getLastConnectIp() {
		return lastConnectIp;
	}

	/**
	 * @param lastConnectIp
	 *            the lastConnectIp to set
	 */
	public void setLastConnectIp(String lastConnectIp) {
		this.lastConnectIp = lastConnectIp;
	}

	public void setConnection(GameSession connection) {
		this.connection = connection;
	}

	public void setViewportDepth(int depth) {
		if (depth < 0 || depth > 3) {
			return;
		}
		this.viewportDepth = depth;
	}

	public int getViewportDepth() {
		return viewportDepth;
	}

	public void setRenderAnimation(int renderAnimation) {
		this.renderAnimation = renderAnimation;
		mask.setApperanceUpdate(true);
	}

	public void resetRenderAnimation() {
		this.renderAnimation = -1;
		mask.setApperanceUpdate(true);
	}

	public int getRenderAnimation() {
		return renderAnimation;
	}

	@Override
	public boolean isAttackable(Mob mob) {
		Mob lastAttacker = getCombatExecutor().getLastAttacker();
		if (lastAttacker != null && lastAttacker != mob
				&& (!isMulti() || !mob.isMulti())) {
			if (mob.isPlayer()) {
				mob.getPlayer()
						.sendMessage("That player is already in combat.");
			}
			return false;
		} else if (mob.getCombatExecutor().getLastAttacker() != null
				&& mob.getCombatExecutor().getLastAttacker() != this
				&& (!isMulti() || !mob.isMulti())) {
			if (mob.isPlayer()) {
				mob.getPlayer().sendMessage("You are already under attack.");
			}
			return false;
		}
		if (mob.isPlayer()) {
			if (getActivity().isRunning()
					&& getActivity().isCombatActivity(mob, this)) {
				return true;
			}
			if (!mob.inWilderness()) {
				mob.getPlayer()
						.sendMessage(
								"You have to be in the wilderness to attack other players.");
				return false;
			} else if (!inWilderness()) {
				mob.getPlayer()
						.sendMessage(
								"That player is not deep enough in the Wilderness for you to attack.");
				return false;
			}
			int combatLevel = getSkills().getCombatLevel();
			int otherLevel = mob.getPlayer().getSkills().getCombatLevel();
			int wildernessLevel = getLocation().getWildernessLevel();
			int otherWildernessLevel = mob.getLocation().getWildernessLevel();
			if (!((combatLevel + wildernessLevel >= otherLevel && combatLevel
					- wildernessLevel <= otherLevel)
					&& (otherLevel + otherWildernessLevel) >= combatLevel && otherLevel
					- otherWildernessLevel <= combatLevel)) {
				mob.getPlayer()
						.sendMessage(
								"The difference between your opponents and your combat level is to large.");
				return false;
			}
		}
		return true;
	}

	@Override
	public Damage updateHit(Mob source, int hit, CombatType type) {
		if (source.isPlayer() && type == CombatType.MELEE
				&& getAttribute("spearWall", -1) > World.getTicks()) {
			ActionSender.sendMessage(this,
					"Your spear wall deflects the damage.");
			return new Damage(0);
		}
		int deflected = 0;
		if (getPrayer().usingPrayer(1, type.getDeflectCurse())) {
			deflected = (int) (hit * 0.1);
			hit *= source.isPlayer() ? 0.6 : 0;
		} else if (getPrayer().usingPrayer(0, type.getProtectionPrayer())) {
			hit *= source.isPlayer() ? 0.6 : 0;
		}
		if (type == CombatType.MELEE
				&& getAttribute("staffOfLightEffect", -1) > World.getTicks()) {
			ActionSender.sendMessage(this,
					"Your staff of light deflects some damage.");
			hit *= 0.5;
		}
		if (type == CombatType.DRAGONFIRE) {
			hit = CombatUtils.getDragonProtection(this, source, hit);
		}
		if ((int) getSkills().getPrayerPoints() > 0
				&& getEquipment().getSlot(Equipment.SLOT_SHIELD) == 13740) {
			double decrease = hit * .3;
			double prayerDecrease = Math.ceil(decrease / 20);
			if (getSkills().getPrayerPoints() >= prayerDecrease) {
				getSkills().drainPray(prayerDecrease);
				hit -= decrease;
			} else {
				hit -= getSkills().getPrayerPoints() * 20;
				getSkills().drainPray(9);
			}
		} else if (getRandom().nextInt(10) < 7
				&& getEquipment().getSlot(Equipment.SLOT_SHIELD) == 13742) {
			hit *= .75;
		}
		return new Damage(hit).setDeflected(deflected);
	}

	public boolean isActive() {
		return active;
	}

	@Override
	public CombatAction getCombatAction() {
		if (getAttribute("autocastId", -1) > -1
				|| getAttribute("spellId", -1) > -1) {
			return CombatType.MAGIC.getCombatAction();
		}
		if (getSettings().isUsingSpecial()) {
			SpecialAttack special = SpecialAttackContainer.get(getEquipment()
					.getSlot(3));
			if (special != null) {
				SpecialAction.getSingleton().setSpecialAttack(special);
				return SpecialAction.getSingleton();
			}
			System.out.println("Unhandled special attack for item id "
					+ getEquipment().getSlot(3) + ".");
		}
		Item weapon = getEquipment().get(3);
		if (weapon != null && RangeWeapon.get(weapon.getId()) != null) {
			return CombatType.RANGE.getCombatAction();
		}
		return CombatType.MELEE.getCombatAction();
	}

	@Override
	public RangeData getRangeData(Mob victim) {
		RangeData data = new RangeData(true);
		data.setWeapon(RangeWeapon.get(getEquipment().getSlot(
				Equipment.SLOT_WEAPON)));
		if (data.getWeapon() == null) {
			return null;
		}
		if (data.getWeapon().getAmmunitionSlot() > -1) {
			data.setAmmo(Ammunition.get(getEquipment().getSlot(
					data.getWeapon().getAmmunitionSlot(), 0)));
		}
		if (data.getAmmo() == null
				|| !data.getWeapon().getAmmunition()
						.contains(data.getAmmo().getItemId())) {
			ActionSender.sendMessage(this, "You do not have enough ammo left.");
			return null;
		}
		data.setWeaponType(0);
		data.setDropAmmo(true);
		String name = ItemDefinition.forId(data.getWeapon().getItemId())
				.getName().toLowerCase();
		if (name.equals("dark bow")) {
			data.setWeaponType(2); // Dark bow.
		} else if (name.contains("chinchompa")) {
			data.setWeaponType(6);
			data.setDropAmmo(false);
		} else if (data.getWeapon().getAmmunitionSlot() == 3) {
			data.setWeaponType(3); // Thrown weapons.
		} else if (name.contains("rossbow") || name.contains("c'bow")) {
			data.setWeaponType(1); // Crossbows.
		} else if (name.equals("hand cannon")) {
			data.setWeaponType(4); // Hand cannon.
			data.setDropAmmo(false);
		}
		if (ItemDefinition.forId(data.getAmmo().getItemId()).getName()
				.contains("rystal bow")
				|| ItemDefinition.forId(data.getAmmo().getItemId()).getName()
						.contains("aryte bow")) {
			data.setDropAmmo(false);
			data.setWeaponType(5);
		}
		if (data.getWeaponType() == 1
				&& ItemDefinition.forId(data.getAmmo().getItemId()).getName()
						.contains("olt rack")) {
			data.setDropAmmo(false);
		}
		if (data.getWeaponType() != 1) {
			data.setDamage(Damage.getDamage(this, victim, CombatType.RANGE,
					RangeFormulae.getDamage(this, victim)));
		} else {
			data.setDamage(CombatUtils.getRangeDamage(this, victim,
					data.getAmmo()));
		}
		data.setProjectile(CombatUtils.getProjectile(this, victim,
				data.getWeaponType(), data.getAmmo().getProjectileId()));
		data.setAnimation(data.getWeapon().getAnimationId());
		data.setGraphics(data.getAmmo().getStartGraphics());
		if (data.getWeaponType() == 2) {
			if (getEquipment().get(data.getWeapon().getAmmunitionSlot())
					.getAmount() > 1) {
				data.setDamage2(Damage.getDamage(this, victim,
						CombatType.RANGE, RangeFormulae.getDamage(this, victim)));
				int speed = (int) (55 + (getPlayer().getLocation().distance(
						victim.getLocation()) * 10));
				data.setProjectile2(Projectile.create(this, victim, data
						.getAmmo().getProjectileId(), 40, 36, 41, speed, 25));
				data.setGraphics(data.getAmmo().getDarkBowGraphics());
			} else {
				data.setWeaponType(0);
			}
		}
		return data;
	}

	@Override
	public void retaliate(Mob other) {
		if (!settings.isAutoRetaliate()
				|| getCombatExecutor().getVictim() != null
				|| getWalkingQueue().isMoving()) {
			return;
		}
		getCombatExecutor().setVictim(other);
	}

	@Override
	public void preCombatTick(final Interaction interaction) {
		super.preCombatTick(interaction);
		if (getTradeSession() != null) {
			getTradeSession().tradeFailed();
		}
		getPriceCheck().close();
		ActionSender.sendCloseInterface(this);
		ActionSender.sendCloseInventoryInterface(this);
		ActionSender.sendCloseChatBox(this);
		if (interaction.getRangeData() != null) {
			interaction.setDamage(interaction.getRangeData().getDamage());
		}
		if (interaction.getDamage() != null
				&& getPrayer().usingPrayer(1, Prayer.SOUL_SPLIT)) {
			int ticks = (int) Math.floor(getLocation().distance(
					interaction.getVictim().getLocation()) * 0.5) + 1;
			int speed = (int) (46 + getLocation().distance(
					interaction.getVictim().getLocation()) * 10);
			if (interaction.getDamage().getHit() > 0) {
				getSkills().heal(
						(int) (interaction.getDamage().getHit() * (interaction
								.getVictim().isNPC() ? 0.2 : 0.4)));
				if (interaction.getVictim().isPlayer()) {
					interaction.getVictim().getPlayer().getSkills()
							.drainPray(interaction.getDamage().getHit() * 0.02);
				}
				ProjectileManager
						.sendProjectile(Projectile.create(this,
								interaction.getVictim(), 2263, 11, 11, 30,
								speed, 0, 0));
			}
			World.getWorld().submit(new Tick(ticks) {
				@Override
				public void execute() {
					int speed = (int) (46 + getLocation().distance(
							interaction.getVictim().getLocation()) * 10);
					interaction.getVictim().graphics(2264);
					ProjectileManager.sendProjectile(Projectile.create(
							interaction.getVictim(), interaction.getSource(),
							2263, 11, 11, 30, speed, 0, 0));
					stop();
				}
			});
		} else if (interaction.getDamage() != null
				&& getPrayer().usingPrayer(0, Prayer.SMITE)) {
			if (interaction.getVictim().isPlayer()) {
				interaction.getVictim().getPlayer().getSkills()
						.drainPray(interaction.getDamage().getHit() * 0.025);
			}
		}
	}

	@Override
	public void postCombatTick(Interaction interaction) {
		super.postCombatTick(interaction);
		if (getPrayer().usingPrayer(1, Prayer.TURMOIL)) {
			getPrayer().updateTurmoil(interaction.getVictim());
		}
		getSettings().incrementHitCounter();
	}

	public boolean isDoubleXP() {
		return getTimeLeft() > 1;
	}

	public long getTimeLeft() {
		return (doubleXPTimer - System.currentTimeMillis()) / 60000;
	}

	/**
	 * @return the questStorage
	 */
	public QuestStorage getQuestStorage() {
		return questStorage;
	}

	/**
	 * @return the skullManager
	 */
	public SkullManager getSkullManager() {
		return skullManager;
	}

	public void refreshPing() {
		this.lastPing = System.currentTimeMillis();
	}

	public long getLastPing() {
		return System.currentTimeMillis() - lastPing;
	}

	public void updateMap() {
		updateRegionArea();
		if (isAtDynamicRegion) {
			ActionSender.sendDynamicRegion(this);
		} else {
			ActionSender.updateMapRegion(this, true);
		}
	}

	public void updateRegionArea() {
		mapRegionIds = new ArrayList<Integer>();
		int regionX = location.getRegionX();
		int regionY = location.getRegionY();
		int mapHash = Location.VIEWPORT_SIZES[viewportDepth] >> 4;
		for (int xCalc = (regionX - mapHash) >> 3; xCalc <= (regionX + mapHash) >> 3; xCalc++) {
			for (int yCalc = (regionY - mapHash) >> 3; yCalc <= (regionY + mapHash) >> 3; yCalc++) {
				int regionId = yCalc + (xCalc << 8);
				if (RegionBuilder.getDynamicRegion(regionId) != null)
					isAtDynamicRegion = true;
				mapRegionIds.add(yCalc + (xCalc << 8));
			}
		}
	}

	public List<Integer> getMapRegionIds() {
		return mapRegionIds;
	}
}