package ac.bris.cs.platformer.theGame.movement;

import ac.bris.cs.platformer.theGame.entities.Entity;
import ac.bris.cs.platformer.theGame.movement.Velocity.Moving;

/**
 * This class encapsulates the dynamic system for each class, keeping
 * track of velocity and acceleration at each game step
 *
 * The classes in this package folder are my implementation of an algorithm
 * explained on the following page, with stuff like horizontal drag added by me
 *
 * http://gamedev.stackexchange.com/questions/15708/how-can-i-implement-gravity
 */
public class Trajectory {

   /************************ Instance Variable *******************/

   private final Entity    what;
   private final Direction acceleration;
   private final Velocity  velocity;
   private final Direction force;

   /************************** Constructor **********************/

   public Trajectory(final Entity what)
   {
      acceleration = new Direction();
      velocity     = new Velocity();
      force        = new Direction();
      this.what    = what;
   }

   /************************ Interface Methods *******************/

   public void applyForce(final double x,
                          final double y)
   {
      force.increase(x, y);
   }

   public void stopY()
   {
      acceleration.clearY();
      velocity.clearY();
      force.clearY();
   }

   public void drag(final double drag)
   {
      velocity.setDrag(drag);
   }

   public Move getMove()
   {
      if(what.getMass() == 0) {
         return null;
      } else {
         velocity.applyDrag();
         final Direction lastAcceleration = acceleration.clone();
         final Move      move             = doMove(lastAcceleration);
         calculateNewAcceleration();
         velocity.recalculate(lastAcceleration, acceleration);
         force.clear();
         return move;
      }
   }
   /************************ Step Calculations *******************/

   private Move doMove(final Direction lastAcceleration)
   {
      final int newX = (int)(what.left() + velocity.x() +
                            (lastAcceleration.x() / 2));
      final int newY = (int)(what.bottom() + velocity.y() +
                            (lastAcceleration.y() / 2));
      return new Move(what, newX, newY);
   }

   private void calculateNewAcceleration()
   {
      final double x = force.x() / what.getMass();
      final double y = force.y() / what.getMass();
      acceleration.set(x, y);
   }

   public Moving moveDirection()
   {
      return velocity.moveDirection();
   }
}
