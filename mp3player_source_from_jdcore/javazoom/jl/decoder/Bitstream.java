package javazoom.jl.decoder;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PushbackInputStream;

public final class Bitstream
  implements BitstreamErrors
{
  static byte INITIAL_SYNC = 0;
  static byte STRICT_SYNC = 1;
  private static final int BUFFER_INT_SIZE = 433;
  private final int[] framebuffer = new int['Ʊ'];
  private int framesize;
  private byte[] frame_bytes = new byte['ۄ'];
  private int wordpointer;
  private int bitindex;
  private int syncword;
  private int header_pos = 0;
  private boolean single_ch_mode;
  private final int[] bitmask = { 0, 1, 3, 7, 15, 31, 63, 127, 255, 511, 1023, 2047, 4095, 8191, 16383, 32767, 65535, 131071 };
  private final PushbackInputStream source;
  private final Header header = new Header();
  private final byte[] syncbuf = new byte[4];
  private Crc16[] crc = new Crc16[1];
  private byte[] rawid3v2 = null;
  private boolean firstframe = true;
  
  public Bitstream(InputStream paramInputStream)
  {
    if (paramInputStream == null) {
      throw new NullPointerException("in");
    }
    paramInputStream = new BufferedInputStream(paramInputStream);
    loadID3v2(paramInputStream);
    firstframe = true;
    source = new PushbackInputStream(paramInputStream, 1732);
    closeFrame();
  }
  
  public int header_pos()
  {
    return header_pos;
  }
  
  private void loadID3v2(InputStream paramInputStream)
  {
    int i = -1;
    try
    {
      paramInputStream.mark(10);
      i = readID3v2Header(paramInputStream);
      header_pos = i;
      try
      {
        paramInputStream.reset();
      }
      catch (IOException localIOException1) {}
      try
      {
        if (i > 0)
        {
          rawid3v2 = new byte[i];
          paramInputStream.read(rawid3v2, 0, rawid3v2.length);
        }
      }
      catch (IOException localIOException4) {}
    }
    catch (IOException localIOException2) {}finally
    {
      try
      {
        paramInputStream.reset();
      }
      catch (IOException localIOException5) {}
    }
  }
  
  private int readID3v2Header(InputStream paramInputStream)
    throws IOException
  {
    byte[] arrayOfByte = new byte[4];
    int i = -10;
    paramInputStream.read(arrayOfByte, 0, 3);
    if ((arrayOfByte[0] == 73) && (arrayOfByte[1] == 68) && (arrayOfByte[2] == 51))
    {
      paramInputStream.read(arrayOfByte, 0, 3);
      int j = arrayOfByte[0];
      int k = arrayOfByte[1];
      paramInputStream.read(arrayOfByte, 0, 4);
      i = (arrayOfByte[0] << 21) + (arrayOfByte[1] << 14) + (arrayOfByte[2] << 7) + arrayOfByte[3];
    }
    return i + 10;
  }
  
  public InputStream getRawID3v2()
  {
    if (rawid3v2 == null) {
      return null;
    }
    ByteArrayInputStream localByteArrayInputStream = new ByteArrayInputStream(rawid3v2);
    return localByteArrayInputStream;
  }
  
  public void close()
    throws BitstreamException
  {
    try
    {
      source.close();
    }
    catch (IOException localIOException)
    {
      throw newBitstreamException(258, localIOException);
    }
  }
  
  public Header readFrame()
    throws BitstreamException
  {
    Header localHeader = null;
    try
    {
      localHeader = readNextFrame();
      if (firstframe == true)
      {
        localHeader.parseVBR(frame_bytes);
        firstframe = false;
      }
    }
    catch (BitstreamException localBitstreamException1)
    {
      if (localBitstreamException1.getErrorCode() == 261) {
        try
        {
          closeFrame();
          localHeader = readNextFrame();
        }
        catch (BitstreamException localBitstreamException2)
        {
          if (localBitstreamException2.getErrorCode() != 260) {
            throw newBitstreamException(localBitstreamException2.getErrorCode(), localBitstreamException2);
          }
        }
      } else if (localBitstreamException1.getErrorCode() != 260) {
        throw newBitstreamException(localBitstreamException1.getErrorCode(), localBitstreamException1);
      }
    }
    return localHeader;
  }
  
  private Header readNextFrame()
    throws BitstreamException
  {
    if (framesize == -1) {
      nextFrame();
    }
    return header;
  }
  
  private void nextFrame()
    throws BitstreamException
  {
    header.read_header(this, crc);
  }
  
  public void unreadFrame()
    throws BitstreamException
  {
    if ((wordpointer == -1) && (bitindex == -1) && (framesize > 0)) {
      try
      {
        source.unread(frame_bytes, 0, framesize);
      }
      catch (IOException localIOException)
      {
        throw newBitstreamException(258);
      }
    }
  }
  
  public void closeFrame()
  {
    framesize = -1;
    wordpointer = -1;
    bitindex = -1;
  }
  
  public boolean isSyncCurrentPosition(int paramInt)
    throws BitstreamException
  {
    int i = readBytes(syncbuf, 0, 4);
    int j = syncbuf[0] << 24 & 0xFF000000 | syncbuf[1] << 16 & 0xFF0000 | syncbuf[2] << 8 & 0xFF00 | syncbuf[3] << 0 & 0xFF;
    try
    {
      source.unread(syncbuf, 0, i);
    }
    catch (IOException localIOException) {}
    boolean bool = false;
    switch (i)
    {
    case 0: 
      bool = true;
      break;
    case 4: 
      bool = isSyncMark(j, paramInt, syncword);
    }
    return bool;
  }
  
  public int readBits(int paramInt)
  {
    return get_bits(paramInt);
  }
  
  public int readCheckedBits(int paramInt)
  {
    return get_bits(paramInt);
  }
  
  protected BitstreamException newBitstreamException(int paramInt)
  {
    return new BitstreamException(paramInt, null);
  }
  
  protected BitstreamException newBitstreamException(int paramInt, Throwable paramThrowable)
  {
    return new BitstreamException(paramInt, paramThrowable);
  }
  
  int syncHeader(byte paramByte)
    throws BitstreamException
  {
    int j = readBytes(syncbuf, 0, 3);
    if (j != 3) {
      throw newBitstreamException(260, null);
    }
    int i = syncbuf[0] << 16 & 0xFF0000 | syncbuf[1] << 8 & 0xFF00 | syncbuf[2] << 0 & 0xFF;
    boolean bool;
    do
    {
      i <<= 8;
      if (readBytes(syncbuf, 3, 1) != 1) {
        throw newBitstreamException(260, null);
      }
      i |= syncbuf[3] & 0xFF;
      bool = isSyncMark(i, paramByte, syncword);
    } while (!bool);
    return i;
  }
  
  public boolean isSyncMark(int paramInt1, int paramInt2, int paramInt3)
  {
    boolean bool = false;
    if (paramInt2 == INITIAL_SYNC)
    {
      bool = (paramInt1 & 0xFFE00000) == -2097152;
    }
    else
    {
      if ((paramInt1 & 0xFFF80C00) == paramInt3) {}
      bool = ((paramInt1 & 0xC0) == 192) == single_ch_mode;
    }
    if (bool) {
      bool = (paramInt1 >>> 10 & 0x3) != 3;
    }
    if (bool) {
      bool = (paramInt1 >>> 17 & 0x3) != 0;
    }
    if (bool) {
      bool = (paramInt1 >>> 19 & 0x3) != 1;
    }
    return bool;
  }
  
  int read_frame_data(int paramInt)
    throws BitstreamException
  {
    int i = 0;
    i = readFully(frame_bytes, 0, paramInt);
    framesize = paramInt;
    wordpointer = -1;
    bitindex = -1;
    return i;
  }
  
  void parse_frame()
    throws BitstreamException
  {
    int i = 0;
    byte[] arrayOfByte = frame_bytes;
    int j = framesize;
    int k = 0;
    while (k < j)
    {
      int m = 0;
      int n = 0;
      int i1 = 0;
      int i2 = 0;
      int i3 = 0;
      n = arrayOfByte[k];
      if (k + 1 < j) {
        i1 = arrayOfByte[(k + 1)];
      }
      if (k + 2 < j) {
        i2 = arrayOfByte[(k + 2)];
      }
      if (k + 3 < j) {
        i3 = arrayOfByte[(k + 3)];
      }
      framebuffer[(i++)] = (n << 24 & 0xFF000000 | i1 << 16 & 0xFF0000 | i2 << 8 & 0xFF00 | i3 & 0xFF);
      k += 4;
    }
    wordpointer = 0;
    bitindex = 0;
  }
  
  public int get_bits(int paramInt)
  {
    int i = 0;
    int j = bitindex + paramInt;
    if (wordpointer < 0) {
      wordpointer = 0;
    }
    if (j <= 32)
    {
      i = framebuffer[wordpointer] >>> 32 - j & bitmask[paramInt];
      if (this.bitindex += paramInt == 32)
      {
        bitindex = 0;
        wordpointer += 1;
      }
      return i;
    }
    int k = framebuffer[wordpointer] & 0xFFFF;
    wordpointer += 1;
    int m = framebuffer[wordpointer] & 0xFFFF0000;
    i = k << 16 & 0xFFFF0000 | m >>> 16 & 0xFFFF;
    i >>>= 48 - j;
    i &= bitmask[paramInt];
    bitindex = (j - 32);
    return i;
  }
  
  void set_syncword(int paramInt)
  {
    syncword = (paramInt & 0xFF3F);
    single_ch_mode = ((paramInt & 0xC0) == 192);
  }
  
  private int readFully(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws BitstreamException
  {
    int i = 0;
    try
    {
      while (paramInt2 > 0)
      {
        int j = source.read(paramArrayOfByte, paramInt1, paramInt2);
        if (j == -1) {
          while (paramInt2-- > 0) {
            paramArrayOfByte[(paramInt1++)] = 0;
          }
        }
        i += j;
        paramInt1 += j;
        paramInt2 -= j;
      }
    }
    catch (IOException localIOException)
    {
      throw newBitstreamException(258, localIOException);
    }
    return i;
  }
  
  private int readBytes(byte[] paramArrayOfByte, int paramInt1, int paramInt2)
    throws BitstreamException
  {
    int i = 0;
    try
    {
      while (paramInt2 > 0)
      {
        int j = source.read(paramArrayOfByte, paramInt1, paramInt2);
        if (j == -1) {
          break;
        }
        i += j;
        paramInt1 += j;
        paramInt2 -= j;
      }
    }
    catch (IOException localIOException)
    {
      throw newBitstreamException(258, localIOException);
    }
    return i;
  }
}
