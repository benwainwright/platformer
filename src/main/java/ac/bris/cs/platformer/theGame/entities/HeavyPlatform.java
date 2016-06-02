package ac.bris.cs.platformer.theGame.entities;

import java.io.IOException;

/**
 * Created by bw12954 on 02/06/16.
 */
public class HeavyPlatform extends Platform {

   private static final int Z_POSITION = 300;
   private static final int MASS       = 1;

   public HeavyPlatform(final int    x,
                        final int    y,
                        final double scale,
                        final String imageFile)
   throws IOException
   {
      super(x, y, Z_POSITION, scale, imageFile);
   }

   @Override
   public int getMass()
   {
      return MASS;
   }

   @Override
   public boolean onCollision(Entity withWhat)
   {
      return super.onCollision(withWhat);
   }
}
