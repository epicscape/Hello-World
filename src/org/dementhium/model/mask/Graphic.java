package org.dementhium.model.mask;

/**
 * @author 'Mystic Flow
 */
public class Graphic {

    public static final Graphic STUNNED_GRAPHIC = Graphic.create(80, 100 << 16);
    public static final Graphic RESET = Graphic.create(-1, 0, 120);

    public static Graphic create(int id) {
        return new Graphic(id);
    }

    public static Graphic create(int id, int delay) {
        return new Graphic(id, delay);
    }

    public static Graphic create(int id, int delay, int height) {
        return new Graphic(id, delay, height);
    }

    private final int delay;
    private final int height;
    private final int id;

    private Graphic(int id) {
        this(id, 0);
    }

    private Graphic(int id, int delay) {
        this(id, delay, 0);
    }

    public Graphic(int id, int delay, int height) {
        this.id = id;
        this.delay = delay;
        this.height = height;
    }

    public int getDelay() {
        return delay;
    }

    public int getHeight() {
        return height;
    }

    public int getId() {
        return id;
    }
}
