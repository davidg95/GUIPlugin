/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author David
 */
public class GUIPlugin extends JavaPlugin {

    protected static ArrayList<Player> playerList = new ArrayList<>();
    protected static GUI g;
    protected static boolean dtAPI;
    public static Plugin plugin;
    private Desktop dt;

    @Override
    public void onEnable() {
        plugin = this;
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            playerList.add(player);
        }

        dtAPI = Desktop.isDesktopSupported();
        dt = Desktop.getDesktop();

        g = new GUI(playerList);

        getServer().getPluginManager().registerEvents(g, this);
        g.setVisible(true);

        g.serverStopTimer();
    }

    @Override
    public void onDisable() {
        this.saveConfig();
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("gui")) {
            if (!(sender instanceof Player)) {
                g.setVisible(true);
                return true;
            } else {
                sender.sendMessage("This command can only be run from the server console");
                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("rendermap")) {
            if (dtAPI) {
                g.renderMap();
            } else {
                sender.sendMessage("Desktop API not supported, render map has been disabled.");
            }
            return true;
        } else if (cmd.getName().equalsIgnoreCase("ignoresleep")) {
            if (sender instanceof Player) {
                Player p = (Player) sender;
                p.setSleepingIgnored(!p.isSleepingIgnored());
                if (p.isSleepingIgnored()) {
                    sender.sendMessage("Sleeping ignored");
                } else {
                    sender.sendMessage("Sleeping no longer ignored");
                }
            }
        } else if (cmd.getName().equalsIgnoreCase("stopshut")) {
            if (dtAPI) {
                try {
                    File document = new File(".\\sleep.lnk");
                    dt.open(document);
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                } catch (IOException ex) {
                    sender.sendMessage("There was an error on opening the sleep file");
                }
            }
        }
        return false;
    }

    /**
     * Method to set the GUI to the decorated state. This method will dispose of
     * the GUI first then repack it.
     */
    public static void minimize() {
        g.setVisible(false);
        g.dispose();
        g.setUndecorated(false);
        g.pack();
        g.setVisible(true);
        g.setIsDecorated(true);
        g.setBtnMinMaxTitle("Maximize GUI");
    }

    /**
     * Method to set the GUI to the undecorated state. This method will dispose
     * of the GUI first then repack it.
     */
    public static void maximize() {
        g.setVisible(false);
        g.dispose();
        g.setUndecorated(true);
        g.pack();
        g.setVisible(true);
        g.setIsDecorated(false);
        g.setBtnMinMaxTitle("Minimize GUI");
    }

    public static void reloadGUI() {
        g = new GUI(playerList);
        g.setVisible(true);
    }

}
