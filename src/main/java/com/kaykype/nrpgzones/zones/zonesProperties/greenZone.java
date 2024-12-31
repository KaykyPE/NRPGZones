package com.kaykype.nrpgzones.zones.zonesProperties;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public class greenZone extends zpHandler {
    @Override
    public void onDeathInZone (PlayerDeathEvent event) {
        event.setKeepInventory(true);
        event.setKeepLevel(true);
    }
}
