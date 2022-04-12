package cosc202.andie;

import java.util.*;
import java.awt.event.*;
import javax.swing.*;

/**
 * <p>
 * Actions provided by the Filter menu.
 * </p>
 * 
 * <p>
 * The Filter menu contains actions that update each pixel in an image based on
 * some small local neighbourhood. 
 * This includes a mean filter (a simple blur) in the sample code, but more operations will need to be added.
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class FilterActions {
   
   /** A list of actions for the Filter menu. */
   protected ArrayList<Action> actions;
   protected ArrayList<Action> tools;

   /**
    * <p>
    * Create a set of Filter menu actions.
    * </p>
    */
   public FilterActions() {
      actions = new ArrayList<Action>();
      tools = new ArrayList<Action>();
      actions.add(new MeanFilterAction("Mean filter", null, "Apply a mean filter", Integer.valueOf(KeyEvent.VK_A)));
      actions.add(new MedianFilterAction("Median filter:", null, "Apply a median filter", Integer.valueOf(KeyEvent.VK_S)));
      actions.add(new SoftBlurAction("Soft Blur", null, "Apply a soft blur", Integer.valueOf(KeyEvent.VK_D)));
      actions.add(new SharpenFilterAction("Sharpen", null, "Sharpen the image", Integer.valueOf(KeyEvent.VK_F)));
      actions.add(new GaussianBlurAction("Gaussian Blur", null, "Apply a Gaussian blur", Integer.valueOf(KeyEvent.VK_G)));
      tools.add(new SoftBlurAction("", new ImageIcon("./src/imageIcons/blur.png"), "Apply a soft blur", Integer.valueOf(KeyEvent.VK_M)));
      tools.add(new SharpenFilterAction("", new ImageIcon("./src/imageIcons/sharpen.jpg"), "Sharpen the image", Integer.valueOf(KeyEvent.VK_M)));
   }

   /**
    * <p>
    * Create a menu contianing the list of Filter actions.
    * </p>
    * 
    * @return The filter menu UI element.
    */
   public JMenu createMenu() {
      JMenu fileMenu = new JMenu("Filter");
   
      for(Action action: actions) {
         fileMenu.add(new JMenuItem(action));
      }
   
      return fileMenu;
   }

   /** 
    * <p>
    * Create a tool bar containing the list of filter actions.
    * </p>
    * 
    * @return The filter tool bar element.
    */
   public JToolBar createToolBar(){
      JToolBar filterTool = new JToolBar();
      for(Action tool: tools){
          filterTool.add(new JMenuItem(tool));
      }
      return filterTool;
  }
   /**
    * <p>
    * Action to blur an image with a mean filter.
    * </p>
    * 
    * @see MeanFilter
    */
   public class MeanFilterAction extends ImageAction {
   
      /**
       * <p>
       * Create a new mean-filter action.
       * </p>
       * 
       * @param name The name of the action (ignored if null).
       * @param icon An icon to use to represent the action (ignored if null).
       * @param desc A brief description of the action  (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
       */
      MeanFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
         super(name, icon, desc, mnemonic);
      }
   
      /**
       * <p>
       * Callback for when the convert-to-grey action is triggered.
       * </p>
       * 
       * <p>
       * This method is called whenever the MeanFilterAction is triggered.
       * It prompts the user for a filter radius, then applys an appropriately sized {@link MeanFilter}.
       * </p>
       * 
       * @param e The event triggering this callback.
       */
      public void actionPerformed(ActionEvent e) {
      
         // Determine the radius - ask the user.
         int radius = 1;
      
         // Pop-up dialog box to ask for the radius value.
         SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
         JSpinner radiusSpinner = new JSpinner(radiusModel);
         int option = JOptionPane.showOptionDialog(null, radiusSpinner, "Enter filter radius", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
      
         // Check the return value from the dialog box.
         if (option == JOptionPane.CANCEL_OPTION) {
            return;
         } else if (option == JOptionPane.OK_OPTION) {
            radius = radiusModel.getNumber().intValue();
         }
      
         // Create and apply the filter
         target.getImage().apply(new MeanFilter(radius));
         target.repaint();
         target.getParent().revalidate();
      }
   
   }

   /**
     * Action to apply a median filter to the image
     * @see MedianFilter
     */
    public class MedianFilterAction extends ImageAction {

      /**
       * Creating a new Filter action for the median filter
       * @param name Name of the action
       * @param icon Icon to represent the action
       * @param desc Description of the filter
       * @param mnemonic Mnemonic hotkey to use the filter
       */
      MedianFilterAction(String name, ImageIcon icon, String desc, Integer mnemonic) {
          super(name, icon, desc, mnemonic);
      }

      public void actionPerformed(ActionEvent e) {

         // Determining the radius for the filter
          int radius = 1;

          // Pop up dialog to determine the radius
          SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
          JSpinner radiusSpinner = new JSpinner(radiusModel);
          int option = JOptionPane.showOptionDialog(null, radiusSpinner, "Enter filter radius", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);

          // Checking return value
          if (option == JOptionPane.CANCEL_OPTION) {
              return;
          } else if (option == JOptionPane.OK_OPTION) {
              radius = radiusModel.getNumber().intValue();
          }

          // Applying the filter based on the given radius
          target.getImage().apply(new MedianFilter(radius));
          target.repaint();
          target.getParent().revalidate();
      }
  }

   /**
    * <p>
    * Action to softly blur an image using convolution.
    * </p>
    * 
    * @see SoftBlur
    */
   public class SoftBlurAction extends ImageAction {
   
   
      /**
       * <p>
       * Create a new soft blur filter action.
       * </p>
       * 
       * @param name The name of the action (ignored if null).
       * @param icon An icon to use to represent the action (ignored if null).
       * @param desc A brief description of the action  (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
       */
         
      SoftBlurAction(String name, ImageIcon icon,
                 String desc, Integer mnemonic) {               
         super(name, icon, desc, mnemonic);         
      }
         
      public void actionPerformed(ActionEvent e) { 
      // Create and apply the filter 
         target.getImage().apply(new SoftBlur()); 
         target.repaint(); 
         target.getParent().revalidate();
      } 
      
   }
  

   /**
    * <p>
    * Action to sharpen an image using convolution.
    * </p>
    * 
    * @see SharpenFilter
    */

   public class SharpenFilterAction extends ImageAction {
   
      /**
       * <p>
       * Create a new sharpen-filter action.
       * </p>
       * 
       * @param name The name of the action (ignored if null).
       * @param icon An icon to use to represent the action (ignored if null).
       * @param desc A brief description of the action  (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
       */  
      SharpenFilterAction(String name, ImageIcon icon,
                 String desc, Integer mnemonic) {               
         super(name, icon, desc, mnemonic);         
      }
         
      public void actionPerformed(ActionEvent e) { 
      // Create and apply the filter 
         target.getImage().apply(new SharpenFilter()); 
         target.repaint(); 
         target.getParent().revalidate();
      } 
      
   }
   
   /**
    * <p>
    * Action to blur an image with a Gaussian filter.
    * </p>
    * 
    * @see GaussianBlurFilter
    */


   public class GaussianBlurAction extends ImageAction {
   
   
      /**
       * <p>
       * Create a new Gaussian blur action.
       * </p>
       * 
       * @param name The name of the action (ignored if null).
       * @param icon An icon to use to represent the action (ignored if null).
       * @param desc A brief description of the action  (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
       */      
      GaussianBlurAction(String name, ImageIcon icon,
                 String desc, Integer mnemonic) {               
         super(name, icon, desc, mnemonic);         
      }
      
      
      /**
       * <p>
       * Callback for when the convert-to-grey action is triggered.
       * </p>
       * 
       * <p>
       * This method is called whenever the MeanFilterAction is triggered.
       * It prompts the user for a filter radius, then applys an appropriately sized {@link MeanFilter}.
       * </p>
       * 
       * @param e The event triggering this callback.
       */
         
      public void actionPerformed(ActionEvent e) { 
      
      // Determine the radius - ask the user.
         int radius = 1;
      
         // Pop-up dialog box to ask for the radius value.
         SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
         JSpinner radiusSpinner = new JSpinner(radiusModel);
         int option = JOptionPane.showOptionDialog(null, radiusSpinner, "Enter filter radius", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
      
         // Check the return value from the dialog box.
         if (option == JOptionPane.CANCEL_OPTION) {
            return;
         } else if (option == JOptionPane.OK_OPTION) {
            radius = radiusModel.getNumber().intValue();
         }
      
      
      // Create and apply the filter 
         target.getImage().apply(new GaussianBlurFilter(radius)); 
         target.repaint(); 
         target.getParent().revalidate();
      } 
      
   }
}
