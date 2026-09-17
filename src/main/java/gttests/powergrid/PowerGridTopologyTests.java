package gttests.powergrid;

import static gregtech.api.util.GTRecipeConstants.COIL_HEAT;

import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizons.horizonqa.api.GameTestAssertException;
import com.gtnewhorizons.horizonqa.api.GameTestHelper;
import com.gtnewhorizons.horizonqa.api.TestPos;
import com.gtnewhorizons.horizonqa.api.TickCallbackHandle;
import com.gtnewhorizons.horizonqa.api.annotation.GameTest;
import com.gtnewhorizons.horizonqa.api.annotation.GameTestHolder;
import com.gtnewhorizons.horizonqa.api.gt.Multiblock;

import gregtech.api.enums.GTValues;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.TierEU;
import gregtech.api.graphs.Node;
import gregtech.api.metatileentity.BaseMetaPipeEntity;
import gregtech.api.metatileentity.BaseMetaTileEntity;
import gregtech.api.metatileentity.implementations.MTECable;
import gregtech.api.metatileentity.implementations.MTEHatchEnergy;
import gregtech.api.metatileentity.implementations.MTEHatchInputBusDebug;
import gregtech.api.util.shutdown.ShutDownReasonRegistry;
import gregtech.common.items.IDMetaTool01;
import gregtech.common.items.MetaGeneratedTool01;
import tectech.thing.metaTileEntity.single.MTEDebugPowerGenerator;

@GameTestHolder(value = Mods.ModIDs.GREG_TECH, templatePrefix = "power_grid")
public final class PowerGridTopologyTests {

    private static final int DIRECT_WARMUP_TICKS = 40;
    private static final int CABLE_UPDATE_TICKS = 12;
    private static final int P2P_DELIVERY_TICKS = 25;

    private PowerGridTopologyTests() {}

    @GameTest(template = "line", timeoutTicks = 100, batch = "gt5.power_grid")
    public static void airFacingEndpointEditsKeepPowerFlowing(GameTestHelper helper) {
        configureGenerators(helper, "generator_a");
        initializeNetworks(helper, "generator_a");
        isolateProbe(helper, "probe_a");

        helper.startSequence()
            .thenWaitUntil(
                "line establishes power",
                DIRECT_WARMUP_TICKS,
                () -> assertPoweredAndClear(helper, "probe_a"))
            .thenExecute("add air-facing endpoint", () -> {
                clearProbe(helper, "probe_a");
                connect(helper, "edit_path", "edit_air_above_path");
            })
            .thenIdle(1)
            .thenExecute("power survives endpoint addition", () -> assertPoweredAndClear(helper, "probe_a"))
            .thenExecute("remove air-facing endpoint", () -> {
                clearProbe(helper, "probe_a");
                disconnect(helper, "edit_path", "edit_air_above_path");
            })
            .thenIdle(1)
            .thenExecute("power survives endpoint removal", () -> assertPoweredAndClear(helper, "probe_a"))
            .thenExecute("add degree-2 path endpoint", () -> {
                clearProbe(helper, "probe_a");
                connect(helper, "cable_end", ForgeDirection.UP);
            })
            .thenIdle(1)
            .thenExecute("power survives path endpoint addition", () -> assertPoweredAndClear(helper, "probe_a"))
            .thenExecute("remove degree-2 path endpoint", () -> {
                clearProbe(helper, "probe_a");
                disconnect(helper, "cable_end", ForgeDirection.UP);
            })
            .thenIdle(1)
            .thenExecute("power survives path endpoint removal", () -> assertPoweredAndClear(helper, "probe_a"))
            .thenIdle(CABLE_UPDATE_TICKS)
            .thenExecute("power survives debounced rebuild", () -> assertPoweredAndClear(helper, "probe_a"))
            .thenSucceed();
    }

