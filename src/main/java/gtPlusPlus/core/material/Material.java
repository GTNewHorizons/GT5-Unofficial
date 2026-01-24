package gtPlusPlus.core.material;

import static gregtech.api.enums.GTValues.M;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gtPlusPlus.core.util.math.MathUtils.safeCast_LongToInt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.interfaces.ISubTagContainer;
import gregtech.api.util.GTUtility;
import gregtech.api.util.StringUtils;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.item.base.BaseItemComponent.ComponentTypes;
import gtPlusPlus.core.item.base.cell.BaseItemCell;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.xmod.tinkers.material.BaseTinkersMaterial;

public class Material implements IOreMaterial {

    public static final Set<Material> mMaterialMap = new HashSet<>();
    public static final HashMap<String, Material> mMaterialCache = new HashMap<>();
    public static final HashMap<String, Material> mMaterialsByName = new HashMap<>();
    public static final Map<String, Map<String, ItemStack>> mComponentMap = new HashMap<>();
    public static final HashMap<String, String> sChemicalFormula = new HashMap<>();

    private String unlocalizedName;
    private String defaultLocalName;

    private MaterialState materialState;
    private TextureSet textureSet;

    private Fluid mFluid;
    private Fluid mPlasma;

    private boolean vGenerateCells;

    private ArrayList<MaterialStack> vMaterialInput = new ArrayList<>();
    public long[] vSmallestRatio;
    public short vComponentCount;

    private short[] RGBA;

    private boolean usesBlastFurnace;
    public boolean isRadioactive;
    public byte vRadiationLevel;

    private int meltingPointK;
    private int boilingPointK;
    private int meltingPointC;
    private int boilingPointC;
    private long vProtons;
    private long vNeutrons;
    private long vMass;
    public int smallestStackSizeWhenProcessing; // Add a check for <=0 || > 64
    public int vTier;
    public int vVoltageMultiplier;
    public String vChemicalFormula;
    public String vChemicalSymbol;

    public long vDurability;
    public int vToolQuality;
    public int vHarvestLevel;

    public BaseTinkersMaterial vTiConHandler;

    public short werkstoffID;

    public static ArrayList<Materials> invalidMaterials = new ArrayList<>();

    /** A cache field for raw ores to prevent constant map lookups. */
    private ItemStack rawOre;

    private boolean hasOre;

    public Material(final String materialName, final MaterialState defaultState, final MaterialStack... inputs) {
        this(materialName, defaultState, null, inputs);
    }

    public Material(final String materialName, final MaterialState defaultState, final short[] rgba,
        final MaterialStack... inputs) {
        this(materialName, defaultState, null, 0, rgba, -1, -1, -1, -1, false, "", 0, false, false, inputs);
    }

    public Material(final String materialName, final MaterialState defaultState, final short[] rgba, int radiationLevel,
        MaterialStack... materialStacks) {
        this(
            materialName,
            defaultState,
            null,
            0,
            rgba,
            -1,
            -1,
            -1,
            -1,
            false,
            "",
            radiationLevel,
            false,
            materialStacks);
    }

    public Material(String materialName, MaterialState defaultState, short[] rgba, int j, int k, int l, int m,
        int radiationLevel, MaterialStack[] materialStacks) {
        this(materialName, defaultState, null, 0, rgba, j, k, l, m, false, "", radiationLevel, false, materialStacks);
    }

    public Material(String materialName, MaterialState defaultState, final TextureSet set, short[] rgba,
        int meltingPoint, int boilingPoint, int protons, int neutrons, int radiationLevel,
        MaterialStack[] materialStacks) {
        this(
            materialName,
            defaultState,
            set,
            0,
            rgba,
            meltingPoint,
            boilingPoint,
            protons,
            neutrons,
            false,
            "",
            radiationLevel,
            false,
            materialStacks);
    }

    public Material(final String materialName, final MaterialState defaultState, final short[] rgba,
        final int meltingPoint, final int boilingPoint, final long protons, final long neutrons,
        final boolean blastFurnace, final MaterialStack... inputs) {
        this(
            materialName,
            defaultState,
            0,
            rgba,
            meltingPoint,
            boilingPoint,
            protons,
            neutrons,
            blastFurnace,
            "",
            0,
            inputs);
    }

    public Material(final String materialName, final MaterialState defaultState, final short[] rgba,
        final int meltingPoint, final int boilingPoint, final long protons, final long neutrons,
        final boolean blastFurnace, final String chemSymbol, final MaterialStack... inputs) {
        this(
            materialName,
            defaultState,
            0,
            rgba,
            meltingPoint,
            boilingPoint,
            protons,
            neutrons,
            blastFurnace,
            chemSymbol,
            0,
            inputs);
    }

    public Material(final String materialName, final MaterialState defaultState, final short[] rgba,
        final int meltingPoint, final int boilingPoint, final long protons, final long neutrons,
        final boolean blastFurnace, boolean generateCells, final MaterialStack... inputs) {
        this(
            materialName,
            defaultState,
            null,
            0,
            rgba,
            meltingPoint,
            boilingPoint,
            protons,
            neutrons,
            blastFurnace,
            "",
            0,
            generateCells,
            true,
            inputs);
    }

    public Material(final String materialName, final MaterialState defaultState, final short[] rgba,
        final int meltingPoint, final int boilingPoint, final long protons, final long neutrons,
        final boolean blastFurnace, final int radiationLevel, final MaterialStack... inputs) {
        this(
            materialName,
            defaultState,
            0,
            rgba,
            meltingPoint,
            boilingPoint,
            protons,
            neutrons,
            blastFurnace,
            "",
            radiationLevel,
            inputs);
    }

    public Material(final String materialName, final MaterialState defaultState, final long durability,
        final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons,
        final boolean blastFurnace, final int radiationLevel, final MaterialStack... inputs) {
        this(
            materialName,
            defaultState,
            durability,
            rgba,
            meltingPoint,
            boilingPoint,
            protons,
            neutrons,
            blastFurnace,
            "",
            radiationLevel,
            inputs);
    }

    public Material(final String materialName, final MaterialState defaultState, final short[] rgba,
        final int meltingPoint, final int boilingPoint, final long protons, final long neutrons,
        final boolean blastFurnace, final String chemicalSymbol, final int radiationLevel,
        final MaterialStack... inputs) {
        this(
            materialName,
            defaultState,
            0,
            rgba,
            meltingPoint,
            boilingPoint,
            protons,
            neutrons,
            blastFurnace,
            chemicalSymbol,
            radiationLevel,
            inputs);
    }

    public Material(final String materialName, final MaterialState defaultState, final short[] rgba,
        final int meltingPoint, final int boilingPoint, final long protons, final long neutrons,
        final boolean blastFurnace, final String chemicalSymbol, final int radiationLevel, boolean addCells,
        final MaterialStack... inputs) {
        this(
            materialName,
            defaultState,
            null,
            0,
            rgba,
            meltingPoint,
            boilingPoint,
            protons,
            neutrons,
            blastFurnace,
            chemicalSymbol,
            radiationLevel,
            addCells,
            true,
            inputs);
    }

