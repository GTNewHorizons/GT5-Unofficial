package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.metatileentity.BaseTileEntity.TOOLTIP_DELAY;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

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

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;

import appeng.api.AEApi;
import appeng.api.networking.GridFlags;
import appeng.api.networking.IGridNode;
import appeng.api.networking.crafting.ICraftingPatternDetails;
import appeng.api.networking.crafting.ICraftingProvider;
import appeng.api.networking.crafting.ICraftingProviderHelper;
import appeng.api.networking.events.MENetworkCraftingPatternChange;
import appeng.api.networking.security.BaseActionSource;
import appeng.api.networking.security.IActionHost;
import appeng.api.networking.security.MachineSource;
import appeng.api.storage.IMEMonitor;
import appeng.api.storage.data.IAEItemStack;
import appeng.api.util.DimensionalCoord;
import appeng.core.worlddata.WorldData;
import appeng.items.misc.ItemEncodedPattern;
import appeng.me.GridAccessException;
import appeng.me.GridNode;
import appeng.me.helpers.AENetworkProxy;
import appeng.me.helpers.IGridProxyable;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import com.gtnewhorizon.gtnhlib.blockpos.BlockPos;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizons.modularui.api.drawable.IDrawable;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.common.FMLCommonHandler;
import gregtech.api.casing.Casings;
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
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.tileentities.machines.MTEHatchCraftingInputME;
import it.unimi.dsi.fastutil.objects.Object2LongMap;
import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;

