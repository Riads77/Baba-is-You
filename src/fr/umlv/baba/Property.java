package fr.umlv.baba;

import javax.swing.ImageIcon;

/**
 * Property enumerate all the word in game recognized as a property. They are used as rule to create rules.
 * They defined the characteristics of an element. 
 * @see Element
 * @see Word
 * @see Rule
 */
public enum Property implements Rule {
  YOU(BoardDisplay.loadImage("ressources/YOU/Text_YOU_0.gif")),
  PUSH(BoardDisplay.loadImage("ressources/PUSH/Text_PUSH_0.gif")),
  STOP(BoardDisplay.loadImage("ressources/STOP/Text_STOP_0.gif")),
  WIN(BoardDisplay.loadImage("ressources/WIN/Text_WIN_0.gif"));
  
  private final ImageIcon imageIcon;
  
  /**
   * A Property is associated at an image for the board display.
   *@param imageIcon
   *
   *@see ImageIcon
   */
  private Property(ImageIcon imageIcon) {
    this.imageIcon = imageIcon;
  }
  
  /**
   * Get the image of a Property.
   * @return the ImageIcon of Property.
   * 
   * @see ImageIcon
   *
   */
  public ImageIcon getImageIcon() {
    return imageIcon;
  }
  
  /**
   * Get the element type of Property.
   * @return the type Type.PROPERTY.
   * 
   * @see Type
   *
   */
  public Type getType() {
    return Type.PROPERTY;
  }
}
