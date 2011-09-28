package org.dementhium.model.definition;

/**
 * @author 'Mystic Flow
 */
public class PlayerDefinition {

    private String username;
    private String password;
    private int rights;
    private int donor;

    public PlayerDefinition(String user, String pass) {
        this.username = user;
        this.password = pass;
    }

    public String getName() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public void setRights(int rights) {
        this.rights = rights;
    }

    public int getRights() {
        return rights;
    }
    
    public void setDonor(int value){
    	this.donor = value;
    }
    
    public int getDonor(){
    	return donor;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}