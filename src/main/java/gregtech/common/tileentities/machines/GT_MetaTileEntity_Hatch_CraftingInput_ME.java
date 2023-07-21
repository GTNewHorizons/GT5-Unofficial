package gregtech.common.tileentities.machines;

import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_ME_INPUT_HATCH_ACTIVE;

import java.util.*;
import java.util.stream.Collectors;

import javax.annotation.Nullable;

import appeng.api.AEApi;
import appeng.api.implementations.ICraftingPatternItem;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.api.storage.data.IAEFluidStack;
import appeng.api.util.DimensionalCoord;
import appeng.util.IWideReadableNumberConverter;
import appeng.util.Platform;
import appeng.util.ReadableNumberConverter;
import com.glodblock.github.common.item.ItemFluidPacket;
import com.gtnewhorizons.modularui.common.internal.wrapper.BaseSlot;
import gregtech.api.gui.modularui.GT_UITextures;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.DrawableWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.SlotWidget;

import appeng.api.implementations.IPowerChannelState;
import appeng.api.networking.GridFlags;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.AECableType;
import appeng.me.GridAccessException;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import gregtech.api.GregTech_API;
import gregtech.api.enums.ItemList;
import gregtech.api.interfaces.IConfigurationCircuitSupport;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.modularui.IAddGregtechLogo;
import gregtech.api.interfaces.modularui.IAddUIWidgets;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.MetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_InputBus;
import gregtech.api.render.TextureFactory;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.ArrayUtils;
import org.jetbrains.annotations.NotNull;
/*
4. ui button to return all items
quick recipe trigger
rename
blocking mode?
 */
