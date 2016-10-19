/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import java.awt.image.BufferedImage;
import javax.swing.DefaultListModel;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 *
 * @author David
 */
public class PlayerDetails extends javax.swing.JDialog {

    private static JDialog dialog;
    
    private Player player;
    private OfflinePlayer offlinePlayer;
    private String PLAYER_NAME;
    private BufferedImage mapImage;
    private UpdateThread updateThread;

    /**
     * Creates new form PlayerDetails
     *
     * @param p the Player to show details for.
     */
    public PlayerDetails(Player p) {
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
        this.player = p;
        PLAYER_NAME = p.getName();
        initComponents();
        txtName.setText(player.getName());
        txtCustomName.setText(player.getDisplayName());
        txtAddress.setText(player.getAddress().getHostString());
        txtUUID.setText(player.getUniqueId().toString());
        txtGameMode.setText(player.getGameMode().toString());
        checkOp.setSelected(player.isOp());
        checkSleep.setSelected(player.isSleepingIgnored());
        prgHealth.setValue((int) player.getHealth() * 5);
        prgHungar.setValue(player.getFoodLevel() * 5);
        lblXP.setText(Integer.toString(player.getLevel()));
        prgXP.setValue((int) (player.getExp() * 100));
        updateThread = new UpdateThread();
        updateThread.start();
        this.setLocationRelativeTo(null);
        this.setModal(true);
        PlayerInventory inventory = player.getInventory();
        Inventory ender = player.getEnderChest();
        DefaultListModel lmInv = new DefaultListModel();
        ItemStack[] items = inventory.getContents();
        for (ItemStack is : items) {
            lmInv.addElement(is.toString());
        }
        if (lmInv.isEmpty()) {
            lmInv.addElement("This player has no items in their inventory");
        }
        lstInventory.setModel(lmInv);
        DefaultListModel lmEnd = new DefaultListModel();
        ItemStack[] enderItems = ender.getContents();
        for (ItemStack is : enderItems) {
            lmEnd.addElement(is.toString());
        }
        if (lmEnd.isEmpty()) {
            lmEnd.addElement("This player has no items in their ender chest");
        }
        lstEnderChest.setModel(lmEnd);
    }

    /**
     * Creates new form PlayerDetails
     *
     * @param p the Player to show details for.
     */
    public PlayerDetails(OfflinePlayer p) {
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
        this.offlinePlayer = p;
        PLAYER_NAME = p.getName();
        initComponents();
        txtName.setText(offlinePlayer.getName());
        txtCustomName.setEnabled(false);
        txtAddress.setEnabled(false);
        txtUUID.setText(offlinePlayer.getUniqueId().toString());
        txtGameMode.setEnabled(false);
        checkOp.setSelected(offlinePlayer.isOp());
        checkSleep.setEnabled(false);
        prgHealth.setEnabled(false);
        prgHungar.setEnabled(false);
        lblXP.setText("0");
        prgXP.setEnabled(false);
        txtMessage.setEnabled(false);
        btnSend.setEnabled(false);
        btnKick.setEnabled(false);
        btnBan.setEnabled(false);
        btnGameMode.setEnabled(false);
        this.setLocationRelativeTo(null);
        this.setModal(true);
    }
    
    private void updateDetails(){
        txtGameMode.setText(player.getGameMode().toString());
        prgHealth.setValue((int) player.getHealth() * 5);
        prgHungar.setValue(player.getFoodLevel() * 5);
        lblXP.setText(Integer.toString(player.getLevel()));
        prgXP.setValue((int) (player.getExp() * 100));
    }

