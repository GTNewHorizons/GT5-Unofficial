package gregtech.common.tileentities.machines.multi;

import static com.github.bartimaeusnek.bartworks.util.BW_Util.ofGlassTieredMixed;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_Values.AuthorOmdaCZ;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.annotation.Nonnull;

import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_Output;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_ExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings2;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import gregtech.common.blocks.GT_Block_Casings10;
public class GT_MetaTileEntity_MultiSolidifier extends
    GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_MultiSolidifier> implements ISurvivalConstructable {

    protected final String MS_LEFT = mName + "left";
    protected final String MS_MID = mName + "mid";
    protected final String MS_RIGHT = mName + "right";

    protected int casingAmount = 0;
    protected int height = 0;
    protected int eV = 0, mCeil = 0, mFloor = 0;

    private int moldParallel;

    private final String STRUCTURE_PIECE_MAIN = "main";
    private final IStructureDefinition<GT_MetaTileEntity_MultiSolidifier> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_MultiSolidifier>builder()
        .addShape(
            MS_LEFT,
            (transpose(new String[][] { { "BBBBBBBBB", "BAAAAAAAB", "BAAAAAAAB", "BCCCECCCB", "BBBBBBBBB" } })))
        .addShape(
            MS_MID,
            (transpose(
                new String[][] { { "BB", "BB", "BB", "BB", "BB" }, { "AA", "  ", "-F", "  ", "AA" },
                    { "AA", "  ", "  ", "  ", "AA" }, { "CC", "  ", " D", "  ", "CC" },
                    { "BB", "BB", "BB", "BB", "BB" } })))
        .addShape(
            MS_RIGHT,
            (transpose(
                new String[][] { { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" },
                    { "B", "B", "B", "B", "B" }, { "B", "B", "B", "B", "B" } })))
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (transpose(
                new String[][] { { "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB" },
                    { "AAAAAAA", "       ", "D D D D", "       ", "AAAAAAA" },
                    { "AAAAAAA", "       ", "       ", "       ", "AAAAAAA" },
                    { "CCCBCCC", "       ", "F F F F", "       ", "CCCECCC" },
                    { "BBB~BBB", "BBBBBBB", "BBBBBBB", "BBBBBBB", "BBBBBBB" } })))
        .addElement('A', ofGlassTieredMixed((byte) 4, (byte) 127, 2))
        .addElement(
            'B',
            buildHatchAdder(GT_MetaTileEntity_MultiSolidifier.class)
                .atLeast(InputBus, OutputBus, Maintenance, Energy, InputHatch)
                .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(3))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_MultiSolidifier::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings10, 3))))
        .addElement('C', ofBlock(GregTech_API.sBlockCasings2, 13))
        .addElement('D', ofBlock(GregTech_API.sBlockCasings2, 13))
        .addElement(
            'E',
            buildHatchAdder(GT_MetaTileEntity_MultiSolidifier.class).atLeast(InputHatch)
                .casingIndex(((GT_Block_Casings2) GregTech_API.sBlockCasings2).getTextureIndex(0))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_MultiSolidifier::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings2, 0))))
        .addElement('F', ofBlock(GregTech_API.sBlockCasings2, 13))
        .build();

    public GT_MetaTileEntity_MultiSolidifier(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }
    public GT_MetaTileEntity_MultiSolidifier(String aName) {
        super(aName);
    }
    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_MultiSolidifier(this.mName);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings2, 0)) };
        }
        return rTexture;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Fluid solidifier")
            .addInfo("Controller Block for the Fluid Shaper 2024")
            .addInfo("Can get speed bonus from coolants")
            .addInfo("Water: +20%")
            .addInfo("IC2 Coolant: +50%")
            .addInfo("Cryotheum: +100%")
            .addInfo("Super Coolant: +200%")
            .addInfo("Gains 2x mold parallels per width expansion")
            .addInfo(EnumChatFormatting.BLUE + "Pretty solid, isn't it")
            .addInfo(AuthorOmdaCZ)
            .addSeparator()
            .beginVariableStructureBlock(9, 65, 5, 5, 5, 5, true)
            .addController("Front Center bottom")
            .addCasingInfoMin("Solid Steel Machine Casing", 69, false)
            .addCasingInfoMin("Steel Pipe Casing", 18, false)
            .addInputBus("Any Solid Steel Casing", 1)
            .addOutputBus("Any Solid Steel Casing", 1)
            .addInputHatch("Any Solid Steel Casing", 1)
            .addInputHatch("Solid Steel Casing second layer in the back centre", 1)
            .addEnergyHatch("Any Solid Steel Casing", 1)
            .addMaintenanceHatch("Any Solid Steel Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 3, 4, 0);
        int tTotalHeight = Math.min(30, stackSize.stackSize + 3); // min 2 output layer, so at least 1 + 2 height
        for (int i = 1; i < tTotalHeight - 1; i++) {
            buildPiece(MS_MID, stackSize, hintsOnly, 2 * i, 4, 0);
            buildPiece(MS_MID, stackSize, hintsOnly, -2 * i, 4, 0);
        }
        buildPiece(MS_RIGHT, stackSize, hintsOnly, tTotalHeight * 2 - 3, 4, 0);
        buildPiece(MS_RIGHT, stackSize, hintsOnly, -tTotalHeight * 2 + 3, 4, 0);
    }

    protected final List<List<GT_MetaTileEntity_Hatch_Output>> mOutputHatchesByLayer = new ArrayList<>();
    protected int mHeight;
    protected int nHeight;

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        mHeight = 0;
        nHeight = 0;
        int built = survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 3, 4, 0, elementBudget, env, false, true);
        if (built >= 0) return built;
        int tTotalHeight = Math.min(30, stackSize.stackSize + 3); // min 2 output layer, so at least 1 + 2 height
        for (int i = 1; i < tTotalHeight - 1; i++) {
            mHeight = i;
            nHeight = i;
            built = survivialBuildPiece(MS_MID, stackSize, 2 * i, 4, 0, elementBudget, env, false, true);
            if (built >= 0) return built;
            built = survivialBuildPiece(MS_MID, stackSize, -2 * i, 4, 0, elementBudget, env, false, true);
            if (built >= 0) return built;
        }
        if (mHeight == tTotalHeight - 2) return survivialBuildPiece(
            MS_RIGHT,
            stackSize,
            tTotalHeight * 2 - 3,
            4,
            0,
            elementBudget,
            env,
            false,
            true);
        else return survivialBuildPiece(
            MS_RIGHT,
            stackSize,
            -tTotalHeight * 2 + 3,
            4,
            0,
            elementBudget,
            env,
            false,
            true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_MultiSolidifier> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    protected boolean mTopLayerFound;
    protected int mCasing;

    protected void onCasingFound() {
        mCasing++;
    }

    protected void onTopLayerFound(boolean aIsCasing) {
        mTopLayerFound = true;
        if (aIsCasing) onCasingFound();
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mOutputHatchesByLayer.forEach(List::clear);
        mHeight = 1;
        mTopLayerFound = false;
        mCasing = 0;
        // check base
        if (!checkPiece(STRUCTURE_PIECE_MAIN, 3, 4, 0)) return false;

        // check each layer
        while (mHeight < 30) {
            if (!checkPiece(MS_MID, 2 * mHeight, 4, 0)) {
                if(!checkPiece(MS_RIGHT, 2 * mHeight - 3, 4, 0))
                return false;
                else break;
            }
            if (mTopLayerFound) {
                break;
            }
            // not top
            mHeight++;
        }

        // validate final invariants... (actual height is mHeight+1)
        return mCasing >= 7 * (mHeight + 1) - 5 && mHeight + 1 >= 0
            && mTopLayerFound
            && mMaintenanceHatches.size() == 1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 2F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    public int getMaxParallelRecipes() {
        return ((1 + moldParallel) * height);
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.fluidCannerRecipes, RecipeMaps.cannerRecipes);
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return false;
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.machines.oreprocessor1") + " "
                + EnumChatFormatting.WHITE
                + EnumChatFormatting.RESET);
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getDamageToComponent(ItemStack aStack) {
        return 0;
    }

    @Override
    public boolean explodesOnComponentBreak(ItemStack aStack) {
        return false;
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public boolean supportsInputSeparation() {
        return false;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }
}
