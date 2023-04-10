package gregtech.loaders.postload;

import static gregtech.api.enums.Mods.NotEnoughItems;

import gregtech.api.enums.Materials;
import gregtech.loaders.postload.chains.GT_BauxiteRefineChain;
import gregtech.loaders.postload.chains.GT_NaniteChain;
import gregtech.loaders.postload.chains.GT_PCBFactoryRecipes;
import gregtech.loaders.postload.recipes.*;

public class GT_MachineRecipeLoader implements Runnable {

    public static final String aTextTCGTPage = "gt.research.page.1.";
    public static final Boolean isNEILoaded = NotEnoughItems.isModLoaded();

    /** @deprecated AE2 is a required dependency now */
    @Deprecated
    public static final Boolean isAE2Loaded = true;

    public static final Materials[] solderingMats = new Materials[] { Materials.Lead, Materials.SolderingAlloy,
        Materials.Tin };

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
