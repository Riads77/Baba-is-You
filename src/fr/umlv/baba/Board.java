package fr.umlv.baba;

import java.awt.Graphics2D;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * Board is the representation of all the data of the game Baba Is You.
 *
 */
public class Board {
  private final int xMax;
  private final int yMax;  
  private final HashMap<Element, Set<Integer>> elements = new HashMap<>();;
  private final HashMap<Element, Set<Rule>> rules = new HashMap<>();
  
  /**
   * Board is a hollow representation of the game, but it still needs dimensions.
   * 
   * @param xMax
   *          Number of cases at length.
   * @param yMax
   *          Number of cases at height.
   *          
   *
   */
  public Board(int xMax, int yMax) {
    if(xMax < 0 || yMax < 0) {
      throw new IllegalArgumentException("The number of cases can't be negative");
    }
    Objects.requireNonNull(elements);
    this.xMax = xMax;
    this.yMax = yMax;
  }
  
  /**
   * A getter to know xMax in Board.
   * 
   * @return xMax
   *              
   */
  public int getXMax() {
    return xMax;
  }
  
  /**
   * A getter to know yMax in Board.
   * 
   * @return yMax
   *              
   */
  public int getYMax() {
    return yMax;
  }
  
  /**
   * Display the board with zen5.
   * 
   * @param graphics2D
   *                the graphics animation of zen5.
   *              
   */
  public void displayBoard(Graphics2D graphics2D) {
    Objects.requireNonNull(graphics2D);
    elements.forEach((element, coordList) -> {
      for(var coordinates: coordList) {
        BoardDisplay.displayElement(element, graphics2D, coordinates % xMax, coordinates / xMax);
      }
    });
  }
  
  /**
   * add an element in a coord to the HashMap elements.
   * 
   * @param element
   *              The element to add.
   * @param coord
   *            The coord where is the element
   * 
   * @see Element
   *              
   */
  public void add(Element element, int coord) {
    Objects.requireNonNull(element);
    if(coord < 0 || coord >= xMax * yMax) {
      throw new IllegalArgumentException("The coordinates are out of the board");
    }
    var list = elements.get(element);
    if(list == null) {
      list = new HashSet<>();  
    }
    list.add(Integer.valueOf(coord));
    elements.put(element, list);
  }
  
  /**
   * add an element in all the coords to the elements HashMap.
   * 
   * @param element
   *              The element to add.
   * @param listCoord
   *            The list of the coords where are the elements
   * 
   * @see Element
   * @see Set
   * @see Integer
   *              
   */
  public void addAll(Element element, Set<Integer> listCoord) {
    Objects.requireNonNull(element);
    Objects.requireNonNull(listCoord);
    var list = elements.get(element);
    if(list == null) {
      list = new HashSet<>();
    }
    list.addAll(listCoord);
    elements.put(element, list);
  }
  
  /**
   * translate a String direction to its integer value.
   * 
   * @param direction
   *              A direction ("LEFT", "RIGHT", "UP", "DOWN").
   * @return the integer to add to a coordinate to move it?
   * 
   * @see String
   *              
   */
  public int switchDirection(String direction) {
    Objects.requireNonNull(direction);
    switch (direction) {
      case "UP":
        return -xMax;
      case "DOWN":
        return xMax;
      case "LEFT":
        return -1;
      case "RIGHT":
        return 1; 
      default:
        return 0;
    }
  }
  
  /**
   * Check if an element in some coordinate is still in board after he moved in a direction.
   * @param coord
   *              The original coordinates before the move.
   * @param direction
   *              A direction ("LEFT", "RIGHT", "UP", "DOWN").
   * @return true if the move is in board, false else.
   * 
   *              
   */
  private boolean moveInBoard(int coord, int direction) {
    if(coord < 0 || coord >= xMax * yMax) {
      throw new IllegalArgumentException("Coordinates out of the board");
    }
    if(Math.abs(direction) != 1 && Math.abs(direction) != xMax) {
      throw new IllegalArgumentException("Invalid direction");
    }
    if(Math.abs(direction) == 1) {
      if(coord / xMax != (coord + direction) / xMax || coord + direction < 0) {
        return false;
      }
    }
    else if((direction == -xMax && coord + direction < 0) || (direction == xMax && coord + direction >= xMax * yMax)) {
        return false;
    }
    return true;
  }
  
