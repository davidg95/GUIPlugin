/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author David
 */
public class GUIPlugin extends JavaPlugin {

    protected static ArrayList<Player> playerList = new ArrayList<>();
    protected static GUI g;
    protected static FileConfiguration conf;
    protected static int STOP_HOUR = 23;
    protected static int STOP_MINUTE = 28;
    public static String LOOK_FEEL = "Metal";
    protected static Timer timWarning = new Timer();
    protected static Timer timStop = new Timer();

    @Override
    public void onEnable() {
        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
            playerList.add(player);
        }

        conf = this.getConfig();

        g = new GUI(playerList);

        getServer().getPluginManager().registerEvents(g, this);
        g.setVisible(true);

        serverStopTimer();
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
            g.renderMap();
            return true;
        }
        return false;
    }

    public static void serverStopTimer() {
        Calendar warning = Calendar.getInstance();
        warning.set(Calendar.HOUR, 23);
        warning.set(Calendar.MINUTE, 23);
        warning.set(Calendar.SECOND, 0);
        warning.set(Calendar.MILLISECOND, 0);
        // Schedule to run every night at 23:23
        timWarning.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say ***SERVER SHUTDOWN IN 5 MINUTES***");
                    }
                },
                warning.getTime(),
                1000 * 60 * 60 * 24 * 7
        );
        Calendar stopper = Calendar.getInstance();
        stopper.set(Calendar.HOUR, STOP_HOUR);
        stopper.set(Calendar.MINUTE, STOP_MINUTE);
        stopper.set(Calendar.SECOND, 0);
        stopper.set(Calendar.MILLISECOND, 0);
        // Schedule to run every night at 23:28
        timStop.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                    }
                },
                stopper.getTime(),
                1000 * 60 * 60 * 24 * 7
        );
    }
    
    public static void cancelStopTimer(){
        timWarning.cancel();
        timStop.cancel();
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
