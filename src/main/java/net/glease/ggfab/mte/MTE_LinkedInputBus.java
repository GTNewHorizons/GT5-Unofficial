package net.glease.ggfab.mte;

import java.util.*;

import net.glease.ggfab.GGConstants;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.WorldSavedData;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import com.gtnewhorizons.modularui.common.widget.*;
import com.gtnewhorizons.modularui.common.widget.textfield.TextFieldWidget;

import gregtech.api.enums.ItemList;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.util.GT_OreDictUnificator;
import gregtech.api.util.GT_Utility;

public class MTE_LinkedInputBus extends GT_MetaTileEntity_Hatch_InputBus {

    public static final int SIZE_INVENTORY = 18;
    private SharedInventory mRealInventory;
    private final ItemStackHandlerProxy handler = new ItemStackHandlerProxy();
    private String mChannel;
    private boolean mPrivate;
    private State mState;
    private WorldSave save;

    public MTE_LinkedInputBus(int id, String name, String nameRegional, int tier) {
        super(
                id,
                name,
                nameRegional,
                tier,
                1,
                new String[] { SIZE_INVENTORY + " slot input bus linked together wirelessly",
                        "Link does not cross world boundary",
                        "Left/right click with data stick to copy/paste configuration", GGConstants.GGMARK_TOOLTIP, });
    }

