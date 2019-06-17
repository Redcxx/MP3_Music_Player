import java.io.IOException;
import javax.swing.JPanel;
import javax.swing.JLabel;

//NOTE: UPDATE THIS WHEN MAIN FEATURE FINISHED

public class SongPlayer extends MP3Player {

  private Song currentSong;
  private final JPanel gui;
  private final JLabel songInfo, timeElapsed;
  private final Thread guiUpdate;
  private final String default_current_song_info_string =
                                      "---------------------------------------";


  public SongPlayer() {
    super();
    gui = new JPanel();
    songInfo = new JLabel(default_current_song_info_string);
    timeElapsed = new JLabel("0/0");

    guiUpdate = new Thread(new Runnable() {
      @Override
      public void run() {
        //set the current time and end time and start time
        //while currenttime < endtime
        //currenttime = System.getcurrenttimemillis
        //if (endtime-starttime).to min sec != current display
        //  timeElapsed setText min/sec

      }//run
    });
  }//SongPlayer

  public void play(Song song) throws IOException {
    currentSong = song;
    super.play(song.getURL());

    //start a new update gui thread

  }

  @Override
  public void stop() {
    super.stop();
    //end the updateing thread
  }

  public JPanel getGui() {
    return this.gui;
  }


}//class SongPlayer
