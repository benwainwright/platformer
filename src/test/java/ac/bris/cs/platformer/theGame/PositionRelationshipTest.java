package ac.bris.cs.platformer.theGame;

import ac.bris.cs.platformer.theGame.entities.Platform;
import org.junit.Assert;
import org.junit.Test;

import static ac.bris.cs.platformer.theGame.PositionRelationship.Side.*;
import static ac.bris.cs.platformer.theGame.PositionRelationship.Type.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by bw12954 on 30/05/2016.
 */
public class PositionRelationshipTest {

   @Test
   public void shouldCorrectlyClassifySeparateEntities()
   throws Exception
   {
      Platform p1 = new Platform(0, 0, 10, 10);
      Platform p2 = new Platform(11, 11, 10, 10);
      Assert
         .assertEquals(SEPARATE, PositionRelationship.compare(p1, p2).type);
      assertEquals(0, PositionRelationship.compare(p1, p2).sides.size());

      p1 = new Platform(10, 7, 10, 10);
      p2 = new Platform(21, 2, 6, 6);
      assertEquals(SEPARATE, PositionRelationship.compare(p1, p2).type);
      assertEquals(0, PositionRelationship.compare(p1, p2).sides.size());

      p2 = new Platform(34, 10, 3, 3);
      assertEquals(SEPARATE, PositionRelationship.compare(p1, p2).type);
      assertEquals(0, PositionRelationship.compare(p1, p2).sides.size());
   }


   @Test
   public void shouldCorrectlyClassifBottomRightOverlap()
   {
      Platform p1 = new Platform(10, 7, 11, 10);
      Platform p2 = new Platform(19, 2, 5, 9);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(OVERLAP, relation.type);
      assertTrue(relation.sides.contains(RIGHT));
      assertFalse(relation.sides.contains(TOP));
      assertTrue(relation.sides.contains(BOTTOM));
      assertFalse(relation.sides.contains(LEFT));
   }

   @Test
   public void shouldCorrectlyClassifyRightOverlap()
   {
      Platform p1 = new Platform(10, 7, 10, 10);
      Platform p2 = new Platform(18, 7, 6, 10);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(OVERLAP, relation.type);
      assertTrue(relation.sides.contains(RIGHT));
      assertFalse(relation.sides.contains(TOP));
      assertFalse(relation.sides.contains(BOTTOM));
      assertFalse(relation.sides.contains(LEFT));

      p2 = new Platform(17, 11, 9, 5);
      relation = PositionRelationship.compare(p1, p2);
      assertEquals(OVERLAP, relation.type);
      assertTrue(relation.sides.contains(RIGHT));
      assertFalse(relation.sides.contains(TOP));
      assertFalse(relation.sides.contains(BOTTOM));
      assertFalse(relation.sides.contains(LEFT));   }

   @Test
   public void shouldCorrectlyClassifyTopRightOverlap()
   {
      Platform p1 = new Platform(10, 7, 10, 10);
      Platform p2 = new Platform(16, 14, 13, 8);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(OVERLAP, relation.type);
      assertTrue(relation.sides.contains(RIGHT));
      assertTrue(relation.sides.contains(TOP));
      assertFalse(relation.sides.contains(BOTTOM));
      assertFalse(relation.sides.contains(LEFT));
   }

   @Test
   public void shouldCorrectlyClassifyTopOverlap()
   {
      Platform p1 = new Platform(10, 7, 10, 10);
      Platform p2 = new Platform(10, 16, 10, 2);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(OVERLAP, relation.type);
      assertFalse(relation.sides.contains(RIGHT));
      assertTrue(relation.sides.contains(TOP));
      assertFalse(relation.sides.contains(BOTTOM));
      assertFalse(relation.sides.contains(LEFT));
   }

   @Test
   public void shouldCorrectlyClassifyTopLeftOverlap()
   {
      Platform p1 = new Platform(10, 7, 10, 10);
      Platform p2 = new Platform(6, 14, 8, 5);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(OVERLAP, relation.type);
      assertFalse(relation.sides.contains(RIGHT));
      assertTrue(relation.sides.contains(TOP));
      assertFalse(relation.sides.contains(BOTTOM));
      assertTrue(relation.sides.contains(LEFT));
   }
   @Test
   public void shouldCorrectlyClassifyLeftOverlap()
   {
      Platform p1 = new Platform(10, 7, 10, 10);
      Platform p2 = new Platform(9, 7, 6, 10);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(OVERLAP, relation.type);
      assertFalse(relation.sides.contains(RIGHT));
      assertFalse(relation.sides.contains(TOP));
      assertFalse(relation.sides.contains(BOTTOM));
      assertTrue(relation.sides.contains(LEFT));
   }

   @Test
   public void shouldCorrectlyClassifyBottomLeftOverlap()
   {
      Platform p1 = new Platform(10, 7, 10, 10);
      Platform p2 = new Platform(1, 5, 11, 4);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(OVERLAP, relation.type);
      assertFalse(relation.sides.contains(RIGHT));
      assertFalse(relation.sides.contains(TOP));
      assertTrue(relation.sides.contains(BOTTOM));
      assertTrue(relation.sides.contains(LEFT));
   }

