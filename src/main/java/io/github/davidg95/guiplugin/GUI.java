/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import java.awt.CardLayout;
import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import javax.swing.text.DefaultCaret;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerKickEvent;
import org.bukkit.event.player.PlayerLoginEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.Calendar;
import java.util.Date;

/**
 * The GUI class.
 * @author David
 */
public class GUI extends javax.swing.JFrame implements Listener{
    ArrayList<Player> playerList = new ArrayList<>();
    private final String FILE = "GUIConfig.txt";
    /**
     * Creates new form GUI
     */
    public GUI(ArrayList playerList) {
        this.playerList = playerList;
        initComponents();
        updateOnline();
        try{
            String content = new String(Files.readAllBytes(Paths.get(FILE)));
            this.setTitle(content);
        }catch(IOException e){
            
        }
    }
    
    /**
     * Displays message saying functionality has not been implemented yet.
     */
    public void notImp(){
        JOptionPane.showMessageDialog(rootPane, "Not implemented yet!");
    }
    
    /**
     * Updates the list of current online players by reading the contents of the playerList ArrayList.
     */
    public void updateOnline(){
        DefaultListModel lm = new DefaultListModel();
        for(int i = 0; i < playerList.size(); i++){
            lm.addElement(playerList.get(i).getName().toString());
        }
        
        lstOnline.setModel(lm);
    }
    
    /**
     * Enters a String of text to the server logs and appends it to the teat area in the GUI.
     */
    public void enterToLogs(){
        Time time = new Time(new Date().getTime());
        String input = txtText.getText();
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say " + input);
        toTextArea("SERVER - " + input);
        txtText.setText("");
    }
    
    public void toTextArea(String input){
        Time time = new Time(new Date().getTime());
        txtLogs.append("[" + time.toString() + "] " + input + "\n");
    }
    
