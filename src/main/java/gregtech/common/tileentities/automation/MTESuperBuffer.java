package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_SUPERBUFFER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_SUPERBUFFER_GLOW;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;

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
    protected void addMainUI(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SUPER_BUFFER)
                .setPos(61, 4)
                .setSize(54, 54));
    }
}