  /**
   * Move an element in the board (with PUSH Rule).
   * @param coord
   *              The original coordinates before the move.
   * @param direction
   *              A direction ("LEFT", "RIGHT", "UP", "DOWN").
   * @param element
   *              The element to move.
   * @return true only if the move is done.
   * 
   * @see Element             
   */
  private boolean moveOneElement(int coord, int direction, Element element) {
    if(coord < 0 || coord >= xMax * yMax) {
      throw new IllegalArgumentException("Coordinates out of the board");
    }
    if(Math.abs(direction) != 1 && Math.abs(direction) != xMax) {
      throw new IllegalArgumentException("Invalid direction");
    }
    var coordList = elements.get(element);
    if(!coordList.contains(coord)) {
      throw new IllegalArgumentException("No specified item in this coordinates");
    }
    if(this.moveInBoard(coord, direction)) {
      coordList.remove(Integer.valueOf(coord));
      coordList.add(coord + direction);
      elements.put(element, coordList);
      return true;
    }
    return false;
  }
  
  /**
   * Check if an element with the STOP property is at the case when an element try to move.
   * @param coord
   *              The original coordinates before the move.
   * @param direction
   *              A direction ("LEFT", "RIGHT", "UP", "DOWN").
   * @return true only if they are an element with the STOP property in coord + direction.             
   */
  private boolean moveStop(int coord, int direction) {
    if(coord < 0 || coord >= xMax * yMax) {
      throw new IllegalArgumentException("Coordinates out of the board");
    }
    if(Math.abs(direction) != 1 && Math.abs(direction) != xMax) {
      throw new IllegalArgumentException("Invalid direction");
    }
    for(var entry: elements.entrySet()) {
      if(this.isRule(entry.getKey(), Property.STOP)) {
        if(entry.getValue().contains(coord + direction)) {
          return true;
        }
      }
    }
    return false;
  }
  
  /**
   * Check if an element with the PUSH property is at the case when an element try to move. try to push it recursively, unless he has the YOU property.
   * @param coord
   *              The original coordinates before the move.
   * @param direction
   *              A direction ("LEFT", "RIGHT", "UP", "DOWN").
   * @return true only if they are an element with the PUSH property but not YOU property in coord + direction.             
   */
  private boolean movePush(int coord, int direction) {
    if(coord < 0 || coord >= xMax * yMax) {
      throw new IllegalArgumentException("Coordinates out of the board");
    }
    if(Math.abs(direction) != 1 && Math.abs(direction) != xMax) {
      throw new IllegalArgumentException("Invalid direction");
    }
    if(Math.abs(direction) == 1 && coord / xMax != (coord + direction) / xMax) {
      return true;
    }
    for(var entry: elements.entrySet()) {
      var isYou = false;
      var element = entry.getKey();
      if(this.isRule(element, Property.YOU)) {
        if(entry.getValue().contains(coord + direction)) {
          isYou = !moveNeighbor(coord + direction, direction);
        }
      }
      if(this.isRule(element, Property.PUSH)) {
        if(entry.getValue().contains(coord + direction)) {
          if(!this.moveNeighbor(coord + direction, direction)) {
            if(!isYou) {
              return !this.moveOneElement(coord + direction, direction, entry.getKey());
            }
            if(this.moveInBoard(coord + direction, direction)) {
              return false;
            }
            return true;
          }
          else {
            return true;
          }
        }
      }
    }
    return false;
  }

  /**
   * Check if a move is possible by looking at the arrival square.
   * @param coord
   *              The original coordinates before the move.
   * @param direction
   *              A direction ("LEFT", "RIGHT", "UP", "DOWN").
   * @return true only if the move cannot be done.             
   */
  private boolean moveNeighbor(int coord, int direction) {
    if(coord < 0 || coord >= xMax * yMax) {
      throw new IllegalArgumentException("Coordinates out of the board");
    }
    if(Math.abs(direction) != 1 && Math.abs(direction) != xMax) {
      throw new IllegalArgumentException("Invalid direction");
    }
    if(moveStop(coord, direction)) {
      return true;
    }
    return movePush(coord, direction);
  }
  
