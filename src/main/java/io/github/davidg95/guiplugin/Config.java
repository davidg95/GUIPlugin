/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import java.awt.Component;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import static javax.swing.JOptionPane.getRootFrame;
import javax.swing.border.TitledBorder;
import org.bukkit.Bukkit;

/**
 *
 * @author David
 */
public class Config extends javax.swing.JDialog {

    private static JDialog dialog;
    private static GUIInterface g;
    private static Properties properties;

    public static String SERVER_NAME = "";
    public static String CUSTOM1_TEXT = "";
    public static String CUSTOM2_TEXT = "";
    public static String CUSTOM1_COMMAND = "";
    public static String CUSTOM2_COMMAND = "";
    public static String CUSTOM3_TEXT = "";
    public static String CUSTOM3_COMMAND = "";
    public static boolean CUSTOM1_LOCK = false;
    public static boolean CUSTOM2_LOCK = false;
    public static boolean CUSTOM3_LOCK = false;
    public static boolean autoStop = false;
    public static int WARNING_HOUR;
    public static int WARNING_MINUTE;
    public static int STOP_HOUR;
    public static int STOP_MINUTE;
    public static String WARNING_MESSAGE = "***SERVER SHUTDOWN***";
    public static ShutdownOption SHUTDOWN_OPTION = ShutdownOption.NOTHING;
    public static boolean GUI_LOCK = false;
    public static boolean BACKUP_ON_CLOSE = true;
    public static File bg_file;
    public static BufferedImage bg_image;
    public static String CODE = "";

    public static String SHUT_URL = "";
    public static String SLEEP_URL = "";
    public static String TS_DISCON_URL = "";
    public static String BACKUP_URL = "";
    public static String RENDER_MAP_URL = "";

    public static List<JButton> customButtons;

    public boolean initAutoStop;
    public boolean shutdownChange;

    public enum ShutdownOption {

        SHUT_DOWN, STANDBY, NOTHING
    }

    /**
     * Creates new form Config
     *
     * @param parent the parent component.
     */
    public Config(Window parent) {
        super(parent);
        initAutoStop = autoStop;
        shutdownChange = false;
        initComponents();
        panelCustomButtons.setBorder(new TitledBorder("Custom Buttons"));
        panelServerStop.setBorder(new TitledBorder("Server Stop Options"));
        this.setLocationRelativeTo(null);
        this.setModal(true);
        txtServerName.setText(SERVER_NAME);
        txtCustom1Text.setText(CUSTOM1_TEXT);
        txtCustom1Command.setText(CUSTOM1_COMMAND);
        txtCustom2Text.setText(CUSTOM2_TEXT);
        txtCustom2Command.setText(CUSTOM2_COMMAND);
        txtCustom3Text.setText(CUSTOM3_TEXT);
        txtCustom3Command.setText(CUSTOM3_COMMAND);
        chkLock1.setSelected(CUSTOM1_LOCK);
        chkLock2.setSelected(CUSTOM2_LOCK);
        chkLock3.setSelected(CUSTOM3_LOCK);
        checkWhitelist.setSelected(Bukkit.hasWhitelist());
        if (CODE.equals("")) {
            checkPasscode.setSelected(false);
            checkPasscode.setEnabled(false);
            GUI_LOCK = false;
        } else {
            checkPasscode.setEnabled(true);
            checkPasscode.setSelected(GUI_LOCK);
        }
        checkLockdown.setSelected(g.isLockDown());
        chkStop.setSelected(autoStop);
        if (chkStop.isSelected()) {
            txtStopHour.setEnabled(true);
            txtStopMin.setEnabled(true);
            txtWarnHour.setEnabled(true);
            txtWarnMin.setEnabled(true);
            txtWarnMessage.setEnabled(true);
            chkBackup.setEnabled(true);
            chkShutDown.setEnabled(true);
            chkStandby.setEnabled(true);
            chkNothing.setEnabled(true);
            jLabel6.setEnabled(true);
            jLabel8.setEnabled(true);
            jLabel10.setEnabled(true);
            jLabel4.setEnabled(true);
            lblBATWarn.setEnabled(true);
            lblBATWarn.setText("<html>The server is set to stop at " + Config.STOP_HOUR + ":" + Config.STOP_MINUTE + (BACKUP_ON_CLOSE ? ", a backup <br>will be taken" : ", a backup will not be taken</html>"));
        } else {
            txtStopHour.setEnabled(false);
            txtStopMin.setEnabled(false);
            txtWarnHour.setEnabled(false);
            txtWarnMin.setEnabled(false);
            txtWarnMessage.setEnabled(false);
            chkBackup.setEnabled(false);
            chkShutDown.setEnabled(false);
            chkStandby.setEnabled(false);
            chkNothing.setEnabled(false);
            jLabel6.setEnabled(false);
            jLabel8.setEnabled(false);
            jLabel10.setEnabled(false);
            jLabel4.setEnabled(false);
            lblBATWarn.setEnabled(false);
            lblBATWarn.setText("Server stop is diabled");
        }
        chkBackup.setSelected(BACKUP_ON_CLOSE);
        if (SHUTDOWN_OPTION.equals(ShutdownOption.SHUT_DOWN)) {
            chkShutDown.setSelected(true);
        } else if (SHUTDOWN_OPTION.equals(ShutdownOption.STANDBY)) {
            chkStandby.setSelected(true);
        } else if (SHUTDOWN_OPTION.equals(ShutdownOption.NOTHING)) {
            chkNothing.setSelected(true);
        }
    }

