/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import java.util.ArrayList;
import javax.swing.DefaultListModel;
import javax.swing.JOptionPane;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

/**
 *
 * @author David
 */
public class BanList extends javax.swing.JDialog {

    private ArrayList<OfflinePlayer> playerList;
    private final GUI g;
    
    /**
     * Creates new form BanList
     */
    public BanList(GUI g) {
        this.g = g;
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
        initComponents();
        setModal(true);
        playerList = new ArrayList<>();
        updateBanList();
        this.setLocationRelativeTo(null);
    }

    @Override
    public void setVisible(boolean visible) {
        updateBanList();
        super.setVisible(visible);
    }

    public final void updateBanList() {
        DefaultListModel lm = new DefaultListModel();
        playerList = new ArrayList<>();
        for (OfflinePlayer p : Bukkit.getBannedPlayers()) {
            lm.addElement(p.getName());
            playerList.add(p);
        }
        if (lm.isEmpty()) {
            lm.addElement("There are no banned players");
        }
        lstBanList.setModel(lm);
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
        lstBanList = new javax.swing.JList();
        btnClose = new javax.swing.JButton();
        btnBan = new javax.swing.JButton();
        btnPardon = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Ban List");
        setAlwaysOnTop(true);
        setResizable(false);

        lstBanList.setModel(new javax.swing.AbstractListModel() {
            String[] strings = { "Item 1", "Item 2", "Item 3", "Item 4", "Item 5" };
            public int getSize() { return strings.length; }
            public Object getElementAt(int i) { return strings[i]; }
        });
        lstBanList.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lstBanListMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(lstBanList);

        btnClose.setText("Close");
        btnClose.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnClose.setPreferredSize(new java.awt.Dimension(90, 90));
        btnClose.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCloseActionPerformed(evt);
            }
        });

        btnBan.setText("Ban");
        btnBan.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnBan.setPreferredSize(new java.awt.Dimension(90, 90));
        btnBan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBanActionPerformed(evt);
            }
        });

        btnPardon.setText("Pardon");
        btnPardon.setMargin(new java.awt.Insets(0, 0, 0, 0));
        btnPardon.setPreferredSize(new java.awt.Dimension(90, 90));
        btnPardon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnPardonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnPardon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 280, Short.MAX_VALUE)
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(btnBan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(btnClose, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnPardon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnBanActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBanActionPerformed
        String name = JOptionPane.showInputDialog(this, "Enter player to ban", "Ban Player", JOptionPane.PLAIN_MESSAGE);
        if (!name.equals("")) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "ban " + name);
            updateBanList();
            JOptionPane.showMessageDialog(this, name + " has been banned from this server");
            g.toTextArea(name + " has been banned from this server");
        } else {
            JOptionPane.showMessageDialog(rootPane, "You must enter a player!", "Ban Player", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnBanActionPerformed

    private void btnPardonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnPardonActionPerformed
        if (lstBanList.getSelectedIndex() != -1) {
            if (!playerList.isEmpty()) {
                String name = playerList.get(lstBanList.getSelectedIndex()).getName();
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), "pardon " + name);
                updateBanList();
                JOptionPane.showMessageDialog(this, name + " has been pardoned");
                g.toTextArea(name + " has been pardoned");
            } else {
                JOptionPane.showMessageDialog(this, "Select a player!", "Ban List", JOptionPane.ERROR_MESSAGE);
            }
        } else {
            JOptionPane.showMessageDialog(this, "Select a player!", "Ban List", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_btnPardonActionPerformed

    private void btnCloseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCloseActionPerformed
        this.dispose();
    }//GEN-LAST:event_btnCloseActionPerformed

    private void lstBanListMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lstBanListMouseClicked
        if(evt.getClickCount() == 2){
            new PlayerDetails(playerList.get(lstBanList.getSelectedIndex())).setVisible(true);
            this.dispose();
        }
    }//GEN-LAST:event_lstBanListMouseClicked

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
//            java.util.logging.Logger.getLogger(Whitelist.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (InstantiationException ex) {
//            java.util.logging.Logger.getLogger(Whitelist.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (IllegalAccessException ex) {
//            java.util.logging.Logger.getLogger(Whitelist.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
//            java.util.logging.Logger.getLogger(Whitelist.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
//        }
//        //</editor-fold>
//
//        /* Create and display the form */
//        java.awt.EventQueue.invokeLater(new Runnable() {
//            public void run() {
//                new Whitelist().setVisible(true);
//            }
//        });
//    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBan;
    private javax.swing.JButton btnClose;
    private javax.swing.JButton btnPardon;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JList lstBanList;
    // End of variables declaration//GEN-END:variables
}