    /**
     * Ops a player if they are not op already and deops them if they are.
     */
    public void op(){
        int i = lstOnline.getSelectedIndex();
        if(i != -1){
            Player p = playerList.get(i);
            if(p.isOp()){
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "op " + p.getName());
            } else{
             Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "deop " + p.getName());
            }
        } else{
            toTextArea("Select a player!");
        }
    }
    
    /**
     * Kicks a player from the server.
     */
    public void kick(){
        int i = lstOnline.getSelectedIndex();
        String reason = JOptionPane.showInputDialog("Enter reason, leave blank for no reason.");
        if(i != -1){
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick " + playerList.get(i).getPlayer().getName() + " " + reason);
        } else{
            toTextArea("Select a player!");
        }
    }
    
    /**
     * Bans a player from the server by making a call to Bukkit.dispatchCommand().
     */
    public void ban(){
        int i = lstOnline.getSelectedIndex();
        if(i != -1){
            toTextArea("Banned player " + playerList.get(i).getPlayer().getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + playerList.get(i).getName());
        } else{
            toTextArea("Select a player!");
        }
    }
    
    /**
     * Calls the GUI asking if they are sure they want to stop the server.
     */
    public void stop(){
        new StopServerPrompt().setVisible(true);
    }
    
    /**
     * Saves the server by making a call to Bukkit.dispatchCommand().
     */
    public void save(){
        toTextArea("Server saved!");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
    }
    
    /**
     * Calls the GUI asking if they are sure they want to stop the server.
     */
    public void reload(){
        new ReloadServerPrompt().setVisible(true);
    }
    
    /**
     * Method which gets called whenever a player log into the server.
     * @param event holds details about the login including the player.
     */
    @EventHandler
    public void onLogin(PlayerLoginEvent event){
        playerList.add(event.getPlayer());
        updateOnline();
        toTextArea(event.getPlayer().getName() + " has joined!");
    }
    
    /**
     * Method which gets called whenever a player leaves the server.
     * @param event holds details about the quit including the player.
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event){
        for(int i = 0; i < playerList.size(); i++){
            if(event.getPlayer().equals(playerList.get(i))){
                playerList.remove(i);
            }
        }
        toTextArea(event.getPlayer().getName() + " has left");
        updateOnline();
    }
    
    /**
     * A method which gets called whenever a player gets kicked from the server.
     * @param event holds details about the kick such as the player.
     */
    @EventHandler
    public void onKick(PlayerKickEvent event){
        for(int i = 0; i < playerList.size(); i++){
            if(event.getPlayer().equals(playerList.get(i))){
                playerList.remove(i);
            }
        }
        toTextArea(event.getPlayer().getName() + " has been kicked with reason " + event.getReason());
        updateOnline();
    }
    
    /**
     * Method which gets called whenever someone talks in the chat.
     * @param e holds details about the chat such as the text and the player.
     */
    @EventHandler
    public void onChat(AsyncPlayerChatEvent  e){
        Time time = new Time(new Date().getTime());
        
        toTextArea(e.getPlayer().getName() + " - " + e.getMessage());
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        txtLogs = new javax.swing.JTextArea();
        txtText = new javax.swing.JTextField();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstOnline = new javax.swing.JList();
        cmdOp = new javax.swing.JButton();
        cmKick = new javax.swing.JButton();
        cmdBan = new javax.swing.JButton();
        cmdEnterText = new javax.swing.JButton();
        btnCloseGUI = new javax.swing.JButton();
        btnWeather = new javax.swing.JButton();
        btnTime = new javax.swing.JButton();
        btnDifficulty = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnReload = new javax.swing.JButton();
        Menus = new javax.swing.JPanel();
        WeatherPanel = new javax.swing.JPanel();
        btnClear = new javax.swing.JButton();
        btnRain = new javax.swing.JButton();
        btnThunder = new javax.swing.JButton();
        lblDuration = new javax.swing.JLabel();
        txtDuration = new javax.swing.JTextField();
        lblSeconds = new javax.swing.JLabel();
        TimePanel = new javax.swing.JPanel();
        btnMorning = new javax.swing.JButton();
        btnNoon = new javax.swing.JButton();
        btnEvening = new javax.swing.JButton();
        btnNight = new javax.swing.JButton();
        btnToggleCycle = new javax.swing.JButton();
        lblManualEntry = new javax.swing.JLabel();
        txtTimeEntry = new javax.swing.JTextField();
        btnEnterTime = new javax.swing.JButton();
        DifficultyPanel = new javax.swing.JPanel();
        btnPeaceful = new javax.swing.JButton();
        btnEasy = new javax.swing.JButton();
        btnNormal = new javax.swing.JButton();
        btnHard = new javax.swing.JButton();

        txtLogs.setEditable(false);
        txtLogs.setColumns(20);
        txtLogs.setRows(5);
        jScrollPane1.setViewportView(txtLogs);
        DefaultCaret caret = (DefaultCaret)txtLogs.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        txtText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTextActionPerformed(evt);
            }
        });

        lstOnline.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lstOnline);

        cmdOp.setText("Op/Deop");
        cmdOp.setMaximumSize(new java.awt.Dimension(73, 73));
        cmdOp.setMinimumSize(new java.awt.Dimension(73, 73));
        cmdOp.setPreferredSize(new java.awt.Dimension(73, 73));
        cmdOp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdOpActionPerformed(evt);
            }
        });

        cmKick.setText("Kick");
        cmKick.setPreferredSize(new java.awt.Dimension(73, 73));
        cmKick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmKickActionPerformed(evt);
            }
        });

        cmdBan.setText("Ban");
        cmdBan.setPreferredSize(new java.awt.Dimension(73, 73));
        cmdBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdBanActionPerformed(evt);
            }
        });

        cmdEnterText.setText("Enter");
        cmdEnterText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEnterTextActionPerformed(evt);
            }
        });

        btnCloseGUI.setText("Close GUI");
        btnCloseGUI.setPreferredSize(new java.awt.Dimension(140, 60));
        btnCloseGUI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseGUIActionPerformed(evt);
            }
        });

        btnWeather.setText("Weather Controls");
        btnWeather.setPreferredSize(new java.awt.Dimension(100, 100));
        btnWeather.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWeatherActionPerformed(evt);
            }
        });

        btnTime.setText("Time Controls");
        btnTime.setPreferredSize(new java.awt.Dimension(100, 100));
        btnTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimeActionPerformed(evt);
            }
        });

        btnDifficulty.setText("Difficulty Controls");
        btnDifficulty.setPreferredSize(new java.awt.Dimension(100, 100));
        btnDifficulty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDifficultyActionPerformed(evt);
            }
        });

        btnStop.setBackground(new java.awt.Color(255, 0, 0));
        btnStop.setText("STOP SERVER");
        btnStop.setPreferredSize(new java.awt.Dimension(140, 60));
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(0, 255, 0));
        btnSave.setText("SAVE SERVER");
        btnSave.setPreferredSize(new java.awt.Dimension(140, 60));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnReload.setBackground(new java.awt.Color(0, 0, 255));
        btnReload.setText("RELOAD SERVER");
        btnReload.setPreferredSize(new java.awt.Dimension(140, 60));
        btnReload.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnReloadActionPerformed(evt);
            }
        });

        Menus.setLayout(new java.awt.CardLayout());

        btnClear.setText("Clear");
        btnClear.setPreferredSize(new java.awt.Dimension(100, 100));
        btnClear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnClearActionPerformed(evt);
            }
        });

        btnRain.setText("Rain");
        btnRain.setPreferredSize(new java.awt.Dimension(100, 100));
        btnRain.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRainActionPerformed(evt);
            }
        });

        btnThunder.setText("Thunder");
        btnThunder.setPreferredSize(new java.awt.Dimension(100, 100));
        btnThunder.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnThunderActionPerformed(evt);
            }
        });

        lblDuration.setText("Duration:");

        txtDuration.setText("1800");

        lblSeconds.setText("seconds");

        javax.swing.GroupLayout WeatherPanelLayout = new javax.swing.GroupLayout(WeatherPanel);
        WeatherPanel.setLayout(WeatherPanelLayout);
        WeatherPanelLayout.setHorizontalGroup(
            WeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WeatherPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(WeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblDuration))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(WeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WeatherPanelLayout.createSequentialGroup()
                        .addComponent(btnRain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnThunder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(WeatherPanelLayout.createSequentialGroup()
                        .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSeconds)))
                .addContainerGap(321, Short.MAX_VALUE))
        );
        WeatherPanelLayout.setVerticalGroup(
            WeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WeatherPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(WeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThunder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30)
                .addGroup(WeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDuration)
                    .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSeconds))
                .addContainerGap(144, Short.MAX_VALUE))
        );

        Menus.add(WeatherPanel, "WeatherCard");

        btnMorning.setText("Morning");
        btnMorning.setPreferredSize(new java.awt.Dimension(100, 100));
        btnMorning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMorningActionPerformed(evt);
            }
        });

        btnNoon.setText("Noon");
        btnNoon.setPreferredSize(new java.awt.Dimension(100, 100));
        btnNoon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNoonActionPerformed(evt);
            }
        });

        btnEvening.setText("Evening");
        btnEvening.setPreferredSize(new java.awt.Dimension(100, 100));
        btnEvening.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEveningActionPerformed(evt);
            }
        });

        btnNight.setText("Night");
        btnNight.setPreferredSize(new java.awt.Dimension(100, 100));
        btnNight.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNightActionPerformed(evt);
            }
        });

        btnToggleCycle.setText("Toggle Cycle");
        btnToggleCycle.setPreferredSize(new java.awt.Dimension(100, 100));
        btnToggleCycle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnToggleCycleActionPerformed(evt);
            }
        });

        lblManualEntry.setText("Manual Entry:");

        txtTimeEntry.setText("0");

        btnEnterTime.setText("Enter");
        btnEnterTime.setPreferredSize(new java.awt.Dimension(100, 100));
        btnEnterTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEnterTimeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout TimePanelLayout = new javax.swing.GroupLayout(TimePanel);
        TimePanel.setLayout(TimePanelLayout);
        TimePanelLayout.setHorizontalGroup(
            TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TimePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblManualEntry)
                    .addComponent(btnMorning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnNoon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTimeEntry))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TimePanelLayout.createSequentialGroup()
                        .addComponent(btnEvening, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnNight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnToggleCycle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnEnterTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(109, Short.MAX_VALUE))
        );
        TimePanelLayout.setVerticalGroup(
            TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TimePanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnToggleCycle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEvening, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnNoon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMorning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnterTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimeEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblManualEntry))
                .addContainerGap(88, Short.MAX_VALUE))
        );

        Menus.add(TimePanel, "TimeCard");

        btnPeaceful.setText("Peaceful");
        btnPeaceful.setPreferredSize(new java.awt.Dimension(100, 100));
        btnPeaceful.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPeacefulActionPerformed(evt);
            }
        });

        btnEasy.setText("Easy");
        btnEasy.setPreferredSize(new java.awt.Dimension(100, 100));
        btnEasy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEasyActionPerformed(evt);
            }
        });

        btnNormal.setText("Normal");
        btnNormal.setPreferredSize(new java.awt.Dimension(100, 100));
        btnNormal.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNormalActionPerformed(evt);
            }
        });

        btnHard.setText("Hard");
        btnHard.setPreferredSize(new java.awt.Dimension(100, 100));
        btnHard.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnHardActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout DifficultyPanelLayout = new javax.swing.GroupLayout(DifficultyPanel);
        DifficultyPanel.setLayout(DifficultyPanelLayout);
        DifficultyPanelLayout.setHorizontalGroup(
            DifficultyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DifficultyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(btnPeaceful, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnEasy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNormal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnHard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(215, Short.MAX_VALUE))
        );
        DifficultyPanelLayout.setVerticalGroup(
            DifficultyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DifficultyPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(DifficultyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNormal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEasy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPeaceful, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(194, Short.MAX_VALUE))
        );

        Menus.add(DifficultyPanel, "DifficultyCard");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtText)
                            .addComponent(jScrollPane1))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(cmdOp, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmKick, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmdBan, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(cmdEnterText, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 231, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 421, Short.MAX_VALUE)
                        .addComponent(btnCloseGUI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnWeather, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnDifficulty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(Menus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 261, Short.MAX_VALUE)
                        .addComponent(jScrollPane1))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(cmdOp, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmKick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(cmdBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(9, 9, 9)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtText)
                    .addComponent(cmdEnterText, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnWeather, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDifficulty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(Menus, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCloseGUI, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cmdEnterTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdEnterTextActionPerformed
        enterToLogs();
    }//GEN-LAST:event_cmdEnterTextActionPerformed

    private void txtTextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtTextActionPerformed
        enterToLogs();
    }//GEN-LAST:event_txtTextActionPerformed

    private void btnCloseGUIActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseGUIActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_btnCloseGUIActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        stop();
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReloadActionPerformed
        reload();
    }//GEN-LAST:event_btnReloadActionPerformed

    private void cmdOpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdOpActionPerformed
        op();
    }//GEN-LAST:event_cmdOpActionPerformed

    private void cmKickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmKickActionPerformed
        kick();
    }//GEN-LAST:event_cmKickActionPerformed

    private void cmdBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cmdBanActionPerformed
        ban();
    }//GEN-LAST:event_cmdBanActionPerformed

    private void btnWeatherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWeatherActionPerformed
        CardLayout card = (CardLayout) Menus.getLayout();
        card.show(Menus, "WeatherCard");
    }//GEN-LAST:event_btnWeatherActionPerformed

    private void btnTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimeActionPerformed
        CardLayout card = (CardLayout) Menus.getLayout();
        card.show(Menus, "TimeCard");
    }//GEN-LAST:event_btnTimeActionPerformed

    private void btnDifficultyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDifficultyActionPerformed
        CardLayout card = (CardLayout) Menus.getLayout();
        card.show(Menus, "DifficultyCard");
    }//GEN-LAST:event_btnDifficultyActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        txtLogs.append("Set weather to clear for " + txtDuration.getText() + "\n");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "weather clear " + Integer.parseInt(txtDuration.getText()));
    }//GEN-LAST:event_btnClearActionPerformed

    private void btnRainActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRainActionPerformed
        toTextArea("Set weather to rain for " + txtDuration.getText());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "weather rain " + Integer.parseInt(txtDuration.getText()));
    }//GEN-LAST:event_btnRainActionPerformed

    private void btnThunderActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnThunderActionPerformed
        toTextArea("Set weather to thunder for " + txtDuration.getText());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "weather thunder " + Integer.parseInt(txtDuration.getText()));
    }//GEN-LAST:event_btnThunderActionPerformed

    private void btnMorningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMorningActionPerformed
        toTextArea("Set time to Morning (2000)");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set 2000");
    }//GEN-LAST:event_btnMorningActionPerformed

    private void btnNoonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNoonActionPerformed
        toTextArea("Set time to Noon (6000)");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set 6000");
    }//GEN-LAST:event_btnNoonActionPerformed

    private void btnEveningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEveningActionPerformed
        toTextArea("Set time to Evening (10000)");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set 10000");
    }//GEN-LAST:event_btnEveningActionPerformed

    private void btnNightActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNightActionPerformed
        toTextArea("Set time to Night (15000)");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set 15000");
    }//GEN-LAST:event_btnNightActionPerformed

    private void btnToggleCycleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnToggleCycleActionPerformed
        notImp();
    }//GEN-LAST:event_btnToggleCycleActionPerformed

    private void btnEnterTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEnterTimeActionPerformed
        toTextArea("Set time to " + txtTimeEntry.getText());
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "time set " + Integer.parseInt(txtTimeEntry.getText()));
    }//GEN-LAST:event_btnEnterTimeActionPerformed

    private void btnPeacefulActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPeacefulActionPerformed
        toTextArea("Set difficulty to Peaceful");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "difficulty peaceful");
    }//GEN-LAST:event_btnPeacefulActionPerformed

    private void btnEasyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEasyActionPerformed
        toTextArea("Set difficulty to Easy");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "difficulty easy");
    }//GEN-LAST:event_btnEasyActionPerformed

    private void btnNormalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNormalActionPerformed
        toTextArea("Set difficulty to Normal");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "difficulty normal");
    }//GEN-LAST:event_btnNormalActionPerformed

    private void btnHardActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnHardActionPerformed
        toTextArea("Set difficulty to Hard");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "difficulty hard");
    }//GEN-LAST:event_btnHardActionPerformed

    /**
     * @param args the command line arguments
     */
