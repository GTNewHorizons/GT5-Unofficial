package tectech.thing.metaTileEntity.multi;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.onElementPass;
import static gregtech.api.GregTechAPI.sBlockCasings1;
import static gregtech.api.enums.HatchElement.Dynamo;
import static gregtech.api.enums.HatchElement.Energy;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static net.minecraft.util.StatCollector.translateToLocal;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.MultiblockTooltipBuilder;
import tectech.Reference;
import tectech.thing.casing.BlockGTCasingsTT;
import tectech.thing.casing.TTCasingsContainer;
import tectech.thing.metaTileEntity.multi.base.TTMultiblockBase;
import tectech.thing.metaTileEntity.multi.base.render.TTRenderedExtendedFacingTexture;
import tectech.util.CommonValues;

/**
 * Created by danie_000 on 17.12.2016.
 */
public class MTEActiveTransformer extends TTMultiblockBase implements ISurvivalConstructable {

    // Gives a one-chance grace period for deforming the multi. This is to allow you to hotswap hatches without
    // powerfailing due to an unlucky tick timing - this grace period is already a part of base TecTech but the
    // tick timer is essentially random, so it was extremely unreliable. Now you are guaranteed the length
    // of one structure check to finish your hotswap before it deforms.
    private boolean grace = false;

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        casingCount = 0;
        if (structureCheck_EM("main", 1, 1, 0) && casingCount >= 5) {
            grace = true;
            return true;
        } else if (grace) {
            grace = false;
            return true;
        }
        return false;
    }

    @Override
    public void onFirstTick_EM(IGregTechTileEntity aBaseMetaTileEntity) {
        if (!mMachine) {
            aBaseMetaTileEntity.disableWorking();
        }
    }

    // region structure
    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal("gt.blockmachines.multimachine.em.transformer.hint"), // 1 - Energy IO Hatches or High
                                                                               // Power Casing
    };
    private static final IStructureDefinition<MTEActiveTransformer> STRUCTURE_DEFINITION = IStructureDefinition
        .<MTEActiveTransformer>builder()
        .addShape(
            "main",
            new String[][] { { "111", "1~1", "111", }, { "111", "101", "111", }, { "111", "111", "111", }, })
        .addElement('0', ofBlock(sBlockCasings1, 15))
        .addElement(
            '1',
            buildHatchAdder(MTEActiveTransformer.class)
                .atLeast(Energy, HatchElement.EnergyMulti, Dynamo, HatchElement.DynamoMulti)
                .casingIndex(BlockGTCasingsTT.textureOffset)
                .dot(1)
                .buildAndChain(onElementPass(t -> t.casingCount++, ofBlock(TTCasingsContainer.sBlockCasingsTT, 0))))
        .build();
    private int casingCount = 0;

    @Override
    public IStructureDefinition<MTEActiveTransformer> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }
    // endregion

    public MTEActiveTransformer(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
        eDismantleBoom = false;
    }

    public MTEActiveTransformer(String aName) {
        super(aName);
        eDismantleBoom = false;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new MTEActiveTransformer(mName);
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        if (ePowerPass) {
            mEfficiencyIncrease = 10000;
            mMaxProgresstime = 20;
        } else {
            mEfficiencyIncrease = 0;
            mMaxProgresstime = 0;
        }
        eAmpereFlow = 0;
        mEUt = 0;
        return ePowerPass ? SimpleCheckRecipeResult.ofSuccess("routing")
            : SimpleCheckRecipeResult.ofFailure("no_routing");
    }

    @Override
    public MultiblockTooltipBuilder createTooltip() {
        final MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.transformer.name")) // Machine Type:
                                                                                                 // Transformer
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.transformer.desc.0")) // Controller block of
                                                                                              // the
            // Active Transformer
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.transformer.desc.1")) // Can transform to
                                                                                              // and from any
                                                                                              // voltage
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.transformer.desc.2")) // Only 0.004% power
                                                                                              // loss, HAYO!
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.transformer.desc.3")) // Will explode if
                                                                                              // broken while
                                                                                              // running
            .addSeparator()
            .beginStructureBlock(3, 3, 3, false)
            .addController(translateToLocal("tt.keyword.Structure.FrontCenter")) // Controller: Front center
            .addCasingInfoMin(translateToLocal("gt.blockcasingsTT.0.name"), 5, false) // 5x High Power Casing
                                                                                      // (minimum)
            .addOtherStructurePart(
                translateToLocal("tt.keyword.Structure.SuperconductingCoilBlock"),
                translateToLocal("tt.keyword.Structure.Center")) // SuperconductingCoilBlock: Center
            .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Energy Hatch: Any
                                                                                            // High Power Casing
            .addDynamoHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Dynamo Hatch: Any
                                                                                            // High Power Casing
            .toolTipFinisher(CommonValues.TEC_MARK_GENERAL);
        return tt;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][0],
                new TTRenderedExtendedFacingTexture(aActive ? TTMultiblockBase.ScreenON : TTMultiblockBase.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[BlockGTCasingsTT.texturePage][0] };
    }

    public static final ResourceLocation activitySound = new ResourceLocation(Reference.MODID + ":fx_noise");

    @Override
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound() {
        return activitySound;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        return true;
    }

    @Override
    public boolean doRandomMaintenanceDamage() {
        return true;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        if ((aTick & 31) == 31) {
            ePowerPass = aBaseMetaTileEntity.isAllowedToWork();
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 1, 1, 0, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivialBuildPiece("main", stackSize, 1, 1, 0, elementBudget, source, actor, false, true);
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    @Override
    public boolean isPowerPassButtonEnabled() {
        return true;
    }

    @Override
    public boolean isSafeVoidButtonEnabled() {
        return false;
    }

    @Override
    public boolean isAllowedToWorkButtonEnabled() {
        return true;
    }

    @Override
    public boolean getDefaultHasMaintenanceChecks() {
        return false;
    }
}
