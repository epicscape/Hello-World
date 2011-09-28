package org.dementhium.identifiers;

import org.dementhium.identifiers.impl.CombatAfterEffect;
import org.dementhium.identifiers.impl.DropArrowIdentifier;

import java.util.HashMap;
import java.util.Map;

/**
 * @author `Discardedx2 <the_shawn@discardedx2.info>
 */
public class IdentifierManager {

    public static Identifier DEFAULT = (new Identifier() {
        @Override
        public void identify(Object... args) {

        }
    });

    private static Map<String, Identifier> identifiers = new HashMap<String, Identifier>();

    public static void registerIdentifiers() {
        identifiers.put("drop_arrows", new DropArrowIdentifier());
        identifiers.put("combat_after_effect", new CombatAfterEffect());
    }

    public static void register(String name, Identifier i) {
        identifiers.put(name, i);
    }

    public static void unregister(String name) {
        identifiers.remove(name);
    }

    public static Identifier get(String name) {
        Identifier identifier = identifiers.get(name);
        if (identifier == null) {
            return DEFAULT;
        }
        return identifier;
    }

    @SuppressWarnings("unchecked")
    public static <T> T getIdentifier(String string) {
        return (T) identifiers.get(string);
    }

    public Map<String, Identifier> getIdentifiers() {
        return identifiers;
    }
}