//    public static void main(String args[]) {
//        /* Set the Nimbus look and feel */
//        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
//        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
//         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
//         */
//        try {
//            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
//                if ("Nimbus".equals(info.getName())) {
//                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
//                    break;
//                }
//            }
//        } catch (ClassNotFoundException ex) {
//            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new GUI().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel DifficultyPanel;
    private javax.swing.JPanel Menus;
    private javax.swing.JPanel TimePanel;
    private javax.swing.JPanel WeatherPanel;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnCloseGUI;
    private javax.swing.JButton btnDifficulty;
    private javax.swing.JButton btnEasy;
    private javax.swing.JButton btnEnterTime;
    private javax.swing.JButton btnEvening;
    private javax.swing.JButton btnHard;
    private javax.swing.JButton btnMorning;
    private javax.swing.JButton btnNight;
    private javax.swing.JButton btnNoon;
    private javax.swing.JButton btnNormal;
    private javax.swing.JButton btnPeaceful;
    private javax.swing.JButton btnRain;
    private javax.swing.JButton btnReload;
    private javax.swing.JButton btnSave;
    private javax.swing.JButton btnStop;
    private javax.swing.JButton btnThunder;
    private javax.swing.JButton btnTime;
    private javax.swing.JButton btnToggleCycle;
    private javax.swing.JButton btnWeather;
    private javax.swing.JButton cmKick;
    private javax.swing.JButton cmdBan;
    private javax.swing.JButton cmdEnterText;
    private javax.swing.JButton cmdOp;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDuration;
    private javax.swing.JLabel lblManualEntry;
    private javax.swing.JLabel lblSeconds;
    private javax.swing.JList lstOnline;
    private javax.swing.JTextField txtDuration;
    private javax.swing.JTextArea txtLogs;
    private javax.swing.JTextField txtText;
    private javax.swing.JTextField txtTimeEntry;
    // End of variables declaration//GEN-END:variables
}
