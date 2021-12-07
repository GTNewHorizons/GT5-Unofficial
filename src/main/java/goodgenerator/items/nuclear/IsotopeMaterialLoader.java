package goodgenerator.items.nuclear;

public class IsotopeMaterialLoader implements Runnable {

    protected static final int OffsetID = 0;

    public static final IsotopeMaterial Thorium232 = new IsotopeMaterial(
            OffsetID,
            "Thorium232", "Thorium", "Thorium-232",
            NuclearTextures.STABLE1, 59, 59, 59,
            232
    );

    @Override
    public void run() { }
}
