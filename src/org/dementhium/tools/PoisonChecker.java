package org.dementhium.tools;

import org.dementhium.model.definition.ItemDefinition;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Steve <golden_32@live.com>
 */
public class PoisonChecker {

    public static void main(String[] args) {
        BufferedWriter writer;
        try {
            ItemDefinition.init();
            writer = new BufferedWriter(new FileWriter("data/item/poisoningitems.txt", true));
            for (ItemDefinition itemdef : ItemDefinition.getDefinitions()) {
                if (itemdef.getName() != null && !itemdef.getName().equals("")) {
                    int amt = 0, id = itemdef.getId();
                    boolean isRanged = itemdef.getName().contains("arrows") || itemdef.getName().contains("bolts") || itemdef.getName().contains("knives") || itemdef.getName().contains("javelin");
                    if (itemdef.getName().contains("(p)")) {
                        amt = 48;
                    } else if (itemdef.getName().contains("(p+)")) {
                        amt = 58;
                    } else if (itemdef.getName().contains("(p++)")) {
                        amt = 68;
                    } else if (itemdef.getName().contains("(kp)")) {
                        amt = 68;
                    } else if (itemdef.getName().contains("(kp)")) {
                        isRanged = false;
                        amt = 58;
                    }
                    if (isRanged) {
                        amt -= 20;
                    }
                    if (amt > 0 && id > 0) {
                        System.out.println(id + ":" + amt);
                        writer.write(id + ":" + amt);
                        writer.newLine();
                    }
                }
            }
            writer.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

}
