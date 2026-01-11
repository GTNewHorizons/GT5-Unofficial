package tectech.thing.item;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.ArrayList;
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
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import tectech.Reference;
import tectech.TecTech;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.multi.base.Parameters;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.util.CommonValues;

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
        if (metaTE instanceof TTMultiblockBase controller) {
            if (aStack.getTagCompound() == null) {
                aStack.setTagCompound(new NBTTagCompound());
            }
            NBTTagCompound tNBT = aStack.getTagCompound();
            if (aStack.getItemDamage() == 1) {
                // Prevent pasting configuration from a different multiblock
                if (!hasIdenticalParameterList(getControllerParameters(controller), tNBT)) {
                    String reason;
                    if (!tNBT.hasKey("controller")) {
                        reason = translateToLocal("item.em.parametrizerMemoryCard.noConfig");
                    } else {
                        reason = translateToLocalFormatted(
                            "item.em.parametrizerMemoryCard.controllerMismatch",
                            tNBT.getString("controller"),
                            controller.getLocalName());
                    }
                    GTUtility.sendChatToPlayer(aPlayer, reason);

                    return true;
                }
                // write to controller
                NBTTagList tagList = tNBT.getTagList("paramList", Constants.NBT.TAG_COMPOUND);
                for (int hatch = 0; hatch < 10; hatch++) {
                    NBTTagCompound tag = tagList.getCompoundTagAt(hatch);

                    controller.parametrization
                        .trySetParameters(hatch, tag.getDouble("value0D"), tag.getDouble("value1D"));
                }
                GTUtility.sendChatTrans(aPlayer, "item.em.parametrizerMemoryCard.pasteMessage");
            } else {
                // read from controller
                NBTTagCompound newTag = new NBTTagCompound();
                NBTTagList tagList = new NBTTagList();
                for (int hatch = 0; hatch < 10; hatch++) {
                    NBTTagCompound tagChild = new NBTTagCompound();
                    Parameters.Group.ParameterIn[] parameters = controller.parametrization.getGroup(hatch).parameterIn;
                    // Tesla tower for some reason has a bunch of parameters called "unused"
                    if (parameters[0] != null && !parameters[0].getBrief()
                        .equals(translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.9"))) {
                        tagChild.setDouble("value0D", parameters[0].get());
                        tagChild.setString("name0", parameters[0].getBrief());
                    }
                    if (parameters[1] != null && !parameters[1].getBrief()
                        .equals(translateToLocal("gt.blockmachines.multimachine.tm.teslaCoil.cfgi.9"))) {
                        tagChild.setDouble("value1D", parameters[1].get());
                        tagChild.setString("name1", parameters[1].getBrief());
                    }
                    tagList.appendTag(tagChild);
                }
                newTag.setString("controller", controller.getLocalName());
                newTag.setString("coords", aX + ", " + aY + ", " + aZ);
                newTag.setTag("paramList", tagList);
                aStack.setTagCompound(newTag);
                GTUtility.sendChatTrans(aPlayer, "item.em.parametrizerMemoryCard.copyMessage");
            }
            return true;
        }
        return false;
    }

    private ArrayList<String> getControllerParameters(TTMultiblockBase controller) {
        ArrayList<String> parameterList = new ArrayList<>();
        for (int hatch = 0; hatch < 10; hatch++) {
            Parameters.Group.ParameterIn[] parameters = controller.parametrization.getGroup(hatch).parameterIn;
            if (parameters[0] != null) {
                parameterList.add(parameters[0].getBrief());
            }
            if (parameters[1] != null) {
                parameterList.add(parameters[1].getBrief());
            }
        }
        return parameterList;
    }

    private boolean hasIdenticalParameterList(ArrayList<String> controllerParameters, NBTTagCompound tNBT) {
        if (tNBT.hasKey("paramList", Constants.NBT.TAG_LIST)) {
            NBTTagList tagList = tNBT.getTagList("paramList", Constants.NBT.TAG_COMPOUND);
            for (int hatch = 0; hatch < 10; hatch++) {
                NBTTagCompound tag = tagList.getCompoundTagAt(hatch);
                if (tag.hasNoTags()) {
                    continue;
                }
                if (tag.hasKey("name0") && !controllerParameters.contains(tag.getString("name0"))) {
                    return false;
                }
                if (tag.hasKey("name1") && !controllerParameters.contains(tag.getString("name1"))) {
                    return false;
                }
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
            return translateToLocal("item.em.parametrizerMemoryCard.name.paste");
        } else {
            return translateToLocal("item.em.parametrizerMemoryCard.name.copy");
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
        if (tNBT == null) return;
        if (tNBT.hasKey("controller")) {
            aList.add(
                translateToLocalFormatted(
                    "item.em.parametrizerMemoryCard.desc.copied_controller",
                    EnumChatFormatting.RED + tNBT.getString("controller") + EnumChatFormatting.RESET,
                    EnumChatFormatting.GREEN + tNBT.getString("coords")));
        }
        if (tNBT.hasKey("paramList", Constants.NBT.TAG_LIST)) {
            NBTTagList tagList = tNBT.getTagList("paramList", Constants.NBT.TAG_COMPOUND);
            for (int hatch = 0; hatch < 10; hatch++) {
                NBTTagCompound tag = tagList.getCompoundTagAt(hatch);
                if (tag.hasKey("name0")) {
                    aList.add(
                        EnumChatFormatting.AQUA + tag.getString("name0")
                            + ": "
                            + EnumChatFormatting.GRAY
                            + tag.getDouble("value0D"));
                }
                if (tag.hasKey("name1")) {
                    aList.add(
                        EnumChatFormatting.AQUA + tag.getString("name1")
                            + ": "
                            + EnumChatFormatting.GRAY
                            + tag.getDouble("value1D"));
                }
            }
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
