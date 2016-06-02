package ac.bris.cs.platformer.theGame;

/**
 * Created by bw12954 on 30/05/2016.
 */
public class SpriteSequence {

   private final int[] sequence;
   private int         position;

   public SpriteSequence(final int[] sequence)
   {
      this.sequence = sequence.clone();
   }

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
