package gregtech.common.tileentities.machines.multi.nanochip.hatches;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.network.NetworkUtils;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.modularui2.MetaTileEntityGuiHandler;
import gregtech.common.gui.modularui.hatch.MTEHatchSplitterRedstoneGui;

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
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!NetworkUtils.isClient(aPlayer)) {
            MetaTileEntityGuiHandler.open(aPlayer, this);
        }
        return true;
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
        this.setRedstoneInput(baseMetaTileEntity.getStrongestRedstone());
        System.out.println(redstoneInput);
        super.onPostTick(baseMetaTileEntity, tick);
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchSplitterRedstoneGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    protected boolean useMui2() {
        return true;
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
