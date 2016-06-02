package ac.bris.cs.platformer.theGame.entities;

import ac.bris.cs.platformer.theGame.PositionRelationship;
import ac.bris.cs.platformer.theGame.PositionRelationship.Side;

import java.io.IOException;

import static ac.bris.cs.platformer.Utils.downScaleInt;

/**
 * Entity sub-class giving my player things to stand on...
 */

public class Platform extends Entity {

   private static final String IMAGE_FILE       = "platform1.png";
   private static final int    Z_POSITION       = 1000;
   private static final double GEM_BOUNCE_FORCE = 4.0;
   private static final int    MASS             = 0;

   private int topOffset;

   public Platform(final int    x,
                   final int    y,
                   final double scale,
                   final String imageFile)
   throws IOException
   {
      super(x, y, Z_POSITION, scale, imageFile);
   }

   public Platform(final int    x,
                   final int    y,
                   final int    topOffset,
                   final double scale,
                   final String imageFile)
   throws IOException
   {
      this(x, y, scale, imageFile);
      this.topOffset = downScaleInt(topOffset, scale);
   }

   public Platform(final int    x,
                   final int    y,
                   final int    topOffset,
                   final double scale)
   throws IOException
   {
      this(x, y, topOffset, scale, IMAGE_FILE);
   }

   public Platform(final int    x,
                   final int    y,
                   final int    w,
                   final int    h,
                   final double scale)
   {
      super(downScaleInt(x, scale), downScaleInt(y, scale),
            downScaleInt(w, scale), downScaleInt(h, scale));
   }

   public Platform(final int x,
                   final int y,
                   final int w,
                   final int h)
   {
      this(x, y, w, h, 1.0);
   }

   @Override
   public int top()
   {
      return super.top() - topOffset;
   }

   @Override
   public int getMass()
   {
      return MASS;
   }

   @Override
   public boolean onCollision(final Entity withWhat)
   {
      PositionRelationship pr = PositionRelationship.compare(this, withWhat);
      if(pr.sides.contains(Side.TOP)) {
         withWhat.trajectory.stopY();
         withWhat.moveY(top() + 1);
         if(withWhat instanceof Gem) {
            withWhat.applyForce(0.0, GEM_BOUNCE_FORCE);
         }
         return false;
      }
      return true;
   }

   @Override
   protected void onSpriteUpdate()
   {

   }

   @Override
   public void onTick()
   {

   }
}
