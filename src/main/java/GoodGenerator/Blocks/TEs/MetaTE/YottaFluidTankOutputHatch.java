package GoodGenerator.Blocks.TEs.MetaTE;

import GoodGenerator.Blocks.TEs.YottaFluidTank;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.render.TextureFactory;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_OUT;

public class YottaFluidTankOutputHatch extends GT_MetaTileEntity_Hatch {

    private String mFluidName = "";
    private int mOutputSpeed = 0;
    private int mX, mZ, mY;
    private boolean isBound = false;

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        mFluidName = aNBT.getString("mFluidName");
        mOutputSpeed = aNBT.getInteger("mOutputSpeed");
        mX = aNBT.getInteger("mX");
        mZ = aNBT.getInteger("mZ");
        mY = aNBT.getInteger("mY");
        isBound = aNBT.getBoolean("isBound");
        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setString("mFluidName", mFluidName);
        aNBT.setInteger("mOutputSpeed", mOutputSpeed);
        aNBT.setInteger("mX", mX);
        aNBT.setInteger("mZ", mZ);
        aNBT.setInteger("mY", mY);
        aNBT.setBoolean("isBound", isBound);
        super.saveNBTData(aNBT);
    }

    public YottaFluidTankOutputHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Output Fluid From YOTTank.");
    }

    public YottaFluidTankOutputHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    public void setFluid(FluidStack output) {
        if (output == null) {
            mFluidName = "";
            mOutputSpeed = 0;
            return;
        }
        mFluidName = output.getFluid().getName();
        mOutputSpeed = output.amount;
    }

    public void setControl(int x, int y, int z) {
        mX = x;
        mY = y;
        mZ = z;
        isBound = true;
    }

    public void unBounded() {
        isBound = false;
    }

    public boolean isBounded() {
        return isBound;
    }

    @Override
    public boolean isFacingValid(byte aFacing) {
        return true;
    }

    @Override
    public boolean isLiquidInput(byte aSide) {
        return false;
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            IFluidHandler tTileEntity = aBaseMetaTileEntity.getITankContainerAtSide(aBaseMetaTileEntity.getFrontFacing());
            FluidStack tOutput = FluidRegistry.getFluidStack(mFluidName, mOutputSpeed);
            IGregTechTileEntity tController = aBaseMetaTileEntity.getIGregTechTileEntity(mX, mY, mZ);
            if (tTileEntity != null && tOutput != null && tController.getMetaTileEntity() instanceof YottaFluidTank && isBound) {
                int tAmount = Math.min(tTileEntity.fill(ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()), tOutput, false), mOutputSpeed);
                if (tAmount > 0) {
                    tOutput.amount = tAmount;
                    if (((YottaFluidTank) tController).reduceFluid(tAmount))
                        tTileEntity.fill(ForgeDirection.getOrientation(aBaseMetaTileEntity.getBackFacing()), tOutput, true);
                }
            }
            if (tController == null || !(tController.getMetaTileEntity() instanceof YottaFluidTank)) isBound = false;
        }
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT)};
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[]{aBaseTexture, TextureFactory.of(OVERLAY_PIPE_OUT)};
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new YottaFluidTankOutputHatch(mName, mTier, mDescriptionArray, mTextures);
    }
}