    public Material(final String materialName, final MaterialState defaultState, TextureSet textureSet,
        final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons,
        final boolean blastFurnace, final String chemicalSymbol, final int radiationLevel,
        final MaterialStack... inputs) {
        this(
            materialName,
            defaultState,
            textureSet,
            0,
            rgba,
            meltingPoint,
            boilingPoint,
            protons,
            neutrons,
            blastFurnace,
            chemicalSymbol,
            radiationLevel,
            true,
            true,
            inputs);
    }

    public Material(final String materialName, final MaterialState defaultState, TextureSet textureSet,
        final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons,
        final boolean blastFurnace, final String chemicalSymbol, final int radiationLevel, boolean addCells,
        final MaterialStack... inputs) {
        this(
            materialName,
            defaultState,
            textureSet,
            0,
            rgba,
            meltingPoint,
            boilingPoint,
            protons,
            neutrons,
            blastFurnace,
            chemicalSymbol,
            radiationLevel,
            addCells,
            true,
            inputs);
    }

    private Material(final String materialName, final MaterialState defaultState, final long durability,
        final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons, final long neutrons,
        final boolean blastFurnace, final String chemicalSymbol, final int radiationLevel,
        final MaterialStack... inputs) {
        this(
            materialName,
            defaultState,
            null,
            durability,
            rgba,
            meltingPoint,
            boilingPoint,
            protons,
            neutrons,
            blastFurnace,
            chemicalSymbol,
            radiationLevel,
            true,
            true,
            inputs);
    }

    public Material(final String materialName, final MaterialState defaultState, final TextureSet set,
        final long durability, final short[] rgba, final int meltingPoint, final int boilingPoint, final long protons,
        final long neutrons, final boolean blastFurnace, final String chemicalSymbol, final int radiationLevel,
        boolean generateCells, final MaterialStack... inputs) {
        this(
            materialName,
            defaultState,
            set,
            durability,
            rgba,
            meltingPoint,
            boilingPoint,
            protons,
            neutrons,
            blastFurnace,
            chemicalSymbol,
            radiationLevel,
            true,
            true,
            inputs);
    }

    public Material(final String materialName, final MaterialState defaultState, final TextureSet set,
        final long durability, short[] rgba, final int meltingPoint, final int boilingPoint, final long protons,
        final long neutrons, final boolean blastFurnace, String chemicalSymbol, final int radiationLevel,
        boolean generateCells, boolean generateFluid, final MaterialStack... inputs) {
        this(
            materialName,
            materialName,
            defaultState,
            set,
            durability,
            rgba,
            meltingPoint,
            boilingPoint,
            protons,
            neutrons,
            blastFurnace,
            chemicalSymbol,
            radiationLevel,
            generateCells,
            generateFluid,
            inputs);
    }

