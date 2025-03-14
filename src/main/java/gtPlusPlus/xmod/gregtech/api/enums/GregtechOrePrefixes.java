package gtPlusPlus.xmod.gregtech.api.enums;

import static gregtech.api.enums.GTValues.M;
import static gtPlusPlus.core.util.Utils.getTcAspectStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ConfigCategories;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.Element;
import gregtech.api.enums.Materials;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TCAspects;
import gregtech.api.enums.TCAspects.TC_AspectStack;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IColorModulationContainer;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.objects.MaterialStack;
import gregtech.api.util.GTConfig;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.lib.GTPPCore;

public enum GregtechOrePrefixes {

    ;

    public enum GT_Materials implements IColorModulationContainer, ISubTagContainer {

        /**
         * This is the Default Material returned in case no Material has been found or a NullPointer has been inserted
         * at a location where it shouldn't happen.
         * <p/>
         * Mainly for preventing NullPointer Exceptions and providing Default Values.
         * <p>
         * Unknown Material Components. Dead End Section.
         * <p>
         * Alkalus Range 730-799 & 970-998 (aMetaItemSubID, TextureSet, aToolSpeed, aToolDurability, aToolQuality,
         * aTypes, R, G, B, Alpha, aLocalName, aFuelType, aFuelPower, aMeltingPoint, aBlastFurnaceTemp,
         * aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider, aColor
         * this(aMetaItemSubID, aIconSet, aToolSpeed, aToolDurability, aToolQuality, true);
         *
         */
        _NULL(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "NULL", 0, 0, 0, 0, false, false, 1, 1, 1,
            Dyes._NULL, Element._NULL, Collections.singletonList(getTcAspectStack(TCAspects.VACUOS.name(), 1))),

        // Lapis(526, TextureSet.SET_LAPIS, 1.0F, 0, 1, 1 | 4 | 8, 70, 70, 220, 0, "Lapis", 0, 0, -1, 0, false, false,
        // 3, 1, 1, Dyes.dyeBlue, 2, Arrays.asList(new MaterialStack(Materials.Lazurite, 12), new
        // MaterialStack(Materials.Sodalite, 2), new MaterialStack(Materials.Pyrite, 1), new
        // MaterialStack(Materials.Calcite, 1)), Arrays.asList(getTcAspectStack(TC_Aspects.SENSUS, 1))),
        Pyrotheum(20, TextureSet.SET_FLUID, 1.0F, 0, 1, 2 | 16 | 32, 255, 128, 0, 0, "Pyrotheum", 0, 0, -1, 0, false,
            false, 2, 3, 1, Dyes.dyeYellow, 2,
            Arrays.asList(
                new MaterialStack(Materials.Coal, 1),
                new MaterialStack(Materials.Redstone, 1),
                new MaterialStack(Materials.Blaze, 1),
                new MaterialStack(Materials.Sulfur, 1)),
            Arrays.asList(getTcAspectStack("PRAECANTATIO", 2), getTcAspectStack(TCAspects.IGNIS, 1))),
        Cryotheum(21, TextureSet.SET_FLUID, 1.0F, 0, 1, 2 | 16 | 32, 102, 178, 255, 0, "Cryotheum", 0, 0, -1, 0, false,
            false, 2, 3, 1, Dyes.dyeLightBlue, 2,
            Arrays.asList(
                new MaterialStack(Materials.Blizz, 1),
                new MaterialStack(Materials.Redstone, 1),
                new MaterialStack(Materials.Snow, 1),
                new MaterialStack(Materials.Niter, 1)),
            Arrays.asList(getTcAspectStack("PRAECANTATIO", 2), getTcAspectStack(TCAspects.GELUM, 1))),
        Ender(22, TextureSet.SET_FLUID, 1.0F, 0, 2, 1, 255, 255, 255, 0, "Ender", 0, 0, -1, 0, false, false, 3, 1, 1,
            Dyes.dyeGreen),

        /**
         * Circuitry, Batteries and other Technical things
         */
        Symbiotic(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "IV Tier", 0, 0, -1, 0, false, false, 1, 1,
            1, Dyes.dyeLightGray,
            Arrays.asList(getTcAspectStack(TCAspects.ELECTRUM, 4), getTcAspectStack(TCAspects.MACHINA, 4))),
        Neutronic(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "LuV Tier", 0, 0, -1, 0, false, false, 1, 1,
            1, Dyes.dyeLightGray,
            Arrays.asList(getTcAspectStack(TCAspects.ELECTRUM, 6), getTcAspectStack(TCAspects.MACHINA, 6))),
        Quantum(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 255, 255, 255, 0, "ZPM Tier", 0, 0, -1, 0, false, false, 1, 1,
            1, Dyes.dyeLightGray,
            Arrays.asList(getTcAspectStack(TCAspects.ELECTRUM, 8), getTcAspectStack(TCAspects.MACHINA, 8))),

        Superconductor(-1, TextureSet.SET_NONE, 1.0F, 0, 0, 0, 190, 240, 190, 0, "Superconductor", 0, 0, -1, 0, false,
            false, 1, 1, 1, Dyes.dyeGreen, Collections.singletonList(getTcAspectStack(TCAspects.ELECTRUM, 8))),

