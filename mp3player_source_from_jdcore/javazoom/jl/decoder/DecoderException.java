package javazoom.jl.decoder;

public class DecoderException
  extends JavaLayerException
  implements DecoderErrors
{
  private int errorcode = 512;
  
  public DecoderException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
  
  public DecoderException(int paramInt, Throwable paramThrowable)
  {
    this(getErrorString(paramInt), paramThrowable);
  }
  
  public int getErrorCode()
  {
    return errorcode;
  }
  
  public static String getErrorString(int paramInt)
  {
    return "Decoder errorcode " + Integer.toHexString(paramInt);
  }
}