    @GameTest(template = "branch", timeoutTicks = 100, batch = "gt5.power_grid")
    public static void unusedJunctionEditsKeepConsumersPowered(GameTestHelper helper) {
        configureGenerators(helper, "generator_a");
        initializeNetworks(helper, "generator_a");
        isolateProbe(helper, "probe_east", "probe_north", "probe_south");
        Node[] initialGraph = { null };

        helper.startSequence()
            .thenWaitUntil(
                "branch establishes power",
                DIRECT_WARMUP_TICKS,
                () -> assertPoweredAndClear(helper, "probe_east"))
            .thenIdle(CABLE_UPDATE_TICKS)
            .thenExecute("capture settled graph", () -> {
                initialGraph[0] = nodeMap(helper, "junction");
                helper.assertNotNull(initialGraph[0], "Branch graph did not initialize");
            })
            .thenExecute("remove unused junction branch", () -> {
                isolateProbe(helper, "probe_east", "probe_north", "probe_south");
                disconnect(helper, "junction", "dead_end_cable");
            })
            .thenIdle(1)
            .thenExecute("east consumer survives removal", () -> assertPoweredAndClear(helper, "probe_east"))
            .thenExecute(
                "prepare north consumer",
                () -> isolateProbe(helper, "probe_north", "probe_east", "probe_south"))
            .thenIdle(1)
            .thenExecute("north consumer survives removal", () -> assertPoweredAndClear(helper, "probe_north"))
            .thenExecute(
                "prepare south consumer",
                () -> isolateProbe(helper, "probe_south", "probe_east", "probe_north"))
            .thenIdle(1)
            .thenExecute("south consumer survives removal", () -> assertPoweredAndClear(helper, "probe_south"))
            .thenExecute(
                "unused branch removal does not rebuild graph",
                () -> helper.assertTrue(
                    initialGraph[0] == nodeMap(helper, "junction"),
                    "Removing an unused branch rebuilt the graph before the debounce"))
            .thenExecute("restore unused junction branch", () -> {
                isolateProbe(helper, "probe_east", "probe_north", "probe_south");
                connect(helper, "junction", "dead_end_cable");
            })
            .thenIdle(1)
            .thenExecute("east consumer survives restoration", () -> assertPoweredAndClear(helper, "probe_east"))
            .thenExecute(
                "prepare north consumer after restoration",
                () -> isolateProbe(helper, "probe_north", "probe_east", "probe_south"))
            .thenIdle(1)
            .thenExecute("north consumer survives restoration", () -> assertPoweredAndClear(helper, "probe_north"))
            .thenExecute(
                "prepare south consumer after restoration",
                () -> isolateProbe(helper, "probe_south", "probe_east", "probe_north"))
            .thenIdle(1)
            .thenExecute("south consumer survives restoration", () -> assertPoweredAndClear(helper, "probe_south"))
            .thenExecute(
                "prepare consumer after debounce",
                () -> isolateProbe(helper, "probe_east", "probe_north", "probe_south"))
            .thenIdle(CABLE_UPDATE_TICKS)
            .thenExecute("east consumer survives debounced rebuild", () -> {
                helper.assertTrue(
                    initialGraph[0] != nodeMap(helper, "junction"),
                    "Unused branch edits did not trigger the debounced graph rebuild");
                assertPoweredAndClear(helper, "probe_east");
            })
            .thenSucceed();
    }