    public Material(final String materialName, final String materialDefaultLocalName, final MaterialState defaultState,
        final TextureSet set, final long durability, short[] rgba, final int meltingPoint, final int boilingPoint,
        final long protons, final long neutrons, final boolean blastFurnace, String chemicalSymbol,
        final int radiationLevel, boolean generateCells, boolean generateFluid, final MaterialStack... inputs) {

        mMaterialMap.add(this);

        if (defaultState == MaterialState.ORE) {
            rgba = null;
        }

        mComponentMap.put(unlocalizedName, new HashMap<>());

        try {
            this.unlocalizedName = StringUtils.sanitizeString(materialName);
            this.defaultLocalName = materialDefaultLocalName;
            MaterialUtils.generateMaterialLocalizedName(unlocalizedName, defaultLocalName);
            mMaterialCache.put(getDefaultLocalName().toLowerCase(), this);
            mMaterialsByName.put(unlocalizedName, this);
            Logger
                .INFO("Stored " + getDefaultLocalName() + " to cache with key: " + getDefaultLocalName().toLowerCase());

            this.materialState = defaultState;

            Logger.MATERIALS(this.getDefaultLocalName() + " is " + defaultState.name() + ".");

            this.vGenerateCells = generateCells;

            // Add Components to an array.
            if (inputs == null) {
                this.vMaterialInput = null;
            } else {
                if (inputs.length != 0) {
                    for (int i = 0; i < inputs.length; i++) {
                        if (inputs[i] != null) {
                            this.vMaterialInput.add(i, inputs[i]);
                        }
                    }
                }
            }

            // set RGB

            if (rgba == null) {
                if (!vMaterialInput.isEmpty()) {

                    this.RGBA = getRGBColorForMat();

                } else {
                    // Boring Grey Material

                    int aValueForGen = this.getUnlocalizedName()
                        .hashCode();
                    int hashSize = MathUtils.howManyPlaces(aValueForGen);

                    String a = String.valueOf(aValueForGen);
                    String b = "";

                    if (hashSize < 9) {
                        int aSecondHash = this.materialState.hashCode();
                        int hashSize2 = MathUtils.howManyPlaces(aSecondHash);
                        if (hashSize2 + hashSize >= 9) {
                            b = String.valueOf(aValueForGen);
                        } else {
                            StringBuilder c = new StringBuilder(b);
                            while (MathUtils.howManyPlaces(hashSize + c.length()) < 9) {
                                c.append(
                                    c.toString()
                                        .hashCode());
                            }
                            b = c.toString();
                        }
                    }

                    String valueR;
                    valueR = a + b;
                    short[] fc = new short[3];
                    int aIndex = 0;
                    for (char gg : valueR.toCharArray()) {
                        short ui = Short.parseShort("" + gg);
                        if (ui > 255 || ui < 0) {
                            if (ui > 255) {
                                while (ui > 255) {
                                    ui = (short) (ui / 2);
                                }
                            } else {
                                ui = 0;
                            }
                        }
                        fc[aIndex++] = ui;
                    }
                    this.RGBA = fc;
                }
            } else {
                this.RGBA = rgba;
            }

            // Set Melting/Boiling point, if value is -1 calculate it from compound inputs.
            if (meltingPoint != -1) {
                this.meltingPointC = meltingPoint;
            } else {
                this.meltingPointC = this.calculateMeltingPoint();
            }
            if (boilingPoint != -1) {
                if (boilingPoint != 0) {
                    this.boilingPointC = boilingPoint;
                } else {
                    this.boilingPointC = meltingPoint * 4;
                }
            } else {
                this.boilingPointC = this.calculateBoilingPoint();
            }

            this.meltingPointK = (int) MathUtils.celsiusToKelvin(this.meltingPointC);
            this.boilingPointK = (int) MathUtils.celsiusToKelvin(this.boilingPointC);

            // Set Proton/Neutron count, if value is -1 calculate it from compound inputs.
            if (protons != -1) {
                this.vProtons = protons;
            } else {
                this.vProtons = this.calculateProtons();
            }
            if (boilingPoint != -1) {
                this.vNeutrons = neutrons;
            } else {
                this.vNeutrons = this.calculateNeutrons();
            }

            this.vMass = this.getMass();

            // Sets tool Durability
            if (durability != 0) {
                this.vDurability = durability;
            } else {
                long aTempDura = 0;
                for (MaterialStack g : this.getComposites()) {
                    if (g != null) {
                        aTempDura += safeCast_LongToInt(
                            g.getStackMaterial()
                                .getMass() * 2000);
                    }
                }
                this.vDurability = aTempDura > 0 ? aTempDura
                    : (this.getComposites()
                        .isEmpty() ? 51200
                            : 32_000L * this.getComposites()
                                .size());
            }

            if ((this.vDurability >= 0) && (this.vDurability < 64000)) {
                this.vToolQuality = 1;
                this.vHarvestLevel = 2;
            } else if ((this.vDurability >= 64000) && (this.vDurability < 128000)) {
                this.vToolQuality = 2;
                this.vHarvestLevel = 2;
            } else if ((this.vDurability >= 128000) && (this.vDurability < 256000)) {
                this.vToolQuality = 3;
                this.vHarvestLevel = 2;
            } else if ((this.vDurability >= 256000) && (this.vDurability < 512000)) {
                this.vToolQuality = 3;
                this.vHarvestLevel = 3;
            } else if ((this.vDurability >= 512000) && (this.vDurability <= Integer.MAX_VALUE)) {
                this.vToolQuality = 4;
                this.vHarvestLevel = 4;
            } else {
                this.vToolQuality = 1;
                this.vHarvestLevel = 1;
            }

            // Sets the Rad level
            if (radiationLevel > 0) {
                Logger.MATERIALS(this.getDefaultLocalName() + " is radioactive. Level: " + radiationLevel + ".");
                this.isRadioactive = true;
                this.vRadiationLevel = (byte) radiationLevel;
            } else {
                if (!vMaterialInput.isEmpty()) {
                    final byte radiation = calculateRadiation();
                    if (radiation > 0) {
                        Logger.MATERIALS(
                            this.getDefaultLocalName() + " is radioactive due to trace elements. Level: "
                                + radiation
                                + ".");
                        this.isRadioactive = true;
                        this.vRadiationLevel = radiation;
                    } else {
                        Logger.MATERIALS(this.getDefaultLocalName() + " is not radioactive.");
                        this.isRadioactive = false;
                        this.vRadiationLevel = 0;
                    }
                } else {
                    Logger.MATERIALS(this.getDefaultLocalName() + " is not radioactive.");
                    this.isRadioactive = false;
                    this.vRadiationLevel = 0;
                }
            }

            this.vTier = MaterialUtils.getTierOfMaterial(meltingPoint);

            // Sets the materials 'tier'. Will probably replace this logic.

            this.usesBlastFurnace = blastFurnace;
            this.vVoltageMultiplier = MaterialUtils.getVoltageForTier(vTier);

            this.vComponentCount = this.getComponentCount(inputs);
            this.vSmallestRatio = this.getSmallestRatio(this.vMaterialInput);
            int tempSmallestSize = 0;

            for (long l : this.vSmallestRatio) {
                tempSmallestSize = (int) (tempSmallestSize + l);
            }
            this.smallestStackSizeWhenProcessing = tempSmallestSize; // Valid stacksizes

            // Makes a Fancy Chemical Tooltip

            if (chemicalSymbol == null) {
                chemicalSymbol = "";
            }

            this.vChemicalSymbol = chemicalSymbol;
            if (this.vMaterialInput != null) {
                this.vChemicalFormula = this.getToolTip(chemicalSymbol, OrePrefixes.dust.getMaterialAmount() / M, true);
            } else if (!this.vChemicalSymbol.isEmpty()) {
                Logger.MATERIALS("materialInput is null, using a valid chemical symbol.");
                this.vChemicalFormula = this.vChemicalSymbol;
            } else {
                Logger.MATERIALS("MaterialInput == null && chemicalSymbol probably equals nothing");
                this.vChemicalSymbol = "??";
                this.vChemicalFormula = "??";
            }

            this.textureSet = setTextureSet(set, vTier);

            if (generateFluid) {
                final Materials aGregtechMaterial = tryFindGregtechMaterialEquivalent();
                FluidStack aTest = FluidUtils.getWildcardFluidStack(defaultLocalName, 1);
                if (aTest != null) {
                    this.mFluid = aTest.getFluid();
                    checkForCellAndGenerate(this);
                } else {
                    if (aGregtechMaterial != null && !MaterialUtils.isNullGregtechMaterial(aGregtechMaterial)) {
                        aTest = FluidUtils.getWildcardFluidStack(aGregtechMaterial, 1);
                    }
                    if (aTest != null) {
                        this.mFluid = aTest.getFluid();
                        checkForCellAndGenerate(this);
                    } else {
                        mFluid = generateFluid();
                    }
                }
                // Don't generate plasma for composites
                if (this.getComposites()
                    .isEmpty()) {
                    this.mPlasma = this.generatePlasma();
                }
            } else {
                this.mFluid = null;
                this.mPlasma = null;
            }
            StringBuilder ratio = new StringBuilder();
            if (this.vSmallestRatio != null) {
                for (long l : this.vSmallestRatio) {
                    if (ratio.toString()
                        .isEmpty()) {
                        ratio = new StringBuilder(String.valueOf(l));
                    } else {
                        ratio.append(":")
                            .append(l);
                    }
                }
            }

            if (TinkerConstruct.isModLoaded() && this.materialState == MaterialState.SOLID) {
                if (this.getProtons() >= 98 || this.getComposites()
                    .size() > 1 || this.getMeltingPointC() >= 3600) {
                    this.vTiConHandler = new BaseTinkersMaterial(this);
                }
            }

            sChemicalFormula.put(materialDefaultLocalName.toLowerCase(), this.vChemicalFormula);
            Logger.MATERIALS("Creating a Material instance for " + materialDefaultLocalName);
            Logger.MATERIALS(
                "Formula: " + this.vChemicalFormula
                    + " Smallest Stack: "
                    + this.smallestStackSizeWhenProcessing
                    + " Smallest Ratio:"
                    + ratio);
            Logger.MATERIALS("Protons: " + this.vProtons);
            Logger.MATERIALS("Neutrons: " + this.vNeutrons);
            Logger.MATERIALS("Mass: " + this.vMass + "/units");
            Logger.MATERIALS("Melting Point: " + this.meltingPointC + "C.");
            Logger.MATERIALS("Boiling Point: " + this.boilingPointC + "C.");
        } catch (Throwable t) {
            Logger.MATERIALS("Stack Trace for " + materialDefaultLocalName);
            t.printStackTrace();
        }
    }

