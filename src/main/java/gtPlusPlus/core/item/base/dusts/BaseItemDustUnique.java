package gtPlusPlus.core.item.base.dusts;

import static gregtech.api.enums.Mods.GregTech;
import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.StringUtils;
import gregtech.common.config.Client;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.core.material.Material;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.MaterialUtils;

public class BaseItemDustUnique extends Item {

    protected final int colour;
    protected final int sRadiation;
    protected final String typeLoc;
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
        this.colour = colour == 0 ? Dyes._NULL.toInt() : colour;
        this.materialName = materialName;
        MaterialUtils.generateMaterialLocalizedName(materialName);
        if (mChemicalFormula == null || mChemicalFormula.isEmpty() || mChemicalFormula.equals("NullFormula")) {
            this.chemicalNotation = StringUtils.subscript(materialName);
        } else {
            this.chemicalNotation = StringUtils.subscript(mChemicalFormula);
        }
        this.sRadiation = ItemUtils.getRadioactivityLevel(materialName);
        GameRegistry.registerItem(this, unlocalizedName);

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
            this.typeLoc = "gt.oreprefix.tiny_pile_of_material";
        } else if (temp.contains("DustSmall")) {
            this.typeLoc = "gt.oreprefix.small_pile_of_material_dust";
        } else {
            this.typeLoc = "gt.oreprefix.material_dust";
        }
        temp = temp.replace("itemD", "d");
        Logger.WARNING("Generating OreDict Name: " + temp);
        if (!temp.isEmpty()) {
            GTOreDictUnificator.registerOre(temp, new ItemStack(this));
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
        String aKey = OrePrefixes.dust.getName();
        ItemStack x = aMap.get(aKey);
        if (x == null) {
            aMap.put(aKey, new ItemStack(this));
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
        return translateToLocalFormatted(typeLoc, MaterialUtils.getMaterialLocalizedName(this.materialName));
    }

    private String getCorrectTexture(final String pileSize) {
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
        if (Client.tooltip.showRadioactiveText) {
            if (this.sRadiation > 0) {
                list.add(StatCollector.translateToLocalFormatted("GTPP.core.GT_Tooltip_Radioactive", this.sRadiation));
            }
        }
        if (Client.tooltip.showFormula) {
            if (!this.chemicalNotation.isEmpty() && !chemicalNotation.equals("NullFormula")) {
                list.add(this.chemicalNotation);
            }
        }
        super.addInformation(stack, aPlayer, list, bool);
    }

    public final String getMaterialName() {
        return StringUtils.subscript(this.materialName);
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
        return this.colour;
    }
}
