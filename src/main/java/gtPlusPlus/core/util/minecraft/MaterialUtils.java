package gtPlusPlus.core.util.minecraft;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;

import gregtech.api.enums.Element;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.TextureSet;
import gregtech.api.util.GT_Utility;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.AutoMap;
import gtPlusPlus.api.objects.data.TypeCounter;
import gtPlusPlus.core.client.CustomTextureSet.TextureSets;
import gtPlusPlus.core.item.base.BaseItemComponent;
import gtPlusPlus.core.item.base.BaseItemComponent.ComponentTypes;
import gtPlusPlus.core.item.base.foil.BaseItemFoil;
import gtPlusPlus.core.item.base.wire.BaseItemFineWire;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.material.MaterialStack;
import gtPlusPlus.core.material.state.MaterialState;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.math.MathUtils;

public class MaterialUtils {

    public static List<?> oreDictValuesForEntry(final String oredictName) {
        List<?> oredictItemNames;
        if (OreDictionary.doesOreNameExist(oredictName)) {
            oredictItemNames = OreDictionary.getOres(oredictName);
            return oredictItemNames;
        }
        return null;
    }

    private static final Map<String, Material> mGeneratedMaterialMap = new HashMap<>();

    public static Material generateMaterialFromGtENUM(final Materials material) {
        return generateMaterialFromGtENUM(material, null, null);
    }

    public static Material generateMaterialFromGtENUM(final Materials material, TextureSet aCustomTextures) {
        return generateMaterialFromGtENUM(material, null, aCustomTextures);
    }

    public static Material generateMaterialFromGtENUM(final Materials material, short[] customRGB) {
        return generateMaterialFromGtENUM(material, customRGB, null);
    }

    public static Material generateMaterialFromGtENUM(final Materials material, short[] customRGB,
            TextureSet aCustomTextures) {
        String aMaterialKey = getMaterialName(material).toLowerCase();
        if (mGeneratedMaterialMap.containsKey(aMaterialKey)) {
            return mGeneratedMaterialMap.get(aMaterialKey);
        }

        try {
            String name = material.mName;
            final short[] rgba = (customRGB == null ? material.mRGBa : customRGB);
            final int melting = material.mMeltingPoint;
            final int boiling = material.mBlastFurnaceTemp;
            final long protons = material.getProtons();
            final long neutrons = material.getNeutrons();
            final boolean blastFurnace = material.mBlastFurnaceRequired;
            int radioactivity = 0;
            if (material.isRadioactive()) {
                ItemStack aDustStack = ItemUtils.getOrePrefixStack(OrePrefixes.dust, material, 1);
                radioactivity = aDustStack != null ? GT_Utility.getRadioactivityLevel(aDustStack) : 0;
                if (radioactivity == 0) {
                    long aProtons = material.getProtons();
                    radioactivity = (int) Math.min(Math.max((aProtons / 30), 1), 9);
                }
            }
            Logger.MATERIALS("[Debug] Calculated Radiation level to be " + radioactivity + ".");
            TextureSet iconSet = null;
            if (aCustomTextures == null) {
                if (material.isRadioactive()) {
                    iconSet = TextureSets.NUCLEAR.get();
                } else {
                    iconSet = material.mIconSet;
                }
            } else {
                iconSet = aCustomTextures;
            }
            if (iconSet == null || iconSet.mSetName.toLowerCase().contains("fluid")) {
                iconSet = TextureSet.SET_METALLIC;
            }
            Logger.MATERIALS("[Debug] Calculated Texture Set to be " + iconSet.mSetName + ".");

            final int durability = material.mDurability;
            boolean mGenerateCell = false;
            boolean mGenerateFluid = true;
            MaterialState materialState;
            String chemicalFormula = StringUtils.subscript(Utils.sanitizeString(material.mChemicalFormula));
            final Element element = material.mElement;

            // Weird Blacklist of Bad Chemical Strings
            if (material.mElement == Element.Pb || material.mElement == Element.Na || material.mElement == Element.Ar) {
                chemicalFormula = StringUtils.subscript(Utils.sanitizeString(material.mElement.name()));
            }

            // Determine default state
            Logger.MATERIALS("[Debug] Setting State of GT generated material. " + material.mDefaultLocalName);
            if (material.getMolten(1) != null || material.getSolid(1) != null) {
                materialState = MaterialState.SOLID;
                Logger.MATERIALS("[Debug] Molten or Solid was not null.");
                if (material.getMolten(1) == null && material.getSolid(1) != null) {
                    Logger.MATERIALS("[Debug] Molten is Null, Solid is not. Enabling cell generation.");
                    mGenerateCell = true;
                } else if (material.getMolten(1) != null && material.getSolid(1) == null) {
                    Logger.MATERIALS("[Debug] Molten is not Null, Solid is null. Not enabling cell generation.");
                    // mGenerateCell = true;
                }
                Logger.MATERIALS("[Debug] State set as solid.");
            } else if (material.getFluid(1) != null) {
                Logger.MATERIALS("[Debug] State set as liquid.");
                materialState = MaterialState.LIQUID;
            } else if (material.getGas(1) != null) {
                Logger.MATERIALS("[Debug] State set as gas.");
                materialState = MaterialState.GAS;
            } /*
               * else if (material.getPlasma(1) != null){ Logger.MATERIALS("[Debug] State set as plasma.");
               * materialState = MaterialState.PLASMA; }
               */ else {
                Logger.MATERIALS(
                        "[Debug] State set as solid. This material has no alternative states, so for safety we wont generate anything.");
                materialState = MaterialState.SOLID;
                mGenerateFluid = false;
            }

            if (name.toLowerCase().contains("infused")) {
                final String tempname = name.substring(7);
                name = "Infused " + tempname;
            }
            if (hasValidRGBA(rgba) || (element == Element.H)
                    || ((material == Materials.InfusedAir) || (material == Materials.InfusedFire)
                            || (material == Materials.InfusedEarth)
                            || (material == Materials.InfusedWater))) {
                Material M = new Material(
                        name,
                        materialState,
                        iconSet,
                        durability,
                        rgba,
                        melting,
                        boiling,
                        protons,
                        neutrons,
                        blastFurnace,
                        chemicalFormula,
                        radioactivity,
                        mGenerateCell,
                        mGenerateFluid);
                mGeneratedMaterialMap.put(aMaterialKey, M);
                return M;
            } else {
                Logger.DEBUG_MATERIALS(
                        "Failed to generate GT++ material instance for " + material.mName
                                + " | Valid RGB? "
                                + (hasValidRGBA(rgba)));
            }
        } catch (Throwable t) {
            Logger.DEBUG_MATERIALS("Failed to generate GT++ material instance for " + material.mName);
            t.printStackTrace();
        }
        return null;
    }

