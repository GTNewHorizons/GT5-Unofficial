package gtPlusPlus.core.item.general;

import static gregtech.api.enums.Mods.GTPlusPlus;
import static gtPlusPlus.core.lib.CORE.RANDOM;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

import gregtech.api.enums.ItemList;
import gtPlusPlus.api.objects.Logger;
import gtPlusPlus.api.objects.data.Pair;
import gtPlusPlus.core.entity.item.ItemEntityGiantEgg;
import gtPlusPlus.core.item.base.BaseItemTickable;
import gtPlusPlus.core.item.general.spawn.ItemCustomSpawnEgg;
import gtPlusPlus.core.lib.CORE;
import gtPlusPlus.core.util.Utils;
import gtPlusPlus.core.util.math.MathUtils;
import gtPlusPlus.core.util.minecraft.ItemUtils;
import gtPlusPlus.core.util.minecraft.NBTUtils;
import gtPlusPlus.core.util.reflect.ReflectionUtils;

public class ItemGiantEgg extends BaseItemTickable {

    private static ItemStack turnsIntoItem;
    private static ItemStack mCorrectEgg;
    private static ItemStack mCorrectStemCells;

    public void registerFuel(int burn) {
        CORE.burnables.add(new Pair<Integer, ItemStack>(burn, ItemUtils.getSimpleStack(this, 1)));
    }

    public final void registerOrdictionary(String name) {
        ItemUtils.addItemToOreDictionary(ItemUtils.getSimpleStack(this), name);
    }

    public ItemGiantEgg() {
        this(
                Utils.rgbtoHexValue(255, 255, 255),
                Short.MAX_VALUE * Byte.MAX_VALUE,
                new String[] { "I had best try disassemble this.. for science!" });
    }

    private ItemGiantEgg(int colour, int maxTicks, String[] desc1) {
        super(true, false, "itemBigEgg", colour, maxTicks, desc1);
        setTextureName(GTPlusPlus.ID + ":itemBigEgg");
        this.setMaxStackSize(1);
        registerFuel(5000);
        registerOrdictionary("fuelLargeChickenEgg");
    }

    public static void postInit(ItemGiantEgg aGiantEggItem) {
        ItemGiantEgg.turnsIntoItem = getSpawnEggStack();
        // new DecayableRecipe(aGiantEggItem.maxTicks, getSimpleStack(aGiantEggItem),
        // ItemUtils.getSimpleStack(ItemGiantEgg.turnsIntoItem, 1));
    }

    private static ItemStack getSpawnEggStack() {
        // Set the correct egg for future hatches
        if (mCorrectEgg == null) {
            /*
             * for (int g=0;g<Byte.MAX_VALUE;g++) { ItemStack mSpawn = ItemUtils.simpleMetaStack(Items.spawn_egg, g, 1);
             * if (mSpawn != null) { //String s = ("" + StatCollector.translateToLocal(mSpawn.getUnlocalizedName() +
             * ".name")).trim(); String s1 = EntityList.getStringFromID(mSpawn.getItemDamage()); if (s1 != null){ //s =
             * s + " " + StatCollector.translateToLocal("entity." + s1 + ".name"); if
             * (s1.equalsIgnoreCase("bigChickenFriendly")) { mCorrectEgg = mSpawn; return mCorrectEgg; } } } }
             */
            ItemStack aTempEgg = ItemCustomSpawnEgg.getSpawnEggForEntityname("bigChickenFriendly", 1);
            if (aTempEgg != null) {
                mCorrectEgg = aTempEgg;
            }
        }
        return mCorrectEgg;
    }

    private static ItemStack getStemCellStack() {
        if (mCorrectStemCells == null) {
            mCorrectStemCells = ItemList.Circuit_Chip_Stemcell.get(1);
        }
        return mCorrectStemCells;
    }

    protected int getMaxTicks(ItemStack aStack) {
        if (aStack != null && aStack.hasTagCompound() && aStack.getTagCompound().hasKey("mEggAge")) {
            return NBTUtils.getInteger(aStack, "mEggAge");
        }
        return maxTicks;
    }

