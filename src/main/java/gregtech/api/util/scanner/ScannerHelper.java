package gregtech.api.util.scanner;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static gregtech.api.enums.GTValues.D1;
import static gregtech.api.enums.GTValues.E;
import static gregtech.api.util.GTUtility.getFluidName;
import static gregtech.api.util.GTUtility.getTier;
import static gregtech.api.util.GTUtility.translate;
import static gregtech.common.UndergroundOil.undergroundOilReadInformation;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChatStyle;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
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
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
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

    private static IChatComponent transComp(String name) {
        return new ChatComponentTranslation("GT5U.scanner." + name);
    }

    private static IChatComponent transComp(String name, Object... params) {
        return new ChatComponentTranslation("GT5U.scanner." + name, params);
    }

    private static IChatComponent goldComp(Object val) {
        return new ChatComponentText(String.valueOf(val))
            .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD));
    }
    // endregion

    // region init

    /**
     * Scans a block and fills {@code list} with already-translated strings (server locale).
     * Kept for backward compatibility. Prefer {@link #scanComponents} so the client can translate in its own locale.
     *
     * @deprecated use {@link #scanComponents} instead
     */
    @Deprecated
    public static int scan(List<String> list, EntityPlayer player, World world, int scanLevel, int x, int y, int z,
        ForgeDirection side, float clickX, float clickY, float clickZ) {
        if (list == null) return 0;
        final List<IChatComponent> components = new ArrayList<>();
        final int cost = scanComponents(components, player, world, scanLevel, x, y, z, side, clickX, clickY, clickZ);
        for (IChatComponent comp : components) {
            list.add(comp.getFormattedText());
        }
        return cost;
    }

    /**
     * Scans a block and fills {@code list} with {@link IChatComponent} objects.
     * Translation keys are preserved so the client translates in its own locale.
     * Third-party strings (machine info, debug blocks, covers) are wrapped in {@link ChatComponentText}
     * and will still appear in the server's locale.
     */
    public static int scanComponents(List<IChatComponent> list, EntityPlayer player, World world, int scanLevel, int x,
        int y, int z, ForgeDirection side, float clickX, float clickY, float clickZ) {
        if (list == null) return 0;

        final List<IChatComponent> resultList = new ArrayList<>();
        final Chunk currentChunk = world.getChunkFromBlockCoords(x, z);
        final Block block = world.getBlock(x, y, z);
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        int euAmount = 0;

        if (ScannerConfig.showBaseInfo) {
            addBaseInfoComp(player, world, x, y, z, resultList, tileEntity, block);
        }

        if (tileEntity != null) {
            if (ScannerConfig.showFluidHandlerInfo) {
                euAmount += addFluidHandlerInfoComp(side, resultList, tileEntity);
            }

            if (ScannerConfig.showReactorInfo) {
                euAmount += addReactorInfoComp(resultList, tileEntity);
            }

            if (ScannerConfig.showAlignmentInfo) {
                euAmount += addAlignmentInfoComp(resultList, tileEntity);
            }

            if (ScannerConfig.showWrenchInfo) {
                euAmount += addWrenchInfoComp(player, resultList, tileEntity);
            }

            if (ScannerConfig.showIC2Info) {
                euAmount += addIC2InfoComp(resultList, tileEntity);
            }

            if (ScannerConfig.showCoverableInfo) {
                euAmount += addCoverableInfoComp(side, resultList, tileEntity);
            }

            if (ScannerConfig.showEnergyContainerInfo) {
                addEnergyContainerInfoComp(resultList, tileEntity);
            }

            if (ScannerConfig.showMachineInfo) {
                addMachineInfoComp(resultList, tileEntity);
            }

            if (ScannerConfig.showCustomInfo) {
                addCustomInfoComp(resultList, tileEntity);
            }

            if (ScannerConfig.showForestryLeavesInfo) {
                euAmount += addForestryLeavesInfoComp(resultList, tileEntity);
            }
        }

        if (ScannerConfig.showChunkInfo) {
            addChunkInfoComp(resultList, currentChunk, player);
        }

        if (ScannerConfig.showDebugInfo) {
            euAmount += addDebuggableBlockInfoComp(player, x, y, z, resultList, block);
        }

        final List<String> legacyStrings = new ArrayList<>();
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
            legacyStrings,
            resultList,
            clickX,
            clickY,
            clickZ);
        event.mEUCost = euAmount;
        MinecraftForge.EVENT_BUS.post(event);
        if (!event.isCanceled()) {
            list.addAll(resultList);
            // pick up any strings added by legacy event handlers
            for (String s : legacyStrings) {
                list.add(new ChatComponentText(s));
            }
        }
        return event.mEUCost;
    }

    // endregion

    // region Text
    public static IChatComponent addTitleComp(String name) {
        String dashes = "-".repeat(15);
        // Set AQUA style explicitly on the translation sibling so it inherits the title color.
        // Without this, the §r reset in the root text would leave the sibling unstyled (white).
        IChatComponent titleText = transComp(name).setChatStyle(new ChatStyle().setColor(EnumChatFormatting.AQUA));
        return new ChatComponentText(
            EnumChatFormatting.AQUA.toString() + EnumChatFormatting.STRIKETHROUGH
                + dashes
                + EnumChatFormatting.RESET
                + " ").appendSibling(titleText)
                    .appendSibling(
                        new ChatComponentText(
                            " " + EnumChatFormatting.AQUA + EnumChatFormatting.STRIKETHROUGH + dashes));
    }

    // region IChatComponent variants

    private static void addBaseInfoComp(EntityPlayer player, World world, int x, int y, int z,
        List<IChatComponent> list, TileEntity tileEntity, Block block) {
        list.add(addTitleComp("title_base_info"));
        list.add(
            transComp("base_info_1", goldComp(formatNumber(x)), goldComp(formatNumber(y)), goldComp(formatNumber(z))));
        try {
            IChatComponent nameComp = ((tileEntity instanceof IInventory inv)
                ? new ChatComponentTranslation(inv.getInventoryName())
                : new ChatComponentTranslation(block.getUnlocalizedName() + ".name"))
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD));
            int meta = world.getBlockMetadata(x, y, z);
            float hardness = block.getBlockHardness(world, x, y, z);
            float explosionResist = block
                .getExplosionResistance(player, world, x, y, z, player.posX, player.posY, player.posZ);

            list.add(transComp("base_info_2", nameComp, goldComp(meta)));
            if (tileEntity instanceof IGregTechTileEntity gtTE && gtTE.getMetaTileEntity() != null) {
                list.add(
                    transComp(
                        "base_info_meta_id",
                        goldComp(
                            gtTE.getMetaTileEntity()
                                .getMetaName())));
            }
            list.add(transComp("base_info_3", goldComp(hardness), goldComp(explosionResist)));

            if (block.isBeaconBase(world, x, y, z, x, y + 1, z)) {
                list.add(transComp("base_info_4").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD)));
            }
        } catch (Exception e) {
            list.add(transComp("error_base_info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static int addFluidHandlerInfoComp(ForgeDirection side, List<IChatComponent> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof IFluidHandler fluidHandler) {
                final FluidTankInfo[] tanks = fluidHandler.getTankInfo(side);
                if (tanks != null && tanks.length > 0) {
                    list.add(addTitleComp("title_fluid_handler_info"));
                    for (int i = 0; i < tanks.length; i++) {
                        String currAmount = formatNumber((tanks[i].fluid == null ? 0 : tanks[i].fluid.amount));
                        String maxAmount = formatNumber(tanks[i].capacity);
                        String name = getFluidName(tanks[i].fluid, true);

                        list.add(transComp("fluid_handler_info_1", i, goldComp(currAmount), goldComp(maxAmount), name));
                        euAmount += 500;
                    }
                }
            }
        } catch (Exception e) {
            list.add(
                transComp("error_fluid_handler_info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addReactorInfoComp(List<IChatComponent> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof ic2.api.reactor.IReactorChamber chamber) {
                tileEntity = (TileEntity) chamber.getReactor();
            }
            if (tileEntity instanceof IReactor reactor) {
                list.add(addTitleComp("title_reactor_info"));
                String currHeat = formatNumber(reactor.getHeat());
                String maxHeat = formatNumber(reactor.getMaxHeat());
                float heatModifier = reactor.getHeatEffectModifier();

                list.add(transComp("reactor_info_1", goldComp(currHeat), goldComp(maxHeat), goldComp(heatModifier)));
                euAmount += 500;
            }
        } catch (Exception e) {
            list.add(transComp("error_reactor_info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addDebuggableBlockInfoComp(EntityPlayer player, int x, int y, int z, List<IChatComponent> list,
        Block block) {
        int euAmount = 0;
        try {
            if (block instanceof IDebugableBlock debugableBlock) {
                final List<String> temp = debugableBlock.getDebugInfo(player, x, y, z, 3);
                if (temp != null && !temp.isEmpty()) {
                    list.add(addTitleComp("title_debug_info"));
                    for (String s : temp) list.add(new ChatComponentText(s));
                    euAmount += 500;
                }
            }
        } catch (Exception e) {
            list.add(transComp("error_debug_info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static void addMachineInfoComp(List<IChatComponent> list, TileEntity tileEntity) {
        try {
            if (tileEntity instanceof IGregTechDeviceInformation info && info.isGivingInformation()) {
                final String[] data = info.getInfoData();
                if (data == null || data.length == 0) return;

                list.add(addTitleComp("title_machine_info"));
                for (String s : data) list.add(IGregTechDeviceInformation.toComponent(s));
            }
        } catch (Exception e) {
            list.add(transComp("error_machine_info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static void addCustomInfoComp(List<IChatComponent> list, TileEntity tileEntity) {
        try {
            if (tileEntity instanceof IGregTechDeviceInformation info && info.isGivingInformation()) {
                final List<String> resultList = new ArrayList<>();
                info.getExtraInfoData(resultList);
                if (resultList.isEmpty()) return;

                list.add(addTitleComp("title_custom_info"));
                for (String s : resultList) list.add(IGregTechDeviceInformation.toComponent(s));
            }
        } catch (Exception e) {
            list.add(transComp("error_custom_info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static int addAlignmentInfoComp(List<IChatComponent> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof IAlignmentProvider alignmentProvider) {
                final IAlignment alignment = alignmentProvider.getAlignment();
                if (alignment != null) {
                    list.add(addTitleComp("title_side_info"));
                    list.add(transComp("side_info_1", goldComp(alignment.getExtendedFacing())));
                    euAmount += 100;
                }
            }
        } catch (Exception e) {
            list.add(transComp("error_side_info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addWrenchInfoComp(EntityPlayer player, List<IChatComponent> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof IWrenchable wrenchable) {
                short face = wrenchable.getFacing();
                float chance = wrenchable.getWrenchDropRate() * 100;

                list.add(addTitleComp("title_wrench_info"));
                list.add(transComp("wrench_info_1", goldComp(face), goldComp(chance)));
                list.add(
                    wrenchable.wrenchCanRemove(player)
                        ? transComp("wrench_info_2").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GOLD))
                        : transComp("wrench_info_3").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
                euAmount += 100;
            }
        } catch (Exception e) {
            list.add(transComp("error_wrench_info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addIC2InfoComp(List<IChatComponent> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            boolean hasConductor = tileEntity instanceof IEnergyConductor;
            boolean hasStorage = tileEntity instanceof IEnergyStorage;
            if (hasConductor || hasStorage) {
                list.add(addTitleComp("title_ic2_info"));
                if (hasConductor) {
                    list.add(transComp("ic2_info_1", goldComp(((IEnergyConductor) tileEntity).getConductionLoss())));
                    euAmount += 200;
                }
                if (hasStorage) {
                    IEnergyStorage storage = (IEnergyStorage) tileEntity;
                    list.add(
                        transComp(
                            "ic2_info_2",
                            goldComp(formatNumber(storage.getStored())),
                            goldComp(formatNumber(storage.getCapacity()))));
                    euAmount += 200;
                }
            }
        } catch (Exception e) {
            list.add(transComp("error_ic2_info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static int addCoverableInfoComp(ForgeDirection side, List<IChatComponent> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (tileEntity instanceof ICoverable coverable) {
                final String coverInfo = coverable.getCoverAtSide(side)
                    .getDescription();
                if (coverInfo != null && !coverInfo.equals(E)) {
                    list.add(addTitleComp("title_cover_info"));
                    list.add(new ChatComponentText(coverInfo));
                    euAmount += 300;
                }
            }
        } catch (Exception e) {
            list.add(transComp("error_cover_info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static void addEnergyContainerInfoComp(List<IChatComponent> list, TileEntity tileEntity) {
        try {
            if (tileEntity instanceof IBasicEnergyContainer energyContainer && energyContainer.getEUCapacity() > 0) {
                list.add(addTitleComp("title_energy_info"));
                String inputVoltage = formatNumber(energyContainer.getInputVoltage());
                String inputTier = GTValues.VN[getTier(energyContainer.getInputVoltage())];
                String inputAmperage = formatNumber(energyContainer.getInputAmperage());

                String outputVoltage = formatNumber(energyContainer.getOutputVoltage());
                String outputTier = GTValues.VN[getTier(energyContainer.getOutputVoltage())];
                String outputAmperage = formatNumber(energyContainer.getOutputAmperage());

                String storedEU = formatNumber(energyContainer.getStoredEU());
                String euCapacity = formatNumber(energyContainer.getEUCapacity());

                list.add(transComp("energy_info_1", goldComp(inputVoltage), inputTier, goldComp(inputAmperage)));
                list.add(transComp("energy_info_2", goldComp(outputVoltage), outputTier, goldComp(outputAmperage)));
                list.add(transComp("energy_info_3", goldComp(storedEU), goldComp(euCapacity)));
            }
        } catch (Exception e) {
            list.add(transComp("error_energy_info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            if (D1) e.printStackTrace(GTLog.err);
        }
    }

    private static int addForestryLeavesInfoComp(List<IChatComponent> list, TileEntity tileEntity) {
        int euAmount = 0;
        try {
            if (Mods.Forestry.isModLoaded() && tileEntity instanceof TileLeaves tileLeaves) {
                final ITree tree = tileLeaves.getTree();
                if (tree != null) {
                    if (!tree.isAnalyzed()) tree.analyze();
                    list.add(addTitleComp("title_leaves_info"));
                    final List<String> temp = new ArrayList<>();
                    tree.addTooltip(temp);
                    for (String s : temp) list.add(new ChatComponentText(s));
                    euAmount += 1000;
                }
            }
        } catch (Exception e) {
            list.add(transComp("error_leaves_info").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
            if (D1) e.printStackTrace(GTLog.err);
        }
        return euAmount;
    }

    private static void addChunkInfoComp(List<IChatComponent> list, Chunk currentChunk, EntityPlayer player) {
        list.add(addTitleComp("title_chunk_info"));
        if (Pollution.hasPollution(currentChunk)) {
            list.add(
                transComp("chunk_info_1", goldComp(formatNumber(Pollution.getPollution(currentChunk))))
                    .setChatStyle(new ChatStyle().setColor(EnumChatFormatting.RED)));
        } else {
            list.add(transComp("chunk_info_2").setChatStyle(new ChatStyle().setColor(EnumChatFormatting.GREEN)));
        }

        if (player.capabilities.isCreativeMode) {
            final FluidStack fluid = undergroundOilReadInformation(currentChunk);
            if (fluid != null) {
                list.add(
                    transComp(
                        "chunk_info_3",
                        new ChatComponentTranslation(fluid.getUnlocalizedName()),
                        goldComp(formatNumber(fluid.amount))));
            } else {
                list.add(transComp("chunk_info_4"));
            }
        }
    }

    // endregion
}