    public static void initConfig(GUIInterface gui) {
        g = gui;
        bg_image = null;
        properties = new Properties();
        customButtons = new ArrayList<>();
        loadProperties();
        g.updateConfig();
    }

    public static void showConfigDialog(Component parent) {
        Window window;
        if (parent instanceof Frame || parent instanceof Dialog) {
            window = (Window) parent;
        } else {
            window = getRootFrame();
        }
        dialog = new Config(window);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    private static void loadProperties() {
        InputStream input = null;

        try {
            input = new FileInputStream("GUIConfig.properties");

            properties.load(input);

            SERVER_NAME = properties.getProperty("NAME");
            CUSTOM1_TEXT = properties.getProperty("CUST1TEXT");
            CUSTOM1_COMMAND = properties.getProperty("CUST1COMM");
            CUSTOM2_TEXT = properties.getProperty("CUST2TEXT");
            CUSTOM2_COMMAND = properties.getProperty("CUST2COMM");
            CUSTOM3_TEXT = properties.getProperty("CUST3TEXT");
            CUSTOM3_COMMAND = properties.getProperty("CUST3COMM");
            CUSTOM1_LOCK = Boolean.parseBoolean(properties.getProperty("CUST1LOCK"));
            CUSTOM2_LOCK = Boolean.parseBoolean(properties.getProperty("CUST2LOCK"));
            CUSTOM3_LOCK = Boolean.parseBoolean(properties.getProperty("CUST3LOCK"));
            autoStop = Boolean.parseBoolean(properties.getProperty("AUTOSTOP"));
            WARNING_HOUR = Integer.parseInt(properties.getProperty("WARNHOUR"));
            WARNING_MINUTE = Integer.parseInt(properties.getProperty("WARNMIN"));
            STOP_HOUR = Integer.parseInt(properties.getProperty("STOPHOUR"));
            STOP_MINUTE = Integer.parseInt(properties.getProperty("STOPMIN"));
            WARNING_MESSAGE = properties.getProperty("WARNMESSAGE");
            String shut_opt = properties.getProperty("SHUTOPT");
            switch (shut_opt) {
                case "SHUT_DOWN":
                    SHUTDOWN_OPTION = ShutdownOption.SHUT_DOWN;
                    break;
                case "STANDBY":
                    SHUTDOWN_OPTION = ShutdownOption.STANDBY;
                    break;
                case "NOTHING":
                    SHUTDOWN_OPTION = ShutdownOption.NOTHING;
                    break;
            }
            GUI_LOCK = Boolean.parseBoolean(properties.getProperty("GUILOCK"));
            CODE = properties.getProperty("CODE");
            BACKUP_ON_CLOSE = Boolean.parseBoolean(properties.getProperty("BACKUPONCLOSE"));
            SHUT_URL = properties.getProperty("SHUTURL");
            SLEEP_URL = properties.getProperty("SLEEPURL");
            TS_DISCON_URL = properties.getProperty("DISCONURL");
            BACKUP_URL = properties.getProperty("BACKUPURL");
            RENDER_MAP_URL = properties.getProperty("RENDERURL");

            int count = Integer.parseInt(properties.getProperty("COUNT"));

            for (int i = 0; i < count; i++) {
                String text = properties.getProperty("TEXT" + i);
                String command = properties.getProperty("COMMAND" + i);
                JButton button = new JButton();
                button.setText(text);
                button.setMaximumSize(new java.awt.Dimension(100, 100));
                button.setMinimumSize(new java.awt.Dimension(100, 100));
                button.setPreferredSize(new java.awt.Dimension(100, 100));
                button.setSize(100, 100);
                button.setAction(new CustomButtonAction(text, command));
                customButtons.add(button);
            }

        } catch (FileNotFoundException ex) {
            File file = new File("GUIConfig.properties");
            try {
                file.createNewFile();
                saveProperties();
            } catch (IOException ex1) {
            }
        } catch (IOException ex) {
        }
    }

    public static void saveProperties() {
        OutputStream output = null;

        try {
            output = new FileOutputStream("GUIConfig.properties");

            properties.setProperty("NAME", SERVER_NAME);
            properties.setProperty("CUST1TEXT", CUSTOM1_TEXT);
            properties.setProperty("CUST1COMM", CUSTOM1_COMMAND);
            properties.setProperty("CUST2TEXT", CUSTOM2_TEXT);
            properties.setProperty("CUST2COMM", CUSTOM2_COMMAND);
            properties.setProperty("CUST3TEXT", CUSTOM3_TEXT);
            properties.setProperty("CUST3COMM", CUSTOM3_COMMAND);
            properties.setProperty("CUST1LOCK", Boolean.toString(CUSTOM1_LOCK));
            properties.setProperty("CUST2LOCK", Boolean.toString(CUSTOM2_LOCK));
            properties.setProperty("CUST3LOCK", Boolean.toString(CUSTOM3_LOCK));
            properties.setProperty("AUTOSTOP", Boolean.toString(autoStop));
            properties.setProperty("WARNHOUR", Integer.toString(WARNING_HOUR));
            properties.setProperty("WARNMIN", Integer.toString(WARNING_MINUTE));
            properties.setProperty("STOPHOUR", Integer.toString(STOP_HOUR));
            properties.setProperty("STOPMIN", Integer.toString(STOP_MINUTE));
            properties.setProperty("WARNMESSAGE", WARNING_MESSAGE);
            properties.setProperty("SHUTOPT", SHUTDOWN_OPTION.toString());
            properties.setProperty("GUILOCK", Boolean.toString(GUI_LOCK));
            properties.setProperty("CODE", CODE);
            properties.setProperty("BACKUPONCLOSE", Boolean.toString(BACKUP_ON_CLOSE));
            properties.setProperty("SHUTURL", SHUT_URL);
            properties.setProperty("SLEEPURL", SLEEP_URL);
            properties.setProperty("DISCONURL", TS_DISCON_URL);
            properties.setProperty("BACKUPURL", BACKUP_URL);
            properties.setProperty("RENDERURL", RENDER_MAP_URL);

            int count = customButtons.size();

            properties.setProperty("COUNT", Integer.toString(count));

            for (int i = 0; i < count; i++) {
                JButton button = customButtons.get(i);
                properties.setProperty("TEXT" + i, button.getText());
                properties.setProperty("COMMAND" + i, button.getActionCommand());
            }

            properties.store(output, null);
        } catch (FileNotFoundException ex) {
        } catch (IOException ex) {
        }
    }

    private void showUpdatePrompt() {
        lblUpdatePrompt.setText("*Click update to apply changes");
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btngrpStopOptions = new javax.swing.ButtonGroup();
        jLabel1 = new javax.swing.JLabel();
        txtServerName = new javax.swing.JTextField();
        btnUpdate = new javax.swing.JButton();
        panelServerStop = new javax.swing.JPanel();
        txtWarnHour = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        txtWarnMin = new javax.swing.JTextField();
        txtWarnMessage = new javax.swing.JTextField();
        jLabel10 = new javax.swing.JLabel();
        chkStop = new javax.swing.JCheckBox();
        txtStopHour = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtStopMin = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        chkBackup = new javax.swing.JCheckBox();
        chkShutDown = new javax.swing.JRadioButton();
        chkStandby = new javax.swing.JRadioButton();
        chkNothing = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        lblBATWarn = new javax.swing.JLabel();
        panelCustomButtons = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCustom2Text = new javax.swing.JTextField();
        txtCustom2Command = new javax.swing.JTextField();
        chkLock2 = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtCustom3Text = new javax.swing.JTextField();
        txtCustom3Command = new javax.swing.JTextField();
        chkLock3 = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCustom1Text = new javax.swing.JTextField();
        txtCustom1Command = new javax.swing.JTextField();
        chkLock1 = new javax.swing.JCheckBox();
        checkWhitelist = new javax.swing.JCheckBox();
        checkPasscode = new javax.swing.JCheckBox();
        btnClose = new javax.swing.JButton();
        checkLockdown = new javax.swing.JCheckBox();
        btnBatch = new javax.swing.JButton();
        lblUpdatePrompt = new javax.swing.JLabel();
        btnPasscode = new javax.swing.JButton();

        setTitle("Configuration");
        setAlwaysOnTop(true);
        setMaximumSize(new java.awt.Dimension(640, 436));
        setMinimumSize(new java.awt.Dimension(640, 436));
        setResizable(false);

        jLabel1.setText("Server Name:");

        txtServerName.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtServerNameKeyTyped(evt);
            }
        });

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        panelServerStop.setMaximumSize(new java.awt.Dimension(277, 244));
        panelServerStop.setMinimumSize(new java.awt.Dimension(277, 244));
        panelServerStop.setPreferredSize(new java.awt.Dimension(277, 244));

        txtWarnHour.setText(Integer.toString(WARNING_HOUR));
        txtWarnHour.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtWarnHourKeyTyped(evt);
            }
        });

        jLabel9.setText(":");

        txtWarnMin.setText(Integer.toString(WARNING_MINUTE));
        txtWarnMin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtWarnMinKeyTyped(evt);
            }
        });

        txtWarnMessage.setText(WARNING_MESSAGE);
        txtWarnMessage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtWarnMessageKeyTyped(evt);
            }
        });

        jLabel10.setText("Warning Message:");

        chkStop.setText("Auto Server Stop");
        chkStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkStopActionPerformed(evt);
            }
        });

        txtStopHour.setText(Integer.toString(STOP_HOUR));
        txtStopHour.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStopHourKeyTyped(evt);
            }
        });

        jLabel6.setText("Server Stop Time:");

        txtStopMin.setText(Integer.toString(STOP_MINUTE));
        txtStopMin.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtStopMinKeyTyped(evt);
            }
        });

        jLabel7.setText(":");

        jLabel8.setText("Waring Time:");

        chkBackup.setText("Backup On Close");
        chkBackup.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBackupActionPerformed(evt);
            }
        });

        btngrpStopOptions.add(chkShutDown);
        chkShutDown.setText("Shut Down");
        chkShutDown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkShutDownActionPerformed(evt);
            }
        });

        btngrpStopOptions.add(chkStandby);
        chkStandby.setText("Standby");
        chkStandby.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkStandbyActionPerformed(evt);
            }
        });

        btngrpStopOptions.add(chkNothing);
        chkNothing.setText("Nothing");
        chkNothing.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkNothingActionPerformed(evt);
            }
        });

        jLabel4.setText("On Stop:");

        javax.swing.GroupLayout panelServerStopLayout = new javax.swing.GroupLayout(panelServerStop);
        panelServerStop.setLayout(panelServerStopLayout);
        panelServerStopLayout.setHorizontalGroup(
            panelServerStopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelServerStopLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelServerStopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkStop)
                    .addGroup(panelServerStopLayout.createSequentialGroup()
                        .addGroup(panelServerStopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(panelServerStopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(panelServerStopLayout.createSequentialGroup()
                                .addGroup(panelServerStopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtStopHour)
                                    .addComponent(txtWarnHour, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(panelServerStopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(panelServerStopLayout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtStopMin, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(panelServerStopLayout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtWarnMin, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(txtWarnMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addComponent(chkBackup)
                    .addGroup(panelServerStopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addComponent(lblBATWarn, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, panelServerStopLayout.createSequentialGroup()
                            .addComponent(jLabel4)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(chkShutDown)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(chkStandby)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(chkNothing))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelServerStopLayout.setVerticalGroup(
            panelServerStopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelServerStopLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chkStop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(panelServerStopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtStopHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtStopMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(panelServerStopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtWarnHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtWarnMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(panelServerStopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtWarnMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkBackup)
                .addGap(3, 3, 3)
                .addGroup(panelServerStopLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(chkShutDown)
                    .addComponent(chkStandby)
                    .addComponent(chkNothing))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblBATWarn, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        panelCustomButtons.setMaximumSize(new java.awt.Dimension(291, 244));
        panelCustomButtons.setMinimumSize(new java.awt.Dimension(291, 244));

        jLabel3.setText("Custom Button 2-");

        txtCustom2Text.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCustom2TextKeyTyped(evt);
            }
        });

        txtCustom2Command.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCustom2CommandKeyTyped(evt);
            }
        });

        chkLock2.setText("Protected");
        chkLock2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLock2ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCustom2Command, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCustom2Text, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkLock2)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCustom2Text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCustom2Command, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(23, 23, 23)
                        .addComponent(chkLock2)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel5.setText("Custom Button 3-");

        txtCustom3Text.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCustom3TextKeyTyped(evt);
            }
        });

        txtCustom3Command.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCustom3CommandKeyTyped(evt);
            }
        });

        chkLock3.setText("Protected");
        chkLock3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLock3ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtCustom3Command)
                    .addComponent(txtCustom3Text, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 6, Short.MAX_VALUE)
                .addComponent(chkLock3)
                .addContainerGap())
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel5)
                            .addComponent(txtCustom3Text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCustom3Command, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel3Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(chkLock3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setText("Custom Button 1-");

        txtCustom1Text.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCustom1TextKeyTyped(evt);
            }
        });

        txtCustom1Command.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txtCustom1CommandKeyTyped(evt);
            }
        });

        chkLock1.setText("Protected");
        chkLock1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkLock1ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCustom1Command, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtCustom1Text, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(chkLock1)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(txtCustom1Text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCustom1Command, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(24, 24, 24)
                        .addComponent(chkLock1)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout panelCustomButtonsLayout = new javax.swing.GroupLayout(panelCustomButtons);
        panelCustomButtons.setLayout(panelCustomButtonsLayout);
        panelCustomButtonsLayout.setHorizontalGroup(
            panelCustomButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(panelCustomButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        panelCustomButtonsLayout.setVerticalGroup(
            panelCustomButtonsLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelCustomButtonsLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        checkWhitelist.setText("Enable Whitelist");
        checkWhitelist.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkWhitelistActionPerformed(evt);
            }
        });

        checkPasscode.setText("Require Passcode");
        checkPasscode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkPasscodeActionPerformed(evt);
            }
        });

        btnClose.setText("Close");
        btnClose.setPreferredSize(new java.awt.Dimension(67, 23));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        checkLockdown.setText("Lockdown");
        checkLockdown.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkLockdownActionPerformed(evt);
            }
        });

        btnBatch.setText("Set Batch Files");
        btnBatch.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBatchActionPerformed(evt);
            }
        });

        btnPasscode.setText("Change Passcode");
        btnPasscode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPasscodeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addComponent(panelCustomButtons, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(panelServerStop, javax.swing.GroupLayout.DEFAULT_SIZE, 312, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(lblUpdatePrompt, javax.swing.GroupLayout.PREFERRED_SIZE, 229, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addComponent(jLabel1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtServerName, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(67, 67, 67)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(btnBatch)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(btnPasscode))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(checkWhitelist)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkPasscode)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkLockdown)))
                        .addGap(0, 61, Short.MAX_VALUE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtServerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1)
                    .addComponent(checkWhitelist)
                    .addComponent(checkPasscode)
                    .addComponent(checkLockdown))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnBatch)
                    .addComponent(btnPasscode))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(panelCustomButtons, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(panelServerStop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(45, 45, 45)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblUpdatePrompt, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnUpdateActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnUpdateActionPerformed
        SERVER_NAME = txtServerName.getText();
        CUSTOM1_TEXT = txtCustom1Text.getText();
        CUSTOM1_COMMAND = txtCustom1Command.getText();
        CUSTOM2_TEXT = txtCustom2Text.getText();
        CUSTOM2_COMMAND = txtCustom2Command.getText();
        CUSTOM3_TEXT = txtCustom3Text.getText();
        CUSTOM3_COMMAND = txtCustom3Command.getText();
        CUSTOM1_LOCK = chkLock1.isSelected();
        CUSTOM2_LOCK = chkLock2.isSelected();
        CUSTOM3_LOCK = chkLock3.isSelected();
        autoStop = chkStop.isSelected();
        Bukkit.setWhitelist(checkWhitelist.isSelected());
        boolean initLockdown = g.isLockDown();
        g.setLockDown(checkLockdown.isSelected());
        Config.GUI_LOCK = checkPasscode.isSelected();
        WARNING_HOUR = Integer.parseInt(txtWarnHour.getText());
        WARNING_MINUTE = Integer.parseInt(txtWarnMin.getText());
        STOP_HOUR = Integer.parseInt(txtStopHour.getText());
        STOP_MINUTE = Integer.parseInt(txtStopMin.getText());
        WARNING_MESSAGE = txtWarnMessage.getText();
        BACKUP_ON_CLOSE = chkBackup.isSelected();
        if (autoStop == false) {
            g.cancelStopTimer();
            g.setStopTimeLabel("Server stop disabled");
            g.toTextArea("Server stop disabled");
        } else {
            if (initAutoStop != autoStop) {
                g.serverStopTimer();
                g.setStopTimeLabel("Server will stop at " + STOP_HOUR + ":" + STOP_MINUTE);
                g.toTextArea("Server will stop at " + STOP_HOUR + ":" + STOP_MINUTE);
            } else if (shutdownChange) {
                g.serverStopTimer();
                g.setStopTimeLabel("Server will stop at " + STOP_HOUR + ":" + STOP_MINUTE);
                g.toTextArea("Server will stop at " + STOP_HOUR + ":" + STOP_MINUTE);
            }
        }
        if (chkShutDown.isSelected()) {
            SHUTDOWN_OPTION = ShutdownOption.SHUT_DOWN;
        } else if (chkStandby.isSelected()) {
            SHUTDOWN_OPTION = ShutdownOption.STANDBY;
        } else if (chkNothing.isSelected()) {
            SHUTDOWN_OPTION = ShutdownOption.NOTHING;
        }
        if (initLockdown != g.isLockDown()) {
            if (g.isLockDown()) {
                JOptionPane.showMessageDialog(this, "Server is now in lockdown, no new players will be able to join", "Lockdown", JOptionPane.WARNING_MESSAGE);
            }
        }
        initAutoStop = autoStop;
        saveProperties();
        g.updateConfig();
        this.setVisible(false);
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

    private void chkStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkStopActionPerformed
        if (chkStop.isSelected()) {
            txtStopHour.setEnabled(true);
            txtStopMin.setEnabled(true);
            txtWarnHour.setEnabled(true);
            txtWarnMin.setEnabled(true);
            txtWarnMessage.setEnabled(true);
            chkBackup.setEnabled(true);
            chkShutDown.setEnabled(true);
            chkStandby.setEnabled(true);
            chkNothing.setEnabled(true);
            jLabel6.setEnabled(true);
            jLabel8.setEnabled(true);
            jLabel10.setEnabled(true);
            jLabel4.setEnabled(true);
            lblBATWarn.setEnabled(true);
        } else {
            txtStopHour.setEnabled(false);
            txtStopMin.setEnabled(false);
            txtWarnHour.setEnabled(false);
            txtWarnMin.setEnabled(false);
            txtWarnMessage.setEnabled(false);
            chkBackup.setEnabled(false);
            chkShutDown.setEnabled(false);
            chkStandby.setEnabled(false);
            chkNothing.setEnabled(false);
            jLabel6.setEnabled(false);
            jLabel8.setEnabled(false);
            jLabel10.setEnabled(false);
            jLabel4.setEnabled(false);
            lblBATWarn.setEnabled(false);
        }
        showUpdatePrompt();
    }//GEN-LAST:event_chkStopActionPerformed

    private void chkShutDownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkShutDownActionPerformed
        shutdownChange = true;
        if (SHUT_URL.equals("")) {
            lblBATWarn.setText("<html>*No batch file has been specified for shutting the computer down*</html>");
        }
        showUpdatePrompt();
    }//GEN-LAST:event_chkShutDownActionPerformed

    private void chkStandbyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkStandbyActionPerformed
        shutdownChange = true;
        if (SLEEP_URL.equals("")) {
            lblBATWarn.setText("<html>*No batch file has been specified for putting the computer to sleep*</html>");
        }
        showUpdatePrompt();
    }//GEN-LAST:event_chkStandbyActionPerformed

    private void chkNothingActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkNothingActionPerformed
        shutdownChange = true;
        showUpdatePrompt();
    }//GEN-LAST:event_chkNothingActionPerformed

    private void chkBackupActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBackupActionPerformed
        if (chkBackup.isSelected()) {
            if (BACKUP_URL.equals("")) {
                lblBATWarn.setText("<html>*No batch file has been specified for backing the server up*</html>");
            }
        }
        showUpdatePrompt();
    }//GEN-LAST:event_chkBackupActionPerformed

    private void btnBatchActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBatchActionPerformed
        BatchFilesDialog.showBatchDialog(this);
    }//GEN-LAST:event_btnBatchActionPerformed

    private void checkWhitelistActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkWhitelistActionPerformed
        showUpdatePrompt();
    }//GEN-LAST:event_checkWhitelistActionPerformed

    private void checkPasscodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkPasscodeActionPerformed
        showUpdatePrompt();
    }//GEN-LAST:event_checkPasscodeActionPerformed

    private void checkLockdownActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkLockdownActionPerformed
        showUpdatePrompt();
    }//GEN-LAST:event_checkLockdownActionPerformed

    private void txtServerNameKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtServerNameKeyTyped
        showUpdatePrompt();
    }//GEN-LAST:event_txtServerNameKeyTyped

    private void txtCustom1TextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustom1TextKeyTyped
        showUpdatePrompt();
    }//GEN-LAST:event_txtCustom1TextKeyTyped

    private void txtCustom2TextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustom2TextKeyTyped
        showUpdatePrompt();
    }//GEN-LAST:event_txtCustom2TextKeyTyped

    private void txtCustom2CommandKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustom2CommandKeyTyped
        showUpdatePrompt();
    }//GEN-LAST:event_txtCustom2CommandKeyTyped

    private void txtCustom3TextKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustom3TextKeyTyped
        showUpdatePrompt();
    }//GEN-LAST:event_txtCustom3TextKeyTyped

    private void txtCustom3CommandKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustom3CommandKeyTyped
        showUpdatePrompt();
    }//GEN-LAST:event_txtCustom3CommandKeyTyped

    private void chkLock1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLock1ActionPerformed
        showUpdatePrompt();
    }//GEN-LAST:event_chkLock1ActionPerformed

    private void chkLock2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLock2ActionPerformed
        showUpdatePrompt();
    }//GEN-LAST:event_chkLock2ActionPerformed

    private void chkLock3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkLock3ActionPerformed
        showUpdatePrompt();
    }//GEN-LAST:event_chkLock3ActionPerformed

    private void txtStopHourKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStopHourKeyTyped
        showUpdatePrompt();
    }//GEN-LAST:event_txtStopHourKeyTyped

    private void txtStopMinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtStopMinKeyTyped
        showUpdatePrompt();
    }//GEN-LAST:event_txtStopMinKeyTyped

    private void txtWarnHourKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWarnHourKeyTyped
        showUpdatePrompt();
    }//GEN-LAST:event_txtWarnHourKeyTyped

    private void txtWarnMinKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWarnMinKeyTyped
        showUpdatePrompt();
    }//GEN-LAST:event_txtWarnMinKeyTyped

    private void txtWarnMessageKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtWarnMessageKeyTyped
        showUpdatePrompt();
    }//GEN-LAST:event_txtWarnMessageKeyTyped

    private void txtCustom1CommandKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCustom1CommandKeyTyped
        showUpdatePrompt();
    }//GEN-LAST:event_txtCustom1CommandKeyTyped

    private void btnPasscodeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPasscodeActionPerformed
        if (!CODE.equals("")) {
            if (CodeEntry.showCodeEntryDialog(this, "Enter current code", CODE)) {
                String newCode = CodeEntry.showCodeEntryDialog(this, "Enter new code");
                if (CodeEntry.showCodeEntryDialog(this, "Enter again to confirm", newCode)) {
                    CODE = newCode;
                    checkPasscode.setEnabled(true);
                    showUpdatePrompt();
                } else {
                    JOptionPane.showMessageDialog(this, "Codes do not match", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        } else {
            String newCode = CodeEntry.showCodeEntryDialog(this, "Enter new code");
            if (CodeEntry.showCodeEntryDialog(this, "Enter again to confirm", newCode)) {
                CODE = newCode;
                checkPasscode.setEnabled(true);
                showUpdatePrompt();
            } else {
                JOptionPane.showMessageDialog(this, "Codes do not match", "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }//GEN-LAST:event_btnPasscodeActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBatch;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnPasscode;
    private javax.swing.JButton btnUpdate;
    private javax.swing.ButtonGroup btngrpStopOptions;
    private javax.swing.JCheckBox checkLockdown;
    private javax.swing.JCheckBox checkPasscode;
    private javax.swing.JCheckBox checkWhitelist;
    private javax.swing.JCheckBox chkBackup;
    private javax.swing.JCheckBox chkLock1;
    private javax.swing.JCheckBox chkLock2;
    private javax.swing.JCheckBox chkLock3;
    private javax.swing.JRadioButton chkNothing;
    private javax.swing.JRadioButton chkShutDown;
    private javax.swing.JRadioButton chkStandby;
    private javax.swing.JCheckBox chkStop;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JLabel lblBATWarn;
    private javax.swing.JLabel lblUpdatePrompt;
    private javax.swing.JPanel panelCustomButtons;
    private javax.swing.JPanel panelServerStop;
    private javax.swing.JTextField txtCustom1Command;
    private javax.swing.JTextField txtCustom1Text;
    private javax.swing.JTextField txtCustom2Command;
    private javax.swing.JTextField txtCustom2Text;
    private javax.swing.JTextField txtCustom3Command;
    private javax.swing.JTextField txtCustom3Text;
    private javax.swing.JTextField txtServerName;
    private javax.swing.JTextField txtStopHour;
    private javax.swing.JTextField txtStopMin;
    private javax.swing.JTextField txtWarnHour;
    private javax.swing.JTextField txtWarnMessage;
    private javax.swing.JTextField txtWarnMin;
    // End of variables declaration//GEN-END:variables
}
