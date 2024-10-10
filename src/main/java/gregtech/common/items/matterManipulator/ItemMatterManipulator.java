package gregtech.common.items.matterManipulator;

import static gregtech.api.enums.GTValues.V;
import static gregtech.api.enums.Mods.GregTech;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderWorldLastEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import org.joml.Vector3d;
import org.joml.Vector3f;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.spongepowered.libraries.com.google.common.collect.MapMaker;

import com.gtnewhorizon.gtnhlib.util.AboveHotbarHUD;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizon.structurelib.entity.fx.WeightlessParticleFX;
import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;

import appeng.api.features.INetworkEncodable;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.util.GTLanguageManager;
import gregtech.common.items.matterManipulator.NBTState.BlockRemoveMode;
import gregtech.common.items.matterManipulator.NBTState.BlockSelectMode;
import gregtech.common.items.matterManipulator.NBTState.Location;
import gregtech.common.items.matterManipulator.NBTState.PendingAction;
import gregtech.common.items.matterManipulator.NBTState.PendingBlock;
import gregtech.common.items.matterManipulator.NBTState.PlaceMode;
import gregtech.common.items.matterManipulator.NBTState.Shape;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

public class ItemMatterManipulator extends Item implements IElectricItem, INetworkEncodable {

    public final ManipulatorTier tier;

    public ItemMatterManipulator(ManipulatorTier tier) {
        String name = "itemMatterManipulator" + tier.mTier;

        this.setCreativeTab(CreativeTabs.tabTools);
        this.setUnlocalizedName(name);
        this.setMaxStackSize(1);
        this.setTextureName(GregTech.resourceDomain + ":" + name);

        this.tier = tier;

        String defaultLocalName = switch (tier) {
            case Tier0 -> "Prototype Matter Manipulator";
            case Tier1 -> "Matter Manipulator MKI";
            case Tier2 -> "Matter Manipulator MKII";
            case Tier3 -> "Matter Manipulator MKIII";
        };

        GTLanguageManager.addStringLocalization("item." + name + ".name", defaultLocalName);
        GameRegistry.registerItem(this, name);
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance()
            .bus()
            .register(this);
    }

    public static enum ManipulatorTier {

        Tier0(false, 32, false, false, false, 4, 1_000_000),
        Tier1(true, 64, true, false, false, 5, 10_000_000),
        Tier2(true, 128, true, false, true, 6, 100_000_000),
        Tier3(true, Integer.MAX_VALUE, true, true, true, 7, 1_000_000_000);

        public final int mTier = ordinal();
        public final boolean mConnectsToAE;
        public final int mMaxRange;
        public final boolean mAllowRemoving;
        public final boolean mAllowConfiguring;
        public final int mVoltageTier;
        public final double mMaxCharge;
        public final boolean mAllowCopying;

        private ManipulatorTier(boolean ae, int maxRange, boolean removing, boolean configuring, boolean copying,
            int voltageTier, double maxCharge) {
            mConnectsToAE = ae;
            mMaxRange = maxRange;
            mAllowRemoving = removing;
            mAllowConfiguring = configuring;
            mAllowCopying = copying;
            mVoltageTier = voltageTier;
            mMaxCharge = maxCharge;
        }
    }

    // #region Energy

    @Override
    public Item getEmptyItem(ItemStack itemStack) {
        return this;
    }

    @Override
    public boolean canProvideEnergy(ItemStack itemStack) {
        return false;
    }

    @Override
    public double getMaxCharge(ItemStack itemStack) {
        return tier.mMaxCharge;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return tier.mVoltageTier;
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return V[tier.mVoltageTier];
    }

    @Override
    public Item getChargedItem(ItemStack itemStack) {
        return this;
    }

