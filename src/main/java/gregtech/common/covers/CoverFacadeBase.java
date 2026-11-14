package gregtech.common.covers;

import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.Fluid;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import com.google.common.io.ByteArrayDataInput;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;

import cpw.mods.fml.common.network.ByteBufUtils;
import gregtech.api.covers.CoverContext;
import gregtech.api.covers.CoverPlacementPredicate;
import gregtech.api.enums.Textures;
import gregtech.api.gui.modularui.CoverUIBuildContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTByteBuffer;
import gregtech.api.util.GTRenderingWorld;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.cover.base.CoverBaseGui;
import gregtech.common.gui.modularui.cover.base.CoverFacadeBaseGui;
import gregtech.common.gui.mui1.cover.FacadeBaseUIFactory;
import io.netty.buffer.ByteBuf;

public abstract class CoverFacadeBase extends Cover {

    private static final int REDSTONE_PASS_FLAG = 0x1;
    private static final int ENERGY_PASS_FLAG = 0x2;
    private static final int FLUID_PASS_FLAG = 0x4;
    private static final int ITEM_PASS_FLAG = 0x8;

    public static CoverPlacementPredicate isCoverPlaceable(Function<ItemStack, Block> getTargetBlock) {
        return (side, coverItem, coverable) -> getTargetBlock.apply(coverItem) != null;
    }

    ItemStack mStack;
    int mFlags;

    /**
     * This is the Dummy, if there is a generic Cover without behavior
     */
    public CoverFacadeBase(CoverContext context) {
        super(context, null);
        ItemStack coverItem = context.getCoverItem();
        mStack = copyItemStackIfPresent(coverItem);
        mFlags = 0;
    }

    private static @Nullable ItemStack copyItemStackIfPresent(ItemStack coverItem) {
        if (coverItem == null) return null;
        return GTUtility.copyAmount(1, coverItem);
    }

    public ItemStack getStack() {
        return this.mStack;
    }

    public CoverFacadeBase setStack(ItemStack stack) {
        this.mStack = stack;
        ICoverable coverable = coveredTile.get();
        if (coverable != null && coverable.isClientSide()) {
            GTRenderingWorld.register(
                coverable.getXCoord(),
                coverable.getYCoord(),
                coverable.getZCoord(),
                getTargetBlock(mStack),
                getTargetMeta(mStack));
        }
        return this;
    }

    public int getFlags() {
        return this.mFlags;
    }

    public CoverFacadeBase setFlags(int flags) {
        this.mFlags = flags;
        return this;
    }

    public boolean getRedstonePass() {
        return (getFlags() & REDSTONE_PASS_FLAG) > 0;
    }

    public void setRedstonePass(boolean redstonePass) {
        int flags = getFlags();
        if (redstonePass) {
            flags |= REDSTONE_PASS_FLAG;
        } else {
            flags &= ~REDSTONE_PASS_FLAG;
        }
        setFlags(flags);
    }

    public boolean getEnergyPass() {
        return (getFlags() & ENERGY_PASS_FLAG) > 0;
    }

    public void setEnergyPass(boolean energyPass) {
        int flags = getFlags();
        if (energyPass) {
            flags |= ENERGY_PASS_FLAG;
        } else {
            flags &= ~ENERGY_PASS_FLAG;
        }
        setFlags(flags);
    }

    public boolean getFluidPass() {
        return (getFlags() & FLUID_PASS_FLAG) > 0;
    }

    public void setFluidPass(boolean fluidPass) {
        int flags = getFlags();
        if (fluidPass) {
            flags |= FLUID_PASS_FLAG;
        } else {
            flags &= ~FLUID_PASS_FLAG;
        }
        setFlags(flags);
    }

    public boolean getItemPass() {
        return (getFlags() & ITEM_PASS_FLAG) > 0;
    }

    public void setItemPass(boolean itemPass) {
        int flags = getFlags();
        if (itemPass) {
            flags |= ITEM_PASS_FLAG;
        } else {
            flags &= ~ITEM_PASS_FLAG;
        }
        setFlags(flags);
    }

    @Override
    protected void readDataFromNbt(NBTBase nbt) {
        final NBTTagCompound tag = (NBTTagCompound) nbt;
        setStack(ItemStack.loadItemStackFromNBT(tag.getCompoundTag("mStack")));
        setFlags(tag.getByte("mFlags"));
    }

    @Override
    public void readDataFromPacket(ByteArrayDataInput byteData) {
        setFlags(byteData.readByte());
        setStack(GTByteBuffer.readItemStackFromGreggyByteBuf(byteData));
    }

    @Override
    protected @NotNull NBTBase saveDataToNbt() {
        final NBTTagCompound tag = new NBTTagCompound();
        if (mStack != null) tag.setTag("mStack", mStack.writeToNBT(new NBTTagCompound()));
        tag.setByte("mFlags", (byte) mFlags);
        return tag;
    }

