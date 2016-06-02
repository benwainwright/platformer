package ac.bris.cs.platformer;

/**
 * Main class
 *
 * Doesn't do very much at this point except for bootstrapping the
 * Window class.
 */

final class Main {

   static final boolean DEBUG = true;

   private Main() { /* NOOP */ }

   public static void main(final String[] args)
   {
      new Window();
   }
}
