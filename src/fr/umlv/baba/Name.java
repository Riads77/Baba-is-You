package fr.umlv.baba;

import javax.swing.ImageIcon;

/**
 * Name enumerate all the words in game that are an item name. They are used to create rules, like a Name at the left
 * of an operator, or like a Rule at the right.
 * @see Element
 * @see Word
 * @see Rule
 */
public enum Name implements Rule {
  TBABA(BoardDisplay.loadImage("ressources/BABA/Text_BABA_0.gif"), Item.BABA), 
  TWALL(BoardDisplay.loadImage("ressources/WALL/Text_WALL_0.gif"), Item.WALL), 
  TROCK(BoardDisplay.loadImage("ressources/ROCK/Text_ROCK_0.gif"), Item.ROCK), 
  TFLAG(BoardDisplay.loadImage("ressources/FLAG/Text_FLAG_0.gif"), Item.FLAG);
  
  private final ImageIcon imageIcon;
  private final Item item;
  
  /**
   * A Name is associated at an image for the board display. 
   * It has also a link to the type it represent. This type is concerned by the rules the Name had create. 
   *@param imageIcon
   *@param type
   *
   *@see ImageIcon
   *@see Item
   */
  private Name(ImageIcon imageIcon, Item type) {
    this.imageIcon = imageIcon;
    this.item = type;
  }
  
  /**
   * Get the image of a Name.
   * @return the ImageIcon of Item.
   * 
   * @see ImageIcon
   *
   */
  public ImageIcon getImageIcon() {
    return imageIcon;
  }
  
  public Item getItem() {
    return item;
  }
  
  /**
   * Get the element type of Name.
   * @return the type Type.NAME.
   * 
   * @see Type
   *
   */
  public Type getType() {
    return Type.NAME;
  }
}
