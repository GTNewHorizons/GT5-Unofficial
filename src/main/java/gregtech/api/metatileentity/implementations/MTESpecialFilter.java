package gregtech.api.metatileentity.implementations;

import java.util.List;
import java.util.function.Function;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;

public abstract class MTESpecialFilter extends MTEFilterBase {

    private static final String ALLOW_NBT_TOOLTIP = "GT5U.machines.allow_nbt.tooltip";
    private boolean allowNbt = false;

    public MTESpecialFilter(int aID, String aName, String aNameRegional, int aTier, String[] aDescription) {
        // 9 buffer slot, 1 representation slot, 1 holo slot. last seems not needed...
        super(aID, aName, aNameRegional, aTier, 11, aDescription);
    }

    public MTESpecialFilter(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTESpecialFilter(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("bNBTAllowed", this.allowNbt);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.allowNbt = aNBT.getBoolean("bNBTAllowed");
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return (super.allowPutStack(aBaseMetaTileEntity, aIndex, side, aStack))
            && ((this.allowNbt) || (!aStack.hasTagCompound()))
            && (this.isStackAllowed(aStack) != this.invertFilter);
    }

    protected abstract boolean isStackAllowed(ItemStack aStack);

    protected List<Text> getEmptySlotTooltip() {
        return null;
    }

    protected Function<List<String>, List<String>> getItemStackReplacementTooltip() {
        return list -> list;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        addAllowNbtButton(builder);
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_ARROW_24_WHITE.apply(27, false))
                .setPos(6, 19)
                .setSize(27, 24))
            .widget(
                new DrawableWidget().setDrawable(GTUITextures.PICTURE_ARROW_24_BLUE.apply(42, true))
                    .setPos(53, 19)
                    .setSize(42, 24))
            .widget(
                new DrawableWidget().setDrawable(GTUITextures.PICTURE_ARROW_24_RED.apply(19, true))
                    .setPos(152, 19)
                    .setSize(19, 24))
            .widget(
                createFilterIconSlot(BaseSlot.phantom(inventoryHandler, 9)).disableShiftInsert()
                    .setPos(34, 22)
                    .setBackground(GTUITextures.BUTTON_STANDARD))
            .widget(
                SlotGroup.ofItemHandler(inventoryHandler, 3)
                    .endAtSlot(8)
                    .build()
                    .setPos(97, 4));
    }

    private void addAllowNbtButton(ModularWindow.Builder builder) {
        builder.widget(
            createToggleButton(
                () -> allowNbt,
                val -> allowNbt = val,
                GTUITextures.OVERLAY_BUTTON_NBT,
                () -> mTooltipCache.getData(ALLOW_NBT_TOOLTIP)));
    }

    protected abstract SlotWidget createFilterIconSlot(BaseSlot slot);

    protected abstract class FilterIconSlotWidget extends SlotWidget {

        public FilterIconSlotWidget(BaseSlot slot) {
            super(slot);
        }

        @Override
        protected abstract void phantomClick(ClickData clickData, ItemStack cursorStack);

        @Override
        public void buildTooltip(List<Text> tooltip) {
            super.buildTooltip(tooltip);
            List<Text> emptySlotTooltip = getEmptySlotTooltip();
            if (emptySlotTooltip != null) {
                tooltip.addAll(emptySlotTooltip);
            }
        }

        @Override
        public Function<List<String>, List<String>> getOverwriteItemStackTooltip() {
            return getItemStackReplacementTooltip();
        }
    }
}
