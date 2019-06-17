package javazoom.jl.decoder;

import java.io.IOException;

final class SynthesisFilter
{
  private float[] v1;
  private float[] v2;
  private float[] actual_v;
  private int actual_write_pos;
  private float[] samples;
  private int channel;
  private float scalefactor;
  private float[] eq;
  private float[] _tmpOut = new float[32];
  private static final double MY_PI = 3.141592653589793D;
  private static final float cos1_64 = (float)(1.0D / (2.0D * Math.cos(0.04908738521234052D)));
  private static final float cos3_64 = (float)(1.0D / (2.0D * Math.cos(0.14726215563702155D)));
  private static final float cos5_64 = (float)(1.0D / (2.0D * Math.cos(0.2454369260617026D)));
  private static final float cos7_64 = (float)(1.0D / (2.0D * Math.cos(0.3436116964863836D)));
  private static final float cos9_64 = (float)(1.0D / (2.0D * Math.cos(0.44178646691106466D)));
  private static final float cos11_64 = (float)(1.0D / (2.0D * Math.cos(0.5399612373357456D)));
  private static final float cos13_64 = (float)(1.0D / (2.0D * Math.cos(0.6381360077604268D)));
  private static final float cos15_64 = (float)(1.0D / (2.0D * Math.cos(0.7363107781851077D)));
  private static final float cos17_64 = (float)(1.0D / (2.0D * Math.cos(0.8344855486097889D)));
  private static final float cos19_64 = (float)(1.0D / (2.0D * Math.cos(0.9326603190344698D)));
  private static final float cos21_64 = (float)(1.0D / (2.0D * Math.cos(1.030835089459151D)));
  private static final float cos23_64 = (float)(1.0D / (2.0D * Math.cos(1.1290098598838318D)));
  private static final float cos25_64 = (float)(1.0D / (2.0D * Math.cos(1.227184630308513D)));
  private static final float cos27_64 = (float)(1.0D / (2.0D * Math.cos(1.325359400733194D)));
  private static final float cos29_64 = (float)(1.0D / (2.0D * Math.cos(1.423534171157875D)));
  private static final float cos31_64 = (float)(1.0D / (2.0D * Math.cos(1.521708941582556D)));
  private static final float cos1_32 = (float)(1.0D / (2.0D * Math.cos(0.09817477042468103D)));
  private static final float cos3_32 = (float)(1.0D / (2.0D * Math.cos(0.2945243112740431D)));
  private static final float cos5_32 = (float)(1.0D / (2.0D * Math.cos(0.4908738521234052D)));
  private static final float cos7_32 = (float)(1.0D / (2.0D * Math.cos(0.6872233929727672D)));
  private static final float cos9_32 = (float)(1.0D / (2.0D * Math.cos(0.8835729338221293D)));
  private static final float cos11_32 = (float)(1.0D / (2.0D * Math.cos(1.0799224746714913D)));
  private static final float cos13_32 = (float)(1.0D / (2.0D * Math.cos(1.2762720155208536D)));
  private static final float cos15_32 = (float)(1.0D / (2.0D * Math.cos(1.4726215563702154D)));
  private static final float cos1_16 = (float)(1.0D / (2.0D * Math.cos(0.19634954084936207D)));
  private static final float cos3_16 = (float)(1.0D / (2.0D * Math.cos(0.5890486225480862D)));
  private static final float cos5_16 = (float)(1.0D / (2.0D * Math.cos(0.9817477042468103D)));
  private static final float cos7_16 = (float)(1.0D / (2.0D * Math.cos(1.3744467859455345D)));
  private static final float cos1_8 = (float)(1.0D / (2.0D * Math.cos(0.39269908169872414D)));
  private static final float cos3_8 = (float)(1.0D / (2.0D * Math.cos(1.1780972450961724D)));
  private static final float cos1_4 = (float)(1.0D / (2.0D * Math.cos(0.7853981633974483D)));
  private static float[] d = null;
  private static float[][] d16 = (float[][])null;
  
  public SynthesisFilter(int paramInt, float paramFloat, float[] paramArrayOfFloat)
  {
    if (d == null)
    {
      d = load_d();
      d16 = splitArray(d, 16);
    }
    v1 = new float['Ȁ'];
    v2 = new float['Ȁ'];
    samples = new float[32];
    channel = paramInt;
    scalefactor = paramFloat;
    setEQ(eq);
    reset();
  }
  
  public void setEQ(float[] paramArrayOfFloat)
  {
    eq = paramArrayOfFloat;
    if (eq == null)
    {
      eq = new float[32];
      for (int i = 0; i < 32; i++) {
        eq[i] = 1.0F;
      }
    }
    if (eq.length < 32) {
      throw new IllegalArgumentException("eq0");
    }
  }
  
  public void reset()
  {
    for (int i = 0; i < 512; i++)
    {
      float tmp20_19 = 0.0F;
      v2[i] = tmp20_19;
      v1[i] = tmp20_19;
    }
    for (i = 0; i < 32; i++) {
      samples[i] = 0.0F;
    }
    actual_v = v1;
    actual_write_pos = 15;
  }
  
  public void input_sample(float paramFloat, int paramInt)
  {
    samples[paramInt] = (eq[paramInt] * paramFloat);
  }
  
  public void input_samples(float[] paramArrayOfFloat)
  {
    for (int i = 31; i >= 0; i--) {
      samples[i] = (paramArrayOfFloat[i] * eq[i]);
    }
  }
  
