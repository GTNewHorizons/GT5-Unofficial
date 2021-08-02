package common.tileentities;

import client.GTTexture;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.util.GT_Utility;
import kekztech.MultiFluidHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import java.util.HashMap;

public class GTMTE_TFFTMultiHatch extends GT_MetaTileEntity_Hatch {

    private static final HashMap<Integer, Integer> vals = new HashMap<>();
    static {
        vals.put(3, 2000);
        vals.put(5, 20000);
        vals.put(7, 200000);
    }
    private static final int INV_SLOT_COUNT = 2;

    private MultiFluidHandler mfh;
    private boolean outputting = false;

    public GTMTE_TFFTMultiHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, INV_SLOT_COUNT, new String[] {
                "All-in-one access for the T.F.F.T",
                "Right-click with a screwdriver to toggle auto-output",
                "Throughput: " + vals.get(aTier) + "L/s per fluid"}
        );
    }

    public GTMTE_TFFTMultiHatch(String aName, int aTier, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, INV_SLOT_COUNT, aDescription, aTextures);
    }

    public GTMTE_TFFTMultiHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, INV_SLOT_COUNT, aDescription, aTextures);
    }

    public void setMultiFluidHandler(MultiFluidHandler mfh) {
        this.mfh = mfh;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("outputting", outputting);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        outputting = aNBT.getBoolean("outputting");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        //return new ITexture[]{aBaseTexture, new GT_RenderedTexture(GTTexture.MULTI_HATCH_ON)};
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_PIPE_STEEL)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        //return new ITexture[]{aBaseTexture, new GT_RenderedTexture(GTTexture.MULTI_HATCH_OFF)};
        return new ITexture[]{aBaseTexture, new GT_RenderedTexture(Textures.BlockIcons.MACHINE_CASING_PIPE_POLYTETRAFLUOROETHYLENE)};
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new GTMTE_TFFTMultiHatch(super.mName, super.mTier, super.mDescriptionArray, super.mTextures);
    }

    @Override
    public boolean isMachineBlockUpdateRecursive() {
        return false;
    }

    @Override
    public void onScrewdriverRightClick(byte aSide, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        outputting = !outputting;
        GT_Utility.sendChatToPlayer(aPlayer, outputting ? "Auto-output enabled" : "Auto-output disabled");
    }

    @Override
    public int getCapacity() {
        return (mfh != null) ? mfh.getCapacity() : 0;
    }

    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && mfh != null) {
            if(outputting && (aTick % 20 == 0)) {
                doAutoOutputPerSecond(aBaseMetaTileEntity);
            }
        }
    }

    /**
     * Handle the Multi Hatch's auto-output feature. Should be called once per second only.
     * @param aBaseMetaTileEntity
     *              this MetaTileEntity
     */
    private void doAutoOutputPerSecond(IGregTechTileEntity aBaseMetaTileEntity) {
        final ForgeDirection outSide = ForgeDirection.getOrientation(aBaseMetaTileEntity.getFrontFacing());
        final TileEntity adjacentTE = aBaseMetaTileEntity.getTileEntityOffset(outSide.offsetX, outSide.offsetY, outSide.offsetZ);
        if(adjacentTE instanceof IFluidHandler) {
            final IFluidHandler adjFH = (IFluidHandler) adjacentTE;
            // Cycle through fluids
            for(int i = 0; i < mfh.getDistinctFluids(); i++) {
                final FluidStack fluidCopy = mfh.getFluidCopy(i);
                // Make sure the adjacent IFluidHandler can accept this fluid
                if(adjFH.canFill(outSide.getOpposite(), fluidCopy.getFluid())) {

                    // Limit to output rate
                    fluidCopy.amount = Math.min(fluidCopy.amount, vals.get(super.mTier));

                    // Test how much can be drawn
                    fluidCopy.amount = mfh.pullFluid(fluidCopy, false);

                    // Test how much can be filled (and fill if possible)
                    fluidCopy.amount = adjFH.fill(outSide.getOpposite(), fluidCopy, true);

                    // Actually deplete storage
                    mfh.pullFluid(fluidCopy, true);
                }
            }

        }
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        return (mfh != null) ? mfh.pushFluid(resource, doFill) : 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        return (mfh != null) ? new FluidStack(resource.getFluid(), mfh.pullFluid(resource, doDrain)) : null;
    }

    /**
     * Drains fluid out of 0th internal tank.
     * If the TFFT Controller contains an Integrated Circuit, drain fluid
     * from the slot equal to the circuit configuration.
     *
     * @param from
     *            Orientation the fluid is drained to.
     * @param maxDrain
     *            Maximum amount of fluid to drain.
     * @param doDrain
     *            If false, drain will only be simulated.
     * @return FluidStack representing the Fluid and amount that was (or would have been, if
     *         simulated) drained.
     */
    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if(mfh != null) {
            final FluidStack drain = mfh.getFluidCopy(0);
            if(drain != null) {
                // If there's no integrated circuit in the T.F.F.T. controller, output slot 0
                final byte selectedSlot = (mfh.getSelectedFluid() == -1) ? 0 : mfh.getSelectedFluid();

                return new FluidStack(
                        drain.getFluid(),
                        mfh.pullFluid(new FluidStack(drain.getFluid(), maxDrain), selectedSlot, doDrain)
                );
            }
        }
        return null;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        if (mfh == null)
            return null;
        FluidStack[] fluids = mfh.getAllFluids();
        int length = fluids.length;
        int maxCapcity = mfh.getCapacity();
        FluidTankInfo[] tankInfo = new FluidTankInfo[length];
        for (int i = 0; i < length; i++) {
            tankInfo[i] = new FluidTankInfo(fluids[i],maxCapcity);
        }
        return tankInfo;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, byte aSide, ItemStack aStack) {
        return false;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }
}
