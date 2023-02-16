package gregtech.loaders.postload;

import static gregtech.api.enums.GT_Values.*;

import cpw.mods.fml.common.Loader;
import gregtech.loaders.postload.chains.GT_BauxiteRefineChain;
import gregtech.loaders.postload.chains.GT_NaniteChain;
import gregtech.loaders.postload.chains.GT_PCBFactoryRecipes;
import gregtech.loaders.postload.recipes.*;

public class GT_MachineRecipeLoader implements Runnable {

    public static final String aTextAE = "appliedenergistics2";
    public static final String aTextAEMM = "item.ItemMultiMaterial";
    public static final String aTextForestry = "Forestry";
    public static final String aTextEBXL = "ExtrabiomesXL";
    public static final String aTextTCGTPage = "gt.research.page.1.";
    public static final Boolean isNEILoaded = Loader.isModLoaded("NotEnoughItems");
    public static final Boolean isThaumcraftLoaded = Loader.isModLoaded("Thaumcraft");
    public static final Boolean isBartWorksLoaded = Loader.isModLoaded("bartworks");
    public static final Boolean isGTNHLanthanidLoaded = Loader.isModLoaded("gtnhlanth");
    public static final Boolean isGTPPLoaded = Loader.isModLoaded(MOD_ID_GTPP);
    public static final Boolean isGalaxySpaceLoaded = Loader.isModLoaded("GalaxySpace");
    public static final Boolean isGalacticraftMarsLoaded = Loader.isModLoaded("GalacticraftMars");
    public static final Boolean isIronChestLoaded = Loader.isModLoaded("IronChest");
    public static final Boolean isCoremodLoaded = Loader.isModLoaded(MOD_ID_DC);
    public static final Boolean isBuildCraftFactoryLoaded = Loader.isModLoaded("BuildCraft|Factory");
    public static final Boolean isIronTankLoaded = Loader.isModLoaded("irontank");
    public static final Boolean isExtraUtilitiesLoaded = Loader.isModLoaded("ExtraUtilities");
    public static final Boolean isEBXLLoaded = Loader.isModLoaded(GT_MachineRecipeLoader.aTextEBXL);
    public static final Boolean isRailcraftLoaded = Loader.isModLoaded(MOD_ID_RC);
    public static final Boolean isForestryloaded = Loader.isModLoaded(GT_MachineRecipeLoader.aTextForestry);
    public static final Boolean isIC2NuclearControlLoaded = Loader.isModLoaded("IC2NuclearControl");
    public static final Boolean isAvaritiaAddonsLoaded = Loader.isModLoaded("avaritiaddons");
    public static final Boolean isTinkersConstructLoaded = Loader.isModLoaded("TConstruct");
    public static final Boolean isHardcoreEnderExpansionLoaded = Loader.isModLoaded("HardcoreEnderExpansion");
    public static final Boolean isForestryLoaded = Loader.isModLoaded(MOD_ID_FR);
    /** @deprecated AE2 is a required dependency now */
    @Deprecated
    public static final Boolean isAE2Loaded = true;

    @Override
    public void run() {
        new AlloySmelterRecipes().run();
        new ArcFurnaceRecipes().run();
        new AssemblerRecipes().run();
        new AssemblyLineRecipes().run();
        new AutoclaveRecipes().run();
        new BenderRecipes().run();
        new BlastFurnaceRecipes().run();
        new BreweryRecipes().run();
        new CannerRecipes().run();
        new CentrifugeRecipes().run();
        new ChemicalBathRecipes().run();
        new ChemicalRecipes().run();
        new CircuitAssemblerRecipes().run();
        new CompressorRecipes().run();
        new CropProcessingRecipes().run();
        new CuttingRecipes().run();
        new DistilleryRecipes().run();
        new ElectrolyzerRecipes().run();
        new ElectromagneticSeparatorRecipes().run();
        new ExtractorRecipes().run();
        new ExtruderRecipes().run();
        new FermenterRecipes().run();
        new FluidCannerRecipes().run();
        new FluidExtractorRecipes().run();
        new FluidHeaterRecipes().run();
        new FluidSolidifierRecipes().run();
        new ForgeHammerRecipes().run();
        new FormingPressRecipes().run();
        new FuelRecipes().run();
        new FusionReactorRecipes().run();
        new ImplosionCompressorRecipes().run();
        new LaserEngraverRecipes().run();
        new LatheRecipes().run();
        new MatterAmplifierRecipes().run();
        new MixerRecipes().run();
        new NEIHiding().run();
        new OreDictUnification().run();
        new PackagerRecipes().run();
        new PlasmaForgeRecipes().run();
        new PrinterRecipes().run();
        new Pulverizer().run();
        new PyrolyseRecipes().run();
        new RecipeRemover().run();
        new SifterRecipes().run();
        new SlicerRecipes().run();
        new SmelterRecipes().run();
        new ThaumcraftRecipes().run();
        new ThermalCentrifugeRecipes().run();
        new VacuumFreezerRecipes().run();
        new WiremillRecipes().run();
        new TranscendentPlasmaMixerRecipes().run();

        GT_BauxiteRefineChain.run();
        GT_NaniteChain.run();
        GT_PCBFactoryRecipes.load();
    }
}
