package ac.bris.cs.platformer.theGame.movement;

/**
 * Encapsulation of velocity - responsible for recalculating at each
 * step based on acceleration and also handles horizontal drag
 */
public class Velocity extends Direction {

   /************************ Class Constants *******************/

   private static final int MAX_SPEED         = 5;
   private static final int TERMINAL_VELOCITY = 10;

   public enum Moving {
      RIGHT,
      LEFT,
      STILL
   }

   /************************ Instance Variable *******************/

   private double drag;

   /************************ Interface Methods *******************/

   void setDrag(final double drag)
   {
      this.drag = drag;
   }

   void recalculate(final Direction previousAcceleration,
                    final Direction acceleration)
   {
      if((x < MAX_SPEED) && (x > -MAX_SPEED)) {
         x += (acceleration.x() + previousAcceleration.x()) / 2.0;
      }
      if(y > -TERMINAL_VELOCITY) {
         y += (acceleration.y() + previousAcceleration.y()) / 2.0;
      }
   }
   
   void applyDrag()
   {
      if(x > 0.0) {
         x -= drag;
         if(x < 0.0) {
            x = 0.0;
         }
      } else if(x < 0.0) {
         x += drag;
         if (x > 0.0) {
            x = 0.0;
         }
      }
   }

   Moving moveDirection()
   {
      if(x > 0.0) {
         return Moving.RIGHT;
      } else if (x < 0.0) {
         return Moving.LEFT;
      } else {
         return Moving.STILL;
      }
   }
}
