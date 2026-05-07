package tectech.thing.item;

import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.List;
import java.util.stream.IntStream;

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

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.GTUtility;
import tectech.Reference;
import tectech.TecTech;
import tectech.thing.CustomItemList;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.parameter.IParametrized;
import tectech.thing.metaTileEntity.multi.base.parameter.Parameter;
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
        if (metaTE instanceof TTMultiblockBase controller && controller instanceof IParametrized parametrized) {
            NBTTagCompound tNBT = ItemStackNBT.get(aStack);
            List<Parameter<?>> parameterList = parametrized.getParameters();

            if (aStack.getItemDamage() == 1) {
                // Prevent pasting configuration from a different multiblock
                if (!hasIdenticalParameterList(parameterList, tNBT)) {
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
                for (int i = 0; i < tagList.tagList.size(); i++) parameterList.get(i)
                    .loadFromParameterCard(tagList.getCompoundTagAt(i));

                GTUtility.sendChatTrans(aPlayer, "item.em.parametrizerMemoryCard.pasteMessage");
            } else {
                // read from controller
                NBTTagCompound newTag = new NBTTagCompound();
                NBTTagList tagList = new NBTTagList();
                for (Parameter<?> parameter : parameterList) {
                    NBTTagCompound parameterTag = new NBTTagCompound();
                    parameter.saveToParameterCard(parameterTag);
                    tagList.appendTag(parameterTag);
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

    private boolean hasIdenticalParameterList(List<Parameter<?>> controllerParameters, NBTTagCompound tNBT) {
        if (tNBT.hasKey("paramList", Constants.NBT.TAG_LIST)) {
            NBTTagList tagList = tNBT.getTagList("paramList", Constants.NBT.TAG_COMPOUND);

            if (tagList.tagList.size() != controllerParameters.size()) return false;

            for (int i = 0; i < tagList.tagList.size(); i++) {
                NBTTagCompound tag = tagList.getCompoundTagAt(i);
                Parameter<?> parameter = controllerParameters.get(i);
                if (!tag.getString("key")
                    .equals(parameter.getLangKey())) return false;
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
            for (int i = 0; i < tagList.tagList.size(); i++) {
                NBTTagCompound tag = tagList.getCompoundTagAt(i);
                Object[] args = null;

                NBTTagList argsList = tag.getTagList("langArgs", Constants.NBT.TAG_STRING);
                if (argsList != null && argsList.tagCount() > 0) args = IntStream.range(0, argsList.tagCount())
                    .mapToObj(argsList::getStringTagAt)
                    .toArray(Object[]::new);

                aList.add(
                    EnumChatFormatting.AQUA + GTUtility.translate(tag.getString("langKey"), args)
                        + ": "
                        + EnumChatFormatting.GRAY
                        + getValueFromTag(tag));
            }
        }
    }

    private String getValueFromTag(NBTTagCompound tag) {
        return switch (tag.getString("type")) {
            case "integer" -> String.valueOf(tag.getInteger("value"));
            case "double" -> String.valueOf(tag.getDouble("value"));
            case "string" -> tag.getString("value");
            case "boolean" -> String.valueOf(tag.getBoolean("value"));
            default -> "";
        };
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
        ItemStack stack = new ItemStack(this, 1);
        stack.setTagCompound(new NBTTagCompound());
        list.add(stack);
    }
}
