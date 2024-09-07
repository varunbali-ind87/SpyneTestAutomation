package org.spyne.utilities;

import org.apache.commons.io.FileUtils;
import org.awaitility.Awaitility;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;

public class FolderUtils
{
    private FolderUtils()
    {
    }

    public static String getTempDownloadDirectory() throws IOException
    {
        var simpleFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        var date = new Date();
        var folderName = simpleFormat.format(date);
        var downloadPath = Paths.get(System.getProperty("java.io.tmpdir"), folderName);
        FileUtils.forceMkdir(new File(downloadPath.toString()));
        return downloadPath.toString();
    }

    public static String getDownloadedFilePath(final String filePath, final String extension)
    {
        Awaitility.await().atMost(Duration.ofSeconds(10)).until(() ->
        {
            // Get the list of all the files residing the parent directory
            var listOfFiles = FileUtils.listFiles(new File(filePath), new String[] {extension}, true);
            return listOfFiles.isEmpty();
        });

        return FileUtils.listFiles(new File(filePath), new String[] {extension}, true).stream().toList().getFirst().getAbsolutePath();
    }
}
