package com.kaykype.nrpgzones;

import com.kaykype.nrpgzones.commands.zone;
import com.kaykype.nrpgzones.events.zoneEvents;
import com.kaykype.nrpgzones.zones.listAllZones;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

import static com.kaykype.nrpgzones.database.connection.*;

public final class NRPGZones extends JavaPlugin {
    public static List allZones;

    @Override
    public void onEnable() {
        connect();
        table();

        allZones = listAllZones.get();

        getServer().getPluginManager().registerEvents(new zoneEvents(), this);

        getCommand("zone").setExecutor(new zone());
    }

    @Override
    public void onDisable() {
        desconnect();
    }
}
