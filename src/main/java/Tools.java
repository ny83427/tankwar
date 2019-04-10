import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

class Tools {
    private static final Random RANDOM = new Random();

    static boolean nextBoolean() {
        return RANDOM.nextBoolean();
    }

    /**
     * generate a random number less than giving number
     * @param endExclusive  exclusive maximum number
     */
    static int nextInt(final int endExclusive) {
        return RANDOM.nextInt(endExclusive);
    }

    /**
     * generate a random number within the giving two numbers
     * @param beginInclusive inclusive minimum number
     * @param endExclusive  exclusive maximum number
     */
    static int nextInt(final int beginInclusive, final int endExclusive) {
        return beginInclusive + RANDOM.nextInt(endExclusive - beginInclusive);
    }

    /**
     * Play an audio file located under directory "assets/audios"
     */
    static synchronized void playAudio(final String audioFile) {
        File file = new File("assets/audios/" + audioFile);
        Media media = new Media(file.toURI().toString());
        new MediaPlayer(media).play();
    }

    /**
     * Get {@link Image} from given file
     * @param imageFileName name of image file under directory "assets/images"
     */
    static Image getImage(final String imageFileName) {
        return new ImageIcon("assets/images/" + imageFileName).getImage();
    }

    static void sleepSilently(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            // -> Ignore
        }
    }

    /**
     * Set Swing Theme: Windows or Nimbus
     */
    static void setTheme() {
        String theme = System.getProperty("os.name").startsWith("Windows") ? "Windows" : "Nimbus";
        try {
            for (UIManager.LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
                if (theme.equals(info.getName())) {
                    UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (Exception ex) {
            Logger.getLogger(Tools.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
