package org.dementhium.model.npc;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import org.dementhium.cache.format.CacheNPCDefinition;
import org.dementhium.content.activity.impl.CastleWarsActivity;
import org.dementhium.content.activity.impl.barrows.BarrowsConstants;
import org.dementhium.content.clans.Clan;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.Mob;
import org.dementhium.model.Projectile;
import org.dementhium.model.World;
import org.dementhium.model.combat.CombatAction;
import org.dementhium.model.combat.CombatType;
import org.dementhium.model.combat.CombatUtils;
import org.dementhium.model.combat.Damage;
import org.dementhium.model.combat.RangeData;
import org.dementhium.model.combat.RangeFormulae;
import org.dementhium.model.definition.ItemDefinition;
import org.dementhium.model.definition.NPCDefinition;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.model.map.Region;
import org.dementhium.model.map.path.DefaultPathFinder;
import org.dementhium.model.mask.ForceText;
import org.dementhium.model.mask.Graphic;
import org.dementhium.model.misc.GroundItem;
import org.dementhium.model.misc.GroundItemManager;
import org.dementhium.model.npc.NPCDropLoader.Drop;
import org.dementhium.model.player.Equipment;
import org.dementhium.model.player.Player;
import org.dementhium.model.player.Skills;
import org.dementhium.net.ActionSender;
import org.dementhium.task.Task;
import org.dementhium.task.impl.NPCResetTask;
import org.dementhium.task.impl.NPCTickTask;
import org.dementhium.tickable.Tick;
import org.dementhium.util.Constants;
import org.dementhium.util.Misc;

public class NPC extends Mob {

    public static final int[] UNRESPAWNABLE_NPCS = {
            2746, 2025, 2026, 2027, 2028, 2029, 2030, 1532, 1533, 1534, 1535,
            13447, 13448, 13449, 13450, 13451, 13452, 13453, 13454, 8127, 3851,
            5384, 4278, 4279, 4280, 4281, 4282, 4283, 4284
    };

    public static final int[][] SPECIAL_DEATH_NPCS = {
            {6260, 6261, 6263, 6265},
            {6247, 6248, 6250, 6252},
            {6222, 6223, 6225, 6227},
            {6203, 6204, 6206, 6208}
    };
    //164, 163
    
    private Task[] npcTasks = {
            new NPCTickTask(this), new NPCResetTask(this)
    };//388, 386

    private String name;
    private int id;
    private int hitpoints = 100;

    private Location originalLoc;
    private boolean isDead;

    private NPCDefinition definition;

    private boolean doesWalk = false;

    private int faceDir = 1;

    public boolean testingmask;
    public boolean testingmask2;

    /**
     * The dynamic bonuses: changes if the NPC gets sapped/leeched.
     */
    private int attackModifier, strengthModifier,
            defenceModifier, rangeModifier, magicModifier;

    private boolean unrespawnable;

    private int aggressiveTicks = 2;
    
    private boolean hitsThroughPrayer;

    public NPC(int id) {
        super();
        this.setId(id);
        this.definition = NPCDefinition.forId(id);
        this.hitpoints = definition.getHitpoints();
        getWalkingQueue();
        loadEntityVariables();
    }

    public NPC(int id, Location location) {
        super();
        this.setId(id);
        this.definition = NPCDefinition.forId(id);
        this.hitpoints = definition.getHitpoints();
        setLocation(location);
        setOriginalLocation(location);
        getWalkingQueue();
        loadEntityVariables();
    }

    public NPC(int id, int x, int y, int z) {
        super();
        this.setId(id);
        this.definition = NPCDefinition.forId(id);
        this.hitpoints = definition.getHitpoints();
        setOriginalLocation(Location.locate(x, y, z));
        setLocation(originalLoc);
        getWalkingQueue();
        loadEntityVariables();
    }

    public void tick() {
        if (getCombatExecutor().getVictim() == null && getRandom().nextDouble() > 0.85 && mask.getInteractingEntity() == null && doesWalk && !isDead()) {
            int moveX = originalLoc.getX() + Misc.random(-3, 3), moveY = originalLoc.getY() + Misc.random(-3, 3);
            requestClippedWalk(moveX, moveY);
        }
        if (originalLoc != null && location.getDistance(originalLoc) > 14) {
            resetCombat();
            if (location.getDistance(originalLoc) > 17) {
                World.getWorld().doPath(new DefaultPathFinder(), this, originalLoc.getX(), originalLoc.getY());
            }
        }
    }

