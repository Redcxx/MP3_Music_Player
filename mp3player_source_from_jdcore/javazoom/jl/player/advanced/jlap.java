package javazoom.jl.player.advanced;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import javazoom.jl.decoder.JavaLayerException;

public class jlap
{
  public jlap() {}
  
  public static void main(String[] paramArrayOfString)
  {
    jlap localJlap = new jlap();
    if (paramArrayOfString.length != 1)
    {
      localJlap.showUsage();
      System.exit(0);
    }
    else
    {
      try
      {
        localJlap.play(paramArrayOfString[0]);
      }
      catch (Exception localException)
      {
        System.err.println(localException.getMessage());
        System.exit(0);
      }
    }
  }
  
  public void play(String paramString)
    throws JavaLayerException, IOException
  {
    InfoListener localInfoListener = new InfoListener();
    playMp3(new File(paramString), localInfoListener);
  }
  
  public void showUsage()
  {
    System.out.println("Usage: jla <filename>");
    System.out.println("");
    System.out.println(" e.g. : java javazoom.jl.player.advanced.jlap localfile.mp3");
  }
  
  public static AdvancedPlayer playMp3(File paramFile, PlaybackListener paramPlaybackListener)
    throws IOException, JavaLayerException
  {
    return playMp3(paramFile, 0, Integer.MAX_VALUE, paramPlaybackListener);
  }
  
  public static AdvancedPlayer playMp3(File paramFile, int paramInt1, int paramInt2, PlaybackListener paramPlaybackListener)
    throws IOException, JavaLayerException
  {
    return playMp3(new BufferedInputStream(new FileInputStream(paramFile)), paramInt1, paramInt2, paramPlaybackListener);
  }
  
  public static AdvancedPlayer playMp3(InputStream paramInputStream, int paramInt1, int paramInt2, PlaybackListener paramPlaybackListener)
    throws JavaLayerException
  {
    AdvancedPlayer localAdvancedPlayer = new AdvancedPlayer(paramInputStream);
    localAdvancedPlayer.setPlayBackListener(paramPlaybackListener);
    new Thread()
    {
      private final AdvancedPlayer val$player;
      private final int val$start;
      private final int val$end;
      
      public void run()
      {
        try
        {
          val$player.play(val$start, val$end);
        }
        catch (Exception localException)
        {
          throw new RuntimeException(localException.getMessage());
        }
      }
    }.start();
    return localAdvancedPlayer;
  }
  
  public class InfoListener
    extends PlaybackListener
  {
    public InfoListener() {}
    
    public void playbackStarted(PlaybackEvent paramPlaybackEvent)
    {
      System.out.println("Play started from frame " + paramPlaybackEvent.getFrame());
    }
    
    public void playbackFinished(PlaybackEvent paramPlaybackEvent)
    {
      System.out.println("Play completed at frame " + paramPlaybackEvent.getFrame());
      System.exit(0);
    }
  }
}
