package gregtech.api.util;

/**
 * Color conversion util, to match vanilla minecraft source code
 * the default color format is ARGB packed into a single integer.
 * This class contains various util methods for converting colors,
 * if not specified in the method name, it assumes the color format
 * for the method parameter or return type is the default : ARGB integer.
 */
@SuppressWarnings("unused")
public final class ColorUtil {

    private ColorUtil() {
        throw new UnsupportedOperationException("ColorUtil is a utility class and cannot be instantiated");
    }

    /**
     * Returns the alpha component of the color.
     *
     * @param argb - color in argb format
     * @return the alpha component of the color (0-255)
     */
    public static int getAlpha(int argb) {
        return (argb >> 24) & 0xFF;
    }

    /**
     * Returns the red component of the color.
     *
     * @param argb - color in argb format
     * @return the red component of the color (0-255)
     */
    public static int getRed(int argb) {
        return (argb >> 16) & 0xFF;
    }

    /**
     * Returns the green component of the color.
     *
     * @param argb - color in argb format
     * @return the green component of the color (0-255)
     */
    public static int getGreen(int argb) {
        return (argb >> 8) & 0xFF;
    }

    /**
     * Returns the blue component of the color.
     *
     * @param argb - color in argb format
     * @return the blue component of the color (0-255)
     */
    public static int getBlue(int argb) {
        return argb & 0xFF;
    }

    /**
     * Returns the alpha component of the color as a float.
     *
     * @param argb - color in argb format
     * @return the alpha component of the color (0-1)
     */
    public static float getAlpha_F(int argb) {
        return ((argb >> 24) & 0xFF) / 255.0F;
    }

    /**
     * Returns the red component of the color as a float.
     *
     * @param argb - color in argb format
     * @return the red component of the color (0-1)
     */
    public static float getRed_F(int argb) {
        return ((argb >> 16) & 0xFF) / 255.0F;
    }

    /**
     * Returns the green component of the color as a float.
     *
     * @param argb - color in argb format
     * @return the green component of the color (0-1)
     */
    public static float getGreen_F(int argb) {
        return ((argb >> 8) & 0xFF) / 255.0F;
    }

    /**
     * Returns the blue component of the color as a float.
     *
     * @param argb - color in argb format
     * @return the blue component of the color (0-1)
     */
    public static float getBlue_F(int argb) {
        return (argb & 0xFF) / 255.0F;
    }

    /**
     * Converts an ARGB color array to an ARGB color integer.
     *
     * @param argb - the color array in ARGB format
     * @return color integer in ARGB format
     */
    public static int fromARGB(byte[] argb) {
        if (argb.length != 4) throw new IllegalArgumentException("ARGB color array must be of length 4!");
        return from(argb[0], argb[1], argb[2], argb[3]);
    }

    /**
     * Converts an ARGB color array to an ARGB color integer.
     *
     * @param argb - the color array in ARGB format
     * @return color integer in ARGB format
     */
    public static int fromARGB(short[] argb) {
        if (argb.length != 4) throw new IllegalArgumentException("ARGB color array must be of length 4!");
        return from(argb[0], argb[1], argb[2], argb[3]);
    }

    /**
     * Converts an ARGB color array to an RGB color integer (alpha component set to 0).
     *
     * @param argb - the color array in ARGB format
     * @return color integer in RGB format
     */
    public static int fromARGBToRGB(byte[] argb) {
        if (argb.length != 4) throw new IllegalArgumentException("ARGB color array must be of length 4!");
        return toRGB(argb[1], argb[2], argb[3]);
    }

    /**
     * Converts an ARGB color array to an RGB color integer (alpha component set to 0).
     *
     * @param argb - the color array in ARGB format
     * @return color integer in RGB format
     */
    public static int fromARGBToRGB(short[] argb) {
        if (argb.length != 4) throw new IllegalArgumentException("ARGB color array must be of length 4!");
        return toRGB(argb[1], argb[2], argb[3]);
    }

    /**
     * Packs ARGB components into a single ARGB color integer.
     *
     * @param a alpha component (0-255)
     * @param r red component (0-255)
     * @param g green component (0-255)
     * @param b blue component (0-255)
     * @return color integer in ARGB format
     */
    public static int from(int a, int r, int g, int b) {
        return ((a & 0xFF) << 24) | ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    /**
     * Converts an RGBA color array to an ARGB color integer.
     *
     * @param rgba - the color array in RGBA format
     * @return color integer in ARGB format
     */
    public static int fromRGBA(byte[] rgba) {
        if (rgba.length != 4) throw new IllegalArgumentException("RGBA color array must be of length 4!");
        return from(rgba[3], rgba[0], rgba[1], rgba[2]);
    }

    /**
     * Converts an RGBA color array to an ARGB color integer.
     *
     * @param rgba - the color array in RGBA format
     * @return color integer in ARGB format
     */
    public static int fromRGBA(short[] rgba) {
        if (rgba.length != 4) throw new IllegalArgumentException("RGBA color array must be of length 4!");
        return from(rgba[3], rgba[0], rgba[1], rgba[2]);
    }

    /**
     * Converts an RGBA color array to an RGB color integer (alpha component set to 0).
     *
     * @param rgba - the color array in RGBA format
     * @return color integer in RGB format
     */
    public static int fromRGBAToRGB(byte[] rgba) {
        if (rgba.length != 4) throw new IllegalArgumentException("RGBA color array must be of length 4!");
        return toRGB(rgba[0], rgba[1], rgba[2]);
    }

    /**
     * Converts an RGBA color array to an RGB color integer (alpha component set to 0).
     *
     * @param rgba - the color array in RGBA format
     * @return color integer in RGB format
     */
    public static int fromRGBAToRGB(short[] rgba) {
        if (rgba.length != 4) throw new IllegalArgumentException("RGBA color array must be of length 4!");
        return toRGB(rgba[0], rgba[1], rgba[2]);
    }

    /**
     * Converts an ARGB color integer to an RGB color integer by removing the alpha component.
     *
     * @param argb - color in argb format
     * @return color in rgb format
     */
    public static int toRGB(int argb) {
        return argb & 0x00FFFFFF;
    }

    /**
     * Packs RGB components into a single RGB color integer (alpha component set to 0).
     *
     * @param r red component (0-255)
     * @param g green component (0-255)
     * @param b blue component (0-255)
     * @return color integer in RGB format
     */
    public static int toRGB(int r, int g, int b) {
        return ((r & 0xFF) << 16) | ((g & 0xFF) << 8) | (b & 0xFF);
    }

    /**
     * Replaces the alpha component of an ARGB color integer.
     *
     * @param argb - color in argb format
     * @return color in argb format
     */
    public static int setAlpha(int argb, int alpha) {
        return (argb & 0x00FFFFFF) | ((alpha & 0xFF) << 24);
    }
}
