package gregtech.common.covers;

import static gregtech.api.enums.GT_Values.ALL_VALID_SIDES;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.drawable.ItemDrawable;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.GT_CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_CoverBehaviorBase;
import gregtech.api.util.GT_RenderingWorld;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.ISerializableObject;
import gregtech.common.gui.modularui.widget.CoverDataControllerWidget;
import gregtech.common.gui.modularui.widget.CoverDataFollower_ToggleButtonWidget;
import io.netty.buffer.ByteBuf;

public abstract class GT_Cover_FacadeBase extends GT_CoverBehaviorBase<GT_Cover_FacadeBase.FacadeData> {

    /**
     * This is the Dummy, if there is a generic Cover without behavior
     */
    public GT_Cover_FacadeBase() {
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
    protected FacadeData onCoverScrewdriverClickImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        aCoverVariable.mFlags = ((aCoverVariable.mFlags + 1) & 15);
        GT_Utility.sendChatToPlayer(
            aPlayer,
            ((aCoverVariable.mFlags & 1) != 0 ? GT_Utility.trans("128.1", "Redstone ") : "")
                + ((aCoverVariable.mFlags & 2) != 0 ? GT_Utility.trans("129.1", "Energy ") : "")
                + ((aCoverVariable.mFlags & 4) != 0 ? GT_Utility.trans("130.1", "Fluids ") : "")
                + ((aCoverVariable.mFlags & 8) != 0 ? GT_Utility.trans("131.1", "Items ") : ""));
        return aCoverVariable;
    }

