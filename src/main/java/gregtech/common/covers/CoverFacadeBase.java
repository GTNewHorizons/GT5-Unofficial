package gregtech.common.covers;

import java.util.Objects;

import javax.annotation.Nonnull;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
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
import gregtech.api.covers.CoverContext;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.render.TextureFactory;
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
    public CoverFacadeBase(CoverContext context) {
        super(context, FacadeData.class, null);
    }

    @Override
    protected FacadeData initializeData() {
        return new CoverFacadeBase.FacadeData();
    }

    @Override
    public FacadeData loadFromItemStack(ItemStack cover) {
        if (Objects.isNull(cover)) return initializeData();
        return new CoverFacadeBase.FacadeData(GTUtility.copyAmount(1, cover), 0);
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        coverData.mFlags = ((coverData.mFlags + 1) & 15);
        GTUtility.sendChatToPlayer(
            aPlayer,
            ((coverData.mFlags & 1) != 0 ? GTUtility.trans("128.1", "Redstone ") : "")
                + ((coverData.mFlags & 2) != 0 ? GTUtility.trans("129.1", "Energy ") : "")
                + ((coverData.mFlags & 4) != 0 ? GTUtility.trans("130.1", "Fluids ") : "")
                + ((coverData.mFlags & 8) != 0 ? GTUtility.trans("131.1", "Items ") : ""));
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return (coverData.mFlags & 1) != 0;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return (coverData.mFlags & 1) != 0;
    }

    @Override
    public boolean letsEnergyIn() {
        return (coverData.mFlags & 2) != 0;
    }

    @Override
    public boolean letsEnergyOut() {
        return (coverData.mFlags & 2) != 0;
    }

    @Override
    public boolean letsFluidIn(Fluid fluid) {
        return (coverData.mFlags & 4) != 0;
    }

    @Override
    public boolean letsFluidOut(Fluid fluid) {
        return (coverData.mFlags & 4) != 0;
    }

    @Override
    public boolean letsItemsIn(int slot) {
        return (coverData.mFlags & 8) != 0;
    }

    @Override
    public boolean letsItemsOut(int slot) {
        return (coverData.mFlags & 8) != 0;
    }

    @Override
    public void onPlayerAttach(EntityPlayer player, ItemStack coverItem) {
        ICoverable coverable = coveredTile.get();
        if (coverable != null && coverable.isClientSide()) GTRenderingWorld.getInstance()
            .register(
                coverable.getXCoord(),
                coverable.getYCoord(),
                coverable.getZCoord(),
                getTargetBlock(coverItem),
                getTargetMeta(coverItem));
    }

    @Override
    public ItemStack asItemStack() {
        return coverData.mStack;
    }

    @Override
    public ITexture getOverlayTexture() {
        return getSpecialFaceTexture();
    }

    @Override
    public ITexture getSpecialFaceTexture() {
        if (GTUtility.isStackInvalid(coverData.mStack)) return Textures.BlockIcons.ERROR_RENDERING[0];
        Block block = getTargetBlock(coverData.mStack);
        if (block == null) return Textures.BlockIcons.ERROR_RENDERING[0];
        // TODO: change this when *someone* made the block render in both pass
        if (block.getRenderBlockPass() != 0) return Textures.BlockIcons.ERROR_RENDERING[0];
        return TextureFactory.builder()
            .setFromBlock(block, getTargetMeta(coverData.mStack))
            .useWorldCoord()
            .setFromSide(coverSide)
            .build();
    }

    @Override
    public Block getFacadeBlock() {
        if (GTUtility.isStackInvalid(coverData.mStack)) return null;
        return getTargetBlock(coverData.mStack);
    }

    @Override
    public int getFacadeMeta() {
        if (GTUtility.isStackInvalid(coverData.mStack)) return 0;
        return getTargetMeta(coverData.mStack);
    }

    protected abstract Block getTargetBlock(ItemStack aFacadeStack);

    protected abstract int getTargetMeta(ItemStack aFacadeStack);

    @Override
    public boolean isDataNeededOnClient() {
        return true;
    }

    @Override
    public void onDataChanged() {
        ICoverable coverable = coveredTile.get();
        if (coverable != null && coverable.isClientSide()) GTRenderingWorld.getInstance()
            .register(
                coverable.getXCoord(),
                coverable.getYCoord(),
                coverable.getZCoord(),
                getTargetBlock(coverData.mStack),
                getTargetMeta(coverData.mStack));
    }

    @Override
    public void onCoverRemoval() {
        ICoverable coverable = coveredTile.get();
        if (coverable != null && coverable.isClientSide()) {
            for (final ForgeDirection iSide : ForgeDirection.VALID_DIRECTIONS) {
                if (iSide == coverSide) continue;
                // since we do not allow multiple type of facade per block, this check would be enough.
                if (coverable.getCoverAtSide(iSide) instanceof CoverFacadeBase) return;
            }
            if (coverData.mStack != null)
                // mStack == null -> cover removed before data reach client
                GTRenderingWorld.getInstance()
                    .unregister(
                        coverable.getXCoord(),
                        coverable.getYCoord(),
                        coverable.getZCoord(),
                        getTargetBlock(coverData.mStack),
                        getTargetMeta(coverData.mStack));
        }
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        // in case cover data didn't hit client somehow. maybe he had a ridiculous view distance
        ICoverable coverable = coveredTile.get();
        if (coverable != null) coverable.issueCoverUpdate(coverSide);
        return false;
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

        @Override
        public void readFromPacket(ByteArrayDataInput aBuf) {
            mStack = ISerializableObject.readItemStackFromGreggyByteBuf(aBuf);
            mFlags = aBuf.readByte();
        }
    }

    // GUI stuff

    @Override
    public boolean hasCoverGUI() {
        return true;
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
                    CoverFacadeBase.this::loadFromNbt,
                    this::isEnabled,
                    (id, coverData) -> {
                        coverData.mFlags = getNewCoverVariable(id, coverData);
                        return coverData;
                    },
                    getUIBuildContext())
                        .addToggleButton(
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
