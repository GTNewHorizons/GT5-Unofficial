package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.GTValues.AuthorFourIsTheNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_EMS_HOUSING;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_EMS_HOUSING_GLOW;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.gui.modularui.GTUIInfos;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.common.tileentities.machines.multi.MTEIndustrialElectromagneticSeparator;

public class MTEHatchMagnet extends MTEHatch {

    public MTEHatchMagnet(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional, 5, 1, "Holds electromagnet for the Magnetic Flux Exhibitor");
    }

    public MTEHatchMagnet(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription[0], aTextures);
    }

    @Override
    public boolean isSimpleMachine() {
        return true;
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.addAll(this.mDescriptionArray, AuthorFourIsTheNumber);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new SlotWidget(inventoryHandler, 0).setFilter(MTEIndustrialElectromagneticSeparator::isValidElectromagnet)
                .setAccess(true, true)
                .setPos(79, 34));
    }

    @Override
    public boolean isAccessAllowed(EntityPlayer aPlayer) {
        return true;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        GTUIInfos.openGTTileEntityUI(aBaseMetaTileEntity, aPlayer);
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
}
