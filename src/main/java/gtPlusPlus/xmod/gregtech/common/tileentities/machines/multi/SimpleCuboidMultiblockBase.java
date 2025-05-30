package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.Arrays;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.StringUtils;
import gregtech.common.misc.GTStructureChannels;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;

public abstract class SimpleCuboidMultiblockBase extends GTPPMultiBlockBase<SimpleCuboidMultiblockBase>
    implements ISurvivalConstructable {

    // Height and width MUST be odd.
    private int height = 3;
    private int width = 3;

    private int minLength = 3;
    private int maxLength = 9;

    private int minCasingsBase = 2;
    private int minCasingsPerLayer = 2;

    private int casingTextureIndex = 0;

    private Block casingBlock;
    private int casingMeta = 0;
    private String casingName = "";

    private Block innerBlock = Blocks.air;
    private int innerBlockMeta = 0;
    private String innerBlockName = "";

    private List<IHatchElement<? super SimpleCuboidMultiblockBase>> validHatches;

    private IStructureDefinition<SimpleCuboidMultiblockBase> STRUCTURE_DEFINITION;
    private static final String STRUCTURE_PIECE_FRONT = "front";
    private static final String STRUCTURE_PIECE_MIDDLE = "middle";
    private static final String STRUCTURE_PIECE_BACK = "back";

    private int casingCount = 0;

    public SimpleCuboidMultiblockBase(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public SimpleCuboidMultiblockBase(String aName) {
        super(aName);
    }

    protected void setWidth(int width) {
        if (width % 2 == 0) throw new IllegalArgumentException("Width of the structure must be odd.");
        this.width = width;
    }

    protected void setHeight(int height) {
        if (height % 2 == 0) throw new IllegalArgumentException("Height of the structure must be odd.");
        this.height = height;
    }

    protected void setMinLength(int minLength) {
        this.minLength = minLength;
    }

    protected void setMaxLength(int maxLength) {
        this.maxLength = maxLength;
    }

    public void setMinCasingsBase(int minCasingsBase) {
        this.minCasingsBase = minCasingsBase;
    }

    public void setMinCasingsPerLayer(int minCasingsPerLayer) {
        this.minCasingsPerLayer = minCasingsPerLayer;
    }

    protected void setCasingTextureIndex(int casingTextureIndex) {
        this.casingTextureIndex = casingTextureIndex;
    }

    protected void setCasingBlock(Block casingBlock, int casingMeta) {
        this.casingBlock = casingBlock;
        this.casingMeta = casingMeta;
        this.casingName = new ItemStack(casingBlock, 0, casingMeta).getDisplayName();
    }

    protected void setCasingBlock(Block casingBlock) {
        setCasingBlock(casingBlock, 0);
    }

    protected void setInnerBlock(Block innerBlock, int innerBlockMeta) {
        this.innerBlock = innerBlock;
        this.innerBlockMeta = innerBlockMeta;
        this.innerBlockName = new ItemStack(innerBlock, 0, innerBlockMeta).getDisplayName();
    }

    protected void setInnerBlock(Block innerBlock) {
        setInnerBlock(innerBlock, 0);
    }

    protected void setValidHatches(IHatchElement<? super SimpleCuboidMultiblockBase>... validHatches) {
        this.validHatches = Arrays.asList(validHatches);
    }

    @Override
    public IStructureDefinition<SimpleCuboidMultiblockBase> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            String line_casings = StringUtils.getRepetitionOf('c', width);
            String line_middle = 'c' + StringUtils.getRepetitionOf(innerBlock == Blocks.air ? '-' : 'i', width - 2)
                + 'c';
            String line_controller = StringUtils.getRepetitionOf('c', width / 2) + '~'
                + StringUtils.getRepetitionOf('c', width / 2);

            String[][] structurePieceFront = new String[1][height];
            Arrays.fill(structurePieceFront[0], line_casings);
            structurePieceFront[0][height / 2] = line_controller;

            String[][] structurePieceMiddle = new String[1][height];
            Arrays.fill(structurePieceMiddle[0], line_middle);
            structurePieceMiddle[0][0] = line_casings;
            structurePieceMiddle[0][height - 1] = line_casings;

            String[][] structurePieceBack = new String[1][height];
            Arrays.fill(structurePieceBack[0], line_casings);

            var builder = StructureDefinition.<SimpleCuboidMultiblockBase>builder()
                .addShape(STRUCTURE_PIECE_FRONT, structurePieceFront)
                .addShape(STRUCTURE_PIECE_MIDDLE, structurePieceMiddle)
                .addShape(STRUCTURE_PIECE_BACK, structurePieceBack)
                .addElement(
                    'c',
                    buildHatchAdder(SimpleCuboidMultiblockBase.class).atLeastList(validHatches)
                        .casingIndex(casingTextureIndex)
                        .allowOnly(ForgeDirection.NORTH)
                        .dot(1)
                        .buildAndChain(onElementPass(t -> t.casingCount++, ofBlock(casingBlock, casingMeta))));
            if (innerBlock != Blocks.air) {
                builder.addElement('i', ofBlock(innerBlock, innerBlockMeta));
            }
            STRUCTURE_DEFINITION = builder.build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingCount = 0;

        if (!checkPiece(STRUCTURE_PIECE_FRONT, width / 2, height / 2, 0)) return false;

        int length = 2;

        while (length <= maxLength) {
            if (checkPiece(STRUCTURE_PIECE_MIDDLE, width / 2, height / 2, -(length - 1))) {
                length++;
                continue;
            }

            if (checkPiece(STRUCTURE_PIECE_BACK, width / 2, height / 2, -(length - 1))) {
                break;
            } else {
                return false;
            }
        }

        if (length < minLength) return false;
        if (length > maxLength) return false;
        if (casingCount < minCasingsBase + minCasingsPerLayer * length) return false;

        // Maintenance and muffler are mandatory, everything else is optional.
        if (validHatches.contains(Maintenance) && mMaintenanceHatches.isEmpty()) return false;
        if (validHatches.contains(Muffler) && mMufflerHatches.isEmpty()) return false;

        return true;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        // If length is set by channel, it is the exact length.
        // Otherwise, stack size of 1 corresponds to minimum length.
        int length;
        if (GTStructureChannels.STRUCTURE_LENGTH.hasValue(stackSize)) {
            length = Math.max(GTStructureChannels.STRUCTURE_LENGTH.getValueClamped(stackSize, 1, maxLength), minLength);
        } else {
            length = Math.min(stackSize.stackSize + minLength - 1, maxLength);
        }

        buildPiece(STRUCTURE_PIECE_FRONT, stackSize, hintsOnly, width / 2, height / 2, 0);

        for (int i = 1; i < length - 1; i++) {
            buildPiece(STRUCTURE_PIECE_MIDDLE, stackSize, hintsOnly, width / 2, height / 2, -i);
        }
        buildPiece(STRUCTURE_PIECE_BACK, stackSize, hintsOnly, width / 2, height / 2, -(length - 1));
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        // If length is set by channel, it is the exact length.
        // Otherwise, stack size of 1 corresponds to minimum length.
        int length;
        if (GTStructureChannels.STRUCTURE_LENGTH.hasValue(stackSize)) {
            length = Math.max(GTStructureChannels.STRUCTURE_LENGTH.getValueClamped(stackSize, 1, maxLength), minLength);
        } else {
            length = Math.min(stackSize.stackSize + minLength - 1, maxLength);
        }

        int built = survivalBuildPiece(
            STRUCTURE_PIECE_FRONT,
            stackSize,
            width / 2,
            height / 2,
            0,
            elementBudget,
            env,
            false,
            true);
        if (built >= 0) return built;

        for (int i = 1; i < length - 1; i++) {
            built = survivalBuildPiece(
                STRUCTURE_PIECE_MIDDLE,
                stackSize,
                width / 2,
                height / 2,
                -i,
                elementBudget,
                env,
                false,
                true);
            if (built >= 0) return built;
        }

        return survivalBuildPiece(
            STRUCTURE_PIECE_BACK,
            stackSize,
            width / 2,
            height / 2,
            -(length - 1),
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    protected int getCasingTextureId() {
        return casingTextureIndex;
    }

    protected MultiblockTooltipBuilder addStructureInfoToTooltip(MultiblockTooltipBuilder builder) {
        builder
            .beginVariableStructureBlock(width, width, height, height, minLength, maxLength, innerBlock == Blocks.air);

        if (minCasingsBase > 0 && minCasingsPerLayer > 0) {
            builder.addOtherStructurePart(
                casingName,
                minCasingsBase + " + " + minCasingsPerLayer + " x length (minimum)",
                1);
        } else if (minCasingsBase > 0) {
            builder.addOtherStructurePart(casingName, minCasingsBase + " (minimum)", 1);
        } else if (minCasingsPerLayer > 0) {
            builder.addOtherStructurePart(casingName, minCasingsPerLayer + " x length (minimum)", 1);
        } else {
            builder.addOtherStructurePart(casingName, "", 1);
        }

        builder.addStructureInfo("Additional length provides no benefit,")
            .addStructureInfo("only more space for buses/hatches.");

        if (innerBlock == Blocks.air) {
            for (var hatchType : validHatches) {
                if (hatchType == InputBus) builder.addInputBus("Any casing", 1);
                if (hatchType == OutputBus) builder.addOutputBus("Any casing", 1);
                if (hatchType == InputHatch) builder.addInputHatch("Any casing", 1);
                if (hatchType == OutputHatch) builder.addOutputHatch("Any casing", 1);
                if (hatchType == Energy) builder.addEnergyHatch("Any casing", 1);
                if (hatchType == Maintenance) builder.addMaintenanceHatch("Any casing", 1);
                if (hatchType == Muffler) builder.addMufflerHatch("Any casing", 1);
            }

        } else {
            builder
                .addOtherStructurePart(
                    casingName,
                    "At least " + minCasingsBase + " + " + minCasingsPerLayer + " x length")
                .addOtherStructurePart(innerBlockName, "Inside");

            for (var hatchType : validHatches) {
                if (hatchType == InputBus) builder.addInputBus("Any external casing", 1);
                if (hatchType == OutputBus) builder.addOutputBus("Any external casing", 1);
                if (hatchType == InputHatch) builder.addInputHatch("Any external casing", 1);
                if (hatchType == OutputHatch) builder.addOutputHatch("Any external casing", 1);
                if (hatchType == Energy) builder.addEnergyHatch("Any external casing", 1);
                if (hatchType == Maintenance) builder.addMaintenanceHatch("Any external casing", 1);
                if (hatchType == Muffler) builder.addMufflerHatch("Any external casing", 1);
            }
        }

        return builder;
    }
}
