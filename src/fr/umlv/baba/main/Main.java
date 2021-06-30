package fr.umlv.baba.main;

import java.awt.Color;
import java.awt.geom.Rectangle2D;
import java.nio.file.Path;

import fr.umlv.baba.BoardDisplay;
import fr.umlv.baba.SaverLoader;
import fr.umlv.zen5.Application;
import fr.umlv.zen5.Event;
import fr.umlv.zen5.Event.Action;
import fr.umlv.zen5.ScreenInfo; 

public class Main {
  
  public static void main(String[] args) {
    Application.run(Color.BLACK, context -> {
      var level = Path.of("./levels/level_0.txt");
      
      ScreenInfo screenInfo = context.getScreenInfo();
      float width = screenInfo.getWidth();
      float height = screenInfo.getHeight();
      
      var boardDisplay = new BoardDisplay(width, height, 33, 18); //resolve problem of static non final variable.
      System.out.println(boardDisplay);
      
      var board = SaverLoader.configureLevel(level);

      board.refreshRules();
      if(!board.isValid()) {
        System.err.println("Board not valid");
        context.exit(1);
      }
   
      context.renderFrame(graphics2D -> {
        graphics2D.fill(new Rectangle2D.Float(0, 0, width, height));
        board.displayBoard(graphics2D);
      });
          
      for(;;) {
        Event event = context.pollOrWaitEvent(10);
        if (event == null) {
          continue;
        }
        Action action = event.getAction();
        if (action == Action.KEY_PRESSED) {
          var key = event.getKey();
          if(key.name() == "RIGHT" || key.name() == "LEFT" || key.name() == "UP" || key.name() == "DOWN") {
            board.move(board.switchDirection(key.name()));
            board.refreshRules();
            if(board.isWin()) {
              System.out.println("Win");
              context.exit(0);
              return;
            }
            
            else if(board.isLoose()) {
              System.out.println("Loose");
              context.exit(0);
              return;
            }
          }
        
          if (key.name() == "E") {
            context.exit(0);
            return;
          }
        }     
        context.renderFrame(graphics2D -> {
          graphics2D.fill(new Rectangle2D.Float(0, 0, width, height));
          board.displayBoard(graphics2D);
        });       
      }
    });
  }
}