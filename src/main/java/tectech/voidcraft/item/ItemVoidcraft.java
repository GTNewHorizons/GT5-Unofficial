package tectech.voidcraft.item;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiData;
import com.cleanroommc.modularui.factory.PlayerInventoryGuiFactory;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.modularui2.GTModularScreen;
import tectech.Reference;
import tectech.voidcraft.gui.VoidcraftProgramGui;
import tectech.voidcraft.gui.VoidcraftProgramItemSource;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftCoverComponent;
import tectech.voidcraft.ship.VoidcraftEngineType;
import tectech.voidcraft.ship.VoidcraftFuel;
import tectech.voidcraft.ship.VoidcraftNbt;

/**
 * The digitized Voidcraft — a single, non-stackable item carrying the blueprint grid and the derived stats.
 *
 * <p>
 * This is the payload the Voidcraft Assembler outputs and the Unstable Solar System gateway consumes. It is
 * intentionally not a machine item and not placeable: the ship "lives" inside this item's NBT
 * (see {@link VoidcraftNbt}).
 */
public class ItemVoidcraft extends Item implements IGuiHolder<PlayerInventoryGuiData> {

    public static final ItemVoidcraft INSTANCE;

    private ItemVoidcraft() {
        setHasSubtypes(false);
        setMaxStackSize(1);
        setUnlocalizedName("tm.voidcraft");
        setTextureName(Reference.MODID + ":itemVoidcraft");
    }

    static {
        INSTANCE = new ItemVoidcraft();
    }

    /**
     * Build the voidcraft item for a digitized blueprint.
     *
     * @param blueprint validated blueprint
     * @param name      display name stored in the payload
     * @param uuid      stable identity
     * @param createdAt epoch millis
     * @return the item, ready for output
     */
    public static ItemStack fromBlueprint(VoidcraftBlueprint blueprint, String name, String uuid, long createdAt) {
        return fromBlueprint(blueprint, name, uuid, createdAt, null);
    }

    /**
     * Build the voidcraft item for a digitized blueprint, carrying the controller's program.
     *
     * @param blueprint validated blueprint
     * @param name      display name stored in the payload
     * @param uuid      stable identity
     * @param createdAt epoch millis
     * @param program   the controller's stored program node list ({@code USSProgram} NBT), or null when the
     *                  controller had none (the ship HOLDS at the origin on launch)
     * @return the item, ready for output
     */
    public static ItemStack fromBlueprint(VoidcraftBlueprint blueprint, String name, String uuid, long createdAt,
        NBTTagList program) {
        ItemStack stack = new ItemStack(INSTANCE, 1);
        NBTTagCompound nbt = new NBTTagCompound();
        VoidcraftNbt.write(nbt, blueprint, uuid, name, createdAt);
        if (program != null) {
            nbt.setTag(VoidcraftNbt.TAG_PROGRAM, program);
        }
        stack.setTagCompound(nbt);
        return stack;
    }

