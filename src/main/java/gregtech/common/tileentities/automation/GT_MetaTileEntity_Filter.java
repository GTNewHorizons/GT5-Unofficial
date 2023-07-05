package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_FILTER;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_FILTER_GLOW;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Buffer;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Utility;

public class GT_MetaTileEntity_Filter extends GT_MetaTileEntity_Buffer {

    public boolean bIgnoreNBT = false;
    public boolean bInvertFilter = false;

    public GT_MetaTileEntity_Filter(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            19,
            new String[] { "Filters up to 9 different Items", "Use Screwdriver to regulate output stack size",
                "Does not consume energy to move Item" });
    }

    public GT_MetaTileEntity_Filter(String aName, int aTier, int aInvSlotCount, String aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public GT_MetaTileEntity_Filter(String aName, int aTier, int aInvSlotCount, String[] aDescription,
        ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Filter(
            this.mName,
            this.mTier,
            this.mInventory.length,
            this.mDescriptionArray,
            this.mTextures);
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
    public boolean isValidSlot(int aIndex) {
        return aIndex < 9;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("bInvertFilter", this.bInvertFilter);
        aNBT.setBoolean("bIgnoreNBT", this.bIgnoreNBT);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.bInvertFilter = aNBT.getBoolean("bInvertFilter");
        this.bIgnoreNBT = aNBT.getBoolean("bIgnoreNBT");
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (!super.allowPutStack(aBaseMetaTileEntity, aIndex, side, aStack)) {
            return false;
        }
        if (this.bInvertFilter) {
            for (byte i = 9; i < 18; i = (byte) (i + 1)) {
                if (GT_Utility.areStacksEqual(this.mInventory[i], aStack, this.bIgnoreNBT)) {
                    return false;
                }
            }
            return true;
        }
        return GT_Utility.areStacksEqual(this.mInventory[(aIndex + 9)], aStack, this.bIgnoreNBT);
    }

    @Override
    protected void handleRedstoneOutput(IGregTechTileEntity aBaseMetaTileEntity) {
        if (bRedstoneIfFull) {
            int emptySlots = 0;
            for (int i = 0; i < 9; i++) {
                if (mInventory[i] == null) ++emptySlots;
            }
            if (!bInvert) emptySlots = 9 - emptySlots;
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                aBaseMetaTileEntity.setInternalOutputRedstoneSignal(side, (byte) emptySlots);
            }
        } else {
            for (final ForgeDirection side : ForgeDirection.VALID_DIRECTIONS) {
                aBaseMetaTileEntity.setInternalOutputRedstoneSignal(side, (byte) 0);
            }
        }
    }

    // @Override
    // public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
    // addEmitEnergyButton(builder);
    // addEmitRedstoneButton(builder);
    // addInvertRedstoneButton(builder);
    // builder.widget(new ButtonWidget().setOnClick((clickData, widget) -> {
    // bInvertFilter = !bInvertFilter;
    // if (bInvertFilter) {
    // GT_Utility.sendChatToPlayer(
    // widget.getContext()
    // .getPlayer(),
    // GT_Utility.trans("124", "Invert Filter"));
    // } else {
    // GT_Utility.sendChatToPlayer(
    // widget.getContext()
    // .getPlayer(),
    // GT_Utility.trans("125", "Don't invert Filter"));
    // }
    // })
    // .setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_INVERT_FILTER)
    // .setPos(61, 62)
    // .setSize(18, 18))
    // .widget(new ButtonWidget().setOnClick((clickData, widget) -> {
    // bIgnoreNBT = !bIgnoreNBT;
    // if (bIgnoreNBT) {
    // GT_Utility.sendChatToPlayer(
    // widget.getContext()
    // .getPlayer(),
    // GT_Utility.trans("126", "Ignore NBT"));
    // } else {
    // GT_Utility.sendChatToPlayer(
    // widget.getContext()
    // .getPlayer(),
    // GT_Utility.trans("127", "NBT has to match"));
    // }
    // })
    // .setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_NBT)
    // .setPos(79, 62)
    // .setSize(18, 18))
    // .widget(
    // new DrawableWidget().setDrawable(GT_UITextures.PICTURE_ARROW_24_WHITE.apply(9, false))
    // .setPos(6, 19)
    // .setSize(9, 24))
    // .widget(
    // new DrawableWidget().setDrawable(GT_UITextures.PICTURE_ARROW_24_BLUE.apply(24, true))
    // .setPos(71, 19)
    // .setSize(24, 24))
    // .widget(
    // new DrawableWidget().setDrawable(GT_UITextures.PICTURE_ARROW_24_RED.apply(19, true))
    // .setPos(152, 19)
    // .setSize(19, 24))
    // .widget(
    // new DrawableWidget().setDrawable(GT_UITextures.PICTURE_SLOTS_HOLO_3BY3)
    // .setPos(16, 4)
    // .setSize(54, 54))
    // .widget(
    // SlotGroup.ofItemHandler(inventoryHandler, 3)
    // .startFromSlot(9)
    // .endAtSlot(17)
    // .phantom(true)
    // .applyForWidget(
    // widget -> widget.disableShiftInsert()
    // .setBackground(GT_UITextures.TRANSPARENT))
    // .build()
    // .setPos(16, 4))
    // .widget(
    // SlotGroup.ofItemHandler(inventoryHandler, 3)
    // .startFromSlot(0)
    // .endAtSlot(8)
    // .build()
    // .setPos(97, 4));
    // }
}
