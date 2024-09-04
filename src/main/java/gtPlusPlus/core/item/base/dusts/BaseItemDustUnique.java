package gtPlusPlus.core.item.base.dusts;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gregtech.api.enums.Mods.GregTech;
import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.data.StringUtils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BaseItemDustUnique extends Item {

    protected final int colour;
    protected final int sRadiation;
    protected final String materialName;
    protected final String chemicalNotation;

    public BaseItemDustUnique(final String unlocalizedName, final String materialName, final int colour,
        final String pileSize) {
        this(unlocalizedName, materialName, "NullFormula", colour, pileSize);
    }

    public BaseItemDustUnique(final String unlocalizedName, final String materialName, final String mChemicalFormula,
        final int colour, final String pileSize) {
        this.setUnlocalizedName(unlocalizedName);
        this.setMaxStackSize(64);
        this.setTextureName(this.getCorrectTexture(pileSize));
        this.setCreativeTab(tabMisc);
        this.colour = colour;
        this.materialName = materialName;
        if (mChemicalFormula == null || mChemicalFormula.equals("") || mChemicalFormula.equals("NullFormula")) {
            this.chemicalNotation = StringUtils.subscript(materialName);
        } else {
            this.chemicalNotation = StringUtils.subscript(mChemicalFormula);
        }
        this.sRadiation = ItemUtils.getRadioactivityLevel(materialName);
        GameRegistry.registerItem(this, unlocalizedName);

        String type;
        if (this.getUnlocalizedName()
            .contains("DustTiny")) {
            type = "Tiny Pile of %material Dust";
        } else if (this.getUnlocalizedName()
            .contains("DustSmall")) {
                type = "Small Pile of %material Dust";
            } else {
                type = "%material Dust";
            }
        GTLanguageManager.addStringLocalization("gtplusplus." + this.getUnlocalizedName() + ".name", type);

        String temp = "";
        Logger.WARNING("Unlocalized name for OreDict nameGen: " + this.getUnlocalizedName());
        if (this.getUnlocalizedName()
            .contains("item.")) {
            temp = this.getUnlocalizedName()
                .replace("item.", "");
            Logger.WARNING("Generating OreDict Name: " + temp);
        } else {
            temp = this.getUnlocalizedName();
        }
        if (temp.contains("DustTiny")) {
            temp = temp.replace("itemD", "d");
            Logger.WARNING("Generating OreDict Name: " + temp);
        } else if (temp.contains("DustSmall")) {
            temp = temp.replace("itemD", "d");
            Logger.WARNING("Generating OreDict Name: " + temp);
        } else {
            temp = temp.replace("itemD", "d");
            Logger.WARNING("Generating OreDict Name: " + temp);
        }
        if ((temp != null) && !temp.equals("")) {
            GTOreDictUnificator.registerOre(temp, ItemUtils.getSimpleStack(this));
        }
        registerComponent();
    }

    public boolean registerComponent() {
        if (this.materialName == null) {
            return false;
        }
        String aName = materialName;
        // Register Component
        Map<String, ItemStack> aMap = Material.mComponentMap.get(aName);
        if (aMap == null) {
            aMap = new HashMap<>();
        }
        String aKey = OrePrefixes.dust.name();
        ItemStack x = aMap.get(aKey);
        if (x == null) {
            aMap.put(aKey, ItemUtils.getSimpleStack(this));
            Logger.MATERIALS("Registering a material component. Item: [" + aName + "] Map: [" + aKey + "]");
            Material.mComponentMap.put(aName, aMap);
            return true;
        } else {
            // Bad
            Logger.MATERIALS("Tried to double register a material component. ");
            return false;
        }
    }

    @Override
    public String getItemStackDisplayName(final ItemStack iStack) {
        return GTLanguageManager.getTranslation("gtplusplus." + getUnlocalizedName() + ".name")
            .replace("%material", GTLanguageManager.getTranslation("gtplusplus.material." + materialName));
    }

    private String getCorrectTexture(final String pileSize) {
        if (!GTPPCore.ConfigSwitches.useGregtechTextures) {
            if ((pileSize.equals("dust")) || (pileSize.equals("Dust"))) {
                this.setTextureName(GTPlusPlus.ID + ":" + "dust");
            } else {
                this.setTextureName(GTPlusPlus.ID + ":" + "dust" + pileSize);
            }
        }
        if (pileSize.toLowerCase()
            .contains("small")) {
            return GregTech.ID + ":" + "materialicons/SHINY/dustSmall";
        } else if (pileSize.toLowerCase()
            .contains("tiny")) {
                return GregTech.ID + ":" + "materialicons/SHINY/dustTiny";
            }
        return GregTech.ID + ":" + "materialicons/SHINY/dust";
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
        if (this.sRadiation > 0) {
            list.add(GTPPCore.GT_Tooltip_Radioactive.get());
        }
        if (this.chemicalNotation.length() > 0 && !chemicalNotation.equals("")
            && !chemicalNotation.equals("NullFormula")) {
            list.add(this.chemicalNotation);
        }
        super.addInformation(stack, aPlayer, list, bool);
    }

    public final String getMaterialName() {
        return StringUtils.subscript(this.materialName);
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
        if (this.colour == 0) {
            return MathUtils.generateSingularRandomHexValue();
        }
        return this.colour;
    }
}
