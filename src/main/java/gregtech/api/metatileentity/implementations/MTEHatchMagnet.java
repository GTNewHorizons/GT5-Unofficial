package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_EMS_HOUSING;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_EMS_HOUSING_GLOW;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.gui.modularui.hatch.MTEHatchMagnetGui;
import gregtech.common.tileentities.machines.multi.MTEIndustrialElectromagneticSeparator;

public class MTEHatchMagnet extends MTEHatch {

    public MTEHatchMagnet(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 5, 1, "Holds electromagnet for the Magnetic Flux Exhibitor");
    }

    public MTEHatchMagnet(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(OVERLAY_EMS_HOUSING)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_EMS_HOUSING_GLOW)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.builder()
            .addIcon(OVERLAY_EMS_HOUSING)
            .extFacing()
            .build(),
            TextureFactory.builder()
                .addIcon(OVERLAY_EMS_HOUSING_GLOW)
                .extFacing()
                .glow()
                .build() };
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchMagnet(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchMagnetGui(this).build(data, syncManager, uiSettings);
    }

    @Override
    public boolean isItemValidForSlot(int index, ItemStack itemStack) {
        return MTEIndustrialElectromagneticSeparator.isValidElectromagnet(itemStack)
            && super.isItemValidForSlot(index, itemStack);
    }
}
