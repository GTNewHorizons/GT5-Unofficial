package gregtech.common.items.matterManipulator;

import static gregtech.api.enums.Mods.GregTech;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.function.Function;

import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
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
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3d;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.spongepowered.libraries.com.google.common.collect.MapMaker;
import org.spongepowered.libraries.com.google.gson.Gson;

import com.gtnewhorizon.gtnhlib.util.AboveHotbarHUD;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import appeng.api.features.INetworkEncodable;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.GTMod;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.INetworkUpdatableItem;
import gregtech.api.net.GTPacketUpdateItem;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTUtility;
import gregtech.common.items.matterManipulator.BlockAnalyzer.BlockApplyContext;
import gregtech.common.items.matterManipulator.NBTState.BlockRemoveMode;
import gregtech.common.items.matterManipulator.NBTState.BlockSelectMode;
import gregtech.common.items.matterManipulator.NBTState.Config;
import gregtech.common.items.matterManipulator.NBTState.CoordMode;
import gregtech.common.items.matterManipulator.NBTState.Location;
import gregtech.common.items.matterManipulator.NBTState.PendingAction;
import gregtech.common.items.matterManipulator.NBTState.PendingBlock;
import gregtech.common.items.matterManipulator.NBTState.PlaceMode;
import gregtech.common.items.matterManipulator.NBTState.Shape;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;

public class ItemMatterManipulator extends Item implements IElectricItem, INetworkUpdatableItem, INetworkEncodable {

    public ItemMatterManipulator() {
        this.setCreativeTab(CreativeTabs.tabTools);
        this.setUnlocalizedName("itemMatterManipulator");
        this.setMaxStackSize(1);
        this.setTextureName(GregTech.ID + ":" + "itemMatterManipulator");
        GTLanguageManager.addStringLocalization("item.itemMatterManipulator.name", "Matter Manipulator");

        GameRegistry.registerItem(this, "itemMatterManipulator");
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance()
            .bus()
            .register(this);
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
        return 1_000_000_000;
    }

    @Override
    public int getTier(ItemStack itemStack) {
        return 7; // ZPM
    }

    @Override
    public double getTransferLimit(ItemStack itemStack) {
        return 131_072;
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
        ElectricItem.manager.charge(stack, getMaxCharge(null), Integer.MAX_VALUE, true, false);
        subItems.add(stack);
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return EnumRarity.rare;
    }

    private static NBTState getState(ItemStack itemStack) {
        return NBTState.load(getOrCreateNbtData(itemStack));
    }

    private static void setState(ItemStack itemStack, NBTState state) {
        NBTTagCompound newState = state.save();

        if (!Objects.equals(newState, itemStack.getTagCompound())) {
            boolean configChanged = itemStack.getTagCompound() == null || !Objects.equals(
                newState.getTag("config"),
                itemStack.getTagCompound()
                    .getTag("config"));
            boolean needsSyncToServer = NetworkUtils.isClient() && configChanged;

            itemStack.setTagCompound(newState);

            if (needsSyncToServer) {
                GTValues.NW.sendToServer(new GTPacketUpdateItem(newState.getCompoundTag("config")));
            }
        }
    }

    /**
     * Only called on the server when the player changes some config (via the radial menu).
     */
    @Override
    public boolean receive(ItemStack stack, EntityPlayerMP player, NBTTagCompound tag) {
        getOrCreateNbtData(stack).setTag("config", tag);

        return true;
    }

