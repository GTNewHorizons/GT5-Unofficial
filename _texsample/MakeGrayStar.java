import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import javax.imageio.ImageIO;

/**
 * Derives the USS star base textures: luminance-grayscale copies of the legacy orange models/StarLayerN.png so the
 * per-class tint reads true. Alpha is preserved; the output is neutral gray (R=G=B).
 */
public class MakeGrayStar {
    public static void main(String[] args) throws Exception {
        File root = new File(args[0], "src" + File.separator + "main" + File.separator + "resources" + File.separator
            + "assets" + File.separator + "tectech");
        File outDir = new File(root, "textures" + File.separator + "uss" + File.separator + "star");
        if (!outDir.isDirectory() && !outDir.mkdirs()) {
            throw new IllegalStateException("cannot create " + outDir);
        }
        for (String n : new String[] { "StarLayer0", "StarLayer1", "StarLayer2" }) {
            BufferedImage im = ImageIO.read(new File(root, "models" + File.separator + n + ".png"));
            int w = im.getWidth(), h = im.getHeight();
            BufferedImage img = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            int aMin = 255, aMax = 0;
            int[] grayHist = new int[16];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int p = im.getRGB(x, y);
                    int a = (p >> 24) & 0xFF;
                    int r = (p >> 16) & 0xFF, g = (p >> 8) & 0xFF, b = p & 0xFF;
                    int lum = (int) Math.round(0.299 * r + 0.587 * g + 0.114 * b);
                    grayHist[Math.min(15, lum * 16 / 256)]++;
                    aMin = Math.min(aMin, a);
                    aMax = Math.max(aMax, a);
                    img.setRGB(x, y, (0xFF000000 | (a << 24) | (lum << 16) | (lum << 8) | lum) & 0xFFFFFFFF);
                }
            }
            File out = new File(outDir, n + ".png");
            ImageIO.write(img, "png", out);
            StringBuilder sb = new StringBuilder();
            for (int v : grayHist) sb.append(String.format("%4d", v)).append(' ');
            System.out.println(n + " -> " + out.getPath() + " (" + out.length() + " bytes) " + w + "x" + h
                + " alpha=" + aMin + "-" + aMax + " hist: " + sb);
        }
    }
}
