package ac.bris.cs.platformer;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine.Info;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;

import static ac.bris.cs.platformer.Utils.getResourceUrl;
import static javax.sound.sampled.LineEvent.Type;

/**
 * On creation, this class will load a given audio file and cache the
 * data in a byte array, allowing sound to be played repeatedly.
 */
public class SoundPlayer implements LineListener {

   /************************ Class Constants *******************/

   private static final int STREAM_END  = -1;
   private static final int BUFFER_SIZE = 1024;

   private enum Status {
      LOADING,
      PLAYING,
      LOOPING,
      STOPPING,
      FINISHED
   }

   /************************ Instance Variables * *******************/

   private final String           fileName;
   private final URL              fileUrl;
   private       byte[]           data;
   private       AudioInputStream stream;
   private       AudioFormat      format;
   private       Clip             clip;
   private       Info             info;
   private       Status           status = Status.LOADING;

   /************************* Constructor ************************/

   public SoundPlayer(final String fileName)
   {
      this.fileName = fileName;
      fileUrl = getResourceUrl(fileName);
      try {
         initStream();
      } catch(UnsupportedAudioFileException | IOException e) {
         playError(e);
      }
   }

   /************************ Interface Methods *******************/

   public void play()
   {
      if(status != Status.PLAYING) {
         try {
            initClip();
            clip.start();
            status = Status.PLAYING;
         } catch(UnsupportedAudioFileException |
                 IOException                   |
                 LineUnavailableException e) {
            playError(e);
         }
      }
   }

   public void loop()
   {
      if(status != Status.LOOPING) {
         try {
            initClip();
            clip.loop(Clip.LOOP_CONTINUOUSLY);
            status = Status.LOOPING;
         } catch(UnsupportedAudioFileException |
                 IOException                   |
                 LineUnavailableException e) {
            playError(e);
         }
      }
   }

   public void stop()
   {
      if(status == Status.LOOPING) {
         status = Status.STOPPING;
         clip.stop();
         finish();
      }
   }

   @Override
   public void update(final LineEvent event)
   {
      final Type type = event.getType();
      if(type == Type.STOP) {
         status = Status.FINISHED;
         finish();
      } else if(status == Status.STOPPING) {
         status = Status.FINISHED;
      }

   }
   /***************** Initialisation and loading *******************/

   private void initClip()
   throws IOException,
          UnsupportedAudioFileException,
          LineUnavailableException
   {
      initStream();
      clip = (Clip)AudioSystem.getLine(info);
      clip.addLineListener(this);
      clip.open(stream);
   }

   private void initStream()
   throws IOException,
          UnsupportedAudioFileException
   {
      if(stream == null) {
         stream = AudioSystem.getAudioInputStream(fileUrl);
         info   = new Info(Clip.class, format);
         format = stream.getFormat();
         data   = getAudioBytes();
      } else {
         stream = reloadAudioBytes();
      }
   }

   private byte[] getAudioBytes()
   throws IOException
   {
      try(final ByteArrayOutputStream out = new ByteArrayOutputStream()) {
         final byte[] buffer = new byte[BUFFER_SIZE];
         int i;
         do {
            i = stream.read(buffer, 0, buffer.length);
            if (i > 0) {
               out.write(buffer, 0, i);
            }
         } while (i != STREAM_END);
         return out.toByteArray();
      }
   }

   private AudioInputStream reloadAudioBytes()
   {
      final ByteArrayInputStream in = new ByteArrayInputStream(data);
      final int size = data.length / format.getFrameSize();
      return new AudioInputStream(in, format, size);
   }

   private void playError(final Exception e) {
      if(e instanceof IOException) {
         error("Cannot play '" + fileName + "' - IO error...", e);
      } else if(e instanceof LineUnavailableException) {
         error("Cannot play '" + fileName + "' as line is unavailable", e);
      } else if(e instanceof UnsupportedAudioFileException) {
         error("Cannot play'" + fileName + "' as file type is not supported", e);
      } else if(e instanceof URISyntaxException) {
         error("Cannot play'" + fileName + "' as file type is not supported", e);
      }
   }

   private void error(final String m, final Exception e)
   {
      System.out.println(m);
      if(Main.DEBUG) {
         e.printStackTrace();
      }
      finish();
   }

   private void finish()
   {
      try {
         if(clip != null) {
            clip.close();
         }
         if(stream != null) {
            stream.close();
         }
      } catch(final IOException e) {
         System.out.println("Error closing audiostream...");
      }
   }
}
