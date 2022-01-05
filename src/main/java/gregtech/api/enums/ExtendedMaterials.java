package gregtech.api.enums;

import static gregtech.api.enums.GT_Values.MOD_ID_DC;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import cpw.mods.fml.common.Loader;
import gregtech.GT_Mod;
import gregtech.api.GregTech_API;
import gregtech.api.interfaces.IMaterialHandler;
import gregtech.api.objects.MaterialStack;
import gregtech.loaders.materialprocessing.ProcessingConfig;
import gregtech.loaders.materialprocessing.ProcessingModSupport;

public class ExtendedMaterials extends Materials {

	public int mMetaItemID;
    private static Materials[] MATERIALS_ARRAY_EX = new Materials[]{};
    private static final Map<String, Materials> MATERIALS_MAP_EX = new LinkedHashMap<>();
	
    public ExtendedMaterials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality, boolean aUnificatable, String aName, String aDefaultLocalName) {
        this(aMetaItemSubID, aIconSet, aToolSpeed, aDurability, aToolQuality, aUnificatable, aName, aDefaultLocalName, "ore", false, "null");
    }

    public ExtendedMaterials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality, boolean aUnificatable, String aName, String aDefaultLocalName, String aConfigSection, boolean aCustomOre, String aCustomID) {
    	super(-1, aIconSet, aToolSpeed, aDurability, aToolQuality, aUnificatable, aName, aDefaultLocalName, aConfigSection, aCustomOre, aCustomID);
    	mMetaItemID = aMetaItemSubID;
    	MATERIALS_MAP_EX.put(mName, this);
    }

    public ExtendedMaterials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality, int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType, int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor) {
        this(aMetaItemSubID, aIconSet, aToolSpeed, aDurability, aToolQuality, aTypes, aR, aG, aB, aA, aName, aDefaultLocalName, aFuelType, aFuelPower, aMeltingPoint, aBlastFurnaceTemp, aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider, aColor, "ore", false, "null");
    }

    public ExtendedMaterials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality, int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType, int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, Element aElement, List<TC_Aspects.TC_AspectStack> aAspects) {
        this(aMetaItemSubID, aIconSet, aToolSpeed, aDurability, aToolQuality, aTypes, aR, aG, aB, aA, aName, aDefaultLocalName, aFuelType, aFuelPower, aMeltingPoint, aBlastFurnaceTemp, aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider, aColor);
    }

    public ExtendedMaterials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality, int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType, int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, int aExtraData, List<MaterialStack> aMaterialList) {
        this(aMetaItemSubID, aIconSet, aToolSpeed, aDurability, aToolQuality, aTypes, aR, aG, aB, aA, aName, aDefaultLocalName, aFuelType, aFuelPower, aMeltingPoint, aBlastFurnaceTemp, aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider, aColor, aExtraData, aMaterialList, null);
    }

    public ExtendedMaterials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality, int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType, int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, int aExtraData, List<MaterialStack> aMaterialList, List<TC_Aspects.TC_AspectStack> aAspects) {
        this(aMetaItemSubID, aIconSet, aToolSpeed, aDurability, aToolQuality, aTypes, aR, aG, aB, aA, aName, aDefaultLocalName, aFuelType, aFuelPower, aMeltingPoint, aBlastFurnaceTemp, aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider, aColor);
    }

    public ExtendedMaterials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality, int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType, int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, List<TC_Aspects.TC_AspectStack> aAspects) {
        this(aMetaItemSubID, aIconSet, aToolSpeed, aDurability, aToolQuality, aTypes, aR, aG, aB, aA, aName, aDefaultLocalName, aFuelType, aFuelPower, aMeltingPoint, aBlastFurnaceTemp, aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider, aColor);
    }

    public ExtendedMaterials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality, int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType, int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, String aConfigSection) {
        this(aMetaItemSubID, aIconSet, aToolSpeed, aDurability, aToolQuality, aTypes, aR, aG, aB, aA, aName, aDefaultLocalName, aFuelType, aFuelPower, aMeltingPoint, aBlastFurnaceTemp, aBlastFurnaceRequired, aTransparent, aOreValue, aDensityMultiplier, aDensityDivider, aColor, aConfigSection, false, "null");
    }

    /**
     * @param aMetaItemSubID        the Sub-ID used in my own MetaItems. Range 0-1000. -1 for no Material
     * @param aTypes                which kind of Items should be generated. Bitmask as follows:
     *                              1 = Dusts of all kinds.
     *                              2 = Dusts, Ingots, Plates, Rods/Sticks, Machine Components and other Metal specific things.
     *                              4 = Dusts, Gems, Plates, Lenses (if transparent).
     *                              8 = Dusts, Impure Dusts, crushed Ores, purified Ores, centrifuged Ores etc.
     *                              16 = Cells
     *                              32 = Plasma Cells
     *                              64 = Tool Heads
     *                              128 = Gears
     *                              256 = Designates something as empty (only used for the Empty material)
     * @param aR,                   aG, aB Color of the Material 0-255 each.
     * @param aA                    transparency of the Material Texture. 0 = fully visible, 255 = Invisible.
     * @param aName                 The Name used as Default for localization.
     * @param aFuelType             Type of Generator to get Energy from this Material.
     * @param aFuelPower            EU generated. Will be multiplied by 1000, also additionally multiplied by 2 for Gems.
     * @param aMeltingPoint         Used to determine the smelting Costs in Furnii. >>>>**ADD 20000 to remove EBF recipes to add them MANUALLY ! :D**<<<<
     * @param aBlastFurnaceTemp     Used to determine the needed Heat capactiy Costs in Blast Furnii.
     * @param aBlastFurnaceRequired If this requires a Blast Furnace.
     * @param aColor                Vanilla MC Wool Color which comes the closest to this.
     */
    public ExtendedMaterials(int aMetaItemSubID, TextureSet aIconSet, float aToolSpeed, int aDurability, int aToolQuality, int aTypes, int aR, int aG, int aB, int aA, String aName, String aDefaultLocalName, int aFuelType, int aFuelPower, int aMeltingPoint, int aBlastFurnaceTemp, boolean aBlastFurnaceRequired, boolean aTransparent, int aOreValue, int aDensityMultiplier, int aDensityDivider, Dyes aColor, String aConfigSection, boolean aCustomOre, String aCustomID) {
        this(aMetaItemSubID, aIconSet, aToolSpeed, aDurability, aToolQuality, true, aName, aDefaultLocalName, aConfigSection, aCustomOre, aCustomID);
    }

    public ExtendedMaterials(Materials aMaterialInto, boolean aReRegisterIntoThis) {
    	super(aMaterialInto, aReRegisterIntoThis);
    }
    
    public static void init() {
        new ProcessingConfig();
        if (!GT_Mod.gregtechproxy.mEnableAllMaterials)
            new ProcessingModSupport();
        mMaterialHandlers.forEach(IMaterialHandler::onMaterialsInit);//This is where addon mods can add/manipulate materials
        initMaterialProperties(); //No more material addition or manipulation should be done past this point!
        MATERIALS_ARRAY_EX = MATERIALS_MAP_EX.values().toArray(new Materials[0]); //Generate standard object array. This is a lot faster to loop over.
        VALUES = Arrays.asList(MATERIALS_ARRAY_EX);
        if (!Loader.isModLoaded(MOD_ID_DC) && !GT_Mod.gregtechproxy.mEnableAllComponents)
            OrePrefixes.initMaterialComponents();
        else {
            OrePrefixes.ingotHot.mDisabledItems.addAll(
                    Arrays.stream(Materials.values()).parallel()
                            .filter(OrePrefixes.ingotHot::doGenerateItem)
                            .filter(m -> m.mBlastFurnaceTemp < 1750 && m.mAutoGenerateBlastFurnaceRecipes)
                            .collect(Collectors.toSet())
            );
            OrePrefixes.ingotHot.disableComponent(Materials.Reinforced);
            OrePrefixes.ingotHot.disableComponent(Materials.ConductiveIron);
            OrePrefixes.ingotHot.disableComponent(Materials.FierySteel);
            OrePrefixes.ingotHot.disableComponent(Materials.ElectricalSteel);
            OrePrefixes.ingotHot.disableComponent(Materials.EndSteel);
            OrePrefixes.ingotHot.disableComponent(Materials.Soularium);
            OrePrefixes.ingotHot.disableComponent(Materials.EnergeticSilver);
            OrePrefixes.ingotHot.disableComponent(Materials.Cheese);
            OrePrefixes.ingotHot.disableComponent(Materials.Calcium);
            OrePrefixes.ingotHot.disableComponent(Materials.Flerovium);
            OrePrefixes.ingotHot.disableComponent(Materials.Cobalt);
            OrePrefixes.ingotHot.disableComponent(Materials.RedstoneAlloy);
            OrePrefixes.ingotHot.disableComponent(Materials.Ardite);
            OrePrefixes.ingotHot.disableComponent(Materials.DarkSteel);
            OrePrefixes.ingotHot.disableComponent(Materials.EnergeticAlloy);
            OrePrefixes.ingotHot.disableComponent(Materials.PulsatingIron);
            OrePrefixes.ingotHot.disableComponent(Materials.CrudeSteel);
        }

        fillGeneratedMaterialsMap();

        // Fills empty spaces with materials, causes horrible load times.
        /*for (int i = 0; i < GregTech_API.sGeneratedMaterials.length; i++) {
            if (GregTech_API.sGeneratedMaterials[i] == null) {
                GregTech_API.sGeneratedMaterials[i] = new Materials(i, TextureSet.SET_NONE, 1.0F, 0, 2, 1|2|4|8|16|32|64|128, 92, 0, 168, 0, "TestMat" + i, 0, 0, -1, 0, false, false, 3, 1, 1, Dyes._NULL, "testmat");
            }
        }*/
    }
    
    private static void fillGeneratedMaterialsMap() {
        for (Materials aMaterial : MATERIALS_ARRAY_EX) {
            if (aMaterial.mMetaItemSubID >= 0) {
                if (aMaterial.mMetaItemSubID < 32000) {
                    if (aMaterial.mHasParentMod) {
                        if (GregTech_API.sGeneratedMaterials[aMaterial.mMetaItemSubID] == null) {
                            GregTech_API.sGeneratedMaterials[aMaterial.mMetaItemSubID] = aMaterial;
                        } else
                            throw new IllegalArgumentException("The Material Index " + aMaterial.mMetaItemSubID + " for " + aMaterial.mName + " is already used!");
                    }
                } else
                    throw new IllegalArgumentException("The Material Index " + aMaterial.mMetaItemSubID + " for " + aMaterial.mName + " is/over the maximum of 1000");
            }
        }
    }
    
    public static String getLocalizedNameForItem(String aFormat, int aMaterialID) {
        if (aMaterialID >= 0 && aMaterialID < 32000) {
            Materials aMaterial = GregTech_API.sGeneratedExtendedMaterials[aMaterialID];
            if (aMaterial != null)
                return aMaterial.getLocalizedNameForItem(aFormat);
        }
        return aFormat;
    }
	
    /**
     * This is for keeping compatibility with addons mods (Such as TinkersGregworks etc) that looped over the old materials enum
     */
    public static Materials[] values() {
        return MATERIALS_ARRAY_EX;
    }
    
}
