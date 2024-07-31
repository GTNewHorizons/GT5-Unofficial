package goodgenerator.blocks.tileEntity;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static goodgenerator.util.DescTextLocalization.BLUE_PRINT_INFO;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import goodgenerator.blocks.tileEntity.base.GT_MetaTileEntity_TooltipMultiBlockBase_EM;
import goodgenerator.loader.Loaders;
import goodgenerator.util.DescTextLocalization;
import gregtech.api.GregTech_API;
import gregtech.api.enums.GT_HatchElement;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.objects.GT_RenderedTexture;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;

public class AntimatterGenerator extends GT_MetaTileEntity_TooltipMultiBlockBase_EM
    implements IConstructable, ISurvivalConstructable {

    protected IStructureDefinition<goodgenerator.blocks.tileEntity.AntimatterGenerator> multiDefinition = null;
    protected long trueOutput = 0;
    protected int trueEff = 0;
    protected int times = 1;

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return DescTextLocalization.addText("AntimatterGenerator.hint", 8);
    }

    public AntimatterGenerator(String name) {
        super(name);
    }

    public AntimatterGenerator(int id, String name, String nameRegional) {
        super(id, name, nameRegional);
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {

        super.loadNBTData(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {

        super.saveNBTData(aNBT);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return null;
    }

    @Override
    public String[] getInfoData() {
        String[] info = super.getInfoData();
        info[4] = "Probably makes: " + EnumChatFormatting.RED
            + GT_Utility.formatNumbers(Math.abs(this.trueOutput))
            + EnumChatFormatting.RESET
            + " EU/t";
        info[6] = "Problems: " + EnumChatFormatting.RED
            + (this.getIdealStatus() - this.getRepairStatus())
            + EnumChatFormatting.RESET
            + " Efficiency: "
            + EnumChatFormatting.YELLOW
            + trueEff
            + EnumChatFormatting.RESET
            + " %";
        return info;
    }


    @Override
    public boolean checkMachine_EM(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        return structureCheck_EM(mName, 3, 7, 0) && mMaintenanceHatches.size() == 1
            && mDynamoHatches.size() + eDynamoMulti.size() == 1;
    }

    @Override
    public int getMaxEfficiency(ItemStack aStack) {
        return 0;
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return 0;
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
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new goodgenerator.blocks.tileEntity.AntimatterGenerator(this.mName);
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Antimatter Generator")
            .addInfo("Controller block for the Shielded Lagrangian Annihilation Matrix")
            .addInfo("The structure is too complex!")
            .addInfo(BLUE_PRINT_INFO)
            .addSeparator()
            .beginStructureBlock(7, 8, 7, true)
            .addController("Front bottom")
            .toolTipFinisher("Good Generator");
        return tt;
    }

    @Override
    @SuppressWarnings("ALL")
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            if (aActive) return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(44),
                new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT_ACTIVE_GLOW)
                    .glow()
                    .build() };
            return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(44),
                new GT_RenderedTexture(Textures.BlockIcons.NAQUADAH_REACTOR_SOLID_FRONT) };
        }
        return new ITexture[] { Textures.BlockIcons.getCasingTextureForId(44) };
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 3, 7, 0, elementBudget, env, false, true);
    }

    @Override
    public void construct(ItemStack itemStack, boolean hintsOnly) {
        structureBuild_EM(mName, 3, 7, 0, itemStack, hintsOnly);
    }

    @Override
    public IStructureDefinition<goodgenerator.blocks.tileEntity.AntimatterGenerator> getStructure_EM() {
        if (multiDefinition == null) {
            multiDefinition = StructureDefinition.<goodgenerator.blocks.tileEntity.AntimatterGenerator>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] {
                            { "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA", "AAAAAAA" },
                            { "N     N", "       ", "  CCC  ", "  CPC  ", "  CCC  ", "       ", "N     N" },
                            { "N     N", "       ", "  CCC  ", "  CPC  ", "  CCC  ", "       ", "N     N" },
                            { "N     N", "       ", "  CCC  ", "  CPC  ", "  CCC  ", "       ", "N     N" },
                            { "N     N", "       ", "  CCC  ", "  CPC  ", "  CCC  ", "       ", "N     N" },
                            { "AAAAAAA", "A     A", "A CCC A", "A CPC A", "A CCC A", "A     A", "AAAAAAA" },
                            { "ANNNNNA", "N     N", "N CCC N", "N CPC N", "N CCC N", "N     N", "ANNNNNA" },
                            { "XXX~XXX", "XXXXXXX", "XXXXXXX", "XXXXXXX", "XXXXXXX", "XXXXXXX", "XXXXXXX" }, }))
                .addElement(
                    'X',
                    ofChain(
                        buildHatchAdder(goodgenerator.blocks.tileEntity.AntimatterGenerator.class)
                            .atLeast(
                                HatchElement.DynamoMulti.or(GT_HatchElement.Dynamo),
                                GT_HatchElement.InputHatch,
                                GT_HatchElement.OutputHatch,
                                GT_HatchElement.Maintenance)
                            .casingIndex(44)
                            .dot(1)
                            .build(),
                        ofBlock(GregTech_API.sBlockCasings3, 12)))
                .addElement('A', ofBlock(GregTech_API.sBlockCasings3, 12))
                .addElement('N', ofBlock(Loaders.radiationProtectionSteelFrame, 0))
                .addElement('C', ofBlock(Loaders.MAR_Casing, 0))
                .addElement('P', ofBlock(GregTech_API.sBlockCasings2, 15))
                .build();
        }
        return multiDefinition;
    }

}
