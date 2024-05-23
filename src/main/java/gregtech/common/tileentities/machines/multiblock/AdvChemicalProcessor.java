package gregtech.common.tileentities.machines.multiblock;

import static com.google.common.primitives.Ints.saturatedCast;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.multitileentity.enums.PartMode.ENERGY_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.FLUID_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.FLUID_OUTPUT;
import static gregtech.api.multitileentity.enums.PartMode.ITEM_INPUT;
import static gregtech.api.multitileentity.enums.PartMode.ITEM_OUTPUT;
import static gregtech.api.multitileentity.enums.PartMode.NOTHING;
import static gregtech.api.util.GT_StructureUtilityMuTE.MOTOR_CASINGS;
import static gregtech.api.util.GT_StructureUtilityMuTE.ofMuTECasings;

import java.util.ArrayList;
import java.util.HashSet;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidTank;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;
import com.gtnewhorizons.modularui.api.forge.ItemStackHandler;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.math.Color;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.api.widget.IWidgetBuilder;
import com.gtnewhorizons.modularui.common.widget.ButtonWidget;
import com.gtnewhorizons.modularui.common.widget.MultiChildWidget;
import com.gtnewhorizons.modularui.common.widget.SlotGroup;
import com.gtnewhorizons.modularui.common.widget.TextWidget;
import com.gtnewhorizons.modularui.common.widget.textfield.NumericWidget;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Materials;
import gregtech.api.fluid.FluidTankGT;
import gregtech.api.gui.modularui.GT_UITextures;
import gregtech.api.multitileentity.enums.GT_MultiTileCasing;
import gregtech.api.multitileentity.multiblock.base.ComplexParallelController;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_StructureUtility;
import gregtech.api.util.GT_StructureUtilityMuTE;
import gregtech.common.tileentities.machines.multiblock.logic.AdvChemicalProcessorProcessingLogic;

