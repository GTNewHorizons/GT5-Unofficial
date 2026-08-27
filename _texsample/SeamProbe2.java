// Scratch probe v2: for each of the 6 net-adjacent seams in stitched.png,
// find which alignment (direct vs reversed) and inset (0,1,2 px) makes the
// shared physical pixel line continuous. This pins the true per-cell
// content orientation independent of any hypothesis.
//
// Net seams (physical pixel adjacency):
//   front|top   : front row v=0-edge   vs top   row v=1-edge   (along u)
//   front|bottom: front row v=1-edge   vs bottom row v=0-edge  (along u)
//   front|left  : front col u=0-edge   vs left  col u=1-edge   (along v)
//   front|right : front col u=1-edge   vs right col u=0-edge   (along v)
//   left|back   : left  col u=0-edge   vs back  col u=1-edge   (along v)
//   right|back  : right col u=1-edge   vs back  col u=0-edge   (along v)

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class SeamProbe2 {
    static BufferedImage img;
    static int W, H, CW, CH;

    static final int F = 0, B = 1, L = 2, R = 3, T = 4, BO = 5;
    static final int[][] CELL = { {2,1}, {0,1}, {1,1}, {3,1}, {2,0}, {2,2} };
    static final String[] NAME = { "FRONT", "BACK", "LEFT", "RIGHT", "TOP", "BOTTOM" };

    // pixel at cell-local (u,v), u,v in [0,1]; returns -1 out of bounds
    static int px(int col, int row, double u, double v) {
        int x = col * CW + (int) Math.round(u * (CW - 1));
        int y = row * CH + (int) Math.round(v * (CH - 1));
        if (x < 0 || y < 0 || x >= W || y >= H) return -1;
        return img.getRGB(x, y) & 0xFFFFFF;
    }

    static int diff(int a, int b) {
        if (a < 0 || b < 0) return 0;
        return Math.abs(((a >> 16) & 0xFF) - ((b >> 16) & 0xFF))
             + Math.abs(((a >> 8) & 0xFF) - ((b >> 8) & 0xFF))
             + Math.abs((a & 0xFF) - (b & 0xFF));
    }

    public static void main(String[] args) throws Exception {
        String root = args[0];
        String[] planets = { "earth", "jupiter", "normal_rocky_3", "big_gas_2", "small_rock_1", "tiny_rock_1" };
        for (String planet : planets) {
            String dir = root + "/src/main/resources/assets/tectech/textures/uss/planets/" + planet;
            img = ImageIO.read(new File(dir + "/stitched.png"));
            W = img.getWidth(); H = img.getHeight(); CW = W / 4; CH = H / 3;
            System.out.println("== " + planet + " (" + W + "x" + H + ", cell " + CW + "x" + CH + ")");
            seam("front|top  ", F, "row", 0, T,  "row", 1);
            seam("front|bottom", F, "row", 1, BO, "row", 0);
            seam("front|left ", F, "col", 0, L,  "col", 1);
            seam("front|right", F, "col", 1, R,  "col", 0);
            seam("left|back  ", L, "col", 0, B,  "col", 1);
            seam("right|back ", R, "col", 1, B,  "col", 0);
        }
    }

    // one side is a row (fixed v-edge, sweep u) or a col (fixed u-edge, sweep v)
    static void seam(String label, int f1, String m1, int e1, int f2, String m2, int e2) {
        int n = Math.min(CW, CH);
        n = n >= 8 ? 12 : 5;
        for (int inset = 0; inset <= 2; inset++) {
            for (int rev = 0; rev <= 1; rev++) {
                double i1 = inset / (double) (m1.equals("row") ? CH : CW);
                double i2 = inset / (double) (m2.equals("row") ? CH : CW);
                double sum = 0;
                for (int i = 0; i < n; i++) {
                    double s = (i + 0.5) / n;
                    if (rev == 1) s = 1 - s;
                    int a = m1.equals("row") ? px(CELL[f1][0], CELL[f1][1], s, e1 == 0 ? i1 : 1 - i1)
                                             : px(CELL[f1][0], CELL[f1][1], e1 == 0 ? i1 : 1 - i1, s);
                    int b = m2.equals("row") ? px(CELL[f2][0], CELL[f2][1], s, e2 == 0 ? i2 : 1 - i2)
                                             : px(CELL[f2][0], CELL[f2][1], e2 == 0 ? i2 : 1 - i2, s);
                    sum += diff(a, b);
                }
                System.out.printf("    %-14s inset=%d %s        meanRGB=%.2f%n", label, inset,
                        rev == 0 ? "direct  " : "reversed", sum / (3.0 * n));
            }
        }
    }
}