    public static Material generateQuickMaterial(final String materialName, final MaterialState defaultState,
            final short[] colour, final int sRadioactivity) {
        String aMaterialKey = materialName.toLowerCase();
        if (mGeneratedMaterialMap.containsKey(aMaterialKey)) {
            return mGeneratedMaterialMap.get(aMaterialKey);
        }

        final Material temp = new Material(
                materialName,
                defaultState,
                colour,
                1000, // melting
                3000, // boiling
                50, // Protons
                50, // Neutrons
                false,
                "",
                sRadioactivity);
        mGeneratedMaterialMap.put(aMaterialKey, temp);
        return temp;
    }

    public static boolean hasValidRGBA(final short[] rgba) {
        if (rgba == null || rgba.length < 3 || rgba.length > 4) {
            return false;
        }
        return true;
    }

    public static int getTierOfMaterial(final double aMeltingPoint) {
        return aMeltingPoint < 1000 ? 0 : (MathUtils.roundToClosestInt(aMeltingPoint / 1000f));
    }

    public static int getVoltageForTier(int aTier) {
        // aTier += 1; - Probably some logic to this, idk.

        return switch (aTier) {
            case 0 -> 16;
            case 1 -> 30;
            case 2 -> 120;
            case 3 -> 480;
            case 4 -> 1920;
            case 5 -> 7680;
            case 6 -> 30720;
            case 7 -> 122880;
            case 8 -> 491520;
            case 9 -> 1966080;
            case 10 -> 7864320;
            case 11 -> 31457280;
            case 12 -> 125829120;
            case 13 -> 503316480;
            case 14 -> 2013265920;
            default -> Integer.MAX_VALUE;
        };

    }

    private static Materials getMaterialByName(String materialName) {
        for (Materials m : Materials.values()) {
            if (MaterialUtils.getMaterialName(m).toLowerCase().equals(materialName.toLowerCase())) {
                return m;
            }
        }
        return null;
    }

    public static String getMaterialName(Materials mat) {
        String mName = mat.mDefaultLocalName;
        if (mName == null || mName.equals("")) {
            mName = mat.mName;
        }
        return mName;
    }

