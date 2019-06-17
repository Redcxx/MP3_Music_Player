package javazoom.jl.player;

import java.io.InputStream;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;

public class Player
{
  private int frame = 0;
  private Bitstream bitstream;
  private Decoder decoder;
  private AudioDevice audio;
  private boolean closed = false;
  private boolean complete = false;
  private int lastPosition = 0;
  
  public Player(InputStream paramInputStream)
    throws JavaLayerException
  {
    this(paramInputStream, null);
  }
  
  public Player(InputStream paramInputStream, AudioDevice paramAudioDevice)
    throws JavaLayerException
  {
    bitstream = new Bitstream(paramInputStream);
    decoder = new Decoder();
    if (paramAudioDevice != null)
    {
      audio = paramAudioDevice;
    }
    else
    {
      FactoryRegistry localFactoryRegistry = FactoryRegistry.systemRegistry();
      audio = localFactoryRegistry.createAudioDevice();
    }
    audio.open(decoder);
  }
  
  public void play()
    throws JavaLayerException
  {
    play(Integer.MAX_VALUE);
  }
  
  public boolean play(int paramInt)
    throws JavaLayerException
  {
    for (boolean bool = true; (paramInt-- > 0) && (bool); bool = decodeFrame()) {}
    if (!bool)
    {
      AudioDevice localAudioDevice = audio;
      if (localAudioDevice != null)
      {
        localAudioDevice.flush();
        synchronized (this)
        {
          complete = (!closed);
          close();
        }
      }
    }
    return bool;
  }
  
  public synchronized void close()
  {
    AudioDevice localAudioDevice = audio;
    if (localAudioDevice != null)
    {
      closed = true;
      audio = null;
      localAudioDevice.close();
      lastPosition = localAudioDevice.getPosition();
      try
      {
        bitstream.close();
      }
      catch (BitstreamException localBitstreamException) {}
    }
  }
  
  public synchronized boolean isComplete()
  {
    return complete;
  }
  
  public int getPosition()
  {
    int i = lastPosition;
    AudioDevice localAudioDevice = audio;
    if (localAudioDevice != null) {
      i = localAudioDevice.getPosition();
    }
    return i;
  }
  
  protected boolean decodeFrame()
    throws JavaLayerException
  {
    try
    {
      AudioDevice localAudioDevice = audio;
      if (localAudioDevice == null) {
        return false;
      }
      Header localHeader = bitstream.readFrame();
      if (localHeader == null) {
        return false;
      }
      SampleBuffer localSampleBuffer = (SampleBuffer)decoder.decodeFrame(localHeader, bitstream);
      synchronized (this)
      {
        localAudioDevice = audio;
        if (localAudioDevice != null) {
          localAudioDevice.write(localSampleBuffer.getBuffer(), 0, localSampleBuffer.getBufferLength());
        }
      }
      bitstream.closeFrame();
    }
    catch (RuntimeException localRuntimeException)
    {
      throw new JavaLayerException("Exception decoding audio frame", localRuntimeException);
    }
    return true;
  }
}
