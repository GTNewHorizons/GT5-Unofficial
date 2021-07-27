package com.github.bartimaeusnek.crossmod.tectech.tileentites.multi.GT_Replacement;

import com.github.bartimaeusnek.crossmod.tectech.helper.CoilAdder;
import com.github.technus.tectech.mechanics.structure.IStructureDefinition;
import com.github.technus.tectech.mechanics.structure.StructureDefinition;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTech_API;
import gregtech.api.enums.HeatingCoilLevel;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Utility;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.FluidStack;

import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.ADV_STR_CHECK;
import static com.github.bartimaeusnek.bartworks.util.BW_Tooltip_Reference.TT_BLUEPRINT;
import static com.github.technus.tectech.mechanics.structure.StructureUtility.*;

public class TT_PyrolyseOven extends TT_Abstract_GT_Replacement_Coils {
    private static final int TEXTURE_INDEX = 1090;
    private static final int  pollutionPerTick = 30;

    public TT_PyrolyseOven(Object unused, Object unused2) {
        super(1159, "multimachine.pyro", "Pyrolyse Oven");
    }

    public TT_PyrolyseOven(String aName) {
        super(aName);
    }

    private static final Block casingBlock;
    private static final int casingMeta;

    private int blocks = 0;

    static {
        if (Loader.isModLoaded("dreamcraft")) {
            casingBlock = GameRegistry.findBlock("dreamcraft", "gt.blockcasingsNH");
            casingMeta = 2;
        } else {
            casingBlock = GregTech_API.sBlockCasings1;
            casingMeta = 0;
        }
    }

    private static final IStructureDefinition<TT_PyrolyseOven> STRUCTURE_DEFINITION = StructureDefinition
            .<TT_PyrolyseOven>builder()
            .addShape("main",
                    transpose(new String[][]{
                            {"AAAAA", "ACCCA", "ACCCA", "ACCCA", "AAAAA"},
                            {"AAAAA", "A---A", "A---A", "A---A", "AAAAA"},
                            {"AAAAA", "A---A", "A---A", "A---A", "AAAAA"},
                            {"BB~BB", "BDDDB", "BDDDB", "BDDDB", "BBBBB"}
                    })
            ).addElement(
                    'D',
                    CoilAdder.getINSTANCE()
            ).addElement(
                    'A',
                    onElementPass(x -> x.blocks++,
                            ofBlock(
                                casingBlock,
                                casingMeta
                            )
                    )
            ).addElement(
                    'B',
                    ofChain(
                            ofHatchAdder(
                                    TT_PyrolyseOven::addClassicOutputToMachineList, TEXTURE_INDEX,
                                    1
                            ),
                            ofHatchAdder(
                                    TT_PyrolyseOven::addEnergyIOToMachineList, TEXTURE_INDEX,
                                    1
                            ),
                            ofHatchAdder(
                                    TT_PyrolyseOven::addClassicMaintenanceToMachineList, TEXTURE_INDEX,
                                   1
                            ),
                            onElementPass(x -> x.blocks++,
                                    ofBlock(
                                            casingBlock,
                                            casingMeta
                                    )
                            )
                    )

            ).addElement(
                    'C',
                    ofChain(
                            ofHatchAdder(
                                    TT_PyrolyseOven::addClassicInputToMachineList, TEXTURE_INDEX,
                                    2
                            ),
                            ofHatchAdder(
                                    TT_PyrolyseOven::addClassicMufflerToMachineList, TEXTURE_INDEX,
                                    2
                            ),
                            onElementPass(x -> x.blocks++,
                                    ofBlock(
                                            casingBlock,
                                            casingMeta
                                    )
                            )
                    )

            )
            .build();

    @Override
    public IStructureDefinition<TT_PyrolyseOven> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    protected boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        this.setCoilHeat(HeatingCoilLevel.None);
        this.blocks = 0;
        return this.structureCheck_EM("main", 2,3,0) && this.blocks >= 60;
    }

    private static final String[] desc = new String[]{
            "Coke Oven",
            "Controller block for the Pyrolyse Oven",
            "Industrial Charcoal producer",
            "Processing speed scales linearly with Coil tier:",
            "CuNi: 50%, FeAlCr: 100%, Ni4Cr: 150%, Fe50CW: 200%, etc.",
            "EU/t is not affected by Coil tier",
            "Creates up to: " + 20 * pollutionPerTick + " Pollution per Second",
            ADV_STR_CHECK,
            TT_BLUEPRINT
    };

    @Override
    public String[] getDescription() {
        return desc;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, byte aSide, byte aFacing, byte aColorIndex, boolean aActive, boolean aRedstone) {
        if (aSide == aFacing) {
            return new ITexture[]{Textures.BlockIcons.casingTexturePages[8][66], TextureFactory.of(aActive ? TextureFactory.of(TextureFactory.of(Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN_ACTIVE), TextureFactory.builder().addIcon(Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN_ACTIVE_GLOW).glow().build()) : TextureFactory.of(TextureFactory.of(Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN), TextureFactory.builder().addIcon(Textures.BlockIcons.OVERLAY_FRONT_PYROLYSE_OVEN_GLOW).glow().build()))};
        }
        return new ITexture[]{Textures.BlockIcons.casingTexturePages[8][66]};
    }

    @Override
    public boolean checkRecipe_EM(ItemStack aStack) {
        ItemStack[] tInputs = getCompactedInputs();
        FluidStack[] tFluids = getCompactedFluids();

        if (tInputs.length <= 0)
            return false;

        long tVoltage = getMaxInputVoltage();
        byte tTier = (byte) Math.max(1, GT_Utility.getTier(tVoltage));
        GT_Recipe tRecipe = GT_Recipe.GT_Recipe_Map.sPyrolyseRecipes.findRecipe(getBaseMetaTileEntity(), false, gregtech.api.enums.GT_Values.V[tTier], tFluids, tInputs);

        if (tRecipe == null || !tRecipe.isRecipeInputEqual(true, tFluids, tInputs))
            return false;
        setEfficiencyAndOc(tRecipe);
        //In case recipe is too OP for that machine
        if (mMaxProgresstime == Integer.MAX_VALUE - 1 && mEUt == Integer.MAX_VALUE - 1)
            return false;
        if (this.mEUt > 0)
            this.mEUt = (-this.mEUt);
        this.mMaxProgresstime = Math.max(mMaxProgresstime * 2 / (1 + this.heatingCoilLevel.getTier()), 1);
        if (tRecipe.mOutputs.length > 0)
            this.mOutputItems = new ItemStack[]{tRecipe.getOutput(0)};
        if (tRecipe.mFluidOutputs.length > 0)
            this.mOutputFluids = new FluidStack[]{tRecipe.getFluidOutput(0)};
        updateSlots();
        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new TT_PyrolyseOven(this.mName);
    }

    @Override
    public int getPollutionPerTick(ItemStack aStack) {
        return pollutionPerTick;
    }

    private static final String[] sfStructureDescription = new String[] {
            "0 - Air",
            "1 - Output Hatch, Output Bus, Energy Hatch, Maintenance Hatch, Casing",
            "2 - Input Hatch, Input Bus, Muffler Hatch, Casing",
            "60 Casings at least!"
    };

    @Override
    public String[] getStructureDescription(ItemStack itemStack) {
        return sfStructureDescription;
    }

    @Override
    public void construct(ItemStack itemStack, boolean b) {
        this.structureBuild_EM("main", 2,3,0, b, itemStack);
    }
}
