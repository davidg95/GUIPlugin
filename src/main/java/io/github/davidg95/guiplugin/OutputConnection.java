/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import org.bukkit.Bukkit;

/**
 *
 * @author David
 */
public class OutputConnection {

    private final Socket incoming;
    private final GUI g;
    private boolean conn_term = false;
    private BufferedReader in;
    PrintWriter out;

    public OutputConnection(Socket s, GUI g) {
        this.incoming = s;
        this.g = g;

        try {
            in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
            out = new PrintWriter(incoming.getOutputStream(), true);
        } catch (IOException ex) {
        }
    }

    public void init() {
        g.toTextArea(incoming.getInetAddress().getHostAddress() + " connected");

        sendMessage("Server " + Config.SERVER_NAME + " running on port number " + Bukkit.getPort());
        sendMessage("Running " + Bukkit.getVersion() + " " + Bukkit.getBukkitVersion());
        sendMessage("Up to " + Bukkit.getMaxPlayers() + " players can be on at once");
        sendMessage((Bukkit.hasWhitelist() ? "Sever is whitelisted" : "Server is not whitelisted"));
        sendMessage((Bukkit.getOnlineMode() ? "Server authenticates clients" : "Server does not authenticate clients"));
        sendMessage("Spawn protection radius: " + Bukkit.getSpawnRadius());
        sendMessage((Bukkit.getAllowNether() ? "Nether is allowed" : "Nether is not allowed"));
        sendMessage((Bukkit.getAllowEnd() ? "End is allowed" : "End is not allowed"));
        sendMessage("Server will stop at " + Config.STOP_HOUR + ":" + Config.STOP_MINUTE);
    }
    
    public void sendMessage(String message){
        out.println("LOG " + message);
    }
    
    public void playerJoin(String name){
        out.println("JOIN " + name);
    }
    
    public void playerLeave(String name){
        out.println("LEAVE " + name);
    }
    
    public void sendPlayers(String[] players){
        
    }
}