  private void compute_new_v()
  {
    float f16;
    float f15;
    float f14;
    float f13;
    float f12;
    float f11;
    float f10;
    float f9;
    float f5;
    float f4;
    float f3;
    float f2;
    float f1 = f2 = f3 = f4 = f5 = f6 = f7 = f8 = f9 = f10 = f11 = f12 = f13 = f14 = f15 = f16 = f17 = f18 = f19 = f20 = f21 = f22 = f23 = f24 = f25 = f26 = f27 = f28 = f29 = f30 = f31 = f32 = 0.0F;
    float[] arrayOfFloat1 = samples;
    float f33 = arrayOfFloat1[0];
    float f34 = arrayOfFloat1[1];
    float f35 = arrayOfFloat1[2];
    float f36 = arrayOfFloat1[3];
    float f37 = arrayOfFloat1[4];
    float f38 = arrayOfFloat1[5];
    float f39 = arrayOfFloat1[6];
    float f40 = arrayOfFloat1[7];
    float f41 = arrayOfFloat1[8];
    float f42 = arrayOfFloat1[9];
    float f43 = arrayOfFloat1[10];
    float f44 = arrayOfFloat1[11];
    float f45 = arrayOfFloat1[12];
    float f46 = arrayOfFloat1[13];
    float f47 = arrayOfFloat1[14];
    float f48 = arrayOfFloat1[15];
    float f49 = arrayOfFloat1[16];
    float f50 = arrayOfFloat1[17];
    float f51 = arrayOfFloat1[18];
    float f52 = arrayOfFloat1[19];
    float f53 = arrayOfFloat1[20];
    float f54 = arrayOfFloat1[21];
    float f55 = arrayOfFloat1[22];
    float f56 = arrayOfFloat1[23];
    float f57 = arrayOfFloat1[24];
    float f58 = arrayOfFloat1[25];
    float f59 = arrayOfFloat1[26];
    float f60 = arrayOfFloat1[27];
    float f61 = arrayOfFloat1[28];
    float f62 = arrayOfFloat1[29];
    float f63 = arrayOfFloat1[30];
    float f64 = arrayOfFloat1[31];
    float f65 = f33 + f64;
    float f66 = f34 + f63;
    float f67 = f35 + f62;
    float f68 = f36 + f61;
    float f69 = f37 + f60;
    float f70 = f38 + f59;
    float f71 = f39 + f58;
    float f72 = f40 + f57;
    float f73 = f41 + f56;
    float f74 = f42 + f55;
    float f75 = f43 + f54;
    float f76 = f44 + f53;
    float f77 = f45 + f52;
    float f78 = f46 + f51;
    float f79 = f47 + f50;
    float f80 = f48 + f49;
    float f81 = f65 + f80;
    float f82 = f66 + f79;
    float f83 = f67 + f78;
    float f84 = f68 + f77;
    float f85 = f69 + f76;
    float f86 = f70 + f75;
    float f87 = f71 + f74;
    float f88 = f72 + f73;
    float f89 = (f65 - f80) * cos1_32;
    float f90 = (f66 - f79) * cos3_32;
    float f91 = (f67 - f78) * cos5_32;
    float f92 = (f68 - f77) * cos7_32;
    float f93 = (f69 - f76) * cos9_32;
    float f94 = (f70 - f75) * cos11_32;
    float f95 = (f71 - f74) * cos13_32;
    float f96 = (f72 - f73) * cos15_32;
    f65 = f81 + f88;
    f66 = f82 + f87;
    f67 = f83 + f86;
    f68 = f84 + f85;
    f69 = (f81 - f88) * cos1_16;
    f70 = (f82 - f87) * cos3_16;
    f71 = (f83 - f86) * cos5_16;
    f72 = (f84 - f85) * cos7_16;
    f73 = f89 + f96;
    f74 = f90 + f95;
    f75 = f91 + f94;
    f76 = f92 + f93;
    f77 = (f89 - f96) * cos1_16;
    f78 = (f90 - f95) * cos3_16;
    f79 = (f91 - f94) * cos5_16;
    f80 = (f92 - f93) * cos7_16;
    f81 = f65 + f68;
    f82 = f66 + f67;
    f83 = (f65 - f68) * cos1_8;
    f84 = (f66 - f67) * cos3_8;
    f85 = f69 + f72;
    f86 = f70 + f71;
    f87 = (f69 - f72) * cos1_8;
    f88 = (f70 - f71) * cos3_8;
    f89 = f73 + f76;
    f90 = f74 + f75;
    f91 = (f73 - f76) * cos1_8;
    f92 = (f74 - f75) * cos3_8;
    f93 = f77 + f80;
    f94 = f78 + f79;
    f95 = (f77 - f80) * cos1_8;
    f96 = (f78 - f79) * cos3_8;
    f65 = f81 + f82;
    f66 = (f81 - f82) * cos1_4;
    f67 = f83 + f84;
    f68 = (f83 - f84) * cos1_4;
    f69 = f85 + f86;
    f70 = (f85 - f86) * cos1_4;
    f71 = f87 + f88;
    f72 = (f87 - f88) * cos1_4;
    f73 = f89 + f90;
    f74 = (f89 - f90) * cos1_4;
    f75 = f91 + f92;
    f76 = (f91 - f92) * cos1_4;
    f77 = f93 + f94;
    f78 = (f93 - f94) * cos1_4;
    f79 = f95 + f96;
    f80 = (f95 - f96) * cos1_4;
    float f20 = -(f5 = (f13 = f72) + f70) - f71;
    float f28 = -f71 - f72 - f69;
    float f7 = (f11 = (f15 = f80) + f76) + f78;
    float f18 = -(f3 = f80 + f78 + f74) - f79;
    float f97;
    float f22 = (f97 = -f79 - f80 - f75 - f76) - f78;
    float f30 = -f79 - f80 - f77 - f73;
    float f26 = f97 - f77;
    float f32 = -f65;
    f1 = f66;
    float f24 = -(f9 = f68) - f67;
    f65 = (f33 - f64) * cos1_64;
    f66 = (f34 - f63) * cos3_64;
    f67 = (f35 - f62) * cos5_64;
    f68 = (f36 - f61) * cos7_64;
    f69 = (f37 - f60) * cos9_64;
    f70 = (f38 - f59) * cos11_64;
    f71 = (f39 - f58) * cos13_64;
    f72 = (f40 - f57) * cos15_64;
    f73 = (f41 - f56) * cos17_64;
    f74 = (f42 - f55) * cos19_64;
    f75 = (f43 - f54) * cos21_64;
    f76 = (f44 - f53) * cos23_64;
    f77 = (f45 - f52) * cos25_64;
    f78 = (f46 - f51) * cos27_64;
    f79 = (f47 - f50) * cos29_64;
    f80 = (f48 - f49) * cos31_64;
    f81 = f65 + f80;
    f82 = f66 + f79;
    f83 = f67 + f78;
    f84 = f68 + f77;
    f85 = f69 + f76;
    f86 = f70 + f75;
    f87 = f71 + f74;
    f88 = f72 + f73;
    f89 = (f65 - f80) * cos1_32;
    f90 = (f66 - f79) * cos3_32;
    f91 = (f67 - f78) * cos5_32;
    f92 = (f68 - f77) * cos7_32;
    f93 = (f69 - f76) * cos9_32;
    f94 = (f70 - f75) * cos11_32;
    f95 = (f71 - f74) * cos13_32;
    f96 = (f72 - f73) * cos15_32;
    f65 = f81 + f88;
    f66 = f82 + f87;
    f67 = f83 + f86;
    f68 = f84 + f85;
    f69 = (f81 - f88) * cos1_16;
    f70 = (f82 - f87) * cos3_16;
    f71 = (f83 - f86) * cos5_16;
    f72 = (f84 - f85) * cos7_16;
    f73 = f89 + f96;
    f74 = f90 + f95;
    f75 = f91 + f94;
    f76 = f92 + f93;
    f77 = (f89 - f96) * cos1_16;
    f78 = (f90 - f95) * cos3_16;
    f79 = (f91 - f94) * cos5_16;
    f80 = (f92 - f93) * cos7_16;
    f81 = f65 + f68;
    f82 = f66 + f67;
    f83 = (f65 - f68) * cos1_8;
    f84 = (f66 - f67) * cos3_8;
    f85 = f69 + f72;
    f86 = f70 + f71;
    f87 = (f69 - f72) * cos1_8;
    f88 = (f70 - f71) * cos3_8;
    f89 = f73 + f76;
    f90 = f74 + f75;
    f91 = (f73 - f76) * cos1_8;
    f92 = (f74 - f75) * cos3_8;
    f93 = f77 + f80;
    f94 = f78 + f79;
    f95 = (f77 - f80) * cos1_8;
    f96 = (f78 - f79) * cos3_8;
    f65 = f81 + f82;
    f66 = (f81 - f82) * cos1_4;
    f67 = f83 + f84;
    f68 = (f83 - f84) * cos1_4;
    f69 = f85 + f86;
    f70 = (f85 - f86) * cos1_4;
    f71 = f87 + f88;
    f72 = (f87 - f88) * cos1_4;
    f73 = f89 + f90;
    f74 = (f89 - f90) * cos1_4;
    f75 = f91 + f92;
    f76 = (f91 - f92) * cos1_4;
    f77 = f93 + f94;
    f78 = (f93 - f94) * cos1_4;
    f79 = f95 + f96;
    f80 = (f95 - f96) * cos1_4;
    float f6 = (f12 = (f14 = (f16 = f80) + f72) + f76) + f70 + f78;
    float f8 = (f10 = f80 + f76 + f68) + f78;
    float f17 = -(f2 = (f97 = f78 + f80 + f74) + f66) - f79;
    float f19 = -(f4 = f97 + f70 + f72) - f71 - f79;
    float f23 = (f97 = -f75 - f76 - f79 - f80) - f78 - f67 - f68;
    float f21 = f97 - f78 - f70 - f71 - f72;
    float f25 = f97 - f77 - f67 - f68;
    float f98;
    float f27 = f97 - f77 - (f98 = f69 + f71 + f72);
    float f31 = (f97 = -f73 - f77 - f79 - f80) - f65;
    float f29 = f97 - f98;
    float[] arrayOfFloat2 = actual_v;
    int i = actual_write_pos;
    arrayOfFloat2[(0 + i)] = f1;
    arrayOfFloat2[(16 + i)] = f2;
    arrayOfFloat2[(32 + i)] = f3;
    arrayOfFloat2[(48 + i)] = f4;
    arrayOfFloat2[(64 + i)] = f5;
    arrayOfFloat2[(80 + i)] = f6;
    arrayOfFloat2[(96 + i)] = f7;
    arrayOfFloat2[(112 + i)] = f8;
    arrayOfFloat2[(128 + i)] = f9;
    arrayOfFloat2[(144 + i)] = f10;
    arrayOfFloat2[(160 + i)] = f11;
    arrayOfFloat2[(176 + i)] = f12;
    arrayOfFloat2[(192 + i)] = f13;
    arrayOfFloat2[(208 + i)] = f14;
    arrayOfFloat2[(224 + i)] = f15;
    arrayOfFloat2[(240 + i)] = f16;
    arrayOfFloat2[(256 + i)] = 0.0F;
    arrayOfFloat2[(272 + i)] = (-f16);
    arrayOfFloat2[(288 + i)] = (-f15);
    arrayOfFloat2[(304 + i)] = (-f14);
    arrayOfFloat2[(320 + i)] = (-f13);
    arrayOfFloat2[(336 + i)] = (-f12);
    arrayOfFloat2[(352 + i)] = (-f11);
    arrayOfFloat2[(368 + i)] = (-f10);
    arrayOfFloat2[(384 + i)] = (-f9);
    arrayOfFloat2[(400 + i)] = (-f8);
    arrayOfFloat2[(416 + i)] = (-f7);
    arrayOfFloat2[(432 + i)] = (-f6);
    arrayOfFloat2[(448 + i)] = (-f5);
    arrayOfFloat2[(464 + i)] = (-f4);
    arrayOfFloat2[(480 + i)] = (-f3);
    arrayOfFloat2[(496 + i)] = (-f2);
    arrayOfFloat2 = actual_v == v1 ? v2 : v1;
    arrayOfFloat2[(0 + i)] = (-f1);
    arrayOfFloat2[(16 + i)] = f17;
    arrayOfFloat2[(32 + i)] = f18;
    arrayOfFloat2[(48 + i)] = f19;
    arrayOfFloat2[(64 + i)] = f20;
    arrayOfFloat2[(80 + i)] = f21;
    arrayOfFloat2[(96 + i)] = f22;
    arrayOfFloat2[(112 + i)] = f23;
    arrayOfFloat2[(128 + i)] = f24;
    arrayOfFloat2[(144 + i)] = f25;
    arrayOfFloat2[(160 + i)] = f26;
    arrayOfFloat2[(176 + i)] = f27;
    arrayOfFloat2[(192 + i)] = f28;
    arrayOfFloat2[(208 + i)] = f29;
    arrayOfFloat2[(224 + i)] = f30;
    arrayOfFloat2[(240 + i)] = f31;
    arrayOfFloat2[(256 + i)] = f32;
    arrayOfFloat2[(272 + i)] = f31;
    arrayOfFloat2[(288 + i)] = f30;
    arrayOfFloat2[(304 + i)] = f29;
    arrayOfFloat2[(320 + i)] = f28;
    arrayOfFloat2[(336 + i)] = f27;
    arrayOfFloat2[(352 + i)] = f26;
    arrayOfFloat2[(368 + i)] = f25;
    arrayOfFloat2[(384 + i)] = f24;
    arrayOfFloat2[(400 + i)] = f23;
    arrayOfFloat2[(416 + i)] = f22;
    arrayOfFloat2[(432 + i)] = f21;
    arrayOfFloat2[(448 + i)] = f20;
    arrayOfFloat2[(464 + i)] = f19;
    arrayOfFloat2[(480 + i)] = f18;
    arrayOfFloat2[(496 + i)] = f17;
  }
  
