package org.dementhium.io;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.dementhium.content.areas.impl.CircularArea;
import org.dementhium.content.areas.impl.IrregularArea;
import org.dementhium.content.areas.impl.RectangularArea;
import org.dementhium.identifiers.Identifier;
import org.dementhium.model.Item;
import org.dementhium.model.Location;
import org.dementhium.model.definition.WeaponInterface;
import org.dementhium.tools.converters.NPCConverter.NPCDefintion;

import com.thoughtworks.xstream.XStream;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
@SuppressWarnings("unchecked")
public final class XMLHandler {

    private XMLHandler() {
    }

    private static XStream xmlHandler;

    static {
        xmlHandler = new XStream();
        xmlHandler.alias("item", Item.class);
        xmlHandler.alias("rectangle", RectangularArea.class);
        xmlHandler.alias("circle", CircularArea.class);
        xmlHandler.alias("irregular", IrregularArea.class);
        xmlHandler.alias("position", Location.class);
        xmlHandler.alias("identifier", Identifier.class);
        xmlHandler.alias("ban", String.class);
        xmlHandler.alias("npcDefinition", NPCDefintion.class);
        xmlHandler.alias("weaponInterface", WeaponInterface.class);
    }

    public static void toXML(String file, Object object) throws IOException {
        OutputStream out = new FileOutputStream(file);
        try {
            xmlHandler.toXML(object, out);
            out.flush();
        } finally {
            out.close();
        }
    }

    public static <T> T fromXML(String file) throws IOException {
        InputStream in = new FileInputStream(file);
        try {
            return (T) xmlHandler.fromXML(in);
        } catch (Throwable e) {
            e.printStackTrace();
        } finally {
            in.close();
        }
        return null;
    }

    public static void setXmlHandler(XStream xstream) {
        xmlHandler = xstream;
        xmlHandler.alias("item", Item.class);
        xmlHandler.alias("rectangle", RectangularArea.class);
        xmlHandler.alias("circle", CircularArea.class);
        xmlHandler.alias("irregular", IrregularArea.class);
        xmlHandler.alias("position", Location.class);
        xmlHandler.alias("identifier", Identifier.class);
        xmlHandler.alias("ban", String.class);
        xmlHandler.alias("npcDefinition", NPCDefintion.class);
        xmlHandler.alias("weaponInterface", WeaponInterface.class);
    }

}
