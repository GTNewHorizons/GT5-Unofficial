package gregtech.common.tools;

import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.gtnhlib.item.ItemStackNBT;

import appeng.api.util.DimensionalCoord;
import gregtech.api.enums.GTValues;
import gregtech.api.graphs.Node;
import gregtech.api.graphs.PowerNode;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.items.ItemTool;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.MetaPipeEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEMultiBlockBase;
import gregtech.common.networkanalyzer.NetworkAnalyzerData;
import gregtech.common.networkanalyzer.NetworkAnalyzerData.AnalyzerModes;
import gregtech.common.networkanalyzer.events.NetworkAnalyzerPlayerTracker;
import gregtech.common.networkanalyzer.net.GTPacketNetworkAnalyzer;
import gregtech.common.networkanalyzer.net.GTPacketNetworkAnalyzerMode;
import gregtech.common.networkanalyzer.network.PowerNetworkBuilder;

public class ItemNetworkAnalyzer extends ItemTool {

    public ItemNetworkAnalyzer(String aEnglish, String aTooltip, int aMaxDamage, int aEntityDamage,
        boolean aSwingIfUsed) {
        super("NetworkAnalyzer", aEnglish, aTooltip, aMaxDamage, aEntityDamage, aSwingIfUsed);
    }

    public static AnalyzerModes getMode(ItemStack stack) {
        final int id = ItemStackNBT.getByte(stack, "mode") & 0xFF;
        final AnalyzerModes[] values = AnalyzerModes.values();
        return id < values.length ? values[id] : AnalyzerModes.TOPOLOGY;
    }

    public static void setMode(ItemStack stack, AnalyzerModes mode) {
        ItemStackNBT.setByte(stack, "mode", (byte) mode.ordinal());
    }

    public static DimensionalCoord getLocation(ItemStack stack) {
        final NBTTagCompound dim = ItemStackNBT.getCompoundTag(stack, "dim");

        if (dim != null) {
            return DimensionalCoord.readFromNBT(dim);
        }

        return null;
    }

    public void setLocation(ItemStack stack, DimensionalCoord coord) {
        final NBTTagCompound dim = new NBTTagCompound();
        coord.writeToNBT(dim);
        ItemStackNBT.setCompoundTag(stack, "dim", dim);
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer player, List<String> list, boolean extended) {
        String modeKey = "gt.NetworkAnalyzer.mode." + getMode(stack).name()
            .toLowerCase();
        String modeName = StatCollector.translateToLocal(modeKey);
        list.add(StatCollector.translateToLocalFormatted("gt.NetworkAnalyzer.mode", modeName));
    }

    @Override
    public boolean onItemUse(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side,
        float xOff, float yOff, float zOff) {

        if (!world.isRemote) {
            final TileEntity tile = world.getTileEntity(x, y, z);

            if (tile instanceof BaseMetaPipeEntity cableMeta && cableMeta.getMetaTileEntity() instanceof MTECable
                || tile instanceof BaseMetaTileEntity multiMeta
                    && multiMeta.getMetaTileEntity() instanceof MTEMultiBlockBase) {
                final DimensionalCoord coord = new DimensionalCoord(tile);
                setLocation(stack, coord);

                player.addChatMessage(
                    new ChatComponentText(
                        StatCollector.translateToLocalFormatted(
                            "gt.NetworkAnalyzer.bound",
                            String.valueOf(coord.x),
                            String.valueOf(coord.y),
                            String.valueOf(coord.z))));

                return true;
            }

        }

        return false;
    }

    @Override
    public ItemStack onItemRightClick(ItemStack stack, World worldIn, EntityPlayer p) {

        if (worldIn.isRemote && p.isSneaking() && stack.getItem() instanceof ItemNetworkAnalyzer) {
            final AnalyzerModes newMode = nextInMode(
                Arrays.asList(AnalyzerModes.values()),
                ItemNetworkAnalyzer.getMode(stack));

            ItemNetworkAnalyzer.setMode(stack, newMode);
            GTValues.NW.sendToServer(new GTPacketNetworkAnalyzerMode(newMode));
        }

        return stack;
    }

