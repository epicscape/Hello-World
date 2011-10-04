package org.dementhium.tools.converters;

import org.dementhium.cache.Cache;
import org.dementhium.io.XMLHandler;
import org.dementhium.model.definition.NPCDefinition;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 * @author Steve <golden_32@live.com>
 */
public class NPCConverter {

    public class NPCDefintion {
        /*<id>4393</id>
          <hitpoints>180</hitpoints>
          <maximumHit>60</maximumHit>
          <attackAnimation>5578</attackAnimation>
          <defenceAnimation>5579</defenceAnimation>
          <deathAnimation>5580</deathAnimation>
          <attackLevel>35</attackLevel>
          <defenceLevel>28</defenceLevel>
          <meleeAttack>30</meleeAttack>
          <stabDefence>25</stabDefence>
          <slashDefence>15</slashDefence>
          <crushDefence>28</crushDefence>
          <rangeDefence>28</rangeDefence>
          <magicDefence>28</magicDefence>*/
        public int id, hitpoints, maximumHit, attackSpeed, attackAnimation, defenceAnimation, strengthLevel, deathAnimation, attackLevel, defenceLevel, meleeAttack, stabDefence, slashDefence, crushDefence, rangeDefence, magicDefence;
        public int rangeLevel;
        public int magicLevel;
    }

    public static ArrayList<NPCDefinition> defsToConvert = new ArrayList<NPCDefinition>();

    public static void main(String[] args, ArrayList<NPCDefinition> defs) {
        Cache.init();
		defsToConvert = defs;//XMLHandler.fromXML("./data/npcs/npcDefinitions.xml");
		//convert();

    }

    private static void convert() {
        String directory = new File("./").getAbsolutePath() + "/data/npcs/defs";
        for (NPCDefinition def : defsToConvert) {
            File defs = new File(directory + "/NPCDefinition" + def.getId() + ".xml");
            if (def == null) continue;
            System.out.println("Converting "+def.getId());
            if (!defs.exists()) {
                try {
                    BufferedWriter writer = new BufferedWriter(new FileWriter(defs));
                    writer.write("<NpcDefinition>");
                    writer.write("		<CombatLevel>1</CombatLevel> \n"
                            + "<Examine>Examine not added</Examine> \n"
                            + "<Bonus0>" + def.getBonuses()[0] + "</Bonus0> \n"
                            + "<Bonus1>" + def.getBonuses()[1] + "</Bonus1> \n"
                            + "<Bonus2>" + def.getBonuses()[2] + "</Bonus2> \n"
                            + "<Bonus3>" + def.getBonuses()[3] + "</Bonus3> \n"
                            + "<Bonus4>" + def.getBonuses()[4] + "</Bonus4> \n"
                            + "<Bonus5>" + def.getBonuses()[5] + "</Bonus5> \n"
                            + "<Bonus6>" + def.getBonuses()[6] + "</Bonus6> \n"
                            + "<Bonus7>" + def.getBonuses()[7] + "</Bonus7> \n"
                            + "<Bonus8>" + def.getBonuses()[8] + "</Bonus8> \n"
                            + "<Bonus9>" + def.getBonuses()[9] + "</Bonus9> \n"
                            + "<Bonus10>" + def.getBonuses()[10] + "</Bonus10> \n"
                            + "<Bonus11>" + def.getBonuses()[11] + "</Bonus11> \n"
                            + "<Bonus12>" + def.getBonuses()[12] + "</Bonus12> \n"
                            + "<Bonus13>" + def.getBonuses()[13] + "</Bonus13> \n"
                            + "<LifePoints>" + def.getHitpoints() + "</LifePoints> \n"
                            + "<Respawn>30</Respawn> \n"
                            + "<AttackAnimation>" + def.getAttackAnimation() + "</AttackAnimation> \n"
                            + "<DefenceAnimation>" + def.getDefenceAnimation() + "</DefenceAnimation> \n"
                            + " <DeathAnimation>" + def.getDeathAnimation() + "</DeathAnimation> \n"
                            + "<StrengthLevel>" + def.getStrengthLevel() + "</StrengthLevel> \n"
                            + "<AttackLevel>" + def.getAttackLevel() + "</AttackLevel> \n"
                            + "<DefenceLevel>" + def.getDefenceLevel() + "</DefenceLevel> \n"
                            + "<RangeLevel>" + def.getRangeLevel() + "</RangeLevel> \n"
                            + "<MagicLevel>" + def.getMagicLevel() + "</MagicLevel> \n"
                            + "<AttackSpeed>"+ def.getAttackDelay() +"</AttackSpeed> \n"
                            + "<StartGraphics>"+ def.getStartGraphics() +"</StartGraphics> \n"
                            + "<ProjectileId>"+ def.getProjectileId() +"</ProjectileId> \n"
                            + "<EndGraphics>"+ def.getEndGraphics() +"</EndGraphics> \n"
                            + "<UsingMelee>"+ def.isUsingMelee() +"</UsingMelee> \n"
                            + "<UsingRange>"+ def.isUsingRange() +"</UsingRange> \n"
                            + "<UsingMagic>"+ def.isUsingMagic() +"</UsingMagic> \n"
                            + "<Aggressive>"+ def.isAggressive() +"</Aggressive> \n"
                            + "<PoisonImmune>"+ def.isPoisonImmune() +"</PoisonImmune> \n");
                    writer.write("</NpcDefinition>");
                    writer.flush();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
