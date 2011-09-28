package org.dementhium.content.activity.impl.castlewars;

/**
 * An enum containing all castle wars teams.
 *
 * @author Emperor
 */
public enum TeamType {

    /**
     * The zamorak team.
     */
    ZAMORAK,

    /**
     * The saradomin team.
     */
    SARADOMIN;

    /**
     * The opposite team.
     */
    private TeamManager oppositeTeam;

    /**
     * @param oppositeTeam the oppositeTeam to set
     */
    public void setOppositeTeam(TeamManager oppositeTeam) {
        this.oppositeTeam = oppositeTeam;
    }

    /**
     * @return the oppositeTeam
     */
    public TeamManager getOppositeTeam() {
        return oppositeTeam;
    }
}
