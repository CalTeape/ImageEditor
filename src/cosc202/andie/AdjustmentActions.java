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
 * @author Steven Mills, Jack Searle, Callum Teape
 * @version 1.0
 */
public class AdjustmentActions {
    
    /** Lists of Colour actions for the Colour menu and toolbar. */
    protected ArrayList<Action> actions;
    protected ArrayList<Action> tools;

    /**
     * <p>
     * Create a set of Colour menu and toolbar actions.
     * </p>
     */
    public AdjustmentActions() {
        actions = new ArrayList<Action>();
        tools = new ArrayList<Action>();
        actions.add(new PosterizeImageAction("Posterize", null, "Posterize the image", null));

        actions.add(new ConvertToGreyAction("Greyscale", null, "Convert to greyscale", Integer.valueOf(KeyEvent.VK_A)));
        actions.add(new BrightnessAndContrastAction("Brightness & Contrast", null, "Adjust the brightness and/or contrast", Integer.valueOf(KeyEvent.VK_S)));
        tools.add(new BrightnessAndContrastAction("", new ImageIcon("./src/imageIcons/brightness.png"), "Adjust the brightness and/or contrast", null));
    }

    /**
     * <p>
     * Create a menu contianing the list of Colour actions.
     * </p>
     * 
     * @return The colour menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu("Adjustments");

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



    public class PosterizeImageAction extends ImageAction {

        PosterizeImageAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            try{

                int colourRange = 2;
                SpinnerNumberModel pixelColorRangeModel = new SpinnerNumberModel(6, 2, 20, 2);
                JSpinner pixelColoSpinner = new JSpinner(pixelColorRangeModel);

                JPanel panel = new JPanel();
                panel.add(new JLabel("Colour Range"));
                panel.add(pixelColoSpinner);
                
                int option = JOptionPane.showOptionDialog(null, panel, "what is the Posterization threshhold", JOptionPane.OK_CANCEL_OPTION,
               JOptionPane.QUESTION_MESSAGE, null, null, null);

         if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
            return;
         } else if (option == JOptionPane.OK_OPTION) {          
            colourRange = pixelColorRangeModel.getNumber().intValue();
            }
            target.getImage().apply(new PosterizeImage(colourRange));
            target.repaint();
            target.getParent().revalidate();
        }catch(NullPointerException E){
            JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before resizing", "alert!", JOptionPane.ERROR_MESSAGE);
         }
         }
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
     * @see BrightnessAndContrast
     */
    public class BrightnessAndContrastAction extends ImageAction {

        /**
         * <p>
         * Create a new brightness-and-contrast action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        BrightnessAndContrastAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the brightness-and-contrast action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the BrightnessAndContrastAction is triggered.
         * It adjusts the brightness of the image.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            try{
            int brightness = 0;
            int contrast = 0;

            // An instance of JPanel with two seperate sliders to determine new values of 'brightness' and 'contrast'.
            JPanel optionPanel = new JPanel(new GridLayout(0, 1));
            JSlider brightnessSlider = new JSlider(-100, 100, brightness);
            JSlider contrastSlider = new JSlider(-100, 100, contrast);

            // Adds sliders and respective labels to previously created JPanel.
            optionPanel.add(new JLabel("Brightness:"));
            optionPanel.add(brightnessSlider);
            optionPanel.add(new JLabel("Contrast:"));
            optionPanel.add(contrastSlider);
            
            // Pop-up dialog box to ask user for the new 'brightness' and 'contrast' values.
            int selectedOption = JOptionPane.showOptionDialog(null, optionPanel, "Adjust Brightness and Contrast", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
            if(selectedOption == JOptionPane.CANCEL_OPTION){
                return;
            }else if(selectedOption == JOptionPane.OK_OPTION){
                brightness = brightnessSlider.getValue();
                contrast = contrastSlider.getValue();
            }

            // Create and apply the adjustment(s).
            target.getImage().apply(new BrightnessAndContrast(brightness, contrast));
            target.repaint();
            target.getParent().revalidate();
        }catch(NullPointerException E){
            JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before adjusting brightness and contrast", "alert!", JOptionPane.ERROR_MESSAGE);
         }
        }

    }


}
