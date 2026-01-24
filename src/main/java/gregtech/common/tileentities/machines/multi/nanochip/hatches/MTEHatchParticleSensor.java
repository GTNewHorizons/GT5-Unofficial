package gregtech.common.tileentities.machines.multi.nanochip.hatches;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.render.TextureFactory;

public class MTEHatchParticleSensor extends MTEHatch {

    private int targetParticle = 1;

    private static final IIconContainer textureFont = Textures.BlockIcons.OVERLAY_HATCH_PH_SENSOR;
    private static final IIconContainer textureFont_Glow = Textures.BlockIcons.OVERLAY_HATCH_PH_SENSOR_GLOW;

    public MTEHatchParticleSensor(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Indicates required particle.");
    }

    public MTEHatchParticleSensor(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return false;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean allowGeneralRedstoneOutput() {
        return true;
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection Side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return false;
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        getBaseMetaTileEntity().setActive(true);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Can be installed in the " + EnumChatFormatting.GREEN + "Ultra-high Energy Etching Array",
            "Redstone signal strength varies based on requested particle",
            EnumChatFormatting.RED + "12"
                + EnumChatFormatting.GRAY
                + " strength => "
                + EnumChatFormatting.WHITE
                + "Positron",
            EnumChatFormatting.RED + "10"
                + EnumChatFormatting.GRAY
                + " strength => "
                + EnumChatFormatting.WHITE
                + "Alpha",
            EnumChatFormatting.RED + "2"
                + EnumChatFormatting.GRAY
                + " strength => "
                + EnumChatFormatting.WHITE
                + "Electron" };
    }

    public void updateRedstoneOutput(int requiredParticle) {
        targetParticle = ((requiredParticle + 1) * 2);
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            aBaseMetaTileEntity.setInternalOutputRedstoneSignal(side, (byte) targetParticle);
        }
        super.onPostTick(aBaseMetaTileEntity, aTick);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchParticleSensor(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(textureFont), TextureFactory.builder()
            .addIcon(textureFont_Glow)
            .glow()
            .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(textureFont) };
    }
}
