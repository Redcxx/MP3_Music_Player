package javazoom.jl.player.advanced;

import java.io.InputStream;
import javazoom.jl.decoder.Bitstream;
import javazoom.jl.decoder.BitstreamException;
import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.Header;
import javazoom.jl.decoder.JavaLayerException;
import javazoom.jl.decoder.SampleBuffer;
import javazoom.jl.player.AudioDevice;
import javazoom.jl.player.FactoryRegistry;

public class AdvancedPlayer
{
  private Bitstream bitstream;
  private Decoder decoder;
  private AudioDevice audio;
  private boolean closed = false;
  private boolean complete = false;
  private int lastPosition = 0;
  private PlaybackListener listener;
  
  public AdvancedPlayer(InputStream paramInputStream)
    throws JavaLayerException
  {
    this(paramInputStream, null);
  }
  
  public AdvancedPlayer(InputStream paramInputStream, AudioDevice paramAudioDevice)
    throws JavaLayerException
  {
    bitstream = new Bitstream(paramInputStream);
    if (paramAudioDevice != null) {
      audio = paramAudioDevice;
    } else {
      audio = FactoryRegistry.systemRegistry().createAudioDevice();
    }
    audio.open(this.decoder = new Decoder());
  }
  
  public void play()
    throws JavaLayerException
  {
    play(Integer.MAX_VALUE);
  }
  
  public boolean play(int paramInt)
    throws JavaLayerException
  {
    boolean bool = true;
    if (listener != null) {
      listener.playbackStarted(createEvent(PlaybackEvent.STARTED));
    }
    while ((paramInt-- > 0) && (bool)) {
      bool = decodeFrame();
    }
    AudioDevice localAudioDevice = audio;
    if (localAudioDevice != null)
    {
      localAudioDevice.flush();
      synchronized (this)
      {
        complete = (!closed);
        close();
      }
      if (listener != null) {
        listener.playbackFinished(createEvent(localAudioDevice, PlaybackEvent.STOPPED));
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
  
  protected boolean skipFrame()
    throws JavaLayerException
  {
    Header localHeader = bitstream.readFrame();
    if (localHeader == null) {
      return false;
    }
    bitstream.closeFrame();
    return true;
  }
  
  public boolean play(int paramInt1, int paramInt2)
    throws JavaLayerException
  {
    boolean bool = true;
    int i = paramInt1;
    while ((i-- > 0) && (bool)) {
      bool = skipFrame();
    }
    return play(paramInt2 - paramInt1);
  }
  
  private PlaybackEvent createEvent(int paramInt)
  {
    return createEvent(audio, paramInt);
  }
  
  private PlaybackEvent createEvent(AudioDevice paramAudioDevice, int paramInt)
  {
    return new PlaybackEvent(this, paramInt, paramAudioDevice.getPosition());
  }
  
  public void setPlayBackListener(PlaybackListener paramPlaybackListener)
  {
    listener = paramPlaybackListener;
  }
  
  public PlaybackListener getPlayBackListener()
  {
    return listener;
  }
  
  public void stop()
  {
    listener.playbackFinished(createEvent(PlaybackEvent.STOPPED));
    close();
  }
}
