package gregtech.common.items.matterManipulator;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagByte;
import net.minecraft.nbt.NBTTagByteArray;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagDouble;
import net.minecraft.nbt.NBTTagFloat;
import net.minecraft.nbt.NBTTagInt;
import net.minecraft.nbt.NBTTagIntArray;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagLong;
import net.minecraft.nbt.NBTTagShort;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraftforge.common.util.ForgeDirection;

import org.joml.Vector3i;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;

import appeng.api.implementations.items.IUpgradeModule;
import appeng.api.storage.ICellWorkbenchItem;
import appeng.api.storage.data.IAEItemStack;
import appeng.parts.automation.UpgradeInventory;
import appeng.util.Platform;
import appeng.util.item.AEItemStack;
import gregtech.api.util.GTUtility;
import gregtech.common.items.matterManipulator.BlockAnalyzer.RequiredItemAnalysis;
import gregtech.common.items.matterManipulator.NBTState.Location;
import gregtech.common.items.matterManipulator.NBTState.PendingBlock;
import it.unimi.dsi.fastutil.Pair;

public class MMUtils {

    private MMUtils() {}

    public static MovingObjectPosition getHitResult(EntityPlayer player) {
        double reachDistance = player instanceof EntityPlayerMP mp ? mp.theItemInWorldManager.getBlockReachDistance()
            : GTUtility.getClientReachDistance();

        Vec3 posVec = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);

        Vec3 lookVec = player.getLook(1);

        Vec3 modifiedPosVec = posVec
            .addVector(lookVec.xCoord * reachDistance, lookVec.yCoord * reachDistance, lookVec.zCoord * reachDistance);

        MovingObjectPosition hit = player.worldObj.rayTraceBlocks(posVec, modifiedPosVec);

