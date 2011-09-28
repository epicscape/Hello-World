package org.dementhium.model.definition;

import org.dementhium.cache.Cache;
import org.dementhium.cache.format.CacheNPCDefinition;
import org.dementhium.model.misc.GodwarsUtils;
import org.dementhium.model.misc.GodwarsUtils.Faction;
import org.dementhium.tools.converters.NPCConverter;
import org.dementhium.tools.converters.NPCDefinitionPacker;
import org.dementhium.util.BufferUtils;
import org.dementhium.util.Misc;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileChannel.MapMode;
import java.util.ArrayList;

/**
 * The NPC's definitions.
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 * @author Emperor
 */
public class NPCDefinition {

    /**
     * The currently loaded NPC definitions.
     */
    private static NPCDefinition[] definitions = new NPCDefinition[Cache.getAmountOfNpcs()];

    /**
     * Awesum voidz of Mystic Flow, ask him wtf this is doing here.
     */
    private static NPCDefinition rareDropTable;

    /**
     * Initializes the NPC definitions.
     *
     * @throws IOException When an I/O error occurs.
     */
    public static void init() throws IOException {
        NPCDefinitionPacker.pack();
        definitions = new NPCDefinition[Cache.getAmountOfNpcs()];
        System.out.println("Loading npc definitions...");
        File file = //new File("./data/npcs/NPCDefinitions.bin");
                new File(new File("./").getAbsolutePath().replace(Misc.isWindows() ? "Dementhium 637" : "Dementhium 637/", "NDE/NPCDefinitions.bin"));
        FileChannel channel = new RandomAccessFile("data/npcs/NPCDefinitions.bin", "r").getChannel();
        ByteBuffer buffer = channel.map(MapMode.READ_ONLY, 0, channel.size());
        NPCDefinition def = null;
        for (int i = 0; i < definitions.length; i++) {
        	if (!buffer.hasRemaining()) return;
            int id = buffer.getShort();
            def = new NPCDefinition(i);
            if (id == -1) {
                definitions[i] = def;
                continue;
            }
            def.combatLevel = buffer.getShort();
            def.examine = BufferUtils.readRS2String(buffer);
            for (int x = 0; x < 14; x++) {
                def.bonuses[x] = buffer.getShort();
            }
            def.lifepoints = buffer.getShort();
            def.respawn = buffer.get();
            def.attackAnimation = buffer.getShort();
            def.defenceAnimation = buffer.getShort();
            def.deathAnimation = buffer.getShort();
            def.strengthLevel = buffer.getShort();
            def.attackLevel = buffer.getShort();
            def.defenceLevel = buffer.getShort();
            def.rangeLevel = buffer.getShort();
            def.magicLevel = buffer.getShort();
            def.attackSpeed = buffer.get();
            def.startGraphics = buffer.getShort();
            def.projectileId = buffer.getShort();
            def.endGraphics = buffer.getShort();
            def.usingMelee = buffer.get() == 1;
            def.usingRange = buffer.get() == 1;
            def.usingMagic = buffer.get() == 1;
            def.aggressive = buffer.get() == 1;
            def.poisonImmune = buffer.get() == 1;
            Faction f = GodwarsUtils.getFaction(def.getId());
            if (f != null) {
                def.setFaction(f);
            }
            definitions[i] = def;
        }
        channel.close();
        System.out.println("Loaded " + NPCDefinition.definitions.length + " npc definitions.");
        ArrayList<NPCDefinition> ef = new ArrayList<NPCDefinition>();
        for (int i=0; i<NPCDefinition.definitions.length; i++)
        	ef.add(NPCDefinition.definitions[i]);
        NPCConverter.main(null, ef);
    }

    /**
     * Grabs an NPC's definition.
     *
     * @param id The npc id.
     * @return The NPCDefinition.
     */
    public static NPCDefinition forId(int id) {
        if (id == -1) {
            if (rareDropTable == null) {
                rareDropTable = new NPCDefinition(id);
                rareDropTable.name = "Rare drop table";
            }
            return rareDropTable;
        }
        if (id > definitions.length) {
            return null;
        }
        NPCDefinition def = definitions[id];
        if (def == null) {
            def = new NPCDefinition(id);
            NPCDefinition.definitions[id] = def;
        }
        return def;
    }

    /**
     * Gets an NPC Definition depending on the name.
     *
     * @param id The name.
     * @return The NPCDefinition Object.
     */
    public static NPCDefinition forName(String id) {
        for (NPCDefinition def : NPCDefinition.definitions) {
            if (def.getName() != null && def.getName().equalsIgnoreCase(id)) {
                return def;
            }
        }
        return null;
    }

    /**
     * The size of the definitions.
     *
     * @return The definitions.
     */
    public static int definitionSize() {
        return definitions.length;
    }