    @Override
    protected void writeDataToByteBuf(ByteBuf byteBuf) {
        byteBuf.writeByte(mFlags);
        ByteBufUtils.writeItemStack(byteBuf, mStack);
    }

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        mFlags = ((mFlags + 1) & 15);
        IChatComponent message = new ChatComponentText("");
        if ((mFlags & REDSTONE_PASS_FLAG) != 0) {
            message.appendSibling(new ChatComponentTranslation("GT5U.chat.cover.decorative.redstone"))
                .appendText(" ");
        }
        if ((mFlags & ENERGY_PASS_FLAG) != 0) {
            message.appendSibling(new ChatComponentTranslation("GT5U.chat.cover.decorative.energy"))
                .appendText(" ");
        }
        if ((mFlags & FLUID_PASS_FLAG) != 0) {
            message.appendSibling(new ChatComponentTranslation("GT5U.chat.cover.decorative.fluids"))
                .appendText(" ");
        }
        if ((mFlags & ITEM_PASS_FLAG) != 0) {
            message.appendSibling(new ChatComponentTranslation("GT5U.chat.cover.decorative.items"))
                .appendText(" ");
        }
        GTUtility.sendChatComp(aPlayer, message);
    }

    @Override
    public boolean letsRedstoneGoIn() {
        return (mFlags & REDSTONE_PASS_FLAG) != 0;
    }

    @Override
    public boolean letsRedstoneGoOut() {
        return (mFlags & REDSTONE_PASS_FLAG) != 0;
    }

    @Override
    public boolean letsEnergyIn() {
        return (mFlags & ENERGY_PASS_FLAG) != 0;
    }

    @Override
    public boolean letsEnergyOut() {
        return (mFlags & ENERGY_PASS_FLAG) != 0;
    }

    @Override
    public boolean letsFluidIn(Fluid fluid) {
        return (mFlags & FLUID_PASS_FLAG) != 0;
    }

    @Override
    public boolean letsFluidOut(Fluid fluid) {
        return (mFlags & FLUID_PASS_FLAG) != 0;
    }

    @Override
    public boolean letsItemsIn(int slot) {
        return (mFlags & ITEM_PASS_FLAG) != 0;
    }

    @Override
    public boolean letsItemsOut(int slot) {
        return (mFlags & ITEM_PASS_FLAG) != 0;
    }

    @Override
    public void onPlayerAttach(EntityPlayer player, ItemStack coverItem) {
        ICoverable coverable = coveredTile.get();
        if (coverable != null && coverable.isClientSide()) GTRenderingWorld.register(
            coverable.getXCoord(),
            coverable.getYCoord(),
            coverable.getZCoord(),
            getTargetBlock(coverItem),
            getTargetMeta(coverItem));
    }

    @Override
    public ItemStack asItemStack() {
        return mStack;
    }

    @Override
    public ITexture getOverlayTexture() {
        return getSpecialFaceTexture();
    }

    @Override
    public ITexture getSpecialFaceTexture() {
        if (GTUtility.isStackInvalid(mStack)) return Textures.BlockIcons.ERROR_RENDERING[0];
        Block block = getTargetBlock(mStack);
        if (block == null) return Textures.BlockIcons.ERROR_RENDERING[0];
        return TextureFactory.blockBuilder()
            .setFromBlock(block, getTargetMeta(mStack))
            .useWorldCoord()
            .setFromSide(coverSide)
            .build();
    }

    @Override
    public Block getFacadeBlock() {
        if (GTUtility.isStackInvalid(mStack)) return null;
        return getTargetBlock(mStack);
    }

    @Override
    public int getFacadeMeta() {
        if (GTUtility.isStackInvalid(mStack)) return 0;
        return getTargetMeta(mStack);
    }

    protected abstract Block getTargetBlock(ItemStack aFacadeStack);

    protected abstract int getTargetMeta(ItemStack aFacadeStack);

    @Override
    public boolean isDataNeededOnClient() {
        return true;
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
            if (mStack != null)
                // mStack == null -> cover removed before data reach client
                GTRenderingWorld.unregister(
                    coverable.getXCoord(),
                    coverable.getYCoord(),
                    coverable.getZCoord(),
                    getTargetBlock(mStack),
                    getTargetMeta(mStack));
        }
    }

    @Override
    public boolean onCoverRightClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        // in case cover data didn't hit client somehow. maybe he had a ridiculous view distance
        ICoverable coverable = coveredTile.get();
        if (coverable != null) coverable.issueCoverUpdate(coverSide);
        return false;
    }

    // GUI stuff

    @Override
    protected @NotNull CoverBaseGui<?> getCoverGui() {
        return new CoverFacadeBaseGui(this);
    }

    @Override
    public boolean hasCoverGUI() {
        return true;
    }

    @Override
    public ModularWindow createWindow(CoverUIBuildContext buildContext) {
        return new FacadeBaseUIFactory(buildContext).createWindow();
    }

}
