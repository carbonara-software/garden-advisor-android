package com.carbonara.gardenadvisor.util;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import com.carbonara.gardenadvisor.util.AppUtil.Constants;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Test;

public class AppUtilTest {

  @Test
  public void testConstants() {
    assertEquals("ai-cam-", Constants.PICTURE_PREFIX);
  }

  @Test
  public void writeToFileTest() throws IOException {
    Context context = mock(Context.class);
    byte[] content = {1, 2, 3};

    FileOutputStream fileOutputStream = mock(FileOutputStream.class);

    when(context.openFileOutput(eq("test-path"), eq(Context.MODE_PRIVATE)))
        .thenReturn(fileOutputStream);

    AppUtil.writeToFile(context, content, "test-path");

    verify(fileOutputStream).write(content);
    verify(fileOutputStream).flush();
  }

  @Test
  public void writeBitmapToFile() throws IOException {
    Context context = mock(Context.class);
    Bitmap bitmap = mock(Bitmap.class);

    FileOutputStream fileOutputStream = mock(FileOutputStream.class);

    when(context.openFileOutput(eq("test-path"), eq(Context.MODE_PRIVATE)))
        .thenReturn(fileOutputStream);

    AppUtil.writeBitmapToFile(context, bitmap, "test-path");

    verify(bitmap).compress(eq(CompressFormat.PNG), eq(90), eq(fileOutputStream));
  }
}
