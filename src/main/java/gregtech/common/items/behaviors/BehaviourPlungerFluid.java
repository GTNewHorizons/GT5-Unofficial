package gregtech.common.items.behaviors;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.IFluidHandler;

import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.MetaBaseItem;
import gregtech.api.items.MetaGeneratedTool;
import gregtech.api.metatileentity.implementations.MTEBasicTank;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;

public class BehaviourPlungerFluid extends BehaviourNone {

    private final int mCosts;
    private final String mTooltip = GTLanguageManager.addStringLocalization(
        "gt.behaviour.plunger.fluid",
        "Clears 1000 Liters of Fluid from Tanks. Fully clears when sneaking.");

    public BehaviourPlungerFluid(int aCosts) {
        this.mCosts = aCosts;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        int drainAmount = aPlayer.isSneaking() ? Integer.MAX_VALUE : 1000;
        TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((aTileEntity instanceof IFluidHandler)) {
            for (ForgeDirection tDirection : ForgeDirection.VALID_DIRECTIONS) {
                if (((IFluidHandler) aTileEntity).drain(tDirection, drainAmount, false) != null) {
                    if ((aPlayer.capabilities.isCreativeMode)
                        || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                        ((IFluidHandler) aTileEntity).drain(tDirection, drainAmount, true);
                        GTUtility
                            .sendSoundToPlayers(aWorld, SoundResource.GTCEU_OP_PLUNGER, 1.0F, 1.0F, hitX, hitY, hitZ);
                        return true;
                    }
                }
            }
        }
        if (aTileEntity instanceof IGregTechTileEntity tTileEntity) {
            IMetaTileEntity mTileEntity = tTileEntity.getMetaTileEntity();
            if (mTileEntity instanceof MTEBasicTank machine) {
                if (machine.mFluid != null && machine.mFluid.amount > 0)
                    machine.mFluid.amount = machine.mFluid.amount - Math.min(machine.mFluid.amount, drainAmount);
                GTUtility.sendSoundToPlayers(aWorld, SoundResource.GTCEU_OP_PLUNGER, 1.0F, 1.0F, hitX, hitY, hitZ);
                return true;
            }
        }
        return false;
    }

    @Override
    public List<String> getAdditionalToolTips(MetaBaseItem aItem, List<String> aList, ItemStack aStack) {
        aList.add(this.mTooltip);
        return aList;
    }
}
