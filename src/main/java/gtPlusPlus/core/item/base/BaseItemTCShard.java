package gtPlusPlus.core.item.base;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.util.GT_LanguageManager;
import gregtech.api.util.GT_OreDictUnificator;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class BaseItemTCShard extends Item {

    public final String unlocalName;
    public final String displayName;
    public final int itemColour;

    public BaseItemTCShard(final String DisplayName, final int colour) {
        this(DisplayName, colour, null);
    }

    public BaseItemTCShard(final String DisplayName, final int colour, final String[] Description) {
        this.unlocalName = "item" + Utils.sanitizeString(DisplayName);
        this.displayName = DisplayName;
        this.itemColour = colour;
        this.setCreativeTab(AddToCreativeTab.tabMisc);
        this.setUnlocalizedName(this.unlocalName);
        if (Description != null) {
            for (int i = 0; i < Description.length; i++) {
                GT_LanguageManager
                        .addStringLocalization("gtplusplus." + getUnlocalizedName() + ".tooltip." + i, Description[i]);
            }
        }
        this.setMaxStackSize(64);
        this.setTextureName(GTPlusPlus.ID + ":" + "itemShard");
        GameRegistry.registerItem(this, this.unlocalName);
        GT_OreDictUnificator.registerOre("shard" + DisplayName, ItemUtils.getSimpleStack(this));
        GT_OreDictUnificator.registerOre("gemInfused" + DisplayName, ItemUtils.getSimpleStack(this));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
        for (int i = 0;; i++) {
            String tooltip = GT_LanguageManager
                    .getTranslation("gtplusplus." + this.getUnlocalizedName() + ".tooltip" + "." + i);
            if (!("gtplusplus." + this.getUnlocalizedName() + ".tooltip" + "." + i).equals(tooltip)) {
                list.add(tooltip);
            } else break;
        }
    }

    @Override
    public int getColorFromItemStack(final ItemStack stack, final int HEX_OxFFFFFF) {
        return this.itemColour;
    }

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
            final boolean p_77663_5_) {}
}
