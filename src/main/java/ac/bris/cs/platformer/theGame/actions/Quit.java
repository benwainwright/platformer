
package ac.bris.cs.platformer.theGame.actions;

import ac.bris.cs.platformer.theGame.World;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
 * Action object bound to the escape key
 */
public class Quit extends AbstractAction {

   private World world;

   public Quit(World world)
   {
      this.world = world;
   }

   @Override
   public void actionPerformed(ActionEvent e)
   {
      world.endGame();
   }
}