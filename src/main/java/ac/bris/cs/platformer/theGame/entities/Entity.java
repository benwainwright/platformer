package ac.bris.cs.platformer.theGame.entities;

import ac.bris.cs.platformer.Utils;
import ac.bris.cs.platformer.theGame.PositionRelationship;
import ac.bris.cs.platformer.theGame.PositionRelationship.Type;
import ac.bris.cs.platformer.theGame.SpriteSequence;
import ac.bris.cs.platformer.theGame.SpriteSheet;
import ac.bris.cs.platformer.theGame.movement.Move;
import ac.bris.cs.platformer.theGame.movement.Trajectory;
import ac.bris.cs.platformer.theGame.movement.Velocity.Moving;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static ac.bris.cs.platformer.Utils.downScaleInt;
import static ac.bris.cs.platformer.theGame.PositionRelationship.Side;
import static ac.bris.cs.platformer.theGame.PositionRelationship.compare;
import static ac.bris.cs.platformer.theGame.movement.Velocity.Moving.LEFT;
import static ac.bris.cs.platformer.theGame.movement.Velocity.Moving.RIGHT;

/**
 * Base class for all game entities. Initialises and keeps track of
 * entity dimensions and trajectory, as well as containing a number of
 * functions which are called by the game loop in order to update state
 * and draw.
 *
 * As well as this, Entity stipulates some abstract methods that
 * subclasses must implement, as they are called during the game loop at
 * several points. This allows me to specify specific logic to be
 * executed by a specific type of entity at particular points.
 *
 * This class implements comparable, in order that entities can be
 * sorted by Z Position, allowing entities with a higher Z position to
 * be drawn on top of lower Z position entities
 */
public abstract class Entity implements Comparable<Entity> {

   /********************* Class Constants *******************/

   private static final int EQUAL     = 0;
   private static final int LESS_THAN = -1;
   private static final int MORE_THAN = 1;

   /************************ Instance Variables *****************/

   private final SpriteSheet   sprites;
   private final Dimension     dimensions;
   private       boolean       readyForSpriteUpdate;
   private       int           zPosition;
   private       int           spriteNum;
   private       BufferedImage image;
   private final Point         location;

   final Trajectory    trajectory;
   SpriteSequence      leftSeq;
   SpriteSequence      rightSeq;
   boolean             landed;
   boolean             moveActive;
   boolean             alive = true;

   /************************* Constructors ************************/

   protected Entity(final int x, final int y, final int w, final int h,
                    final SpriteSheet sprites)
   {
      location     = new Point(x, y);
      dimensions   = new Dimension(w, h);
      trajectory   = new Trajectory(this);
      this.sprites = sprites;
   }

   protected Entity(final int x, final int y, final int w, final int h,
                    final int zPosition, final SpriteSheet sprites)
   {
      this(x, y, w, h, sprites);
      this.zPosition = zPosition;
   }

   protected Entity(final int x, final int y, final int w, final int h)
   {
      this(x, y, w, h, (SpriteSheet) null);
   }

   protected Entity(final int x, final int y, final int w,
                    final int h, final int zPosition)
   {
      this(x, y, w, h, zPosition, null);
   }

   protected Entity(final int x, final int y, final BufferedImage image)
   {
      this(x, y, 0, image);
   }

   protected Entity(final int x, final int y, final double scale,
                    final String imageFile)
   throws IOException
   {
      this(x, y, 0, scale, imageFile);
   }

   protected Entity(final int x, final int y, final int zPosition,
                    final double scale, final String imageFile)
   throws IOException
   {
      this(downScaleInt(x, scale), downScaleInt(y, scale), zPosition,
           loadAndScaleImage(imageFile, scale));
   }

   protected Entity(final int x, final int y, final int zPosition,
                    final BufferedImage image)
   {
      this(x, y, image.getWidth(), image.getHeight(), zPosition, null);
      this.image = image;
   }



