package gregtech.loaders.postload;

import gregtech.api.enums.Materials;
import gregtech.loaders.postload.chains.GT_BauxiteRefineChain;
import gregtech.loaders.postload.chains.GT_NaniteChain;
import gregtech.loaders.postload.chains.GT_PCBFactoryRecipes;
import gregtech.loaders.postload.chains.GT_PurifiedWaterRecipes;
import gregtech.loaders.postload.recipes.AlloySmelterRecipes;
import gregtech.loaders.postload.recipes.ArcFurnaceRecipes;
import gregtech.loaders.postload.recipes.AssemblerRecipes;
import gregtech.loaders.postload.recipes.AssemblyLineRecipes;
import gregtech.loaders.postload.recipes.AutoclaveRecipes;
import gregtech.loaders.postload.recipes.BenderRecipes;
import gregtech.loaders.postload.recipes.BlastFurnaceRecipes;
import gregtech.loaders.postload.recipes.BreweryRecipes;
import gregtech.loaders.postload.recipes.CannerRecipes;
import gregtech.loaders.postload.recipes.CentrifugeRecipes;
import gregtech.loaders.postload.recipes.ChemicalBathRecipes;
import gregtech.loaders.postload.recipes.ChemicalRecipes;
import gregtech.loaders.postload.recipes.CircuitAssemblerRecipes;
import gregtech.loaders.postload.recipes.CompressorRecipes;
import gregtech.loaders.postload.recipes.CropProcessingRecipes;
import gregtech.loaders.postload.recipes.CuttingRecipes;
import gregtech.loaders.postload.recipes.DistilleryRecipes;
import gregtech.loaders.postload.recipes.ElectrolyzerRecipes;
import gregtech.loaders.postload.recipes.ElectromagneticSeparatorRecipes;
import gregtech.loaders.postload.recipes.ExtractorRecipes;
import gregtech.loaders.postload.recipes.ExtruderRecipes;
import gregtech.loaders.postload.recipes.FermenterRecipes;
import gregtech.loaders.postload.recipes.FluidCannerRecipes;
import gregtech.loaders.postload.recipes.FluidExtractorRecipes;
import gregtech.loaders.postload.recipes.FluidHeaterRecipes;
import gregtech.loaders.postload.recipes.FluidSolidifierRecipes;
import gregtech.loaders.postload.recipes.ForgeHammerRecipes;
import gregtech.loaders.postload.recipes.FormingPressRecipes;
import gregtech.loaders.postload.recipes.FuelRecipes;
import gregtech.loaders.postload.recipes.FusionReactorRecipes;
import gregtech.loaders.postload.recipes.ImplosionCompressorRecipes;
import gregtech.loaders.postload.recipes.LaserEngraverRecipes;
import gregtech.loaders.postload.recipes.LatheRecipes;
import gregtech.loaders.postload.recipes.MatterAmplifierRecipes;
import gregtech.loaders.postload.recipes.MixerRecipes;
import gregtech.loaders.postload.recipes.NEIHiding;
import gregtech.loaders.postload.recipes.OreDictUnification;
import gregtech.loaders.postload.recipes.PackagerRecipes;
import gregtech.loaders.postload.recipes.PlasmaForgeRecipes;
import gregtech.loaders.postload.recipes.PrinterRecipes;
import gregtech.loaders.postload.recipes.Pulverizer;
import gregtech.loaders.postload.recipes.PyrolyseRecipes;
import gregtech.loaders.postload.recipes.RecipeRemover;
import gregtech.loaders.postload.recipes.SifterRecipes;
import gregtech.loaders.postload.recipes.SlicerRecipes;
import gregtech.loaders.postload.recipes.SmelterRecipes;
import gregtech.loaders.postload.recipes.ThaumcraftRecipes;
import gregtech.loaders.postload.recipes.ThermalCentrifugeRecipes;
import gregtech.loaders.postload.recipes.TranscendentPlasmaMixerRecipes;
import gregtech.loaders.postload.recipes.VacuumFreezerRecipes;
import gregtech.loaders.postload.recipes.WiremillRecipes;

public class GT_MachineRecipeLoader implements Runnable {

    public static final String aTextTCGTPage = "gt.research.page.1.";

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
        GT_PurifiedWaterRecipes.run();
    }
}
