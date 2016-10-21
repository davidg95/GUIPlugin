/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.ComponentOrientation;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
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
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerLoginEvent.Result;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.event.server.ServerCommandEvent;

/**
 * The GUI class.
 *
 * @author David
 */
public class GUI extends javax.swing.JFrame implements Listener, GUIInterface {

    private ArrayList<Player> playerList = new ArrayList<>(); //Stores the current online players

    private boolean isDecorated = false; //Indicates whether the GUI is in full screen or not
    private final MouseListener mouseClick; //Mouselistener for the double click to make the GUI full screen

    protected static Timer timWarning = new Timer(); //Timer for dispalying the warning message
    protected static Timer timStop = new Timer(); //Timer for stopping the server
    protected ClockThread clockThread; //The thread for the clock in the top left corner
    protected FlashCompThread stopPanelThread; //The thread to make the top panel flash red

    private Desktop dt; //Desktop object for calling batch files
    private final String TS_DISCON = "C:\\Users\\David\\Desktop\\Disconnect RDP.lnk"; //URL of link for batch file to disconnect an RDP session

    private final String CODE = "3696"; //The code for the GUI

    private final CardLayout card; //Card layout for the server controls

    private boolean commandMode; //Indicates which mode the text entry field is in
    private boolean lockDown; //Indicates whether the server is in lock down

    /**
     * Creates new form GUI
     *
     * @param playerList the list of online players.
     */
    public GUI(ArrayList playerList) {
        this.playerList = playerList;
        initComponents();
        card = (CardLayout) Menus.getLayout();
        mouseClick = new MouseClickListener(); //Listener for the double click action to maximise the GUI.
        this.addMouseListener((MouseListener) mouseClick);
        clockThread = new ClockThread(lblTime); //Thread for the clock display.
        clockThread.start();
        jScrollPane1.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        jScrollPane2.setVerticalScrollBarPolicy(javax.swing.JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        txtLogs.setLineWrap(true);
        updateOnline();
        Config.initConfig(this);
        this.setTitle(Config.SERVER_NAME + " " + Bukkit.getBukkitVersion());
        setStopTimeLabel("Server will stop at " + Config.STOP_HOUR + ":" + Config.STOP_MINUTE);
        toTextArea("Server " + Config.SERVER_NAME + " running on port number " + Bukkit.getPort());
        toTextArea("Running " + Bukkit.getVersion() + " " + Bukkit.getBukkitVersion());
        toTextArea("Processor Cores: " + Runtime.getRuntime().availableProcessors());
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
            BigPrompt.showMessageDialog("Desktop API Not Supported", "Warning, Desktop API not supported on this device, some buttons have been disabled.");
        } else {
            dt = Desktop.getDesktop();
        }
    }

    @Override
    public void displayConnectionMessage() {

    }

    public class MouseClickListener implements MouseListener {

        @Override
        public void mouseClicked(MouseEvent evt) {
            if (evt.getClickCount() == 2) {
                if (isDecorated) {
                    GUIPlugin.maximize();
                }
            }

        }

        @Override
        public void mousePressed(MouseEvent e) {

        }

        @Override
        public void mouseReleased(MouseEvent e) {

        }

        @Override
        public void mouseEntered(MouseEvent e) {

        }

        @Override
        public void mouseExited(MouseEvent e) {

        }

    }

    public class FlashCompThread extends Thread {

        protected boolean isRunning;

        protected JComponent flashComp;

        protected boolean isRed;

        public FlashCompThread(JComponent flashComp) {
            this.flashComp = flashComp;
            this.isRunning = false;
            this.isRed = false;
        }

        @Override
        public void run() {
            while (isRunning) {
                SwingUtilities.invokeLater(() -> {
                    if (isRed) {
                        flashComp.setBackground(null);
                        isRed = false;
                    } else {
                        flashComp.setBackground(Color.RED);
                        isRed = true;
                    }
                });

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                }
            }
        }

