import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class StarTexProbe {
    public static void main(String[] args) throws Exception {
        String root = args[0] + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator
            + "assets" + File.separator + "tectech" + File.separator + "models";
        for (String n : new String[] { "StarLayer0", "StarLayer1", "StarLayer2" }) {
            BufferedImage im = ImageIO.read(new File(root + File.separator + n + ".png"));
            int w = im.getWidth(), h = im.getHeight();
            int aMin = 255, aMax = 0, aSum = 0;
            int rMin = 255, gMin = 255, bMin = 255, rMax = 0, gMax = 0, bMax = 0;
            long lumMax = 0, lumSum = 0; int nPx = 0;
            // histogram of luminance in 16 bins
            int[] hist = new int[16];
            for (int y = 0; y < h; y++) for (int x = 0; x < w; x++) {
                int p = im.getRGB(x, y);
                int a = (p >> 24) & 0xFF, r = (p >> 16) & 0xFF, g = (p >> 8) & 0xFF, b = p & 0xFF;
                aMin = Math.min(aMin, a); aMax = Math.max(aMax, a); aSum += a;
                rMin = Math.min(rMin, r); gMin = Math.min(gMin, g); bMin = Math.min(bMin, b);
                rMax = Math.max(rMax, r); gMax = Math.max(gMax, g); bMax = Math.max(bMax, b);
                long lum = (long) (0.299 * r + 0.587 * g + 0.114 * b);
                lumMax = Math.max(lumMax, lum); lumSum += lum;
                hist[(int) Math.min(15, lum * 16 / 256)]++;
                nPx++;
            }
            System.out.println(n + ": " + w + "x" + h + " type=" + im.getType());
            System.out.println("  alpha: min=" + aMin + " max=" + aMax + " avg=" + (aSum / (double) nPx));
            System.out.println("  R " + rMin + "-" + rMax + "  G " + gMin + "-" + gMax + "  B " + bMin + "-" + bMax);
            System.out.println("  lum max=" + lumMax + " avg=" + (lumSum / (double) nPx));
            StringBuilder sb = new StringBuilder("  hist: ");
            for (int i = 0; i < 16; i++) sb.append(String.format("%4d", hist[i])).append(' ');
            System.out.println(sb);
        }
    }
}
