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
        return new MaterialBuilder().setName("BauxiteSlurry")
            .setDefaultLocalName("Bauxite Slurry")
            .setMetaItemSubID(409)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x2543a8)
            .addCell()
            .addFluid()
            .setMeltingPoint(295)
            .constructMaterial();
    }

    private static Materials loadHeatedBauxiteSlurry() {
        return new MaterialBuilder().setName("HeadedBauxiteSlurry")
            .setDefaultLocalName("Heated Bauxite Slurry")
            .setMetaItemSubID(410)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x375cd4)
            .addCell()
            .addFluid()
            .setMeltingPoint(295)
            .constructMaterial();
    }

    private static Materials loadSluiceJuice() {
        return new MaterialBuilder().setName("SluiceJuice")
            .setDefaultLocalName("Sluice Juice")
            .setMetaItemSubID(411)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGray)
            .setRGB(0x5c3c24)
            .addCell()
            .addFluid()
            .setMeltingPoint(295)
            .constructMaterial();
    }

    private static Materials loadSluiceSand() {
        return new MaterialBuilder().setName("SluiceSand")
            .setDefaultLocalName("Sluice Sand")
            .setMetaItemSubID(412)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeGray)
            .setRGB(0xa5a578)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadBauxiteSlag() {
        return new MaterialBuilder().setName("BauxiteSlag")
            .setDefaultLocalName("Bauxite Slag")
            .setMetaItemSubID(413)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeRed)
            .setRGB(0x6e1f1f)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadIlmeniteSlag() {
        return new MaterialBuilder().setName("IlmeniteSlag")
            .setDefaultLocalName("Ilmenite Slag")
            .setMetaItemSubID(414)
            .setIconSet(TextureSet.SET_FINE)
            .setColor(Dyes.dyeBrown)
            .setRGB(0xa32626)
            .addDustItems()
            .constructMaterial();
    }

    private static Materials loadGreenSapphireJuice() {
        return new MaterialBuilder().setName("GreenSapphireJuice")
            .setDefaultLocalName("Green Sapphire Juice")
            .setMetaItemSubID(415)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeGreen)
            .setRGB(0x64c882)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadSapphireJuice() {
        return new MaterialBuilder().setName("SapphireJuice")
            .setDefaultLocalName("Sapphire Juice")
            .setMetaItemSubID(416)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeBlue)
            .setRGB(0x6464c8)
            .addCell()
            .addFluid()
            .constructMaterial();
    }

    private static Materials loadRubyJuice() {
        return new MaterialBuilder().setName("RubyJuice")
            .setDefaultLocalName("Ruby Juice")
            .setMetaItemSubID(417)
            .setIconSet(TextureSet.SET_FLUID)
            .setColor(Dyes.dyeRed)
            .setRGB(0xff6464)
            .addCell()
            .addFluid()
            .constructMaterial();
    }
}
