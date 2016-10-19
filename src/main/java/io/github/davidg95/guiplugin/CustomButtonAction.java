/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

import java.awt.event.ActionEvent;
import javax.swing.AbstractAction;
import org.bukkit.Bukkit;

/**
 *
 * @author David
 */
public class CustomButtonAction extends AbstractAction {

        private static final long serialVersionUID = 1;

        CustomButtonAction(String text, String actionCommand) {
            super(text);
            putValue(ACTION_COMMAND_KEY, actionCommand);
        }

        @Override
        public void actionPerformed(ActionEvent event) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), event.getActionCommand());
        }
    }