public class GT_MetaTileEntity_Hatch_CraftingInput_ME extends GT_MetaTileEntity_Hatch_InputBus
    implements IConfigurationCircuitSupport, IAddGregtechLogo, IAddUIWidgets, IPowerChannelState, ICraftingProvider, IGridProxyable {

    // mInventory is used for storing patterns, circuit and manual slot (typically NC items)
    private static final int MAX_PATTERN_COUNT = 4*9-2;
    private static final int MAX_INV_COUNT = MAX_PATTERN_COUNT + 2;
    private static final int SLOT_MANUAL = MAX_INV_COUNT - 1;
    private static final int SLOT_CIRCUIT = MAX_INV_COUNT - 2;

    private BaseActionSource requestSource = null;
    private @Nullable AENetworkProxy gridProxy = null;

    // Each pattern slot in the crafting input hatch has its own internal inventory
    public static class PatternSlot {
        public interface SharedItemGetter {
            ItemStack[] getSharedItem();
        }

        private ItemStack pattern;
        private ICraftingPatternDetails patternDetails;
        private List<ItemStack> itemInventory;
        private List<FluidStack> fluidInventory;
        private SharedItemGetter sharedItemGetter;

        public PatternSlot(ItemStack pattern, World world, SharedItemGetter getter) {
            this.pattern = pattern;
            this.patternDetails = ((ICraftingPatternItem) Objects.requireNonNull(pattern.getItem())).getPatternForItem(pattern, world);
            this.itemInventory = new ArrayList<>();
            this.fluidInventory = new ArrayList<>();
            this.sharedItemGetter = getter;
        }

        public PatternSlot(NBTTagCompound nbt, World world, SharedItemGetter getter) {
            this.pattern = ItemStack.loadItemStackFromNBT(nbt.getCompoundTag("pattern"));
            this.patternDetails = ((ICraftingPatternItem) Objects.requireNonNull(pattern.getItem())).getPatternForItem(pattern, world);
            this.itemInventory = new ArrayList<>();
            this.fluidInventory = new ArrayList<>();
            this.sharedItemGetter = getter;
            NBTTagList inv = nbt.getTagList("inventory", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < inv.tagCount(); i++) {
                itemInventory.add(ItemStack.loadItemStackFromNBT(inv.getCompoundTagAt(i)));
            }
            NBTTagList fluidInv = nbt.getTagList("fluidInventory", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < fluidInv.tagCount(); i++) {
                fluidInventory.add(FluidStack.loadFluidStackFromNBT(fluidInv.getCompoundTagAt(i)));
            }
        }

        public boolean hasChanged(ItemStack newPattern, World world) {
            return newPattern == null || (
                !ItemStack.areItemStacksEqual(pattern, newPattern) &&
                !this.patternDetails.equals(
                    ((ICraftingPatternItem) Objects.requireNonNull(pattern.getItem())).getPatternForItem(pattern, world)
                ));
        }

        public ItemStack[] getItemInputs() {
            return ArrayUtils.addAll(
                itemInventory.toArray(new ItemStack[0]),
                sharedItemGetter.getSharedItem()
            );
        }

        public FluidStack[] getFluidInputs() {
            return fluidInventory.toArray(new FluidStack[0]);
        }

        public ICraftingPatternDetails getPatternDetails() {
            return patternDetails;
        }

        public void refund(AENetworkProxy proxy, BaseActionSource src) throws GridAccessException {
            IMEMonitor<IAEItemStack> sg = proxy.getStorage().getItemInventory();
            for (ItemStack itemStack : itemInventory) {
                if (itemStack == null || itemStack.stackSize == 0) continue;
                IAEItemStack rest = Platform.poweredInsert(proxy.getEnergy(), sg,
                    AEApi.instance()
                        .storage()
                        .createItemStack(itemStack),
                    src
                );
                itemStack.stackSize = rest != null && rest.getStackSize() > 0 ? (int) rest.getStackSize() : 0;
            }
            IMEMonitor<IAEFluidStack> fsg = proxy.getStorage().getFluidInventory();
            for (FluidStack fluidStack : fluidInventory) {
                if (fluidStack == null || fluidStack.amount == 0) continue;
                IAEFluidStack rest = Platform.poweredInsert(proxy.getEnergy(), fsg,
                    AEApi.instance()
                        .storage()
                        .createFluidStack(fluidStack),
                    src
                );
                fluidStack.amount = rest != null && rest.getStackSize() > 0 ? (int) rest.getStackSize() : 0;
            }
        }

        public void insertItemsAndFluids(InventoryCrafting inventoryCrafting) {
            for (int i = 0; i < inventoryCrafting.getSizeInventory(); ++i) {
                ItemStack itemStack = inventoryCrafting.getStackInSlot(i);
                if (itemStack == null) continue;

                boolean inserted = false;
                if (itemStack.getItem() instanceof ItemFluidPacket) { // insert fluid
                    var fluidStack = ItemFluidPacket.getFluidStack(itemStack);
                    if (fluidStack == null) continue;
                    for (var fluid : fluidInventory) {
                        if (fluid.isFluidEqual(fluidStack)) {
                            fluid.amount += fluidStack.amount;
                            inserted = true;
                            break;
                        }
                    }
                    if (!inserted) {
                        fluidInventory.add(fluidStack);
                    }
                } else { // insert item
                    for (var item : itemInventory) {
                        if (itemStack.isItemEqual(item)) {
                            item.stackSize += itemStack.stackSize;
                            inserted = true;
                            break;
                        }
                    }
                    if (!inserted) {
                        itemInventory.add(itemStack);
                    }
                }
            }
        }

        public NBTTagCompound writeToNBT(NBTTagCompound nbt) {
            nbt.setTag("pattern", pattern.writeToNBT(new NBTTagCompound()));

            NBTTagList itemInventoryNbt = new NBTTagList();
            for (ItemStack itemStack : this.itemInventory) {
                itemInventoryNbt.appendTag(itemStack.writeToNBT(new NBTTagCompound()));
            }
            nbt.setTag("inventory", itemInventoryNbt);

            NBTTagList fluidInventoryNbt = new NBTTagList();
            for (FluidStack fluidStack : fluidInventory) {
                fluidInventoryNbt.appendTag(fluidStack.writeToNBT(new NBTTagCompound()));
            }
            nbt.setTag("fluidInventory", fluidInventoryNbt);

            return nbt;
        }
    }


    // holds all internal inventories
    private PatternSlot[] internalInventory = new PatternSlot[MAX_PATTERN_COUNT];

    // a hash map for faster lookup of pattern slots, not necessarily all valid.
    private Map<ICraftingPatternDetails, PatternSlot> patternDetailsPatternSlotMap = new HashMap<>(MAX_PATTERN_COUNT);

    private boolean initialPatternSyncDone = false;
    private boolean justHadNewItems = false;

    public GT_MetaTileEntity_Hatch_CraftingInput_ME(int aID, String aName, String aNameRegional) {
        super(
            aID,
            aName,
            aNameRegional,
            1,
            MAX_INV_COUNT,
            new String[] { "Advanced item input for Multiblocks", "Processes patterns directly from ME" });
        disableSort = true;
    }

    public GT_MetaTileEntity_Hatch_CraftingInput_ME(String aName, int aTier, String[] aDescription,
                                               ITexture[][][] aTextures) {
        super(aName, aTier, MAX_INV_COUNT, aDescription, aTextures);
        disableSort = true;
    }

    @Override
    public MetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_Hatch_CraftingInput_ME(mName, mTier, mDescriptionArray, mTextures);
    }

    @Override
    public ITexture[] getTexturesActive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_INPUT_HATCH_ACTIVE) };
    }

    @Override
    public ITexture[] getTexturesInactive(ITexture aBaseTexture) {
        return new ITexture[] { aBaseTexture, TextureFactory.of(OVERLAY_ME_INPUT_HATCH) };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTimer) {
        super.onPostTick(aBaseMetaTileEntity, aTimer);

        if (!initialPatternSyncDone && aTimer % 10 == 0 && getBaseMetaTileEntity().isServerSide()) {
            try {
                getProxy().getGrid().postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
            } catch (GridAccessException ignored) {
                return;
            }
            initialPatternSyncDone = true;
        }
    }

    @Override
    public void onFirstTick(IGregTechTileEntity aBaseMetaTileEntity) {
        super.onFirstTick(aBaseMetaTileEntity);
        getProxy().onReady();
    }

    @Override
    public IGridNode getGridNode(ForgeDirection dir) {
        return getProxy().getNode();
    }

    @Override
    public AECableType getCableConnectionType(ForgeDirection forgeDirection) {
        return isOutputFacing(forgeDirection) ? AECableType.SMART : AECableType.NONE;
    }

    @Override
    public void securityBreak() {}

    @Override
    public AENetworkProxy getProxy() {
        if (gridProxy == null) {
            gridProxy = new AENetworkProxy(
                this,
                "proxy",
                ItemList.Hatch_CraftingInput_Bus_ME.get(1),
                true);
            gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
            if (getBaseMetaTileEntity().getWorld() != null) gridProxy.setOwner(
                getBaseMetaTileEntity().getWorld()
                    .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName()));
        }

        return this.gridProxy;
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(
            getBaseMetaTileEntity().getWorld(),
            getBaseMetaTileEntity().getXCoord(),
            getBaseMetaTileEntity().getYCoord(),
            getBaseMetaTileEntity().getZCoord()
        );
    }

    @Override
    public void gridChanged() {
        super.gridChanged();
        if (getProxy().isReady()) {
            getProxy().getNode().updateState();
        }
    }

    @Override
    public boolean isPowered() {
        return getProxy() != null && getProxy().isPowered();
    }

    @Override
    public boolean isActive() {
        return getProxy() != null && getProxy().isActive();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        // save internalInventory
        NBTTagList internalInventoryNBT = new NBTTagList();
        for (int i = 0; i < internalInventory.length; i++) {
            if (internalInventory[i] != null) {
                NBTTagCompound internalInventorySlotNBT = new NBTTagCompound();
                internalInventorySlotNBT.setInteger("patternSlot", i);
                internalInventorySlotNBT.setTag("patternSlotNBT", internalInventory[i].writeToNBT(new NBTTagCompound()));
                internalInventoryNBT.appendTag(internalInventorySlotNBT);
            }
        }
        aNBT.setTag("internalInventory", internalInventoryNBT);

        if (GregTech_API.mAE2) {
            getProxy().writeToNBT(aNBT);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        // load internalInventory
        NBTTagList internalInventoryNBT = aNBT.getTagList("internalInventory", Constants.NBT.TAG_COMPOUND);
        for (int i = 0; i < internalInventoryNBT.tagCount(); i++) {
            NBTTagCompound internalInventorySlotNBT = internalInventoryNBT.getCompoundTagAt(i);
            int patternSlot = internalInventorySlotNBT.getInteger("patternSlot");
            internalInventory[patternSlot] =
                new PatternSlot(
                    internalInventorySlotNBT.getCompoundTag("patternSlotNBT"),
                    getBaseMetaTileEntity().getWorld(),
                    this::getSharedItems
                );
        }

        // reconstruct patternDetailsPatternSlotMap
        patternDetailsPatternSlotMap.clear();
        for (PatternSlot patternSlot : internalInventory) {
            if (patternSlot != null) {
                patternDetailsPatternSlotMap.put(patternSlot.getPatternDetails(), patternSlot);
            }
        }

        if (GregTech_API.mAE2) {
            getProxy().readFromNBT(aNBT);
        }
    }

    @Override
    public boolean isGivingInformation() {
        return true;
    }

    private String describePattern(ICraftingPatternDetails patternDetails) {
        return Arrays.stream(patternDetails.getCondensedOutputs())
            .map(aeItemStack -> aeItemStack.getItem().getItemStackDisplayName(aeItemStack.getItemStack()))
            .collect(Collectors.joining(", "));
    }

    @Override
    public String[] getInfoData() {
        if (GregTech_API.mAE2) {
            var ret = new ArrayList<String>();
            ret.add("The bus is " + ((getProxy() != null && getProxy().isActive()) ? EnumChatFormatting.GREEN + "online"
                : EnumChatFormatting.RED + "offline" + getAEDiagnostics()) + EnumChatFormatting.RESET);
            ret.add("Internal Inventory: ");
            var i = 0;
            for (var slot : internalInventory) {
                if (slot == null) continue;
                IWideReadableNumberConverter nc = ReadableNumberConverter.INSTANCE;

                i += 1;
                ret.add("Slot " + i + " " + EnumChatFormatting.BLUE + describePattern(slot.patternDetails) + EnumChatFormatting.RESET);
                for(var item : slot.itemInventory) {
                    if (item == null || item.stackSize == 0) continue;
                    ret.add(item.getItem().getItemStackDisplayName(item) + ": "
                        + EnumChatFormatting.GOLD
                        + nc.toWideReadableForm(item.stackSize)
                        + EnumChatFormatting.RESET
                    );
                }
                for(var fluid : slot.fluidInventory) {
                    if (fluid == null || fluid.amount == 0) continue;
                    ret.add(fluid.getLocalizedName() + ": "
                        + EnumChatFormatting.AQUA
                        + nc.toWideReadableForm(fluid.amount)
                        + EnumChatFormatting.RESET
                    );
                }
            }
            return ret.toArray(new String[0]);
        } else return new String[]{};
    }

    @Override
    public boolean allowPullStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
                                  ItemStack aStack) {
        return false;
    }

    @Override
    public boolean allowPutStack(IGregTechTileEntity aBaseMetaTileEntity, int aIndex, ForgeDirection side,
                                 ItemStack aStack) {
        return false;
    }

    @Override
    public int getCircuitSlot() {
        return SLOT_CIRCUIT;
    }

    @Override
    public int getCircuitSlotX() {
        return 152;
    }

    @Override
    public int getCircuitSlotY() {
        return 64;
    }

    @Override
    public boolean isValidSlot(int aIndex) {
        return true;
    }

    @Override
    public void addUIWidgets(ModularWindow.@NotNull Builder builder, UIBuildContext buildContext) {
        builder
            .widget(
                SlotGroup.ofItemHandler(inventoryHandler, 9)
                    .startFromSlot(0)
                    .endAtSlot(MAX_PATTERN_COUNT - 1)
                    .phantom(false)
                    .background(getGUITextureSet().getItemSlot(), GT_UITextures.OVERLAY_SLOT_PATTERN_ME)
                    .widgetCreator(slot -> new SlotWidget(slot)
                        .setFilter(itemStack -> itemStack.getItem() instanceof ICraftingPatternItem)
                        .setChangeListener(() -> onPatternChange(slot))
                    )
                    .build()
                    .setPos(7, 9)
            )
            .widget(
                new SlotWidget(inventoryHandler, SLOT_MANUAL)
                    .setShiftClickPriority(11)
                    .setBackground(getGUITextureSet().getItemSlot())
                    .setPos(133, 63)
            );
    }

    @Override
    public void updateSlots() {
        if (mInventory[SLOT_MANUAL] != null && mInventory[SLOT_MANUAL].stackSize <= 0)
            mInventory[SLOT_MANUAL] = null;
    }

    private BaseActionSource getRequest() {
        if (requestSource == null) requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        return requestSource;
    }
    private void onPatternChange(BaseSlot slot) {
        if (!getBaseMetaTileEntity().isServerSide()) return;

        var world = getBaseMetaTileEntity().getWorld();

        // remove old if applicable
        var originalPattern = internalInventory[slot.getSlotIndex()];
        if (originalPattern != null) {
            if (originalPattern.hasChanged(slot.getStack(), world)) {
                try {
                    originalPattern.refund(getProxy(), getRequest());
                } catch (GridAccessException ignored) {}
                internalInventory[slot.getSlotIndex()] = null;
            } else {
                return; // nothing has changed
            }
        }

        // original does not exist or has changed
        var pattern = slot.getStack();
        if (pattern == null || !(pattern.getItem() instanceof ICraftingPatternItem)) return;

        var patternSlot = new PatternSlot(pattern, world, this::getSharedItems);
        internalInventory[slot.getSlotIndex()] = patternSlot;
        patternDetailsPatternSlotMap.put(patternSlot.getPatternDetails(), patternSlot);

        try {
            getProxy().getGrid().postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
        } catch (GridAccessException ignored) {}
    }

    private ItemStack[] getSharedItems() {
        return new ItemStack[] { mInventory[SLOT_CIRCUIT], mInventory[SLOT_MANUAL] };
    }

    @Override
    public void addGregTechLogo(ModularWindow.Builder builder) {
        builder.widget(
            new DrawableWidget().setDrawable(getGUITextureSet().getGregTechLogo())
                .setSize(17, 17)
                .setPos(80, 63));
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor,
                             IWailaConfigHandler config) {
        NBTTagCompound tag = accessor.getNBTData();
        if (tag.hasKey("inventory")) {
            var inventory = tag.getTagList("inventory", Constants.NBT.TAG_COMPOUND);
            for (int i = 0; i < inventory.tagCount(); ++i) {
                var item = inventory.getCompoundTagAt(i);
                var name = item.getString("name");
                var amount = item.getInteger("amount");
                currenttip.add(name + ": " + EnumChatFormatting.GOLD + ReadableNumberConverter.INSTANCE.toWideReadableForm(amount) + EnumChatFormatting.RESET);
            }
        }
        super.getWailaBody(itemStack, currenttip, accessor, config);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
                                int z) {

        NBTTagList inventory = new NBTTagList();
        HashMap<String, Integer> nameToAmount = new HashMap<>();
        for (Iterator<PatternSlot> it = inventories(); it.hasNext(); ) {
            var i = it.next();
            for (var item : i.itemInventory) {
                if (item != null && item.stackSize > 0) {
                    var name = item.getDisplayName();
                    var amount = nameToAmount.getOrDefault(name, 0);
                    nameToAmount.put(name, amount + item.stackSize);
                }
            }
            for (var fluid : i.fluidInventory) {
                if (fluid != null && fluid.amount > 0) {
                    var name = fluid.getLocalizedName();
                    var amount = nameToAmount.getOrDefault(name, 0);
                    nameToAmount.put(name, amount + fluid.amount);
                }
            }
        }
        for (var entry : nameToAmount.entrySet()) {
            var item = new NBTTagCompound();
            item.setString("name", entry.getKey());
            item.setInteger("amount", entry.getValue());
            inventory.appendTag(item);
        }

        tag.setTag("inventory", inventory);
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
    }

    @Override
    public void provideCrafting(ICraftingProviderHelper craftingTracker) {
        if (!isActive()) return;

        for (PatternSlot slot : internalInventory) {
            if (slot == null) continue;
            craftingTracker.addCraftingOption(this, slot.getPatternDetails());
        }
    }

    @Override
    public boolean pushPattern(ICraftingPatternDetails patternDetails, InventoryCrafting table) {
        if (!isActive()) return false;
        patternDetailsPatternSlotMap.get(patternDetails).insertItemsAndFluids(table);
        justHadNewItems = true;
        return true;
    }

    @Override
    public boolean isBusy() {
        return false;
    }

    public Iterator<PatternSlot> inventories() {
        return Arrays.stream(internalInventory).filter(Objects::nonNull).iterator();
    }

    @Override
    public void onBlockDestroyed() {
        for(var slot : internalInventory) {
            if (slot == null) continue;
            try {
                slot.refund(getProxy(), getRequest());
            } catch (GridAccessException ignored) {}
        }

        super.onBlockDestroyed();
    }

    public boolean justUpdated() {
        var ret = justHadNewItems;
        justHadNewItems = false;
        return ret;
    }
}
