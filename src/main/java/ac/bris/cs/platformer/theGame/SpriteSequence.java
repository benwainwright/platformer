package ac.bris.cs.platformer.theGame;

/**
 * Simple class allowing the index sequences of specific spritesheet
 * animations to be stored in order to besed in loops
 */
public class SpriteSequence {

   /************************ Instance Variables *******************/

   private final int[] sequence;
   private int         position;

   /************************* Constructor ************************/

   public SpriteSequence(final int[] sequence)
   {
      this.sequence = sequence.clone();
   }

   /************************** Interface *************************/

   public int nextIndex()
   {
      final int sprite = sequence[position];
      if(position >= (sequence.length - 1)) {
         position = 0;
      } else {
         position++;
      }
      return sprite;
   }
}