    private static AnalyzerModes nextInMode(List<AnalyzerModes> seq, AnalyzerModes elem) {
        int pos = seq.indexOf(elem);
        return seq.get((pos + 1) % seq.size());
    }

    @Override
    public void onUpdate(ItemStack stack, World world, Entity entity, int slot, boolean aIsInHand) {

        if (!aIsInHand || world.isRemote || !(entity instanceof EntityPlayerMP player)) {
            return;
        }

        stack = player.inventory.getCurrentItem();
        final DimensionalCoord coord = getLocation(stack);

        if (coord != null && coord.getDimension() == world.provider.dimensionId
            && NetworkAnalyzerPlayerTracker.needToUpdate(player, coord)) {
            final TileEntity tile = world.getTileEntity(coord.x, coord.y, coord.z);

            if (tile instanceof BaseMetaPipeEntity aTileEntity) {
                final IMetaTileEntity metaTileEntity = aTileEntity.getMetaTileEntity();
                final PowerNetworkBuilder builder = new PowerNetworkBuilder();

                if (metaTileEntity instanceof MTECable && findNode(aTileEntity) instanceof PowerNode tNode) {
                    builder.addNetwork(tNode);
                }

                GTValues.NW.sendToPlayer(new GTPacketNetworkAnalyzer(builder.getData(getMode(stack))), player);
            } else if (tile instanceof BaseMetaTileEntity multiMeta
                && multiMeta.getMetaTileEntity() instanceof MTEMultiBlockBase multiBlock) {
                    final PowerNetworkBuilder builder = new PowerNetworkBuilder();

                    collectHatchNetworks(multiBlock.mEnergyHatches, builder);
                    collectHatchNetworks(multiBlock.mDynamoHatches, builder);
                    collectHatchNetworks(multiBlock.getExoticEnergyHatches(), builder);
                    collectHatchNetworks(multiBlock.getExoticDynamoHatches(), builder);

                    GTValues.NW.sendToPlayer(new GTPacketNetworkAnalyzer(builder.getData(getMode(stack))), player);
                } else {
                    ItemStackNBT.removeTag(stack, "dim");
                    NetworkAnalyzerPlayerTracker.reset(player);
                    GTValues.NW.sendToPlayer(new GTPacketNetworkAnalyzer(new NetworkAnalyzerData()), player);
                    player.addChatMessage(
                        new ChatComponentText(StatCollector.translateToLocal("gt.NetworkAnalyzer.target_lost")));
                }

        }

    }

    private static void collectHatchNetworks(Iterable<? extends MTEHatch> hatches, PowerNetworkBuilder builder) {
        for (MTEHatch hatch : hatches) {
            final IGregTechTileEntity hatchBase = hatch.getBaseMetaTileEntity();
            if (hatchBase == null) continue;
            final ForgeDirection facing = hatchBase.getFrontFacing();
            final TileEntity neighbor = hatchBase.getWorld()
                .getTileEntity(
                    hatchBase.getXCoord() + facing.offsetX,
                    hatchBase.getYCoord() + facing.offsetY,
                    hatchBase.getZCoord() + facing.offsetZ);
            if (neighbor instanceof BaseMetaPipeEntity pipe && pipe.getNode() instanceof PowerNode tNode) {
                builder.addNetwork(tNode);
            }
        }
    }

    private Node findNode(BaseMetaPipeEntity pipe) {
        final Set<BaseMetaPipeEntity> visited = new HashSet<>();
        final ArrayDeque<BaseMetaPipeEntity> queue = new ArrayDeque<>();
        queue.add(pipe);
        visited.add(pipe);

        while (!queue.isEmpty()) {
            final BaseMetaPipeEntity current = queue.poll();
            final MetaPipeEntity tMetaPipe = (MetaPipeEntity) current.getMetaTileEntity();
            final Node node = current.getNode();

            if (node != null) {
                return node;
            }

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                if (!tMetaPipe.isConnectedAtSide(dir)) continue;
                final TileEntity neighbor = current.getTileEntityAtSide(dir);

                if (neighbor instanceof BaseMetaPipeEntity neighborPipe && !visited.contains(neighborPipe)) {
                    visited.add(neighborPipe);
                    queue.add(neighborPipe);
                }
            }
        }

        return null;
    }

}
