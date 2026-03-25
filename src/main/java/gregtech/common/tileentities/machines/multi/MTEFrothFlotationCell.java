
package gregtech.common.tileentities.machines.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.enums.HatchElement.InputBus;
import static gregtech.api.enums.HatchElement.InputHatch;
import static gregtech.api.enums.HatchElement.Maintenance;
import static gregtech.api.enums.HatchElement.OutputHatch;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTStructureUtility.ofFrame;

import java.util.List;
import java.util.Objects;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
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
import gregtech.api.enums.GTValues;
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

    private int casingAmount;
    private static final int OFFSET_X = 5;
    private static final int OFFSET_Y = 3;
    private static final int OFFSET_Z = 0;
    private static IStructureDefinition<MTEFrothFlotationCell> STRUCTURE_DEFINITION = null;

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
            .toolTipFinisher(GTValues.AuthorNoc.get());
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
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<MTEFrothFlotationCell>builder()
                .addShape(
                    mName,
                    new String[][] { { "           ", "           ", "           ", "    D~D    ", "   CCCCC   " },
                        { "           ", "           ", "           ", "  DD   DD  ", "  CCCCCCC  " },
                        { "           ", "       BB  ", "           ", " D       D ", " CCCCCCCCC " },
                        { "           ", "      BBB  ", "       B   ", " D       D ", "CCCCCCCCCCC" },
                        { "   EEEEE   ", " AE  BBBEA ", "A         A", "D    D    D", "CCCCCCCCCCC" },
                        { " EEEEEEEEE ", " E  BEB  E ", "     E     ", "D   DED   D", "CCCCCCCCCCC" },
                        { "   EEEEE   ", " AEBBB  EA ", "A         A", "D    D    D", "CCCCCCCCCCC" },
                        { "           ", "  BBB      ", "   B       ", " D       D ", "CCCCCCCCCCC" },
                        { "           ", "  BB       ", "           ", " D       D ", " CCCCCCCCC " },
                        { "           ", "           ", "           ", "  DD   DD  ", "  CCCCCCC  " },
                        { "           ", "           ", "           ", "    DDD    ", "   CCCCC   " } })
                .addElement(
                    'C',
                    buildHatchAdder(MTEFrothFlotationCell.class)
                        .atLeast(InputBus, InputHatch, OutputHatch, Maintenance, Energy)
                        .casingIndex(Casings.InconelReinforcedCasing.textureId)
                        .hint(1)
                        .buildAndChain(
                            onElementPass(x -> ++x.casingAmount, Casings.InconelReinforcedCasing.asElement())))
                .addElement('D', Casings.FlotationCellCasings.asElement())
                .addElement('E', Casings.InconelReinforcedCasing.asElement())
                .addElement('A', ofFrame(MaterialsAlloy.INCONEL_690))
                .addElement('B', ofFrame(MaterialsAlloy.STABALLOY))
                .build();
        }
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
        return checkPiece(mName, OFFSET_X, OFFSET_Y, OFFSET_Z) && casingAmount >= 90 && checkHatch();
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
}