    @Override
    public void registerIcons(final IIconRegister i) {
        this.mIcon[0] = i.registerIcon(GTPlusPlus.ID + ":" + "itemBigEgg");
    }

    @Override
    protected boolean createNBT(World world, ItemStack aStack) {

        if (aStack.getTagCompound() != null && aStack.getTagCompound().hasKey("size")) {
            return false;
        }
        Logger.INFO("Egg: " + ReflectionUtils.getMethodName(1));
        Logger.INFO("Egg: " + ReflectionUtils.getMethodName(2));
        Logger.INFO("Egg: " + ReflectionUtils.getMethodName(3));
        Logger.INFO("Egg: " + ReflectionUtils.getMethodName(4));
        Logger.INFO("Egg: " + ReflectionUtils.getMethodName(5));
        Logger.INFO("Egg: " + ReflectionUtils.getMethodName(6));
        // Logger.INFO("Creating Egg NBT.");
        boolean aSuper = super.createNBT(world, aStack);
        int size = MathUtils.randInt(1, 8);
        NBTUtils.setInteger(aStack, "size", size);
        NBTUtils.setInteger(aStack, "mEggAge", ((MathUtils.randInt(8000, 16000) * size)));
        ItemStack aStemCells = getStemCellStack();
        if (aStemCells != null) {
            int mSize = NBTUtils.getInteger(aStack, "size");
            float mSizeMod = (MathUtils.randInt(-5, 5) / 5);
            mSize += mSizeMod;
            mSize = Math.max(mSize, 1);
            ItemStack eggYolks[] = new ItemStack[mSize];
            for (int u = 0; u < mSize; u++) {
                eggYolks[u] = ItemUtils.getSimpleStack(aStemCells, MathUtils.randInt(1, 4));
            }
            int mexpected = 0;
            for (ItemStack e : eggYolks) {
                if (e != null) {
                    mexpected += e.stackSize;
                }
            }
            if (mexpected > 0) {
                NBTUtils.setInteger(aStack, "mExpected", mexpected);
                NBTUtils.writeItemsToGtCraftingComponents(
                        aStack,
                        new ItemStack[] { ItemUtils.getSimpleStack(aStemCells, mexpected) },
                        true);
            }
        }
        return aSuper;
    }

    @Override
    public void onUpdate(final ItemStack iStack, final World world, final Entity entityHolding, final int p_77663_4_,
            final boolean p_77663_5_) {
        if (world == null || iStack == null) {
            return;
        }
        if (world.isRemote) {
            return;
        }
        if (iStack.getTagCompound() == null || !iStack.getTagCompound().hasKey("size")) {
            this.createNBT(world, iStack);
            Logger.INFO("Egg has no NBT, creating (onUpdate)");
        }
        boolean a1, a2;
        a1 = this.isTicking(world, iStack);
        a2 = a1 ? tickItemTag(world, iStack) : false;

        // Logger.INFO("Is Ticking? "+a1);
        // Logger.INFO("Did Tick? "+a2);
        if (!a1 && !a2) {
            if (entityHolding instanceof EntityPlayer) {
                if (MathUtils.randInt(0, 1000) >= 990) {
                    if (NBTUtils.hasKey(iStack, "size")) {
                        if ((NBTUtils.getInteger(iStack, "size") + 1) >= MathUtils.randInt(0, 9)) {
                            ItemStack replacement = ItemUtils.getSimpleStack(getHatchResult(), 1);
                            if (replacement == null) {}

                            // Logger.INFO("Replacing "+iStack.getDisplayName()+" with
                            // "+replacement.getDisplayName()+".");
                            final ItemStack tempTransform = replacement.copy();
                            if (iStack.stackSize > 1) {
                                int u = iStack.stackSize;
                                tempTransform.stackSize = u;
                                ((EntityPlayer) entityHolding).inventory.addItemStackToInventory((tempTransform));
                                for (int l = 0; l < u; l++) {
                                    ((EntityPlayer) entityHolding).inventory.consumeInventoryItem(this);
                                }
                            } else {
                                tempTransform.stackSize = 1;
                                ((EntityPlayer) entityHolding).inventory.addItemStackToInventory((tempTransform));
                                ((EntityPlayer) entityHolding).inventory.consumeInventoryItem(this);
                            }
                        }
                    }
                }
            }
        }
    }

