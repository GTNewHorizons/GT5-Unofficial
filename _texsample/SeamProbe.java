// Scratch probe: does the stitched.png cross net fold seamlessly into a cube?
//
// Layout (verified earlier): 4 cols x 3 rows, face cell = 1/4 width x 1/3 height.
//   col 0 row 1 = back, col 1 row 1 = left, col 2 row 1 = front, col 3 row 1 = right,
//   col 2 row 0 = top, col 2 row 2 = bottom.
//
// PART A: for the hypothesized net-fold orientation (each cell drawn so the net folds
// with the printed side OUT), every cube edge must be continuous:
//   FRONT (z-): u along +x, v along -y      BACK (z+): u along -x, v along -y
//   LEFT (x-):  u along -z, v along -y      RIGHT (x+): u along +z, v along -y
//   TOP (y+):   u along +x, v along -z      BOTTOM (y-): u along +x, v along +z
// For each of the 12 cube edges we sample both faces' cells along the shared edge
// (1 px inset, N samples) and compare RGB. CONTROL edges test the OLD mapping on two
// seams so we can see the probe's sensitivity.
//
// PART B: each cell vs its face_*.png original under {identity, hflip, vflip, rot180}
// (informational: which orientation the cell holds relative to the artist's face render).

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.File;

public class SeamProbe {
    static BufferedImage img;
    static int W, H, CW, CH;

    static int[] cellPx(int col, int row, double u, double v) {
        int x = col * CW + (int) Math.round(u * (CW - 1));
        int y = row * CH + (int) Math.round(v * (CH - 1));
        return new int[]{x, y};
    }

    static double[] diffAcc = new double[3];
    static int diffN = 0;

    static void samplePair(int[] a, int[] b) {
        int pa = img.getRGB(a[0], a[1]) & 0xFFFFFF;
        int pb = img.getRGB(b[0], b[1]) & 0xFFFFFF;
        diffAcc[0] += Math.abs(((pa >> 16) & 0xFF) - ((pb >> 16) & 0xFF));
        diffAcc[1] += Math.abs(((pa >> 8) & 0xFF) - ((pb >> 8) & 0xFF));
        diffAcc[2] += Math.abs((pa & 0xFF) - (pb & 0xFF));
        diffN++;
    }

    static double[] finish() {
        double[] m = { diffAcc[0] / diffN, diffAcc[1] / diffN, diffAcc[2] / diffN, (diffAcc[0] + diffAcc[1] + diffAcc[2]) / (3.0 * diffN) };
        diffAcc = new double[3];
        diffN = 0;
        return m;
    }

    static final int F = 0, B = 1, L = 2, R = 3, T = 4, BO = 5;
    static final int[][] CELL = { {2,1}, {0,1}, {1,1}, {3,1}, {2,0}, {2,2} };
    static final String[] NAME = { "FRONT", "BACK", "LEFT", "RIGHT", "TOP", "BOTTOM" };

    public static void main(String[] args) throws Exception {
        String root = args[0];
        String[] planets = { "earth", "jupiter", "normal_rocky_3", "big_gas_2", "small_rock_1", "tiny_rock_1" };
        for (String planet : planets) {
            String dir = root + "/src/main/resources/assets/tectech/textures/uss/planets/" + planet;
            img = ImageIO.read(new File(dir + "/stitched.png"));
            if (img == null) { System.out.println(planet + ": NO stitched.png"); continue; }
            W = img.getWidth(); H = img.getHeight(); CW = W / 4; CH = H / 3;
            System.out.println(planet + ": stitched " + W + "x" + H + " cell " + CW + "x" + CH);
            runEdges();
            runFaceCompare(dir);
        }
    }

    static void runEdges() {
        System.out.println("  PART A - 12 cube edges under the net-fold hypothesis (1px inset):");
        checkRowEdge("1  front|top",       F, 0, T,  1);
        checkRowEdge("2  front|bottom",    F, 1, BO, 0);
        checkColEdge("3  front|left",      F, 0, L,  1);
        checkColEdge("4  front|right",     F, 1, R,  0);
        checkColEdge("5  left|back",       L, 0, B,  1);
        checkColEdge("6  right|back",      R, 1, B,  0);
        checkXEdge("7  top|left",          T,  0, L,  0, true);
        checkXEdge("8  top|right",         T,  1, R,  0, false);
        checkXEdge("9  top|back",          T,  0, B,  0, false);
        checkXEdge("10 bottom|left",       BO, 0, L,  1, false);
        checkXEdge("11 bottom|right",      BO, 1, R,  1, true);
        checkXEdge("12 bottom|back",       BO, 1, B,  1, false);
        // CONTROLS under the OLD mapping (front u-flip, top v-flip):
        checkColEdge("C  front|left(OLD)", F, 1, L,  0);
        checkRowEdge("C2 front|top(OLD)",  F, 0, T,  0);
    }

