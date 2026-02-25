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

    private int mAirBuffer = 0;
    private int mEarthBuffer = 0;
    private int mFireBuffer = 0;
    private int mWaterBuffer = 0;
    private int mOrderBuffer = 0;
    private int mEntropyBuffer = 0;
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
            translateToLocal("gt.blockmachines.magical.maintenance.desc.0"), // For magically maintaining Multiblocks with Centi-Vis.
            translateToLocal("gt.blockmachines.magical.maintenance.desc.1"), // Passively Consumes 1 Centi-Vis of each primal aspect every 2.5 Seconds.
            translateToLocal("gt.blockmachines.magical.maintenance.desc.2"), // Consumes 25 primal vis to fix corresponding maintenance issue.
            translateToLocal("gt.blockmachines.magical.maintenance.desc.3"), // Aer (Wrench), Terra (Screwdriver), Ignis (Soldering Iron)
            translateToLocal("gt.blockmachines.magical.maintenance.desc.4"), // Aqua (Crowbar), Ordo (Soft Mallet), Perditio (Hard Hammer)

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
        nbt.setInteger("mAirBuffer", this.mAirBuffer);
        nbt.setInteger("mEarthBuffer", this.mEarthBuffer);
        nbt.setInteger("mFireBuffer", this.mFireBuffer);
        nbt.setInteger("mWaterBuffer", this.mWaterBuffer);
        nbt.setInteger("mOrderBuffer", this.mOrderBuffer);
        nbt.setInteger("mEntropyBuffer", this.mEntropyBuffer);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        this.mAirBuffer = nbt.getInteger("mAirBuffer");
        this.mEarthBuffer = nbt.getInteger("mEarthBuffer");
        this.mFireBuffer = nbt.getInteger("mFireBuffer");
        this.mWaterBuffer = nbt.getInteger("mWaterBuffer");
        this.mOrderBuffer = nbt.getInteger("mOrderBuffer");
        this.mEntropyBuffer = nbt.getInteger("mEntropyBuffer");
    }


    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        // Internal Buffer, Soft Caps at 50 vis
        if (mAirBuffer < mVisCap) {
            mAirBuffer += VisNetHandler.drainVis(getWorld(), getXCoord(), getYCoord(), getZCoord(), Aspect.AIR, 5);
        }
        if (mEarthBuffer < mVisCap) {
            mEarthBuffer += VisNetHandler.drainVis(getWorld(), getXCoord(), getYCoord(), getZCoord(), Aspect.EARTH, 5);
        }
        if (mFireBuffer < mVisCap) {
            mFireBuffer += VisNetHandler.drainVis(getWorld(), getXCoord(), getYCoord(), getZCoord(), Aspect.FIRE, 5);
        }
        if (mWaterBuffer < mVisCap) {
            mWaterBuffer += VisNetHandler.drainVis(getWorld(), getXCoord(), getYCoord(), getZCoord(), Aspect.WATER, 5);
        }
        if (mOrderBuffer < mVisCap) {
            mOrderBuffer += VisNetHandler.drainVis(getWorld(), getXCoord(), getYCoord(), getZCoord(), Aspect.ORDER, 5);
        }
        if (mEntropyBuffer < mVisCap) {
            mEntropyBuffer += VisNetHandler.drainVis(getWorld(), getXCoord(), getYCoord(), getZCoord(), Aspect.ENTROPY, 5);
        }

        // Consume buffered vis to repair maintenance tools
        if (mAirBuffer >= mVisCost && !this.mWrench) {
            this.mWrench = true;
            mAirBuffer -= mVisCost;
        }
        if (mEarthBuffer >= mVisCost && !this.mScrewdriver) {
            this.mScrewdriver = true;
            mEarthBuffer -= mVisCost;
        }
        if (mFireBuffer >= mVisCost && !this.mSolderingTool) {
            this.mSolderingTool = true;
            mFireBuffer -= mVisCost;
        }
        if (mWaterBuffer >= mVisCost && !this.mCrowbar) {
            this.mCrowbar = true;
            mWaterBuffer -= mVisCost;
        }
        if (mOrderBuffer >= mVisCost && !this.mSoftMallet) {
            this.mSoftMallet = true;
            mOrderBuffer -= mVisCost;
        }
        if (mEntropyBuffer >= mVisCost && !this.mHardHammer) {
            this.mHardHammer = true;
            mEntropyBuffer -= mVisCost;
        }

        // Passive Buffer 1 Centi-Vis every 2.5 seconds
        if (aTick % 50 == 0) {

            if (mAirBuffer > 0) mAirBuffer -= mVisPassiveDrain;
            if (mEarthBuffer > 0) mEarthBuffer -= mVisPassiveDrain;
            if (mFireBuffer > 0) mFireBuffer -= mVisPassiveDrain;
            if (mWaterBuffer > 0) mWaterBuffer -= mVisPassiveDrain;
            if (mOrderBuffer > 0) mOrderBuffer -= mVisPassiveDrain;
            if (mEntropyBuffer > 0) mEntropyBuffer -= mVisPassiveDrain;
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
