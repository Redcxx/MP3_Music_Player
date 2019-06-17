package javazoom.jl.decoder;

public class OutputChannels
{
  public static final int BOTH_CHANNELS = 0;
  public static final int LEFT_CHANNEL = 1;
  public static final int RIGHT_CHANNEL = 2;
  public static final int DOWNMIX_CHANNELS = 3;
  public static final OutputChannels LEFT = new OutputChannels(1);
  public static final OutputChannels RIGHT = new OutputChannels(2);
  public static final OutputChannels BOTH = new OutputChannels(0);
  public static final OutputChannels DOWNMIX = new OutputChannels(3);
  private int outputChannels;
  
  public static OutputChannels fromInt(int paramInt)
  {
    switch (paramInt)
    {
    case 1: 
      return LEFT;
    case 2: 
      return RIGHT;
    case 0: 
      return BOTH;
    case 3: 
      return DOWNMIX;
    }
    throw new IllegalArgumentException("Invalid channel code: " + paramInt);
  }
  
  private OutputChannels(int paramInt)
  {
    outputChannels = paramInt;
    if ((paramInt < 0) || (paramInt > 3)) {
      throw new IllegalArgumentException("channels");
    }
  }
  
  public int getChannelsOutputCode()
  {
    return outputChannels;
  }
  
  public int getChannelCount()
  {
    int i = outputChannels == 0 ? 2 : 1;
    return i;
  }
  
  public boolean equals(Object paramObject)
  {
    boolean bool = false;
    if ((paramObject instanceof OutputChannels))
    {
      OutputChannels localOutputChannels = (OutputChannels)paramObject;
      bool = outputChannels == outputChannels;
    }
    return bool;
  }
  
  public int hashCode()
  {
    return outputChannels;
  }
}
