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
 * The Colour menu contains actions that allow the user to select and reassign the 
 * activeColour data field which will be accessed by any user-painted elements.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills, Jack Searle
 * @version 1.0
 */
public class ColourActions {
    
    /** The active colour to be used for user-painted elements. */
    public static Color activeColour = Color.WHITE;
    /** Lists of Colour actions for the Colour menu and toolbar. */
    protected ArrayList<Action> actions;
    protected ArrayList<Action> tools;

    /**
     * <p>
     * Create a set of colour menu and toolbar actions.
     * </p>
     */
    public ColourActions() {
        actions = new ArrayList<Action>();
        tools = new ArrayList<Action>();
        actions.add(new ColourSelectionAction("Colour Select", null, "Select a colour", null));
        tools.add(new ColourSelectionAction("", new ImageIcon("./src/imageIcons/palette.png"), "Select a colour", null));
        actions.add(new OpacityAction("Opacity Select", null, "Select opacity", null));
        tools.add(new OpacityAction("", new ImageIcon("./src/imageIcons/opacity.png"), "Select opacity", null));
    }

    /**
     * <p>
     * Create a menu contianing the list of colour actions.
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
    * Create a tool bar containing the list of colour actions.
    * </p>
    * 
    * @return The colour tool bar element.
    */
    public JToolBar createToolBar(){
        JToolBar editTool = new JToolBar();
        for(Action tool: tools){
            editTool.add(new JMenuItem(tool));
        }
        return editTool;
    }



    /**
     * <p>
     * Action to select the active colour.
     * </p>
     */
    public class ColourSelectionAction extends ImageAction {

        /**
         * <p>
         * Create a new colour-selection action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        ColourSelectionAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the colour-selection action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the ColourSelectionAction is triggered.
         * It reassigns the activeColour variable.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {

            // An instance of JPanel and a colour chooser.
            JPanel optionPanel = new JPanel(new GridLayout(0, 1));
            JColorChooser colourChooser = new JColorChooser(activeColour);
            optionPanel.add(colourChooser);
            
            // Pop-up dialog box to ask user to select a colour.
            int selectedOption = JOptionPane.showOptionDialog(null, optionPanel, "Select a colour", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if(selectedOption == JOptionPane.CANCEL_OPTION){
                return;
            }else if(selectedOption == JOptionPane.OK_OPTION){
                activeColour = colourChooser.getColor();
            }
        
        }

    }

    /**
     * <p>
     * Action to select the opacity of the active colour.
     * </p>
     */
    public class OpacityAction extends ImageAction {

        /**
         * <p>
         * Create a new opacity-selection action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        OpacityAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the opacity-selection action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the OpacityAction is triggered.
         * It reassigns the activeColour variable.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {

            // An instance of JPanel and a colour chooser.
            JPanel optionPanel = new JPanel(new GridLayout(0, 1));
            JSlider opacitySlider = new JSlider(0, 255, activeColour.getAlpha());
            optionPanel.add(new JLabel("Opacity:"));
            optionPanel.add(opacitySlider);
            
            // Pop-up dialog box to ask user to select a colour.
            int selectedOption = JOptionPane.showOptionDialog(null, optionPanel, "Select opacity", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if(selectedOption == JOptionPane.CANCEL_OPTION){
                return;
            }else if(selectedOption == JOptionPane.OK_OPTION){
                activeColour = new Color(activeColour.getRed(), activeColour.getGreen(), activeColour.getBlue(), opacitySlider.getValue());
            }
        
        }

    }

}