    public void walkTo(int moveX, int moveY) {
        if (location.getX() == moveX && location.getY() == moveY) {
            return;
        }
        int firstX = moveX - (location.getRegionX() - 6) * 8;
        int firstY = moveY - (location.getRegionY() - 6) * 8;
        walkingQueue.reset();
        walkingQueue.addToWalkingQueue(firstX, firstY);
    }

    public void setOriginalLocation(Location location) {
        this.originalLoc = location;
    }


    public void hit(int damage) {
        if (getHitPoints() <= 0) {
            sendDead();
            return;
        }
        if (damage > getHitPoints()) {
            damage = getHitPoints();
        }
        this.hitpoints = getHitPoints() - damage;
        if (getHitPoints() <= 0) {
           /* if (isNex()) {
                mask.setSwitchId(Nex.WRATH_NEX);
                forceText("Taste my wrath!");
                World.getWorld().submit(new Tick(3) {
                    public void execute() {
                        stop();
                        Prayer.wrathEffect(NPC.this, NPC.this.getCombatExecutor().getLastAttacker());
                    }
                });
            }*/
            sendDead();
        }
    }

    public void sendDead() {
        if (isDead()) {
            return;
        }
        Mob last = getCombatExecutor().getLastAttacker();
        if (last != null) {
        	last.setAttribute("combatTicks", 0);
        }
        getPoisonManager().removePoison();
        final Mob killer = getDamageManager().getKiller();
        setDead(true);
        resetTurnTo();
        animate(getDeathAnimation());
        int deathTick = getDeathTick();
        if (getDefinition().getFaction() != null && killer != null && killer.isPlayer()) {
            switch (getDefinition().getFaction()) {
                case ARMADYL:
                    killer.getPlayer().getSettings().getKillCount()[Constants.ARMADYL_KILL_COUNT]++;
                    break;
                case BANDOS:
                    killer.getPlayer().getSettings().getKillCount()[Constants.BANDOS_KILL_COUNT]++;
                    break;
                case ZAMORAK:
                    killer.getPlayer().getSettings().getKillCount()[Constants.ZAMMY_KILL_COUNT]++;
                    break;
                case SARADOMIN:
                    killer.getPlayer().getSettings().getKillCount()[Constants.SARA_KILL_COUNT]++;
                    break;
                case ZAROS:
                    killer.getPlayer().getSettings().getKillCount()[Constants.ZAROS_KILL_COUNT]++;
                    break;
            }
        }
        World.getWorld().submit(new Tick(deathTick) {
            @Override
            public void execute() {
                stop();
                if (killer != null) {
                    loot(killer);
                    getDamageManager().clearEnemyHits();
                }
            }
        });
        for (int i : UNRESPAWNABLE_NPCS) {
            if (id == i || unrespawnable) {
                World.getWorld().submit(new Tick(deathTick) {
                    @Override
                    public void execute() {
                        if (id == 1532 || id == 1533) {
                            CastleWarsActivity.getSingleton().getSaradominTeam().removeBarricade(NPC.this);
                        } else if (id == 1534 || id == 1535) {
                            CastleWarsActivity.getSingleton().getZamorakTeam().removeBarricade(NPC.this);
                        } else {
                            World.getWorld().getNpcs().remove(NPC.this);
                        }
                        stop();
                    }
                });
                return;
            }
        }
        World.getWorld().submit(new Tick(deathTick) {
            @Override
            public void execute() {
                setHidden(true);
                stop();
                if (isSpecial(id)) {
                    doSpecialDeath();
                    return;
                }
            }
        });

        if (!isSpecial(id)) {
            World.getWorld().submit(new Tick(60 + deathTick) {
                @Override
                public void execute() {
                    resetCombat();
                    setHidden(false);
                    teleport(getOriginalLocation());
                    setHp(getMaxHp());
                    setDead(false);
                    stop();
                }
            });
        }

    }

