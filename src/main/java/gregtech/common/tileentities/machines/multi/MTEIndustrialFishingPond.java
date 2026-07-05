package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_GLOW;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofFrame;
import static gregtech.api.util.GTStructureUtility.ofSheetMetal;

import java.util.List;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.ICasingTextureProvider;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.structure.error.StructureError;
import gregtech.api.util.GTStructureUtility;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEIndustrialFishingPond extends MTEExtendedPowerMultiBlockBase<MTEIndustrialFishingPond>
    implements ISurvivalConstructable, ICasingTextureProvider {

    private static final int OFFSET_X = 5;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 0;

    public static final int FISH_MODE = 14;
    public static final int JUNK_MODE = 15;
    public static final int TREASURE_MODE = 16;

    private static final String[][] structure = { { "           ", "    CCC    ", "    C~C    ", "    CCC    " },
        { "           ", "  CC A CC  ", "  CCDBDCC  ", "  CCCCCCC  " },
        { "           ", " C   A   C ", " CDDDBDDDC ", " CCCCCCCCC " },
        { "           ", " C   A   C ", " CDDDBDDDC ", " CCCCCCCCC " },
        { "    CCC    ", "C   CCC   C", "CDDDCCCDDDC", "CCCCCCCCCCC" },
        { "    CCC    ", "CAAACCCAAAC", "CBBBCCCBBBC", "CCCCCCCCCCC" },
        { "    CCC    ", "C   CCC   C", "CDDDCCCDDDC", "CCCCCCCCCCC" },
        { "           ", " C   A   C ", " CDDDBDDDC ", " CCCCCCCCC " },
        { "           ", " C   A   C ", " CDDDBDDDC ", " CCCCCCCCC " },
        { "           ", "  CC A CC  ", "  CCDBDCC  ", "  CCCCCCC  " },
        { "           ", "    CCC    ", "    CCC    ", "    CCC    " } };
    private static IStructureDefinition<MTEIndustrialFishingPond> STRUCTURE_DEFINITION;
    private int casingAmount;
    private boolean needsWaterFill = false;

    public MTEIndustrialFishingPond(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialFishingPond(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialFishingPond(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Fish Trap, ZFP")
            .addInfo("Can process (Tier + 1) * 2 recipes")
            .addInfo("Put a numbered circuit into the input bus or controller")
            .addInfo("Circuit " + FISH_MODE + " for Fish")
            .addInfo("Circuit " + JUNK_MODE + " for Junk")
            .addInfo("Circuit " + TREASURE_MODE + " for Treasure")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(11, 11, 4, false)
            .addController("Front center")
            .addCasing("160-167", "Aquatic Casing", false)
            .addCasing("40", "Water", false)
            .addCasing("12", "Stainless Steel Sheetmetal", false)
            .addCasing("12", "Stainless Steel Frame Box", false)
            .addEnergyHatch("1+", "Any casing", 1)
            .addMaintenanceHatch("1", "Any casing", 1)
            .addMufflerHatch("1", "Any casing", 1)
            .addInputBus("0+", "Any casing", 1)
            .addInputHatch("0+", "Any casing", 1)
            .addOutputBus("1+", "Any casing", 1)
            .addStructureInfo("")
            .addStructureFooter(StatCollector.translateToLocal("GT5U.MBTT.Structure.WaterCost"))
            .addStructureAuthors(EnumChatFormatting.GOLD + "VorTex")
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialFishingPond> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialFishingPond>builder()
                .addShape(mName, structure)
                .addElement(
                    'C',
                    ofChain(
                        buildHatchAdder(MTEIndustrialFishingPond.class)
                            .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch)
                            .casingIndex(Casings.AquaticCasing.textureId)
                            .hint(1)
                            .build(),
                        onElementPass(x -> ++x.casingAmount, Casings.AquaticCasing.asElement())))
                .addElement('A', ofFrame(Materials.StainlessSteel))
                .addElement('B', ofSheetMetal(Materials.StainlessSteel))
                .addElement('D', ofChain(ofAnyWater(false), isAir()))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
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
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, OFFSET_X, OFFSET_Y, OFFSET_Z, elementBudget, env, false, true);
    }

    @Override
    public void checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack, List<StructureError> errors) {
        needsWaterFill = false;
        casingAmount = 0;
        if (!checkPiece(mName, OFFSET_X, OFFSET_Y, OFFSET_Z, errors)) {
            needsWaterFill = GTStructureUtility.hasWaterAtStructurePosition(
                aBaseMetaTileEntity,
                getExtendedFacing(),
                structure,
                OFFSET_X,
                OFFSET_Y,
                OFFSET_Z,
                'D');
            return;
        }
        checkCasingMin(errors, casingAmount, 160);
        checkHasEnergyHatch(errors);
        checkHasMaintenanceHatch(errors);
        checkHasMufflerHatch(errors);
        checkHasOutputBus(errors);
        if (!errors.isEmpty()) return;
        needsWaterFill = true;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        return Textures.BlockIcons.createTextureWithCasing(
            this,
            side,
            aFacing,
            aActive,
            OVERLAY_FRONT_VACUUM_FREEZER,
            OVERLAY_FRONT_VACUUM_FREEZER_GLOW,
            OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE,
            OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW);
    }

    @Override
    public ITexture getCasingTexture() {
        return Casings.AquaticCasing.getCasingTexture();
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.fishPondRecipes;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (2 * (GTUtility.getTier(this.getMaxInputVoltage()) + 1));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialFishingPond;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && needsWaterFill && aTick % 20 == 0) {
            if (GTStructureUtility.fillStructureWithWater(
                aBaseMetaTileEntity,
                getExtendedFacing(),
                structure,
                OFFSET_X,
                OFFSET_Y,
                OFFSET_Z,
                'D')) {
                needsWaterFill = false;
            }
        }
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
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && !f.isVerticallyFliped();
    }
}
