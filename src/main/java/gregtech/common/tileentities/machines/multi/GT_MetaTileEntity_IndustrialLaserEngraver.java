package gregtech.common.tileentities.machines.multi;

import static com.github.technus.tectech.util.TT_Utility.getUniqueIdentifier;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.*;
import static gregtech.api.enums.GT_HatchElement.*;
import static gregtech.api.enums.GT_Values.AuthorFourIsTheNumber;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_MULTI_CANNER_GLOW;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static gregtech.api.util.GT_StructureUtility.ofFrame;

import gregtech.api.enums.OrePrefixes;
import gregtech.api.util.GT_OreDictUnificator;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;

import gregtech.api.GregTech_API;
import gregtech.api.enums.Materials;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.logic.ProcessingLogic;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_EnhancedMultiBlockBase;
import gregtech.api.multitileentity.multiblock.casing.Glasses;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.RecipeMaps;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import gregtech.common.blocks.GT_Block_Casings4;
import gregtech.common.blocks.GT_Block_Laser;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GT_MetaTileEntity_IndustrialLaserEngraver
    extends GT_MetaTileEntity_EnhancedMultiBlockBase<GT_MetaTileEntity_IndustrialLaserEngraver>
    implements ISurvivalConstructable {

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<GT_MetaTileEntity_IndustrialLaserEngraver> STRUCTURE_DEFINITION = StructureDefinition
        .<GT_MetaTileEntity_IndustrialLaserEngraver>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            // spotless:off
            (transpose(
                new String[][]{
                    {" f ","faf","faf","faf","aaa"},
                    {"   "," g ","   "," a ","aaa"},
                    {"   "," g ","   "," a ","aaa"},
                    {"   "," g ","   "," a ","aaa"},
                    {"a~a","ara","aaa","aaa","aaa"}
                })))
            //spotless:on
        .addElement(
            'a',
            buildHatchAdder(GT_MetaTileEntity_IndustrialLaserEngraver.class)
                .atLeast(InputBus, OutputBus, Maintenance, Energy)
                .casingIndex(((GT_Block_Casings4) GregTech_API.sBlockCasings4).getTextureIndex(0))
                .dot(1)
                .buildAndChain(
                    onElementPass(
                        GT_MetaTileEntity_IndustrialLaserEngraver::onCasingAdded,
                        ofBlock(GregTech_API.sBlockCasings4, 0))))
        .addElement('f', ofFrame(Materials.TungstenSteel))
        .addElement('g', Glasses.chainAllGlasses())
        .addElement(
            'r',
            ofBlockAdder(GT_MetaTileEntity_IndustrialLaserEngraver::laserRendererAdder, GregTech_API.sLaserRender, 0))
        .build();

    private static GT_Block_Laser renderer;

    private boolean laserRendererAdder(Block block, int meta) {
        if (block == GregTech_API.sLaserRender) {
            renderer = (GT_Block_Laser) block;
            return true;
        }
        return false;
    }

    public GT_MetaTileEntity_IndustrialLaserEngraver(final int aID, final String aName, final String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_IndustrialLaserEngraver(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_IndustrialLaserEngraver> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public boolean isCorrectMachinePart(ItemStack aStack) {
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_IndustrialLaserEngraver(this.mName);
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity baseMetaTileEntity, ForgeDirection side, ForgeDirection aFacing,
        int colorIndex, boolean aActive, boolean redstoneLevel) {
        ITexture[] rTexture;
        if (side == aFacing) {
            if (aActive) {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_ACTIVE)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_ACTIVE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            } else {
                rTexture = new ITexture[] {
                    Textures.BlockIcons
                        .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 0)),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER)
                        .extFacing()
                        .build(),
                    TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_MULTI_CANNER_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
            }
        } else {
            rTexture = new ITexture[] { Textures.BlockIcons
                .getCasingTextureForId(GT_Utility.getCasingTextureIndex(GregTech_API.sBlockCasings4, 0)) };
        }
        return rTexture;
    }

    private boolean stopAllRendering = false;

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        stopAllRendering = !stopAllRendering;
        if (stopAllRendering) {
            renderer.laserRender.shouldRender = false;
        }
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Laser Engraver")
            .addInfo("Controller Block for the High Power Laser Emitter")
            .addInfo("Use screwdriver to disable laser rendering")
            .addInfo("200% the speed of single block machines of the same voltage")
            .addInfo("Gains 8 parallels per voltage tier")
            .addInfo(AuthorFourIsTheNumber)
            .addSeparator()
            .beginStructureBlock(7, 5, 7, true)
            .addController("Front Center")
            .addCasingInfoMin("Solid Steel Machine Casing", 85, false)
            .addCasingInfoExactly("Steel Pipe Casing", 24, false)
            .addInputBus("Any Solid Steel Casing", 1)
            .addOutputBus("Any Solid Steel Casing", 1)
            .addInputHatch("Any Solid Steel Casing", 1)
            .addOutputHatch("Any Solid Steel Casing", 1)
            .addEnergyHatch("Any Solid Steel Casing", 1)
            .addMaintenanceHatch("Any Solid Steel Casing", 1)
            .toolTipFinisher("GregTech");
        return tt;
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        buildPiece(STRUCTURE_PIECE_MAIN, stackSize, hintsOnly, 1, 4, 0);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivialBuildPiece(STRUCTURE_PIECE_MAIN, stackSize, 1, 4, 0, elementBudget, env, false, true);
    }

    private int mCasingAmount;

    private void onCasingAdded() {
        mCasingAmount++;
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack aStack) {
        mCasingAmount = 0;
        mEnergyHatches.clear();

        if (!checkPiece(STRUCTURE_PIECE_MAIN, 1, 4, 0)) return false;
        // TODO FIX
        if (mCasingAmount < 0) return false;

        // All checks passed!
        return true;
    }

    private static String getUniqueIdentifier(ItemStack is) {
        return is.getItem().getUnlocalizedName() + is.getItemDamage();
    }

    @Override
    protected ProcessingLogic createProcessingLogic() {
        return new ProcessingLogic() {

            @NotNull
            @Override
            protected CheckRecipeResult onRecipeStart(@NotNull GT_Recipe recipe) {
                Colors c = Colors.White;
                for (int i = 0; i < recipe.mInputs.length; i++) {
                    String uid = getUniqueIdentifier(recipe.mInputs[i]);
                    if (lensColors.containsKey(uid)) {
                        c = lensColors.get(uid);
                        renderer.laserRender.setColors(c.r, c.g, c.b);
                        //break;
                    }
                }
                if (!stopAllRendering) renderer.laserRender.shouldRender = true;
                return super.onRecipeStart(recipe);
            }

            @Override
            public ProcessingLogic clear() {
                renderer.laserRender.shouldRender = false;
                return super.clear();
            }
        }.setSpeedBonus(1F / 2F)
            .setMaxParallelSupplier(this::getMaxParallelRecipes);
    }

    public int getMaxParallelRecipes() {
        return (8 * GT_Utility.getTier(this.getMaxInputVoltage()));
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return RecipeMaps.laserEngraverRecipes;
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
        //getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.FoolsRuby, 1));
    }

    private enum Colors {
        White(1,1,1),
        Red(1,0,0),
        Green(0,1,0),
        Blue(0,0,1),
        Yellow(1,1,0),
        Purple(1,0,1),
        Cyan(0,1,1),
        Orange(1,0.5F,0),
        Black(0,0,0);

        final float r, g, b;

        Colors(float r, float g, float b) {
            this.r = r;
            this.g = g;
            this.b = b;
        }
    }

    private static final Map<String, Colors> lensColors;
    static {
        lensColors = new HashMap<>();

        //Black lenses
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Forcicium, 1)), Colors.Black);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Forcillium, 1)), Colors.Black);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.InfusedEntropy, 1)), Colors.Black);

        //White lenses
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.NetherStar, 1)), Colors.White);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Dilithium, 1)), Colors.White);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Glass, 1)), Colors.White);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.InfusedOrder, 1)), Colors.White);

        //Green lenses
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Emerald, 1)), Colors.Green);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Olivine, 1)), Colors.Green);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.GreenSapphire, 1)), Colors.Green);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.InfusedEarth, 1)), Colors.Green);

        //Red lenses
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Ruby, 1)), Colors.Red);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Firestone, 1)), Colors.Red);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Jasper, 1)), Colors.Red);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.FoolsRuby, 1)), Colors.Red);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.GarnetRed, 1)), Colors.Red);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.InfusedFire, 1)), Colors.Red);

        //Blue lenses
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.BlueTopaz, 1)), Colors.Blue);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Opal, 1)), Colors.Blue);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.InfusedWater, 1)), Colors.Blue);

        //Yellow lenses
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.GarnetYellow, 1)), Colors.Yellow);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Force, 1)), Colors.Yellow);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.InfusedAir, 1)), Colors.Yellow);

        //Purple lenses
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Amethyst, 1)), Colors.Purple);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Tanzanite, 1)), Colors.Purple);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Sapphire, 1)), Colors.Purple);

        //Cyan lenses
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.EnderEye, 1)), Colors.Cyan);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.EnderPearl, 1)), Colors.Cyan);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Diamond, 1)), Colors.Cyan);

        //Orange lenses
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Topaz, 1)), Colors.Orange);
        lensColors.put(getUniqueIdentifier(GT_OreDictUnificator.get(OrePrefixes.lens, Materials.Amber, 1)), Colors.Orange);
    }

}