    private void doSpecialDeath() {
        final int index = getGroupIndex();
        int amt = 0;
        for (int npcId : SPECIAL_DEATH_NPCS[index]) {
            if (isSpecial(npcId)) {
                if (SPECIAL_DEATH_NPCS[index].length == 1 || (SPECIAL_DEATH_NPCS[index].length > 1 && World.getWorld().getNpcs().getById(npcId).isHidden()))
                    amt++;
            }
        }
        if (amt == SPECIAL_DEATH_NPCS[index].length) {
            World.getWorld().submit(new Tick(30 + getDeathTick()) {
                @Override
                public void execute() {
                    for (int npcId : SPECIAL_DEATH_NPCS[index]) {
                        if (isSpecial(npcId)) {
                            NPC npc = World.getWorld().getNpcs().getById(npcId).getNPC();
                            npc.resetCombat();
                            npc.setHidden(false);
                            npc.teleport(npc.getOriginalLocation());
                            npc.setHp(npc.getMaxHp());
                            npc.setDead(false);
                        }
                    }
                    this.stop();
                }
            });
        }

    }

    private int getGroupIndex() {
        for (int i = 0; i < SPECIAL_DEATH_NPCS.length; i++) {
            for (int y : SPECIAL_DEATH_NPCS[i]) {
                if (y == id) {
                    return i;
                }
            }
        }
        return -1;
    }

    private boolean isSpecial(int id) {
        for (int i[] : SPECIAL_DEATH_NPCS) {
            for (int y : i) {
                if (y == id) return true;
            }
        }
        return false;
    }

    public int getDeathTick() {
        if (id >= 13447 && id <= 13450) {
            return 7;
        }
        if (id == 5073) {
            return 10;
        }
        if (id == 5075) {
            return 40;
        }
        if (id == 5076) {
            return 50;
        }
        if (id == 5074) {
            return 60;
        }
        if (id == 5072) {
            return 70;
        }
        if (id == 5080) {
            return 30;
        }
        return 4;
    }

