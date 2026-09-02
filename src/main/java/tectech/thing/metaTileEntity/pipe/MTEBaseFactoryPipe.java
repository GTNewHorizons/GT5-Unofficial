package tectech.thing.metaTileEntity.pipe;

import static gregtech.api.enums.Dyes.MACHINE_METAL;
import static gregtech.api.util.GTRecipeBuilder.SECONDS;

import java.util.ArrayList;
import java.util.List;

import gregtech.api.enums.Mods;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Dyes;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HarvestTool;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.render.TextureFactory;
import io.netty.buffer.ByteBuf;

public abstract class MTEBaseFactoryPipe extends MetaPipeEntity {

    public static final IIconContainer EM_PIPE = Textures.BlockIcons.custom(Mods.GregTech.resourceDomain, "iconsets/EM_DATA");
    public static final IIconContainer EM_BAR = Textures.BlockIcons.custom(Mods.GregTech.resourceDomain, "iconsets/EM_BAR");
    public static final IIconContainer EM_BAR_ACTIVE = Textures.BlockIcons.custom(Mods.GregTech.resourceDomain, "iconsets/EM_BAR_ACTIVE");

    private boolean mIsActive;

    protected float mThickness = 0.5f;

    public MTEBaseFactoryPipe(int aID, String aName) {
        super(aID, aName, 0);
    }

    protected MTEBaseFactoryPipe(MTEBaseFactoryPipe prototype) {
        super(prototype.mName, 0);
        mThickness = prototype.mThickness;
    }

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
        mIsActive = nbtTagCompound.getBoolean("eActive");
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

    public boolean getActive() {
        return mIsActive;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity base) {
        super.onFirstTick(base);
        onPostTick(base, 31);
    }

    @Override
    public boolean needsClientTick() {
        return false;
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

                boolean isActive = checkActive();
                if (mIsActive != isActive) {
                    mIsActive = isActive;
                    base.issueTileUpdate();
                }
            }
        }
    }

    protected boolean checkActive() {
        return false;
    }

    @Override
    protected boolean deferCheckConnection() {
        return false;
    }

    @Override
    protected void checkConnections() {
        mCheckConnections = false;
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public String[] getInfoData() {
        return new String[] { getActive() ? IGregTechDeviceInformation.encode("tt.infodata.pipe.active")
            : IGregTechDeviceInformation.encode("tt.infodata.pipe.inactive") };
    }

    @Override
    public void writeToStream(ByteBuf buffer) {
        super.writeToStream(buffer);
        buffer.writeBoolean(mIsActive);
    }

    @Override
    public void readFromStream(ByteBuf buffer) {
        super.readFromStream(buffer);
        mIsActive = buffer.readBoolean();
    }
}
