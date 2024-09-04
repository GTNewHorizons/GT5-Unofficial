package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlocksTiered;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.enums.Mods.EnderIO;
import static gregtech.api.enums.Mods.Railcraft;
import static gregtech.api.enums.Mods.ThaumicBases;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;

import org.apache.commons.lang3.tuple.Pair;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.ITierConverter;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TAE;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.core.lib.GTPPCore;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

@SuppressWarnings("SpellCheckingInspection")
public class MTEIndustrialForgeHammer extends GTPPMultiBlockBase<MTEIndustrialForgeHammer>
    implements ISurvivalConstructable {

    private int mCasing;
    private int mAnvilTier = 0;
    private static IStructureDefinition<MTEIndustrialForgeHammer> STRUCTURE_DEFINITION = null;

    public MTEIndustrialForgeHammer(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEIndustrialForgeHammer(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEIndustrialForgeHammer(this.mName);
    }

    @Override
    public String getMachineType() {
        return "Forge Hammer";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("Controller Block for the Industrial Forge Hammer")
            .addInfo("Speed: +100% | EU Usage: 100% | Parallel: Tier x Anvil Tier x 8")
            .addInfo("T1 - Vanilla Anvil");
        if (Railcraft.isModLoaded()) {
            tt.addInfo("T2 - Steel Anvil");
        }
        if (EnderIO.isModLoaded()) {
            tt.addInfo("T3 - Dark Steel Anvil");
        }
        if (ThaumicBases.isModLoaded()) {
            tt.addInfo("T3 - Thaumium Anvil");
            tt.addInfo("T4 - Void Metal Anvil");
        }

        tt.addPollutionAmount(getPollutionPerSecond(null))
            .addSeparator()
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front Center")
            .addCasingInfoMin("Forge Casing", 6, false)
            .addInputBus("Any Casing", 1)
            .addOutputBus("Any Casing", 1)
            .addInputHatch("Any Casing", 1)
            .addOutputHatch("Any Casing", 1)
            .addEnergyHatch("Any Casing", 1)
            .addMaintenanceHatch("Any Casing", 1)
            .addMufflerHatch("Any Casing", 1)
            .addOtherStructurePart("Anvil", "In the center of 3x3x3 structure", 2)
            .toolTipFinisher(GTPPCore.GT_Tooltip_Builder.get());
        return tt;
    }

    @Override
    public IStructureDefinition<MTEIndustrialForgeHammer> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            Map<Block, Integer> anvilTiers = new HashMap<>();

            anvilTiers.put(Blocks.anvil, 1);

            if (Railcraft.isModLoaded()) {
                anvilTiers.put(GameRegistry.findBlock(Railcraft.ID, "anvil"), 2);
            }

            if (EnderIO.isModLoaded()) {
                anvilTiers.put(GameRegistry.findBlock(EnderIO.ID, "blockDarkSteelAnvil"), 3);
            }

            if (ThaumicBases.isModLoaded()) {
                anvilTiers.put(GameRegistry.findBlock(ThaumicBases.ID, "thaumicAnvil"), 3);
                anvilTiers.put(GameRegistry.findBlock(ThaumicBases.ID, "voidAnvil"), 4);
            }

            STRUCTURE_DEFINITION = StructureDefinition.<MTEIndustrialForgeHammer>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "CAC", "CCC" }, { "CCC", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEIndustrialForgeHammer.class)
                        .atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler, InputHatch, OutputHatch)
                        .casingIndex(TAE.getIndexFromPage(1, 11))
                        .dot(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings5Misc, 6))))
                .addElement(
                    'A',
                    ofBlocksTiered(
                        anvilTierConverter(anvilTiers),
                        getAllAnvilTiers(anvilTiers),
                        0,
                        MTEIndustrialForgeHammer::setAnvilTier,
                        MTEIndustrialForgeHammer::getAnvilTier))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    private static List<Pair<Block, Integer>> getAllAnvilTiers(Map<Block, Integer> anvilTiers) {
        return anvilTiers.entrySet()
            .stream()
            .map(e -> Pair.of(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }

    private static ITierConverter<Integer> anvilTierConverter(Map<Block, Integer> anvilTiers) {
        return (block, meta) -> block == null ? 0 : anvilTiers.getOrDefault(block, 0);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 6 && checkHatch();
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.RANDOM_ANVIL_USE;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialForgeHammerActive;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialForgeHammer;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.getIndexFromPage(1, 11);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.hammerRecipes;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().setSpeedBonus(1 / 2F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (8 * getAnvilTier() * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getMaxEfficiency(final ItemStack aStack) {
        return 10000;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return GTPPCore.ConfigSwitches.pollutionPerSecondMultiIndustrialForgeHammer;
    }

    @Override
    public boolean explodesOnComponentBreak(final ItemStack aStack) {
        return false;
    }

    private void setAnvilTier(int tier) {
        mAnvilTier = tier;
    }

    private int getAnvilTier() {
        return mAnvilTier;
    }
}
