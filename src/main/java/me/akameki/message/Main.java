package me.akameki.message;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.*;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

public final class Main extends JavaPlugin implements TabExecutor {
    private Map<UUID, UUID> messageMap = new HashMap<>();
    @Override
    public void onEnable() {
        this.getCommand("message").setExecutor(this);
        this.getCommand("message").setTabCompleter(this);
        this.getCommand("reply").setExecutor(this);
        this.getCommand("reply").setTabCompleter(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (command.getName().equalsIgnoreCase("message")) {
            Player player, target;
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(ChatColor.RED + "Only players can use this command");
                return true;
            }
            target = Bukkit.getPlayer(args[0]);
            if (target==null) {
                player.sendMessage(ChatColor.RED + "Invalid player");
                return false;
            }

            StringBuilder message = new StringBuilder();
            for (int i = 1; i < args.length; i++) {
                message.append(" ").append(args[i]);
            }
            if (message.toString().equals("")) return true;
            if (player.equals(target)) {
                player.sendMessage(ChatColor.GRAY + "[You-> You]" + message);
            } else {
                player.sendMessage(ChatColor.GRAY + "[You-> " + target.getName() + "]" + message);
                target.sendMessage(ChatColor.GRAY + "[" + player.getName() + "]" + message);
                messageMap.put(player.getUniqueId(), target.getUniqueId());
                messageMap.put(target.getUniqueId(), player.getUniqueId());            }
            return true;
        }

        if (command.getName().equalsIgnoreCase("reply")) {
            Player player, target;
            if (sender instanceof Player) {
                player = (Player) sender;
            } else {
                sender.sendMessage(ChatColor.RED + "Only players can use this command");
                return true;
            }
            target = Bukkit.getPlayer(messageMap.get(player.getUniqueId()));
            if (target == null) {
                player.sendMessage(ChatColor.RED + "Send or receive a message first!");
            } else {
                StringBuilder message = new StringBuilder();
                for (String arg : args) {
                    message.append(" ").append(arg);
                }
                if (message.toString().equals("")) return true;
                if (player.equals(target)) {
                    player.sendMessage(ChatColor.GRAY + "[You-> You]" + message);
                } else {
                    player.sendMessage(ChatColor.GRAY + "[You-> " + target.getName() + "]" + message);
                    target.sendMessage(ChatColor.GRAY + "[" + player.getName() + "]" + message);
                    messageMap.put(player.getUniqueId(), target.getUniqueId());
                    messageMap.put(target.getUniqueId(), player.getUniqueId());

                }
            }
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String alias, String[] args) {
        if (command.getName().equalsIgnoreCase("message")) {
            if (args.length==1) {
                return null;
            } else {
                return new ArrayList<>();
            }
        }
        if (command.getName().equalsIgnoreCase("reply")) {
            return new ArrayList<>();
        }
        return null;
    }
}
