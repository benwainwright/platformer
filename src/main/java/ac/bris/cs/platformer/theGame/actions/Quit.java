
package ac.bris.cs.platformer.theGame.actions;

import ac.bris.cs.platformer.theGame.World;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
 * Created by bw12954 on 02/06/16.
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