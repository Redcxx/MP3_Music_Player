package javazoom.jl.converter;

import javazoom.jl.decoder.Obuffer;

public class WaveFileObuffer
  extends Obuffer
{
  private short[] buffer;
  private short[] bufferp;
  private int channels;
  private WaveFile outWave;
  short[] myBuffer = new short[2];
  
  public WaveFileObuffer(int paramInt1, int paramInt2, String paramString)
  {
    if (paramString == null) {
      throw new NullPointerException("FileName");
    }
    buffer = new short['ऀ'];
    bufferp = new short[2];
    channels = paramInt1;
    for (int i = 0; i < paramInt1; i++) {
      bufferp[i] = ((short)i);
    }
    outWave = new WaveFile();
    i = outWave.OpenForWrite(paramString, paramInt2, (short)16, (short)channels);
  }
  
  public void append(int paramInt, short paramShort)
  {
    buffer[bufferp[paramInt]] = paramShort;
    int tmp17_16 = paramInt;
    short[] tmp17_13 = bufferp;
    tmp17_13[tmp17_16] = ((short)(tmp17_13[tmp17_16] + channels));
  }
  
  public void write_buffer(int paramInt)
  {
    int i = 0;
    int j = 0;
    j = outWave.WriteData(buffer, bufferp[0]);
    for (int k = 0; k < channels; k++) {
      bufferp[k] = ((short)k);
    }
  }
  
  public void close()
  {
    outWave.Close();
  }
  
  public void clear_buffer() {}
  
  public void set_stop_flag() {}
}
