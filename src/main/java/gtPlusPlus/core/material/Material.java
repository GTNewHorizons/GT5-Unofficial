package gtPlusPlus.core.material;

import static gregtech.api.enums.GTValues.M;
import static gregtech.api.enums.Mods.TinkerConstruct;
import static gtPlusPlus.core.util.math.MathUtils.safeCast_LongToInt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.util.GTLanguageManager;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.core.item.base.BaseItemComponent.ComponentTypes;
import gtPlusPlus.core.item.base.cell.BaseItemCell;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.FluidUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;
import gtPlusPlus.xmod.tinkers.material.BaseTinkersMaterial;

public class Material {

    public static final Set<Material> mMaterialMap = new HashSet<>();
    public static HashMap<String, Material> mMaterialCache = new HashMap<>();

    public static final Map<String, Map<String, ItemStack>> mComponentMap = new HashMap<>();

    public static HashMap<String, String> sChemicalFormula = new HashMap<>();

    private String unlocalizedName;
    private String localizedName;
    private String translatedName;

    private MaterialState materialState;
    private TextureSet textureSet;

    private Fluid mFluid;
    private Fluid mPlasma;

    private boolean vGenerateCells;

    protected Object dataVar = MathUtils.generateSingularRandomHexValue();

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

    public static AutoMap<Materials> invalidMaterials = new AutoMap<>();

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

        if (mMaterialMap.add(this)) {}

        if (defaultState == MaterialState.ORE) {
            rgba = null;
        }

        mComponentMap.put(unlocalizedName, new HashMap<>());

        try {
            this.unlocalizedName = Utils.sanitizeString(materialName);
            this.localizedName = materialName;
            this.translatedName = GTLanguageManager
                .addStringLocalization("gtplusplus.material." + unlocalizedName, localizedName);
            mMaterialCache.put(getLocalizedName().toLowerCase(), this);
            Logger.INFO("Stored " + getLocalizedName() + " to cache with key: " + getLocalizedName().toLowerCase());

            this.materialState = defaultState;

            Logger.MATERIALS(this.getLocalizedName() + " is " + defaultState.name() + ".");

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
                if (vMaterialInput.size() > 0) {

                    try {
                        Short[] mMixedRGB = new Short[3];
                        AutoMap<Material> mMaterialSet = MaterialUtils.getCompoundMaterialsRecursively(this);
                        for (int mnh = 0; mnh < 3; mnh++) {
                            AutoMap<Short> aDataSet = new AutoMap<>();
                            Set<Material> set4 = new HashSet<>();
                            for (Material u : mMaterialSet) {
                                // if (u.getState() == MaterialState.ORE || u.getState() == MaterialState.SOLID)
                                set4.add(u);
                            }
                            for (Material e : set4) {
                                aDataSet.put(e.getRGB()[mnh]);
                            }

                            Short aAverage = MathUtils.getShortAverage(aDataSet);
                            if (aAverage > Short.MAX_VALUE || aAverage < Short.MIN_VALUE
                                || aAverage < 0
                                || aAverage > 255) {
                                if (aAverage > 255) {
                                    while (aAverage > 255) {
                                        aAverage = (short) (aAverage / 2);
                                    }
                                }
                                aAverage = (short) Math.max(Math.min(aAverage, 255), 0);
                            }
                            mMixedRGB[mnh] = aAverage;
                        }

                        if (mMixedRGB != null && mMixedRGB[0] != null && mMixedRGB[1] != null && mMixedRGB[2] != null) {
                            this.RGBA = new short[] { mMixedRGB[0], mMixedRGB[1], mMixedRGB[2], 0 };
                        } else {
                            this.RGBA = Materials.Steel.mRGBa;
                        }
                    } catch (Throwable t) {
                        t.printStackTrace();
                        this.RGBA = Materials.Steel.mRGBa;
                    }
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
                            String c = b;
                            while (MathUtils.howManyPlaces(hashSize + c.length()) < 9) {
                                c = c + c.hashCode();
                            }
                            b = c;
                        }
                    }

