package javazoom.jl.player;

public class NullAudioDevice
  extends AudioDeviceBase
{
  public NullAudioDevice() {}
  
  public int getPosition()
  {
    return 0;
  }
}