    @GameTest(template = "branch", timeoutTicks = 80, batch = "gt5.power_grid")
    public static void disconnectedConsumerBranchIsIsolated(GameTestHelper helper) {
        configureGenerators(helper, "generator_a");
        initializeNetworks(helper, "generator_a");
        isolateProbe(helper, "probe_east", "probe_north", "probe_south");

        helper.startSequence()
            .thenWaitUntil(
                "branch establishes power",
                DIRECT_WARMUP_TICKS,
                () -> assertPoweredAndClear(helper, "probe_east"))
            .thenExecute("disconnect north consumer", () -> {
                clearProbe(helper, "probe_east");
                clearProbe(helper, "probe_north");
                fillProbe(helper, "probe_south");
                disconnect(helper, "junction", "probe_north");
            })
            .thenIdle(1)
            .thenExecute("unaffected branch remains powered during debounce", () -> {
                assertPoweredAndClear(helper, "probe_east");
                assertUnpowered(helper, "probe_north");
            })
            .thenIdle(CABLE_UPDATE_TICKS)
            .thenExecute("clear probes after debounced rebuild", () -> {
                clearProbe(helper, "probe_east");
                clearProbe(helper, "probe_north");
            })
            .thenIdle(1)
            .thenExecute("only north consumer loses power", () -> {
                assertPoweredAndClear(helper, "probe_east");
                assertUnpowered(helper, "probe_north");
            })
            .thenExecute("prepare south consumer", () -> {
                fillProbe(helper, "probe_east");
                clearProbe(helper, "probe_north");
                clearProbe(helper, "probe_south");
            })
            .thenIdle(1)
            .thenExecute("south consumer remains powered", () -> {
                assertPoweredAndClear(helper, "probe_south");
                assertUnpowered(helper, "probe_north");
            })
            .thenSucceed();
    }

    @GameTest(template = "dual_source_bridge", timeoutTicks = 80, batch = "gt5.power_grid")
    public static void disconnectedBridgeLeavesBothLocalNetworksPowered(GameTestHelper helper) {
        testBridgeSplit(helper, false);
    }

    @GameTest(template = "dual_source_bridge", timeoutTicks = 80, batch = "gt5.power_grid")
    public static void removedBridgeLeavesBothLocalNetworksPowered(GameTestHelper helper) {
        testBridgeSplit(helper, true);
    }

    @GameTest(template = "loop", timeoutTicks = 80, batch = "gt5.power_grid")
    public static void cutLoopEdgeUsesAlternateRoute(GameTestHelper helper) {
        configureGenerators(helper, "generator_a");
        initializeNetworks(helper, "generator_a");
        isolateProbe(helper, "probe_north", "probe_east");
        Node[] repairedGraph = { null };

        helper.startSequence()
            .thenWaitUntil(
                "loop establishes power",
                DIRECT_WARMUP_TICKS,
                () -> assertPoweredAndClear(helper, "probe_north"))
            .thenIdle(CABLE_UPDATE_TICKS)
            .thenExecute("cut loop edge", () -> {
                isolateProbe(helper, "probe_north", "probe_east");
                disconnect(helper, "loop_edge", ForgeDirection.EAST);
            })
            .thenIdle(1)
            .thenExecute("north consumer uses alternate route", () -> assertPoweredAndClear(helper, "probe_north"))
            .thenExecute("prepare east consumer", () -> isolateProbe(helper, "probe_east", "probe_north"))
            .thenIdle(1)
            .thenExecute("east consumer uses alternate route", () -> {
                assertPoweredAndClear(helper, "probe_east");
                repairedGraph[0] = nodeMap(helper, "loop_edge");
                helper.assertNotNull(repairedGraph[0], "Loop graph was not repaired after using the cut route");
            })
            .thenExecute(
                "prepare north consumer after debounce",
                () -> isolateProbe(helper, "probe_north", "probe_east"))
            .thenIdle(1)
            .thenExecute(
                "repaired graph survives the next transfer",
                () -> helper.assertTrue(
                    repairedGraph[0] == nodeMap(helper, "loop_edge"),
                    "The repaired loop graph was replaced on the next power transfer"))
            .thenIdle(CABLE_UPDATE_TICKS - 1)
            .thenExecute("skip redundant delayed rebuild", () -> {
                Node currentGraph = nodeMap(helper, "loop_edge");
                helper.assertTrue(
                    repairedGraph[0] == currentGraph,
                    "The repaired loop graph was rebuilt again after the debounce (old valid: " + repairedGraph[0]
                        .isNodeMapValid() + ", new valid: " + currentGraph.isNodeMapValid() + ")");
            })
            .thenExecute("clear north consumer after debounced rebuild", () -> clearProbe(helper, "probe_north"))
            .thenIdle(1)
            .thenExecute(
                "north consumer still uses alternate route",
                () -> assertPoweredAndClear(helper, "probe_north"))
            .thenSucceed();
    }

