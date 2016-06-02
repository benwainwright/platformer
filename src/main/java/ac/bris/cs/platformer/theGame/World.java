package ac.bris.cs.platformer.theGame;

import ac.bris.cs.platformer.SoundPlayer;
import ac.bris.cs.platformer.Utils;
import ac.bris.cs.platformer.Window;
import ac.bris.cs.platformer.theGame.entities.Entity;
import ac.bris.cs.platformer.theGame.entities.EntityList;
import ac.bris.cs.platformer.theGame.entities.Player;
import org.imgscalr.Scalr;
import org.imgscalr.Scalr.Method;
import org.imgscalr.Scalr.Mode;

import javax.swing.JPanel;
import javax.swing.Timer;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static ac.bris.cs.platformer.theGame.GameEngine.RenderType.ACTIVE;
import static ac.bris.cs.platformer.theGame.GameEngine.RenderType.PASSIVE;
import static java.awt.Font.PLAIN;

/**
 * Superclass for game levels that sets up the game environment,
 * initialises the game engine acts as the canvas which graphics are
 * drawn on, providing the logic to draw all the major components
 */
public abstract class World extends JPanel {

   /********************** Class Constants *******************/

   private static final int    SCORE_X_POS       = 40;
   private static final int    SCORE_Y_POS       = 40;
   private static final int    SCORE_FONT_SIZE   = 20;
   private static final int    MESSAGE_FONT_SIZE = 25;
   private static final int    MESSAGE_TIME      = 4000;
   private static final String BACKGROUND_FILE   = "background.jpg";

   /******************* Instance Variables *******************/

   private final      Window        parent;
   protected volatile Player        player;
   private            BufferedImage background;
   private            int           backgroundYPosAdjust;
   private            Timer         messageTimer;
   private            String        message;
   private            GameEngine    engine;

   final  EntityList  entities;
   double scaleFactor;

   /*********************** Constructor *******************/

   World(final Window parent)
   throws IOException
   {
      entities = new EntityList();
      this.parent = parent;
      loadBackground();
      loadSoundtrack();
   }

   /******************** Interface Methods *******************/

   public void startEngine(final BufferStrategy strategy)
   {
      engine = new GameEngine(this, entities, ACTIVE, parent, strategy);
   }

   public void startEngine()
   {
      engine = new GameEngine(this, entities, parent, PASSIVE);
   }

   public void endGame()
   {
      engine.killThread();
   }

   public Player getPlayer()
   {
      return player;
   }

   void quit()
   {
      parent.exit();
   }

   void message(final String message)
   {
      if (this.message == null) {
         this.message = message;
         messageTimer = new Timer(MESSAGE_TIME, this::killMessage);
         messageTimer.start();
      }
   }

   @Override
   public void paintComponent(final Graphics g)
   {
      super.paintComponent(g);
      final Dimension size = getPreferredSize();
      g.drawImage(background, 0, -backgroundYPosAdjust, null);
      for (Entity e : entities) {
         final int winHeight = (int) size.getHeight();
         e.paintMe(g, winHeight);
      }
      drawScore(g);
      drawMessage(g);
   }

   @Override
   public Dimension getPreferredSize()
   {
      return Toolkit.getDefaultToolkit().getScreenSize();
   }

   /***************** Abstract Methods **********************/

   protected abstract String soundtrackFilename();

   protected abstract void resetLevel();

   protected abstract boolean hasWon();

   /*********** Initialization and Drawing ****************/

   private void loadBackground()
   throws IOException
   {
      background               = Utils.loadImage(BACKGROUND_FILE);
      final Dimension size     = getPreferredSize();
      final double    oldWidth = background.getWidth();
      final int       winWidth = (int) size.getWidth();
      background               = Scalr.resize(background,
                                              Method.QUALITY,
                                              Mode.FIT_TO_WIDTH,
                                              winWidth);
      final double newWidth = background.getWidth();
      scaleFactor           = oldWidth / newWidth;
      final int difference  = (int) (background.getHeight(null) -
                                     size.getHeight());
      if (difference > 0) {
         backgroundYPosAdjust = difference;
      }
   }

   private void loadSoundtrack()
   {
      final String file            = soundtrackFilename();
      final SoundPlayer soundtrack = new SoundPlayer(file);
      soundtrack.loop();
   }

   private void drawScore(final Graphics g)
   {
      final Font   font  = new Font("SansSerif", Font.BOLD, SCORE_FONT_SIZE);
      final String score = Integer.toString(player.getScore());
      g.setFont(font);
      g.drawString("Score: " + score, SCORE_X_POS, SCORE_Y_POS);
   }

   private void drawMessage(final Graphics g)
   {
      if (message != null) {
         final Font font = new Font("SansSerif", PLAIN, MESSAGE_FONT_SIZE);
         final FontMetrics metrics      = g.getFontMetrics();
         final int         stringWidth  = metrics.stringWidth(message);
         final int         ascent       = metrics.getAscent();
         final int         width        = (int)getPreferredSize().getWidth();
         final int         height       = (int)getPreferredSize().getHeight();
         final int         x            = (width / 2) - (stringWidth / 2);
         final int         y            = (height / 2) - (ascent / 2);
         g.setFont(font);
         g.drawString(message, x, y);
      }
   }

   private void killMessage(final ActionEvent e)
   {
      message = null;
      messageTimer.stop();
   }
}

