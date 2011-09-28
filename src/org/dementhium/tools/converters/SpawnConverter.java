package org.dementhium.tools.converters;

import org.dementhium.io.FileUtilities;
import org.dementhium.util.Misc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author Steve <golden_32@live.com>
 */
public class SpawnConverter {
    //spawn	=
    public static void main(String[] args) throws IOException {
        BufferedWriter writer = new BufferedWriter(new FileWriter("data/npcs/npcspawns.txt", true));
        for (String string : FileUtilities.readFile("data/npcs/godwarsstuff")) {
            if (string.startsWith("spawn")) {
                string = string.replace("spawn	=	", "");
                String[] s = string.split("	"); //7
                writer.newLine();
                writer.write(s[0] + " " + s[1] + " " + s[2] + " " + s[3] + " " + Misc.random(7) + " true");
            }
        }
        writer.close();
    }
}
