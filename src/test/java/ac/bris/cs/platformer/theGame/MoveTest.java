package ac.bris.cs.platformer.theGame;

import ac.bris.cs.platformer.theGame.entities.Platform;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by bw12954 on 27/05/16.
 */
public class MoveTest {

   @Test
   public void collidesWithShouldIgnoreMovesThatDontOverlap()
   throws Exception
   {
      Platform                                    p1 = new Platform(0, 0, 10, 10);
      Platform                                    p2 = new Platform(100, 100, 10, 10);
      ac.bris.cs.platformer.theGame.movement.Move m  = new ac.bris.cs.platformer.theGame.movement.Move(p2, 11, 11);
      assertFalse(m.doesCollide(p1));

      p1 = new Platform(10, 7, 10, 10);
      p2 = new Platform(100, 100, 6, 6);
      m = new ac.bris.cs.platformer.theGame.movement.Move(p2, 20, 2);
      assertFalse(m.doesCollide(p1));

      p2 = new Platform(100, 100, 3, 3);
      m = new ac.bris.cs.platformer.theGame.movement.Move(p2, 34, 10);
      assertFalse(m.doesCollide(p1));
   }

   @Test
   public void collidesWithShouldSpotBottomRightOverlap()
   {
      Platform                                    p1 = new Platform(10, 7, 11, 10);
      Platform                                    p2 = new Platform(53, 100, 8, 4);
      ac.bris.cs.platformer.theGame.movement.Move m  = new ac.bris.cs.platformer.theGame.movement.Move(p2, 19, 4);
      assertTrue(m.doesCollide(p1));
   }

   @Test
   public void collidesWithShouldSpotRightOverlap()
   {
      Platform                                    p1 = new Platform(10, 7, 10, 10);
      Platform                                    p2 = new Platform(730, 231, 6, 10);
      ac.bris.cs.platformer.theGame.movement.Move m  = new ac.bris.cs.platformer.theGame.movement.Move(p2, 18, 7);
      assertTrue(m.doesCollide(p1));
   }

   @Test
   public void collideWithShouldSpotTopRightOverlap()
   {
      Platform                                    p1 = new Platform(10, 7, 10, 10);
      Platform                                    p2 = new Platform(730, 231, 13, 8);
      ac.bris.cs.platformer.theGame.movement.Move m  = new ac.bris.cs.platformer.theGame.movement.Move(p2, 16, 14);
      assertTrue(m.doesCollide(p1));
   }

   @Test
   public void collidesWithShouldSpotTopOverlap()
   {
      Platform                                    p1 = new Platform(10, 7, 10, 10);
      Platform                                    p2 = new Platform(730, 231, 10, 2);
      ac.bris.cs.platformer.theGame.movement.Move m  = new ac.bris.cs.platformer.theGame.movement.Move(p2, 10, 16);
      assertTrue(m.doesCollide(p1));
   }

   @Test
   public void collidesWithShouldSpotTopLeftOverlap()
   {
      Platform                                    p1 = new Platform(10, 7, 10, 10);
      Platform                                    p2 = new Platform(730, 231, 8, 5);
      ac.bris.cs.platformer.theGame.movement.Move m  = new ac.bris.cs.platformer.theGame.movement.Move(p2, 6, 14);
      assertTrue(m.doesCollide(p1));
   }

   @Test
   public void collidesWithShouldSpotLeftOverlap()
   {
      Platform                                    p1 = new Platform(10, 7, 10, 10);
      Platform                                    p2 = new Platform(730, 231, 6, 10);
      ac.bris.cs.platformer.theGame.movement.Move m  = new ac.bris.cs.platformer.theGame.movement.Move(p2, 9, 7);
      assertTrue(m.doesCollide(p1));
   }

   @Test
   public void collidesWithShouldSpotBottomLeftOverlap()
   {
      Platform                                    p1 = new Platform(10, 7, 10, 10);
      Platform                                    p2 = new Platform(730, 231, 11, 4);
      ac.bris.cs.platformer.theGame.movement.Move m  = new ac.bris.cs.platformer.theGame.movement.Move(p2, 1, 5);
      assertTrue(m.doesCollide(p1));
   }

   @Test
   public void collidesWithShouldSpotBottomOverlap()
   {
      Platform                                    p1 = new Platform(10, 7, 10, 10);
      Platform                                    p2 = new Platform(730, 231, 10, 9);
      ac.bris.cs.platformer.theGame.movement.Move m  = new ac.bris.cs.platformer.theGame.movement.Move(p2, 10, 0);
      assertTrue(m.doesCollide(p1));
   }

   @Test
   public void collidesWithShouldSpotCentralOverlap()
   {
      Platform                                    p1 = new Platform(10, 7, 10, 10);
      Platform                                    p2 = new Platform(730, 231, 10, 10);
      ac.bris.cs.platformer.theGame.movement.Move m  = new ac.bris.cs.platformer.theGame.movement.Move(p2, 10, 7);
      assertTrue(m.doesCollide(p1));
   }

   @Test
   public void collidesWithShouldSpotContainingOverlap()
   {
      Platform                                    p1 = new Platform(10, 7, 10, 10);
      Platform                                    p2 = new Platform(730, 231, 31, 22);
      ac.bris.cs.platformer.theGame.movement.Move m  = new ac.bris.cs.platformer.theGame.movement.Move(p2, 4, 2);
      assertTrue(m.doesCollide(p1));
   }

   @Test
   public void collidesWithShouldSpotEnvelopedMove()
   {
      Platform                                    p1 = new Platform(10, 7, 10, 10);
      Platform                                    p2 = new Platform(730, 231, 3, 3);
      ac.bris.cs.platformer.theGame.movement.Move m  = new ac.bris.cs.platformer.theGame.movement.Move(p2, 16, 12);
      assertTrue(m.doesCollide(p1));

      p2 = new Platform(730, 231, 4, 4);
      m = new ac.bris.cs.platformer.theGame.movement.Move(p2, 10, 7);
      assertTrue(m.doesCollide(p1));

      p2 = new Platform(730, 231, 3, 2);
      m = new ac.bris.cs.platformer.theGame.movement.Move(p2, 15, 7);
      assertTrue(m.doesCollide(p1));
   }
}