   /************************ Interface Methods *******************/

   public void updateState(final EntityList entities, final double friction,
                           final double gravity, final Dimension screenSize)
   {
      onTick();
      checkIfIveLanded(entities);
      if (landed) {
         trajectory.drag(friction);
      } else {
         applyForce(0.0, -gravity);
         trajectory.drag(0.0);
      }
      if (isOffScreen(screenSize)) {
         alive = false;
      } else {
         doMoveIfValid(entities);
         readyForSpriteUpdate = true;
      }
   }

   public void paintMe(final Graphics g, final int screenHeight)
   {
      final int           x        = (int)location.getX();
      final int           y        = (int)location.getY();
      final int           height   = (int)dimensions.getHeight();
      final int           reversed = screenHeight - y - height;
      final BufferedImage draw;
      draw = (sprites != null)? sprites.get(spriteNum) : image;
      g.drawImage(draw, x, reversed, null);
   }

   public void doMove(final int x, final int y)
   {
      location.move(x, y);
   }

   void applyForce(final double x, final double y)
   {
      trajectory.applyForce(x, y);
   }

   void moveY(final int y)
   {
      final int x = (int) location.getX();
      doMove(x, y);
   }

   void updateSpriteIfReady(final SpriteSequence sequence)
   {
      if (readyForSpriteUpdate && (sprites != null) && (sequence != null)) {
         spriteNum = sequence.nextIndex();
         readyForSpriteUpdate = false;
      }
   }

   void momentumMovementAnimation()
   {
      if (!moveActive) {
         if (moveDirection() == LEFT) {
            updateSpriteIfReady(leftSeq);
         } else if (moveDirection() == RIGHT) {
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

   @Override
   public int compareTo(final Entity entity)
   {
      if (zPosition == entity.zPosition) {
         return EQUAL;
      } else if (zPosition > entity.zPosition) {
         return MORE_THAN;
      } else {
         return LESS_THAN;
      }
   }

   public int left()
   {
      return (int)location.getX();
   }

   public int right()
   {
      return (int)((location.getX() + dimensions.getWidth()) - 1);
   }

   public int top()
   {
      return (int)((location.getY() + dimensions.getHeight()) - 1);
   }

   public int bottom()
   {
      return (int)location.getY();
   }
   boolean hasLanded()
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

   public boolean isAlive()
   {
      return alive;
   }

   Trajectory getTrajectory()
   {
      return trajectory;
   }

   /*************** Collision Detection and Movement*******************/

   private void checkIfIveLanded(final EntityList entities)
   {
      boolean hasLanded = false;
      for (final Entity other : entities) {
         if (!Objects.equals(other, this)) {
            final PositionRelationship pr = compare(other, this);
            if ((pr.type == Type.EDGE) && pr.sides.contains(Side.TOP)) {
               hasLanded = true;
            }
         }
      }
      landed = hasLanded;
   }

   private void doMoveIfValid(final EntityList entities)
   {
      final Move move = trajectory.getMove();
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

   private Moving moveDirection()
   {
      return trajectory.moveDirection();
   }

   private boolean isOffScreen(final Dimension screenSize)
   {
      return (location.getX() < 0)                     ||
             (location.getX() > screenSize.getWidth()) ||
             (location.getY() < 0)                     ||
             (location.getY() > screenSize.getHeight());
   }

   private static BufferedImage loadAndScaleImage(final String fileName,
                                                 final double scale)
   throws IOException
   {
      final BufferedImage image     = Utils.loadImage(fileName);
      final double        newWidth  = image.getWidth() / scale;
      final double        newHeight = image.getHeight() / scale;
      return Scalr.resize(image, Method.QUALITY, (int)newWidth,
                                                 (int)newHeight);
   }

   /************************ Abstract methods **********************/

   public abstract void onTick();

   protected abstract void onSpriteUpdate();

   public abstract boolean onCollision(Entity withWhat);

   public abstract int getMass();
}
