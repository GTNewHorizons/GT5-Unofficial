package gtPlusPlus.core.item.materials;

import static gregtech.api.enums.Mods.GregTech;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import gregtech.api.enums.ItemList;
import gregtech.api.hazards.IRadioactiveItem;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.OreDictUtils;
import gregtech.common.config.Client;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.handler.Recipes.DecayableRecipe;
import gtPlusPlus.core.item.base.BaseItemTickable;

public class DustDecayable extends BaseItemTickable implements IRadioactiveItem {

    private final ItemStack turnsIntoItem;
    private final int radLevel;
    private final String decayResultKey;
    private final String decayProduct;

    /**
     * @param formula      the isotope of this dust, shown as its formula
     * @param decayProduct the isotope this dust decays into, passed as the argument of the decay result line
     */
    public DustDecayable(String unlocal, int colour, int maxTicks, String formula, String decayProduct,
        ItemStack turnsInto, int radLevel, GTRecipeConstants.DecayType decayType) {
        super(true, unlocal, colour, maxTicks, new String[] { formula });
        this.turnsIntoItem = turnsInto;
        this.radLevel = radLevel;
        this.decayResultKey = "gtplusplus.item." + unlocal + ".decay_result";
        this.decayProduct = decayProduct;
        this.maxStackSize = 64;
        OreDictUtils.registerOre(unlocal, new ItemStack(this));
        new DecayableRecipe(maxTicks, new ItemStack(this), turnsInto, decayType);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        String gt = GregTech.ID + ":" + "materials/" + "NUCLEAR" + "/" + "dust";
        this.mIcon[0] = reg.registerIcon(gt);
        String gt2 = GregTech.ID + ":" + "materials/" + "NUCLEAR" + "/" + "dust" + "_OVERLAY";
        this.mIcon[1] = reg.registerIcon(gt2);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean adv) {
        super.addInformation(stack, player, list, adv);
        list.add(
            EnumChatFormatting.GRAY + StatCollector.translateToLocalFormatted(this.decayResultKey, this.decayProduct));
        if (Client.tooltip.showRadioactiveText) {
            if (this.radLevel > 0) {
                list.add(StatCollector.translateToLocal("GTPP.core.GT_Tooltip_Radioactive"));
                list.add(StatCollector.translateToLocal("GTPP.core.GT_Tooltip_HazmatWarning"));
            }
        }
        list.add(
            StatCollector.translateToLocalFormatted(
                "GTPP.tooltip.dust-decay-hint",
                ModBlocks.blockDecayablesChest.getLocalizedName(),
                ItemList.DecayWarehouse.get(1)
                    .getDisplayName()));
    }

    /// Decay advances only in a decayables chest or a decay warehouse, never in an inventory.
    @Override
    public void onUpdate(final ItemStack stack, final World world, final Entity entityHolding, final int slot,
        final boolean heldInHand) {}

    @Override
    public int getRadiationLevel(ItemStack stack) {
        return radLevel;
    }

    public ItemStack getDecayResult() {
        return turnsIntoItem;
    }
}
