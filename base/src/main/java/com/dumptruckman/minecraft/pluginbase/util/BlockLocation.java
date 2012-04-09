package com.dumptruckman.minecraft.pluginbase.util;

public class BlockLocation {

    private static final String DELIMITER = "_";

    private final String world;
    private final int x;
    private final int y;
    private final int z;

    private final String stringForm;

    BlockLocation(String world, int x, int y, int z) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.stringForm = this.x + DELIMITER + this.y + DELIMITER + this.z + DELIMITER + this.world;
    }

    public final String getWorld() {
        return world;
    }

    public final int getX() {
        return x;
    }

    public final int getY() {
        return y;
    }

    public final int getZ() {
        return z;
    }

    @Override
    public final String toString() {
        return stringForm;
    }

    @Override
    public final boolean equals(Object o) {
        if (o instanceof BlockLocation) {
            BlockLocation otherLoc = (BlockLocation) o;
            if (this.getWorld().equals(otherLoc.getWorld())
                    && this.getX() == otherLoc.getX()
                    && this.getY() == otherLoc.getY()
                    && this.getZ() == otherLoc.getZ())  {
                return true;
            }
        }
        return false;
    }

    @Override
    public final int hashCode() {
        return this.toString().hashCode();
    }

    public static BlockLocation valueOf(String stringFormat) {
        String[] sections = stringFormat.split(DELIMITER, 4);
        if (sections.length != 4) {
            Logging.finer("Unable to parse location: " + stringFormat);
            return null;
        }
        try {
            return new BlockLocation(sections[3],
                    Integer.valueOf(sections[0]),
                    Integer.valueOf(sections[1]),
                    Integer.valueOf(sections[2]));
        } catch (Exception e) {
            Logging.finer("Unable to parse location: " + stringFormat);
            return null;
        }
    }
}