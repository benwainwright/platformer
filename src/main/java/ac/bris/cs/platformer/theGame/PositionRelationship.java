package ac.bris.cs.platformer.theGame;

import ac.bris.cs.platformer.theGame.entities.Entity;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static ac.bris.cs.platformer.theGame.PositionRelationship.Side.*;
import static ac.bris.cs.platformer.theGame.PositionRelationship.Type.*;

/**
 * Classifies the relationship between two entities in terms of position
 *
 * Note that entity origins are considered to be at the BOTTOM left,
 * not top left. This is because when the background is scaled down.
 * it is moved upwards to ensure the bottom part of the background image is
 * not cut off (which may contain the 'floor'). Reversed y coordinates
 * matches this adjustment.
 *
 * Relationships are classified in terms of the relationship of Entity Two to
 * entity ONE
 *
 * e.g
 *
 * LEFT and EDGE indicates that two is on the left edge of one
 * CONTAINER indicates that two is the container of one
 * CONTAINED indicates that two is contained by one
 *
 * Note: Code in this class is not particularly elegant, but it does the job.
 * definitely a target for refactoring at a later stage
 */

public final class PositionRelationship {

   public final List<Side> sides;
   public final Type       type;

   public enum Side {
      TOP,
      LEFT,
      RIGHT,
      BOTTOM,
   }

   public enum Type {
      EDGE,
      OVERLAP,
      CONTAINER,
      CONTAINED,
      IDENTICAL,
      SEPARATE
   }

   private PositionRelationship(final Side[] sides,
                                final Type type)
   {
      this.type  = type;
      this.sides = Collections.unmodifiableList(Arrays.asList(sides));
   }

   @SuppressWarnings("FeatureEnvy")
   public static PositionRelationship compare(final Entity one,
                                              final Entity two)
   {
      if(one == two) {
         return new PositionRelationship(new Side[] { }, IDENTICAL);
      } else {
         final int l1 = one.left();
         final int r1 = one.right();
         final int t1 = one.top();
         final int b1 = one.bottom();
         final int l2 = two.left();
         final int r2 = two.right();
         final int t2 = two.top();
         final int b2 = two.bottom();
         return compare(l1, r1, t1, b1, l2, r2, t2, b2);
      }
   }

   @SuppressWarnings("NestedAssignment")
   public static PositionRelationship compare(final int l1, final int r1,
                                              final int t1, final int b1,
                                              final int l2, final int r2,
                                              final int t2, final int b2)
   {
      PositionRelationship result;
      if((l1 == l2) && (r1 == r2) && (t1 == t2) && (b1 == b2)) {
         return new PositionRelationship(new Side[] { }, IDENTICAL);
      }
      if((l2 <= l1) && (r2 >= r1) && (t2 >= t1) && (b2 <= b1)) {
         return new PositionRelationship(new Side[] { }, CONTAINER);
      }
      if((l2 >= l1) && (r2 <= r1) && (t2 <= t1) && (b2 >= b1)) {
         return new PositionRelationship(new Side[] { }, CONTAINED);
      }
      if((result = checkForEdges(l1, r1, t1, b1, l2, r2, t2, b2)) != null) {
         return result;
      }
      if((result = checkForOverlaps(l1, r1, t1, b1, l2, r2, t2, b2)) != null) {
         return result;
      }
      return new PositionRelationship(new Side[] { }, SEPARATE);
   }

   private static PositionRelationship checkForEdges(final int l1, final int r1,
                                                     final int t1, final int b1,
                                                     final int l2, final int r2,
                                                     final int t2, final int b2)
   {
      final Side[] side = new Side[1];
      if((r2 == (l1 - 1)) && (isBetween(t2, b1, t1) || isBetween(b2, b1, t1))) {
         side[0] = LEFT;
      } else if((r1 == (l2 - 1)) && (isBetween(t2, b1, t1) || isBetween(b2, b1, t1))) {
         side[0] = RIGHT;
      } else if((t1 == (b2 - 1)) && (isBetween(l2, l1, r1) || isBetween(r2, l1, r1))) {
         side[0] = TOP;
      } else if((t2 == (b1 - 1)) && (isBetween(l2, l1, r1) || isBetween(r2, l1, r1))) {
         side[0] = BOTTOM;
      } else {
         return null;
      }
      return new PositionRelationship(side, EDGE);
   }

   private static PositionRelationship checkForOverlaps(final int l1, final int r1,
                                                        final int t1, final int b1,
                                                        final int l2, final int r2,
                                                        final int t2, final int b2)
   {
      final Side[] sides = new Side[2];
      if((l2 >= l1) && (r2 <= r1) && isBetween(b2, b1, t1)){
         sides[0] = TOP;
      } else if((l2 >= l1) && (r2 <= r1) && isBetween(t2, b1, t1)){
         sides[0] = BOTTOM;
      } else if((t2 <= t1) && (b2 >= b1) && isBetween(r2, l1, r1)){
         sides[0] = LEFT;
      } else if((t2 <= t1) && (b2 >= b1) && isBetween(l2, l1, r1)) {
         sides[0] = RIGHT;
      } else if(isBetween(l2, l1, r1) && isBetween(t2, b1, t1)) {
         sides[0] = BOTTOM;
         sides[1] = RIGHT;
      } else if(isBetween(r2, l1, r1) && isBetween(t2, b1, t1)) {
         sides[0] = BOTTOM;
         sides[1] = LEFT;
      } else if(isBetween(r2, l1, r1) && isBetween(b2, b1, t1)) {
         sides[0] = TOP;
         sides[1] = LEFT;
      } else if(isBetween(l2, l1, r1) && isBetween(b2, b1, t1)) {
         sides[0] = TOP;
         sides[1] = RIGHT;
      } else {
         return null;
      }
      return new PositionRelationship(sides, OVERLAP);
   }

   private static boolean isBetween(final int num,
                                    final int lower,
                                    final int upper)
   {
      return (num >= lower) && (num <= upper);
   }
}
