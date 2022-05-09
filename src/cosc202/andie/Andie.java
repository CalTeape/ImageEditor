package cosc202.andie;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.imageio.*;

/**
 * <p>
 * Main class for A Non-Destructive Image Editor (ANDIE).
 * </p>
 * 
 * <p>
 * This class is the entry point for the program.
 * It creates a Graphical User Interface (GUI) that provides access to various image editing and processing operations.
 * </p>
 * 
 * <p>
 * <a href="https://creativecommons.org/licenses/by-nc-sa/4.0/">CC BY-NC-SA 4.0</a>
 * </p>
 * 
 * @author Steven Mills
 * @version 1.0
 */
public class Andie {

    public static ImagePanel imagePanel = new ImagePanel();

    /**
     * <p>
     * Launches the main GUI for the ANDIE program.
     * </p>
     * 
     * <p>
     * This method sets up an interface consisting of an active image (an {@code ImagePanel})
     * and various menus which can be used to trigger operations to load, save, edit, etc. 
     * These operations are implemented {@link ImageOperation}s and triggerd via 
     * {@code ImageAction}s grouped by their general purpose into menus.
     * </p>
     * 
     * @see ImagePanel
     * @see ImageAction
     * @see ImageOperation
     * @see FileActions
     * @see EditActions
     * @see ViewActions
     * @see FilterActions
     * @see AdjustmentActions
     * 
     * @throws Exception if something goes wrong.
     */
    private static void createAndShowGUI() throws Exception {
        // Set up the main GUI frame
        JFrame frame = new JFrame("ANDIE");

        Image image = ImageIO.read(new File("./src/icon.png"));
        frame.setIconImage(image);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // The main content area is an ImagePanel
        ImageAction.setTarget(imagePanel);
        JScrollPane scrollPane = new JScrollPane(imagePanel);
        frame.add(scrollPane, BorderLayout.CENTER);

        // Add in menus for various types of action the user may perform.
        JMenuBar menuBar = new JMenuBar();

        // File menus are pretty standard, so things that usually go in File menus go here.
        FileActions fileActions = new FileActions();
        JMenuItem fActions = fileActions.createMenu();
        fActions.setMnemonic(KeyEvent.VK_Q);
        menuBar.add(fActions);

        // Likewise Edit menus are very common, so should be clear what might go here.
        EditActions editActions = new EditActions();
        JMenuItem eActions = editActions.createMenu();
        eActions.setMnemonic(KeyEvent.VK_W);
        menuBar.add(eActions);

        // View actions control how the image is displayed, but do not alter its actual content
        ViewActions viewActions = new ViewActions();
        JMenuItem vActions = viewActions.createMenu();
        vActions.setMnemonic(KeyEvent.VK_E);
        menuBar.add(vActions);

        // Filters apply a per-pixel operation to the image, generally based on a local window
        FilterActions filterActions = new FilterActions();
        JMenuItem filtActions = filterActions.createMenu();
        filtActions.setMnemonic(KeyEvent.VK_R);
        menuBar.add(filtActions);

        // Actions to adjust the representation of colour in the image
        AdjustmentActions adjustmentActions = new AdjustmentActions();
        JMenuItem aActions = adjustmentActions.createMenu();
        aActions.setMnemonic(KeyEvent.VK_T);
        menuBar.add(aActions);

        // Actions that transform the image
        TransformsActions transformsActions = new TransformsActions();
        JMenuItem tActions = transformsActions.createMenu();
        tActions.setMnemonic(KeyEvent.VK_Y);
        menuBar.add(tActions);

        // Actions that change the selected colour
        ColourActions colourActions = new ColourActions();
        JMenuItem cActions = colourActions.createMenu();
        menuBar.add(cActions);

        // Create the tool bar, which consists of sub toolbars for each action class
        JToolBar TB = new JToolBar();
        TB.add(fileActions.createToolBar());
        TB.add(viewActions.createToolBar());
        TB.add(editActions.createToolBar());
        TB.add(filterActions.createToolBar());
        TB.add(adjustmentActions.createToolBar());
        TB.add(transformsActions.createToolBar());
        TB.add(colourActions.createToolBar());

        frame.add(TB, BorderLayout.NORTH);
        
        frame.setJMenuBar(menuBar);
        frame.pack();
        frame.setVisible(true);
    }

    /**
     * <p>
     * Method for retrieving information about the next mouse drag.
     * </p>
     * 
     * <p>
     * Retrieves and returns coordinates of mouse at the start and end of a mouse
     * in the format of an int array (x1, y1, x2, y2).
     * </p>
     * 
     * @return coordinates of mouse press and mouse release.
     */
    public static int[] mouseDrag(){
        int[] dragCoordinates = new int[4];

        // Mouse listener to get mouse coordinates on mouse click.
        imagePanel.addMouseListener(new MouseAdapter(){
            public void mousePressed(MouseEvent e){
                System.out.println("press:\t " + e.getX() + "," + e.getY());
                dragCoordinates[0] = e.getX();
                dragCoordinates[1] = e.getY();
            }
            public void mouseReleased(MouseEvent e){
                System.out.println("release: " + e.getX() + "," + e.getY());
                dragCoordinates[2] = e.getX();
                dragCoordinates[3] = e.getY();
            }
        });

        return dragCoordinates;
    }
    
    /**
     * <p>
     * Main entry point to the ANDIE program.
     * </p>
     * 
     * <p>
     * Creates and launches the main GUI in a separate thread.
     * As a result, this is essentially a wrapper around {@code createAndShowGUI()}.
     * </p>
     * 
     * @param args Command line arguments, not currently used
     * @throws Exception If something goes awry
     * @see #createAndShowGUI()
     */
    public static void main(String[] args) throws Exception {
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                try {
                    createAndShowGUI();
                } catch (Exception ex) {
                    System.exit(1);
                }
            }
        });
    }
}
