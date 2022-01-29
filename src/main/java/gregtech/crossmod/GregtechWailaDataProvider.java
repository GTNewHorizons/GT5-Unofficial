package gregtech.crossmod;

import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_BasicMachine;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Transformer;
import gregtech.common.covers.GT_Cover_Fluidfilter;
import gregtech.common.tileentities.boilers.GT_MetaTileEntity_Boiler_Solar;
import gregtech.common.tileentities.machines.long_distance.GT_MetaTileEntity_LongDistancePipelineBase;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import static mcp.mobius.waila.api.SpecialChars.RESET;
import static mcp.mobius.waila.api.SpecialChars.GOLD;
import static mcp.mobius.waila.api.SpecialChars.BLUE;
import static mcp.mobius.waila.api.SpecialChars.GREEN;
import static mcp.mobius.waila.api.SpecialChars.RED;

import java.util.List;

public class GregtechWailaDataProvider implements IWailaDataProvider {
    private String getKey(String key) {
        return "Gregtech5U:" + key;
    }
    
    @Override
    public ItemStack getWailaStack(IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        return currenttip;
    }
    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        final TileEntity tile = accessor.getTileEntity();
        MovingObjectPosition pos = accessor.getPosition();
        NBTTagCompound tag = accessor.getNBTData();
        final int side = (byte)accessor.getSide().ordinal();

        final IGregTechTileEntity tBaseMetaTile = tile instanceof IGregTechTileEntity ? ((IGregTechTileEntity) tile) : null;
        final IMetaTileEntity tMeta = tBaseMetaTile != null ? tBaseMetaTile.getMetaTileEntity() : null;
        final BaseMetaTileEntity mBaseMetaTileEntity = tile instanceof  BaseMetaTileEntity ? ((BaseMetaTileEntity) tile) : null;
        final GT_MetaTileEntity_BasicMachine BasicMachine = tMeta instanceof GT_MetaTileEntity_BasicMachine ? ((GT_MetaTileEntity_BasicMachine) tMeta) : null;
        final GT_MetaTileEntity_MultiBlockBase multiBlockBase = tMeta instanceof GT_MetaTileEntity_MultiBlockBase ? ((GT_MetaTileEntity_MultiBlockBase) tMeta) : null;

        final boolean showTransformer = tMeta instanceof GT_MetaTileEntity_Transformer && config.getConfig(getKey("transformer"));
        final boolean showLDP = tMeta instanceof GT_MetaTileEntity_LongDistancePipelineBase && config.getConfig(getKey("LDP"));
        final boolean showSolar = tMeta instanceof GT_MetaTileEntity_Boiler_Solar && config.getConfig(getKey("solar"));
        final boolean allowedToWork = tag.hasKey("isAllowedToWork") && tag.getBoolean("isAllowedToWork");

        if (tBaseMetaTile != null && config.getConfig(getKey("fluidFilter"))) {
            final String filterKey = "filterInfo" + side;
            if (tag.hasKey(filterKey)) {
                currenttip.add(tag.getString(filterKey));
            }
        }