  /**
   * Check if a move is possible.
   * @param coord
   *              The original coordinates before the move.
   * @param direction
   *              A direction ("LEFT", "RIGHT", "UP", "DOWN").
   * @return true only if the move is possible.             
   */
  private boolean validateMove(int coord, int direction) {
    if(coord < 0 || coord >= xMax * yMax) {
      throw new IllegalArgumentException("Coordinates out of the board");
    }
    if(Math.abs(direction) != 1 && Math.abs(direction) != xMax) {
      throw new IllegalArgumentException("Invalid direction");
    }
    return this.moveInBoard(coord, direction) && !this.moveNeighbor(coord, direction);
  }
  
  /**
   * Tries to move an element in a fixed direction. 
   * @param direction
   *              A direction ("LEFT", "RIGHT", "UP", "DOWN").
   * @param element
   *              The element to move.
   * @param coordList
   *      The element coordinates.   
   * @see Element
   * @see Set
   * @see Integer          
   */
  private void moveElement(int direction, Element element, Set<Integer> coordList) {
    if(Math.abs(direction) != 1 && Math.abs(direction) != xMax) {
      throw new IllegalArgumentException("Invalid direction");
    }
    Objects.requireNonNull(element);
    Objects.requireNonNull(coordList);
    var newCoordList = new HashSet<Integer>(coordList.stream()
        .filter(coord -> validateMove(coord, direction))
        .map(coord -> coord + direction)
        .collect(Collectors.toSet())
    );
    var stayCoordList = new HashSet<Integer>(coordList.stream()
        .filter(coord -> !validateMove(coord, direction))
        .collect(Collectors.toSet())
    );
    newCoordList.addAll(stayCoordList);
    var finalCoordList = new HashSet<Integer>(newCoordList);
    elements.put(element, finalCoordList);   
  }
  
  /**
   * Try to move everything in the board that can do it at a specific direction. 
   * Currently, only all the elements with the YOU property are allowed to move.
   * @param direction
   *              A direction ("LEFT", "RIGHT", "UP", "DOWN").            
   */
  public void move(int direction) {
    if(Math.abs(direction) != 1 && Math.abs(direction) != xMax) {
      throw new IllegalArgumentException("Invalid direction");
    }
    elements.forEach((element, coordList) -> {
      if(this.isRule(element, Property.YOU)) {
        this.moveElement(direction, element, coordList);
      }
    });
  }
  
  /**
   * Check if a YOU element is on a WIN element. The game is won then.
   * @return true if an element with YOU property contains a coordinate that an element with WIN property has..          
   */
  public boolean isWin() {
    var winCoord = new HashSet<>();
    for(var entry: elements.entrySet()) {
      if(this.isRule(entry.getKey(), Property.WIN)) {
        winCoord.addAll(entry.getValue());
      }
    }
    for(var entry: elements.entrySet()) {
      if(this.isRule(entry.getKey(), Property.YOU)) {
        for(var coord: entry.getValue()) {
          if(winCoord.contains(coord)) {
            return true;
          }
        }
      }
    }
    return false;
  }
  
  /**
   * Check if no rule are defined with the YOU property. The game is lost then.
   * @return true if no element in the rules has the YOU property.          
   */
  public boolean isLoose() {
    for(var entry: elements.entrySet()) {
      if(this.isRule(entry.getKey(), Property.YOU)) {
        if(!entry.getValue().isEmpty()) {
          return false;
        }
      }
    }
    return true;
  }
  
  /**
   * Check an element contains a specific rule.
   * @param element
   * @param rule
   * 
   * @return true if this rule exist.
   * 
   * @see Element
   * @see Rule
   */
  private boolean isRule(Element element, Rule rule) {
    if(!rules.containsKey(element)) {
      return false;
    }
    return rules.get(element).contains(rule);
  }
  
  /**
   * Add a specific rule to an element.
   * @param element
   * @param rule
   * 
   * @see Element
   * @see Rule
   */
  private void addRule(Element element, Rule rule) {
    Objects.requireNonNull(element);
    Objects.requireNonNull(rule);
    var list = rules.get(element);
    if(list == null) {
      list = new HashSet<>();
    }
    list.add(rule);
    rules.put(element, list);
  }
  
  /**
   * Add a list of rules to an element.
   * @param element
   * @param listRules
   * 
   * @see Element
   * @see Set
   * @see Rule
   */
  private void addAllRules(Element element, Set<Rule> listRules) {
    Objects.requireNonNull(element);
    Objects.requireNonNull(listRules);
    var list = rules.get(element);
    if(list == null) {
      list = new HashSet<>();
    }
    list.addAll(listRules);
    rules.put(element, list);
  }
  
