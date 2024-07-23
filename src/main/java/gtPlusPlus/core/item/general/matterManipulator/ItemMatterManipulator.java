package gtPlusPlus.core.item.general.matterManipulator;

import static gregtech.api.enums.Mods.GTPlusPlus;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;

import org.joml.Vector3d;
import org.lwjgl.opengl.GL11;

import com.gtnewhorizon.gtnhlib.util.AboveHotbarHUD;
import com.gtnewhorizon.structurelib.StructureLibAPI;
import com.gtnewhorizons.modularui.api.UIInfos;
import com.gtnewhorizons.modularui.api.math.Size;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import appeng.api.features.INetworkEncodable;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.INetworkUpdatableItem;
import gregtech.api.net.GT_Packet_UpdateItem;
import gtPlusPlus.core.creative.AddToCreativeTab;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.BlockSelectMode;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.CoordMode;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.Location;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.PendingAction;
import gtPlusPlus.core.item.general.matterManipulator.NBTState.Shape;
import ic2.api.item.ElectricItem;
import ic2.api.item.IElectricItem;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.EnumAction;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.common.MinecraftForge;

public class ItemMatterManipulator extends Item implements IElectricItem, INetworkUpdatableItem, INetworkEncodable {

    public ItemMatterManipulator() {
        this.setCreativeTab(AddToCreativeTab.tabTools);
        this.setUnlocalizedName("itemMatterManipulator");
        this.setMaxStackSize(1);
        this.setTextureName(GTPlusPlus.ID + ":" + "itemMatterManipulator");

        GameRegistry.registerItem(this, "itemMatterManipulator");
        MinecraftForge.EVENT_BUS.register(this);
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
        return 8; // ZPM
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
        ElectricItem.manager.charge(stack, Integer.MAX_VALUE, Integer.MAX_VALUE, true, false);
        final ItemStack fullStack = new ItemStack(this, 1, this.getMaxDamage());
        subItems.add(fullStack);
    }

    @Override
    public EnumRarity getRarity(ItemStack p_77613_1_) {
        return EnumRarity.rare;
    }

    private static NBTState getState(ItemStack itemStack) {
        return NBTState.load(getOrCreateNbtData(itemStack));
    }

    private static void setState(ItemStack itemStack, NBTState state) {
        var newState = state.save();

        if(!Objects.equals(newState, itemStack.getTagCompound())) {
            boolean needsSyncToServer =
                NetworkUtils.isClient() &&
                (
                    itemStack.getTagCompound() == null || 
                    !Objects.equals(newState.getTag("config"), itemStack.getTagCompound().getTag("config"))
                );

            itemStack.setTagCompound(newState);

            if(needsSyncToServer) {
                GT_Values.NW.sendToServer(new GT_Packet_UpdateItem(
                    newState.getCompoundTag("config")
                ));
            }
        }
    }

    @Override
    public boolean receive(ItemStack stack, EntityPlayerMP player, NBTTagCompound tag) {
        getOrCreateNbtData(stack).setTag("config", tag);

        return true;
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
        var state = getState(itemStack);

        addInfoLine(desc, "Coordinate A: %s", state.config.coordA);
        addInfoLine(desc, "Coordinate B: %s", state.config.coordB);

        addInfoLine(desc, "Corner block: %s", state.config.getCorners(), ItemStack::getDisplayName);
        addInfoLine(desc, "Edge block: %s", state.config.getEdges(), ItemStack::getDisplayName);
        addInfoLine(desc, "Face block: %s", state.config.getFaces(), ItemStack::getDisplayName);
        addInfoLine(desc, "Volume block: %s", state.config.getVolumes(), ItemStack::getDisplayName);
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
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float subX, float subY, float subZ) {
        var state = getState(itemStack);
        
        if(state.config.action == PendingAction.SELECTING_BLOCK) {
            var hit = Minecraft.getMinecraft().objectMouseOver;

            Optional<ItemStack> selected = null;

            if(hit != null && hit.typeOfHit == MovingObjectType.BLOCK) {
                var block = world.getBlock(hit.blockX, hit.blockY, hit.blockZ);
    
                selected = Optional.ofNullable(block.getPickBlock(hit, world, hit.blockX, hit.blockY, hit.blockZ, player));
            } else if(hit == null || hit.typeOfHit == MovingObjectType.MISS) {
                selected = Optional.empty();
            }

            if(selected != null) {
                switch(state.config.blockSelectMode) {
                    case CORNERS: {
                        state.config.setCorners(selected.orElse(null));
                        break;
                    }
                    case EDGES: {
                        state.config.setEdges(selected.orElse(null));
                        break;
                    }
                    case FACES: {
                        state.config.setFaces(selected.orElse(null));
                        break;
                    }
                    case VOLUMES: {
                        state.config.setVolumes(selected.orElse(null));
                        break;
                    }
                    case ALL: {
                        state.config.setCorners(selected.orElse(null));
                        state.config.setEdges(selected.orElse(null));
                        state.config.setFaces(selected.orElse(null));
                        state.config.setVolumes(selected.orElse(null));
                        break;
                    }
                }
            }

            state.config.action = null;
        } else if(state.config.action == null) {
            switch(state.config.coordMode) {
                case SET_A: {
                    state.config.coordA = new Location(world, x, y, z).offset(DirectionUtil.fromSide(side));
                    break;
                }
                case SET_B: {
                    state.config.coordB = new Location(world, x, y, z).offset(DirectionUtil.fromSide(side));
                    break;
                }
                case SET_INTERLEAVED: {
                    if(state.config.coordB != null) {
                        state.config.coordA = new Location(world, x, y, z).offset(DirectionUtil.fromSide(side));
                        state.config.coordB = null;
                    } else {
                        state.config.coordB = new Location(world, x, y, z).offset(DirectionUtil.fromSide(side));
                    }
                    break;
                }
            }
        }

        setState(itemStack, state);

        return true;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStackIn, World worldIn, EntityPlayer player) {
        var state = getState(itemStackIn);

        if(state.config.action == PendingAction.MOVING_COORDS) {
            state.config.updateCoords(player);

            state.config.coordAStart = null;
            state.config.coordBStart = null;
            state.config.action = null;

            if(worldIn.isRemote) {
                setState(itemStackIn, state);
            }
        } else if (player.isSneaking()) {
            player.setItemInUse(itemStackIn, Integer.MAX_VALUE);
        } else {
            if(worldIn.isRemote) {
                UIInfos.openClientUI(player, this::createWindow);
            }
        }

        return itemStackIn;
    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.bow;
    }

