/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import javax.swing.JDialog;
import org.bukkit.GameMode;

/**
 *
 * @author David
 */
public class GameModeDialog extends javax.swing.JDialog {

    private static JDialog dialog;
    private static GameMode gameMode;

    /**
     * Creates new form GameMode
     */
    public GameModeDialog() {
        initComponents();
        if (gameMode.equals(GameMode.CREATIVE)) {
            btnCreative.setSelected(true);
        } else if (gameMode.equals(GameMode.SURVIVAL)) {
            btnSurvival.setSelected(true);
        } else if (gameMode.equals(GameMode.ADVENTURE)) {
            btnAdventure.setSelected(true);
        } else if (gameMode.equals(GameMode.SPECTATOR)) {
            btnSpectator.setSelected(true);
        }
        this.setLocationRelativeTo(null);
        this.setModal(true);
    }

    public static GameMode showGameModeDialog(GameMode gm) {
        gameMode = gm;
        dialog = new GameModeDialog();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
        return gameMode;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnCreative = new javax.swing.JToggleButton();
        btnSurvival = new javax.swing.JToggleButton();
        btnAdventure = new javax.swing.JToggleButton();
        btnSpectator = new javax.swing.JToggleButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Game Mode");
        setMaximumSize(new java.awt.Dimension(200, 400));
        setMinimumSize(new java.awt.Dimension(200, 400));
        setPreferredSize(new java.awt.Dimension(200, 400));
        setResizable(false);

        btnCreative.setText("Creative");
        btnCreative.setPreferredSize(new java.awt.Dimension(200, 100));
        btnCreative.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCreativeActionPerformed(evt);
            }
        });

        btnSurvival.setText("Survival");
        btnSurvival.setPreferredSize(new java.awt.Dimension(200, 100));
        btnSurvival.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSurvivalActionPerformed(evt);
            }
        });

        btnAdventure.setText("Adventure");
        btnAdventure.setPreferredSize(new java.awt.Dimension(200, 100));
        btnAdventure.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnAdventureActionPerformed(evt);
            }
        });

        btnSpectator.setText("Spectator");
        btnSpectator.setPreferredSize(new java.awt.Dimension(200, 100));
        btnSpectator.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSpectatorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                        .addComponent(btnSurvival, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnAdventure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addComponent(btnSpectator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(btnCreative, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(0, 0, 0))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(btnCreative, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnSurvival, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnAdventure, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnSpectator, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnCreativeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCreativeActionPerformed
        btnCreative.setSelected(true);
        btnSurvival.setSelected(false);
        btnAdventure.setSelected(false);
        btnSurvival.setSelected(false);
        gameMode = GameMode.CREATIVE;
        this.setVisible(false);
    }//GEN-LAST:event_btnCreativeActionPerformed

    private void btnSurvivalActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSurvivalActionPerformed
        btnCreative.setSelected(false);
        btnSurvival.setSelected(true);
        btnAdventure.setSelected(false);
        btnSurvival.setSelected(false);
        gameMode = GameMode.SURVIVAL;
        this.setVisible(false);
    }//GEN-LAST:event_btnSurvivalActionPerformed

    private void btnAdventureActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnAdventureActionPerformed
        btnCreative.setSelected(false);
        btnSurvival.setSelected(false);
        btnAdventure.setSelected(true);
        btnSurvival.setSelected(false);
        gameMode = GameMode.ADVENTURE;
        this.setVisible(false);
    }//GEN-LAST:event_btnAdventureActionPerformed

    private void btnSpectatorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSpectatorActionPerformed
        btnCreative.setSelected(false);
        btnSurvival.setSelected(false);
        btnAdventure.setSelected(false);
        btnSurvival.setSelected(true);
        gameMode = GameMode.SPECTATOR;
        this.setVisible(false);
    }//GEN-LAST:event_btnSpectatorActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JToggleButton btnAdventure;
    private javax.swing.JToggleButton btnCreative;
    private javax.swing.JToggleButton btnSpectator;
    private javax.swing.JToggleButton btnSurvival;
    // End of variables declaration//GEN-END:variables
}