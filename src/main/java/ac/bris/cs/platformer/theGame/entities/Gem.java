package ac.bris.cs.platformer.theGame.entities;

import ac.bris.cs.platformer.SoundPlayer;

import java.io.IOException;

/**
 * Collectible gems. These behave mostly like any other entity, accept
 * when the player collides with them, they disappear and increase his
 * score
 */
public class Gem extends Entity {

   /************************ Class Constants *******************/

   private static final String      IMAGE_FILE = "triangleGem.png";
   private static final int         Z_POSITION = 500;
   private static final int         MASS       = 1;

   private        final SoundPlayer ding;

   /************************* Constructor ************************/

   public Gem(final int x, final int y, final double scale)
   throws IOException
   {
      this(x, y, scale, IMAGE_FILE);
   }

   private Gem(final int  x, final int y, final double scale,
               final String imageFile)
   throws IOException
   {
      super(x, y, Z_POSITION, scale, imageFile);
      ding = new SoundPlayer("audio/ding.wav");
   }

   /************************ Interface Methods *******************/

   @Override
   public int getMass()
   {
      return MASS;
   }

   @Override
   public boolean onCollision(final Entity withWhat)
   {
      if(withWhat instanceof Player) {
         alive = false;
         ding.play();
         ((Player)withWhat).increaseScore();
      }
      return false;
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
