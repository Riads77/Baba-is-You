package fr.umlv.baba;

import java.awt.Graphics2D;
import java.awt.Image;
import java.util.Objects;

import javax.swing.ImageIcon;

/**
 * BoardDisplay contains the functions of element displaying and of image loading.
 *
 */
public class BoardDisplay {
  private static float width;
  private static float height;
  private static int xMax;
  private static int yMax; 
  
  /**
   * BoardDisplay knows the window dimensions and the number of squares in the board.
   * @param width
   *             The width of the window where the game is displayed.
   * @param height
   *              The height of the window where the game is displayed.
   * @param xMax
   *            The number of squares in a range.
   * @param yMax
   *            The number of squares in a column.
   *
   */
  public BoardDisplay(float width, float height, int xMax, int yMax) {
    if(xMax < 0 || yMax < 0) {
      throw new IllegalArgumentException("The number of cases can't be negative");
    }
    if(width < 0 || height < 0) {
      throw new IllegalArgumentException("The dimensions can't be negative");
    }
    BoardDisplay.width = width;
    BoardDisplay.height = height;
    BoardDisplay.xMax = xMax;
    BoardDisplay.yMax = yMax;
  }
  
  /**
   * BoardDisplay knows the window dimensions and the number of squares in the board.
   * @return a String containing the width and the height of the window.
   * 
   * @see String
   *
   */
  @Override
  public String toString() {
    return "size of the screen (" + width + " x " + height + ")";
  }
  
  /**
   * Calculates dynamically the adapted size of the side of square in the window.
   * @return the size of the side of a square.
   *
   */
  public static int size() {
    return (int) Math.min(BoardDisplay.width / BoardDisplay.xMax, BoardDisplay.height / BoardDisplay.yMax);
  }
  
  /**
   * Displays an element at specific coordinates in the graphic interface.
   * @param element 
   *              The element to display.
   * @param graphics2D 
   *              The graphics interface where the element is displayed.
   * @param x 
   *              The abscissa of the element.
   * @param y
   *              The ordinate of the element.        
   *
   *@see Element
   *@see Graphics2D
   */
  public static void displayElement(Element element, Graphics2D graphics2D, int x, int y) {
    Objects.requireNonNull(element);
    Objects.requireNonNull(graphics2D);
    if(x < 0 || y < 0) {
      throw new IllegalArgumentException("The coordinates (x, y) are out of the board");
    }
    var size = size();
    element.getImageIcon().paintIcon(null, graphics2D, x * size + (int) (BoardDisplay.width - BoardDisplay.xMax * size) / 2, y * size + (int) (BoardDisplay.height - BoardDisplay.yMax * size) / 2); 
  }
  
  /**
   * Load an ImageIcon.
   * @param file 
   *              The path of the image.
   * @return the loaded image.
   * @see String
   * @see ImageIcon        
   *
   */
  public static ImageIcon loadImage(String file) {
    var imageIcon = new ImageIcon(file);
    var image = imageIcon.getImage().getScaledInstance(size(), size(), Image.SCALE_DEFAULT);
    var newImageIcon = new ImageIcon(image);
    return newImageIcon; 
  }
}