        Staballoy(30, TextureSet.SET_ROUGH, 10.0F, 5120, 4, 1 | 2 | 16 | 32 | 64 | 128, 68, 75, 66, 0, "Staballoy", 0,
            0, 1500, 2800, true, false, 1, 3, 1, Dyes.dyeGreen, 2,
            Arrays.asList(new MaterialStack(Materials.Titanium, 1), new MaterialStack(Materials.Uranium, 9)),
            Arrays.asList(getTcAspectStack(TCAspects.METALLUM, 8), getTcAspectStack(TCAspects.STRONTIO, 3))),
        Bedrockium(31, TextureSet.SET_FINE, 8.0F, 8196, 3, 1 | 2 | 16 | 32 | 64 | 128, 39, 39, 39, 0, "Bedrockium", 0,
            0, -1, 0, false, false, 1, 5, 1, Dyes.dyeLightGray, 2,
            Arrays.asList(new MaterialStack(Materials.Carbon, 63), new MaterialStack(Materials.Carbon, 56)),
            Arrays.asList(getTcAspectStack(TCAspects.VACUOS, 8), getTcAspectStack(TCAspects.TUTAMEN, 3))),
        BloodSteel(32, TextureSet.SET_METALLIC, 11.0F, 768, 4, 1 | 2 | 16 | 32 | 64 | 128, 142, 28, 0, 0, "Blood Steel",
            0, 0, -1, 0, false, false, 1, 1, 1, Dyes.dyeRed, 2, Arrays.asList(new MaterialStack(Materials.Steel, 3)),
            Arrays.asList(getTcAspectStack(TCAspects.VICTUS, 8), getTcAspectStack(TCAspects.IGNIS, 3))),
        Void(33, TextureSet.SET_METALLIC, 6.0F, 1280, 3, 1 | 2 | 16 | 32 | 64 | 128, 82, 17, 82, 0, "Void Metal", 0, 0,
            -1, 0, false, false, 3, 1, 1, Dyes.dyeBlack,
            Arrays.asList(getTcAspectStack("PRAECANTATIO", 5), getTcAspectStack(TCAspects.VACUOS, 7))),
        ConductiveIron(34, TextureSet.SET_METALLIC, 5.0F, 256, 2, 1 | 2, 164, 109, 100, 0, "Conductive Iron", 0, 0, -1,
            0, false, false, 3, 1, 1, Dyes.dyeRed, 2,
            Arrays.asList(new MaterialStack(Materials.Iron, 6), new MaterialStack(Materials.Redstone, 2)),
            Arrays.asList(getTcAspectStack(TCAspects.POTENTIA, 2), getTcAspectStack(TCAspects.METALLUM, 2))),
        ElectricalSteel(35, TextureSet.SET_METALLIC, 7.0F, 768, 3, 1 | 2 | 64 | 128, 194, 194, 194, 0,
            "Electrical Steel", 0, 0, 1811, 1000, true, false, 3, 1, 1, Dyes.dyeLightGray, 2,
            Arrays.asList(
                new MaterialStack(Materials.Iron, 3),
                new MaterialStack(Materials.Coal, 2),
                new MaterialStack(Materials.Silicon, 2)),
            Arrays.asList(getTcAspectStack(TCAspects.MAGNETO, 2), getTcAspectStack(TCAspects.ELECTRUM, 5))),
        EnergeticAlloy(36, TextureSet.SET_SHINY, 5.0F, 512, 3, 1 | 2 | 64 | 128, 252, 152, 45, 0, "Energetic Alloy", 0,
            0, -1, 0, false, false, 3, 1, 1, Dyes.dyeOrange, 2,
            Arrays.asList(
                new MaterialStack(Materials.Gold, 3),
                new MaterialStack(Materials.Glowstone, 2),
                new MaterialStack(Materials.Redstone, 2)),
            Arrays.asList(getTcAspectStack(TCAspects.POTENTIA, 4), getTcAspectStack(TCAspects.LUX, 3))),
        VibrantAlloy(37, TextureSet.SET_SHINY, 7.0F, 1280, 4, 1 | 2 | 64 | 128, 204, 242, 142, 0, "Vibrant Alloy", 0, 0,
            -1, 0, false, false, 3, 1, 1, Dyes.dyeLime, 2,
            Arrays.asList(new MaterialStack(Materials.EnergeticAlloy, 1), new MaterialStack(Materials.EnderPearl, 3)),
            Arrays.asList(getTcAspectStack(TCAspects.MACHINA, 5), getTcAspectStack(TCAspects.TELUM, 4))),
        PulsatingIron(38, TextureSet.SET_SHINY, 5.0F, 256, 2, 1 | 2 | 64 | 128, 50, 91, 21, 0, "Pulsating Iron", 0, 0,
            -1, 0, false, false, 3, 1, 1, Dyes.dyeGreen, 2,
            Arrays.asList(new MaterialStack(Materials.Iron, 2), new MaterialStack(Materials.EnderPearl, 2)),
            Arrays.asList(getTcAspectStack(TCAspects.ALIENIS, 3), getTcAspectStack(TCAspects.METALLUM, 3))),
        /* TODO */ RedstoneAlloy(39, TextureSet.SET_METALLIC, 1.0F, 256, 2, 1 | 2 | 16 | 32 | 64, 178, 34, 34, 0,
            "Redstone Alloy", 0, 0, -1, 0, false, false, 3, 1, 1, Dyes.dyeRed, 2,
            Arrays.asList(new MaterialStack(Materials.Iron, 2), new MaterialStack(Materials.Redstone, 4))),

        // Needs more Use, I think.
        Tantalloy60(40, TextureSet.SET_DULL, 8.0F, 5120, 3, 1 | 2 | 16 | 32 | 64 | 128, 68, 75, 166, 0, "Tantalloy-60",
            0, 0, 3035, 2200, true, false, 1, 2, 1, Dyes.dyeLightBlue, 2,
            Arrays.asList(new MaterialStack(Materials.Tungsten, 1), new MaterialStack(Materials.Tantalum, 9)),
            Arrays.asList(getTcAspectStack(TCAspects.METALLUM, 8), getTcAspectStack(TCAspects.STRONTIO, 3))),
        Tantalloy61(41, TextureSet.SET_DULL, 7.0F, 5120, 2, 1 | 2 | 16 | 32 | 64 | 128, 122, 135, 196, 0,
            "Tantalloy-61", 0, 0, 3015, 2150, true, false, 1, 2, 1, Dyes.dyeLightBlue, 2,
            Arrays.asList(
                new MaterialStack(Materials.Tungsten, 1),
                new MaterialStack(Materials.Tantalum, 9),
                new MaterialStack(Materials.Titanium, 1)),
            Arrays.asList(getTcAspectStack(TCAspects.METALLUM, 8), getTcAspectStack(TCAspects.STRONTIO, 3))),

