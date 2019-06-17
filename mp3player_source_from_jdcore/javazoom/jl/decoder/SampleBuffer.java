package javazoom.jl.decoder;

public class SampleBuffer
  extends Obuffer
{
  private short[] buffer = new short['ऀ'];
  private int[] bufferp = new int[2];
  private int channels;
  private int frequency;
  
  public SampleBuffer(int paramInt1, int paramInt2)
  {
    channels = paramInt2;
    frequency = paramInt1;
    for (int i = 0; i < paramInt2; i++) {
      bufferp[i] = ((short)i);
    }
  }
  
  public int getChannelCount()
  {
    return channels;
  }
  
  public int getSampleFrequency()
  {
    return frequency;
  }
  
  public short[] getBuffer()
  {
    return buffer;
  }
  
  public int getBufferLength()
  {
    return bufferp[0];
  }
  
  public void append(int paramInt, short paramShort)
  {
    buffer[bufferp[paramInt]] = paramShort;
    bufferp[paramInt] += channels;
  }
  
  public void appendSamples(int paramInt, float[] paramArrayOfFloat)
  {
    int i = bufferp[paramInt];
    int k = 0;
    while (k < 32)
    {
      float f = paramArrayOfFloat[(k++)];
      f = f < -32767.0F ? -32767.0F : f > 32767.0F ? 32767.0F : f;
      int j = (short)(int)f;
      buffer[i] = j;
      i += channels;
    }
    bufferp[paramInt] = i;
  }
  
  public void write_buffer(int paramInt) {}
  
  public void close() {}
  
  public void clear_buffer()
  {
    for (int i = 0; i < channels; i++) {
      bufferp[i] = ((short)i);
    }
  }
  
  public void set_stop_flag() {}
}
