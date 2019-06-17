package javazoom.jl.decoder;

public class BitstreamException
  extends JavaLayerException
  implements BitstreamErrors
{
  private int errorcode = 256;
  
  public BitstreamException(String paramString, Throwable paramThrowable)
  {
    super(paramString, paramThrowable);
  }
  
  public BitstreamException(int paramInt, Throwable paramThrowable)
  {
    this(getErrorString(paramInt), paramThrowable);
  }
  
  public int getErrorCode()
  {
    return errorcode;
  }
  
  public static String getErrorString(int paramInt)
  {
    return "Bitstream errorcode " + Integer.toHexString(paramInt);
  }
}
