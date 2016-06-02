package ac.bris.cs.platformer.theGame.entities;

import ac.bris.cs.platformer.Utils;
import ac.bris.cs.platformer.theGame.PositionRelationship;
import ac.bris.cs.platformer.theGame.PositionRelationship.Type;
import ac.bris.cs.platformer.theGame.SpriteSequence;
import ac.bris.cs.platformer.theGame.SpriteSheet;
import ac.bris.cs.platformer.theGame.movement.Move;
import ac.bris.cs.platformer.theGame.movement.Trajectory;
import ac.bris.cs.platformer.theGame.movement.Velocity;
import ac.bris.cs.platformer.theGame.movement.Velocity.Moving;
import org.imgscalr.Scalr;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static ac.bris.cs.platformer.Utils.downScaleInt;
import static ac.bris.cs.platformer.theGame.PositionRelationship.Side;
import static ac.bris.cs.platformer.theGame.PositionRelationship.compare;

/**
 * This is the base class for all game entities
 */
public abstract class Entity implements Comparable<Entity> {

   /****
    * Private attributes
    ****/

   private final SpriteSheet sprites;
   private final Dimension   dimensions;
   private       boolean     readyForSpriteUpdate;

   /*****
    * Package local attributes
    ****/

   boolean moveActive;

   /*****
    * Protected attributes
    *****/

   protected       int            sprite;
   protected       boolean        landed;
   protected       BufferedImage  image;
   protected final Trajectory     trajectory;
   protected final Point          location;
   protected       int            zPosition;
   protected       SpriteSequence leftSeq;
   protected       SpriteSequence rightSeq;
   protected boolean alive = true;

   /*****
    * Constants
    *****/

   private static final int EQUAL     = 0;
   private static final int LESS_THAN = -1;
   private static final int MORE_THAN = 1;

   /****
    * Constructors
    ****/

   public Entity(int x, int y, int w, int h, SpriteSheet sprites)
   {
      location = new Point(x, y);
      dimensions = new Dimension(w, h);
      trajectory = new Trajectory(this);
      this.sprites = sprites;
   }

   public Entity(int x, int y, int w,
                 int h, int zPosition, SpriteSheet sprites)
   {
      this(x, y, w, h, sprites);
      this.zPosition = zPosition;
   }

   public Entity(int x, int y, int w, int h)
   {
      this(x, y, w, h, (SpriteSheet) null);
   }

   public Entity(int x, int y, int w, int h, int zPosition)
   {
      this(x, y, w, h, zPosition, null);
   }

   public Entity(int x, int y, BufferedImage image)
   {
      this(x, y, 0, image);
   }

   public Entity(int x, int y, double scale, String imageFile)
   throws IOException
   {
      this(x, y, 0, scale, imageFile);
   }

   public Entity(int x, int y, int zPosition, double scale, String
      imageFile)
   throws IOException
   {
      this(downScaleInt(x, scale), downScaleInt(y, scale), zPosition,
           loadAndScaleImage(imageFile, scale));
   }

   public Entity(int x, int y, int zPosition, BufferedImage image)
   {
      this(x, y, image.getWidth(), image.getHeight(), zPosition, null);
      this.image = image;
   }

   public static BufferedImage loadAndScaleImage(final String fileName,
                                                 final double scale)
   throws IOException
   {
      final BufferedImage image     = Utils.loadImage(fileName);
      final double        newWidth  = image.getWidth() / scale;
      final double        newHeight = image.getHeight() / scale;
      return Scalr.resize(image, Scalr.Method.QUALITY,
                          (int)newWidth, (int)newHeight);
   }

   public Move getMove()
   {
      return trajectory.getMove();
   }

   public void doMove(int x, int y)
   {
      location.move(x, y);
   }

   public void checkIfIveLanded(final EntityList entities)
   {
      boolean landed = false;
      for (final Entity other : entities) {
         if (other != this) {
            PositionRelationship pr = compare(other, this);
            if ((pr.type == Type.EDGE) && pr.sides.contains(Side.TOP)) {
               landed = true;
            }
         }
      }
      this.landed = landed;
   }

