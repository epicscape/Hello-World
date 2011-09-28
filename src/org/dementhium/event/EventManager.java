package org.dementhium.event;

import org.dementhium.event.EventListener.ClickOption;
import org.dementhium.model.Location;
import org.dementhium.model.map.GameObject;
import org.dementhium.model.player.Player;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.*;

/**
 * @author 'Mystic Flow <Steven@rune-server.org>
 */
public class EventManager {

    private static final EventManager MANAGER = new EventManager();

    public static EventManager getEventManager() {
        return MANAGER;
    }

    private Map<Integer, EventListener> objectListeners = new HashMap<Integer, EventListener>();
    private Map<Integer, List<EventListener>> interfaceListeners = new HashMap<Integer, List<EventListener>>();
    private List<EventListener> listeners = new LinkedList<EventListener>();

    public void load() throws Exception {
        System.out.println("Loading event listeners...");

        BufferedReader reader = new BufferedReader(new FileReader("data/eventlisteners.txt"));
        String string;
        while ((string = reader.readLine()) != null) {
            if (!string.startsWith(">")) {
                continue;
            }
            string = string.substring(1);
            EventListener listener = (EventListener) Class.forName(string).newInstance();
            listener.register(this);

            listeners.add(listener); // we add it just in case
        }
        System.out.println("Loaded " + interfaceListeners.size() + " interface listeners and " + objectListeners.size() + " object listeners.");
    }

    public List<File> allFiles(File[] files) {
        List<File> list = new ArrayList<File>();
        addDirectoryFiles(list, files);
        return list;
    }

    public void addDirectoryFiles(List<File> list, File[] files) {
        for (File f : files) {
            if (f.isDirectory()) {
                addDirectoryFiles(list, f.listFiles());
            } else {
                list.add(f);
            }
        }
    }

    public boolean handleObjectOption(Player player, int objectId, GameObject gameObject, Location location, ClickOption option) {
        EventListener listener = objectListeners.get(objectId);
        return listener != null && listener.objectOption(player, objectId, gameObject, location, option);
    }

    public boolean handleInterfaceOption(Player player, int interfaceId, int buttonId, int slot, int itemId, int opcode) {
        List<EventListener> listeners = interfaceListeners.get(interfaceId);
        boolean handled = false;
        if (listeners != null) {
            for (EventListener listener : listeners) {
                if (listener.interfaceOption(player, interfaceId, buttonId, slot, itemId, opcode)) {
                    handled = true;
                }
            }
        }
        return handled;
    }

    public void registerObjectListener(int objectId, EventListener listener) {
        if (objectListeners.get(objectId) != null) {
            System.out.println(objectId);
            throw new IllegalStateException("There is already a listener bound to this id!");
        }
        objectListeners.put(objectId, listener);
    }

    public void registerInterfaceListener(int interfaceId, EventListener listener) {
        List<EventListener> listeners = interfaceListeners.get(interfaceId);
        if (listeners == null) {
            interfaceListeners.put(interfaceId, listeners = new ArrayList<EventListener>());
        }
        listeners.add(listener);
    }


}
