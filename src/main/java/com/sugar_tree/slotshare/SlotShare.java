package com.sugar_tree.slotshare;

import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public final class SlotShare extends JavaPlugin implements Listener, TabExecutor {

    public static final String PREFIX = ChatColor.LIGHT_PURPLE + "[" + ChatColor.AQUA + "SlotShare" + ChatColor.LIGHT_PURPLE + "] " + ChatColor.RESET;

    private boolean SlotShare = true;
    private int currentSlot = 0;

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onEnable() {
        saveDefaultConfig();
        SlotShare = getConfig().getBoolean("SlotShare");
        if (SlotShare) patchAll();
        getCommand("slotshare").setExecutor(this);
        getCommand("slotshare").setTabCompleter(this);
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getConsoleSender().sendMessage(PREFIX + ChatColor.YELLOW + "Enabled 슬롯 공유 플러그인 by. " + ChatColor.GREEN + "sugar_tree");
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @EventHandler
    public void onPlayerItemHeld(PlayerItemHeldEvent event) {
        if (SlotShare) {
            currentSlot = event.getNewSlot();
            patchAll();
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        event.getPlayer().sendMessage(Component.text(PREFIX + ChatColor.YELLOW + "This server is using 슬롯 공유 플러그인 by." + ChatColor.GREEN + "sugar_tree"));
        if (SlotShare) patch(event.getPlayer());
    }

    private void patchAll() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.getInventory().setHeldItemSlot(currentSlot);
        }
    }

    private void patch(Player p) {
        p.getInventory().setHeldItemSlot(currentSlot);
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("slotshare") || command.getName().equalsIgnoreCase("ss")) {
            if (sender.hasPermission("slotshare")) {
                SlotShare = !SlotShare;
                sender.sendMessage(PREFIX + ChatColor.YELLOW + "슬롯 공유: " + ChatColor.GREEN + SlotShare);
                getConfig().set("SlotShare", SlotShare);
                saveConfig();
            }
            else {
                sender.sendMessage(Bukkit.getPermissionMessage());
            }
            return true;
        }
        return false;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command command, @NotNull String alias, @NotNull String[] args) {
        if (command.getName().equalsIgnoreCase("slotshare") || command.getName().equalsIgnoreCase("ss")) {
            if (sender.hasPermission("slotshare")) {
                return new ArrayList<>();
            }
        }
        return null;
    }
}
