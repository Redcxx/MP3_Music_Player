package javazoom.jl.player;

import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.JavaLayerException;

public abstract class AudioDeviceBase
  implements AudioDevice
{
  private boolean open = false;
  private Decoder decoder = null;
  
  public AudioDeviceBase() {}
  
  public synchronized void open(Decoder paramDecoder)
    throws JavaLayerException
  {
    if (!isOpen())
    {
      decoder = paramDecoder;
      openImpl();
      setOpen(true);
    }
  }
  
  protected void openImpl()
    throws JavaLayerException
  {}
  
  protected void setOpen(boolean paramBoolean)
  {
    open = paramBoolean;
  }
  
  public synchronized boolean isOpen()
  {
    return open;
  }
  
  public synchronized void close()
  {
    if (isOpen())
    {
      closeImpl();
      setOpen(false);
      decoder = null;
    }
  }
  
  protected void closeImpl() {}
  
  public void write(short[] paramArrayOfShort, int paramInt1, int paramInt2)
    throws JavaLayerException
  {
    if (isOpen()) {
      writeImpl(paramArrayOfShort, paramInt1, paramInt2);
    }
  }
  
  protected void writeImpl(short[] paramArrayOfShort, int paramInt1, int paramInt2)
    throws JavaLayerException
  {}
  
  public void flush()
  {
    if (isOpen()) {
      flushImpl();
    }
  }
  
  protected void flushImpl() {}
  
  protected Decoder getDecoder()
  {
    return decoder;
  }
}
