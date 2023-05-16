package gregtech.common.tileentities.machines.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.Mods.*;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.ENERGY_IN;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.FLUID_IN;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.FLUID_OUT;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.ITEM_IN;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.ITEM_OUT;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.NOTHING;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.logic.interfaces.ProcessingLogicHost;
import gregtech.api.multitileentity.enums.GT_MultiTileCasing;
import gregtech.api.util.GT_StructureUtility;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.SoundResource;
import gregtech.api.multitileentity.multiblock.base.StackableController;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;
import gregtech.common.tileentities.machines.multiblock.logic.GenericProcessingLogic;

public class LayeredCokeBattery extends StackableController<LayeredCokeBattery> implements ProcessingLogicHost {
    private static IStructureDefinition<LayeredCokeBattery> STRUCTURE_DEFINITION_MEGA = null;
    protected static final String STRUCTURE_PIECE_BASE = "T1";
    private static final Vec3Impl STRUCTURE_OFFSET_BASE = new Vec3Impl(2, 2, 0);
    private static final Vec3Impl STRUCTURE_OFFSET_MEGA_POSITION = new Vec3Impl(4, 7, -4);
    private static final Vec3Impl STRUCTURE_OFFSET_MEGA_START = new Vec3Impl(0, 0, -3);
    private static final Vec3Impl STRUCTURE_OFFSET_MEGA_STACK = new Vec3Impl(0, 0, -2);
    private static final Vec3Impl STRUCTURE_OFFSET_MEGA_STOP = new Vec3Impl(0, 0, -1);
    private final ProcessingLogic foundryProcessingLogic = new GenericProcessingLogic(GT_Recipe_Map.sPyrolyseRecipes);

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.layeredcokebattery";
    }

    @Override
    public String getLocalName() {
        return "Layered Coke Battery";
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
    public IStructureDefinition<LayeredCokeBattery> getStructureDefinition() {
        if (STRUCTURE_DEFINITION_MEGA == null) {
            STRUCTURE_DEFINITION_MEGA = StructureDefinition.<LayeredCokeBattery>builder()
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
                            {"AAAAAAAAAAAAA","AAAAAAAAAAAAA"},
                            {" B B B B B B ","AFAFAFAFAFAFA"},
                            {"CB B B B B BC","AFAFAFAFAFAFA"},
                            {" B B B B B B ","AFAFAFAFAFAFA"},
                            {" B B B B B B ","AFAFAFAFAFAFA"},
                            {" B B B B B B ","AFAFAFAFAFAFA"},
                            {" B B B B B B ","AFAFAFAFAFAFA"},
                            {" B B B B B B ","AFAFAFAFAFAFA"},
                            {" B B B B B B ","AFAFAFAFAFAFA"},
                            {"CB B B B B BC","AFAFAFAFAFAFA"},
                            {" B B B B B B ","AFAFAFAFAFAFA"},
                            {"AAAAAAAAAAAAA","AAAAAAAAAAAAA"}
                        }))
                .addShape(
                    STACKABLE_MIDDLE,
                    transpose(
                        new String[][]{
                            {"ADFFFFAFFFFDA","AAAAAAAAAAAAA"},
                            {" B    A    B ","AAAAAAAAAAAAA"},
                            {"CB    A    BC","AAAAAAAAAAAAA"},
                            {" B    A    B ","AAAAAAAAAAAAA"},
                            {" B    A    B ","AAAAAAAAAAAAA"},
                            {" B    A    B ","AAAAAAAAAAAAA"},
                            {" B    A    B ","AAAAAAAAAAAAA"},
                            {" B    A    B ","AAAAAAAAAAAAA"},
                            {" B    A    B ","AAAAAAAAAAAAA"},
                            {"CB    A    BC","AAAAAAAAAAAAA"},
                            {" B    A    B ","AAAAAAAAAAAAA"},
                            {"AAAAAAAAAAAAA","AAAAAAAAAAAAA"}
                        }))
                .addShape(
                    STACKABLE_START,
                    transpose(
                        new String[][]{
                            {"AAAAAAAAAAAAA","AAAAAAAAAAAAA","AAAAAAAAAAAAA"},
                            {"AFAFAFAFAFAFA"," B B B B B B ","AAAAAAAAAAAAA"},
                            {"AFAFAFAFAFAFA","CB B B B B BC","AAAAAAAAAAAAA"},
                            {"AFAFAFAFAFAFA"," B B B B B B ","AAAAAAAAAAAAA"},
                            {"AFAFAFAFAFAFA"," B B B B B B ","AAAAAAAAAAAAA"},
                            {"AFAFAFAFAFAFA"," B B B B B B ","AAAAAAAAAAAAA"},
                            {"AFAFAFAFAFAFA"," B B B B B B ","AAAAAAAAAAAAA"},
                            {"AFAFAFAFAFAFA"," B B B B B B ","AAAAAAAAAAAAA"},
                            {"AFAFAFAFAFAFA"," B B B B B B ","AAAAAAAAAAAAA"},
                            {"AFAFAFAFAFAFA","CB B B B B BC","AAAAAAAAAAAAA"},
                            {"AFAFAFAFAFAFA"," B B B B B B ","AAAAAAAAAAAAA"},
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
                .addElement(
                    'F',
                    ofChain(
                        ofBlockUnlocalizedName(IndustrialCraft2.ID, "blockAlloyGlass", 0, true),
                        ofBlockUnlocalizedName(BartWorks.ID, "BW_GlasBlocks", 0, true),
                        ofBlockUnlocalizedName(BartWorks.ID, "BW_GlasBlocks2", 0, true),
                        ofBlockUnlocalizedName(Thaumcraft.ID, "blockCosmeticOpaque", 2, false)))
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
        return GT_MultiTileCasing.Distillation.getId();
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
        return 20;
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
        return foundryProcessingLogic;
    }
}
