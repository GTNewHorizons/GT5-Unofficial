package gregtech.common.covers;

import net.minecraft.entity.player.EntityPlayer;

import gregtech.api.covers.CoverContext;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.tileentity.ICoverable;
import gregtech.api.interfaces.tileentity.IMachineProgress;
import gregtech.api.util.GTUtility;
import gregtech.common.covers.modes.BlockMode;
import gregtech.common.covers.modes.MachineProcessingCondition;
import gregtech.common.covers.modes.TransferMode;

public abstract class CoverIOBase extends CoverLegacyData {

    protected CoverIOBase(CoverContext context, ITexture coverTexture) {
        super(context, coverTexture);
    }

    public TransferMode getIOMode() {
        return ((0x1 & coverData) == 1) ? TransferMode.IMPORT : TransferMode.EXPORT;
    }

    public void setIOMode(TransferMode mode) {
        coverData = switch (mode) {
            case EXPORT -> coverData & ~0x1;
            case IMPORT -> coverData | 0x1;
        };
    }

    public MachineProcessingCondition getMachineProcessingCondition() {
        if ((coverData % 6) < 2) {
            return MachineProcessingCondition.ALWAYS;
        }
        if ((coverData % 6) == 2 || (coverData % 6) == 3) {
            return MachineProcessingCondition.CONDITIONAL;
        }
        return MachineProcessingCondition.INVERTED;
    }

    public void setMachineProcessingCondition(MachineProcessingCondition mode) {
        coverData = switch (mode) {
            case ALWAYS -> {
                if (coverData > 5) {
                    yield 0x6 | (coverData & ~0xE);
                }
                yield (coverData & ~0xE);
            }
            case CONDITIONAL -> {
                if (coverData > 5) {
                    yield 0x8 | (coverData & ~0xE);
                }
                yield 0x2 | (coverData & ~0xE);
            }
            case INVERTED -> {
                if (coverData > 5) {
                    yield 0xA | (coverData & ~0xE);
                }
                yield (0x4 | (coverData & ~0xE));
            }
        };
    }

    public BlockMode getBlockMode() {
        return coverData < 6 ? BlockMode.BLOCK : BlockMode.ALLOW;
    }

    public void setBlockMode(BlockMode mode) {
        coverData = switch (mode) {
            case ALLOW -> {
                if (coverData <= 5) {
                    yield coverData + 6;
                }
                yield coverData;
            }
            case BLOCK -> {
                if (coverData > 5) {
                    yield coverData - 6;
                }
                yield coverData;
            }
        };
    }

    @Override
    public void doCoverThings(byte aInputRedstone, long aTimer) {
        ICoverable coverable = coveredTile.get();
        if (coverable == null) {
            return;
        }
        if ((this.coverData % 6 > 1) && ((coverable instanceof IMachineProgress machine))) {
            if (machine.isAllowedToWork() != this.coverData % 6 < 4) {
                return;
            }
        }
        doTransfer(coverable);
    }

    protected abstract void doTransfer(ICoverable coverable);

    @Override
    public void onCoverScrewdriverClick(EntityPlayer aPlayer, float aX, float aY, float aZ) {
        coverData = (coverData + (aPlayer.isSneaking() ? -1 : 1)) % 12;
        if (coverData < 0) {
            coverData = 11;
        }
        switch (coverData) {
            case 0 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.export");
            case 1 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.import");
            case 2 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.export_cond");
            case 3 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.import_cond");
            case 4 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.export_invert_cond");
            case 5 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.import_invert_cond");
            case 6 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.export_allow");
            case 7 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.import_allow");
            case 8 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.export_allow_cond");
            case 9 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.import_allow_cond");
            case 10 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.export_allow_invert_cond");
            case 11 -> GTUtility.sendChatTrans(aPlayer, "gt.interact.desc.import_allow_invert_cond");
        }
    }
}
