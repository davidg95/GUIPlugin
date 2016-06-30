/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import java.awt.CardLayout;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
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
import java.sql.Time;
import java.util.Date;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;

/**
 * The GUI class.
 *
 * @author David
 */
public class GUI extends javax.swing.JFrame implements Listener {

    private ArrayList<Player> playerList = new ArrayList<>();
    private boolean isDecorated = false;
    private final Config c;

    private Desktop dt;
    private final String TS_DISCON = "C:\\Users\\David\\Desktop\\Disconnect RDP.lnk";

    private final String CODE = "3696";

    /**
     * Creates new form GUI
     *
     * @param playerList the list of online players.
     */
    public GUI(ArrayList playerList) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if (GUIPlugin.LOOK_FEEL.equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(GUI.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        this.playerList = playerList;
        initComponents();
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        txtLogs.setLineWrap(true);
        setStopTimeLabel("Server will stop at " + Config.STOP_HOUR + ":" + Config.STOP_MINUTE);
        updateOnline();
        c = new Config(this);
        updateConfig();
        toTextArea("Server " + c.SERVER_NAME + " running on port number " + Bukkit.getPort());
        toTextArea("Running " + Bukkit.getVersion() + " " + Bukkit.getBukkitVersion());
        toTextArea("Up to " + Bukkit.getMaxPlayers() + " players can be on at once");
        toTextArea((Bukkit.hasWhitelist() ? "Sever is whitelisted" : "Server is not whitelisted"));
        toTextArea((Bukkit.getOnlineMode() ? "Server authenticates clients" : "Server does not authenticate clients"));
        toTextArea("Spawn protection radius: " + Bukkit.getSpawnRadius());
        toTextArea((Bukkit.getAllowNether() ? "Nether is allowed" : "Nether is not allowed"));
        toTextArea((Bukkit.getAllowEnd() ? "End is allowed" : "End is not allowed"));
        toTextArea("Server will stop at " + Config.STOP_HOUR + ":" + Config.STOP_MINUTE);
        if (!Desktop.isDesktopSupported()) {
            btnDiconRDP.setEnabled(false);
            btn3DMap.setEnabled(false);
            btnBackup.setEnabled(false);
            JOptionPane.showMessageDialog(rootPane, "Warning, Desktop API not supported on this device, some buttons have been disabled.");
        } else {
            dt = Desktop.getDesktop();
        }
    }

    public final void updateConfig() {
        lblServerInfo.setText(c.SERVER_NAME + " running " + Bukkit.getVersion() + " " + Bukkit.getBukkitVersion() + " " + (Bukkit.getIp().equals("") ? "No Bound IP" : "IP Address: " + Bukkit.getIp()) + " Port Number: " + Bukkit.getPort());
        if (c.CUSTOM1_TEXT.equals("")) {
            btnCustom1.setEnabled(false);
            btnCustom1.setText("");
        } else {
            btnCustom1.setEnabled(true);
            btnCustom1.setText(c.CUSTOM1_TEXT);
        }
        if (c.CUSTOM2_TEXT.equals("")) {
            btnCustom2.setEnabled(false);
            btnCustom2.setText("");
        } else {
            btnCustom2.setEnabled(true);
            btnCustom2.setText(c.CUSTOM2_TEXT);
        }
        if (c.CUSTOM3_TEXT.equals("")) {
            btnCustom3.setEnabled(false);
            btnCustom3.setText("");
        } else {
            btnCustom3.setEnabled(true);
            btnCustom3.setText(c.CUSTOM3_TEXT);
        }
    }

    public final void setStopTimeLabel(String s) {
        lblStopTime.setText(s);
    }

    /**
     * Displays message saying functionality has not been implemented yet.
     */
    public void notImp() {
        JOptionPane.showMessageDialog(rootPane, "Not implemented yet!");
    }

    /**
     * Updates the list of current online players by reading the contents of the
     * playerList ArrayList.
     */
    public final void updateOnline() {
        DefaultListModel lm = new DefaultListModel();
        for (Player p : playerList) {
            lm.addElement(p.getName() + " " + (p.isOp() ? "*" : ""));
        }
        if (lm.isEmpty()) {
            lm.addElement("There are currently no players online");
        }
        lstOnline.setModel(lm);
        lblOnline.setText(playerList.size() + "/" + Bukkit.getMaxPlayers() + " players online");
    }

    /**
     * Enters a String of text to the server logs and appends it to the teat
     * area in the GUI.
     */
    public void enterToLogs() {
        String input = txtText.getText();
        if (!input.equals("")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say " + input);
            toTextArea("SERVER - " + input);
            txtText.setText("");
        }
    }

    public final void toTextArea(String input) {
        txtLogs.append("[" + new Time(new Date().getTime()).toString() + "] " + input + "\n");
    }

    /**
     * Ops a player if they are not op already and deops them if they are.
     */
    public void op() {
        int i = lstOnline.getSelectedIndex();
        if (i != -1) {
            Player p = playerList.get(i);
            if (p.isOp()) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "op " + p.getName());
            } else {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "deop " + p.getName());
            }
        } else {
            toTextArea("Select a player!");
        }
    }

    /**
     * Kicks a player from the server.
     */
    public void kick() {
        int i = lstOnline.getSelectedIndex();
        String reason = JOptionPane.showInputDialog("Enter reason, leave blank for no reason.");
        if (i != -1) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "kick " + playerList.get(i).getPlayer().getName() + " " + reason);
        } else {
            toTextArea("Select a player!");
        }
    }

    /**
     * Bans a player from the server by making a call to
     * Bukkit.dispatchCommand().
     */
    public void ban() {
        int i = lstOnline.getSelectedIndex();
        if (i != -1) {
            toTextArea("Banned player " + playerList.get(i).getPlayer().getName());
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + playerList.get(i).getName());
        } else {
            toTextArea("Select a player!");
        }
    }

    /**
     * Calls the GUI asking if they are sure they want to stop the server.
     */
    public void stop() {
        new StopServerPrompt().setVisible(true);
    }

    /**
     * Saves the server by making a call to Bukkit.dispatchCommand().
     */
    public void save() {
        toTextArea("Server saved!");
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "save-all");
    }

    /**
     * Calls the GUI asking if they are sure they want to stop the server.
     */
    public void reload() {
        new ReloadServerPrompt().setVisible(true);
    }

    public void renderMap() {
        try {
            File document = new File(".\\Overviewer.lnk");
            dt.open(document);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say ***Rendering 3D map***");
            toTextArea("***Rendering 3D map***");
        } catch (IOException ex) {
            toTextArea("***ERROR- Overviewer.lnk not found***");
        }
    }

    public void backup() {
        try {
            File document = new File(".\\Backup.lnk");
            dt.open(document);
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say ***Saving Backup***");
            toTextArea("***Saving Backup***");
        } catch (IOException ex) {
            toTextArea("***ERROR- Backup.lnk not found***");
        }
    }

    /**
     * Method which gets called whenever a player log into the server.
     *
     * @param event holds details about the login including the player.
     */
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (event.getResult().equals(Result.ALLOWED)) {
            playerList.add(event.getPlayer());
            updateOnline();
            toTextArea(event.getPlayer().getName() + " has joined!");
        } else {
            toTextArea("Player " + event.getPlayer().getName() + " couldnt join with reason: " + event.getResult().toString());
        }
    }

    /**
     * Method which gets called whenever a player leaves the server.
     *
     * @param event holds details about the quit including the player.
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        for (int i = 0; i < playerList.size(); i++) {
            if (event.getPlayer().equals(playerList.get(i))) {
                playerList.remove(i);
            }
        }
        toTextArea(event.getPlayer().getName() + " has left");
        updateOnline();
    }

    /**
     * A method which gets called whenever a player gets kicked from the server.
     *
     * @param event holds details about the kick such as the player.
     */
    @EventHandler
    public void onKick(PlayerKickEvent event) {
        for (int i = 0; i < playerList.size(); i++) {
            if (event.getPlayer().equals(playerList.get(i))) {
                playerList.remove(i);
            }
        }
        toTextArea(event.getPlayer().getName() + " has been kicked with reason " + event.getReason());
        updateOnline();
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        String name = event.getEntity().getName();
        String message = event.getDeathMessage();

        toTextArea(message);
    }

    /**
     * Method which gets called whenever someone talks in the chat.
     *
     * @param e holds details about the chat such as the text and the player.
     */
    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        Time time = new Time(new Date().getTime());

        toTextArea(e.getPlayer().getName() + " - " + e.getMessage());
    }

    public void setIsDecorated(boolean b) {
        this.isDecorated = b;
    }

    public void setBtnMinMaxTitle(String t) {
        btnMinMax.setText(t);
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
        cmdEnterText = new javax.swing.JButton();
        btnCloseGUI = new javax.swing.JButton();
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
        btnMinMax = new javax.swing.JButton();
        btnDispatchCommand = new javax.swing.JButton();
        btnConfig = new javax.swing.JButton();
        lblServerInfo = new javax.swing.JLabel();
        btnCustom1 = new javax.swing.JButton();
        btnCustom2 = new javax.swing.JButton();
        btnCustom3 = new javax.swing.JButton();
        txtPlayerDetails = new javax.swing.JButton();
        btnWhitelist = new javax.swing.JButton();
        btnBanList = new javax.swing.JButton();
        btnDiconRDP = new javax.swing.JButton();
        btn3DMap = new javax.swing.JButton();
        btnBackup = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        lblStopTime = new javax.swing.JLabel();
        btnWeather = new javax.swing.JToggleButton();
        btnTime = new javax.swing.JToggleButton();
        btnDifficulty = new javax.swing.JToggleButton();
        lblOnline = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();

        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setMaximumSize(new java.awt.Dimension(1024, 768));
        setMinimumSize(new java.awt.Dimension(1024, 768));
        setUndecorated(true);

        txtLogs.setEditable(false);
        txtLogs.setColumns(20);
        txtLogs.setRows(5);
        jScrollPane1.setViewportView(txtLogs);
        DefaultCaret caret = (DefaultCaret)txtLogs.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        txtText.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
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

        cmdEnterText.setText("Enter");
        cmdEnterText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEnterTextActionPerformed(evt);
            }
        });

        btnCloseGUI.setText("Close GUI");
        btnCloseGUI.setMargin(new java.awt.Insets(2, 0, 0, 0));
        btnCloseGUI.setPreferredSize(new java.awt.Dimension(146, 60));
        btnCloseGUI.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseGUIActionPerformed(evt);
            }
        });

        btnStop.setBackground(new java.awt.Color(255, 0, 0));
        btnStop.setText("STOP SERVER");
        btnStop.setMargin(new java.awt.Insets(2, 0, 0, 0));
        btnStop.setPreferredSize(new java.awt.Dimension(146, 60));
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        btnSave.setBackground(new java.awt.Color(0, 255, 0));
        btnSave.setText("SAVE SERVER");
        btnSave.setMargin(new java.awt.Insets(2, 0, 0, 0));
        btnSave.setPreferredSize(new java.awt.Dimension(146, 60));
        btnSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveActionPerformed(evt);
            }
        });

        btnReload.setBackground(new java.awt.Color(0, 0, 255));
        btnReload.setText("RELOAD SERVER");
        btnReload.setMargin(new java.awt.Insets(2, 0, 0, 0));
        btnReload.setPreferredSize(new java.awt.Dimension(146, 60));
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
                .addGroup(WeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, WeatherPanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblDuration)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(WeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(WeatherPanelLayout.createSequentialGroup()
                        .addComponent(btnRain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnThunder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(WeatherPanelLayout.createSequentialGroup()
                        .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSeconds)))
                .addContainerGap(343, Short.MAX_VALUE))
        );
        WeatherPanelLayout.setVerticalGroup(
            WeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(WeatherPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(WeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnClear, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnRain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnThunder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(40, 40, 40)
                .addGroup(WeatherPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lblDuration)
                    .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblSeconds))
                .addContainerGap(146, Short.MAX_VALUE))
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
                .addGroup(TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnMorning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, TimePanelLayout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblManualEntry)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                .addGroup(TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(btnNoon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtTimeEntry))
                .addGap(0, 0, 0)
                .addGroup(TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(TimePanelLayout.createSequentialGroup()
                        .addComponent(btnEvening, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnNight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnToggleCycle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnEnterTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(143, Short.MAX_VALUE))
        );
        TimePanelLayout.setVerticalGroup(
            TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(TimePanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnToggleCycle, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNight, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEvening, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(btnNoon, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnMorning, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
                .addGap(0, 0, 0)
                .addGroup(TimePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnEnterTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtTimeEntry, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblManualEntry))
                .addContainerGap(106, Short.MAX_VALUE))
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
                .addGap(0, 0, 0)
                .addComponent(btnPeaceful, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnEasy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnNormal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnHard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(243, Short.MAX_VALUE))
        );
        DifficultyPanelLayout.setVerticalGroup(
            DifficultyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(DifficultyPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(DifficultyPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnHard, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnNormal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnEasy, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnPeaceful, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(206, Short.MAX_VALUE))
        );

        Menus.add(DifficultyPanel, "DifficultyCard");

        btnMinMax.setText("Minimize GUI");
        btnMinMax.setMargin(new java.awt.Insets(2, 0, 0, 0));
        btnMinMax.setPreferredSize(new java.awt.Dimension(146, 60));
        btnMinMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinMaxActionPerformed(evt);
            }
        });

        btnDispatchCommand.setText("DISPATCH COMMAND");
        btnDispatchCommand.setMargin(new java.awt.Insets(2, 0, 0, 0));
        btnDispatchCommand.setPreferredSize(new java.awt.Dimension(146, 60));
        btnDispatchCommand.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDispatchCommandActionPerformed(evt);
            }
        });

        btnConfig.setText("Config");
        btnConfig.setMargin(new java.awt.Insets(2, 0, 0, 0));
        btnConfig.setPreferredSize(new java.awt.Dimension(146, 60));
        btnConfig.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnConfigActionPerformed(evt);
            }
        });

        btnCustom1.setEnabled(false);
        btnCustom1.setPreferredSize(new java.awt.Dimension(100, 100));
        btnCustom1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustom1ActionPerformed(evt);
            }
        });

        btnCustom2.setEnabled(false);
        btnCustom2.setPreferredSize(new java.awt.Dimension(100, 100));
        btnCustom2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustom2ActionPerformed(evt);
            }
        });

        btnCustom3.setEnabled(false);
        btnCustom3.setPreferredSize(new java.awt.Dimension(100, 100));
        btnCustom3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustom3ActionPerformed(evt);
            }
        });

        txtPlayerDetails.setText("Player Details");
        txtPlayerDetails.setMaximumSize(new java.awt.Dimension(73, 73));
        txtPlayerDetails.setMinimumSize(new java.awt.Dimension(73, 73));
        txtPlayerDetails.setPreferredSize(new java.awt.Dimension(73, 73));
        txtPlayerDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlayerDetailsActionPerformed(evt);
            }
        });

        btnWhitelist.setText("Whitelist");
        btnWhitelist.setPreferredSize(new java.awt.Dimension(73, 73));
        btnWhitelist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWhitelistActionPerformed(evt);
            }
        });

        btnBanList.setText("Ban List");
        btnBanList.setPreferredSize(new java.awt.Dimension(73, 73));
        btnBanList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBanListActionPerformed(evt);
            }
        });

        btnDiconRDP.setBackground(new java.awt.Color(255, 0, 0));
        btnDiconRDP.setText("Disconnect RDP");
        btnDiconRDP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiconRDPActionPerformed(evt);
            }
        });

        btn3DMap.setText("Render 3D Map");
        btn3DMap.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btn3DMap.setPreferredSize(new java.awt.Dimension(100, 100));
        btn3DMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3DMapActionPerformed(evt);
            }
        });

        btnBackup.setText("Backup");
        btnBackup.setPreferredSize(new java.awt.Dimension(100, 100));
        btnBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackupActionPerformed(evt);
            }
        });

        jButton1.setEnabled(false);
        jButton1.setPreferredSize(new java.awt.Dimension(100, 100));
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btnWeather.setSelected(true);
        btnWeather.setText("Weather");
        btnWeather.setPreferredSize(new java.awt.Dimension(100, 100));
        btnWeather.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWeatherActionPerformed(evt);
            }
        });

        btnTime.setText("Time");
        btnTime.setPreferredSize(new java.awt.Dimension(100, 100));
        btnTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimeActionPerformed(evt);
            }
        });

        btnDifficulty.setText("Diffuculty");
        btnDifficulty.setPreferredSize(new java.awt.Dimension(100, 100));
        btnDifficulty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDifficultyActionPerformed(evt);
            }
        });

        jLabel1.setText("* - Op");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCustom3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btn3DMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnBackup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnWeather, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnDifficulty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(Menus, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnDispatchCommand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnConfig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addComponent(btnMinMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnCloseGUI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lblServerInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addComponent(txtText)
                    .addComponent(jScrollPane1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addGroup(layout.createSequentialGroup()
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(txtPlayerDetails, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnWhitelist, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(btnBanList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                            .addComponent(cmdEnterText, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(18, 18, 18)
                            .addComponent(lblOnline, javax.swing.GroupLayout.PREFERRED_SIZE, 96, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGap(33, 33, 33)
                            .addComponent(jLabel1)
                            .addContainerGap()))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(lblStopTime, javax.swing.GroupLayout.PREFERRED_SIZE, 139, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnDiconRDP))))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(5, 5, 5)
                        .addComponent(lblServerInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnDiconRDP, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(lblStopTime, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(txtPlayerDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnWhitelist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnBanList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.DEFAULT_SIZE, 302, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(cmdEnterText, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(lblOnline, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnWeather, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(0, 0, 0)
                                .addComponent(btnDifficulty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCustom3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn3DMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBackup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(Menus, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 27, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnDispatchCommand, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMinMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCloseGUI, javax.swing.GroupLayout.PREFERRED_SIZE, 61, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfig, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))
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
        if (Config.GUI_LOCK) {
            CodeEntry.showCodeEntryDialog("Close GUI", CODE, new Runnable() {
                @Override
                public void run() {
                    setVisible(false);
                }
            });
        } else {
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnCloseGUIActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        if (Config.GUI_LOCK) {
            CodeEntry.showCodeEntryDialog("Stop Server", CODE, new Runnable() {
                @Override
                public void run() {
                    stop();
                }
            });
        } else {
            stop();
        }
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReloadActionPerformed
        if (Config.GUI_LOCK) {
            CodeEntry.showCodeEntryDialog("Reload Server", CODE, new Runnable() {
                @Override
                public void run() {
                    reload();
                }
            });
        } else {
            reload();
        }
    }//GEN-LAST:event_btnReloadActionPerformed

    private void btnClearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnClearActionPerformed
        toTextArea("Set weather to clear for " + txtDuration.getText());
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

    private void btnMinMaxActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnMinMaxActionPerformed
        if (isDecorated) {
            GUIPlugin.maximize();
        } else {
            if (Config.GUI_LOCK) {
                CodeEntry.showCodeEntryDialog("Minimize GUI", CODE, new Runnable() {
                    @Override
                    public void run() {
                        GUIPlugin.minimize();
                    }
                });
            } else {
                GUIPlugin.minimize();
            }
        }
    }//GEN-LAST:event_btnMinMaxActionPerformed

    private void btnDispatchCommandActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDispatchCommandActionPerformed
        String command = JOptionPane.showInputDialog("Enter command to send to server:");

        if (command != null && !command.equals("")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
            toTextArea("Dispatched command- " + command);
        }
    }//GEN-LAST:event_btnDispatchCommandActionPerformed

    private void btnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigActionPerformed
        if (Config.GUI_LOCK) {
            CodeEntry.showCodeEntryDialog("Config Window", CODE, new Runnable() {
                public void run() {
                    c.setVisible(true);
                }
            });
        } else {
            c.setVisible(true);
        }
    }//GEN-LAST:event_btnConfigActionPerformed

    private void btnCustom1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustom1ActionPerformed
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c.CUSTOM1_COMMAND);
        //toTextArea("Dispatched command- " + c.CUSTOM1_COMMAND);
    }//GEN-LAST:event_btnCustom1ActionPerformed

    private void btnCustom2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustom2ActionPerformed
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c.CUSTOM2_COMMAND);
        toTextArea("Dispatched command- " + c.CUSTOM2_COMMAND);
    }//GEN-LAST:event_btnCustom2ActionPerformed

    private void btnCustom3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustom3ActionPerformed
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), c.CUSTOM3_COMMAND);
        toTextArea("Dispatched command- " + c.CUSTOM3_COMMAND);
    }//GEN-LAST:event_btnCustom3ActionPerformed

    private void txtPlayerDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlayerDetailsActionPerformed
        int i = lstOnline.getSelectedIndex();

        if (i == -1) {
            JOptionPane.showMessageDialog(rootPane, "Select a player!", "Player Details", JOptionPane.ERROR_MESSAGE);
            //new PlayerDetails().setVisible(true);
        } else {
            if (!playerList.isEmpty()) {
                new PlayerDetails(playerList.get(i)).setVisible(true);
            } else {
                JOptionPane.showMessageDialog(rootPane, "Select a player!", "Player Details", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtPlayerDetailsActionPerformed

    private void btnWhitelistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWhitelistActionPerformed
        new Whitelist(this).setVisible(true);
    }//GEN-LAST:event_btnWhitelistActionPerformed

    private void btnDiconRDPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiconRDPActionPerformed
        try {
            File document = new File(TS_DISCON);
            dt.open(document);
        } catch (IOException ex) {

        }
    }//GEN-LAST:event_btnDiconRDPActionPerformed

    private void btn3DMapActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn3DMapActionPerformed
        renderMap();
    }//GEN-LAST:event_btn3DMapActionPerformed

    private void btnBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackupActionPerformed
        backup();
    }//GEN-LAST:event_btnBackupActionPerformed

    private void btnBanListActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBanListActionPerformed
        new BanList(this).setVisible(true);
    }//GEN-LAST:event_btnBanListActionPerformed

    private void btnWeatherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWeatherActionPerformed
        btnWeather.setSelected(true);
        btnTime.setSelected(false);
        btnDifficulty.setSelected(false);
        CardLayout card = (CardLayout) Menus.getLayout();
        card.show(Menus, "WeatherCard");
    }//GEN-LAST:event_btnWeatherActionPerformed

    private void btnTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimeActionPerformed
        btnWeather.setSelected(false);
        btnTime.setSelected(true);
        btnDifficulty.setSelected(false);
        CardLayout card = (CardLayout) Menus.getLayout();
        card.show(Menus, "TimeCard");
    }//GEN-LAST:event_btnTimeActionPerformed

    private void btnDifficultyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDifficultyActionPerformed
        btnWeather.setSelected(false);
        btnTime.setSelected(false);
        btnDifficulty.setSelected(true);
        CardLayout card = (CardLayout) Menus.getLayout();
        card.show(Menus, "DifficultyCard");
    }//GEN-LAST:event_btnDifficultyActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        //new Help().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

