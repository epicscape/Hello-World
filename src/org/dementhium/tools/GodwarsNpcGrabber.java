package org.dementhium.tools;

import org.dementhium.model.definition.ItemDefinition;

import java.io.IOException;

/**
 * @author Steve <golden_32@live.com>
 */
public class GodwarsNpcGrabber {
    static String[] items = new String[]{"Armadyl helmet", "Armadyl mitre", "Armadyl full helm", "Armadyl coif", "Torva full helm", "Pernix cowl", "Virtus mask",
            "Armadyl cloak",
            "Armadyl stole", "Armadyl pendant",
            "Armadyl godsword", "Armadyl crozier",
            "Armadyl chestplate", "Armadyl robe top", "Armadyl body", "Armadyl platebody", "Torva platebody", "Pernix body", "Virtus robe top",
            "Book of law", "Armadyl kiteshield",
            "Armadyl plateskirt", "Armadyl chaps", "Armadyl robe legs", "Armadyl platelegs", "Armadyl Chainskirt", "Torva platelegs", "Pernix chaps", "Virtus robe legs",
            "Armadyl vambraces"};

    public static void main(String[] args) throws IOException {
        ItemDefinition.init();
        for (String s : items) {
            if (ItemDefinition.forName(s) != null)
                System.out.print(ItemDefinition.forName(s).getId() + ", ");
            else
                System.out.print(s);
        }
        /*
           * URL url = new URL("http://runescape.wikia.com/wiki/Godwars_dungeon");
           * URLConnection curl = url.openConnection(); BufferedReader in = new
           * BufferedReader(new InputStreamReader(curl.getInputStream())); String
           * s; String varName = null; boolean addNpcs = false; while ((s =
           * in.readLine()) != null) { if
           * (s.contains("<h3><span class=\"mw-headline\" id=\"")) { varName =
           * s.substring(34, s.indexOf("\"", 34)); for (String str : gwds) { if
           * (str.equalsIgnoreCase(varName)) { addNpcs = true; } } } else if
           * (addNpcs) { ArrayList<String> npcsVals = new ArrayList<String>(20);
           * String npcs[] = s.split(" "); for (String npc : npcs) { if
           * (npc.contains("href=\"/wiki/") && !npc.contains("File:")) {
           * npcsVals.add(npc.substring(12, npc.indexOf("\"", 12)).replace("_",
           * " ").replace(" (monster)", "")); } }
           * System.out.print("public static final int[] "
           * +varName.toLowerCase()+"Ids = new int[]{"); for (String y : npcsVals)
           * { System.out.print(NPCDefinition.forName(y).getId() + ", "); }
           * System.out.print("};"); System.out.println(); addNpcs = false; } }
           */
    }
}