        if (tMeta != null) {
            String facingStr = "Facing";
            if (showTransformer && tag.hasKey("isAllowedToWork")) {
                currenttip.add(
                    String.format(
                        "%s %d(%dA) -> %d(%dA)",
                        (allowedToWork ? (GREEN + "Step Down") : (RED + "Step Up")) + RESET,
                        tag.getLong("maxEUInput"),
                        tag.getLong("maxAmperesIn"),
                        tag.getLong("maxEUOutput"),
                        tag.getLong("maxAmperesOut")
                    )
                );
                facingStr = tag.getBoolean("isAllowedToWork") ? "Input" : "Output";
            }
            if (showSolar && tag.hasKey("calcificationOutput")) {
                currenttip.add(String.format((GOLD + "Solar Boiler Output: " + RESET + "%d/%d L/s"), tag.getInteger("calcificationOutput"), tag.getInteger("maxCalcificationOutput")));
            }

            if (mBaseMetaTileEntity != null && config.getConfig(getKey("machineFacing"))) {
                final int facing = mBaseMetaTileEntity.getFrontFacing();
                if(showTransformer) {
                    if((side == facing && allowedToWork) || (side != facing && !allowedToWork)) {
                        currenttip.add(String.format(GOLD + "Input:" + RESET + " %d(%dA)", tag.getLong("maxEUInput"), tag.getLong("maxAmperesIn")));
                    } else {
                        currenttip.add(String.format(BLUE + "Output:" + RESET + " %d(%dA)", tag.getLong("maxEUOutput"), tag.getLong("maxAmperesOut")));
                    }
                } else if (showLDP) {
                    if(side == facing)
                        currenttip.add(GOLD + "Pipeline Input" + RESET);
                    else if (side == ForgeDirection.OPPOSITES[facing])
                        currenttip.add(BLUE + "Pipeline Output" + RESET);
                    else
                        currenttip.add("Pipeline Side");
                } else {
                    currenttip.add(String.format("%s: %s", facingStr, ForgeDirection.getOrientation(facing).name()));
                }
            }

            if (BasicMachine != null && config.getConfig(getKey("basicmachine"))) {
                currenttip.add(String.format("Progress: %d s / %d s", tag.getInteger("progressSingleBlock"), tag.getInteger("maxProgressSingleBlock")));
            }

            if(multiBlockBase != null && config.getConfig(getKey("multiblock"))) {
                if(tag.getBoolean("incompleteStructure")) {
                    currenttip.add(RED + "** INCOMPLETE STRUCTURE **" + RESET);
                }
                currenttip.add((tag.getBoolean("hasProblems") ? (RED + "** HAS PROBLEMS **") : GREEN + "Running Fine") + RESET
                                   + "  Efficiency: " + tag.getFloat("efficiency") + "%");

                currenttip.add(String.format("Progress: %d s / %d s", tag.getInteger("progress"), tag.getInteger("maxProgress")));

            }
        }
        return currenttip;
    }
    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> currenttip, IWailaDataAccessor accessor, IWailaConfigHandler config) {
        final TileEntity tile = accessor.getTileEntity();
        return currenttip;
    }

    @Override
    public NBTTagCompound getNBTData(final EntityPlayerMP player, final TileEntity tile, final NBTTagCompound tag, final World world, int x, int y, int z) {
        final IGregTechTileEntity tBaseMetaTile = tile instanceof IGregTechTileEntity ? ((IGregTechTileEntity) tile) : null;
        final IMetaTileEntity tMeta = tBaseMetaTile != null ? tBaseMetaTile.getMetaTileEntity() : null;
        final GT_MetaTileEntity_BasicMachine BasicMachine = tMeta instanceof GT_MetaTileEntity_BasicMachine ? ((GT_MetaTileEntity_BasicMachine) tMeta) : null;
        final GT_MetaTileEntity_MultiBlockBase multiBlockBase = tMeta instanceof GT_MetaTileEntity_MultiBlockBase ? ((GT_MetaTileEntity_MultiBlockBase) tMeta) : null;

        if (tMeta != null) {
            if (tMeta instanceof GT_MetaTileEntity_Transformer) {
                final GT_MetaTileEntity_Transformer transformer = (GT_MetaTileEntity_Transformer)tMeta;
                tag.setBoolean("isAllowedToWork", tMeta.getBaseMetaTileEntity().isAllowedToWork());
                tag.setLong("maxEUInput", transformer.maxEUInput());
                tag.setLong("maxAmperesIn", transformer.maxAmperesIn());
                tag.setLong("maxEUOutput", transformer.maxEUOutput());
                tag.setLong("maxAmperesOut", transformer.maxAmperesOut());
            } else if (tMeta instanceof GT_MetaTileEntity_Boiler_Solar) {
                final GT_MetaTileEntity_Boiler_Solar solar = (GT_MetaTileEntity_Boiler_Solar)tMeta;
                tag.setInteger("calcificationOutput", (solar.getProductionPerSecond()));
                tag.setInteger("maxCalcificationOutput", (solar.getMaxOutputPerSecond()));
            }

            if (BasicMachine != null) {
                final int progressSingleBlock = BasicMachine.mProgresstime/20;
                final int maxProgressSingleBlock = BasicMachine.mMaxProgresstime/20;
                tag.setInteger("progressSingleBlock", progressSingleBlock);
                tag.setInteger("maxProgressSingleBlock", maxProgressSingleBlock);
            }

            if (multiBlockBase != null) {
                final int problems = multiBlockBase.getIdealStatus() - multiBlockBase.getRepairStatus();
                final float efficiency = multiBlockBase.mEfficiency / 100.0F;
                final int progress = multiBlockBase.mProgresstime/20;
                final int maxProgress = multiBlockBase.mMaxProgresstime/20;

                tag.setBoolean("hasProblems", problems > 0);
                tag.setFloat("efficiency", efficiency);
                tag.setInteger("progress", progress);
                tag.setInteger("maxProgress", maxProgress);
                tag.setBoolean("incompleteStructure", (tBaseMetaTile.getErrorDisplayID() & 64) != 0);

            }
        }
        if (tBaseMetaTile != null) {
            if (tBaseMetaTile instanceof BaseMetaPipeEntity) {
                for(byte side=0 ; side < 6 ; side++) {
                    if(tBaseMetaTile.getCoverBehaviorAtSideNew(side) instanceof GT_Cover_Fluidfilter) {
                        tag.setString("filterInfo" + side, ((GT_Cover_Fluidfilter) tBaseMetaTile.getCoverBehaviorAtSideNew(side)).getDescription(side, tBaseMetaTile.getCoverIDAtSide(side), tBaseMetaTile.getComplexCoverDataAtSide(side), tBaseMetaTile));
                    }
                }
            }
        }

        return tag;
    }
}
