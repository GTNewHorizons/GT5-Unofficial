package gtPlusPlus.core.item.base.dusts;

import static gregtech.api.enums.Mods.GregTech;
import static gtPlusPlus.core.creative.AddToCreativeTab.tabMisc;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.Dyes;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.StringUtils;
import gregtech.common.config.Client;
import gtPlusPlus.core.util.minecraft.ItemUtils;

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
        registerLocalizedName(materialName);
        if (mChemicalFormula == null || mChemicalFormula.isEmpty() || mChemicalFormula.equals("NullFormula")) {
            this.chemicalNotation = StringUtils.subscript(materialName);
        } else {
            this.chemicalNotation = StringUtils.subscript(mChemicalFormula);
        }
        this.sRadiation = ItemUtils.getRadioactivityLevel(materialName);
        GameRegistry.registerItem(this, unlocalizedName);

        String temp = "";
        if (this.getUnlocalizedName()
            .contains("item.")) {
            temp = this.getUnlocalizedName()
                .replace("item.", "");
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
        if (!temp.isEmpty()) {
            GTOreDictUnificator.registerOre(temp, new ItemStack(this));
        }
    }

    @Override
    public String getItemStackDisplayName(final ItemStack iStack) {
        return translateToLocalFormatted(typeLoc, localizedName(this.materialName));
    }

    private String getCorrectTexture(final String pileSize) {
        if (pileSize.toLowerCase()
            .contains("small")) {
            return GregTech.ID + ":" + "materials/SHINY/dustSmall";
        } else if (pileSize.toLowerCase()
            .contains("tiny")) {
                return GregTech.ID + ":" + "materials/SHINY/dustTiny";
            }
        return GregTech.ID + ":" + "materials/SHINY/dust";
    }

    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer player, final List<String> list,
        final boolean adv) {
        if (Client.tooltip.showFormula) {
            if (!this.chemicalNotation.isEmpty() && !chemicalNotation.equals("NullFormula")) {
                list.add(this.chemicalNotation);
            }
        }
        if (Client.tooltip.showRadioactiveText) {
            if (this.sRadiation > 0) {
                list.add(StatCollector.translateToLocalFormatted("GTPP.core.GT_Tooltip_Radioactive", this.sRadiation));
            }
        }
        super.addInformation(stack, player, list, adv);
    }

    public final String getMaterialName() {
        return StringUtils.subscript(this.materialName);
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
        return this.colour;
    }

    /// Registers `name` as its own display name under [#nameKey].
    private static void registerLocalizedName(String name) {
        GTLanguageManager.addStringLocalization(nameKey(name), name);
    }

    /// The display name registered for `name`, or the key itself when the lang file has no entry.
    private static String localizedName(String name) {
        return StatCollector.translateToLocal(nameKey(name));
    }

    /// The lang key for a unique dust's display name. These dusts back no MaterialLib material, so their
    /// names are free-form display strings ("Tumbaga Mix", "Cooked ZrCl4") rather than material names --
    /// hence the non-alphanumeric stripping, which the material-backed
    /// [gregtech.api.material.MaterialUtils#localizedNameKey] scheme does not do. The two schemes are not
    /// interchangeable.
    private static String nameKey(String name) {
        return "Material." + name.toLowerCase()
            .replaceAll("[^a-zA-Z0-9]", "");
    }

}
