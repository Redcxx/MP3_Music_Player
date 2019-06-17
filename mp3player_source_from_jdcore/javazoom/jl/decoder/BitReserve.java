package javazoom.jl.decoder;

final class BitReserve
{
  private static final int BUFSIZE = 32768;
  private static final int BUFSIZE_MASK = 32767;
  private int offset = 0;
  private int totbit = 0;
  private int buf_byte_idx = 0;
  private final int[] buf = new int[32768];
  private int buf_bit_idx;
  
  BitReserve() {}
  
  public int hsstell()
  {
    return totbit;
  }
  
  public int hgetbits(int paramInt)
  {
    totbit += paramInt;
    int i = 0;
    int j = buf_byte_idx;
    if (j + paramInt < 32768) {
      while (paramInt-- > 0)
      {
        i <<= 1;
        i |= (buf[(j++)] != 0 ? 1 : 0);
      }
    }
    while (paramInt-- > 0)
    {
      i <<= 1;
      i |= (buf[j] != 0 ? 1 : 0);
      j = j + 1 & 0x7FFF;
    }
    buf_byte_idx = j;
    return i;
  }
  
  public int hget1bit()
  {
    totbit += 1;
    int i = buf[buf_byte_idx];
    buf_byte_idx = (buf_byte_idx + 1 & 0x7FFF);
    return i;
  }
  
  public void hputbuf(int paramInt)
  {
    int i = offset;
    buf[(i++)] = (paramInt & 0x80);
    buf[(i++)] = (paramInt & 0x40);
    buf[(i++)] = (paramInt & 0x20);
    buf[(i++)] = (paramInt & 0x10);
    buf[(i++)] = (paramInt & 0x8);
    buf[(i++)] = (paramInt & 0x4);
    buf[(i++)] = (paramInt & 0x2);
    buf[(i++)] = (paramInt & 0x1);
    if (i == 32768) {
      offset = 0;
    } else {
      offset = i;
    }
  }
  
  public void rewindNbits(int paramInt)
  {
    totbit -= paramInt;
    buf_byte_idx -= paramInt;
    if (buf_byte_idx < 0) {
      buf_byte_idx += 32768;
    }
  }
  
  public void rewindNbytes(int paramInt)
  {
    int i = paramInt << 3;
    totbit -= i;
    buf_byte_idx -= i;
    if (buf_byte_idx < 0) {
      buf_byte_idx += 32768;
    }
  }
}
