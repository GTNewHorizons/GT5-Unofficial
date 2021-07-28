package GoodGenerator.Loader;

import GoodGenerator.Blocks.RegularBlock.Casing;
import GoodGenerator.Blocks.RegularBlock.Frame;
import GoodGenerator.Blocks.RegularBlock.TEBlock;
import GoodGenerator.Blocks.TEs.*;
import GoodGenerator.Items.MyItemBlocks;
import GoodGenerator.Items.MyItems;
import GoodGenerator.Main.GoodGenerator;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.render.TextureFactory;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

public class Loaders {

    public static final int IDOffset = 32001;
    public static final byte GoodGeneratorTexturePage = 12;

    public static final Item radiationProtectionPlate = new MyItems("radiationProtectionPlate", GoodGenerator.GG);
    public static final Item wrappedUraniumIngot = new MyItems("wrappedUraniumIngot", GoodGenerator.GG);
    public static final Item highDensityUraniumNugget = new MyItems("highDensityUraniumNugget", GoodGenerator.GG);
    public static final Item highDensityUranium = new MyItems("highDensityUranium", GoodGenerator.GG);
    public static final Item wrappedThoriumIngot = new MyItems("wrappedThoriumIngot", GoodGenerator.GG);
    public static final Item highDensityThoriumNugget = new MyItems("highDensityThoriumNugget", GoodGenerator.GG);
    public static final Item highDensityThorium = new MyItems("highDensityThorium", GoodGenerator.GG);
    public static final Item wrappedPlutoniumIngot = new MyItems("wrappedPlutoniumIngot", GoodGenerator.GG);
    public static final Item highDensityPlutoniumNugget = new MyItems("highDensityPlutoniumNugget", GoodGenerator.GG);
    public static final Item highDensityPlutonium = new MyItems("highDensityPlutonium", GoodGenerator.GG);
    public static final Item rawAtomicSeparationCatalyst = new MyItems("rawAtomicSeparationCatalyst", GoodGenerator.GG);
    public static final Item advancedRadiationProtectionPlate = new MyItems("advancedRadiationProtectionPlate", GoodGenerator.GG);
    public static final Item aluminumNitride = new MyItems("aluminumNitride", GoodGenerator.GG);
    public static final Item specialCeramics = new MyItems("specialCeramics", GoodGenerator.GG);
    public static final Item specialCeramicsPlate = new MyItems("specialCeramicsPlate", GoodGenerator.GG);

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
    public static final Block essentiaHatch = new TEBlock("essentiaHatch", new String[]{GoodGenerator.MOD_ID+":essentiaHatch"}, 1);

    public static final Block[] essentiaCells = new Block[]{essentiaCell};

    public static ItemStack MAR;
    public static ItemStack FRF;
    public static ItemStack UCFE;
    public static ItemStack LEG;

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
        GameRegistry.registerBlock(essentiaHatch, MyItemBlocks.class, "essentiaHatch");
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
        GameRegistry.registerTileEntity(EssentiaHatch.class, "EssentiaHatch");
        Loaders.MAR = new MultiNqGenerator(12732, "NaG", "Large Naquadah Reactor").getStackForm(1L);
        Loaders.FRF = new FuelRefineFactory(16999, "FRF", "Naquadah Fuel Refinery").getStackForm(1L);
        Loaders.UCFE = new UniversalChemicalFuelEngine(IDOffset, "UniversalChemicalFuelEngine", "Universal Chemical Fuel Engine").getStackForm(1L);
        if (Loader.isModLoaded("Thaumcraft")){
            GameRegistry.registerBlock(magicCasing, MyItemBlocks.class, "magicCasing");
            GameRegistry.registerBlock(essentiaCells[0], MyItemBlocks.class, "essentiaCell");
            Loaders.LEG = new LargeEssentiaGenerator(IDOffset + 1, "LargeEssentiaGenerator", "Large Essentia Generator").getStackForm(1L);
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
        }
    }
}
