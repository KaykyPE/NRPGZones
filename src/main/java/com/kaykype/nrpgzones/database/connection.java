package com.kaykype.nrpgzones.database;

import org.bukkit.Bukkit;

import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.SQLException;

public class connection {
    public static Connection con;

    public static void connect() {
        String url = "jdbc:mysql://190.102.40.72:3306/s14905_nrpg?useSSL=false&serverTimezone=UTC";

        String username = "u14905_03fMlDFw28";

        String password = "a+@SY8!vGkbUDuqLPCJ!dohH";

        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");

                con = DriverManager.getConnection(url, username, password);

                Bukkit.getConsoleSender().sendMessage("§f[§7NRPGZones§f] §aBanco de dados iniciado com sucesso");
            } catch(ClassNotFoundException exception) {

            }
        } catch (SQLException exception) {
            Bukkit.getConsoleSender().sendMessage("§f[§7NRPGZones§f] §cNão foi possivel iniciar o banco de dados: §f"+exception);
        }
    }

    public static void desconnect () {
        try {
            if (con != null) {
                con.close();
                Bukkit.getConsoleSender().sendMessage("§f[§7NRPGZones§f] §aBanco de dados fechado com sucesso");
            }
        } catch (SQLException exception) {
            Bukkit.getConsoleSender().sendMessage("§f[§7NRPGZones§f] §cNão foi possivel fechar o banco de dados: §f"+exception);
        }
    }

    public static void table () {
        String table = "CREATE TABLE IF NOT EXISTS zones (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "creator VARCHAR (50)," +
                "name VARCHAR (30)," +
                "danger_level INT NOT NULL," +
                "x1 INT NOT NULL," +
                "z1 INT NOT NULL," +
                "x2 INT NOT NULL," +
                "z2 INT NOT NULL," +
                "date TIMESTAMP NOT NULL);";

        try {
            con.createStatement().execute(table);
            Bukkit.getConsoleSender().sendMessage("§f[§7NRPGZones§f] §aTabela criada com sucesso");
        } catch(SQLException exception) {
            Bukkit.getConsoleSender().sendMessage("§f[§7NRPGZones§f] §cNão foi possivel criar a tabela: §f"+exception);
        }
    }
}