                    String valueR;
                    if (b != null) {
                        valueR = a + b;
                    } else {
                        valueR = a;
                    }
                    short fc[] = new short[3];
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
                            : 32000 * this.getComposites()
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
                Logger.MATERIALS(this.getLocalizedName() + " is radioactive. Level: " + radiationLevel + ".");
                this.isRadioactive = true;
                this.vRadiationLevel = (byte) radiationLevel;
            } else {
                if (vMaterialInput.size() > 0) {
                    AutoMap<Byte> aDataSet = new AutoMap<>();
                    for (MaterialStack m : this.vMaterialInput) {
                        aDataSet.put(m.getStackMaterial().vRadiationLevel);
                    }
                    byte aAverage = MathUtils.getByteAverage(aDataSet);
                    if (aAverage > 0) {
                        Logger.MATERIALS(
                            this.getLocalizedName() + " is radioactive due to trace elements. Level: "
                                + aAverage
                                + ".");
                        this.isRadioactive = true;
                        this.vRadiationLevel = aAverage;
                    } else {
                        Logger.MATERIALS(this.getLocalizedName() + " is not radioactive.");
                        this.isRadioactive = false;
                        this.vRadiationLevel = 0;
                    }
                } else {
                    Logger.MATERIALS(this.getLocalizedName() + " is not radioactive.");
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

            if (this.vSmallestRatio != null) {
                for (long l : this.vSmallestRatio) {
                    tempSmallestSize = (int) (tempSmallestSize + l);
                }
                this.smallestStackSizeWhenProcessing = tempSmallestSize; // Valid stacksizes
            } else {
                this.smallestStackSizeWhenProcessing = 1; // Valid stacksizes
            }

            // Makes a Fancy Chemical Tooltip

            if (chemicalSymbol == null) {
                chemicalSymbol = "";
            }

            this.vChemicalSymbol = chemicalSymbol;
            if (this.vMaterialInput != null) {
                this.vChemicalFormula = this.getToolTip(chemicalSymbol, OrePrefixes.dust.mMaterialAmount / M, true);
            } else if (!this.vChemicalSymbol.equals("")) {
                Logger.MATERIALS("materialInput is null, using a valid chemical symbol.");
                this.vChemicalFormula = this.vChemicalSymbol;
            } else {
                Logger.MATERIALS("MaterialInput == null && chemicalSymbol probably equals nothing");
                this.vChemicalSymbol = "??";
                this.vChemicalFormula = "??";
            }

            if (generateFluid) {
                final Materials aGregtechMaterial = tryFindGregtechMaterialEquivalent();
                FluidStack aTest = FluidUtils.getWildcardFluidStack(localizedName, 1);
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
            String ratio = "";
            if (this.vSmallestRatio != null) {
                for (long l : this.vSmallestRatio) {
                    if (ratio.equals("")) {
                        ratio = String.valueOf(l);
                    } else {
                        ratio = ratio + ":" + l;
                    }
                }
            }

            this.textureSet = setTextureSet(set, vTier);

            if (TinkerConstruct.isModLoaded() && this.materialState == MaterialState.SOLID) {
                if (this.getProtons() >= 98 || this.getComposites()
                    .size() > 1 || this.getMeltingPointC() >= 3600) {
                    this.vTiConHandler = new BaseTinkersMaterial(this);
                }
            }

            sChemicalFormula.put(materialName.toLowerCase(), this.vChemicalFormula);
            Logger.MATERIALS("Creating a Material instance for " + materialName);
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
            Logger.MATERIALS("Stack Trace for " + materialName);
            t.printStackTrace();
        }
    }

    private static void checkForCellAndGenerate(Material material) {
        if (!material.vGenerateCells) {
            return;
        }
        String aName = Utils.sanitizeString(material.unlocalizedName);
        String aName2 = Utils.sanitizeString(material.unlocalizedName.toLowerCase());
        String aName3 = (material.localizedName == null) ? aName : material.localizedName;
        ItemStack aTestCell1 = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell" + aName, 1);
        ItemStack aTestCell2 = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell" + aName2, 1);
        ItemStack aTestCell3 = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell" + aName3, 1);
        if (aTestCell1 == null && aTestCell2 == null && aTestCell3 == null) {
            Logger.INFO("Generating cell for " + material.localizedName);
            new BaseItemCell(material);
        } else {
            if (aTestCell1 != null) {
                Logger.INFO("Registering existing cell for " + material.localizedName + ", " + aName);
                material.registerComponentForMaterial(OrePrefixes.cell, aTestCell1);
            } else if (aTestCell2 != null) {
                Logger.INFO("Registering existing cell for " + material.localizedName + ", " + aName2);
                material.registerComponentForMaterial(OrePrefixes.cell, aTestCell2);
            } else if (aTestCell3 != null) {
                Logger.INFO("Registering existing cell for " + material.localizedName + ", " + aName3);
                material.registerComponentForMaterial(OrePrefixes.cell, aTestCell3);
            }
        }
    }

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
                "Set textureset for " + this.localizedName
                    + " to be "
                    + set.mSetName
                    + ". This textureSet was supplied.");
            return set;
        }

        int aGem = 0;
        int aShiny = 0;
        TextureSet aSet = null;

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

        if (aSet == null) {
            if (aGem >= this.getComposites()
                .size() / 2) {
                if (MathUtils.isNumberEven(aGem)) {
                    Logger.MATERIALS(
                        "Set textureset for " + this.localizedName
                            + " to be "
                            + TextureSet.SET_GEM_HORIZONTAL.mSetName
                            + ".");
                    return TextureSet.SET_GEM_HORIZONTAL;
                } else {
                    Logger.MATERIALS(
                        "Set textureset for " + this.localizedName
                            + " to be "
                            + TextureSet.SET_GEM_VERTICAL.mSetName
                            + ".");
                    return TextureSet.SET_GEM_VERTICAL;
                }
            }
        }

        if (aSet == null) {
            if (aShiny >= this.getComposites()
                .size() / 3) {
                Logger.MATERIALS(
                    "Set textureset for " + this.localizedName + " to be " + TextureSet.SET_SHINY.mSetName + ".");
                return TextureSet.SET_SHINY;
            }
        }

        // build hash table with count
        AutoMap<Material> sets = new AutoMap<>();
        if (this.vMaterialInput != null) {
            for (MaterialStack r : this.vMaterialInput) {
                if (r.getStackMaterial()
                    .getTextureSet().mSetName.toLowerCase()
                        .contains("fluid")) {
                    sets.put(MaterialsElements.getInstance().GOLD);
                } else {
                    sets.put(r.getStackMaterial());
                }
            }
            TextureSet mostUsedTypeTextureSet = MaterialUtils.getMostCommonTextureSet(new ArrayList<>(sets.values()));
            if (mostUsedTypeTextureSet != null && mostUsedTypeTextureSet instanceof TextureSet) {
                Logger.MATERIALS(
                    "Set textureset for " + this.localizedName + " to be " + mostUsedTypeTextureSet.mSetName + ".");
                return mostUsedTypeTextureSet;
            }
        }
        Logger.MATERIALS(
            "Set textureset for " + this.localizedName + " to be " + Materials.Iron.mIconSet.mSetName + ". [Fallback]");
        return Materials.Gold.mIconSet;
    }

    public final String getLocalizedName() {
        if (this.localizedName != null) {
            return this.localizedName;
        }
        return "ERROR BAD LOCALIZED NAME";
    }

    public final String getUnlocalizedName() {
        if (this.unlocalizedName != null) {
            return this.unlocalizedName;
        }
        return "ERROR.BAD.UNLOCALIZED.NAME";
    }

    public final String getTranslatedName() {
        if (this.translatedName != null) {
            return this.translatedName;
        }
        return "ERROR.BAD.TRANSLATED.NAME";
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
            return (int) this.dataVar;
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
        String aKey = aPrefix.name();
        Map<String, ItemStack> g = mComponentMap.get(this.unlocalizedName);
        if (g == null) {
            Map<String, ItemStack> aMap = new HashMap<>();
            mComponentMap.put(unlocalizedName, aMap);
            g = aMap;
        }
        ItemStack i = g.get(aKey);
        if (i != null) {
            return ItemUtils.getSimpleStack(i, stacksize);
        } else {
            // Try get a GT Material
            Materials Erf = MaterialUtils.getMaterial(this.unlocalizedName);
            if (Erf != null && !MaterialUtils.isNullGregtechMaterial(Erf)) {
                ItemStack Erg = ItemUtils.getOrePrefixStack(aPrefix, Erf, stacksize);
                if (Erg != null && ItemUtils.checkForInvalidItems(Erg)) {
                    Logger.MATERIALS("Found \"" + aKey + this.unlocalizedName + "\" using backup GT Materials option.");
                    g.put(aKey, Erg);
                    mComponentMap.put(unlocalizedName, g);
                    return Erg;
                } else {
                    // Try get a molten cell
                    if (aPrefix == OrePrefixes.cell) {
                        Erg = ItemUtils.getOrePrefixStack(OrePrefixes.cellMolten, Erf, stacksize);
                        if (Erg != null && ItemUtils.checkForInvalidItems(Erg)) {
                            Logger.MATERIALS(
                                "Found \"" + OrePrefixes.cellMolten.name()
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
            // Logger.MATERIALS("Unabled to find \"" + aKey + this.unlocalizedName + "\"");
            return ItemUtils.getErrorStack(stacksize, (aKey + this.unlocalizedName + " x" + stacksize));
        }
    }

    public final Block getBlock() {
        Block b = Block.getBlockFromItem(getBlock(1).getItem());
        if (b == null) {
            Logger.INFO(
                "[ERROR] Tried to get invalid block for " + this.getLocalizedName()
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

    /**
     * Ore Components
     *
     * @return
     */
    public final ItemStack getOre(final int stacksize) {
        return ItemUtils.getItemStackOfAmountFromOreDictNoBroken(
            "ore" + Utils.sanitizeString(this.getUnlocalizedName()),
            stacksize);
    }

    public final Block getOreBlock(final int stacksize) {
        // Logger.DEBUG_MATERIALS("Trying to get ore block for "+this.getLocalizedName()+". Looking for
        // '"+"ore"+Utils.sanitizeString(this.getUnlocalizedName())+"'.");
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
                        "ore" + Utils.sanitizeString(this.unlocalizedName),
                        stacksize)
                    .getItem());
            if (x != null) {
                return x;
            }
        } catch (Throwable t) {
            // t.printStackTrace();
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
        return getComponentByPrefix(OrePrefixes.rawOre, stacksize);
    }

    public final boolean hasSolidForm() {
        if (ItemUtils
            .checkForInvalidItems(new ItemStack[] { getDust(1), getBlock(1), getTinyDust(1), getSmallDust(1) })) {
            return true;
        }
        return false;
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
                    Logger.MATERIALS("Failed gathering material stack for " + this.localizedName + ".");
                    Logger.MATERIALS("What Failed: Length:" + this.vMaterialInput.size() + " current:" + i);
                }
                try {
                    if (testNull != null) {
                        // Utils.LOG_MATERIALS("not null");
                        temp[i] = this.vMaterialInput.get(i)
                            .getValidStack();
                    }
                } catch (final Throwable r) {
                    Logger.MATERIALS("Failed setting slot " + i + ", using " + this.localizedName);
                }
            }
            return temp;
        }
        return new ItemStack[] {};
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
        return new int[] {};
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
                Logger.MATERIALS("(inputs != null): " + (tempInput != null));
                // Utils.LOG_MATERIALS("length: "+inputs.length);
                final long[] tempRatio = new long[tempInput.size()];
                for (int x = 0; x < tempInput.size(); x++) {
                    if (tempInput.get(x) != null) {
                        tempRatio[x] = tempInput.get(x)
                            .getPartsPerOneHundred();
                    }
                }

                final long[] smallestRatio = MathUtils.simplifyNumbersToSmallestForm(tempRatio);

                if (smallestRatio.length > 0) {
                    String tempRatioStringThing1 = "";
                    for (long value : tempRatio) {
                        tempRatioStringThing1 = tempRatioStringThing1 + value + " : ";
                    }
                    Logger.MATERIALS("Default Ratio: " + tempRatioStringThing1);

                    String tempRatioStringThing = "";
                    int tempSmallestCraftingUseSize = 0;
                    for (long l : smallestRatio) {
                        tempRatioStringThing = tempRatioStringThing + l + " : ";
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
        Logger.MATERIALS("===============| Calculating Atomic Formula for " + this.localizedName + " |===============");
        if (!chemSymbol.equals("")) {
            return chemSymbol;
        }
        final ArrayList<MaterialStack> tempInput = this.vMaterialInput;
        if (tempInput != null) {
            if (!tempInput.isEmpty()) {
                String dummyFormula = "";
                final long[] dummyFormulaArray = this.getSmallestRatio(tempInput);
                if (dummyFormulaArray != null) {
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

                                            if (aChemFormula.length() > 3
                                                || StringUtils.uppercaseCount(aChemFormula) > 1) {
                                                dummyFormula = dummyFormula + "("
                                                    + aChemFormula
                                                    + ")"
                                                    + dummyFormulaArray[e];
                                            } else {
                                                dummyFormula = dummyFormula + aChemFormula + dummyFormulaArray[e];
                                            }
                                        } else if (dummyFormulaArray[e] == 1) {
                                            if (aChemFormula.length() > 3
                                                || StringUtils.uppercaseCount(aChemFormula) > 1) {
                                                dummyFormula = dummyFormula + "(" + aChemFormula + ")";
                                            } else {
                                                dummyFormula = dummyFormula + aChemFormula;
                                            }
                                        } else {
                                            dummyFormula = dummyFormula + "??";
                                        }
                                    } else {
                                        dummyFormula = dummyFormula + "??";
                                    }
                                } else {
                                    dummyFormula = dummyFormula + "??";
                                }
                            }
                        }
                        return StringUtils.subscript(dummyFormula);
                        // return dummyFormula;
                    }
                    Logger.MATERIALS("dummyFormulaArray <= 0");
                }
                Logger.MATERIALS("dummyFormulaArray == null");
            }
            Logger.MATERIALS("tempInput.length <= 0");
        }
        Logger.MATERIALS("tempInput == null");
        return "??";
    }

    public final boolean queueFluidGeneration() {
        return isFluidQueued = true;
    }

    public static final void generateQueuedFluids() {
        for (Material m : mMaterialMap) {
            if (m.isFluidQueued) {}
        }
    }

    // If we need a fluid, let's just queue it for later.
    public boolean isFluidQueued = false;

    public final Fluid generateFluid() {
        if (this.materialState == MaterialState.ORE) {
            return null;
        }

        Fluid aGTBaseFluid = null;

        // Clean up Internal Fluid Generation
        final Materials n1 = MaterialUtils
            .getMaterial(this.getLocalizedName(), Utils.sanitizeString(this.getLocalizedName()));
        final Materials n2 = MaterialUtils
            .getMaterial(this.getUnlocalizedName(), Utils.sanitizeString(this.getUnlocalizedName()));

        FluidStack f1 = FluidUtils.getWildcardFluidStack(n1, 1);
        FluidStack f2 = FluidUtils.getWildcardFluidStack(n2, 1);
        FluidStack f3 = FluidUtils
            .getWildcardFluidStack(Utils.sanitizeString(this.getUnlocalizedName(), new char[] { '-', '_' }), 1);
        FluidStack f4 = FluidUtils
            .getWildcardFluidStack(Utils.sanitizeString(this.getLocalizedName(), new char[] { '-', '_' }), 1);

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
        ItemStack aFullCell2 = ItemUtils.getItemStackOfAmountFromOreDictNoBroken("cell" + this.getLocalizedName(), 1);
        ItemStack aFullCell3 = ItemUtils.getItemStackOfAmountFromOreDictNoBroken(
            "cell" + Utils.sanitizeString(this.getUnlocalizedName(), new char[] { '-', '_' }),
            1);
        ItemStack aFullCell4 = ItemUtils.getItemStackOfAmountFromOreDictNoBroken(
            "cell" + Utils.sanitizeString(this.getLocalizedName(), new char[] { '-', '_' }),
            1);

        Logger.MATERIALS("Generating our own fluid.");
        // Generate a Cell if we need to, but first validate all four searches are invalid

        if (!ItemUtils.checkForInvalidItems(new ItemStack[] { aFullCell, aFullCell2, aFullCell3, aFullCell4 })) {
            if (this.vGenerateCells) {
                Item g = new BaseItemCell(this);
                aFullCell = ItemUtils.getSimpleStack(g);
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
            return FluidUtils.addGTFluid(
                this.getUnlocalizedName(),
                "Molten " + this.getLocalizedName(),
                this.RGBA,
                4,
                this.getMeltingPointK(),
                aFullCell,
                ItemUtils.getEmptyCell(),
                1000,
                this.vGenerateCells);
        } else if (this.materialState == MaterialState.LIQUID || this.materialState == MaterialState.PURE_LIQUID) {
            return FluidUtils.addGTFluid(
                this.getUnlocalizedName(),
                this.getLocalizedName(),
                this.RGBA,
                0,
                this.getMeltingPointK(),
                aFullCell,
                ItemUtils.getEmptyCell(),
                1000,
                this.vGenerateCells);
        } else if (this.materialState == MaterialState.GAS || this.materialState == MaterialState.PURE_GAS) {
            return FluidUtils
                .generateGas(unlocalizedName, this.getLocalizedName(), getMeltingPointK(), getRGBA(), vGenerateCells);
            /*
             * return FluidUtils.addGTFluid( this.getUnlocalizedName(), this.getLocalizedName()+" Gas", this.RGBA, 2,
             * this.getMeltingPointK(), aFullCell, ItemUtils.getEmptyCell(), 1000, this.vGenerateCells);
             */
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
            for (Materials m : invalidMaterials.values()) {
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
        final FluidStack moltenFluid = new FluidStack(this.mFluid, fluidAmount);
        return moltenFluid;
    }

    public final boolean setFluid(Fluid aFluid) {
        if (this.mFluid == null) {
            this.mFluid = aFluid;
            return true;
        }
        return false;
    }

    public final int calculateMeltingPoint() {
        try {
            AutoMap<Integer> aDataSet = new AutoMap<>();
            for (MaterialStack m : this.vMaterialInput) {
                aDataSet.put(
                    m.getStackMaterial()
                        .getMeltingPointC());
            }
            long aAverage = MathUtils.getIntAverage(aDataSet);
            return MathUtils.safeInt(aAverage);
        } catch (Throwable r) {
            r.printStackTrace();
            return 500;
        }
    }

    public final int calculateBoilingPoint() {
        try {

            AutoMap<Integer> aDataSet = new AutoMap<>();
            for (MaterialStack m : this.vMaterialInput) {
                aDataSet.put(
                    m.getStackMaterial()
                        .getBoilingPointC());
            }
            long aAverage = MathUtils.getIntAverage(aDataSet);
            return MathUtils.safeInt(aAverage);
        } catch (Throwable r) {
            r.printStackTrace();
            return 2500;
        }
    }

    public final long calculateProtons() {
        try {

            AutoMap<Long> aDataSet = new AutoMap<>();
            for (MaterialStack m : this.vMaterialInput) {
                aDataSet.put(
                    m.getStackMaterial()
                        .getProtons());
            }
            long aAverage = MathUtils.getLongAverage(aDataSet);
            return MathUtils.safeInt(aAverage);
        } catch (Throwable r) {
            r.printStackTrace();
            return 50;
        }
    }

    public final long calculateNeutrons() {
        try {

            AutoMap<Long> aDataSet = new AutoMap<>();
            for (MaterialStack m : this.vMaterialInput) {
                aDataSet.put(
                    m.getStackMaterial()
                        .getNeutrons());
            }
            long aAverage = MathUtils.getLongAverage(aDataSet);
            return MathUtils.safeInt(aAverage);
        } catch (Throwable r) {
            r.printStackTrace();
            return 75;
        }
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!Material.class.isInstance(obj)) {
            return false;
        }
        Material aObj = (Material) obj;
        if (aObj.unlocalizedName.equals(this.unlocalizedName)) {
            if (aObj.localizedName.equals(this.localizedName)) {
                return true;
            }
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
        String aKey = aPrefix.name();
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

    public static Materials tryFindGregtechMaterialEquivalent(Material aMaterial) {
        String aMaterialName = aMaterial.getLocalizedName();
        Materials aGregtechMaterial = Materials.get(aMaterialName);
        if (aGregtechMaterial == null || MaterialUtils.isNullGregtechMaterial(aGregtechMaterial)) {
            aMaterialName = aMaterialName.replace(" ", "_");
            aGregtechMaterial = Materials.get(aMaterialName);
            if (aGregtechMaterial == null || MaterialUtils.isNullGregtechMaterial(aGregtechMaterial)) {
                aMaterialName = aMaterialName.replace(" ", "");
                aGregtechMaterial = Materials.get(aMaterialName);
                if (aGregtechMaterial == null || MaterialUtils.isNullGregtechMaterial(aGregtechMaterial)) {
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
}
