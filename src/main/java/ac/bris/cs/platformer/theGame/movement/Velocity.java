package ac.bris.cs.platformer.theGame.movement;

/**
 * Created by bw12954 on 01/06/16.
 */
public class Velocity extends Direction {

   public enum Moving {
      RIGHT,
      LEFT,
      STILL
   }

   private static final int MAX_SPEED         = 5;
   private static final int TERMINAL_VELOCITY = 10;

   private double drag;

   void setDrag(final double drag)
   {
      this.drag = drag;
   }

   void recalculate(final Direction prevAccel,
                           final Direction accel)
   {
      if((x < MAX_SPEED) && (x > -MAX_SPEED)) {
         x += (accel.x() + prevAccel.x()) / 2.0;
      }
      if(y > -TERMINAL_VELOCITY) {
         y += (accel.y() + prevAccel.y()) / 2.0;
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