  /**
   * Init the list of rules of the board. The non-item elements (words) get the PUSH property.
   */
  public void initRules() {
    for(var element: elements.keySet()) {
      if(element.getType() != Type.ITEM) {
        this.addRule(element, Property.PUSH);
      }
    }
  }
  
  /**
   * Check in the board all the current rules and manage then with the RuleManager class.
   * 
   * @return a HashMap containing all the found rules in the board.
   * 
   * @see HashMap
   * @see Item
   * @see Rule
   * @see RuleManager
   */
  private HashMap<Item, Set<Rule>> searchRules() {
    return RuleManager.searchRules(elements, xMax);
  }
  
  /**
   * Transforms a type of item in another (caused by a rule of type : Name Property Name).
   * 
   * @param item
   *            The item to transform.
   * @param name
   *            The name of the result item.
   * 
   * @see Item
   */
  private void transformItem(Item item, Item name) {
    Objects.requireNonNull(item);
    Objects.requireNonNull(name);
    var listCoord = new HashSet<Integer>(elements.get(item));
    elements.put(item, new HashSet<Integer>());
    var newListCoord = elements.get(name);
    newListCoord.addAll(listCoord);
    this.addAll(name, new HashSet<Integer>(newListCoord));
  }
  
  /**
   * To prevent a bad reading of many rules of type "Name Property Name", transforms the rules connected by transitivity.
   * Example : if ROCK IS FLAG and FLAG IS BABA, so we had to replace the first one with ROCK IS BABA.
   * 
   * @param nameRules
   *            All the "Name Property Name" rules.
   * @return the obtained HashMap.
   * @see HashMap
   * @see Item
   */
  private HashMap<Item, Item> transitiveRules(HashMap<Item, Item> nameRules) {
    nameRules.forEach((item, name) -> {
      if(nameRules.containsKey(name)) {
        var newName = nameRules.get(name);
        nameRules.put(item, newName);
        nameRules.forEach((sItem, sName) -> {
          if(sName == item) {
            nameRules.put(sItem, newName);
          }
        });
      }
    });
    return nameRules;
  }
  
  /**
   * Takes into account the "Name Property Name" rules of the board and transform all the items at their right name rule.
   */
  private void transformItems() {
    var nameRules = new HashMap<Item, Item>();
    rules.forEach((element, ruleList) -> {
      for(var rule: ruleList) {
        if(rule.getType() == Type.NAME && element.getType() == Type.ITEM) {
          var item = (Item) element;
          var name = (Name) rule;
          nameRules.put(item, name.getItem());
        }
      }
    });
    var finalNameRules = this.transitiveRules(nameRules);
    finalNameRules.forEach((finalItem, finalName) -> {
      transformItem(finalItem, finalName);
    });
  }
  
  /**
   * Refresh the rules in the board in two step. searching the current rules in the board and transform items concerned by "Name Property Name" rules.
   */
  public void refreshRules() {
    for(var element: rules.keySet()) {
      if(element.getType() == Type.ITEM) {
        rules.put(element, new HashSet<>());
      }
    }
    var newRules = this.searchRules();
    for(var entry: newRules.entrySet()) {
      this.addAllRules(entry.getKey(), entry.getValue());
    }
    this.transformItems();
  }
  
  /**
   * Called with a start board. Check if some elements are stacked at a same coordinates.
   * @return true if at least two elements contains the same coordinates.
   */
  private boolean unstackedElements() {
    for(var i = 0; i < xMax * yMax; i++) {
      var numberElements = 0;
      for(var coordList: elements.values()) {
        if(coordList.contains(Integer.valueOf(i))) {
          numberElements++;
        }
      }
      if(numberElements > 1) {
        System.out.println("Error : They are stacked elements in your level");
        return false;
      }
    }
    return true;
  }
  
  /**
   * Called with a start board. Check if the game is winnable by checking the presence of the WIN property.
   * @return true if the elements are containing the WIN property.
   */
  private boolean possibleWin() {
    var flag =  elements.containsKey(Property.WIN);
    if(!flag) {
      System.out.println("Error : They are not win property in your level");
    }
    return flag;
  }
  
  /**
   * Called with a start board. Check if a board is valid and respect all the conventions of "Baba Is You".
   * @return true only if the board is valid.
   */
  public boolean isValid() {
    return this.unstackedElements() && this.possibleWin() && !this.isLoose();
  }
}
