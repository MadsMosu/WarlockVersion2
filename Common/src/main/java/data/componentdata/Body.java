/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package data.componentdata;


public class Body
{
    private int height;
    private int width;
    private Geometry geometry;

    public enum Geometry
    {
        CIRCLE, RECTANGLE
    }

    public Body(int height, int width, Geometry geometry)
    {
        this.height = height;
        this.width = width;
        this.geometry = geometry;
    }

    public Geometry getGeometry()
    {
        return geometry;
    }

    public void setGeometry(Geometry geometry)
    {
        this.geometry = geometry;
    }

    public int getHeight()
    {
        return height;
    }

    public void setHeight(int height)
    {
        this.height = height;
    }

    public int getWidth()
    {
        return width;
    }

    public void setWidth(int width)
    {
        this.width = width;
    }
}