//    /**
//     * @param args the command line arguments
//     */
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
//                new GUI(new ArrayList<Player>()).setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel DifficultyPanel;
    private javax.swing.JPanel Menus;
    private javax.swing.JPanel TimePanel;
    private javax.swing.JPanel WeatherPanel;
    private javax.swing.JButton btn3DMap;
    private javax.swing.JButton btnBackup;
    private javax.swing.JButton btnBanList;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnCloseGUI;
    private javax.swing.JButton btnConfig;
    private javax.swing.JButton btnCustom1;
    private javax.swing.JButton btnCustom2;
    private javax.swing.JButton btnCustom3;
    private javax.swing.JButton btnDiconRDP;
    private javax.swing.JToggleButton btnDifficulty;
    private javax.swing.JButton btnDispatchCommand;
    private javax.swing.JButton btnEasy;
    private javax.swing.JButton btnEnterTime;
    private javax.swing.JButton btnEvening;
    private javax.swing.JButton btnHard;
    private javax.swing.JButton btnMinMax;
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
    private javax.swing.JToggleButton btnTime;
    private javax.swing.JButton btnToggleCycle;
    private javax.swing.JToggleButton btnWeather;
    private javax.swing.JButton btnWhitelist;
    private javax.swing.JButton cmdEnterText;
    private javax.swing.JButton jButton1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDuration;
    private javax.swing.JLabel lblManualEntry;
    private javax.swing.JLabel lblOnline;
    private javax.swing.JLabel lblSeconds;
    private javax.swing.JLabel lblServerInfo;
    private javax.swing.JLabel lblStopTime;
    private javax.swing.JList lstOnline;
    private javax.swing.JTextField txtDuration;
    private javax.swing.JTextArea txtLogs;
    private javax.swing.JButton txtPlayerDetails;
    private javax.swing.JTextField txtText;
    private javax.swing.JTextField txtTimeEntry;
    // End of variables declaration//GEN-END:variables
}