    /**
     * @param stack an item stack
     * @return the blueprint stored in it, or null if the payload is missing/corrupt
     */
    public static VoidcraftBlueprint getBlueprint(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ItemVoidcraft)) {
            return null;
        }
        NBTTagCompound nbt = stack.getTagCompound();
        return VoidcraftNbt.read(nbt);
    }

    /**
     * Random identity for freshly digitized ships.
     */
    public static String newUuid() {
        return UUID.randomUUID()
            .toString();
    }

    // Non-stackable via setMaxStackSize(1) in the constructor (1.7.10 has no isStackable()).

    @Override
    public boolean isFull3D() {
        return true;
    }

    /**
     * Whether this is an "empty frame" (no payload) — used to reject gateways and show a distinct tooltip.
     */
    public static boolean isEmptyFrame(ItemStack stack) {
        if (stack == null || stack.getItem() != INSTANCE) {
            return true;
        }
        return VoidcraftNbt.read(stack.getTagCompound()) == null;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        NBTTagCompound nbt = aStack.getTagCompound();
        VoidcraftBlueprint blueprint = VoidcraftNbt.read(nbt);
        if (blueprint == null) {
            aList.add(EnumChatFormatting.GRAY + translateToLocal("item.tm.voidcraft.empty"));
            return;
        }

        String name = nbt.hasKey(VoidcraftNbt.TAG_NAME) ? nbt.getString(VoidcraftNbt.TAG_NAME)
            : translateToLocal("item.tm.voidcraft.unnamed");
        aList.add(EnumChatFormatting.YELLOW + translateToLocalFormatted("item.tm.voidcraft.named", name));

        // The ship's program (the controller's instruction list).
        if (nbt.hasKey(VoidcraftNbt.TAG_PROGRAM)) {
            NBTTagList program = nbt.getTagList(VoidcraftNbt.TAG_PROGRAM, 10);
            aList.add(
                EnumChatFormatting.GREEN + translateToLocalFormatted("item.tm.voidcraft.program", program.tagCount())); // 1.7.10:
                                                                                                                        // tagCount(),
                                                                                                                        // not
                                                                                                                        // tagList().size
        } else {
            aList.add(EnumChatFormatting.GRAY + translateToLocal("item.tm.voidcraft.program.none"));
        }
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.voidcraft.editor"));

        long mass = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_MASS);
        long thrust = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_THRUST);
        double speed = VoidcraftNbt.readDouble(nbt, VoidcraftNbt.TAG_SPEED);
        long cargo = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_CARGO);
        long mining = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_MINING);
        long scan = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_SCAN);
        long construction = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_CONSTRUCTION);
        long starlifter = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_STARLIFTER);
        long logistics = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_LOGISTICS);
        long buffer = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_ENERGY_BUFFER);
        long draw = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_ENERGY_DRAW);
        long integrity = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_INTEGRITY);

        aList.add(
            EnumChatFormatting.GRAY + translateToLocalFormatted(
                "item.tm.voidcraft.stats",
                NumberFormatUtil.formatNumber(mass),
                NumberFormatUtil.formatNumber(thrust),
                String.format("%.2f", speed),
                NumberFormatUtil.formatNumber(cargo)));
        if (mining > 0) {
            aList.add(EnumChatFormatting.GRAY + translateToLocalFormatted("item.tm.voidcraft.stat.mining", mining));
        }
        if (scan > 0) {
            aList.add(EnumChatFormatting.GRAY + translateToLocalFormatted("item.tm.voidcraft.stat.scan", scan));
        }
        if (construction > 0) {
            aList.add(
                EnumChatFormatting.GRAY
                    + translateToLocalFormatted("item.tm.voidcraft.stat.construction", construction));
        }
        if (starlifter > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted("item.tm.voidcraft.stat.starlifter", starlifter));
        }
        if (logistics > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted("item.tm.voidcraft.stat.logistics", logistics));
        }
        if (buffer > 0) {
            aList.add(EnumChatFormatting.GRAY + translateToLocalFormatted("item.tm.voidcraft.stat.buffer", buffer));
        }
        if (draw > 0) {
            aList.add(EnumChatFormatting.GRAY + translateToLocalFormatted("item.tm.voidcraft.stat.draw", draw));
        }
        long thrusters = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_THRUSTERS);
        int engineType = VoidcraftNbt.readInt(nbt, VoidcraftNbt.TAG_ENGINE);
        long fuelCapacity = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_FUEL);
        int frameTier = VoidcraftNbt.readInt(nbt, VoidcraftNbt.TAG_FRAME_TIER);
        if (thrusters > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted("item.tm.voidcraft.stat.thrusters", thrusters));
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted(
                    "item.tm.voidcraft.stat.engine",
                    translateToLocal(engineFamilyKey(engineType))));
        }
        if (fuelCapacity > 0 && engineFamilyRequiresFuel(engineType)) {
            Fluid fuel = VoidcraftFuel.engineFuel(VoidcraftEngineType.byId(engineType));
            if (fuel != null) {
                aList.add(
                    EnumChatFormatting.GRAY + translateToLocalFormatted(
                        "item.tm.voidcraft.stat.fuel",
                        NumberFormatUtil.formatNumber(fuelCapacity),
                        fuel.getLocalizedName()));
            }
        }
        if (frameTier > 0) {
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted("item.tm.voidcraft.stat.frame_tier", frameTier));
        }
        for (Map.Entry<VoidcraftCoverComponent, Long> fee : blueprint.reactorLaunchFuel()
            .entrySet()) {
            Fluid feeFuel = VoidcraftFuel.reactorLaunchFluid(fee.getKey());
            if (feeFuel != null) {
                aList.add(
                    EnumChatFormatting.GRAY + translateToLocalFormatted(
                        "item.tm.voidcraft.stat.launch_fuel",
                        NumberFormatUtil.formatNumber(fee.getValue()),
                        feeFuel.getLocalizedName(),
                        blueprint.countCover(fee.getKey()),
                        fee.getKey()
                            .getDisplayName()));
            }
        }
        // Integrity is the ship's TIME LIMIT: the seconds it survives in the USS (it drops
        // by 1 per second, starting at this maximum on entry; at 0 the ship is lost with its cargo).
        aList.add(
            EnumChatFormatting.GRAY
                + translateToLocalFormatted("item.tm.voidcraft.integrity", NumberFormatUtil.formatNumber(integrity)));
    }

    private static String engineFamilyKey(int engineType) {
        switch (VoidcraftEngineType.byId(engineType)) {
            case ION:
                return "tt.voidcraft.engine.ion";
            case FUSION:
                return "tt.voidcraft.engine.fusion";
            case ANTIMATTER:
                return "tt.voidcraft.engine.antimatter";
            case NONE:
            case STANDARD:
            default:
                return "tt.voidcraft.engine.standard";
        }
    }

    private static boolean engineFamilyRequiresFuel(int engineType) {
        return VoidcraftEngineType.byId(engineType)
            .requiresFuel();
    }

    // region program editor GUI — right-clicking the digitized item in hand opens the same editor as the controller
    // block

    /** Marks a same-tick block right-click (see {@link #openProgramEditor}) on both sides. */
    private static final String BLOCK_USE_TICK_KEY = "vc_program_editor_block_use_tick";

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        player.getEntityData()
            .setLong(BLOCK_USE_TICK_KEY, world.getTotalWorldTime());
        return super.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ);
    }

    /**
     * Right-clicking the digitized item in hand (in the AIR, not sneaking) opens the program editor. Block
     * right-clicks are left untouched (the gateway blueprint flow is block-driven).
     */
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (openProgramEditor(stack, player, world)) {
            return stack;
        }
        return super.onItemRightClick(stack, world, player);
    }

    private static boolean openProgramEditor(ItemStack stack, EntityPlayer player, World world) {
        if (player.isSneaking() || isEmptyFrame(stack)) {
            return false;
        }
        // 1.7.10 sends a second USE_ITEM packet for a block right-click the block did not consume (same tick) —
        // only an AIR right-click opens the editor.
        if (world.getTotalWorldTime() == player.getEntityData()
            .getLong(BLOCK_USE_TICK_KEY)) {
            return false;
        }
        if (world.isRemote) {
            PlayerInventoryGuiFactory.INSTANCE.openFromMainHandClient();
        } else if (player instanceof EntityPlayerMP) {
            PlayerInventoryGuiFactory.INSTANCE.openFromMainHand(player);
        }
        return true;
    }

    @Override
    public ModularPanel buildUI(PlayerInventoryGuiData guiData, PanelSyncManager syncManager, UISettings settings) {
        return new VoidcraftProgramGui(new VoidcraftProgramItemSource(guiData, INSTANCE))
            .build(guiData, syncManager, settings);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public ModularScreen createScreen(PlayerInventoryGuiData data, ModularPanel mainPanel) {
        return new GTModularScreen(mainPanel, GTGuiThemes.TECTECH_STANDARD);
    }
    // endregion

    /**
     * Register the item with the game registry. Called from the voidcraft loader (pre-load phase).
     */
    public static void run() {
        GameRegistry.registerItem(INSTANCE, "tm.voidcraft");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
    }
}
