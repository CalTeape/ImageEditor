package cosc202.andie;

import java.util.*;
import java.awt.event.*;

import javax.swing.*;

 /**
 * <p>
 * Actions provided by the Edit menu.
 * </p>
 * 
 * <p>
 * The Edit menu is very common across a wide range of applications.
 * There are a lot of operations that a user might expect to see here.
 * In the sample code there are Undo and Redo actions, but more may need to be added.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills, Callum Teape, Jarod Peacock
 * @version 1.0
 */
public class EditActions {
    
    /** A list of actions for the Edit menu. */
    protected ArrayList<Action> actions;
    protected ArrayList<Action> tools;

    /**
     * <p>
     * Create a set of Edit menu actions.
     * </p>
     */
    public EditActions() {
        actions = new ArrayList<Action>();
        tools = new ArrayList<Action>();
        actions.add(new UndoAction("Undo", null, "Undo", Integer.valueOf(KeyEvent.VK_A)));
        actions.add(new RedoAction("Redo", null, "Redo", Integer.valueOf(KeyEvent.VK_S)));
        actions.add(new RecordMacroAction( "Record Macro", null, "RecordMacro", Integer.valueOf(KeyEvent.VK_D)));
        actions.add(new PerformMacroAction( "Perform Macro", null, "PerformMacro", Integer.valueOf(KeyEvent.VK_F)));
        tools.add(new UndoAction("", new ImageIcon("./src/imageIcons/undo.jpg"), "Undo", Integer.valueOf(KeyEvent.VK_Z)));
        tools.add(new RedoAction("", new ImageIcon("./src/imageIcons/redo.jpg"), "Redo", Integer.valueOf(KeyEvent.VK_Y)));
    }

    /**
     * <p>
     * Create a menu contianing the list of Edit actions.
     * </p>
     * 
     * @return The edit menu UI element.
     */
    public JMenu createMenu() {
        JMenu editMenu = new JMenu("Edit");

        for (Action action: actions) {
            editMenu.add(new JMenuItem(action));
        }

        return editMenu;
    }


    /** 
    * <p>
    * Create a tool bar containing the list of edit actions.
    * </p>
    * 
    * @return The edit tool bar element.
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
     * Action to undo an {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#undo()
     */
    public class UndoAction extends ImageAction {

        /**
         * <p>
         * Create a new undo action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        UndoAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        /**
         * <p>
         * Callback for when the undo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the UndoAction is triggered.
         * It undoes the most recently applied operation.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            try{
            target.getImage().undo();
            target.repaint();
            target.getParent().revalidate();
        }catch(EmptyStackException E){
            JOptionPane.showMessageDialog(null, "Error: no operations to undo!", "alert!", JOptionPane.ERROR_MESSAGE);
        }
        }
    }

    /**
     * Method to record one or more {@link ImageOperation}s
     * 
     * @param name The name of the action
     * @param icon icon to represent action
     * @param desc brief description
     * @param mnemonic A shortcut key to use for accessing the method
     */
    public class RecordMacroAction extends ImageAction{

        RecordMacroAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {

            if (!(target.getImage().macro)) {
                int m = JOptionPane.showConfirmDialog(null, "Would you like to begin recording of a new Macro?\nMake sure there is currently an image open in the editor!", "New Macro?",  JOptionPane.YES_NO_OPTION);
                    if (m == JOptionPane.YES_OPTION) {
                        target.getImage().recordMacro();
                    }
                } else {
                int r = JOptionPane.showConfirmDialog(null, "Would you like to save the current Macro?", "Save Macro?",  JOptionPane.YES_NO_OPTION);
                if (r == JOptionPane.YES_OPTION) {
                    JFileChooser fChooser = new JFileChooser();
                    int result = fChooser.showSaveDialog(target);

                    if (result == JFileChooser.APPROVE_OPTION) {
                        try {
                            String fp = fChooser.getSelectedFile().getCanonicalPath();
                            target.getImage().saveMacro(fp);
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(null, "Error: this is not a valid ops file. Please try again.", "alert!", JOptionPane.ERROR_MESSAGE);
                        }
                    }
                } else {
                    int s = JOptionPane.showConfirmDialog(null, "Would you like to begin recording of a new Macro?", "New Macro?",  JOptionPane.YES_NO_OPTION);
                    if (s == JOptionPane.YES_OPTION) {
                        target.getImage().recordMacro(true);
                    } else {
                        target.getImage().recordMacro(false);
                    }
                }
            }

            return;
        }
    }

    /**
     * Method to be used in conjunction with MacroStartAction to save a macro
     * 
     * @param name The name of the action
     * @param icon icon to represent action
     * @param desc brief description
     * @param mnemonic A shortcut key to use for accessing the method
     */
    public class PerformMacroAction extends ImageAction {

        PerformMacroAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        public void actionPerformed(ActionEvent e) {
            JFileChooser fileChooser = new JFileChooser();
            int result = fileChooser.showOpenDialog(target);

            if (result == JFileChooser.APPROVE_OPTION) {
                try {
                    String ofp = fileChooser.getSelectedFile().getCanonicalPath();
                    target.getImage().performMacro(ofp);
                    target.repaint();
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(null, "Error: Make sure an image is loaded and that a valid ops file is selected", "alert!", JOptionPane.ERROR_MESSAGE);
                }
            }
        }
    }

     /**
     * <p>
     * Action to redo an {@link ImageOperation}.
     * </p>
     * 
     * @see EditableImage#redo()
     */   
    public class RedoAction extends ImageAction {

        /**
         * <p>
         * Create a new redo action.
         * </p>
         * 
         * @param name The name of the action (ignored if null).
         * @param icon An icon to use to represent the action (ignored if null).
         * @param desc A brief description of the action  (ignored if null).
         * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
         */
        RedoAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
            super(name, icon, desc, mnemonic);
        }

        
        /**
         * <p>
         * Callback for when the redo action is triggered.
         * </p>
         * 
         * <p>
         * This method is called whenever the RedoAction is triggered.
         * It redoes the most recently undone operation.
         * </p>
         * 
         * @param e The event triggering this callback.
         */
        public void actionPerformed(ActionEvent e) {
            try{
            target.getImage().redo();
            target.repaint();
            target.getParent().revalidate();
        }catch(EmptyStackException E){
            JOptionPane.showMessageDialog(null, "Error: no operations to redo!", "alert!", JOptionPane.ERROR_MESSAGE);
        }
        }
    }

}
