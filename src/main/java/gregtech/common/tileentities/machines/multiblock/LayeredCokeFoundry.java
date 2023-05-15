package gregtech.common.tileentities.machines.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.ENERGY_IN;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.FLUID_IN;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.FLUID_OUT;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.ITEM_IN;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.ITEM_OUT;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.NOTHING;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.logic.interfaces.PollutionLogicHost;
import gregtech.api.logic.interfaces.ProcessingLogicHost;
import gregtech.api.multitileentity.enums.GT_MultiTileCasing;
import gregtech.api.util.GT_StructureUtility;
import gregtech.common.tileentities.machines.multiblock.logic.CokeOvenProcessingLogic;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.tuple.Pair;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TierEU;
import gregtech.api.multitileentity.multiblock.base.StackableController;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.common.tileentities.machines.multiblock.logic.GenericProcessingLogic;

public class LayeredCokeFoundry extends StackableController<LayeredCokeFoundry> implements ProcessingLogicHost {
    private static IStructureDefinition<LayeredCokeFoundry> STRUCTURE_DEFINITION_MEGA = null;
    protected static final String STRUCTURE_PIECE_BASE = "T1";
    private static final Vec3Impl STRUCTURE_OFFSET_BASE = new Vec3Impl(2, 2, 0);
    private static final Vec3Impl STRUCTURE_OFFSET_MEGA_POSITION = new Vec3Impl(4, 7, -4);
    private static final Vec3Impl STRUCTURE_OFFSET_MEGA_START = new Vec3Impl(0, 0, -3);
    private static final Vec3Impl STRUCTURE_OFFSET_MEGA_STACK = new Vec3Impl(0, 0, -2);
    private static final Vec3Impl STRUCTURE_OFFSET_MEGA_STOP = new Vec3Impl(0, 0, -1);
    private final ProcessingLogic PROCESSING_LOGIC = new GenericProcessingLogic(GT_Recipe_Map.sPyrolyseRecipes);
    public LayeredCokeFoundry() {
        super();
    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.extensiblecokefoundry";
    }

    @Override
    public Vec3Impl getStartingStructureOffset() {
        return STRUCTURE_OFFSET_BASE;
    }

    @Override
    public void construct(ItemStack trigger, boolean hintsOnly) {
        final int blueprintCount = (trigger.stackSize - 1) + getMinStacks();
        final int stackCount = Math.min(blueprintCount, getMaxStacks());

        buildState.startBuilding(getStartingStructureOffset());
        buildPiece(STRUCTURE_PIECE_BASE, trigger, hintsOnly, buildState.getCurrentOffset());
        buildState.addOffset(getMegaPositionOffset());

        if (stackCount >= 1) {
            buildPiece(STACKABLE_START, trigger, hintsOnly, buildState.getCurrentOffset());
            buildState.addOffset(getStartingStackOffset());

            for (int i = 0; i < stackCount; i++) {
                buildPiece(STACKABLE_MIDDLE, trigger, hintsOnly, buildState.getCurrentOffset());
                buildState.addOffset(getPerStackOffset());
            }
            if (hasTop()) {
                buildPiece(STACKABLE_STOP, trigger, hintsOnly, buildState.getCurrentOffset());
            }
        }

        buildState.stopBuilding();
    }

