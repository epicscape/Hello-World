package org.dementhium.util;

public class WorldDef {

    private final String activity;
    private final int country;
    private final int flag;
    private final String ip;
    private final int location;
    private final String region;
    private final int worldId;

    public WorldDef(int worldId, int location, int flag, String activity,
                    String ip, String region, int country) {
        this.worldId = worldId;
        this.location = location;
        this.flag = flag;
        this.activity = activity;
        this.ip = ip;
        this.region = region;
        this.country = country;
    }

    public String getActivity() {
        return activity;
    }

    public int getCountry() {
        return country;
    }

    public int getFlag() {
        return flag;
    }

    public String getIp() {
        return ip;
    }

    public int getLocation() {
        return location;
    }

    public String getRegion() {
        return region;
    }

    public int getWorldId() {
        return worldId;
    }

}
