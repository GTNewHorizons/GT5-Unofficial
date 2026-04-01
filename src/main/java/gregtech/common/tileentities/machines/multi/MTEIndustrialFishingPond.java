package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.Collection;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cofh.asmhooks.block.BlockTickingWater;
import cofh.asmhooks.block.BlockWater;
import gregtech.api.casing.Casings;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Mods;
import gregtech.api.enums.StructureError;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.api.util.ReflectionUtil;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.material.MaterialsAlloy;
import ic2.core.init.BlocksItems;
import ic2.core.init.InternalName;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

public class MTEIndustrialFishingPond extends MTEExtendedPowerMultiBlockBase<MTEIndustrialFishingPond>
    implements ISurvivalConstructable {

    private static final int OFFSET_X = 5;
    private static final int OFFSET_Y = 2;
    private static final int OFFSET_Z = 0;

    public static final int FISH_MODE = 14;
    public static final int JUNK_MODE = 15;
    public static final int TREASURE_MODE = 16;

    private static IStructureDefinition<MTEIndustrialFishingPond> STRUCTURE_DEFINITION;
    private int casingAmount;

    private static final Class<?> cofhWater;

    static {
        cofhWater = ReflectionUtil.getClass("cofh.asmhooks.block.BlockWater");
    }

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
        tt.addMachineType("Fish Trap")
            .addInfo("Can process (Tier + 1) * 2 recipes")
            .addInfo("Put a numbered circuit into the input bus")
            .addInfo("Circuit " + FISH_MODE + " for Fish")
            .addInfo("Circuit " + JUNK_MODE + " for Junk")
            .addInfo("Circuit " + TREASURE_MODE + " for Treasure")
            .addInfo("Needs to be filled with water")
            .addInfo("Will automatically fill water from input hatch")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(11, 4, 11, false)
            .addController("Front Center")
            .addCasingInfoMin("Aquatic Casings", 160, false)
            .addCasingInfoExactly("Eglin Steel Frame", 24, false)
            .addInputBus("Any Aquatic Casing", 1)
            .addOutputBus("Any Aquatic Casing", 1)
            .addInputHatch("Any Aquatic Casing", 1)
            .addEnergyHatch("Any Aquatic Casing", 1)
            .addMaintenanceHatch("Any Aquatic Casing", 1)
            .addMufflerHatch("Any Aquatic Casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialFishingPond> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialFishingPond>builder()
                .addShape(
                    mName,
                    new String[][] { { "           ", "    BBB    ", "    B~B    ", "    BBB    " },
                        { "           ", "  BB A BB  ", "  BBCACBB  ", "  BBBBBBB  " },
                        { "           ", " B   A   B ", " BCCCACCCB ", " BBBBBBBBB " },
                        { "           ", " B   A   B ", " BCCCACCCB ", " BBBBBBBBB " },
                        { "    BBB    ", "B   BBB   B", "BCCCBBBCCCB", "BBBBBBBBBBB" },
                        { "    BBB    ", "BAAABBBAAAB", "BAAABBBAAAB", "BBBBBBBBBBB" },
                        { "    BBB    ", "B   BBB   B", "BCCCBBBCCCB", "BBBBBBBBBBB" },
                        { "           ", " B   A   B ", " BCCCACCCB ", " BBBBBBBBB " },
                        { "           ", " B   A   B ", " BCCCACCCB ", " BBBBBBBBB " },
                        { "           ", "  BB A BB  ", "  BBCACBB  ", "  BBBBBBB  " },
                        { "           ", "    BBB    ", "    BBB    ", "    BBB    " } })
                .addElement(
                    'B',
                    buildHatchAdder(MTEIndustrialFishingPond.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch)
                        .casingIndex(Casings.AquaticCasing.textureId)
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.AquaticCasing.asElement())))
                .addElement('A', ofFrame(MaterialsAlloy.EGLIN_STEEL))
                .addElement('C', ofChain(isAir(), ofAnyWater(false)))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, OFFSET_X, OFFSET_Y, OFFSET_Z, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        return checkPiece(mName, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    protected void validateStructure(Collection<StructureError> errors, NBTTagCompound context) {
        super.validateStructure(errors, context);

        if (casingAmount < 160) {
            errors.add(StructureError.TOO_FEW_CASINGS);
            context.setInteger("casings", casingAmount);
        }
    }

    @Override
    protected void localizeStructureErrors(Collection<StructureError> errors, NBTTagCompound context,
        List<String> lines) {
        super.localizeStructureErrors(errors, context, lines);

        if (errors.contains(StructureError.TOO_FEW_CASINGS)) {
            lines.add(
                StatCollector.translateToLocalFormatted("GT5U.gui.missing_casings", 160, context.getInteger("casings")));
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.AquaticCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.AquaticCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER)
                .extFacing()
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_VACUUM_FREEZER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.AquaticCasing.getCasingTexture() };
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
        return new ProcessingLogic() {

            @Override
            protected @NotNull CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                if (!checkForWater()) return SimpleCheckRecipeResult.ofFailure("no_water");
                return super.validateRecipe(recipe);
            }
        }.setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (2 * (GTUtility.getTier(this.getMaxInputVoltage()) + 1));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialFishingPond;
    }

    private Block getCasingBlock() {
        return ModBlocks.blockCasings3Misc;
    }

    private byte getCasingMeta() {
        return 0;
    }

    private boolean checkForWater() {

        // Get Facing direction
        IGregTechTileEntity aBaseMetaTileEntity = this.getBaseMetaTileEntity();
        final int mCurrentDirectionX = 4;
        final int mCurrentDirectionZ = 4;
        final int mOffsetX_Lower = -4;
        final int mOffsetX_Upper = 4;
        final int mOffsetZ_Lower = -4;
        final int mOffsetZ_Upper = 4;
        final int xDir = aBaseMetaTileEntity.getBackFacing().offsetX * mCurrentDirectionX;
        final int zDir = aBaseMetaTileEntity.getBackFacing().offsetZ * mCurrentDirectionZ;

        int tAmount = 0;
        for (int i = mOffsetX_Lower + 1; i <= mOffsetX_Upper - 1; ++i) {
            for (int j = mOffsetZ_Lower + 1; j <= mOffsetZ_Upper - 1; ++j) {
                for (int h = 0; h < 2; h++) {
                    Block tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
                    int tMeta = aBaseMetaTileEntity.getMetaIDOffset(xDir + i, h, zDir + j);
                    if (isNotStaticWater(tBlock, tMeta)) {
                        if (this.getStoredFluids() != null) {
                            for (FluidStack stored : this.getStoredFluids()) {
                                if (stored.isFluidEqual(Materials.Water.getFluid(1))) {
                                    if (stored.amount >= 1000) {
                                        // Utils.LOG_WARNING("Going to try swap an air block for water from inut bus.");
                                        stored.amount -= 1000;
                                        aBaseMetaTileEntity.getWorld()
                                            .setBlock(
                                                aBaseMetaTileEntity.getXCoord() + xDir + i,
                                                aBaseMetaTileEntity.getYCoord() + h,
                                                aBaseMetaTileEntity.getZCoord() + zDir + j,
                                                Blocks.water);
                                    }
                                }
                            }
                        }
                    }
                    tBlock = aBaseMetaTileEntity.getBlockOffset(xDir + i, h, zDir + j);
                    if (tBlock == Blocks.water || tBlock == Blocks.flowing_water) {
                        ++tAmount;
                    } else if (Mods.COFHCore.isModLoaded()) {
                        if (tBlock instanceof BlockWater || tBlock instanceof BlockTickingWater) {
                            ++tAmount;
                        }
                    }
                }
            }
        }

        return tAmount >= 60;
    }

    private boolean isNotStaticWater(Block block, int meta) {
        return block == Blocks.air || block == Blocks.flowing_water
            || block == BlocksItems.getFluidBlock(InternalName.fluidDistilledWater)
            || (cofhWater != null && cofhWater.isAssignableFrom(block.getClass()) && meta != 0);
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
}