    public void loot(Mob mob) {
        if (mob != null && mob.isPlayer()) {
            Player killer = mob.getPlayer();
            if (killer.getSlayer().getSlayerTask() != null) {
                if (killer.getSlayer().getSlayerTask().getName().equalsIgnoreCase(definition.getCacheDefinition().name)) {
                    killer.getSlayer().killedTask();
                }
            }
            if (BarrowsConstants.isInBarrowsZone(killer)) {
            	killer.getSettings().setBarrowsKillcount(killer.getSettings().getBarrowsKillcount() + 1);
        		int hash = 0;
        		for (int id : killer.getSettings().getBarrowsKilled()) {
        			hash |= 1 << (id - 2025);
        		}
        		ActionSender.sendConfig(killer, 453, killer.getSettings().getBarrowsKillcount() << 17 | hash);
            }
/*            if (id >= 2025 && id <= 2030) { // barrow brothers
                if (killer.getAttribute(Barrows.FIGHTING_ATTRIBUTE) != null) {
                    NPC npc = killer.getAttribute(Barrows.FIGHTING_ATTRIBUTE);
                    if (this == npc) {
                        killer.getSettings().getKilledBrothers()[Barrows.getIndexForBrother(this)] = true;
                        killer.getPlayerArea().updateBarrowsInterface();
                    }
                    Brother brother = killer.getAttribute(Barrows.TUNNEL_CRYPT);
                    if (brother != null && brother.getNpcId() == id) {
                        killer.getSettings().getKillCount()[Constants.BARROW_KILL_COUNT] = 0;
                        killer.removeAttribute(Barrows.TUNNEL_CRYPT);
                        killer.setAttribute("canLoot", Boolean.TRUE);
                    }
                }
            }
            for (int i : Barrows.UNDERGROUND_NPCS) {
                if (id == i) {
                    killer.getSettings().getKillCount()[Constants.BARROW_KILL_COUNT]++;
                    if (killer.getSettings().getKillCount()[Constants.BARROW_KILL_COUNT] > 9994) {
                        killer.getSettings().getKillCount()[Constants.BARROW_KILL_COUNT] = 9994;
                    }
                    killer.getPlayerArea().updateBarrowsInterface();
                }
            }*/
            List<Drop> drops = World.getWorld().getNpcDropLoader().getDropMap().get(id);
            if (drops == null) {
                return;
            }
            Collections.shuffle(drops, new Random());
            if (drops != null) {
                List<Drop> hitDrops = new ArrayList<Drop>();
                boolean rareDrop = false;
                for (Drop d : drops) {
                    int chance = getRandom().nextInt(100);
                    if (chance <= d.getRate()) {
                        if (d.getRate() < 100) {
                            if (d.getItemId() >= 20135 && d.getItemId() <= 20174) {
                                if (rareDrop) {
                                    continue;
                                }
                                rareDrop = true;
                            }
                            hitDrops.add(d);
                        } else {
                            Player receiver = killer;
                            int itemAmount = Misc.random(d.getMinAmount(), d.getMaxAmount());
                            if (receiver.getSettings().getCurrentClan() != null && receiver.getSettings().getCurrentClan().isLootsharing() && receiver.getSettings().getCurrentClan().isCoinSharing()) {
                                if (ItemDefinition.forId(d.getItemId()).getExchangePrice() > 500000) {
                                    splitDrop(d, receiver.getSettings().getCurrentClan());
                                } else {
                                    receiver = getLooter(receiver, d.getItemId(), itemAmount, receiver.getSettings().getCurrentClan(), getLocation());
                                    GroundItemManager.createGroundItem(new GroundItem(receiver, new Item(d.getItemId(), Misc.random(d.getMinAmount(), d.getMaxAmount())), getLocation(), false));
                                }
                            } else if (receiver.getSettings().getCurrentClan() != null && receiver.getSettings().getCurrentClan().isCoinSharing()) {
                                splitDrop(d, receiver.getSettings().getCurrentClan());
                            } else if (receiver.getSettings().getCurrentClan() != null && receiver.getSettings().getCurrentClan().isLootsharing()) {
                                GroundItemManager.createGroundItem(new GroundItem(getLooter(receiver, d.getItemId(), itemAmount, receiver.getSettings().getCurrentClan(), getLocation()), new Item(d.getItemId(), Misc.random(d.getMinAmount(), d.getMaxAmount())), getLocation(), false));
                            } else {
                                GroundItemManager.createGroundItem(new GroundItem(receiver, new Item(d.getItemId(), Misc.random(d.getMinAmount(), d.getMaxAmount())), getLocation(), false));
                            }
                        }

                    }
                }
                int amount = getDropAmount();
                while (!hitDrops.isEmpty() && amount > 0) {
                    int dropIndex = getRandom().nextInt(hitDrops.size());
                    Drop item = hitDrops.get(dropIndex);
                    Player receiver = killer;
                    int itemAmount = Misc.random(item.getMinAmount(), item.getMaxAmount());
                    if (receiver.getSettings().getCurrentClan() != null && receiver.getSettings().getCurrentClan().isLootsharing()) {
                        receiver = getLooter(receiver, item.getItemId(), itemAmount, receiver.getSettings().getCurrentClan(), getLocation());
                    }
                    GroundItemManager.createGroundItem(new GroundItem(receiver, new Item(item.getItemId(), itemAmount), getLocation(), false));
                    hitDrops.remove(dropIndex);
                    amount--;
                }
            }
        }
    }

    private void splitDrop(Drop d, Clan clan) {
        ArrayList<Player> receivingPlayers = new ArrayList<Player>();
        int amount = Misc.random(d.getMinAmount(), d.getMaxAmount());
        int price = ItemDefinition.forId(d.getItemId()).getExchangePrice() * amount;
        for (Player pl : Region.getLocalPlayers(location, 16)) {
            if (clan.getMembers().contains(pl)) {
                if (!getDamageManager().getEnemyHits().containsKey(pl)) {
                    continue;
                }
                receivingPlayers.add(pl);
            }
        }
        int priceSplit = price / receivingPlayers.size();
        for (Player pl : receivingPlayers) {
            GroundItemManager.createGroundItem(new GroundItem(pl, new Item(995, priceSplit), getLocation(), false));
            pl.sendMessage("<col=009900>You received " + priceSplit + " coins as your split of the drop: " + priceSplit + " " + ItemDefinition.forId(d.getItemId()).getName());
        }

    }

