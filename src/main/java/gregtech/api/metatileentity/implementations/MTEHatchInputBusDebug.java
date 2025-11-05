package gregtech.api.metatileentity.implementations;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.MTEHatchInputBusDebugGui;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;

import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.BOLD;
import static com.gtnewhorizon.gtnhlib.util.AnimatedTooltipHandler.GREEN;

public class MTEHatchInputBusDebug extends MTEHatchInputBus{

    public MTEHatchInputBusDebug(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier);
    }
    public MTEHatchInputBusDebug(String aName, byte aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aDescription, aTextures);
    }


    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_ENERGY_IN_DEBUG) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(Textures.BlockIcons.OVERLAY_ENERGY_IN_DEBUG) };
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchInputBusDebug(mName, mTier, new String[] { "" }, mTextures);
    }


    @Override
    public String[] getDescription() {
        return new String[] { EnumChatFormatting.GRAY + "Stocks Items internally",
            EnumChatFormatting.GRAY + "Configure Supplied Items in the UI",
            EnumChatFormatting.ITALIC + "Who knew it was this easy???",
            "Author: " + GREEN + BOLD + "Chrom" };
    }

    @Override
    public boolean canExtractItem(int index, ItemStack itemStack, int ordinalSide) {
        return false;
    }

    @Override
    public boolean canInsertItem(int index, ItemStack itemStack, int ordinalSide) {
        return false;
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchInputBusDebugGui(this).build(data, syncManager, uiSettings);
    }
}
