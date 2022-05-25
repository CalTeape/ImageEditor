package cosc202.andie;

import java.util.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the Draw menu.
 * </p>
 * 
 * <p>
 * The Draw menu contains actions that allow the user to draw elements on to the image.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills, Jack Searle
 * @version 1.0
 */
public class DrawActions {
    
    /** Lists of Drawing actions for the Draw menu and toolbar. */
    protected ArrayList<Action> actions;
    protected ArrayList<Action> tools;

    /**
     * <p>
     * Create a set of draw menu and toolbar actions.
     * </p>
     */
    public DrawActions() {
        actions = new ArrayList<Action>();
        tools = new ArrayList<Action>();
        actions.add(new DrawRectangleAction("Draw Rectangle", null, "Draw a rectangle", null));
        tools.add(new DrawRectangleAction("", new ImageIcon("./src/imageIcons/drawRectangle.png"), "Draw a rectangle", null));
        actions.add(new DrawOvalAction("Draw Oval", null, "Draw an oval", null));
        tools.add(new DrawOvalAction("", new ImageIcon("./src/imageIcons/drawCircle.png"), "Draw an oval", null));
    }

    /**
     * <p>
     * Create a menu contianing the list of draw actions.
     * </p>
     * 
     * @return The draw menu UI element.
     */
    public JMenu createMenu() {
        JMenu fileMenu = new JMenu("Draw");

        for(Action action: actions) {
            fileMenu.add(new JMenuItem(action));
        }

        return fileMenu;
    }


        /** 
    * <p>
    * Create a tool bar containing the list of draw actions.
    * </p>
    * 
    * @return The draw tool bar element.
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
     * Action to draw a rectangle.
     * </p>
     */
    public class DrawRectangleAction extends ImageAction {

        /**
         * <p>
         * Create a new draw-rectangle action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        DrawRectangleAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the draw-rectangle action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the DrawRectangleAction is triggered.
         * It draws a rectangle in the region select in the active colour.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            try{
                // An instance of JPanel and a colour chooser.
                JPanel optionPanel = new JPanel(new GridLayout(0, 1));
                JCheckBox checkbox = new JCheckBox("Fill Colour");
                optionPanel.add(checkbox);
                
                // Pop-up dialog box to ask user to select a colour.
                int selectedOption = JOptionPane.showOptionDialog(null, optionPanel, "Fill Rectangle?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if(selectedOption == JOptionPane.CANCEL_OPTION){
                    return;
                }else if(selectedOption == JOptionPane.OK_OPTION){
                    // undo selection visuals and reassign boolean.
                    target.getImage().undo();
                    Andie.activeSelection = false;
                    // draw rectangle accordingly and refresh image.
                    if(checkbox.isSelected())  target.getImage().apply(new DrawRectangle(Andie.mouseSelection, ColourActions.activeColour, true));
                    if(!checkbox.isSelected()) target.getImage().apply(new DrawRectangle(Andie.mouseSelection, ColourActions.activeColour, false));
                    target.repaint();
                    target.getParent().revalidate();
                }
            }catch(NullPointerException E){
                JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before drawing a rectangle", "alert!", JOptionPane.ERROR_MESSAGE);
            }
        
        }

    }


    /**
     * <p>
     * Action to draw an oval.
     * </p>
     */
    public class DrawOvalAction extends ImageAction {

        /**
         * <p>
         * Create a new draw-oval action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        DrawOvalAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the draw-oval action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the DrawOvalAction is triggered.
         * It draws an oval in the region select in the active colour.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            try{
                // An instance of JPanel and a colour chooser.
                JPanel optionPanel = new JPanel(new GridLayout(0, 1));
                JCheckBox checkbox = new JCheckBox("Fill Colour");
                optionPanel.add(checkbox);
                
                // Pop-up dialog box to ask user to select a colour.
                int selectedOption = JOptionPane.showOptionDialog(null, optionPanel, "Fill Oval?", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
                if(selectedOption == JOptionPane.CANCEL_OPTION){
                    return;
                }else if(selectedOption == JOptionPane.OK_OPTION){
                    // undo selection visuals and reassign boolean.
                    target.getImage().undo();
                    Andie.activeSelection = false;
                    // draw oval accordingly and refresh image.
                    if(checkbox.isSelected())  target.getImage().apply(new DrawOval(Andie.mouseSelection, ColourActions.activeColour,  true));
                    if(!checkbox.isSelected()) target.getImage().apply(new DrawOval(Andie.mouseSelection, ColourActions.activeColour, false));
                    target.repaint();
                    target.getParent().revalidate();
                }
            }catch(NullPointerException E){
                JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before drawing an oval", "alert!", JOptionPane.ERROR_MESSAGE);
            }
        
        }

    }

}