  private void compute_new_v_old()
  {
    float[] arrayOfFloat1 = new float[32];
    float[] arrayOfFloat2 = new float[16];
    float[] arrayOfFloat3 = new float[16];
    for (int i = 31; i >= 0; i--) {
      arrayOfFloat1[i] = 0.0F;
    }
    float[] arrayOfFloat4 = samples;
    arrayOfFloat4[0] += arrayOfFloat4[31];
    arrayOfFloat4[1] += arrayOfFloat4[30];
    arrayOfFloat4[2] += arrayOfFloat4[29];
    arrayOfFloat4[3] += arrayOfFloat4[28];
    arrayOfFloat4[4] += arrayOfFloat4[27];
    arrayOfFloat4[5] += arrayOfFloat4[26];
    arrayOfFloat4[6] += arrayOfFloat4[25];
    arrayOfFloat4[7] += arrayOfFloat4[24];
    arrayOfFloat4[8] += arrayOfFloat4[23];
    arrayOfFloat4[9] += arrayOfFloat4[22];
    arrayOfFloat4[10] += arrayOfFloat4[21];
    arrayOfFloat4[11] += arrayOfFloat4[20];
    arrayOfFloat4[12] += arrayOfFloat4[19];
    arrayOfFloat4[13] += arrayOfFloat4[18];
    arrayOfFloat4[14] += arrayOfFloat4[17];
    arrayOfFloat4[15] += arrayOfFloat4[16];
    arrayOfFloat2[0] += arrayOfFloat2[15];
    arrayOfFloat2[1] += arrayOfFloat2[14];
    arrayOfFloat2[2] += arrayOfFloat2[13];
    arrayOfFloat2[3] += arrayOfFloat2[12];
    arrayOfFloat2[4] += arrayOfFloat2[11];
    arrayOfFloat2[5] += arrayOfFloat2[10];
    arrayOfFloat2[6] += arrayOfFloat2[9];
    arrayOfFloat2[7] += arrayOfFloat2[8];
    arrayOfFloat3[8] = ((arrayOfFloat2[0] - arrayOfFloat2[15]) * cos1_32);
    arrayOfFloat3[9] = ((arrayOfFloat2[1] - arrayOfFloat2[14]) * cos3_32);
    arrayOfFloat3[10] = ((arrayOfFloat2[2] - arrayOfFloat2[13]) * cos5_32);
    arrayOfFloat3[11] = ((arrayOfFloat2[3] - arrayOfFloat2[12]) * cos7_32);
    arrayOfFloat3[12] = ((arrayOfFloat2[4] - arrayOfFloat2[11]) * cos9_32);
    arrayOfFloat3[13] = ((arrayOfFloat2[5] - arrayOfFloat2[10]) * cos11_32);
    arrayOfFloat3[14] = ((arrayOfFloat2[6] - arrayOfFloat2[9]) * cos13_32);
    arrayOfFloat3[15] = ((arrayOfFloat2[7] - arrayOfFloat2[8]) * cos15_32);
    arrayOfFloat3[0] += arrayOfFloat3[7];
    arrayOfFloat3[1] += arrayOfFloat3[6];
    arrayOfFloat3[2] += arrayOfFloat3[5];
    arrayOfFloat3[3] += arrayOfFloat3[4];
    arrayOfFloat2[4] = ((arrayOfFloat3[0] - arrayOfFloat3[7]) * cos1_16);
    arrayOfFloat2[5] = ((arrayOfFloat3[1] - arrayOfFloat3[6]) * cos3_16);
    arrayOfFloat2[6] = ((arrayOfFloat3[2] - arrayOfFloat3[5]) * cos5_16);
    arrayOfFloat2[7] = ((arrayOfFloat3[3] - arrayOfFloat3[4]) * cos7_16);
    arrayOfFloat3[8] += arrayOfFloat3[15];
    arrayOfFloat3[9] += arrayOfFloat3[14];
    arrayOfFloat3[10] += arrayOfFloat3[13];
    arrayOfFloat3[11] += arrayOfFloat3[12];
    arrayOfFloat2[12] = ((arrayOfFloat3[8] - arrayOfFloat3[15]) * cos1_16);
    arrayOfFloat2[13] = ((arrayOfFloat3[9] - arrayOfFloat3[14]) * cos3_16);
    arrayOfFloat2[14] = ((arrayOfFloat3[10] - arrayOfFloat3[13]) * cos5_16);
    arrayOfFloat2[15] = ((arrayOfFloat3[11] - arrayOfFloat3[12]) * cos7_16);
    arrayOfFloat2[0] += arrayOfFloat2[3];
    arrayOfFloat2[1] += arrayOfFloat2[2];
    arrayOfFloat3[2] = ((arrayOfFloat2[0] - arrayOfFloat2[3]) * cos1_8);
    arrayOfFloat3[3] = ((arrayOfFloat2[1] - arrayOfFloat2[2]) * cos3_8);
    arrayOfFloat2[4] += arrayOfFloat2[7];
    arrayOfFloat2[5] += arrayOfFloat2[6];
    arrayOfFloat3[6] = ((arrayOfFloat2[4] - arrayOfFloat2[7]) * cos1_8);
    arrayOfFloat3[7] = ((arrayOfFloat2[5] - arrayOfFloat2[6]) * cos3_8);
    arrayOfFloat2[8] += arrayOfFloat2[11];
    arrayOfFloat2[9] += arrayOfFloat2[10];
    arrayOfFloat3[10] = ((arrayOfFloat2[8] - arrayOfFloat2[11]) * cos1_8);
    arrayOfFloat3[11] = ((arrayOfFloat2[9] - arrayOfFloat2[10]) * cos3_8);
    arrayOfFloat2[12] += arrayOfFloat2[15];
    arrayOfFloat2[13] += arrayOfFloat2[14];
    arrayOfFloat3[14] = ((arrayOfFloat2[12] - arrayOfFloat2[15]) * cos1_8);
    arrayOfFloat3[15] = ((arrayOfFloat2[13] - arrayOfFloat2[14]) * cos3_8);
    arrayOfFloat3[0] += arrayOfFloat3[1];
    arrayOfFloat2[1] = ((arrayOfFloat3[0] - arrayOfFloat3[1]) * cos1_4);
    arrayOfFloat3[2] += arrayOfFloat3[3];
    arrayOfFloat2[3] = ((arrayOfFloat3[2] - arrayOfFloat3[3]) * cos1_4);
    arrayOfFloat3[4] += arrayOfFloat3[5];
    arrayOfFloat2[5] = ((arrayOfFloat3[4] - arrayOfFloat3[5]) * cos1_4);
    arrayOfFloat3[6] += arrayOfFloat3[7];
    arrayOfFloat2[7] = ((arrayOfFloat3[6] - arrayOfFloat3[7]) * cos1_4);
    arrayOfFloat3[8] += arrayOfFloat3[9];
    arrayOfFloat2[9] = ((arrayOfFloat3[8] - arrayOfFloat3[9]) * cos1_4);
    arrayOfFloat3[10] += arrayOfFloat3[11];
    arrayOfFloat2[11] = ((arrayOfFloat3[10] - arrayOfFloat3[11]) * cos1_4);
    arrayOfFloat3[12] += arrayOfFloat3[13];
    arrayOfFloat2[13] = ((arrayOfFloat3[12] - arrayOfFloat3[13]) * cos1_4);
    arrayOfFloat3[14] += arrayOfFloat3[15];
    arrayOfFloat2[15] = ((arrayOfFloat3[14] - arrayOfFloat3[15]) * cos1_4);
    float tmp1175_1174 = ((arrayOfFloat1[12] = arrayOfFloat2[7]) + arrayOfFloat2[5]);
    arrayOfFloat1[4] = tmp1175_1174;
    arrayOfFloat1[19] = (-tmp1175_1174 - arrayOfFloat2[6]);
    arrayOfFloat1[27] = (-arrayOfFloat2[6] - arrayOfFloat2[7] - arrayOfFloat2[4]);
    float tmp1222_1221 = ((arrayOfFloat1[14] = arrayOfFloat2[15]) + arrayOfFloat2[11]);
    arrayOfFloat1[10] = tmp1222_1221;
    arrayOfFloat1[6] = (tmp1222_1221 + arrayOfFloat2[13]);
    float tmp1249_1248 = (arrayOfFloat2[15] + arrayOfFloat2[13] + arrayOfFloat2[9]);
    arrayOfFloat1[2] = tmp1249_1248;
    arrayOfFloat1[17] = (-tmp1249_1248 - arrayOfFloat2[14]);
    float tmp1281_1280 = (-arrayOfFloat2[14] - arrayOfFloat2[15] - arrayOfFloat2[10] - arrayOfFloat2[11]);
    float f1 = tmp1281_1280;
    arrayOfFloat1[21] = (tmp1281_1280 - arrayOfFloat2[13]);
    arrayOfFloat1[29] = (-arrayOfFloat2[14] - arrayOfFloat2[15] - arrayOfFloat2[12] - arrayOfFloat2[8]);
    arrayOfFloat1[25] = (f1 - arrayOfFloat2[12]);
    arrayOfFloat1[31] = (-arrayOfFloat2[0]);
    arrayOfFloat1[0] = arrayOfFloat2[1];
    float tmp1348_1347 = arrayOfFloat2[3];
    arrayOfFloat1[8] = tmp1348_1347;
    arrayOfFloat1[23] = (-tmp1348_1347 - arrayOfFloat2[2]);
    arrayOfFloat2[0] = ((arrayOfFloat4[0] - arrayOfFloat4[31]) * cos1_64);
    arrayOfFloat2[1] = ((arrayOfFloat4[1] - arrayOfFloat4[30]) * cos3_64);
    arrayOfFloat2[2] = ((arrayOfFloat4[2] - arrayOfFloat4[29]) * cos5_64);
    arrayOfFloat2[3] = ((arrayOfFloat4[3] - arrayOfFloat4[28]) * cos7_64);
    arrayOfFloat2[4] = ((arrayOfFloat4[4] - arrayOfFloat4[27]) * cos9_64);
    arrayOfFloat2[5] = ((arrayOfFloat4[5] - arrayOfFloat4[26]) * cos11_64);
    arrayOfFloat2[6] = ((arrayOfFloat4[6] - arrayOfFloat4[25]) * cos13_64);
    arrayOfFloat2[7] = ((arrayOfFloat4[7] - arrayOfFloat4[24]) * cos15_64);
    arrayOfFloat2[8] = ((arrayOfFloat4[8] - arrayOfFloat4[23]) * cos17_64);
    arrayOfFloat2[9] = ((arrayOfFloat4[9] - arrayOfFloat4[22]) * cos19_64);
    arrayOfFloat2[10] = ((arrayOfFloat4[10] - arrayOfFloat4[21]) * cos21_64);
    arrayOfFloat2[11] = ((arrayOfFloat4[11] - arrayOfFloat4[20]) * cos23_64);
    arrayOfFloat2[12] = ((arrayOfFloat4[12] - arrayOfFloat4[19]) * cos25_64);
    arrayOfFloat2[13] = ((arrayOfFloat4[13] - arrayOfFloat4[18]) * cos27_64);
    arrayOfFloat2[14] = ((arrayOfFloat4[14] - arrayOfFloat4[17]) * cos29_64);
    arrayOfFloat2[15] = ((arrayOfFloat4[15] - arrayOfFloat4[16]) * cos31_64);
    arrayOfFloat2[0] += arrayOfFloat2[15];
    arrayOfFloat2[1] += arrayOfFloat2[14];
    arrayOfFloat2[2] += arrayOfFloat2[13];
    arrayOfFloat2[3] += arrayOfFloat2[12];
    arrayOfFloat2[4] += arrayOfFloat2[11];
    arrayOfFloat2[5] += arrayOfFloat2[10];
    arrayOfFloat2[6] += arrayOfFloat2[9];
    arrayOfFloat2[7] += arrayOfFloat2[8];
    arrayOfFloat3[8] = ((arrayOfFloat2[0] - arrayOfFloat2[15]) * cos1_32);
    arrayOfFloat3[9] = ((arrayOfFloat2[1] - arrayOfFloat2[14]) * cos3_32);
    arrayOfFloat3[10] = ((arrayOfFloat2[2] - arrayOfFloat2[13]) * cos5_32);
    arrayOfFloat3[11] = ((arrayOfFloat2[3] - arrayOfFloat2[12]) * cos7_32);
    arrayOfFloat3[12] = ((arrayOfFloat2[4] - arrayOfFloat2[11]) * cos9_32);
    arrayOfFloat3[13] = ((arrayOfFloat2[5] - arrayOfFloat2[10]) * cos11_32);
    arrayOfFloat3[14] = ((arrayOfFloat2[6] - arrayOfFloat2[9]) * cos13_32);
    arrayOfFloat3[15] = ((arrayOfFloat2[7] - arrayOfFloat2[8]) * cos15_32);
    arrayOfFloat3[0] += arrayOfFloat3[7];
    arrayOfFloat3[1] += arrayOfFloat3[6];
    arrayOfFloat3[2] += arrayOfFloat3[5];
    arrayOfFloat3[3] += arrayOfFloat3[4];
    arrayOfFloat2[4] = ((arrayOfFloat3[0] - arrayOfFloat3[7]) * cos1_16);
    arrayOfFloat2[5] = ((arrayOfFloat3[1] - arrayOfFloat3[6]) * cos3_16);
    arrayOfFloat2[6] = ((arrayOfFloat3[2] - arrayOfFloat3[5]) * cos5_16);
    arrayOfFloat2[7] = ((arrayOfFloat3[3] - arrayOfFloat3[4]) * cos7_16);
    arrayOfFloat3[8] += arrayOfFloat3[15];
    arrayOfFloat3[9] += arrayOfFloat3[14];
    arrayOfFloat3[10] += arrayOfFloat3[13];
    arrayOfFloat3[11] += arrayOfFloat3[12];
    arrayOfFloat2[12] = ((arrayOfFloat3[8] - arrayOfFloat3[15]) * cos1_16);
    arrayOfFloat2[13] = ((arrayOfFloat3[9] - arrayOfFloat3[14]) * cos3_16);
    arrayOfFloat2[14] = ((arrayOfFloat3[10] - arrayOfFloat3[13]) * cos5_16);
    arrayOfFloat2[15] = ((arrayOfFloat3[11] - arrayOfFloat3[12]) * cos7_16);
    arrayOfFloat2[0] += arrayOfFloat2[3];
    arrayOfFloat2[1] += arrayOfFloat2[2];
    arrayOfFloat3[2] = ((arrayOfFloat2[0] - arrayOfFloat2[3]) * cos1_8);
    arrayOfFloat3[3] = ((arrayOfFloat2[1] - arrayOfFloat2[2]) * cos3_8);
    arrayOfFloat2[4] += arrayOfFloat2[7];
    arrayOfFloat2[5] += arrayOfFloat2[6];
    arrayOfFloat3[6] = ((arrayOfFloat2[4] - arrayOfFloat2[7]) * cos1_8);
    arrayOfFloat3[7] = ((arrayOfFloat2[5] - arrayOfFloat2[6]) * cos3_8);
    arrayOfFloat2[8] += arrayOfFloat2[11];
    arrayOfFloat2[9] += arrayOfFloat2[10];
    arrayOfFloat3[10] = ((arrayOfFloat2[8] - arrayOfFloat2[11]) * cos1_8);
    arrayOfFloat3[11] = ((arrayOfFloat2[9] - arrayOfFloat2[10]) * cos3_8);
    arrayOfFloat2[12] += arrayOfFloat2[15];
    arrayOfFloat2[13] += arrayOfFloat2[14];
    arrayOfFloat3[14] = ((arrayOfFloat2[12] - arrayOfFloat2[15]) * cos1_8);
    arrayOfFloat3[15] = ((arrayOfFloat2[13] - arrayOfFloat2[14]) * cos3_8);
    arrayOfFloat3[0] += arrayOfFloat3[1];
    arrayOfFloat2[1] = ((arrayOfFloat3[0] - arrayOfFloat3[1]) * cos1_4);
    arrayOfFloat3[2] += arrayOfFloat3[3];
    arrayOfFloat2[3] = ((arrayOfFloat3[2] - arrayOfFloat3[3]) * cos1_4);
    arrayOfFloat3[4] += arrayOfFloat3[5];
    arrayOfFloat2[5] = ((arrayOfFloat3[4] - arrayOfFloat3[5]) * cos1_4);
    arrayOfFloat3[6] += arrayOfFloat3[7];
    arrayOfFloat2[7] = ((arrayOfFloat3[6] - arrayOfFloat3[7]) * cos1_4);
    arrayOfFloat3[8] += arrayOfFloat3[9];
    arrayOfFloat2[9] = ((arrayOfFloat3[8] - arrayOfFloat3[9]) * cos1_4);
    arrayOfFloat3[10] += arrayOfFloat3[11];
    arrayOfFloat2[11] = ((arrayOfFloat3[10] - arrayOfFloat3[11]) * cos1_4);
    arrayOfFloat3[12] += arrayOfFloat3[13];
    arrayOfFloat2[13] = ((arrayOfFloat3[12] - arrayOfFloat3[13]) * cos1_4);
    arrayOfFloat3[14] += arrayOfFloat3[15];
    arrayOfFloat2[15] = ((arrayOfFloat3[14] - arrayOfFloat3[15]) * cos1_4);
    float tmp2565_2564 = ((arrayOfFloat1[13] = (arrayOfFloat1[15] = arrayOfFloat2[15]) + arrayOfFloat2[7]) + arrayOfFloat2[11]);
    arrayOfFloat1[11] = tmp2565_2564;
    arrayOfFloat1[5] = (tmp2565_2564 + arrayOfFloat2[5] + arrayOfFloat2[13]);
    float tmp2596_2595 = (arrayOfFloat2[15] + arrayOfFloat2[11] + arrayOfFloat2[3]);
    arrayOfFloat1[9] = tmp2596_2595;
    arrayOfFloat1[7] = (tmp2596_2595 + arrayOfFloat2[13]);
    float tmp2630_2629 = ((f1 = arrayOfFloat2[13] + arrayOfFloat2[15] + arrayOfFloat2[9]) + arrayOfFloat2[1]);
    arrayOfFloat1[1] = tmp2630_2629;
    arrayOfFloat1[16] = (-tmp2630_2629 - arrayOfFloat2[14]);
    float tmp2655_2654 = (f1 + arrayOfFloat2[5] + arrayOfFloat2[7]);
    arrayOfFloat1[3] = tmp2655_2654;
    arrayOfFloat1[18] = (-tmp2655_2654 - arrayOfFloat2[6] - arrayOfFloat2[14]);
    float tmp2692_2691 = (-arrayOfFloat2[10] - arrayOfFloat2[11] - arrayOfFloat2[14] - arrayOfFloat2[15]);
    f1 = tmp2692_2691;
    arrayOfFloat1[22] = (tmp2692_2691 - arrayOfFloat2[13] - arrayOfFloat2[2] - arrayOfFloat2[3]);
    arrayOfFloat1[20] = (f1 - arrayOfFloat2[13] - arrayOfFloat2[5] - arrayOfFloat2[6] - arrayOfFloat2[7]);
    arrayOfFloat1[24] = (f1 - arrayOfFloat2[12] - arrayOfFloat2[2] - arrayOfFloat2[3]);
    float tmp2776_2775 = (arrayOfFloat2[4] + arrayOfFloat2[6] + arrayOfFloat2[7]);
    float f2 = tmp2776_2775;
    arrayOfFloat1[26] = (f1 - arrayOfFloat2[12] - tmp2776_2775);
    float tmp2804_2803 = (-arrayOfFloat2[8] - arrayOfFloat2[12] - arrayOfFloat2[14] - arrayOfFloat2[15]);
    f1 = tmp2804_2803;
    arrayOfFloat1[30] = (tmp2804_2803 - arrayOfFloat2[0]);
    arrayOfFloat1[28] = (f1 - f2);
    arrayOfFloat4 = arrayOfFloat1;
    float[] arrayOfFloat5 = actual_v;
    arrayOfFloat5[(0 + actual_write_pos)] = arrayOfFloat4[0];
    arrayOfFloat5[(16 + actual_write_pos)] = arrayOfFloat4[1];
    arrayOfFloat5[(32 + actual_write_pos)] = arrayOfFloat4[2];
    arrayOfFloat5[(48 + actual_write_pos)] = arrayOfFloat4[3];
    arrayOfFloat5[(64 + actual_write_pos)] = arrayOfFloat4[4];
    arrayOfFloat5[(80 + actual_write_pos)] = arrayOfFloat4[5];
    arrayOfFloat5[(96 + actual_write_pos)] = arrayOfFloat4[6];
    arrayOfFloat5[(112 + actual_write_pos)] = arrayOfFloat4[7];
    arrayOfFloat5[(128 + actual_write_pos)] = arrayOfFloat4[8];
    arrayOfFloat5[(144 + actual_write_pos)] = arrayOfFloat4[9];
    arrayOfFloat5[(160 + actual_write_pos)] = arrayOfFloat4[10];
    arrayOfFloat5[(176 + actual_write_pos)] = arrayOfFloat4[11];
    arrayOfFloat5[(192 + actual_write_pos)] = arrayOfFloat4[12];
    arrayOfFloat5[(208 + actual_write_pos)] = arrayOfFloat4[13];
    arrayOfFloat5[(224 + actual_write_pos)] = arrayOfFloat4[14];
    arrayOfFloat5[(240 + actual_write_pos)] = arrayOfFloat4[15];
    arrayOfFloat5[(256 + actual_write_pos)] = 0.0F;
    arrayOfFloat5[(272 + actual_write_pos)] = (-arrayOfFloat4[15]);
    arrayOfFloat5[(288 + actual_write_pos)] = (-arrayOfFloat4[14]);
    arrayOfFloat5[(304 + actual_write_pos)] = (-arrayOfFloat4[13]);
    arrayOfFloat5[(320 + actual_write_pos)] = (-arrayOfFloat4[12]);
    arrayOfFloat5[(336 + actual_write_pos)] = (-arrayOfFloat4[11]);
    arrayOfFloat5[(352 + actual_write_pos)] = (-arrayOfFloat4[10]);
    arrayOfFloat5[(368 + actual_write_pos)] = (-arrayOfFloat4[9]);
    arrayOfFloat5[(384 + actual_write_pos)] = (-arrayOfFloat4[8]);
    arrayOfFloat5[(400 + actual_write_pos)] = (-arrayOfFloat4[7]);
    arrayOfFloat5[(416 + actual_write_pos)] = (-arrayOfFloat4[6]);
    arrayOfFloat5[(432 + actual_write_pos)] = (-arrayOfFloat4[5]);
    arrayOfFloat5[(448 + actual_write_pos)] = (-arrayOfFloat4[4]);
    arrayOfFloat5[(464 + actual_write_pos)] = (-arrayOfFloat4[3]);
    arrayOfFloat5[(480 + actual_write_pos)] = (-arrayOfFloat4[2]);
    arrayOfFloat5[(496 + actual_write_pos)] = (-arrayOfFloat4[1]);
  }
  
