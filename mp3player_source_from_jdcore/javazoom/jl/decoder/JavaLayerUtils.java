package javazoom.jl.decoder;

import java.io.IOException;
import java.io.InputStream;
import java.io.InvalidClassException;
import java.io.InvalidObjectException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;

public class JavaLayerUtils
{
  private static JavaLayerHook hook = null;
  
  public JavaLayerUtils() {}
  
  public static Object deserialize(InputStream paramInputStream, Class paramClass)
    throws IOException
  {
    if (paramClass == null) {
      throw new NullPointerException("cls");
    }
    Object localObject = deserialize(paramInputStream, paramClass);
    if (!paramClass.isInstance(localObject)) {
      throw new InvalidObjectException("type of deserialized instance not of required class.");
    }
    return localObject;
  }
  
  public static Object deserialize(InputStream paramInputStream)
    throws IOException
  {
    if (paramInputStream == null) {
      throw new NullPointerException("in");
    }
    ObjectInputStream localObjectInputStream = new ObjectInputStream(paramInputStream);
    Object localObject;
    try
    {
      localObject = localObjectInputStream.readObject();
    }
    catch (ClassNotFoundException localClassNotFoundException)
    {
      throw new InvalidClassException(localClassNotFoundException.toString());
    }
    return localObject;
  }
  
  public static Object deserializeArray(InputStream paramInputStream, Class paramClass, int paramInt)
    throws IOException
  {
    if (paramClass == null) {
      throw new NullPointerException("elemType");
    }
    if (paramInt < -1) {
      throw new IllegalArgumentException("length");
    }
    Object localObject = deserialize(paramInputStream);
    Class localClass1 = localObject.getClass();
    if (!localClass1.isArray()) {
      throw new InvalidObjectException("object is not an array");
    }
    Class localClass2 = localClass1.getComponentType();
    if (localClass2 != paramClass) {
      throw new InvalidObjectException("unexpected array component type");
    }
    if (paramInt != -1)
    {
      int i = Array.getLength(localObject);
      if (i != paramInt) {
        throw new InvalidObjectException("array length mismatch");
      }
    }
    return localObject;
  }
  
  public static Object deserializeArrayResource(String paramString, Class paramClass, int paramInt)
    throws IOException
  {
    InputStream localInputStream = getResourceAsStream(paramString);
    if (localInputStream == null) {
      throw new IOException("unable to load resource '" + paramString + "'");
    }
    Object localObject = deserializeArray(localInputStream, paramClass, paramInt);
    return localObject;
  }
  
  public static void serialize(OutputStream paramOutputStream, Object paramObject)
    throws IOException
  {
    if (paramOutputStream == null) {
      throw new NullPointerException("out");
    }
    if (paramObject == null) {
      throw new NullPointerException("obj");
    }
    ObjectOutputStream localObjectOutputStream = new ObjectOutputStream(paramOutputStream);
    localObjectOutputStream.writeObject(paramObject);
  }
  
  public static synchronized void setHook(JavaLayerHook paramJavaLayerHook)
  {
    hook = paramJavaLayerHook;
  }
  
  public static synchronized JavaLayerHook getHook()
  {
    return hook;
  }
  
  public static synchronized InputStream getResourceAsStream(String paramString)
  {
    InputStream localInputStream = null;
    if (hook != null)
    {
      localInputStream = hook.getResourceAsStream(paramString);
    }
    else
    {
      Class localClass = JavaLayerUtils.class;
      localInputStream = localClass.getResourceAsStream(paramString);
    }
    return localInputStream;
  }
}
