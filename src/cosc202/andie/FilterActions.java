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
 * </p>
 * 
 * <p> 
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills, Callum Teape, Jarod Peacock
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
      actions.add(new EmbossFilterAction("Emboss", null, "Emboss the image", null));
      actions.add(new SobelFilterAction("Sobel Filter", null, "Apply a sobel filter", null));
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
      try{
         // Determine the radius - ask the user.
         int radius = 1;
      
         // Pop-up dialog box to ask for the radius value.
         SpinnerNumberModel radiusModel = new SpinnerNumberModel(1, 1, 10, 1);
         JSpinner radiusSpinner = new JSpinner(radiusModel);
         int option = JOptionPane.showOptionDialog(null, radiusSpinner, "Enter filter radius", JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, null, null);
      
         // Check the return value from the dialog box.
         if (option == JOptionPane.OK_OPTION) {
            radius = radiusModel.getNumber().intValue();;
         } else {
            return;
         }
      
         // Create and apply the filter
         target.getImage().apply(new MeanFilter(radius));
         target.repaint();
         target.getParent().revalidate();
      }catch(NullPointerException E){
         JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before blurring", "alert!", JOptionPane.ERROR_MESSAGE);
      }
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
         try{

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
         }catch(NullPointerException E){
            JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before blurring", "alert!", JOptionPane.ERROR_MESSAGE);
         }
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
         try{
      // Create and apply the filter 
         target.getImage().apply(new SoftBlur()); 
         target.repaint(); 
         target.getParent().revalidate();
      }catch(NullPointerException E){
         JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before blurring", "alert!", JOptionPane.ERROR_MESSAGE);
      }
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
         try{
      // Create and apply the filter 
         target.getImage().apply(new SharpenFilter()); 
         target.repaint(); 
         target.getParent().revalidate();
      }catch(NullPointerException E){
         JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before sharpening", "alert!", JOptionPane.ERROR_MESSAGE);
      }
      } 
      
   }


   public class EmbossFilterAction extends ImageAction {

      EmbossFilterAction(String name, ImageIcon icon,
            String desc, Integer mnemonic) {               
      super(name, icon, desc, mnemonic);         
      }

      public void actionPerformed(ActionEvent e) { 
         String[] options = {
            "Left -> Right",
            "Top-Left -> Bottom-Right",
            "Up -> Down",
            "Top-Right -> Bottom-Left",
            "Right -> Left",
            "Bottom-Right -> Top-Left",
            "Down -> Up",
            "Bottom-Left -> Top-Right"
         };  

         Action[] embossActions = {
            new EmbossLeftRightAction("Emboss (left -> right", null, "Emboss the image", null),
            new EmbossLeftDownDiagonalAction("Emboss (top left -> bottom right)", null, "Emboss the image", null),
            new EmbossUpDownAction("Emboss (up -> down)", null, "Emboss the image", null),
            new EmbossRightDownDiagonalAction("Emboss (top right -> bottom left)", null, "Emboss the image", null),
            new EmbossRightLeftAction("Emboss (right -> left)", null, "Emboss the image", null),
            new EmbossRightUpDiagonalAction("Emboss (bottom right -> top left)", null, "Emboss the image", null),
            new EmbossDownUpAction("Emboss (down -> up)", null, "Emboss the image", null),
            new EmbossLeftUpDiagonalAction("Emboss (bottom left -> top right)", null, "Emboss the image", null)    
         };

         Object selectedDirection = JOptionPane.showInputDialog(null, "Choose a Direction to Emboss in", "Directions", JOptionPane.QUESTION_MESSAGE, null, options, null);
         
         int index = 0;
         for(int i = 0; i < options.length; i ++){
             if(options[i].equals(selectedDirection)) index = i;
         }
         embossActions[index].actionPerformed(e);
      }

   }
   /**
   * <p>
   * Action to emboss an image using convolution.
   * </p>
   * 
   * @see EmbossFilter
   */
   public class EmbossLeftRightAction extends ImageAction {
   
      /**
       * <p>
       * Create a new emboss-filter action.
       * </p>
       * 
       * @param name The name of the action (ignored if null).
       * @param icon An icon to use to represent the action (ignored if null).
       * @param desc A brief description of the action  (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
       */  
      EmbossLeftRightAction(String name, ImageIcon icon,
                 String desc, Integer mnemonic) {               
         super(name, icon, desc, mnemonic);         
      }
         
      public void actionPerformed(ActionEvent e) { 
         try{
      // Create and apply the filter 
         target.getImage().apply(new EmbossLeftRight()); 
         target.repaint(); 
         target.getParent().revalidate();
      }catch(NullPointerException E){
         JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before emboss", "alert!", JOptionPane.ERROR_MESSAGE);
      }
      } 
      
   }

   /**
   * <p>
   * Action to emboss an image using convolution.
   * </p>
   * 
   * @see EmbossFilter
   */
   public class EmbossLeftDownDiagonalAction extends ImageAction {
   
      /**
       * <p>
       * Create a new emboss-filter action.
       * </p>
       * 
       * @param name The name of the action (ignored if null).
       * @param icon An icon to use to represent the action (ignored if null).
       * @param desc A brief description of the action  (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
       */  
      EmbossLeftDownDiagonalAction(String name, ImageIcon icon,
                 String desc, Integer mnemonic) {               
         super(name, icon, desc, mnemonic);         
      }
         
      public void actionPerformed(ActionEvent e) { 
         try{
      // Create and apply the filter 
         target.getImage().apply(new EmbossLeftDownDiagonal()); 
         target.repaint(); 
         target.getParent().revalidate();
      }catch(NullPointerException E){
         JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before embossing", "alert!", JOptionPane.ERROR_MESSAGE);
      }
      } 
      
   }



   /**
   * <p>
   * Action to emboss an image using convolution.
   * </p>
   * 
   * @see EmbossFilter
   */
   public class EmbossUpDownAction extends ImageAction {
   
      /**
       * <p>
       * Create a new emboss-filter action.
       * </p>
       * 
       * @param name The name of the action (ignored if null).
       * @param icon An icon to use to represent the action (ignored if null).
       * @param desc A brief description of the action  (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
       */  
      EmbossUpDownAction(String name, ImageIcon icon,
                 String desc, Integer mnemonic) {               
         super(name, icon, desc, mnemonic);         
      }
         
      public void actionPerformed(ActionEvent e) { 
         try{
      // Create and apply the filter 
         target.getImage().apply(new EmbossUpDown()); 
         target.repaint(); 
         target.getParent().revalidate();
      }catch(NullPointerException E){
         JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before emboss", "alert!", JOptionPane.ERROR_MESSAGE);
      }
      } 
      
   }



   /**
   * <p>
   * Action to emboss an image using convolution.
   * </p>
   * 
   * @see EmbossFilter
   */
   public class EmbossRightDownDiagonalAction extends ImageAction {
   
      /**
       * <p>
       * Create a new emboss-filter action.
       * </p>
       * 
       * @param name The name of the action (ignored if null).
       * @param icon An icon to use to represent the action (ignored if null).
       * @param desc A brief description of the action  (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
       */  
      EmbossRightDownDiagonalAction(String name, ImageIcon icon,
                 String desc, Integer mnemonic) {               
         super(name, icon, desc, mnemonic);         
      }
         
      public void actionPerformed(ActionEvent e) { 
         try{
      // Create and apply the filter 
         target.getImage().apply(new EmbossRightDownDiagonal()); 
         target.repaint(); 
         target.getParent().revalidate();
      }catch(NullPointerException E){
         JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before emboss", "alert!", JOptionPane.ERROR_MESSAGE);
      }
      } 
      
   }


   /**
   * <p>
   * Action to emboss an image using convolution.
   * </p>
   * 
   * @see EmbossFilter
   */
   public class EmbossRightLeftAction extends ImageAction {
   
      /**
       * <p>
       * Create a new emboss-filter action.
       * </p>
       * 
       * @param name The name of the action (ignored if null).
       * @param icon An icon to use to represent the action (ignored if null).
       * @param desc A brief description of the action  (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
       */  
      EmbossRightLeftAction(String name, ImageIcon icon,
                 String desc, Integer mnemonic) {               
         super(name, icon, desc, mnemonic);         
      }
         
      public void actionPerformed(ActionEvent e) { 
         try{
      // Create and apply the filter 
         target.getImage().apply(new EmbossRightLeft()); 
         target.repaint(); 
         target.getParent().revalidate();
      }catch(NullPointerException E){
         JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before emboss", "alert!", JOptionPane.ERROR_MESSAGE);
      }
      } 
      
   }



   /**
   * <p>
   * Action to emboss an image using convolution.
   * </p>
   * 
   * @see EmbossFilter
   */
   public class EmbossRightUpDiagonalAction extends ImageAction {
   
      /**
       * <p>
       * Create a new emboss-filter action.
       * </p>
       * 
       * @param name The name of the action (ignored if null).
       * @param icon An icon to use to represent the action (ignored if null).
       * @param desc A brief description of the action  (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
       */  
      EmbossRightUpDiagonalAction(String name, ImageIcon icon,
                 String desc, Integer mnemonic) {               
         super(name, icon, desc, mnemonic);         
      }
         
      public void actionPerformed(ActionEvent e) { 
         try{
      // Create and apply the filter 
         target.getImage().apply(new EmbossRightUpDiagonal()); 
         target.repaint(); 
         target.getParent().revalidate();
      }catch(NullPointerException E){
         JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before emboss", "alert!", JOptionPane.ERROR_MESSAGE);
      }
      } 
      
   }


   /**
   * <p>
   * Action to emboss an image using convolution.
   * </p>
   * 
   * @see EmbossFilter
   */
   public class EmbossDownUpAction extends ImageAction {
   
      /**
       * <p>
       * Create a new emboss-filter action.
       * </p>
       * 
       * @param name The name of the action (ignored if null).
       * @param icon An icon to use to represent the action (ignored if null).
       * @param desc A brief description of the action  (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
       */  
      EmbossDownUpAction(String name, ImageIcon icon,
                 String desc, Integer mnemonic) {               
         super(name, icon, desc, mnemonic);         
      }
         
      public void actionPerformed(ActionEvent e) { 
         try{
      // Create and apply the filter 
         target.getImage().apply(new EmbossDownUp()); 
         target.repaint(); 
         target.getParent().revalidate();
      }catch(NullPointerException E){
         JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before emboss", "alert!", JOptionPane.ERROR_MESSAGE);
      }
      } 
      
   }


   /**
   * <p>
   * Action to emboss an image using convolution.
   * </p>
   * 
   * @see EmbossFilter
   */
   public class EmbossLeftUpDiagonalAction extends ImageAction {
   
      /**
       * <p>
       * Create a new emboss-filter action.
       * </p>
       * 
       * @param name The name of the action (ignored if null).
       * @param icon An icon to use to represent the action (ignored if null).
       * @param desc A brief description of the action  (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
       */  
      EmbossLeftUpDiagonalAction(String name, ImageIcon icon,
                 String desc, Integer mnemonic) {               
         super(name, icon, desc, mnemonic);         
      }
         
      public void actionPerformed(ActionEvent e) { 
         try{
      // Create and apply the filter 
         target.getImage().apply(new EmbossLeftUpDiagonal()); 
         target.repaint(); 
         target.getParent().revalidate();
      }catch(NullPointerException E){
         JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before emboss", "alert!", JOptionPane.ERROR_MESSAGE);
      }
      } 
      
   }




   public class SobelFilterAction extends ImageAction {

      SobelFilterAction(String name, ImageIcon icon,
            String desc, Integer mnemonic) {               
      super(name, icon, desc, mnemonic);         
      }

      public void actionPerformed(ActionEvent e) { 
         String[] options = {
            "Left -> Right",
            "Up -> Down",
            "Black/White Edge Detector"
         };  

         Action[] sobelActions = {
            new SobelLeftRightAction("Sobel Filter (left -> right)", null, "Apply a sobel filter", null),
            new SobelUpDownAction("Sobel Filter (up -> down)", null, "Apply a sobel filter", null),
            new SobelGreyscaleAction("Sobel Filter (black and white edge detection)", null, "Apply a sobel filter", null)
         };

         Object selectedDirection = JOptionPane.showInputDialog(null, "Choose a Direction to apply the Sobel Filter in", "Directions", JOptionPane.QUESTION_MESSAGE, null, options, null);
         
         int index = 0;
         for(int i = 0; i < options.length; i ++){
             if(options[i].equals(selectedDirection)) index = i;
         }
         sobelActions[index].actionPerformed(e);
      }

   }

   /**
   * <p>
   * Action to Sobel an image using convolution.
   * </p>
   * 
   * @see SobelFilter
   */
    public class SobelLeftRightAction extends ImageAction {
   
      /**
       * <p>
       * Create a new sobel filter action.
       * </p>
       * 
       * @param name The name of the action (ignored if null).
       * @param icon An icon to use to represent the action (ignored if null).
       * @param desc A brief description of the action  (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
       */  
      SobelLeftRightAction(String name, ImageIcon icon,
                 String desc, Integer mnemonic) {               
         super(name, icon, desc, mnemonic);         
      }
         
      public void actionPerformed(ActionEvent e) { 
         try{
      // Create and apply the filter 
         target.getImage().apply(new SobelLeftRight()); 
         target.repaint(); 
         target.getParent().revalidate();
      }catch(NullPointerException E){
         JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before Sobel", "alert!", JOptionPane.ERROR_MESSAGE);
      }
      } 
      
   }



   /**
   * <p>
   * Action to Sobel an image using convolution.
   * </p>
   * 
   * @see SobelFilter
   */
   public class SobelUpDownAction extends ImageAction {
   
      /**
       * <p>
       * Create a new sobel-filter action.
       * </p>
       * 
       * @param name The name of the action (ignored if null).
       * @param icon An icon to use to represent the action (ignored if null).
       * @param desc A brief description of the action  (ignored if null).
       * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
       */  
      SobelUpDownAction(String name, ImageIcon icon,
                 String desc, Integer mnemonic) {               
         super(name, icon, desc, mnemonic);         
      }
         
      public void actionPerformed(ActionEvent e) { 
         try{
      // Create and apply the filter 
         target.getImage().apply(new SobelUpDown()); 
         target.repaint(); 
         target.getParent().revalidate();
      }catch(NullPointerException E){
         JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before Sobel", "alert!", JOptionPane.ERROR_MESSAGE);
      }
      } 
      
   }

    /**
   * <p>
   * Action to Sobel an image using convolution.
   * </p>
   * 
   * @see SobelFilter
   */
  public class SobelGreyscaleAction extends ImageAction {
   
   /**
    * <p>
    * Create a new sobel filter action.
    * </p>
    * 
    * @param name The name of the action (ignored if null).
    * @param icon An icon to use to represent the action (ignored if null).
    * @param desc A brief description of the action  (ignored if null).
    * @param mnemonic A mnemonic key to use as a shortcut  (ignored if null).
    */  
   SobelGreyscaleAction(String name, ImageIcon icon,
              String desc, Integer mnemonic) {               
      super(name, icon, desc, mnemonic);         
   }
      
   public void actionPerformed(ActionEvent e) { 
      try{
   // Create and apply the filter 
      target.getImage().apply(new SobelGreyscale()); 
      target.repaint(); 
      target.getParent().revalidate();
   }catch(NullPointerException E){
      JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before Sobel", "alert!", JOptionPane.ERROR_MESSAGE);
   }
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
      try{
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
      }catch(NullPointerException E){
         JOptionPane.showMessageDialog(null, "Error: there is no image loaded! please load an image before blurring", "alert!", JOptionPane.ERROR_MESSAGE);
      }
      } 
      
   }
}