    public ItemStack getHatchResult() {
        return turnsIntoItem;
    }

    @Override
    public String getItemStackDisplayName(ItemStack aStack) {
        String localName = super.getItemStackDisplayName(aStack);
        /*
         * if (aStack.getTagCompound() == null){ createNBT(null, aStack);
         * Logger.INFO("Egg has no NBT, creating (getDisplayName)"); }
         */
        int size = 1;
        if (NBTUtils.hasKey(aStack, "size")) {
            size = NBTUtils.getInteger(aStack, "size");
            return "" + size + " " + localName;
        }
        return "?? " + localName;
    }

    @Override
    public boolean hasCustomEntity(ItemStack stack) {
        return true;
    }

    @Override
    public Entity createEntity(World world, Entity location, ItemStack itemstack) {
        if (location instanceof EntityPlayer) {
            EntityPlayer player = (EntityPlayer) location;
            if (itemstack == null) {
                return null;
            } else if (itemstack.stackSize == 0) {
                return null;
            } else {
                ItemEntityGiantEgg entityitem = new ItemEntityGiantEgg(
                        world,
                        player.posX,
                        player.posY - 0.30000001192092896D + (double) player.getEyeHeight(),
                        player.posZ,
                        itemstack);
                entityitem.delayBeforeCanPickup = 40;
                entityitem.func_145799_b(player.getCommandSenderName());
                float f = 0.1F;
                float f1;
                f = 0.3F;
                entityitem.motionX = (double) (-MathHelper.sin(player.rotationYaw / 180.0F * (float) Math.PI)
                        * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI)
                        * f);
                entityitem.motionZ = (double) (MathHelper.cos(player.rotationYaw / 180.0F * (float) Math.PI)
                        * MathHelper.cos(player.rotationPitch / 180.0F * (float) Math.PI)
                        * f);
                entityitem.motionY = (double) (-MathHelper.sin(player.rotationPitch / 180.0F * (float) Math.PI) * f
                        + 0.1F);
                f = 0.02F;
                f1 = RANDOM.nextFloat() * (float) Math.PI * 2.0F;
                f *= RANDOM.nextFloat();
                entityitem.motionX += Math.cos((double) f1) * (double) f;
                entityitem.motionY += (double) ((RANDOM.nextFloat() - RANDOM.nextFloat()) * 0.1F);
                entityitem.motionZ += Math.sin((double) f1) * (double) f;
                return entityitem;
            }
        }
        return null;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer aPlayer, List list, boolean bool) {
        int size = 0;
        long age = 0;
        long life = 0;
        int expected = 0;
        if (this.descriptionString.length > 0) {
            list.add(EnumChatFormatting.GRAY + this.descriptionString[0]);
        }
        if (NBTUtils.hasKey(stack, "size")) {
            size = NBTUtils.getInteger(stack, "size");
            if (size > 0 && NBTUtils.hasKey(stack, "TickableItem")) {
                NBTTagCompound aNBT = stack.getTagCompound();
                if (aNBT != null) {
                    aNBT = aNBT.getCompoundTag("TickableItem");
                    if (aNBT != null) {
                        age = aNBT.getLong("Tick");
                    }
                }
            }
            if (NBTUtils.hasKey(stack, "mEggAge")) {
                life = NBTUtils.getInteger(stack, "mEggAge");
            }
            if (NBTUtils.hasKey(stack, "mExpected")) {
                expected = NBTUtils.getInteger(stack, "mExpected");
            }
        }
        String aSize = size > 0 ? "" + size : "??";
        String aExpected = expected > 0 ? "" + expected : "??";
        String aAge = age > 0 ? "" + (age / 20) : "??";
        String aLife = life > 0 ? "" + (life / 20) : "??";
        list.add("Egg Size: " + aSize + " ounces");
        list.add("Expected Stem Cells: " + aExpected);
        list.add("Age: " + aAge + "s" + " / " + aLife + "s");
        list.add("Larger eggs take longer to hatch,");
        list.add("but have a better chance of hatching.");
    }
}
