package gregtech.common.tileentities.machines.multi.nanochip.hatches;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;

public class MTEHatchSplitterRedstone extends MTEHatch {

    private byte redstoneInput = 0;
    private int channel = 1;

    public MTEHatchSplitterRedstone(int aID, String aName, String aNameRegional, int aTier) {
        super(aID, aName, aNameRegional, aTier, 0, "Allows Redstone Control for Spliter");
    }

    public MTEHatchSplitterRedstone(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 0, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[0];
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[0];
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchSplitterRedstone(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public String[] getDescription() {
        return new String[] { "Can be installed in a Splitter module for Nanochip Assembly Complex",
            "Allows for Redstone Input for rules control" };
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        this.setChannel((channel + 1) % 20);
        aPlayer.addChatComponentMessage(new ChatComponentText(channel + ""));
        super.onScrewdriverRightClick(side, aPlayer, aX, aY, aZ, aTool);
    }

    @Override
    public void onPostTick(IGregTechTileEntity baseMetaTileEntity, long tick) {
        for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
            this.setRedstoneInput(baseMetaTileEntity.getInputRedstoneSignal(side));
        }
        super.onPostTick(baseMetaTileEntity, tick);
    }

    public byte getRedstoneInput() {
        return redstoneInput;
    }

    public void setRedstoneInput(byte redstoneInput) {
        this.redstoneInput = redstoneInput;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

}
