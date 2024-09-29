package gregtech.common.items.matterManipulator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import com.google.gson.JsonElement;
import com.gtnewhorizon.structurelib.alignment.IAlignment;
import com.gtnewhorizon.structurelib.alignment.IAlignmentProvider;
import com.gtnewhorizon.structurelib.alignment.enumerable.ExtendedFacing;

import appeng.api.parts.IPart;
import appeng.api.parts.IPartHost;
import appeng.api.parts.PartItemStack;
import appeng.api.util.AEColor;
import appeng.helpers.ICustomNameObject;
import appeng.parts.AEBasePart;
import appeng.tile.AEBaseTile;
import appeng.tile.networking.TileCableBus;
import appeng.util.SettingsFrom;
import cpw.mods.fml.common.registry.GameRegistry.UniqueIdentifier;
import gregtech.api.interfaces.metatileentity.IConnectable;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEBasicMachine;
import gregtech.api.util.GTUtility.ItemId;
import gregtech.common.covers.CoverInfo;
import gregtech.common.items.matterManipulator.BlockAnalyzer.IBlockApplyContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.common.util.ForgeDirection;

public class TileAnalysisResult {

    public Byte mConnections = null;
    public Byte mGTColour = null;
    public ForgeDirection mGTFront = null, mGTMainFacing = null;
    public Byte mGTBasicIOFlags = null;
    public ExtendedFacing mGTFacing = null;
    public CoverData[] mCovers = null;
    public Byte mStrongRedstone = null;
    public String mGTCustomName = null;

    public AEColor mAEColour = null;
    public ForgeDirection mAEUp = null, mAEForward = null;
    public JsonElement mAEConfig = null;
    public UniqueIdentifier[] mAEUpgrades = null;
    public String mAECustomName = null;
    public AEPartData[] mAEParts = null;

    private static int counter = 0;
    public static final byte GT_BASIC_IO_PUSH_ITEMS = (byte) (0b1 << counter++);
    public static final byte GT_BASIC_IO_PUSH_FLUIDS = (byte) (0b1 << counter++);
    public static final byte GT_BASIC_IO_DISABLE_FILTER = (byte) (0b1 << counter++);
    public static final byte GT_BASIC_IO_DISABLE_MULTISTACK = (byte) (0b1 << counter++);
    public static final byte GT_BASIC_IO_INPUT_FROM_OUTPUT_SIDE = (byte) (0b1 << counter++);

    private static final ForgeDirection[] ALL_DIRECTIONS = ForgeDirection.values();

    private TileAnalysisResult() {

    }

    public TileAnalysisResult(Supplier<EntityPlayer> fakePlayer, TileEntity te) {
        if (te instanceof IGregTechTileEntity gte) {
            IMetaTileEntity mte = gte.getMetaTileEntity();

            if (gte.getColorization() != -1) mGTColour = gte.getColorization();

            if (mte instanceof MTEBasicMachine basicMachine) {
                mGTMainFacing = basicMachine.mMainFacing;

                byte flags = 0;

                if (basicMachine.mItemTransfer) flags |= GT_BASIC_IO_PUSH_ITEMS;
                if (basicMachine.mFluidTransfer) flags |= GT_BASIC_IO_PUSH_FLUIDS;
                if (basicMachine.mDisableFilter) flags |= GT_BASIC_IO_DISABLE_FILTER;
                if (basicMachine.mDisableMultiStack) flags |= GT_BASIC_IO_DISABLE_MULTISTACK;
                if (basicMachine.mAllowInputFromOutputSide) flags |= GT_BASIC_IO_INPUT_FROM_OUTPUT_SIDE;

                if (flags != 0) mGTBasicIOFlags = flags;
            }

            if (mte instanceof IConnectable connectable) {
                byte con = 0;

                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                    if (connectable.isConnectedAtSide(dir)) {
                        con |= dir.flag;
                    }
                }

                mConnections = con;
            }

            if (mte instanceof IAlignmentProvider provider) {
                IAlignment alignment = provider.getAlignment();

                mGTFacing = alignment != null ? alignment.getExtendedFacing() : null;
            } else {
                mGTFront = BlockAnalyzer.nullIfUnknown(gte.getFrontFacing());
            }

            CoverData[] covers = new CoverData[6];
            boolean hasCover = false;
            byte strongRedstone = 0;

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                if (gte.getCoverIDAtSide(dir) != 0) {
                    covers[dir.ordinal()] = CoverData.fromInfo(gte.getCoverInfoAtSide(dir));
                    hasCover = true;

                    if (gte.getRedstoneOutputStrength(dir)) {
                        strongRedstone |= dir.flag;
                    }
                }
            }