public class MTELargeMolecularAssembler extends MTEExtendedPowerMultiBlockBase<MTELargeMolecularAssembler>
    implements ICraftingProvider, IActionHost, IGridProxyable, ISurvivalConstructable,
    IStructureProvider<MTELargeMolecularAssembler> {

    protected final StructureWrapper<MTELargeMolecularAssembler> structure;
    protected final StructureWrapperInstanceInfo<MTELargeMolecularAssembler> structureInstanceInfo;

    private static final String MACHINE_TYPE = "Molecular Assembler, LMA";
    private static final int EU_PER_TICK_BASIC = 16;
    private static final int EU_PER_TICK_CRAFTING = 64;
    private static final String NBT_KEY_CACHED_OUTPUTS = "cachedOutputs";
    private static final String NBT_KEY_CONFIG_HIDDEN_CRAFTING_FX = "config:hiddenCraftingFX";
    private static final int STRUCTURE_HORIZONTAL_OFFSET = 2;
    private static final int STRUCTURE_VERTICAL_OFFSET = 4;
    private static final int STRUCTURE_DEPTH_OFFSET = 0;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTELargeMolecularAssembler> STRUCTURE_DEFINITION = StructureDefinition
        .<MTELargeMolecularAssembler>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] { { "CCCCC", "CGGGC", "CGGGC", "CGGGC", "CCCCC" },
                    { "CGGGC", "G---G", "G---G", "G---G", "CGGGC" }, { "CGGGC", "G---G", "G-X-G", "G---G", "CGGGC" },
                    { "CGGGC", "G---G", "G---G", "G---G", "CGGGC" },
                    { "CC~CC", "CGGGC", "CGGGC", "CGGGC", "CCCCC" }, }))
        .addElement(
            'C',
            ofChain(
                ofHatchAdder(MTELargeMolecularAssembler::addToLargeMolecularAssemblerList, CASING_INDEX, 1),
                buildHatchAdder(MTELargeMolecularAssembler.class).atLeast(Energy, InputBus, Maintenance)
                    .casingIndex(CASING_INDEX)
                    .dot(1)
                    .build(),
                onElementPass(it -> it.casing++, ofBlock(GregTechAPI.sBlockCasings4, 0))))
        .addElement(
            'G',
            ofBlockAnyMeta(
                AEApi.instance()
                    .definitions()
                    .blocks()
                    .quartzVibrantGlass()
                    .maybeBlock()
                    .get()))
        .addElement('X', (IStructureElementCheckOnly<MTELargeMolecularAssembler>) (mte, world, x, y, z) -> {
            if (world.isAirBlock(x, y, z)) {
                mte.craftingDisplayPoint = new CraftingDisplayPoint(world, x, y, z);
                return true;
            }
            return false;
        })
        .build();

    private byte casing;
    private CraftingDisplayPoint craftingDisplayPoint;

    private ItemStack cachedDataOrb;
    private List<List<ItemStack>> cachedAeJobs = new ArrayList<>();
    private boolean aeJobsDirty;

    private List<ICraftingPatternDetails> cachedPatternDetails = new ArrayList<>();
    private Map<ItemStack, Pair<NBTTagCompound, ICraftingPatternDetails>> patternDetailCache = new IdentityHashMap<>();

    private BaseActionSource requestSource;
    private long lastOutputTick;
    private boolean justEjectedContainers = false;

    private final Object2LongOpenHashMap<GTUtility.ItemId> cachedOutputs = new Object2LongOpenHashMap<>();
    private final ArrayDeque<ItemStack> pendingCrafts = new ArrayDeque<>();

    public boolean hiddenCraftingFX;

    private AENetworkProxy gridProxy;

    public MTELargeMolecularAssembler(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);

        structure = new StructureWrapper<>(this);
        structureInstanceInfo = null;
    }

    public MTELargeMolecularAssembler(MTELargeMolecularAssembler prototype) {
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
            .withHatches(1, 19, Arrays.asList(Energy, InputBus, Maintenance));
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
            return new ITexture[] { Casings.RobustTungstensteelMachineCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_ME_HATCH)
                .extFacing()
                .build() };
        }
        return new ITexture[] { Casings.RobustTungstensteelMachineCasing.getCasingTexture() };
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);

        if (aBaseMetaTileEntity.isServerSide()) {
            flushCachedOutputsIfNeeded(aTick);
            syncAEProxyActive(aBaseMetaTileEntity);
            issuePatternChangeIfNeeded(aTick);
        }
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        int craftingProgressTime = 20;
        long craftingEUt = EU_PER_TICK_CRAFTING;

        // Tier EU_PER_TICK_CRAFTING == 2
        int extraTier = Math.max(0, GTUtility.getTier(getMaxInputVoltage()) - 2);

        // The first two Overclocks reduce the Finish time to 0.5s and 0.25s
        for (int i = 0; i < 2 && extraTier > 0; i++, extraTier--) {
            craftingProgressTime /= 2;
            craftingEUt *= 4;
        }

        int parallel = 2 << extraTier;
        craftingEUt = craftingEUt << 2 * extraTier;

        List<ItemStack> outputs = new ArrayList<>();

        while (parallel > 0 && !pendingCrafts.isEmpty()) {
            ItemStack first = pendingCrafts.getFirst();

            int toRemove = Math.min(parallel, first.stackSize);
            parallel -= toRemove;

            outputs.add(first.splitStack(toRemove));

            if (first.stackSize == 0) {
                pendingCrafts.removeFirst();
            }
        }

        if (!outputs.isEmpty()) {
            mOutputItems = outputs.toArray(GTValues.emptyItemStackArray);

            mMaxProgresstime = craftingProgressTime;
            lEUt = -craftingEUt;
            mEfficiency = 10000 - (getIdealStatus() - getRepairStatus()) * 1000;

            addCraftingFX(mOutputItems[0]);

            return CheckRecipeResultRegistry.SUCCESSFUL;
        } else {
            mMaxProgresstime = 0;
            lEUt = 0;
            mEfficiency = 0;

            return CheckRecipeResultRegistry.NO_RECIPE;
        }
    }

    @Override
    public boolean addItemOutputs(ItemStack[] outputItems) {
        for (ItemStack stack : outputItems) {
            cachedOutputs.addTo(GTUtility.ItemId.create(stack), stack.stackSize);
        }

        markDirty();

        return true;
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);

        NBTTagList isList = new NBTTagList();

        for (var e : cachedOutputs.object2LongEntrySet()) {
            if (e.getLongValue() <= 0) break;
            NBTTagCompound tag = new NBTTagCompound();
            NBTTagCompound isTag = new NBTTagCompound();
            e.getKey().getItemStack().writeToNBT(isTag);
            tag.setTag("itemStack", isTag);
            tag.setLong("size", e.getLongValue());
            isList.appendTag(tag);
        }
        aNBT.setTag(NBT_KEY_CACHED_OUTPUTS, isList);

        aNBT.setBoolean(NBT_KEY_CONFIG_HIDDEN_CRAFTING_FX, hiddenCraftingFX);
        NBTTagCompound proxyTag = new NBTTagCompound();
        getProxy().writeToNBT(proxyTag);
        aNBT.setTag("proxy", proxyTag);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);

        NBTBase nbtBase = aNBT.getTag(NBT_KEY_CACHED_OUTPUTS);
        if (nbtBase instanceof NBTTagList isList) {
            for (int i = 0; i < isList.tagCount(); i++) {
                NBTTagCompound tag = isList.getCompoundTagAt(i);
                NBTTagCompound stackTag = tag.getCompoundTag("itemStack");

                ItemStack itemStack = GTUtility.loadItem(stackTag);
                long size = tag.getLong("size");

                cachedOutputs.put(GTUtility.ItemId.create(itemStack), size);
            }
        }

        hiddenCraftingFX = aNBT.getBoolean(NBT_KEY_CONFIG_HIDDEN_CRAFTING_FX);
        if (aNBT.hasKey("proxy")) {
            getProxy().readFromNBT(aNBT.getCompoundTag("proxy"));
        }
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        StructureWrapperTooltipBuilder<MTELargeMolecularAssembler> tt = new StructureWrapperTooltipBuilder<>(this.structure);

        tt.addMachineType(MACHINE_TYPE)
            .addInfo(
                "Basic: " + EnumChatFormatting.GREEN
                    + EU_PER_TICK_BASIC
                    + EnumChatFormatting.GRAY
                    + " EU/t, unaffected by overclocking")
            .addInfo(
                "Crafting: " + EnumChatFormatting.GREEN
                    + EU_PER_TICK_CRAFTING
                    + EnumChatFormatting.GRAY
                    + " EU/t, Finish "
                    + EnumChatFormatting.WHITE
                    + "2"
                    + EnumChatFormatting.GRAY
                    + " Jobs in "
                    + EnumChatFormatting.WHITE
                    + "1"
                    + EnumChatFormatting.GRAY
                    + "s")
            .addInfo("The first two Overclocks:")
            .addInfo(
                "-Reduce the Finish time to " + EnumChatFormatting.WHITE
                    + "0.5"
                    + EnumChatFormatting.GRAY
                    + "s and "
                    + EnumChatFormatting.WHITE
                    + "0.25"
                    + EnumChatFormatting.GRAY
                    + "s")
            .addInfo("Subsequent Overclocks:")
            .addInfo("-Double the number of Jobs finished at once");

        tt.beginStructureBlock(true).addAllCasingInfo().toolTipFinisher();

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

        BlockPos displayPoint = structureInstanceInfo.getSocket(this, 'X');

        if (displayPoint != null) {
            GTPacketLMACraftingFX packet = new GTPacketLMACraftingFX(
                displayPoint.x,
                displayPoint.y,
                displayPoint.z,
                mMaxProgresstime + 10,
                AEApi.instance().storage().createItemStack(itemStack));

            GTValues.NW.sendPacketToAllPlayersInRange(
                getBaseMetaTileEntity().getWorld(),
                packet,
                displayPoint.x,
                displayPoint.z);
        }
    }

    private BaseActionSource getRequest() {
        if (requestSource == null) {
            requestSource = new MachineSource((IActionHost) getBaseMetaTileEntity());
        }
        return requestSource;
    }

    private void flushCachedOutputsIfNeeded(long tick) {
        if (tick <= lastOutputTick + 40 && !justEjectedContainers) {
            return;
        }

        try {
            AENetworkProxy proxy = getProxy();
            IMEMonitor<IAEItemStack> storage = proxy.getStorage()
                .getItemInventory();

            for (ObjectIterator<Object2LongMap.Entry<GTUtility.ItemId>> iterator = cachedOutputs.object2LongEntrySet().iterator(); iterator.hasNext(); ) {
                var e = iterator.next();

                IAEItemStack stack = AEItemStack.create(e.getKey().getItemStack(1)).setStackSize(e.getLongValue());

                IAEItemStack rejected = Platform.poweredInsert(proxy.getEnergy(), storage, stack, getRequest());

                if (rejected != null && rejected.getStackSize() > 0) {
                    e.setValue(rejected.getStackSize());
                } else {
                    iterator.remove();
                }
            }
        } catch (GridAccessException ignored) {
            // :P
        }

        if (!justEjectedContainers) {
            lastOutputTick = tick;
        }

        justEjectedContainers = false;
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
        } else {
            if (!proxy.getConnectableSides()
                .isEmpty()) {
                proxy.setValidSides(EnumSet.noneOf(ForgeDirection.class));
            }
        }
    }

    @Override
    public boolean pushPattern(ICraftingPatternDetails patternDetails, InventoryCrafting table) {
        World w = getBaseMetaTileEntity().getWorld();
        ItemStack mainOutput = patternDetails.getOutput(table, w);
        if (mainOutput == null) return false;

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

            cachedOutputs.addTo(GTUtility.ItemId.create(container), container.stackSize);
            justEjectedContainers = true;
        }

        return true;
    }

    @Override
    public boolean isBusy() {
        return mMaxProgresstime > 0;
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

    @Override
    public boolean isBatchModeEnabled() {
        return false;
    }

    @Override
    public VoidingMode getVoidingMode() {
        return VoidingMode.VOID_NONE;
    }
}
