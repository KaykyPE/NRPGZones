package com.kaykype.nrpgzones.database;

import com.kaykype.nrpgzones.utils.reference;
import com.kaykype.nrpgzones.zones.listAllZones;
import com.kaykype.nrpgzones.zones.zone;
import com.sk89q.worldedit.IncompleteRegionException;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.internal.annotation.Selection;
import com.sk89q.worldedit.regions.Region;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.chat.hover.content.Content;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.w3c.dom.Text;

import java.sql.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;

import static com.kaykype.nrpgzones.NRPGZones.allZones;
import static com.kaykype.nrpgzones.database.connection.con;
import static com.kaykype.nrpgzones.utils.reference.prefix;

public class metods {
    public static boolean exists(int zoneId) {
        String sql = "SELECT COUNT(*) FROM zones WHERE id = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);

            stmt.setInt(1, zoneId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException exception) {
            Bukkit.getConsoleSender().sendMessage(prefix + "§cNão foi possivel verificar se a zona §f" + zoneId + " §cexiste");
        }
        return false;
    }

    public static int getInt(int zoneId, String key) {
        String sql = "SELECT * FROM zones WHERE id = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, zoneId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(key);
            }
        } catch (SQLException exception) {
            Bukkit.getConsoleSender().sendMessage(prefix + "§cNão foi possivel verificar as informações de §f" + zoneId);
        }
        return 0;
    }

    public static String getString(int zoneId, String key) {
        String sql = "SELECT * FROM zones WHERE id = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, zoneId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getString(key);
            }
        } catch (SQLException exception) {
            Bukkit.getConsoleSender().sendMessage(prefix + "§cNão foi possivel verificar as informações de §f" + zoneId);
        }
        return null;
    }

    public static Timestamp getDate(int zoneId) {
        String sql = "SELECT * FROM zones WHERE id = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, zoneId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getTimestamp("date");
            }
        } catch (SQLException exception) {
            Bukkit.getConsoleSender().sendMessage(prefix + "§cNão foi possivel verificar as informações de §f" + zoneId);
        }
        return null;
    }

    public static void create(String name, Player player, int danger) {
        String sql = "INSERT INTO zones (creator, name, danger_level, x1, z1, x2, z2, date) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try {
            Region playerSelection = WorldEdit.getInstance().getSessionManager().findByName(player.getName()).getSelection(WorldEdit.getInstance().getSessionManager().findByName(player.getName()).getSelectionWorld());

            if (danger < 0 || danger > 3) {
                player.sendMessage(prefix + "§cO nivel de perigo da zona deve estar entre 0 e 3 (§f/zones info §cpara informações sobre cada zona)");
                return;
            }

            boolean isOverlapping = false;
            zone overlappingZone = null;

            List<zone> zones = allZones;

            int newMinX = playerSelection.getMinimumPoint().getX();
            int newMaxX = playerSelection.getMaximumPoint().getX();
            int newMinZ = playerSelection.getMinimumPoint().getZ();
            int newMaxZ = playerSelection.getMaximumPoint().getZ();

            for (zone existingZone : zones) {
                int existingMinX = metods.getInt(existingZone.getId(), "x1");
                int existingMaxX = metods.getInt(existingZone.getId(), "x2");
                int existingMinZ = metods.getInt(existingZone.getId(), "z1");
                int existingMaxZ = metods.getInt(existingZone.getId(), "z2");

                if (newMinX <= existingMaxX && newMaxX >= existingMinX &&
                        newMinZ <= existingMaxZ && newMaxZ >= existingMinZ) {
                    isOverlapping = true;
                    overlappingZone = existingZone;
                    break;
                }
            }

            if (isOverlapping) {
                player.sendMessage(prefix + "§cVocê está tentando criar esta zona por cima de §e"
                        + metods.getString(overlappingZone.getId(), "name")
                        + " [X: " + metods.getInt(overlappingZone.getId(), "x1")
                        + " Z: " + metods.getInt(overlappingZone.getId(), "z1")
                        + " até X: " + metods.getInt(overlappingZone.getId(), "x2")
                        + " Z: " + metods.getInt(overlappingZone.getId(), "z2") + "]");
                return;
            }

            try {
                PreparedStatement stmt = con.prepareStatement(sql);
                stmt.setString(1, player.getName());
                stmt.setString(2, name);
                stmt.setInt(3, danger);
                stmt.setInt(4, playerSelection.getMinimumPoint().getX());
                stmt.setInt(5, playerSelection.getMinimumPoint().getZ());
                stmt.setInt(6, playerSelection.getMaximumPoint().getX());
                stmt.setInt(7, playerSelection.getMaximumPoint().getZ());
                stmt.setTimestamp(8, new Timestamp(ZonedDateTime.now(ZoneId.of("America/Sao_Paulo")).toInstant().toEpochMilli()));
                stmt.executeUpdate();

                allZones = listAllZones.get();

                TextComponent execute_info = new TextComponent("§e[/zone info " + name + "]");
                execute_info.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zone info "+name));
                execute_info.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eClique para executar o comando").create()));

                TextComponent finalMessage = new TextComponent(prefix + "§aÁrea criada com ");
                finalMessage.addExtra(execute_info);
                player.spigot().sendMessage(finalMessage);
            } catch (SQLException exception) {
                player.sendMessage(prefix + "§cNão foi possivel criar a zona §f" + name);
            }
        } catch (IncompleteRegionException exception) {
            player.sendMessage(prefix + "§cVocê deve primeiro selecionar uma area");
        } catch (Exception e) {
            player.sendMessage(prefix + "§cVocê deve primeiro selecionar uma area");
            e.printStackTrace();
        }
    }

    public static void delete(int zoneId) {
        String sql = "DELETE FROM zones WHERE id = ?";
        try {
            PreparedStatement stmt = con.prepareStatement(sql);
            stmt.setInt(1, zoneId);
            stmt.executeUpdate();
            allZones = listAllZones.get();
        } catch (SQLException exception) {
            Bukkit.getConsoleSender().sendMessage(prefix + "§cNão foi possivel deletar a zona §f" + zoneId);
        }
    }
}
