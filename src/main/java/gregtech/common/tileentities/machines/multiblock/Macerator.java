package gregtech.common.tileentities.machines.multiblock;

import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofChain;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.Textures.BlockIcons.MACHINE_CASINGS;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW;
import static gregtech.api.enums.Textures.BlockIcons.OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.ENERGY_IN;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.FLUID_IN;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.FLUID_OUT;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.ITEM_IN;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.ITEM_OUT;
import static gregtech.api.multitileentity.multiblock.base.MultiBlockPart.NOTHING;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.apache.commons.lang3.tuple.Pair;

import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizon.structurelib.util.Vec3Impl;

import gregtech.api.enums.SoundResource;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.ITexture;
import gregtech.api.multitileentity.multiblock.base.StackableController;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Recipe;
import gregtech.api.util.GT_Recipe.GT_Recipe_Map;

public class Macerator extends StackableController<Macerator> {

    private static IStructureDefinition<Macerator> STRUCTURE_DEFINITION = null;

    public Macerator() {
        super();
    }

    @Override
    public String getTileEntityName() {
        return "gt.multitileentity.multiblock.macerator";
    }

    @Override
    public IStructureDefinition<Macerator> getStructureDefinition() {
        if (STRUCTURE_DEFINITION == null) {
            STRUCTURE_DEFINITION = StructureDefinition.<Macerator>builder()
                .addShape(STACKABLE_TOP, transpose(new String[][] { { " CCC ", "CCCCC", "CCCCC", "CCCCC", " CCC " }, }))
                .addShape(
                    STACKABLE_MIDDLE,
                    transpose(new String[][] { { "  BBB  ", " B---B ", "DC---CD", " B---B ", "  BBB  " }, }))
                .addShape(
                    STACKABLE_BOTTOM,
                    transpose(new String[][] { { " G~F ", "AAAAA", "AAAAA", "AAAAA", " AAA " }, }))
                .addElement('A', ofChain(addMultiTileCasing("gt.multitileentity.casings", getCasingMeta(), ENERGY_IN)))
                .addElement(
                    'B',
                    ofChain(
                        addMultiTileCasing(
                            "gt.multitileentity.casings",
                            getCasingMeta(),
                            FLUID_IN | ITEM_IN | FLUID_OUT | ITEM_OUT)))
                .addElement('C', addMultiTileCasing("gt.multitileentity.casings", getCasingMeta(), NOTHING))
                .addElement('D', addMultiTileCasing("gt.multitileentity.casings", getCasingMeta(), NOTHING))
                .addElement(
                    'F',
                    ofChain(
                        addMultiTileCasing("gt.multitileentity.casings", 20001, NOTHING),
                        addMultiTileCasing("gt.multitileentity.casings", 20002, NOTHING)))
                .addElement('G', addMultiTileCasing("gt.multitileentity.casings", 10000, NOTHING))
                .build();
        }
        return STRUCTURE_DEFINITION;
    }

    @Override
    public short getCasingRegistryID() {
        return 0;
    }

    @Override
    public int getCasingMeta() {
        return 18000;
    }

    @Override
    public boolean hasTop() {
        return true;
    }

    @Override
    protected GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType("Macerator")
            .addInfo("Controller for the Macerator")
            .addSeparator()
            .beginVariableStructureBlock(7, 9, 2 + getMinStacks(), 2 + getMaxStacks(), 7, 9, true)
            .addController("Bottom Front Center")
            .addCasingInfoExactly("Test Casing", 60, false)
            .addEnergyHatch("Any bottom layer casing")
            .addInputHatch("Any non-optional external facing casing on the stacks")
            .addInputBus("Any non-optional external facing casing on the stacks")
            .addOutputHatch("Any non-optional external facing casing on the stacks")
            .addOutputBus("Any non-optional external facing casing on the stacks")
            .addStructureInfo(
                String.format("Stackable middle stacks between %d-%d time(s).", getMinStacks(), getMaxStacks()))
            .toolTipFinisher("Wildcard");
        return tt;
    }

    @Override
    public int getMinStacks() {
        return 1;
    }

    @Override
    public int getMaxStacks() {
        return 10;
    }

    @Override
    public Vec3Impl getStartingStructureOffset() {
        return new Vec3Impl(2, 0, 0);
    }

    @Override
    public Vec3Impl getStartingStackOffset() {
        return new Vec3Impl(1, 1, 0);
    }

    @Override
    public Vec3Impl getPerStackOffset() {
        return new Vec3Impl(0, 1, 0);
    }

    @Override
    public Vec3Impl getAfterLastStackOffset() {
        return new Vec3Impl(-1, 0, 0);
    }

    @Override
    public ITexture[] getTexture(Block aBlock, byte aSide, boolean isActive, int aRenderPass) {
        // TODO: MTE(Texture)
        if (facing == aSide) {
            return new ITexture[] {
                // Base Texture
                MACHINE_CASINGS[1][0],
                // Active
                isActive() ? TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE)
                    .extFacing()
                    .build()
                    : TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE)
                        .extFacing()
                        .build(),
                // Active Glow
                isActive() ? TextureFactory.builder()
                    .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build()
                    : TextureFactory.builder()
                        .addIcon(OVERLAY_FRONT_ELECTRIC_BLAST_FURNACE_GLOW)
                        .extFacing()
                        .glow()
                        .build() };
        }
        // Base Texture
        return new ITexture[] { MACHINE_CASINGS[1][0] };
    }

    @Override
    protected boolean checkRecipe() {
        if (isSeparateInputs()) {
            for (Pair<ItemStack[], String> tItemInputs : getItemInputsForEachInventory()) {
                if (processRecipe(tItemInputs.getLeft(), tItemInputs.getRight())) {
                    return true;
                }
            }
            return false;
        } else {
            ItemStack[] tItemInputs = getInventoriesForInput().getStacks()
                .toArray(new ItemStack[0]);
            return processRecipe(tItemInputs, null);
        }
    }

    private boolean processRecipe(ItemStack[] aItemInputs, String aInventory) {
        GT_Recipe_Map tRecipeMap = GT_Recipe_Map.sMaceratorRecipes;
        GT_Recipe tRecipe = tRecipeMap.findRecipe(this, false, TierEU.IV, null, aItemInputs);
        if (tRecipe == null) {
            return false;
        }

        if (!tRecipe.isRecipeInputEqual(true, false, 1, null, aItemInputs)) {
            return false;
        }

        setDuration(tRecipe.mDuration);
        setEut(tRecipe.mEUt);

        setItemOutputs(aInventory, tRecipe.mOutputs);
        return true;
    }

    @Override
    protected ResourceLocation getActivitySoundLoop() {
        return SoundResource.IC2_MACHINES_MACERATOR_OP.resourceLocation;
    }
}