    private static void checkForCellAndGenerate(Material material) {
        if (!material.vGenerateCells) {
            return;
        }
        String aName = StringUtils.sanitizeString(material.unlocalizedName);
        String aName2 = StringUtils.sanitizeString(material.unlocalizedName.toLowerCase());
        String aName3 = (material.defaultLocalName == null) ? aName : material.defaultLocalName;
        ItemStack aTestCell1 = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell" + aName, 1);
        ItemStack aTestCell2 = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell" + aName2, 1);
        ItemStack aTestCell3 = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell" + aName3, 1);
        if (aTestCell1 == null && aTestCell2 == null && aTestCell3 == null) {
            Logger.INFO("Generating cell for " + material.defaultLocalName);
            new BaseItemCell(material);
        } else {
            if (aTestCell1 != null) {
                Logger.INFO("Registering existing cell for " + material.defaultLocalName + ", " + aName);
                material.registerComponentForMaterial(OrePrefixes.cell, aTestCell1);
            } else if (aTestCell2 != null) {
                Logger.INFO("Registering existing cell for " + material.defaultLocalName + ", " + aName2);
                material.registerComponentForMaterial(OrePrefixes.cell, aTestCell2);
            } else {
                Logger.INFO("Registering existing cell for " + material.defaultLocalName + ", " + aName3);
                material.registerComponentForMaterial(OrePrefixes.cell, aTestCell3);
            }
        }
    }

    @Override
    public final TextureSet getTextureSet() {
        synchronized (this) {
            return textureSet;
        }
    }

    public TextureSet setTextureSet(TextureSet set) {
        return setTextureSet(set, vTier);
    }

    public TextureSet setTextureSet(TextureSet set, int aTier) {
        if (set != null) {
            Logger.MATERIALS(
                "Set textureset for " + this.defaultLocalName
                    + " to be "
                    + set.mSetName
                    + ". This textureSet was supplied.");
            return set;
        }

        int aGem = 0;
        int aShiny = 0;

        // Check Mixture Contents
        for (MaterialStack m : this.getComposites()) {

            // Gems
            if (m.getStackMaterial() == MaterialsElements.getInstance().AER) {
                aGem++;
            } else if (m.getStackMaterial() == MaterialsElements.getInstance().AQUA) {
                aGem++;
            } else if (m.getStackMaterial() == MaterialsElements.getInstance().IGNIS) {
                aGem++;
            } else if (m.getStackMaterial() == MaterialsElements.getInstance().TERRA) {
                aGem++;
            } else if (m.getStackMaterial() == MaterialsElements.getInstance().MAGIC) {
                aGem++;
            }
            // Shiny Materials
            if (m.getStackMaterial() == MaterialsElements.getInstance().GOLD) {
                aShiny++;
            } else if (m.getStackMaterial() == MaterialsElements.getInstance().SILVER) {
                aShiny++;
            } else if (m.getStackMaterial() == MaterialsElements.getInstance().PLATINUM) {
                aShiny++;
            } else if (m.getStackMaterial() == MaterialsElements.getInstance().TITANIUM) {
                aShiny++;
            } else if (m.getStackMaterial() == MaterialsElements.getInstance().GERMANIUM) {
                aShiny++;
            } else if (m.getStackMaterial() == MaterialsElements.getInstance().GALLIUM) {
                aShiny++;
            } else if (m.getStackMaterial() == MaterialsElements.getInstance().MERCURY) {
                aShiny++;
            } else if (m.getStackMaterial() == MaterialsElements.getInstance().MAGIC) {
                aShiny++;
            } else if (m.getStackMaterial() == MaterialsElements.getInstance().SAMARIUM) {
                aShiny++;
            } else if (m.getStackMaterial() == MaterialsElements.getInstance().TANTALUM) {
                aShiny++;
            }
        }

        if (aGem >= this.getComposites()
            .size() / 2) {
            if (MathUtils.isNumberEven(aGem)) {
                Logger.MATERIALS(
                    "Set textureset for " + this.defaultLocalName
                        + " to be "
                        + TextureSet.SET_GEM_HORIZONTAL.mSetName
                        + ".");
                return TextureSet.SET_GEM_HORIZONTAL;
            } else {
                Logger.MATERIALS(
                    "Set textureset for " + this.defaultLocalName
                        + " to be "
                        + TextureSet.SET_GEM_VERTICAL.mSetName
                        + ".");
                return TextureSet.SET_GEM_VERTICAL;
            }
        }

        if (aShiny >= this.getComposites()
            .size() / 3) {
            Logger.MATERIALS(
                "Set textureset for " + this.defaultLocalName + " to be " + TextureSet.SET_SHINY.mSetName + ".");
            return TextureSet.SET_SHINY;
        }

        // build hash table with count
        ArrayList<Material> sets = new ArrayList<>();
        for (MaterialStack r : this.vMaterialInput) {
            if (r.getStackMaterial()
                .getTextureSet().mSetName.toLowerCase()
                    .contains("fluid")) {
                sets.add(MaterialsElements.getInstance().GOLD);
            } else {
                sets.add(r.getStackMaterial());
            }
        }
        TextureSet mostUsedTypeTextureSet = MaterialUtils.getMostCommonTextureSet(sets);
        if (mostUsedTypeTextureSet instanceof TextureSet) {
            Logger.MATERIALS(
                "Set textureset for " + this.defaultLocalName + " to be " + mostUsedTypeTextureSet.mSetName + ".");
            return mostUsedTypeTextureSet;
        }
        Logger.MATERIALS(
            "Set textureset for " + this.defaultLocalName
                + " to be "
                + Materials.Iron.mIconSet.mSetName
                + ". [Fallback]");
        return Materials.Gold.mIconSet;
    }

    public final String getDefaultLocalName() {
        if (this.defaultLocalName != null) {
            return this.defaultLocalName;
        }
        return "ERROR BAD DEFAULT LOCAL NAME";
    }

    @Override
    public String getLocalizedName() {
        return MaterialUtils.getMaterialLocalizedName(unlocalizedName);
    }

    @Override
    public String getLocalizedNameKey() {
        return MaterialUtils.getMaterialLocalizedNameKey(unlocalizedName);
    }

    @Override
    public int getId() {
        ItemStack dust = getDust(1);

        if (dust != null) return Item.getIdFromItem(dust.getItem());

        ItemStack ingot = getIngot(1);

        if (ingot != null) return Item.getIdFromItem(ingot.getItem());

        ItemStack ore = getOre(1);

        if (ore != null) return Item.getIdFromItem(ore.getItem());

        return 0;
    }

    @Override
    public List<IStoneType> getValidStones() {
        return StoneType.STONE_ONLY;
    }

    @Override
    public String getInternalName() {
        return getUnlocalizedName();
    }

    public final String getUnlocalizedName() {
        if (this.unlocalizedName != null) {
            return this.unlocalizedName;
        }
        return "ERROR.BAD.UNLOCALIZED.NAME";
    }

    @Override
    public String toString() {
        return "Material{" + "unlocalizedName='" + unlocalizedName + '\'' + '}';
    }

    public final MaterialState getState() {
        return this.materialState;
    }

    public final short[] getRGB() {
        if (this.RGBA != null) {
            return this.RGBA;
        }
        return new short[] { 255, 0, 0 };
    }

    @Override
    public final short[] getRGBA() {
        if (this.RGBA != null) {
            if (this.RGBA.length == 4) {
                return this.RGBA;
            } else {
                return new short[] { this.RGBA[0], this.RGBA[1], this.RGBA[2], 0 };
            }
        }
        return new short[] { 255, 0, 0, 0 };
    }

