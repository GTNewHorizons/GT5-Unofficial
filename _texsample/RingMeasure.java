import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/** Measures the annulus geometry (inner/outer radius in pixels, alpha profile) of the USS ring textures. */
public class RingMeasure {
    public static void main(String[] args) throws Exception {
        File root = new File("src/main/resources/assets/tectech/textures/uss/rings");
        List<File> files = new ArrayList<File>();
        collect(root, files);
        Collections.sort(files, new Comparator<File>() {
            public int compare(File a, File b) {
                return a.getPath().compareTo(b.getPath());
            }
        });
        System.out.println("count=" + files.size());
        for (File f : files) {
            BufferedImage img = ImageIO.read(f);
            int w = img.getWidth();
            int h = img.getHeight();
            double cx = (w - 1) / 2.0;
            double cy = (h - 1) / 2.0;
            double minR = Double.MAX_VALUE;
            double maxR = -1;
            int opaque = 0;
            int transparent = 0;
            int semi = 0;
            int centerAlpha = img.getRGB((int) Math.round(cx), (int) Math.round(cy)) >>> 24;
            int cornerAlpha = img.getRGB(0, 0) >>> 24;
            int[] buckets = new int[10];
            for (int y = 0; y < h; y++) {
                for (int x = 0; x < w; x++) {
                    int a = img.getRGB(x, y) >>> 24;
                    if (a == 0) {
                        transparent++;
                    } else if (a == 255) {
                        opaque++;
                    } else {
                        semi++;
                    }
                    if (a > 0) {
                        double r = Math.hypot(x - cx, y - cy);
                        if (r < minR) minR = r;
                        if (r > maxR) maxR = r;
                        int b = (int) Math.min(9, r / (w / 2.0) * 10);
                        buckets[b]++;
                    }
                }
            }
            String path = f.getPath().replace("src/main/resources/assets/tectech/textures/uss/rings/", "");
            System.out.printf("%-10s %3dx%-3d centerA=%3d cornerA=%3d minR=%5.1fpx (%.3fu) maxR=%5.1fpx (%.3fu) opaque=%d semi=%d transp=%d",
                path, w, h, centerAlpha, cornerAlpha, minR, minR / (w / 2.0), maxR, maxR / (w / 2.0), opaque, semi, transparent);
            StringBuilder hist = new StringBuilder("  hist[");
            for (int i = 0; i < 10; i++) hist.append(buckets[i]).append(i < 9 ? "," : "");
            hist.append("]");
            System.out.println(hist);
        }
    }

    static void collect(File dir, List<File> out) {
        File[] kids = dir.listFiles();
        if (kids == null) return;
        for (File k : kids) {
            if (k.isDirectory()) collect(k, out);
            else if (k.getName().endsWith(".png")) out.add(k);
        }
    }
}
