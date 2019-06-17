package javazoom.jl.decoder;

public final class Crc16
{
  private static short polynomial = 32773;
  private short crc = -1;
  
  public Crc16() {}
  
  public void add_bits(int paramInt1, int paramInt2)
  {
    int i = 1 << paramInt2 - 1;
    do
    {
      if ((((crc & 0x8000) == 0 ? 1 : 0) ^ ((paramInt1 & i) == 0 ? 1 : 0)) != 0)
      {
        crc = ((short)(crc << 1));
        crc = ((short)(crc ^ polynomial));
      }
      else
      {
        crc = ((short)(crc << 1));
      }
    } while (i >>>= 1 != 0);
  }
  
  public short checksum()
  {
    short s = crc;
    crc = -1;
    return s;
  }
}