    @Override
    public IStructureDefinition<LayeredCokeFoundry> getStructureDefinition() {
        if (STRUCTURE_DEFINITION_MEGA == null) {
            STRUCTURE_DEFINITION_MEGA = StructureDefinition.<LayeredCokeFoundry>builder()
                .addShape(
                    STRUCTURE_PIECE_BASE,
                    transpose(
                        new String[][]{
                            {" AAA ","AAAAA","AEEEA","AAAAA"},
                            {" AAA ","A   A","A   A","AAAAA"},
                            {" A~A ","A   A","A   A","AAAAA"},
                            {" AAA ","A   A","A   A","AAAAA"},
                            {" AAA ","AAAAA","AAAAA","AAAAA"}
                        }))
                .addShape(
                    STACKABLE_STOP,
                    transpose(
                        new String[][]{
                            {"ADADADADADADA","AAAAAAAAAAAAA"},
                            {" B B B B B B ","A A A A A A A"},
                            {"CB B B B B BC","ACACACACACACA"},
                            {" B B B B B B ","A A A A A A A"},
                            {" B B B B B B ","A A A A A A A"},
                            {" B B B B B B ","A A A A A A A"},
                            {" B B B B B B ","A A A A A A A"},
                            {" B B B B B B ","A A A A A A A"},
                            {" B B B B B B ","A A A A A A A"},
                            {"CB B B B B BC","ACACACACACACA"},
                            {" B B B B B B ","A A A A A A A"},
                            {"AAAAAAAAAAAAA","AAAAAAAAAAAAA"}
                        }))
                .addShape(
                    STACKABLE_MIDDLE,
                    transpose(
                        new String[][]{
                            {"ADAAAAAAAAADA","AAAAAAAAAAAAA"},
                            {" B         B ","A           A"},
                            {"CB         BC","A           A"},
                            {" B         B ","A           A"},
                            {" B         B ","A           A"},
                            {" B         B ","A           A"},
                            {" B         B ","A           A"},
                            {" B         B ","A           A"},
                            {" B         B ","A           A"},
                            {"CB         BC","A           A"},
                            {" B         B ","A           A"},
                            {"AAAAAAAAAAAAA","AAAAAAAAAAAAA"}
                        }))
                .addShape(
                    STACKABLE_START,
                    transpose(
                        new String[][]{
                            {"AAAAAAAAAAAAA","ADADADADADADA","AAAAAAAAAAAAA"},
                            {"A A A A A A A"," B B B B B B ","A           A"},
                            {"ACACACACACACA","CB B B B B BC","A           A"},
                            {"A A A A A A A"," B B B B B B ","A           A"},
                            {"A A A A A A A"," B B B B B B ","A           A"},
                            {"A A A A A A A"," B B B B B B ","A           A"},
                            {"A A A A A A A"," B B B B B B ","A           A"},
                            {"A A A A A A A"," B B B B B B ","A           A"},
                            {"A A A A A A A"," B B B B B B ","A           A"},
                            {"ACACA A ACACA","CB B B B B BC","A           A"},
                            {"A A A A A A A"," B B B B B B ","A           A"},
                            {"AAAAAAAAAAAAA","AAAAAAAAAAAAA","AAAAAAAAAAAAA"}
                        }))
                .addElement(
                    'A',
                    addMultiTileCasing(
                        "gt.multitileentity.casings",
                        GT_MultiTileCasing.Chemical.getId(),
                        FLUID_IN | ITEM_IN | FLUID_OUT | ITEM_OUT | ENERGY_IN))
                .addElement(
                    'B',
                    addMultiTileCasing(
                        "gt.multitileentity.casings",
                        getCasingMeta(),
                        FLUID_IN | ITEM_IN | FLUID_OUT | ITEM_OUT | ENERGY_IN))
                .addElement('C', ofBlock(GregTech_API.sBlockCasings4, 1))
                .addElement('D', GT_StructureUtility.ofFrame(Materials.Steel))
                .addElement('E', addMotorCasings(NOTHING))
                .build();
        }
        return STRUCTURE_DEFINITION_MEGA;
    }

    public boolean checkMachine() {
        stackCount = 0;

        buildState.startBuilding(getStartingStructureOffset());
        if (!checkPiece(STRUCTURE_PIECE_BASE, buildState.getCurrentOffset())) return buildState.failBuilding();

        buildState.addOffset(getMegaPositionOffset());
        if (checkPiece(STACKABLE_START, buildState.getCurrentOffset())){
            buildState.addOffset(getStartingStackOffset());
            for (int i = 0; i < getMaxStacks(); i++) {
                if (checkPiece(getStackableMiddle(i), buildState.getCurrentOffset())) {
                    buildState.addOffset(getPerStackOffset());
                    stackCount++;
                } else {
                    break;
                }
            }
            if (stackCount < getMinStacks()) return buildState.failBuilding();

            if (!checkPiece(getStackableStop(), buildState.stopBuilding())) {
                return buildState.failBuilding();
            }
        }

        calculateTier();
        updatePowerLogic();
        return tier > 0;
    }

    @Override
    public short getCasingRegistryID() {
        return 0;
    }

    @Override
    public int getCasingMeta() {
        return 18000;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Coke Oven")
            .addInfo("Controller for the Layered Coke Foundry")
            .addSeparator()
            .beginVariableStructureBlock(7, 9, 2 + getMinStacks(), 2 + getMaxStacks(), 7, 9, true)
            .addController("Bottom Front Center")
            .addCasingInfoExactly("Test Casing", 60, false)
            .addEnergyHatch("Any bottom layer casing")
            .addInputHatch("Any non-optional external facing casing on the stacks")
            .addInputBus("Any non-optional external facing casing on the stacks")
            .addOutputHatch("Any non-optional external facing casing on the stacks")
            .addOutputBus("Any non-optional external facing casing on the stacks")
            .addStructureInfo(
                String.format("Stackable middle stacks between %d-%d time(s).", getMinStacks(), getMaxStacks()))
            .toolTipFinisher("Wildcard");
        return tt;
    }

    @Override
    public int getMinStacks() {
        return 0;
    }

    @Override
    public int getMaxStacks() {
        return 10;
    }

    public Vec3Impl getMegaPositionOffset() {
        return STRUCTURE_OFFSET_MEGA_POSITION;
    }

    @Override
    public Vec3Impl getStartingStackOffset() {
        return STRUCTURE_OFFSET_MEGA_START;
    }

    @Override
    public Vec3Impl getPerStackOffset() {
        return STRUCTURE_OFFSET_MEGA_STACK;
    }

    @Override
    public Vec3Impl getAfterLastStackOffset() {
        return STRUCTURE_OFFSET_MEGA_STOP;
    }


    @SideOnly(Side.CLIENT)
    @Override
    protected ResourceLocation getActivitySoundLoop() {
        return SoundResource.IC2_MACHINES_MACERATOR_OP.resourceLocation;
    }

    @Override
    public ProcessingLogic getProcessingLogic() {
        return PROCESSING_LOGIC;
    }
}