        Potin(42, TextureSet.SET_METALLIC, 7.0F, 5120, 2, 1 | 2 | 16 | 32 | 64 | 128, 201, 151, 129, 0, "Potin", 0, 0,
            3015, 2150, true, false, 1, 2, 1, Dyes.dyeLightBlue, 2,
            Arrays.asList(
                new MaterialStack(Materials.Tungsten, 1),
                new MaterialStack(Materials.Tantalum, 9),
                new MaterialStack(Materials.Titanium, 1)),
            Arrays.asList(getTcAspectStack(TCAspects.METALLUM, 8), getTcAspectStack(TCAspects.STRONTIO, 3))),
        Inconel792(43, TextureSet.SET_METALLIC, 7.0F, 5120, 2, 1 | 2 | 16 | 32 | 64 | 128, 108, 240, 118, 0,
            "Inconel-792", 0, 0, 3015, 2150, true, false, 1, 2, 1, Dyes.dyeLightBlue, 2,
            Arrays.asList(
                new MaterialStack(Materials.Tungsten, 1),
                new MaterialStack(Materials.Tantalum, 9),
                new MaterialStack(Materials.Titanium, 1)),
            Arrays.asList(getTcAspectStack(TCAspects.METALLUM, 8), getTcAspectStack(TCAspects.STRONTIO, 3))),
        Inconel690(44, TextureSet.SET_DULL, 7.0F, 5120, 2, 1 | 2 | 16 | 32 | 64 | 128, 118, 220, 138, 0, "Inconel-690",
            0, 0, 3015, 2150, true, false, 1, 2, 1, Dyes.dyeLightBlue, 2,
            Arrays.asList(
                new MaterialStack(Materials.Tungsten, 1),
                new MaterialStack(Materials.Tantalum, 9),
                new MaterialStack(Materials.Titanium, 1)),
            Arrays.asList(getTcAspectStack(TCAspects.METALLUM, 8), getTcAspectStack(TCAspects.STRONTIO, 3))),
        MaragingSteel300(45, TextureSet.SET_METALLIC, 7.0F, 5120, 2, 1 | 2 | 16 | 32 | 64 | 128, 150, 150, 150, 0,
            "Maraging Steel 300", 0, 0, 3015, 2150, true, false, 1, 2, 1, Dyes.dyeLightBlue, 2,
            Arrays.asList(
                new MaterialStack(Materials.Tungsten, 1),
                new MaterialStack(Materials.Tantalum, 9),
                new MaterialStack(Materials.Titanium, 1)),
            Arrays.asList(getTcAspectStack(TCAspects.METALLUM, 8), getTcAspectStack(TCAspects.STRONTIO, 3))),
        MaragingSteel350(46, TextureSet.SET_METALLIC, 7.0F, 5120, 2, 1 | 2 | 16 | 32 | 64 | 128, 160, 160, 160, 0,
            "Maraging Steel 350", 0, 0, 3015, 2150, true, false, 1, 2, 1, Dyes.dyeLightBlue, 2,
            Arrays.asList(
                new MaterialStack(Materials.Tungsten, 1),
                new MaterialStack(Materials.Tantalum, 9),
                new MaterialStack(Materials.Titanium, 1)),
            Arrays.asList(getTcAspectStack(TCAspects.METALLUM, 8), getTcAspectStack(TCAspects.STRONTIO, 3))),
        HastelloyX(47, TextureSet.SET_SHINY, 7.0F, 5120, 2, 1 | 2 | 16 | 32 | 64 | 128, 255, 193, 37, 0, "Hastelloy-X",
            0, 0, 3015, 2150, true, false, 1, 2, 1, Dyes.dyeLightBlue, 2,
            Arrays.asList(
                new MaterialStack(Materials.Tungsten, 1),
                new MaterialStack(Materials.Tantalum, 9),
                new MaterialStack(Materials.Titanium, 1)),
            Arrays.asList(getTcAspectStack(TCAspects.METALLUM, 8), getTcAspectStack(TCAspects.STRONTIO, 3))),

        TriniumNaquadahCarbonite(48, TextureSet.SET_SHINY, 7.0F, 5120, 2, 1 | 2 | 16 | 32 | 64 | 128, 255, 233, 0, 0,
            "Trinium Naquadah Carbonite", 0, 0, 3015, 2150, true, false, 1, 2, 1, Dyes.dyeLightBlue, 2,
            Arrays.asList(
                new MaterialStack(Materials.Tungsten, 1),
                new MaterialStack(Materials.Tantalum, 9),
                new MaterialStack(Materials.Titanium, 1)),
            Arrays.asList(getTcAspectStack(TCAspects.METALLUM, 8), getTcAspectStack(TCAspects.STRONTIO, 3))),

