package com.kaykype.nrpgzones.commands;

import com.kaykype.nrpgzones.database.metods;
import com.kaykype.nrpgzones.events.zoneEvents;
import com.kaykype.nrpgzones.utils.zoneManager;
import com.kaykype.nrpgzones.zones.listAllZones;
import com.sk89q.worldedit.IncompleteRegionException;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.kaykype.nrpgzones.NRPGZones.allZones;
import static com.kaykype.nrpgzones.utils.reference.prefix;
import static com.kaykype.nrpgzones.utils.zoneManager.pedingConsoleDeletion;
import static com.kaykype.nrpgzones.utils.zoneManager.pendingDeletions;
import static java.lang.Integer.parseInt;

public class zone implements CommandExecutor {

    public zone() {
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            if(!sender.hasPermission("nrpgzones.atual")) {
                sender.sendMessage(prefix + "§cVocê não tem permissão para este comando [§6nrpgzones.atual§c]");
            }

            com.kaykype.nrpgzones.zones.zone currentZone = zoneEvents.playerZones.get(((Player) sender).getUniqueId());

            if(currentZone == null) {
                sender.sendMessage(prefix+"§aVocê não esta em nenhuma zona");
                return false;
            }

            String zoneColor = "";

            if(metods.getInt(currentZone.getId(), "danger_level") == 0) zoneColor = "§a";
            if(metods.getInt(currentZone.getId(), "danger_level") == 1) zoneColor = "§e";
            if(metods.getInt(currentZone.getId(), "danger_level") == 2) zoneColor = "§c";
            if(metods.getInt(currentZone.getId(), "danger_level") == 3) zoneColor = "§8";
            
            sender.sendMessage(prefix + "§aVocê "+(currentZone.getId() == 0 ? "não esta em nenhuma zona" : ("em §f"+metods.getString(currentZone.getId(), "name")+" §aque tem o nivel de perigo "+zoneColor+zoneManager.zoneDangerString(metods.getInt(currentZone.getId(), "danger_level")))));
            return false;
        }

        if(args[0].equalsIgnoreCase("list")) {
            if(!sender.hasPermission("nrpgzones.list")) {
                sender.sendMessage(prefix + "§cVocê não tem permissão para este comando [§6nrpgzones.list§c]");
            }

            List<com.kaykype.nrpgzones.zones.zone> zones = allZones;
            int totalZones = zones.size();

            int zonesPerPage = 5;

            int page = 1;
            if (args.length > 1) {
                try {
                    page = Integer.parseInt(args[1]);
                } catch (NumberFormatException e) {
                    sender.sendMessage(prefix + "§cA página deve ser um número válido.");
                    return true;
                }
            }

            int totalPages = (int) Math.ceil((double) totalZones / zonesPerPage);

            if (page < 1 || page > totalPages) {
                sender.sendMessage(prefix + "§cPágina inválida. Escolha entre 1 e " + totalPages + ".");
                return true;
            }

            int startIndex = (page - 1) * zonesPerPage;
            int endIndex = Math.min(startIndex + zonesPerPage, totalZones);

            StringBuilder list = new StringBuilder();
            for (int i = startIndex; i < endIndex; i++) {
                com.kaykype.nrpgzones.zones.zone zona = zones.get(i);
                list.append("§f").append(metods.getString(zona.getId(), "name"))
                        .append("\n§4- Criado por: §f").append(metods.getString(zona.getId(), "creator"))
                        .append("\n§4- ID: §f").append(metods.getInt(zona.getId(), "id"))
                        .append("\n");
            }

            sender.sendMessage("§cLista de zonas (§7Pag. "+page+" de "+totalPages+"§c)\n \n"+list.toString());
            return false;
        }

        if (args[0].equalsIgnoreCase("info")) {
            if(!sender.hasPermission("nrpgzones.info")) {
                sender.sendMessage(prefix + "§cVocê não tem permissão para este comando [§6nrpgzones.info§c]");
            }

            if (args.length < 2) {
                sender.sendMessage(prefix + "§cUse: /zone info <id>");
                return true;
            }

            if (!(args[1].matches("-?\\d+(\\.\\d+)?"))) {
                sender.sendMessage(prefix + "§cDigite um numero valido");
                return true;
            }

            if (!metods.exists(parseInt(args[1]))) {
                sender.sendMessage(prefix + "§cEsta zona não existe");
                return true;
            }

            int id = parseInt(args[1]);

            String zoneColor = "";

            if(metods.getInt(id, "danger_level") == 0) zoneColor = "§a";
            if(metods.getInt(id, "danger_level") == 1) zoneColor = "§e";
            if(metods.getInt(id, "danger_level") == 2) zoneColor = "§c";
            if(metods.getInt(id, "danger_level") == 3) zoneColor = "§8";

            sender.sendMessage("§aInformações de §f"+metods.getString(id, "name") +
                    "\n§aId: §f" + metods.getInt(id, "id") +
                    "\n§aCriador: §f" + metods.getString(id, "creator") +
                    "\n§aNivel de Perigo: " + zoneColor + zoneManager.zoneDangerString(metods.getInt(id, "danger_level")) +
                    "\n§aPonto 1:\n- §fx: " + metods.getInt(id, "x1") + " z: " + metods.getInt(id, "z1") +
                    "\n§aPonto 2:\n- §fx: " + metods.getInt(id, "x2") + " z: " + metods.getInt(id, "z2") +
                    "\n§aCriado em: §f" + metods.getDate(id));
            return true;
        }

