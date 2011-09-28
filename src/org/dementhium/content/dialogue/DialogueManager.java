package org.dementhium.content.dialogue;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dementhium.model.player.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * The dialogue manager, holds all dialogue data, methods to send the dialogues, etc.
 * @author Emperor
 *
 */
public class DialogueManager {

	/**
	 * Holds all the dialogues.
	 */
	private static final Map<Integer, Dialogue> DIALOGUES = new HashMap<Integer, Dialogue>();
	
	/**
	 * The mapping holding the start dialogue id for every NPC id.
	 */
	private static final Map<Integer, Integer> NPC_DIALOGUES = new HashMap<Integer, Integer>();
 
	/**
     * The document builder instance.
     */
    private static DocumentBuilder builder;

    /**
     * The document builder factory instance.
     */
    private static final DocumentBuilderFactory FACTORY = DocumentBuilderFactory
            .newInstance();
    
	/**
	 * Initializes all dialogues.
	 * @return {@code True} if succeeded, {@code false} if not.
	 */
	public static boolean init() {
		DIALOGUES.clear();
		NPC_DIALOGUES.clear();
		Document doc;
        try {
    		builder = FACTORY.newDocumentBuilder();
			doc = builder.parse(new File("./data/xml/dialogue/dialogues.xml"));
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		int count = 0;
		System.out.println("Loading dialogues...");
		for (int i = 1; i < nodeList.getLength(); i += 2) {
			Node n = nodeList.item(i);
			if (n != null) {
				if (n.getNodeName().equalsIgnoreCase("dialogue")) {
					NodeList list = n.getChildNodes();
					Dialogue dialogue = new Dialogue();
					int id = -2;
					for (int a = 1; a < list.getLength(); a += 2) {
						Node node = list.item(a);
						if (node.getNodeName().equals("id")) {
							id = Integer.parseInt(node.getTextContent());
						} else if (node.getNodeName().equals("speaker")) {
							dialogue.setSpeaker(Integer.parseInt(node.getTextContent()));
						} else if (node.getNodeName().equals("anim")) {
							dialogue.setAnimationId(Integer.parseInt(node.getTextContent()));
						} else if (node.getNodeName().equals("type")) {
							dialogue.setType(Enum.valueOf(DialogueType.class, node.getTextContent()));
						} else if (node.getNodeName().equals("item")) {
							dialogue.getItems().add(Integer.parseInt(node.getTextContent()));
						} else if (node.getNodeName().equals("req")) {
							NodeList reqNodes = node.getChildNodes();
							Requirement.Type req = null;
							List<Object> args = new ArrayList<Object>();//Increase when required.
							for (int j = 1; j < reqNodes.getLength(); j += 2) {
								Node reqNode = reqNodes.item(j);
								if (reqNode.getNodeName().equals("type")) {
									req = Enum.valueOf(Requirement.Type.class, reqNode.getTextContent());
								} else if (reqNode.getNodeName().equals("arg")) {
									args.add(Integer.parseInt(reqNode.getTextContent()));
								} else if (reqNode.getNodeName().equals("action")) {
									args.add(reqNode.getTextContent());
								}
							}
							if (req != null) {
								dialogue.getRequirements().add(Requirement.create(req, args.toArray()));
							}
						} else if (node.getNodeName().equals("message")) {
							dialogue.getMessage().add(node.getTextContent());
						} else if (node.getNodeName().equals("fail")) {
							dialogue.getFailDialogueIds().add(Integer.parseInt(node.getTextContent()));
						} else if (node.getNodeName().equals("option")) {
							NodeList optNodes = node.getChildNodes();
							OptionAction.ActionType act = null;
							List<Object> args = new ArrayList<Object>();//Increase when required.
							for (int j = 1; j < optNodes.getLength(); j += 2) {
								Node reqNode = optNodes.item(j);
								if (reqNode.getNodeName().equals("type")) {
									act = Enum.valueOf(OptionAction.ActionType.class, reqNode.getTextContent());
								} else if (reqNode.getNodeName().equals("arg")) {
									args.add(Integer.parseInt(reqNode.getTextContent()));
								} else if (reqNode.getNodeName().equals("action") || reqNode.getNodeName().equals("string")) {
									args.add(reqNode.getTextContent());
								}
							}
							if (act != null) {
								dialogue.getActions().add(OptionAction.create(act, args.toArray()));
							}
						}
					}
					if (DIALOGUES.containsKey(id)) {
						throw new IllegalStateException("Duplicate dialogue loaded; id " + id + ".");
					}
					DIALOGUES.put(id, dialogue);
					count++;
				}
			}
		}
		System.out.println("Loaded " + count + " dialogues.");
		try {
    		builder = FACTORY.newDocumentBuilder();
			doc = builder.parse(new File("./data/xml/dialogue/npc_dialogue_ids.xml"));
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		nodeList = doc.getDocumentElement().getChildNodes();
		count = 0;
		System.out.println("Loading NPC dialogue ids...");
		for (int i = 1; i < nodeList.getLength(); i += 2) {
			Node n = nodeList.item(i);
			if (n != null) {
				if (n.getNodeName().equalsIgnoreCase("npc")) {
					NodeList list = n.getChildNodes();
					int id = -2;
					for (int a = 1; a < list.getLength(); a += 2) {
						Node node = list.item(a);
						if (node.getNodeName().equals("id")) {
							id = Integer.parseInt(node.getTextContent());
						} else if (node.getNodeName().equals("dialogueId")) {
							NPC_DIALOGUES.put(id, Integer.parseInt(node.getTextContent()));
						}
					}
					count++;
				}
			}
		}
		System.out.println("Loaded " + count + " NPC dialogue ids.");
		return true;
	}
	
	/**
	 * Gets a dialogue by dialogue id.
	 * @param dialogueId The dialogue id.
	 * @return The {@code Dialogue} instance,
	 * 			or {@code null} if the specified value for the key didn't exist.
	 */
	public static Dialogue get(int dialogueId) {
		return DIALOGUES.get(dialogueId);
	}
	
	/**
	 * Gets the first dialogue the specified NPC sends.
	 * @param npcId The npc id.
	 * @return The {@code Dialogue} instance,
	 * 			or {@code null} if the NPC doesn't have any dialogues.
	 */
	public static Dialogue getForNPC(int npcId) {
		Integer dialogueId = NPC_DIALOGUES.get(npcId);
		if (dialogueId != null) {
			return get(dialogueId);
		}
		return null;
	}

	/**
	 * Sends a dialogue.
	 * @param player The player.
	 * @param dialogueId The dialogue id to send.
	 */
	public static void sendDialogue(Player player, int dialogueId) {
		Dialogue dialogue = get(dialogueId);
		if (dialogue == null) {
			return;
		}
		dialogue.send(player);
	}
}