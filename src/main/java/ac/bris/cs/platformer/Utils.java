package ac.bris.cs.platformer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

/**
 * Utility class for static methods that don't really have a natural
 * home anywhere else
 */

@SuppressWarnings("StringConcatenationMissingWhitespace")
public final class Utils {

   private static final String GRAPHICS_LOCATION = "graphics";
   private Utils() { /* NOOP */ }

   /********************* Public Methods  *******************/

   public static BufferedImage loadImage(final String fileName)
   throws IOException
   {
      final String newFileName = GRAPHICS_LOCATION +
                                 File.separator    +
                                 fileName;
      return ImageIO.read(getResourceUrl(newFileName));
   }

   public static int downScaleInt(final int num, final double fact)
   {
      return (int)(num / fact);
   }

   /********************* Package Methods  *******************/

   static URL getResourceUrl(final String fileName)
   {
      final ClassLoader loader = Utils.class.getClassLoader();
      return loader.getResource(fileName);
   }


}