        // Radioactive Materials
        HydrofluoricAcid(-1, TextureSet.SET_FLUID, 1.0F, 0, 1, 2 | 16 | 32, 200, 200, 200, 0, "Hydrofluoric Acid", 0, 0,
            -1, 0, false, false, 2, 3, 1, Dyes.dyeWhite, 2,
            Arrays.asList(
                new MaterialStack(Materials.Coal, 1),
                new MaterialStack(Materials.Redstone, 1),
                new MaterialStack(Materials.Blaze, 1),
                new MaterialStack(Materials.Sulfur, 1))),
        UraniumHexaFluoride(-1, TextureSet.SET_FLUID, 1.0F, 0, 1, 2 | 16 | 32, 73, 220, 83, 0, "Uranium Hexafluoride",
            0, 0, -1, 0, false, false, 2, 3, 1, Dyes.dyeLime, 2,
            Arrays.asList(
                new MaterialStack(Materials.Coal, 1),
                new MaterialStack(Materials.Redstone, 1),
                new MaterialStack(Materials.Blaze, 1),
                new MaterialStack(Materials.Sulfur, 1))),
        UraniumTetraFluoride(-1, TextureSet.SET_FLUID, 1.0F, 0, 1, 2 | 16 | 32, 73, 220, 83, 0, "Uranium Tetrafluoride",
            0, 0, -1, 0, false, false, 2, 3, 1, Dyes.dyeLime, 2,
            Arrays.asList(
                new MaterialStack(Materials.Coal, 1),
                new MaterialStack(Materials.Redstone, 1),
                new MaterialStack(Materials.Blaze, 1),
                new MaterialStack(Materials.Sulfur, 1))),
        ThoriumTetraFluoride(-1, TextureSet.SET_FLUID, 1.0F, 0, 1, 2 | 16 | 32, 15, 120, 15, 0, "Thorium Tetrafluoride",
            0, 0, -1, 0, false, false, 2, 3, 1, Dyes.dyeGreen, 2,
            Arrays.asList(
                new MaterialStack(Materials.Coal, 1),
                new MaterialStack(Materials.Redstone, 1),
                new MaterialStack(Materials.Blaze, 1),
                new MaterialStack(Materials.Sulfur, 1))),

        SulfurousAcid(-1, TextureSet.SET_FLUID, 1.0F, 0, 1, 2 | 16 | 32, 110, 220, 30, 0, "Sulfurous Acid", 0, 0, -1, 0,
            false, false, 2, 3, 1, Dyes.dyeWhite, 2,
            Arrays.asList(
                new MaterialStack(Materials.Coal, 1),
                new MaterialStack(Materials.Redstone, 1),
                new MaterialStack(Materials.Blaze, 1),
                new MaterialStack(Materials.Sulfur, 1))),
        SulfurDioxide(-1, TextureSet.SET_FLUID, 1.0F, 0, 1, 2 | 16 | 32, 150, 200, 50, 0, "Sulfur Dioxide", 0, 0, -1, 0,
            false, false, 2, 3, 1, Dyes.dyeWhite, 2,
            Arrays.asList(
                new MaterialStack(Materials.Coal, 1),
                new MaterialStack(Materials.Redstone, 1),
                new MaterialStack(Materials.Blaze, 1),
                new MaterialStack(Materials.Sulfur, 1))),
        HydrogenChloride(-1, TextureSet.SET_FLUID, 1.0F, 0, 1, 2 | 16 | 32, 150, 240, 90, 0, "Hydrogen Chloride", 0, 0,
            -1, 0, false, false, 2, 3, 1, Dyes.dyeWhite, 2,
            Arrays.asList(
                new MaterialStack(Materials.Coal, 1),
                new MaterialStack(Materials.Redstone, 1),
                new MaterialStack(Materials.Blaze, 1),
                new MaterialStack(Materials.Sulfur, 1))),
        SulfuricApatite(-1, TextureSet.SET_FLUID, 1.0F, 0, 1, 2 | 16 | 32, 0, 105, 105, 0, "Sulfuric Apatite Solution",
            0, 0, -1, 0, false, false, 2, 3, 1, Dyes.dyeWhite, 2,
            Arrays.asList(
                new MaterialStack(Materials.Coal, 1),
                new MaterialStack(Materials.Redstone, 1),
                new MaterialStack(Materials.Blaze, 1),
                new MaterialStack(Materials.Sulfur, 1))),

        SulfuricLithium(-1, TextureSet.SET_FLUID, 1.0F, 0, 1, 2 | 16 | 32, 0, 105, 105, 0, "Sulfuric Lithium Solution",
            0, 0, -1, 0, false, false, 2, 3, 1, Dyes.dyeWhite, 2,
            Arrays.asList(
                new MaterialStack(Materials.Coal, 1),
                new MaterialStack(Materials.Redstone, 1),
                new MaterialStack(Materials.Blaze, 1),
                new MaterialStack(Materials.Sulfur, 1))),
        LithiumHydroxide(-1, TextureSet.SET_FLUID, 1.0F, 0, 1, 2 | 16 | 32, 0, 105, 105, 0, "Lithium Hydroxide", 0, 0,
            -1, 0, false, false, 2, 3, 1, Dyes.dyeWhite, 2,
            Arrays.asList(
                new MaterialStack(Materials.Coal, 1),
                new MaterialStack(Materials.Redstone, 1),
                new MaterialStack(Materials.Blaze, 1),
                new MaterialStack(Materials.Sulfur, 1))),;

        /**
         * List of all Materials.
         */
        public static final Collection<GT_Materials> VALUES = new HashSet<>(Arrays.asList(values()));

        static {
            /*
             * Primitive.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING); Basic.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
             * Good.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING); Advanced.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
             * Data.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING); Elite.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
             * Master.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING); Ultimate.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
             * Superconductor.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING); Infinite.add(SubTag.NO_SMASHING,
             * SubTag.NO_SMELTING);
             */
            Symbiotic.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
            Neutronic.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
            Quantum.add(SubTag.NO_SMASHING, SubTag.NO_SMELTING);
        }

        /**
         * This Array can be changed dynamically by a Tick Handler in order to get a glowing Effect on all GT Meta Items
         * out of this Material.
         */
        public final short[] mRGBa = new short[] { 255, 255, 255, 0 }, mMoltenRGBa = new short[] { 255, 255, 255, 0 };

