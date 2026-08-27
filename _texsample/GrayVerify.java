import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class GrayVerify {
    public static void main(String[] args) throws Exception {
        File dir = new File(args[0], "src" + File.separator + "main" + File.separator + "resources" + File.separator
            + "assets" + File.separator + "tectech" + File.separator + "textures" + File.separator + "uss"
            + File.separator + "star");
        boolean allOk = true;
        for (String n : new String[] { "StarLayer0", "StarLayer1", "StarLayer2" }) {
            BufferedImage im = ImageIO.read(new File(dir, n + ".png"));
            long bad = 0, aBad = 0;
            int lumMin = 255, lumMax = 0;
            for (int y = 0; y < im.getHeight(); y++) {
                for (int x = 0; x < im.getWidth(); x++) {
                    int p = im.getRGB(x, y);
                    int a = (p >> 24) & 0xFF, r = (p >> 16) & 0xFF, g = (p >> 8) & 0xFF, b = p & 0xFF;
                    if (r != g || g != b) bad++;
                    if (a != 255) aBad++;
                    lumMin = Math.min(lumMin, r);
                    lumMax = Math.max(lumMax, r);
                }
            }
            boolean ok = bad == 0 && aBad == 0;
            allOk &= ok;
            System.out.println(n + ": " + im.getWidth() + "x" + im.getHeight() + " nonNeutral=" + bad
                + " alphaNonOpaque=" + aBad + " grayRange=" + lumMin + "-" + lumMax + (ok ? "  OK" : "  FAIL"));
        }
        System.out.println(allOk ? "ALL PASS" : "FAILED");
    }
}
