package org.spyne.dataprovider;

import org.spyne.constants.ResourceConstants;
import org.testng.annotations.DataProvider;

import java.nio.file.Paths;

public class ImageProvider
{
    @DataProvider(name = "imageprovider")
    public Object[][] dataprovider()
    {
        return new Object[][]{{String.valueOf(Paths.get(ResourceConstants.RESOURCES_DIR, "dianthus-pink-and-white-flower-pinks-pixabay.jpg"))},
                {String.valueOf(Paths.get(ResourceConstants.RESOURCES_DIR, "flowers_name_in_english.jpg"))},
                {String.valueOf(Paths.get(ResourceConstants.RESOURCES_DIR, "sacred-lotus-gettyimages.png"))}};
    }
}
