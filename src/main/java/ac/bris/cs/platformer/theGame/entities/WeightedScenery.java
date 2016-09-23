package ac.bris.cs.platformer.theGame.entities;

import java.io.IOException;

/**
 * Subclass of Entity - scenery such as trees, that react to gravity, do
 * not collide with the player, but do obscure him from view
 */
public class WeightedScenery extends Entity {

   /************************ Class Constants *******************/

   private static final int Z_POSITION = 1000;
   private static final int MASS       = 1;

   /************************* Constructor ************************/

   public WeightedScenery(final int x,
                          final int y,
                          final double scale,
                          final String imageFile)
   throws IOException
   {
      super(x, y, Z_POSITION, scale, imageFile);
   }

   /************************ Interface Methods *******************/

   @Override
   public int getMass()
   {
      return MASS;
   }

   @Override
   public boolean onCollision(Entity withWhat)
   {
      return true;
   }

   @Override
   public void onSpriteUpdate()
   {
      // Required by superclass
   }

   @Override
   public void onTick()
   {
      // Required by superclass
   }
}
