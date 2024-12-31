package com.kaykype.nrpgzones.events;

import com.kaykype.nrpgzones.database.metods;
import com.kaykype.nrpgzones.utils.zoneManager;
import com.kaykype.nrpgzones.zones.listAllZones;
import com.kaykype.nrpgzones.zones.zone;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import static com.kaykype.nrpgzones.NRPGZones.allZones;
import static com.kaykype.nrpgzones.utils.reference.prefix;

public class zoneEvents implements Listener {
    public static Map<UUID, zone> playerZones = new ConcurrentHashMap<>();

    @EventHandler
    public void deathProp(PlayerDeathEvent e) {
        int dangerLevel;

        if(playerZones.get(e.getEntity().getUniqueId()) == null) dangerLevel = 0;
        else if (playerZones.get(e.getEntity().getUniqueId()).getId() == 0) dangerLevel = 0;
        else dangerLevel = playerZones.get(e.getEntity().getUniqueId()).getId();

        int danger = metods.getInt(dangerLevel, "danger_level");
        com.kaykype.nrpgzones.zones.zonesProperties.metods.getProp(danger).onDeathInZone(e);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        List<zone> zones = allZones;

        Location location = e.getPlayer().getLocation();

        zone currentZone = playerZones.get(e.getPlayer().getUniqueId());
        zone newZone = null;

        for (zone zone : zones) {
            if (zone.contains(location)) {
                newZone = zone;
                break;
            }
        }

        currentZone = currentZone != null ? currentZone : new zone(0, 0, 0, 0, 0);
        newZone = newZone != null ? newZone : new zone(0, 0, 0, 0, 0);

        if (newZone.getId() != currentZone.getId()) {
            playerZones.put(e.getPlayer().getUniqueId(), newZone);

            if(newZone.getId() == 0) return;

            String zoneColor = "";

            if(metods.getInt(newZone.getId(), "danger_level") == 0) zoneColor = "§a";
            if(metods.getInt(newZone.getId(), "danger_level") == 1) zoneColor = "§e";
            if(metods.getInt(newZone.getId(), "danger_level") == 2) zoneColor = "§c";
            if(metods.getInt(newZone.getId(), "danger_level") == 3) zoneColor = "§8";

            e.getPlayer().sendTitle(metods.getString(newZone.getId(), "name"), zoneColor+zoneManager.zoneDangerString(metods.getInt(newZone.getId(), "danger_level")));
        }
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent e) {
        List<zone> zones = allZones;

        Location location = e.getPlayer().getLocation();

        zone currentZone = playerZones.get(e.getPlayer().getUniqueId());
        zone newZone = null;

        for (zone zone : zones) {
            if (zone.contains(location)) {
                newZone = zone;
                break;
            }
        }

        currentZone = currentZone != null ? currentZone : new zone(0, 0, 0, 0, 0);
        newZone = newZone != null ? newZone : new zone(0, 0, 0, 0, 0);

        if (newZone.getId() != currentZone.getId()) {
            playerZones.put(e.getPlayer().getUniqueId(), newZone);

            if(newZone.getId() == 0) return;

            String zoneColor = "";

            if(metods.getInt(newZone.getId(), "danger_level") == 0) zoneColor = "§a";
            if(metods.getInt(newZone.getId(), "danger_level") == 1) zoneColor = "§e";
            if(metods.getInt(newZone.getId(), "danger_level") == 2) zoneColor = "§c";
            if(metods.getInt(newZone.getId(), "danger_level") == 3) zoneColor = "§8";

            e.getPlayer().sendTitle(metods.getString(newZone.getId(), "name"), zoneColor+zoneManager.zoneDangerString(metods.getInt(newZone.getId(), "danger_level")));
        }
    }

    @EventHandler
    public void onTp(PlayerTeleportEvent e) {
        List<zone> zones = allZones;
        Location location = e.getTo();
        Location oldlocation = e.getFrom();

        if (oldlocation.getBlockX() == location.getBlockX() &&
                oldlocation.getBlockZ() == location.getBlockZ()) {
            return;
        }

        zone currentZone = playerZones.get(e.getPlayer().getUniqueId());
        zone newZone = null;

        for (zone zone : zones) {
            if (zone.contains(location)) {
                newZone = zone;
                break;
            }
        }

        currentZone = currentZone != null ? currentZone : new zone(0, 0, 0, 0, 0);
        newZone = newZone != null ? newZone : new zone(0, 0, 0, 0, 0);

        if (newZone.getId() != currentZone.getId()) {
            playerZones.put(e.getPlayer().getUniqueId(), newZone);

            if(newZone.getId() == 0) return;

            String zoneColor = "";

            if(metods.getInt(newZone.getId(), "danger_level") == 0) zoneColor = "§a";
            if(metods.getInt(newZone.getId(), "danger_level") == 1) zoneColor = "§e";
            if(metods.getInt(newZone.getId(), "danger_level") == 2) zoneColor = "§c";
            if(metods.getInt(newZone.getId(), "danger_level") == 3) zoneColor = "§8";

            e.getPlayer().sendTitle(metods.getString(newZone.getId(), "name"), zoneColor+zoneManager.zoneDangerString(metods.getInt(newZone.getId(), "danger_level")));
        }
    }

    @EventHandler
    public void onZoneEnter(PlayerMoveEvent e) {
        List<zone> zones = allZones;
        Location location = e.getTo();
        Location oldlocation = e.getFrom();

        if (oldlocation.getBlockX() == location.getBlockX() &&
                oldlocation.getBlockZ() == location.getBlockZ()) {
            return;
        }

        zone currentZone = playerZones.get(e.getPlayer().getUniqueId());
        zone newZone = null;

        for (zone zone : zones) {
            if (zone.contains(location)) {
                newZone = zone;
                break;
            }
        }

        currentZone = currentZone != null ? currentZone : new zone(0, 0, 0, 0, 0);
        newZone = newZone != null ? newZone : new zone(0, 0, 0, 0, 0);

        if (newZone.getId() != currentZone.getId()) {
            playerZones.put(e.getPlayer().getUniqueId(), newZone);

            if(newZone.getId() == 0) return;

            String zoneColor = "";

            if(metods.getInt(newZone.getId(), "danger_level") == 0) zoneColor = "§a";
            if(metods.getInt(newZone.getId(), "danger_level") == 1) zoneColor = "§e";
            if(metods.getInt(newZone.getId(), "danger_level") == 2) zoneColor = "§c";
            if(metods.getInt(newZone.getId(), "danger_level") == 3) zoneColor = "§8";

            e.getPlayer().sendTitle(metods.getString(newZone.getId(), "name"), zoneColor+zoneManager.zoneDangerString(metods.getInt(newZone.getId(), "danger_level")));
        }
    }
}