    @Override
    public void onUsingTick(ItemStack stack, EntityPlayer player, int count) {
        new Exception().printStackTrace();

        int ticksUsed = Integer.MAX_VALUE - count;

        if(player.worldObj.isRemote && ticksUsed > 0 && (ticksUsed % (2 * 20)) == 0) {
            player.worldObj.playSoundAtEntity(
                player,
                SoundResource.MOB_ENDERMEN_PORTAL.name(),
                1.0F, 1.0F
            );
        }
    }

    @Override
    public String getEncryptionKey(ItemStack item) {
        return getState(item).encKey;
    }

    @Override
    public void setEncryptionKey(ItemStack item, String encKey, String name) {
        withState(item, state -> {
            state.encKey = encKey;

            if(state.hasMEConnection()) {
                state.connectToMESystem();
            }
        });
    }

    //#region UI

    public ModularWindow createWindow(UIBuildContext buildContext) {
        buildContext.setShowNEI(false);

        ModularWindow.Builder builder = ModularWindow.builder(new Size(176, 272));

        var heldStack = buildContext.getPlayer().getCurrentEquippedItem();

        builder.widget(getMenuOptions(buildContext, heldStack).build());

        return builder.build();
    }

    private static void withState(ItemStack heldStack, Consumer<NBTState> fn) {
        var state = getState(heldStack);
        fn.accept(state);
        setState(heldStack, state);
    }