    public final int getRgbAsHex() {
        final int returnValue = Utils.rgbtoHexValue(this.RGBA[0], this.RGBA[1], this.RGBA[2]);
        if (returnValue == 0) {
            return Dyes._NULL.toInt();
        }
        return Utils.rgbtoHexValue(this.RGBA[0], this.RGBA[1], this.RGBA[2]);
    }

    public final long getProtons() {
        return this.vProtons;
    }

    public final long getNeutrons() {
        return this.vNeutrons;
    }

    public final long getMass() {
        return this.vProtons + this.vNeutrons;
    }

    public final int getMeltingPointC() {
        return this.meltingPointC;
    }

    public final int getBoilingPointC() {
        return this.boilingPointC;
    }

    public final int getMeltingPointK() {
        return this.meltingPointK;
    }

    public final int getBoilingPointK() {
        return this.boilingPointK;
    }

    public final boolean requiresBlastFurnace() {
        return this.usesBlastFurnace;
    }

    public final ItemStack getComponentByPrefix(OrePrefixes aPrefix, int stacksize) {
        String aKey = aPrefix.getName();
        Map<String, ItemStack> g = mComponentMap.get(this.unlocalizedName);
        if (g == null) {
            Map<String, ItemStack> aMap = new HashMap<>();
            mComponentMap.put(unlocalizedName, aMap);
            g = aMap;
        }
        ItemStack i = g.get(aKey);
        if (i != null) {
            return GTUtility.copyAmount(stacksize, i);
        } else {
            // Try get a GT Material
            Materials Erf = MaterialUtils.getMaterial(this.unlocalizedName);
            if (Erf != null && !MaterialUtils.isNullGregtechMaterial(Erf)) {
                ItemStack Erg = ItemUtils.getOrePrefixStack(aPrefix, Erf, stacksize);
                if (Erg != null) {
                    Logger.MATERIALS("Found \"" + aKey + this.unlocalizedName + "\" using backup GT Materials option.");
                    g.put(aKey, Erg);
                    mComponentMap.put(unlocalizedName, g);
                    return Erg;
                } else {
                    // Try get a molten cell
                    if (aPrefix == OrePrefixes.cell) {
                        Erg = ItemUtils.getOrePrefixStack(OrePrefixes.cellMolten, Erf, stacksize);
                        if (Erg != null) {
                            Logger.MATERIALS(
                                "Found \"" + OrePrefixes.cellMolten.getName()
                                    + this.unlocalizedName
                                    + "\" using backup GT Materials option.");
                            g.put(aKey, Erg);
                            mComponentMap.put(unlocalizedName, g);
                            return Erg;
                        }
                    }
                }
            } else {
                ItemStack u = ItemUtils.getItemStackOfAmountFromOreDictNoBroken(aKey + this.unlocalizedName, stacksize);
                if (u != null) {
                    g.put(aKey, u);
                    mComponentMap.put(unlocalizedName, g);
                    return u;
                }
            }
            return null;
        }
    }

    public final Block getBlock() {
        Block b = Block.getBlockFromItem(getBlock(1).getItem());
        if (b == null) {
            Logger.INFO(
                "[ERROR] Tried to get invalid block for " + this.getDefaultLocalName()
                    + ", returning debug block instead.");
        }
        return b != null ? b : Blocks.lit_furnace;
    }

    public final ItemStack getBlock(final int stacksize) {
        ItemStack i = getComponentByPrefix(OrePrefixes.block, stacksize);
        return i != null ? i
            : ItemUtils.getItemStackOfAmountFromOreDictNoBroken("block" + this.unlocalizedName, stacksize);
    }

    public final ItemStack getDust(final int stacksize) {
        ItemStack i = getComponentByPrefix(OrePrefixes.dust, stacksize);
        return i != null ? i : ItemUtils.getGregtechDust("dust" + this.unlocalizedName, stacksize);
    }

