/*
 * Class LoadNPCLists
 *
 * Version 1.0
 *
 * Monday, August 18, 2008
 *
 * Created by Palidino76
 */

package org.dementhium.tools.converters;

import java.io.*;
@SuppressWarnings("unused")
public class LoadNPCLists {

    public static void main(String... args) {
        new LoadNPCLists();
    }

    public LoadNPCLists() {
        //loadNPCList();
        try {
            loadNPCs();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private boolean loadNPCs() throws IOException {
		String line = "", token = "", token2 = "", token2_2 = "", token3[] = new String[10];
        BufferedReader cfgFile = null;
        File file = new File("data/npcspawns.txt");
        if (!file.exists()) {
            file.createNewFile();
        }
        BufferedWriter bf = new BufferedWriter(new FileWriter(file, true));
        //bf.write("npc\t=\t"+id+"\t"+(localX + absX) + "\t"+(localY + absY)+"\t"+z);
        try {
            cfgFile = new BufferedReader(new FileReader("data/xfer.txt"));
            line = cfgFile.readLine().trim();
        } catch (Exception e) {
            System.out.println("Error");
            line = token = token2 = token2_2 = null;
            token3 = null;
            return false;
        }
        while (line != null) {
            int index = line.indexOf("=");
            if (index > -1) {
                token = line.substring(0, index - 1).trim();
                token2 = line.substring(index + 1).trim();
                token2_2 = token2.replaceAll(" ", "\t");
                token3 = token2_2.split("\t");
                String[] split = line.substring(index).split(" ");
                if (true) {
                    try {
                        System.out.println("Hi");
                        bf.write(Integer.parseInt(split[1]) + " " + Integer.parseInt(split[2]) + " " + Integer.parseInt(split[3]) + " " + Integer.parseInt(split[4]) + " 0" + " true");
                        bf.newLine();
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    // Engine.newNPC(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]), Integer.parseInt(token3[3]),
                    //Integer.parseInt(token3[4]), Integer.parseInt(token3[5]), Integer.parseInt(token3[6]), Integer.parseInt(token3[7]), true, 0);
                }
            } else if (line.equals("[ENDOFSPAWNLIST]")) {
                try {
                    bf.flush();
                    cfgFile.close();
                } catch (Exception ioe) {
                }
                cfgFile = null;
                line = token = token2 = token2_2 = null;
                token3 = null;
                return true;
            }
            try {
                line = cfgFile.readLine();
            } catch (Exception ioe) {
                line = null;
                line = token = token2 = token2_2 = null;
                token3 = null;
                cfgFile = null;
                return false;
            }
        }
        try {
            cfgFile.close();
            cfgFile = null;
        } catch (Exception ioe) {
        }
        return false;
    }

    private boolean loadNPCList() {
        String line = "", token = "", token2 = "", token2_2 = "", token3[] = new String[10];
        BufferedReader list = null;
        try {
            list = new BufferedReader(new FileReader("./data/npcs/npclist.cfg"));
            line = list.readLine().trim();
        } catch (Exception e) {
            //  Misc.println("Error loading NPC Lists.");
            return false;
        }
        while (line != null) {
            int spot = line.indexOf("=");
            if (spot > -1) {
                token = line.substring(0, spot).trim();
                token2 = line.substring(spot + 1).trim();
                token2_2 = token2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token2_2 = token2_2.replaceAll("\t\t", "\t");
                token3 = token2_2.split("\t");
                if (token.equals("npc")) {
                    // newNPCList(Integer.parseInt(token3[0]), Integer.parseInt(token3[1]), Integer.parseInt(token3[2]),
                    //   Integer.parseInt(token3[3]), Integer.parseInt(token3[4]), Integer.parseInt(token3[5]),
                    //  Integer.parseInt(token3[6]), Integer.parseInt(token3[7]), Integer.parseInt(token3[8]), token3[9].replaceAll("_", " "), token3[10].replaceAll("_", " "));
                }
            } else {
                if (line.equals("[ENDOFNPCLIST]")) {
                    try {
                        list.close();
                    } catch (Exception ioexception) {
                    }
                    list = null;
                    return true;
                }
            }
            try {
                line = list.readLine().trim();
            } catch (Exception ioexception1) {
                try {
                    list.close();
                } catch (Exception ioexception) {
                }
                list = null;
                return true;
            }
        }
        return false;
    }

}