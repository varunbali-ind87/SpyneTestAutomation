package org.spyne.utilities;

import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class ImageUtils
{
    private ImageUtils()
    {
    }

    public static int getImageWidth(final String imagePath) throws IOException
    {
        var image = ImageIO.read(new File(imagePath));
        return image.getWidth();
    }

    public static int getImageHeight(final String imagePath) throws IOException
    {
        var image = ImageIO.read(new File(imagePath));
        return image.getHeight();
    }
}
