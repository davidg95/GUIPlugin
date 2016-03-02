/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.SwingUtilities;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
/**
 *
 * @author David
 */
public class GUIPlugin extends JavaPlugin{
    protected ArrayList<Player> playerList = new ArrayList<>();
    protected GUI g;
    
    @Override
    public void onEnable(){
        for(Player player: Bukkit.getServer().getOnlinePlayers()){
            playerList.add(player);
        }
        
        g = new GUI(playerList);
        
        getServer().getPluginManager().registerEvents(g, this);
        g.setVisible(true);
    }
    
    @Override
    public void onDisable(){
        
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
}
