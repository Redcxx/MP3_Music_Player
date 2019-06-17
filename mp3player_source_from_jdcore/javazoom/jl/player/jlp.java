package javazoom.jl.player;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import javazoom.jl.decoder.JavaLayerException;

public class jlp
{
  private String fFilename = null;
  private boolean remote = false;
  
  public static void main(String[] paramArrayOfString)
  {
    int i = 0;
    try
    {
      jlp localJlp = createInstance(paramArrayOfString);
      if (localJlp != null) {
        localJlp.play();
      }
    }
    catch (Exception localException)
    {
      System.err.println(localException);
      localException.printStackTrace(System.err);
      i = 1;
    }
    System.exit(i);
  }
  
  public static jlp createInstance(String[] paramArrayOfString)
  {
    jlp localJlp = new jlp();
    if (!localJlp.parseArgs(paramArrayOfString)) {
      localJlp = null;
    }
    return localJlp;
  }
  
  private jlp() {}
  
  public jlp(String paramString)
  {
    init(paramString);
  }
  
  protected void init(String paramString)
  {
    fFilename = paramString;
  }
  
  protected boolean parseArgs(String[] paramArrayOfString)
  {
    boolean bool = false;
    if (paramArrayOfString.length == 1)
    {
      init(paramArrayOfString[0]);
      bool = true;
      remote = false;
    }
    else if (paramArrayOfString.length == 2)
    {
      if (!paramArrayOfString[0].equals("-url"))
      {
        showUsage();
      }
      else
      {
        init(paramArrayOfString[1]);
        bool = true;
        remote = true;
      }
    }
    else
    {
      showUsage();
    }
    return bool;
  }
  
  public void showUsage()
  {
    System.out.println("Usage: jlp [-url] <filename>");
    System.out.println("");
    System.out.println(" e.g. : java javazoom.jl.player.jlp localfile.mp3");
    System.out.println("        java javazoom.jl.player.jlp -url http://www.server.com/remotefile.mp3");
    System.out.println("        java javazoom.jl.player.jlp -url http://www.shoutcastserver.com:8000");
  }
  
  public void play()
    throws JavaLayerException
  {
    try
    {
      System.out.println("playing " + fFilename + "...");
      InputStream localInputStream = null;
      if (remote == true) {
        localInputStream = getURLInputStream();
      } else {
        localInputStream = getInputStream();
      }
      AudioDevice localAudioDevice = getAudioDevice();
      Player localPlayer = new Player(localInputStream, localAudioDevice);
      localPlayer.play();
    }
    catch (IOException localIOException)
    {
      throw new JavaLayerException("Problem playing file " + fFilename, localIOException);
    }
    catch (Exception localException)
    {
      throw new JavaLayerException("Problem playing file " + fFilename, localException);
    }
  }
  
  protected InputStream getURLInputStream()
    throws Exception
  {
    URL localURL = new URL(fFilename);
    InputStream localInputStream = localURL.openStream();
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(localInputStream);
    return localBufferedInputStream;
  }
  
  protected InputStream getInputStream()
    throws IOException
  {
    FileInputStream localFileInputStream = new FileInputStream(fFilename);
    BufferedInputStream localBufferedInputStream = new BufferedInputStream(localFileInputStream);
    return localBufferedInputStream;
  }
  
  protected AudioDevice getAudioDevice()
    throws JavaLayerException
  {
    return FactoryRegistry.systemRegistry().createAudioDevice();
  }
}
