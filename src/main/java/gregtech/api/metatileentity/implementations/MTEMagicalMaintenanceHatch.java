package gregtech.api.metatileentity.implementations;

import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import thaumcraft.api.aspects.Aspect;
import thaumcraft.api.visnet.VisNetHandler;

public class MTEMagicalMaintenanceHatch extends MTEHatchMaintenance {

    private static IIconContainer face;

    private int airBuffer = 0;
    private int earthBuffer = 0;
    private int fireBuffer = 0;
    private int waterBuffer = 0;
    private int orderBuffer = 0;
    private int entropyBuffer = 0;

    private static final int centiVisCap = 5000;
    private static final int centiVisCost = 2500;

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
            translateToLocal("gt.blockmachines.magical.maintenance.desc.2") };
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister aBlockIconRegister) {
        super.registerIcons(aBlockIconRegister);
        face = Textures.BlockIcons.custom("iconsets/OVERLAY_MAGICALMAINTENANCE");
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
        nbt.setInteger("airBuffer", this.airBuffer);
        nbt.setInteger("earthBuffer", this.earthBuffer);
        nbt.setInteger("fireBuffer", this.fireBuffer);
        nbt.setInteger("waterBuffer", this.waterBuffer);
        nbt.setInteger("orderBuffer", this.orderBuffer);
        nbt.setInteger("entropyBuffer", this.entropyBuffer);

        nbt.setBoolean("wrench", this.mWrench);
        nbt.setBoolean("screwdriver", this.mScrewdriver);
        nbt.setBoolean("softMallet", this.mSoftMallet);
        nbt.setBoolean("hardHammer", this.mHardHammer);
        nbt.setBoolean("solderingTool", this.mSolderingTool);
        nbt.setBoolean("crowbar", this.mCrowbar);
    }

    @Override
    public void loadNBTData(NBTTagCompound nbt) {
        super.loadNBTData(nbt);
        this.airBuffer = nbt.getInteger("airBuffer");
        this.earthBuffer = nbt.getInteger("earthBuffer");
        this.fireBuffer = nbt.getInteger("fireBuffer");
        this.waterBuffer = nbt.getInteger("waterBuffer");
        this.orderBuffer = nbt.getInteger("orderBuffer");
        this.entropyBuffer = nbt.getInteger("entropyBuffer");

        this.mWrench = nbt.getBoolean("wrench");
        this.mScrewdriver = nbt.getBoolean("screwdriver");
        this.mSoftMallet = nbt.getBoolean("softMallet");
        this.mHardHammer = nbt.getBoolean("hardHammer");
        this.mSolderingTool = nbt.getBoolean("solderingTool");
        this.mCrowbar = nbt.getBoolean("crowbar");
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            performMaintenance();
        }
    }

    private void performMaintenance() {
        // Internal Buffer, Caps at 5000 vis
        airBuffer = fillIfBelowCap(airBuffer, Aspect.AIR);
        earthBuffer = fillIfBelowCap(earthBuffer, Aspect.EARTH);
        fireBuffer = fillIfBelowCap(fireBuffer, Aspect.FIRE);
        waterBuffer = fillIfBelowCap(waterBuffer, Aspect.WATER);
        orderBuffer = fillIfBelowCap(orderBuffer, Aspect.ORDER);
        entropyBuffer = fillIfBelowCap(entropyBuffer, Aspect.ENTROPY);

        boolean shouldRepair = !this.mWrench || !this.mScrewdriver
            || !this.mSolderingTool
            || !this.mCrowbar
            || !this.mSoftMallet
            || !this.mHardHammer;

        boolean canRepair = airBuffer >= centiVisCost && earthBuffer >= centiVisCost
            && fireBuffer >= centiVisCost
            && waterBuffer >= centiVisCost
            && orderBuffer >= centiVisCost
            && entropyBuffer >= centiVisCost;

        // Repair if needed + Drain
        if (canRepair && shouldRepair) {
            this.mWrench = this.mScrewdriver = this.mSolderingTool = this.mCrowbar = this.mSoftMallet = this.mHardHammer = true;
            airBuffer -= centiVisCost;
            earthBuffer -= centiVisCost;
            fireBuffer -= centiVisCost;
            waterBuffer -= centiVisCost;
            orderBuffer -= centiVisCost;
            entropyBuffer -= centiVisCost;
        }
    }

    private int fillIfBelowCap(int buffer, Aspect aspect) {
        if (buffer >= centiVisCap) return buffer;

        int space = centiVisCap - buffer;
        int drained = VisNetHandler.drainVis(
            getBaseMetaTileEntity().getWorld(),
            getBaseMetaTileEntity().getXCoord(),
            getBaseMetaTileEntity().getYCoord(),
            getBaseMetaTileEntity().getZCoord(),
            aspect,
            Math.min(space, 5));

        return buffer + drained;
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
}