    @GameTest(template = "line", timeoutTicks = 80, batch = "gt5.power_grid")
    public static void reconnectingComponentsRestoresPower(GameTestHelper helper) {
        configureGenerators(helper, "generator_a");
        initializeNetworks(helper, "generator_a");
        isolateProbe(helper, "probe_a");

        helper.startSequence()
            .thenWaitUntil(
                "line establishes power",
                DIRECT_WARMUP_TICKS,
                () -> assertPoweredAndClear(helper, "probe_a"))
            .thenExecute("split line", () -> {
                clearProbe(helper, "probe_a");
                disconnect(helper, "edit_path", "cable_end");
            })
            .thenWaitUntil(
                "split consumer becomes unpowered",
                DIRECT_WARMUP_TICKS,
                () -> assertNoNewPowerAndClear(helper, "probe_a"))
            .thenExecute("reconnect line", () -> {
                clearProbe(helper, "probe_a");
                connect(helper, "edit_path", "cable_end");
            })
            .thenWaitUntil(
                "reconnected consumer is powered",
                DIRECT_WARMUP_TICKS,
                () -> assertPoweredAndClear(helper, "probe_a"))
            .thenSucceed();
    }

    @GameTest(template = "dual_source_bridge", timeoutTicks = 80, batch = "gt5.power_grid")
    public static void disconnectingOneGeneratorKeepsConsumersPowered(GameTestHelper helper) {
        configureGenerators(helper, "generator_a", "generator_b");
        initializeNetworks(helper, "generator_a");
        clearProbe(helper, "probe_a");
        clearProbe(helper, "probe_b");

        helper.startSequence()
            .thenWaitUntil("dual-source grid establishes power", DIRECT_WARMUP_TICKS, () -> {
                assertPowered(helper, "probe_a");
                assertPowered(helper, "probe_b");
                clearProbe(helper, "probe_a");
                clearProbe(helper, "probe_b");
            })
            .thenExecute("disconnect generator A", () -> {
                isolateProbe(helper, "probe_a", "probe_b");
                disconnect(helper, "junction_a", "generator_a");
            })
            .thenIdle(1)
            .thenExecute("remote consumer remains powered", () -> assertPoweredAndClear(helper, "probe_a"))
            .thenExecute("prepare local consumer", () -> isolateProbe(helper, "probe_b", "probe_a"))
            .thenIdle(1)
            .thenExecute("local consumer remains powered", () -> assertPoweredAndClear(helper, "probe_b"))
            .thenExecute("prepare consumers after debounce", () -> isolateProbe(helper, "probe_a", "probe_b"))
            .thenIdle(CABLE_UPDATE_TICKS)
            .thenExecute("clear remote consumer after debounced rebuild", () -> clearProbe(helper, "probe_a"))
            .thenIdle(1)
            .thenExecute(
                "remote consumer remains powered after rebuild",
                () -> assertPoweredAndClear(helper, "probe_a"))
            .thenSucceed();
    }

    @GameTest(template = "p2p_multi_output", timeoutTicks = 130, batch = "gt5.power_grid")
    public static void p2pInputDisconnectStopsDelivery(GameTestHelper helper) {
        configureGenerators(helper, "generator_a");
        initializeNetworks(helper, "generator_a", "probe_a", "probe_b");
        isolateProbe(helper, "probe_a", "probe_b");
        TickCallbackHandle noP2PDelivery = helper.onEachTickDisabled("disconnected P2P input delivers no EU", () -> {
            assertUnpowered(helper, "probe_a");
            assertUnpowered(helper, "probe_b");
        });

        helper.startSequence()
            .thenWaitUntil(
                "P2P establishes delivery",
                P2P_DELIVERY_TICKS,
                () -> assertPoweredAndClear(helper, "probe_a"))
            .thenExecute("disconnect P2P input", () -> {
                clearProbe(helper, "probe_a");
                clearProbe(helper, "probe_b");
                disconnect(helper, "input_cable", "p2p_input");
            })
            .thenIdle(CABLE_UPDATE_TICKS)
            .thenExecute("begin disconnected observation", () -> {
                clearProbe(helper, "probe_a");
                clearProbe(helper, "probe_b");
                noP2PDelivery.enable();
            })
            .thenIdle(P2P_DELIVERY_TICKS)
            .thenExecute("finish disconnected observation", noP2PDelivery::remove)
            .thenSucceed();
    }