        if(args[0].equalsIgnoreCase("delete")) {
            if(!sender.hasPermission("nrpgzones.delete")) {
                sender.sendMessage(prefix + "§cVocê não tem permissão para este comando [§6nrpgzones.delete§c]");
            }

            if (args.length < 2) {
                sender.sendMessage(prefix + "§cUse: /zone delete <id>");
                return true;
            }

            if (!(args[1].matches("-?\\d+(\\.\\d+)?"))) {
                sender.sendMessage(prefix + "§cDigite um id valido");
                return true;
            }

            List<com.kaykype.nrpgzones.zones.zone> zonesList = allZones;

            int zoneFetchId = 0;

            for(com.kaykype.nrpgzones.zones.zone currentZone : zonesList) {
                if(currentZone.getId() == parseInt(args[1])) {
                    zoneFetchId = currentZone.getId();
                    break;
                }
            }

            if(zoneFetchId == 0){
                sender.sendMessage(prefix + "§cEsta zona não existe");
                return true;
            }

            if(sender instanceof Player) {
                pendingDeletions.put(((Player) sender).getUniqueId(), parseInt(args[1]));

                TextComponent confirm = new TextComponent("§2[Confirmar]");
                confirm.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/zone confirm"));
                confirm.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("§eClique para deletar a zona").create()));

                TextComponent finalMessage = new TextComponent(prefix + "§aVocê deseja mesmo apagar §e"+metods.getString(parseInt(args[1]), "name")+"§a? | ");
                finalMessage.addExtra(confirm);
                sender.spigot().sendMessage(finalMessage);
            } else {
                zoneManager.pedingConsoleDeletion = parseInt(args[1]);

                sender.sendMessage(prefix+"§aDigite §e/zone confirm §apara deletar a zona");
            }
        }

        if(args[0].equalsIgnoreCase("confirm")) {
            if(sender instanceof Player) {
                if(!sender.hasPermission("nrpgzones.delete")) {
                    sender.sendMessage(prefix + "§cVocê não tem permissão para este comando [§6nrpgzones.delete§c]");
                }

                if (pendingDeletions.get(((Player) sender).getUniqueId()) == null || pendingDeletions.get(((Player) sender).getUniqueId()) == 0) {
                    sender.sendMessage(prefix+"§cVocê não esta deletando nenhuma zona");
                    return false;
                }

                int id = pendingDeletions.get(((Player) sender).getUniqueId());

                sender.sendMessage(prefix+"§aVocê deletou §e"+metods.getString(id, "name")+" §acom sucesso!");
                metods.delete(id);
                pendingDeletions.put(((Player) sender).getUniqueId(), 0);
            } else {
                if (pedingConsoleDeletion == 0) {
                    sender.sendMessage(prefix+"§cVocê não esta deletando nenhuma zona");
                    return false;
                }

                sender.sendMessage(prefix+"§aVocê deletou §e"+metods.getString(pedingConsoleDeletion, "name")+" §acom sucesso!");
                metods.delete(pedingConsoleDeletion);
                pedingConsoleDeletion = 0;
            }
        }

        if (args[0].equalsIgnoreCase("create")) {
            if(!sender.hasPermission("nrpgzones.create")) {
                sender.sendMessage(prefix + "§cVocê não tem permissão para este comando [§6nrpgzones.create§c]");
            }

            if(!(sender instanceof Player)) return false;

            if (args.length < 3) {
                sender.sendMessage(prefix + "§cUse: /zone create <danger> <name>");
                return true;
            }

            if (!(args[1].matches("-?\\d+(\\.\\d+)?"))) {
                sender.sendMessage(prefix + "§cDigite um numero valido");
                return true;
            }

            metods.create(String.join(" ", java.util.Arrays.copyOfRange(args, 2, args.length)), (Player) sender, parseInt(args[1]));
            return true;
        }
        return true;
    }
}