    // #endregion

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTab, List<ItemStack> subItems) {
        final ItemStack stack = new ItemStack(this, 1);
        stack.setTagCompound(new NBTState().save());
        subItems.add(stack.copy());
        ElectricItem.manager.charge(stack, getMaxCharge(stack), Integer.MAX_VALUE, true, false);
        subItems.add(stack);
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return switch (tier) {
            case Tier0 -> EnumRarity.common;
            case Tier1 -> EnumRarity.uncommon;
            case Tier2 -> EnumRarity.rare;
            case Tier3 -> EnumRarity.epic;
        };
    }

    public static NBTState getState(ItemStack itemStack) {
        return NBTState.load(getOrCreateNbtData(itemStack));
    }

    public static void setState(ItemStack itemStack, NBTState state) {
        itemStack.setTagCompound(state.save());
    }

    @SubscribeEvent
    public void stopClientClearUsing(PlayerTickEvent event) {
        // spotless:off
        boolean isHandValid = event.player.getItemInUse() != null && event.player.getItemInUse().getItem() == this;
        boolean isCurrentItemValid = event.player.inventory.getCurrentItem() != null && event.player.inventory.getCurrentItem().getItem() == this;
        boolean isClient = FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT;
        // spotless:on

        if (isHandValid && isCurrentItemValid && isClient) {
            NBTTagCompound inInventory = event.player.inventory.getCurrentItem()
                .getTagCompound();
            NBTTagCompound using = (NBTTagCompound) event.player.getItemInUse()
                .getTagCompound()
                .copy();

            // we don't want to stop using the item if only the charge changes
            using.setDouble("charge", inInventory.getDouble("charge"));

            if (inInventory.equals(using)) {
                event.player.setItemInUse(event.player.inventory.getCurrentItem(), event.player.getItemInUseCount());
            }
        }
    }

    public static NBTTagCompound getOrCreateNbtData(ItemStack itemStack) {
        NBTTagCompound ret = itemStack.getTagCompound();
        if (ret == null) {
            ret = new NBTTagCompound();
            itemStack.setTagCompound(ret);
        }
        return ret;
    }

    @Override
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> desc,
        boolean advancedItemTooltips) {
        NBTState state = getState(itemStack);

        // spotless:off
        if (!GuiScreen.isShiftKeyDown()) {
            desc.add("Hold shift for more information.");
        } else {
            if (tier.mConnectsToAE) {
                if (state.connectToMESystem()) {
                    desc.add("Has an ME connection. (" + (state.canInteractWithAE(player) ? "Can interact currently" : "Cannot interact currently") + ")");
                } else {
                    desc.add("Does not have an ME connection.");
                }
            }

            if (state.config.action != null) {
                addInfoLine(desc, "Pending Action: %s", switch (state.config.action) {
                    case GEOM_MOVING_COORDS -> "Moving coordinates";
                    case GEOM_SELECTING_BLOCK -> "Selecting blocks to place";
                    case MARK_COPY_A -> "Marking first copy corner";
                    case MARK_COPY_B -> "Marking second copy corner";
                    case MARK_CUT_A -> "Marking first cut corner";
                    case MARK_CUT_B -> "Marking second cut corner";
                    case MARK_PASTE -> "Marking paste location";
                });
            }

            if (tier.mAllowConfiguring || tier.mAllowCopying) {
                addInfoLine(desc, "Mode: %s", switch (state.config.placeMode) {
                    case GEOMETRY -> "Geometry";
                    case MOVING -> "Moving";
                    case COPYING -> "Copying";
                });
            }

            if (tier.mAllowRemoving) {
                addInfoLine(desc, "Removing: %s", switch (state.config.removeMode) {
                    case ALL -> "All blocks";
                    case REPLACEABLE -> "Replaceable blocks";
                    case NONE -> "No blocks";
                });
            }
            
            if (state.config.placeMode == PlaceMode.GEOMETRY) {
                addInfoLine(desc, "Shape: %s", switch (state.config.shape) {
                    case LINE -> "Line";
                    case CUBE -> "Cube";
                    case SPHERE -> "Sphere";
                });
            
                addInfoLine(desc, "Coordinate A: %s", state.config.coordA);
                addInfoLine(desc, "Coordinate B: %s", state.config.coordB);
        
                addInfoLine(desc, "Corner block: %s", state.config.getCorners(), ItemStack::getDisplayName);
                addInfoLine(desc, "Edge block: %s", state.config.getEdges(), ItemStack::getDisplayName);
                addInfoLine(desc, "Face block: %s", state.config.getFaces(), ItemStack::getDisplayName);
                addInfoLine(desc, "Volume block: %s", state.config.getVolumes(), ItemStack::getDisplayName);
            }
        }
        // spotless:on
    }

    private <T> void addInfoLine(List<String> desc, String format, T value) {
        addInfoLine(desc, format, value, obj -> obj.toString());
    }

    private <T> void addInfoLine(List<String> desc, String format, T value, Function<T, String> toString) {
        if (value != null) {
            desc.add(
                String.format(
                    format,
                    EnumChatFormatting.BLUE.toString() + toString.apply(value) + EnumChatFormatting.RESET.toString()));
        } else {
            desc.add(
                String
                    .format(format, EnumChatFormatting.GRAY.toString() + "None" + EnumChatFormatting.RESET.toString()));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        NBTState state = getState(stack);

        MovingObjectPosition hit = MMUtils.getHitResult(player);

        if (hit != null && hit.typeOfHit != MovingObjectType.BLOCK) hit = null;

        if (state.config.action != null) {
            if (handleAction(stack, world, player, state, hit)) {
                setState(stack, state);

                return stack;
            }
        }

        if (hit != null) {
            Location location = new Location(world, hit.blockX, hit.blockY, hit.blockZ);

            if (!player.isSneaking()) {
                location.offset(ForgeDirection.getOrientation(hit.sideHit));
            }

            if (state.config.placeMode == PlaceMode.GEOMETRY) {
                state.config.coordA = location;
                state.config.coordB = null;
                state.config.coordBOffset = new Vector3i();
                state.config.action = PendingAction.GEOM_MOVING_COORDS;
            }

            setState(stack, state);
            return stack;
        }

        if (player.isSneaking()) {
            player.setItemInUse(stack, Integer.MAX_VALUE);
        } else if (world.isRemote) {
            UIInfos.openClientUI(player, this::createWindow);
        }

        return stack;
    }

    public boolean handleAction(ItemStack itemStack, World world, EntityPlayer player, NBTState state,
        MovingObjectPosition hit) {
        switch (state.config.action) {
            case GEOM_MOVING_COORDS: {
                state.config.coordA = state.config.getCoordA(player);
                state.config.coordB = state.config.getCoordB(player);
                state.config.action = null;
                state.config.coordAOffset = null;
                state.config.coordBOffset = null;

                return true;
            }
            case GEOM_SELECTING_BLOCK: {
                state.config.action = null;

                onPickBlock(world, player, itemStack, state, hit);

                return true;
            }
            case MARK_COPY_A: {
                state.config.coordA = new Location(world, MMUtils.getLookingAtLocation(player));
                state.config.action = PendingAction.MARK_COPY_B;
                return true;
            }
            case MARK_COPY_B: {
                state.config.coordB = new Location(world, MMUtils.getLookingAtLocation(player));
                state.config.action = null;
                return true;
            }
            case MARK_PASTE: {
                state.config.coordC = new Location(world, MMUtils.getLookingAtLocation(player));
                state.config.action = null;
                return true;
            }
            case MARK_CUT_A: {
                throw new IllegalStateException();
            }
            case MARK_CUT_B: {
                throw new IllegalStateException();
            }
        }

        return false;
    }

    @SubscribeEvent
    public void onMouseEvent(MouseEvent event) {
        final EntityClientPlayerMP player = Minecraft.getMinecraft().thePlayer;

        if (player == null || player.isDead) {
            return;
        }

        final ItemStack heldItem = player.getHeldItem();
        if (heldItem == null) {
            return;
        }

        if (event.button == 2 /* MMB */ && event.buttonstate && heldItem.getItem() == this) {
            event.setCanceled(true);

            Messages.MMBPressed.sendToServer();
        }
    }

    public void onMMBPressed(EntityPlayerMP player) {
        final ItemStack heldItem = player.getHeldItem();
        if (heldItem == null) {
            return;
        }

        NBTState state = getState(heldItem);

        if (state.config.placeMode == PlaceMode.GEOMETRY) {
            onPickBlock(player.getEntityWorld(), player, heldItem, state, MMUtils.getHitResult(player));

            setState(heldItem, state);
        }
    }

    private void onPickBlock(World world, EntityPlayer player, ItemStack stack, NBTState state,
        MovingObjectPosition hit) {
        ItemStack selected = null;

        if (hit != null && hit.typeOfHit == MovingObjectType.BLOCK) {
            Block block = world.getBlock(hit.blockX, hit.blockY, hit.blockZ);

            selected = block.getPickBlock(hit, world, hit.blockX, hit.blockY, hit.blockZ, player);

            if (selected != null && !(selected.getItem() instanceof ItemBlock)) {
                selected = null;
            }
        } else if (hit == null || hit.typeOfHit != MovingObjectType.BLOCK) {
            selected = null;
        }

        String what = null;

        switch (state.config.blockSelectMode) {
            case CORNERS: {
                state.config.setCorners(selected);
                what = "corners";
                break;
            }
            case EDGES: {
                state.config.setEdges(selected);
                what = "edges";
                break;
            }
            case FACES: {
                state.config.setFaces(selected);
                what = "faces";
                break;
            }
            case VOLUMES: {
                state.config.setVolumes(selected);
                what = "volumes";
                break;
            }
            case ALL: {
                state.config.setCorners(selected);
                state.config.setEdges(selected);
                state.config.setFaces(selected);
                state.config.setVolumes(selected);
                what = "all blocks";
                break;
            }
        }

        player.addChatMessage(
            new ChatComponentText(
                String.format(
                    "%s%sSet %s to: %s",
                    EnumChatFormatting.ITALIC,
                    EnumChatFormatting.GRAY,
                    what,
                    selected == null ? "nothing" : selected.getDisplayName())));
    }

    static final Map<EntityPlayer, PendingBuild> PENDING_BUILDS = new MapMaker().weakKeys()
        .makeMap();

    private static final ExecutorService BUILD_ASSEMBLING_POOL = new ThreadPoolExecutor(
        0,
        1,
        10L,
        TimeUnit.SECONDS,
        new SynchronousQueue<Runnable>());

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack p_77626_1_) {
        return Integer.MAX_VALUE;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int itemUseCount) {
        if (!world.isRemote) {
            PendingBuild build = PENDING_BUILDS.remove(player);

            if (build != null && build.assembleTask != null) {
                build.assembleTask.cancel(true);
            }
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        int ticksUsed = Integer.MAX_VALUE - count;

        if (ticksUsed == 1) {
            if (player.worldObj.isRemote) {
                // play startup sound
            } else {
                PendingBuild pending = new PendingBuild();
                pending.pendingBlocks = null;
                pending.placingPlayer = player;
                pending.manipulator = getState(stack);

                pending.assembleTask = BUILD_ASSEMBLING_POOL.submit(() -> {
                    List<PendingBlock> blocks = pending.manipulator.getPendingBlocks();

                    Comparator<UniqueIdentifier> blockId = Comparator.nullsFirst(
                        Comparator.comparing((UniqueIdentifier id) -> id.modId)
                            .thenComparing(id -> id.name));
                    Comparator<PendingBlock> comparePending = Comparator.comparingInt((PendingBlock b) -> b.buildOrder)
                        .thenComparing(Comparator.nullsFirst(Comparator.comparing(b -> b.blockId, blockId)))
                        .thenComparingInt(b -> b.metadata);

                    blocks.sort(comparePending);

                    return new LinkedList<>(blocks);
                });

                PENDING_BUILDS.put(player, pending);
            }
        }

        if (ticksUsed >= 20 && (ticksUsed % 2) == 0 && !player.worldObj.isRemote) {
            PENDING_BUILDS.get(player)
                .tryPlaceBlocks(stack, player);
        }
    }

    @Override
    public String getEncryptionKey(ItemStack item) {
        if (tier.mConnectsToAE) {
            Long key = getState(item).encKey;
            return key != null ? Long.toHexString(key) : null;
        } else {
            return null;
        }
    }

    @Override
    public void setEncryptionKey(ItemStack item, String encKey, String name) {
        NBTState state = getState(item);

        if (tier.mConnectsToAE) {
            try {
                state.encKey = Long.parseLong(encKey);
            } catch (NumberFormatException e) {
                state.encKey = null;
            }
        } else {
            state.encKey = null;
        }

        setState(item, state);
    }

    // #region UI

    public ModularWindow createWindow(UIBuildContext buildContext) {
        buildContext.setShowNEI(false);

        ModularWindow.Builder builder = ModularWindow.builder(new Size(176, 272));

        builder.widget(getMenuOptions(buildContext).build());

        return builder.build();
    }

    // spotless:off
    private RadialMenuBuilder getMenuOptions(UIBuildContext buildContext) {
        ItemStack heldStack = buildContext.getPlayer().getHeldItem();
        NBTState initialState = getState(heldStack);

        return new RadialMenuBuilder(buildContext)
            .innerIcon(new ItemStack(this))
            .pipe(builder -> {
                addCommonOptions(builder, buildContext, heldStack);
            })
            .pipe(builder -> {
                switch (initialState.config.placeMode) {
                    case GEOMETRY -> addGeometryOptions(builder, buildContext, heldStack);
                    case COPYING -> addCopyingOptions(builder, buildContext, heldStack);
                    case MOVING -> addMovingOptions(builder, buildContext, heldStack);
                }
            });
    }

    private void addCommonOptions(RadialMenuBuilder builder, UIBuildContext buildContext, ItemStack heldStack) {
        builder.branch()
                .label("Set Mode")
                .branch()
                    .label("Set Remove Mode")
                    .hidden(!tier.mAllowRemoving)
                    .option()
                        .label("Remove None")
                        .onClicked(() -> {
                            Messages.SetRemoveMode.sendToServer(BlockRemoveMode.NONE);
                        })
                    .done()
                    .option()
                        .label("Remove Replaceable")
                        .onClicked(() -> {
                            Messages.SetRemoveMode.sendToServer(BlockRemoveMode.REPLACEABLE);
                        })
                    .done()
                    .option()
                        .label("Remove All")
                        .onClicked(() -> {
                            Messages.SetRemoveMode.sendToServer(BlockRemoveMode.ALL);
                        })
                    .done()
                .done()
                .option()
                    .label("Geometry")
                    .onClicked(() -> {
                        Messages.SetPlaceMode.sendToServer(PlaceMode.GEOMETRY);
                    })
                .done()
                .option()
                    .label("Moving")
                    .hidden(!tier.mAllowCopying)
                    .onClicked(() -> {
                        Messages.SetPlaceMode.sendToServer(PlaceMode.MOVING);
                    })
                .done()
                .option()
                    .label("Copying")
                    .hidden(!tier.mAllowCopying)
                    .onClicked(() -> {
                        Messages.SetPlaceMode.sendToServer(PlaceMode.COPYING);
                    })
                .done()
            .done();
    }

    private void addGeometryOptions(RadialMenuBuilder builder, UIBuildContext buildContext, ItemStack heldStack) {
        builder
            .branch()
                .label("Select Blocks")
                .option()
                    .label("Set Corners")
                    .onClicked(() -> {
                        Messages.SetBlockSelectMode.sendToServer(BlockSelectMode.CORNERS);
                        Messages.SetPendingAction.sendToServer(PendingAction.GEOM_SELECTING_BLOCK);
                    })
                .done()
                .option()
                    .label("Set Edges")
                    .onClicked(() -> {
                        Messages.SetBlockSelectMode.sendToServer(BlockSelectMode.EDGES);
                        Messages.SetPendingAction.sendToServer(PendingAction.GEOM_SELECTING_BLOCK);
                    })
                .done()
                .option()
                    .label("Set Faces")
                    .onClicked(() -> {
                        Messages.SetBlockSelectMode.sendToServer(BlockSelectMode.FACES);
                        Messages.SetPendingAction.sendToServer(PendingAction.GEOM_SELECTING_BLOCK);
                    })
                .done()
                .option()
                    .label("Set Volumes")
                    .onClicked(() -> {
                        Messages.SetBlockSelectMode.sendToServer(BlockSelectMode.VOLUMES);
                        Messages.SetPendingAction.sendToServer(PendingAction.GEOM_SELECTING_BLOCK);
                    })
                .done()
                .option()
                    .label("Set All")
                    .onClicked(() -> {
                        Messages.SetBlockSelectMode.sendToServer(BlockSelectMode.ALL);
                        Messages.SetPendingAction.sendToServer(PendingAction.GEOM_SELECTING_BLOCK);
                    })
                .done()
                .option()
                    .label("Clear All")
                    .onClicked(() -> {
                        Messages.ClearBlocks.sendToServer();
                    })
                .done()
            .done()
            .branch()
                .label("Set Shape")
                .option()
                    .label("Line")
                    .onClicked(() -> {
                        Messages.SetShape.sendToServer(Shape.LINE);
                    })
                .done()
                .option()
                    .label("Cube")
                    .onClicked(() -> {
                        Messages.SetShape.sendToServer(Shape.CUBE);
                    })
                .done()
                .option()
                    .label("Sphere")
                    .onClicked(() -> {
                        Messages.SetShape.sendToServer(Shape.SPHERE);
                    })
                .done()
            .done()
            .branch()
                .label("Move Coords")
                .option()
                    .label("Move Coord A")
                    .onClicked(() -> {
                        Messages.MoveA.sendToServer();
                    })
                .done()
                .option()
                    .label("Move Both")
                    .onClicked(() -> {
                        Messages.MoveBoth.sendToServer();
                    })
                .done()
                .option()
                    .label("Move Coord B")
                    .onClicked(() -> {
                        Messages.MoveB.sendToServer();
                    })
                .done()
                .option()
                    .label("Move Here")
                    .onClicked(() -> {
                        Messages.MoveHere.sendToServer();
                    })
                .done()
            .done();
    }

    private void addCopyingOptions(RadialMenuBuilder builder, UIBuildContext buildContext, ItemStack heldStack) {
        builder
            .option()
                .label("Mark Copy")
                .onClicked(() -> {
                    Messages.MarkCopy.sendToServer();
                })
            .done()
            .option()
                .label("Planning")
                .onClicked(() -> {
                    Messages.MarkPaste.sendToServer();
                })
            .done()
            .option()
                .label("Mark Paste")
                .onClicked(() -> {
                    Messages.MarkPaste.sendToServer();
                })
            .done();
    }

    private void addMovingOptions(RadialMenuBuilder builder, UIBuildContext buildContext, ItemStack heldStack) {

    }
    // spotless:on

    // #endregion

    // #region Rendering

    private static final MethodHandle GET_FXLAYERS;

    static {
        try {
            Field field = ReflectionHelper.findField(EffectRenderer.class, "fxLayers");
            field.setAccessible(true);
            GET_FXLAYERS = MethodHandles.lookup()
                .unreflectGetter(field);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Could not get field EffectRenderer.fxLayers", e);
        }
    }

    @SideOnly(Side.CLIENT)
    private static long LAST_SPAWN_MS_EPOCH = 0;

    @SideOnly(Side.CLIENT)
    private static NBTState.Config DRAWN_CONFIG = null;

    private static final long SPAWN_INTERVAL_MS = 10_000;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void renderSelection(RenderWorldLastEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack held = player.getHeldItem();

        if (held != null && held.getItem() == this) {
            NBTState state = getState(held);

            switch (state.config.placeMode) {
                case GEOMETRY: {
                    renderGeom(event, state, player);
                    break;
                }
                case COPYING:
                case MOVING: {
                    renderRegions(event, state, player);
                    break;
                }
            }

            try {
                List<EntityFX>[] fxLayers = (List<EntityFX>[]) GET_FXLAYERS
                    .invokeExact(Minecraft.getMinecraft().effectRenderer);

                fxLayers[0].removeIf(particle -> particle instanceof WeightlessParticleFX);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        } else {
            LAST_SPAWN_MS_EPOCH = 0;
        }
    }

    private void renderGeom(RenderWorldLastEvent event, NBTState state, EntityPlayer player) {
        Location coordA = state.config.getCoordA(player);
        Location coordB = state.config.getCoordB(player);
        state.config.coordA = coordA;
        state.config.coordB = coordB;

        boolean isAValid = coordA != null && coordA.isInWorld(player.worldObj);
        boolean isBValid = coordB != null && coordB.isInWorld(player.worldObj);

        if (isAValid && state.config.coordAOffset != null) {
            GL11.glColor4f(0.15f, 0.6f, 0.75f, 0.75F);
            drawRulers(player, coordA, false, event.partialTicks);
        }

        if (isBValid && state.config.coordBOffset != null) {
            GL11.glColor4f(0.15f, 0.6f, 0.75f, 0.75F);
            drawRulers(player, coordB, false, event.partialTicks);
        }

        if (isAValid && isBValid) {
            Objects.requireNonNull(coordA);
            Objects.requireNonNull(coordB);

            BoxRenderer.INSTANCE.start(event.partialTicks);
            BoxRenderer.INSTANCE.drawAround(
                MMUtils.getBoundingBox(coordA, MMUtils.getRegionDeltas(coordA, coordB)),
                new Vector3f(0.15f, 0.6f, 0.75f));
            BoxRenderer.INSTANCE.finish();

            boolean spawn = (System.currentTimeMillis() - LAST_SPAWN_MS_EPOCH) >= SPAWN_INTERVAL_MS
                || !Objects.equals(DRAWN_CONFIG, state.config);

            if (spawn) {
                LAST_SPAWN_MS_EPOCH = System.currentTimeMillis();
                DRAWN_CONFIG = state.config;

                StructureLibAPI.startHinting(player.worldObj);

                for (PendingBlock pendingBlock : state.getPendingBlocks()) {
                    Block block = pendingBlock.getBlock();

                    if (pendingBlock.isInWorld(player.worldObj) && block != null && block != Blocks.air) {
                        StructureLibAPI.hintParticle(
                            player.worldObj,
                            pendingBlock.x,
                            pendingBlock.y,
                            pendingBlock.z,
                            block,
                            pendingBlock.metadata);
                    }
                }

                StructureLibAPI.endHinting(player.worldObj);

                int x1 = coordA.x;
                int y1 = coordA.y;
                int z1 = coordA.z;
                int x2 = coordB.x;
                int y2 = coordB.y;
                int z2 = coordB.z;

                int minX = Math.min(x1, x2);
                int minY = Math.min(y1, y2);
                int minZ = Math.min(z1, z2);
                int maxX = Math.max(x1, x2);
                int maxY = Math.max(y1, y2);
                int maxZ = Math.max(z1, z2);

                AboveHotbarHUD.renderTextAboveHotbar(
                    String.format("dX=%d dY=%d dZ=%d", maxX - minX + 1, maxY - minY + 1, maxZ - minZ + 1),
                    (int) (SPAWN_INTERVAL_MS * 20 / 1000),
                    false,
                    false);
            }
        }
    }

    private void renderRegions(RenderWorldLastEvent event, NBTState state, EntityPlayer player) {
        Location sourceA = state.config.coordA;
        Location sourceB = state.config.coordB;
        Location paste = state.config.coordC;

        if (state.config.action != null) {
            switch (state.config.action) {
                case MARK_COPY_A:
                case MARK_CUT_A: {
                    sourceA = new Location(player.worldObj, MMUtils.getLookingAtLocation(player));
                    GL11.glColor4f(0.15f, 0.6f, 0.75f, 0.75F);
                    drawRulers(player, sourceA, false, event.partialTicks);
                    break;
                }
                case MARK_COPY_B:
                case MARK_CUT_B: {
                    sourceB = new Location(player.worldObj, MMUtils.getLookingAtLocation(player));
                    GL11.glColor4f(0.15f, 0.6f, 0.75f, 0.75F);
                    drawRulers(player, sourceB, false, event.partialTicks);
                    break;
                }
                case MARK_PASTE: {
                    paste = new Location(player.worldObj, MMUtils.getLookingAtLocation(player));
                    GL11.glColor4f(0.75f, 0.5f, 0.15f, 0.75F);
                    drawRulers(player, paste, false, event.partialTicks);
                    break;
                }
                default: {
                    return;
                }
            }
        }

        state.config.coordA = sourceA;
        state.config.coordB = sourceB;
        state.config.coordC = paste;

        boolean isSourceAValid = sourceA != null && sourceA.isInWorld(player.worldObj);
        boolean isSourceBValid = sourceB != null && sourceB.isInWorld(player.worldObj);
        boolean isPasteValid = paste != null && paste.isInWorld(player.worldObj);

        Vector3i deltas = null;

        BoxRenderer.INSTANCE.start(event.partialTicks);

        if (isSourceAValid && isSourceBValid) {
            Objects.requireNonNull(sourceA);
            Objects.requireNonNull(sourceB);

            deltas = MMUtils.getRegionDeltas(sourceA, sourceB);

            BoxRenderer.INSTANCE.drawAround(MMUtils.getBoundingBox(sourceA, deltas), new Vector3f(0.15f, 0.6f, 0.75f));

            AboveHotbarHUD.renderTextAboveHotbar(
                String.format(
                    "dX=%d dY=%d dZ=%d",
                    Math.abs(deltas.x) + 1,
                    Math.abs(deltas.y) + 1,
                    Math.abs(deltas.z) + 1),
                (int) (SPAWN_INTERVAL_MS * 20 / 1000),
                false,
                false);
        }

        if (isPasteValid) {
            Objects.requireNonNull(paste);

            Vector3i deltas2 = deltas == null ? new Vector3i() : deltas;

            BoxRenderer.INSTANCE.drawAround(MMUtils.getBoundingBox(paste, deltas2), new Vector3f(0.75f, 0.5f, 0.15f));

            boolean spawn = (System.currentTimeMillis() - LAST_SPAWN_MS_EPOCH) >= SPAWN_INTERVAL_MS
                || !Objects.equals(DRAWN_CONFIG, state.config);

            if (spawn) {
                LAST_SPAWN_MS_EPOCH = System.currentTimeMillis();
                DRAWN_CONFIG = state.config;

                StructureLibAPI.startHinting(player.worldObj);

                for (PendingBlock pendingBlock : state.getPendingBlocks()) {
                    Block block = pendingBlock.getBlock();

                    if (pendingBlock.isInWorld(player.worldObj) && block != null && block != Blocks.air) {
                        StructureLibAPI.hintParticle(
                            player.worldObj,
                            pendingBlock.x,
                            pendingBlock.y,
                            pendingBlock.z,
                            block,
                            pendingBlock.metadata);
                    }
                }

                StructureLibAPI.endHinting(player.worldObj);
            }
        }

        BoxRenderer.INSTANCE.finish();
    }

    private static Vector3d getVecForDir(ForgeDirection dir) {
        return new Vector3d(dir.offsetX, dir.offsetY, dir.offsetZ);
    }

    private static final int RULER_LENGTH = 128;

    private void drawRulers(EntityPlayer player, Location l, boolean fromSurface, float partialTickTime) {
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);

        GL11.glPointSize(4);

        OpenGlHelper.glBlendFunc(770, 771, 1, 0);

        GL11.glPushMatrix();

        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTickTime;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTickTime;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTickTime;
        GL11.glTranslated(l.x - d0 + 0.5, l.y - d1 + 0.5, l.z - d2 + 0.5);

        Tessellator tessellator = Tessellator.instance;

        tessellator.startDrawing(GL11.GL_LINES);

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            Vector3d delta = getVecForDir(dir);

            if (fromSurface) {
                tessellator.addVertex(delta.x * 0.5, delta.y * 0.5, delta.z * 0.5);
            } else {
                tessellator.addVertex(0, 0, 0);
            }
            tessellator.addVertex(delta.x * RULER_LENGTH, delta.y * RULER_LENGTH, delta.z * RULER_LENGTH);
        }

        tessellator.draw();

        GL11.glPopMatrix();

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    // #endregion
}
