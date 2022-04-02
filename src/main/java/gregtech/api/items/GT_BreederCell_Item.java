package gregtech.api.items;

import gregtech.api.GregTech_API;
import gregtech.api.util.GT_Utility;
import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorComponent;
import ic2.core.IC2Potion;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

import java.util.List;
import java.util.function.Supplier;

/**
 * A {@link ic2.core.item.reactor.ItemReactorLithiumCell}, but can be used to produce anything!
 *
 * @author glee8e
 */
public class GT_BreederCell_Item extends GT_Generic_Item implements IReactorComponent {
    protected final int mHeatBonusStep;
    protected final int mHeatBonusMultiplier;
    protected ItemStack mProduct;

    public GT_BreederCell_Item(String aUnlocalized, String aEnglish, String aEnglishTooltip, int aHeatBonusStep, int aHeatBonusMultiplier, int aRequiredPulse, Supplier<ItemStack> aProduct) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        this.mHeatBonusStep = aHeatBonusStep;
        this.mHeatBonusMultiplier = aHeatBonusMultiplier;
        this.setMaxDamage(aRequiredPulse);
        setNoRepair();
        GregTech_API.sAfterGTServerstart.add(() -> mProduct = aProduct.get());
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slotIndex, boolean isCurrentItem) {
        if ((entity instanceof EntityLivingBase)) {
            EntityLivingBase entityLiving = (EntityLivingBase) entity;
            if (!GregTech_API.mIC2Classic && !GT_Utility.isWearingFullRadioHazmat(entityLiving)) {
                IC2Potion.radiation.applyTo(entityLiving, 20, 1);
            }
        }
    }

    @Override
    public void addAdditionalToolTips(List aList, ItemStack aStack, EntityPlayer aPlayer) {
        aList.add(transItem("019", "Bath with neutron in a hot reactor"));
        int rDmg = aStack.getItemDamage() * 4 / getMaxDamage();
        EnumChatFormatting color2;
        switch (rDmg) {
            case 0:
                color2 = EnumChatFormatting.DARK_GRAY;
                break;
            case 1:
            case 2:
                color2 = EnumChatFormatting.GRAY;
                break;
            default:
                color2 = EnumChatFormatting.WHITE;
                break;
        }
        aList.add(String.format(transItem("020", "Progress: %s/%s"), "" + color2 + aStack.getItemDamage() + EnumChatFormatting.RESET, "" + getMaxDamage()));
        if (aStack.getItemDamage() > 0)
            aList.add(EnumChatFormatting.RED + transItem("021", "Radiation Hazard"));
    }

    @Override
    public int getItemStackLimit(ItemStack stack) {
        return stack.getItemDamage() == 0 ? maxStackSize : 1;
    }

    @Override
    public void processChamber(IReactor reactor, ItemStack yourStack, int x, int y, boolean heatrun) {
    }

    @Override
    public boolean acceptUraniumPulse(IReactor reactor, ItemStack yourStack, ItemStack pulsingStack, int youX, int youY, int pulseX, int pulseY, boolean heatrun) {
        if (heatrun) {
            int myLevel = getNewDamage(reactor, yourStack);
            if (myLevel >= getMaxDamage())
                reactor.setItemAt(youX, youY, mProduct.copy());
            else
                yourStack.setItemDamage(myLevel);
        }

        return true;
    }

    protected int getNewDamage(IReactor reactor, ItemStack stack) {
        return stack.getItemDamage() + 1 + reactor.getHeat() / mHeatBonusStep * mHeatBonusMultiplier;
    }

    @Override
    public boolean canStoreHeat(IReactor reactor, ItemStack yourStack, int x, int y) {
        return false;
    }

    @Override
    public int getMaxHeat(IReactor reactor, ItemStack yourStack, int x, int y) {
        return 0;
    }

    @Override
    public int getCurrentHeat(IReactor reactor, ItemStack yourStack, int x, int y) {
        return 0;
    }

    @Override
    public int alterHeat(IReactor reactor, ItemStack yourStack, int x, int y, int heat) {
        return heat;
    }

    @Override
    public float influenceExplosion(IReactor reactor, ItemStack yourStack) {
        return 0.0F;
    }

    @Override
    public double getDurabilityForDisplay(ItemStack stack) {
        return 1.0D - (double) stack.getItemDamageForDisplay() / (double) stack.getMaxDamage();
    }
}
