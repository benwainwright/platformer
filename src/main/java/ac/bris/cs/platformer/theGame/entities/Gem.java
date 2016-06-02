package ac.bris.cs.platformer.theGame.entities;

import ac.bris.cs.platformer.SoundPlayer;

import java.io.File;
import java.io.IOException;

/**
 * Created by bw12954 on 01/06/16.
 */
@SuppressWarnings("StringConcatenationMissingWhitespace")
public class Gem extends Entity {

   private static final String      IMAGE_FILE = "triangleGem.png";
   private static final int         Z_POSITION = 500;
   private static final int         MASS       = 1;
   private        final SoundPlayer ding;

   public Gem(final int    x,
              final int    y,
              final double scale,
              final String imageFile)
   throws IOException
   {
      super(x, y, Z_POSITION, scale, imageFile);
      ding = new SoundPlayer("audio" + File.separator + "ding.wav");
   }

   public Gem(final int    x,
              final int    y,
              final double scale)
   throws IOException
   {
      this(x, y, scale, IMAGE_FILE);
   }

   @Override
   public int getMass()
   {
      return MASS;
   }

   @Override
   @SuppressWarnings("CastToConcreteClass")
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

   }

   @Override
   public void onTick()
   {
   }
}