public class AdvChemicalProcessor
    extends ComplexParallelController<AdvChemicalProcessor, AdvChemicalProcessorProcessingLogic> {

    private static IStructureDefinition<AdvChemicalProcessor> STRUCTURE_DEFINITION = null;
    protected static final String STRUCTURE_PIECE_T1 = "T1";
    protected static final String STRUCTURE_PIECE_T2 = "T2";
    protected static final String STRUCTURE_PIECE_T3 = "T3";
    protected static final String STRUCTURE_PIECE_T4 = "T4";
    protected static final String STRUCTURE_PIECE_T5_6 = "T5_6";
    protected static final String STRUCTURE_PIECE_T7_8 = "T7_8";
    protected static final Vec3Impl STRUCTURE_OFFSET_T1 = new Vec3Impl(3, 1, 0);
    protected static final Vec3Impl STRUCTURE_OFFSET_T2 = new Vec3Impl(1, 4, -3);
    protected static final Vec3Impl STRUCTURE_OFFSET_T3 = new Vec3Impl(8, 0, 5);
    protected static final Vec3Impl STRUCTURE_OFFSET_T4 = new Vec3Impl(-14, 0, 0);
    protected static final Vec3Impl STRUCTURE_OFFSET_T5 = new Vec3Impl(14, 0, -6);
    protected static final Vec3Impl STRUCTURE_OFFSET_T6 = new Vec3Impl(-16, 0, 0);
    protected static final Vec3Impl STRUCTURE_OFFSET_T7 = new Vec3Impl(16, 0, 15);
    protected static final Vec3Impl STRUCTURE_OFFSET_T8 = new Vec3Impl(-16, 0, 0);
    protected static final int PROCESS_WINDOW_BASE_ID = 100;
    protected static final int ITEM_WHITELIST_SLOTS = 8;
    protected static final int FLUID_WHITELIST_SLOTS = 8;
    protected static final int MAX_PROCESSES = 8;
    protected HeatingCoilLevel coilTier;
    protected final ArrayList<HashSet<String>> processWhitelists = new ArrayList<>(MAX_PROCESSES);
    protected final ArrayList<ItemStackHandler> processWhitelistInventoryHandlers = new ArrayList<>(MAX_PROCESSES);
    protected final ArrayList<ArrayList<IFluidTank>> processFluidWhiteLists = new ArrayList<>(MAX_PROCESSES);
    protected boolean wasWhitelistOpened = false;

    public AdvChemicalProcessor() {
        super();
        for (int i = 0; i < MAX_PROCESSES; i++) {
            processWhitelists.add(null);
            processWhitelistInventoryHandlers.add(new ItemStackHandler(ITEM_WHITELIST_SLOTS));
            ArrayList<IFluidTank> processFluidTanks = new ArrayList<>(FLUID_WHITELIST_SLOTS);
            for (int j = 0; j < FLUID_WHITELIST_SLOTS; j++) {
                processFluidTanks.add(new FluidTankGT());
            }
            processFluidWhiteLists.add(processFluidTanks);
        }
        setMaxComplexParallels(1, false);
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        super.readFromNBT(nbt);
        setMaxComplexParallels(nbt.getInteger("processors"), false);
        final NBTTagCompound processWhiteLists = nbt.getCompoundTag("whiteLists");
        long capacity = 1000;
        if (nbt.hasKey(GT_Values.NBT.TANK_CAPACITY)) {
            capacity = saturatedCast(nbt.getLong(GT_Values.NBT.TANK_CAPACITY));
        }
        for (int i = 0; i < MAX_PROCESSES; i++) {

            if (processWhiteLists == null) {
                continue;
            }

            final NBTTagCompound itemList = processWhiteLists.getCompoundTag("items" + i);
            if (itemList != null) {
                processWhitelistInventoryHandlers.get(i)
                    .deserializeNBT(itemList);
            }
            final NBTTagList fluidList = processWhiteLists.getTagList("fluids" + i, Constants.NBT.TAG_COMPOUND);

            if (fluidList == null) {
                continue;
            }

            for (int j = 0; j < fluidList.tagCount(); j++) {
                final NBTTagCompound fluid = fluidList.getCompoundTagAt(j);

                if (fluid == null) {
                    continue;
                }

                short index = fluid.getShort("s");
                FluidStack fluidStack = FluidStack.loadFluidStackFromNBT(fluid);
                if (fluidStack != null) {
                    processFluidWhiteLists.get(i)
                        .get(index)
                        .fill(fluidStack, true);
                }
            }
        }
    }

    @Override
    public void writeToNBT(NBTTagCompound nbt) {
        super.writeToNBT(nbt);
        nbt.setInteger("processors", maxComplexParallels);
        final NBTTagCompound processWhiteLists = new NBTTagCompound();
        for (int i = 0; i < MAX_PROCESSES; i++) {
            processWhiteLists.setTag(
                "items" + i,
                processWhitelistInventoryHandlers.get(i)
                    .serializeNBT());
            final NBTTagList fluidList = new NBTTagList();
            for (int j = 0; j < FLUID_WHITELIST_SLOTS; j++) {
                final FluidStack fluidStack = processFluidWhiteLists.get(i)
                    .get(j)
                    .getFluid();
                if (fluidStack != null) {
                    final NBTTagCompound tag = new NBTTagCompound();
                    tag.setByte("s", (byte) j);
                    fluidStack.writeToNBT(tag);
                    fluidList.appendTag(tag);
                }
            }
            processWhiteLists.setTag("fluids" + i, fluidList);
        }
        nbt.setTag("whiteLists", processWhiteLists);
    }

    @Override
    public short getCasingRegistryID() {
        return GT_MultiTileCasing.Chemical.getRegistryId();
    }

    @Override
    public int getCasingMeta() {
        return GT_MultiTileCasing.Chemical.getId();
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Chemical Reactor")
            .addInfo("Controller block for the Advanced Chemical Processor")
            .addInfo("Does not lose efficiency when overclocked")
            .addInfo("Accepts fluids instead of fluid cells")
            .addInfo("Can do multiple different recipes at once")
            .addInfo("By using the whitelist filter a recipe can push its output")
            .addInfo("to a different recipes input to chain them")
            .addInfo("Disclaimer: Still WIP - Use at your own risk")
            .addInfo(GT_Values.Authorminecraft7771)
            .addSeparator()
            .beginStructureBlock(5, 3, 3, false)
            .addController("Front center")
            .addCasingInfoExactly("PTFE Pipe Machine Casing", 8, false)
            .addCasingInfoExactly("Heating Coils", 3, true)
            .addCasingInfoExactly("EV+ Glass", 3, true)
            .addCasingInfoExactly("Motor Casing", 3, true)
            .addCasingInfoExactly("Chemical Casing", 27, false)
            .toolTipFinisher("Gregtech");
        return tt;
    }

    @Override
    public Vec3Impl getStartingStructureOffset() {
        return STRUCTURE_OFFSET_T1;
    }

    @Override
    public boolean checkMachine() {
        setCoilTier(HeatingCoilLevel.None);
        buildState.startBuilding(getStartingStructureOffset());
        if (!checkPiece(STRUCTURE_PIECE_T1, buildState.getCurrentOffset())) return buildState.failBuilding();
        if (maxComplexParallels > 1) {
            buildState.addOffset(STRUCTURE_OFFSET_T2);
            if (!checkPiece(STRUCTURE_PIECE_T2, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        if (maxComplexParallels > 2) {
            buildState.addOffset(STRUCTURE_OFFSET_T3);
            if (!checkPiece(STRUCTURE_PIECE_T3, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        if (maxComplexParallels > 3) {
            buildState.addOffset(STRUCTURE_OFFSET_T4);
            if (!checkPiece(STRUCTURE_PIECE_T4, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        if (maxComplexParallels > 4) {
            buildState.addOffset(STRUCTURE_OFFSET_T5);
            if (!checkPiece(STRUCTURE_PIECE_T5_6, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        if (maxComplexParallels > 5) {
            buildState.addOffset(STRUCTURE_OFFSET_T6);
            if (!checkPiece(STRUCTURE_PIECE_T5_6, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        if (maxComplexParallels > 6) {
            buildState.addOffset(STRUCTURE_OFFSET_T7);
            if (!checkPiece(STRUCTURE_PIECE_T7_8, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        if (maxComplexParallels > 7) {
            buildState.addOffset(STRUCTURE_OFFSET_T8);
            if (!checkPiece(STRUCTURE_PIECE_T7_8, buildState.getCurrentOffset())) return buildState.failBuilding();
        }
        buildState.stopBuilding();
        return super.checkMachine();
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        buildState.startBuilding(getStartingStructureOffset());
        buildPiece(STRUCTURE_PIECE_T1, trigger, hintsOnly, buildState.getCurrentOffset());
        if (maxComplexParallels > 1) {
            buildState.addOffset(STRUCTURE_OFFSET_T2);
            buildPiece(STRUCTURE_PIECE_T2, trigger, hintsOnly, buildState.getCurrentOffset());
        }
        if (maxComplexParallels > 2) {
            buildState.addOffset(STRUCTURE_OFFSET_T3);
            buildPiece(STRUCTURE_PIECE_T3, trigger, hintsOnly, buildState.getCurrentOffset());
        }
        if (maxComplexParallels > 3) {
            buildState.addOffset(STRUCTURE_OFFSET_T4);
            buildPiece(STRUCTURE_PIECE_T4, trigger, hintsOnly, buildState.getCurrentOffset());
        }
        if (maxComplexParallels > 4) {
            buildState.addOffset(STRUCTURE_OFFSET_T5);
            buildPiece(STRUCTURE_PIECE_T5_6, trigger, hintsOnly, buildState.getCurrentOffset());
        }
        if (maxComplexParallels > 5) {
            buildState.addOffset(STRUCTURE_OFFSET_T6);
            buildPiece(STRUCTURE_PIECE_T5_6, trigger, hintsOnly, buildState.getCurrentOffset());
        }
        if (maxComplexParallels > 6) {
            buildState.addOffset(STRUCTURE_OFFSET_T7);
            buildPiece(STRUCTURE_PIECE_T7_8, trigger, hintsOnly, buildState.getCurrentOffset());
        }
        if (maxComplexParallels > 7) {
            buildState.addOffset(STRUCTURE_OFFSET_T8);
            buildPiece(STRUCTURE_PIECE_T7_8, trigger, hintsOnly, buildState.getCurrentOffset());
        }

        buildState.stopBuilding();
    }

    @Override
    public IStructureDefinition<AdvChemicalProcessor> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<AdvChemicalProcessor>builder()
                .addShape(
                    STRUCTURE_PIECE_T1,
                    transpose(
                        new String[][] { { "CPCPC", "CCCCC", "CPCPC" }, { "CGC~C", "GWWWU", "CGCCC" },
                            { "CPCPC", "CTTTC", "CPCPC" } }))
                .addShape(
                    STRUCTURE_PIECE_T2,
                    new String[][] { { "       ", "       ", "       ", "       ", "       ", "  F F  ", "  B B  " },
                        { "       ", "       ", "       ", "       ", "       ", "  F F  ", "  B B  " },
                        { "       ", "       ", "       ", "       ", "       ", "  F F  ", "  BBB  " },
                        { "   C   ", "  CGC  ", "  CGC  ", "  CGC  ", "  CGC  ", "  CGC  ", " BCCCB " },
                        { "  BBB  ", " C   C ", " C   C ", " C   C ", " C   C ", " C   C ", "BCPPPCB" },
                        { " CBBBC ", " G W G ", " G W G ", " G W G ", " G W G ", " G W G ", "BCPCPCB" },
                        { "  BBB  ", " C   C ", " C   C ", " C   C ", " C   C ", " C   C ", "BCPPPCB" },
                        { "   C   ", "  CGC  ", "  CGC  ", "  CGC  ", "  CGC  ", "  CGC  ", " BCCCB " },
                        { "       ", "       ", "       ", "       ", "       ", "       ", "  BBB  " } })
                .addShape(
                    STRUCTURE_PIECE_T3,
                    new String[][] {
                        { "         ", "         ", "         ", "         ", "         ", "         ", "  BBB    " },
                        { "   C     ", "  CGC    ", "  CGC    ", "  CGC    ", "  CGC    ", "  CGC    ", " BCCCB   " },
                        { "  BBB    ", " C   C   ", " C   C   ", " C   C   ", " C   C   ", " C   CFFF", "BCPPPCBBB" },
                        { " CBBBC   ", " G W G   ", " G W G   ", " G W G   ", " G W G   ", " G W G   ", "BCPCPCB  " },
                        { "  BBB    ", " C   C   ", " C   C   ", " C   C   ", " C   C   ", " C   CFFF", "BCPPPCBBB" },
                        { "   C     ", "  CGC    ", "  CGC    ", "  CGC    ", "  CGC    ", "  CGC    ", " BCCCB   " },
                        { "         ", "         ", "         ", "         ", "         ", "         ", "  BBB    " } })
                .addShape(
                    STRUCTURE_PIECE_T4,
                    new String[][] {
                        { "         ", "         ", "         ", "         ", "         ", "         ", "    BBB  " },
                        { "     C   ", "    CGC  ", "    CGC  ", "    CGC  ", "    CGC  ", "    CGC  ", "   BCCCB " },
                        { "    BBB  ", "   C   C ", "   C   C ", "   C   C ", "   C   C ", "FFFC   C ", "BBBCPPPCB" },
                        { "   CBBBC ", "   G W G ", "   G W G ", "   G W G ", "   G W G ", "   G W G ", "  BCPCPCB" },
                        { "    BBB  ", "   C   C ", "   C   C ", "   C   C ", "   C   C ", "FFFC   C ", "BBBCPPPCB" },
                        { "     C   ", "    CGC  ", "    CGC  ", "    CGC  ", "    CGC  ", "    CGC  ", "   BCCCB " },
                        { "         ", "         ", "         ", "         ", "         ", "         ", "    BBB  " } })
                .addShape(
                    STRUCTURE_PIECE_T5_6,
                    new String[][] { { "       ", "       ", "       ", "       ", "       ", "  F F  ", "       " },
                        { "       ", "       ", "       ", "       ", "       ", "  F F  ", "  B B  " },
                        { "       ", "       ", "       ", "       ", "       ", "  F F  ", "  B B  " },
                        { "       ", "       ", "       ", "       ", "       ", "  F F  ", "  BBB  " },
                        { "   C   ", "  CGC  ", "  CGC  ", "  CGC  ", "  CGC  ", "  CGC  ", " BCCCB " },
                        { "  BBB  ", " C   C ", " C   C ", " C   C ", " C   C ", " C   C ", "BCPPPCB" },
                        { " CBBBC ", " G W G ", " G W G ", " G W G ", " G W G ", " G W G ", "BCPCPCB" },
                        { "  BBB  ", " C   C ", " C   C ", " C   C ", " C   C ", " C   C ", "BCPPPCB" },
                        { "   C   ", "  CGC  ", "  CGC  ", "  CGC  ", "  CGC  ", "  CGC  ", " BCCCB " },
                        { "       ", "       ", "       ", "       ", "       ", "       ", "  BBB  " } })
                .addShape(
                    STRUCTURE_PIECE_T7_8,
                    new String[][] { { "       ", "       ", "       ", "       ", "       ", "       ", "  BBB  " },
                        { "   C   ", "  CGC  ", "  CGC  ", "  CGC  ", "  CGC  ", "  CGC  ", " BCCCB " },
                        { "  BBB  ", " C   C ", " C   C ", " C   C ", " C   C ", " C   C ", "BCPPPCB" },
                        { " CBBBC ", " G W G ", " G W G ", " G W G ", " G W G ", " G W G ", "BCPCPCB" },
                        { "  BBB  ", " C   C ", " C   C ", " C   C ", " C   C ", " C   C ", "BCPPPCB" },
                        { "   C   ", "  CGC  ", "  CGC  ", "  CGC  ", "  CGC  ", "  CGC  ", " BCCCB " },
                        { "       ", "       ", "       ", "       ", "       ", "  F F  ", "  BBB  " },
                        { "       ", "       ", "       ", "       ", "       ", "  F F  ", "  B B  " },
                        { "       ", "       ", "       ", "       ", "       ", "  F F  ", "  B B  " },
                        { "       ", "       ", "       ", "       ", "       ", "  F F  ", "       " } })
                .addElement(
                    'C',
                    ofMuTECasings(
                        FLUID_INPUT.getValue() | ITEM_INPUT.getValue()
                            | FLUID_OUTPUT.getValue()
                            | ITEM_OUTPUT.getValue()
                            | ENERGY_INPUT.getValue(),
                        GT_MultiTileCasing.Chemical.getCasing()))
                .addElement('P', ofBlock(GregTech_API.sBlockCasings8, 1))
                .addElement('T', ofMuTECasings(NOTHING.getValue(), MOTOR_CASINGS))
                .addElement(
                    'W',
                    GT_StructureUtility.ofCoil(AdvChemicalProcessor::setCoilTier, AdvChemicalProcessor::getCoilTier))
                .addElement('G', Glasses.chainAllGlasses())
                .addElement('B', ofBlock(GregTech_API.sBlockCasings4, 1))
                .addElement('F', GT_StructureUtility.ofFrame(Materials.Steel))
                .addElement(
                    'U',
                    ofMuTECasings(
                        FLUID_INPUT.getValue() | ITEM_INPUT.getValue()
                            | FLUID_OUTPUT.getValue()
                            | ITEM_OUTPUT.getValue()
                            | ENERGY_INPUT.getValue(),
                        GT_MultiTileCasing.Chemical.getCasing(),
                        GT_StructureUtilityMuTE.INVENTORY_CASINGS))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected void setMaxComplexParallels(int parallel, boolean stopMachine) {
        super.setMaxComplexParallels(parallel, stopMachine);
        onStructureChange();
    }

    protected MultiChildWidget createMainPage(IWidgetBuilder<?> builder) {
        MultiChildWidget child = new MultiChildWidget();
        for (int i = 0; i < MAX_PROCESSES; i++) {
            final int processIndex = i;
            child.addChild(
                new ButtonWidget().setPlayClickSound(true)
                    .setOnClick(
                        (clickData, widget) -> {
                            if (!widget.isClient()) widget.getContext()
                                .openSyncedWindow(PROCESS_WINDOW_BASE_ID + processIndex);
                        })
                    .setBackground(GT_UITextures.BUTTON_STANDARD, GT_UITextures.OVERLAY_BUTTON_WHITELIST)
                    .setSize(18, 18)
                    .setEnabled((widget -> processIndex < maxComplexParallels))
                    .setPos(20 * (i % 4) + 18, 18 + (i / 4) * 20));
        }
        child.addChild(
            new NumericWidget().setGetter(() -> maxComplexParallels)
                .setSetter(parallel -> setMaxComplexParallels((int) parallel, true))
                .setBounds(1, MAX_PROCESSES)
                .setTextColor(Color.WHITE.normal)
                .setTextAlignment(Alignment.Center)
                .addTooltip("Tier")
                .setBackground(GT_UITextures.BACKGROUND_TEXT_FIELD)
                .setSize(18, 18)
                .setPos(130, 85));
        return child;
    }

    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        for (int i = 0; i < MAX_PROCESSES; i++) {
            final int processIndex = i;
            buildContext.addSyncedWindow(
                PROCESS_WINDOW_BASE_ID + i,
                (player) -> createProcessConfigWindow(player, processIndex));
        }
        buildContext.addCloseListener(() -> {
            // Reset HashSet, we will let it re-generate on next item output
            if (wasWhitelistOpened) {
                for (int i = 0; i < MAX_PROCESSES; i++) {
                    processWhitelists.set(i, null);
                }
                wasWhitelistOpened = false;
            }
        });
    }

    protected ModularWindow createProcessConfigWindow(final EntityPlayer player, final int processIndex) {
        wasWhitelistOpened = true;
        ModularWindow.Builder builder = ModularWindow.builder(86, 100);
        builder.widget(
            new TextWidget("Process " + processIndex).setTextAlignment(Alignment.Center)
                .setPos(13, 7));
        builder.setBackground(GT_UITextures.BACKGROUND_SINGLEBLOCK_DEFAULT);
        builder.widget(
            SlotGroup.ofItemHandler(processWhitelistInventoryHandlers.get(processIndex), 4)
                .startFromSlot(0)
                .endAtSlot(ITEM_WHITELIST_SLOTS - 1)
                .phantom(true)
                .build()
                .setPos(7, 19));
        builder.widget(
            SlotGroup.ofFluidTanks(processFluidWhiteLists.get(processIndex), 4)
                .startFromSlot(0)
                .endAtSlot(FLUID_WHITELIST_SLOTS - 1)
                .phantom(true)
                .build()
                .setPos(7, 55));
        return builder.build();
    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.advchemicalprocessor";
    }

    public String getLocalName() {
        return "Advanced Chemical Processor";
    }

    public void setCoilTier(HeatingCoilLevel coilTier) {
        this.coilTier = coilTier;
    }

    public HeatingCoilLevel getCoilTier() {
        return coilTier;
    }

    @Override
    protected boolean hasPerfectOverclock() {
        return true;
    }

    protected void generateWhitelist(int processIndex) {
        HashSet<String> whitelist = new HashSet<>();
        for (ItemStack itemStack : processWhitelistInventoryHandlers.get(processIndex)
            .getStacks()) {
            if (itemStack != null) {
                whitelist.add(getWhitelistString(itemStack));
            }
        }
        for (IFluidTank tank : processFluidWhiteLists.get(processIndex)) {
            if (tank.getFluid() != null) {
                whitelist.add(getWhitelistString(tank.getFluid()));
            }
        }
        processWhitelists.set(processIndex, whitelist);
    }

    protected String getWhitelistString(ItemStack itemStack) {
        if (itemStack != null) {
            return itemStack.getUnlocalizedName();
        }
        return null;
    }

    protected String getWhitelistString(FluidStack fluidStack) {
        if (fluidStack != null) {
            return fluidStack.getUnlocalizedName();
        }
        return null;
    }

    @Override
    @Nonnull
    protected AdvChemicalProcessorProcessingLogic createProcessingLogic() {
        return new AdvChemicalProcessorProcessingLogic();
    }
}
