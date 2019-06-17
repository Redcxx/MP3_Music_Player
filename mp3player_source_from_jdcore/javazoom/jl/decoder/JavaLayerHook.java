package javazoom.jl.decoder;

import java.io.InputStream;

public abstract interface JavaLayerHook
{
  public abstract InputStream getResourceAsStream(String paramString);
}
