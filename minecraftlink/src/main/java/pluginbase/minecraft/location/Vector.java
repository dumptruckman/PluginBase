package pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

/**
 * A 3-dimensional immutable mathematical vector.
 * <p/>
 * Any mutator methods in this class will produce a NEW object.
 */
public class Vector extends VectorBase implements Cloneable {

    /**
     * Construct the vector with all components as 0.
     */
    public Vector() {
        super();
    }

    /**
     * Construct the vector with provided integer components.
     *
     * @param x X component.
     * @param y Y component.
     * @param z Z component.
     */
    public Vector(int x, int y, int z) {
        super(x, y, z);
    }

    /**
     * Construct the vector with provided double components.
     *
     * @param x X component.
     * @param y Y component.
     * @param z Z component.
     */
    public Vector(double x, double y, double z) {
        super(x, y, z);
    }

    /**
     * Construct the vector with provided float components.
     *
     * @param x X component.
     * @param y Y component.
     * @param z Z component.
     */
    public Vector(float x, float y, float z) {
        super(x, y, z);
    }

    /** {@inheritDoc} */
    @Override
    public Vector getMidpoint(@NotNull final Coordinates o) {
        return new Vector((getX() + o.getX()) / 2, (getY() + o.getY()) / 2, (getZ() + o.getZ()) / 2);
    }

    /**
     * Adds a point to this one.
     *
     * @param o The other point.
     * @return a new Vector representing the sum.
     */
    public Vector add(@NotNull final Coordinates o) {
        return new Vector(getX() + o.getX(), getY() + o.getY(), getZ() + o.getZ());
    }

    /**
     * Subtracts a point from this one.
     *
     * @param o The other point.
     * @return a new Vector representing the difference.
     */
    public Vector subtract(@NotNull final Coordinates o) {
        return new Vector(getX() - o.getX(), getY() - o.getY(), getZ() - o.getZ());
    }

    /**
     * Multiplies this vector by another point.
     *
     * @param o The other point.
     * @return a new Vector representing the product.
     */
    public Vector multiply(@NotNull final Coordinates o) {
        return new Vector(getX() * o.getX(), getY() * o.getY(), getZ() * o.getZ());
    }

    /**
     * Divides this vector by another point.
     *
     * @param o The other point.
     * @return a new Vector representing the quotient.
     */
    public Vector divide(@NotNull final Coordinates o) {
       return new Vector(getX() / o.getX(), getY() / o.getY(), getZ() / o.getZ());
    }

    /**
     * Creates a new vector representing the midpoint between this vector and another point.
     *
     * @param o The other vector.
     * @return a new Vector representing the midpoint.
     */
    public Vector midpoint(@NotNull final Coordinates o) {
        return new Vector((getX() + o.getX()) / 2, (getY() + o.getY()) / 2, (getZ() + o.getZ()) / 2);
    }

    /**
     * Performs scalar multiplication, multiplying all components with a scalar.
     *
     * @param m The factor.
     * @return a new Vector representing the product.
     */
    public Vector multiply(final int m) {
        return new Vector(getX() * m, getY() * m, getZ() * m);
    }

    /**
     * Performs scalar multiplication, multiplying all components with a scalar.
     *
     * @param m The factor.
     * @return a new Vector representing the product.
     */
    public Vector multiply(final double m) {
        return new Vector(getX() * m, getY() * m, getZ() * m);
    }

    /**
     * Performs scalar multiplication, multiplying all components with a scalar.
     *
     * @param m The factor.
     * @return a new Vector representing the product.
     */
    public Vector multiply(final float m) {
        return new Vector(getX() * m, getY() * m, getZ() * m);
    }

    /**
     * Calculates the cross product of this point with another. The cross
     * product is defined as:
     * <p>
     * x = y1 * z2 - y2 * z1<br/>
     * y = z1 * x2 - z2 * x1<br/>
     * z = x1 * y2 - x2 * y1
     *
     * @param o The other point.
     * @return a new Vector representing the cross product.
     */
    public Vector crossProduct(@NotNull final Coordinates o) {
        double newX = getY() * o.getZ() - o.getY() * getZ();
        double newY = getZ() * o.getX() - o.getZ() * getX();
        double newZ = getX() * o.getY() - o.getX() * getY();

        return new Vector(newX, newY, newZ);
    }

    /**
     * Converts this point to a unit point (a point with length of 1).
     *
     * @return a new normalized point.
     */
    public Vector normalize() {
        double length = length();
        return new Vector(x / length, y / length, z / length);
    }

    /** {@inheritDoc} */
    @Override
    public Vector clone() {
        try {
            return (Vector) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    /** {@inheritDoc} */
    @Override
    public String toString() {
        return String.format("Vector {x: %s, y: %s, z: %s}", getX(), getY(), getZ());
    }

    /**
     * Gets the minimum components of two points.
     *
     * @param v1 The first point.
     * @param v2 The second point.
     * @return a new vector representing the minimum of the two points.
     */
    public static Vector getMinimum(@NotNull final Coordinates v1, @NotNull final Coordinates v2) {
        return new Vector(Math.min(v1.getX(), v2.getX()), Math.min(v1.getY(), v2.getY()), Math.min(v1.getZ(), v2.getZ()));
    }

    /**
     * Gets the maximum components of two points.
     *
     * @param v1 The first point.
     * @param v2 The second point.
     * @return a new vector representing the maximum of the two points.
     */
    public static Vector getMaximum(@NotNull final Coordinates v1, @NotNull final Coordinates v2) {
        return new Vector(Math.max(v1.getX(), v2.getX()), Math.max(v1.getY(), v2.getY()), Math.max(v1.getZ(), v2.getZ()));
    }
}
