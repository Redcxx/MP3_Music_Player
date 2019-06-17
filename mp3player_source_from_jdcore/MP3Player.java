import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;
import javazoom.jl.player.advanced.AdvancedPlayer;
import javazoom.jl.player.advanced.PlaybackListener;












































public class MP3Player
{
  private static Thread thread;
  private static AudioDevice device;
  private static AdvancedPlayer player;
  private PlaybackListener playbackListener;
  private InputStream input;
  
  public MP3Player()
  {
    playbackListener = new PlayListener();
  }
  











  public void play(String paramString)
    throws IOException
  {
    input = new URL(paramString).openStream();
    
    stop();
    
    thread = new Thread(new Runnable()
    {

      public void run()
      {
        try
        {
          if (MP3Player.thread == Thread.currentThread())
          {
            MP3Player.access$102(FactoryRegistry.systemRegistry().createAudioDevice());
            MP3Player.access$202(new AdvancedPlayer(input, MP3Player.device));
            
            if (playbackListener != null)
            {
              MP3Player.player.setPlayBackListener(playbackListener);
            }
            
            MP3Player.player.play();
          }
          

        }
        catch (Exception localException) {}
      }
    });
    thread.start();
  }
  





  public void stop()
  {
    if (player != null)
    {
      player.stop();
      player = null;
    }
    thread = null;
  }
  




  public void setPlaybackListener(PlayFinishedListener paramPlayFinishedListener)
  {
    playbackListener = new PlayListener(paramPlayFinishedListener);
  }
}