        public final TextureSet mIconSet;
        public final int mMetaItemSubID;
        public final boolean mUnificatable;
        public final GT_Materials mMaterialInto;
        public final List<MaterialStack> mMaterialList = new ArrayList<>();
        public final List<GT_Materials> mOreByProducts = new ArrayList<>(), mOreReRegistrations = new ArrayList<>();
        public final List<TC_AspectStack> mAspects = new ArrayList<>();
        private final ArrayList<ItemStack> mMaterialItems = new ArrayList<>();
        private final Collection<SubTag> mSubTags = new HashSet<>();
        public Enchantment mEnchantmentTools = null, mEnchantmentArmors = null;
        public byte mEnchantmentToolsLevel = 0, mEnchantmentArmorsLevel = 0;
        public boolean mBlastFurnaceRequired = false;
        public float mToolSpeed = 1.0F, mHeatDamage = 0.0F;
        public String mChemicalFormula = "?", mDefaultLocalName = "null";
        public Dyes mColor = Dyes._NULL;
        public short mMeltingPoint = 0, mBlastFurnaceTemp = 0;
        public int mTypes = 0, mDurability = 16, mFuelPower = 0, mFuelType = 0, mExtraData = 0, mOreValue = 0,
            mOreMultiplier = 1, mByProductMultiplier = 1, mSmeltingMultiplier = 1;
        public long mDensity = M;
        public Element mElement = null;
        public GT_Materials mDirectSmelting = this;
        public GT_Materials mOreReplacement = this;
        public GT_Materials mMacerateInto = this;
        public GT_Materials mSmeltInto = this;
        public GT_Materials mArcSmeltInto = this;
        public final GT_Materials mHandleMaterial = this;
        public byte mToolQuality = 0;
        public final Fluid mSolid = null;
        public Fluid mFluid = null;
        public Fluid mGas = null;
        public Fluid mPlasma = null;
        /**
         * This Fluid is used as standard Unit for Molten Materials. 1296 is a Molten Block, what means 144 is one
         * Material Unit worth
         */
        public final Fluid mStandardMoltenFluid = null;

        GT_Materials(final int aMetaItemSubID, final TextureSet aIconSet, final float aToolSpeed,
            final int aToolDurability, final int aToolQuality, final boolean aUnificatable) {
            this.mUnificatable = aUnificatable;
            this.mMaterialInto = this;
            this.mMetaItemSubID = aMetaItemSubID;
            this.mToolQuality = (byte) aToolQuality;
            this.mDurability = aToolDurability;
            this.mToolSpeed = aToolSpeed;
            this.mIconSet = aIconSet;
            if (aMetaItemSubID >= 0) {
                if (GTPPCore.sMU_GeneratedMaterials[aMetaItemSubID] == null) {
                    GTPPCore.sMU_GeneratedMaterials[aMetaItemSubID] = this;
                } else {
                    throw new IllegalArgumentException("The Index " + aMetaItemSubID + " is already used!");
                }
            }
        }

        GT_Materials(final GT_Materials aMaterialInto, final boolean aReRegisterIntoThis) {
            this.mUnificatable = false;
            this.mDefaultLocalName = aMaterialInto.mDefaultLocalName;
            this.mMaterialInto = aMaterialInto.mMaterialInto;
            if (aReRegisterIntoThis) {
                this.mMaterialInto.mOreReRegistrations.add(this);
            }
            this.mChemicalFormula = aMaterialInto.mChemicalFormula;
            this.mMetaItemSubID = -1;
            this.mIconSet = TextureSet.SET_NONE;
        }

        /**
         * @param aMetaItemSubID        the Sub-ID used in my own MetaItems. Range 0-1000. -1 for no Material
         * @param aTypes                which kind of Items should be generated. Bitmask as follows: 1 = Dusts of all
         *                              kinds. 2 = Dusts, Ingots, Plates, Rods/Sticks, Machine Components and other
         *                              Metal specific things. 4 = Dusts, Gems, Plates, Lenses (if transparent). 8 =
         *                              Dusts, Impure Dusts, crushed Ores, purified Ores, centrifuged Ores etc. 16 =
         *                              Cells 32 = Plasma Cells 64 = Tool Heads 128 = Gears
         * @param aR,                   aG, aB Color of the Material 0-255 each.
         * @param aA                    transparency of the Material Texture. 0 = fully visible, 255 = Invisible.
         * @param aLocalName            The Name used as Default for localization.
         * @param aFuelType             Type of Generator to get Energy from this Material.
         * @param aFuelPower            EU generated. Will be multiplied by 1000, also additionally multiplied by 2 for
         *                              Gems.
         * @param aAmplificationValue   Amount of UUM amplifier gotten from this.
         * @param aUUMEnergy            Amount of EU needed to shape the UUM into this Material.
         * @param aMeltingPoint         Used to determine the smelting Costs in Furnii.
         * @param aBlastFurnaceTemp     Used to determine the needed Heat capactiy Costs in Blast Furnii.
         * @param aBlastFurnaceRequired If this requires a Blast Furnace.
         * @param aColor                Vanilla MC Wool Color which comes the closest to this.
         */
        GT_Materials(final int aMetaItemSubID, final TextureSet aIconSet, final float aToolSpeed,
            final int aToolDurability, final int aToolQuality, final int aTypes, final int aR, final int aG,
            final int aB, final int aA, final String aLocalName, final int aFuelType, final int aFuelPower,
            final int aMeltingPoint, final int aBlastFurnaceTemp, final boolean aBlastFurnaceRequired,
            final boolean aTransparent, final int aOreValue, final int aDensityMultiplier, final int aDensityDivider,
            final Dyes aColor) {
            this(aMetaItemSubID, aIconSet, aToolSpeed, aToolDurability, aToolQuality, true);
            this.mDefaultLocalName = aLocalName;
            this.mMeltingPoint = (short) aMeltingPoint;
            this.mBlastFurnaceTemp = (short) aBlastFurnaceTemp;
            this.mBlastFurnaceRequired = aBlastFurnaceRequired;
            if (aTransparent) {
                this.add(SubTag.TRANSPARENT);
            }
            this.mFuelPower = aFuelPower;
            this.mFuelType = aFuelType;
            this.mOreValue = aOreValue;
            this.mDensity = (M * aDensityMultiplier) / aDensityDivider;
            this.mColor = aColor == null ? Dyes._NULL : aColor;
            if (this.mColor != null) {
                this.add(SubTag.HAS_COLOR);
            }
            this.mRGBa[0] = this.mMoltenRGBa[0] = (short) aR;
            this.mRGBa[1] = this.mMoltenRGBa[1] = (short) aG;
            this.mRGBa[2] = this.mMoltenRGBa[2] = (short) aB;
            this.mRGBa[3] = this.mMoltenRGBa[3] = (short) aA;
            this.mTypes = aTypes;
            if ((this.mTypes & 2) != 0) {
                this.add(SubTag.SMELTING_TO_FLUID);
            }
        }

