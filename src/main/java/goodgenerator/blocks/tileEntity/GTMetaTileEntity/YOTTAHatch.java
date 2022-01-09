package goodgenerator.blocks.tileEntity.GTMetaTileEntity;

import goodgenerator.blocks.tileEntity.YottaFluidTank;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.render.TextureFactory;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import java.math.BigInteger;

public class YOTTAHatch extends GT_MetaTileEntity_Hatch {

    private static final IIconContainer textureFont = new Textures.BlockIcons.CustomIcon("icons/YOTTAHatch");

    private YottaFluidTank host;

    public YOTTAHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Special I/O port for EC2 Fluid Storage Bus.");
    }

    public YOTTAHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    public void setTank(YottaFluidTank te) {
        this.host = te;
    }

    @Override
    public int getCapacity() {
        if (host == null || host.getBaseMetaTileEntity() == null || !host.getBaseMetaTileEntity().isActive()) return 0;
        if (host.mStorage.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) >= 0) {
            return Integer.MAX_VALUE;
        }
        else return host.mStorage.intValue();
    }

    @Override
    public int fill(ForgeDirection from, FluidStack resource, boolean doFill) {
        if (host == null || host.getBaseMetaTileEntity() == null || !host.getBaseMetaTileEntity().isActive()) return 0;
        if (host.mFluidName == null || host.mFluidName.equals("") || host.mFluidName.equals(resource.getFluid().getName())) {
            host.mFluidName = resource.getFluid().getName();
            if (host.mStorage.subtract(host.mStorageCurrent).compareTo(BigInteger.valueOf(resource.amount)) >= 0) {
                if (doFill)
                    host.addFluid(resource.amount);
                return resource.amount;
            }
            else {
                int added = host.mStorage.subtract(host.mStorageCurrent).intValue();
                if (doFill)
                    host.addFluid(added);
                return added;
            }
        }
        return 0;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack resource, boolean doDrain) {
        if (host == null || host.getBaseMetaTileEntity() == null || !host.getBaseMetaTileEntity().isActive())
            return null;
        if (host.mFluidName == null || host.mFluidName.equals("") || !host.mFluidName.equals(resource.getFluid().getName()))
            return null;
        int ready;
        if (host.mStorageCurrent.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0) {
            ready = Integer.MAX_VALUE;
        }
        else ready = host.mStorageCurrent.intValue();
        ready = Math.min(ready, resource.amount);
        if (doDrain) {
            host.reduceFluid(ready);
        }
        return new FluidStack(resource.getFluid(), ready);
    }

    @Override
    public FluidStack drain(ForgeDirection from, int maxDrain, boolean doDrain) {
        if (host == null || host.getBaseMetaTileEntity() == null || !host.getBaseMetaTileEntity().isActive())
            return null;
        if (host.mFluidName == null || host.mFluidName.equals(""))
            return null;
        return this.drain(from, FluidRegistry.getFluidStack(host.mFluidName, maxDrain), doDrain);
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        FluidTankInfo[] tankInfo = new FluidTankInfo[1];
        tankInfo[0] = new FluidTankInfo(null, 0);
        if (host == null || host.getBaseMetaTileEntity() == null || !host.getBaseMetaTileEntity().isActive())
            return tankInfo;
        FluidStack fluid = null;
        if (host.mFluidName != null && !host.mFluidName.equals("")) {
            int camt;
            if (host.mStorageCurrent.compareTo(BigInteger.valueOf(Integer.MAX_VALUE)) > 0)
                camt = Integer.MAX_VALUE;
            else camt = host.mStorageCurrent.intValue();
            fluid = FluidRegistry.getFluidStack(host.mFluidName, camt);
        }

        tankInfo[0] = new FluidTankInfo(fluid, getCapacity());
        return tankInfo;
    }

    @Override
    public boolean canTankBeFilled() {
        return true;
    }

    @Override
    public boolean canTankBeEmptied() {
        return true;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] {
                aBaseTexture,
                TextureFactory.of(textureFont),
        };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] {
                aBaseTexture,
                TextureFactory.of(textureFont),
        };
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new YOTTAHatch(mName, mTier, mDescriptionArray, mTextures);
    }
}
