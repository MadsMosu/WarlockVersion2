package data.util;

import data.componentdata.Position;

public class Vector2
{
    private float x;
    private float y;

    /**
     * Constructs vector from pos1 to pos2
     *
     * @param pos1 Starting point of vector
     * @param pos2 End point of vector
     */
    public Vector2(Position pos1, Position pos2)
    {
        this.x = pos2.getX() - pos1.getX();
        this.y = pos2.getY() - pos1.getY();
    }

    /**
     * Copies another vector.
     *
     * @param vec The vector to be copied.
     */
    public Vector2(Vector2 vec)
    {
        this.x = vec.x;
        this.y = vec.y;
    }

    /**
     * Creates a vector with offset x and y.
     *
     * @param x The offset x
     * @param y The offset y
     */
    public Vector2(float x, float y)
    {
        this.x = x;
        this.y = y;
    }

    /**
     * Sets the offset x.
     *
     * @param x The offset x.
     */
    public void setX(float x)
    {
        this.x = x;
    }

    /**
     * Rotates the vector counterclockwise if input is positive.
     *
     * @param degrees The degrees to rotate the vector.
     * @return This vector, making it possible to chain calls.
     */
    public Vector2 rotateDegrees(float degrees)
    {
        float radians = (float) Math.toRadians(degrees);
        float px = x * (float)Math.cos(radians) - y * (float)Math.sin(radians);
        float py = x * (float)Math.sin(radians) + y * (float)Math.cos(radians);
        x = px;
        y = py;
        return this;
    }

    /**
     * Sets the offset y.
     *
     * @param y The offset y.
     */
    public void setY(float y)
    {
        this.y = y;
    }

    /**
     * Gets the offset x.
     *
     * @return The offsetx.
     */
    public float getX()
    {
        return this.x;
    }

    /**
     * Gets the offset y.
     *
     * @return The offset y.
     */
    public float getY()
    {
        return this.y;
    }

    /**
     * Gets the magnitude/length of the vector.
     *
     * @return The magnitude/length of the vector.
     */
    public float getMagnitude()
    {
        return (float)Math.sqrt(Math.pow(getX(), 2) + Math.pow(getY(), 2));
    }

    /**
     * Gets the angle of the vector in degrees. For example 90 would mean the vector is pointing NORTH, and 0 would mean the vector is
     * pointing EAST.
     *
     * @return The angle of the vector in degrees. For example 90 would mean the vector is pointing NORTH, and 0 would mean the vector is
     * pointing EAST.
     */
    public float getAngle()
    {
        return (float)Math.toDegrees(Math.atan2(y, x));
    }

    /**
     * Multiplies a vector with another vector.
     *
     * @param multiplier The vector that multiplies this vector.
     * @return The scalar product of the 2 vectors.
     */
    public float scalarProduct(Vector2 multiplier)
    {

        return this.x * multiplier.getX() + this.y * multiplier.getY();
    }

    /**
     * Multiplies this vectors x and y with the input.
     *
     * @param multiplier The amount to multiply the vector with.
     * @return This vector, making it possible to chain calls.
     */
    public Vector2 scalarMultiply(float multiplier)
    {
        this.x *= multiplier;
        this.y *= multiplier;
        return this;
    }

    /**
     * Substract this vector with another vector.
     *
     * @param vector The substracting vector.
     * @return This vector, making it possible to chain calls.
     */
    public Vector2 substractVector(Vector2 vector)
    {
        this.x -= vector.getX();
        this.y -= vector.getY();
        return this;
    }

    /**
     * Adds another vector to this vector.
     *
     * @param vector The added vector.
     * @return This vector, making it possible to chain calls.
     */
    public Vector2 addVector(Vector2 vector)
    {
        this.x += vector.getX();
        this.y += vector.getY();
        return this;
    }

    /**
     * Adds another vector described by the given floats
     * x and y to this vector.
     *
     * @param x
     * @param y
     * @return This vector, making it possible to chain calls.
     */
    public Vector2 addVector(float x, float y)
    {
        this.x += x;
        this.y += y;
        return this;
    }

    /**
     * Substracts one vector with another and returns the result.
     *
     * @param vector1 The vector to be substracted from.
     * @param substractor The substractor vector.
     * @return Returns vector1 substracted with the substractor.
     */
    public static Vector2 substractedVector(Vector2 vector1, Vector2 substractor)
    {
        return new Vector2(vector1.getX() - substractor.getX(), vector1.getY() - substractor.getY());
    }

    /**
     * Addes on vector to another and returns the result.
     *
     * @param vector1 The vector to be added to.
     * @param adder The adder vector.
     * @return The result of vector1+adder.
     */
    public static Vector2 addedVector(Vector2 vector1, Vector2 adder)
    {
        return new Vector2(vector1.getX() + adder.getX(), vector1.getY() + adder.getY());
    }

    /**
     * Normalizes this vector. i.e (2,1) would result in (1, 0.5) and (1,1) would result in (1,1).
     *
     * @return This vector, making it possible to chain calls.
     */
    public Vector2 normalize()
    {
        float length = this.getMagnitude();
        if (!(x == 0 && y == 0))
        {
            x /= length;
            y /= length;
        }
        return this;
    }

    /**
     * Normalizes a vector. i.e (2,1) would result in (1, 0.5) and (1,1) would result in (1,1).
     *
     * @param vector The vector to be normalized
     * @return The vector that was normalized
     */
    public static Vector2 normalize(Vector2 vector)
    {
        float length = vector.getMagnitude();
        return new Vector2(vector.getX() / length, vector.getY() / length);

    }

    @Override
    public String toString()
    {
        return String.format("Vector2 [x=%s, y=%s, magnitude=%s, angle=%s]", x, y, getMagnitude(), getAngle());
    }

    @Override
    public int hashCode()
    {
        final int prime = 31;
        int result = 1;
        long temp;
        temp = Double.doubleToLongBits(x);
        result = prime * result + (int)(temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(y);
        result = prime * result + (int)(temp ^ (temp >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
        {
            return true;
        }
        if (obj == null)
        {
            return false;
        }
        if (getClass() != obj.getClass())
        {
            return false;
        }
        Vector2 other = (Vector2)obj;
        if (!(Math.abs(x - other.x) < 0.00001f))
        {
            return false;
        }
        if (!(Math.abs(y - other.y) < 0.00001f))
        {
            return false;
        }
        return true;
    }

    /**
     * Gets the average of an array of vectors.
     *
     * @param array The array of vectors to get the average from.
     * @return The average vector calculated from the input of vectors.
     */
    public static Vector2 averageVector(Vector2[] array)
    {
        float xTotal = 0;
        float yTotal = 0;
        for (Vector2 vector : array)
        {
            xTotal += vector.getX();
            yTotal += vector.getY();
        }
        if (!(xTotal == 0 && yTotal == 0))
        {
            xTotal /= array.length;
            yTotal /= array.length;
        }

        return new Vector2(xTotal, yTotal);
    }

    /**
     * Sets the magnitude of this vector.
     *
     * @param i The magnitude to be set to this vector.
     * @return This vector, making it possible to chain calls.
     */
    public Vector2 setMagnitude(float i)
    {
        this.normalize();
        this.scalarMultiply(i);
        return this;
    }

    public float projectionOn(Vector2 axis)
    {
        return scalarProduct(Vector2.normalize(axis));
    }

}
