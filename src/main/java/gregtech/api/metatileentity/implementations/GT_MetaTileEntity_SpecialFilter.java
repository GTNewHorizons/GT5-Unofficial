package gregtech.api.metatileentity.implementations;

import java.util.Collections;
import java.util.List;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;

import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GT_Utility;

public abstract class GT_MetaTileEntity_SpecialFilter extends GT_MetaTileEntity_Buffer implements IAddUIWidgets {

    public static final int BUFFER_SLOT_COUNT = 9;
    public static final int SPECIAL_SLOT_INDEX = 9;
    public boolean bNBTAllowed = false;
    public boolean bInvertFilter = false;

    public GT_MetaTileEntity_SpecialFilter(int aID, String aName, String aNameRegional, int aTier,
        String[] aDescription) {
        // 9 buffer slot, 1 representation slot, 1 holo slot. last seems not needed...
        super(aID, aName, aNameRegional, aTier, 11, aDescription);
    }

    public GT_MetaTileEntity_SpecialFilter(String aName, int aTier, int aInvSlotCount, String aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_SpecialFilter(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < 9;
    }

    public abstract void clickTypeIcon(boolean aRightClick, ItemStack aHandStack);

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("bInvertFilter", this.bInvertFilter);
        aNBT.setBoolean("bNBTAllowed", this.bNBTAllowed);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.bInvertFilter = aNBT.getBoolean("bInvertFilter");
        this.bNBTAllowed = aNBT.getBoolean("bNBTAllowed");
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection aSide,
        ItemStack aStack) {
        return (super.allowPutStack(aBaseMetaTileEntity, aIndex, aSide, aStack))
            && ((this.bNBTAllowed) || (!aStack.hasTagCompound()))
            && (this.isStackAllowed(aStack) != this.bInvertFilter);
    }

    protected abstract boolean isStackAllowed(ItemStack aStack);

    protected List<Text> getEmptySlotTooltip() {
        return null;
    }

    protected List<String> getItemExtraTooltip() {
        return null;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        addEmitEnergyButton(builder);
        addEmitRedstoneButton(builder);
        addInvertRedstoneButton(builder);
        builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
            bInvertFilter = !bInvertFilter;
            if (bInvertFilter) {
                GT_Utility.sendChatToPlayer(
                    widget.getContext()
                        .getPlayer(),
                    GT_Utility.trans("124", "Invert Filter"));
            } else {
                GT_Utility.sendChatToPlayer(
                    widget.getContext()
                        .getPlayer(),
                    GT_Utility.trans("125", "Don't invert Filter"));
            }
        })
            .setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_INVERT_FILTER)
            .setPos(61, 62)
            .setSize(18, 18))
            .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
                bNBTAllowed = !bNBTAllowed;
                if (bNBTAllowed) {
                    GT_Utility.sendChatToPlayer(
                        widget.getContext()
                            .getPlayer(),
                        GT_Utility.trans("126", "Ignore NBT"));
                } else {
                    GT_Utility.sendChatToPlayer(
                        widget.getContext()
                            .getPlayer(),
                        GT_Utility.trans("127", "NBT has to match"));
                }
            })
                .setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_NBT)
                .setPos(79, 62)
                .setSize(18, 18))
            .widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_ARROW_24_WHITE.apply(27, false))
                    .setPos(6, 19)
                    .setSize(27, 24))
            .widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_ARROW_24_BLUE.apply(42, true))
                    .setPos(53, 19)
                    .setSize(42, 24))
            .widget(
                new DrawableWidget().setDrawable(GT_UITextures.PICTURE_ARROW_24_RED.apply(19, true))
                    .setPos(152, 19)
                    .setSize(19, 24))
            .widget(new SlotWidget(BaseSlot.phantom(inventoryHandler, 9)) {

                @Override
                protected void phantomClick(ClickData clickData, ItemStack cursorStack) {
                    clickTypeIcon(clickData.mouseButton != 0, cursorStack);
                }

                @Override
                public void buildTooltip(List<Text> tooltip) {
                    if (getEmptySlotTooltip() != null) {
                        tooltip.addAll(getEmptySlotTooltip());
                    }
                    super.buildTooltip(tooltip);
                }

                @Override
                public List<String> getExtraTooltip() {
                    if (getItemExtraTooltip() != null) {
                        return getItemExtraTooltip();
                    }
                    return Collections.emptyList();
                }
            }.disableShiftInsert()
                .setPos(34, 22)
                .setBackground(GT_UITextures.BUTTON_STANDARD))
            .widget(
                SlotGroup.ofItemHandler(inventoryHandler, 3)
                    .endAtSlot(8)
                    .build()
                    .setPos(97, 4));
    }
}
