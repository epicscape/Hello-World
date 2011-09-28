package org.dementhium.model;

import java.io.File;
import java.util.Map;
import java.util.HashMap;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 *
 * @author 'Mystic Flow <Steven@rune-server.org>
 * @author Steve <golden_32@live.com>
 * @author Lumby <lumbyjr@hotmail.com>
 * @author Emperor <black_dragon_686@hotmail.com>
 */
public class SpecialAttackContainer {
	
	/**
	 * The mapping.
	 */
	private static final Map<Integer, SpecialAttack> SPECIAL_ATTACKS = new HashMap<Integer, SpecialAttack>(5);
	
	/**
	 * Initializes the SpecialAttackManager.
	 * @return {@code True} if succesfull, {@code false} if not.
	 */
	public static boolean initialize() {
		Document doc;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File("./data/xml/SpecialAttacksPlugIn.xml"));
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		int count = 0;
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		System.out.println("Loading special attacks...");
		for (short i = 1; i < nodeList.getLength(); i += 2) {
			Node n = nodeList.item(i);
			if (n != null) {
				if (n.getNodeName().equalsIgnoreCase("SpecialAttack")) {
					NodeList list = n.getChildNodes();
					SpecialAttack specialAttack = null;
					for (int a = 1; a < list.getLength(); a += 2) {
						Node node = list.item(a);
						if (node.getNodeName().equalsIgnoreCase("specialAttackHandler")) {
							try {
								specialAttack = (SpecialAttack) Class.forName(node.getTextContent()).newInstance();
							} catch (Throwable e) {
							}
						} else if (node.getNodeName().equalsIgnoreCase("weaponId")) {
							if (specialAttack != null)
							SPECIAL_ATTACKS.put(Integer.parseInt(node.getTextContent()), specialAttack);
						}
					}
					count++;
				}
			}
		}
		System.out.println("Loaded " + count + " special attacks.");
		return true;
	}
	
	/**
	 * Gets a {@code SpecialAttack} instance from the mapping.
	 * @param id The item id used.
	 * @return The special attack instance.
	 */
	public static final SpecialAttack get(int id) {
		return SPECIAL_ATTACKS.get(id);
	}
}