package gregtech.common.tileentities.machines.multi;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;

public class MTEHatchDopingControl extends MTEHatch {

    private byte outputStrength;

    private static final IIconContainer OVERLAY = Textures.BlockIcons.OVERLAY_HATCH_SPLITTER_REDSTONE;
    private static final IIconContainer OVERLAY_GLOW = Textures.BlockIcons.OVERLAY_HATCH_SPLITTER_REDSTONE_GLOW;

    public MTEHatchDopingControl(int id, String name, String regionalName, int tier) {
        super(id, name, regionalName, tier, 0, StatCollector.translateToLocal("GT5U.hatch.doping_control.description"));
    }

    public MTEHatchDopingControl(String name, int tier, String[] description, ITexture[][][] textures) {
        super(name, tier, 0, description, textures);
    }

    @Override
    public boolean isValidSlot(int index) {
        return false;
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity base, int index, ForgeDirection side, ItemStack stack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity base, int index, ForgeDirection side, ItemStack stack) {
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public void initDefaultModes(NBTTagCompound tag) {
        getBaseMetaTileEntity().setActive(true);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity tile) {
        return new MTEHatchDopingControl(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { StatCollector.translateToLocal("GT5U.hatch.doping_control.tooltip.installation"),
            StatCollector.translateToLocal("GT5U.hatch.doping_control.tooltip.output") };
    }

    @Override
    public void loadNBTData(NBTTagCompound tag) {
        outputStrength = tag.getByte("mOutputStrength");
        super.loadNBTData(tag);
    }

    @Override
    public void saveNBTData(NBTTagCompound tag) {
        tag.setByte("mOutputStrength", outputStrength);
        super.saveNBTData(tag);
    }

    public void updateOutputSignal(byte signal) {
        outputStrength = (byte) Math.clamp(signal, 0, 15);
        IGregTechTileEntity base = getBaseMetaTileEntity();
        if (base == null || !base.isServerSide()) return;
        ForgeDirection facing = base.getFrontFacing();
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            base.setOutputRedstoneSignal(side, side == facing ? outputStrength : (byte) 0);
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity base, long tick) {
        if (!base.isServerSide()) {
            super.onPostTick(base, tick);
            return;
        }
        ForgeDirection facing = base.getFrontFacing();
        for (ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            base.setOutputRedstoneSignal(side, side == facing ? outputStrength : (byte) 0);
        }
        super.onPostTick(base, tick);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture baseTexture) {
        return new ITexture[] { baseTexture, TextureFactory.of(OVERLAY), TextureFactory.builder()
            .addIcon(OVERLAY_GLOW)
            .glow()
            .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture baseTexture) {
        return new ITexture[] { baseTexture, TextureFactory.of(OVERLAY) };
    }
}
