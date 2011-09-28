package org.dementhium.event.impl.object;

import org.dementhium.event.EventListener;
import org.dementhium.event.EventManager;

/**
 * @author Steve <golden_32@live.com>
 */
public class PoolObjectListener extends EventListener {

    private static final int[] OBJECT_IDS = null;

    @Override
    public void register(EventManager manager) {
        for (int i : OBJECT_IDS)
            manager.registerObjectListener(i, this);

    }

}
