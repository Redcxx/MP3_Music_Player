package javazoom.jl.decoder;

public abstract interface DecoderErrors
  extends JavaLayerErrors
{
  public static final int UNKNOWN_ERROR = 512;
  public static final int UNSUPPORTED_LAYER = 513;
}
