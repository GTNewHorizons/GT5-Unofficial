package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.xmod.gregtech.api.gui.GTPP_UITextures;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.machines.GregtechMetaSafeBlockBase;

public class GregtechMetaSafeBlock extends GregtechMetaSafeBlockBase implements IAddUIWidgets {

    public GregtechMetaSafeBlock(final int aID, final String aName, final String aNameRegional, final int aTier) {
        super(aID, aName, aNameRegional, aTier, 28, "Protecting your items from sticky fingers.");
    }

    public GregtechMetaSafeBlock(final String aName, final int aTier, final int aInvSlotCount,
            final String[] aDescription, final ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.add(this.mDescriptionArray, CORE.GT_Tooltip.get());
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new GregtechMetaSafeBlock(
                this.mName,
                this.mTier,
                this.mInventory.length,
                this.mDescriptionArray,
                this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return new GT_RenderedTexture(Textures.BlockIcons.VOID);
    }

    @Override
    public boolean isValidSlot(final int aIndex) {
        return aIndex < (this.mInventory.length - 1);
    }

    @Override
    public boolean useModularUI() {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(SlotGroup.ofItemHandler(inventoryHandler, 9).endAtSlot(26).build().setPos(7, 4))
                .widget(
                        new ButtonWidget().setOnClick((clickData, widget) -> bUnbreakable = !bUnbreakable)
                                .setBackground(GT_UITextures.BUTTON_STANDARD, GTPP_UITextures.OVERLAY_BUTTON_LOCK)
                                .setPos(43, 62).setSize(18, 18))
                .widget(new TextWidget("Safe Status").setPos(76, 61))
                .widget(
                        TextWidget.dynamicString(() -> bUnbreakable ? "Locked" : "Unlocked").setSynced(false)
                                .setPos(82, 73))
                .widget(new FakeSyncWidget.BooleanSyncer(() -> bUnbreakable, val -> bUnbreakable = val));
    }
}
