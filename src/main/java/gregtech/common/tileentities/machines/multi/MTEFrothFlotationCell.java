
package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.isAir;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofAnyWater;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;
import java.util.Objects;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.oredict.OreDictionary;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.screen.ModularWindow;
import com.gtnewhorizons.modularui.api.screen.UIBuildContext;
import com.gtnewhorizons.modularui.common.widget.FakeSyncWidget;

import gregtech.api.casing.Casings;
import gregtech.api.enums.GTAuthors;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.SoundResource;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.MTEExtendedPowerMultiBlockBase;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTRecipe;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.pollution.PollutionConfig;
import gtPlusPlus.api.recipe.GTPPRecipeMaps;
import gtPlusPlus.core.material.MaterialsAlloy;
import gtPlusPlus.xmod.gregtech.common.blocks.textures.TexturesGtBlock;

public class MTEFrothFlotationCell extends MTEExtendedPowerMultiBlockBase<MTEFrothFlotationCell>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final int OFFSET_X = 5;
    private static final int OFFSET_Y = 3;
    private static final int OFFSET_Z = 0;

    private int casingAmount;
    private boolean needsWaterFill = false;

    private static final String[][] structure = new String[][] {
        { "           ", "           ", "           ", "    D~D    ", "   CCCCC   " },
        { "           ", "           ", "           ", "  DDWWWDD  ", "  CCCCCCC  " },
        { "           ", "       BB  ", "           ", " DWWWWWWWD ", " CCCCCCCCC " },
        { "           ", "      BBB  ", "       B   ", " DWWWWWWWD ", "CCCCCCCCCCC" },
        { "   EEEEE   ", " AE  BBBEA ", "A         A", "DWWWWDWWWWD", "CCCCCCCCCCC" },
        { " EEEEEEEEE ", " E  BEB  E ", "     E     ", "DWWWDEDWWWD", "CCCCCCCCCCC" },
        { "   EEEEE   ", " AEBBB  EA ", "A         A", "DWWWWDWWWWD", "CCCCCCCCCCC" },
        { "           ", "  BBB      ", "   B       ", " DWWWWWWWD ", "CCCCCCCCCCC" },
        { "           ", "  BB       ", "           ", " DWWWWWWWD ", " CCCCCCCCC " },
        { "           ", "           ", "           ", "  DDWWWDD  ", "  CCCCCCC  " },
        { "           ", "           ", "           ", "    DDD    ", "   CCCCC   " } };

    private static final IStructureDefinition<MTEFrothFlotationCell> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEFrothFlotationCell>builder()
        .addShape(STRUCTURE_PIECE_MAIN, structure)
        .addElement(
            'C',
            buildHatchAdder(MTEFrothFlotationCell.class).atLeast(InputBus, InputHatch, OutputHatch, Maintenance, Energy)
                .casingIndex(Casings.InconelReinforcedCasing.textureId)
                .hint(1)
                .buildAndChain(onElementPass(x -> ++x.casingAmount, Casings.InconelReinforcedCasing.asElement())))
        .addElement('D', Casings.FlotationCellCasings.asElement())
        .addElement('E', Casings.InconelReinforcedCasing.asElement())
        .addElement('A', ofFrame(MaterialsAlloy.INCONEL_690))
        .addElement('B', ofFrame(MaterialsAlloy.STABALLOY))
        .addElement('W', ofChain(isAir(), ofAnyWater(false)))
        .build();

    public MTEFrothFlotationCell(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public MTEFrothFlotationCell(final String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(final IGregTechTileEntity aTileEntity) {
        return new MTEFrothFlotationCell(this.mName);
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Flotation Cell, FCR")
            .addInfo("Process that milled ore!")
            .addInfo("You can only ever process one type of material per controller")
            .addPerfectOCInfo()
            .addPollutionAmount(getPollutionPerSecond(null))
            .beginStructureBlock(11, 5, 11, false)
            .addController("Front Center")
            .addCasingInfoMin("Inconel Reinforced Casing", 120, false)
            .addCasingInfoMin("Flotation Cell Casing", 31, false)
            .addCasingInfoMin("Staballoy Frame Box", 20, false)
            .addCasingInfoMin("Inconel-690 Frame Box", 8, false)
            .addInputBus("Bottom Casing", 1)
            .addInputHatch("Bottom Casing", 1)
            .addOutputHatch("Bottom Casing", 1)
            .addEnergyHatch("Bottom Casing", 1)
            .addMaintenanceHatch("Bottom Casing", 1)
            .toolTipFinisher(GTAuthors.AuthorNoc.get());
        return tt;
    }

    @Override
    protected SoundResource getProcessStartSound() {
        return SoundResource.GTCEU_LOOP_HUM;
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return GTPPRecipeMaps.flotationCellRecipes;
    }

    @Override
    public IStructureDefinition<MTEFrothFlotationCell> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        if (side == aFacing) {
            if (aActive) return new ITexture[] { Casings.InconelReinforcedCasing.getCasingTexture(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDFrothFlotationCellActive)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDFrothFlotationCellActiveGlow)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] { Casings.InconelReinforcedCasing.getCasingTexture(), TextureFactory.builder()
                .addIcon(TexturesGtBlock.oMCDFrothFlotationCell)
                .extFacing()
                .build(),
                TextureFactory.builder()
                    .addIcon(TexturesGtBlock.oMCDFrothFlotationCellGlow)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] { Casings.InconelReinforcedCasing.getCasingTexture() };
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, OFFSET_X, OFFSET_Y, OFFSET_Z);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece(
            STRUCTURE_PIECE_MAIN,
            stackSize,
            OFFSET_X,
            OFFSET_Y,
            OFFSET_Z,
            elementBudget,
            env,
            false,
            true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        casingAmount = 0;
        boolean valid = checkPiece(STRUCTURE_PIECE_MAIN, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmount >= 90
            && checkHatch();
        if (valid) needsWaterFill = true;
        return valid;
    }

    public boolean checkHatch() {
        return mEnergyHatches.size() >= 1;
    }

    @Override
    public int getPollutionPerSecond(final ItemStack aStack) {
        return PollutionConfig.pollutionPerSecondMultiFrothFlotationCell;
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult validateRecipe(@NotNull GTRecipe recipe) {
                // Make sure we lock to a specific milled ore, checked via oredict
                String milledName = getMilledStackName(recipe);
                if (milledName == null) {
                    return CheckRecipeResultRegistry.NO_RECIPE;
                }

                // Set material locked for this controller
                // "milled" check is to clear old save data since the name caching system changed
                if (lockedMaterialName == null || !lockedMaterialName.startsWith("milled")) {
                    lockedMaterialName = milledName;
                }

                // Ensure oredict matches
                if (!Objects.equals(lockedMaterialName, milledName)) {
                    return SimpleCheckRecipeResult.ofFailure("machine_locked_to_different_recipe");
                }
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }

            private String getMilledStackName(GTRecipe recipe) {
                if (recipe == null || recipe.mInputs == null) {
                    return null;
                }

                for (ItemStack stack : recipe.mInputs) {
                    for (int oreID : OreDictionary.getOreIDs(stack)) {
                        String oredict = OreDictionary.getOreName(oreID);
                        if (oredict.startsWith(OrePrefixes.milled.toString())) {
                            return oredict;
                        }
                    }
                }
                return null;
            }
        }.enablePerfectOverclock();
    }

    /*
     * Handle NBT
     */

    private String lockedMaterialName = null;

    @Override
    public void setItemNBT(NBTTagCompound aNBT) {
        if (lockedMaterialName != null) {
            aNBT.setString("lockedMaterialName", lockedMaterialName);
        }
        super.setItemNBT(aNBT);
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        if (lockedMaterialName != null) {
            aNBT.setString("lockedMaterialName", lockedMaterialName);
        }
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("lockedMaterialName", Constants.NBT.TAG_STRING)) {
            lockedMaterialName = aNBT.getString("lockedMaterialName");
        }
    }

    @Override
    public void addAdditionalTooltipInformation(ItemStack stack, List<String> tooltip) {
        if (stack.hasTagCompound() && stack.getTagCompound()
            .hasKey("lockedMaterialName")) {
            tooltip.add(
                StatCollector.translateToLocal("tooltip.flotationCell.lockedTo") + " "
                    + StatCollector.translateToLocal(
                        stack.getTagCompound()
                            .getString("lockedMaterialName")));
        }
    }

    @Override
    public boolean isRecipeLockingEnabled() {
        return lockedMaterialName != null && !lockedMaterialName.isEmpty();
    }

    @Override
    public void addUIWidgets(ModularWindow.Builder builder, UIBuildContext buildContext) {
        super.addUIWidgets(builder, buildContext);
        builder.widget(new FakeSyncWidget.StringSyncer(() -> lockedMaterialName, val -> lockedMaterialName = val));
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
    public boolean supportsVoidProtection() {
        return true;
    }

    @Override
    public boolean supportsBatchMode() {
        return true;
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && needsWaterFill && mMachine && aTick % 20 == 0) {
            World world = aBaseMetaTileEntity.getWorld();
            boolean allFilled = true;
            int controllerX = aBaseMetaTileEntity.getXCoord();
            int controllerY = aBaseMetaTileEntity.getYCoord();
            int controllerZ = aBaseMetaTileEntity.getZCoord();

            for (int sliceZ = 0; sliceZ < structure.length; sliceZ++) {
                String[] layers = structure[sliceZ];
                for (int layerY = 0; layerY < layers.length; layerY++) {
                    String row = layers[layerY];
                    for (int charX = 0; charX < row.length(); charX++) {
                        if (row.charAt(charX) != 'W') continue;

                        int[] abc = new int[] { charX - OFFSET_X, layerY - OFFSET_Y, sliceZ - OFFSET_Z };
                        int[] xyz = new int[] { 0, 0, 0 };
                        this.getExtendedFacing()
                            .getWorldOffset(abc, xyz);
                        int wx = controllerX + xyz[0];
                        int wy = controllerY + xyz[1];
                        int wz = controllerZ + xyz[2];

                        Block block = world.getBlock(wx, wy, wz);
                        if (block != Blocks.water) {
                            boolean isReplaceable = block == Blocks.air || block == Blocks.flowing_water
                                || block.isReplaceable(world, wx, wy, wz);
                            if (isReplaceable) {
                                world.setBlock(wx, wy, wz, Blocks.water, 0, 3);
                            } else {
                                allFilled = false;
                            }
                        }
                    }
                }
            }

            if (allFilled) needsWaterFill = false;
        }
    }
}
