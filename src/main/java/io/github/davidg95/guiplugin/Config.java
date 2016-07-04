/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import javax.swing.JOptionPane;
import org.bukkit.Bukkit;

/**
 *
 * @author David
 */
public class Config extends javax.swing.JDialog {

    private final GUIInterface g;
    public String SERVER_NAME;
    public String CUSTOM1_TEXT;
    public String CUSTOM2_TEXT;
    public String CUSTOM1_COMMAND;
    public String CUSTOM2_COMMAND;
    public String CUSTOM3_TEXT;
    public String CUSTOM3_COMMAND;
    public String LOOK_FEEL;
    private final String FILE = "GUIConfig.txt";
    private int selected = 0;
    public boolean autoStop = true;
    public boolean initAutoStop;
    protected static int WARNING_HOUR = 23;
    protected static int WARNING_MINUTE = 23;
    protected static int STOP_HOUR = 23;
    protected static int STOP_MINUTE = 28;
    protected static String WARNING_MESSAGE = "***SERVER SHUTDOWN IN 5 MINUTES***";
    protected static boolean GUI_LOCK = false;

    /**
     * Creates new form Config
     *
     * @param g reference to the main GUI.
     */
    public Config(GUIInterface g) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
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
        //</editor-fold>
        SERVER_NAME = "";
        CUSTOM1_TEXT = "";
        CUSTOM1_COMMAND = "";
        CUSTOM2_TEXT = "";
        CUSTOM2_COMMAND = "";
        CUSTOM3_TEXT = "";
        CUSTOM3_COMMAND = "";
        LOOK_FEEL = "";
        this.g = g;
        initAutoStop = autoStop;
        loadConfig();
        initComponents();
        setModal(true);
        getLookAndFeel();
        cmbLookFeel.setSelectedIndex(selected);
        this.setLocationRelativeTo(null);
        txtServerName.setText(SERVER_NAME);
        txtCustom1Text.setText(CUSTOM1_TEXT);
        txtCustom1Command.setText(CUSTOM1_COMMAND);
        txtCustom2Text.setText(CUSTOM2_TEXT);
        txtCustom2Command.setText(CUSTOM2_COMMAND);
        txtCustom3Text.setText(CUSTOM3_TEXT);
        txtCustom3Command.setText(CUSTOM3_COMMAND);
        checkWhitelist.setSelected(Bukkit.hasWhitelist());
        checkPasscode.setSelected(GUI_LOCK);
    }

    @Override
    public void setVisible(boolean visible) {
        this.setLocationRelativeTo(null);
        super.setVisible(visible);
    }

    public final void getLookAndFeel() {
        for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
            cmbLookFeel.addItem(info.getName());
        }
    }

    public void save() {
        GUIPlugin.conf.set("ServerName", SERVER_NAME);
        GUIPlugin.conf.set("Custom1Text", CUSTOM1_TEXT);
        GUIPlugin.conf.set("Custom1Command", CUSTOM1_COMMAND);
        GUIPlugin.conf.set("Custom2Text", CUSTOM2_TEXT);
        GUIPlugin.conf.set("Custom2Command", CUSTOM2_COMMAND);
    }

    public void load() {
        SERVER_NAME = (String) GUIPlugin.conf.get("ServerName");
        CUSTOM1_TEXT = (String) GUIPlugin.conf.get("Custom1Text");
        CUSTOM1_COMMAND = (String) GUIPlugin.conf.get("Custom1Command");
        CUSTOM2_TEXT = (String) GUIPlugin.conf.get("Custom2Text");
        CUSTOM2_COMMAND = (String) GUIPlugin.conf.get("Custom2Command");
    }

    public void saveConfig() {
        try {
            File dataFolder = Bukkit.getServer().getPluginManager().getPlugin("GUIPlugin").getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            File configFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GUIPlugin").getDataFolder(), FILE);
            if (!configFile.exists()) {
                configFile.createNewFile();
            }
            boolean temp = configFile.delete();
            configFile.createNewFile();
            FileWriter fw = new FileWriter(configFile, true);
            try (PrintWriter pw = new PrintWriter(fw)) {
                if (SERVER_NAME.equals("")) {
                    pw.println("NULL");
                } else {
                    pw.println(SERVER_NAME);
                }
                if (CUSTOM1_TEXT.equals("")) {
                    pw.println("NULL");
                } else {
                    pw.println(CUSTOM1_TEXT);
                }
                if (CUSTOM1_COMMAND.equals("")) {
                    pw.println("NULL");
                } else {
                    pw.println(CUSTOM1_COMMAND);
                }
                if (CUSTOM2_TEXT.equals("")) {
                    pw.println("NULL");
                } else {
                    pw.println(CUSTOM2_TEXT);
                }
                if (CUSTOM2_COMMAND.equals("")) {
                    pw.println("NULL");
                } else {
                    pw.println(CUSTOM2_COMMAND);
                }
                if (CUSTOM3_TEXT.equals("")) {
                    pw.println("NULL");
                } else {
                    pw.println(CUSTOM3_TEXT);
                }
                if (CUSTOM3_COMMAND.equals("")) {
                    pw.println("NULL");
                } else {
                    pw.println(CUSTOM3_COMMAND);
                }
                if (GUI_LOCK) {
                    pw.println("TRUE");
                } else {
                    pw.println("FALSE");
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public final void loadConfig() {
        try {
            File dataFolder = Bukkit.getServer().getPluginManager().getPlugin("GUIPlugin").getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            File configFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GUIPlugin").getDataFolder(), FILE);
            if (!configFile.exists()) {
                configFile.createNewFile();
                FileWriter fw = new FileWriter(configFile, true);
                try (PrintWriter pw = new PrintWriter(fw)) {
                    pw.println("NULL");
                    pw.println("NULL");
                    pw.println("NULL");
                    pw.println("NULL");
                    pw.println("NULL");
                    pw.println("NULL");
                    pw.println("NULL");
                    pw.println("FALSE");
                }
            }
            FileReader fr = new FileReader(configFile);
            BufferedReader br = new BufferedReader(fr);
            String name = br.readLine();
            if (!name.equals("NULL")) {
                SERVER_NAME = name;
            }
            String cus1Text = br.readLine();
            if (!cus1Text.equals("NULL")) {
                CUSTOM1_TEXT = cus1Text;
            }
            String cus1Com = br.readLine();
            if (!cus1Com.equals("NULL")) {
                CUSTOM1_COMMAND = cus1Com;
            }
            String cus2Text = br.readLine();
            if (!cus2Text.equals("NULL")) {
                CUSTOM2_TEXT = cus2Text;
            }
            String cus2Com = br.readLine();
            if (!cus2Com.equals("NULL")) {
                CUSTOM2_COMMAND = cus2Com;
            }
            String cus3Text = br.readLine();
            if (!cus3Text.equals("NULL")) {
                CUSTOM3_TEXT = cus3Text;
            }
            String cus3Com = br.readLine();
            if (!cus3Com.equals("NULL")) {
                CUSTOM3_COMMAND = cus3Com;
            }
            String lock = br.readLine();
            if (!lock.equals("FALSE")) {
                GUI_LOCK = true;
            }
            br.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        txtServerName = new javax.swing.JTextField();
        btnUpdate = new javax.swing.JButton();
        cmbLookFeel = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        jPanel4 = new javax.swing.JPanel();
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
        jPanel5 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCustom2Text = new javax.swing.JTextField();
        txtCustom2Command = new javax.swing.JTextField();
        chkLog2 = new javax.swing.JCheckBox();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtCustom3Text = new javax.swing.JTextField();
        txtCustom3Command = new javax.swing.JTextField();
        chkLog3 = new javax.swing.JCheckBox();
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCustom1Text = new javax.swing.JTextField();
        txtCustom1Command = new javax.swing.JTextField();
        chkLog1 = new javax.swing.JCheckBox();
        checkWhitelist = new javax.swing.JCheckBox();
        checkPasscode = new javax.swing.JCheckBox();
        btnClose = new javax.swing.JButton();

        setTitle("Configuration");
        setAlwaysOnTop(true);

        jLabel1.setText("Server Name:");

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        cmbLookFeel.setEnabled(false);

        jLabel4.setText("Look and Feel:");

        txtWarnHour.setText(Integer.toString(WARNING_HOUR));

        jLabel9.setText(":");

        txtWarnMin.setText(Integer.toString(WARNING_MINUTE));

        txtWarnMessage.setText(WARNING_MESSAGE);

        jLabel10.setText("Warning Message:");

        chkStop.setSelected(true);
        chkStop.setText("Auto Server Stop");

        txtStopHour.setText(Integer.toString(STOP_HOUR));

        jLabel6.setText("Server Stop Time:");

        txtStopMin.setText(Integer.toString(STOP_MINUTE));

        jLabel7.setText(":");

        jLabel8.setText("Waring Time:");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkStop)
                    .addGroup(jPanel4Layout.createSequentialGroup()
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jLabel6)
                            .addComponent(jLabel8)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel4Layout.createSequentialGroup()
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(txtStopHour)
                                    .addComponent(txtWarnHour, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel7)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtStopMin, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))
                                    .addGroup(jPanel4Layout.createSequentialGroup()
                                        .addComponent(jLabel9)
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                        .addComponent(txtWarnMin, javax.swing.GroupLayout.PREFERRED_SIZE, 19, javax.swing.GroupLayout.PREFERRED_SIZE))))
                            .addComponent(txtWarnMessage, javax.swing.GroupLayout.PREFERRED_SIZE, 161, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(chkStop)
                .addGap(18, 18, 18)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(txtStopHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel7)
                    .addComponent(txtStopMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel8)
                    .addComponent(txtWarnHour, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel9)
                    .addComponent(txtWarnMin, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(8, 8, 8)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtWarnMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel10))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setText("Custom Button 2-");

        chkLog2.setText("Log Output");
        chkLog2.setEnabled(false);

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
                .addComponent(chkLog2)
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
                        .addComponent(chkLog2)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        jLabel5.setText("Custom Button 3-");

        chkLog3.setText("Log Output");
        chkLog3.setEnabled(false);

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
                .addComponent(chkLog3)
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
                        .addComponent(chkLog3)))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel2.setText("Custom Button 1-");

        chkLog1.setText("Log Output");
        chkLog1.setEnabled(false);

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
                .addComponent(chkLog1)
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
                        .addComponent(chkLog1)))
                .addContainerGap(16, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel2, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel3, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        checkWhitelist.setText("Enable Whitelist");

        checkPasscode.setText("Require Passcode");

        btnClose.setText("Close");
        btnClose.setPreferredSize(new java.awt.Dimension(67, 23));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(131, 131, 131)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(68, 68, 68)
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtServerName, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(22, 22, 22)
                                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(20, 20, 20)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(checkWhitelist)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(checkPasscode))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel4)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(cmbLookFeel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(16, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(19, 19, 19)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtServerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(checkWhitelist)
                    .addComponent(checkPasscode))
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel4)
                            .addComponent(cmbLookFeel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 40, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(19, 19, 19))
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
        selected = cmbLookFeel.getSelectedIndex();
        LOOK_FEEL = cmbLookFeel.getItemAt(selected).toString();
        autoStop = chkStop.isSelected();
        Bukkit.setWhitelist(checkWhitelist.isSelected());
        Config.GUI_LOCK = checkPasscode.isSelected();
        WARNING_HOUR = Integer.parseInt(txtWarnHour.getText());
        WARNING_MINUTE = Integer.parseInt(txtWarnMin.getText());
        STOP_HOUR = Integer.parseInt(txtStopHour.getText());
        STOP_MINUTE = Integer.parseInt(txtStopMin.getText());
        WARNING_MESSAGE = txtWarnMessage.getText();
        if (initAutoStop == false && autoStop == true) {
            GUIPlugin.serverStopTimer();
            g.setStopTimeLabel("Server will stop at " + STOP_HOUR + ":" + STOP_MINUTE);
            g.toTextArea("Server will stop at " + STOP_HOUR + ":" + STOP_MINUTE);
        } else if (initAutoStop == true && autoStop == false) {
            GUIPlugin.cancelStopTimer();
            g.setStopTimeLabel("Server stop disabled");
            g.toTextArea("Server stip disabled");
        } else if (initAutoStop == true && autoStop == true) {
            GUIPlugin.cancelStopTimer();
            GUIPlugin.serverStopTimer();
            g.setStopTimeLabel("Server will stop at " + STOP_HOUR + ":" + STOP_MINUTE);
        }
        initAutoStop = autoStop;
        saveConfig();
        g.updateConfig();
        this.setVisible(false);
    }//GEN-LAST:event_btnUpdateActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.setVisible(false);
    }//GEN-LAST:event_btnCloseActionPerformed

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
//            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Config.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Config().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnUpdate;
    private javax.swing.JCheckBox checkPasscode;
    private javax.swing.JCheckBox checkWhitelist;
    private javax.swing.JCheckBox chkLog1;
    private javax.swing.JCheckBox chkLog2;
    private javax.swing.JCheckBox chkLog3;
    private javax.swing.JCheckBox chkStop;
    private javax.swing.JComboBox cmbLookFeel;
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
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
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
