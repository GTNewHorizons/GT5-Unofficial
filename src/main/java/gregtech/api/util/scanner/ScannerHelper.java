package gregtech.api.util.scanner;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.D1;
import static gregtech.api.enums.GTValues.E;
import static gregtech.api.util.GTUtility.getFluidName;
import static gregtech.api.util.GTUtility.getTier;
import static gregtech.api.util.GTUtility.translate;
import static gregtech.common.UndergroundOil.undergroundOilReadInformation;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
import gregtech.api.interfaces.tileentity.IBasicEnergyContainer;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IGregTechDeviceInformation;
import gregtech.api.util.GTLog;
import gregtech.common.pollution.Pollution;
import ic2.api.energy.tile.IEnergyConductor;
import ic2.api.reactor.IReactor;
import ic2.api.tile.IEnergyStorage;
import ic2.api.tile.IWrenchable;

public class ScannerHelper {

    // region Translate
    private static String trans(String name) {
        return translate("GT5U.scanner." + name);
    }

    private static String trans(String name, Object... param) {
        return translate("GT5U.scanner." + name, param);
    }
    // endregion

    // region init
    public static int scan(List<String> list, EntityPlayer player, World world, int scanLevel, int x, int y, int z,
        ForgeDirection side, float clickX, float clickY, float clickZ) {
        if (list == null) return 0;

        final List<String> resultList = new ArrayList<>();
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
    private static String addTitle(String name) {
        int maxWidth = 45;

        if (name == null) name = "";

        int paddingLength = Math.max(0, maxWidth - name.length() - 2) / 2;

        StringBuilder padding = new StringBuilder();

        for (int i = 0; i < paddingLength; i++) {
            padding.append("-");
        }

        return EnumChatFormatting.AQUA.toString() + EnumChatFormatting.STRIKETHROUGH
            + padding
            + EnumChatFormatting.RESET
            + EnumChatFormatting.AQUA
            + " "
            + trans(name)
            + " "
            + EnumChatFormatting.STRIKETHROUGH
            + padding;
    }

    private static void addBaseInfo(EntityPlayer player, World world, int x, int y, int z, List<String> list,
        TileEntity tileEntity, Block block) {
        list.add(addTitle("title_base_info"));
        list.add(trans("base_info_1", formatNumber(x), formatNumber(y), formatNumber(z)));
        try {
            String name = ((tileEntity instanceof IInventory inv) ? inv.getInventoryName()
                : block.getUnlocalizedName());
            int meta = world.getBlockMetadata(x, y, z);
            float hardness = block.getBlockHardness(world, x, y, z);
            float explosionResist = block
                .getExplosionResistance(player, world, x, y, z, player.posX, player.posY, player.posZ);

            list.add(trans("base_info_2", name, meta));
            list.add(trans("base_info_3", hardness, explosionResist));

            if (block.isBeaconBase(world, x, y, z, x, y + 1, z)) {
                list.add(trans("base_info_4"));
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_base_info"));
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static int addFluidHandlerInfo(ForgeDirection side, List<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof IFluidHandler fluidHandler) {
                final FluidTankInfo[] tanks = fluidHandler.getTankInfo(side);
                if (tanks != null && tanks.length > 0) {
                    list.add(addTitle("title_fluid_handler_info"));
                    for (int i = 0; i < tanks.length; i++) {
                        String currAmount = formatNumber((tanks[i].fluid == null ? 0 : tanks[i].fluid.amount));
                        String maxAmount = formatNumber(tanks[i].capacity);
                        String name = getFluidName(tanks[i].fluid, true);

                        list.add(trans("fluid_handler_info_1", i, currAmount, maxAmount, name));
                        euAmount += 500;
                    }
                }
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_fluid_handler_info"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addReactorInfo(List<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof ic2.api.reactor.IReactorChamber chamber) {
                tileEntity = (TileEntity) chamber.getReactor();
            }
            if (tileEntity instanceof IReactor reactor) {
                list.add(addTitle("title_reactor_info"));
                String currHeat = formatNumber(reactor.getHeat());
                String maxHeat = formatNumber(reactor.getMaxHeat());
                float heatModifier = reactor.getHeatEffectModifier();

                list.add(trans("reactor_info_1", currHeat, maxHeat, heatModifier));
                euAmount += 500;
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_reactor_info"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addDebuggableBlockInfo(EntityPlayer player, int x, int y, int z, List<String> list,
        Block block) {
        int euAmount = 0;
        try {
            if (block instanceof IDebugableBlock debugableBlock) {
                final List<String> temp = debugableBlock.getDebugInfo(player, x, y, z, 3);
                if (temp != null && !temp.isEmpty()) {
                    list.add(addTitle("title_debug_info"));
                    list.addAll(temp);
                    euAmount += 500;
                }
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_debug_info"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static void addMachineInfo(List<String> list, TileEntity tileEntity) {
        try {
            if (tileEntity instanceof IGregTechDeviceInformation info && info.isGivingInformation()) {
                final List<String> resultList = Arrays.asList(info.getInfoData());

                if (resultList.isEmpty()) return;

                list.add(addTitle("title_machine_info"));
                list.addAll(resultList);
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_machine_info"));
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static void addCustomInfo(List<String> list, TileEntity tileEntity) {
        try {
            if (tileEntity instanceof IGregTechDeviceInformation info && info.isGivingInformation()) {
                final List<String> resultList = new ArrayList<>();

                info.getExtraInfoData(resultList);

                if (resultList.isEmpty()) return;

                list.add(addTitle("title_custom_info"));
                list.addAll(resultList);
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_custom_info"));
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static int addAlignmentInfo(List<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof IAlignmentProvider alignmentProvider) {
                final IAlignment alignment = alignmentProvider.getAlignment();
                if (alignment != null) {
                    list.add(addTitle("title_side_info"));
                    list.add(trans("side_info_1", alignment.getExtendedFacing()));
                    euAmount += 100;
                }
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + "error_side_info");
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addWrenchInfo(EntityPlayer player, List<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof IWrenchable wrenchable) {
                short face = wrenchable.getFacing();
                float chance = wrenchable.getWrenchDropRate() * 100;

                list.add(addTitle("title_wrench_info"));
                list.add(trans("wrench_info_1", face, chance));

                if (wrenchable.wrenchCanRemove(player)) {
                    list.add(trans("wrench_info_2"));
                } else {
                    list.add(trans("wrench_info_3"));
                }
                euAmount += 100;
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_wrench_info"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addIC2Info(List<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            List<String> tempList = new ArrayList<>();
            if (tileEntity instanceof IEnergyConductor conductor) {
                euAmount += 200;
                tempList.add(trans("ic2_info_1", conductor.getConductionLoss()));
            }

            if (tileEntity instanceof IEnergyStorage storage) {
                euAmount += 200;
                String currStored = formatNumber(storage.getStored());
                String maxStored = formatNumber(storage.getCapacity());

                tempList.add(trans("ic2_info_2", currStored, maxStored));
            }

            if (!tempList.isEmpty()) {
                list.add(addTitle("title_ic2_info"));
                list.addAll(tempList);
            }

        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_ic2_info"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addCoverableInfo(ForgeDirection side, List<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof ICoverable coverable) {
                final String coverInfo = coverable.getCoverAtSide(side)
                    .getDescription();

                if (coverInfo != null && !coverInfo.equals(E)) {
                    list.add(addTitle("title_cover_info"));
                    list.add(coverInfo);
                    euAmount += 300;
                }
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_cover_info"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static void addEnergyContainerInfo(List<String> list, TileEntity tileEntity) {
        try {
            if (tileEntity instanceof IBasicEnergyContainer energyContainer && energyContainer.getEUCapacity() > 0) {
                list.add(addTitle("title_energy_info"));
                String inputVoltage = formatNumber(energyContainer.getInputVoltage());
                String inputTier = GTValues.VN[getTier(energyContainer.getInputVoltage())];
                String inputAmperage = formatNumber(energyContainer.getInputAmperage());

                String outputVoltage = formatNumber(energyContainer.getOutputVoltage());
                String outputTier = GTValues.VN[getTier(energyContainer.getOutputVoltage())];
                String outputAmperage = formatNumber(energyContainer.getOutputAmperage());

                String storedEU = formatNumber(energyContainer.getStoredEU());
                String euCapacity = formatNumber(energyContainer.getEUCapacity());

                list.add(trans("energy_info_1", inputVoltage, inputTier, inputAmperage));
                list.add(trans("energy_info_2", outputVoltage, outputTier, outputAmperage));
                list.add(trans("energy_info_3", storedEU, euCapacity));

            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_energy_info"));
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static int addForestryLeavesInfo(List<String> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (Mods.Forestry.isModLoaded() && tileEntity instanceof TileLeaves tileLeaves) {
                final ITree tree = tileLeaves.getTree();
                if (tree != null) {
                    if (!tree.isAnalyzed()) tree.analyze();
                    list.add(addTitle("title_leaves_info"));
                    tree.addTooltip(list);
                    euAmount += 1000;
                }
            }
        } catch (Exception e) {
            list.add(EnumChatFormatting.RED + trans("error_leaves_info"));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static void addChunkInfo(List<String> list, Chunk currentChunk, EntityPlayer player) {
        list.add(addTitle("title_chunk_info"));
        if (Pollution.hasPollution(currentChunk)) {
            String pollution = formatNumber(Pollution.getPollution(currentChunk));
            list.add(trans("chunk_info_1", pollution));
        } else {
            list.add(trans("chunk_info_2"));
        }

        if (player.capabilities.isCreativeMode) {
            final FluidStack fluid = undergroundOilReadInformation(currentChunk);
            if (fluid != null) {
                String fluidName = fluid.getLocalizedName();
                String fluidAmount = formatNumber(fluid.amount);

                list.add(trans("chunk_info_3", fluidName, fluidAmount));
            } else {
                list.add(trans("chunk_info_4"));
            }
        }
    }
    // endregion
}