    @GameTest(template = "p2p_multi_output", timeoutTicks = 110, batch = "gt5.power_grid")
    public static void reconnectingP2pInputRestoresDelivery(GameTestHelper helper) {
        configureGenerators(helper, "generator_a");
        initializeNetworks(helper, "generator_a", "probe_a", "probe_b");
        isolateProbe(helper, "probe_a", "probe_b");

        helper.startSequence()
            .thenWaitUntil(
                "P2P establishes delivery",
                P2P_DELIVERY_TICKS,
                () -> assertPoweredAndClear(helper, "probe_a"))
            .thenExecute("disconnect P2P input", () -> {
                clearProbe(helper, "probe_a");
                clearProbe(helper, "probe_b");
                disconnect(helper, "input_cable", "p2p_input");
            })
            .thenIdle(P2P_DELIVERY_TICKS)
            .thenExecute("reconnect P2P input", () -> {
                isolateProbe(helper, "probe_a", "probe_b");
                connect(helper, "input_cable", "p2p_input");
            })
            .thenWaitUntil("P2P delivery resumes", P2P_DELIVERY_TICKS, () -> assertPoweredAndClear(helper, "probe_a"))
            .thenSucceed();
    }

    @GameTest(template = "p2p_multi_output", timeoutTicks = 110, batch = "gt5.power_grid")
    public static void disconnectedP2pOutputDoesNotAffectOtherOutput(GameTestHelper helper) {
        configureGenerators(helper, "generator_a");
        initializeNetworks(helper, "generator_a", "probe_a", "probe_b");
        isolateProbe(helper, "probe_b", "probe_a");
        TickCallbackHandle outputAIsolated = helper
            .onEachTickDisabled("disconnected P2P output A stays empty", () -> assertUnpowered(helper, "probe_a"));

        helper.startSequence()
            .thenWaitUntil(
                "P2P output B establishes delivery",
                P2P_DELIVERY_TICKS,
                () -> assertPoweredAndClear(helper, "probe_b"))
            .thenExecute("disconnect P2P output A", () -> {
                clearProbe(helper, "probe_a");
                clearProbe(helper, "probe_b");
                disconnect(helper, "output_cable_a", "p2p_output_a");
            })
            .thenIdle(CABLE_UPDATE_TICKS)
            .thenExecute("begin output isolation observation", () -> {
                clearProbe(helper, "probe_a");
                clearProbe(helper, "probe_b");
                outputAIsolated.enable();
            })
            .thenWaitUntil(
                "P2P output B keeps delivering",
                P2P_DELIVERY_TICKS,
                () -> assertPoweredAndClear(helper, "probe_b"))
            .thenExecute("finish output isolation observation", outputAIsolated::remove)
            .thenSucceed();
    }

