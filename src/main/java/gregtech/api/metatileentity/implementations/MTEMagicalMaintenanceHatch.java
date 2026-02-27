package gregtech.api.metatileentity.implementations;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.visnet.VisNetHandler;

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
        return new String[] { translateToLocal("gt.blockmachines.magical.maintenance.desc.0"),
            translateToLocal("gt.blockmachines.magical.maintenance.desc.1"),
            translateToLocal("gt.blockmachines.magical.maintenance.desc.2"),
            translateToLocal("gt.blockmachines.magical.maintenance.desc.3") };
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
        if (aBaseMetaTileEntity.isServerSide()) {
            preformMaintenance();
        }
    }

    private void preformMaintenance() {
        // Internal Buffer, Soft Caps at 50 vis
        mAirBuffer = fillIfBelowCap(mAirBuffer, Aspect.AIR);
        mEarthBuffer = fillIfBelowCap(mEarthBuffer, Aspect.EARTH);
        mFireBuffer = fillIfBelowCap(mFireBuffer, Aspect.FIRE);
        mWaterBuffer = fillIfBelowCap(mWaterBuffer, Aspect.WATER);
        mOrderBuffer = fillIfBelowCap(mOrderBuffer, Aspect.ORDER);
        mEntropyBuffer = fillIfBelowCap(mEntropyBuffer, Aspect.ENTROPY);

        // Repair if needed + Drain
        boolean shouldRepair = !this.mWrench || !this.mScrewdriver
            || !this.mSolderingTool
            || !this.mCrowbar
            || !this.mSoftMallet
            || !this.mHardHammer;

        boolean canRepair = mAirBuffer >= mVisCost && mEarthBuffer >= mVisCost
            && mFireBuffer >= mVisCost
            && mWaterBuffer >= mVisCost
            && mOrderBuffer >= mVisCost
            && mEntropyBuffer >= mVisCost;

        if (canRepair && shouldRepair) {
            this.mWrench = this.mScrewdriver = this.mSolderingTool = this.mCrowbar = this.mSoftMallet = this.mHardHammer = true;
            mAirBuffer -= mVisCost;
            mEarthBuffer -= mVisCost;
            mFireBuffer -= mVisCost;
            mWaterBuffer -= mVisCost;
            mOrderBuffer -= mVisCost;
            mEntropyBuffer -= mVisCost;
        }
    }

    private int fillIfBelowCap(int buffer, Aspect aspect) {
        return buffer < mVisCap
            ? buffer + VisNetHandler.drainVis(getWorld(), getXCoord(), getYCoord(), getZCoord(), aspect, 1)
            : buffer;
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