        GT_Materials(final int aMetaItemSubID, final TextureSet aIconSet, final float aToolSpeed,
            final int aToolDurability, final int aToolQuality, final int aTypes, final int aR, final int aG,
            final int aB, final int aA, final String aLocalName, final int aFuelType, final int aFuelPower,
            final int aMeltingPoint, final int aBlastFurnaceTemp, final boolean aBlastFurnaceRequired,
            final boolean aTransparent, final int aOreValue, final int aDensityMultiplier, final int aDensityDivider,
            final Dyes aColor, final List<TC_AspectStack> aAspects) {
            this(
                aMetaItemSubID,
                aIconSet,
                aToolSpeed,
                aToolDurability,
                aToolQuality,
                aTypes,
                aR,
                aG,
                aB,
                aA,
                aLocalName,
                aFuelType,
                aFuelPower,
                aMeltingPoint,
                aBlastFurnaceTemp,
                aBlastFurnaceRequired,
                aTransparent,
                aOreValue,
                aDensityMultiplier,
                aDensityDivider,
                aColor);
            this.mAspects.addAll(aAspects);
        }

        /**
         * @param aElement The Element Enum represented by this Material
         */
        GT_Materials(final int aMetaItemSubID, final TextureSet aIconSet, final float aToolSpeed,
            final int aToolDurability, final int aToolQuality, final int aTypes, final int aR, final int aG,
            final int aB, final int aA, final String aLocalName, final int aFuelType, final int aFuelPower,
            final int aMeltingPoint, final int aBlastFurnaceTemp, final boolean aBlastFurnaceRequired,
            final boolean aTransparent, final int aOreValue, final int aDensityMultiplier, final int aDensityDivider,
            final Dyes aColor, final Element aElement, final List<TC_AspectStack> aAspects) {
            this(
                aMetaItemSubID,
                aIconSet,
                aToolSpeed,
                aToolDurability,
                aToolQuality,
                aTypes,
                aR,
                aG,
                aB,
                aA,
                aLocalName,
                aFuelType,
                aFuelPower,
                aMeltingPoint,
                aBlastFurnaceTemp,
                aBlastFurnaceRequired,
                aTransparent,
                aOreValue,
                aDensityMultiplier,
                aDensityDivider,
                aColor);
            this.mElement = aElement;
            // mElement.mLinkedMaterials.add(this);
            if (aElement == Element._NULL) {
                this.mChemicalFormula = "Empty";
            } else {
                this.mChemicalFormula = aElement.toString();
                this.mChemicalFormula = this.mChemicalFormula.replaceAll("_", "-");
            }
            this.mAspects.addAll(aAspects);
        }

        GT_Materials(final int aMetaItemSubID, final TextureSet aIconSet, final float aToolSpeed,
            final int aToolDurability, final int aToolQuality, final int aTypes, final int aR, final int aG,
            final int aB, final int aA, final String aLocalName, final int aFuelType, final int aFuelPower,
            final int aMeltingPoint, final int aBlastFurnaceTemp, final boolean aBlastFurnaceRequired,
            final boolean aTransparent, final int aOreValue, final int aDensityMultiplier, final int aDensityDivider,
            final Dyes aColor, final int aExtraData, final List<MaterialStack> aMaterialList) {
            this(
                aMetaItemSubID,
                aIconSet,
                aToolSpeed,
                aToolDurability,
                aToolQuality,
                aTypes,
                aR,
                aG,
                aB,
                aA,
                aLocalName,
                aFuelType,
                aFuelPower,
                aMeltingPoint,
                aBlastFurnaceTemp,
                aBlastFurnaceRequired,
                aTransparent,
                aOreValue,
                aDensityMultiplier,
                aDensityDivider,
                aColor,
                aExtraData,
                aMaterialList,
                null);
        }

        GT_Materials(final int aMetaItemSubID, final TextureSet aIconSet, final float aToolSpeed,
            final int aToolDurability, final int aToolQuality, final int aTypes, final int aR, final int aG,
            final int aB, final int aA, final String aLocalName, final int aFuelType, final int aFuelPower,
            final int aMeltingPoint, final int aBlastFurnaceTemp, final boolean aBlastFurnaceRequired,
            final boolean aTransparent, final int aOreValue, final int aDensityMultiplier, final int aDensityDivider,
            final Dyes aColor, final int aExtraData, final List<MaterialStack> aMaterialList,
            final List<TC_AspectStack> aAspects) {
            this(
                aMetaItemSubID,
                aIconSet,
                aToolSpeed,
                aToolDurability,
                aToolQuality,
                aTypes,
                aR,
                aG,
                aB,
                aA,
                aLocalName,
                aFuelType,
                aFuelPower,
                aMeltingPoint,
                aBlastFurnaceTemp,
                aBlastFurnaceRequired,
                aTransparent,
                aOreValue,
                aDensityMultiplier,
                aDensityDivider,
                aColor);
            this.mExtraData = aExtraData;
            this.mMaterialList.addAll(aMaterialList);
            final StringBuilder sb = new StringBuilder();
            for (final MaterialStack tMaterial : this.mMaterialList) {
                sb.append(tMaterial.toString());
            }
            this.mChemicalFormula = sb.toString()
                .replaceAll("_", "-");

            int tAmountOfComponents = 0, tMeltingPoint = 0;
            for (final MaterialStack tMaterial : this.mMaterialList) {
                tAmountOfComponents += tMaterial.mAmount;
                if (tMaterial.mMaterial.mMeltingPoint > 0) {
                    tMeltingPoint += tMaterial.mMaterial.mMeltingPoint * tMaterial.mAmount;
                }
                if (aAspects == null) {
                    for (final TC_AspectStack tAspect : tMaterial.mMaterial.mAspects) {
                        tAspect.addToAspectList(this.mAspects);
                    }
                }
            }

            if (this.mMeltingPoint < 0) {
                this.mMeltingPoint = (short) (tMeltingPoint / tAmountOfComponents);
            }

            tAmountOfComponents *= aDensityMultiplier;
            tAmountOfComponents /= aDensityDivider;
            if (aAspects == null) {
                for (final TC_AspectStack tAspect : this.mAspects) {
                    tAspect.mAmount = Math.max(1, tAspect.mAmount / Math.max(1, tAmountOfComponents));
                }
            } else {
                this.mAspects.addAll(aAspects);
            }
        }

