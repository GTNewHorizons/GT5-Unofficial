package gregtech.api.enums;

public class MaterialsOreAlum {

    public static Materials BauxiteSlurry;
    public static Materials HeatedBauxiteSlurry;
    public static Materials SluiceJuice;
    public static Materials SluiceSand;
    public static Materials BauxiteSlag;
    public static Materials IlmeniteSlag;
    public static Materials GreenSapphireJuice;
    public static Materials SapphireJuice;
    public static Materials RubyJuice;

    public static void load() {
        MaterialsOreAlum.BauxiteSlurry = loadBauxiteSlurry();
        MaterialsOreAlum.HeatedBauxiteSlurry = loadHeatedBauxiteSlurry();
        MaterialsOreAlum.SluiceJuice = loadSluiceJuice();
        MaterialsOreAlum.SluiceSand = loadSluiceSand();
        MaterialsOreAlum.BauxiteSlag = loadBauxiteSlag();
        MaterialsOreAlum.IlmeniteSlag = loadIlmeniteSlag();
        MaterialsOreAlum.GreenSapphireJuice = loadGreenSapphireJuice();
        MaterialsOreAlum.SapphireJuice = loadSapphireJuice();
        MaterialsOreAlum.RubyJuice = loadRubyJuice();
    }

    private static Materials loadBauxiteSlurry() {
        return new MaterialBuilder(409, TextureSet.SET_FLUID, "Bauxite Slurry").setName("BauxiteSlurry")
            .addCell()
            .addFluid()
            .setRGB(37, 67, 168)
            .setMeltingPoint(295)
            .setColor(Dyes.dyeBlue)
            .constructMaterial();
    }

    private static Materials loadHeatedBauxiteSlurry() {
        return new MaterialBuilder(410, TextureSet.SET_FLUID, "Heated Bauxite Slurry").setName("HeadedBauxiteSlurry")
            .addCell()
            .addFluid()
            .setRGB(55, 92, 212)
            .setMeltingPoint(295)
            .setColor(Dyes.dyeBlue)
            .constructMaterial();
    }

    private static Materials loadSluiceJuice() {
        return new MaterialBuilder(411, TextureSet.SET_FLUID, "Sluice Juice").setName("SluiceJuice")
            .addCell()
            .addFluid()
            .setRGB(92, 60, 36)
            .setMeltingPoint(295)
            .setColor(Dyes.dyeGray)
            .constructMaterial();
    }

    private static Materials loadSluiceSand() {
        return new MaterialBuilder(412, TextureSet.SET_FINE, "Sluice Sand").setName("SluiceSand")
            .addDustItems()
            .setRGB(165, 165, 120)
            .setColor(Dyes.dyeGray)
            .constructMaterial();
    }

    private static Materials loadBauxiteSlag() {
        return new MaterialBuilder(413, TextureSet.SET_FINE, "Bauxite Slag").setName("BauxiteSlag")
            .addDustItems()
            .setRGB(110, 31, 31)
            .setColor(Dyes.dyeRed)
            .constructMaterial();
    }

    private static Materials loadIlmeniteSlag() {
        return new MaterialBuilder(414, TextureSet.SET_FINE, "Ilmenite Slag").setName("IlmeniteSlag")
            .addDustItems()
            .setRGB(163, 38, 38)
            .setColor(Dyes.dyeBrown)
            .constructMaterial();
    }

    private static Materials loadGreenSapphireJuice() {
        return new MaterialBuilder(415, TextureSet.SET_FLUID, "Green Sapphire Juice").setName("GreenSapphireJuice")
            .addCell()
            .addFluid()
            .setRGB(100, 200, 130)
            .setColor(Dyes.dyeGreen)
            .constructMaterial();
    }

    private static Materials loadSapphireJuice() {
        return new MaterialBuilder(416, TextureSet.SET_FLUID, "Sapphire Juice").setName("SapphireJuice")
            .addCell()
            .addFluid()
            .setRGB(100, 100, 200)
            .setColor(Dyes.dyeBlue)
            .constructMaterial();
    }

    private static Materials loadRubyJuice() {
        return new MaterialBuilder(417, TextureSet.SET_FLUID, "Ruby Juice").setName("RubyJuice")
            .addCell()
            .addFluid()
            .setRGB(255, 100, 100)
            .setColor(Dyes.dyeRed)
            .constructMaterial();
    }
}
