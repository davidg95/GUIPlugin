/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.davidg95.guiplugin;

/**
 * Interface for any GUI used in this server. Any GUI which access the config
 * window must implement this interface.
 *
 * @author David
 */
public interface GUIInterface {

    /**
     * Method to output to the text area.
     *
     * @param input the String to output to the text area.
     */
    public void toTextArea(String input);

    /**
     * Set the value of the label for the stop time.
     *
     * @param s the String to set as the value.
     */
    public void setStopTimeLabel(String s);

    /**
     * Update the GUI according to the config.
     */
    public void updateConfig();
}
