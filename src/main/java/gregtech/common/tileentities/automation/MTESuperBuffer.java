package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_SUPERBUFFER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_SUPERBUFFER_GLOW;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.singleblock.MTESuperBufferGui;

public class MTESuperBuffer extends MTEChestBuffer {

    public MTESuperBuffer(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            257,
            new String[] { "Buffers up to 256 Item Stacks", "Use Screwdriver to regulate output stack size",
                getTickRateDesc(aTier) });
    }

    public MTESuperBuffer(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTESuperBuffer(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(
            TextureFactory.of(AUTOMATION_SUPERBUFFER),
            TextureFactory.builder()
                .addIcon(AUTOMATION_SUPERBUFFER_GLOW)
                .glow()
                .build());
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTESuperBufferGui(this).build(guiData, syncManager, uiSettings);
    }
}
