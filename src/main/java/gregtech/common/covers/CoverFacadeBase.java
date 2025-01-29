package gregtech.common.covers;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.cleanroommc.modularui.api.drawable.IKey;
import com.cleanroommc.modularui.utils.Alignment;
import com.cleanroommc.modularui.value.sync.BooleanSyncValue;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.cleanroommc.modularui.widgets.ToggleButton;
import com.cleanroommc.modularui.widgets.layout.Flow;
import com.cleanroommc.modularui.widgets.layout.Grid;
import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.gui.modularui2.CoverGuiData;
import gregtech.api.gui.modularui2.GTGuiTextures;
import gregtech.api.gui.modularui2.GTWidgetThemes;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.CoverBehaviorBase;
import gregtech.api.util.GTRenderingWorld;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollowerToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public abstract class CoverFacadeBase extends CoverBehaviorBase<CoverFacadeBase.FacadeData> {

    /**
     * This is the Dummy, if there is a generic Cover without behavior
     */
    public CoverFacadeBase() {
        super(FacadeData.class);
    }

    @Override
    public boolean isSimpleCover() {
        return true;
    }

    @Override
    public FacadeData createDataObject(int aLegacyData) {
        return new FacadeData();
    }

    @Override
    public FacadeData createDataObject() {
        return new FacadeData();
    }

    @Override
    protected FacadeData onCoverScrewdriverClickImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable.mFlags = ((aCoverVariable.mFlags + 1) & 15);
        GTUtility.sendChatToPlayer(
            aPlayer,
            ((aCoverVariable.mFlags & 1) != 0 ? GTUtility.trans("128.1", "Redstone ") : "")
                + ((aCoverVariable.mFlags & 2) != 0 ? GTUtility.trans("129.1", "Energy ") : "")
                + ((aCoverVariable.mFlags & 4) != 0 ? GTUtility.trans("130.1", "Fluids ") : "")
                + ((aCoverVariable.mFlags & 8) != 0 ? GTUtility.trans("131.1", "Items ") : ""));
        return aCoverVariable;
    }

    @Override
    protected boolean letsRedstoneGoInImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 1) != 0;
    }

    @Override
    protected boolean letsRedstoneGoOutImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 1) != 0;
    }

    @Override
    protected boolean letsEnergyInImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 2) != 0;
    }

    @Override
    protected boolean letsEnergyOutImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 2) != 0;
    }

    @Override
    protected boolean letsFluidInImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 4) != 0;
    }

    @Override
    protected boolean letsFluidOutImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 4) != 0;
    }

    @Override
    protected boolean letsItemsInImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 8) != 0;
    }

    @Override
    protected boolean letsItemsOutImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 8) != 0;
    }

    @Override
    public void placeCover(ForgeDirection side, ItemStack aCover, ICoverable aTileEntity) {
        aTileEntity.setCoverIdAndDataAtSide(
            side,
            GTUtility.stackToInt(aCover),
            new FacadeData(GTUtility.copyAmount(1, aCover), 0));

        if (aTileEntity.isClientSide()) GTRenderingWorld.getInstance()
            .register(
                aTileEntity.getXCoord(),
                aTileEntity.getYCoord(),
                aTileEntity.getZCoord(),
                getTargetBlock(aCover),
                getTargetMeta(aCover));
    }

    @Override
    protected ItemStack getDropImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        return aCoverVariable.mStack;
    }

    @Override
    protected ITexture getSpecialCoverFGTextureImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        return getSpecialCoverTextureImpl(side, aCoverID, aCoverVariable, aTileEntity);
    }

    @Override
    protected ITexture getSpecialCoverTextureImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        if (GTUtility.isStackInvalid(aCoverVariable.mStack)) return Textures.BlockIcons.ERROR_RENDERING[0];
        Block block = getTargetBlock(aCoverVariable.mStack);
        if (block == null) return Textures.BlockIcons.ERROR_RENDERING[0];
        // TODO: change this when *someone* made the block render in both pass
        if (block.getRenderBlockPass() != 0) return Textures.BlockIcons.ERROR_RENDERING[0];
        return TextureFactory.builder()
            .setFromBlock(block, getTargetMeta(aCoverVariable.mStack))
            .useWorldCoord()
            .setFromSide(side)
            .build();
    }

    @Override
    protected Block getFacadeBlockImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        if (GTUtility.isStackInvalid(aCoverVariable.mStack)) return null;
        return getTargetBlock(aCoverVariable.mStack);
    }

    @Override
    protected int getFacadeMetaImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        if (GTUtility.isStackInvalid(aCoverVariable.mStack)) return 0;
        return getTargetMeta(aCoverVariable.mStack);
    }

    protected abstract Block getTargetBlock(ItemStack aFacadeStack);

    protected abstract int getTargetMeta(ItemStack aFacadeStack);

    @Override
    protected boolean isDataNeededOnClientImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected void onDataChangedImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        if (aTileEntity.isClientSide()) GTRenderingWorld.getInstance()
            .register(
                aTileEntity.getXCoord(),
                aTileEntity.getYCoord(),
                aTileEntity.getZCoord(),
                getTargetBlock(aCoverVariable.mStack),
                getTargetMeta(aCoverVariable.mStack));
    }

    @Override
    protected void onDroppedImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable, ICoverable aTileEntity) {
        if (aTileEntity.isClientSide()) {
            for (final ForgeDirection iSide : ForgeDirection.VALID_DIRECTIONS) {
                if (iSide == side) continue;
                // since we do not allow multiple type of facade per block, this check would be enough.
                if (aTileEntity.getCoverBehaviorAtSideNew(iSide) instanceof CoverFacadeBase) return;
            }
            if (aCoverVariable.mStack != null)
                // mStack == null -> cover removed before data reach client
                GTRenderingWorld.getInstance()
                    .unregister(
                        aTileEntity.getXCoord(),
                        aTileEntity.getYCoord(),
                        aTileEntity.getZCoord(),
                        getTargetBlock(aCoverVariable.mStack),
                        getTargetMeta(aCoverVariable.mStack));
        }
    }

    @Override
    protected boolean onCoverRightClickImpl(ForgeDirection side, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        // in case cover data didn't hit client somehow. maybe he had a ridiculous view distance
        aTileEntity.issueCoverUpdate(side);
        return super.onCoverRightClickImpl(side, aCoverID, aCoverVariable, aTileEntity, aPlayer, aX, aY, aZ);
    }

    @Override
    public boolean isCoverPlaceable(ForgeDirection side, ItemStack aStack, ICoverable aTileEntity) {
        // blocks that are not rendered in pass 0 are now accepted but rendered awkwardly
        // to render it correctly require changing GT_Block_Machine to render in both pass, which is not really a good
        // idea...
        if (!super.isCoverPlaceable(side, aStack, aTileEntity)) return false;
        final Block targetBlock = getTargetBlock(aStack);
        if (targetBlock == null) return false;
        // we allow one single type of facade on the same block for now
        // otherwise it's not clear which block this block should impersonate
        // this restriction can be lifted later by specifying a certain facade as dominate one as an extension to this
        // class
        for (final ForgeDirection iSide : ForgeDirection.VALID_DIRECTIONS) {
            if (iSide == side) continue;
            final CoverInfo coverInfo = aTileEntity.getCoverInfoAtSide(iSide);
            if (!coverInfo.isValid()) continue;
            final Block facadeBlock = coverInfo.getFacadeBlock();
            if (facadeBlock == null) continue;
            if (facadeBlock != targetBlock) return false;
            if (coverInfo.getFacadeMeta() != getTargetMeta(aStack)) return false;
        }
        return true;
    }

    public static class FacadeData implements ISerializableObject {

        ItemStack mStack;
        int mFlags;

        public FacadeData() {}

        public FacadeData(ItemStack mStack, int mFlags) {
            this.mStack = mStack;
            this.mFlags = mFlags;
        }

        @Nonnull
        @Override
        public ISerializableObject copy() {
            return new FacadeData(mStack, mFlags);
        }

        @Nonnull
        @Override
        public NBTBase saveDataToNBT() {
            final NBTTagCompound tag = new NBTTagCompound();
            if (mStack != null) tag.setTag("mStack", mStack.writeToNBT(new NBTTagCompound()));
            tag.setByte("mFlags", (byte) mFlags);
            return tag;
        }

        @Override
        public void writeToByteBuf(ByteBuf aBuf) {
            ByteBufUtils.writeItemStack(aBuf, mStack);
            aBuf.writeByte(mFlags);
        }

        @Override
        public void loadDataFromNBT(NBTBase aNBT) {
            final NBTTagCompound tag = (NBTTagCompound) aNBT;
            mStack = ItemStack.loadItemStackFromNBT(tag.getCompoundTag("mStack"));
            mFlags = tag.getByte("mFlags");
        }

        @Nonnull
        @Override
        public ISerializableObject readFromPacket(ByteArrayDataInput aBuf, EntityPlayerMP aPlayer) {
            mStack = ISerializableObject.readItemStackFromGreggyByteBuf(aBuf);
            mFlags = aBuf.readByte();
            return this;
        }
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    protected String getGuiId() {
        return "cover.facade";
    }

    @Override
    public void addUIWidgets(CoverGuiData guiData, PanelSyncManager syncManager, Flow column) {
        column.child(
            new Grid().marginLeft(WIDGET_MARGIN)
                .coverChildren()
                .minElementMarginRight(WIDGET_MARGIN)
                .minElementMarginBottom(2)
                .minElementMarginTop(0)
                .minElementMarginLeft(0)
                .alignment(Alignment.CenterLeft)
                .row(
                    new ToggleButton().value(
                        new BooleanSyncValue(() -> getRedstonePass(guiData), value -> setRedstonePass(value, guiData)))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.str(GTUtility.trans("128", "Redstone"))
                        .asWidget())
                .row(
                    new ToggleButton()
                        .value(
                            new BooleanSyncValue(() -> getEnergyPass(guiData), value -> setEnergyPass(value, guiData)))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.str(GTUtility.trans("129", "Energy"))
                        .asWidget())
                .row(
                    new ToggleButton()
                        .value(new BooleanSyncValue(() -> getFluidPass(guiData), value -> setFluidPass(value, guiData)))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.str(GTUtility.trans("130", "Fluids"))
                        .asWidget())
                .row(
                    new ToggleButton()
                        .value(new BooleanSyncValue(() -> getItemPass(guiData), value -> setItemPass(value, guiData)))
                        .overlay(true, GTGuiTextures.OVERLAY_BUTTON_CHECKMARK)
                        .overlay(false, GTGuiTextures.OVERLAY_BUTTON_CROSS)
                        .size(16),
                    IKey.str(GTUtility.trans("131", "Items"))
                        .asWidget()));
    }

    @Override
    protected void addTitleToUI(CoverGuiData guiData, Flow column) {
        ItemStack coverItem = getCoverData(guiData).mStack;
        if (coverItem == null) return;
        column.child(
            Flow.row()
                .coverChildren()
                .marginBottom(4)
                .child(new com.cleanroommc.modularui.drawable.ItemDrawable(coverItem).asWidget())
                .child(
                    new com.cleanroommc.modularui.widgets.TextWidget(coverItem.getDisplayName()).marginLeft(4)
                        .widgetTheme(GTWidgetThemes.TITLE_TEXT)));
    }

    private boolean getRedstonePass(CoverGuiData guiData) {
        FacadeData coverData = getCoverData(guiData);
        return (coverData.mFlags & 0x1) > 0;
    }

    private void setRedstonePass(boolean redstonePass, CoverGuiData guiData) {
        FacadeData coverData = getCoverData(guiData);
        boolean wasEnabled = getRedstonePass(guiData);
        if (redstonePass == wasEnabled) return;

        if (redstonePass) {
            coverData.mFlags |= 0x1;
        } else {
            coverData.mFlags &= ~0x1;
        }
        guiData.setCoverData(coverData);
    }

    private boolean getEnergyPass(CoverGuiData guiData) {
        FacadeData coverData = getCoverData(guiData);
        return (coverData.mFlags & 0x2) > 0;
    }

    private void setEnergyPass(boolean energyPass, CoverGuiData guiData) {
        FacadeData coverData = getCoverData(guiData);
        boolean wasEnabled = getEnergyPass(guiData);
        if (energyPass == wasEnabled) return;

        if (energyPass) {
            coverData.mFlags |= 0x2;
        } else {
            coverData.mFlags &= ~0x2;
        }
        guiData.setCoverData(coverData);
    }

    private boolean getFluidPass(CoverGuiData guiData) {
        FacadeData coverData = getCoverData(guiData);
        return (coverData.mFlags & 0x4) > 0;
    }

    private void setFluidPass(boolean fluidPass, CoverGuiData guiData) {
        FacadeData coverData = getCoverData(guiData);
        boolean wasEnabled = getFluidPass(guiData);
        if (fluidPass == wasEnabled) return;

        if (fluidPass) {
            coverData.mFlags |= 0x4;
        } else {
            coverData.mFlags &= ~0x4;
        }
        guiData.setCoverData(coverData);
    }

    private boolean getItemPass(CoverGuiData guiData) {
        FacadeData coverData = getCoverData(guiData);
        return (coverData.mFlags & 0x8) > 0;
    }

    private void setItemPass(boolean itemPass, CoverGuiData guiData) {
        FacadeData coverData = getCoverData(guiData);
        boolean wasEnabled = getItemPass(guiData);
        if (itemPass == wasEnabled) return;

        if (itemPass) {
            coverData.mFlags |= 0x8;
        } else {
            coverData.mFlags &= ~0x8;
        }
        guiData.setCoverData(coverData);
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new FacadeBaseUIFactory(buildContext).createWindow();
    }

    private class FacadeBaseUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public FacadeBaseUIFactory(CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder.widget(
                new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                    this::getCoverData,
                    this::setCoverData,
                    CoverFacadeBase.this,
                    this::isEnabled,
                    (id, coverData) -> {
                        coverData.mFlags = getNewCoverVariable(id, coverData);
                        return coverData;
                    }).addToggleButton(
                        0,
                        CoverDataFollowerToggleButtonWidget.ofCheckAndCross(),
                        widget -> widget.setPos(spaceX * 0, spaceY * 0))
                        .addToggleButton(
                            1,
                            CoverDataFollowerToggleButtonWidget.ofCheckAndCross(),
                            widget -> widget.setPos(spaceX * 0, spaceY * 1))
                        .addToggleButton(
                            2,
                            CoverDataFollowerToggleButtonWidget.ofCheckAndCross(),
                            widget -> widget.setPos(spaceX * 0, spaceY * 2))
                        .addToggleButton(
                            3,
                            CoverDataFollowerToggleButtonWidget.ofCheckAndCross(),
                            widget -> widget.setPos(spaceX * 0, spaceY * 3))
                        .setPos(startX, startY))
                .widget(
                    new ItemDrawable(() -> getCoverData() != null ? getCoverData().mStack : null).asWidget()
                        .setPos(5, 5)
                        .setSize(16, 16))
                .widget(
                    TextWidget.dynamicString(() -> getCoverData() != null ? getCoverData().mStack.getDisplayName() : "")
                        .setSynced(false)
                        .setDefaultColor(COLOR_TITLE.get())
                        .setPos(25, 9))
                .widget(
                    new TextWidget(GTUtility.trans("128", "Redstone")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GTUtility.trans("129", "Energy")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GTUtility.trans("130", "Fluids")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 2))
                .widget(
                    new TextWidget(GTUtility.trans("131", "Items")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 3));
        }

        @Override
        protected void addTitleToUI(ModularWindow.Builder builder) {}

        private int getNewCoverVariable(int id, FacadeData coverVariable) {
            return switch (id) {
                case 0 -> coverVariable.mFlags ^ 0x1;
                case 1 -> coverVariable.mFlags ^ 0x2;
                case 2 -> coverVariable.mFlags ^ 0x4;
                case 3 -> coverVariable.mFlags ^ 0x8;
                default -> coverVariable.mFlags;
            };
        }

        private boolean isEnabled(int id, FacadeData coverVariable) {
            return switch (id) {
                case 0 -> (coverVariable.mFlags & 0x1) > 0;
                case 1 -> (coverVariable.mFlags & 0x2) > 0;
                case 2 -> (coverVariable.mFlags & 0x4) > 0;
                case 3 -> (coverVariable.mFlags & 0x8) > 0;
                default -> false;
            };
        }
    }
}