    /**
     * Constructs a new {@code NPCDefinition} {@code Object}.
     *
     * @param id The npc id.
     */
    private NPCDefinition(int id) {
        this.id = (short) id;
        if (id != -1) {
            this.def = CacheNPCDefinition.forID(id);
            this.name = def.name;
        }
        this.bonuses = new short[14];
        this.examine = "It's a " + name + ".";
        this.lifepoints = 100;
    }

    /**
     * The...faction?
     */
    private Faction faction;

    /**
     * Gets the NPC's cache definitions.
     */
    private CacheNPCDefinition def;

    /**
     * The NPCs bonuses.
     */
    private short[] bonuses;

    /**
     * The NPCs name.
     */
    private String name;

    /**
     * The NPCs examine info.
     */
    private String examine;

    /**
     * The NPCs combat level.
     */
    private short combatLevel;

    /**
     * The NPCs lifepoints.
     */
    private short lifepoints;

    /**
     * The NPCs respawn time.
     */
    private byte respawn;

    /**
     * The NPCs attack animation.
     */
    private short attackAnimation;

    /**
     * The NPCs defence animation.
     */
    private short defenceAnimation;

    /**
     * The NPCs death animation.
     */
    private short deathAnimation;

    /**
     * The NPCs strength level.
     */
    private short strengthLevel;

    /**
     * The NPCs attack level.
     */
    private short attackLevel;

    /**
     * The NPCs defence level.
     */
    private short defenceLevel;

    /**
     * The NPCs range level.
     */
    private short rangeLevel;

    /**
     * The NPCs magic level.
     */
    private short magicLevel;

    /**
     * The NPCs attack speed.
     */
    private byte attackSpeed;

    /**
     * The start graphics (Magic-Range)
     */
    private short startGraphics;

    /**
     * The projectile graphics id. (Magic-Range)
     */
    private short projectileId;

    /**
     * The end graphics (Magic-Range)
     */
    private short endGraphics;

    /**
     * If the NPC is using melee.
     */
    private boolean usingMelee;

    /**
     * If the NPC is using range.
     */
    private boolean usingRange;

    /**
     * If the NPC is using magic.
     */
    private boolean usingMagic;

    /**
     * If the NPC is immune to poison.
     */
    private boolean poisonImmune;

    /**
     * If the NPC is aggressive.
     */
    private boolean aggressive;

    /**
     * The npc's id.
     */
    private short id;

    public int getAttackLevel() {
        return attackLevel;
    }

    public int getDefenceLevel() {
        return defenceLevel;
    }

    public int getRangeLevel() {
        return rangeLevel;
    }

    public int getId() {
        return id;
    }

    public int getAttackAnimation() {
        return attackAnimation;
    }

    public int getDefenceAnimation() {
        return defenceAnimation;
    }

    public int getAttackDelay() {
        return attackSpeed;
    }

    public int getHitpoints() {
        return lifepoints;
    }

    public int getDeathAnimation() {
        return deathAnimation;
    }

    public String getName() {
        return name;
    }

    public int getProjectileId() {
        return projectileId;
    }

    public boolean isAggressive() {
        return aggressive;
    }

    public int getDefenceBonus(int type) {
        return bonuses[type + 5];
    }

    public int getMagicLevel() {
        return magicLevel;
    }

    /**
     * @return the examine
     */
    public String getExamine() {
        return examine;
    }

    /**
     * @return the combatLevel
     */
    public short getCombatLevel() {
        return combatLevel;
    }

    /**
     * @return the respawn
     */
    public byte getRespawn() {
        return respawn;
    }

    /**
     * @return the strengthLevel
     */
    public short getStrengthLevel() {
        return strengthLevel;
    }

    /**
     * @return the startGraphics
     */
    public short getStartGraphics() {
        return startGraphics;
    }

    /**
     * @return the endGraphics
     */
    public short getEndGraphics() {
        return endGraphics;
    }

    /**
     * @return the usingMelee
     */
    public boolean isUsingMelee() {
        return usingMelee;
    }

    /**
     * @return the usingRange
     */
    public boolean isUsingRange() {
        return usingRange;
    }

    /**
     * @return the usingMagic
     */
    public boolean isUsingMagic() {
        return usingMagic;
    }

    /**
     * @return the poisonImmune
     */
    public boolean isPoisonImmune() {
        return poisonImmune;
    }

    /**
     * @return the def
     */
    public CacheNPCDefinition getCacheDefinition() {
        if (def == null) {
            def = CacheNPCDefinition.forID(id);
        }
        return def;
    }

    public short[] getBonuses() {
        return bonuses;
    }

    /**
     * @param faction the faction to set
     */
    public void setFaction(Faction faction) {
        this.faction = faction;
    }

    /**
     * @return the faction
     */
    public Faction getFaction() {
        return faction;
    }

}
