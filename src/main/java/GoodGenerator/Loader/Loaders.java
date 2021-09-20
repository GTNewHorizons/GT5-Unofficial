package GoodGenerator.Loader;

import GoodGenerator.Blocks.MyFluids.FluidsBuilder;
import GoodGenerator.Blocks.RegularBlock.Casing;
import GoodGenerator.Blocks.RegularBlock.ComplexTextureCasing;
import GoodGenerator.Blocks.RegularBlock.Frame;
import GoodGenerator.Blocks.RegularBlock.TEBlock;
import GoodGenerator.Blocks.TEs.*;
import GoodGenerator.Blocks.TEs.MetaTE.NeutronAccelerator;
import GoodGenerator.Blocks.TEs.MetaTE.NeutronSensor;
import GoodGenerator.CrossMod.NEI.IMCForNEI;
import GoodGenerator.Items.MyItemBlocks;
import GoodGenerator.Items.MyItems;
import GoodGenerator.Items.RadioactiveItem;
import GoodGenerator.Main.GoodGenerator;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import static GoodGenerator.util.DescTextLocalization.addText;

public class Loaders {

    public static final int IDOffset = 32001;
    public static final byte GoodGeneratorTexturePage = 12;

    public static final Item radiationProtectionPlate = new MyItems("radiationProtectionPlate", GoodGenerator.GG);
    public static final Item wrappedUraniumIngot = new MyItems("wrappedUraniumIngot", GoodGenerator.GG);
    public static final Item highDensityUraniumNugget = new RadioactiveItem("highDensityUraniumNugget", GoodGenerator.GG, 200);
    public static final Item highDensityUranium = new RadioactiveItem("highDensityUranium", GoodGenerator.GG, 1800);
    public static final Item wrappedThoriumIngot = new MyItems("wrappedThoriumIngot", GoodGenerator.GG);
    public static final Item highDensityThoriumNugget = new RadioactiveItem("highDensityThoriumNugget", GoodGenerator.GG, 50);
    public static final Item highDensityThorium = new RadioactiveItem("highDensityThorium", GoodGenerator.GG, 450);
    public static final Item wrappedPlutoniumIngot = new MyItems("wrappedPlutoniumIngot", GoodGenerator.GG);
    public static final Item highDensityPlutoniumNugget = new RadioactiveItem("highDensityPlutoniumNugget", GoodGenerator.GG, 450);
    public static final Item highDensityPlutonium = new RadioactiveItem("highDensityPlutonium", GoodGenerator.GG, 4050);
    public static final Item rawAtomicSeparationCatalyst = new MyItems("rawAtomicSeparationCatalyst", GoodGenerator.GG);
    public static final Item advancedRadiationProtectionPlate = new MyItems("advancedRadiationProtectionPlate", GoodGenerator.GG);
    public static final Item aluminumNitride = new MyItems("aluminumNitride", "AlN", GoodGenerator.GG);
    public static final Item specialCeramics = new MyItems("specialCeramics", GoodGenerator.GG);
    public static final Item specialCeramicsPlate = new MyItems("specialCeramicsPlate", GoodGenerator.GG);
    public static final Item radioactiveWaste = new RadioactiveItem("radioactiveWaste", GoodGenerator.GG, 400);
    public static final Item plasticCase = new MyItems("plasticCase", GoodGenerator.GG);
    public static final Item quartzWafer = new MyItems("quartzWafer", GoodGenerator.GG);
    public static final Item microHeater = new MyItems("microHeater", GoodGenerator.GG);
    public static final Item quartzCrystalResonator = new MyItems("quartzCrystalResonator", GoodGenerator.GG);
    public static final Item inverter = new MyItems("inverter", addText("inverter.tooltip", 1), GoodGenerator.GG);
    public static final Item neutronSource = new MyItems("neutronSource", GoodGenerator.GG);
    public static final Item naquadahMass = new MyItems("naquadahMass", addText("naquadahMass.tooltip", 1), GoodGenerator.GG);
    public static final Item enrichedNaquadahMass = new MyItems("enrichedNaquadahMass", addText("enrichedNaquadahMass.tooltip", 1), GoodGenerator.GG);
    public static final Item naquadriaMass = new MyItems("naquadriaMass", addText("naquadriaMass.tooltip", 1), GoodGenerator.GG);
    public static final Item advancedFuelRod = new MyItems("advancedFuelRod", GoodGenerator.GG);
    public static final Item fluidCore = new MyItems("fluidCore", GoodGenerator.GG, new String[]{GoodGenerator.MOD_ID+":fluidCore/1", GoodGenerator.MOD_ID+":fluidCore/2", GoodGenerator.MOD_ID+":fluidCore/3", GoodGenerator.MOD_ID+":fluidCore/4", GoodGenerator.MOD_ID+":fluidCore/5", GoodGenerator.MOD_ID+":fluidCore/6"});