    public PlayerDetails() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setModal(true);
    }
    
    public static void showPlayerDetailsDialog(Player p){
        dialog = new PlayerDetails(p);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
    
    public static void showPlayerDetailsDialog(OfflinePlayer p){
        dialog = new PlayerDetails(p);
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }
    
    public class UpdateThread extends Thread {

        protected boolean isRunning;

        public UpdateThread() {
            this.isRunning = true;
        }

        @Override
        public void run() {
            while (isRunning) {
                SwingUtilities.invokeLater(() -> {
                    updateDetails();
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

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        txtName = new javax.swing.JTextField();
        txtCustomName = new javax.swing.JTextField();
        txtAddress = new javax.swing.JTextField();
        jLabel4 = new javax.swing.JLabel();
        txtMessage = new javax.swing.JTextField();
        btnSend = new javax.swing.JButton();
        btnClose = new javax.swing.JButton();
        btnKick = new javax.swing.JButton();
        btnBan = new javax.swing.JButton();
        checkOp = new javax.swing.JCheckBox();
        txtUUID = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        txtGameMode = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        prgHealth = new javax.swing.JProgressBar();
        jLabel8 = new javax.swing.JLabel();
        prgHungar = new javax.swing.JProgressBar();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        lblXP = new javax.swing.JLabel();
        prgXP = new javax.swing.JProgressBar();
        checkSleep = new javax.swing.JCheckBox();
        btnGameMode = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        lstInventory = new javax.swing.JList();
        jScrollPane2 = new javax.swing.JScrollPane();
        lstEnderChest = new javax.swing.JList();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Details for " + PLAYER_NAME);
        setAlwaysOnTop(true);
        setMaximumSize(new java.awt.Dimension(655, 400));
        setMinimumSize(new java.awt.Dimension(655, 400));
        setResizable(false);

        jLabel1.setText("Player Name:");

        jLabel2.setText("IP Address:");

        jLabel3.setText("Custom Name:");

        txtName.setEditable(false);

        txtCustomName.setEditable(false);

        txtAddress.setEditable(false);

        jLabel4.setText("Send message to player:");

        txtMessage.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtMessageActionPerformed(evt);
            }
        });

        btnSend.setText("Send");
        btnSend.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSendActionPerformed(evt);
            }
        });

        btnClose.setText("Close");
        btnClose.setPreferredSize(new java.awt.Dimension(80, 80));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnKick.setText("Kick");
        btnKick.setPreferredSize(new java.awt.Dimension(80, 80));
        btnKick.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnKickActionPerformed(evt);
            }
        });

        btnBan.setText("Ban");
        btnBan.setPreferredSize(new java.awt.Dimension(80, 80));
        btnBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBanActionPerformed(evt);
            }
        });

        checkOp.setText("Op");
        checkOp.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkOpActionPerformed(evt);
            }
        });

        txtUUID.setEditable(false);

        jLabel6.setText("UUID:");

        txtGameMode.setEditable(false);

        jLabel7.setText("Game Mode:");

        jLabel8.setText("Health:");

        jLabel9.setText("Hungar:");

        jLabel10.setText("Experience Level:");

        checkSleep.setText("Sleeping Ignored");
        checkSleep.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkSleepActionPerformed(evt);
            }
        });

        btnGameMode.setText("Game Mode");
        btnGameMode.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnGameMode.setMaximumSize(new java.awt.Dimension(80, 80));
        btnGameMode.setMinimumSize(new java.awt.Dimension(80, 80));
        btnGameMode.setPreferredSize(new java.awt.Dimension(80, 80));
        btnGameMode.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGameModeActionPerformed(evt);
            }
        });

        lstInventory.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane1.setViewportView(lstInventory);

        lstEnderChest.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        jScrollPane2.setViewportView(lstEnderChest);

        jLabel5.setText("Inventory");

        jLabel11.setText("Ender Chest");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnKick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnGameMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel10)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                            .addGap(49, 49, 49)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                .addComponent(jLabel3)
                                .addComponent(jLabel1, javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel2, javax.swing.GroupLayout.Alignment.TRAILING)))
                        .addComponent(jLabel6, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel7, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel8, javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(jLabel9, javax.swing.GroupLayout.Alignment.TRAILING))
                    .addComponent(jLabel4))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtMessage, javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(checkOp)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(checkSleep))
                    .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                        .addComponent(lblXP, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(prgXP, javax.swing.GroupLayout.PREFERRED_SIZE, 137, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(prgHungar, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(prgHealth, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(txtGameMode, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtUUID, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtAddress, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtCustomName, javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtName, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, 179, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnSend)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel11)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 153, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(0, 11, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(txtName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(jLabel11))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jScrollPane2)
                    .addGroup(layout.createSequentialGroup()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCustomName, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAddress, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel2))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtUUID, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addGap(8, 8, 8)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtGameMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel7))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(checkOp)
                            .addComponent(checkSleep))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel8)
                                    .addComponent(prgHealth, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(prgHungar, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel9))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel10)
                                    .addComponent(lblXP, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(prgXP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 3, Short.MAX_VALUE))
                    .addComponent(jScrollPane1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(txtMessage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel4)
                    .addComponent(btnSend))
                .addGap(65, 65, 65)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(btnKick, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnGameMode, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSendActionPerformed
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "tell " + player.getName() + " " + txtMessage.getText());
        txtMessage.setText("");
    }//GEN-LAST:event_btnSendActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void btnKickActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnKickActionPerformed
        player.kickPlayer(JOptionPane.showInputDialog("Enter reason, leave blank for no reason"));
        JOptionPane.showMessageDialog(this, player.getName() + " has been kicked from this server");
    }//GEN-LAST:event_btnKickActionPerformed

    private void btnBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBanActionPerformed
        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + player.getName());
        JOptionPane.showMessageDialog(this, player.getName() + " has been banned from this server");
    }//GEN-LAST:event_btnBanActionPerformed

    private void txtMessageActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtMessageActionPerformed
        btnSend.doClick();
    }//GEN-LAST:event_txtMessageActionPerformed

    private void checkOpActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkOpActionPerformed
        if (player.isOp()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "deop " + player.getName());
            JOptionPane.showMessageDialog(this, player.getName() + " has been de opped");
        } else {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "op " + player.getName());
            JOptionPane.showMessageDialog(this, player.getName() + " has been opped");
        }
    }//GEN-LAST:event_checkOpActionPerformed

    private void checkSleepActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkSleepActionPerformed
        if (player.isSleepingIgnored()) {
            player.setSleepingIgnored(false);
        } else {
            player.setSleepingIgnored(true);
        }
    }//GEN-LAST:event_checkSleepActionPerformed

    private void btnGameModeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGameModeActionPerformed
        player.setGameMode(GameModeDialog.showGameModeDialog(player.getGameMode()));
    }//GEN-LAST:event_btnGameModeActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBan;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnGameMode;
    private javax.swing.JButton btnKick;
    private javax.swing.JButton btnSend;
    private javax.swing.JCheckBox checkOp;
    private javax.swing.JCheckBox checkSleep;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JLabel lblXP;
    private javax.swing.JList lstEnderChest;
    private javax.swing.JList lstInventory;
    private javax.swing.JProgressBar prgHealth;
    private javax.swing.JProgressBar prgHungar;
    private javax.swing.JProgressBar prgXP;
    private javax.swing.JTextField txtAddress;
    private javax.swing.JTextField txtCustomName;
    private javax.swing.JTextField txtGameMode;
    private javax.swing.JTextField txtMessage;
    private javax.swing.JTextField txtName;
    private javax.swing.JTextField txtUUID;
    // End of variables declaration//GEN-END:variables
}
