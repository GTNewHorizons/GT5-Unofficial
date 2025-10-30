package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.coords.Position;
import com.gtnewhorizon.structurelib.coords.WorldCoords;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.mojang.authlib.GameProfile;

import appeng.api.AEApi;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.api.networking.security.IActionHost;
import appeng.api.util.DimensionalCoord;
import appeng.core.worlddata.WorldData;
import appeng.items.misc.ItemEncodedPattern;
import appeng.me.GridAccessException;
import appeng.me.GridNode;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.Platform;
import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.casing.Casings;
import gregtech.api.enums.ChatMessage;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Textures;
import gregtech.api.enums.VoidingMode;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.net.GTPacketLMACraftingFX;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.structure.IStructureInstance;
import gregtech.api.structure.IStructureProvider;
import gregtech.api.structure.StructureWrapper;
import gregtech.api.structure.StructureWrapperInstanceInfo;
import gregtech.api.structure.StructureWrapperTooltipBuilder;
import gregtech.api.util.GTLog;
import gregtech.api.util.GTTextBuilder;
import gregtech.api.util.GTUtility;
import gregtech.api.util.ItemEjectionHelper;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTELargeMolecularAssembler extends MTEExtendedPowerMultiBlockBase<MTELargeMolecularAssembler>
    implements ICraftingProvider, IActionHost, IGridProxyable, ISurvivalConstructable,
    IStructureProvider<MTELargeMolecularAssembler> {

    protected final StructureWrapper<MTELargeMolecularAssembler> structure;
    protected final StructureWrapperInstanceInfo<MTELargeMolecularAssembler> structureInstanceInfo;

    private static final int EU_PER_TICK_BASIC = 16;
    private static final int EU_PER_TICK_CRAFTING = 64;

    private List<ICraftingPatternDetails> cachedPatternDetails = new ArrayList<>();
    private final Map<ItemStack, Pair<NBTTagCompound, ICraftingPatternDetails>> patternDetailCache = new IdentityHashMap<>();

    private final ArrayDeque<ItemStack> pendingCrafts = new ArrayDeque<>();

    private boolean hiddenCraftingFX, active;

    private AENetworkProxy gridProxy;

    private static class OverclockInfo {

        public final int craftingProgressTime;
        public final long craftingEUt;
        public final int parallels;

        public OverclockInfo(int craftingProgressTime, long craftingEUt, int parallels) {
            this.craftingProgressTime = craftingProgressTime;
            this.craftingEUt = craftingEUt;
            this.parallels = parallels;
        }
    }

    private OverclockInfo overclockInfo;

    public MTELargeMolecularAssembler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);

        structure = new StructureWrapper<>(this);
        structureInstanceInfo = null;
    }

    protected MTELargeMolecularAssembler(MTELargeMolecularAssembler prototype) {
        super(prototype.mName);

        structure = prototype.structure;
        structureInstanceInfo = new StructureWrapperInstanceInfo<>(structure);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTELargeMolecularAssembler(this);
    }

    @Override
    public String[][] getDefinition() {
        // spotless:off
        return transpose(new String[][] {
            { "CCCCC", "CGGGC", "CGGGC", "CGGGC", "CCCCC" },
            { "CGGGC", "G---G", "G---G", "G---G", "CGGGC" },
            { "CGGGC", "G---G", "G-X-G", "G---G", "CGGGC" },
            { "CGGGC", "G---G", "G---G", "G---G", "CGGGC" },
            { "CC~CC", "CGGGC", "CGGGC", "CGGGC", "CCCCC" },
        });
        // spotless:on
    }

    @Override
    public IStructureDefinition<MTELargeMolecularAssembler> compile(String[][] definition) {
        structure.addCasing('C', Casings.RobustTungstensteelMachineCasing)
            .withHatches(1, 19, Arrays.asList(Energy, InputBus, Maintenance, OutputBus));
        structure.addCasing('G', Casings.VibrantGlass);

        structure.addSocket('X', '-');

        return structure.buildStructure(definition);
    }

    @Override
    public IStructureInstance<MTELargeMolecularAssembler> getStructureInstance() {
        return structureInstanceInfo;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean active, boolean redstoneLevel) {
        if (side == facing) {
            return new ITexture[] { Casings.RobustTungstensteelMachineCasing.getCasingTexture(), TextureFactory
                .builder()
                .addIcon(
                    baseMetaTileEntity.isAllowedToWork() && this.active ? Textures.BlockIcons.OVERLAY_ME_HATCH_ACTIVE
                        : Textures.BlockIcons.OVERLAY_ME_HATCH)
                .extFacing()
                .build() };
        }
        return new ITexture[] { Casings.RobustTungstensteelMachineCasing.getCasingTexture() };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {
            syncAEProxyActive(aBaseMetaTileEntity);
            issuePatternChangeIfNeeded(aTick);
        }
    }

    private void updateOverclockInfo() {
        int craftingProgressTime = 20;
        long craftingEUt = EU_PER_TICK_CRAFTING;

        // Tier EU_PER_TICK_CRAFTING == 2
        int extraTier = Math.max(0, GTUtility.getTier(getMaxInputVoltage()) - 2);

        // The first two Overclocks reduce the Finish time to 0.5s and 0.25s
        for (int i = 0; i < 2 && extraTier > 0; i++, extraTier--) {
            craftingProgressTime /= 2;
            craftingEUt *= 4;
        }

        int parallels = 2 << extraTier;
        craftingEUt = craftingEUt << 2 * extraTier;

        overclockInfo = new OverclockInfo(craftingProgressTime, craftingEUt, parallels);
    }

    @Override
    protected boolean shouldCheckRecipeThisTick() {
        return !pendingCrafts.isEmpty() || super.shouldCheckRecipeThisTick();
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        updateOverclockInfo();

        if (pendingCrafts.isEmpty()) {
            this.mMaxProgresstime = 0;
            this.lEUt = 0;
            this.mEfficiency = 0;

            return CheckRecipeResultRegistry.NO_RECIPE;
        }

        ItemEjectionHelper ejectionHelper = new ItemEjectionHelper(this);

        // Check if we can fit the outputs, if not don't craft anything
        if (ejectionHelper.ejectItems(new ArrayList<>(pendingCrafts), 1) == 0) {
            return CheckRecipeResultRegistry.ITEM_OUTPUT_FULL;
        }

        mOutputItems = pendingCrafts.toArray(GTValues.emptyItemStackArray);
        pendingCrafts.clear();

        mMaxProgresstime = overclockInfo.craftingProgressTime;
        lEUt = -overclockInfo.craftingEUt;
        mEfficiency = 10000 - (getIdealStatus() - getRepairStatus()) * 1000;

        addCraftingFX(mOutputItems[0]);

        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    public ArrayList<ItemStack> getDroppedItem() {
        return super.getDroppedItem();
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        aNBT.setInteger("version", 1);

        aNBT.setBoolean("config:hiddenCraftingFX", hiddenCraftingFX);

        NBTTagCompound proxyTag = new NBTTagCompound();
        getProxy().writeToNBT(proxyTag);
        aNBT.setTag("proxy", proxyTag);

        NBTTagList pendingCrafts = new NBTTagList();

        for (ItemStack stack : this.pendingCrafts) {
            if (stack.stackSize <= 0) break;

            pendingCrafts.appendTag(stack.writeToNBT(new NBTTagCompound()));
        }

        aNBT.setTag("pendingCrafts", pendingCrafts);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        switch (aNBT.getInteger("version")) {
            case 0 -> {
                hiddenCraftingFX = aNBT.getBoolean("config:hiddenCraftingFX");
                if (aNBT.hasKey("proxy")) {
                    getProxy().readFromNBT(aNBT.getCompoundTag("proxy"));
                }

                if (aNBT.getTag("cachedOutputs") instanceof NBTTagList isList) {
                    for (int i = 0; i < isList.tagCount(); i++) {
                        NBTTagCompound tag = isList.getCompoundTagAt(i);
                        NBTTagCompound stackTag = tag.getCompoundTag("itemStack");

                        ItemStack itemStack = GTUtility.loadItem(stackTag);
                        // If someone's somehow storing more than 2.7 billion items in an LMA, this truncation is their
                        // problem :kekw:
                        itemStack.stackSize = GTUtility.longToInt(tag.getLong("size"));

                        // Stick any cachedOutputs back into the pendingCrafts queue.
                        // This isn't very elegant, but it's better than having a ton of migration code specifically to
                        // prevent these items from being voided (which will probably never happen because no one uses
                        // the LMA :kekw:).
                        pendingCrafts.add(itemStack);
                    }
                }
            }
            case 1 -> {
                hiddenCraftingFX = aNBT.getBoolean("config:hiddenCraftingFX");

                if (aNBT.hasKey("proxy")) {
                    getProxy().readFromNBT(aNBT.getCompoundTag("proxy"));
                }

                for (NBTTagCompound tag : GTUtility.getTagList(aNBT, "pendingCrafts")) {
                    ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
                    if (stack != null) pendingCrafts.add(stack);
                }
            }
            default -> {

            }
        }
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);

        NBTTagList pendingCrafts = new NBTTagList();

        for (ItemStack stack : this.pendingCrafts) {
            if (stack.stackSize <= 0) break;

            pendingCrafts.appendTag(stack.writeToNBT(new NBTTagCompound()));
        }

        tag.setTag("pendingCrafts", pendingCrafts);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> ss, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, ss, accessor, config);

        List<ItemStack> stacks = new ArrayList<>();

        for (NBTTagCompound tag : GTUtility.getTagList(accessor.getNBTData(), "pendingCrafts")) {
            ItemStack stack = ItemStack.loadItemStackFromNBT(tag);
            if (stack != null) stacks.add(stack);
        }

        if (stacks.isEmpty()) {
            ss.add(GTUtility.translate("GT5U.gui.text.lma.empty"));
        } else {
            if (!accessor.getPlayer()
                .isSneaking()) {
                ss.add(GTUtility.translate("GT5U.gui.text.sneak_for_more_info"));
            } else {
                ss.add(
                    new GTTextBuilder(ChatMessage.LMAHeader).setBase(EnumChatFormatting.GRAY)
                        .addNumber(stacks.size())
                        .toString());

                for (ItemStack stack : stacks) {
                    ss.add(
                        new GTTextBuilder("GT5U.gui.text.lma.entry").setBase(EnumChatFormatting.GRAY)
                            .add(null, stack.getDisplayName())
                            .addNumber(stack.stackSize)
                            .toString());
                }
            }
        }
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTELargeMolecularAssembler> tt = new StructureWrapperTooltipBuilder<>(
            this.structure);

        tt.addMachineType("Molecular Assembler, LMA")
            .addInfo("Consumes §a" + EU_PER_TICK_BASIC + "§7 EU/t prior to overclocks.")
            .addInfo("Crafts take §fone§7 second to finish initially.")
            .addInfo("The first two overclocks halve crafting time, for a total of §f0.25§7 seconds.")
            .addInfo("Subsequent overclocks double the number of items crafted per batch.")
            .addSeparator()
            .addInfo("Place crafting patterns in the input busses.")
            .addInfo("The controller must be enabled for it connect to an ME cable.")
            .addInfo("Once crafted, items and containers are placed into the output busses.");

        tt.beginStructureBlock(true)
            .addAllCasingInfo()
            .toolTipFinisher();

        return tt;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structure.checkStructure(this);
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        structure.construct(this, trigger, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack trigger, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;

        return structure.survivalConstruct(this, trigger, elementBudget, env);
    }

    @Override
    public IStructureDefinition<MTELargeMolecularAssembler> getStructureDefinition() {
        return structure.getStructureDefinition();
    }

    private void addCraftingFX(ItemStack itemStack) {
        if (hiddenCraftingFX) return;

        Position<WorldCoords> displayPoint = structure.getSocket(this, "main", 'X');

        if (displayPoint != null) {
            GTPacketLMACraftingFX packet = new GTPacketLMACraftingFX(
                displayPoint.x,
                displayPoint.y,
                displayPoint.z,
                mMaxProgresstime + 10,
                AEApi.instance()
                    .storage()
                    .createItemStack(itemStack));

            GTValues.NW.sendPacketToAllPlayersInRange(
                getBaseMetaTileEntity().getWorld(),
                packet,
                displayPoint.x,
                displayPoint.z);
        }
    }

    @Override
    public boolean isBatchModeEnabled() {
        return false;
    }

    @Override
    public VoidingMode getVoidingMode() {
        return VoidingMode.VOID_NONE;
    }

    @Override
    public boolean protectsExcessItem() {
        return true;
    }

    @Override
    public void onBlockDestroyed() {
        super.onBlockDestroyed();

        IGregTechTileEntity igte = getBaseMetaTileEntity();

        World world = igte.getWorld();
        int x = igte.getXCoord();
        int y = igte.getYCoord();
        int z = igte.getZCoord();

        // The player shouldn't have these items without crafting them, but it's either this or we void the items since
        // we can't undo the recipes. There isn't much of a reason to exploit this behaviour, it's faster to just use
        // the LMA properly.
        for (ItemStack stack : pendingCrafts) {
            EntityItem item = new EntityItem(world, x, y, z, stack);

            world.spawnEntityInWorld(item);
        }

        pendingCrafts.clear();
    }

    private void issuePatternChangeIfNeeded(long tick) {
        if (tick % 20 != 0) {
            return;
        }

        Set<ItemStack> inputs = GTUtility.filterValidMTEs(mInputBusses)
            .stream()
            .filter(bus -> !(bus instanceof MTEHatchCraftingInputME))
            .flatMap(
                bus -> IntStream.range(0, bus.getSizeInventory())
                    .mapToObj(bus::getStackInSlot))
            .filter(Objects::nonNull)
            .collect(Collectors.toSet());

        List<ICraftingPatternDetails> patterns = inputs.stream()
            .map(is -> {
                if (!(is.getItem() instanceof ItemEncodedPattern pattern)) {
                    return null;
                }
                var entry = patternDetailCache.get(is);
                if (entry == null || !Objects.equals(is.getTagCompound(), entry.getKey())) {
                    ICraftingPatternDetails detail = pattern.getPatternForItem(is, getBaseMetaTileEntity().getWorld());
                    patternDetailCache.put(is, Pair.of(is.getTagCompound(), detail));
                    return detail;
                }
                return entry.getValue();
            })
            .filter(Objects::nonNull)
            .filter(it -> ((ICraftingPatternDetails) it).isCraftable())
            .collect(Collectors.toList());
        if (patterns.equals(cachedPatternDetails)) return;
        cachedPatternDetails = patterns;
        patternDetailCache.keySet()
            .retainAll(inputs);

        try {
            AENetworkProxy proxy = getProxy();
            proxy.getGrid()
                .postEvent(new MENetworkCraftingPatternChange(this, getProxy().getNode()));
        } catch (GridAccessException ignored) {}
    }

    private void syncAEProxyActive(IGregTechTileEntity baseMetaTileEntity) {
        AENetworkProxy proxy = getProxy();

        if (baseMetaTileEntity.isAllowedToWork()) {
            if (!proxy.isReady()) {
                proxy.onReady();
                IGridNode node = proxy.getNode();
                if (node == null) return;
                if (node.getPlayerID() == -1) {
                    GTLog.out.printf(
                        "Found a LMA at %s without valid AE playerID.\n",
                        ((BaseMetaTileEntity) baseMetaTileEntity).getLocation());
                    GTLog.out.println("Try to recover playerID with UUID: " + baseMetaTileEntity.getOwnerUuid());
                    // recover ID from old version
                    int playerAEID = WorldData.instance()
                        .playerData()
                        .getPlayerID(
                            new GameProfile(baseMetaTileEntity.getOwnerUuid(), baseMetaTileEntity.getOwnerName()));
                    node.setPlayerID(playerAEID);
                    ((GridNode) node).setLastSecurityKey(-1);
                    node.updateState(); // refresh the security connection
                    GTLog.out.println("Now it has playerID: " + playerAEID);
                }
            }

            if (proxy.getConnectableSides()
                .isEmpty()) {
                proxy.setValidSides(EnumSet.complementOf(EnumSet.of(ForgeDirection.UNKNOWN)));
            }

            if (this.active != proxy.isActive()) {
                this.active = proxy.isActive();
                baseMetaTileEntity.issueTileUpdate();
            }
        } else {
            if (!proxy.getConnectableSides()
                .isEmpty()) {
                proxy.setValidSides(EnumSet.noneOf(ForgeDirection.class));
            }
        }
    }

    @Override
    public NBTTagCompound getDescriptionData() {
        NBTTagCompound tag = super.getDescriptionData();

        if (tag == null) tag = new NBTTagCompound();

        tag.setBoolean("isActive", active);

        return tag;
    }

    @Override
    public void onDescriptionPacket(NBTTagCompound data) {
        super.onDescriptionPacket(data);

        active = data.getBoolean("isActive");
        getBaseMetaTileEntity().issueTextureUpdate();
    }

    @Override
    public boolean pushPattern(ICraftingPatternDetails patternDetails, InventoryCrafting table) {
        World w = getBaseMetaTileEntity().getWorld();
        ItemStack mainOutput = patternDetails.getOutput(table, w);
        if (mainOutput == null) return false;

        if (getRemainingParallels() <= 0) return false;

        FMLCommonHandler.instance()
            .firePlayerCraftingEvent(Platform.getPlayer((WorldServer) w), mainOutput, table);

        if (!pendingCrafts.isEmpty()) {
            ItemStack last = pendingCrafts.getLast();

            if (GTUtility.areStacksEqual(last, mainOutput)) {
                int toAdd = (int) Math.min(mainOutput.stackSize, (long) Integer.MAX_VALUE - last.stackSize);

                last.stackSize += toAdd;
                mainOutput.stackSize -= toAdd;
            }
        }

        if (mainOutput.stackSize > 0) {
            pendingCrafts.add(mainOutput);
        }

        for (ItemStack stack : GTUtility.getInventoryList(table)) {
            if (stack == null) continue;

            ItemStack container = Platform.getContainerItem(stack);

            if (container == null) continue;

            // Try to immediately eject the container. If it fails, add it to the pendingCrafts (the player will just
            // have to lose the one parallel :caught:)
            if (!addOutputAtomic(container)) {
                pendingCrafts.add(container);
            }
        }

        return true;
    }

    @Override
    public boolean isBusy() {
        return getRemainingParallels() <= 0;
    }

    private int getRemainingParallels() {
        int usedParallels = pendingCrafts.stream()
            .mapToInt(s -> s.stackSize)
            .sum();

        int availableParallels = overclockInfo == null ? 0 : overclockInfo.parallels;

        return availableParallels - usedParallels;
    }

    @Override
    public void provideCrafting(ICraftingProviderHelper craftingTracker) {
        AENetworkProxy proxy = getProxy();
        if (proxy.isReady()) {
            for (ICraftingPatternDetails detail : cachedPatternDetails) {
                craftingTracker.addCraftingOption(this, detail);
            }
        }
    }

    @Override
    public @NotNull AENetworkProxy getProxy() {
        if (gridProxy == null) {
            gridProxy = new AENetworkProxy(this, "proxy", getStackForm(1), true);
            gridProxy.setFlags(GridFlags.REQUIRE_CHANNEL);
            if (getBaseMetaTileEntity().getWorld() != null) {
                EntityPlayer player = getBaseMetaTileEntity().getWorld()
                    .getPlayerEntityByName(getBaseMetaTileEntity().getOwnerName());
                gridProxy.setOwner(player);
            }
        }
        return gridProxy;
    }

    @Override
    public IGridNode getGridNode(ForgeDirection dir) {
        return getActionableNode();
    }

    @Override
    public void securityBreak() {
        getBaseMetaTileEntity().disableWorking();
    }

    @Override
    public DimensionalCoord getLocation() {
        return new DimensionalCoord(
            getBaseMetaTileEntity().getWorld(),
            getBaseMetaTileEntity().getXCoord(),
            getBaseMetaTileEntity().getYCoord(),
            getBaseMetaTileEntity().getZCoord());
    }

    @Override
    public IGridNode getActionableNode() {
        return getProxy().getNode();
    }

    @Override
    protected boolean useMui2() {
        return false;
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(
            new ButtonWidget().setOnClick((clickData, widget) -> hiddenCraftingFX = !hiddenCraftingFX)
                .setPlayClickSound(true)
                .setBackground(() -> {
                    List<UITexture> ret = new ArrayList<>();
                    if (hiddenCraftingFX) {
                        ret.add(GTUITextures.BUTTON_STANDARD);
                        ret.add(GTUITextures.OVERLAY_BUTTON_LMA_ANIMATION_OFF);
                    } else {
                        ret.add(GTUITextures.BUTTON_STANDARD_PRESSED);
                        ret.add(GTUITextures.OVERLAY_BUTTON_LMA_ANIMATION_ON);
                    }
                    return ret.toArray(new IDrawable[0]);
                })
                .attachSyncer(
                    new FakeSyncWidget.BooleanSyncer(() -> hiddenCraftingFX, val -> hiddenCraftingFX = val),
                    builder)
                .addTooltip(StatCollector.translateToLocal("GT5U.gui.text.lma_craftingfx"))
                .setTooltipShowUpDelay(TOOLTIP_DELAY)
                .setPos(80, 91)
                .setSize(16, 16));
    }
}