    public static TextureSet getMostCommonTextureSet(List<Material> list) {
        TypeCounter<TextureSet> aCounter = new TypeCounter<>(TextureSet.class);
        for (Material m : list) {
            TextureSet t = m.getTextureSet();
            if (t == null) {
                t = Materials.Gold.mIconSet;
            }
            if (t != null) {
                aCounter.add(t, t.mSetName);
            }
        }
        return aCounter.getResults();
    }

    public static Materials getMaterial(String aMaterialName, String aFallbackMaterialName) {
        Materials g = getMaterial(aMaterialName);
        if (g == null) {
            g = getMaterial(aFallbackMaterialName);
        }
        if (g == null) {
            Logger.INFO(
                    "Failed finding material '" + aMaterialName
                            + "' & fallback '"
                            + aFallbackMaterialName
                            + "', returning _NULL.");
            CORE.crash();
            // g = Materials._NULL;
        }
        return g;
    }

    public static Materials getMaterial(String aMaterialName) {
        Materials m = Materials.get(aMaterialName);
        if (m == Materials._NULL) {
            m = getMaterialByName(aMaterialName);
        }
        if (m == null) {
            Logger.INFO("Failed finding material '" + aMaterialName + "', returning _NULL.");
            m = Materials._NULL;
        }
        return m;
    }

    public static AutoMap<Material> getCompoundMaterialsRecursively(Material aMat) {
        return getCompoundMaterialsRecursively_Speiger(aMat);
    }

    public static AutoMap<Material> getCompoundMaterialsRecursively_Speiger(Material toSearch) {
        AutoMap<Material> resultList = new AutoMap<>();
        if (toSearch.getComposites().isEmpty()) {
            resultList.put(toSearch);
            return resultList;
        }
        final int HARD_LIMIT = 1000;

        // Could be a Deque but i dont use the interface
        // enough to use it as default.
        LinkedList<Material> toCheck = new LinkedList<>();

        toCheck.add(toSearch);
        int processed = 0;
        while (toCheck.size() > 0 && processed < HARD_LIMIT) {
            Material current = toCheck.remove();
            if (current.getComposites().isEmpty()) {
                resultList.put(current);
            } else {
                for (MaterialStack entry : current.getComposites()) {
                    toCheck.add(entry.getStackMaterial());
                }
            }
            processed++;
        }
        return resultList;
    }

    public static void generateComponentAndAssignToAMaterial(ComponentTypes aType, Material aMaterial) {
        generateComponentAndAssignToAMaterial(aType, aMaterial, true);
    }

    public static void generateComponentAndAssignToAMaterial(ComponentTypes aType, Material aMaterial,
            boolean generateRecipes) {
        Item aGC;
        if (aType == ComponentTypes.FINEWIRE) {
            aGC = new BaseItemFineWire(aMaterial);
        } else if (aType == ComponentTypes.FOIL) {
            aGC = new BaseItemFoil(aMaterial);
        } else {
            aGC = new BaseItemComponent(aMaterial, aType);
        }
        String aFormattedLangName = aType.getName();

        if (!aFormattedLangName.startsWith(" ")) {
            if (aFormattedLangName.contains("@")) {
                String[] aSplit = aFormattedLangName.split("@");
                aFormattedLangName = aSplit[0] + " " + aMaterial.getLocalizedName() + " " + aSplit[1];
            }
        }

        if (aFormattedLangName.equals(aType.getName())) {
            aFormattedLangName = aMaterial.getLocalizedName() + aFormattedLangName;
        }

        Logger.MATERIALS("[Lang] " + aGC.getUnlocalizedName() + ".name=" + aFormattedLangName);
        aMaterial.registerComponentForMaterial(aType, ItemUtils.getSimpleStack(aGC));
    }

    public static void generateSpecialDustAndAssignToAMaterial(Material aMaterial, boolean generateMixerRecipes) {
        Item[] aDusts = ItemUtils.generateSpecialUseDusts(aMaterial, false, !generateMixerRecipes);
        if (aDusts.length > 0) {
            aMaterial.registerComponentForMaterial(OrePrefixes.dust, ItemUtils.getSimpleStack(aDusts[0]));
            aMaterial.registerComponentForMaterial(OrePrefixes.dustSmall, ItemUtils.getSimpleStack(aDusts[1]));
            aMaterial.registerComponentForMaterial(OrePrefixes.dustTiny, ItemUtils.getSimpleStack(aDusts[2]));
        }
    }

    public static boolean isNullGregtechMaterial(Materials aGregtechMaterial) {
        return aGregtechMaterial == Materials._NULL || aGregtechMaterial.equals(Materials._NULL)
                || aGregtechMaterial.mName.equals(Materials._NULL.mName);
    }
}
