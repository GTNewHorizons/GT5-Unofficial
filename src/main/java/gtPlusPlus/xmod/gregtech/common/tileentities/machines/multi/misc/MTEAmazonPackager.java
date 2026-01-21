package gtPlusPlus.xmod.gregtech.common.tileentities.machines.multi.misc;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.Muffler;
import static gregtech.api.enums.HatchElement.OutputBus;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraft.util.StatCollector.translateToLocal;
import static net.minecraft.util.StatCollector.translateToLocalFormatted;

import java.util.Arrays;
import java.util.Collection;

import javax.annotation.Nonnull;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.enums.TAE;
import gregtech.api.gui.modularui.GTUITextures;
import gregtech.api.interfaces.IIconContainer;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.modularui2.GTGuiTextures;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.util.GTUtility;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.gui.modularui.multiblock.base.MTEMultiBlockBaseGui;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.core.block.ModBlocks;
import gtPlusPlus.xmod.gregtech.api.metatileentity.implementations.base.GTPPMultiBlockBase;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEAmazonPackager extends GTPPMultiBlockBase<MTEAmazonPackager> implements ISurvivalConstructable {

    private int mCasing;

    private static final int MACHINEMODE_PACKAGER = 0;
    private static final int MACHINEMODE_UNPACKAGER = 1;

    private static IStructureDefinition<MTEAmazonPackager> STRUCTURE_DEFINITION = null;

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEAmazonPackager(mName);
    }

    public MTEAmazonPackager(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEAmazonPackager(String aName) {
        super(aName);
    }

    @Override
    public String getMachineType() {
        return "Packager, Unpackager, AWD";
    }

    @Override
    public IStructureDefinition<MTEAmazonPackager> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEAmazonPackager>builder()
                .addShape(
                    mName,
                    transpose(
                        new String[][] { { "CCC", "CCC", "CCC" }, { "C~C", "C-C", "CCC" }, { "CCC", "CCC", "CCC" }, }))
                .addElement(
                    'C',
                    buildHatchAdder(MTEAmazonPackager.class).atLeast(InputBus, OutputBus, Maintenance, Energy, Muffler)
                        .casingIndex(TAE.getIndexFromPage(2, 9))
                        .hint(1)
                        .buildAndChain(onElementPass(x -> ++x.mCasing, ofBlock(ModBlocks.blockCasings3Misc, 9))))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(getMachineType())
            .addInfo("This Multiblock is used for EXTREME packaging requirements")
            .addBulkMachineInfo(16, 6f, 0.75f)
            .addInfo("Can be configured with a screwdriver to work as an Unpackager")
            .addInfo("Dust Schematics are inserted into the input buses")
            .addInfo("If inserted into the controller, it is shared across all buses")
            .addInfo("1x, 2x, 3x & Other Schematics are to be placed into the controller GUI slot")
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(3, 3, 3, true)
            .addController("Front center")
            .addCasingInfoMin("Supply Depot Casings", 10, false)
            .addInputBus("Any casing", 1)
            .addOutputBus("Any casing", 1)
            .addEnergyHatch("Any casing", 1)
            .addMaintenanceHatch("Any casing", 1)
            .addMufflerHatch("Any casing", 1)
            .toolTipFinisher();
        return tt;
    }

    @Override
    protected IIconContainer getActiveOverlay() {
        return TexturesGtBlock.oMCAAmazonPackagerActive;
    }

    @Override
    protected IIconContainer getActiveGlowOverlay() {
        return TexturesGtBlock.oMCAAmazonPackagerActiveGlow;
    }

    @Override
    protected IIconContainer getInactiveOverlay() {
        return TexturesGtBlock.oMCAAmazonPackager;
    }

    @Override
    protected IIconContainer getInactiveGlowOverlay() {
        return TexturesGtBlock.oMCAAmazonPackagerGlow;
    }

    @Override
    protected int getCasingTextureId() {
        return TAE.getIndexFromPage(2, 9);
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return (machineMode == MACHINEMODE_PACKAGER) ? RecipeMaps.packagerRecipes : RecipeMaps.unpackagerRecipes;
    }

    @Nonnull
    @Override
    public Collection<RecipeMap<?>> getAvailableRecipeMaps() {
        return Arrays.asList(RecipeMaps.packagerRecipes, RecipeMaps.unpackagerRecipes);
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic().noRecipeCaching()
            .setSpeedBonus(1F / 6F)
            .setEuModifier(0.75F)
            .setMaxParallelSupplier(this::getTrueParallel);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasing = 0;
        return checkPiece(mName, 1, 1, 0) && mCasing >= 10 && checkHatch();
    }

    @Override
    public int getPollutionPerSecond(ItemStack arg0) {
        return PollutionConfig.pollutionPerSecondMultiPackager;
    }

    @Override
    public int getMaxParallelRecipes() {
        return (16 * GTUtility.getTier(this.getMaxInputVoltage()));
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
    public boolean supportsInputSeparation() {
        return true;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        if (aNBT.hasKey("mPackageMode")) {
            machineMode = aNBT.getInteger("mPackageMode");
        }
        super.loadNBTData(aNBT);
    }

    @Override
    public void getWailaNBTData(EntityPlayerMP player, TileEntity tile, NBTTagCompound tag, World world, int x, int y,
        int z) {
        super.getWailaNBTData(player, tile, tag, world, x, y, z);
        tag.setString("mode", getMachineModeName());
    }

    @Override
    public String getMachineModeName() {
        return translateToLocal("GT5U.GTPP_MULTI_PACKAGER.mode." + machineMode);
    }

    @Override
    public boolean supportsMachineModeSwitch() {
        return true;
    }

    @Override
    public void onModeChangeByScrewdriver(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        setMachineMode(nextMachineMode());
        GTUtility
            .sendChatToPlayer(aPlayer, translateToLocalFormatted("GT5U.MULTI_MACHINE_CHANGE", getMachineModeName()));
    }

    @Override
    public void setMachineModeIcons() {
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_PACKAGER);
        machineModeIcons.add(GTUITextures.OVERLAY_BUTTON_MACHINEMODE_UNPACKAGER);
    }

    @Override
    protected @NotNull MTEMultiBlockBaseGui getGui() {
        return new MTEMultiBlockBaseGui(this).withMachineModeIcons(
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_PACKAGER,
            GTGuiTextures.OVERLAY_BUTTON_MACHINEMODE_UNPACKAGER);
    }
}
