/*
 * Created by Zach
 *
 */
package org.dementhium.action;

public abstract class PlayerAction {

    public int cycle; //-1 for infinite, otherwise 1 = 600 ms
    public String type; //Runescape has different types of actions (Damage, Interface, Animation, Etc.)
    public boolean flag;

    public PlayerAction(int cycle, String type, boolean flag) {
        this.type = type;
        this.cycle = cycle;
        this.flag = flag;
    }

    public abstract void execute();

}