    @GameTest(template = "p2p_multi_output", timeoutTicks = 110, batch = "gt5.power_grid")
    public static void p2pFedEndpointEditsKeepPowerFlowing(GameTestHelper helper) {
        configureGenerators(helper, "generator_a");
        initializeNetworks(helper, "generator_a", "probe_a", "probe_b");
        isolateProbe(helper, "probe_a", "probe_b");

        helper.startSequence()
            .thenWaitUntil(
                "P2P establishes delivery",
                P2P_DELIVERY_TICKS,
                () -> assertPoweredAndClear(helper, "probe_a"))
            .thenExecute("add P2P-fed endpoint", () -> {
                isolateProbe(helper, "probe_a", "probe_b");
                connect(helper, "output_cable_a", ForgeDirection.EAST);
            })
            .thenWaitUntil(
                "P2P delivery survives endpoint addition",
                P2P_DELIVERY_TICKS,
                () -> assertPoweredAndClear(helper, "probe_a"))
            .thenExecute("remove P2P-fed endpoint", () -> {
                isolateProbe(helper, "probe_a", "probe_b");
                disconnect(helper, "output_cable_a", ForgeDirection.EAST);
            })
            .thenWaitUntil(
                "P2P delivery survives endpoint removal",
                P2P_DELIVERY_TICKS,
                () -> assertPoweredAndClear(helper, "probe_a"))
            .thenIdle(CABLE_UPDATE_TICKS)
            .thenExecute("clear P2P-fed consumer after debounce", () -> clearProbe(helper, "probe_a"))
            .thenWaitUntil(
                "P2P delivery survives debounced rebuild",
                P2P_DELIVERY_TICKS,
                () -> assertPoweredAndClear(helper, "probe_a"))
            .thenSucceed();
    }

    @GameTest(template = "ebf_cable_edit", timeoutTicks = 240, batch = "gt5.power_grid")
    public static void runningEbfSurvivesCableConnectionEdit(GameTestHelper helper) {
        TestPos controller = helper.pos("controller");
        Multiblock ebf = helper.gtnh()
            .multiblock(controller);
        ebf.fixMaintenance();
        ebf.assertFormed();

        ItemStack input = new ItemStack(Blocks.bedrock, 1);
        helper.gtnh()
            .withTestRecipe(
                ebf,
                GTValues.RA.stdBuilder()
                    .duration(600)
                    .eut(TierEU.RECIPE_EV)
                    .metadata(COIL_HEAT, 1_200)
                    .itemInputs(input.copy())
                    .itemOutputs(new ItemStack(Blocks.obsidian, 1)));
        MTEHatchInputBusDebug inputBus = (MTEHatchInputBusDebug) helper.gtnh()
            .metaTileEntity(helper.pos("debug_input_bus"));
        BaseMetaTileEntity generator = (BaseMetaTileEntity) helper.gtnh()
            .gtTile(helper.pos("debug_generator"));
        generator.enableWorking();

        MTECable editCable = cable(helper, "edit_cable");
        generator.generatePowerNodes();
        helper.assertNotNull(
            ((BaseMetaPipeEntity) editCable.getBaseMetaTileEntity()).getNode(),
            "Cable power graph did not initialize");
        FakePlayer player = helper.spawnFakePlayer("ebf-cable-edit");
        player.capabilities.isCreativeMode = true;
        ItemStack wireCutter = MetaGeneratedTool01.INSTANCE
            .getToolWithStats(IDMetaTool01.WIRECUTTER.ID, 1, Materials.Steel, Materials.Steel, null);
        int[] progressAtEdit = { 0 };
        int[] previousProgress = { 0 };
        TickCallbackHandle noPowerFailure = helper.onEachTickDisabled("EBF remains powered after cable edit", () -> {
            helper.assertNotEquals(
                ShutDownReasonRegistry.POWER_LOSS.getKey(),
                helper.gtnh()
                    .gtTile(controller)
                    .getLastShutDownReason()
                    .getKey(),
                "Cable connection edit caused a power-loss shutdown");
            helper.assertTrue(ebf.isProcessing(), "EBF stopped processing after the cable connection edit");
            int progress = ebf.progress();
            helper.assertTrue(progress >= previousProgress[0], "EBF recipe progress reset after the cable edit");
            previousProgress[0] = progress;
        });

        helper.startSequence()
            .thenExecute("stock bedrock input", () -> inputBus.phantomHolder.setStackInSlot(0, input))
            .thenWaitUntil(
                "long EV recipe starts",
                100,
                () -> helper.assertTrue(ebf.isProcessing(), "EBF did not start the synthetic EV recipe"))
            .thenExecute("add unused cable connection", () -> {
                progressAtEdit[0] = ebf.progress();
                previousProgress[0] = progressAtEdit[0];
                helper
                    .assertFalse(editCable.isConnectedAtSide(ForgeDirection.DOWN), "Cable already connected downward");
                helper.assertTrue(
                    helper.simulateRightClick("edit_cable", player, wireCutter),
                    "Wire-cutter click was not handled");
                helper.assertTrue(
                    editCable.isConnectedAtSide(ForgeDirection.DOWN),
                    "Wire-cutter click did not add the downward connection");
                noPowerFailure.enable();
            })
            .thenIdle(80)
            .thenExecute("verify uninterrupted EBF progress", () -> {
                noPowerFailure.remove();
                helper.assertTrue(ebf.progress() > progressAtEdit[0], "EBF made no progress after the cable edit");
                ebf.assertNoExplosion();
            })
            .thenSucceed();
    }