    public Player getLooter(Player player, int id, int amount, Clan clan, Location location) {
        Player done = player;
        if (clan != null) {
            if (clan.isLootsharing()) {
                List<Player> playersGetLoot = new LinkedList<Player>();
                int best = 0;
                for (Player pl : World.getWorld().getPlayers()) { //TODO Region.getLocalPlayers(location, 16)) {
                    if (pl.getLocation().getDistance(location) > 20 || !clan.canShareLoot(pl)) {
                        continue;
                    }
                    if (clan.getMembers().contains(pl)) {
                        if (!getDamageManager().getEnemyHits().containsKey(pl)) {
                            continue;
                        }
                        int damage = getDamageManager().getEnemyHits().get(pl) + pl.getSettings().getChances();
                        if (damage > best) {
                            playersGetLoot.add(pl);
                            best = damage;
                        } else {
                            if (Misc.random(2) == 1) {
                                playersGetLoot.add(pl);
                            }
                        }
                        pl.getSettings().incChances();
                        if (pl.getSettings().getChances() < 0) {
                            pl.getSettings().setChances(0);
                        }
                    }
                }
                for (Player pl : playersGetLoot) {
                    if (Misc.random(2) == 1) {
                        done = pl;
                        for (int i = 0; i < 5; i++) {
                            pl.getSettings().decChances();
                        }
                        break;
                    }
                }
            }
        }
        if (clan != null && clan.isLootsharing()) {
            ActionSender.sendMessage(done, "<col=009900>You received: " + amount + " " + ItemDefinition.forId(id).getName());
            for (final Player pl : World.getWorld().getPlayers()) { //TODO Region.getLocalPlayers(location, 16)) {
                if (pl.getLocation().getDistance(location) > 20 || !clan.canShareLoot(pl)) {
                    continue;
                }
                if (pl.getIndex() == done.getIndex()) {
                    continue;
                }
                if (clan.getMembers().contains(pl)) {
                    ActionSender.sendMessage(pl, Misc.formatPlayerNameForDisplay(done.getUsername()) + " received: " + amount + " " + ItemDefinition.forId(id).getName());
                    World.getWorld().submit(new Tick(6) {
                        @Override
                        public void execute() {
                            ActionSender.sendMessage(pl, "Your chance of receiving loot has improved.");
                            this.stop();

                        }
                    });
                }
            }
        }
        return done;
    }


    private int getDropAmount() {
        int dropAmount;
        switch (id) {
        /*    case Nex.DEFAULT_NEX_ID:
            case Nex.MELEE_DEFLECT_NEX:
            case Nex.SOUL_SPLIT_NEX:
            case Nex.WRATH_NEX:
                dropAmount = 3;
                break;*/
            case 1:
                dropAmount = 2;
                break;
            default:
                dropAmount = 1;
                break;
        }
        return dropAmount;
    }

    private int getDeathAnimation() {
        return definition.getDeathAnimation();
    }

