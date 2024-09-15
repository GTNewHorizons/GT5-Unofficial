package gtPlusPlus.core.item.general.matterManipulator;

import static gregtech.api.enums.Mods.GTPlusPlus;

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

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3i;
import org.lwjgl.opengl.GL11;
import org.spongepowered.libraries.com.google.common.collect.MapMaker;

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
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GTValues;
import gregtech.api.interfaces.INetworkUpdatableItem;
import gregtech.api.net.GTPacketUpdateItem;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.BlockRemoveMode;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.BlockSelectMode;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.Config;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.CoordMode;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.Location;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.PendingAction;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.PendingBlock;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.PlaceMode;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.Shape;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
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
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;

public class ItemMatterManipulator extends Item implements IElectricItem, INetworkUpdatableItem, INetworkEncodable {

    public ItemMatterManipulator() {
        this.setCreativeTab(AddToCreativeTab.tabTools);
        this.setUnlocalizedName("itemMatterManipulator");
        this.setMaxStackSize(1);
        this.setTextureName(GTPlusPlus.ID + ":" + "itemMatterManipulator");

        GameRegistry.registerItem(this, "itemMatterManipulator");
        MinecraftForge.EVENT_BUS.register(this);
        FMLCommonHandler.instance().bus().register(this);
    }

    //#region Energy

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

    //#endregion

