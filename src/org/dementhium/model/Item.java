package org.dementhium.model;

import org.dementhium.model.definition.ItemDefinition;

/**
 * @author 'Mystic Flow
 * @author Emperor (Idea)
 */
public class Item {

    private int id;
    private long amount;

    public Item(int id) {
        this.id = id;
        this.amount = 1;
    }
    
    public Item(int id, long amount) {
        this.id = id;
        this.amount = amount;
    }

    public Item(int id, int amount) {
        this.id = id;
        this.amount = amount;
    }

    public Item(Item item) {
        this.id = item.getId();
        this.amount = item.getAmount();
    }

    public ItemDefinition getDefinition() {
        return ItemDefinition.forId(id);
    }

    public int getAmount() {
    	if (amount > Integer.MAX_VALUE) {
    		return (int) (amount & 0x7fffffff);
    	}
        if (amount < 0 || amount == Integer.MAX_VALUE) {
            amount = Integer.MAX_VALUE - 1;
        }
        /*if (amount == 0) {
        	amount = 1;
        }*/
        return (int) amount;
    }
    
    public void setHealth(int health) {
    	long healthHash = (long) health << 31;
    	this.amount = healthHash | getAmount();
    }
    
    public int getHealth() {
    	return (int) (amount >> 31);
    }
    
    public long getHash() {
    	return amount;
    }

    public int getId() {
        return id;
    }

    public void setAmount(int amount) {
    	int health = getHealth();
    	if (health > 0) {
    		long healthHash = (long) health << 31;
    		this.amount = healthHash | amount;
    		return;
    	}
        this.amount = amount;
    }
    
    @Override
    public String toString() {
    	return new StringBuilder("id: ").append(id).append(" amount: ").append(getAmount())
    			.append(" health: ").append(getHealth()).toString();
    }

}
