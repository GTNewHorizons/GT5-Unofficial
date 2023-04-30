package com.gtnewhorizons.gtnhintergalactic.tile.multi;

import java.util.HashMap;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraftforge.common.util.ForgeDirection;

import org.lwjgl.input.Keyboard;

import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.IConstructable;
import com.gtnewhorizon.structurelib.alignment.enumerable.Flip;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import gregtech.api.interfaces.ISecondaryDescribable;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import micdoodle8.mods.galacticraft.core.util.GCCoreUtil;

public abstract class GT_MetaTileEntity_EnhancedMultiBlockBase_EM extends GT_MetaTileEntity_MultiblockBase_EM
        implements IConstructable, ISecondaryDescribable {

    private static Map<Integer, GT_Multiblock_Tooltip_Builder> tooltips = new HashMap<>();

    protected GT_MetaTileEntity_EnhancedMultiBlockBase_EM(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    protected GT_MetaTileEntity_EnhancedMultiBlockBase_EM(String aName) {
        super(aName);
    }

    @Override
    public boolean onWrenchRightClick(ForgeDirection side, ForgeDirection wrenchingSide, EntityPlayer aPlayer, float aX,
            float aY, float aZ) {
        if (wrenchingSide != getBaseMetaTileEntity().getFrontFacing())
            return super.onWrenchRightClick(side, wrenchingSide, aPlayer, aX, aY, aZ);
        if (aPlayer.isSneaking()) {
            // we won't be allowing horizontal flips, as it can be perfectly emulated by rotating twice and flipping
            // horizontally
            // allowing an extra round of flip make it hard to draw meaningful flip markers in GT_Proxy#drawGrid
            toolSetFlip(getFlip().isHorizontallyFlipped() ? Flip.NONE : Flip.HORIZONTAL);
        } else {
            toolSetRotation(null);
        }
        return true;
    }

    protected abstract GT_Multiblock_Tooltip_Builder createTooltip();

    @Override
    public String[] getDescription() {
        return getCurrentDescription();
    }

    @Override
    public boolean isDisplaySecondaryDescription() {
        return Keyboard.isKeyDown(Keyboard.KEY_LSHIFT);
    }

    public String[] getPrimaryDescription() {
        return getTooltip().getInformation();
    }

    public String[] getSecondaryDescription() {
        return getTooltip().getStructureInformation();
    }

    protected GT_Multiblock_Tooltip_Builder getTooltip() {
        int tId = getBaseMetaTileEntity().getMetaTileID();
        GT_Multiblock_Tooltip_Builder tooltip = tooltips.get(tId);
        if (tooltip == null) {
            tooltip = createTooltip();
            tooltips.put(tId, tooltip);
        }
        return tooltip;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return getTooltip().getStructureHint();
    }

    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> !f.isVerticallyFliped();
    }

    @Override
    public abstract IStructureDefinition<? extends GT_MetaTileEntity_MultiblockBase_EM> getStructure_EM();

    protected static String buildAddedBy(String... contributors) {
        StringBuilder addedByString = new StringBuilder(GCCoreUtil.translate("ig.structure.author") + " ");
        int numOfContributors = contributors.length;
        while (numOfContributors > 1) {
            addedByString.append(
                    contributors[contributors.length - numOfContributors] + EnumChatFormatting.RESET
                            + EnumChatFormatting.GRAY);
            if (numOfContributors > 2) {
                addedByString.append(", ");
            }
            numOfContributors--;
        }
        if (contributors.length > 1) {
            addedByString.append(GCCoreUtil.translate("ig.structure.and") + " ");
        }
        addedByString.append(contributors[contributors.length - 1]);
        return addedByString.toString();
    }
}
