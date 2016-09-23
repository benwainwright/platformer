package ac.bris.cs.platformer.theGame.movement;

/**
 * Simple class to encapsulate information about a quantity applied in a
 * particularly direction (given a set of x and y coordinates), used for
 * things like velocity and acceleration
 */
class Direction {

   /************************ Instance Variable *******************/

   double x;
   double y;

   /************************ Interface Methods *******************/

   double x() {
      return x;
   }


   void set(final double x, final double y)
   {
      this.x = x;
      this.y = y;
   }

   void increase(final double x, final double y)
   {
      this.x += x;
      this.y += y;
   }

   public double y() {
      return y;
   }

   void clearX()
   {
      x = 0.0;
   }

   void clearY()
   {
      y = 0.0;
   }

   void clear()
   {
      clearX();
      clearY();
   }


   @Override
   public Direction clone()
   {
      try {
         super.clone();
      } catch(CloneNotSupportedException e) {
         // It IS supported. Do nothing...
      }
      final Direction dir = new Direction();
      dir.set(x, y);
      return dir;
   }
}
