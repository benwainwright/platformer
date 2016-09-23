package ac.bris.cs.platformer.theGame.actions;

import ac.bris.cs.platformer.theGame.World;
import ac.bris.cs.platformer.theGame.entities.Player;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
 * Action object bound to the spacebar
 */
public class Jump extends AbstractAction {

   private World world;

   public Jump(World world)
   {
      this.world = world;
   }

   @Override
   public void actionPerformed(ActionEvent e)
   {
      final Player player = world.getPlayer();
      player.jump();
   }
}