    @Override
    protected boolean letsRedstoneGoInImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 1) != 0;
    }

    @Override
    protected boolean letsRedstoneGoOutImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 1) != 0;
    }

    @Override
    protected boolean letsEnergyInImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 2) != 0;
    }

    @Override
    protected boolean letsEnergyOutImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 2) != 0;
    }

    @Override
    protected boolean letsFluidInImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 4) != 0;
    }

    @Override
    protected boolean letsFluidOutImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable, Fluid aFluid,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 4) != 0;
    }

    @Override
    protected boolean letsItemsInImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 8) != 0;
    }

    @Override
    protected boolean letsItemsOutImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable, int aSlot,
        ICoverable aTileEntity) {
        return (aCoverVariable.mFlags & 8) != 0;
    }

    @Override
    public void placeCover(ForgeDirection aSide, ItemStack aCover, ICoverable aTileEntity) {
        aTileEntity.setCoverIdAndDataAtSide(
            aSide,
            GT_Utility.stackToInt(aCover),
            new FacadeData(GT_Utility.copyAmount(1, aCover), 0));

        if (aTileEntity.isClientSide()) GT_RenderingWorld.getInstance()
            .register(
                aTileEntity.getXCoord(),
                aTileEntity.getYCoord(),
                aTileEntity.getZCoord(),
                getTargetBlock(aCover),
                getTargetMeta(aCover));
    }

    @Override
    protected ItemStack getDropImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        return aCoverVariable.mStack;
    }

    @Override
    protected ITexture getSpecialCoverFGTextureImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        return getSpecialCoverTextureImpl(aSide, aCoverID, aCoverVariable, aTileEntity);
    }

    @Override
    protected ITexture getSpecialCoverTextureImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        if (GT_Utility.isStackInvalid(aCoverVariable.mStack)) return Textures.BlockIcons.ERROR_RENDERING[0];
        Block block = getTargetBlock(aCoverVariable.mStack);
        if (block == null) return Textures.BlockIcons.ERROR_RENDERING[0];
        // TODO: change this when *someone* made the block render in both pass
        if (block.getRenderBlockPass() != 0) return Textures.BlockIcons.ERROR_RENDERING[0];
        return TextureFactory.builder()
            .setFromBlock(block, getTargetMeta(aCoverVariable.mStack))
            .useWorldCoord()
            .setFromSide(ForgeDirection.getOrientation(aSide))
            .build();
    }

    @Override
    protected Block getFacadeBlockImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        if (GT_Utility.isStackInvalid(aCoverVariable.mStack)) return null;
        return getTargetBlock(aCoverVariable.mStack);
    }

    @Override
    protected int getFacadeMetaImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        if (GT_Utility.isStackInvalid(aCoverVariable.mStack)) return 0;
        return getTargetMeta(aCoverVariable.mStack);
    }

    protected abstract Block getTargetBlock(ItemStack aFacadeStack);

    protected abstract int getTargetMeta(ItemStack aFacadeStack);

    @Override
    protected boolean isDataNeededOnClientImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        return true;
    }

    @Override
    protected void onDataChangedImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        if (aTileEntity.isClientSide()) GT_RenderingWorld.getInstance()
            .register(
                aTileEntity.getXCoord(),
                aTileEntity.getYCoord(),
                aTileEntity.getZCoord(),
                getTargetBlock(aCoverVariable.mStack),
                getTargetMeta(aCoverVariable.mStack));
    }

    @Override
    protected void onDroppedImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity) {
        if (aTileEntity.isClientSide()) {
            for (byte i : ALL_VALID_SIDES) {
                if (i == aSide) continue;
                // since we do not allow multiple type of facade per block, this check would be enough.
                if (aTileEntity.getCoverBehaviorAtSideNew(i) instanceof GT_Cover_FacadeBase) return;
            }
            if (aCoverVariable.mStack != null)
                // mStack == null -> cover removed before data reach client
                GT_RenderingWorld.getInstance()
                    .unregister(
                        aTileEntity.getXCoord(),
                        aTileEntity.getYCoord(),
                        aTileEntity.getZCoord(),
                        getTargetBlock(aCoverVariable.mStack),
                        getTargetMeta(aCoverVariable.mStack));
        }
    }

    @Override
    protected boolean onCoverRightClickImpl(ForgeDirection aSide, int aCoverID, FacadeData aCoverVariable,
        ICoverable aTileEntity, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        // in case cover data didn't hit client somehow. maybe he had a ridiculous view distance
        aTileEntity.issueCoverUpdate(aSide);
        return super.onCoverRightClickImpl(aSide, aCoverID, aCoverVariable, aTileEntity, aPlayer, aX, aY, aZ);
    }

    @Override
    public boolean isCoverPlaceable(ForgeDirection aSide, ItemStack aStack, ICoverable aTileEntity) {
        // blocks that are not rendered in pass 0 are now accepted but rendered awkwardly
        // to render it correctly require changing GT_Block_Machine to render in both pass, which is not really a good
        // idea...
        if (!super.isCoverPlaceable(aSide, aStack, aTileEntity)) return false;
        final Block targetBlock = getTargetBlock(aStack);
        if (targetBlock == null) return false;
        // we allow one single type of facade on the same block for now
        // otherwise it's not clear which block this block should impersonate
        // this restriction can be lifted later by specifying a certain facade as dominate one as an extension to this
        // class
        for (byte i : ALL_VALID_SIDES) {
            if (i == aSide) continue;
            final CoverInfo coverInfo = aTileEntity.getCoverInfoAtSide(i);
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
    public boolean useModularUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(GT_CoverUIBuildContext buildContext) {
        return new FacadeBaseUIFactory(buildContext).createWindow();
    }

    private class FacadeBaseUIFactory extends UIFactory {

        private static final int startX = 10;
        private static final int startY = 25;
        private static final int spaceX = 18;
        private static final int spaceY = 18;

        public FacadeBaseUIFactory(GT_CoverUIBuildContext buildContext) {
            super(buildContext);
        }

        @SuppressWarnings("PointlessArithmeticExpression")
        @Override
        protected void addUIWidgets(ModularWindow.Builder builder) {
            builder.widget(
                new CoverDataControllerWidget.CoverDataIndexedControllerWidget_ToggleButtons<>(
                    this::getCoverData,
                    this::setCoverData,
                    GT_Cover_FacadeBase.this,
                    this::isEnabled,
                    (id, coverData) -> {
                        coverData.mFlags = getNewCoverVariable(id, coverData);
                        return coverData;
                    }).addToggleButton(
                        0,
                        CoverDataFollower_ToggleButtonWidget.ofCheckAndCross(),
                        widget -> widget.setPos(spaceX * 0, spaceY * 0))
                        .addToggleButton(
                            1,
                            CoverDataFollower_ToggleButtonWidget.ofCheckAndCross(),
                            widget -> widget.setPos(spaceX * 0, spaceY * 1))
                        .addToggleButton(
                            2,
                            CoverDataFollower_ToggleButtonWidget.ofCheckAndCross(),
                            widget -> widget.setPos(spaceX * 0, spaceY * 2))
                        .addToggleButton(
                            3,
                            CoverDataFollower_ToggleButtonWidget.ofCheckAndCross(),
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
                    new TextWidget(GT_Utility.trans("128", "Redstone")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 0))
                .widget(
                    new TextWidget(GT_Utility.trans("129", "Energy")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 1))
                .widget(
                    new TextWidget(GT_Utility.trans("130", "Fluids")).setDefaultColor(COLOR_TEXT_GRAY.get())
                        .setPos(3 + startX + spaceX * 1, 4 + startY + spaceY * 2))
                .widget(
                    new TextWidget(GT_Utility.trans("131", "Items")).setDefaultColor(COLOR_TEXT_GRAY.get())
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
