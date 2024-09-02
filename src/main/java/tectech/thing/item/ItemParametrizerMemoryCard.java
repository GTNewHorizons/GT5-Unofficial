package tectech.thing.item;

import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import tectech.Reference;
import tectech.TecTech;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.hatch.MTEHatchParam;
import tectech.thing.metaTileEntity.hatch.MTEHatchParamText;
import tectech.thing.metaTileEntity.multi.base.Parameters;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.util.CommonValues;
import tectech.util.TTUtility;

/**
 * Created by Tec on 15.03.2017.
 */
public final class ItemParametrizerMemoryCard extends Item {

    public static ItemParametrizerMemoryCard INSTANCE;
    private static IIcon locked, unlocked;

    private ItemParametrizerMemoryCard() {
        setMaxStackSize(1);
        setHasSubtypes(true);
        setUnlocalizedName("em.parametrizerMemoryCard");
        setTextureName(Reference.MODID + ":itemParametrizerMemoryCardUnlocked");
        setCreativeTab(TecTech.creativeTabTecTech);
    }

    @Override
    public boolean onItemUseFirst(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ,
        int ordinalSide, float hitX, float hitY, float hitZ) {
        TileEntity tTileEntity = aWorld.getTileEntity(aX, aY, aZ);
        if (!(aPlayer instanceof EntityPlayerMP)) return false;
        if (!(tTileEntity instanceof IGregTechTileEntity)) return false;
        aStack.stackSize = 1;
        IMetaTileEntity metaTE = ((IGregTechTileEntity) tTileEntity).getMetaTileEntity();

        if (metaTE instanceof MTEHatchParamText parametrizer) {
            if (aStack.getTagCompound() == null) {
                aStack.setTagCompound(new NBTTagCompound());
            }
            NBTTagCompound tNBT = aStack.getTagCompound();
            if (aStack.getItemDamage() == 1) {
                // write to parametrizer
                parametrizer.param = tNBT.getInteger("param");
                parametrizer.value0D = tNBT.getDouble("value0D");
                parametrizer.value1D = tNBT.getDouble("value1D");
                parametrizer.value0s = tNBT.getString("value0s");
                parametrizer.value1s = tNBT.getString("value1s");
            } else {
                // read from parametrizer
                NBTTagCompound newTag = new NBTTagCompound();
                newTag.setInteger("param", parametrizer.param);
                newTag.setDouble("value0D", parametrizer.value0D);
                newTag.setDouble("value1D", parametrizer.value1D);
                newTag.setString("value0s", parametrizer.value0s);
                newTag.setString("value1s", parametrizer.value1s);
                aStack.setTagCompound(newTag);
            }
            return true;
        } else if (metaTE instanceof MTEHatchParam parametrizer) {
            if (aStack.getTagCompound() == null) {
                aStack.setTagCompound(new NBTTagCompound());
            }
            NBTTagCompound tNBT = aStack.getTagCompound();
            if (aStack.getItemDamage() == 1) {
                // write to parametrizer
                parametrizer.param = tNBT.getInteger("param");
                parametrizer.value0D = tNBT.getDouble("value0D");
                parametrizer.value1D = tNBT.getDouble("value1D");
            } else {
                // read from parametrizer
                NBTTagCompound newTag = new NBTTagCompound();
                tNBT.setInteger("param", parametrizer.param);
                tNBT.setDouble("value0D", parametrizer.value0D);
                tNBT.setDouble("value1D", parametrizer.value1D);
                aStack.setTagCompound(newTag);
            }
            return true;
        } else if (metaTE instanceof TTMultiblockBase controller) {
            if (aStack.getTagCompound() == null) {
                aStack.setTagCompound(new NBTTagCompound());
            }
            NBTTagCompound tNBT = aStack.getTagCompound();
            if (aStack.getItemDamage() == 1) {
                // write to controller
                if (tNBT.hasKey("paramList", Constants.NBT.TAG_LIST)) {
                    // from controller
                    NBTTagList tagList = tNBT.getTagList("paramList", Constants.NBT.TAG_COMPOUND);
                    for (int hatch = 0; hatch < 10; hatch++) {
                        NBTTagCompound tag = tagList.getCompoundTagAt(hatch);
                        controller.parametrization
                            .trySetParameters(hatch, tag.getDouble("value0D"), tag.getDouble("value1D"));
                    }
                } else {
                    // from parametrizer
                    controller.parametrization.trySetParameters(
                        tNBT.getInteger("param"),
                        tNBT.getDouble("value0D"),
                        tNBT.getDouble("value1D"));
                }
            } else {
                // read from controller
                NBTTagCompound newTag = new NBTTagCompound();
                NBTTagList tagList = new NBTTagList();
                for (int hatch = 0; hatch < 10; hatch++) {
                    NBTTagCompound tagChild = new NBTTagCompound();
                    Parameters.Group.ParameterIn[] parameters = controller.parametrization.getGroup(hatch).parameterIn;
                    if (parameters[0] != null) {
                        tagChild.setDouble("value0D", parameters[0].get());
                    }
                    if (parameters[1] != null) {
                        tagChild.setDouble("value1D", parameters[1].get());
                    }
                    tagList.appendTag(tagChild);
                }
                newTag.setTag("paramList", tagList);
                aStack.setTagCompound(newTag);
            }
            return true;
        }
        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack aStack, World aWorld, EntityPlayer aPlayer) {
        if (aPlayer instanceof EntityPlayerMP && aPlayer.isSneaking()) {
            aStack.stackSize = 1;
            if (aStack.getItemDamage() == 1) {
                aStack.setItemDamage(0);
            } else {
                aStack.setItemDamage(1);
            }
            return aStack;
        }
        return aStack;
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        if (stack.getItemDamage() == 1) {
            return StatCollector.translateToLocal("item.em.parametrizerMemoryCard.name.paste");
        } else {
            return StatCollector.translateToLocal("item.em.parametrizerMemoryCard.name.copy");
        }
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer ep, List<String> aList, boolean boo) {
        NBTTagCompound tNBT = aStack.getTagCompound();
        aList.add(CommonValues.THETA_MOVEMENT);
        aList.add(translateToLocal("item.em.parametrizerMemoryCard.desc.0")); // Stores Parameters

        if (aStack.getItemDamage() == 1) {
            // Use on Multiblock Controller to configure it
            aList.add(EnumChatFormatting.BLUE + translateToLocal("item.em.parametrizerMemoryCard.desc.1"));
        } else {
            // Use on Multiblock Controller to store parameters
            aList.add(EnumChatFormatting.BLUE + translateToLocal("item.em.parametrizerMemoryCard.desc.2"));
        }
        // Sneak right click to lock/unlock
        aList.add(EnumChatFormatting.BLUE + translateToLocal("item.em.parametrizerMemoryCard.desc.3"));

        double temp;
        if (tNBT != null && tNBT.hasKey("param")) {
            aList.add("Hatch ID: " + EnumChatFormatting.AQUA + tNBT.getInteger("param"));
            temp = tNBT.getInteger("value0D");
            aList.add("Value 0D: " + EnumChatFormatting.AQUA + temp);
            aList.add(
                "Value 0B: " + EnumChatFormatting.AQUA
                    + TTUtility.longBitsToShortString(Double.doubleToLongBits(temp)));
            aList.add("Value 0s: " + EnumChatFormatting.AQUA + tNBT.getString("value0s"));
            temp = tNBT.getInteger("value1D");
            aList.add("Value 1D: " + EnumChatFormatting.AQUA + temp);
            aList.add(
                "Value 1B: " + EnumChatFormatting.AQUA
                    + TTUtility.longBitsToShortString(Double.doubleToLongBits(temp)));
            aList.add("Value 1s: " + EnumChatFormatting.AQUA + tNBT.getString("value1s"));
        }
    }

    public static void run() {
        INSTANCE = new ItemParametrizerMemoryCard();
        GameRegistry.registerItem(INSTANCE, INSTANCE.getUnlocalizedName());
        CustomItemList.parametrizerMemory.set(INSTANCE);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void registerIcons(IIconRegister iconRegister) {
        locked = iconRegister.registerIcon(Reference.MODID + ":itemParametrizerMemoryCardLocked");
        unlocked = itemIcon = iconRegister.registerIcon(getIconString());
    }

    @Override
    public IIcon getIconFromDamage(int damage) {
        if (damage == 1) {
            return locked;
        }
        return unlocked;
    }

    @Override
    public void getSubItems(Item item, CreativeTabs tab, List<ItemStack> list) {
        ItemStack that = new ItemStack(this, 1);
        that.setTagCompound(new NBTTagCompound());
        list.add(that);
    }
}
