package fr.umlv.baba;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * SaverLoader contains all functions to save and load a game level.
 * 
 * 
 *
 */

public class SaverLoader {
  static final String SEPARATOR = "#";
  static final String NAME_TYPE = "N";
  static final String OPERATOR_TYPE = "O";
  static final String PROPERTY_TYPE = "P";
  static final String ITEM_TYPE = "I";
  
  /**
   * toTextFormat make a String representation of an element and his coordinates to save it in a file with saveInTextFormat.
   * 
   * @param element
   *              An element to save.
   * @param coordList
   *                The list of the coordinates where the element are.
   * @return That String representation
   *              
   * @see Element
   * @see ArrayList
   * @see Integer
   */
  
  private static String toTextFormat(Element element, ArrayList<Integer> coordList) {
    return element.getType() + SEPARATOR + element.toString() + SEPARATOR + coordList.stream()
    .map(coordinates -> coordinates.toString())
    .collect(Collectors.joining(SEPARATOR));
  }
  
  private static String DimensionsFormat(int xMax, int yMax) {
    return xMax + SEPARATOR + yMax;
  }
  
  /**
   * saveInTextFormat save Board datas in a file.
   * 
   * @param elements
   *              A board elements and there coordinates.
   * @param xMax
   *                The number of cases at length in the board display.
   * @param yMax
   *                The number of cases at height in the board display
   * @param writer
   *                The writer used to save the game.
   * 
   * @throws IOException
   *                Only if a problem of IO is coming.
   * 
   * @see Board
   * @see Element
   * @see HashMap
   * @see BufferedWriter
   * @see ArrayList
   * @see Integer
   */
  public static void saveInTextFormat(HashMap<Element, ArrayList<Integer>> elements, int xMax, int yMax, BufferedWriter writer) throws IOException {
    Objects.requireNonNull(elements);
    Objects.requireNonNull(writer);
    try {
      writer.write(DimensionsFormat(xMax,  yMax));
      writer.newLine();
    } catch (IOException e) {
      System.err.println(e.getMessage());
      System.exit(1);
    }
    elements.forEach((element, coordList) -> {
      try {
        writer.write(toTextFormat(element, coordList));
        writer.newLine();
      } catch (IOException e) {
        System.err.println(e.getMessage());
        System.exit(1);
      }
    });
  }
  
  /**
   * Parse a line with dimensions and create a board with them.
   * 
   * @param line
   *              The line to parse.
   *              
   * @return a board with the parsed dimensions
   * 
   * @see Board
   * @see Integer
   * @see String
   */
  private static Board parseFirstLine(String line) {
    Objects.requireNonNull(line);
    var tokens = line.split(SEPARATOR);
    if(tokens.length != 2) {
      throw new IllegalStateException("The dimensions of the level in the file are not correct (first line)");
    }
    var xMax = Integer.valueOf(tokens[0]);
    var yMax = Integer.valueOf(tokens[1]);
    if(xMax <= 0 || yMax <= 0) {
      throw new IllegalArgumentException("xMax and yMax in the level file must be positive");
    }
    var board = new Board(xMax, yMax);
    return board;
  }
  
  /**
   * Parse a line with elements data and add it in a board.
   * 
   * @param board 
   *              the board to refresh
   * @param line
   *              The line to parse.
   * @param elementParseMap
   *                      A function that creates an element with the type requested.
   *              
   * @return a board with the element parsed
   * 
   * @see Board
   * @see Integer
   * @see Map
   * @see String
   * @see Function
   * @see Element
   */
  private static Board parseLine(Board board, String line, Map<String, Function<String[], Element>> elementParseMap) {
    Objects.requireNonNull(board);
    Objects.requireNonNull(line);
    Objects.requireNonNull(elementParseMap);
    var tokens = line.split(SEPARATOR);
    if(tokens.length < 2) {
      throw new IllegalStateException("Error in the level file (miss argument)");
    }
    var type = tokens[0];
    var function = elementParseMap.get(type);
    var element = function.apply(tokens);
    for(var i = 2; i < tokens.length; i++) {
      var coord = Integer.valueOf(tokens[i]);
      if(coord < 0) {
        throw new IllegalArgumentException("Negatives coordinates are ot allowed in the level file");
      }
      board.add(element, coord);
    }
    return board;
  }
  
  /**
   * Load a Board from a level file.
   * 
   * @param reader 
   *              that reader take the file line by line
   * @param elementParseMap
   *                      A function that creates an element with the type requested.
   *              
   * @return a board.
   * 
   * @throws IOException
   * 
   * @see Board
   * @see BufferedReader
   * @see Map
   * @see String
   * @see Function
   * @see Element
   * 
   */
  private static Board loadFromTextFormat(BufferedReader reader, Map<String, Function<String[], Element>> elementParseMap) throws IOException {
    Objects.requireNonNull(reader);
    Objects.requireNonNull(elementParseMap);
    var line = reader.readLine();
    Board board;
    if(line != null) {
      board = parseFirstLine(line);
    }
    else {
      return null;
    }
    for(line = reader.readLine(); line != null; line = reader.readLine()) {
      board = parseLine(board, line, elementParseMap);;
    }
    return board;
  }
  
  
  /**
   * Create a board from a level file.
   * 
   * @param path 
   *              The path of the level file.
   *              
   * @return a board.
   * 
   * 
   * @see Board
   * @see Path
   * 
   */
  public static Board configureLevel(Path path){
    Map<String, Function<String[], Element>> elementParserMap = Map.of(
        NAME_TYPE, parts -> Name.valueOf(parts[1]),
        OPERATOR_TYPE, parts -> Operator.valueOf(parts[1]),
        PROPERTY_TYPE, parts -> Property.valueOf(parts[1]),
        ITEM_TYPE, parts -> Item.valueOf(parts[1])
        );
    Board board;
    try(BufferedReader reader = Files.newBufferedReader(path, StandardCharsets.UTF_8)) {
    board = SaverLoader.loadFromTextFormat(reader, elementParserMap);
    } catch(IOException e) {
    System.err.println(e.getMessage());
    System.exit(1);
    return null;
    }
    board.initRules();
    return board;
  }
}
