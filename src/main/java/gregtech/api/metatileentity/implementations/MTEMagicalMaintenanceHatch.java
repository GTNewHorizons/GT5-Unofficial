package gregtech.api.metatileentity.implementations;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.visnet.VisNetHandler;

import static net.minecraft.util.StatCollector.translateToLocal;

public class MTEMagicalMaintenanceHatch extends MTEHatchMaintenance {

    private static Textures.BlockIcons.CustomIcon face;

    private int mAirDrain = 0;
    private int mEarthDrain = 0;
    private int mFireDrain = 0;
    private int mWaterDrain = 0;
    private int mOrderDrain = 0;
    private int mEntropyDrain = 0;
    private final int mVisCap = 50;
    private final int mVisCost = 25;
    private final int mVisPassiveDrain = 1;


    private static final ResourceLocation focusMaintenanceSound = new ResourceLocation(
        "emt",
        "maintenance.MaintenanceWandFocus");

    public MTEMagicalMaintenanceHatch(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier);
    }

    public MTEMagicalMaintenanceHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures, false);
    }

    @Override
    public String[] getDescription() {
        return new String[] {
            translateToLocal("gt.blockmachines.magical.maintenance.desc.0"), // For magically maintaining Multiblocks with Centi-Vis
            translateToLocal("gt.blockmachines.magical.maintenance.desc.1"), // Consumes 25 Centi-Vis of each primal aspect to fix all maintenance issues.
            translateToLocal("gt.blockmachines.magical.maintenance.desc.2"), // Passively Consumes 1 Centi-Vis of each primal aspect every 2.5 Seconds.
        };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        face = new Textures.BlockIcons.CustomIcon("iconsets/OVERLAY_MAGICALMAINTENANCE");
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(face) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(face) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEMagicalMaintenanceHatch(this.mName, this.mTier, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound nbt) {
        super.saveNBTData(nbt);
        nbt.setInteger("mAirDrained", this.mAirDrain);
        nbt.setInteger("mEarthDrain", this.mEarthDrain);
        nbt.setInteger("mFireDrain", this.mFireDrain);
        nbt.setInteger("mWaterDrain", this.mWaterDrain);
        nbt.setInteger("mOrderDrain", this.mOrderDrain);
        nbt.setInteger("mAirDrained", this.mAirDrain);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        this.mAirDrain = nbt.getInteger("mAirDrained");
        this.mEarthDrain = nbt.getInteger("mEarthDrain");
        this.mFireDrain = nbt.getInteger("mFireDrain");
        this.mWaterDrain = nbt.getInteger("mWaterDrain");
        this.mOrderDrain = nbt.getInteger("mOrderDrain");
        this.mEntropyDrain = nbt.getInteger("mEntropyDrain");
    }


    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        // Internal Buffer Soft Caps at 50 vis
        if (mAirDrain < mVisCap) {
            mAirDrain += VisNetHandler.drainVis(getWorld(), getXCoord(), getYCoord(), getZCoord(), Aspect.AIR, 5);
        }
        if (mEarthDrain < mVisCap) {
            mEarthDrain += VisNetHandler.drainVis(getWorld(), getXCoord(), getYCoord(), getZCoord(), Aspect.EARTH, 5);
        }
        if (mFireDrain < mVisCap) {
            mFireDrain += VisNetHandler.drainVis(getWorld(), getXCoord(), getYCoord(), getZCoord(), Aspect.FIRE, 5);
        }
        if (mWaterDrain < mVisCap) {
            mWaterDrain += VisNetHandler.drainVis(getWorld(), getXCoord(), getYCoord(), getZCoord(), Aspect.WATER, 5);
        }
        if (mOrderDrain < mVisCap) {
            mOrderDrain += VisNetHandler.drainVis(getWorld(), getXCoord(), getYCoord(), getZCoord(), Aspect.ORDER, 5);
        }
        if (mEntropyDrain < mVisCap) {
            mEntropyDrain += VisNetHandler.drainVis(getWorld(), getXCoord(), getYCoord(), getZCoord(), Aspect.ENTROPY, 5);
        }

        // Drain if needed
        if (mAirDrain >= mVisCost && !this.mWrench) {
            this.mWrench = true;
            mAirDrain -= mVisCost;
        }
        if (mEarthDrain >= mVisCost && !this.mScrewdriver) {
            this.mScrewdriver = true;
            mEarthDrain -= mVisCost;
        }
        if (mFireDrain >= mVisCost && !this.mSolderingTool) {
            this.mSolderingTool = true;
            mFireDrain -= mVisCost;
        }
        if (mWaterDrain >= mVisCost && !this.mCrowbar) {
            this.mCrowbar = true;
            mWaterDrain -= mVisCost;
        }
        if (mOrderDrain >= mVisCost && !this.mSoftMallet) {
            this.mSoftMallet = true;
            mOrderDrain -= mVisCost;
        }
        if (mEntropyDrain >= mVisCost && !this.mHardHammer) {
            this.mHardHammer = true;
            mEntropyDrain -= mVisCost;
        }

        // Passive Drain 1 Centi-Vis every 2.5 seconds
        if (aTick % 50 == 0) {
            mAirDrain -= mVisPassiveDrain;
            mEarthDrain -= mVisPassiveDrain;
            mFireDrain -= mVisPassiveDrain;
            mWaterDrain -= mVisPassiveDrain;
            mOrderDrain -= mVisPassiveDrain;
            mEntropyDrain -= mVisPassiveDrain;
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
                                float aX, float aY, float aZ) {
        return false;
    }

    @Override
    public void onMaintenancePerformed(MTEMultiBlockBase aMaintenanceTarget) {
        setMaintenanceSound(focusMaintenanceSound, 1.0F, 1.0F);
        super.onMaintenancePerformed(aMaintenanceTarget);
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
                                  ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
                                 ItemStack aStack) {
        return false;
    }

    private World getWorld() {
        return getBaseMetaTileEntity().getWorld();
    }

    private int getXCoord() {
        return getBaseMetaTileEntity().getXCoord();
    }

    private int getYCoord() {
        return getBaseMetaTileEntity().getYCoord();
    }

    private int getZCoord() {
        return getBaseMetaTileEntity().getZCoord();
    }
}
