package fr.umlv.baba;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;


/**
 * RuleManager is looking the Board to refresh the current rules during the evolution of the game.
 *
 */
public class RuleManager {
  /**
   * Direction is an enum that contains RIGHT and DOWN. It let the possibility to know if a rule is read vertically or horizontally.
   *
   */
  private enum Direction {
    RIGHT, DOWN;
  }
  
  /**
   * searchRules browses a HashMap of elements and observes if each name is potentially followed by an operator and a rule.
   * The rule is add at a new rules HashMap
   * 
   * @param elements
   *              regroup all the elements (of a board) and their coordinates.
   * @param xMax
   *              the number of squares in a range in a board. Useful to verify if a neighbor word is not in a different range.
   * @return a HashMap containing all the rules grouped by item. Indeed, only the items can change rules.
   * 
   *@see HashMap
   *@see Item
   *@see Set
   *@see Rule
   *@see Element
   *@see Integer
   *
   */
  public static HashMap<Item, Set<Rule>> searchRules(HashMap<Element, Set<Integer>> elements, int xMax) {
    Objects.requireNonNull(elements);
    if(xMax < 0) {
      throw new IllegalArgumentException("xMax must be positive");
    }
    var rules = new HashMap<Item, Set<Rule>>();
    elements.forEach((element, coordList) -> {
      if(element.getType() == Type.NAME) {
        var name = (Name) element;
        rules.put(name.getItem(), searchOperator(elements, coordList, xMax));
      }
    });
    return rules;
  }
  
  /**
   * searchOperator browses a HashMap of elements and observes if a specific name is followed by an operator at its right or at its down.
   * If yes, look at the coordinates of the operator a following property.
   * 
   * @param elements
   *              regroup all the elements (of a board) and their coordinates.
   * @param coordinates
   *              A set with the coordinates where the Name is.
   * @param xMax
   *              the number of squares in a range in a board. Useful to verify if a neighbor word is not in a different range.
   * @return a set of all the rules founded for an element.
   * 
   * @see Integer
   * @see HashMap
   * @see Set
   * @see Rule
   * @see Element
   *
   */
  private static Set<Rule> searchOperator(HashMap<Element, Set<Integer>> elements, Set<Integer> coordinates, int xMax) {
    Objects.requireNonNull(elements);
    Objects.requireNonNull(coordinates);
    if(xMax < 0) {
      throw new IllegalArgumentException("xMax must be positive");
    }
    var rules = new HashSet<Rule>();
    elements.forEach((element, coordList) -> {
      if(element.getType() == Type.OPERATOR) {
        for(var coord: coordinates) {
          if(coord / xMax == (coord + 1) / xMax) {
            if(coordList.contains(coord + 1)) {
              var operator = (Operator) element;
              rules.addAll(searchProperty(elements, operator, coord + 1, xMax, Direction.RIGHT));
            }
          }
          if(coordList.contains(coord + xMax)) {
            var operator = (Operator) element;
            rules.addAll(searchProperty(elements, operator, coord + xMax, xMax, Direction.DOWN));
          }
        }
      }
    });
    return rules;
  }
  
  /**
   * searchProperty browses a HashMap of elements and observes if a specific operator is followed by a property at its right or at its down.
   * If yes, return a set of the founded rules.
   * 
   * @param elements
   *              Regroup all the elements (of a board) and their coordinates.
   * @param operator
   *              The operator concerned by the rule.
   * @param coord
   *              The coordinate of the operator.
   * @param xMax
   *              The number of squares in a range in a board. Useful to verify if a neighbor word is not in a different range.
   * @param direction
   *               The direction of the reading of the rule. RIGHT if the rule is horizontal and DOWN if it's vertical.
   * @return a set of all the rules founded with this operator.
   * 
   * @see Integer
   * @see HashMap
   * @see Set
   * @see Rule
   * @see Element
   * @see Operator
   * @see Direction
   *
   */
  private static Set<Rule> searchProperty(HashMap<Element, Set<Integer>> elements, Operator operator, int coord, int xMax, Direction direction) {
    Objects.requireNonNull(elements);
    Objects.requireNonNull(operator);
    Objects.requireNonNull(direction);
    if(xMax < 0) {
      throw new IllegalArgumentException("xMax must be positive");
    }
    if(coord < 0) {
      throw new IllegalArgumentException("coordinates must be positive");
    }
    var rules = new HashSet<Rule>();
    elements.forEach((element, coordList) -> {
      if(element.getType() == Type.PROPERTY || element.getType() == Type.NAME) {
        int dir;
        if(direction == Direction.RIGHT) {
          dir = 1;
        }
        else if(direction == Direction.DOWN) {
          dir = xMax;
        }
        else {
          throw new IllegalArgumentException("Unknown direction");
        }
        if(coordList.contains(coord + dir)) {
          if((dir == 1 && coord / xMax == (coord + dir) / xMax) || dir == xMax) {
            if(operator == Operator.IS) {
              rules.add((Rule) element);
            }
          }
        }
      }
    });
    return rules;
  }
}
