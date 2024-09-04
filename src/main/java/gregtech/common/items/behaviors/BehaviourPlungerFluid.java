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
    private final String mTooltip = GTLanguageManager
        .addStringLocalization("gt.behaviour.plunger.fluid", "Clears 1000 Liters of Fluid from Tanks");

    public BehaviourPlungerFluid(int aCosts) {
        this.mCosts = aCosts;
    }

    @Override
    public boolean onItemUseFirst(MetaBaseItem aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX,
        int aY, int aZ, ForgeDirection side, float hitX, float hitY, float hitZ) {
        if (aWorld.isRemote) {
            return false;
        }
        TileEntity aTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if ((aTileEntity instanceof IFluidHandler)) {
            for (ForgeDirection tDirection : ForgeDirection.VALID_DIRECTIONS) {
                if (((IFluidHandler) aTileEntity).drain(tDirection, 1000, false) != null) {
                    if ((aPlayer.capabilities.isCreativeMode)
                        || (((MetaGeneratedTool) aItem).doDamage(aStack, this.mCosts))) {
                        ((IFluidHandler) aTileEntity).drain(tDirection, 1000, true);
                        GTUtility.sendSoundToPlayers(
                            aWorld,
                            SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE,
                            1.0F,
                            -1.0F,
                            aX,
                            aY,
                            aZ);
                        return true;
                    }
                }
            }
        }
        if (aTileEntity instanceof IGregTechTileEntity tTileEntity) {
            IMetaTileEntity mTileEntity = tTileEntity.getMetaTileEntity();
            if (mTileEntity instanceof MTEBasicTank machine) {
                if (machine.mFluid != null && machine.mFluid.amount > 0)
                    machine.mFluid.amount = machine.mFluid.amount - Math.min(machine.mFluid.amount, 1000);
                GTUtility
                    .sendSoundToPlayers(aWorld, SoundResource.IC2_TOOLS_RUBBER_TRAMPOLINE, 1.0F, -1.0F, aX, aY, aZ);
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
