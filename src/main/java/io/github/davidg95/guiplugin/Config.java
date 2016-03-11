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
import org.bukkit.Bukkit;

/**
 *
 * @author David
 */
public class Config extends javax.swing.JFrame {

    private GUI g;
    public String SERVER_NAME;
    public String CUSTOM1_TEXT;
    public String CUSTOM2_TEXT;
    public String CUSTOM1_COMMAND;
    public String CUSTOM2_COMMAND;
    public String CUSTOM3_TEXT;
    public String CUSTOM3_COMMAND;
    private final String FILE = "GUIConfig.txt";

    /**
     * Creates new form Config
     */
    public Config(GUI g) {
        SERVER_NAME = "";
        CUSTOM1_TEXT = "";
        CUSTOM1_COMMAND = "";
        CUSTOM2_TEXT = "";
        CUSTOM2_COMMAND = "";
        CUSTOM3_TEXT = "";
        CUSTOM3_COMMAND = "";
        this.g = g;
        loadConfig();
        initComponents();
        this.setLocationRelativeTo(null);
        txtServerName.setText(SERVER_NAME);
        txtCustom1Text.setText(CUSTOM1_TEXT);
        txtCustom1Command.setText(CUSTOM1_COMMAND);
        txtCustom2Text.setText(CUSTOM2_TEXT);
        txtCustom2Command.setText(CUSTOM2_COMMAND);
        txtCustom3Text.setText(CUSTOM3_TEXT);
        txtCustom3Command.setText(CUSTOM3_COMMAND);
    }

//    public Config(GUI g) {
//        this.g = g;
//        initComponents();
//    }
    
    public void save(){
        GUIPlugin.conf.set("ServerName", SERVER_NAME);
        GUIPlugin.conf.set("Custom1Text", CUSTOM1_TEXT);
        GUIPlugin.conf.set("Custom1Command", CUSTOM1_COMMAND);
        GUIPlugin.conf.set("Custom2Text", CUSTOM2_TEXT);
        GUIPlugin.conf.set("Custom2Command", CUSTOM2_COMMAND);
    }
    
    public void load(){
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
            PrintWriter pw = new PrintWriter(fw);
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
//            pw.write(SERVER_NAME);
//            pw.write(CUSTOM1_TEXT);
//            pw.write(CUSTOM1_COMMAND);
//            pw.write(CUSTOM2_TEXT);
//            pw.write(CUSTOM2_COMMAND);
            pw.close();
        } catch (FileNotFoundException e) {
            System.out.println(e);
        } catch (IOException e) {
            System.out.println(e);
        }
    }

    public void loadConfig() {
        try {
            File dataFolder = Bukkit.getServer().getPluginManager().getPlugin("GUIPlugin").getDataFolder();
            if (!dataFolder.exists()) {
                dataFolder.mkdir();
            }
            File configFile = new File(Bukkit.getServer().getPluginManager().getPlugin("GUIPlugin").getDataFolder(), FILE);
            if (!configFile.exists()) {
                configFile.createNewFile();
                FileWriter fw = new FileWriter(configFile, true);
                PrintWriter pw = new PrintWriter(fw);
                pw.println("NULL");
                pw.println("NULL");
                pw.println("NULL");
                pw.println("NULL");
                pw.println("NULL");
                pw.println("NULL");
                pw.println("NULL");
                pw.close();
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
        jPanel1 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        txtCustom1Text = new javax.swing.JTextField();
        txtCustom1Command = new javax.swing.JTextField();
        jPanel2 = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        txtCustom2Text = new javax.swing.JTextField();
        txtCustom2Command = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        txtCustom3Text = new javax.swing.JTextField();
        txtCustom3Command = new javax.swing.JTextField();

        setAlwaysOnTop(true);

        jLabel1.setText("Server Name:");

        btnUpdate.setText("Update");
        btnUpdate.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnUpdateActionPerformed(evt);
            }
        });

        jLabel2.setText("Custom Button 1-");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCustom1Command, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(txtCustom1Text, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(txtCustom1Text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCustom1Command, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel3.setText("Custom Button 2-");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(98, 98, 98)
                .addComponent(txtCustom2Command, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCustom2Text, javax.swing.GroupLayout.PREFERRED_SIZE, 88, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtCustom2Text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCustom2Command, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jLabel5.setText("Custom Button 3-");

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
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel5)
                    .addComponent(txtCustom3Text, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtCustom3Command, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(38, 38, 38)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(jLabel1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(txtServerName, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(184, 184, 184)
                        .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 145, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap(108, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(20, 20, 20)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtServerName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 69, Short.MAX_VALUE)
                .addComponent(btnUpdate, javax.swing.GroupLayout.PREFERRED_SIZE, 56, javax.swing.GroupLayout.PREFERRED_SIZE)
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
        g.updateConfig();
        saveConfig();
        this.setVisible(false);
    }//GEN-LAST:event_btnUpdateActionPerformed

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
    private javax.swing.JButton btnUpdate;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JTextField txtCustom1Command;
    private javax.swing.JTextField txtCustom1Text;
    private javax.swing.JTextField txtCustom2Command;
    private javax.swing.JTextField txtCustom2Text;
    private javax.swing.JTextField txtCustom3Command;
    private javax.swing.JTextField txtCustom3Text;
    private javax.swing.JTextField txtServerName;
    // End of variables declaration//GEN-END:variables
}