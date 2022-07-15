// package goodgenerator.items.nuclear;
//
// public class IsotopeMaterialLoader implements Runnable {
//
//    protected static final int OffsetID = 0;
//
//    public static final IsotopeMaterial Thorium232 = new IsotopeMaterial(
//            OffsetID,
//            "Thorium232", "Thorium", "Thorium-232",
//            NuclearTextures.STABLE1, 59, 59, 59,
//            232
//    );
//
//    public static final IsotopeMaterial Thorium230 = new IsotopeMaterial(
//            OffsetID + 1,
//            "Thorium232", "Thorium", "Thorium-230",
//            NuclearTextures.STABLE2, 59, 59, 59,
//            230
//    );
//
//    public static final IsotopeMaterial Uranium233 = new IsotopeMaterial(
//            OffsetID + 2,
//            "Uranium233", "Uranium", "Uranium-233",
//            NuclearTextures.UNSTABLE4, 60, 167, 85,
//            233
//    );
//
//    public static final IsotopeMaterial Uranium235 = new IsotopeMaterial(
//            OffsetID + 3,
//            "Uranium235", "Uranium", "Uranium-235",
//            NuclearTextures.UNSTABLE3, 60, 167, 85,
//            235
//    );
//
//    public static final IsotopeMaterial Uranium238 = new IsotopeMaterial(
//            OffsetID + 4,
//            "Uranium238", "Uranium", "Uranium-238",
//            NuclearTextures.STABLE2, 60, 167, 85,
//            238
//    );
//
//    public static final IsotopeMaterial Neptunium236 = new IsotopeMaterial(
//            OffsetID + 5,
//            "Neptunium236", "Neptunium", "Neptunium-236",
//            NuclearTextures.UNSTABLE1, 60, 170, 176,
//            236
//    );
//
//    public static final IsotopeMaterial Neptunium237 = new IsotopeMaterial(
//            OffsetID + 6,
//            "Neptunium237", "Neptunium", "Neptunium-237",
//            NuclearTextures.STABLE2, 60, 170, 176,
//            237
//    );
//
//    public static final IsotopeMaterial Plutonium238 = new IsotopeMaterial(
//            OffsetID + 7,
//            "Plutonium238", "Plutonium", "Plutonium-238",
//            NuclearTextures.STABLE1, 169, 169, 169,
//            238
//    );
//
//    public static final IsotopeMaterial Plutonium239 = new IsotopeMaterial(
//            OffsetID + 8,
//            "Plutonium239", "Plutonium", "Plutonium-239",
//            NuclearTextures.UNSTABLE1, 169, 169, 169,
//            239
//    );
//
//    public static final IsotopeMaterial Plutonium241 = new IsotopeMaterial(
//            OffsetID + 9,
//            "Plutonium241", "Plutonium", "Plutonium-241",
//            NuclearTextures.UNSTABLE2, 169, 169, 169,
//            241
//    );
//
//    public static final IsotopeMaterial Plutonium242 = new IsotopeMaterial(
//            OffsetID + 10,
//            "Plutonium242", "Plutonium", "Plutonium-242",
//            NuclearTextures.STABLE2, 169, 169, 169,
//            242
//    );
//
//    public static final IsotopeMaterial Americium241 = new IsotopeMaterial(
//            OffsetID + 11,
//            "Americium241", "Americium", "Americium-241",
//            NuclearTextures.STABLE1, 150, 120, 22,
//            241
//    );
//
//    public static final IsotopeMaterial Americium242 = new IsotopeMaterial(
//            OffsetID + 12,
//            "Americium242", "Americium", "Americium-242",
//            NuclearTextures.UNSTABLE4, 150, 120, 22,
//            242
//    );
//
//    public static final IsotopeMaterial Americium243 = new IsotopeMaterial(
//            OffsetID + 13,
//            "Americium243", "Americium", "Americium-243",
//            NuclearTextures.STABLE2, 150, 120, 22,
//            243
//    );
//
//    public static final IsotopeMaterial Curium243 = new IsotopeMaterial(
//            OffsetID + 14,
//            "Curium243", "Curium", "Curium-243",
//            NuclearTextures.UNSTABLE1, 107, 6, 105,
//            243
//    );
//
//    public static final IsotopeMaterial Curium245 = new IsotopeMaterial(
//            OffsetID + 15,
//            "Curium245", "Curium", "Curium-245",
//            NuclearTextures.UNSTABLE3, 107, 6, 105,
//            245
//    );
//
//    public static final IsotopeMaterial Curium246 = new IsotopeMaterial(
//            OffsetID + 16,
//            "Curium246", "Curium", "Curium-246",
//            NuclearTextures.STABLE2, 107, 6, 105,
//            246
//    );
//
//    public static final IsotopeMaterial Curium247 = new IsotopeMaterial(
//            OffsetID + 17,
//            "Curium247", "Curium", "Curium-247",
//            NuclearTextures.UNSTABLE4, 107, 6, 105,
//            247
//    );
//
//    public static final IsotopeMaterial Berkelium247 = new IsotopeMaterial(
//            OffsetID + 18,
//            "Berkelium247", "Berkelium", "Berkelium-247",
//            NuclearTextures.STABLE2, 130, 54, 29,
//            247
//    );
//
//    public static final IsotopeMaterial Berkelium248 = new IsotopeMaterial(
//            OffsetID + 19,
//            "Berkelium248", "Berkelium", "Berkelium-248",
//            NuclearTextures.UNSTABLE1, 130, 54, 29,
//            248
//    );
//
//    public static final IsotopeMaterial Californium249 = new IsotopeMaterial(
//            OffsetID + 20,
//            "Californium249", "Californium", "Californium-249",
//            NuclearTextures.UNSTABLE2, 186, 55, 11,
//            249
//    );
//
//    public static final IsotopeMaterial Californium250 = new IsotopeMaterial(
//            OffsetID + 21,
//            "Californium250", "Californium", "Californium-250",
//            NuclearTextures.STABLE1, 186, 55, 11,
//            250
//    );
//
//    public static final IsotopeMaterial Californium251 = new IsotopeMaterial(
//            OffsetID + 22,
//            "Californium251", "Californium", "Californium-251",
//            NuclearTextures.UNSTABLE4, 186, 55, 11,
//            251
//    );
//
//    public static final IsotopeMaterial Californium252 = new IsotopeMaterial(
//            OffsetID + 23,
//            "Californium252", "Californium", "Californium-252",
//            NuclearTextures.UNSTABLE1, 186, 55, 11,
//            252
//    );
//
//    @Override
//    public void run() { }
// }
