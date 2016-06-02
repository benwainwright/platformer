package ac.bris.cs.platformer.theGame.entities;

import ac.bris.cs.platformer.SoundPlayer;
import ac.bris.cs.platformer.theGame.SpriteSequence;
import ac.bris.cs.platformer.theGame.SpriteSheet;
import ac.bris.cs.platformer.theGame.movement.Velocity.Moving;

import java.io.IOException;

import static ac.bris.cs.platformer.theGame.movement.Velocity.Moving.STILL;

/**
 * Maintains the identity of the player entity
 */

public class Player extends Entity {

   private static final String fileName   = "mario.png";
   private static final int    ROWS       = 4;
   private static final int    COLS       = 12;
   private static final int    WIDTH      = 338;
   private static final int    HEIGHT     = 163;
   private static final int    MASS       = 1;
   private static final double MOVE_FORCE = 3;
   private static final double JUMP_FORCE = 10;
   private final  SoundPlayer jump;
   private       int         score;

   public Player(int x, int y)
   throws IOException
   {
      super(x, y, WIDTH / COLS, HEIGHT / ROWS,
            new SpriteSheet(fileName, ROWS, COLS,
                            WIDTH, HEIGHT));
      leftSeq  = new SpriteSequence(new int[] { 18, 19, 20 });
      rightSeq = new SpriteSequence(new int[] { 31, 32, 33 });
      jump     = new SoundPlayer("audio/jump.wav");
   }

   public void move(final Moving direction)
   {
      moveActive = true;
      final SpriteSequence seq;
      final Double         force;
      switch(direction) {
         case LEFT:
            force = -MOVE_FORCE;
            seq   = leftSeq;
            break;
         case RIGHT:
            force = MOVE_FORCE;
            seq   = rightSeq;
            break;
         default:
            force = 0.0;
            seq   = null;
      }
      applyForce(force, 0.0);
      updateSpriteIfReady(seq);
   }

   public void finishedMove()
   {
      moveActive = false;
   }

   public void jump()
   {
      if(getLanded()) {
         applyForce(0.0, JUMP_FORCE);
         jump.play();
      }
   }

   void increaseScore()
   {
      score++;
   }

   public int getScore()
   {
      return score;
   }

   public void resetScore()
   {
      score = 0;
   }

   @Override
   public int getMass()
   {
      return MASS;
   }

   @Override
   public boolean onCollision(final Entity withWhat)
   {
      return false;
   }

   @Override
   public void onSpriteUpdate()
   {
      if(landed && (trajectory.moveDirection() != STILL)) {
         momentumMovementAnimation();
      }
   }

   @Override
   public void onTick()
   {

   }
}