        /**
         * Called in preInit with the Config to set Values.
         *
         * @param aConfiguration
         */
        public static void init(final GTConfig aConfiguration) {
            for (final GT_Materials tMaterial : VALUES) {
                final String tString = tMaterial.toString()
                    .toLowerCase();
                tMaterial.mHeatDamage = (float) aConfiguration
                    .get(ConfigCategories.Materials.heatdamage, tString, tMaterial.mHeatDamage);
                if (tMaterial.mBlastFurnaceRequired) {
                    tMaterial.mBlastFurnaceRequired = aConfiguration
                        .get(ConfigCategories.Materials.blastfurnacerequirements, tString, true);
                }
                if (tMaterial.mBlastFurnaceRequired && aConfiguration.get(
                    ConfigCategories.Materials.blastinductionsmelter,
                    tString,
                    tMaterial.mBlastFurnaceTemp < 1500)) {}
            }
        }

        public boolean isRadioactive() {
            if (this.mElement != null) {
                return this.mElement.mHalfLifeSeconds >= 0;
            }
            for (final MaterialStack tMaterial : this.mMaterialList) {
                if (tMaterial.mMaterial.isRadioactive()) {
                    return true;
                }
            }
            return false;
        }

        public long getProtons() {
            if (this.mElement != null) {
                return this.mElement.getProtons();
            }
            if (this.mMaterialList.size() <= 0) {
                return Element.Tc.getProtons();
            }
            long rAmount = 0, tAmount = 0;
            for (final MaterialStack tMaterial : this.mMaterialList) {
                tAmount += tMaterial.mAmount;
                rAmount += tMaterial.mAmount * tMaterial.mMaterial.getProtons();
            }
            return (this.getDensity() * rAmount) / (tAmount * M);
        }

        public long getNeutrons() {
            if (this.mElement != null) {
                return this.mElement.getNeutrons();
            }
            if (this.mMaterialList.size() <= 0) {
                return Element.Tc.getNeutrons();
            }
            long rAmount = 0, tAmount = 0;
            for (final MaterialStack tMaterial : this.mMaterialList) {
                tAmount += tMaterial.mAmount;
                rAmount += tMaterial.mAmount * tMaterial.mMaterial.getNeutrons();
            }
            return (this.getDensity() * rAmount) / (tAmount * M);
        }

        public long getMass() {
            if (this.mElement != null) {
                return this.mElement.getMass();
            }
            if (this.mMaterialList.size() <= 0) {
                return Element.Tc.getMass();
            }
            long rAmount = 0, tAmount = 0;
            for (final MaterialStack tMaterial : this.mMaterialList) {
                tAmount += tMaterial.mAmount;
                rAmount += tMaterial.mAmount * tMaterial.mMaterial.getMass();
            }
            return (this.getDensity() * rAmount) / (tAmount * M);
        }

        public long getDensity() {
            return this.mDensity;
        }

        public String getToolTip() {
            return this.getToolTip(1, false);
        }

        public String getToolTip(final boolean aShowQuestionMarks) {
            return this.getToolTip(1, aShowQuestionMarks);
        }

        public String getToolTip(final long aMultiplier) {
            return this.getToolTip(aMultiplier, false);
        }

        public String getToolTip(final long aMultiplier, final boolean aShowQuestionMarks) {
            if (!aShowQuestionMarks && this.mChemicalFormula.equals("?")) {
                return "";
            }
            if ((aMultiplier >= (M * 2)) && !this.mMaterialList.isEmpty()) {
                return (((this.mElement != null)
                    || ((this.mMaterialList.size() < 2) && (this.mMaterialList.get(0).mAmount == 1)))
                        ? this.mChemicalFormula
                        : "(" + this.mChemicalFormula + ")")
                    + aMultiplier;
            }
            return this.mChemicalFormula;
        }

        /**
         * Adds an ItemStack to this Material.
         */
        public GT_Materials add(final ItemStack aStack) {
            if ((aStack != null) && !this.contains(aStack)) {
                this.mMaterialItems.add(aStack);
            }
            return this;
        }

        /**
         * This is used to determine if any of the ItemStacks belongs to this Material.
         */
        public boolean contains(final ItemStack... aStacks) {
            if ((aStacks == null) || (aStacks.length <= 0)) {
                return false;
            }
            for (final ItemStack tStack : this.mMaterialItems) {
                for (final ItemStack aStack : aStacks) {
                    if (GTUtility.areStacksEqual(aStack, tStack, !tStack.hasTagCompound())) {
                        return true;
                    }
                }
            }
            return false;
        }

        /**
         * This is used to determine if an ItemStack belongs to this Material.
         */
        public boolean remove(final ItemStack aStack) {
            if (aStack == null) {
                return false;
            }
            boolean temp = false;
            for (int i = 0; i < this.mMaterialItems.size(); i++) {
                if (GTUtility.areStacksEqual(aStack, this.mMaterialItems.get(i))) {
                    this.mMaterialItems.remove(i--);
                    temp = true;
                }
            }
            return temp;
        }

