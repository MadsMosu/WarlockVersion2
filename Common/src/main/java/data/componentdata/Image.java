/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.componentdata;


public class Image
{
    private final String imageFile;
    private final boolean repeatImage;

    public Image(String path, boolean repeatImage)
    {
        this.imageFile = path;
        this.repeatImage = repeatImage;
    }

    public String getImageFilePath()
    {
        return imageFile;
    }

    public boolean isRepeat()
    {
        return repeatImage;
    }

}
