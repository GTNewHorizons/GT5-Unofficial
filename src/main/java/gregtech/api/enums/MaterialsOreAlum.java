package gregtech.api.enums;

public class MaterialsOreAlum {

    public static Materials BauxiteSlurry = new MaterialBuilder(409, TextureSet.SET_FLUID, "Bauxite Slurry")
        .setName("BauxiteSlurry")
        .addCell()
        .addFluid()
        .setRGB(37, 67, 168)
        .setMeltingPoint(295)
        .setColor(Dyes.dyeBlue)
        .constructMaterial();
    public static Materials HeatedBauxiteSlurry = new MaterialBuilder(
        410,
        TextureSet.SET_FLUID,
        "Heated Bauxite Slurry").setName("HeadedBauxiteSlurry")
            .addCell()
            .addFluid()
            .setRGB(55, 92, 212)
            .setLiquidTemperature(533)
            .setMeltingPoint(295)
            .setColor(Dyes.dyeBlue)
            .constructMaterial();
    public static Materials SluiceJuice = new MaterialBuilder(411, TextureSet.SET_FLUID, "Sluice Juice")
        .setName("SluiceJuice")
        .addCell()
        .addFluid()
        .setRGB(92, 60, 36)
        .setLiquidTemperature(295)
        .setMeltingPoint(295)
        .setColor(Dyes.dyeGray)
        .constructMaterial();
    public static Materials SluiceSand = new MaterialBuilder(412, TextureSet.SET_FINE, "Sluice Sand")
        .setName("SluiceSand")
        .addDustItems()
        .setRGB(165, 165, 120)
        .setColor(Dyes.dyeGray)
        .constructMaterial();
    public static Materials BauxiteSlag = new MaterialBuilder(413, TextureSet.SET_FINE, "Bauxite Slag")
        .setName("BauxiteSlag")
        .addDustItems()
        .setRGB(110, 31, 31)
        .setColor(Dyes.dyeRed)
        .constructMaterial();
    public static Materials IlmeniteSlag = new MaterialBuilder(414, TextureSet.SET_FINE, "Ilmenite Slag")
        .setName("IlmeniteSlag")
        .addDustItems()
        .setRGB(163, 38, 38)
        .setColor(Dyes.dyeBrown)
        .constructMaterial();
    public static Materials GreenSapphireJuice = new MaterialBuilder(415, TextureSet.SET_FLUID, "Green Sapphire Juice")
        .setName("GreenSapphireJuice")
        .addCell()
        .addFluid()
        .setRGB(100, 200, 130)
        .setColor(Dyes.dyeGreen)
        .constructMaterial();
    public static Materials SapphireJuice = new MaterialBuilder(416, TextureSet.SET_FLUID, "Sapphire Juice")
        .setName("SapphireJuice")
        .addCell()
        .addFluid()
        .setRGB(100, 100, 200)
        .setColor(Dyes.dyeBlue)
        .constructMaterial();
    public static Materials RubyJuice = new MaterialBuilder(417, TextureSet.SET_FLUID, "Ruby Juice")
        .setName("RubyJuice")
        .addCell()
        .addFluid()
        .setRGB(255, 100, 100)
        .setColor(Dyes.dyeRed)
        .constructMaterial();

    /**
     * called by Materials. Can be safely called multiple times. exists to allow Materials ensure this class is
     * initialized
     */
    public static void init() {
        // no-op. all work is done by <clinit>
    }
}
