package gregtech.api.metatileentity.implementations;

import static gregtech.api.enums.Textures.BlockIcons.ITEM_IN_SIGN;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_COLORS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_PIPE_IN;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.cleanroommc.modularui.factory.PosGuiData;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.CycleButtonWidget;

import gregtech.GTMod;
import gregtech.api.enums.Dyes;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTClientPreference;
import gregtech.api.util.GTOreDictUnificator;
import gregtech.api.util.GTTooltipDataCache;
import gregtech.api.util.GTUtility;
import gregtech.api.util.extensions.ArrayExt;
import gregtech.common.gui.modularui.hatch.MTEHatchInputBusGui;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEHatchInputBus extends MTEHatch implements IConfigurationCircuitSupport, IAddUIWidgets {

    private static final String SORTING_MODE_TOOLTIP = "GT5U.machines.sorting_mode.tooltip";
    private static final String ONE_STACK_LIMIT_TOOLTIP = "GT5U.machines.one_stack_limit.tooltip";
    private static final int BUTTON_SIZE = 18;

    public RecipeMap<?> mRecipeMap = null;
    public boolean disableSort;
    public boolean disableFilter = true;
    public boolean disableLimited = true;
    protected int uiButtonCount = 0;

    public MTEHatchInputBus(int id, String name, String nameRegional, int tier) {
        this(id, name, nameRegional, tier, getSlots(tier) + 1);
    }

    protected MTEHatchInputBus(int id, String name, String nameRegional, int tier, int slots, String[] description) {
        super(id, name, nameRegional, tier, slots, description);
    }

    public MTEHatchInputBus(int id, String name, String nameRegional, int tier, int slots) {
        super(
            id,
            name,
            nameRegional,
            tier,
            slots,
            ArrayExt.of(
                "Item Input for Multiblocks",
                "Shift + right click with screwdriver to turn Sort mode on/off",
                "Capacity: " + getSlots(tier) + " stack" + (getSlots(tier) >= 2 ? "s" : "")));
    }

    public MTEHatchInputBus(String aName, int aTier, String[] aDescription, ITexture[][][] aTextures) {
        this(aName, aTier, getSlots(aTier) + 1, aDescription, aTextures);
    }

    public MTEHatchInputBus(String aName, int aTier, int aSlots, String[] aDescription, ITexture[][][] aTextures) {
        super(aName, aTier, aSlots, aDescription, aTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        byte color = getBaseMetaTileEntity().getColorization();
        ITexture coloredPipeOverlay = TextureFactory.of(OVERLAY_PIPE_COLORS[color + 1]);
        return GTMod.proxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), coloredPipeOverlay,
                TextureFactory.of(ITEM_IN_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), coloredPipeOverlay };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        byte color = getBaseMetaTileEntity().getColorization();
        ITexture coloredPipeOverlay = TextureFactory.of(OVERLAY_PIPE_COLORS[color + 1]);
        return GTMod.proxy.mRenderIndicatorsOnHatch
            ? new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), coloredPipeOverlay,
                TextureFactory.of(ITEM_IN_SIGN) }
            : new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_PIPE_IN), coloredPipeOverlay };
    }

    @Override
    public boolean isFacingValid(ForgeDirection facing) {
        return true;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return aIndex != getCircuitSlot();
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEHatchInputBus(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public boolean onRightclick(IGregTechTileEntity aBaseMetaTileEntity, EntityPlayer aPlayer) {
        openGui(aPlayer);
        return true;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setByte("color", (byte) (getBaseMetaTileEntity().getColorization() + 1));

    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currenttip, accessor, config);
        byte color = (byte) (accessor.getNBTData()
            .getByte("color") - 1);
        if (color >= 0 && color < 16) currenttip.add(
            StatCollector.translateToLocalFormatted(
                "GT5U.waila.hatch.color_channel",
                Dyes.VALUES[color].formatting + Dyes.VALUES[color].getLocalizedDyeName() + EnumChatFormatting.GRAY));
    }

    @Override
    public void initDefaultModes(NBTTagCompound aNBT) {
        if (!getBaseMetaTileEntity().getWorld().isRemote) {
            GTClientPreference tPreference = GTMod.proxy.getClientPreference(getBaseMetaTileEntity().getOwnerUuid());
            if (tPreference != null) disableFilter = !tPreference.isInputBusInitialFilterEnabled();
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        if (aBaseMetaTileEntity.isServerSide()) {
            updateSlots();
        }
    }

    public void updateSlots() {
        for (int i = 0; i < mInventory.length - 1; i++)
            if (mInventory[i] != null && mInventory[i].stackSize <= 0) mInventory[i] = null;
        if (!disableSort) fillStacksIntoFirstSlots();
    }

    protected void fillStacksIntoFirstSlots() {
        final int L = mInventory.length - 1;
        HashMap<GTUtility.ItemId, Integer> slots = new HashMap<>(L);
        HashMap<GTUtility.ItemId, ItemStack> stacks = new HashMap<>(L);
        List<GTUtility.ItemId> order = new ArrayList<>(L);
        List<Integer> validSlots = new ArrayList<>(L);
        for (int i = 0; i < L; i++) {
            if (!isValidSlot(i)) continue;
            validSlots.add(i);
            ItemStack s = mInventory[i];
            if (s == null) continue;
            GTUtility.ItemId sID = GTUtility.ItemId.createNoCopy(s);
            slots.merge(sID, s.stackSize, Integer::sum);
            if (!stacks.containsKey(sID)) stacks.put(sID, s);
            order.add(sID);
            mInventory[i] = null;
        }
        int slotindex = 0;
        for (GTUtility.ItemId sID : order) {
            int toSet = slots.get(sID);
            if (toSet == 0) continue;
            int slot = validSlots.get(slotindex);
            slotindex++;
            mInventory[slot] = stacks.get(sID)
                .copy();
            toSet = Math.min(toSet, mInventory[slot].getMaxStackSize());
            mInventory[slot].stackSize = toSet;
            slots.merge(sID, toSet, (a, b) -> a - b);
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("disableSort", disableSort);
        aNBT.setBoolean("disableFilter", disableFilter);
        aNBT.setBoolean("disableLimited", disableLimited);
        if (mRecipeMap != null) {
            aNBT.setString("recipeMap", mRecipeMap.unlocalizedName);
        }
        aNBT.setBoolean("migrationCircuitSlot", true);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        disableSort = aNBT.getBoolean("disableSort");
        disableFilter = aNBT.getBoolean("disableFilter");
        if (aNBT.hasKey("disableLimited")) {
            disableLimited = aNBT.getBoolean("disableLimited");
        }
        mRecipeMap = RecipeMap.getFromOldIdentifier(aNBT.getString("recipeMap"));

        // TODO Delete this code after one update. Also, don't forget to delete the NbtTag - "migrationCircuitSlot".
        if (allowSelectCircuit()) {
            if (!aNBT.hasKey("migrationCircuitSlot")) {
                int newCircuitSlot = getSlots(mTier);
                int oldCircuitSlot = mTier < 1 ? 1 : mTier == 1 ? 4 : mTier == 2 ? 9 : 16;
                ItemStack oldCircuit = getStackInSlot(oldCircuitSlot);

                if (oldCircuit != null && getStackInSlot(newCircuitSlot) == null) {
                    setInventorySlotContents(newCircuitSlot, oldCircuit.copy());
                    setInventorySlotContents(oldCircuitSlot, null);
                }
            }
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (!getBaseMetaTileEntity().getCoverAtSide(side)
            .isGUIClickable()) return;
        if (aPlayer.isSneaking()) {
            if (disableSort) {
                disableSort = false;
            } else {
                if (disableLimited) {
                    disableLimited = false;
                } else {
                    disableSort = true;
                    disableLimited = true;
                }
            }
            GTUtility.sendChatToPlayer(
                aPlayer,
                StatCollector.translateToLocal("GT5U.hatch.disableSort." + disableSort) + "   "
                    + StatCollector.translateToLocal("GT5U.hatch.disableLimited." + disableLimited));
        } else {
            disableFilter = !disableFilter;
            GTUtility.sendChatTrans(aPlayer, "GT5U.hatch.disableFilter." + disableFilter);
        }
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        if (aIndex == getCircuitSlot()) return false;
        return side == getBaseMetaTileEntity().getFrontFacing();
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
        ItemStack aStack) {
        return side == getBaseMetaTileEntity().getFrontFacing() && aIndex != getCircuitSlot()
            && (mRecipeMap == null || disableFilter || mRecipeMap.containsInput(aStack))
            && (disableLimited || limitedAllowPutStack(aIndex, aStack));
    }

    protected boolean limitedAllowPutStack(int aIndex, ItemStack aStack) {
        for (int i = 0; i < getSizeInventory(); i++)
            if (GTUtility.areStacksEqual(GTOreDictUnificator.get_nocopy(aStack), mInventory[i])) return i == aIndex;
        return mInventory[aIndex] == null;
    }

    @Override
    public boolean allowSelectCircuit() {
        return true;
    }

    @Override
    public int getCircuitSlot() {
        return getSlots(mTier);
    }

    @Override
    protected boolean useMui2() {
        return true;
    }

    @Override
    public ModularPanel buildUI(PosGuiData data, PanelSyncManager syncManager, UISettings uiSettings) {
        return new MTEHatchInputBusGui(this).build(data, syncManager, uiSettings);
    }

    private Widget createToggleButton(Supplier<Boolean> getter, Consumer<Boolean> setter, UITexture picture,
        Supplier<GTTooltipDataCache.TooltipData> tooltipDataSupplier) {
        return new CycleButtonWidget().setToggle(getter, setter)
            .setStaticTexture(picture)
            .setVariableBackground(GTUITextures.BUTTON_STANDARD_TOGGLE)
            .setTooltipShowUpDelay(TOOLTIP_DELAY)
            .setPos(6 + (uiButtonCount++ * BUTTON_SIZE), 60 + getOffsetY())
            .setSize(BUTTON_SIZE, BUTTON_SIZE)
            .setGTTooltip(tooltipDataSupplier);
    }

    protected void addSortStacksButton(ModularWindow.Builder builder) {
        builder.widget(
            createToggleButton(
                () -> !disableSort,
                val -> disableSort = !val,
                GTUITextures.OVERLAY_BUTTON_SORTING_MODE,
                () -> mTooltipCache.getData(SORTING_MODE_TOOLTIP)));
    }

    protected void addOneStackLimitButton(ModularWindow.Builder builder) {
        builder.widget(createToggleButton(() -> !disableLimited, val -> {
            disableLimited = !val;
            updateSlots();
        }, GTUITextures.OVERLAY_BUTTON_ONE_STACK_LIMIT, () -> mTooltipCache.getData(ONE_STACK_LIMIT_TOOLTIP)));
    }

    @Override
    public int getGUIWidth() {
        return super.getGUIWidth() + getOffsetX();
    }

    @Override
    public int getGUIHeight() {
        return super.getGUIHeight() + getOffsetY();
    }

    @Override
    public int getCircuitSlotX() {
        return 153 + getOffsetX();
    }

    @Override
    public int getCircuitSlotY() {
        return 60 + getOffsetY();
    }

    /**
     * Removes the specified quantity of items matching any of the target ItemStacks.
     *
     * @param targets Array of target ItemStacks to search for (compared by type and metadata).
     * @param amount  Number of items to remove.
     * @return ItemStack of removed items, or null if nothing was removed.
     */
    public ItemStack removeResource(ItemStack[] targets, int amount) {
        if (targets == null || targets.length == 0 || amount <= 0 || getBaseMetaTileEntity() == null) {
            return null;
        }

        ItemStack result = null;
        int remaining = amount;
        for (int i = 0; i < this.getSizeInventory() && remaining > 0; i++) {
            if (i == getCircuitSlot()) continue;
            ItemStack slotStack = this.getStackInSlot(i);
            if (slotStack == null || slotStack.stackSize < amount) {
                continue;
            }
            for (ItemStack target : targets) {
                if (target != null && GTUtility.areStacksEqual(slotStack, target)) {
                    if (result == null || GTUtility.areStacksEqual(result, slotStack)) {
                        int toRemove = Math.min(remaining, slotStack.stackSize);
                        ItemStack removed = getBaseMetaTileEntity().decrStackSize(i, toRemove);
                        if (removed != null) {
                            if (result == null) {
                                result = removed.copy();
                            } else {
                                result.stackSize += removed.stackSize;
                            }
                            remaining -= removed.stackSize;
                            updateSlots();
                        }
                    }
                }
            }
        }
        return result;
    }

    /**
     * Removes all items matching any of the target ItemStacks.
     *
     * @param targets Array of target ItemStacks to search for (compared by type and metadata).
     * @return ItemStack with the total quantity of removed items, or null if nothing was removed.
     */
    public ItemStack removeAllResource(ItemStack[] targets) {
        if (targets == null || targets.length == 0 || getBaseMetaTileEntity() == null) {
            return null;
        }

        ItemStack result = null;
        boolean updated = false;
        for (int i = 0; i < getSizeInventory(); i++) {
            if (i == getCircuitSlot()) continue;
            ItemStack slotStack = getStackInSlot(i);
            if (slotStack == null) {
                continue;
            }
            for (ItemStack target : targets) {
                if (target != null && GTUtility.areStacksEqual(slotStack, target)) {
                    if (result == null || GTUtility.areStacksEqual(result, slotStack)) {
                        ItemStack removed = getBaseMetaTileEntity().decrStackSize(i, slotStack.stackSize);
                        if (removed != null) {
                            if (result == null) {
                                result = removed.copy();
                            } else if (GTUtility.areStacksEqual(result, removed)) {
                                result.stackSize += removed.stackSize;
                            }
                            updated = true;
                        }
                    }
                }
            }
        }
        if (updated) {
            updateSlots();
        }
        return result;
    }

    /**
     * Finds the first ItemStack matching any of the target ItemStacks.
     *
     * @param targets Array of target ItemStacks to search for (compared by type and metadata).
     * @return First found ItemStack, or null if nothing was found.
     */
    public ItemStack findResource(ItemStack[] targets) {
        if (targets == null || targets.length == 0 || getSizeInventory() == 0) {
            return null;
        }

        for (int i = 0; i < getSizeInventory(); i++) {
            if (i == getCircuitSlot()) continue;
            ItemStack slotStack = getStackInSlot(i);
            if (slotStack == null) {
                continue;
            }
            for (ItemStack target : targets) {
                if (target != null && GTUtility.areStacksEqual(slotStack, target)) {
                    return slotStack.copy();
                }
            }
        }
        return null;
    }

    /**
     * Checks if the inventory contains an item matching any of the target ItemStacks.
     *
     * @param targets Array of target ItemStacks to search for (compared by type and metadata).
     * @return true if a matching item is found, false otherwise.
     */
    public boolean hasResource(ItemStack[] targets) {
        if (targets == null || targets.length == 0 || getSizeInventory() == 0) {
            return false;
        }

        for (int i = 0; i < getSizeInventory(); i++) {
            if (i == getCircuitSlot()) continue;
            ItemStack slotStack = getStackInSlot(i);
            if (slotStack == null) {
                continue;
            }
            for (ItemStack target : targets) {
                if (target != null && GTUtility.areStacksEqual(slotStack, target)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Checks if the inventory contains at least the specified quantity of items matching any of the target ItemStacks.
     *
     * @param targets Array of target ItemStacks to search for (compared by type and metadata).
     * @param amount  Number of items to check for.
     * @return true if at least the specified quantity is found, false otherwise.
     */
    public boolean hasResource(ItemStack[] targets, int amount) {
        if (targets == null || targets.length == 0 || amount <= 0 || getSizeInventory() == 0) {
            return false;
        }

        int total = 0;
        for (int i = 0; i < getSizeInventory(); i++) {
            if (i == getCircuitSlot()) continue;
            ItemStack slotStack = getStackInSlot(i);
            if (slotStack == null) {
                continue;
            }
            for (ItemStack target : targets) {
                if (target != null && GTUtility.areStacksEqual(slotStack, target)) {
                    total += slotStack.stackSize;
                    if (total >= amount) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // one element target methods

    public ItemStack removeResource(ItemStack target, int amount) {
        if (target == null) return null;
        return removeResource(new ItemStack[] { target }, amount);
    }

    public ItemStack removeAllResource(ItemStack target) {
        if (target == null) return null;
        return removeAllResource(new ItemStack[] { target });
    }

    public ItemStack findResource(ItemStack target) {
        if (target == null) return null;
        return findResource(new ItemStack[] { target });
    }

    public boolean hasResource(ItemStack target) {
        if (target == null) return false;
        return hasResource(new ItemStack[] { target });
    }

    public boolean hasResource(ItemStack target, int amount) {
        if (target == null) return false;
        return hasResource(new ItemStack[] { target }, amount);
    }

    @Override
    public NBTTagCompound getDescriptionData() {

        NBTTagCompound tag = super.getDescriptionData();
        for (int i = 0; i < mInventory.length; i++) {
            ItemStack stack = mInventory[i];
            if (stack != null) {
                NBTTagCompound s = new NBTTagCompound();
                stack.writeToNBT(s);
                tag.setTag("slot" + i, s);
            }
        }
        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        for (int i = 0; i < mInventory.length; i++) {
            String key = "slot" + i;
            if (data.hasKey(key)) {
                mInventory[i] = ItemStack.loadItemStackFromNBT(data.getCompoundTag(key));
            } else {
                mInventory[i] = null;
            }
        }
        super.onDescriptionPacket(data);
    }
}