    private static void testBridgeSplit(GameTestHelper helper, boolean removeBridge) {
        configureGenerators(helper, "generator_a", "generator_b");
        initializeNetworks(helper, "generator_a");
        clearProbe(helper, "probe_a");
        clearProbe(helper, "probe_b");

        helper.startSequence()
            .thenWaitUntil("dual-source grid establishes power", DIRECT_WARMUP_TICKS, () -> {
                assertPowered(helper, "probe_a");
                assertPowered(helper, "probe_b");
                clearProbe(helper, "probe_a");
                clearProbe(helper, "probe_b");
            })
            .thenExecute(removeBridge ? "remove bridge cable" : "disconnect bridge cable", () -> {
                clearProbe(helper, "probe_a");
                clearProbe(helper, "probe_b");
                if (removeBridge) {
                    helper.destroyBlock("bridge");
                } else {
                    disconnect(helper, "bridge", "junction_b");
                }
            })
            .thenIdle(1)
            .thenExecute("both local networks remain powered during debounce", () -> {
                assertPoweredAndClear(helper, "probe_a");
                assertPoweredAndClear(helper, "probe_b");
            })
            .thenIdle(CABLE_UPDATE_TICKS)
            .thenExecute("clear probes after debounced rebuild", () -> {
                clearProbe(helper, "probe_a");
                clearProbe(helper, "probe_b");
            })
            .thenIdle(1)
            .thenExecute("both local networks remain powered after rebuild", () -> {
                assertPoweredAndClear(helper, "probe_a");
                assertPoweredAndClear(helper, "probe_b");
            })
            .thenSucceed();
    }

    private static void configureGenerators(GameTestHelper helper, String... labels) {
        for (String label : labels) {
            Object metaTileEntity = helper.gtnh()
                .metaTileEntity(helper.pos(label));
            helper.assertTrue(
                metaTileEntity instanceof MTEDebugPowerGenerator,
                "Expected " + label + " to be a debug power generator");
            MTEDebugPowerGenerator generator = (MTEDebugPowerGenerator) metaTileEntity;
            generator.setProducing(true);
            generator.setLASER(false);
            generator.setUsingTiers(true);
            generator.setVoltageTier((byte) 1);
            generator.setAmperage(1);
            generator.getBaseMetaTileEntity()
                .enableWorking();
        }
    }

    private static void initializeNetworks(GameTestHelper helper, String... machineLabels) {
        for (String label : machineLabels) {
            helper.assertTileEntityPresent(BaseMetaTileEntity.class, label)
                .generatePowerNodes();
        }
    }

    private static void isolateProbe(GameTestHelper helper, String target, String... otherProbes) {
        clearProbe(helper, target);
        for (String other : otherProbes) {
            fillProbe(helper, other);
        }
    }

    private static void clearProbe(GameTestHelper helper, String label) {
        BaseMetaTileEntity probe = probe(helper, label);
        helper.assertTrue(probe.setStoredEU(0), "Could not empty energy probe " + label);
    }