   void applyForce(final double x,
                   final double y)
   {
      trajectory.applyForce(x, y);
   }

   private Moving moveDirection()
   {
      return trajectory.moveDirection();
   }

   void moveY(final int y)
   {
      final int x = (int) location.getX();
      doMove(x, y);
   }

   public void updateState(final EntityList entities,
                           final double friction,
                           final double gravity,
                           final Dimension screenSize)
   {
      onTick();
      checkIfIveLanded(entities);
      if (landed) {
         setDrag(friction);
      } else {
         applyForce(0.0, -gravity);
         setDrag(0.0);
      }
      if (isOffScreen(screenSize)) {
         alive = false;
      } else {
         doMoveIfValid(entities);
         readyForSpriteUpdate = true;
      }
   }

   private boolean isOffScreen(Dimension screenSize)
   {
      if (location.getX() < 0 ||
          location.getX() > screenSize.getWidth() ||
          location.getY() < 0 ||
          location.getY() > screenSize.getHeight()) {
         return true;
      } else {
         return false;
      }
   }

   private void doMoveIfValid(final EntityList entities)
   {
      final Move move = getMove();
      if (move != null) {
         boolean doMove = true;
         for (final Entity checking : entities) {
            if (!Objects.equals(this, checking) &&
                move.doesCollide(checking) &&
                !checking.onCollision(this)) {
               doMove = false;
            }
         }
         if (doMove) {
            move.execute();
         }
      }
   }

   /*****
    * Rendering
    *****/

   public void paintMe(Graphics g, int height)
   {
      int           reversedHeight =
         height - (int) location.getY() - (int) dimensions.getHeight();
      BufferedImage image          =
         this.image == null? sprites.get(sprite) : this.image;
      g.drawImage(image, (int) location.getX(), reversedHeight, null);
   }

   void updateSpriteIfReady(SpriteSequence sequence)
   {
      if (readyForSpriteUpdate &&
          sprites != null &&
          sequence != null) {
         sprite = sequence.nextIndex();
         readyForSpriteUpdate = false;
      }
   }

   void momentumMovementAnimation()
   {
      if (!moveActive) {
         if (moveDirection() == Velocity.Moving.LEFT) {
            updateSpriteIfReady(leftSeq);
         } else if (moveDirection() == Velocity.Moving.RIGHT) {
            updateSpriteIfReady(rightSeq);
         }
      }
   }

   public void onAnimationTick()
   {
      if (readyForSpriteUpdate) {
         onSpriteUpdate();
      }
   }

   /****
    * Dimensions
    ****/

   public int left()
   {
      return (int) location.getX();
   }

   public int right()
   {
      return (int) (location.getX() + dimensions.getWidth() - 1);
   }

   public int top()
   {
      return (int) (location.getY() + dimensions.getHeight() - 1);
   }

   public int bottom()
   {
      return (int) location.getY();
   }


   /****
    * Overrides
    ****/

   @Override
   public int compareTo(Entity entity)
   {
      if (zPosition == entity.getZposition()) {
         return EQUAL;
      } else if (zPosition > entity.getZposition()) {
         return MORE_THAN;
      } else {
         return LESS_THAN;
      }
   }

   /****
    * Setters and getters
    *****/

   public void setDrag(double drag)
   {
      trajectory.drag(drag);
   }

   public boolean getLanded()
   {
      return landed;
   }

   public void setReadyForSpriteUpdate()
   {
      readyForSpriteUpdate = true;
   }

   public Dimension getDimensions()
   {
      return dimensions;
   }

   private int getZposition()
   {
      return zPosition;
   }


   public boolean isAlive()
   {
      return alive;
   }

   public void setAlive(boolean alive)
   {
      this.alive = alive;
   }

   /***** Abstract methods *****/

   public abstract void onTick();

   protected abstract void onSpriteUpdate();

   public abstract boolean onCollision(Entity withWhat);

   public abstract int getMass();
}