    public Location getOriginalLocation() {
        return originalLoc;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setHp(int hp) {
        this.hitpoints = hp;
    }

    public int getHp() {
        return hitpoints;
    }

    public void forceText(String string) {
        this.getMask().setLastForceText(new ForceText(string));
    }

    public int getMaxHp() {
        return definition.getHitpoints();
    }

    public boolean isDead() {
        return isDead;
    }

    public void setDead(boolean dead) {
        this.isDead = dead;
    }

    @Override
    public int getAttackAnimation() {
        return definition.getAttackAnimation();
    }

    @Override
    public int getAttackDelay() {
        return definition.getAttackDelay();
    }

    @Override
    public int getDefenceAnimation() {
        return definition.getDefenceAnimation();
    }

    public NPCDefinition getDefinition() {
        return definition;
    }

    public void setDefinition(NPCDefinition definition) {
        this.definition = definition;
    }

    @Override
    public boolean isNPC() {
        return true;
    }

    @Override
    public NPC getNPC() {
        return this;
    }

    public boolean isDoesWalk() {
        return doesWalk;
    }

    public void setDoesWalk(boolean doesWalk) {
        this.doesWalk = doesWalk;
    }

    public void setFaceDir(int faceDir) {
        this.faceDir = faceDir;
    }

    public boolean isAttackable() {
        return CacheNPCDefinition.forID(getId()).combatLevel > 0;
    }

    public int getFaceDir() {
        return faceDir;
    }

    @Override
    public int getHitPoints() {
        return hitpoints;
    }

    @Override
    public int getMaximumHitPoints() {
        return definition.getHitpoints();
    }

    public boolean isNex() {
        return false;
    }

    public void heal(int amount) {
        int healedAmount = hitpoints + amount;
        if (healedAmount > definition.getHitpoints()) {
            healedAmount = definition.getHitpoints();
        }
        hitpoints = healedAmount;
    }

    public void setUnrespawnable(boolean unrespawnable) {
        this.unrespawnable = unrespawnable;
    }

    public void setAggressiveTicks(int aggressiveTicks) {
        this.aggressiveTicks = aggressiveTicks;
    }

    public int getAggressiveTicks() {
        return aggressiveTicks;
    }

    public Task[] getNPCTasks() {
        return npcTasks;
    }

    public boolean hitsThroughPrayer() {
        return hitsThroughPrayer;
    }

    public void setHitsThroughPrayer(boolean b) {
        this.hitsThroughPrayer = b;
    }

    public void setPrivate(Player player) {
    	setHidden(true);
    	setAttribute("spawnedFor", player);
    }
    
	public boolean isPrivate(Player player) {
		Player p = (Player) getAttribute("spawnedFor");
		return p == player;
	}
    
    public void instantDeath() {
        setHidden(true);
        setDead(true);
        World.getWorld().getNpcs().remove(this);
    }

    public void hunterDeath() {
        setHidden(true);
        setDead(true);
        World.getWorld().submit(new Tick(30 + getDeathTick()) {
            @Override
            public void execute() {
                stop();
                resetCombat();
                setHidden(false);
                teleport(getOriginalLocation());
                setHp(getMaxHp());
                setDead(false);
            }
        });
    }

    /**
     * @return the attackModifier
     */
    public double getAttackModifier() {
        return attackModifier / 100D;
    }

    /**
     * @param drain the drain to decrease with
     */
    public void decreaseAttackModifier(int drain) {
        this.attackModifier -= drain;
    }

    /**
     * @return the strengthModifier
     */
    public double getStrengthModifier() {
        return strengthModifier / 100D;
    }

    /**
     * @param drain the drain to set
     */
    public void decreaseStrengthModifier(int drain) {
        this.strengthModifier -= drain;
    }

    /**
     * @return the defenceModifier
     */
    public double getDefenceModifier() {
        return defenceModifier / 100D;
    }

    /**
     * @param defenceModifier the defenceModifier to set
     */
    public void decreaseDefenceModifier(int drain) {
        this.defenceModifier -= drain;
    }

    /**
     * @return the rangeModifier
     */
    public double getRangeModifier() {
        return rangeModifier / 100D;
    }

    /**
     * @param rangeModifier the rangeModifier to set
     */
    public void decreaseRangeModifier(int drain) {
        this.rangeModifier -= drain;
    }

    /**
     * @return the magicModifier
     */
    public double getMagicModifier() {
        return magicModifier / 100D;
    }

    /**
     * @param magicModifier the magicModifier to set
     */
    public void decreaseMagicModifier(int drain) {
        this.magicModifier -= drain;
    }

	@Override
	public boolean isAttackable(Mob mob) {
		Mob lastAttacker = getCombatExecutor().getLastAttacker();
		if (lastAttacker != null && lastAttacker != mob && (!isMulti() || !mob.isMulti())) {
			if (mob.isPlayer()) {
				mob.getPlayer().sendMessage("That monster is already in combat.");
			}
			return false;
		} else if (mob.getCombatExecutor().getLastAttacker() != null && 
				mob.getCombatExecutor().getLastAttacker() != this && (!isMulti() || !mob.isMulti())) {
			if (mob.isPlayer()) {
				mob.getPlayer().sendMessage("You are already under attack.");
			}
			return false;
		} else if (mob.isPlayer()) {
			//TODO BEtter way of requirements.
			Player player = mob.getPlayer();
			if(player.getSkills().getLevel(Skills.SLAYER) < 85 && getId() == 1615){
				player.sendMessage("You need a slayer level of 85 to attack this creature.");
				return false;
			} else if(player.getSkills().getLevel(Skills.SLAYER) < 75 && getId() == 1610){
				player.sendMessage("You need a slayer level of 75 to attack this creature.");
				return false;
			} else if(player.getSkills().getLevel(Skills.SLAYER) < 80 && getId() == 1613){
				player.sendMessage("You need a slayer level of 80 to attack this creature.");
				return false;
			} else if(player.getSkills().getLevel(Skills.SLAYER) < 78 && getId() == 9172) {
				player.sendMessage("You need a slayer level of 78 to attack this creature.");
				return false;
			} else if(player.getSkills().getLevel(Skills.SLAYER) < 70 && getId() == 1608) {
				player.sendMessage("You need a slayer level of 70 to attack this creature.");
				return false;
			} else if(player.getSkills().getLevel(Skills.SLAYER) < 55 && getId() == 1627) {
				player.sendMessage("You need a slayer level of 55 to attack this creature.");
				return false;
			} else if(player.getSkills().getLevel(Skills.SLAYER) < 52 && getId() == 1637) {
				player.sendMessage("You need a slayer level of 52 to attack this creature.");
				return false;
			} else if(player.getSkills().getLevel(Skills.SLAYER) < 50 && getId() == 1618){
				player.sendMessage("You need a slayer level of 50 to attack this creature.");
				return false;
			} else if(player.getSkills().getLevel(Skills.SLAYER) < 45 && getId() == 1643){
				player.sendMessage("You need a slayer level of 45 to attack this creature.");
				return false;
			} else if(player.getSkills().getLevel(Skills.SLAYER) < 40 && getId() == 1616) {
				player.sendMessage("You need a slayer level of 40 to attack this creature.");
				return false;
			} else if(player.getSkills().getLevel(Skills.SLAYER) < 30 && getId() == 1633) {
				player.sendMessage("You need a slayer level of 30 to attack this creature.");
				return false;
			} else if(player.getSkills().getLevel(Skills.SLAYER) < 25 && getId() == 1620) {
				player.sendMessage("You need a slayer level of 25 to attack this creature.");
				return false;
			}
		}
		return true;
	}
	
	@Override
	public Damage updateHit(Mob source, int hit, CombatType type) {
		if (CombatUtils.usingProtection(this, type)) {
			hit *= source.isPlayer() ? 0.6 : 0;
		}
		if (source.isPlayer() && source.getPlayer().getSlayer() != null && source.getPlayer().getSlayer().getSlayerTask() != null) {
			if (source.getPlayer().getSlayer().getSlayerTask().getName().equalsIgnoreCase(definition.getCacheDefinition().getName())) {
				Item item = source.getPlayer().getEquipment().get(Equipment.SLOT_HAT);
				if (item != null && item.getDefinition().getName().contains("lack mask")) {
					hit *= 1.15;
				}
			}
		}
		Item weapon = source.isPlayer() ? source.getPlayer().getEquipment().get(3) : null;
		if (weapon != null && weapon.getId() == 15403 && getDefinition().getName().contains("agannoth")) {
			hit *= 1.75;
			source.graphics(source.getPlayer().getSettings().getCombatType() == WeaponInterface.TYPE_CRUSH ? 2272 : 2273, 96 << 16);
		}
		return new Damage(hit);
	}

	@Override
	public CombatAction getCombatAction() {
		if (getDefinition().isUsingRange()) {
			return CombatType.RANGE.getCombatAction();
		} else if (getDefinition().isUsingMagic()) {
			return CombatType.MAGIC.getCombatAction();
		}
		return CombatType.MELEE.getCombatAction();
	}

	@Override
	public RangeData getRangeData(Mob victim) {
		RangeData data = new RangeData(false);
		data.setWeaponType(0);
		data.setDamage(Damage.getDamage(this, victim, CombatType.RANGE, RangeFormulae.getDamage(this, victim)));
		int speed = (int) (46 + (getLocation().distance(victim.getLocation()) * 5));
		data.setProjectile(Projectile.create(this, victim, getDefinition().getProjectileId(), 40, 36, 41, speed, 5));
		data.setAnimation(getDefinition().getAttackAnimation());
		data.setGraphics(Graphic.create(getDefinition().getStartGraphics(), 96 << 16));
		return data;
	}
	
	@Override
	public void retaliate(Mob other) {
		if (getCombatExecutor().getVictim() != null || getWalkingQueue().isMoving()) {
			return;
		}
		getCombatExecutor().setVictim(other);
	}

}
