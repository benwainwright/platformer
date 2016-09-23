package ac.bris.cs.platformer.theGame;

import ac.bris.cs.platformer.Window;
import ac.bris.cs.platformer.theGame.entities.Entity;
import ac.bris.cs.platformer.theGame.entities.Gem;
import ac.bris.cs.platformer.theGame.entities.Platform;
import ac.bris.cs.platformer.theGame.entities.Player;
import ac.bris.cs.platformer.theGame.entities.WeightedScenery;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class sets out the initial state of a particular level, provides
 * logic to determine the game winning condition, along with a
 * soundtrack to be played during the level
 */
public class Level extends World {

   /************************ Instance Variable *******************/

   private final List<Entity> gems;

   /************************ Class Constants *******************/

   private static final String soundtrack = "audio/soundtrack.wav";
   private static final int    GEM_INIT   = 10;

   /************************* Constructor ************************/

   public Level(final Window window)
   throws IOException
   {
      super(window);
      gems = new ArrayList<>(GEM_INIT);
      setupGems();
      setObjects();
   }

   /************************ Interface Methods *******************/

   @Override
   public void resetLevel()
   {
      player.resetScore();
      entities.clear();
      setupGems();
      setObjects();
   }

   @Override
   public String soundtrackFilename()
   {
      return soundtrack;
   }

   @Override
   public boolean hasWon()
   {
      return player.getScore() == gems.size();
   }

   /*********************** Initial State ****************************/

   private void setupGems()
   {
      try {
         gems.clear();
         gems.add(new Gem(600, 400, scaleFactor));
         gems.add(new Gem(1200, 400, scaleFactor));
         gems.add(new Gem(1600, 1000, scaleFactor));
      } catch(final IOException e) {
         parent.fatalError("Failed to load resources...");
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
      } catch (final IOException e) {
         parent.fatalError("Failed to load resources...");
      }
   }
}
