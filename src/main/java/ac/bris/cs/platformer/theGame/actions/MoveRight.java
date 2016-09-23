package ac.bris.cs.platformer.theGame.actions;

import ac.bris.cs.platformer.theGame.World;
import ac.bris.cs.platformer.theGame.entities.Player;
import ac.bris.cs.platformer.theGame.movement.Velocity;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
 * Action object bound to the right arrow key
 */
public class MoveRight extends AbstractAction {

   private World world;

   public MoveRight(World world)
   {
      this.world = world;
   }

   @Override
   public void actionPerformed(ActionEvent e)
   {
      final Player player = world.getPlayer();
      player.move(Velocity.Moving.RIGHT);
   }
}