    @SubscribeEvent
    public void stopClientClearUsing(PlayerTickEvent event) {
        // spotless:off
        boolean isHandValid = event.player.getItemInUse() != null && event.player.getItemInUse().getItem() == this;
        boolean isCurrentItemValid = event.player.inventory.getCurrentItem() != null && event.player.inventory.getCurrentItem().getItem() == this;
        boolean isClient = FMLCommonHandler.instance().getSide() == Side.CLIENT;
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
            if (state.connectToMESystem()) {
                desc.add("Has an ME connection. (" + (state.canInteractWithAE(player) ? "Can interact currently" : "Cannot interact currently") + ")");
            } else {
                desc.add("Does not have an ME connection.");
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
                    case SCAN -> "Scan";
                });
            }

            addInfoLine(desc, "Mode: %s", switch (state.config.placeMode) {
                case GEOMETRY -> "Geometry";
                case MOVING -> "Moving";
                case COPYING -> "Copying";
                case DEBUGGING -> "Debugging";
            });

            addInfoLine(desc, "Removing: %s", switch (state.config.removeMode) {
                case ALL -> "All blocks";
                case REPLACEABLE -> "Replaceable blocks";
                case NONE -> "No blocks";
            });
            
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

    private TileAnalysisResult result;

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if (player.getItemInUse() != null && player.getItemInUse()
            .getItem() == this) {
            return itemStack;
        }

        MovingObjectPosition hit = Config.getHitResult(player);

        if (hit != null && hit.typeOfHit != MovingObjectType.BLOCK) {
            hit = null;
        }

        NBTState state = getState(itemStack);

        if (state.config.action != null) {
            if (handleAction(itemStack, world, player, hit, state)) {
                if (world.isRemote) {
                    setState(itemStack, state);
                }

                return itemStack;
            }
        }

        if (hit != null) {
            Location location = new Location(world, hit.blockX, hit.blockY, hit.blockZ);

            if (!player.isSneaking()) {
                location.offset(ForgeDirection.getOrientation(hit.sideHit));
            }

            if (state.config.placeMode == PlaceMode.DEBUGGING) {
                if (!world.isRemote) {
                    BlockApplyContext bac = new BlockApplyContext();
                    bac.world = world;
                    bac.x = location.x;
                    bac.y = location.y;
                    bac.z = location.z;
                    bac.player = player;
                    bac.build = null;
                    bac.manipulator = itemStack;

                    if (result == null) {
                        try {
                            result = BlockAnalyzer.analyze(bac);

                            GTUtility.sendChatToPlayer(player, "analysis: " + new Gson().toJson(result));
                        } catch (Throwable t) {
                            GTMod.GT_FML_LOGGER.error("analysis error", t);

                            GTUtility.sendChatToPlayer(player, "analysis error: " + t.getMessage());
                        }
                    } else {
                        try {
                            GTUtility.sendChatToPlayer(player, "apply: " + result.apply(bac));

                            result = null;
                        } catch (Throwable t) {
                            GTMod.GT_FML_LOGGER.error("apply error", t);

                            GTUtility.sendChatToPlayer(player, "apply error: " + t.getMessage());
                        }
                    }
                }

                return itemStack;
            }

            if (state.config.placeMode == PlaceMode.GEOMETRY) {
                switch (state.config.coordMode) {
                    case SET_A: {
                        state.config.coordA = location;
                        break;
                    }
                    case SET_B: {
                        state.config.coordB = location;
                        break;
                    }
                    case SET_INTERLEAVED: {
                        if (state.config.coordA == null) {
                            state.config.coordA = location;
                        } else {
                            if (state.config.coordB == null) {
                                state.config.coordB = location;
                            } else {
                                state.config.coordA = location;
                                state.config.coordB = null;
                            }
                        }

                        break;
                    }
                    case SET_PASTE: {

                        break;
                    }
                }
            }

            setState(itemStack, state);
            return itemStack;
        }

        if (player.isSneaking()) {
            if (state.config.coordA != null && state.config.coordB != null) {
                player.setItemInUse(itemStack, Integer.MAX_VALUE);
            } else {
                player.addChatMessage(
                    new ChatComponentText(String.format("Both coords must be set to use the geometry mode.")));
            }
        } else {
            if (world.isRemote) {
                UIInfos.openClientUI(player, this::createWindow);
            }
        }

        return itemStack;
    }

    private boolean handleAction(ItemStack itemStack, World world, EntityPlayer player,
        @Nullable MovingObjectPosition hit, NBTState state) {
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

                ItemStack selected = null;

                if (hit != null && hit.typeOfHit == MovingObjectType.BLOCK) {
                    Block block = world.getBlock(hit.blockX, hit.blockY, hit.blockZ);

                    selected = block.getPickBlock(hit, world, hit.blockX, hit.blockY, hit.blockZ, player);

                    if (selected != null && !(selected.getItem() instanceof ItemBlock)) {
                        selected = null;
                    }
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

                if (world.isRemote) {
                    player.addChatMessage(
                        new ChatComponentText(
                            String.format(
                                "%s%sSet %s to: %s",
                                EnumChatFormatting.ITALIC,
                                EnumChatFormatting.GRAY,
                                what,
                                selected == null ? "nothing" : selected.getDisplayName())));
                }

                return true;
            }
            case MARK_COPY_A: {
                state.config.coordA = new Location(world, Config.getLookingAtLocation(player));
                state.config.action = PendingAction.MARK_COPY_B;
                return true;
            }
            case MARK_COPY_B: {
                state.config.coordB = new Location(world, Config.getLookingAtLocation(player));
                state.config.action = null;
                return true;
            }
            case MARK_PASTE: {
                state.config.coordC = new Location(world, Config.getLookingAtLocation(player));
                state.config.action = null;
                return true;
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

            NBTState state = getState(heldItem);

            MovingObjectPosition hit = Config.getHitResult(player);

            if (hit != null && hit.typeOfHit == MovingObjectType.BLOCK) {
                state.config.action = PendingAction.GEOM_SELECTING_BLOCK;

                if (handleAction(heldItem, player.worldObj, player, hit, state)) {
                    setState(heldItem, state);
                }
            }
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
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

                    Comparator<UniqueIdentifier> blockId = Comparator.comparing((UniqueIdentifier id) -> id.modId)
                        .thenComparing(id -> id.name);
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
        return getState(item).encKey;
    }

    @Override
    public void setEncryptionKey(ItemStack item, String encKey, String name) {
        NBTState state = getState(item);

        try {
            state.encKey = Long.toHexString(Long.parseLong(encKey));
        } catch (NumberFormatException e) {
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

    private static void withState(UIBuildContext buildContext, Consumer<NBTState> fn) {
        ItemStack heldStack = buildContext.getPlayer()
            .getHeldItem();
        NBTState state = getState(heldStack);
        fn.accept(state);
        setState(heldStack, state);
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
                    case DEBUGGING -> addDebuggingOptions(builder, buildContext, heldStack);
                }
            });
    }

    private void addCommonOptions(RadialMenuBuilder builder, UIBuildContext buildContext, ItemStack heldStack) {
        builder.branch()
                .label("Set Mode")
                .branch()
                    .label("Set Remove Mode")
                    .option()
                        .label("Remove None")
                        .onClicked(() -> {
                            withState(buildContext, state -> {
                                state.config.removeMode = BlockRemoveMode.NONE;
                            });
                        })
                    .done()
                    .option()
                        .label("Remove Replaceable")
                        .onClicked(() -> {
                            withState(buildContext, state -> {
                                state.config.removeMode = BlockRemoveMode.REPLACEABLE;
                            });
                        })
                    .done()
                    .option()
                        .label("Remove All")
                        .onClicked(() -> {
                            withState(buildContext, state -> {
                                state.config.removeMode = BlockRemoveMode.ALL;
                            });
                        })
                    .done()
                .done()
                .option()
                    .label("Geometry")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.placeMode = PlaceMode.GEOMETRY;
                        });
                    })
                .done()
                .option()
                    .label("Moving")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.placeMode = PlaceMode.MOVING;
                        });
                    })
                .done()
                .option()
                    .label("Copying")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.placeMode = PlaceMode.COPYING;
                        });
                    })
                .done()
                .option()
                    .label("Debugging")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.placeMode = PlaceMode.DEBUGGING;
                        });
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
                        withState(buildContext, state -> {
                            state.config.blockSelectMode = BlockSelectMode.CORNERS;
                            state.config.action = PendingAction.GEOM_SELECTING_BLOCK;
                        });
                    })
                .done()
                .option()
                    .label("Set Edges")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.blockSelectMode = BlockSelectMode.EDGES;
                            state.config.action = PendingAction.GEOM_SELECTING_BLOCK;
                        });
                    })
                .done()
                .option()
                    .label("Set Faces")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.blockSelectMode = BlockSelectMode.FACES;
                            state.config.action = PendingAction.GEOM_SELECTING_BLOCK;
                        });
                    })
                .done()
                .option()
                    .label("Set Volumes")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.blockSelectMode = BlockSelectMode.VOLUMES;
                            state.config.action = PendingAction.GEOM_SELECTING_BLOCK;
                        });
                    })
                .done()
                .option()
                    .label("Set All")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.blockSelectMode = BlockSelectMode.ALL;
                            state.config.action = PendingAction.GEOM_SELECTING_BLOCK;
                        });
                    })
                .done()
                .option()
                    .label("Clear All")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.setCorners(null);
                            state.config.setEdges(null);
                            state.config.setFaces(null);
                            state.config.setVolumes(null);
                            state.config.action = null;
                        });
                    })
                .done()
            .done()
            .branch()
                .label("Set Shape")
                .option()
                    .label("Line")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.shape = Shape.LINE;
                        });
                    })
                .done()
                .option()
                    .label("Cube")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.shape = Shape.CUBE;
                        });
                    })
                .done()
                .option()
                    .label("Sphere")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.shape = Shape.SPHERE;
                        });
                    })
                .done()
            .done()
            .branch()
                .label("Change Coords")
                .option()
                    .label("Set Coord A")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.coordMode = CoordMode.SET_A;
                            state.config.action = null;
                        });
                    })
                .done()
                .option()
                    .label("Set Coord B")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.coordMode = CoordMode.SET_B;
                            state.config.action = null;
                        });
                    })
                .done()
                .option()
                    .label("Set Both")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.coordMode = CoordMode.SET_INTERLEAVED;
                            state.config.action = null;
                        });
                    })
                .done()
                .option()
                    .label("Move Coord A")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.action = PendingAction.GEOM_MOVING_COORDS;
                            state.config.coordAOffset = new Vector3i();
                            state.config.coordBOffset = null;
                        });
                    })
                .done()
                .option()
                    .label("Move Coord B")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.action = PendingAction.GEOM_MOVING_COORDS;
                            state.config.coordAOffset = null;
                            state.config.coordBOffset = new Vector3i();
                        });
                    })
                .done()
                .option()
                    .label("Move Both")
                    .onClicked(() -> {
                        withState(buildContext, state -> {
                            state.config.action = PendingAction.GEOM_MOVING_COORDS;

                            Vector3i lookingAt = Config.getLookingAtLocation(buildContext.getPlayer());

                            if (state.config.coordA == null) {
                                state.config.coordAOffset = null;
                            } else {
                                state.config.coordAOffset = state.config.coordA.toVec().sub(lookingAt);
                            }

                            if (state.config.coordB == null) {
                                state.config.coordBOffset = null;
                            } else {
                                state.config.coordBOffset = state.config.coordB.toVec().sub(lookingAt);
                            }
                        });
                    })
                .done()
            .done();
    }

    private void addCopyingOptions(RadialMenuBuilder builder, UIBuildContext buildContext, ItemStack heldStack) {
        builder
            .option()
                .label("Mark Copy")
                .onClicked(() -> {
                    withState(buildContext, state -> {
                        state.config.action = PendingAction.MARK_COPY_A;
                        state.config.coordA = null;
                        state.config.coordB = null;
                    });
                })
            .done()
            .option()
                .label("Mark Paste")
                .onClicked(() -> {
                    withState(buildContext, state -> {
                        state.config.action = PendingAction.MARK_PASTE;
                        state.config.coordC = null;
                    });
                })
            .done();
    }

    private void addMovingOptions(RadialMenuBuilder builder, UIBuildContext buildContext, ItemStack heldStack) {

    }

    private void addDebuggingOptions(RadialMenuBuilder builder, UIBuildContext buildContext, ItemStack heldStack) {

    }
    // spotless:on

    // #endregion

    // #region Rendering

    @SideOnly(Side.CLIENT)
    private static long LAST_SPAWN_MS_EPOCH = 0;

    @SideOnly(Side.CLIENT)
    private static NBTState.Config DRAWN_CONFIG = null;

    private static final long SPAWN_INTERVAL_MS = 10_000;

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void renderSelection(RenderHandEvent event) {
        EntityPlayer player = Minecraft.getMinecraft().thePlayer;
        ItemStack held = player.getHeldItem();

        if (held != null && held.getItem() == this) {
            NBTState state = getState(held);

            switch (state.config.placeMode) {
                case GEOMETRY:
                case DEBUGGING: {
                    renderGeom(event, state, player);
                    break;
                }
                case COPYING:
                case MOVING: {
                    renderRegions(event, state, player);
                    break;
                }
            }
        } else {
            LAST_SPAWN_MS_EPOCH = 0;
        }
    }

    private void renderGeom(RenderHandEvent event, NBTState state, EntityPlayer player) {
        Location coordA = state.config.getCoordA(player);
        Location coordB = state.config.getCoordB(player);
        state.config.coordA = coordA;
        state.config.coordB = coordB;

        boolean isAValid = coordA != null && coordA.isInWorld(player.worldObj);
        boolean isBValid = coordB != null && coordB.isInWorld(player.worldObj);

        if (isAValid) {
            Objects.requireNonNull(coordA);
            drawBox(player, coordA, event.partialTicks);

            if (state.config.coordAOffset != null) {
                GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.75F);
                drawRulers(player, coordA, true, event.partialTicks);
            }
        }

        if (isBValid) {
            Objects.requireNonNull(coordB);
            drawBox(player, coordB, event.partialTicks);

            if (state.config.coordBOffset != null) {
                GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.75F);
                drawRulers(player, coordB, true, event.partialTicks);
            }
        }

        if (isAValid && isBValid) {
            Objects.requireNonNull(coordA);
            Objects.requireNonNull(coordB);

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

    private void renderRegions(RenderHandEvent event, NBTState state, EntityPlayer player) {
        Location sourceA = state.config.coordA;
        Location sourceB = state.config.coordB;
        Location paste = state.config.coordC;

        if (state.config.action != null) {
            switch (state.config.action) {
                case MARK_COPY_A:
                case MARK_CUT_A: {
                    sourceA = new Location(player.worldObj, Config.getLookingAtLocation(player));
                    GL11.glColor4f(0.0F, 0.0F, 1.0F, 0.75F);
                    drawRulers(player, sourceA, false, event.partialTicks);
                    break;
                }
                case MARK_COPY_B:
                case MARK_CUT_B: {
                    sourceB = new Location(player.worldObj, Config.getLookingAtLocation(player));
                    GL11.glColor4f(0.0F, 0.0F, 1.0F, 0.75F);
                    drawRulers(player, sourceB, false, event.partialTicks);
                    break;
                }
                case MARK_PASTE: {
                    paste = new Location(player.worldObj, Config.getLookingAtLocation(player));
                    GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.75F);
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

        if (isSourceAValid && isSourceBValid) {
            Objects.requireNonNull(sourceA);
            Objects.requireNonNull(sourceB);

            deltas = Config.getRegionDeltas(sourceA, sourceB);

            GL11.glColor4f(0.0F, 0.0F, 1.0F, 0.75F);
            drawAABB(player, Config.getBoundingBox(sourceA, deltas), event.partialTicks);

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

            GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.75F);
            if (deltas != null) {
                drawAABB(player, Config.getBoundingBox(paste, deltas), event.partialTicks);
            } else {
                drawAABB(player, Config.getBoundingBox(paste, new Vector3i()), event.partialTicks);
            }

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
    }

    private void drawBox(EntityPlayer player, Location l, float partialTickTime) {
        Block block = player.worldObj.getBlock(l.x, l.y, l.z);
        block.setBlockBoundsBasedOnState(player.worldObj, l.x, l.y, l.z);
        AxisAlignedBB aabb = block.getSelectedBoundingBoxFromPool(player.worldObj, l.x, l.y, l.z);

        GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.75F);
        drawAABB(player, aabb, partialTickTime);
    }

    private void drawAABB(EntityPlayer player, AxisAlignedBB aabb, float partialTickTime) {
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        float f1 = 0.002F;

        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double) partialTickTime;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double) partialTickTime;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double) partialTickTime;
        RenderGlobal.drawOutlinedBoundingBox(
            aabb.expand((double) f1, (double) f1, (double) f1)
                .getOffsetBoundingBox(-d0, -d1, -d2),
            -1);

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
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
