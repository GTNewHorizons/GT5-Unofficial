// Verifies the corrected per-face UV table of EOHRenderingUtils against the
// net-geometry requirement: each face's PHYSICAL cell edges (the net lines) must
// map to the cube edge they sit on when the cross net is folded.
//
// Net lines (physical image adjacency) and their cube edges:
//   front left  -> (x=-0.5, z=-0.5)   front right -> (x=+0.5, z=-0.5)
//   front top   -> (y=+0.5, z=-0.5)   front bottom-> (y=-0.5, z=-0.5)
//   left right  -> (x=-0.5, z=-0.5)   left left   -> (x=-0.5, z=+0.5)
//   right left  -> (x=+0.5, z=-0.5)   right right -> (x=+0.5, z=+0.5)
//   back right  -> (x=-0.5, z=+0.5)   back left   -> (x=+0.5, z=+0.5)
//   top bottom  -> (y=+0.5, z=-0.5)   top top     -> (y=+0.5, z=+0.5)
//   top left    -> (x=-0.5, y=+0.5)   top right   -> (x=+0.5, y=+0.5)
//   bottom top  -> (y=-0.5, z=-0.5)   bottom bottom-> (y=-0.5, z=+0.5)
//   bottom left -> (x=-0.5, y=-0.5)   bottom right-> (x=+0.5, y=-0.5)
//
// Corner table (same as the renderer): index -> (x, y, z) in +-0.5
public class UVVerify {
    static final int[][] CORNERS = {
        { 1, 0, 7, 6 }, // left   (x=-0.5)
        { 5, 2, 1, 6 }, // bottom (y=-0.5)
        { 6, 7, 4, 5 }, // front  (z=-0.5)
        { 5, 4, 3, 2 }, // right  (x=+0.5)
        { 3, 4, 7, 0 }, // top    (y=+0.5)
        { 2, 3, 0, 1 }  // back   (z=+0.5)
    };
    static final String[] NAME = { "LEFT", "BOTTOM", "FRONT", "RIGHT", "TOP", "BACK" };
    static final int[][] U = {
        { 0, 0, 1, 1 }, // left
        { 1, 1, 0, 0 }, // bottom
        { 0, 0, 1, 1 }, // front
        { 0, 0, 1, 1 }, // right
        { 1, 1, 0, 0 }, // top
        { 0, 0, 1, 1 }  // back
    };
    static final int[][] V = {
        { 1, 0, 0, 1 }, // left
        { 0, 1, 1, 0 }, // bottom
        { 1, 0, 0, 1 }, // front
        { 1, 0, 0, 1 }, // right
        { 0, 1, 1, 0 }, // top
        { 1, 0, 0, 1 }  // back
    };
    static final float[] X = { -0.5f, -0.5f, 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f };
    static final float[] Y = { 0.5f, -0.5f, -0.5f, 0.5f, 0.5f, -0.5f, -0.5f, 0.5f };
    static final float[] Z = { 0.5f, 0.5f, 0.5f, 0.5f, -0.5f, -0.5f, -0.5f, -0.5f };

    static int fails = 0;

    // expected: for each face, (leftEdgeXorZ, rightEdgeXorZ, topYorZ, bottomYorZ, axis)
    static final float[][] EXP = {
        // face,   left,  right, top,   bottom, whichAxis(0=x,1=y,2=z)
        { -0.5f, +0.5f, +0.5f, -0.5f, 0f }, // LEFT:   left z=+0.5 ... wait axis is z
    };

    public static void main(String[] args) {
        // expected edge coordinate per face: [left, right, top, bottom] along the face's sweep axis
        // LEFT (x=-0.5): edges vary z: left->z=+0.5, right->z=-0.5 ; top/bottom vary y: +0.5/-0.5
        check("LEFT",   -0.5f, new float[]{ +0.5f, -0.5f }, new float[]{ +0.5f, -0.5f }, 2); // (leftZ, rightZ), (topY, botY)
        check("BOTTOM", -0.5f, new float[]{ -0.5f, +0.5f }, new float[]{ -0.5f, +0.5f }, 2); // (leftZ, rightZ) -> bottom left x? see below
        // note: BOTTOM/LEFT/etc: left/right edges are along the face's horizontal image axis;
        // for bottom (y fixed) the horizontal axis is x... encode per-face below instead.
        System.out.println("see per-face explicit checks below");
        explicitChecks();
        System.out.println(fails == 0 ? "ALL PASS" : (fails + " FAILURES"));
        if (fails > 0) System.exit(1);
    }

    static void check(String name, float fixed, float[] lr, float[] tb, int sweep) {
        // placeholder (superseded by explicitChecks)
    }

