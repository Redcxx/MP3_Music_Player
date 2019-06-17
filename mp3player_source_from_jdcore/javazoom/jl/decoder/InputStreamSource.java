package javazoom.jl.decoder;

import java.io.IOException;
import java.io.InputStream;

public class InputStreamSource
  implements Source
{
  private final InputStream in;
  
  public InputStreamSource(InputStream paramInputStream)
  {
    if (paramInputStream == null) {
      throw new NullPointerException("in");
    }
    in = paramInputStream;
  }
  
  public int read(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws IOException
  {
    int i = in.read(paramArrayOfByte, paramInt1, paramInt2);
    return i;
  }
  
  public boolean willReadBlock()
  {
    return true;
  }
  
  public boolean isSeekable()
  {
    return false;
  }
  
  public long tell()
  {
    return -1L;
  }
  
  public long seek(long paramLong)
  {
    return -1L;
  }
  
  public long length()
  {
    return -1L;
  }
}