    public final ItemStack getSmallDust(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.dustSmall, stacksize);
    }

    public final ItemStack getTinyDust(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.dustTiny, stacksize);
    }

    public final ItemStack getIngot(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.ingot, stacksize);
    }

    public final ItemStack getHotIngot(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.ingotHot, stacksize);
    }

    public final ItemStack getPlate(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.plate, stacksize);
    }

    public final ItemStack getPlateDouble(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.plateDouble, stacksize);
    }

    public final ItemStack getPlateDense(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.plateDense, stacksize);
    }

    public final ItemStack getGear(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.gearGt, stacksize);
    }

    public final ItemStack getRod(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.stick, stacksize);
    }

    public final ItemStack getLongRod(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.stickLong, stacksize);
    }

    public final ItemStack getBolt(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.bolt, stacksize);
    }

    public final ItemStack getScrew(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.screw, stacksize);
    }

    public final ItemStack getFineWire(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.wireFine, stacksize);
    }

    public final ItemStack getFoil(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.foil, stacksize);
    }

    public final ItemStack getRing(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.ring, stacksize);
    }

    public final ItemStack getRotor(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.rotor, stacksize);
    }

    public final ItemStack getFrameBox(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.frameGt, stacksize);
    }

    public final ItemStack getCell(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.cell, stacksize);
    }

    public final ItemStack getPlasmaCell(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.cellPlasma, stacksize);
    }

    public final ItemStack getNugget(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.nugget, stacksize);
    }

    public final ItemStack getWire01(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.wireGt01, stacksize);
    }

    public final ItemStack getWire02(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.wireGt02, stacksize);
    }

    public final ItemStack getWire04(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.wireGt04, stacksize);
    }

    public final ItemStack getWire08(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.wireGt08, stacksize);
    }

    public final ItemStack getWire12(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.wireGt12, stacksize);
    }

    public final ItemStack getWire16(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.wireGt16, stacksize);
    }

    public final ItemStack getCable01(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.cableGt01, stacksize);
    }

    public final ItemStack getCable02(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.cableGt02, stacksize);
    }

    public final ItemStack getCable04(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.cableGt04, stacksize);
    }

    public final ItemStack getCable08(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.cableGt08, stacksize);
    }

    public final ItemStack getCable12(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.cableGt12, stacksize);
    }

    public final ItemStack getCable16(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.cableGt16, stacksize);
    }

    private ItemStack ore;

    /**
     * Ore Components
     *
     * @return
     */
    public final ItemStack getOre(final int stacksize) {
        if (ore == null) {
            ore = ItemUtils.getItemStackOfAmountFromOreDictNoBroken(
                "ore" + StringUtils.sanitizeString(this.getUnlocalizedName()),
                1);
        }

        return GTUtility.copyAmount(stacksize, ore);
    }

    public final boolean hasOre() {
        return hasOre;
    }

    public void setHasOre() {
        this.hasOre = true;
    }

    public final Block getOreBlock(final int stacksize) {
        // Logger.DEBUG_MATERIALS("Trying to get ore block for "+this.getLocalizedName()+". Looking for
        // '"+"ore"+StringUtils.sanitizeString(this.getUnlocalizedName())+"'.");
        try {
            ItemStack a1 = getOre(1);
            Item a2 = a1.getItem();
            Block a3 = Block.getBlockFromItem(a2);
            if (a3 != null) {
                return a3;
            }

            Block x = Block.getBlockFromItem(
                ItemUtils
                    .getItemStackOfAmountFromOreDictNoBroken(
                        "ore" + StringUtils.sanitizeString(this.unlocalizedName),
                        stacksize)
                    .getItem());
            if (x != null) {
                return x;
            }
        } catch (Exception t) {
            t.printStackTrace();
        }
        // Logger.MATERIALS("Failed getting the Ore Block for "+this.getLocalizedName()+".");
        return Blocks.stone;
    }

    public final ItemStack getCrushed(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.crushed, stacksize);
    }

    public final ItemStack getCrushedPurified(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.crushedPurified, stacksize);
    }

    public final ItemStack getCrushedCentrifuged(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.crushedCentrifuged, stacksize);
    }

    public final ItemStack getDustPurified(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.dustPure, stacksize);
    }

    public final ItemStack getDustImpure(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.dustImpure, stacksize);
    }

    public final ItemStack getMilled(final int stacksize) {
        return getComponentByPrefix(OrePrefixes.milled, stacksize);
    }

    public final ItemStack getRawOre(final int stacksize) {
        if (rawOre == null) {
            rawOre = getComponentByPrefix(OrePrefixes.rawOre, 1);
        }

        return GTUtility.copyAmount(stacksize, rawOre);
    }

    public final boolean hasSolidForm() {
        return ItemUtils
            .checkForInvalidItems(new ItemStack[] { getDust(1), getBlock(1), getTinyDust(1), getSmallDust(1) });
    }

    public final ItemStack[] getMaterialComposites() {
        if (this.vMaterialInput != null && !this.vMaterialInput.isEmpty()) {
            final ItemStack[] temp = new ItemStack[this.vMaterialInput.size()];
            for (int i = 0; i < this.vMaterialInput.size(); i++) {
                // Utils.LOG_MATERIALS("i:"+i);
                ItemStack testNull = null;
                try {
                    testNull = this.vMaterialInput.get(i)
                        .getValidStack();
                } catch (final Throwable r) {
                    Logger.MATERIALS("Failed gathering material stack for " + this.defaultLocalName + ".");
                    Logger.MATERIALS("What Failed: Length:" + this.vMaterialInput.size() + " current:" + i);
                }
                try {
                    if (testNull != null) {
                        // Utils.LOG_MATERIALS("not null");
                        temp[i] = this.vMaterialInput.get(i)
                            .getValidStack();
                    }
                } catch (final Throwable r) {
                    Logger.MATERIALS("Failed setting slot " + i + ", using " + this.defaultLocalName);
                }
            }
            return temp;
        }
        return GTValues.emptyItemStackArray;
    }

    public final ArrayList<MaterialStack> getComposites() {
        return this.vMaterialInput;
    }

    public final int[] getMaterialCompositeStackSizes() {
        if (!this.vMaterialInput.isEmpty()) {
            final int[] temp = new int[this.vMaterialInput.size()];
            for (int i = 0; i < this.vMaterialInput.size(); i++) {
                if (this.vMaterialInput.get(i) != null) {
                    temp[i] = this.vMaterialInput.get(i)
                        .getDustStack().stackSize;
                } else {
                    temp[i] = 0;
                }
            }
            return temp;
        }
        return GTValues.emptyIntArray;
    }

    private short getComponentCount(final MaterialStack[] inputs) {

        if (inputs == null || inputs.length < 1) {
            return 1;
        }
        int counterTemp = 0;
        for (final MaterialStack m : inputs) {
            if (m.getStackMaterial() != null) {
                counterTemp++;
            }
        }
        if (counterTemp != 0) {
            return (short) counterTemp;
        } else {
            return 1;
        }
    }

    public final long[] getSmallestRatio(final ArrayList<MaterialStack> tempInput) {
        if (tempInput != null) {
            if (!tempInput.isEmpty()) {
                Logger.MATERIALS("length: " + tempInput.size());
                Logger.MATERIALS("(inputs != null): true");
                final long[] tempRatio = new long[tempInput.size()];
                for (int x = 0; x < tempInput.size(); x++) {
                    if (tempInput.get(x) != null) {
                        tempRatio[x] = tempInput.get(x)
                            .getPartsPerOneHundred();
                    }
                }

                final long[] smallestRatio = MathUtils.simplifyNumbersToSmallestForm(tempRatio);

                if (smallestRatio.length > 0) {
                    StringBuilder tempRatioStringThing1 = new StringBuilder();
                    for (long value : tempRatio) {
                        tempRatioStringThing1.append(value)
                            .append(" : ");
                    }
                    Logger.MATERIALS("Default Ratio: " + tempRatioStringThing1);

                    StringBuilder tempRatioStringThing = new StringBuilder();
                    int tempSmallestCraftingUseSize = 0;
                    for (long l : smallestRatio) {
                        tempRatioStringThing.append(l)
                            .append(" : ");
                        tempSmallestCraftingUseSize = (int) (tempSmallestCraftingUseSize + l);
                    }
                    // this.smallestStackSizeWhenProcessing = tempSmallestCraftingUseSize;
                    Logger.MATERIALS("Smallest Ratio: " + tempRatioStringThing);
                    return smallestRatio;
                }
            }
        }
        return new long[] {};
    }

    public final String getToolTip(final String chemSymbol, final long aMultiplier, final boolean aShowQuestionMarks) {
        if (!aShowQuestionMarks && (this.vChemicalFormula.equals("?") || this.vChemicalFormula.equals("??"))) {
            return "";
        }
        Logger.MATERIALS(
            "===============| Calculating Atomic Formula for " + this.defaultLocalName + " |===============");
        if (!chemSymbol.isEmpty()) {
            return chemSymbol;
        }
        final ArrayList<MaterialStack> tempInput = this.vMaterialInput;
        if (tempInput != null) {
            if (!tempInput.isEmpty()) {
                StringBuilder dummyFormula = new StringBuilder();
                final long[] dummyFormulaArray = this.getSmallestRatio(tempInput);
                if (dummyFormulaArray.length >= 1) {
                    for (int e = 0; e < tempInput.size(); e++) {
                        MaterialStack g = tempInput.get(e);
                        if (g != null) {
                            if (g.getStackMaterial() != null) {

                                String aChemSymbol = g.getStackMaterial().vChemicalSymbol;
                                String aChemFormula = g.getStackMaterial().vChemicalFormula;

                                if (aChemSymbol == null) {
                                    aChemSymbol = "??";
                                }
                                if (aChemFormula == null) {
                                    aChemFormula = "??";
                                }

                                if (!aChemSymbol.equals("??")) {
                                    if (dummyFormulaArray[e] > 1) {

                                        if (aChemFormula.length() > 3 || StringUtils.uppercaseCount(aChemFormula) > 1) {
                                            dummyFormula.append("(")
                                                .append(aChemFormula)
                                                .append(")")
                                                .append(dummyFormulaArray[e]);
                                        } else {
                                            dummyFormula.append(aChemFormula)
                                                .append(dummyFormulaArray[e]);
                                        }
                                    } else if (dummyFormulaArray[e] == 1) {
                                        if (aChemFormula.length() > 3 || StringUtils.uppercaseCount(aChemFormula) > 1) {
                                            dummyFormula.append("(")
                                                .append(aChemFormula)
                                                .append(")");
                                        } else {
                                            dummyFormula.append(aChemFormula);
                                        }
                                    } else {
                                        dummyFormula.append("??");
                                    }
                                } else {
                                    dummyFormula.append("??");
                                }
                            } else {
                                dummyFormula.append("??");
                            }
                        }
                    }
                    return StringUtils.subscript(dummyFormula.toString());
                    // return dummyFormula;
                }
                Logger.MATERIALS("dummyFormulaArray <= 0");
                Logger.MATERIALS("dummyFormulaArray == null");
            }
            Logger.MATERIALS("tempInput.length <= 0");
        }
        Logger.MATERIALS("tempInput == null");
        return "??";
    }

    public final Fluid generateFluid() {
        if (this.materialState == MaterialState.ORE) {
            return null;
        }

        Fluid aGTBaseFluid = null;

        // Clean up Internal Fluid Generation
        final Materials n1 = MaterialUtils
            .getMaterial(this.getDefaultLocalName(), StringUtils.sanitizeString(this.getDefaultLocalName()));
        final Materials n2 = MaterialUtils
            .getMaterial(this.getUnlocalizedName(), StringUtils.sanitizeString(this.getUnlocalizedName()));

        FluidStack f1 = FluidUtils.getWildcardFluidStack(n1, 1);
        FluidStack f2 = FluidUtils.getWildcardFluidStack(n2, 1);
        FluidStack f3 = FluidUtils
            .getWildcardFluidStack(StringUtils.sanitizeStringKeepDashes(this.getUnlocalizedName()), 1);
        FluidStack f4 = FluidUtils
            .getWildcardFluidStack(StringUtils.sanitizeStringKeepDashes(this.getDefaultLocalName()), 1);

        if (f1 != null) {
            aGTBaseFluid = f1.getFluid();
        } else if (f2 != null) {
            aGTBaseFluid = f2.getFluid();
        } else if (f3 != null) {
            aGTBaseFluid = f3.getFluid();
        } else if (f4 != null) {
            aGTBaseFluid = f4.getFluid();
        }

        ItemStack aFullCell = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell" + this.getUnlocalizedName(), 1);
        ItemStack aFullCell2 = ItemUtils
            .getItemStackOfAmountFromOreDictNoBroken("cell" + this.getDefaultLocalName(), 1);
        ItemStack aFullCell3 = ItemUtils.getItemStackOfAmountFromOreDictNoBroken(
            "cell" + StringUtils.sanitizeStringKeepDashes(this.getUnlocalizedName()),
            1);
        ItemStack aFullCell4 = ItemUtils.getItemStackOfAmountFromOreDictNoBroken(
            "cell" + StringUtils.sanitizeStringKeepDashes(this.getDefaultLocalName()),
            1);

        Logger.MATERIALS("Generating our own fluid.");
        // Generate a Cell if we need to, but first validate all four searches are invalid

        if (!ItemUtils.checkForInvalidItems(new ItemStack[] { aFullCell, aFullCell2, aFullCell3, aFullCell4 })) {
            if (this.vGenerateCells) {
                Item g = new BaseItemCell(this);
                aFullCell = new ItemStack(g);
                Logger.MATERIALS("Generated a cell for " + this.getUnlocalizedName());
            } else {
                Logger.MATERIALS("Did not generate a cell for " + this.getUnlocalizedName());
            }
        } else {
            // One cell we searched for was valid, let's register it.
            if (aFullCell != null) {
                this.registerComponentForMaterial(ComponentTypes.CELL, aFullCell);
            } else if (aFullCell2 != null) {
                this.registerComponentForMaterial(ComponentTypes.CELL, aFullCell2);
            } else if (aFullCell3 != null) {
                this.registerComponentForMaterial(ComponentTypes.CELL, aFullCell3);
            } else if (aFullCell4 != null) {
                this.registerComponentForMaterial(ComponentTypes.CELL, aFullCell4);
            }
        }

        // We found a GT fluid, let's use it.
        // Good chance we registered the cell from this material too.
        if (aGTBaseFluid != null) {
            return aGTBaseFluid;
        }

        // This fluid does not exist at all, time to generate it.
        if (this.materialState == MaterialState.SOLID) {
            return FluidUtils.addGTFluidMolten(
                this.getUnlocalizedName(),
                "Molten " + this.getDefaultLocalName(),
                this.RGBA,
                4,
                this.getMeltingPointK(),
                aFullCell,
                ItemList.Cell_Empty.get(1),
                1000,
                this.vGenerateCells,
                this);
        } else if (this.materialState == MaterialState.LIQUID || this.materialState == MaterialState.PURE_LIQUID) {
            return FluidUtils.addGTFluidMolten(
                this.getUnlocalizedName(),
                this.getDefaultLocalName(),
                this.RGBA,
                0,
                this.getMeltingPointK(),
                aFullCell,
                ItemList.Cell_Empty.get(1),
                1000,
                this.vGenerateCells,
                this);
        } else if (this.materialState == MaterialState.GAS || this.materialState == MaterialState.PURE_GAS) {
            return FluidUtils.generateGas(
                unlocalizedName,
                this.getDefaultLocalName(),
                getMeltingPointK(),
                getRGBA(),
                vGenerateCells);

        } else { // Plasma
            return this.generatePlasma();
        }
    }

    public final Fluid generatePlasma() {
        if (this.materialState == MaterialState.ORE) {
            return null;
        }
        final Materials isValid = tryFindGregtechMaterialEquivalent();

        if (!this.vGenerateCells) {
            return null;
        }
        if (isValid != null) {
            for (Materials m : invalidMaterials) {
                if (isValid == m) {
                    return null;
                }
            }
            if (isValid.mPlasma != null) {
                Logger.MATERIALS("Using a pre-defined Plasma from GT.");
                return isValid.mPlasma;
            }
        }
        Logger.MATERIALS("Generating our own Plasma.");
        return FluidUtils.addGTPlasma(this);
    }

    public Fluid getFluid() {
        return mFluid;
    }

    public Fluid getPlasma() {
        return mPlasma;
    }

    public final FluidStack getFluidStack(final int fluidAmount) {
        if (this.mFluid == null) {
            return null;
        }
        return new FluidStack(this.mFluid, fluidAmount);
    }

    public final boolean setFluid(Fluid aFluid) {
        if (this.mFluid == null) {
            this.mFluid = aFluid;
            return true;
        }
        return false;
    }

    private short[] getRGBColorForMat() {
        try {
            Set<Material> materialSet = new HashSet<>(MaterialUtils.getCompoundMaterialsRecursively(this));
            final int size = materialSet.size();
            if (size == 0) {
                return Materials.Steel.mRGBa;
            }
            long redSum = 0;
            long greenSum = 0;
            long blueSum = 0;
            for (Material mat : materialSet) {
                final short[] rgb = mat.getRGB();
                redSum += rgb[0];
                greenSum += rgb[1];
                blueSum += rgb[2];
            }
            short avgRed = getAvgColor(redSum, size);
            short avgGreen = getAvgColor(greenSum, size);
            short avgBlue = getAvgColor(blueSum, size);
            if (avgRed != 0 && avgGreen != 0 && avgBlue != 0) {
                return new short[] { avgRed, avgGreen, avgBlue, 0 };
            } else {
                return Materials.Steel.mRGBa;
            }
        } catch (Exception t) {
            t.printStackTrace();
            return Materials.Steel.mRGBa;
        }
    }

    private short getAvgColor(long sum, int size) {
        long avg = sum / size;
        if (avg < 0 || avg > 255) {
            while (avg > 255) {
                avg = avg / 2;
            }
            avg = Math.max(avg, 0);
        }
        return (short) avg;
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private byte calculateRadiation() {
        ArrayList<MaterialStack> list = this.vMaterialInput;
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        long sum = 0;
        for (int i = 0; i < size; i++) {
            sum += list.get(i)
                .getStackMaterial().vRadiationLevel;
        }
        return MathUtils.safeByte(sum / size);
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private int calculateMeltingPoint() {
        final ArrayList<MaterialStack> list = this.vMaterialInput;
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        long sum = 0;
        for (int i = 0; i < size; i++) {
            sum += list.get(i)
                .getStackMaterial()
                .getMeltingPointC();
        }
        return MathUtils.safeInt(sum / size);
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private int calculateBoilingPoint() {
        final ArrayList<MaterialStack> list = this.vMaterialInput;
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        long sum = 0;
        for (int i = 0; i < size; i++) {
            sum += list.get(i)
                .getStackMaterial()
                .getBoilingPointC();
        }
        return MathUtils.safeInt(sum / size);
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private long calculateProtons() {
        final ArrayList<MaterialStack> list = this.vMaterialInput;
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        long sum = 0;
        for (int i = 0; i < size; i++) {
            sum += list.get(i)
                .getStackMaterial()
                .getProtons();
        }
        return MathUtils.safeInt(sum / size);
    }

    @SuppressWarnings("ForLoopReplaceableByForEach")
    private long calculateNeutrons() {
        final ArrayList<MaterialStack> list = this.vMaterialInput;
        int size = list.size();
        if (size == 0) {
            return 0;
        }
        long sum = 0;
        for (int i = 0; i < size; i++) {
            sum += list.get(i)
                .getStackMaterial()
                .getNeutrons();
        }
        return MathUtils.safeInt(sum / size);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!(obj instanceof Material aObj)) {
            return false;
        }
        if (aObj.unlocalizedName.equals(this.unlocalizedName)) {
            return aObj.defaultLocalName.equals(this.defaultLocalName);
        }
        return false;
    }

    public boolean registerComponentForMaterial(FluidStack aStack) {
        return registerComponentForMaterial(this, aStack);
    }

    private static boolean registerComponentForMaterial(Material componentMaterial, FluidStack aStack) {
        if (componentMaterial != null && aStack != null && componentMaterial.mFluid == null) {
            componentMaterial.mFluid = aStack.getFluid();
            return true;
        }
        return false;
    }

    public boolean registerComponentForMaterial(ComponentTypes aPrefix, ItemStack aStack) {
        return registerComponentForMaterial(this, aPrefix.getGtOrePrefix(), aStack);
    }

    public boolean registerComponentForMaterial(OrePrefixes aPrefix, ItemStack aStack) {
        return registerComponentForMaterial(this, aPrefix, aStack);
    }

    public static boolean registerComponentForMaterial(Material componentMaterial, ComponentTypes aPrefix,
        ItemStack aStack) {
        return registerComponentForMaterial(componentMaterial, aPrefix.getGtOrePrefix(), aStack);
    }

    public static boolean registerComponentForMaterial(Material componentMaterial, OrePrefixes aPrefix,
        ItemStack aStack) {
        if (componentMaterial == null) {
            return false;
        }
        // Register Component
        Map<String, ItemStack> aMap = Material.mComponentMap.get(componentMaterial.getUnlocalizedName());
        if (aMap == null) {
            aMap = new HashMap<>();
        }
        String aKey = aPrefix.getName();
        ItemStack x = aMap.get(aKey);
        if (x == null) {
            aMap.put(aKey, aStack);
            Logger.MATERIALS(
                "Registering a material component. Item: [" + componentMaterial.getUnlocalizedName()
                    + "] Map: ["
                    + aKey
                    + "]");
            Material.mComponentMap.put(componentMaterial.getUnlocalizedName(), aMap);
            return true;
        } else {
            // Bad
            Logger.MATERIALS("Tried to double register a material component. ");
            return false;
        }
    }

    public Materials tryFindGregtechMaterialEquivalent() {
        return tryFindGregtechMaterialEquivalent(this);
    }

    @Override
    public @Nullable Materials getGTMaterial() {
        return tryFindGregtechMaterialEquivalent();
    }

    @Override
    public boolean generatesPrefix(OrePrefixes prefix) {
        // This is really unreliable but it's also gt++ so there isn't a better solution
        return getComponentByPrefix(prefix, 1) != null;
    }

    public static Materials tryFindGregtechMaterialEquivalent(Material aMaterial) {
        String aMaterialName = aMaterial.getDefaultLocalName();
        Materials aGregtechMaterial = Materials.get(aMaterialName);
        if (MaterialUtils.isNullGregtechMaterial(aGregtechMaterial)) {
            aMaterialName = aMaterialName.replace(" ", "_");
            aGregtechMaterial = Materials.get(aMaterialName);
            if (MaterialUtils.isNullGregtechMaterial(aGregtechMaterial)) {
                aMaterialName = aMaterialName.replace(" ", "");
                aGregtechMaterial = Materials.get(aMaterialName);
                if (MaterialUtils.isNullGregtechMaterial(aGregtechMaterial)) {
                    return null;
                } else {
                    return aGregtechMaterial;
                }
            } else {
                return aGregtechMaterial;
            }
        } else {
            return aGregtechMaterial;
        }
    }

    public void setWerkstoffID(short werkstoffID) {
        this.werkstoffID = werkstoffID;
    }

    @Override
    public boolean contains(SubTag aTag) {
        return false;
    }

    @Override
    public ISubTagContainer add(SubTag... aTags) {
        throw new UnsupportedOperationException("GT++ does not implement subtags");
    }

    @Override
    public boolean remove(SubTag aTag) {
        return false;
    }

    @Override
    public void addTooltips(List<String> list) {
        if (this.vChemicalFormula.contains("?")) {
            list.add(StringUtils.sanitizeStringKeepBracketsQuestion(this.vChemicalFormula));
        } else {
            list.add(StringUtils.sanitizeStringKeepBrackets(this.vChemicalFormula));
        }

        if (this.isRadioactive) {
            list.add(StatCollector.translateToLocalFormatted("GTPP.core.GT_Tooltip_Radioactive", this.vRadiationLevel));
        }
    }
}
