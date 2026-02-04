package gtPlusPlus.xmod.gregtech.common.tileentities.storage;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.ArrayUtils;

import com.gtnewhorizons.modularui.api.NumberFormatMUI;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;
import com.gtnewhorizons.modularui.common.widget.TextWidget;

import gregtech.api.enums.Textures.BlockIcons;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTETieredMachineBlock;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTUtility;
import gtPlusPlus.core.lib.GTPPCore;

@Deprecated
// GT++ was adding super chests too for some reason when GT already has some
// this is deprecated but not deleted because it is used for the MTEInfiniteItemHolder
public class MTETieredChest extends MTETieredMachineBlock implements IAddUIWidgets {

    public int mItemCount = 0;
    public ItemStack mItemStack = null;
    private static final double mStorageFactor = (270000.0D / 16);

    public MTETieredChest(Args args) {
        super(
            args.toBuilder()
                .inventorySlotCount(3)
                .descriptionArray(
                    new String[] {
                        "This Chest stores " + (int) (GTUtility.powInt(6.0D, args.getTier()) * mStorageFactor)
                            + " Items" })
                .build());
    }

    @Deprecated
    public MTETieredChest(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            3,
            "This Chest stores " + (int) (GTUtility.powInt(6.0D, aTier) * mStorageFactor) + " Items");
    }

    @Deprecated
    public MTETieredChest(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 3, aDescription, aTextures);
    }

    @Override
    public String[] getDescription() {
        return ArrayUtils.add(this.mDescriptionArray, GTPPCore.GT_Tooltip.get());
    }

    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTETieredChest(getPrototype());
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (this.getBaseMetaTileEntity()
            .isServerSide()
            && this.getBaseMetaTileEntity()
                .isAllowedToWork()) {
            if (this.getItemCount() <= 0) {
                this.mItemStack = null;
                this.mItemCount = 0;
            }

            if (this.mItemStack == null && this.mInventory[0] != null) {
                this.mItemStack = this.mInventory[0].copy();
            }

            if (this.mInventory[0] != null && this.mItemCount < this.getMaxItemCount()
                && GTUtility.areStacksEqual(this.mInventory[0], this.mItemStack)) {
                this.mItemCount += this.mInventory[0].stackSize;
                if (this.mItemCount > this.getMaxItemCount()) {
                    this.mInventory[0].stackSize = this.mItemCount - this.getMaxItemCount();
                    this.mItemCount = this.getMaxItemCount();
                } else {
                    this.mInventory[0] = null;
                }
            }

            if (this.mInventory[1] == null && this.mItemStack != null) {
                this.mInventory[1] = this.mItemStack.copy();
                this.mInventory[1].stackSize = Math.min(this.mItemStack.getMaxStackSize(), this.mItemCount);
                this.mItemCount -= this.mInventory[1].stackSize;
            } else if (this.mItemCount > 0 && GTUtility.areStacksEqual(this.mInventory[1], this.mItemStack)
                && this.mInventory[1].getMaxStackSize() > this.mInventory[1].stackSize) {
                    int tmp = Math
                        .min(this.mItemCount, this.mInventory[1].getMaxStackSize() - this.mInventory[1].stackSize);
                    this.mInventory[1].stackSize += tmp;
                    this.mItemCount -= tmp;
                }

            if (this.mItemStack != null) {
                this.mInventory[2] = this.mItemStack.copy();
                this.mInventory[2].stackSize = Math.min(this.mItemStack.getMaxStackSize(), this.mItemCount);
            } else {
                this.mInventory[2] = null;
            }
        }
    }

    private int getItemCount() {
        return this.mItemCount;
    }

    @Override
    public void setItemCount(int aCount) {
        this.mItemCount = aCount;
    }

    @Override
    public int getProgresstime() {
        return this.mItemCount + (this.mInventory[0] == null ? 0 : this.mInventory[0].stackSize)
            + (this.mInventory[1] == null ? 0 : this.mInventory[1].stackSize);
    }

    @Override
    public int maxProgresstime() {
        return this.getMaxItemCount();
    }

    @Override
    public int getMaxItemCount() {
        return (int) (GTUtility.powInt(6.0D, this.mTier) * mStorageFactor - 128.0D);
    }

    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return aIndex == 1;
    }

    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return aIndex == 0 && (this.mInventory[0] == null || GTUtility.areStacksEqual(this.mInventory[0], aStack));
    }

    @Override
    public String[] getInfoData() {
        return this.mItemStack == null
            ? new String[] { StatCollector.translateToLocalFormatted("gtpp.infodata.tiered_chest.name"),
                StatCollector.translateToLocal("GT5U.infodata.digital_chest.stored_items"),
                StatCollector.translateToLocal("GT5U.infodata.digital_chest.stored_items.empty"), Integer.toString(0),
                Integer.toString(this.getMaxItemCount()) }
            : new String[] { StatCollector.translateToLocal("gtpp.infodata.tiered_chest.name"),
                StatCollector.translateToLocal("GT5U.infodata.digital_chest.stored_items"),
                this.mItemStack.getDisplayName(), Integer.toString(this.mItemCount),
                Integer.toString(this.getMaxItemCount()) };
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        aNBT.setInteger("mItemCount", this.mItemCount);
        if (this.mItemStack != null) {
            aNBT.setTag("mItemStack", this.mItemStack.writeToNBT(new NBTTagCompound()));
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("mItemCount")) {
            this.mItemCount = aNBT.getInteger("mItemCount");
        }

        if (aNBT.hasKey("mItemStack")) {
            this.mItemStack = ItemStack.loadItemStackFromNBT((NBTTagCompound) aNBT.getTag("mItemStack"));
        }
    }

    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int aColorIndex, boolean aActive, boolean aRedstone) {
        return aBaseMetaTileEntity.getFrontFacing() == ForgeDirection.DOWN && side == ForgeDirection.WEST
            ? new ITexture[] { BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1],
                TextureFactory.of(BlockIcons.OVERLAY_QCHEST) }
            : (side == aBaseMetaTileEntity.getFrontFacing()
                ? new ITexture[] { BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1],
                    TextureFactory.of(BlockIcons.OVERLAY_QCHEST) }
                : new ITexture[] { BlockIcons.MACHINE_CASINGS[this.mTier][aColorIndex + 1] });
    }

    @Override
    public ITexture[][][] getTextureSet(ITexture[] aTextures) {
        return new ITexture[0][0][0];
    }

    protected static final NumberFormatMUI numberFormat = new NumberFormatMUI();

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
            new DrawableWidget().setDrawable(GTUITextures.PICTURE_SCREEN_BLACK)
                .setPos(7, 16)
                .setSize(71, 45))
            .widget(
                new SlotWidget(inventoryHandler, 0)
                    .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_IN)
                    .setPos(79, 16))
            .widget(
                new SlotWidget(inventoryHandler, 1).setAccess(true, false)
                    .setBackground(getGUITextureSet().getItemSlot(), GTUITextures.OVERLAY_SLOT_OUT)
                    .setPos(79, 52))
            .widget(
                SlotWidget.phantom(inventoryHandler, 2)
                    .disableInteraction()
                    .setBackground(GTUITextures.TRANSPARENT)
                    .setPos(59, 42))
            .widget(
                new TextWidget(StatCollector.translateToLocal("GT5U.gui.text.item_amount"))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 20))
            .widget(
                new TextWidget().setStringSupplier(() -> numberFormat.format(mItemCount))
                    .setDefaultColor(COLOR_TEXT_WHITE.get())
                    .setPos(10, 30));
    }
}
