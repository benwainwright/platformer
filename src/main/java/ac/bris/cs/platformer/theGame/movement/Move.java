package ac.bris.cs.platformer.theGame.movement;

import ac.bris.cs.platformer.theGame.PositionRelationship;
import ac.bris.cs.platformer.theGame.entities.Entity;

import java.awt.Dimension;

/**
 * Abstraction of a potential game movement, allowing moves to be created,
 * then checked for collisions before they are actually executed
 */
public class Move {

   private final Entity what;
   private final int    newLeft;
   private final int    newTop;
   private final int    newRight;
   private final int    newBottom;

   public Move(final Entity what,
               final int    x,
               final int    y)
   {
      final Dimension dims = what.getDimensions();
      this.what      = what;
      newLeft   = x;
      newRight  = (x + (int)dims.getWidth()) - 1;
      newBottom = y;
      newTop    = (y + (int)dims.getHeight()) - 1;
   }

   public boolean doesCollide(final Entity check)
   {
      PositionRelationship pr = PositionRelationship.compare(check.left(),
                                                             check.right(),
                                                             check.top(),
                                                             check.bottom(),
                                                             newLeft, newRight,
                                                             newTop, newBottom);
      switch(pr.type) {
         case OVERLAP:
         case IDENTICAL:
         case CONTAINER:
         case CONTAINED:
            return true;
         default:
            return false;
      }
   }

   public void execute()
   {
      what.doMove(newLeft, newBottom);
   }
}
