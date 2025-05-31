package gtPlusPlus.core.item.materials;

import static gregtech.api.enums.Mods.GregTech;
import static gtPlusPlus.core.util.minecraft.ItemUtils.getSimpleStack;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import gregtech.api.enums.ItemList;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTRecipeConstants;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.handler.Recipes.DecayableRecipe;
import gtPlusPlus.core.item.base.BaseItemTickable;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.core.util.minecraft.EntityUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;

public class DustDecayable extends BaseItemTickable {

    private final ItemStack turnsIntoItem;
    private final int radLevel;

    public DustDecayable(String unlocal, int colour, int maxTicks, String[] desc1, ItemStack turnsInto, int radLevel,
        GTRecipeConstants.DecayType decayType) {
        super(true, true, unlocal, colour, maxTicks, desc1);
        this.turnsIntoItem = turnsInto;
        this.radLevel = radLevel;
        this.maxStackSize = 64;
        GTOreDictUnificator.registerOre(unlocal, ItemUtils.getSimpleStack(this));
        new DecayableRecipe(maxTicks, getSimpleStack(this), turnsInto, decayType);
    }

    @Override
    public void registerIcons(IIconRegister reg) {
        String gt = GregTech.ID + ":" + "materialicons/" + "NUCLEAR" + "/" + "dust";
        this.mIcon[0] = reg.registerIcon(gt);
        String gt2 = GregTech.ID + ":" + "materialicons/" + "NUCLEAR" + "/" + "dust" + "_OVERLAY";
        this.mIcon[1] = reg.registerIcon(gt2);
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List list, boolean bool) {
        super.addInformation(stack, player, list, bool);
        if (this.radLevel > 0) {
            list.add(GTPPCore.GT_Tooltip_Radioactive.get());
        }
        list.add(
            GTUtility.translate(
                "GTPP.tooltip.dust-decay-hint",
                ModBlocks.blockDecayablesChest.getLocalizedName(),
                ItemList.DecayWarehouse.get(1)
                    .getDisplayName()));
    }

    @Override
    public void onUpdate(final ItemStack stack, final World world, final Entity entityHolding, final int slot,
        final boolean heldInHand) {
        if (world == null || stack == null) {
            return;
        }
        if (world.isRemote) {
            return;
        }

        if (entityHolding instanceof EntityPlayer) {
            if (!((EntityPlayer) entityHolding).capabilities.isCreativeMode) {
                EntityUtils.applyRadiationDamageToEntity(stack.stackSize, this.radLevel, world, entityHolding);
            }
            // don't decay when held by a player
            return;
        }
    }

    public ItemStack getDecayResult() {
        return turnsIntoItem;
    }
}
