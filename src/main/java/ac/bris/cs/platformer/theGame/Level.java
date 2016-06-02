package ac.bris.cs.platformer.theGame;

import ac.bris.cs.platformer.Window;
import ac.bris.cs.platformer.theGame.entities.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ben on 31/05/2016.
 */
public class Level extends World {

   private static final String soundtrack = "audio/soundtrack.wav";
   private final List<Entity> gems;

   public Level(Window window)
   throws IOException
   {
      super(window);
      gems = new ArrayList<>();
      setupGems();
      setObjects();
   }

   public void resetLevel()
   {
      player.resetScore();
      entities.clear();
      setupGems();
      setObjects();
   }

   private void setupGems()
   {
      try {
         gems.clear();
         gems.add(new Gem(600, 400, scaleFactor));
         gems.add(new Gem(1200, 400, scaleFactor));
         gems.add(new Gem(1600, 1000, scaleFactor));
      } catch(IOException e) {

      }
   }

   private void setObjects()
   {
      try {
         player = new Player(300, 1000);
         super.message("Press escape to exit game...");
         entities.add(player);
         entities.add(new Platform(0, 0, 79, scaleFactor, "floor.png"));
         entities.add(new Platform(200, 700, 5, scaleFactor));
         entities.add(new Platform(1500, 800, 5, scaleFactor, "platform2.png"));
         entities.add(new Platform(1100, 600, 5, scaleFactor, "platform2.png"));
         entities.add(new WeightedScenery(900, 300, scaleFactor, "tree.png"));
         entities.add(new WeightedScenery(1800, 500, scaleFactor, "tree2.png"));
         entities.addAll(gems);
      } catch (IOException e) {
         // TODO
      }
   }


   @Override
   public String soundtrackFilename()
   {
      return soundtrack;
   }

   @Override
   public boolean hasWon()
   {
      if(player.getScore() == gems.size()) {
         return true;
      }
      return false;
   }
}