            if (hasCover) mCovers = covers;
            if (strongRedstone != 0) mStrongRedstone = strongRedstone;

            if (mte instanceof ICustomNameObject customName && customName.hasCustomName()) {
                mGTCustomName = customName.getCustomName();
            }
        }

        if (te instanceof AEBaseTile ae) {
            mAEUp = BlockAnalyzer.nullIfUnknown(ae.getUp());
            mAEForward = BlockAnalyzer.nullIfUnknown(ae.getForward());
            mAEConfig = NBTState.toJsonObject(ae.downloadSettings(SettingsFrom.MEMORY_CARD));
            mAECustomName = !(ae instanceof TileCableBus) && ae.hasCustomName() ? ae.getCustomName() : null;

            if (ae instanceof IPartHost partHost) {
                mAEParts = new AEPartData[ALL_DIRECTIONS.length];

                for (ForgeDirection dir : ALL_DIRECTIONS) {
                    if (partHost.getPart(dir) instanceof AEBasePart basePart) {
                        mAEParts[dir.ordinal()] = new AEPartData(fakePlayer.get(), basePart);
                    }
                }
            }
        }
    }

    private static final TileAnalysisResult NO_OP = new TileAnalysisResult();

    public boolean doesAnything() {
        return !this.equals(NO_OP);
    }

    @SuppressWarnings("unused")
    public boolean apply(IBlockApplyContext ctx) {
        TileEntity te = ctx.getTileEntity();

        if (te instanceof IGregTechTileEntity gte) {
            IMetaTileEntity mte = gte.getMetaTileEntity();

            if (mGTColour != null) {
                gte.setColorization(mGTColour);
            }

            if (mte instanceof MTEBasicMachine basicMachine) {
                if (mGTMainFacing != null) basicMachine.mMainFacing = mGTMainFacing;

                mGTMainFacing = basicMachine.mMainFacing;

                basicMachine.mItemTransfer = (mGTBasicIOFlags & GT_BASIC_IO_PUSH_ITEMS) != 0;
                basicMachine.mFluidTransfer = (mGTBasicIOFlags & GT_BASIC_IO_PUSH_FLUIDS) != 0;
                basicMachine.mDisableFilter = (mGTBasicIOFlags & GT_BASIC_IO_DISABLE_FILTER) != 0;
                basicMachine.mDisableMultiStack = (mGTBasicIOFlags & GT_BASIC_IO_DISABLE_MULTISTACK) != 0;
                basicMachine.mAllowInputFromOutputSide = (mGTBasicIOFlags & GT_BASIC_IO_INPUT_FROM_OUTPUT_SIDE)
                    != 0;
            }

            if (mte instanceof IConnectable connectable) {
                for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                    boolean shouldBeConnected = (mConnections & dir.flag) != 0;
                    if (connectable.isConnectedAtSide(dir) != shouldBeConnected) {
                        if (shouldBeConnected) {
                            connectable.connect(dir);
                        } else {
                            connectable.disconnect(dir);
                        }
                    }
                }
            }

            if (mte instanceof IAlignmentProvider provider) {
                IAlignment alignment = provider.getAlignment();

                if (mGTFacing != null && alignment != null) alignment.setExtendedFacing(mGTFacing);
            } else {
                if (mGTFront != null) gte.setFrontFacing(mGTFront);
            }

            for (ForgeDirection dir : ForgeDirection.VALID_DIRECTIONS) {
                CoverData expected = mCovers == null ? null : mCovers[dir.ordinal()];
                CoverInfo actual = new CoverInfo(
                    dir,
                    gte.getCoverIDAtSide(dir),
                    gte,
                    gte.getComplexCoverDataAtSide(dir));

                if (actual == null && expected != null) {
                    installCover(ctx, gte, dir, expected);
                } else if (actual != null && expected == null) {
                    removeCover(ctx, gte, dir);
                } else if (actual != null && expected != null) {
                    if (actual.getCoverID() != expected.getCoverID()) {
                        removeCover(ctx, gte, dir);
                        installCover(ctx, gte, dir, expected);
                    } else if (!Objects.equals(actual.getCoverData(), expected.getCoverData())) {
                        updateCover(ctx, gte, dir, expected);
                    }
                }

                if (mStrongRedstone != null) {
                    gte.setRedstoneOutputStrength(dir, (mStrongRedstone & dir.flag) != 0);
                }
            }

            if (mte instanceof ICustomNameObject customName && mGTCustomName != null) {
                customName.setCustomName(mGTCustomName);
            }
        }

        if (te instanceof AEBaseTile ae) {
            if (mAEUp != null && mAEForward != null) {
                ae.setOrientation(mAEForward, mAEUp);
            }

            if (mAEConfig != null) {
                ae.uploadSettings(SettingsFrom.MEMORY_CARD, (NBTTagCompound) NBTState.toNbt(mAEConfig));
            }

            if (mAECustomName != null && !(ae instanceof TileCableBus)) {
                ae.setCustomName(mAECustomName);
            }

            if (ae instanceof IPartHost partHost && mAEParts != null) {
                for (ForgeDirection dir : ALL_DIRECTIONS) {
                    IPart part = partHost.getPart(dir);
                    AEPartData expected = mAEParts[dir.ordinal()];

                    ItemId actualItem = part == null ? null
                        : ItemId.createWithoutNBT(part.getItemStack(PartItemStack.Break));
                    ItemId expectedItem = expected == null ? null
                        : ItemId.createWithoutNBT(expected.getPartStack());

                    if ((expectedItem == null || !Objects.equals(actualItem, expectedItem)) && actualItem != null) {
                        removePart(ctx, partHost, dir);
                        actualItem = null;
                    }

                    if (actualItem == null && expectedItem != null) {
                        if (!installPart(ctx, partHost, dir, expected)) {
                            return false;
                        }
                    }

                    if (expected != null) {
                        if (!expected.updatePart(ctx, partHost, dir)) {
                            return false;
                        }
                    }
                }
            }
        }

        return true;
    }

    private void removeCover(IBlockApplyContext context, IGregTechTileEntity gte, ForgeDirection side) {
        if (gte.getCoverIDAtSide(side) != 0) {
            context.givePlayerItems(gte.removeCoverAtSide(side, true));
        }
    }

    private void installCover(IBlockApplyContext context, IGregTechTileEntity gte, ForgeDirection side,
        CoverData cover) {
        if (gte.getCoverIDAtSide(side) == 0 && gte.canPlaceCoverItemAtSide(side, cover.getCover())
            && context.tryConsumeItems(cover.getCover())) {
            gte.setCoverIdAndDataAtSide(
                side,
                cover.getCoverID(),
                cover.getCoverBehaviour()
                    .allowsCopyPasteTool() ? cover.getCoverData() : null);
        }
    }

    private void updateCover(IBlockApplyContext context, IGregTechTileEntity gte, ForgeDirection side,
        CoverData target) {
        if (gte.getCoverIDAtSide(side) == target.getCoverID() && gte.getCoverBehaviorAtSideNew(side)
            .allowsCopyPasteTool()) {
            gte.setCoverDataAtSide(side, target.getCoverData());
        }
    }

    private void removePart(IBlockApplyContext context, IPartHost partHost, ForgeDirection side) {
        IPart part = partHost.getPart(side);

        if (part == null) return;

        List<ItemStack> drops = new ArrayList<>();

        part.getDrops(drops, false);

        context.givePlayerItems(drops.toArray(new ItemStack[drops.size()]));

        ItemStack partStack = part.getItemStack(PartItemStack.Break);

        NBTTagCompound tag = partStack.getTagCompound();

        if (tag != null) {
            tag.removeTag("display");

            if (tag.hasNoTags()) {
                partStack.setTagCompound(null);
            }
        }

        context.givePlayerItems(partStack);

        partHost.removePart(side, false);
    }

    private boolean installPart(IBlockApplyContext context, IPartHost partHost, ForgeDirection side,
        AEPartData partData) {
        ItemStack partStack = partData.getPartStack();

        if (!partHost.canAddPart(partStack, side)) {
            return false;
        }

        context.tryConsumeItems(partStack);

        if (partHost.addPart(partStack, side, context.getPlacingPlayer()) == null) {
            context.givePlayerItems(partStack);
            return false;
        }

        return true;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((mConnections == null) ? 0 : mConnections.hashCode());
        result = prime * result + ((mGTColour == null) ? 0 : mGTColour.hashCode());
        result = prime * result + ((mGTFront == null) ? 0 : mGTFront.hashCode());
        result = prime * result + ((mGTMainFacing == null) ? 0 : mGTMainFacing.hashCode());
        result = prime * result + ((mGTBasicIOFlags == null) ? 0 : mGTBasicIOFlags.hashCode());
        result = prime * result + ((mGTFacing == null) ? 0 : mGTFacing.hashCode());
        result = prime * result + Arrays.hashCode(mCovers);
        result = prime * result + ((mStrongRedstone == null) ? 0 : mStrongRedstone.hashCode());
        result = prime * result + ((mGTCustomName == null) ? 0 : mGTCustomName.hashCode());
        result = prime * result + ((mAEColour == null) ? 0 : mAEColour.hashCode());
        result = prime * result + ((mAEUp == null) ? 0 : mAEUp.hashCode());
        result = prime * result + ((mAEForward == null) ? 0 : mAEForward.hashCode());
        result = prime * result + ((mAEConfig == null) ? 0 : mAEConfig.hashCode());
        result = prime * result + Arrays.hashCode(mAEUpgrades);
        result = prime * result + ((mAECustomName == null) ? 0 : mAECustomName.hashCode());
        result = prime * result + Arrays.hashCode(mAEParts);
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        TileAnalysisResult other = (TileAnalysisResult) obj;
        if (mConnections == null) {
            if (other.mConnections != null)
                return false;
        } else if (!mConnections.equals(other.mConnections))
            return false;
        if (mGTColour == null) {
            if (other.mGTColour != null)
                return false;
        } else if (!mGTColour.equals(other.mGTColour))
            return false;
        if (mGTFront != other.mGTFront)
            return false;
        if (mGTMainFacing != other.mGTMainFacing)
            return false;
        if (mGTBasicIOFlags == null) {
            if (other.mGTBasicIOFlags != null)
                return false;
        } else if (!mGTBasicIOFlags.equals(other.mGTBasicIOFlags))
            return false;
        if (mGTFacing != other.mGTFacing)
            return false;
        if (!Arrays.equals(mCovers, other.mCovers))
            return false;
        if (mStrongRedstone == null) {
            if (other.mStrongRedstone != null)
                return false;
        } else if (!mStrongRedstone.equals(other.mStrongRedstone))
            return false;
        if (mGTCustomName == null) {
            if (other.mGTCustomName != null)
                return false;
        } else if (!mGTCustomName.equals(other.mGTCustomName))
            return false;
        if (mAEColour != other.mAEColour)
            return false;
        if (mAEUp != other.mAEUp)
            return false;
        if (mAEForward != other.mAEForward)
            return false;
        if (mAEConfig == null) {
            if (other.mAEConfig != null)
                return false;
        } else if (!mAEConfig.equals(other.mAEConfig))
            return false;
        if (!Arrays.equals(mAEUpgrades, other.mAEUpgrades))
            return false;
        if (mAECustomName == null) {
            if (other.mAECustomName != null)
                return false;
        } else if (!mAECustomName.equals(other.mAECustomName))
            return false;
        if (!Arrays.equals(mAEParts, other.mAEParts))
            return false;
        return true;
    }
}