    // row edge: side1 row v=(mode1==0? inset : 1-inset) at u=s, side2 same at u=s
    static void checkRowEdge(String label, int f1, int mode1, int f2, int mode2) {
        double dv = 1.0 / (CH - 1);
        int n = Math.min(CW, CH) >= 8 ? 12 : 5;
        for (int i = 0; i < n; i++) {
            double s = (i + 0.5) / n;
            int[] p1 = cellPx(CELL[f1][0], CELL[f1][1], s, mode1 == 0 ? dv : 1 - dv);
            int[] p2 = cellPx(CELL[f2][0], CELL[f2][1], s, mode2 == 0 ? dv : 1 - dv);
            samplePair(p1, p2);
        }
        double[] m = finish();
        System.out.printf("    %-22s meanRGB=%.2f  %s%n", label, m[3], m[3] < 3.0 ? "MATCH" : "MISMATCH");
    }

    // col edge: side1 col u=(mode1==0? inset : 1-inset) at v=s, side2 same at v=s
    static void checkColEdge(String label, int f1, int mode1, int f2, int mode2) {
        double du = 1.0 / (CW - 1);
        int n = Math.min(CW, CH) >= 8 ? 12 : 5;
        for (int i = 0; i < n; i++) {
            double s = (i + 0.5) / n;
            int[] p1 = cellPx(CELL[f1][0], CELL[f1][1], mode1 == 0 ? du : 1 - du, s);
            int[] p2 = cellPx(CELL[f2][0], CELL[f2][1], mode2 == 0 ? du : 1 - du, s);
            samplePair(p1, p2);
        }
        double[] m = finish();
        System.out.printf("    %-22s meanRGB=%.2f  %s%n", label, m[3], m[3] < 3.0 ? "MATCH" : "MISMATCH");
    }

    // cross edge: side1 col u=(mode1==0? inset:1-inset) at v=s  vs  side2 row v=(mode2==0? inset:1-inset)
    // at u=(direct ? s : 1-s)
    static void checkXEdge(String label, int f1, int mode1, int f2, int mode2, boolean direct) {
        double du = 1.0 / (CW - 1), dv = 1.0 / (CH - 1);
        int n = Math.min(CW, CH) >= 8 ? 12 : 5;
        for (int i = 0; i < n; i++) {
            double s = (i + 0.5) / n;
            int[] p1 = cellPx(CELL[f1][0], CELL[f1][1], mode1 == 0 ? du : 1 - du, s);
            int[] p2 = cellPx(CELL[f2][0], CELL[f2][1], direct ? s : 1 - s, mode2 == 0 ? dv : 1 - dv);
            samplePair(p1, p2);
        }
        double[] m = finish();
        System.out.printf("    %-22s meanRGB=%.2f  %s%n", label, m[3], m[3] < 3.0 ? "MATCH" : "MISMATCH");
    }

    static void runFaceCompare(String dir) throws Exception {
        System.out.println("  PART B - cell vs face_*.png (best of identity/hflip/vflip/rot180):");
        String[] faceFiles = { "face_front.png", "face_back.png", "face_left.png", "face_right.png", "face_top.png", "face_bottom.png" };
        int[] order = { F, B, L, R, T, BO };
        for (int i = 0; i < 6; i++) {
            BufferedImage face = ImageIO.read(new File(dir + "/" + faceFiles[i]));
            if (face == null) { System.out.println("    " + NAME[i] + ": missing " + faceFiles[i]); continue; }
            if (face.getWidth() != CW || face.getHeight() != CH) {
                BufferedImage scaled = new BufferedImage(CW, CH, BufferedImage.TYPE_INT_ARGB);
                Graphics2D g = scaled.createGraphics();
                g.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
                g.drawImage(face, 0, 0, CW, CH, null);
                g.dispose();
                face = scaled;
            }
            int[] c = CELL[order[i]];
            double[] best = new double[4];
            for (int y = 0; y < CH; y++) {
                for (int x = 0; x < CW; x++) {
                    int pc = img.getRGB(c[0] * CW + x, c[1] * CH + y) & 0xFFFFFF;
                    int[] fx = { x, CW - 1 - x, x, CW - 1 - x };
                    int[] fy = { y, CH - 1 - y, y, CH - 1 - y };
                    for (int o = 0; o < 4; o++) {
                        int pf = face.getRGB(fx[o], fy[o]) & 0xFFFFFF;
                        double d = Math.abs(((pc >> 16) & 0xFF) - ((pf >> 16) & 0xFF))
                                 + Math.abs(((pc >> 8) & 0xFF) - ((pf >> 8) & 0xFF))
                                 + Math.abs((pc & 0xFF) - (pf & 0xFF));
                        best[o] += d;
                    }
                }
            }
            int bi = 0;
            for (int o = 1; o < 4; o++) if (best[o] < best[bi]) bi = o;
            String[] on = { "identity", "hflip", "vflip", "rot180" };
            System.out.printf("    %-7s best=%-8s (%.2f / id %.2f / hf %.2f / vf %.2f / r180 %.2f)%n",
                    NAME[i], on[bi], best[bi] / (3.0 * CW * CH),
                    best[0] / (3.0 * CW * CH), best[1] / (3.0 * CW * CH),
                    best[2] / (3.0 * CW * CH), best[3] / (3.0 * CW * CH));
        }
    }
}