        return hit != null && hit.typeOfHit != MovingObjectType.BLOCK ? null : hit;
    }

    public static Vector3i getLookingAtLocation(EntityPlayer player) {
        double reachDistance = player instanceof EntityPlayerMP mp ? mp.theItemInWorldManager.getBlockReachDistance()
            : GTUtility.getClientReachDistance();

        Vec3 posVec = Vec3.createVectorHelper(player.posX, player.posY + player.getEyeHeight(), player.posZ);

        Vec3 lookVec = player.getLook(1);

        Vec3 modifiedPosVec = posVec
            .addVector(lookVec.xCoord * reachDistance, lookVec.yCoord * reachDistance, lookVec.zCoord * reachDistance);

        MovingObjectPosition hit = player.worldObj.rayTraceBlocks(posVec, modifiedPosVec);

        Vector3i target;

        if (hit != null && hit.typeOfHit == MovingObjectType.BLOCK) {
            target = new Vector3i(hit.blockX, hit.blockY, hit.blockZ);

            if (!player.isSneaking()) {
                ForgeDirection dir = ForgeDirection.getOrientation(hit.sideHit);
                target.add(dir.offsetX, dir.offsetY, dir.offsetZ);
            }
        } else {
            target = new Vector3i(
                MathHelper.floor_double(modifiedPosVec.xCoord),
                MathHelper.floor_double(modifiedPosVec.yCoord),
                MathHelper.floor_double(modifiedPosVec.zCoord));
        }

        return target;
    }

    public static Vector3i getRegionDeltas(Location a, Location b) {
        if (a == null || b == null || a.worldId != b.worldId) return null;

        int x1 = a.x;
        int y1 = a.y;
        int z1 = a.z;
        int x2 = b.x;
        int y2 = b.y;
        int z2 = b.z;

        int minX = Math.min(x1, x2);
        int minY = Math.min(y1, y2);
        int minZ = Math.min(z1, z2);
        int maxX = Math.max(x1, x2);
        int maxY = Math.max(y1, y2);
        int maxZ = Math.max(z1, z2);

        int dX = (maxX - minX) * (minX < x1 ? -1 : 1);
        int dY = (maxY - minY) * (minY < y1 ? -1 : 1);
        int dZ = (maxZ - minZ) * (minZ < z1 ? -1 : 1);

        return new Vector3i(dX, dY, dZ);
    }

    public static Vector3i getRegionDeltas(Location a, Location b, Location c) {
        if (a == null || b == null || c == null || a.worldId != b.worldId || a.worldId != c.worldId) return null;

        Vector3i vA = a.toVec();
        Vector3i vB = b.toVec();
        Vector3i vC = c.toVec();

        Vector3i max = new Vector3i(vA).max(vB)
            .max(vC);
        Vector3i min = new Vector3i(vA).min(vB)
            .min(vC);

        int dX = (max.x - min.x) * (min.x < a.x ? -1 : 1);
        int dY = (max.y - min.y) * (min.y < a.y ? -1 : 1);
        int dZ = (max.z - min.z) * (min.z < a.z ? -1 : 1);

        return new Vector3i(dX, dY, dZ);
    }

    public static AxisAlignedBB getBoundingBox(Location l, Vector3i deltas) {
        int minX = Math.min(l.x, l.x + deltas.x);
        int minY = Math.min(l.y, l.y + deltas.y);
        int minZ = Math.min(l.z, l.z + deltas.z);
        int maxX = Math.max(l.x, l.x + deltas.x) + 1;
        int maxY = Math.max(l.y, l.y + deltas.y) + 1;
        int maxZ = Math.max(l.z, l.z + deltas.z) + 1;

        return AxisAlignedBB.getBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
    }

    public static List<Vector3i> getBlocksInBB(Location l, Vector3i deltas) {
        int minX = Math.min(l.x, l.x + deltas.x);
        int minY = Math.min(l.y, l.y + deltas.y);
        int minZ = Math.min(l.z, l.z + deltas.z);
        int maxX = Math.max(l.x, l.x + deltas.x) + 1;
        int maxY = Math.max(l.y, l.y + deltas.y) + 1;
        int maxZ = Math.max(l.z, l.z + deltas.z) + 1;

        int dX = maxX - minX;
        int dY = maxY - minY;
        int dZ = maxZ - minZ;

        List<Vector3i> blocks = new ArrayList<>();

        for (int y = 0; y < dY; y++) {
            for (int z = 0; z < dZ; z++) {
                for (int x = 0; x < dX; x++) {
                    blocks.add(new Vector3i(minX + x, minY + y, minZ + z));
                }
            }
        }

        return blocks;
    }

    public static void emptyInventory(IPseudoInventory dest, IInventory src) {
        if (src == null) return;

        int size = src.getSizeInventory();

        for (int i = 0; i < size; i++) {
            ItemStack stack = src.getStackInSlot(i);

            if (stack != null && stack.getItem() != null) {
                src.setInventorySlotContents(i, null);

                if (stack.getItem() instanceof ICellWorkbenchItem cellWorkbenchItem
                    && cellWorkbenchItem.isEditable(stack)) {
                    emptyInventory(dest, cellWorkbenchItem.getUpgradesInventory(stack));
                    clearInventory(cellWorkbenchItem.getConfigInventory(stack));

                    NBTTagCompound tag = Platform.openNbtData(stack);
                    tag.removeTag("FuzzyMode");
                    tag.removeTag("upgrades");
                    tag.removeTag("list");
                    tag.removeTag("OreFilter");

                    if (tag.hasNoTags()) tag = null;

                    stack.setTagCompound(tag);

                    dest.givePlayerItems(stack);
                } else {
                    dest.givePlayerItems(stack);
                }
            }
        }

        src.markDirty();
    }

    public static void clearInventory(IInventory inv) {
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            inv.setInventorySlotContents(i, null);
        }
        inv.markDirty();
    }

    public static PortableItemStack[] fromInventory(IInventory inventory) {
        List<ItemStack> merged = GTUtility.mergeStacks(Arrays.asList(GTUtility.inventoryToArray(inventory, false)));
        ArrayList<PortableItemStack> out = new ArrayList<>();

        for (ItemStack stack : merged) {
            out.add(new PortableItemStack(stack));
        }

        return out.toArray(new PortableItemStack[out.size()]);
    }

    public static PortableItemStack[] fromInventoryNoMerge(IInventory inventory) {
        PortableItemStack[] out = new PortableItemStack[inventory.getSizeInventory()];

        for (int i = 0; i < out.length; i++) {
            ItemStack stack = inventory.getStackInSlot(i);

            out[i] = stack == null ? null : new PortableItemStack(stack);
        }

        return out;
    }

    public static boolean installUpgrades(IPseudoInventory src, UpgradeInventory dest, PortableItemStack[] pupgrades,
        boolean consume, boolean simulate) {
        List<ItemStack> stacks = GTUtility.mapToList(pupgrades, PortableItemStack::toStack);

        stacks.removeIf(i -> i == null || !(i.getItem() instanceof IUpgradeModule));

        for (ItemStack stack : stacks) {
            stack.stackSize = Math
                .min(stack.stackSize, dest.getMaxInstalled(((IUpgradeModule) stack.getItem()).getType(stack)));
        }

        List<ItemStack> split = GTUtility.getStacksOfSize(stacks, dest.getInventoryStackLimit());

        ItemStack[] upgrades = split.subList(0, Math.min(split.size(), dest.getSizeInventory()))
            .toArray(new ItemStack[0]);

        if (!consume || src.tryConsumeItems(upgrades)) {
            if (!simulate) {
                emptyInventory(src, dest);

                for (int i = 0; i < upgrades.length; i++) {
                    dest.setInventorySlotContents(i, upgrades[i]);
                }

                dest.markDirty();
            }

            return true;
        } else {
            return false;
        }
    }

    @SuppressWarnings("unchecked")
    public static JsonElement toJsonObject(NBTBase nbt) {
        if (nbt == null) {
            return null;
        }

        if (nbt instanceof NBTTagCompound) {
            // NBTTagCompound
            final NBTTagCompound nbtTagCompound = (NBTTagCompound) nbt;
            final Map<String, NBTBase> tagMap = (Map<String, NBTBase>) nbtTagCompound.tagMap;

            JsonObject root = new JsonObject();

            for (Map.Entry<String, NBTBase> nbtEntry : tagMap.entrySet()) {
                root.add(nbtEntry.getKey(), toJsonObject(nbtEntry.getValue()));
            }

            return root;
        } else if (nbt instanceof NBTTagByte) {
            // Number (byte)
            return new JsonPrimitive(((NBTTagByte) nbt).func_150290_f());
        } else if (nbt instanceof NBTTagShort) {
            // Number (short)
            return new JsonPrimitive(((NBTTagShort) nbt).func_150289_e());
        } else if (nbt instanceof NBTTagInt) {
            // Number (int)
            return new JsonPrimitive(((NBTTagInt) nbt).func_150287_d());
        } else if (nbt instanceof NBTTagLong) {
            // Number (long)
            return new JsonPrimitive(((NBTTagLong) nbt).func_150291_c());
        } else if (nbt instanceof NBTTagFloat) {
            // Number (float)
            return new JsonPrimitive(((NBTTagFloat) nbt).func_150288_h());
        } else if (nbt instanceof NBTTagDouble) {
            // Number (double)
            return new JsonPrimitive(((NBTTagDouble) nbt).func_150286_g());
        } else if (nbt instanceof NBTBase.NBTPrimitive) {
            // Number
            return new JsonPrimitive(((NBTBase.NBTPrimitive) nbt).func_150286_g());
        } else if (nbt instanceof NBTTagString) {
            // String
            return new JsonPrimitive(((NBTTagString) nbt).func_150285_a_());
        } else if (nbt instanceof NBTTagList) {
            // Tag List
            final NBTTagList list = (NBTTagList) nbt;

            JsonArray arr = new JsonArray();
            list.tagList.forEach(c -> arr.add(toJsonObject((NBTBase) c)));
            return arr;
        } else if (nbt instanceof NBTTagIntArray) {
            // Int Array
            final NBTTagIntArray list = (NBTTagIntArray) nbt;

            JsonArray arr = new JsonArray();

            for (int i : list.func_150302_c()) {
                arr.add(new JsonPrimitive(i));
            }

            return arr;
        } else if (nbt instanceof NBTTagByteArray) {
            // Byte Array
            final NBTTagByteArray list = (NBTTagByteArray) nbt;

            JsonArray arr = new JsonArray();

            for (byte i : list.func_150292_c()) {
                arr.add(new JsonPrimitive(i));
            }

            return arr;
        } else {
            throw new IllegalArgumentException("Unsupported NBT Tag: " + NBTBase.NBTTypes[nbt.getId()] + " - " + nbt);
        }
    }

    public static NBTBase toNbt(JsonElement jsonElement) {
        if (jsonElement == null) {
            return null;
        }

        if (jsonElement instanceof JsonPrimitive) {
            final JsonPrimitive jsonPrimitive = (JsonPrimitive) jsonElement;

            if (jsonPrimitive.isNumber()) {
                if (jsonPrimitive.getAsBigDecimal()
                    .remainder(BigDecimal.ONE)
                    .equals(BigDecimal.ZERO)) {
                    long lval = jsonPrimitive.getAsLong();

                    if (lval >= Byte.MIN_VALUE && lval <= Byte.MAX_VALUE) {
                        return new NBTTagByte((byte) lval);
                    }

                    if (lval >= Short.MIN_VALUE && lval <= Short.MAX_VALUE) {
                        return new NBTTagShort((short) lval);
                    }

                    if (lval >= Integer.MIN_VALUE && lval <= Integer.MAX_VALUE) {
                        return new NBTTagInt((int) lval);
                    }

                    return new NBTTagLong(lval);
                } else {
                    double dval = jsonPrimitive.getAsDouble();
                    float fval = (float) dval;

                    if (Math.abs(dval - fval) < 0.0001) {
                        return new NBTTagFloat(fval);
                    }

                    return new NBTTagDouble(dval);
                }
            } else {
                return new NBTTagString(jsonPrimitive.getAsString());
            }
        } else if (jsonElement instanceof JsonArray) {
            // NBTTagIntArray or NBTTagList
            final JsonArray jsonArray = (JsonArray) jsonElement;
            final List<NBTBase> nbtList = new ArrayList<>();

            for (JsonElement element : jsonArray) {
                nbtList.add(toNbt(element));
            }

            // spotless:off
            if (nbtList.stream().allMatch(n -> n instanceof NBTTagInt)) {
                return new NBTTagIntArray(nbtList.stream().mapToInt(i -> ((NBTTagInt) i).func_150287_d()).toArray());
            } else if (nbtList.stream().allMatch(n -> n instanceof NBTTagByte)) {
                final byte[] abyte = new byte[nbtList.size()];
    
                for (int i = 0; i < nbtList.size(); i++) {
                    abyte[i] = ((NBTTagByte) nbtList.get(i)).func_150290_f();
                }
    
                return new NBTTagByteArray(abyte);
            } else {
                NBTTagList nbtTagList = new NBTTagList();
                nbtList.forEach(nbtTagList::appendTag);
    
                return nbtTagList;
            }
            // spotless:on
        } else if (jsonElement instanceof JsonObject) {
            // NBTTagCompound
            final JsonObject jsonObject = (JsonObject) jsonElement;

            NBTTagCompound nbtTagCompound = new NBTTagCompound();

            for (Map.Entry<String, JsonElement> jsonEntry : jsonObject.entrySet()) {
                nbtTagCompound.setTag(jsonEntry.getKey(), toNbt(jsonEntry.getValue()));
            }

            return nbtTagCompound;
        }

        throw new IllegalArgumentException("Unhandled element " + jsonElement);
    }

    public static boolean areBlocksBasicallyEqual(PendingBlock a, PendingBlock b) {
        return a.getBlock() == b.getBlock() && a.metadata == b.metadata;
    }

    public static boolean areStacksBasicallyEqual(ItemStack a, ItemStack b) {
        if (a == null || b == null) {
            return a == null && b == null;
        }

        return a.getItem() == b.getItem() && a.getItemDamage() == b.getItemDamage()
            && ItemStack.areItemStackTagsEqual(a, b);
    }

    public static void createPlanImpl(EntityPlayer player, NBTState state, ItemMatterManipulator manipulator,
        int flags) {
        List<PendingBlock> blocks = state.getPendingBlocks(player.getEntityWorld());
        RequiredItemAnalysis itemAnalysis = BlockAnalyzer
            .getRequiredItemsForBuild(player, blocks, (flags & Messages.PLAN_ALL) != 0);

        List<IAEItemStack> requiredItems = GTUtility.mapToList(
            itemAnalysis.requiredItems.entrySet(),
            e -> Objects.requireNonNull(
                AEItemStack.create(
                    e.getKey()
                        .getItemStack()))
                .setStackSize(e.getValue()));

        MMInventory inv = new MMInventory(player, state, manipulator.tier);

        Pair<Boolean, List<IAEItemStack>> extractResult = inv.tryConsumeItems(
            requiredItems,
            IPseudoInventory.CONSUME_SIMULATED | IPseudoInventory.CONSUME_PARTIAL
                | IPseudoInventory.CONSUME_IGNORE_CREATIVE);

        List<IAEItemStack> availableItems = extractResult.right() == null ? new ArrayList<>() : extractResult.right();

        GTUtility.sendInfoToPlayer(player, "Required items:");

        if (!requiredItems.isEmpty()) {
            requiredItems.stream()
                .map((IAEItemStack stack) -> {
                    long available = availableItems.stream()
                        .filter(s -> s.isSameType(stack))
                        .mapToLong(s -> s.getStackSize())
                        .sum();

                    if (stack.getStackSize() - available > 0) {
                        return String.format(
                            "%s%s%s: %s%d%s (%s%d%s missing)",
                            EnumChatFormatting.AQUA.toString(),
                            stack.getItemStack()
                                .getDisplayName(),
                            EnumChatFormatting.GRAY.toString(),
                            EnumChatFormatting.GOLD.toString(),
                            stack.getStackSize(),
                            EnumChatFormatting.GRAY.toString(),
                            EnumChatFormatting.RED.toString(),
                            stack.getStackSize() - available,
                            EnumChatFormatting.GRAY.toString());
                    } else {
                        return String.format(
                            "%s%s%s: %s%d%s",
                            EnumChatFormatting.AQUA.toString(),
                            stack.getItemStack()
                                .getDisplayName(),
                            EnumChatFormatting.GRAY.toString(),
                            EnumChatFormatting.GOLD.toString(),
                            stack.getStackSize(),
                            EnumChatFormatting.GRAY.toString());
                    }
                })
                .sorted()
                .forEach(message -> { GTUtility.sendInfoToPlayer(player, message); });
        } else {
            GTUtility.sendInfoToPlayer(player, "None");
        }

        if (!requiredItems.isEmpty()) {
            if (state.connectToUplink()) {
                if ((flags & Messages.PLAN_ALL) == 0) {
                    requiredItems.forEach(stack -> {
                        long available = availableItems.stream()
                            .filter(s -> s.isSameType(stack))
                            .mapToLong(s -> s.getStackSize())
                            .sum();

                        stack.decStackSize(available);
                    });

                    Iterator<IAEItemStack> iter = requiredItems.iterator();

                    while (iter.hasNext()) {
                        IAEItemStack stack = iter.next();

                        if (stack.getStackSize() == 0) iter.remove();
                    }
                }

                if (!requiredItems.isEmpty()) {
                    state.uplink.submitPlan(
                        player,
                        state.config.coordA.toString(),
                        requiredItems,
                        (flags & Messages.PLAN_AUTO_SUBMIT) != 0);
                } else {
                    GTUtility.sendInfoToPlayer(player, "Not creating pattern because all required items are present.");
                }
            } else {
                GTUtility
                    .sendErrorToPlayer(player, "Manipulator not connected to an uplink: cannot create a fake pattern.");
            }
        }
    }
}
