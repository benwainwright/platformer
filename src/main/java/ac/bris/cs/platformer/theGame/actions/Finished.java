package ac.bris.cs.platformer.theGame.actions;

import ac.bris.cs.platformer.theGame.World;
import ac.bris.cs.platformer.theGame.entities.Player;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
 * Action object fired when either the left, right, escape keys or the
 * space bar is pressed
 */
public class Finished extends AbstractAction {

   private World world;

   public Finished(final World world)
   {
      this.world = world;
   }

   @Override
   public void actionPerformed(final ActionEvent e)
   {
      final Player player = world.getPlayer();
      player.finishedMove();
   }
}