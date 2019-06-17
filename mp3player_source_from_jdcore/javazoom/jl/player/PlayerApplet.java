package javazoom.jl.player;

import java.applet.Applet;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import javazoom.jl.decoder.JavaLayerException;

public class PlayerApplet
  extends Applet
  implements Runnable
{
  public static final String AUDIO_PARAMETER = "audioURL";
  private Player player = null;
  private Thread playerThread = null;
  private String fileName = null;
  
  public PlayerApplet() {}
  
  protected AudioDevice getAudioDevice()
    throws JavaLayerException
  {
    return FactoryRegistry.systemRegistry().createAudioDevice();
  }
  
  protected InputStream getAudioStream()
  {
    InputStream localInputStream = null;
    try
    {
      URL localURL = getAudioURL();
      if (localURL != null) {
        localInputStream = localURL.openStream();
      }
    }
    catch (IOException localIOException)
    {
      System.err.println(localIOException);
    }
    return localInputStream;
  }
  
  protected String getAudioFileName()
  {
    String str = fileName;
    if (str == null) {
      str = getParameter("audioURL");
    }
    return str;
  }
  
  protected URL getAudioURL()
  {
    String str = getAudioFileName();
    URL localURL = null;
    if (str != null) {
      try
      {
        localURL = new URL(getDocumentBase(), str);
      }
      catch (Exception localException)
      {
        System.err.println(localException);
      }
    }
    return localURL;
  }
  
  public void setFileName(String paramString)
  {
    fileName = paramString;
  }
  
  public String getFileName()
  {
    return fileName;
  }
  
  protected void stopPlayer()
    throws JavaLayerException
  {
    if (player != null)
    {
      player.close();
      player = null;
      playerThread = null;
    }
  }
  
  protected void play(InputStream paramInputStream, AudioDevice paramAudioDevice)
    throws JavaLayerException
  {
    stopPlayer();
    if ((paramInputStream != null) && (paramAudioDevice != null))
    {
      player = new Player(paramInputStream, paramAudioDevice);
      playerThread = createPlayerThread();
      playerThread.start();
    }
  }
  
  protected Thread createPlayerThread()
  {
    return new Thread(this, "Audio player thread");
  }
  
  public void init() {}
  
  public void start()
  {
    String str = getAudioFileName();
    try
    {
      InputStream localInputStream = getAudioStream();
      AudioDevice localAudioDevice = getAudioDevice();
      play(localInputStream, localAudioDevice);
    }
    catch (JavaLayerException localJavaLayerException)
    {
      synchronized (System.err)
      {
        System.err.println("Unable to play " + str);
        localJavaLayerException.printStackTrace(System.err);
      }
    }
  }
  
  public void stop()
  {
    try
    {
      stopPlayer();
    }
    catch (JavaLayerException localJavaLayerException)
    {
      System.err.println(localJavaLayerException);
    }
  }
  
  public void destroy() {}
  
  public void run()
  {
    if (player != null) {
      try
      {
        player.play();
      }
      catch (JavaLayerException localJavaLayerException)
      {
        System.err.println("Problem playing audio: " + localJavaLayerException);
      }
    }
  }
}
