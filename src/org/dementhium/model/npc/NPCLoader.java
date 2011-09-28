package org.dementhium.model.npc;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dementhium.io.FileUtilities;
import org.dementhium.model.Location;
import org.dementhium.model.World;
import org.dementhium.model.definition.NPCDefinition;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Loads all NPCs.
 * @author Emperor <NPC class loading>
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class NPCLoader {

	/**
	 * The NPC classes.
	 */
	private static final Map<Integer, Class<?>> CUSTOM_NPCS = new HashMap<Integer, Class<?>>();


	public static void load() throws IOException, InstantiationException, IllegalAccessException, ClassNotFoundException, IllegalArgumentException, SecurityException, InvocationTargetException, NoSuchMethodException {
		loadCustomizations();
		System.out.println("Loading default npc spawns...");
		int size = 0;
		boolean ignore = false;
		for (String string : FileUtilities.readFile("data/npcs/npcspawns.txt")) {
			if (string.startsWith("//") || string.equals("")) {
				continue;
			}
			if (string.contains("/*")) {
				ignore = true;
				continue;
			}
			if (ignore) {
				if (string.contains("*/")) {
					ignore = false;
				}
				continue;
			}
			String[] spawn = string.split(" ");
			int id = Integer.parseInt(spawn[0]), x = Integer.parseInt(spawn[1]), y = Integer.parseInt(spawn[2]), z = Integer.parseInt(spawn[3]), faceDir = Integer.parseInt(spawn[4]);
			if (id > NPCDefinition.definitionSize()) {
				continue;
			}
			boolean doesWalk = Boolean.parseBoolean(spawn[5]);
			NPC npc = null;
			Class<?> npcHandler = CUSTOM_NPCS.get(id);
			if (npcHandler == null) {
				npc = new NPC(id);
			} else {
				npc = (NPC) npcHandler.getConstructor(int.class).newInstance(id);
			}
			/*
			if (spawn.length == 6) {
				npc = new NPC(id);
			} else if (spawn.length == 7) {
				npc = (NPC) Class.forName("org.dementhium.model.npc.impl." + spawn[6]).getConstructor(int.class).newInstance(id);
			}*/
			if (npc != null) {
				Location spawnLoc = Location.locate(x, y, z);
				npc.setLocation(spawnLoc);
				npc.setOriginalLocation(spawnLoc);
				npc.setDoesWalk(doesWalk);
				npc.setFaceDir(faceDir);
				npc.loadEntityVariables();
				World.getWorld().getNpcs().add(npc);
				size++;
				if (npc.getDefinition().getName().contains("banker")) {
					npc.setDoesWalk(false);
				}
			}
		}
		System.out.println("Loaded " + size + " default npc spawns.");
	}
	
	/**
	 * Loads custom NPC classes.
	 * @return {@code True} if succesful, {@code false} if not.
	 */
	private static boolean loadCustomizations() {
		Document doc;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File("./data/xml/custom_npcs.xml"));
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		System.out.println("Loading customized NPC classes...");
		for (short i = 1; i < nodeList.getLength(); i += 2) {
			Node n = nodeList.item(i);
			if (n != null) {
				if (n.getNodeName().equalsIgnoreCase("npc")) {
					NodeList list = n.getChildNodes();
					Class<?> npcObject = null;
					for (int a = 1; a < list.getLength(); a += 2) {
						Node node = list.item(a);
						if (node.getNodeName().equalsIgnoreCase("handler")) {
							try {
								npcObject = Class.forName(node.getTextContent());
							} catch (Throwable e) {
								e.printStackTrace();
								break;
							}
						} else if (node.getNodeName().equalsIgnoreCase("id")) {
							CUSTOM_NPCS.put(Integer.parseInt(node.getTextContent()), npcObject);
						}
					}
				}
			}
		}
		System.out.println("Loaded " + CUSTOM_NPCS.size() + " customized NPCs.");
		return false;
	}

	/**
	 * Gets the NPC object.
	 * @param npcId The npc id.
	 * @return The NPC object.
	 */
	public static NPC getNPC(int npcId) {
		Class<?> npcHandler = CUSTOM_NPCS.get(npcId);
		if (npcHandler != null) {
			try {
				return (NPC) npcHandler.getConstructor(int.class).newInstance(npcId);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}		
		return new NPC(npcId);
	}

}
