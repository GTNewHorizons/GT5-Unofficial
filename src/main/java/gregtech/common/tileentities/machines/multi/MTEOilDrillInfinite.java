package gregtech.common.tileentities.machines.multi;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import gregtech.api.enums.ItemList;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;

public class MTEOilDrillInfinite extends MTEOilDrillBase {

    public MTEOilDrillInfinite(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEOilDrillInfinite(String aName) {
        super(aName);
    }

    @Override
    public boolean onWireCutterRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer,
        float aX, float aY, float aZ, ItemStack aTool) {
        this.batchMode = !this.batchMode;
        GTUtility.sendChatTrans(
            aPlayer,
            this.batchMode ? "GT5U.chat.machine.batch_mode.enable" : "GT5U.chat.machine.batch_mode.disable");
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEOilDrillInfinite(mName);
    }

    @Override
    protected FluidStack pumpOil(float speed, boolean simulate) {
        // always simulate to not deplete vein
        return super.pumpOil(speed, true);
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    protected ItemList getCasingBlockItem() {
        return ItemList.Casing_MiningNeutronium;
    }

    @Override
    protected Materials getFrameMaterial() {
        return Materials.Neutronium;
    }

    @Override
    protected int getCasingTextureIndex() {
        return 178;
    }

    @Override
    protected int getRangeInChunks() {
        return 8;
    }

    @Override
    protected float computeSpeed() {
        return .5F + (GTUtility.getTier(getMaxInputVoltage()) - getMinTier() + 5) * .25F;
    }

    @Override
    protected int getMinTier() {
        return 9;
    }
}