  private void compute_pcm_samples0(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(0 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(15 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(14 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(13 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(12 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(11 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(10 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(9 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(8 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(7 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(6 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(5 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(4 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(2 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(1 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples1(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(1 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(0 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(15 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(14 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(13 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(12 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(11 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(10 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(9 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(8 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(7 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(6 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(5 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(4 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(2 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples2(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(2 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(1 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(0 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(15 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(14 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(13 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(12 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(11 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(10 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(9 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(8 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(7 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(6 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(5 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(4 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples3(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    int i = 0;
    float[] arrayOfFloat2 = _tmpOut;
    int j = 0;
    for (int k = 0; k < 32; k++)
    {
      float[] arrayOfFloat3 = d16[k];
      float f = (arrayOfFloat1[(3 + j)] * arrayOfFloat3[0] + arrayOfFloat1[(2 + j)] * arrayOfFloat3[1] + arrayOfFloat1[(1 + j)] * arrayOfFloat3[2] + arrayOfFloat1[(0 + j)] * arrayOfFloat3[3] + arrayOfFloat1[(15 + j)] * arrayOfFloat3[4] + arrayOfFloat1[(14 + j)] * arrayOfFloat3[5] + arrayOfFloat1[(13 + j)] * arrayOfFloat3[6] + arrayOfFloat1[(12 + j)] * arrayOfFloat3[7] + arrayOfFloat1[(11 + j)] * arrayOfFloat3[8] + arrayOfFloat1[(10 + j)] * arrayOfFloat3[9] + arrayOfFloat1[(9 + j)] * arrayOfFloat3[10] + arrayOfFloat1[(8 + j)] * arrayOfFloat3[11] + arrayOfFloat1[(7 + j)] * arrayOfFloat3[12] + arrayOfFloat1[(6 + j)] * arrayOfFloat3[13] + arrayOfFloat1[(5 + j)] * arrayOfFloat3[14] + arrayOfFloat1[(4 + j)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[k] = f;
      j += 16;
    }
  }
  
  private void compute_pcm_samples4(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(4 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(2 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(1 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(0 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(15 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(14 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(13 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(12 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(11 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(10 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(9 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(8 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(7 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(6 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(5 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples5(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(5 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(4 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(2 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(1 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(0 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(15 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(14 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(13 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(12 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(11 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(10 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(9 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(8 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(7 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(6 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples6(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(6 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(5 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(4 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(2 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(1 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(0 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(15 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(14 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(13 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(12 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(11 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(10 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(9 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(8 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(7 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples7(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(7 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(6 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(5 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(4 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(2 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(1 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(0 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(15 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(14 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(13 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(12 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(11 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(10 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(9 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(8 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples8(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(8 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(7 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(6 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(5 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(4 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(2 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(1 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(0 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(15 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(14 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(13 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(12 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(11 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(10 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(9 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples9(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(9 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(8 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(7 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(6 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(5 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(4 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(2 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(1 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(0 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(15 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(14 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(13 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(12 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(11 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(10 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples10(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(10 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(9 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(8 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(7 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(6 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(5 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(4 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(2 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(1 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(0 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(15 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(14 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(13 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(12 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(11 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples11(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(11 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(10 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(9 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(8 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(7 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(6 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(5 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(4 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(2 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(1 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(0 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(15 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(14 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(13 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(12 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples12(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(12 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(11 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(10 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(9 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(8 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(7 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(6 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(5 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(4 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(2 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(1 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(0 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(15 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(14 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(13 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples13(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(13 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(12 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(11 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(10 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(9 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(8 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(7 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(6 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(5 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(4 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(2 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(1 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(0 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(15 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(14 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples14(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(14 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(13 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(12 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(11 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(10 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(9 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(8 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(7 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(6 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(5 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(4 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(2 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(1 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(0 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(15 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples15(Obuffer paramObuffer)
  {
    float[] arrayOfFloat1 = actual_v;
    float[] arrayOfFloat2 = _tmpOut;
    int i = 0;
    for (int j = 0; j < 32; j++)
    {
      float[] arrayOfFloat3 = d16[j];
      float f = (arrayOfFloat1[(15 + i)] * arrayOfFloat3[0] + arrayOfFloat1[(14 + i)] * arrayOfFloat3[1] + arrayOfFloat1[(13 + i)] * arrayOfFloat3[2] + arrayOfFloat1[(12 + i)] * arrayOfFloat3[3] + arrayOfFloat1[(11 + i)] * arrayOfFloat3[4] + arrayOfFloat1[(10 + i)] * arrayOfFloat3[5] + arrayOfFloat1[(9 + i)] * arrayOfFloat3[6] + arrayOfFloat1[(8 + i)] * arrayOfFloat3[7] + arrayOfFloat1[(7 + i)] * arrayOfFloat3[8] + arrayOfFloat1[(6 + i)] * arrayOfFloat3[9] + arrayOfFloat1[(5 + i)] * arrayOfFloat3[10] + arrayOfFloat1[(4 + i)] * arrayOfFloat3[11] + arrayOfFloat1[(3 + i)] * arrayOfFloat3[12] + arrayOfFloat1[(2 + i)] * arrayOfFloat3[13] + arrayOfFloat1[(1 + i)] * arrayOfFloat3[14] + arrayOfFloat1[(0 + i)] * arrayOfFloat3[15]) * scalefactor;
      arrayOfFloat2[j] = f;
      i += 16;
    }
  }
  
  private void compute_pcm_samples(Obuffer paramObuffer)
  {
    switch (actual_write_pos)
    {
    case 0: 
      compute_pcm_samples0(paramObuffer);
      break;
    case 1: 
      compute_pcm_samples1(paramObuffer);
      break;
    case 2: 
      compute_pcm_samples2(paramObuffer);
      break;
    case 3: 
      compute_pcm_samples3(paramObuffer);
      break;
    case 4: 
      compute_pcm_samples4(paramObuffer);
      break;
    case 5: 
      compute_pcm_samples5(paramObuffer);
      break;
    case 6: 
      compute_pcm_samples6(paramObuffer);
      break;
    case 7: 
      compute_pcm_samples7(paramObuffer);
      break;
    case 8: 
      compute_pcm_samples8(paramObuffer);
      break;
    case 9: 
      compute_pcm_samples9(paramObuffer);
      break;
    case 10: 
      compute_pcm_samples10(paramObuffer);
      break;
    case 11: 
      compute_pcm_samples11(paramObuffer);
      break;
    case 12: 
      compute_pcm_samples12(paramObuffer);
      break;
    case 13: 
      compute_pcm_samples13(paramObuffer);
      break;
    case 14: 
      compute_pcm_samples14(paramObuffer);
      break;
    case 15: 
      compute_pcm_samples15(paramObuffer);
    }
    if (paramObuffer != null) {
      paramObuffer.appendSamples(channel, _tmpOut);
    }
  }
  
  public void calculate_pcm_samples(Obuffer paramObuffer)
  {
    compute_new_v();
    compute_pcm_samples(paramObuffer);
    actual_write_pos = (actual_write_pos + 1 & 0xF);
    actual_v = (actual_v == v1 ? v2 : v1);
    for (int i = 0; i < 32; i++) {
      samples[i] = 0.0F;
    }
  }
  
  private static float[] load_d()
  {
    try
    {
      Class localClass = Float.TYPE;
      Object localObject = JavaLayerUtils.deserializeArrayResource("sfd.ser", localClass, 512);
      return (float[])localObject;
    }
    catch (IOException localIOException)
    {
      throw new ExceptionInInitializerError(localIOException);
    }
  }
  
  private static float[][] splitArray(float[] paramArrayOfFloat, int paramInt)
  {
    int i = paramArrayOfFloat.length / paramInt;
    float[][] arrayOfFloat = new float[i][];
    for (int j = 0; j < i; j++) {
      arrayOfFloat[j] = subArray(paramArrayOfFloat, j * paramInt, paramInt);
    }
    return arrayOfFloat;
  }
  
  private static float[] subArray(float[] paramArrayOfFloat, int paramInt1, int paramInt2)
  {
    if (paramInt1 + paramInt2 > paramArrayOfFloat.length) {
      paramInt2 = paramArrayOfFloat.length - paramInt1;
    }
    if (paramInt2 < 0) {
      paramInt2 = 0;
    }
    float[] arrayOfFloat = new float[paramInt2];
    for (int i = 0; i < paramInt2; i++) {
      arrayOfFloat[i] = paramArrayOfFloat[(paramInt1 + i)];
    }
    return arrayOfFloat;
  }
}
