package com.carbonara.gardenadvisor.util;

import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import android.content.Context;
import java.io.FileOutputStream;
import java.io.IOException;
import org.junit.Test;

public class AppUtilTest {

  @Test
  public void writeToFileTest() throws IOException {
    Context context = mock(Context.class);
    byte[] content = {1, 2, 3};

    FileOutputStream fileOutputStream = mock(FileOutputStream.class);

    when(context.openFileOutput(eq("test-path"), Context.MODE_PRIVATE))
        .thenReturn(fileOutputStream);

    AppUtil.writeToFile(context, content, "test-path");

    verify(fileOutputStream).write(content);
    verify(fileOutputStream).flush();
  }
}
