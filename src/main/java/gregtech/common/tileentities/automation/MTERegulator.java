package gregtech.common.tileentities.automation;

import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_REGULATOR;
import static gregtech.api.enums.Textures.BlockIcons.AUTOMATION_REGULATOR_GLOW;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.item.ItemStackPredicate;

import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBuffer;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTItemTransfer;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.singleblock.MTERegulatorGui;

public class MTERegulator extends MTEBuffer {

    public static final int FILTER_SLOT_INDEX = 9;
    private static final int NUM_FILTER_SLOTS = 9;

    private final int[] mTargetSlots = { 0, 0, 0, 0, 0, 0, 0, 0, 0 };
    private boolean charge = false, decharge = false;

    public MTERegulator(int aID, String aName, String aNameRegional, int aTier) {
        super(
            aID,
            aName,
            aNameRegional,
            aTier,
            20,
            new String[] { "Filters up to 9 different Items", "Allows Item-specific output stack size",
                "Allows Item-specific output slot" });
    }

    public MTERegulator(String aName, int aTier, int aInvSlotCount, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aInvSlotCount, aDescription, aTextures);
    }

    public int getTargetSlot(int index) {
        return mTargetSlots[index];
    }

    public void setTargetSlots(int targetSlot, int index) {
        mTargetSlots[index] = targetSlot;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTERegulator(this.mName, this.mTier, this.mInventory.length, this.mDescriptionArray, this.mTextures);
    }

    @Override
    public ITexture getOverlayIcon() {
        return TextureFactory.of(
            TextureFactory.of(AUTOMATION_REGULATOR),
            TextureFactory.builder()
                .addIcon(AUTOMATION_REGULATOR_GLOW)
                .glow()
                .build());
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex < 9 || aIndex == rechargerSlotStartIndex();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("mTargetSlot1", this.mTargetSlots[0]);
        aNBT.setInteger("mTargetSlot2", this.mTargetSlots[1]);
        aNBT.setInteger("mTargetSlot3", this.mTargetSlots[2]);
        aNBT.setInteger("mTargetSlot4", this.mTargetSlots[3]);
        aNBT.setInteger("mTargetSlot5", this.mTargetSlots[4]);
        aNBT.setInteger("mTargetSlot6", this.mTargetSlots[5]);
        aNBT.setInteger("mTargetSlot7", this.mTargetSlots[6]);
        aNBT.setInteger("mTargetSlot8", this.mTargetSlots[7]);
        aNBT.setInteger("mTargetSlot9", this.mTargetSlots[8]);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.mTargetSlots[0] = aNBT.getInteger("mTargetSlot1");
        this.mTargetSlots[1] = aNBT.getInteger("mTargetSlot2");
        this.mTargetSlots[2] = aNBT.getInteger("mTargetSlot3");
        this.mTargetSlots[3] = aNBT.getInteger("mTargetSlot4");
        this.mTargetSlots[4] = aNBT.getInteger("mTargetSlot5");
        this.mTargetSlots[5] = aNBT.getInteger("mTargetSlot6");
        this.mTargetSlots[6] = aNBT.getInteger("mTargetSlot7");
        this.mTargetSlots[7] = aNBT.getInteger("mTargetSlot8");
        this.mTargetSlots[8] = aNBT.getInteger("mTargetSlot9");
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        // Regulation per Screwdriver is overridden by GUI regulation.
    }

    @Override
    public void moveItems(IGregTechTileEntity igte, long aTimer) {
        GTItemTransfer transfer = new GTItemTransfer();
        transfer.push(igte, igte.getBackFacing());
        transfer.setSourceSlots(0, 1, 2, 3, 4, 5, 6, 7, 8);

        var targetTE = igte.getTileEntityAtSide(igte.getBackFacing());

        for (int i = 0; i < 9; i++) {
            ItemStack filterStack = this.mInventory[i + 9];
            if (filterStack == null) continue;

            int targetSlot = this.mTargetSlots[i];
            int targetSize = filterStack.stackSize;
            int remaining = getRemainingTargetCapacity(targetTE, targetSlot, targetSize, filterStack);
            if (remaining <= 0) continue;

            transfer.setSinkSlots(targetSlot);
            transfer.setMaxSinkSlotStackSize(targetSize);
            transfer.setMaxItemsPerTransfer(remaining);
            transfer.setMaxTotalTransferred(remaining);

            final int needed = remaining;
            transfer.setFilter(
                ItemStackPredicate.matches(filterStack)
                    .and(stack -> stack.getStackSize() >= needed));

            if (transfer.transfer() > 0) {
                igte.markInventoryBeenModified();
                this.mSuccess = 50;
                break;
            }
        }
    }

    private static int getRemainingTargetCapacity(Object targetTE, int targetSlot, int targetSize,
        ItemStack filterStack) {
        if (!(targetTE instanceof IInventory targetInv)) return targetSize;
        if (targetSlot < 0 || targetSlot >= targetInv.getSizeInventory()) return targetSize;

        ItemStack existing = targetInv.getStackInSlot(targetSlot);
        int maxStackSize = existing != null ? existing.getMaxStackSize() : filterStack.getMaxStackSize();
        int maxTargetSize = Math.min(targetSize, Math.min(maxStackSize, targetInv.getInventoryStackLimit()));
        int currentInTarget = existing != null ? existing.stackSize : 0;
        return maxTargetSize - currentInTarget;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return super.allowPutStack(aBaseMetaTileEntity, aIndex, side, aStack) && aIndex >= 0
            && aIndex <= 8
            && GTUtility.areStacksEqual(aStack, this.mInventory[(aIndex + 9)]);
    }

    @Override
    public int rechargerSlotStartIndex() {
        return 19;
    }

    @Override
    public int dechargerSlotStartIndex() {
        return 19;
    }

    @Override
    public int rechargerSlotCount() {
        return charge ? 1 : 0;
    }

    @Override
    public int dechargerSlotCount() {
        return decharge ? 1 : 0;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide()) {
            charge = aBaseMetaTileEntity.getStoredEU() / 2 > aBaseMetaTileEntity.getEUCapacity() / 3;
            decharge = aBaseMetaTileEntity.getStoredEU() < aBaseMetaTileEntity.getEUCapacity() / 3;
        }
    }

    @Override
    public ModularPanel buildUI(PosGuiData guiData, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTERegulatorGui(this).build(guiData, syncManager, uiSettings);
    }

    @Override
    public boolean isItemValidForPhantomSlot(int index, ItemStack itemStack) {
        return FILTER_SLOT_INDEX <= index && index < FILTER_SLOT_INDEX + NUM_FILTER_SLOTS;
    }
}
