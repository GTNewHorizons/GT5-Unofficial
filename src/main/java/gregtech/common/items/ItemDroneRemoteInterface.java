package gregtech.common.items;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

import com.cleanroommc.modularui.api.IGuiHolder;
import com.cleanroommc.modularui.factory.GuiManager;
import com.cleanroommc.modularui.factory.ItemStackGuiData;
import com.cleanroommc.modularui.factory.ItemStackGuiFactory;
import com.cleanroommc.modularui.screen.ModularPanel;
import com.cleanroommc.modularui.screen.ModularScreen;
import com.cleanroommc.modularui.screen.UISettings;
import com.cleanroommc.modularui.value.sync.PanelSyncManager;
import com.gtnewhorizons.modularui.common.internal.network.NetworkUtils;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.GTGenericItem;
import gregtech.api.modularui2.GTGuiThemes;
import gregtech.api.modularui2.GTModularScreen;
import gregtech.common.gui.modularui.item.DroneRemoteInterfaceGUI;
import gregtech.common.tileentities.machines.multi.drone.MTEDroneCentre;

public class ItemDroneRemoteInterface extends GTGenericItem implements IGuiHolder<ItemStackGuiData> {

    ItemStackGuiFactory factory = new ItemStackGuiFactory("mui2:itemstack", this);

    public ItemDroneRemoteInterface(String aUnlocalized, String aEnglish, String aEnglishTooltip) {
        super(aUnlocalized, aEnglish, aEnglishTooltip);
        setMaxStackSize(1);
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
        if (!world.isRemote && stack.hasTagCompound()
            && stack.getTagCompound()
                .hasKey("droneCentre")) {
            GuiManager.open(factory, new ItemStackGuiData(player, stack), (EntityPlayerMP) player);
        }
        return super.onItemRightClick(stack, world, player);
    }

    @Override
    public void addInformation(ItemStack aStack, EntityPlayer aPlayer, List<String> aList, boolean aF3_H) {
        super.addInformation(aStack, aPlayer, aList, aF3_H);
        if (aStack.hasTagCompound() && aStack.getTagCompound()
            .hasKey("droneCentre")) {
            NBTTagCompound centreNbt = aStack.getTagCompound()
                .getCompoundTag("droneCentre");
            int x = centreNbt.getInteger("x");
            int y = centreNbt.getInteger("y");
            int z = centreNbt.getInteger("z");
            int dim = centreNbt.getInteger("dim");
            aList.add(StatCollector.translateToLocalFormatted("GT5U.tooltip.drone_remote_connected", x, y, z, dim));
        } else aList.add(StatCollector.translateToLocal("GT5U.tooltip.drone_remote_disconnected"));
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side,
        float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof IGregTechTileEntity
            && ((IGregTechTileEntity) te).getMetaTileEntity() instanceof MTEDroneCentre) {
            if (!world.isRemote) {
                NBTTagCompound nbt = itemStack.getTagCompound();
                if (nbt == null) {
                    nbt = new NBTTagCompound();
                    itemStack.setTagCompound(nbt);
                }
                NBTTagCompound centreNbt = new NBTTagCompound();
                centreNbt.setInteger("x", x);
                centreNbt.setInteger("y", y);
                centreNbt.setInteger("z", z);
                centreNbt.setInteger("dim", world.provider.dimensionId);
                nbt.setTag("droneCentre", centreNbt);
                player.addChatMessage(new ChatComponentTranslation("GT5U.gui.chat.bindcentre"));
            }
            return true;
        }
        return false;
    }

    @Override
    public ModularPanel buildUI(ItemStackGuiData guiData, PanelSyncManager guiSyncManager, UISettings uiSettings) {
        MTEDroneCentre centre = null;
        ItemStack stack = guiData.getItemStack();
        if (stack.hasTagCompound() && stack.getTagCompound()
            .hasKey("droneCentre")) {
            NBTTagCompound centreNbt = stack.getTagCompound()
                .getCompoundTag("droneCentre");
            int x = centreNbt.getInteger("x");
            int y = centreNbt.getInteger("y");
            int z = centreNbt.getInteger("z");
            int dim = centreNbt.getInteger("dim");
            if (NetworkUtils.isClient()) {
                centre = new MTEDroneCentre("fakeCentre");
            } else {
                World targetWorld = MinecraftServer.getServer()
                    .worldServerForDimension(dim);
                if (targetWorld != null) {
                    TileEntity te = targetWorld.getTileEntity(x, y, z);
                    if (te instanceof IGregTechTileEntity
                        && ((IGregTechTileEntity) te).getMetaTileEntity() instanceof MTEDroneCentre) {
                        centre = (MTEDroneCentre) ((IGregTechTileEntity) te).getMetaTileEntity();
                    }
                }
            }
        }
        return new DroneRemoteInterfaceGUI(guiSyncManager, centre).build();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public ModularScreen createScreen(ItemStackGuiData data, ModularPanel mainPanel) {
        return new GTModularScreen(mainPanel, GTGuiThemes.STANDARD);
    }
}
