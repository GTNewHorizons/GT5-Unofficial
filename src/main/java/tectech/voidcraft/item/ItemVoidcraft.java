package tectech.voidcraft.item;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;
import java.util.UUID;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;

import com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import tectech.Reference;
import tectech.voidcraft.ship.VoidcraftBlueprint;
import tectech.voidcraft.ship.VoidcraftConstants;
import tectech.voidcraft.ship.VoidcraftNbt;
import tectech.voidcraft.ship.VoidcraftRole;

/**
 * The digitized Voidcraft — a single, non-stackable item carrying the blueprint grid, the derived stats, the role
 * set and the hybrid efficiency.
 *
 * <p>
 * This is the payload the Voidcraft Assembler outputs and (in a later phase) the Unstable Solar System gateway
 * consumes. It is intentionally not a machine item and not placeable: the ship "lives" inside this item's NBT
 * (see {@link VoidcraftNbt}).
 */
public class ItemVoidcraft extends Item {

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
        ItemStack stack = new ItemStack(INSTANCE, 1);
        NBTTagCompound nbt = new NBTTagCompound();
        VoidcraftNbt.write(nbt, blueprint, uuid, name, createdAt);
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

        int roles = VoidcraftNbt.readInt(nbt, VoidcraftNbt.TAG_ROLES);
        if (roles == 0) {
            aList.add(EnumChatFormatting.AQUA + translateToLocal("item.tm.voidcraft.role.none"));
        } else {
            StringBuilder sb = new StringBuilder();
            for (VoidcraftRole role : VoidcraftRole.activeRoles(roles)) {
                if (sb.length() > 0) {
                    sb.append(", ");
                }
                sb.append(translateToLocal(role.getLangKey()));
            }
            aList.add(EnumChatFormatting.AQUA + translateToLocalFormatted("item.tm.voidcraft.role.list", sb));
        }

        double efficiency = VoidcraftNbt.readDouble(nbt, VoidcraftNbt.TAG_EFFICIENCY);
        if (efficiency < 1.0) {
            aList.add(
                EnumChatFormatting.GOLD + translateToLocalFormatted(
                    "item.tm.voidcraft.hybrid",
                    String.format("%.0f%%", efficiency * 100.0)));
        }

        long mass = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_MASS);
        long thrust = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_THRUST);
        double speed = VoidcraftNbt.readDouble(nbt, VoidcraftNbt.TAG_SPEED);
        long cargo = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_CARGO);
        long mining = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_MINING);
        long scan = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_SCAN);
        long construction = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_CONSTRUCTION);
        long starlifter = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_STARLIFTER);
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
        if (thrust > 0) {
            long thrustX = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_THRUST_X);
            long thrustY = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_THRUST_Y);
            long thrustZ = VoidcraftNbt.readLong(nbt, VoidcraftNbt.TAG_THRUST_Z);
            aList.add(
                EnumChatFormatting.GRAY + translateToLocalFormatted(
                    "item.tm.voidcraft.stat.net_thrust",
                    String.format("%+d", thrustX),
                    String.format("%+d", thrustY),
                    String.format("%+d", thrustZ)));
        }
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
        if (buffer > 0) {
            aList.add(EnumChatFormatting.GRAY + translateToLocalFormatted("item.tm.voidcraft.stat.buffer", buffer));
        }
        if (draw > 0) {
            aList.add(EnumChatFormatting.GRAY + translateToLocalFormatted("item.tm.voidcraft.stat.draw", draw));
        }
        aList.add(
            EnumChatFormatting.GRAY + translateToLocalFormatted(
                integrity >= VoidcraftConstants.RECOVERABLE_INTEGRITY_THRESHOLD
                    ? "item.tm.voidcraft.integrity.recoverable"
                    : "item.tm.voidcraft.integrity.expendable",
                NumberFormatUtil.formatNumber(integrity)));
    }

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
