package org.dementhium.model.combat;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.dementhium.model.mask.Graphic;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

/**
 * Represents range ammunition types.
 * @author Emperor
 *
 */
public final class Ammunition {

	/**
	 * The ammunition mapping.
	 */
	private static final Map<Integer, Ammunition> AMMUNITION = new HashMap<Integer, Ammunition>();
	
	/**
	 * The ammunition item id.
	 */
	private final int itemId;
	
	/**
	 * The start graphics.
	 */
	private final Graphic startGraphics;
	
	/**
	 * The start graphics when using Dark bow.
	 */
	private final Graphic darkBowGraphics;
	
	/**
	 * The projectile GFX id.
	 */
	private final int projectileId;
		
	/**
	 * The poison damage.
	 */
	private final int poisonDamage;
	
	/**
	 * Constructs a new {@code Ammunition} object.
	 * @param itemId The item id.
	 * @param startGraphics The start graphics.
	 * @param darkBowGraphics The dark bow start graphics.
	 * @param projectileId The projectile GFX id.
	 * @param poisonDamage The poison damage the ammunition can do.
	 */
	private Ammunition(int itemId, Graphic startGraphics, Graphic darkBowGraphics, int projectileId, int poisonDamage) {
		this.itemId = itemId;
		this.startGraphics = startGraphics;
		this.darkBowGraphics = darkBowGraphics;
		this.projectileId = projectileId;
		this.poisonDamage = poisonDamage;
	}
	
	/**
	 * Loads all the {@code Ammunition} info to the mapping.
	 * @return {@code True}.
	 */
	public static final boolean initialize() {
		Document doc;
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = factory.newDocumentBuilder();
			doc = builder.parse(new File("./data/xml/AmmunitionData.xml"));
		} catch (Throwable e) {
			e.printStackTrace();
			return false;
		}
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		System.out.println("Loading range ammunition info...");
		for (short i = 1; i < nodeList.getLength(); i += 2) {
			Node n = nodeList.item(i);
			if (n != null) {
				if (n.getNodeName().equalsIgnoreCase("Ammunition")) {
					NodeList list = n.getChildNodes();
					int itemId = 0;
					int graphicsId = 0;
					Graphic startGraphics = null;
					Graphic darkBowGraphics = null;
					int projectileId = 0;
					for (int a = 1; a < list.getLength(); a += 2) {
						Node node = list.item(a);
						if (node.getNodeName().equalsIgnoreCase("itemId")) {
							itemId = Integer.parseInt(node.getTextContent());
						} else if (node.getNodeName().equalsIgnoreCase("startGraphicsId")) {
							graphicsId = Integer.parseInt(node.getTextContent());
						} else if (node.getNodeName().equalsIgnoreCase("startGraphicsHeight")) {
							startGraphics = new Graphic(graphicsId, Integer.parseInt(node.getTextContent()) << 16, 0);
						} else if (node.getNodeName().equalsIgnoreCase("darkBowGraphicsId")) {
							graphicsId = Integer.parseInt(node.getTextContent());
						} else if (node.getNodeName().equalsIgnoreCase("darkBowGraphicsHeight")) {
							darkBowGraphics = new Graphic(graphicsId, Integer.parseInt(node.getTextContent()) << 16, 0);
						} else if (node.getNodeName().equalsIgnoreCase("projectileId")) {
							projectileId = Integer.parseInt(node.getTextContent());
						} else if (node.getNodeName().equalsIgnoreCase("poisonDamage")) {
							AMMUNITION.put(itemId, new Ammunition(itemId, startGraphics, darkBowGraphics, projectileId, Integer.parseInt(node.getTextContent())));
						}
					}
				}
			}
		}
		System.out.println("Loaded " + AMMUNITION.size() + " ammunition definitions.");
		return true;
	}
	
	/**
	 * Gets an ammunition object from the mapping.
	 * @param id The ammo id.
	 * @return The ammunition object.
	 */
	public static final Ammunition get(int id) {
		return AMMUNITION.get(id);
	}

	/**
	 * @return the itemId
	 */
	public int getItemId() {
		return itemId;
	}

	/**
	 * @return the startGraphics
	 */
	public Graphic getStartGraphics() {
		return startGraphics;
	}

	/**
	 * @return the darkBowGraphics
	 */
	public Graphic getDarkBowGraphics() {
		return darkBowGraphics;
	}

	/**
	 * @return the projectileId
	 */
	public int getProjectileId() {
		return projectileId;
	}

	/**
	 * @return the poisonDamage
	 */
	public int getPoisonDamage() {
		return poisonDamage;
	}
}
