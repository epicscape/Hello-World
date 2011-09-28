package org.dementhium.util;

import org.dementhium.model.Mob;

import java.util.Arrays;
import java.util.Iterator;
import java.util.NoSuchElementException;


/**
 * A class which represents a list of mobs.
 *
 * @param <E> The type of mob.
 * @author Graham Edgecombe
 */
public final class EntityList<E extends Mob> implements Iterable<E> {

    /**
     * Internal mobs array.
     */
    private Mob[] mobs;

    /**
     * Current size.
     */
    private int size = 0;

    /**
     * Creates an mob list with the specified capacity.
     *
     * @param capacity The capacity.
     */
    public EntityList(int capacity) {
        mobs = new Mob[capacity + 1]; // do not use idx 0
    }

    /**
     * Gets an mob.
     *
     * @param index The index.
     * @return The mob.
     * @throws IndexOutOufBoundException if the index is out of bounds.
     */
    @SuppressWarnings("unchecked")
    public E get(int index) {
        if (index <= 0 || index >= mobs.length) {
            return null;
        }
        return (E) mobs[index];
    }

    /**
     * Gets the index of an mob.
     *
     * @param mob The mob.
     * @return The index in the list.
     */
    public int indexOf(Mob mob) {
        return mob.getIndex();
    }

    /**
     * Gets the next free id.
     *
     * @return The next free id.
     */
    private int getNextId() {
        for (int i = 1; i < mobs.length; i++) {
            if (mobs[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public boolean add(E arg0) {
        int id = getNextId();
        if (id == -1) {
            return false;
        }
        mobs[id] = arg0;
        arg0.setIndex(id);
        size++;
        return true;
    }

    public boolean contains(Mob mob) {
        return mobs[mob.getIndex()] == mob;
    }

    @Override
    public Iterator<E> iterator() {
        return new EntityListIterator();
    }

    public boolean remove(E entity) {
        int index = entity.getIndex();
        if (mobs[index] == entity) {
            mobs[index].destroy();
            if (mobs[index].isPlayer()) {
                mobs[index].getPlayer().setOnline(false);
            }
            mobs[index] = null;
            size--;
            return true;
        }
        return false;
    }


    public int size() {
        return size;
    }

    public Mob[] toArray() {
        int size = this.size;
        Mob[] array = new Mob[size];
        int ptr = 0;
        for (int i = 1; i < mobs.length && ptr < size; i++) {
            if (mobs[i] != null) {
                array[ptr++] = mobs[i];
            }
        }
        return array;
    }

    @SuppressWarnings("unchecked")
    public <T> T[] toArray(T[] arg0) {
        Mob[] arr = toArray();
        return (T[]) Arrays.copyOf(arr, arr.length, arg0.getClass());
    }

    /**
     * An implementation of an iterator for an mob list.
     *
     * @param <E> The type of mob.
     * @author Graham Edgecombe
     */
    private final class EntityListIterator implements Iterator<E> {

        /**
         * The mobs.
         */
        private Mob[] mobs;

        /**
         * The previous index.
         */
        private int lastIndex = -1;

        /**
         * The current index.
         */
        private int cursor = 0;

        /**
         * The size of the list.
         */
        private int size;

        /**
         * Creates an mob list iterator.
         *
         * @param entityList The mob list.
         */
        public EntityListIterator() {
            mobs = toArray(new Mob[0]);
            size = mobs.length;
        }

        @Override
        public boolean hasNext() {
            return cursor < size;
        }

        @SuppressWarnings("unchecked")
        @Override
        public E next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            lastIndex = cursor++;
            return (E) mobs[lastIndex];
        }

        @SuppressWarnings("unchecked")
        @Override
        public void remove() {
            if (lastIndex == -1) {
                throw new IllegalStateException();
            }
            EntityList.this.remove((E) mobs[lastIndex]);
        }

    }

    public Mob getById(int id) {
        for (Mob m : mobs) {
            if (m != null && m.getNPC().getId() == id) {
                return m;
            }
        }
        return null;
    }


}