    public static final Block MAR_Casing = new Casing("MAR_Casing", new String[]{GoodGenerator.MOD_ID+":MAR_Casing"});
    public static final Block FRF_Casings = new Casing("FRF_Casing", new String[]{"gregtech:iconsets/MACHINE_CASING_MINING_BLACKPLUTONIUM"});
    public static final Block FRF_Coil_1 = new Casing("FRF_Coil_1", new String[]{GoodGenerator.MOD_ID+":FRF_Coils/1"});
    public static final Block FRF_Coil_2 = new Casing("FRF_Coil_2", new String[]{GoodGenerator.MOD_ID+":FRF_Coils/2"});
    public static final Block FRF_Coil_3 = new Casing("FRF_Coil_3", new String[]{GoodGenerator.MOD_ID+":FRF_Coils/3"});
    public static final Block radiationProtectionSteelFrame = new Frame("radiationProtectionSteelFrame", new String[]{GoodGenerator.MOD_ID+":radiationProtectionSteelFrame"});
    public static final Block fieldRestrictingGlass = new Frame("fieldRestrictingGlass", new String[]{GoodGenerator.MOD_ID+":fieldRestrictingGlass"});
    public static final Block rawCylinder = new Casing("rawCylinder", new String[]{GoodGenerator.MOD_ID+":rawCylinder"});
    public static final Block titaniumPlatedCylinder = new Casing("titaniumPlatedCylinder", new String[]{GoodGenerator.MOD_ID+":titaniumPlatedCylinder"});
    public static final Block magicCasing = new Casing("magicCasing", new String[]{GoodGenerator.MOD_ID+":MagicCasing"});
    public static final Block essentiaCell = new Casing("essentiaCell", new String[]{GoodGenerator.MOD_ID+":essentiaCell/1",GoodGenerator.MOD_ID+":essentiaCell/2",GoodGenerator.MOD_ID+":essentiaCell/3"});
    public static final Block speedingPipe = new ComplexTextureCasing("speedingPipe", new String[]{GoodGenerator.MOD_ID+":speedingPipe_SIDE"}, new String[]{GoodGenerator.MOD_ID+":speedingPipe_TOP"});
    public static final Block yottaFluidTankCell = new Casing("yottaFluidTankCell", new String[]{GoodGenerator.MOD_ID+":yottaFluidTankCell/1", GoodGenerator.MOD_ID+":yottaFluidTankCell/2", GoodGenerator.MOD_ID+":yottaFluidTankCell/3", GoodGenerator.MOD_ID+":yottaFluidTankCell/4", GoodGenerator.MOD_ID+":yottaFluidTankCell/5",
            GoodGenerator.MOD_ID+":yottaFluidTankCell/6", GoodGenerator.MOD_ID+":yottaFluidTankCell/7", GoodGenerator.MOD_ID+":yottaFluidTankCell/8", GoodGenerator.MOD_ID+":yottaFluidTankCell/9", GoodGenerator.MOD_ID+":yottaFluidTankCell/10",});
    public static final Block yottaFluidTankCasing = new ComplexTextureCasing("yottaFluidTankCasing", new String[]{GoodGenerator.MOD_ID+":yottaFluidTankCasing_SIDE"}, new String[]{GoodGenerator.MOD_ID+":yottaFluidTankCasing_TOP"});

    public static Block essentiaHatch;

    public static ItemStack MAR;
    public static ItemStack FRF;
    public static ItemStack UCFE;
    public static ItemStack LEG;
    public static ItemStack NS;
    public static ItemStack NA;
    public static ItemStack YFT;

    public static ItemStack[] NeutronAccelerators = new ItemStack[9];

    public static void GTMetaTileRegister(){
        Loaders.MAR = new MultiNqGenerator(12732, "NaG", "Large Naquadah Reactor").getStackForm(1L);
        Loaders.FRF = new FuelRefineFactory(16999, "FRF", "Naquadah Fuel Refinery").getStackForm(1L);
        Loaders.UCFE = new UniversalChemicalFuelEngine(IDOffset, "UniversalChemicalFuelEngine", "Universal Chemical Fuel Engine").getStackForm(1L);
        for (int i = 0; i < 9; i ++) {
            Loaders.NeutronAccelerators[i] = new NeutronAccelerator(IDOffset + 2 + i, "Neutron Accelerator " + GT_Values.VN[i], "Neutron Accelerator " + GT_Values.VN[i], i).getStackForm(1L);
        }
        Loaders.NS = new NeutronSensor(IDOffset + 11, "Neutron Sensor", "Neutron Sensor", 5).getStackForm(1L);
        Loaders.NA = new NeutronActivator(IDOffset + 12, "NeutronActivator", "Neutron Activator").getStackForm(1L);
        Loaders.YFT = new YottaFluidTank(IDOffset + 13, "YottaFluidTank", "YOTTank").getStackForm(1L);
    }

