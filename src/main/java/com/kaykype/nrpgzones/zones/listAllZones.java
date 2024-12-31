package com.kaykype.nrpgzones.zones;

import org.bukkit.Bukkit;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static com.kaykype.nrpgzones.database.connection.con;
import static com.kaykype.nrpgzones.utils.reference.prefix;

public class listAllZones {
    public static List get() {
        try {
            List<zone> zones = new ArrayList<>();

            Statement statement = con.createStatement();

            ResultSet resultSet = statement.executeQuery("SELECT id, x1, z1, x2, z2 FROM zones");

            while (resultSet.next()) {
                int id = resultSet.getInt("id");
                int xMin = resultSet.getInt("x1");
                int xMax = resultSet.getInt("x2");
                int zMin = resultSet.getInt("z1");
                int zMax = resultSet.getInt("z2");

                zones.add(new zone(id, xMin, xMax, zMin, zMax));
            }

            Bukkit.getConsoleSender().sendMessage(prefix + "§aZonas carregadas com sucesso: §f" + zones.size());
            return zones;
        } catch (SQLException exception) {
            Bukkit.getConsoleSender().sendMessage(prefix+"§cOcorreu um erro ao listar as zonas: §f"+exception);
            return null;
        }
    }
}