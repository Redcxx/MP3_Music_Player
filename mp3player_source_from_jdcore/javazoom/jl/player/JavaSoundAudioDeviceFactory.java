package javazoom.jl.player;

import javazoom.jl.decoder.JavaLayerException;

public class JavaSoundAudioDeviceFactory
  extends AudioDeviceFactory
{
  private boolean tested = false;
  private static final String DEVICE_CLASS_NAME = "javazoom.jl.player.JavaSoundAudioDevice";
  
  public JavaSoundAudioDeviceFactory() {}
  
  public synchronized AudioDevice createAudioDevice()
    throws JavaLayerException
  {
    if (!tested)
    {
      testAudioDevice();
      tested = true;
    }
    try
    {
      return createAudioDeviceImpl();
    }
    catch (Exception localException)
    {
      throw new JavaLayerException("unable to create JavaSound device: " + localException);
    }
    catch (LinkageError localLinkageError)
    {
      throw new JavaLayerException("unable to create JavaSound device: " + localLinkageError);
    }
  }
  
  protected JavaSoundAudioDevice createAudioDeviceImpl()
    throws JavaLayerException
  {
    ClassLoader localClassLoader = getClass().getClassLoader();
    try
    {
      JavaSoundAudioDevice localJavaSoundAudioDevice = (JavaSoundAudioDevice)instantiate(localClassLoader, "javazoom.jl.player.JavaSoundAudioDevice");
      return localJavaSoundAudioDevice;
    }
    catch (Exception localException)
    {
      throw new JavaLayerException("Cannot create JavaSound device", localException);
    }
    catch (LinkageError localLinkageError)
    {
      throw new JavaLayerException("Cannot create JavaSound device", localLinkageError);
    }
  }
  
  public void testAudioDevice()
    throws JavaLayerException
  {
    JavaSoundAudioDevice localJavaSoundAudioDevice = createAudioDeviceImpl();
    localJavaSoundAudioDevice.test();
  }
}
