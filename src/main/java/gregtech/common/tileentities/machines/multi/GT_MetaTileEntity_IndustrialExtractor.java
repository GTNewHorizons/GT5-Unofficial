package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.AuthorFourIsTheNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.ImmutableList;
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
import gregtech.common.blocks.GT_Block_Casings4;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class GT_MetaTileEntity_IndustrialExtractor
    extends GT_MetaTileEntity_ExtendedPowerMultiBlockBase<GT_MetaTileEntity_IndustrialExtractor>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_IndustrialExtractor> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_IndustrialExtractor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            (new String[][] { { "  A  ", " BBB ", "AB~BA", " BBB " }, { "  A  ", " BBB ", "AB BA", " BBB " },
                { "  A  ", " BBB ", "AB BA", " BBB " }, { "  A  ", " BBB ", "ABBBA", " BBB " },
                { "  A  ", "  A  ", "AAAAA", "     " } }))
        .addElement(
            'B',
            buildHatchAdder(GT_MetaTileEntity_IndustrialExtractor.class)
                .atLeast(InputBus, OutputBus, Maintenance, Energy)
                .casingIndex(((GT_Block_Casings4) GregTech_API.sBlockCasings4).getTextureIndex(1))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_IndustrialExtractor::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings4, 1))))
        .addElement(
            'A',
            ofBlocksTiered(
                GT_MetaTileEntity_IndustrialExtractor::getItemPipeTierFromMeta,
                ImmutableList.of(
                    Pair.of(GregTech_API.sBlockCasings11, 0),
                    Pair.of(GregTech_API.sBlockCasings11, 1),
                    Pair.of(GregTech_API.sBlockCasings11, 2),
                    Pair.of(GregTech_API.sBlockCasings11, 3),
                    Pair.of(GregTech_API.sBlockCasings11, 4),
                    Pair.of(GregTech_API.sBlockCasings11, 5),
                    Pair.of(GregTech_API.sBlockCasings11, 6),
                    Pair.of(GregTech_API.sBlockCasings11, 7)),
                -2,
                GT_MetaTileEntity_IndustrialExtractor::setItemPipeTier,
                GT_MetaTileEntity_IndustrialExtractor::getItemPipeTier))
        .build();

    private int itemPipeTier = 0;

    private static Integer getItemPipeTierFromMeta(Block block, Integer metaID) {
        if (block != GregTech_API.sBlockCasings11) return -1;
        if (metaID < 0 || metaID > 7) return -1;
        return metaID + 1;
    }

    private void setItemPipeTier(int tier) {
        itemPipeTier = tier;
    }

    private int getItemPipeTier() {
        return itemPipeTier;
    }

    public GT_MetaTileEntity_IndustrialExtractor(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_IndustrialExtractor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_IndustrialExtractor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_IndustrialExtractor(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 1)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 1)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_INDUSTRIAL_EXTRACTOR_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 1)) };
        }
        return rTexture;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Extractor")
            .addInfo("Controller Block for the Dissection Apparatus")
            .addInfo("200% faster than single block machines of the same voltage")
            .addInfo("Only uses 85% of the EU/t normally required")
            .addInfo("Gains 8 parallels per tier of Item Pipe Casing")
            .addInfo(AuthorFourIsTheNumber)
            .addSeparator()
            .beginStructureBlock(5, 4, 5, true)
            .addController("Front Center")
            .addCasingInfoMin("Stainless Steel Machine Casing", 24, false)
            .addCasingInfoExactly("Item Pipe Casing", 19, true)
            .addInputBus("Any Stainless Steel Casing", 1)
            .addOutputBus("Any Stainless Steel Casing", 1)
            .addEnergyHatch("Any Stainless Steel Casing", 1)
            .addMaintenanceHatch("Any Stainless Steel Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 2, 2, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 2, 2, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        itemPipeTier = -2;

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 2, 2, 0)) return false;
        return mCasingAmount >= 24;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1F / 3F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes)
            .setEuModifier(0.85F);
    }

    public int getMaxParallelRecipes() {
        return 8 * itemPipeTier;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("maxParallelRecipes", getMaxParallelRecipes());
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();
        currentTip.add(
            StatCollector.translateToLocal("GT5U.multiblock.parallelism") + ": "
                + EnumChatFormatting.WHITE
                + tag.getInteger("maxParallelRecipes"));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.extractorRecipes;
    }

    @Override
    public int getRecipeCatalystPriority() {
        return -10;
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
        return true;
    }

    @Override
    public boolean supportsSingleRecipeLocking() {
        return true;
    }

    @Override
    protected void setProcessingLogicPower(ProcessingLogic logic) {
        logic.setAvailableVoltage(GT_Utility.roundUpVoltage(this.getMaxInputVoltage()));
        logic.setAvailableAmperage(1L);
    }
}