    static void explicitChecks() {
        // For each face: world position of each image corner under the table, then edge checks.
        for (int f = 0; f < 6; f++) {
            float[][] imgCorners = new float[4][4]; // [i][xyz]
            for (int i = 0; i < 4; i++) {
                int c = CORNERS[f][i];
                imgCorners[i][0] = X[c]; imgCorners[i][1] = Y[c]; imgCorners[i][2] = Z[c];
            }
            // image corner slots: TL = (u=m,v=m), TR=(u=M,v=m), BR=(u=M,v=M), BL=(u=m,v=M)
            int tl = -1, tr = -1, br = -1, bl = -1;
            for (int i = 0; i < 4; i++) {
                int u = U[f][i], v = V[f][i];
                if (u == 0 && v == 0) tl = i;
                if (u == 1 && v == 0) tr = i;
                if (u == 1 && v == 1) br = i;
                if (u == 0 && v == 1) bl = i;
            }
            System.out.printf("%-7s TL(%5.1f,%5.1f,%5.1f) TR(%5.1f,%5.1f,%5.1f) BR(%5.1f,%5.1f,%5.1f) BL(%5.1f,%5.1f,%5.1f)%n",
                    NAME[f],
                    imgCorners[tl][0], imgCorners[tl][1], imgCorners[tl][2],
                    imgCorners[tr][0], imgCorners[tr][1], imgCorners[tr][2],
                    imgCorners[br][0], imgCorners[br][1], imgCorners[br][2],
                    imgCorners[bl][0], imgCorners[bl][1], imgCorners[bl][2]);
        }
        // Edge assertions: (face, imgEdge, axis, expected)
        // imgEdge: 0=left(TL,BL) 1=right(TR,BR) 2=top(TL,TR) 3=bottom(BL,BR)
        assertEdge(2, 0, 0, -0.5f); assertEdge(2, 1, 0, +0.5f); assertEdge(2, 2, 1, +0.5f); assertEdge(2, 3, 1, -0.5f); // FRONT
        assertEdge(5, 0, 0, +0.5f); assertEdge(5, 1, 0, -0.5f); assertEdge(5, 2, 1, +0.5f); assertEdge(5, 3, 1, -0.5f); // BACK
        assertEdge(0, 0, 2, +0.5f); assertEdge(0, 1, 2, -0.5f); assertEdge(0, 2, 1, +0.5f); assertEdge(0, 3, 1, -0.5f); // LEFT
        assertEdge(3, 0, 2, -0.5f); assertEdge(3, 1, 2, +0.5f); assertEdge(3, 2, 1, +0.5f); assertEdge(3, 3, 1, -0.5f); // RIGHT
        assertEdge(4, 0, 0, -0.5f); assertEdge(4, 1, 0, +0.5f); assertEdge(4, 2, 2, +0.5f); assertEdge(4, 3, 2, -0.5f); // TOP
        assertEdge(1, 0, 0, -0.5f); assertEdge(1, 1, 0, +0.5f); assertEdge(1, 2, 2, -0.5f); assertEdge(1, 3, 2, +0.5f); // BOTTOM
    }

    static void assertEdge(int f, int imgEdge, int axis, float expected) {
        int a = imgEdge == 0 || imgEdge == 2 ? tlBlOf(f, imgEdge, axis) : 0; // computed below instead
        // gather the two corner indices of the image edge
        int[] slots = edgeCorners(f, imgEdge);
        boolean ok = true;
        for (int i : slots) {
            int c = CORNERS[f][i];
            float w = axis == 0 ? X[c] : axis == 1 ? Y[c] : Z[c];
            if (Math.abs(w - expected) > 1e-6) ok = false;
        }
        String[] edgeNames = { "left", "right", "top", "bottom" };
        String[] axNames = { "x", "y", "z" };
        String edgeName = edgeNames[imgEdge];
        String axName = axNames[axis];
        if (ok) System.out.printf("  PASS %-7s image-%-6s edge sits on %s=%.1f%n", NAME[f], edgeName, axName, expected);
        else { System.out.printf("  FAIL %-7s image-%-6s edge NOT on %s=%.1f%n", NAME[f], edgeName, axName, expected); fails++; }
    }

    static int[] edgeCorners(int f, int imgEdge) {
        // find corner indices by (u,v) slot
        int tl = -1, tr = -1, br = -1, bl = -1;
        for (int i = 0; i < 4; i++) {
            int u = U[f][i], v = V[f][i];
            if (u == 0 && v == 0) tl = i;
            if (u == 1 && v == 0) tr = i;
            if (u == 1 && v == 1) br = i;
            if (u == 0 && v == 1) bl = i;
        }
        switch (imgEdge) {
            case 0: return new int[]{ tl, bl };
            case 1: return new int[]{ tr, br };
            case 2: return new int[]{ tl, tr };
            default: return new int[]{ bl, br };
        }
    }

    static int tlBlOf(int f, int e, int a) { return 0; }
}
