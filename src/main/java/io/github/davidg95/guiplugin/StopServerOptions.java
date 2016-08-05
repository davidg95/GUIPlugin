/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import javax.swing.JDialog;

/**
 * From which asks the user how they want to stop the server.
 *
 * @author David
 */
public class StopServerOptions extends javax.swing.JDialog {

    private static JDialog dialog;
    private static int result;

    /**
     * Creates new form StopServerOptions
     */
    public StopServerOptions() {
        initComponents();
        this.setLocationRelativeTo(null);
        this.setModal(true);
    }

    /**
     * Show the stop server options dialog. Returns 0 of the user selects
     * cancel. Returns 1 if they just want to stop the server. returns 2 if they
     * want to stop the server and shut down the computer. Returns 3 if the want
     * to stop the server and put the computer to sleep. If backup is selected,
     * the value returned is increased by 3.
     *
     * @return 0, 1, 2 or 3 depending on what options the user selected.
     */
    public static int showStopOptions() {
        dialog = new StopServerOptions();
        dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        dialog.setVisible(true);

        return result;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        radStop = new javax.swing.JRadioButton();
        radShut = new javax.swing.JRadioButton();
        radSleep = new javax.swing.JRadioButton();
        btnStop = new javax.swing.JButton();
        btnCancel = new javax.swing.JButton();
        chkBackup = new javax.swing.JCheckBox();

        setDefaultCloseOperation(javax.swing.WindowConstants.DISPOSE_ON_CLOSE);
        setTitle("Stop Options");
        setResizable(false);

        buttonGroup1.add(radStop);
        radStop.setSelected(true);
        radStop.setText("Stop server");

        buttonGroup1.add(radShut);
        radShut.setText("Stop server and shut down computer");

        buttonGroup1.add(radSleep);
        radSleep.setText("Stop server and put computer to sleep");

        btnStop.setBackground(new java.awt.Color(255, 0, 0));
        btnStop.setText("STOP");
        btnStop.setPreferredSize(new java.awt.Dimension(200, 129));
        btnStop.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnStopActionPerformed(evt);
            }
        });

        btnCancel.setBackground(new java.awt.Color(0, 255, 0));
        btnCancel.setText("CANCEL");
        btnCancel.setPreferredSize(new java.awt.Dimension(200, 129));
        btnCancel.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelActionPerformed(evt);
            }
        });

        chkBackup.setSelected(true);
        chkBackup.setText("Save backup");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(btnStop, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, 0)
                .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(chkBackup)
                    .addComponent(radSleep)
                    .addComponent(radShut)
                    .addComponent(radStop))
                .addGap(83, 83, 83))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(52, 52, 52)
                .addComponent(radStop)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(radShut)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(radSleep)
                .addGap(6, 6, 6)
                .addComponent(chkBackup)
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnStop, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(btnCancel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void btnStopActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnStopActionPerformed
        if (radStop.isSelected()) {
            result = 1;
        } else if (radShut.isSelected()) {
            result = 2;
        } else if (radSleep.isSelected()) {
            result = 3;
        }
        if (chkBackup.isSelected()) {
            result += 3;
        }
        this.dispose();
    }//GEN-LAST:event_btnStopActionPerformed

    private void btnCancelActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelActionPerformed
        result = 0;
        this.dispose();
    }//GEN-LAST:event_btnCancelActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnCancel;
    private javax.swing.JButton btnStop;
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JCheckBox chkBackup;
    private javax.swing.JRadioButton radShut;
    private javax.swing.JRadioButton radSleep;
    private javax.swing.JRadioButton radStop;
    // End of variables declaration//GEN-END:variables
}