    @Override
    public void getSubItems(Item item, CreativeTabs creativeTab, List<ItemStack> subItems) {
        final ItemStack stack = new ItemStack(this, 1);
        setState(stack, getState(stack));
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

        if(!Objects.equals(newState, itemStack.getTagCompound())) {
            boolean needsSyncToServer =
                NetworkUtils.isClient() &&
                (
                    itemStack.getTagCompound() == null || 
                    !Objects.equals(newState.getTag("config"), itemStack.getTagCompound().getTag("config"))
                );

            itemStack.setTagCompound(newState);

            if(needsSyncToServer) {
                GTValues.NW.sendToServer(new GTPacketUpdateItem(
                    newState.getCompoundTag("config")
                ));
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
        if (event.player.getItemInUse() != null && event.player.getItemInUse().getItem() == this && event.player.inventory.getCurrentItem() != null && event.player.inventory.getCurrentItem().getItem() == this && FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            NBTTagCompound inInventory = event.player.inventory.getCurrentItem().getTagCompound();
            NBTTagCompound using = (NBTTagCompound)event.player.getItemInUse().getTagCompound().copy();

            using.setDouble("charge", inInventory.getDouble("charge"));

            // only the charge has changed
            if(inInventory.equals(using)) {
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
    public void addInformation(ItemStack itemStack, EntityPlayer player, List<String> desc, boolean advancedItemTooltips) {
        NBTState state = getState(itemStack);

        if (!GuiScreen.isShiftKeyDown()) {
            desc.add("Hold shift for more information.");
        } else {
            if (state.connectToMESystem()) {
                desc.add("Has ME connection. (" + (state.canInteractWithAE(player) ? "Can interact currently" : "Cannot interact currently") + ")");
            } else {
                desc.add("Does not have ME connection.");
            }

            addInfoLine(desc, "Coordinate A: %s", state.config.coordA);
            addInfoLine(desc, "Coordinate B: %s", state.config.coordB);
    
            addInfoLine(desc, "Corner block: %s", state.config.getCorners(), ItemStack::getDisplayName);
            addInfoLine(desc, "Edge block: %s", state.config.getEdges(), ItemStack::getDisplayName);
            addInfoLine(desc, "Face block: %s", state.config.getFaces(), ItemStack::getDisplayName);
            addInfoLine(desc, "Volume block: %s", state.config.getVolumes(), ItemStack::getDisplayName);
        }
    }

    private <T> void addInfoLine(List<String> desc, String format, T value) {
        addInfoLine(desc, format, value, obj -> obj.toString());
    }

    private <T> void addInfoLine(List<String> desc, String format, T value, Function<T, String> toString) {
        if(value != null) {
            desc.add(String.format(format, EnumChatFormatting.BLUE.toString() + toString.apply(value) + EnumChatFormatting.RESET.toString()));
        } else {
            desc.add(String.format(format, EnumChatFormatting.GRAY.toString() + "None" + EnumChatFormatting.RESET.toString()));
        }
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        if(player.getItemInUse() != null && player.getItemInUse().getItem() == this) {
            return itemStack;
        }

        MovingObjectPosition hit = Config.getHitResult(player);

        if (hit != null && hit.typeOfHit != MovingObjectType.BLOCK) {
            hit = null;
        }

        NBTState state = getState(itemStack);

        if (state.config.action != null) {
            if (handleAction(itemStack, world, player, hit, state)) {
                if(world.isRemote) {
                    setState(itemStack, state);
                    player.inventory.markDirty();
                }

                return itemStack;
            }
        }

        if (hit != null) {
            Location location = new Location(world, hit.blockX, hit.blockY, hit.blockZ);

            if(!player.isSneaking()) {
                location.offset(DirectionUtil.fromSide(hit.sideHit));
            }

            switch(state.config.coordMode) {
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

            setState(itemStack, state);
            player.inventory.markDirty();
            return itemStack;
        }
        
        if (player.isSneaking()) {
            if (state.config.coordA != null && state.config.coordB != null) {
                player.setItemInUse(itemStack, Integer.MAX_VALUE);
            } else {
                player.addChatMessage(new ChatComponentText(String.format("Both coords must be set to use the geometry mode.")));
            }
        } else {
            if(world.isRemote) {
                UIInfos.openClientUI(player, this::createWindow);
            }
        }

        return itemStack;
    }

    private boolean handleAction(ItemStack itemStack, World world, EntityPlayer player, @Nullable MovingObjectPosition hit, NBTState state) {
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
    
                if(hit != null && hit.typeOfHit == MovingObjectType.BLOCK) {
                    Block block = world.getBlock(hit.blockX, hit.blockY, hit.blockZ);
        
                    selected = block.getPickBlock(hit, world, hit.blockX, hit.blockY, hit.blockZ, player);

                    if (selected != null && !(selected.getItem() instanceof ItemBlock)) {
                        selected = null;
                    }
                }

                String what = null;
    
                switch(state.config.blockSelectMode) {
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

                if(world.isRemote) {
                    player.addChatMessage(new ChatComponentText(String.format(
                        "%s%sSet %s to: %s",
                        EnumChatFormatting.ITALIC, EnumChatFormatting.GRAY,
                        what,
                        selected == null ? "nothing" : selected.getDisplayName()
                    )));
                }

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
                    player.inventory.markDirty();
                }
            }
        }
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
    }

    static final Map<EntityPlayer, PendingBuild> PENDING_BUILDS = new MapMaker()
        .weakKeys()
        .makeMap();

    private static final ExecutorService BUILD_ASSEMBLING_POOL = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

    @Override
    public void onPlayerStoppedUsing(ItemStack stack, World world, EntityPlayer player, int itemUseCount) {
        if(!world.isRemote) {
            PendingBuild build = PENDING_BUILDS.remove(player);
    
            if(build != null && build.assembleTask != null) {
                build.assembleTask.cancel(true);
            }
        }
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        int ticksUsed = Integer.MAX_VALUE - count;

        if(ticksUsed == 1) {
            if(player.worldObj.isRemote) {
                // play startup sound
            } else {
                PendingBuild pending = new PendingBuild();
                pending.pendingBlocks = null;
                pending.placingPlayer = player;
                pending.manipulator = getState(stack);
        
                pending.assembleTask = BUILD_ASSEMBLING_POOL.submit(() -> {
                    List<PendingBlock> blocks = pending.manipulator.getPendingBlocks();
                    blocks.sort((a, b) -> {
                        int buildOrder = Integer.compare(a.buildOrder, b.buildOrder);
                        if (buildOrder != 0) return buildOrder;

                        if (a.block == null && b.block != null) {
                            return -1;
                        }

                        if (a.block != null && b.block == null) {
                            return 1;
                        }

                        if (a.block == null && b.block == null) {
                            return 0;
                        }

                        @SuppressWarnings("null")
                        int idOrder = Integer.compare(Block.getIdFromBlock(a.block.field_150939_a), Block.getIdFromBlock(b.block.field_150939_a));
                        if (idOrder != 0) return idOrder;

                        int metaOrder = Integer.compare(a.metadata, b.metadata);
                        if (metaOrder != 0) return metaOrder;

                        return 0;
                    });
                    
                    return new LinkedList<>(blocks);
                });
        
                PENDING_BUILDS.put(player, pending);
            }
        }

        if(ticksUsed >= 20 && (ticksUsed % 10) == 0 && !player.worldObj.isRemote) {
            PENDING_BUILDS.get(player).tryPlaceBlocks(stack, player);
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

    //#region UI

    public ModularWindow createWindow(UIBuildContext buildContext) {
        buildContext.setShowNEI(false);

        ModularWindow.Builder builder = ModularWindow.builder(new Size(176, 272));

        builder.widget(getMenuOptions(buildContext).build());

        return builder.build();
    }

    private static void withState(UIBuildContext buildContext, Consumer<NBTState> fn) {
        ItemStack heldStack = buildContext.getPlayer().getHeldItem();
        NBTState state = getState(heldStack);
        fn.accept(state);
        setState(heldStack, state);
        buildContext.getPlayer().inventory.markDirty();
    }

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

    }

    private void addMovingOptions(RadialMenuBuilder builder, UIBuildContext buildContext, ItemStack heldStack) {

    }

    private void addDebuggingOptions(RadialMenuBuilder builder, UIBuildContext buildContext, ItemStack heldStack) {

    }

    //#endregion

    //#region Rendering

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

        if(held != null && held.getItem() == this) {
            NBTState state = getState(held);

            Location coordA = state.config.getCoordA(player);
            Location coordB = state.config.getCoordB(player);

            boolean isAValid = coordA != null && coordA.isInWorld(player.worldObj);
            boolean isBValid = coordB != null && coordB.isInWorld(player.worldObj);

            if(isAValid) {
                Objects.requireNonNull(coordA);
                drawBox(player, player.worldObj, coordA.x, coordA.y, coordA.z, event.partialTicks);

                if (state.config.coordAOffset != null) {
                    drawRulers(player, coordA.x, coordA.y, coordA.z, event.partialTicks);
                }
            }

            if(isBValid) {
                Objects.requireNonNull(coordB);
                drawBox(player, player.worldObj, coordB.x, coordB.y, coordB.z, event.partialTicks);

                if (state.config.coordBOffset != null) {
                    drawRulers(player, coordB.x, coordB.y, coordB.z, event.partialTicks);
                }
            }

            if(isAValid && isBValid) {
                Objects.requireNonNull(coordA);
                Objects.requireNonNull(coordB);

                boolean spawn =
                    (System.currentTimeMillis() - LAST_SPAWN_MS_EPOCH) >= SPAWN_INTERVAL_MS ||
                    !Objects.equals(DRAWN_CONFIG, state.config);
                
                if(spawn) {
                    LAST_SPAWN_MS_EPOCH = System.currentTimeMillis();
                    DRAWN_CONFIG = state.config;
                    
                    StructureLibAPI.startHinting(player.worldObj);
        
                    for(PendingBlock block : state.getPendingBlocks()) {
                        if(block.worldId == player.worldObj.provider.dimensionId && block.block != null && block.block.field_150939_a != Blocks.air) {
                            StructureLibAPI.hintParticle(
                                player.worldObj,
                                block.x, block.y, block.z,
                                block.block.field_150939_a,
                                block.metadata
                            );
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
                        (int)(SPAWN_INTERVAL_MS * 20 / 1000),
                        false,
                        false
                    );
                }
            }
        } else {
            LAST_SPAWN_MS_EPOCH = 0;
        }
    }

    private void drawBox(EntityPlayer player, World world, int x, int y, int z, float partialTickTime) {
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.75F);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);
        float f1 = 0.002F;
        Block block = world.getBlock(x, y, z);

        block.setBlockBoundsBasedOnState(world, x, y, z);
        double d0 = player.lastTickPosX + (player.posX - player.lastTickPosX) * (double)partialTickTime;
        double d1 = player.lastTickPosY + (player.posY - player.lastTickPosY) * (double)partialTickTime;
        double d2 = player.lastTickPosZ + (player.posZ - player.lastTickPosZ) * (double)partialTickTime;
        RenderGlobal.drawOutlinedBoundingBox(block.getSelectedBoundingBoxFromPool(world, x, y, z).expand((double)f1, (double)f1, (double)f1).getOffsetBoundingBox(-d0, -d1, -d2), -1);

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    private static final int RULER_LENGTH = 128;
    
    private void drawRulers(EntityPlayer player, int x, int y, int z, float partialTickTime) {
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(1.0F, 0.0F, 0.0F, 0.75F);
        GL11.glLineWidth(2.0F);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glDepthMask(false);

        GL11.glBegin(GL11.GL_LINES);

        double x1 = x + 0.5;
        double y1 = y + 0.5;
        double z1 = z + 0.5;

        for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
            double x2 = dir.offsetX * RULER_LENGTH + x1;
            double y2 = dir.offsetY * RULER_LENGTH + y1;
            double z2 = dir.offsetZ * RULER_LENGTH + z1;

            GL11.glVertex3d(x1, y1, z1);
            GL11.glVertex3d(x2, y2, z2);
        }

        GL11.glEnd();

        GL11.glDepthMask(true);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    //#endregion
}