        /**
         * Adds a SubTag to this Material
         */
        @Override
        public ISubTagContainer add(final SubTag... aTags) {
            if (aTags != null) {
                for (final SubTag aTag : aTags) {
                    if ((aTag != null) && !this.contains(aTag)) {
                        aTag.addContainerToList(this);
                        this.mSubTags.add(aTag);
                    }
                }
            }
            return this;
        }

        /**
         * If this Material has this exact SubTag
         */
        @Override
        public boolean contains(final SubTag aTag) {
            return this.mSubTags.contains(aTag);
        }

        /**
         * Removes a SubTag from this Material
         */
        @Override
        public boolean remove(final SubTag aTag) {
            return this.mSubTags.remove(aTag);
        }

        /**
         * Sets the Heat Damage for this Material (negative = frost)
         */
        public GT_Materials setHeatDamage(final float aHeatDamage) {
            this.mHeatDamage = aHeatDamage;
            return this;
        }

        /**
         * Adds a Material to the List of Byproducts when grinding this Ore. Is used for more precise Ore grinding, so
         * that it is possible to choose between certain kinds of Materials.
         */
        public GT_Materials addOreByProduct(final GT_Materials aMaterial) {
            if (!this.mOreByProducts.contains(aMaterial.mMaterialInto)) {
                this.mOreByProducts.add(aMaterial.mMaterialInto);
            }
            return this;
        }

        /**
         * Adds multiple Materials to the List of Byproducts when grinding this Ore. Is used for more precise Ore
         * grinding, so that it is possible to choose between certain kinds of Materials.
         */
        public GT_Materials addOreByProducts(final GT_Materials... aMaterials) {
            for (final GT_Materials tMaterial : aMaterials) {
                if (tMaterial != null) {
                    this.addOreByProduct(tMaterial);
                }
            }
            return this;
        }

        /**
         * If this Ore gives multiple drops of its Main Material. Lapis Ore for example gives about 6 drops.
         */
        public GT_Materials setOreMultiplier(final int aOreMultiplier) {
            if (aOreMultiplier > 0) {
                this.mOreMultiplier = aOreMultiplier;
            }
            return this;
        }

        /**
         * If this Ore gives multiple drops of its Byproduct Material.
         */
        public GT_Materials setByProductMultiplier(final int aByProductMultiplier) {
            if (aByProductMultiplier > 0) {
                this.mByProductMultiplier = aByProductMultiplier;
            }
            return this;
        }

        /**
         * If this Ore gives multiple drops of its Main Material. Lapis Ore for example gives about 6 drops.
         */
        public GT_Materials setSmeltingMultiplier(final int aSmeltingMultiplier) {
            if (aSmeltingMultiplier > 0) {
                this.mSmeltingMultiplier = aSmeltingMultiplier;
            }
            return this;
        }

        /**
         * This Ore should be smolten directly into an Ingot of this Material instead of an Ingot of itself.
         */
        public GT_Materials setDirectSmelting(final GT_Materials aMaterial) {
            if (aMaterial != null) {
                this.mDirectSmelting = aMaterial.mMaterialInto.mDirectSmelting;
            }
            return this;
        }

        /**
         * This Material should be the Main Material this Ore gets ground into. Example, Chromite giving Chrome or
         * Tungstate giving Tungsten.
         */
        public GT_Materials setOreReplacement(final GT_Materials aMaterial) {
            if (aMaterial != null) {
                this.mOreReplacement = aMaterial.mMaterialInto.mOreReplacement;
            }
            return this;
        }

        /**
         * This Material smelts always into an instance of aMaterial. Used for Magnets.
         */
        public GT_Materials setSmeltingInto(final GT_Materials aMaterial) {
            if (aMaterial != null) {
                this.mSmeltInto = aMaterial.mMaterialInto.mSmeltInto;
            }
            return this;
        }

        /**
         * This Material arc smelts always into an instance of aMaterial. Used for Wrought Iron.
         */
        public GT_Materials setArcSmeltingInto(final GT_Materials aMaterial) {
            if (aMaterial != null) {
                this.mArcSmeltInto = aMaterial.mMaterialInto.mArcSmeltInto;
            }
            return this;
        }

        /**
         * This Material macerates always into an instance of aMaterial.
         */
        public GT_Materials setMaceratingInto(final GT_Materials aMaterial) {
            if (aMaterial != null) {
                this.mMacerateInto = aMaterial.mMaterialInto.mMacerateInto;
            }
            return this;
        }

        public GT_Materials setEnchantmentForTools(final Enchantment aEnchantment, final int aEnchantmentLevel) {
            this.mEnchantmentTools = aEnchantment;
            this.mEnchantmentToolsLevel = (byte) aEnchantmentLevel;
            return this;
        }

        public GT_Materials setEnchantmentForArmors(final Enchantment aEnchantment, final int aEnchantmentLevel) {
            this.mEnchantmentArmors = aEnchantment;
            this.mEnchantmentArmorsLevel = (byte) aEnchantmentLevel;
            return this;
        }

        public FluidStack getSolid(final long aAmount) {
            if (this.mSolid == null) {
                return null;
            }
            return new FluidStack(this.mSolid, (int) aAmount);
        }

        public FluidStack getFluid(final long aAmount) {
            if (this.mFluid == null) {
                return null;
            }
            return new FluidStack(this.mFluid, (int) aAmount);
        }

        public FluidStack getGas(final long aAmount) {
            if (this.mGas == null) {
                return null;
            }
            return new FluidStack(this.mGas, (int) aAmount);
        }

        public FluidStack getPlasma(final long aAmount) {
            if (this.mPlasma == null) {
                return null;
            }
            return new FluidStack(this.mPlasma, (int) aAmount);
        }

        public FluidStack getMolten(final long aAmount) {
            if (this.mStandardMoltenFluid == null) {
                return null;
            }
            return new FluidStack(this.mStandardMoltenFluid, (int) aAmount);
        }

        @Override
        public short[] getRGBA() {
            return this.mRGBa;
        }

        public static final int VERSION = 508;
    }
}
