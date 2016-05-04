/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import java.util.ArrayList;
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
public class GUIPlugin extends JavaPlugin{
    protected static ArrayList<Player> playerList = new ArrayList<>();
    protected static GUI g;
    protected static FileConfiguration conf;
    public static String LOOK_FEEL = "Metal";
    
    @Override
    public void onEnable(){
        for(Player player: Bukkit.getServer().getOnlinePlayers()){
            playerList.add(player);
        }
        
        conf = this.getConfig();
        
        g = new GUI(playerList);
        
        getServer().getPluginManager().registerEvents(g, this);
        g.setVisible(true);
    }
    
    @Override
    public void onDisable(){
        this.saveConfig();
    }
    
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
        if(cmd.getName().equalsIgnoreCase("gui")){
            if(!(sender instanceof Player)){
                g.setVisible(true);
                return true;
            } else{
                sender.sendMessage("This command can only be run from the server console");
                return true;
            }
        }
        
        return false;
    }
    
    /**
     * Method to set the GUI to the decorated state. This method will dispose of the GUI first then repack it.
     */
    public static void minimize(){
        g.setVisible(false);
        g.dispose();
        g.setUndecorated(false);
        g.pack();
        g.setVisible(true);
        g.setIsDecorated(true);
        g.setBtnMinMaxTitle("Maximize GUI");
    }
    
    /**
     * Method to set the GUI to the undecorated state. This method will dispose of the GUI first then repack it.
     */
    public static void maximize(){
        g.setVisible(false);
        g.dispose();
        g.setUndecorated(true);
        g.pack();
        g.setVisible(true);
        g.setIsDecorated(false);
        g.setBtnMinMaxTitle("Minimize GUI");
    }
    
    public static void reloadGUI(){
        g = new GUI(playerList);
        g.setVisible(true);
    }
}
