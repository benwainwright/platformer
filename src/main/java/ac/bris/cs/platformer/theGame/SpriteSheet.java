package ac.bris.cs.platformer.theGame;

import ac.bris.cs.platformer.Utils;

import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Responsible for loading and segmenting sprite sheets, and then providing
 * access to the individual sprites during runtime
 */
public class SpriteSheet {

   private final BufferedImage[] sprites;
   private final int             rows;
   private final int             cols;
   private final int             width;
   private final int             height;

   public SpriteSheet(final String fileName,
                      final int rows,  final int cols,
                      final int width, final int height)
   throws IOException
   {
      final BufferedImage image = Utils.loadImage(fileName);
      this.rows    = rows;
      this.cols    = cols;
      this.width   = width;
      this.height  = height;
      sprites      = loadSprites(image);
   }

   @SuppressWarnings("MethodWithMultipleLoops")
   private BufferedImage[] loadSprites(final BufferedImage image)
   {
      final BufferedImage[] sprites      = new BufferedImage[rows * cols];
      final int             spriteWidth  = width / cols;
      final int             spriteHeight = height / rows;
      for(int i = 0; i < rows; i++) {
         for(int ii = 0; ii < cols; ii++) {
            sprites[(i * cols) + ii] = image.getSubimage(ii * spriteWidth,
                                                         i * spriteHeight,
                                                         spriteWidth,
                                                         spriteHeight);
         }
      }
      return sprites;
   }

   public BufferedImage get(final int i)
   {
      return sprites[i];
   }

   public BufferedImage get()
   {
      return sprites[0];
   }
}
