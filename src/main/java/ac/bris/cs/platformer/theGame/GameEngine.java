package ac.bris.cs.platformer.theGame;

import ac.bris.cs.platformer.Window;
import ac.bris.cs.platformer.theGame.entities.Entity;
import ac.bris.cs.platformer.theGame.entities.EntityList;
import ac.bris.cs.platformer.theGame.entities.Player;

import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;
import java.util.Collection;

import static ac.bris.cs.platformer.theGame.GameEngine.RenderType.ACTIVE;
import static ac.bris.cs.platformer.theGame.GameEngine.RenderType.PASSIVE;

/**
 * This class is responsible for handling maintaining and updating the
 * game state, and then drawing the graphics to screen.
 *
 * If the host system is capable of running in 'Full-Screen Exclusive
 * mode', active rendering on a separate thread is performed. If not,
 * graphics are rendered using Swing Timers in the usual fashion
 */
class GameEngine extends Thread {

   /******************* Class Constants ***********************/

   private static final String THREAD_NAME    = "Game Engine";
   private static final double GRAVITY        = 0.3;
   private static final double FRICTION       = 0.1;
   private static final int    FPS            = 60;
   private static final int    ANIMATION_FPS  = 15;
   private static final int    MILL_IN_SEC    = 1000;
   private static final int    GRAPHICS_TICK  = MILL_IN_SEC / FPS;
   private static final int    ANIMATION_TICK = MILL_IN_SEC / ANIMATION_FPS;
   private static final int    RESET_TIME     = 1000;

   /******************* Instance Variables *******************/

   private final    RenderType     type;
   private final    BufferStrategy strategy;
   private          Timer          render;
   private          Timer          animate;
   private volatile boolean        threadRunning = true;
   private volatile boolean        resetLevel;
   private volatile World          world;
   private volatile EntityList     entities;
   private volatile Window         window;

   enum RenderType {
      ACTIVE,
      PASSIVE
   }

   /************************ Constructors *******************/

   GameEngine(final World world, final EntityList entities,
              final RenderType type, final Window window,
              final BufferStrategy strategy)
   {
      super(THREAD_NAME);
      this.world    = world;
      this.type     = type;
      this.entities = entities;
      this.strategy = strategy;
      this.window   = window;
      if (this.type == ACTIVE) {
         startActiveRender();
      }
      initTimers();
   }

   GameEngine(final World world, final EntityList entities,
              final Window window, final RenderType type)
   {
      this(world, entities, type, window, null);
   }

   /************************ Initialisation ********************/

   private void startActiveRender()
   {
      world.setIgnoreRepaint(true);
      start();
   }

   private void initTimers()
   {
      if (type == PASSIVE) {
         render = new Timer(GRAPHICS_TICK, this::gameTick);
         render.start();
      }
      animate = new Timer(ANIMATION_TICK, this::updateAnimation);
      animate.start();
   }

   private void stopTimers()
   {
      if(render != null) {
         render.stop();
      }
      animate.stop();
   }

   /************************ Interface Methods *******************/

   void killThread()
   {
      threadRunning = false;
   }

   private void resetWithMessage(final String message)
   {
      world.message(message);
      final Timer reset = new Timer(RESET_TIME, this::doReset);
      reset.setRepeats(false);
      reset.start();
   }

   private void doReset(ActionEvent e)
   {
      resetLevel = true;
   }

   /******************** The Game Loop ********************/

   @Override
   public void run()
   {
      while (threadRunning) {
         if(resetLevel) {
            stopTimers();
            world.resetLevel();
            initTimers();
            resetLevel = false;
         } else {
            gameTick(null);
         }
      }
      world.quit();
   }

   /******************* Game Dynamics and Animation ********************/

   private void updateState()
   {
      final int                initDead   = entities.size();
      final Collection<Entity> dead       = new ArrayList<>(initDead);
      final Player             player     = world.getPlayer();
      final Dimension          screenSize = world.getPreferredSize();
      for (final Entity entity : entities) {
         entity.updateState(entities, FRICTION, GRAVITY, screenSize);
         if (!entity.isAlive()) {
            dead.add(entity);
         }
      }
      if (world.hasWon()) {
         resetWithMessage("Congratulations! You won...");
      }
      if (!player.isAlive()) {
         resetWithMessage("Oh dear, you died...");
      }
      entities.removeAll(dead);
   }

   private void gameTick(ActionEvent e)
   {
      updateState();
      if (type == ACTIVE) {
         activeRenderFrame();
      } else {
         world.repaint();
         world.revalidate();
      }
   }

   private void activeRenderFrame()
   {
      final Graphics2D graphicsContext = (Graphics2D)strategy.getDrawGraphics();
      world.paintComponent(graphicsContext);
      strategy.show();
      Toolkit.getDefaultToolkit().sync();
      graphicsContext.dispose();
      sleepUntilEndOfFrame();
   }

   private void sleepUntilEndOfFrame()
   {
      try {
         final long used = System.currentTimeMillis() % GRAPHICS_TICK;
         final long left = GRAPHICS_TICK - used;
         Thread.sleep(left);
      } catch (final InterruptedException e) {
         final String m = e.getMessage();
         window.fatalError(m);
      }
   }

   private void updateAnimation(final ActionEvent e)
   {
      for(final Entity entity : entities) {
         entity.setReadyForSpriteUpdate();
         entity.onAnimationTick();
      }
   }
}