        public void setRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }
    }

    public class ClockThread extends Thread {

        protected boolean isRunning;

        protected JLabel dateTimeLabel;

        protected SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");

        public ClockThread(JLabel dateTimeLabel) {
            this.dateTimeLabel = dateTimeLabel;
            this.isRunning = true;
        }

        @Override
        public void run() {
            while (isRunning) {
                SwingUtilities.invokeLater(() -> {
                    Calendar currentCalendar = Calendar.getInstance();
                    Date currentTime = currentCalendar.getTime();
                    dateTimeLabel.setText(timeFormat.format(currentTime));
                });

                try {
                    Thread.sleep(5000L);
                } catch (InterruptedException e) {
                }
            }
        }

        public void setRunning(boolean isRunning) {
            this.isRunning = isRunning;
        }

    }

    /**
     * Update the GUI according to the config.
     */
    @Override
    public final void updateConfig() {
        if (lockDown) {
            lblServerInfo.setText("Server- " + Config.SERVER_NAME + " LOCKDOWN");
        } else {
            lblServerInfo.setText("Server- " + Config.SERVER_NAME);
        }
        this.setTitle(Config.SERVER_NAME + " " + Bukkit.getBukkitVersion());
        if (Config.CUSTOM1_TEXT.equals("")) {
            btnCustom1.setEnabled(false);
            btnCustom1.setText("");
        } else {
            btnCustom1.setEnabled(true);
            btnCustom1.setText(Config.CUSTOM1_TEXT);
        }
        if (Config.CUSTOM2_TEXT.equals("")) {
            btnCustom2.setEnabled(false);
            btnCustom2.setText("");
        } else {
            btnCustom2.setEnabled(true);
            btnCustom2.setText(Config.CUSTOM2_TEXT);
        }
        if (Config.CUSTOM3_TEXT.equals("")) {
            btnCustom3.setEnabled(false);
            btnCustom3.setText("");
        } else {
            btnCustom3.setEnabled(true);
            btnCustom3.setText(Config.CUSTOM3_TEXT);
        }

        for (JButton button : Config.customButtons) {
            CustomPanel.add(button);
        }
        revalidate();
        repaint();
    }

    /**
     * Set the value of the label for the stop time.
     *
     * @param s the String to set as the value.
     */
    @Override
    public final void setStopTimeLabel(String s) {
        lblStopTime.setText(s);
    }

    /**
     * Displays message saying functionality has not been implemented yet.
     */
    public void notImp() {
        //JOptionPane.showMessageDialog(rootPane, "Not implemented yet!");
        BigPrompt.showMessageDialog("Not Implemented", "Not implemented yet!");
    }

    /**
     * Updates the list of current online players by reading the contents of the
     * playerList ArrayList.
     */
    @Override
    public final void updateOnline() {
        DefaultListModel lm = new DefaultListModel();
        for (Player pl : playerList) {
            lm.addElement(pl.getName() + " " + (pl.isOp() ? "*" : ""));
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
        if (commandMode) {
            if (!input.equals("")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), input);
                toTextArea(input);
            }
        } else {
            if (!input.equals("")) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say " + input);
                toTextArea("SERVER - " + input);
            }
        }
        txtText.setText("");
    }

    /**
     * Method to output to the text area. The time will be appended to the
     * start.
     *
     * @param input the String to output to the text area.
     */
    @Override
    public final void toTextArea(String input) {
        txtLogs.append("[" + new Time(new Date().getTime()).toString() + "] " + input + "\n");
    }

    @Override
    public void setLockDown(boolean lockDown) {
        this.lockDown = lockDown;
    }

    @Override
    public boolean isLockDown() {
        return this.lockDown;
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
     *
     * 0 - do nothing. 1 - normal stop. 2 - stop and shut down. 3 - stop and
     * sleep. 4 - normal stop with backup. 5 - shut down with backup. 6 - sleep
     * with backup.
     *
     * @param option the stop option.
     */
    public void stop(int option) {
        //int choice = StopServerOptions.showStopOptions();

        try {
            switch (option) {
                case 1:
                    //If stop with no backup was selected
                    if (Bukkit.getOnlinePlayers().size() > 0) {
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                                    }
                                },
                                5000
                        );
                        stopPanelThread = new FlashCompThread(topPanel);
                        stopPanelThread.start();
                        stopPanelThread.setRunning(true);
                        Bukkit.broadcastMessage("***SERVER STOPPING IN 5 SECONDS***");
                        JOptionPane.showMessageDialog(this, "Server will stop in 5 seconds", "Server Stop", JOptionPane.WARNING_MESSAGE);
                    } else {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                    }   break;
                case 2:
                    //If shutdown with no backup was selected
                    if (Bukkit.getOnlinePlayers().size() > 0) {
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        File document = new File(".\\shutdown.lnk");
                                        try {
                                            dt.open(document);
                                        } catch (IOException ex) {
                                            
                                        }
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                                    }
                                },
                                5000
                        );
                        stopPanelThread = new FlashCompThread(topPanel);
                        stopPanelThread.start();
                        stopPanelThread.setRunning(true);
                        Bukkit.broadcastMessage("***SERVER STOPPING IN 5 SECONDS***");
                        JOptionPane.showMessageDialog(this, "Server will stop in 5 seconds", "Server Stop", JOptionPane.WARNING_MESSAGE);
                    } else {
                        File document = new File(".\\shutdown.lnk");
                        dt.open(document);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                    }   break;
                case 3:
                    //If sleep with no backup was selected
                    if (Bukkit.getOnlinePlayers().size() > 0) {
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        File document = new File(".\\sleep.lnk");
                                        try {
                                            dt.open(document);
                                        } catch (IOException ex) {
                                            
                                        }
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                                    }
                                },
                                5000
                        );
                        stopPanelThread = new FlashCompThread(topPanel);
                        stopPanelThread.start();
                        stopPanelThread.setRunning(true);
                        Bukkit.broadcastMessage("***SERVER STOPPING IN 5 SECONDS***");
                        JOptionPane.showMessageDialog(this, "Server will stop in 5 seconds", "Server Stop", JOptionPane.WARNING_MESSAGE);
                    } else {
                        File document = new File(".\\sleep.lnk");
                        dt.open(document);
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                    }   break;
                case 4:
                    //If stop with backup was selected
                    backup();
                    if (Bukkit.getOnlinePlayers().size() > 0) {
                        new java.util.Timer().schedule(
                                new java.util.TimerTask() {
                                    @Override
                                    public void run() {
                                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                                    }
                                },
                                5000
                        );
                        stopPanelThread = new FlashCompThread(topPanel);
                        stopPanelThread.start();
                        stopPanelThread.setRunning(true);
                        Bukkit.broadcastMessage("***SERVER STOPPING IN 5 SECONDS***");
                        JOptionPane.showMessageDialog(this, "Server will stop in 5 seconds", "Server Stop", JOptionPane.WARNING_MESSAGE);
                    } else {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                    }   break;
                case 5:
                    //If shutdown with backup was selected
                    backup();
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    File document = new File(".\\shutdown.lnk");
                                    try {
                                        dt.open(document);
                                    } catch (IOException ex) {

                                    }
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                                }
                            },
                            10000
                    );  Bukkit.broadcastMessage("***SERVER STOPPING IN 10 SECONDS***");
                    JOptionPane.showMessageDialog(this, "Server will stop in 10 seconds", "Server Shutdown", JOptionPane.WARNING_MESSAGE);
                    break;
                case 6:
                    //If sleep with backup was selected
                    backup();
                    new java.util.Timer().schedule(
                            new java.util.TimerTask() {
                                @Override
                                public void run() {
                                    File document = new File(".\\sleep.lnk");
                                    try {
                                        dt.open(document);
                                    } catch (IOException ex) {

                                    }
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "stop");
                                }
                            },
                            10000
                    );  Bukkit.broadcastMessage("***SERVER STOPPING IN 10 SECONDS***");
                    JOptionPane.showMessageDialog(this, "Server will stop in 10 seconds", "Server Sleep", JOptionPane.WARNING_MESSAGE);
                    break;
                default:
                    break;
            }
        } catch (IOException ex) {

        }
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
            if (!Config.RENDER_MAP_URL.equals("")) {
                File file = new File(Config.RENDER_MAP_URL);
                dt.open(file);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say ***Rendering 3D map***");
                toTextArea("***Rendering 3D map***");
            } else{
                toTextArea("***ERROR- A batch script has not been set for rendering a map***");
            }
        } catch (IOException ex) {
            toTextArea("***ERROR- " + Config.RENDER_MAP_URL + " not found***");
        }
    }

    public void backup() {
        try {
            if (!Config.BACKUP_URL.equals("")) {
                File file = new File(Config.BACKUP_URL);
                dt.open(file);
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "say ***Saving Backup***");
                toTextArea("***Saving Backup***");
            } else{
                toTextArea("***ERROR- A batch script has not been set for saving a backup***");
            }
        } catch (IOException ex) {
            toTextArea("***ERROR- " + Config.BACKUP_URL + " not found***");
        }
    }

    /**
     * Method which gets called whenever a player log into the server.
     *
     * @param event holds details about the login including the player.
     */
    @EventHandler
    public void onLogin(PlayerLoginEvent event) {
        if (lockDown) {
            event.disallow(Result.KICK_OTHER, "The Server is currently in lockdown and no new players may join");
            toTextArea("Player " + event.getPlayer().getName() + " tried to join");
            Bukkit.broadcastMessage("Player " + event.getPlayer().getName() + " tried to join");

        } else {
            if (event.getResult().equals(Result.ALLOWED)) {
                playerList.add(event.getPlayer());
                updateOnline();
                toTextArea(event.getPlayer().getName() + " has joined!");
            } else {
                toTextArea("Player " + event.getPlayer().getName() + " couldnt join with reason: " + event.getResult().toString());
            }
        }
    }

    /**
     * Method which gets called whenever a player leaves the server.
     *
     * @param event holds details about the quit including the player.
     */
    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        for (int j = 0; j < playerList.size(); j++) {
            if (event.getPlayer().equals(playerList.get(j))) {
                playerList.remove(j);
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

    @EventHandler
    public void onTP(PlayerTeleportEvent e) {
        toTextArea(e.getPlayer().getName() + " has teleported to " + e.getTo().getBlockX() + " " + e.getTo().getBlockY() + " " + e.getTo().getBlockZ());
    }

    @EventHandler
    public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
        toTextArea(e.getPlayer().getName() + " entered command " + e.getMessage());
    }

    @EventHandler
    public void onServerCommand(ServerCommandEvent e) {
        toTextArea(e.getCommand());
    }

    public void setIsDecorated(boolean b) {
        this.isDecorated = b;
    }

    public void setBtnMinMaxTitle(String t) {
        btnMinMax.setText(t);
    }

    @Override
    public void serverStopTimer() {
        timWarning = new Timer();
        timStop = new Timer();
        Calendar warning = Calendar.getInstance();
        warning.set(Calendar.HOUR_OF_DAY, Config.WARNING_HOUR);
        warning.set(Calendar.MINUTE, Config.WARNING_MINUTE);
        warning.set(Calendar.SECOND, 0);
        // Schedule to run every night at 23:23
        timWarning.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        Bukkit.broadcastMessage(Config.WARNING_MESSAGE);
                        toTextArea(Config.WARNING_MESSAGE);
                        stopPanelThread = new FlashCompThread(topPanel);
                        stopPanelThread.start();
                        stopPanelThread.setRunning(true);
                        //backup();
                    }
                },
                warning.getTime(),
                TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        );
        Calendar stopper = Calendar.getInstance();
        stopper.set(Calendar.HOUR_OF_DAY, Config.STOP_HOUR);
        stopper.set(Calendar.MINUTE, Config.STOP_MINUTE);
        stopper.set(Calendar.SECOND, 0);
        // Schedule to run every night at 23:28
        timStop.schedule(
                new TimerTask() {
                    @Override
                    public void run() {
                        if (!Config.BACKUP_ON_CLOSE) {
                            switch (Config.SHUTDOWN_OPTION) {
                                case NOTHING:
                                    stop(1);
                                    break;
                                case SHUT_DOWN:
                                    stop(2);
                                    break;
                                case STANDBY:
                                    stop(3);
                                    break;
                                default:
                                    break;
                            }
                        } else {
                            switch (Config.SHUTDOWN_OPTION) {
                                case NOTHING:
                                    stop(4);
                                    break;
                                case SHUT_DOWN:
                                    stop(5);
                                    break;
                                case STANDBY:
                                    stop(6);
                                    break;
                                default:
                                    break;
                            }
                        }
                    }
                },
                stopper.getTime(),
                TimeUnit.MILLISECONDS.convert(1, TimeUnit.DAYS)
        );
    }

    @Override
    public void cancelStopTimer() {
        timWarning.cancel();
        timStop.cancel();
    }

    private void addButton(String text, String command) {
        JButton button = new JButton();
        button.setText(text);
        button.setMaximumSize(new java.awt.Dimension(100, 100));
        button.setMinimumSize(new java.awt.Dimension(100, 100));
        button.setPreferredSize(new java.awt.Dimension(100, 100));
        button.setSize(100, 100);
        button.setAction(new CustomButtonAction(text, command));
        CustomPanel.add(button);
        Config.customButtons.add(button);
        Config.saveProperties();
        revalidate();
        repaint();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        CARDS = new javax.swing.JPanel();
        Main = new javax.swing.JPanel();
        btnCloseGUI = new javax.swing.JButton();
        btnStop = new javax.swing.JButton();
        btnSave = new javax.swing.JButton();
        btnReload = new javax.swing.JButton();
        btnMinMax = new javax.swing.JButton();
        btnInfo = new javax.swing.JButton();
        btnConfig = new javax.swing.JButton();
        topPanel = new javax.swing.JPanel();
        btnDiconRDP = new javax.swing.JButton();
        lblStopTime = new javax.swing.JLabel();
        lblTime = new javax.swing.JLabel();
        lblServerInfo = new javax.swing.JLabel();
        logPanel = new javax.swing.JPanel();
        txtText = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        txtLogs = new javax.swing.JTextArea();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstOnline = new javax.swing.JList();
        txtPlayerDetails = new javax.swing.JButton();
        btnWhitelist = new javax.swing.JButton();
        btnBanList = new javax.swing.JButton();
        cmdEnterText = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        lblOnline = new javax.swing.JLabel();
        btnChatMode = new javax.swing.JButton();
        centerPanel = new javax.swing.JPanel();
        buttonsPanel = new javax.swing.JPanel();
        btnDifficulty = new javax.swing.JToggleButton();
        btnTime = new javax.swing.JToggleButton();
        btnBackup = new javax.swing.JButton();
        btnWeather = new javax.swing.JToggleButton();
        btn3DMap = new javax.swing.JButton();
        btnCustom3 = new javax.swing.JButton();
        btnCustom2 = new javax.swing.JButton();
        btnCustom1 = new javax.swing.JButton();
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
        CustomPanel = new javax.swing.JPanel();
        btnAddButton = new javax.swing.JButton();
        btnCustomButtons = new javax.swing.JToggleButton();
        Lock = new javax.swing.JPanel();
        btnUnlock = new javax.swing.JButton();

        setTitle(Config.SERVER_NAME + " " + Bukkit.getBukkitVersion());
        setExtendedState(javax.swing.JFrame.MAXIMIZED_BOTH);
        setMaximumSize(new java.awt.Dimension(1024, 768));
        setMinimumSize(new java.awt.Dimension(1024, 768));
        setUndecorated(true);

        CARDS.setMaximumSize(new java.awt.Dimension(1024, 768));
        CARDS.setPreferredSize(new java.awt.Dimension(1024, 768));
        CARDS.setLayout(new java.awt.CardLayout());

        Main.setMaximumSize(new java.awt.Dimension(1024, 768));
        Main.setMinimumSize(new java.awt.Dimension(1024, 768));

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

        btnMinMax.setText("Minimize GUI");
        btnMinMax.setMargin(new java.awt.Insets(2, 0, 0, 0));
        btnMinMax.setPreferredSize(new java.awt.Dimension(146, 60));
        btnMinMax.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnMinMaxActionPerformed(evt);
            }
        });

        btnInfo.setText("Info");
        btnInfo.setMargin(new java.awt.Insets(2, 0, 0, 0));
        btnInfo.setPreferredSize(new java.awt.Dimension(146, 60));
        btnInfo.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnInfoActionPerformed(evt);
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

        topPanel.setMaximumSize(new java.awt.Dimension(1024, 31));
        topPanel.setMinimumSize(new java.awt.Dimension(1024, 31));
        topPanel.setPreferredSize(new java.awt.Dimension(1024, 31));
        topPanel.setRequestFocusEnabled(false);

        btnDiconRDP.setBackground(new java.awt.Color(255, 0, 0));
        btnDiconRDP.setText("Disconnect RDP");
        btnDiconRDP.setMaximumSize(new java.awt.Dimension(107, 31));
        btnDiconRDP.setMinimumSize(new java.awt.Dimension(107, 31));
        btnDiconRDP.setPreferredSize(new java.awt.Dimension(107, 31));
        btnDiconRDP.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDiconRDPActionPerformed(evt);
            }
        });

        lblStopTime.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        lblStopTime.setPreferredSize(new java.awt.Dimension(1024, 23));

        lblTime.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        lblServerInfo.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N

        javax.swing.GroupLayout topPanelLayout = new javax.swing.GroupLayout(topPanel);
        topPanel.setLayout(topPanelLayout);
        topPanelLayout.setHorizontalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(topPanelLayout.createSequentialGroup()
                .addComponent(lblTime, javax.swing.GroupLayout.PREFERRED_SIZE, 113, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(38, 38, 38)
                .addComponent(lblServerInfo, javax.swing.GroupLayout.PREFERRED_SIZE, 412, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(lblStopTime, javax.swing.GroupLayout.PREFERRED_SIZE, 261, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnDiconRDP, javax.swing.GroupLayout.PREFERRED_SIZE, 128, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0))
        );
        topPanelLayout.setVerticalGroup(
            topPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(lblStopTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(lblTime, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(btnDiconRDP, javax.swing.GroupLayout.DEFAULT_SIZE, 34, Short.MAX_VALUE)
            .addComponent(lblServerInfo, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        logPanel.setMaximumSize(new java.awt.Dimension(1024, 340));
        logPanel.setMinimumSize(new java.awt.Dimension(1024, 340));
        logPanel.setName(""); // NOI18N
        logPanel.setPreferredSize(new java.awt.Dimension(1024, 340));

        txtText.setFont(new java.awt.Font("Tahoma", 0, 24)); // NOI18N
        txtText.setPreferredSize(new java.awt.Dimension(5, 35));
        txtText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtTextActionPerformed(evt);
            }
        });

        jScrollPane1.setPreferredSize(new java.awt.Dimension(166, 301));

        txtLogs.setEditable(false);
        txtLogs.setColumns(20);
        txtLogs.setRows(5);
        jScrollPane1.setViewportView(txtLogs);
        DefaultCaret caret = (DefaultCaret)txtLogs.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

        jScrollPane2.setPreferredSize(new java.awt.Dimension(35, 301));

        lstOnline.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstOnline.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstOnlineMouseClicked(evt);
            }
        });
        jScrollPane2.setViewportView(lstOnline);

        txtPlayerDetails.setText("<html><center>Player<br />Details</center></html>");
        txtPlayerDetails.setMargin(new java.awt.Insets(0, 0, 0, 0));
        txtPlayerDetails.setMaximumSize(new java.awt.Dimension(80, 73));
        txtPlayerDetails.setMinimumSize(new java.awt.Dimension(80, 73));
        txtPlayerDetails.setPreferredSize(new java.awt.Dimension(73, 73));
        txtPlayerDetails.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtPlayerDetailsActionPerformed(evt);
            }
        });

        btnWhitelist.setText("Whitelist");
        btnWhitelist.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnWhitelist.setMaximumSize(new java.awt.Dimension(73, 73));
        btnWhitelist.setMinimumSize(new java.awt.Dimension(73, 73));
        btnWhitelist.setPreferredSize(new java.awt.Dimension(73, 73));
        btnWhitelist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnWhitelistActionPerformed(evt);
            }
        });

        btnBanList.setText("Ban List");
        btnBanList.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnBanList.setMaximumSize(new java.awt.Dimension(73, 73));
        btnBanList.setMinimumSize(new java.awt.Dimension(73, 73));
        btnBanList.setPreferredSize(new java.awt.Dimension(73, 73));
        btnBanList.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBanListActionPerformed(evt);
            }
        });

        cmdEnterText.setText("Enter");
        cmdEnterText.setMaximumSize(new java.awt.Dimension(73, 23));
        cmdEnterText.setMinimumSize(new java.awt.Dimension(73, 23));
        cmdEnterText.setPreferredSize(new java.awt.Dimension(73, 23));
        cmdEnterText.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cmdEnterTextActionPerformed(evt);
            }
        });

        jLabel1.setText("* - Op");

        btnChatMode.setText("Command Mode");
        btnChatMode.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnChatMode.setMaximumSize(new java.awt.Dimension(73, 73));
        btnChatMode.setMinimumSize(new java.awt.Dimension(73, 73));
        btnChatMode.setOpaque(false);
        btnChatMode.setPreferredSize(new java.awt.Dimension(73, 73));
        btnChatMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnChatModeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout logPanelLayout = new javax.swing.GroupLayout(logPanel);
        logPanel.setLayout(logPanelLayout);
        logPanelLayout.setHorizontalGroup(
            logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, logPanelLayout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 702, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtText, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 702, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(cmdEnterText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(txtPlayerDetails, javax.swing.GroupLayout.PREFERRED_SIZE, 73, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                            .addComponent(btnWhitelist, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(btnBanList, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addComponent(btnChatMode, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(0, 0, 0)
                .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(logPanelLayout.createSequentialGroup()
                        .addComponent(lblOnline, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(29, 29, 29)
                        .addComponent(jLabel1))
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );
        logPanelLayout.setVerticalGroup(
            logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, logPanelLayout.createSequentialGroup()
                .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(logPanelLayout.createSequentialGroup()
                        .addComponent(txtPlayerDetails, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnWhitelist, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnBanList, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnChatMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, logPanelLayout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblOnline, javax.swing.GroupLayout.PREFERRED_SIZE, 32, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(logPanelLayout.createSequentialGroup()
                        .addGap(4, 4, 4)
                        .addGroup(logPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtText, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel1)
                            .addComponent(cmdEnterText, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        centerPanel.setLayout(new java.awt.CardLayout());

        btnDifficulty.setText("Difficulty");
        btnDifficulty.setPreferredSize(new java.awt.Dimension(100, 100));
        btnDifficulty.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDifficultyActionPerformed(evt);
            }
        });

        btnTime.setText("Time");
        btnTime.setPreferredSize(new java.awt.Dimension(100, 100));
        btnTime.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnTimeActionPerformed(evt);
            }
        });

        btnBackup.setText("Backup");
        btnBackup.setPreferredSize(new java.awt.Dimension(100, 100));
        btnBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackupActionPerformed(evt);
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

        btn3DMap.setText("Render 3D Map");
        btn3DMap.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btn3DMap.setPreferredSize(new java.awt.Dimension(100, 100));
        btn3DMap.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn3DMapActionPerformed(evt);
            }
        });

        btnCustom3.setEnabled(false);
        btnCustom3.setPreferredSize(new java.awt.Dimension(100, 100));
        btnCustom3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustom3ActionPerformed(evt);
            }
        });

        btnCustom2.setEnabled(false);
        btnCustom2.setPreferredSize(new java.awt.Dimension(100, 100));
        btnCustom2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustom2ActionPerformed(evt);
            }
        });

        btnCustom1.setEnabled(false);
        btnCustom1.setPreferredSize(new java.awt.Dimension(100, 100));
        btnCustom1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustom1ActionPerformed(evt);
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
                        .addComponent(txtDuration, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblSeconds))
                    .addGroup(WeatherPanelLayout.createSequentialGroup()
                        .addComponent(btnRain, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnThunder, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
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

        btnToggleCycle.setText("<html><center>Toggle<br />Cycle</center></html>");
        btnToggleCycle.setEnabled(false);
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
                .addContainerGap(166, Short.MAX_VALUE))
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
                .addContainerGap(266, Short.MAX_VALUE))
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

        CustomPanel.setLayout(new FlowLayout());
        CustomPanel.setComponentOrientation(
            ComponentOrientation.LEFT_TO_RIGHT);

        btnAddButton.setText("New Button");
        btnAddButton.setMaximumSize(new java.awt.Dimension(100, 100));
        btnAddButton.setMinimumSize(new java.awt.Dimension(100, 100));
        btnAddButton.setPreferredSize(new java.awt.Dimension(100, 50));
        btnAddButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAddButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout CustomPanelLayout = new javax.swing.GroupLayout(CustomPanel);
        CustomPanel.setLayout(CustomPanelLayout);
        CustomPanelLayout.setHorizontalGroup(
            CustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CustomPanelLayout.createSequentialGroup()
                .addComponent(btnAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 566, Short.MAX_VALUE))
        );
        CustomPanelLayout.setVerticalGroup(
            CustomPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, CustomPanelLayout.createSequentialGroup()
                .addGap(0, 256, Short.MAX_VALUE)
                .addComponent(btnAddButton, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        Menus.add(CustomPanel, "CustomCard");

        btnCustomButtons.setText("Custom Buttons");
        btnCustomButtons.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnCustomButtons.setMaximumSize(new java.awt.Dimension(100, 100));
        btnCustomButtons.setMinimumSize(new java.awt.Dimension(100, 100));
        btnCustomButtons.setPreferredSize(new java.awt.Dimension(100, 100));
        btnCustomButtons.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCustomButtonsActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout buttonsPanelLayout = new javax.swing.GroupLayout(buttonsPanel);
        buttonsPanel.setLayout(buttonsPanelLayout);
        buttonsPanelLayout.setHorizontalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(buttonsPanelLayout.createSequentialGroup()
                        .addComponent(btnCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCustom3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(buttonsPanelLayout.createSequentialGroup()
                        .addComponent(btn3DMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnBackup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnCustomButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(buttonsPanelLayout.createSequentialGroup()
                        .addComponent(btnWeather, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, 0)
                        .addComponent(btnDifficulty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 48, Short.MAX_VALUE)
                .addComponent(Menus, javax.swing.GroupLayout.PREFERRED_SIZE, 666, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        buttonsPanelLayout.setVerticalGroup(
            buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(buttonsPanelLayout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(buttonsPanelLayout.createSequentialGroup()
                        .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnWeather, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnTime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnDifficulty, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnCustom3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCustom2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCustom1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(buttonsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btn3DMap, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnBackup, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(btnCustomButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(Menus, javax.swing.GroupLayout.PREFERRED_SIZE, 306, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
        );

        centerPanel.add(buttonsPanel, "buttonsCard");

        javax.swing.GroupLayout MainLayout = new javax.swing.GroupLayout(Main);
        Main.setLayout(MainLayout);
        MainLayout.setHorizontalGroup(
            MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(MainLayout.createSequentialGroup()
                .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnMinMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnCloseGUI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 2, Short.MAX_VALUE))
            .addGroup(MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(MainLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addGroup(MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addComponent(centerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(logPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(topPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))))
        );
        MainLayout.setVerticalGroup(
            MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, MainLayout.createSequentialGroup()
                .addGap(0, 708, Short.MAX_VALUE)
                .addGroup(MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnSave, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnReload, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnInfo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnMinMax, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnCloseGUI, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnConfig, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
            .addGroup(MainLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(MainLayout.createSequentialGroup()
                    .addGap(0, 0, 0)
                    .addComponent(topPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGap(0, 0, 0)
                    .addComponent(logPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                    .addComponent(centerPanel, javax.swing.GroupLayout.PREFERRED_SIZE, 323, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap()))
        );

        CARDS.add(Main, "MainCard");

        Lock.setMaximumSize(new java.awt.Dimension(1024, 768));
        Lock.setMinimumSize(new java.awt.Dimension(1024, 768));
        Lock.setPreferredSize(new java.awt.Dimension(1024, 768));

        btnUnlock.setText("Unlock");
        btnUnlock.setMaximumSize(new java.awt.Dimension(400, 400));
        btnUnlock.setMinimumSize(new java.awt.Dimension(400, 400));
        btnUnlock.setPreferredSize(new java.awt.Dimension(400, 400));

        javax.swing.GroupLayout LockLayout = new javax.swing.GroupLayout(Lock);
        Lock.setLayout(LockLayout);
        LockLayout.setHorizontalGroup(
            LockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LockLayout.createSequentialGroup()
                .addGap(284, 284, 284)
                .addComponent(btnUnlock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(340, Short.MAX_VALUE))
        );
        LockLayout.setVerticalGroup(
            LockLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(LockLayout.createSequentialGroup()
                .addGap(164, 164, 164)
                .addComponent(btnUnlock, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(204, Short.MAX_VALUE))
        );

        CARDS.add(Lock, "LockCard");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(CARDS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(CARDS, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
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
            if (CodeEntry.showCodeEntryDialog("Close GUI - Enter Code", CODE)) {
                this.setVisible(false);
            }
        } else {
            this.setVisible(false);
        }
    }//GEN-LAST:event_btnCloseGUIActionPerformed

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        if (Config.GUI_LOCK) {
            if (CodeEntry.showCodeEntryDialog("Stop Server - Enter Code", CODE)) {
                stop(StopServerOptions.showStopOptions());
            }
        } else {
            stop(StopServerOptions.showStopOptions());
        }
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveActionPerformed
        save();
    }//GEN-LAST:event_btnSaveActionPerformed

    private void btnReloadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnReloadActionPerformed
        if (Config.GUI_LOCK) {
            if (CodeEntry.showCodeEntryDialog("Reload Server - Enter Code", CODE)) {
                reload();
            }
        } else {
            reload();
        }

        //card.show(Main, "LockCard");

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
                if (CodeEntry.showCodeEntryDialog("Minimize GUI - Enter Code", CODE)) {
                    GUIPlugin.minimize();
                }
            } else {
                GUIPlugin.minimize();
            }
        }
    }//GEN-LAST:event_btnMinMaxActionPerformed

    private void btnInfoActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnInfoActionPerformed
        InfoDialog.showInfoDialog();
    }//GEN-LAST:event_btnInfoActionPerformed

    private void btnConfigActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnConfigActionPerformed
        if (Config.GUI_LOCK) {
            if (CodeEntry.showCodeEntryDialog("Config - Enter Code", CODE)) {
                Config.showConfigDialog();
            }
        } else {
            Config.showConfigDialog();
        }
    }//GEN-LAST:event_btnConfigActionPerformed

    private void btnCustom1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustom1ActionPerformed
        if (Config.CUSTOM1_LOCK) {
            if (CodeEntry.showCodeEntryDialog(Config.CUSTOM1_TEXT + " - Enter Code", CODE)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.CUSTOM1_COMMAND);
            }
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.CUSTOM1_COMMAND);
        }
    }//GEN-LAST:event_btnCustom1ActionPerformed

    private void btnCustom2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustom2ActionPerformed
        if (Config.CUSTOM2_LOCK) {
            if (CodeEntry.showCodeEntryDialog(Config.CUSTOM2_TEXT + " - Enter Code", CODE)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.CUSTOM2_COMMAND);
            }
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.CUSTOM2_COMMAND);
        }
    }//GEN-LAST:event_btnCustom2ActionPerformed

    private void btnCustom3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustom3ActionPerformed
        if (Config.CUSTOM3_LOCK) {
            if (CodeEntry.showCodeEntryDialog(Config.CUSTOM3_TEXT + " - Enter Code", CODE)) {
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.CUSTOM3_COMMAND);
            }
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), Config.CUSTOM3_COMMAND);
        }
    }//GEN-LAST:event_btnCustom3ActionPerformed

    private void txtPlayerDetailsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtPlayerDetailsActionPerformed
        int i = lstOnline.getSelectedIndex();

        if (i == -1) {
            BigPrompt.showMessageDialog("Player Details", "Select a player!");
            //JOptionPane.showMessageDialog(rootPane, "Select a player!", "Player Details", JOptionPane.ERROR_MESSAGE);
        } else {
            if (!playerList.isEmpty()) {
                PlayerDetails.showPlayerDetailsDialog(playerList.get(i));
                this.updateOnline();
            } else {
                BigPrompt.showMessageDialog("Player Details", "Select a player!");
                //JOptionPane.showMessageDialog(rootPane, "Select a player!", "Player Details", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_txtPlayerDetailsActionPerformed

    private void btnWhitelistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWhitelistActionPerformed
        Whitelist.showWhitelist(this);
    }//GEN-LAST:event_btnWhitelistActionPerformed

    private void btnDiconRDPActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDiconRDPActionPerformed
        try {
            if (!Config.TS_DISCON_URL.equals("")) {
                File file = new File(Config.TS_DISCON_URL);
                dt.open(file);
            } else{
                toTextArea("***ERROR- No link/file has been set for disconnecting an RDP session***");
            }
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
        BanList.showBanlist(this);
    }//GEN-LAST:event_btnBanListActionPerformed

    private void btnWeatherActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnWeatherActionPerformed
        btnWeather.setSelected(true);
        btnTime.setSelected(false);
        btnDifficulty.setSelected(false);
        btnCustomButtons.setSelected(false);
        card.show(Menus, "WeatherCard");
    }//GEN-LAST:event_btnWeatherActionPerformed

    private void btnTimeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnTimeActionPerformed
        btnWeather.setSelected(false);
        btnTime.setSelected(true);
        btnDifficulty.setSelected(false);
        btnCustomButtons.setSelected(false);
        card.show(Menus, "TimeCard");
    }//GEN-LAST:event_btnTimeActionPerformed

    private void btnDifficultyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDifficultyActionPerformed
        btnWeather.setSelected(false);
        btnTime.setSelected(false);
        btnDifficulty.setSelected(true);
        btnCustomButtons.setSelected(false);
        card.show(Menus, "DifficultyCard");
    }//GEN-LAST:event_btnDifficultyActionPerformed

    private void lstOnlineMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstOnlineMouseClicked
        if (evt.getClickCount() == 2) {
            int i = lstOnline.getSelectedIndex();

            if (i == -1) {
                BigPrompt.showMessageDialog("Player Details", "Select a player!");
                //JOptionPane.showMessageDialog(rootPane, "Select a player!", "Player Details", JOptionPane.ERROR_MESSAGE);
                //new PlayerDetails().setVisible(true);
            } else {
                if (!playerList.isEmpty()) {
                    PlayerDetails.showPlayerDetailsDialog(playerList.get(i));
                }
            }
        }
    }//GEN-LAST:event_lstOnlineMouseClicked

    private void btnAddButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAddButtonActionPerformed
        String text = JOptionPane.showInputDialog(this, "Enter Text", "New Button", JOptionPane.PLAIN_MESSAGE);
        String command = JOptionPane.showInputDialog(this, "Enter Command", "New Button", JOptionPane.PLAIN_MESSAGE);
        addButton(text, command);
    }//GEN-LAST:event_btnAddButtonActionPerformed

    private void btnChatModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnChatModeActionPerformed
        if (commandMode) {
            btnChatMode.setText("Command Mode");
            commandMode = false;
        } else {
            btnChatMode.setText("Chat Mode");
            commandMode = true;
        }
    }//GEN-LAST:event_btnChatModeActionPerformed

    private void btnCustomButtonsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCustomButtonsActionPerformed
        btnWeather.setSelected(false);
        btnTime.setSelected(false);
        btnDifficulty.setSelected(false);
        btnCustomButtons.setSelected(true);
        card.show(Menus, "CustomCard");
    }//GEN-LAST:event_btnCustomButtonsActionPerformed

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
    private javax.swing.JPanel CARDS;
    private javax.swing.JPanel CustomPanel;
    private javax.swing.JPanel DifficultyPanel;
    private javax.swing.JPanel Lock;
    private javax.swing.JPanel Main;
    private javax.swing.JPanel Menus;
    private javax.swing.JPanel TimePanel;
    private javax.swing.JPanel WeatherPanel;
    private javax.swing.JButton btn3DMap;
    private javax.swing.JButton btnAddButton;
    private javax.swing.JButton btnBackup;
    private javax.swing.JButton btnBanList;
    private javax.swing.JButton btnChatMode;
    private javax.swing.JButton btnClear;
    private javax.swing.JButton btnCloseGUI;
    private javax.swing.JButton btnConfig;
    private javax.swing.JButton btnCustom1;
    private javax.swing.JButton btnCustom2;
    private javax.swing.JButton btnCustom3;
    private javax.swing.JToggleButton btnCustomButtons;
    private javax.swing.JButton btnDiconRDP;
    private javax.swing.JToggleButton btnDifficulty;
    private javax.swing.JButton btnEasy;
    private javax.swing.JButton btnEnterTime;
    private javax.swing.JButton btnEvening;
    private javax.swing.JButton btnHard;
    private javax.swing.JButton btnInfo;
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
    private javax.swing.JButton btnUnlock;
    private javax.swing.JToggleButton btnWeather;
    private javax.swing.JButton btnWhitelist;
    private javax.swing.JPanel buttonsPanel;
    private javax.swing.JPanel centerPanel;
    private javax.swing.JButton cmdEnterText;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblDuration;
    private javax.swing.JLabel lblManualEntry;
    private javax.swing.JLabel lblOnline;
    private javax.swing.JLabel lblSeconds;
    private javax.swing.JLabel lblServerInfo;
    private javax.swing.JLabel lblStopTime;
    private javax.swing.JLabel lblTime;
    private javax.swing.JPanel logPanel;
    private javax.swing.JList lstOnline;
    private javax.swing.JPanel topPanel;
    private javax.swing.JTextField txtDuration;
    private javax.swing.JTextArea txtLogs;
    private javax.swing.JButton txtPlayerDetails;
    private javax.swing.JTextField txtText;
    private javax.swing.JTextField txtTimeEntry;
    // End of variables declaration//GEN-END:variables
}
