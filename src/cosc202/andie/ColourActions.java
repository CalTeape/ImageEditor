package cosc202.andie;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the Colour menu.
 * </p>
 * 
 * <p>
 * The Colour menu contains actions that affect the colour of each pixel directly 
 * without reference to the rest of the image.
 * This includes conversion to greyscale in the sample code, but more operations will need to be added.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class ColourActions {
    
    /** A list of actions for the Colour menu. */
    protected ArrayList<Action> actions;
    protected ArrayList<Action> tools;

    /**
     * <p>
     * Create a set of Colour menu actions.
     * </p>
     */
    public ColourActions() {
        actions = new ArrayList<Action>();
        tools = new ArrayList<Action>();
        actions.add(new ConvertToGreyAction("Greyscale", null, "Convert to greyscale", Integer.valueOf(KeyEvent.VK_A)));
        actions.add(new AdjustBrightnessAndContrastAction("Brightness & Contrast", null, "Adjust the brightness and/or contrast", Integer.valueOf(KeyEvent.VK_S)));
        tools.add(new AdjustBrightnessAndContrastAction("", new ImageIcon("./src/imageIcons/brightness.png"), "Adjust the brightness and/or contrast", null));
    }

    /**
     * <p>
     * Create a menu contianing the list of Colour actions.
     * </p>
     * 
     * @return The colour menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu("Colour");

        for(Action action: actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }

    /** 
    * <p>
    * Create a tool bar containing the list of Colour actions.
    * </p>
    * 
    * @return The colour tool bar element.
    */
    public JToolBar createToolBar(){
        JToolBar colourTool = new JToolBar();
        for(Action tool: tools){
            colourTool.add(new JMenuItem(tool));
        }
        return colourTool;
    }

    /**
     * <p>
     * Action to convert an image to greyscale.
     * </p>
     * 
     * @see ConvertToGrey
     */
    public class ConvertToGreyAction extends ImageAction {

        /**
         * <p>
         * Create a new convert-to-grey action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        ConvertToGreyAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the convert-to-grey action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ConvertToGreyAction is triggered.
         * It changes the image to greyscale.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            try{
            target.getImage().apply(new ConvertToGrey());
            target.repaint();
            target.getParent().revalidate();
        }catch(NullPointerException E){
            JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before greyscaling", "alert!", JOptionPane.ERROR_MESSAGE);
         }
        }

    }

    /**
     * <p>
     * Action to adjust the brightness and contrast of an image.
     * </p>
     * 
     * @see AdjustBrightnessAndContrast
     */
    public class AdjustBrightnessAndContrastAction extends ImageAction {

        /**
         * <p>
         * Create a new adjust-brightness-and-contrast action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        AdjustBrightnessAndContrastAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the adjust-brightness-and-contrast action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the AdjustBrightnessAndContrastAction is triggered.
         * It adjusts the brightness of the image.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            try{
            int brightness = 0;
            int contrast = 0;

            JPanel optionPanel = new JPanel(new GridLayout(0, 1));
            JSlider brightnessSlider = new JSlider(-100, 100, brightness);
            JSlider contrastSlider = new JSlider(-100, 100, contrast);

            optionPanel.add(new JLabel("Brightness:"));
            optionPanel.add(brightnessSlider);
            optionPanel.add(new JLabel("Contrast:"));
            optionPanel.add(contrastSlider);

            
            int selectedOption = JOptionPane.showOptionDialog(null, optionPanel, "Adjust Brightness and Contrast", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            
            if(selectedOption == JOptionPane.CANCEL_OPTION){
                return;
            }else if(selectedOption == JOptionPane.OK_OPTION){
                brightness = brightnessSlider.getValue();
                contrast = contrastSlider.getValue();
            }

            target.getImage().apply(new AdjustBrightnessAndContrast(brightness, contrast));
            target.repaint();
            target.getParent().revalidate();
        }catch(NullPointerException E){
            JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before adjusting brightness and contrast", "alert!", JOptionPane.ERROR_MESSAGE);
         }
        }

    }


}
