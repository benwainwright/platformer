package ac.bris.cs.platformer;

import ac.bris.cs.platformer.theGame.Level;
import ac.bris.cs.platformer.theGame.actions.Finished;
import ac.bris.cs.platformer.theGame.actions.Jump;
import ac.bris.cs.platformer.theGame.actions.MoveLeft;
import ac.bris.cs.platformer.theGame.actions.MoveRight;
import ac.bris.cs.platformer.theGame.actions.Quit;

import javax.swing.InputMap;
import javax.swing.JFrame;
import javax.swing.KeyStroke;
import javax.swing.SwingUtilities;
import java.awt.GraphicsDevice;
import java.awt.GraphicsEnvironment;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.WindowEvent;
import java.awt.image.BufferStrategy;
import java.io.IOException;

import static java.awt.Frame.MAXIMIZED_BOTH;
import static java.awt.event.WindowEvent.WINDOW_CLOSED;
import static javax.swing.JComponent.WHEN_IN_FOCUSED_WINDOW;

/**
 * Responsible for initialising the Swing EDT, setting up the Window,
 * and cleanly shutting down the program when ready
 */

public class Window {

   /************************ Class Constants *******************/

   private static final int    NUM_BUFFERS     = 2;
   private static final String LEFT_ACTION     = "moveLeft";
   private static final String RIGHT_ACTION    = "moveRight";
   private static final String JUMP_ACTION     = "jump";
   private static final String QUIT_ACTION     = "quit";
   private static final String ACTION_FINISHED = "actionFinished";
   private static final int    SUCCESS_EXIT    = 0;
   private static final int    ERROR_EXIT      = 1;

   /************************ Instance Variables *******************/

   private          JFrame  window;
   private volatile Level   level;
   private          boolean activeGraphics;

   /************************* Constructor ************************/

   public Window()
   {
      SwingUtilities.invokeLater(this::run);
   }

   /************************ Interface Methods *******************/

   public void exit()
   {
      exit(SUCCESS_EXIT);
   }


   public void fatalError(final String m)
   {
      System.out.println(m);
      exit(ERROR_EXIT);
   }

   private void exit(final int errorCode)
   {
      final WindowEvent event = new WindowEvent(window, WINDOW_CLOSED);
      Toolkit.getDefaultToolkit().getSystemEventQueue().postEvent(event);
      window.setVisible(false);
      window.dispose();
      System.exit(errorCode);
   }

   /********************* Window Initialisation  *******************/

   private void run()
   {
      initWindow();
      initLevel();
      initGraphics();
      setInputMap();
      mapActionKeys();
      finishWindow();
   }

   private void initWindow()
   {
      window = new JFrame();
      window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
      window.setLocationByPlatform(true);
   }

   private void initLevel()
   {
      try {
         level = new Level(this);
         window.add(level);
      } catch (IOException e) {
         fatalError("level initialisation failed. Exiting...");
      }
   }

   private void setInputMap()
   {
      final int      ifw = WHEN_IN_FOCUSED_WINDOW;
      final InputMap im  = level.getInputMap(ifw);
      im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0), LEFT_ACTION);
      im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), ACTION_FINISHED);
      im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0), RIGHT_ACTION);
      im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), ACTION_FINISHED);
      im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0), JUMP_ACTION);
      im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, true), ACTION_FINISHED);
      im.put(KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), QUIT_ACTION);
   }

   private void mapActionKeys()
   {
      level.getActionMap().put(LEFT_ACTION, new MoveLeft(level));
      level.getActionMap().put(RIGHT_ACTION, new MoveRight(level));
      level.getActionMap().put(JUMP_ACTION, new Jump(level));
      level.getActionMap().put(QUIT_ACTION, new Quit(level));
      level.getActionMap().put(ACTION_FINISHED, new Finished(level));
   }

   private void initGraphics()
   {
      window.setUndecorated(true);
      final GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
      final GraphicsDevice defaultDevice = ge.getDefaultScreenDevice();
      if (defaultDevice.isFullScreenSupported()) {
         initFullScreenExclusiveMode(defaultDevice);
      } else {
         window.setExtendedState(MAXIMIZED_BOTH);
      }
      window.setResizable(false);
   }

   private void initFullScreenExclusiveMode(final GraphicsDevice gd)
   {
      gd.setFullScreenWindow(window);
      window.setIgnoreRepaint(true);
      window.createBufferStrategy(NUM_BUFFERS);
      activeGraphics = true;
   }

   private void finishWindow()
   {
      window.pack();
      window.setVisible(true);
      if (activeGraphics) {
         final BufferStrategy strategy = window.getBufferStrategy();
         level.startEngine(strategy);
      } else {
         level.startEngine();
      }
   }
}
