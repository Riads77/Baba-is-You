package fr.umlv.baba;

import javax.swing.ImageIcon;


/**
 * Item enumerate all the Element playable in the game, a non-text element.
 * @see Element
 */
public enum Item implements Element {
  BABA(BoardDisplay.loadImage("ressources/BABA/BABA_0.gif")), 
  WALL(BoardDisplay.loadImage("ressources/WALL/WALL_0.gif")), 
  ROCK(BoardDisplay.loadImage("ressources/ROCK/ROCK_0.gif")), 
  FLAG(BoardDisplay.loadImage("ressources/FLAG/FLAG_0.gif")),
  TILE(BoardDisplay.loadImage("ressources/TILE/TILE_0.gif"));
  
  private final ImageIcon imageIcon;
  
  /**
   * An Item is associated at an image for the board display.
   *@param imageIcon
   *
   *@see ImageIcon
   */
  private Item(ImageIcon imageIcon) {
    this.imageIcon = imageIcon;
  }
  
  /**
   * Get the image of an item.
   * @return the ImageIcon of Item.
   * 
   * @see ImageIcon
   *
   */
  public ImageIcon getImageIcon() {
    return imageIcon;
  }
  
  /**
   * Get the element type of Item.
   * @return the type Type.ITEM.
   * 
   * @see Type
   *
   */
  public Type getType() {
    return Type.ITEM;
  }
}