    private RadialMenuBuilder getMenuOptions(UIBuildContext buildContext, ItemStack heldStack) {
        return new RadialMenuBuilder(buildContext)
            .innerIcon(new ItemStack(this))
            .branch()
                .label("Move Coords")
                .option()
                    .label("Set Coord A")
                    .onClicked(() -> {
                        withState(heldStack, state -> {
                            state.config.coordMode = CoordMode.SET_A;
                            state.config.action = null;
                        });
                    })
                .done()
                .option()
                    .label("Set Coord B")
                    .onClicked(() -> {
                        withState(heldStack, state -> {
                            state.config.coordMode = CoordMode.SET_B;
                            state.config.action = null;
                        });
                    })
                .done()
                .option()
                    .label("Set Both")
                    .onClicked(() -> {
                        withState(heldStack, state -> {
                            state.config.coordMode = CoordMode.SET_INTERLEAVED;
                            state.config.action = null;
                        });
                    })
                .done()
                .option()
                    .label("Move Coord A")
                    .onClicked(() -> {
                        withState(heldStack, state -> {
                            state.config.action = PendingAction.MOVING_COORDS;

                            var player = buildContext.getPlayer();
                            state.config.coordAStart = new Vector3d(player.posX, player.posY, player.posZ);
                            state.config.coordBStart = null;
                        });
                    })
                .done()
                .option()
                    .label("Move Coord B")
                    .onClicked(() -> {
                        withState(heldStack, state -> {
                            state.config.action = PendingAction.MOVING_COORDS;

                            var player = buildContext.getPlayer();
                            state.config.coordAStart = null;
                            state.config.coordBStart = new Vector3d(player.posX, player.posY, player.posZ);
                        });
                    })
                .done()
                .option()
                    .label("Move Both")
                    .onClicked(() -> {
                        withState(heldStack, state -> {
                            state.config.action = PendingAction.MOVING_COORDS;

                            var player = buildContext.getPlayer();
                            state.config.coordAStart = new Vector3d(player.posX, player.posY, player.posZ);
                            state.config.coordBStart = new Vector3d(player.posX, player.posY, player.posZ);
                        });
                    })
                .done()
            .done()
            .branch()
                .label("Set Mode")
            .done()
            .branch()
                .label("Select Blocks")
                .option()
                    .label("Set Corners")
                    .onClicked(() -> {
                        withState(heldStack, state -> {
                            state.config.blockSelectMode = BlockSelectMode.CORNERS;
                            state.config.action = PendingAction.SELECTING_BLOCK;
                        });
                    })
                .done()
                .option()
                    .label("Set Edges")
                    .onClicked(() -> {
                        withState(heldStack, state -> {
                            state.config.blockSelectMode = BlockSelectMode.EDGES;
                            state.config.action = PendingAction.SELECTING_BLOCK;
                        });
                    })
                .done()
                .option()
                    .label("Set Faces")
                    .onClicked(() -> {
                        withState(heldStack, state -> {
                            state.config.blockSelectMode = BlockSelectMode.FACES;
                            state.config.action = PendingAction.SELECTING_BLOCK;
                        });
                    })
                .done()
                .option()
                    .label("Set Volumes")
                    .onClicked(() -> {
                        withState(heldStack, state -> {
                            state.config.blockSelectMode = BlockSelectMode.VOLUMES;
                            state.config.action = PendingAction.SELECTING_BLOCK;
                        });
                    })
                .done()
                .option()
                    .label("Set All")
                    .onClicked(() -> {
                        withState(heldStack, state -> {
                            state.config.blockSelectMode = BlockSelectMode.ALL;
                            state.config.action = PendingAction.SELECTING_BLOCK;
                        });
                    })
                .done()
                .option()
                    .label("Clear All")
                    .onClicked(() -> {
                        withState(heldStack, state -> {
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
                        withState(heldStack, state -> {
                            state.config.shape = Shape.LINE;
                        });
                    })
                .done()
                .option()
                    .label("Cube")
                    .onClicked(() -> {
                        withState(heldStack, state -> {
                            state.config.shape = Shape.CUBE;
                        });
                    })
                .done()
                .option()
                    .label("Sphere")
                    .onClicked(() -> {
                        withState(heldStack, state -> {
                            state.config.shape = Shape.SPHERE;
                        });
                    })
                .done()
            .done();
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
        var player = Minecraft.getMinecraft().thePlayer;
        var held = player.getHeldItem();

        if(held != null && held.getItem() == this) {
            var state = getState(held);

            if(state.config.action == PendingAction.MOVING_COORDS) {
                state.config.updateCoords(player);
            }

            var coordA = state.config.coordA;
            var coordB = state.config.coordB;

            boolean isAValid = coordA != null && coordA.worldId == player.worldObj.provider.dimensionId;
            boolean isBValid = coordB != null && coordB.worldId == player.worldObj.provider.dimensionId;

            if(!isAValid || !isBValid) {
                if(isAValid) {
                    Objects.requireNonNull(coordA);
                    drawBox(player, player.worldObj, coordA.x, coordA.y, coordA.z, event.partialTicks);
                }

                if(isBValid) {
                    Objects.requireNonNull(coordB);
                    drawBox(player, player.worldObj, coordB.x, coordB.y, coordB.z, event.partialTicks);
                }
            } else {
                if(GuiScreen.isShiftKeyDown() || state.config.action == PendingAction.MOVING_COORDS) {
                    Objects.requireNonNull(coordA);
                    Objects.requireNonNull(coordB);

                    drawBox(player, player.worldObj, coordA.x, coordA.y, coordA.z, event.partialTicks);
                    drawBox(player, player.worldObj, coordB.x, coordB.y, coordB.z, event.partialTicks);
                }

                var spawn =
                    (System.currentTimeMillis() - LAST_SPAWN_MS_EPOCH) >= SPAWN_INTERVAL_MS ||
                    !Objects.equals(DRAWN_CONFIG, state.config);
                
                if(spawn) {
                    LAST_SPAWN_MS_EPOCH = System.currentTimeMillis();
                    DRAWN_CONFIG = state.config;
                    
                    StructureLibAPI.startHinting(player.worldObj);
        
                    for(var block : state.getPendingBlocks()) {
                        if(block.location.worldId == player.worldObj.provider.dimensionId) {
                            StructureLibAPI.hintParticle(
                                player.worldObj,
                                block.location.x, block.location.y, block.location.z,
                                ((ItemBlock)block.block.getItem()).field_150939_a,
                                block.block.getItemDamage()
                            );
                        }
                    }

                    StructureLibAPI.endHinting(player.worldObj);

                    int x1 = state.config.coordA.x;
                    int y1 = state.config.coordA.y;
                    int z1 = state.config.coordA.z;
                    int x2 = state.config.coordB.x;
                    int y2 = state.config.coordB.y;
                    int z2 = state.config.coordB.z;
            
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

    //#endregion
}
