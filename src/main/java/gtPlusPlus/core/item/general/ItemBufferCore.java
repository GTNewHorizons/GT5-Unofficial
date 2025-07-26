package gtPlusPlus.core.item.general;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.util.ColorUtil;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.base.BaseItemWithDamageValue;
import gtPlusPlus.core.util.math.MathUtils;

public class ItemBufferCore extends BaseItemWithDamageValue {

    public int coreTier;

    public ItemBufferCore(final String unlocalizedName, final int i) {
        super(unlocalizedName + i);
        this.setTextureName(GTPlusPlus.ID + ":" + unlocalizedName);
        this.setMaxStackSize(32);
        this.setCreativeTab(AddToCreativeTab.tabMachines);
        this.coreTier = i;
        GameRegistry.registerItem(this, getUnlocalizedName());
    }

    @Override
    public String getItemStackDisplayName(final ItemStack stack) {
        return super.getItemStackDisplayName(stack) /* +" ["+GT_Values.VN[this.coreTier-1]+"]." */;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean requiresMultipleRenderPasses() {
        return true;
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(final ItemStack stack, final EntityPlayer aPlayer, final List list, final boolean bool) {
        list.add(
            EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(
                "gtpp.tooltip.buffer_core.key_crafting_component",
                GTValues.VN[this.coreTier - 1]));
    }

    public final int getCoreTier() {
        return this.coreTier;
    }

    // spotless:off
    private static final int[] mTierColors = new int[] {
        ColorUtil.toRGB(200, 180, 180),
        ColorUtil.toRGB(142, 153, 161),
        ColorUtil.toRGB(230, 121, 75),
        ColorUtil.toRGB(215, 156, 70),
        ColorUtil.toRGB(97, 97, 96), // EV
        ColorUtil.toRGB(202, 202, 201),
        ColorUtil.toRGB(247, 159, 157),
        ColorUtil.toRGB(181, 223, 223),
        ColorUtil.toRGB(187, 219, 185)};
    // spotless:on

    @Override
    public int getColorFromItemStack(final ItemStack stack, int colorRGB) {
        if (this.coreTier == 10) {
            return ColorUtil
                .toRGB(MathUtils.randInt(220, 250), MathUtils.randInt(221, 251), MathUtils.randInt(220, 250));
        }
        return mTierColors[this.coreTier - 1];
    }
}