    public MTE_LinkedInputBus(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, 1, aDescription, aTextures);
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTE_LinkedInputBus(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public int getCircuitSlot() {
        return 0;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        builder.widget(
                new TextFieldWidget().setSynced(true, true).setGetter(() -> mChannel == null ? "" : mChannel)
                        .setSetter(this::setChannel).setTextColor(Color.WHITE.dark(1))
                        .setTextAlignment(Alignment.CenterLeft).setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                        .setGTTooltip(() -> mTooltipCache.getData("ggfab.tooltip.linked_input_bus.change_freq_warn"))
                        .setSize(60, 18).setPos(48, 3))
                .widget(
                        new CycleButtonWidget().setToggle(this::isPrivate, this::setPrivate)
                                .setTextureGetter(
                                        i -> i == 1 ? GT_UITextures.OVERLAY_BUTTON_CHECKMARK
                                                : GT_UITextures.OVERLAY_BUTTON_CROSS)
                                .setVariableBackground(GT_UITextures.BUTTON_STANDARD_TOGGLE).setSynced(true, true)
                                .setGTTooltip(() -> mTooltipCache.getData("ggfab.tooltip.linked_input_bus.private"))
                                .setSize(18, 18).setPos(150, 3))
                .widget(
                        SlotGroup.ofItemHandler(handler, 9).startFromSlot(0).endAtSlot(SIZE_INVENTORY - 1)
                                .background(getGUITextureSet().getItemSlot())
                                .slotCreator(i -> new BaseSlot(handler, i, false) {

                                    @Override
                                    public ItemStack getStack() {
                                        return isEnabled() ? super.getStack() : null;
                                    }

                                    @Override
                                    public boolean isEnabled() {
                                        return mChannel != null;
                                    }
                                }).build().setPos(7, 24))
                .widget(new TextWidget(new Text("Private")).setPos(110, 3).setSize(43, 20))
                .widget(new TextWidget(new Text("Channel")).setPos(5, 3).setSize(43, 20));
    }

    @Override
    public int getCircuitSlotX() {
        return 152;
    }

    @Override
    public ItemStack getStackInSlot(int aIndex) {
        if (aIndex == getCircuitSlot()) return super.getStackInSlot(aIndex);
        if (mState != State.Blocked && mChannel != null && mRealInventory != null) {
            if (aIndex > 0 && aIndex <= SIZE_INVENTORY) return mRealInventory.stacks[aIndex - 1];
        }
        return null;
    }

    @Override
    public void setInventorySlotContents(int aIndex, ItemStack aStack) {
        if (aIndex == getCircuitSlot()) {
            mInventory[0] = GT_Utility.copyAmount(0, aStack);
            markDirty();
        } else if (mState != State.Blocked && mChannel != null && mRealInventory != null) {
            if (aIndex > 0 && aIndex <= SIZE_INVENTORY) {
                mRealInventory.stacks[aIndex - 1] = aStack;
                getWorldSave().markDirty();
            }
        }
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return super.getTexturesActive(aBaseTexture);
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return super.getTexturesInactive(aBaseTexture);
    }

    @Override
    public boolean canInsertItem(int aIndex, ItemStack aStack, int ordinalSide) {
        return isValidSlot(aIndex) && aStack != null
                && mChannel != null
                && mRealInventory != null
                && aIndex > getCircuitSlot()
                && aIndex < SIZE_INVENTORY + 1
                && (mRealInventory.stacks[aIndex - 1] == null
                        || GT_Utility.areStacksEqual(aStack, mRealInventory.stacks[aIndex - 1]))
                && allowPutStack(getBaseMetaTileEntity(), aIndex, ForgeDirection.getOrientation(ordinalSide), aStack);
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
            ItemStack aStack) {
        return side == getBaseMetaTileEntity().getFrontFacing() && aIndex != getCircuitSlot()
                && (mRecipeMap == null || disableFilter || mRecipeMap.containsInput(aStack))
                && (mRealInventory.disableLimited || limitedAllowPutStack(aIndex, aStack));
    }

    @Override
    protected boolean limitedAllowPutStack(int aIndex, ItemStack aStack) {
        for (int i = 0; i < SIZE_INVENTORY; i++)
            if (GT_Utility.areStacksEqual(GT_OreDictUnificator.get_nocopy(aStack), mRealInventory.stacks[i]))
                return i == aIndex - 1;
        return mRealInventory.stacks[aIndex - 1] == null;
    }

    @Override
    public boolean canExtractItem(int aIndex, ItemStack aStack, int aSide) {
        return false;
    }

    @Override
    public int getSizeInventory() {
        if (mState != State.Blocked && mChannel != null && mRealInventory != null) return SIZE_INVENTORY + 1;
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
    public void endRecipeProcessing() {
        if (mState == State.Activated) {
            assert mRealInventory != null;
            mRealInventory.used = false;
        }
        mState = State.Default;
    }

    @Override
    public void updateSlots() {
        if (mChannel == null || mRealInventory == null) return;
        for (int i = 0; i < mRealInventory.stacks.length; i++) {
            if (mRealInventory.stacks[i] != null
                    && (mRealInventory.stacks[i].getItem() == null || mRealInventory.stacks[i].stackSize <= 0))
                mRealInventory.stacks[i] = null;
        }
        if (!mRealInventory.disableSort) fillStacksIntoFirstSlots();
        markDirty();
        getWorldSave().markDirty();
    }

    @Override
    protected void fillStacksIntoFirstSlots() {
        // sanity check
        if (mRealInventory == null) return;
        final int L = SIZE_INVENTORY;
        HashMap<GT_Utility.ItemId, Integer> slots = new HashMap<>(L);
        HashMap<GT_Utility.ItemId, ItemStack> stacks = new HashMap<>(L);
        List<GT_Utility.ItemId> order = new ArrayList<>(L);
        List<Integer> validSlots = new ArrayList<>(L);
        for (int i = 0; i < L; i++) {
            validSlots.add(i);
            ItemStack s = mRealInventory.stacks[i];
            if (s == null) continue;
            GT_Utility.ItemId sID = GT_Utility.ItemId.createNoCopy(s);
            slots.merge(sID, s.stackSize, Integer::sum);
            if (!stacks.containsKey(sID)) stacks.put(sID, s);
            order.add(sID);
            mRealInventory.stacks[i] = null;
        }
        int slotindex = 0;
        for (GT_Utility.ItemId sID : order) {
            int toSet = slots.get(sID);
            if (toSet == 0) continue;
            int slot = validSlots.get(slotindex);
            slotindex++;
            mRealInventory.stacks[slot] = stacks.get(sID).copy();
            toSet = Math.min(toSet, mRealInventory.stacks[slot].getMaxStackSize());
            mRealInventory.stacks[slot].stackSize = toSet;
            slots.merge(sID, toSet, (a, b) -> a - b);
        }
    }

    private void dropItems(ItemStack[] aStacks) {
        for (ItemStack stack : aStacks) {
            if (!GT_Utility.isStackValid(stack)) continue;
            EntityItem ei = new EntityItem(
                    getBaseMetaTileEntity().getWorld(),
                    getBaseMetaTileEntity().getOffsetX(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5,
                    getBaseMetaTileEntity().getOffsetY(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5,
                    getBaseMetaTileEntity().getOffsetZ(getBaseMetaTileEntity().getFrontFacing(), 1) + 0.5,
                    stack);
            ei.motionX = ei.motionY = ei.motionZ = 0;
            getBaseMetaTileEntity().getWorld().spawnEntityInWorld(ei);
        }
    }

    @Override
    public boolean shouldDropItemAt(int index) {
        return mRealInventory != null && mRealInventory.ref <= 1;
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();
        if (mRealInventory != null) {
            if (--mRealInventory.ref <= 0) getWorldSave().remove(mChannel);
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
        if ("".equals(channel)) channel = null;
        this.mChannel = channel;
        mPrivate = aNBT.getBoolean("private");
    }

    public String getChannel() {
        return mChannel;
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        if (mChannel != null) {
            mRealInventory = getWorldSave().get(getRealChannel());
            handler.set(mRealInventory.stacks);
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (!getBaseMetaTileEntity().getCoverBehaviorAtSideNew(side).isGUIClickable(
                side,
                getBaseMetaTileEntity().getCoverIDAtSide(side),
                getBaseMetaTileEntity().getComplexCoverDataAtSide(side),
                getBaseMetaTileEntity()))
            return;
        if (aPlayer.isSneaking()) {
            if (this.mRealInventory == null) {
                aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_bus.no_channel"));
                return;
            }
            if (mRealInventory.disableSort) {
                mRealInventory.disableSort = false;
            } else {
                if (mRealInventory.disableLimited) {
                    mRealInventory.disableLimited = false;
                } else {
                    mRealInventory.disableSort = true;
                    mRealInventory.disableLimited = true;
                }
            }
            GT_Utility.sendChatToPlayer(
                    aPlayer,
                    StatCollector.translateToLocal("GT5U.hatch.disableSort." + mRealInventory.disableSort) + "   "
                            + StatCollector
                                    .translateToLocal("GT5U.hatch.disableLimited." + mRealInventory.disableLimited));
        } else {
            this.disableFilter = !this.disableFilter;
            GT_Utility.sendChatToPlayer(
                    aPlayer,
                    StatCollector.translateToLocal("GT5U.hatch.disableFilter." + this.disableFilter));
        }
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer, ForgeDirection side,
            float aX, float aY, float aZ) {
        if (!(aPlayer instanceof EntityPlayerMP))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
        ItemStack stick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(stick, true, true))
            return super.onRightclick(aBaseMetaTileEntity, aPlayer, side, aX, aY, aZ);
        if (!stick.hasTagCompound() || !"linkedinputbus".equals(stick.stackTagCompound.getString("ggfab.type"))) {
            aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_bus.no_data"));
            return true;
        }
        ItemStack circuit = GT_Utility.loadItem(stick.stackTagCompound, "circuit");
        String channel = stick.stackTagCompound.getString("channel");
        if (GT_Utility.isStackInvalid(circuit)) circuit = null;
        if ("".equals(channel)) {
            aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_bus.no_data"));
            return true;
        } else if (circuit != null && !getConfigurationCircuits().contains(circuit)) {
            aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_bus.no_data"));
            return true;
        }
        UUID owner = stick.stackTagCompound.hasKey("owner1")
                ? new UUID(stick.stackTagCompound.getLong("owner1"), stick.stackTagCompound.getLong("owner2"))
                : null;
        if (owner != null && !owner.equals(getBaseMetaTileEntity().getOwnerUuid())) {
            aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_bus.not_owned"));
            return true;
        }
        setPrivate(owner != null);
        setChannel(channel);
        setInventorySlotContents(getCircuitSlot(), circuit);
        aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_bus.data_pasted", channel));
        return true;
    }

    @Override
    public void onLeftclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        if (!(aPlayer instanceof EntityPlayerMP)) return;
        ItemStack stick = aPlayer.inventory.getCurrentItem();
        if (!ItemList.Tool_DataStick.isStackEqual(stick, true, true)) return;
        if (getChannel() == null) {
            aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_bus.no_channel"));
            return;
        }
        NBTTagCompound tag = new NBTTagCompound();
        tag.setString("ggfab.type", "linkedinputbus");
        tag.setString("channel", getChannel());
        tag.setTag("circuit", GT_Utility.saveItem(getStackInSlot(getCircuitSlot())));
        if (isPrivate()) {
            tag.setLong("owner1", getBaseMetaTileEntity().getOwnerUuid().getMostSignificantBits());
            tag.setLong("owner2", getBaseMetaTileEntity().getOwnerUuid().getLeastSignificantBits());
        }
        aPlayer.addChatMessage(new ChatComponentTranslation("ggfab.info.linked_input_bus.data_copied", getChannel()));
        stick.stackTagCompound = tag;
        stick.setStackDisplayName("Linked Input Bus configuration");
        // abuse the title mechanism here. I assure you it will be fine (tm).
        GT_Utility.ItemNBT.setBookTitle(stick, "Channel: " + getChannel());
        if (getBaseMetaTileEntity().getOwnerName() != null)
            GT_Utility.ItemNBT.setBookAuthor(stick, getBaseMetaTileEntity().getOwnerName());
    }

    private String getRealChannel() {
        if (mChannel == null) return null;
        if (mPrivate) return getBaseMetaTileEntity().getOwnerUuid() + mChannel;
        return new UUID(0, 0) + mChannel;
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
            // last referrer, drop inventory
            dropItems(mRealInventory.stacks);
            getWorldSave().remove(getRealChannel());
        }
        mPrivate = aPrivate;
        mRealInventory = getWorldSave().get(getRealChannel());
        this.handler.set(mRealInventory.stacks);
        mRealInventory.ref++;
        getWorldSave().markDirty();
    }

    public void setChannel(String aChannel) {
        if ("".equals(aChannel)) aChannel = null;
        if (getBaseMetaTileEntity().isClientSide()) {
            mChannel = aChannel;
            return;
        }
        if (Objects.equals(this.mChannel, aChannel)) return; // noop
        if (this.mChannel != null) {
            if (--this.mRealInventory.ref <= 0) {
                // last referrer, drop inventory
                dropItems(mRealInventory.stacks);
                getWorldSave().remove(getRealChannel());
            }
        }
        if (aChannel == null) {
            this.mChannel = null;
            this.mRealInventory = null;
            this.handler.setFake();
        } else {
            this.mChannel = aChannel;
            this.mRealInventory = getWorldSave().get(getRealChannel());
            this.handler.set(mRealInventory.stacks);
            mRealInventory.ref++;
        }
        getWorldSave().markDirty();
    }

    private WorldSave getWorldSave() {
        if (save == null) {
            WorldSave save = (WorldSave) getBaseMetaTileEntity().getWorld()
                    .loadItemData(WorldSave.class, "LinkedInputBusses");
            if (save == null) {
                save = new WorldSave("LinkedInputBusses");
                getBaseMetaTileEntity().getWorld().setItemData(save.mapName, save);
            }
            this.save = save;
        }
        return save;
    }

    private enum State {
        Activated,
        Blocked,
        Default,
    }

    private static class SharedInventory {

        private final ItemStack[] stacks;
        /**
         * Inventory wrapper for ModularUI
         */
        private final ItemStackHandler inventoryHandler;
        public boolean disableLimited = true;
        public boolean disableSort;
        private boolean used;
        private int ref;

        public SharedInventory() {
            this.stacks = new ItemStack[SIZE_INVENTORY];
            inventoryHandler = new ItemStackHandler(stacks);
        }

        public SharedInventory(NBTTagCompound tag) {
            this.stacks = new ItemStack[SIZE_INVENTORY];
            inventoryHandler = new ItemStackHandler(stacks);

            for (int i = 0; i < SIZE_INVENTORY; i++) {
                String key = "" + i;
                if (!tag.hasKey(key, Constants.NBT.TAG_COMPOUND)) continue;
                stacks[i] = ItemStack.loadItemStackFromNBT(tag.getCompoundTag(key));
            }

            ref = tag.getInteger("ref");
            disableLimited = tag.getBoolean("dl");
            disableSort = tag.getBoolean("ds");
        }

        public NBTTagCompound save() {
            NBTTagCompound tag = new NBTTagCompound();
            for (int i = 0; i < SIZE_INVENTORY; i++) {
                ItemStack stack = stacks[i];
                if (stack == null) continue;
                tag.setTag("" + i, stack.writeToNBT(new NBTTagCompound()));
            }
            tag.setInteger("ref", ref);
            tag.setBoolean("ds", disableSort);
            tag.setBoolean("dl", disableLimited);
            return tag;
        }
    }

    public static class WorldSave extends WorldSavedData {

        private final Map<String, SharedInventory> data = new HashMap<>();

        public WorldSave(String p_i2141_1_) {
            super(p_i2141_1_);
        }

        @Override
        public void readFromNBT(NBTTagCompound tag) {
            data.clear();
            @SuppressWarnings("unchecked")
            Set<Map.Entry<String, NBTBase>> set = tag.tagMap.entrySet();
            for (Map.Entry<String, NBTBase> e : set) {
                data.put(e.getKey(), new SharedInventory((NBTTagCompound) e.getValue()));
            }
        }

        @Override
        public void writeToNBT(NBTTagCompound tag) {
            for (Map.Entry<String, SharedInventory> e : data.entrySet()) {
                if (e.getValue().ref > 0) tag.setTag(e.getKey(), e.getValue().save());
            }
        }

        public SharedInventory get(Object channel) {
            return data.computeIfAbsent(channel.toString(), k -> new SharedInventory());
        }

        public void remove(Object channel) {
            data.remove(channel.toString());
            markDirty();
        }
    }

    private static class ItemStackHandlerProxy extends ItemStackHandler {

        private static final ItemStack[] EMPTY = new ItemStack[SIZE_INVENTORY];
        private boolean fake;

        public ItemStackHandlerProxy() {
            super(EMPTY);
            fake = true;
        }

        public void setFake() {
            set(EMPTY);
            fake = true;
        }

        public boolean isFake() {
            return fake;
        }

        public void set(ItemStack[] stacks) {
            this.stacks = Arrays.asList(stacks);
            fake = false;
        }

        @Override
        public NBTTagCompound serializeNBT() {
            NBTTagCompound tag = super.serializeNBT();
            tag.setBoolean("fake", fake);
            return tag;
        }

        @Override
        public void deserializeNBT(NBTTagCompound nbt) {
            super.deserializeNBT(nbt);
            fake = nbt.getBoolean("fake");
        }
    }
}
