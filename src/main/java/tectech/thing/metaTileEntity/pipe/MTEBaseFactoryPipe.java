package tectech.thing.metaTileEntity.pipe;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import tectech.mechanics.pipe.IActivePipe;
import tectech.mechanics.pipe.PipeActivity;

public abstract class MTEBaseFactoryPipe extends MetaPipeEntity implements IActivePipe {

    public static final IIconContainer EM_PIPE = Textures.BlockIcons.custom("iconsets/EM_DATA");
    public static final IIconContainer EM_BAR = Textures.BlockIcons.custom("iconsets/EM_BAR");
    public static final IIconContainer EM_BAR_ACTIVE = Textures.BlockIcons.custom("iconsets/EM_BAR_ACTIVE");

    protected boolean mIsActive;

    protected float mThickness = 0.5f;

    public MTEBaseFactoryPipe(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 0);
    }

    protected MTEBaseFactoryPipe(MTEBaseFactoryPipe prototype) {
        super(prototype.mName, 0);
        mThickness = prototype.mThickness;
    }

    @Override
    public abstract IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity);

    @Override
    public ITexture[] getTexture(IGregTechTileEntity base, ForgeDirection side, int aConnections, int colorIndex,
        boolean aConnected, boolean aRedstone) {

        List<ITexture> textures = new ArrayList<>(2);

        textures.add(
            TextureFactory.builder()
                .addIcon(EM_PIPE)
                .setRGBA(Dyes.getModulation(colorIndex, MACHINE_METAL.getRGBA()))
                .build());

        if (getActive()) {
            textures.add(
                TextureFactory.builder()
                    .addIcon(EM_BAR)
                    .setRGBA(Dyes.getModulation(colorIndex, MACHINE_METAL.getRGBA()))
                    .build());
        }

        return textures.toArray(new ITexture[0]);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity iGregTechTileEntity, int i, ForgeDirection side,
        ItemStack itemStack) {
        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound nbtTagCompound) {
        setActive(nbtTagCompound.getBoolean("eActive"));
        mConnections = nbtTagCompound.getByte("mConnections");
    }

    @Override
    public void saveNBTData(NBTTagCompound nbtTagCompound) {
        nbtTagCompound.setBoolean("eActive", getActive());
        nbtTagCompound.setByte("mConnections", mConnections);
    }

    @Override
    public boolean renderInside(ForgeDirection side) {
        return false;
    }

    @Override
    public byte getTileEntityBaseType() {
        return HarvestTool.WrenchPipeLevel0.toTileEntityBaseType();
    }

    @Override
    public String[] getDescription() {
        return GTValues.emptyStringArray;
    }

    @Override
    public float getCollisionThickness() {
        return mThickness;
    }

    @Override
    public void markUsed() {
        setActive(true);
    }

    @Override
    public void setActive(boolean state) {
        if (state != mIsActive) {
            mIsActive = state;
            getBaseMetaTileEntity().issueTextureUpdate();
        }
    }

    @Override
    public boolean getActive() {
        return mIsActive;
    }

    private boolean prevActivity;

    @Override
    public void onFirstTick(IGregTechTileEntity base) {
        super.onFirstTick(base);
        onPostTick(base, 31);
    }

    @Override
    public void onPostTick(IGregTechTileEntity base, long aTick) {
        super.onPostTick(base, aTick);

        if (base.isServerSide()) {
            if (mCheckConnections && base.isServerSide()) {
                mCheckConnections = false;
                checkConnections();
            }

            if (aTick % SECONDS == 0) {
                checkActive();

                boolean isActive = getActive();

                if (isActive != prevActivity || aTick % (60 * SECONDS) == 0) {
                    prevActivity = isActive;

                    PipeActivity
                        .enqueueUpdate(base.getWorld(), base.getXCoord(), base.getYCoord(), base.getZCoord(), isActive);
                }
            }
        } else {
            if (GTMod.clientProxy()
                .changeDetected() == 4) {
                base.issueTextureUpdate();
            }
        }
    }

    protected void checkActive() {
        mIsActive = false;
    }

    @Override
    protected void checkConnections() {

    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] {
            getActive() ? EnumChatFormatting.GREEN + StatCollector.translateToLocal("tt.infodata.pipe.active")
                : EnumChatFormatting.RED + StatCollector.translateToLocal("tt.infodata.pipe.inactive") };
    }
}
