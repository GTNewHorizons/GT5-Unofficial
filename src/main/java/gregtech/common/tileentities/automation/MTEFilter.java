package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_FILTER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_FILTER_GLOW;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;

import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEFilterBase;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;

public class MTEFilter extends MTEFilterBase {

    private static final int NUM_FILTER_SLOTS = 9;
    private static final String IGNORE_NBT_TOOLTIP = "GT5U.machines.ignore_nbt.tooltip";
    private boolean ignoreNbt = false;

    public MTEFilter(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            19,
            new String[] { "Filters up to 9 different Items", "Use Screwdriver to regulate output stack size" });
    }

    public MTEFilter(String aName, int aTier, int aInvSlotCount, String aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public MTEFilter(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEFilter(this.mName, this.mTier, this.mInventory.length, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(
            TextureFactory.of(AUTOMATION_FILTER),
            TextureFactory.builder()
                .addIcon(AUTOMATION_FILTER_GLOW)
                .glow()
                .build());
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("bIgnoreNBT", this.ignoreNbt);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.ignoreNbt = aNBT.getBoolean("bIgnoreNBT");
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (!super.allowPutStack(aBaseMetaTileEntity, aIndex, side, aStack)) {
            return false;
        }
        if (this.invertFilter) {
            for (int i = 0; i < NUM_FILTER_SLOTS; i++) {
                if (GTUtility.areStacksEqual(this.mInventory[FILTER_SLOT_INDEX + i], aStack, this.ignoreNbt)) {
                    return false;
                }
            }
            return true;
        }
        return GTUtility.areStacksEqual(this.mInventory[(FILTER_SLOT_INDEX + aIndex)], aStack, this.ignoreNbt);
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        addAllowNbtButton(builder);
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_ARROW_24_WHITE.apply(9, false))
                .setPos(6, 19)
                .setSize(9, 24))
            .widget(
                new DrawableWidget().setDrawable(GTUITextures.PICTURE_ARROW_24_BLUE.apply(24, true))
                    .setPos(71, 19)
                    .setSize(24, 24))
            .widget(
                new DrawableWidget().setDrawable(GTUITextures.PICTURE_ARROW_24_RED.apply(19, true))
                    .setPos(152, 19)
                    .setSize(19, 24))
            .widget(
                new DrawableWidget().setDrawable(GTUITextures.PICTURE_SLOTS_HOLO_3BY3)
                    .setPos(16, 4)
                    .setSize(54, 54))
            .widget(
                SlotGroup.ofItemHandler(inventoryHandler, 3)
                    .startFromSlot(FILTER_SLOT_INDEX)
                    .endAtSlot(FILTER_SLOT_INDEX + NUM_FILTER_SLOTS - 1)
                    .phantom(true)
                    .applyForWidget(
                        widget -> widget.disableShiftInsert()
                            .setBackground(GTUITextures.TRANSPARENT))
                    .build()
                    .setPos(16, 4))
            .widget(
                SlotGroup.ofItemHandler(inventoryHandler, 3)
                    .startFromSlot(0)
                    .endAtSlot(NUM_INVENTORY_SLOTS - 1)
                    .build()
                    .setPos(97, 4));
    }

    private void addAllowNbtButton(ModularWindow.Builder builder) {
        builder.widget(
            createToggleButton(
                () -> ignoreNbt,
                val -> ignoreNbt = val,
                GTUITextures.OVERLAY_BUTTON_NBT,
                () -> mTooltipCache.getData(IGNORE_NBT_TOOLTIP)));
    }
}
