package org.dementhium.content.activity.impl.castlewars;

import org.dementhium.model.map.GameObject;

/**
 * A dynamic object to be added or removed.
 *
 * @author Emperor
 */
public class DynamicObject {

    /**
     * The action type of this dynamic object.
     *
     * @author Emperor
     */
    public static enum ActionType {
        ADD(1),
        REMOVE(0);

        /**
         * The other action type.
         */
        private final int other;

        /**
         * Constructs a new {@code ActionType} {@code Object}.
         *
         * @param other The other action type.
         */
        private ActionType(int other) {
            this.other = other;
        }

        /**
         * Gets the other action type.
         *
         * @return The action type.
         */
        public ActionType getOther() {
            return ActionType.values()[other];
        }
    }

    /**
     * Gets the action type.
     */
    private ActionType actionType;

    /**
     * The game object.
     */
    private GameObject gameObject;

    /**
     * Constructs a new {@code DynamicObject}.
     *
     * @param gameObject The game object.
     * @param actionType The action type to execute.
     */
    public DynamicObject(GameObject gameObject, ActionType actionType) {
        this.setGameObject(gameObject);
        this.setActionType(actionType);
    }

    /**
     * @return the action type
     */
    public ActionType getActionType() {
        return actionType;
    }

    /**
     * @param actionType the actionType
     */
    public void setActionType(ActionType actionType) {
        this.actionType = actionType;
    }

    /**
     * @return the gameobject
     */
    public GameObject getGameObject() {
        return gameObject;
    }

    /**
     * @param gameObject the gameobject
     */
    public void setGameObject(GameObject gameObject) {
        this.gameObject = gameObject;
    }

}