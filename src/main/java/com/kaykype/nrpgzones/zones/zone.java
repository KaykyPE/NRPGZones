package com.kaykype.nrpgzones.zones;


import org.bukkit.Location;

public class zone {
    private int id;
    private int xMin, xMax, zMin, zMax;

    public zone(int id, int xMin, int xMax, int zMin, int zMax) {
        this.id = id;
        this.xMin = xMin;
        this.xMax = xMax;
        this.zMin = zMin;
        this.zMax = zMax;
    }

    public boolean contains(Location location) {
        int x = location.getBlockX();
        int z = location.getBlockZ();
        return x >= xMin && x <= xMax &&
                z >= zMin && z <= zMax;
    }

    public int getId() {
        return id;
    }
}
