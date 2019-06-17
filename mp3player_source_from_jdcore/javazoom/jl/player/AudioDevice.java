package javazoom.jl.player;

import javazoom.jl.decoder.Decoder;
import javazoom.jl.decoder.JavaLayerException;

public abstract interface AudioDevice
{
  public abstract void open(Decoder paramDecoder)
    throws JavaLayerException;
  
  public abstract boolean isOpen();
  
  public abstract void write(short[] paramArrayOfShort, int paramInt1, int paramInt2)
    throws JavaLayerException;
  
  public abstract void close();
  
  public abstract void flush();
  
  public abstract int getPosition();
}
