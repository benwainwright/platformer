package ac.bris.cs.platformer.theGame.entities;

import ac.bris.cs.platformer.theGame.PositionRelationship;
import ac.bris.cs.platformer.theGame.PositionRelationship.Side;
import ac.bris.cs.platformer.theGame.movement.Trajectory;

import java.io.IOException;

import static ac.bris.cs.platformer.Utils.downScaleInt;

/**
 * Platform objects. The main part of a platform game
 *
 * They do not react to gravity and block the player from falling
 * through them. Also apply a quick upwards force each time they are hit
 * by a Gem, causing them to bounce.
 */

public class Platform extends Entity {

   /************************ Class Constants *******************/

   private static final String IMAGE_FILE       = "platform1.png";
   private static final int    Z_POSITION       = 1000;
   private static final double GEM_BOUNCE_FORCE = 4.0;
   private static final int    MASS             = 0;

   /************************ Instance Variable *******************/

   private int topOffset;

   /************************* Constructors ***********************/

   public Platform(final int x, final int y, final int topOffset,
                   final double scale, final String imageFile)
   throws IOException
   {
      this(x, y, scale, imageFile);
      this.topOffset = downScaleInt(topOffset, scale);
   }

   public Platform(final int x, final int y,
                   final int w, final int h)
   {
      this(x, y, w, h, 1.0);
   }

   public Platform(final int x, final int y,
                   final int topOffset, final double scale)
   throws IOException
   {
      this(x, y, topOffset, scale, IMAGE_FILE);
   }

   private Platform(final int x, final int y,
                    final int w, final int h,
                    final double scale)
   {
      super(downScaleInt(x, scale), downScaleInt(y, scale),
            downScaleInt(w, scale), downScaleInt(h, scale));
   }

   private Platform(final int x, final int y,
                    final double scale, final String imageFile)
   throws IOException
   {
      super(x, y, Z_POSITION, scale, imageFile);
   }

   /************************ Interface Methods *******************/

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
      final Trajectory hisTrajectory = withWhat.getTrajectory();
      final PositionRelationship pr  = PositionRelationship.compare(this,
                                                                    withWhat);
      if(pr.sides.contains(Side.TOP)) {
         hisTrajectory.stopY();
         final int newTop = top() + 1;
         withWhat.moveY(newTop);
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
      // Required by superclass
   }

   @Override
   public void onTick()
   {
      // Required by superclass
   }
}
