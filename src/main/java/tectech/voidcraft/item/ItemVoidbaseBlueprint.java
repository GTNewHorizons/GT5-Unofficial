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

import org.lwjgl.input.Keyboard;

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
import gregtech.api.util.GTUtility;
import tectech.Reference;
import tectech.voidcraft.gui.VoidcraftProgramGui;
import tectech.voidcraft.gui.VoidcraftProgramItemSource;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.uss.USSItemCargo;

/**
 * The digitized Voidbase - a single, non-stackable, REUSABLE item carrying the station blueprint grid (up to
 * 15x15x15), the derived stats, the parts list and the controller program. The Voidbase Assembler outputs it;
 * the Unstable Solar System gateway keeps it in its blueprint slot and copies the blueprint data into each
 * Constructor it launches (the item itself is never consumed - one blueprint builds as many
 * stations as the player has parts for).
 *
 * <p>
 * The station "lives" inside this item NBT (the same VoidcraftNbt payload format as the Voidcraft item - see
 * {@link VoidcraftNbt#readBase}).
 */
public class ItemVoidbaseBlueprint extends Item implements IGuiHolder<PlayerInventoryGuiData> {

    public static final ItemVoidbaseBlueprint INSTANCE;

    private ItemVoidbaseBlueprint() {
        setHasSubtypes(false);
        setMaxStackSize(1);
        setUnlocalizedName("tm.voidbase_blueprint");
        setTextureName(Reference.MODID + ":itemVoidcraft");
    }

    static {
        INSTANCE = new ItemVoidbaseBlueprint();
    }

    /**
     * Build the voidbase blueprint item for a digitized base.
     *
     * @param blueprint validated base blueprint
     * @param name      display name stored in the payload
     * @param uuid      stable identity
     * @param createdAt epoch millis
     * @param program   the controller stored program node list (USSProgram NBT), or null when the controller
     *                  had none
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
     * @return the base blueprint stored in it, or null if the payload is missing/corrupt
     */
    public static VoidcraftBlueprint getBlueprint(ItemStack stack) {
        if (stack == null || !(stack.getItem() instanceof ItemVoidbaseBlueprint)) {
            return null;
        }
        return VoidcraftNbt.readBase(stack.getTagCompound());
    }

    /** Random identity for freshly digitized bases. */
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
     * Whether this is an "empty blueprint" (no payload) - used to reject gateways and show a distinct tooltip.
     */
    public static boolean isEmptyBlueprint(ItemStack stack) {
        if (stack == null || stack.getItem() != INSTANCE) {
            return true;
        }
        return VoidcraftNbt.readBase(stack.getTagCompound()) == null;
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        NBTTagCompound nbt = aStack.getTagCompound();
        VoidcraftBlueprint blueprint = VoidcraftNbt.readBase(nbt);
        if (blueprint == null) {
            aList.add(EnumChatFormatting.GRAY + translateToLocal("item.tm.voidbase_blueprint.empty"));
            return;
        }

        String name = nbt.hasKey(VoidcraftNbt.TAG_NAME) ? nbt.getString(VoidcraftNbt.TAG_NAME)
            : translateToLocal("item.tm.voidbase_blueprint.unnamed");
        aList.add(EnumChatFormatting.YELLOW + translateToLocalFormatted("item.tm.voidbase_blueprint.named", name));

        if (nbt.hasKey(VoidcraftNbt.TAG_PROGRAM)) {
            NBTTagList program = nbt.getTagList(VoidcraftNbt.TAG_PROGRAM, 10);
            aList.add(
                EnumChatFormatting.GREEN
                    + translateToLocalFormatted("item.tm.voidbase_blueprint.program", program.tagCount()));
        } else {
            aList.add(EnumChatFormatting.GRAY + translateToLocal("item.tm.voidbase_blueprint.program.none"));
        }
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.tm.voidbase_blueprint.editor"));

        long mass = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_MASS);
        long buffer = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_ENERGY_BUFFER);
        long gen = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_ENERGY_GEN);
        long integrity = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_INTEGRITY);
        long parts = 0;
        for (Long count : blueprint.partsList()
            .values()) {
            parts += count;
        }
        aList.add(
            EnumChatFormatting.GRAY + translateToLocalFormatted(
                "item.tm.voidbase_blueprint.stats",
                NumberFormatUtil.formatNumber(mass),
                NumberFormatUtil.formatNumber(parts),
                NumberFormatUtil.formatNumber(buffer),
                NumberFormatUtil.formatNumber(gen)));
        aList.add(
            EnumChatFormatting.GRAY + translateToLocalFormatted(
                "item.tm.voidbase_blueprint.integrity",
                NumberFormatUtil.formatNumber(integrity)));

        // Shift (keyboard-poll, the GT ISecondaryDescribable pattern): the required components - the
        // Constructor's parts list, one line per part.
        if (GTUtility.isClient() && Keyboard.isKeyDown(Keyboard.KEY_LSHIFT)) {
            Map<String, Long> components = blueprint.partsList();
            if (components.isEmpty()) {
                aList.add(EnumChatFormatting.GRAY + translateToLocal("item.tm.voidbase_blueprint.parts.none"));
            } else {
                aList.add(EnumChatFormatting.AQUA + translateToLocal("item.tm.voidbase_blueprint.parts"));
                for (Map.Entry<String, Long> entry : components.entrySet()) {
                    ItemStack component = USSItemCargo.stackOf(entry.getKey(), 1L);
                    String label = component != null ? component.getDisplayName() : entry.getKey();
                    aList.add(
                        EnumChatFormatting.GRAY + NumberFormatUtil.formatNumber(entry.getValue())
                            + "x "
                            + EnumChatFormatting.RESET
                            + label);
                }
            }
        }
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
     * Right-clicking the digitized blueprint in hand (in the AIR, not sneaking) opens the program editor. Block
     * right-clicks are left untouched (the gateway blueprint launch flow is block-driven).
     */
    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (openProgramEditor(stack, player, world)) {
            return stack;
        }
        return super.onItemRightClick(stack, world, player);
    }

    private static boolean openProgramEditor(ItemStack stack, EntityPlayer player, World world) {
        if (player.isSneaking() || isEmptyBlueprint(stack)) {
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
        GameRegistry.registerItem(INSTANCE, "tm.voidbase_blueprint");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        itemIcon = iconRegister.registerIcon(getIconString());
    }
}
