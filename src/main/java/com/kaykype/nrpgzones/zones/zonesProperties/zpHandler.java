package com.kaykype.nrpgzones.zones.zonesProperties;

import org.bukkit.entity.Player;
import org.bukkit.event.entity.PlayerDeathEvent;

public abstract class zpHandler {
    public abstract void onDeathInZone(PlayerDeathEvent event);
}