    private static void fillProbe(GameTestHelper helper, String label) {
        BaseMetaTileEntity probe = probe(helper, label);
        helper.assertTrue(probe.setStoredEU(probe.getEUCapacity()), "Could not fill energy probe " + label);
    }

    private static void assertPowered(GameTestHelper helper, String label) {
        long stored = probe(helper, label).getStoredEU();
        if (stored <= 0) {
            throw new GameTestAssertException(
                "Expected energy probe " + label + " to receive EU, but it stored " + stored,
                helper.absolute(label));
        }
    }

    private static void assertPoweredAndClear(GameTestHelper helper, String label) {
        assertPowered(helper, label);
        clearProbe(helper, label);
    }

    private static void assertUnpowered(GameTestHelper helper, String label) {
        long stored = probe(helper, label).getStoredEU();
        if (stored != 0) {
            throw new GameTestAssertException(
                "Expected energy probe " + label + " to stay empty, but it stored " + stored,
                helper.absolute(label));
        }
    }

    private static void assertNoNewPowerAndClear(GameTestHelper helper, String label) {
        long stored = probe(helper, label).getStoredEU();
        clearProbe(helper, label);
        if (stored != 0) {
            throw new GameTestAssertException(
                "Expected energy probe " + label + " to stop receiving EU, but it stored " + stored,
                helper.absolute(label));
        }
    }

    private static BaseMetaTileEntity probe(GameTestHelper helper, String label) {
        BaseMetaTileEntity probe = helper.assertTileEntityPresent(BaseMetaTileEntity.class, label);
        helper.assertTrue(
            probe.getMetaTileEntity() instanceof MTEHatchEnergy,
            "Expected " + label + " to be a regular energy hatch");
        return probe;
    }

    private static void connect(GameTestHelper helper, String cableLabel, String targetLabel) {
        connect(helper, cableLabel, direction(helper.pos(cableLabel), helper.pos(targetLabel)));
    }

    private static void connect(GameTestHelper helper, String cableLabel, ForgeDirection side) {
        MTECable cable = cable(helper, cableLabel);
        helper.assertEquals(1, cable.connect(side), "Could not connect " + cableLabel + " toward " + side);
        helper.assertTrue(cable.isConnectedAtSide(side), cableLabel + " did not retain its " + side + " connection");
    }

    private static void disconnect(GameTestHelper helper, String cableLabel, String targetLabel) {
        disconnect(helper, cableLabel, direction(helper.pos(cableLabel), helper.pos(targetLabel)));
    }

    private static void disconnect(GameTestHelper helper, String cableLabel, ForgeDirection side) {
        MTECable cable = cable(helper, cableLabel);
        cable.disconnect(side);
        helper.assertFalse(cable.isConnectedAtSide(side), cableLabel + " retained its " + side + " connection");
    }

    private static MTECable cable(GameTestHelper helper, String label) {
        Object metaTileEntity = helper.gtnh()
            .metaTileEntity(helper.pos(label));
        helper.assertTrue(metaTileEntity instanceof MTECable, "Expected " + label + " to be an energy cable");
        return (MTECable) metaTileEntity;
    }

    private static Node nodeMap(GameTestHelper helper, String label) {
        return ((BaseMetaPipeEntity) cable(helper, label).getBaseMetaTileEntity()).getNodeMap();
    }

    private static ForgeDirection direction(TestPos from, TestPos to) {
        int dx = to.x() - from.x();
        int dy = to.y() - from.y();
        int dz = to.z() - from.z();
        for (ForgeDirection direction : ForgeDirection.VALID_DIRECTIONS) {
            if (direction.offsetX == Integer.signum(dx) && direction.offsetY == Integer.signum(dy)
                && direction.offsetZ == Integer.signum(dz)
                && Integer.signum(dx) * Integer.signum(dy) == 0
                && Integer.signum(dx) * Integer.signum(dz) == 0
                && Integer.signum(dy) * Integer.signum(dz) == 0) {
                return direction;
            }
        }
        throw new IllegalArgumentException("Positions are not axis-aligned: " + from + " and " + to);
    }
}
