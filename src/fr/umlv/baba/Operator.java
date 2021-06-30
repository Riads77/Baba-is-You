package fr.umlv.baba;

import javax.swing.ImageIcon;

/**
 * Operator enumerate all the words used to create rules, like an operator that connect two names or a name and a property.
 * @see Element
 * @see Word
 */
public enum Operator implements Word {
  IS(BoardDisplay.loadImage("ressources/IS/Text_IS_0.gif"));
  
  private final ImageIcon imageIcon;
  
  /**
   * An Operator is associated at an image for the board display.
   *@param imageIcon
   *
   *@see ImageIcon
   */
  private Operator(ImageIcon imageIcon) {
    this.imageIcon = imageIcon;
  }
  
  /**
   * Get the image of an operator.
   * @return the ImageIcon of Operator.
   * 
   * @see ImageIcon
   *
   */
  public ImageIcon getImageIcon() {
    return imageIcon;
  }
  
  /**
   * Get the element type of Operator.
   * @return the type Type.OPERATOR.
   * 
   * @see Type
   *
   */
  public Type getType() {
    return Type.OPERATOR;
  }
}
