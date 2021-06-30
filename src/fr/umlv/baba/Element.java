package fr.umlv.baba;

import javax.swing.ImageIcon;


/**
 * This interface group the elements contained in a board. They are 2 types of elements : Item and Word.
 * @see Item
 * @see Word
 *
 */
public interface Element {
  /**
   * Get the image of an element. All the enums that implement Element have an image.
   * @return the ImageIcon associated of the Element.
   * 
   * @see ImageIcon
   *
   */
  ImageIcon getImageIcon();
  
  /**
  * Get the type of the Element.
  * @return One of the four types that exist: ITEM, NAME, OPERATOR and PROPERTY.
  */
  Type getType();
}
