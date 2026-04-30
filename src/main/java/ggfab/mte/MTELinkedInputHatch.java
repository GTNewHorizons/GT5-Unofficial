package ggfab.mte;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.utils.fluid.FluidStackTank;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;

import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IDataCopyable;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.util.GTSplit;
import gregtech.api.util.GTUtility;
import gregtech.common.gui.modularui.hatch.MTELinkedInputHatchGui;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;

@IMetaTileEntity.SkipGenerateDescription
public class MTELinkedInputHatch extends MTEHatchInput implements IRecipeProcessingAwareHatch, IDataCopyable {

    public static final int SIZE_TANKS = 4;
    public static final String COPIED_DATA_IDENTIFIER = "linkedinputhatch";
    private SharedFluidInventory mRealInventory;
    private String mChannel;
    private boolean mPrivate;
    private State mState;
    private WorldSave save;

    public MTELinkedInputHatch(int id, String name, String nameRegional, int tier) {
        super(id, name, nameRegional, tier);
    }

    public MTELinkedInputHatch(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELinkedInputHatch(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTELinkedInputHatchGui(this).build(data, syncManager, uiSettings);
    }

    public int getCircuitSlot() {
        return 0;
    }

    public int getCircuitSlotX() {
        return 152;
    }

    public int getCircuitSlotY() {
        return 62;
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        if (aIndex == getCircuitSlot()) return super.getStackInSlot(aIndex);
        return null;
    }

    @Override
    public void setInventorySlotContents(int aIndex, ItemStack aStack) {
        if (aIndex == getCircuitSlot()) {
            mInventory[0] = GTUtility.copyAmount(0, aStack);
            markDirty();
        }
    }

    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int ordinalSide) {
        return aIndex == getCircuitSlot() && super.canInsertItem(aIndex, aStack, ordinalSide);
    }

    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        return 1;
    }

    @Override
    public void startRecipeProcessing() {
        if (mRealInventory == null) return;
        if (mRealInventory.used) {
            mState = State.Blocked;
        } else {
            mRealInventory.used = true;
            mState = State.Activated;
        }
    }

    @Override
    public CheckRecipeResult endRecipeProcessing(MTEMultiBlockBase controller) {
        if (mState == State.Activated) {
            assert mRealInventory != null;
            mRealInventory.used = false;
        }
        mState = State.Default;
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public void updateSlots() {
        if (mChannel == null || mRealInventory == null) return;
        for (int i = 0; i < mRealInventory.fluids.length; i++) {
            if (mRealInventory.fluids[i] != null && mRealInventory.fluids[i].amount <= 0)
                mRealInventory.fluids[i] = null;
        }
        markDirty();
        getWorldSave().markDirty();
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
        if (mRealInventory != null) {
            if (--mRealInventory.ref <= 0) getWorldSave().remove(getRealChannel());
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (mChannel != null) aNBT.setString("channel", mChannel);
        aNBT.setBoolean("private", mPrivate);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        String channel = aNBT.getString("channel");
        if (channel.isEmpty()) channel = null;
        this.mChannel = channel;
        mPrivate = aNBT.getBoolean("private");
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (mChannel != null) {
            mRealInventory = getWorldSave().get(getRealChannel());
            mRealInventory.ref++;
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (!getBaseMetaTileEntity().getCoverAtSide(side)
            .isGUIClickable()) return;
        if (aPlayer.isSneaking()) {
            if (this.mRealInventory == null) {
                aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_hatch.no_channel"));
                return;
            }
            mRealInventory.disableSort = !mRealInventory.disableSort;
            GTUtility.sendChatComp(
                aPlayer,
                new ChatComponentTranslation("GT5U.hatch.disableSort." + mRealInventory.disableSort));
        } else {
            this.disableFilter = !this.disableFilter;
            GTUtility.sendChatTrans(aPlayer, "GT5U.hatch.disableFilter." + this.disableFilter);
        }
    }

    @Override
    public int getGUIHeight() {
        return 166;
    }

    @Override
    public int getGUIWidth() {
        return 176;
    }

    @Override
    public NBTTagCompound getCopiedData(EntityPlayer player) {
        if (getChannel().isEmpty()) {
            return null;
        }
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("type", COPIED_DATA_IDENTIFIER);
        tag.setString("channel", getChannel());
        tag.setTag("circuit", GTUtility.saveItem(getStackInSlot(getCircuitSlot())));
        if (isPrivate()) {
            tag.setLong(
                "owner1",
                getBaseMetaTileEntity().getOwnerUuid()
                    .getMostSignificantBits());
            tag.setLong(
                "owner2",
                getBaseMetaTileEntity().getOwnerUuid()
                    .getLeastSignificantBits());
        }
        return tag;
    }

    @Override
    public boolean pasteCopiedData(EntityPlayer player, NBTTagCompound nbt) {
        if (nbt == null || !COPIED_DATA_IDENTIFIER.equals(nbt.getString("type"))) {
            return false;
        }
        ItemStack circuit = GTUtility.loadItem(nbt, "circuit");
        String channel = nbt.getString("channel");
        if (GTUtility.isStackInvalid(circuit)) circuit = null;
        if (channel.isEmpty()) {
            return false;
        } else if (circuit != null && GTUtility.getAllIntegratedCircuits()
            .stream()
            .noneMatch(circuit::isItemEqual)) {
                return false;
            }
        UUID owner = nbt.hasKey("owner1") ? new UUID(nbt.getLong("owner1"), nbt.getLong("owner2")) : null;
        if (owner != null && !owner.equals(getBaseMetaTileEntity().getOwnerUuid())) {
            return false;
        }
        setPrivate(owner != null);
        setChannel(channel);
        setInventorySlotContents(getCircuitSlot(), circuit);
        return true;
    }

    @Override
    public String getCopiedDataIdentifier(EntityPlayer player) {
        return COPIED_DATA_IDENTIFIER;
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
        float aX, float aY, float aZ) {
        if (!(aPlayer instanceof EntityPlayerMP))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
        ItemStack stick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(stick, false, true))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
        if (!stick.hasTagCompound() || !COPIED_DATA_IDENTIFIER.equals(stick.stackTagCompound.getString("type"))) {
            aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_hatch.no_data"));
            return true;
        }
        ItemStack circuit = GTUtility.loadItem(stick.stackTagCompound, "circuit");
        String channel = stick.stackTagCompound.getString("channel");
        if (GTUtility.isStackInvalid(circuit)) circuit = null;
        if (channel.isEmpty()) {
            aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_hatch.no_data"));
            return true;
        }
        UUID owner = stick.stackTagCompound.hasKey("owner1")
            ? new UUID(stick.stackTagCompound.getLong("owner1"), stick.stackTagCompound.getLong("owner2"))
            : null;
        if (owner != null && !owner.equals(getBaseMetaTileEntity().getOwnerUuid())) {
            aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_hatch.not_owned"));
            return true;
        }
        setPrivate(owner != null);
        setChannel(channel);
        setInventorySlotContents(getCircuitSlot(), circuit);
        aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_hatch.data_pasted", channel));
        return true;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) return;
        ItemStack stick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(stick, false, true)) return;
        if (getChannel().isEmpty()) {
            aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_hatch.no_channel"));
            return;
        }
        aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_hatch.data_copied", getChannel()));
        stick.stackTagCompound = getCopiedData(aPlayer);
        stick.setStackDisplayName("Linked Input Hatch configuration");
        GTUtility.ItemNBT.setBookTitle(stick, "Channel: " + getChannel());
        if (getBaseMetaTileEntity().getOwnerName() != null)
            GTUtility.ItemNBT.setBookAuthor(stick, getBaseMetaTileEntity().getOwnerName());
    }

    public boolean isPrivate() {
        return mPrivate;
    }

    public void setPrivate(boolean aPrivate) {
        if (aPrivate == mPrivate) return;
        if (getBaseMetaTileEntity().isClientSide()) {
            mPrivate = aPrivate;
            return;
        }
        if (this.mChannel == null) {
            mPrivate = aPrivate;
            return;
        }
        getWorldSave().markDirty();
        if (--this.mRealInventory.ref <= 0) {
            getWorldSave().remove(getRealChannel());
        }
        mPrivate = aPrivate;
        mRealInventory = getWorldSave().get(getRealChannel());
        mRealInventory.ref++;
        getWorldSave().markDirty();
    }

    private String getRealChannel() {
        if (mChannel == null) return null;
        if (mPrivate) return getBaseMetaTileEntity().getOwnerUuid() + mChannel;
        return new UUID(0, 0) + mChannel;
    }

    public String getChannel() {
        return mChannel == null ? "" : mChannel;
    }

    public void setChannel(String aChannel) {
        if (aChannel.isEmpty()) aChannel = null;
        if (getBaseMetaTileEntity().isClientSide()) {
            mChannel = aChannel;
            return;
        }
        if (Objects.equals(this.mChannel, aChannel)) return;
        if (this.mChannel != null) {
            if (--this.mRealInventory.ref <= 0) {
                getWorldSave().remove(getRealChannel());
            }
        }
        if (aChannel == null) {
            this.mChannel = null;
            this.mRealInventory = null;
        } else {
            this.mChannel = aChannel;
            this.mRealInventory = getWorldSave().get(getRealChannel());
            mRealInventory.ref++;
        }
        getWorldSave().markDirty();
    }

    private WorldSave getWorldSave() {
        if (save == null) {
            WorldSave save = (WorldSave) getBaseMetaTileEntity().getWorld()
                .loadItemData(WorldSave.class, "LinkedInputHatches");
            if (save == null) {
                save = new WorldSave("LinkedInputHatches");
                getBaseMetaTileEntity().getWorld()
                    .setItemData(save.mapName, save);
            }
            this.save = save;
        }
        return save;
    }

    @Override
    public int fill(FluidStack aFluid, boolean doFill) {
        if (mState == State.Blocked || mRealInventory == null || aFluid == null || !isFluidInputAllowed(aFluid))
            return 0;
        int filled = 0;
        int tAmount = aFluid.amount;
        for (int i = 0; i < SIZE_TANKS; i++) {
            FluidStack tankFluid = mRealInventory.fluids[i];
            if (tankFluid != null && tankFluid.isFluidEqual(aFluid)) {
                int canFill = getCapacity() - tankFluid.amount;
                int toFill = Math.min(canFill, tAmount);
                if (toFill > 0) {
                    if (doFill) {
                        tankFluid.amount += toFill;
                        getWorldSave().markDirty();
                    }
                    filled += toFill;
                    tAmount -= toFill;
                    if (tAmount <= 0) return filled;
                }
            }
        }
        for (int i = 0; i < SIZE_TANKS; i++) {
            if (mRealInventory.fluids[i] == null) {
                int toFill = Math.min(getCapacity(), tAmount);
                if (doFill) {
                    mRealInventory.fluids[i] = new FluidStack(aFluid, toFill);
                    getWorldSave().markDirty();
                }
                filled += toFill;
                tAmount -= toFill;
                if (tAmount <= 0) return filled;
            }
        }
        return filled;
    }

    @Override
    public FluidStack drain(int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public FluidStack drain(ForgeDirection from, FluidStack aFluid, boolean doDrain) {
        return null;
    }

    @Override
    public FluidTankInfo[] getTankInfo(ForgeDirection from) {
        if (mRealInventory == null) return new FluidTankInfo[0];
        FluidTankInfo[] infos = new FluidTankInfo[SIZE_TANKS];
        for (int i = 0; i < SIZE_TANKS; i++) {
            infos[i] = new FluidTankInfo(mRealInventory.fluids[i], getCapacity());
        }
        return infos;
    }

    public FluidStack[] getStoredFluid() {
        if (mRealInventory == null || mState == State.Blocked) return new FluidStack[0];
        return mRealInventory.fluids;
    }

    public FluidStackTank[] getFluidTanks() {
        FluidStackTank[] tanks = new FluidStackTank[SIZE_TANKS];
        for (int i = 0; i < SIZE_TANKS; i++) {
            final int index = i;
            tanks[i] = new FluidStackTank(() -> mRealInventory == null ? null : mRealInventory.fluids[index], f -> {
                if (mRealInventory != null) {
                    mRealInventory.fluids[index] = f;
                    getWorldSave().markDirty();
                }
            }, getCapacity());
        }
        return tanks;
    }

    private enum State {
        Activated,
        Blocked,
        Default,
    }

    private static class SharedFluidInventory {

        private final FluidStack[] fluids;
        public boolean disableSort;
        private boolean used;
        private int ref;

        public SharedFluidInventory() {
            this.fluids = new FluidStack[SIZE_TANKS];
        }

        public SharedFluidInventory(NBTTagCompound tag) {
            this.fluids = new FluidStack[SIZE_TANKS];
            for (int i = 0; i < SIZE_TANKS; i++) {
                String key = "" + i;
                if (!tag.hasKey(key, Constants.NBT.TAG_COMPOUND)) continue;
                fluids[i] = FluidStack.loadFluidStackFromNBT(tag.getCompoundTag(key));
            }
            ref = tag.getInteger("ref");
            disableSort = tag.getBoolean("ds");
        }

        public NBTTagCompound save() {
            NBTTagCompound tag = new NBTTagCompound();
            for (int i = 0; i < SIZE_TANKS; i++) {
                FluidStack fluid = fluids[i];
                if (fluid == null) continue;
                tag.setTag("" + i, fluid.writeToNBT(new NBTTagCompound()));
            }
            tag.setInteger("ref", ref);
            tag.setBoolean("ds", disableSort);
            return tag;
        }
    }

    public static class WorldSave extends WorldSavedData {

        private final Map<String, SharedFluidInventory> data = new HashMap<>();

        public WorldSave(String name) {
            super(name);
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            data.clear();
            @SuppressWarnings("unchecked")
            Set<Map.Entry<String, NBTBase>> set = tag.tagMap.entrySet();
            for (Map.Entry<String, NBTBase> e : set) {
                data.put(e.getKey(), new SharedFluidInventory((NBTTagCompound) e.getValue()));
            }
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            for (Map.Entry<String, SharedFluidInventory> e : data.entrySet()) {
                if (e.getValue().ref > 0) tag.setTag(
                    e.getKey(),
                    e.getValue()
                        .save());
            }
        }

        public SharedFluidInventory get(Object channel) {
            return data.computeIfAbsent(channel.toString(), k -> new SharedFluidInventory());
        }

        public void remove(Object channel) {
            data.remove(channel.toString());
            markDirty();
        }
    }

    @Override
    public String[] getDescription() {
        return GTSplit.splitLocalizedFormatted("gt.blockmachines.hatch_input_linked.desc", getCapacity());
    }
}
