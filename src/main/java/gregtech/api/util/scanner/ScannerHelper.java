package gregtech.api.util.scanner;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.D1;
import static gregtech.api.enums.GTValues.E;
import static gregtech.api.util.GTUtility.getFluidName;
import static gregtech.api.util.GTUtility.translate;
import static gregtech.api.util.GTUtility.getTier;
import static gregtech.common.UndergroundOil.undergroundOilReadInformation;

import java.util.ArrayList;
import java.util.Arrays;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTankInfo;
import net.minecraftforge.fluids.IFluidHandler;

import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;

import forestry.api.arboriculture.ITree;
import forestry.arboriculture.tiles.TileLeaves;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.Mods;
import gregtech.api.events.BlockScanningEvent;
import gregtech.api.interfaces.IDebugableBlock;
import gregtech.api.interfaces.tileentity.*;
import gregtech.api.util.GTLog;
import gregtech.common.pollution.Pollution;
import ic2.api.crops.ICropTile;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.reactor.IReactor;
import ic2.api.tile.IEnergyStorage;
import ic2.api.tile.IWrenchable;

public class ScannerHelper {

    // region Translate
    private static String trans(String name) {
        return translate("GT5U.scanner." + name);
    }
    // endregion

    // region init
    public static int scan(ArrayList<String> list, EntityPlayer player, World world, int scanLevel, int x, int y, int z,
        ForgeDirection side, float clickX, float clickY, float clickZ) {
        if (list == null) return 0;

        final ArrayList<String> resultList = new ArrayList<>();
        final Chunk currentChunk = world.getChunkFromBlockCoords(x, z);
        final Block block = world.getBlock(x, y, z);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        int euAmount = 0;

        if (ScannerConfig.showBaseInfo) {
            addBaseInfo(player, world, x, y, z, resultList, tileEntity, block);
        }

        if (tileEntity != null) {
            if (ScannerConfig.showFluidHandlerInfo) {
                euAmount += addFluidHandlerInfo(side, resultList, tileEntity);
            }

            if (ScannerConfig.showReactorInfo) {
                euAmount += addReactorInfo(resultList, tileEntity);
            }

            if (ScannerConfig.showAlignmentInfo) {
                euAmount += addAlignmentInfo(resultList, tileEntity);
            }

            if (ScannerConfig.showWrenchInfo) {
                euAmount += addWrenchInfo(player, resultList, tileEntity);
            }

            if (ScannerConfig.showIC2Info) {
                euAmount += addIC2Info(resultList, tileEntity);
            }

            if (ScannerConfig.showCoverableInfo) {
                euAmount += addCoverableInfo(side, resultList, tileEntity);
            }

            if (ScannerConfig.showEnergyContainerInfo) {
                addEnergyContainerInfo(resultList, tileEntity);
            }

            if (ScannerConfig.showMachineInfo) {
                addMachineInfo(resultList, tileEntity);
            }

            if (ScannerConfig.showCustomInfo) {
                addCustomInfo(resultList, tileEntity);
            }

            if (ScannerConfig.showIC2CropInfo) {
                euAmount += addIC2CropInfo(resultList, tileEntity);
            }

            if (ScannerConfig.showForestryLeavesInfo) {
                euAmount += addForestryLeavesInfo(resultList, tileEntity);
            }
        }

        if (ScannerConfig.showChunkInfo) {
            addChunkInfo(resultList, currentChunk, player);
        }

        if (ScannerConfig.showDebugInfo) {
            euAmount += addDebuggableBlockInfo(player, x, y, z, resultList, block);
        }

        final BlockScanningEvent event = new BlockScanningEvent(
            world,
            player,
            x,
            y,
            z,
            side,
            scanLevel,
            block,
            tileEntity,
            resultList,
            clickX,
            clickY,
            clickZ);
        event.mEUCost = euAmount;
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) list.addAll(resultList);
        return event.mEUCost;
    }
    // endregion

    // region Text
    // spotless:off
    private static String addTitle(String name) {
        int maxWidth = 45;

        if (name == null) name = "";

        if (maxWidth < name.length() + 2) {
            maxWidth = name.length() + 2;
        }

        int paddingLength = (maxWidth - name.length() - 2) / 2;

        StringBuilder padding = new StringBuilder();

        for (int i = 0; i < paddingLength; i++) {
            padding.append("-");
        }

        return EnumChatFormatting.AQUA.toString()
            + EnumChatFormatting.STRIKETHROUGH
            + padding
            + EnumChatFormatting.RESET
            + EnumChatFormatting.AQUA
            + " "
            + name
            + " "
            + EnumChatFormatting.STRIKETHROUGH
            + padding;
    }

    private static void addBaseInfo(EntityPlayer player, World world, int x, int y, int z, ArrayList<String> list,
                                    TileEntity tTileEntity, Block tBlock) {
        list.add(addTitle(trans("title_block")));
        list.add(trans("pos")
            + " "
            + trans("x")
            + EnumChatFormatting.GOLD
            + " "
            + x
            + " "
            + EnumChatFormatting.WHITE
            + trans("y")
            + EnumChatFormatting.GOLD
            + " "
            + y
            + " "
            + EnumChatFormatting.WHITE
            + trans("z")
            + EnumChatFormatting.GOLD
            + " "
            + z);
        try {
            list.add(trans("name")
                + EnumChatFormatting.GOLD
                + " "
                + ((tTileEntity instanceof IInventory inv) ? inv.getInventoryName() : tBlock.getUnlocalizedName())
                + " "
                + EnumChatFormatting.WHITE
                + trans("metadata")
                + EnumChatFormatting.GOLD
                + " "
                + world.getBlockMetadata(x, y, z)
                + EnumChatFormatting.WHITE);
            list.add(trans("hardness")
                + EnumChatFormatting.GOLD
                + " "
                + tBlock.getBlockHardness(world, x, y, z)
                + " "
                + EnumChatFormatting.WHITE
                + trans("blast_resistance")
                + EnumChatFormatting.GOLD
                + " "
                + tBlock.getExplosionResistance(player, world, x, y, z, player.posX, player.posY, player.posZ)
                + EnumChatFormatting.WHITE);
            if (tBlock.isBeaconBase(world, x, y, z, x, y + 1, z))
                list.add(EnumChatFormatting.GOLD
                    + trans("beacon")
                    + EnumChatFormatting.RESET);
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_block"));
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static int addFluidHandlerInfo(ForgeDirection side, ArrayList<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof IFluidHandler fluidHandler) {
                final FluidTankInfo[] tanks = fluidHandler.getTankInfo(side);
                if (tanks != null && tanks.length > 0) {
                    list.add(addTitle(trans("title_tank")));
                    for (byte i = 0; i < tanks.length; i++) {
                        list.add(trans("tank")
                            + " "
                            + i + ": "
                            + EnumChatFormatting.GOLD
                            + formatNumber((tanks[i].fluid == null ? 0 : tanks[i].fluid.amount))
                            + "L"
                            + EnumChatFormatting.WHITE
                            + " / "
                            + EnumChatFormatting.GOLD
                            + formatNumber(tanks[i].capacity)
                            + "L"
                            + EnumChatFormatting.WHITE
                            + " "
                            + getFluidName(tanks[i].fluid, true));
                        euAmount += 500;
                    }
                }
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_tank"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addReactorInfo(ArrayList<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof ic2.api.reactor.IReactorChamber chamber) {
                tileEntity = (TileEntity) chamber.getReactor();
            }
            if (tileEntity instanceof IReactor reactor) {
                euAmount += 500;
                list.add(addTitle(trans("title_reactor")));
                list.add(trans("heat")
                    + " "
                    + EnumChatFormatting.GOLD
                    + formatNumber(reactor.getHeat())
                    + EnumChatFormatting.WHITE
                    + " / "
                    + EnumChatFormatting.GOLD
                    + formatNumber(reactor.getMaxHeat())
                    + EnumChatFormatting.WHITE
                    + " "
                    + trans("hem")
                    + " "
                    + EnumChatFormatting.GOLD
                    + reactor.getHeatEffectModifier());
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_reactor"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addDebuggableBlockInfo(EntityPlayer player, int x, int y, int z, ArrayList<String> list, Block block) {
        int euAmount = 0;
        try {
            if (block instanceof IDebugableBlock debugableBlock) {
                euAmount += 500;
                final ArrayList<String> temp = debugableBlock.getDebugInfo(player, x, y, z, 3);
                if (temp != null && !temp.isEmpty()) {
                    list.add(addTitle(trans("title_debug")));
                    list.addAll(temp);
                }
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_debug"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static void addMachineInfo(ArrayList<String> list, TileEntity tileEntity) {
        try {
            if (tileEntity instanceof IGregTechDeviceInformation info && info.isGivingInformation()) {
                final ArrayList<String> resultList = new ArrayList<>(Arrays.asList(info.getInfoData()));

                if (resultList.isEmpty()) return;

                list.add(addTitle(trans("title_machine")));
                list.addAll(resultList);
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_machine"));
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static void addCustomInfo(ArrayList<String> list, TileEntity tileEntity) {
        try {
            if (tileEntity instanceof IGregTechDeviceInformation info && info.isGivingInformation()) {
                final ArrayList<String> resultList = new ArrayList<>();

                info.getExtraInfoData(resultList);

                if (resultList.isEmpty()) return;

                list.add(addTitle(trans("title_custom")));
                list.addAll(resultList);
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_machine"));
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static int addAlignmentInfo(ArrayList<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof IAlignmentProvider alignmentProvider) {
                final IAlignment alignment = alignmentProvider.getAlignment();
                if (alignment != null) {
                    euAmount += 100;
                    list.add(addTitle(trans("title_side")));
                    list.add(trans("side")
                        + " "
                        + EnumChatFormatting.GOLD
                        + alignment.getExtendedFacing());
                }
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + "error_side");
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addWrenchInfo(EntityPlayer player, ArrayList<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof IWrenchable wrenchable) {
                euAmount += 100;

                list.add(addTitle(trans("title_wrench")));
                list.add(trans("facing")
                    + " "
                    + EnumChatFormatting.GOLD
                    + wrenchable.getFacing()
                    + EnumChatFormatting.WHITE
                    + " / "
                    + trans("chance")
                    + " "
                    + EnumChatFormatting.GOLD
                    + (wrenchable.getWrenchDropRate() * 100)
                    + "%");
                list.add(wrenchable.wrenchCanRemove(player)
                    ? EnumChatFormatting.GOLD
                    + trans("wrench_yes")
                    + EnumChatFormatting.WHITE
                    : EnumChatFormatting.RED
                    + trans("wrench_no"));
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_wrench"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addIC2Info(ArrayList<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            final ArrayList<String> resultList = new ArrayList<>();

            if (tileEntity instanceof IEnergyConductor conductor) {
                euAmount += 200;
                resultList.add(trans("conduction_loss")
                    + " "
                    + EnumChatFormatting.GOLD
                    + conductor.getConductionLoss());
            }

            if (tileEntity instanceof IEnergyStorage storage) {
                euAmount += 200;
                resultList.add(trans("contained_energy")
                    + " "
                    + EnumChatFormatting.GOLD
                    + formatNumber(storage.getStored())
                    + " "
                    + trans("eu")
                    + EnumChatFormatting.WHITE
                    + " / "
                    + EnumChatFormatting.GOLD
                    + formatNumber(storage.getCapacity())
                    + " "
                    + trans("eu"));
            }

            if (resultList.isEmpty()) return euAmount;
            list.add(addTitle(trans("title_ic2")));
            list.addAll(resultList);
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_ic2"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addCoverableInfo(ForgeDirection side, ArrayList<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof ICoverable coverable) {
                euAmount += 300;

                final String tString = coverable.getCoverAtSide(side).getDescription();

                if (tString != null && !tString.equals(E)) {
                    list.add(addTitle(trans("title_cover")));
                    list.add(tString);
                }
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_cover"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static void addEnergyContainerInfo(ArrayList<String> list, TileEntity tileEntity) {
        try {
            if (tileEntity instanceof IBasicEnergyContainer energyContainer && energyContainer.getEUCapacity() > 0) {
                list.add(addTitle(trans("title_energy")));
                list.add(trans("max_in")
                    + " "
                    + EnumChatFormatting.GOLD
                    + formatNumber(energyContainer.getInputVoltage())
                    + EnumChatFormatting.WHITE
                    + " ("
                    + GTValues.VN[getTier(energyContainer.getInputVoltage())]
                    + ") "
                    + " "
                    + trans("eu")
                    + " "
                    + trans("at")
                    + EnumChatFormatting.GOLD
                    + " "
                    + formatNumber(energyContainer.getInputAmperage())
                    + " "
                    + trans("a"));
                list.add(trans("max_out")
                    + " "
                    + EnumChatFormatting.GOLD
                    + formatNumber(energyContainer.getOutputVoltage())
                    + EnumChatFormatting.WHITE
                    + " ("
                    + GTValues.VN[getTier(energyContainer.getOutputVoltage())]
                    + ") "
                    + " "
                    + trans("eu")
                    + " "
                    + trans("at")
                    + EnumChatFormatting.GOLD
                    + " "
                    + formatNumber(energyContainer.getOutputAmperage())
                    + trans("a"));
                list.add(trans("energy")
                    + " "
                    + EnumChatFormatting.GOLD
                    + formatNumber(energyContainer.getStoredEU())
                    + " "
                    + trans("eu")
                    + EnumChatFormatting.WHITE
                    + " / "
                    + EnumChatFormatting.GOLD
                    + formatNumber(energyContainer.getEUCapacity())
                    + " "
                    + trans("eu"));
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_device_energy"));
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static int addIC2CropInfo(ArrayList<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof ICropTile crop) {
                euAmount += 1000;
                list.add(addTitle(trans("title_crop")));
                if (crop.getScanLevel() < 4) crop.setScanLevel((byte) 4);
                if (crop.getCrop() != null) {
                    list.add(trans("crop_name")
                        + " "
                        + EnumChatFormatting.GOLD
                        + crop.getCrop().name()
                        + EnumChatFormatting.WHITE
                        + " "
                        + trans("growth")
                        + " "
                        + EnumChatFormatting.GOLD
                        + crop.getGrowth()
                        + EnumChatFormatting.WHITE
                        + " "
                        + trans("gain")
                        + " "
                        + EnumChatFormatting.GOLD
                        + crop.getGain()
                        + EnumChatFormatting.WHITE
                        + " "
                        + trans("resistance")
                        + " "
                        + EnumChatFormatting.GOLD
                        + crop.getResistance());
                }
                list.add(trans("fertilizer")
                    + " "
                    + EnumChatFormatting.GOLD
                    + crop.getNutrientStorage()
                    + EnumChatFormatting.WHITE
                    + " "
                    + trans("water")
                    + " "
                    + EnumChatFormatting.GOLD
                    + crop.getHydrationStorage()
                    + EnumChatFormatting.WHITE
                    + " "
                    + trans("weed_ex")
                    + " "
                    + EnumChatFormatting.GOLD
                    + crop.getWeedExStorage()
                    + EnumChatFormatting.WHITE
                    + " "
                    + trans("scan_level")
                    + " "
                    + EnumChatFormatting.GOLD
                    + crop.getScanLevel());
                list.add(trans("nutrients")
                    + " "
                    + EnumChatFormatting.GOLD
                    + crop.getNutrients()
                    + EnumChatFormatting.WHITE
                    + " "
                    + trans("humidity")
                    + " "
                    + EnumChatFormatting.GOLD
                    + crop.getHumidity()
                    + EnumChatFormatting.WHITE
                    + " "
                    + trans("air_quality")
                    + " "
                    + EnumChatFormatting.GOLD
                    + crop.getAirQuality());
                if (crop.getCrop() != null) {
                    final StringBuilder attributeList = new StringBuilder();
                    for (final String attribute : crop.getCrop()
                        .attributes()) {
                        attributeList.append(", ")
                            .append(attribute);
                    }
                    list.add(trans("attributes")
                        + EnumChatFormatting.GOLD
                        + attributeList.toString().replaceFirst(",", E));
                    list.add(trans("discovered")
                        + EnumChatFormatting.GOLD
                        + " "
                        + crop.getCrop().discoveredBy());
                }
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_crop"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addForestryLeavesInfo(ArrayList<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (Mods.Forestry.isModLoaded() && tileEntity instanceof TileLeaves tileLeaves) {
                final ITree tree = tileLeaves.getTree();
                if (tree != null) {
                    euAmount += 1000;
                    if (!tree.isAnalyzed()) tree.analyze();
                    list.add(addTitle(trans("title_leaves")));
                    tree.addTooltip(list);
                }
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_leaves"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static void addChunkInfo(ArrayList<String> list, Chunk currentChunk, EntityPlayer player) {
        list.add(addTitle(trans("title_chunk")));
        if (Pollution.hasPollution(currentChunk)) {
            list.add(trans("pollution_yes")
                + " "
                + EnumChatFormatting.RED
                + formatNumber(Pollution.getPollution(currentChunk))
                + EnumChatFormatting.RESET
                + trans("gibbl"));
        } else {
            list.add(EnumChatFormatting.GREEN
                + trans("pollution_no")
                + EnumChatFormatting.RESET);
        }

        if (player.capabilities.isCreativeMode) {
            final FluidStack fluid = undergroundOilReadInformation(currentChunk);
            if (fluid != null) {
                list.add(EnumChatFormatting.WHITE
                    + fluid.getLocalizedName()
                    + ": "
                    + EnumChatFormatting.GOLD
                    + formatNumber(fluid.amount)
                    + " L");
            }
            else {
                list.add(EnumChatFormatting.WHITE
                    + trans("nothing")
                    + ": "
                    + EnumChatFormatting.GOLD
                    + '0'
                    + " L");
            }
        }
    }
    //spotless:on
    // endregion
}