    public static void Register(){
        GameRegistry.registerBlock(MAR_Casing, MyItemBlocks.class, "MAR_Casing");
        GameRegistry.registerBlock(radiationProtectionSteelFrame, MyItemBlocks.class, "radiationProtectionSteelFrame");
        GameRegistry.registerBlock(fieldRestrictingGlass, MyItemBlocks.class, "fieldRestrictingGlass");
        GameRegistry.registerBlock(FRF_Casings, MyItemBlocks.class, "FRF_Casings");
        GameRegistry.registerBlock(FRF_Coil_1, MyItemBlocks.class, "FRF_Coil_1");
        GameRegistry.registerBlock(FRF_Coil_2, MyItemBlocks.class, "FRF_Coil_2");
        GameRegistry.registerBlock(FRF_Coil_3, MyItemBlocks.class, "FRF_Coil_3");
        GameRegistry.registerBlock(rawCylinder, MyItemBlocks.class, "rawCylinder");
        GameRegistry.registerBlock(titaniumPlatedCylinder, MyItemBlocks.class, "titaniumPlatedCylinder");
        GameRegistry.registerBlock(speedingPipe, MyItemBlocks.class, "speedingPipe");
        GameRegistry.registerBlock(yottaFluidTankCell, MyItemBlocks.class, "yottaFluidTankCells");
        GameRegistry.registerBlock(yottaFluidTankCasing, MyItemBlocks.class, "yottaFluidTankCasing");
        GameRegistry.registerItem(radiationProtectionPlate, "radiationProtectionPlate", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(wrappedUraniumIngot, "wrappedUraniumIngot", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(highDensityUraniumNugget, "highDensityUraniumNugget", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(highDensityUranium, "highDensityUranium", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(wrappedThoriumIngot, "wrappedThoriumIngot", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(highDensityThoriumNugget, "highDensityThoriumNugget", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(highDensityThorium, "highDensityThorium", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(wrappedPlutoniumIngot, "wrappedPlutoniumIngot", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(highDensityPlutoniumNugget, "highDensityPlutoniumNugget", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(highDensityPlutonium, "highDensityPlutonium", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(rawAtomicSeparationCatalyst, "rawAtomicSeparationCatalyst", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(advancedRadiationProtectionPlate, "advancedRadiationProtectionPlate", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(aluminumNitride, "aluminumNitride", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(specialCeramics, "specialCeramics", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(specialCeramicsPlate, "specialCeramicsPlate", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(radioactiveWaste, "radioactiveWaste", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(plasticCase, "plasticCase", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(quartzWafer, "quartzWafer", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(microHeater, "microHeater", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(quartzCrystalResonator, "quartzCrystalResonator", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(inverter, "inverter", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(neutronSource, "neutronSource", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(naquadahMass, "naquadahMass", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(enrichedNaquadahMass, "enrichedNaquadahMass", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(naquadriaMass, "naquadriaMass", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(advancedFuelRod, "advancedFuelRod", GoodGenerator.MOD_ID);
        GameRegistry.registerItem(fluidCore, "fluidCore", GoodGenerator.MOD_ID);
    }

    public static void compactMod() {
        if (Loader.isModLoaded("Thaumcraft")) {
            GameRegistry.registerTileEntity(EssentiaHatch.class, "EssentiaHatch");
            Loaders.LEG = new LargeEssentiaGenerator(IDOffset + 1, "LargeEssentiaGenerator", "Large Essentia Generator").getStackForm(1L);
            essentiaHatch = new TEBlock("essentiaHatch", new String[]{GoodGenerator.MOD_ID + ":essentiaHatch"}, 1);
            GameRegistry.registerBlock(magicCasing, MyItemBlocks.class, "magicCasing");
            GameRegistry.registerBlock(essentiaCell, MyItemBlocks.class, "essentiaCell");
            GameRegistry.registerBlock(essentiaHatch, MyItemBlocks.class, "essentiaHatch");
        }
    }

    public static void addOreDic(){
        OreDictionary.registerOre("blockGlass", fieldRestrictingGlass);
        OreDictionary.registerOre("blockGlassZPM", fieldRestrictingGlass);
        OreDictionary.registerOre("dustAluminumNitride", aluminumNitride);
    }

    public static void addTexturePage(){
        if (Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage] == null){
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage] = new ITexture[128];
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][0] = TextureFactory.of(magicCasing);
            Textures.BlockIcons.casingTexturePages[GoodGeneratorTexturePage][1] = TextureFactory.of(yottaFluidTankCasing);
        }
    }

    public static void preInitLoad(){
        Register();
        GTMetaTileRegister();
        addOreDic();
        addTexturePage();
        compactMod();
        FluidsBuilder.Register();
        FuelRodLoader.RegisterRod();
        IMCForNEI.IMCSender();
    }

    public static void initLoad(){
        initLoadRecipes();
    }

    public static void postInitLoad(){
        postInitLoadRecipes();
    }

    public static void initLoadRecipes(){
        RecipeLoader.InitLoadRecipe();
        RecipeLoader_02.InitLoadRecipe();
        FuelRecipeLoader.RegisterFuel();
        NaquadahReworkRecipeLoader.RecipeLoad();
    }

    public static void postInitLoadRecipes(){
        RecipeLoader.RecipeLoad();
        RecipeLoader.Fixer();
        RecipeLoader_02.RecipeLoad();
        NeutronActivatorLoader.NARecipeLoad();
    }
}
