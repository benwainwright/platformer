package ac.bris.cs.platformer.theGame.actions;

import ac.bris.cs.platformer.theGame.World;
import ac.bris.cs.platformer.theGame.entities.Player;
import ac.bris.cs.platformer.theGame.movement.Velocity;

import javax.swing.AbstractAction;
import java.awt.event.ActionEvent;

/**
 * Created by bw12954 on 02/06/16.
 */
public class MoveLeft extends AbstractAction {

   private World world;

   public MoveLeft(World world)
   {
      this.world = world;
   }

   @Override
   public void actionPerformed(ActionEvent e)
   {
      final Player player = world.getPlayer();
      player.move(Velocity.Moving.LEFT);
   }
}