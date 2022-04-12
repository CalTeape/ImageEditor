package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the Transforms menu.
 * </p>
 * 
 * <p>
 * The Transforms menu contains actions that update each pixel in an image based
 * on the menu selection.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA
 * 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class TransformsActions {

   /** A list of actions for the Transforms menu. */
   protected ArrayList<Action> actions;
   protected ArrayList<Action> tools;

   /**
    * <p>
    * Create a set of Transforms menu actions.
    * </p>
    */
   public TransformsActions() {
      actions = new ArrayList<Action>();
      tools = new ArrayList<Action>();
      actions.add(new RotateAntiClockwiseAction("Rotate Left", null, "Rotate 90 degrees Anticlockwise",Integer.valueOf(KeyEvent.VK_A)));
      actions.add(new RotateClockwiseAction("Rotate Right", null, "Rotate 90 degrees Clockwise",Integer.valueOf(KeyEvent.VK_S)));
      actions.add(new ResizeAction("Resize the image", null, "change the image size in Pixels", Integer.valueOf(KeyEvent.VK_D)));
      actions.add(new InvertVerticalAction("Invert the Image Vertically", null, "Invert the image on the y axis", Integer.valueOf(KeyEvent.VK_F)));
      actions.add(new InvertHorizontalAction("Invert Image Horizontally", null, "Invert the image along the x axis", Integer.valueOf(KeyEvent.VK_G)));
      tools.add(new RotateAntiClockwiseAction("", new ImageIcon("./src/imageIcons/rotateLeft.png"), "Rotate 90 degrees Anticlockwise", Integer.valueOf(KeyEvent.VK_F1)));
      tools.add(new RotateClockwiseAction("", new ImageIcon("./src/imageIcons/rotateRight.png"), "Rotate 90 degrees Clockwise", Integer.valueOf(KeyEvent.VK_F1)));
   }


     /**
     * <p>
     * Create a menu contianing the list of transform actions.
     * </p>
     * 
     * @return The transform menu UI element.
     */
   public JMenu createMenu() {
      JMenu fileMenu = new JMenu("Transforms");

      for (Action action : actions) {
         fileMenu.add(new JMenuItem(action));
      }

      return fileMenu;
   }

   /** 
    * <p>
    * Create a tool bar containing the list of tansform actions.
    * </p>
    * 
    * @return The transform tool bar element.
    */
   public JToolBar createToolBar(){
      JToolBar transformTool = new JToolBar();
      for(Action tool: tools){
          transformTool.add(new JMenuItem(tool));
      }
      return transformTool;
  }


   public class ResizeAction extends ImageAction {

      /**
       * <p>
       * Create a new ResizeImage action.
       * </p>
       * 
       * @param name     The name of the action (ignored if null).
       * @param icon     An icon to use to represent the action (ignored if null).
       * @param desc     A brief description of the action (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
       */
      ResizeAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
         super(name, icon, desc, mnemonic);
      }

      public void actionPerformed(ActionEvent e) {

         int height = 1;
         int width = 1;

         // taken from FilterActions
         SpinnerNumberModel pixelHeightModel = new SpinnerNumberModel(1, 1, 1920, 1);
         SpinnerNumberModel pixelWidthModel = new SpinnerNumberModel(1, 1, 1920, 1);
         JSpinner pixelHeightSpinner = new JSpinner(pixelHeightModel);
         JSpinner pixelWidthSpinner = new JSpinner(pixelWidthModel);

         JPanel panel = new JPanel();
         panel.add(new JLabel("Height:"));
         panel.add(pixelHeightSpinner);
         panel.add(Box.createHorizontalStrut(15));
         panel.add(new JLabel("Width:"));
         panel.add(pixelWidthSpinner);

         int option = JOptionPane.showOptionDialog(null, panel, "Enter new image size", JOptionPane.OK_CANCEL_OPTION,
               JOptionPane.QUESTION_MESSAGE, null, null, null);

         if (option == JOptionPane.CANCEL_OPTION || option == JOptionPane.CLOSED_OPTION) {
            return;
         } else if (option == JOptionPane.OK_OPTION) {
            height = pixelHeightModel.getNumber().intValue();
            width = pixelWidthModel.getNumber().intValue();
         }

         target.getImage().apply(new ResizeImage(height, width));
         target.repaint();
         target.getParent().revalidate();
      }

   }

   public class RotateAntiClockwiseAction extends ImageAction {

      /**
       * <p>
       * Create a new RotateAntiClockwise action.
       * </p>
       * 
       * @param name     The name of the action (ignored if null).
       * @param icon     An icon to use to represent the action (ignored if null).
       * @param desc     A brief description of the action (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
       */
      RotateAntiClockwiseAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
         super(name, icon, desc, mnemonic);
      }

      // initiates the action on RotateImage
      public void actionPerformed(ActionEvent e) {
         target.getImage().apply(new RotateImage(false));
         target.repaint();
         target.getParent().revalidate();
      }
   }

   public class RotateClockwiseAction extends ImageAction {

      /**
       * <p>
       * Create a new RotateClockwise action.
       * </p>
       * 
       * @param name     The name of the action (ignored if null).
       * @param icon     An icon to use to represent the action (ignored if null).
       * @param desc     A brief description of the action (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
       */
      RotateClockwiseAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
         super(name, icon, desc, mnemonic);
      }

      public void actionPerformed(ActionEvent e) {
         target.getImage().apply(new RotateImage(true));
         target.repaint();
         target.getParent().revalidate();
      }
   }

   public class InvertVerticalAction extends ImageAction {

      /**
       * <p>
       * Create a new InvertVerticalaction.
       * </p>
       * 
       * @param name     The name of the action (ignored if null).
       * @param icon     An icon to use to represent the action (ignored if null).
       * @param desc     A brief description of the action (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
       */
      InvertVerticalAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
         super(name, icon, desc, mnemonic);
      }

      public void actionPerformed(ActionEvent e) {
         target.getImage().apply(new InvertImage(true));
         target.repaint();
         target.getParent().revalidate();
      }
   }

   public class InvertHorizontalAction extends ImageAction {

      /**
       * <p>
       * Create a new InvertHorizontalAction.
       * </p>
       * 
       * @param name     The name of the action (ignored if null).
       * @param icon     An icon to use to represent the action (ignored if null).
       * @param desc     A brief description of the action (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut (ignored if null).
       */
      InvertHorizontalAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
         super(name, icon, desc, mnemonic);
      }

      public void actionPerformed(ActionEvent e) {
         target.getImage().apply(new InvertImage(false));
         target.repaint();
         target.getParent().revalidate();
      }
   }
}