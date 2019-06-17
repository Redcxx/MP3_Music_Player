package javazoom.jl.decoder;

public abstract class Obuffer
{
  public static final int OBUFFERSIZE = 2304;
  public static final int MAXCHANNELS = 2;
  
  public Obuffer() {}
  
  public abstract void append(int paramInt, short paramShort);
  
  public void appendSamples(int paramInt, float[] paramArrayOfFloat)
  {
    int i = 0;
    while (i < 32)
    {
      short s = clip(paramArrayOfFloat[(i++)]);
      append(paramInt, s);
    }
  }
  
  private final short clip(float paramFloat)
  {
    return paramFloat < -32768.0F ? Short.MIN_VALUE : paramFloat > 32767.0F ? Short.MAX_VALUE : (short)(int)paramFloat;
  }
  
  public abstract void write_buffer(int paramInt);
  
  public abstract void close();
  
  public abstract void clear_buffer();
  
  public abstract void set_stop_flag();
}
