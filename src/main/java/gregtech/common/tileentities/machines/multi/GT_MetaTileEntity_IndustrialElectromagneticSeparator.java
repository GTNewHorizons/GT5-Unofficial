package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.AuthorFourIsTheNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_EMS_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.ArrayList;

import gregtech.GT_Mod;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

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
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_MagHatch;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings10;
import gregtech.common.items.GT_MetaGenerated_Item_01;

public class GT_MetaTileEntity_IndustrialElectromagneticSeparator
    extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_IndustrialElectromagneticSeparator>
    implements ISurvivalConstructable {

    private final ArrayList<GT_MetaTileEntity_MagHatch> mMagHatches = new ArrayList<>();

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_IndustrialElectromagneticSeparator> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_IndustrialElectromagneticSeparator>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" }, { "CCC", "CCC", "CCC" }, }))
        .addElement(
            'C',
            ofChain(
                buildHatchAdder(GT_MetaTileEntity_IndustrialElectromagneticSeparator.class)
                    .adder(GT_MetaTileEntity_IndustrialElectromagneticSeparator::addMagHatch)
                    .hatchClass(GT_MetaTileEntity_MagHatch.class)
                    .shouldReject(t -> !t.mMagHatches.isEmpty())
                    .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(0))
                    .dot(1)
                    .build(),
                buildHatchAdder(GT_MetaTileEntity_IndustrialElectromagneticSeparator.class)
                    .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                    .casingIndex(((GT_Block_Casings10) GregTech_API.sBlockCasings10).getTextureIndex(0))
                    .dot(1)
                    .buildAndChain(
                        onElementPass(
                            GT_MetaTileEntity_IndustrialElectromagneticSeparator::onCasingAdded,
                            ofBlock(GregTech_API.sBlockCasings10, 0)))))
        .build();

    public GT_MetaTileEntity_IndustrialElectromagneticSeparator(final int aID, final String aName,
        final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_IndustrialElectromagneticSeparator(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_IndustrialElectromagneticSeparator> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_IndustrialElectromagneticSeparator(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_EMS_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_EMS_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_EMS)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_EMS_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings10, 0)) };
        }
        return rTexture;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Electromagnetic Separator")
            .addInfo("Controller Block for the Industrial Electromagnetic Separator")
            .addInfo("Insert an electromagnet into the electromagnet housing to use")
            .addInfo("Better electromagnets give further bonuses")
            .addInfo("With an iron electromagnet:")
            .addInfo("-150% as fast as single block machines of the same voltage")
            .addInfo("-Only uses 80% of the EU/t normally required")
            .addInfo("-Processes 6 items per voltage tier")
            .addPollutionAmount(getPollutionPerSecond(null))
            .addInfo(AuthorFourIsTheNumber)
            .addSeparator()
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Electromagnetic Casings", 6, false)
            .addOtherStructurePart("Electromagnet Housing", "1 Only, Any Casing")
            .addInputBus("Any Casing", 1)
            .addOutputBus("(Magnetic Outputs) Any Casing", 1)
            .addOutputBus("(Amagnetic Outputs) Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public int getPollutionPerSecond(ItemStack aStack) {
        return GT_Mod.gregtechproxy.mPollutionImplosionCompressorPerSecond;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mMagHatches.clear();
        return checkPiece(STRUCTURE_PIECE_MAIN, 1, 1, 0) && mCasingAmount >= 6 && mMagHatches.size() == 1;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setEuModifier(0.8F)
            .setSpeedBonus(1F / 1.5F)
            .setMaxParallel(6);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.electroMagneticSeparatorRecipes;
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

    public static boolean isValidElectromagnet(ItemStack aMagnet) {
        return (aMagnet != null && aMagnet.getItem() instanceof GT_MetaGenerated_Item_01
            && aMagnet.getItemDamage() >= 32345
            && aMagnet.getItemDamage() <= 32349);
    }

    private boolean addMagHatch(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity != null) {
            final IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
            if (aMetaTileEntity instanceof GT_MetaTileEntity_MagHatch aMagHatch) {
                ((GT_MetaTileEntity_MagHatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                mMagHatches.add(aMagHatch);
                return true;
            }
        }
        return false;
    }
}
