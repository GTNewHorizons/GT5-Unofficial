package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_HatchElement.Energy;
import static gregtech.api.enums.GT_HatchElement.InputBus;
import static gregtech.api.enums.GT_HatchElement.InputHatch;
import static gregtech.api.enums.GT_HatchElement.Maintenance;
import static gregtech.api.enums.GT_HatchElement.OutputBus;
import static gregtech.api.enums.GT_HatchElement.OutputHatch;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SOLAR_FACTORY;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SOLAR_FACTORY_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SOLAR_FACTORY_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_SOLAR_FACTORY_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.casingTexturePages;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;
import static net.minecraft.util.EnumChatFormatting.BLUE;
import static net.minecraft.util.EnumChatFormatting.DARK_AQUA;

import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_Values;
import gregtech.api.enums.Materials;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;

public class GT_MetaTileEntity_SolarFactory
    extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_SolarFactory> implements ISurvivalConstructable {

    private static final int CASING_INDEX = 16;
    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_SolarFactory> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_SolarFactory>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose(
                new String[][] {
                    { "   CCC   ", "  CCCCC  ", " CCCCCCC ", "CCCCCCCCC", " CCCCCCC ", "  CCCCC  ", "   CCC   " },
                    { "   BBB   ", "  C   C  ", " B     B ", "C       C", " B     B ", "  C   C  ", "   BBB   " },
                    { "   CCC   ", "  CCCCC  ", " CCCCCCC ", "CCCCCCCCC", " CCCCCCC ", "  CCCCC  ", "   CCC   " },
                    { "         ", "  EBBBE  ", "  B   B  ", "E B D B E", "  B   B  ", "  EBBBE  ", "         " },
                    { "         ", "  EBBBE  ", "  B   B  ", "E B D B E", "  B   B  ", "  EBBBE  ", "         " },
                    { "         ", "  EBBBE  ", "  B   B  ", "E B D B E", "  B   B  ", "  EBBBE  ", "         " },
                    { "         ", "  EBBBE  ", "  B   B  ", "E B D B E", "  B   B  ", "  EBBBE  ", "         " },
                    { "   CCC   ", "  CCCCC  ", " CCCCCCC ", "CCCCCCCCC", " CCCCCCC ", "  CCCCC  ", "   CCC   " },
                    { "   B~B   ", "  C   C  ", " B     B ", "C       C", " B     B ", "  C   C  ", "   BBB   " },
                    { "   CCC   ", "  CCCCC  ", " CCCCCCC ", "CCCCCCCCC", " CCCCCCC ", "  CCCCC  ", "   CCC   " } }))
        .addElement(
            'C',
            buildHatchAdder(GT_MetaTileEntity_SolarFactory.class)
                .atLeast(InputHatch, OutputHatch, InputBus, OutputBus, Maintenance, Energy)
                .casingIndex(CASING_INDEX)
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_SolarFactory::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings2, 0)))) // solid steel casing (like assembly line !!! yayyy)
        .addElement('D', ofBlock(GregTech_API.sBlockCasings2, 5)) // assembly line block thingy
        .addElement('B', Glasses.chainAllGlasses()) // any glass, i guess
        .addElement('E', ofFrame(Materials.NiobiumTitanium))
        .build();

    private int mCasingAmount;

    public GT_MetaTileEntity_SolarFactory(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_SolarFactory(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_SolarFactory(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Solar Factory")
            .addInfo("Cheaply & Quickly mass produces Solar Panels")
            .addInfo("Has discounted recipes for Tech Solars")
            .addInfo("definitely not stolen from Tyrant")
            .beginStructureBlock(7, 10, 9, true)
            .addStructureInfo(BLUE + "8+ " + DARK_AQUA + "Solid Steel Machine Casings")
            .addStructureInfo(BLUE + "24 " + DARK_AQUA + "Niobium-Titanium Frame Boxes")
            .addStructureInfo(BLUE + "67 " + DARK_AQUA + "EV+ Glass")
            .addStructureInfo(BLUE + "4 " + DARK_AQUA + "Assembling Line Casing")
            .addStructureInfo(BLUE + "1+ " + DARK_AQUA + "Input Hatch")
            .addStructureInfo(BLUE + "1+ " + DARK_AQUA + "Output Bus")
            .addStructureInfo(BLUE + "1+ " + DARK_AQUA + "Input Bus")
            .addStructureInfo(BLUE + "1 " + DARK_AQUA + "Energy Hatch+")
            .addStructureInfo(BLUE + "1 " + DARK_AQUA + "Maintenance Hatch")
            .toolTipFinisher(GT_Values.AuthorPureBluez);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { casingTexturePages[0][16], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { casingTexturePages[0][16], TextureFactory.builder()
                .addIcon(OVERLAY_FRONT_SOLAR_FACTORY)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_SOLAR_FACTORY_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { casingTexturePages[0][16] };
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    public GT_Recipe.GT_Recipe_Map getRecipeMap() {
        return GT_Recipe.GT_Recipe_Map.sMultiblockChemicalRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().enablePerfectOverclock();
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_SolarFactory> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        return checkPiece(STRUCTURE_PIECE_MAIN, 4, 8, 0) && mCasingAmount >= 8
            && !mEnergyHatches.isEmpty()
            && mMaintenanceHatches.size() == 1;
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
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 4, 8, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 4, 8, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }
}
