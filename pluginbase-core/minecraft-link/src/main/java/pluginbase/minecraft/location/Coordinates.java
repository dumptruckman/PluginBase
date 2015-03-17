package pluginbase.minecraft.location;

import org.jetbrains.annotations.NotNull;

/**
 * Represents an unchanging point in 3-dimensional space.
 */
public interface Coordinates extends Cloneable {

    /**
     * Gets the x coordinate of this point.
     *
     * @return The x coordinate of this point.
     */
    double getX();

    /**
     * Gets the block x coordinate represented by this point.
     *
     * @return The block x coordinate represented by this point.
     */
    int getBlockX();

    /**
     * Gets the y coordinate of this point.
     *
     * @return The y coordinate of this point.
     */
    double getY();

    /**
     * Gets the block y coordinate represented by this point.
     *
     * @return The block y coordinate represented by this point.
     */
    int getBlockY();

    /**
     * Gets the z coordinate of this point.
     *
     * @return The z coordinate of this point.
     */
    double getZ();

    /**
     * Gets the block z coordinate represented by this point.
     *
     * @return The block z coordinate represented by this point.
     */
    int getBlockZ();

    /**
     * Gets the magnitude of this point, defined as sqrt(x^2+y^2+z^2). The value
     * of this method is not cached and uses a costly square-root function, so
     * do not repeatedly call this method to get the vector's magnitude. NaN
     * will be returned if the inner result of the sqrt() function overflows,
     * which will be caused if the length is too long.
     *
     * @return the magnitude.
     */
    double length();

    /**
     * Gets the magnitude of this point squared.
     *
     * @return the magnitude squared.
     */
    double lengthSquared();

    /**
     * Get the distance between this point and another. The value
     * of this method is not cached and uses a costly square-root function, so
     * do not repeatedly call this method to get the vector's magnitude. NaN
     * will be returned if the inner result of the sqrt() function overflows,
     * which will be caused if the distance is too long.
     *
     * @param o The other point.
     * @return the distance.
     */
    double distance(@NotNull final Coordinates o);

    /**
     * Get the squared distance between this point and another.
     *
     * @param o The other point.
     * @return the distance squared.
     */
    double distanceSquared(@NotNull final Coordinates o);

    /**
     * Gets the angle between this point and another in radians.
     *
     * @param o The other point.
     * @return angle in radians.
     */
    float angle(@NotNull final Coordinates o);

    /**
     * Calculates the dot product of this point with another. The dot product
     * is defined as x1*x2+y1*y2+z1*z2. The returned value is a scalar.
     *
     * @param o The other point.
     * @return dot product.
     */
    double dot(@NotNull final Coordinates o);

    /**
     * Gets a new midpoint coordinates between this point and another.
     *
     * @param o The other point
     * @return a new midpoint vector
     */
    Coordinates getMidpoint(@NotNull final Coordinates o);

    /**
     * Returns whether this point is in an axis-aligned bounding box.
     * The minimum and maximum vectors given must be truly the minimum and
     * maximum X, Y and Z components.
     *
     * @param min minimum point.
     * @param max maximum point.
     * @return whether this vector is in the AABB.
     */
    boolean isInAABB(@NotNull final Coordinates min, @NotNull final Coordinates max);

    /**
     * Returns whether this point is within a sphere.
     *
     * @param origin Sphere origin.
     * @param radius Sphere radius
     * @return whether this point is in the sphere.
     */
    boolean isInSphere(@NotNull final Coordinates origin, final double radius);

    /**
     * Creates an immutable copy of this object.
     *
     * @return an immutable copy of this object.
     */
    Coordinates immutableCopy();

    /**
     * Creates a mutable copy of this object.
     *
     * @return a mutable copy of this object.
     */
    MutableCoordinates mutableCopy();

    /**
     * Creates a copy of this object as a new immutable vector object.
     *
     * @return a copy of this object as a new immutable vector object.
     */
    Vector toImmutableVector();

    /**
     * Creates a copy of this object as a new mutable vector object.
     *
     * @return a copy of this object as a new mutable vector object.
     */
    MutableVector toMutableVector();
}
