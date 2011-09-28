package org.dementhium.model.combat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dementhium.model.player.Player;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Holds all the combat spells.
 * @author Emperor
 *
 */
public class SpellContainer {

	/**
	 * The modern spells.
	 */
	private static final Map<Integer, MagicSpell> MODERN = new HashMap<Integer, MagicSpell>(92);

	/**
	 * The ancient spells.
	 */
	private static Map<Integer, MagicSpell> ANCIENT = new HashMap<Integer, MagicSpell>(40);

	/**
	 * Initializes the spells.
	 */
	public static void initialize() {
		Document doc;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File("data/xml/MagicSpells.xml"));
		} catch (Throwable e) {
			e.printStackTrace();
			return;
		}
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		int count = 0;
		System.out.println("Loading magic spells...");
		for (short i = 1; i < nodeList.getLength(); i += 2) {
			Node n = nodeList.item(i);
			if (n != null) {
				if (n.getNodeName().equalsIgnoreCase("magicSpell")) {
					NodeList list = n.getChildNodes();
					MagicSpell magicSpell = null;
					int id = 0;
					for (int a = 1; a < list.getLength(); a += 2) {
						try {
							Node node = list.item(a);
							if (node.getNodeName().equalsIgnoreCase("spellHandler")) {
								try {
									magicSpell = (MagicSpell) Class.forName(node.getTextContent()).newInstance();
								} catch (Throwable e) {
									continue;
								}
							} else if (node.getNodeName().equalsIgnoreCase("spellId")) {
								id = Integer.parseInt(node.getTextContent());
							} else if (node.getNodeName().equalsIgnoreCase("spellType")) {
								if (node.getTextContent().equalsIgnoreCase("ANCIENT")) {
									ANCIENT.put(id, magicSpell);
								} else if (node.getTextContent().equalsIgnoreCase("MODERN")) {
									MODERN.put(id, magicSpell);
								}
							}
						} catch (Throwable t) {
							continue;
						}
					}
					count++;
				}
			}
		}
		System.out.println("Loaded " + count + " magic spell handlers.");
	}

	/**
	 * Grabs a spell from the spell-container.<br>
	 * @param source The player casting the spell.
	 * @param spellId The spell id.
	 * @return The magic spell instance.
	 */
	public static MagicSpell grabSpell(Player source, int spellId) {
		try {
			switch (source.getSettings().getSpellBook()) {
			case 192:
				return MODERN.get(spellId);
			case 193:
				return ANCIENT.get(spellId);
			default:
				return null;
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return null;
		}
	}
}