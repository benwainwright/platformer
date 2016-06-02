package ac.bris.cs.platformer.theGame.movement;

/**
 * Created by bw12954 on 01/06/16.
 */
public class Direction {

   double x;
   double y;

   double x() {
      return x;
   }

   void set(double x, double y)
   {
      this.x = x;
      this.y = y;
   }

   void increase(double x, double y)
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
      Direction dir = new Direction();
      dir.set(x, y);
      return dir;
   }
}
