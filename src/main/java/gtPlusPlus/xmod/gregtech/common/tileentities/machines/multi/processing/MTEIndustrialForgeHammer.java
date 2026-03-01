package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.processing;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
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

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

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
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;

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
        return "gt.recipe.hammer";
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("gt.sledgehammer.tips.1")
            .addStaticSpeedInfo(2f)
            .addStaticEuEffInfo(1f)
            .addInfo("gt.sledgehammer.tips.2");
        if (Railcraft.isModLoaded()) {
            tt.addInfo("gt.sledgehammer.tips.3");
        }
        if (EnderIO.isModLoaded()) {
            tt.addInfo("gt.sledgehammer.tips.4");
        }
        if (ThaumicBases.isModLoaded()) {
            tt.addInfo("gt.sledgehammer.tips.5");
        }

        tt.addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 3, true)
            .addController("front_center")
            .addCasingInfoMin("gtplusplus.blockcasings.5.6.name", 6, false)
            .addInputBus("<casing>", 1)
            .addOutputBus("<casing>", 1)
            .addInputHatch("<casing>", 1)
            .addOutputHatch("<casing>", 1)
            .addEnergyHatch("<casing>", 1)
            .addMaintenanceHatch("<casing>", 1)
            .addMufflerHatch("<casing>", 1)
            .addStructurePart("GTPP.tooltip.structure.anvil", "gt.sledgehammer.info.1", 2)
            .toolTipFinisher();
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
                        .hint(1)
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
            .sorted(Comparator.comparingInt(Map.Entry<Block, Integer>::getValue))
            .map(e -> Pair.of(e.getKey(), e.getValue()))
            .collect(Collectors.toList());
    }

    private static ITierConverter<Integer> anvilTierConverter(Map<Block, Integer> anvilTiers) {
        return (block, meta) -> block == null ? null : anvilTiers.getOrDefault(block, null);
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(mName, stackSize, hintsOnly, 1, 1, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(mName, stackSize, 1, 1, 0, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 6 && checkHatch();
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_LOOP_FORGE_HAMMER;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialForgeHammerActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCAIndustrialForgeHammerActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAIndustrialForgeHammer;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCAIndustrialForgeHammerGlow;
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
        return new ProcessingLogic().noRecipeCaching()
            .setSpeedBonus(1 / 2F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public int getMaxParallelRecipes() {
        return (8 * getAnvilTier() * GTUtility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiIndustrialForgeHammer;
    }

    private void setAnvilTier(int tier) {
        mAnvilTier = tier;
    }

    private int getAnvilTier() {
        return mAnvilTier;
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setInteger("tier", mAnvilTier);
    }

    @Override
    public void getWailaBody(ItemStack itemStack, List<String> currentTip, IWailaDataAccessor accessor,
        IWailaConfigHandler config) {
        super.getWailaBody(itemStack, currentTip, accessor, config);
        final NBTTagCompound tag = accessor.getNBTData();

        currentTip.add(
            StatCollector.translateToLocal("GT5U.machines.tier") + ": "
                + EnumChatFormatting.YELLOW
                + formatNumber(tag.getInteger("tier"))
                + EnumChatFormatting.RESET);
    }
}