   @Test
   public void shouldCorrectlyClassifyBottomOverlap()
   {
      Platform p1 = new Platform(10, 7, 10, 10);
      Platform p2 = new Platform(10, 0, 10, 9);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(OVERLAP, relation.type);
      assertFalse(relation.sides.contains(RIGHT));
      assertFalse(relation.sides.contains(TOP));
      assertTrue(relation.sides.contains(BOTTOM));
      assertFalse(relation.sides.contains(LEFT));

      p2 = new Platform(15, 6, 3, 2);
      relation = PositionRelationship.compare(p1, p2);
      assertEquals(OVERLAP, relation.type);
      assertFalse(relation.sides.contains(RIGHT));
      assertFalse(relation.sides.contains(TOP));
      assertTrue(relation.sides.contains(BOTTOM));
      assertFalse(relation.sides.contains(LEFT));

      p2 = new Platform(15, 2, 5, 8);
      relation = PositionRelationship.compare(p1, p2);
      assertEquals(OVERLAP, relation.type);
      assertFalse(relation.sides.contains(RIGHT));
      assertFalse(relation.sides.contains(TOP));
      assertTrue(relation.sides.contains(BOTTOM));
      assertFalse(relation.sides.contains(LEFT));

      p2 = new Platform(11, 3, 7, 6);
      relation = PositionRelationship.compare(p1, p2);
      assertEquals(OVERLAP, relation.type);
      assertFalse(relation.sides.contains(RIGHT));
      assertFalse(relation.sides.contains(TOP));
      assertTrue(relation.sides.contains(BOTTOM));
      assertFalse(relation.sides.contains(LEFT));
   }

   @Test
   public void shouldCorrectlyClassifyIdenticalPosition()
   {
      Platform p1 = new Platform(10, 7, 10, 10);
      Platform p2 = new Platform(10, 7, 10, 10);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(IDENTICAL, relation.type);
      assertEquals(0, relation.sides.size());
   }

   @Test
   public void shouldCorrectlyClassifyContainer()
   {
      Platform p1 = new Platform(10, 7, 10, 10);
      Platform p2 = new Platform(4, 2, 31, 32);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(CONTAINER, relation.type);
      assertEquals(0, relation.sides.size());
   }

   @Test
   public void shouldCorrectlyClassifyContained()
   {
      Platform p1 = new Platform(10, 7, 10, 10);
      Platform p2 = new Platform(16, 12, 3, 3);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(CONTAINED, relation.type);
      assertEquals(0, relation.sides.size());

      p2 = new Platform(10, 7, 4, 4);
      relation = PositionRelationship.compare(p1, p2);
      assertEquals(CONTAINED, relation.type);
      assertEquals(0, relation.sides.size());

      p2 = new Platform(10, 9, 5, 2);
      relation = PositionRelationship.compare(p1, p2);
      assertEquals(CONTAINED, relation.type);
      assertEquals(0, relation.sides.size());
   }

   @Test
   public void shouldCorrectlyClassifyBottomEdge()
   {
      Platform p1 = new Platform(3, 4, 7, 6);
      Platform p2 = new Platform(3, 1, 3, 3);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(EDGE, relation.type);
      assertFalse(relation.sides.contains(RIGHT));
      assertTrue(relation.sides.contains(BOTTOM));
      assertFalse(relation.sides.contains(TOP));
      assertFalse(relation.sides.contains(LEFT));

      p1 = new Platform(3, 4, 7, 6);
      p2 = new Platform(8, 1, 3, 3);
      relation = PositionRelationship.compare(p1, p2);
      assertEquals(EDGE, relation.type);
      assertFalse(relation.sides.contains(RIGHT));
      assertTrue(relation.sides.contains(BOTTOM));
      assertFalse(relation.sides.contains(TOP));
      assertFalse(relation.sides.contains(LEFT));
   }

   @Test
   public void shouldCorrectlyClassifyLeftEdge()
   {
      Platform p1 = new Platform(3, 4, 7, 6);
      Platform p2 = new Platform(1, 4, 2, 4);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(EDGE, relation.type);
      assertFalse(relation.sides.contains(RIGHT));
      assertFalse(relation.sides.contains(TOP));
      assertFalse(relation.sides.contains(BOTTOM));
      assertTrue(relation.sides.contains(LEFT));
   }

   @Test
   public void shouldCorrectlyClassifyRightEdge()
   {
      Platform p1 = new Platform(3, 4, 7, 6);
      Platform p2 = new Platform(10, 6, 4, 2);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(EDGE, relation.type);
      assertTrue(relation.sides.contains(RIGHT));
      assertFalse(relation.sides.contains(TOP));
      assertFalse(relation.sides.contains(BOTTOM));
      assertFalse(relation.sides.contains(LEFT));

      p1 = new Platform(10, 7, 10, 10);
      p2 = new Platform(20, 2, 6, 6);
      assertEquals(EDGE, PositionRelationship.compare(p1, p2).type);
      assertTrue(relation.sides.contains(RIGHT));
      assertFalse(relation.sides.contains(TOP));
      assertFalse(relation.sides.contains(BOTTOM));
      assertFalse(relation.sides.contains(LEFT));
   }

   @Test
   public void shouldCorrectlyClassifyTopEdge()
   {
      Platform p1 = new Platform(3, 4, 7, 6);
      Platform p2 = new Platform(3, 10, 7, 4);
      PositionRelationship relation = PositionRelationship.compare(p1, p2);
      assertEquals(EDGE, relation.type);
      assertFalse(relation.sides.contains(RIGHT));
      assertFalse(relation.sides.contains(BOTTOM));
      assertTrue(relation.sides.contains(TOP));
      assertFalse(relation.sides.contains(LEFT));
   }
}