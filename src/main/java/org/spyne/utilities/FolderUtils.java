package org.spyne.utilities;

import org.apache.commons.io.FileUtils;
import org.awaitility.Awaitility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class FolderUtils
{
    public static String tempDownloadDirectory;

    private FolderUtils()
    {
    }

    public static String createTempDownloadDirectory() throws IOException
    {
        var simpleFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        var date = new Date();
        var folderName = simpleFormat.format(date);
        var downloadPath = Paths.get(System.getProperty("java.io.tmpdir"), folderName);
        FileUtils.forceMkdir(new File(downloadPath.toString()));
        tempDownloadDirectory = downloadPath.toString();
        return tempDownloadDirectory;
    }

    public static String getDownloadedFilePath(final String filePath, final String extension)
    {
        Awaitility.await().atMost(Duration.ofSeconds(10)).until(() ->
        {
            var directoryPath = new File(filePath);
            File[] file = directoryPath.listFiles();
            return file != null && file.length > 0;
        });

        return Arrays.stream(Objects.requireNonNull(new File(filePath).listFiles())).toList().getFirst().getAbsolutePath();
    }
}
