package javazoom.jl.converter;

public class WaveFile
  extends RiffFile
{
  public static final int MAX_WAVE_CHANNELS = 2;
  private WaveFormat_Chunk wave_format = new WaveFormat_Chunk();
  private RiffFile.RiffChunkHeader pcm_data = new RiffFile.RiffChunkHeader(this);
  private long pcm_data_offset = 0L;
  private int num_samples = 0;
  
  public WaveFile()
  {
    pcm_data.ckID = FourCC("data");
    pcm_data.ckSize = 0;
    num_samples = 0;
  }
  
  public int OpenForWrite(String paramString, int paramInt, short paramShort1, short paramShort2)
  {
    if ((paramString == null) || ((paramShort1 != 8) && (paramShort1 != 16)) || (paramShort2 < 1) || (paramShort2 > 2)) {
      return 4;
    }
    wave_format.data.Config(paramInt, paramShort1, paramShort2);
    int i = Open(paramString, 1);
    if (i == 0)
    {
      byte[] arrayOfByte = { 87, 65, 86, 69 };
      i = Write(arrayOfByte, 4);
      if (i == 0)
      {
        i = Write(wave_format.header, 8);
        i = Write(wave_format.data.wFormatTag, 2);
        i = Write(wave_format.data.nChannels, 2);
        i = Write(wave_format.data.nSamplesPerSec, 4);
        i = Write(wave_format.data.nAvgBytesPerSec, 4);
        i = Write(wave_format.data.nBlockAlign, 2);
        i = Write(wave_format.data.nBitsPerSample, 2);
        if (i == 0)
        {
          pcm_data_offset = CurrentFilePosition();
          i = Write(pcm_data, 8);
        }
      }
    }
    return i;
  }
  
  public int WriteData(short[] paramArrayOfShort, int paramInt)
  {
    int i = paramInt * 2;
    pcm_data.ckSize += i;
    return super.Write(paramArrayOfShort, i);
  }
  
  public int Close()
  {
    int i = 0;
    if (fmode == 1) {
      i = Backpatch(pcm_data_offset, pcm_data, 8);
    }
    if (i == 0) {
      i = super.Close();
    }
    return i;
  }
  
  public int SamplingRate()
  {
    return wave_format.data.nSamplesPerSec;
  }
  
  public short BitsPerSample()
  {
    return wave_format.data.nBitsPerSample;
  }
  
  public short NumChannels()
  {
    return wave_format.data.nChannels;
  }
  
  public int NumSamples()
  {
    return num_samples;
  }
  
  public int OpenForWrite(String paramString, WaveFile paramWaveFile)
  {
    return OpenForWrite(paramString, paramWaveFile.SamplingRate(), paramWaveFile.BitsPerSample(), paramWaveFile.NumChannels());
  }
  
  public long CurrentFilePosition()
  {
    return super.CurrentFilePosition();
  }
  
  public class WaveFileSample
  {
    public short[] chan = new short[2];
    
    public WaveFileSample() {}
  }
  
  class WaveFormat_Chunk
  {
    public RiffFile.RiffChunkHeader header = new RiffFile.RiffChunkHeader(WaveFile.this);
    public WaveFile.WaveFormat_ChunkData data = new WaveFile.WaveFormat_ChunkData(WaveFile.this);
    
    public WaveFormat_Chunk()
    {
      header.ckID = RiffFile.FourCC("fmt ");
      header.ckSize = 16;
    }
    
    public int VerifyValidity()
    {
      int i = (header.ckID == RiffFile.FourCC("fmt ")) && ((data.nChannels == 1) || (data.nChannels == 2)) && (data.nAvgBytesPerSec == data.nChannels * data.nSamplesPerSec * data.nBitsPerSample / 8) && (data.nBlockAlign == data.nChannels * data.nBitsPerSample / 8) ? 1 : 0;
      if (i == 1) {
        return 1;
      }
      return 0;
    }
  }
  
  class WaveFormat_ChunkData
  {
    public short wFormatTag = 0;
    public short nChannels = 0;
    public int nSamplesPerSec = 0;
    public int nAvgBytesPerSec = 0;
    public short nBlockAlign = 0;
    public short nBitsPerSample = 0;
    
    public WaveFormat_ChunkData()
    {
      Config(44100, (short)16, (short)1);
    }
    
    public void Config(int paramInt, short paramShort1, short paramShort2)
    {
      nSamplesPerSec = paramInt;
      nChannels = paramShort2;
      nBitsPerSample = paramShort1;
      nAvgBytesPerSec = (nChannels * nSamplesPerSec * nBitsPerSample / 8);
      nBlockAlign = ((short)(nChannels * nBitsPerSample / 8));
    }
  }
}
