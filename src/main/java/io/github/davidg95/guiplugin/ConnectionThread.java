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
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 *
 * @author David
 */
public class ConnectionThread extends Thread {

    private final ServerSocket s;
    private final GUI g;
    private boolean conn_term = false;
    private OutputConnection output;

    public ConnectionThread(ServerSocket s, GUI g) {
        this.s = s;
        this.g = g;
    }

    @Override
    public void run() {
        try {
            Socket incoming = s.accept();
            g.toTextArea(incoming.getInetAddress().getHostAddress() + " connected");
            output = new OutputConnection(incoming, g);
            output.init();
            BufferedReader in = new BufferedReader(new InputStreamReader(incoming.getInputStream()));
            PrintWriter out = new PrintWriter(incoming.getOutputStream(), true);

            while (!conn_term) {
                String input = in.readLine(); //Get input

                String inp[] = input.split(","); //Split up into arguments

                String args = "";
                if (inp.length > 1) {
                    for (int i = 1; i < inp.length; i++) {
                        args += inp[i] + " ";
                    }
                }
                switch (inp[0]) {
                    case "CHAT": //Chat getting sent to the server
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say " + args);
                        break;
                    case "COMM": //Command getting sent
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), args);
                        break;
                    case "PLAYERS":
                        ArrayList<Player> playerList = new ArrayList<>();
                        for (Player player : Bukkit.getServer().getOnlinePlayers()) {
                            playerList.add(player);
                        }
                        String p = "";
                        for (Player pl : playerList) {
                            p += pl.getName() + ",";
                        }
                        out.println("PLAYERS," + p);
                        break;
                    case "SHUT":
                        out.println(Config.STOP_HOUR + ":" + Config.STOP_MINUTE);
                        break;
                    case "OP":
                        
                }
            }
            incoming.close();
            g.toTextArea(incoming.getInetAddress().getHostAddress() + " has disconnected");
        } catch (IOException ex) {
        }
    }
}
