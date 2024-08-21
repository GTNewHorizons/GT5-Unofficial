package com.github.technus.tectech.thing.metaTileEntity.multi;

import static com.github.technus.tectech.recipe.TT_recipeAdder.nullItem;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.textureOffset;
import static com.github.technus.tectech.thing.casing.GT_Block_CasingsTT.texturePage;
import static com.github.technus.tectech.thing.casing.TT_Container_Casings.sBlockCasingsTT;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GT_Values.V;
import static gregtech.api.util.GT_StructureUtility.buildHatchAdder;
import static net.minecraft.util.StatCollector.translateToLocal;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.ForgeDirection;

import org.jetbrains.annotations.NotNull;

import com.github.technus.tectech.Reference;
import com.github.technus.tectech.mechanics.dataTransport.InventoryDataPacket;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_InputDataItems;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_OutputDataItems;
import com.github.technus.tectech.thing.metaTileEntity.hatch.GT_MetaTileEntity_Hatch_WirelessOutputDataItems;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.GT_MetaTileEntity_MultiblockBase_EM;
import com.github.technus.tectech.thing.metaTileEntity.multi.base.render.TT_RenderedExtendedFacingTexture;
import com.github.technus.tectech.util.CommonValues;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IItemSource;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import gregtech.api.enums.Textures;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch;
import gregtech.api.metatileentity.implementations.GT_MetaTileEntity_Hatch_DataAccess;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.SimpleCheckRecipeResult;
import gregtech.api.util.GT_Multiblock_Tooltip_Builder;
import gregtech.api.util.GT_Utility;
import gregtech.api.util.IGT_HatchAdder;
import gregtech.common.WirelessComputationPacket;
import gregtech.common.WirelessDataStore;

public class GT_MetaTileEntity_EM_dataBank extends GT_MetaTileEntity_MultiblockBase_EM
    implements ISurvivalConstructable {

    // region variables
    private final ArrayList<GT_MetaTileEntity_Hatch_OutputDataItems> eStacksDataOutputs = new ArrayList<>();
    private final ArrayList<GT_MetaTileEntity_Hatch_WirelessOutputDataItems> eWirelessStacksDataOutputs = new ArrayList<>();
    private final ArrayList<IInventory> eDataAccessHatches = new ArrayList<>();
    private boolean slave = false;
    private boolean wirelessModeEnabled = false;
    // endregion

    // region structure
    private static final String[] description = new String[] {
        EnumChatFormatting.AQUA + translateToLocal("tt.keyphrase.Hint_Details") + ":",
        translateToLocal("gt.blockmachines.multimachine.em.databank.hint.0"), // 1 - Classic Hatches or high power
                                                                              // casing
        translateToLocal("gt.blockmachines.multimachine.em.databank.hint.1"), // 2 - Data Access/Data Bank Master
                                                                              // Hatches or
        // computer casing
    };

    private static final IStructureDefinition<GT_MetaTileEntity_EM_dataBank> STRUCTURE_DEFINITION = IStructureDefinition
        .<GT_MetaTileEntity_EM_dataBank>builder()
        .addShape(
            "main",
            transpose(
                new String[][] { { "BCCCB", "BDDDB", "BDDDB" }, { "BC~CB", "BAAAB", "BDDDB" },
                    { "BCCCB", "BDDDB", "BDDDB" } }))
        .addElement('A', ofBlock(sBlockCasingsTT, 1))
        .addElement('B', ofBlock(sBlockCasingsTT, 2))
        .addElement('C', classicHatches(textureOffset, 1, sBlockCasingsTT, 0))
        .addElement(
            'D',
            buildHatchAdder(GT_MetaTileEntity_EM_dataBank.class)
                .atLeast(
                    DataBankHatches.OutboundConnector,
                    DataBankHatches.InboundConnector,
                    DataBankHatches.WirelessOutboundConnector)
                .casingIndex(textureOffset + 1)
                .dot(2)
                .buildAndChain(DataBankHatches.DataStick.newAnyOrCasing(textureOffset + 1, 2, sBlockCasingsTT, 1)))
        .build();
    // endregion

    public GT_MetaTileEntity_EM_dataBank(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    public GT_MetaTileEntity_EM_dataBank(String aName) {
        super(aName);
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity aTileEntity) {
        return new GT_MetaTileEntity_EM_dataBank(mName);
    }

    @Override
    public GT_Multiblock_Tooltip_Builder createTooltip() {
        final GT_Multiblock_Tooltip_Builder tt = new GT_Multiblock_Tooltip_Builder();
        tt.addMachineType(translateToLocal("gt.blockmachines.multimachine.em.databank.name")) // Machine Type: Data Bank
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.databank.desc.0")) // Controller block of
                                                                                           // the Data Bank
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.databank.desc.1")) // Used to supply
                                                                                           // Assembling Lines
            // with more Data Sticks
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.databank.desc.2")) // and give multiple
                                                                                           // Assembling Lines access to
                                                                                           // the same Data
            .addInfo(translateToLocal("gt.blockmachines.multimachine.em.databank.desc.3")) // Use screwdriver to toggle
                                                                                           // wireless mode

            // Stick
            .addInfo(translateToLocal("tt.keyword.Structure.StructureTooComplex")) // The structure is too complex!
            .addSeparator()
            .beginStructureBlock(5, 3, 3, false)
            .addOtherStructurePart(
                translateToLocal("tt.keyword.Structure.DataAccessHatch"),
                translateToLocal("tt.keyword.Structure.AnyComputerCasing"),
                2) // Data Access Hatch: Any Computer Casing
            .addOtherStructurePart(
                translateToLocal("gt.blockmachines.hatch.dataoutass.tier.07.name"),
                translateToLocal("tt.keyword.Structure.AnyComputerCasing"),
                2) // Data Bank Master Connector: Any Computer Casing
            .addEnergyHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Energy Hatch: Any
                                                                                            // High Power Casing
            .addMaintenanceHatch(translateToLocal("tt.keyword.Structure.AnyHighPowerCasing"), 1) // Maintenance
                                                                                                 // Hatch: Any High
                                                                                                 // Power Casing
            .toolTipFinisher(CommonValues.TEC_MARK_EM);
        return tt;
    }

    @Override
    public boolean checkMachine_EM(IGregTechTileEntity iGregTechTileEntity, ItemStack itemStack) {
        eDataAccessHatches.clear();
        eStacksDataOutputs.clear();
        eWirelessStacksDataOutputs.clear();
        slave = false;
        return structureCheck_EM("main", 2, 1, 0);
    }

    @Override
    @NotNull
    protected CheckRecipeResult checkProcessing_EM() {
        if (eDataAccessHatches.size() > 0 && (eStacksDataOutputs.size() > 0 || eWirelessStacksDataOutputs.size() > 0)) {
            mEUt = -(int) V[slave ? 6 : 4];
            eAmpereFlow = 1
                + (long) (eStacksDataOutputs.size() + eWirelessStacksDataOutputs.size()) * eDataAccessHatches.size();
            mMaxProgresstime = 20;
            mEfficiencyIncrease = 10000;
            return SimpleCheckRecipeResult.ofSuccess("providing_data");
        }
        return SimpleCheckRecipeResult.ofFailure("no_data");
    }

    @Override
    public void outputAfterRecipe_EM() {
        ArrayList<ItemStack> stacks = new ArrayList<>();
        for (IInventory dataAccess : eDataAccessHatches) {
            int count = dataAccess.getSizeInventory();
            for (int i = 0; i < count; i++) {
                ItemStack stack = dataAccess.getStackInSlot(i);
                if (stack != null) {
                    stacks.add(stack);
                }
            }
        }
        if (stacks.size() > 0) {
            ItemStack[] arr = stacks.toArray(nullItem);
            for (GT_MetaTileEntity_Hatch_OutputDataItems hatch : eStacksDataOutputs) {
                hatch.q = new InventoryDataPacket(arr);
            }
            if (wirelessModeEnabled) {
                for (GT_MetaTileEntity_Hatch_WirelessOutputDataItems hatch : eWirelessStacksDataOutputs) {
                    hatch.dataPacket = new InventoryDataPacket(arr);
                }
            }
        } else {
            for (GT_MetaTileEntity_Hatch_OutputDataItems hatch : eStacksDataOutputs) {
                hatch.q = null;
            }
            for (GT_MetaTileEntity_Hatch_WirelessOutputDataItems hatch : eWirelessStacksDataOutputs) {
                hatch.dataPacket = null;
            }
        }
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side, ForgeDirection facing,
        int colorIndex, boolean aActive, boolean aRedstone) {
        if (side == facing) {
            return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][1],
                new TT_RenderedExtendedFacingTexture(
                    aActive ? GT_MetaTileEntity_MultiblockBase_EM.ScreenON
                        : GT_MetaTileEntity_MultiblockBase_EM.ScreenOFF) };
        }
        return new ITexture[] { Textures.BlockIcons.casingTexturePages[texturePage][1] };
    }

    public static final ResourceLocation activitySound = new ResourceLocation(Reference.MODID + ":fx_hi_freq");

    @Override
    @SideOnly(Side.CLIENT)
    protected ResourceLocation getActivitySound() {
        return activitySound;
    }

    public final boolean addDataBankHatchToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (aTileEntity == null) {
            return false;
        }
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) {
            return false;
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_WirelessOutputDataItems) {
            ((GT_MetaTileEntity_Hatch_WirelessOutputDataItems) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eWirelessStacksDataOutputs.add((GT_MetaTileEntity_Hatch_WirelessOutputDataItems) aMetaTileEntity);
        }
        if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_OutputDataItems) {
            ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
            return eStacksDataOutputs.add((GT_MetaTileEntity_Hatch_OutputDataItems) aMetaTileEntity);
        } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_DataAccess
            && !(aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputDataItems)) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                return eDataAccessHatches.add(aMetaTileEntity);
            } else if (aMetaTileEntity instanceof GT_MetaTileEntity_Hatch_InputDataItems) {
                ((GT_MetaTileEntity_Hatch) aMetaTileEntity).updateTexture(aBaseCasingIndex);
                slave = true;
                return eDataAccessHatches.add(aMetaTileEntity);
            }
        return false;
    }

    @Override
    public void onPreTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPreTick(aBaseMetaTileEntity, aTick);
        // Every 200 ticks, clear wireless data store so hatches need to provide their data again in
        // their onPostTick() call. This also happens every 200 ticks
        if (mMachine && aBaseMetaTileEntity.isActive() && wirelessModeEnabled && aTick % 200 == 0) {
            WirelessDataStore wirelessStore = WirelessDataStore
                .getWirelessDataSticks(aBaseMetaTileEntity.getOwnerUuid());
            wirelessStore.clearData();

            // After reset, clear uploadedSinceReset of all connected hatches
            for (GT_MetaTileEntity_Hatch_WirelessOutputDataItems hatch : eWirelessStacksDataOutputs) {
                hatch.uploadedSinceReset = false;
            }
        }
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ) {
        if (getBaseMetaTileEntity().isServerSide()) {
            wirelessModeEnabled = !wirelessModeEnabled;
            if (wirelessModeEnabled) {
                GT_Utility.sendChatToPlayer(aPlayer, "Wireless mode enabled");
                WirelessComputationPacket.enableWirelessNetWork(getBaseMetaTileEntity());
            } else {
                GT_Utility.sendChatToPlayer(aPlayer, "Wireless mode disabled");
                WirelessComputationPacket.disableWirelessNetWork(getBaseMetaTileEntity());
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setBoolean("wirelessModeEnabled", wirelessModeEnabled);
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        if (aNBT.hasKey("wirelessModeEnabled")) {
            wirelessModeEnabled = aNBT.getBoolean("wirelessModeEnabled");
        } else {
            wirelessModeEnabled = false;
        }
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        structureBuild_EM("main", 2, 1, 0, stackSize, hintsOnly);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, IItemSource source, EntityPlayerMP actor) {
        if (mMachine) return -1;
        return survivialBuildPiece("main", stackSize, 2, 1, 0, elementBudget, source, actor, false, true);
    }

    @Override
    public IStructureDefinition<GT_MetaTileEntity_EM_dataBank> getStructure_EM() {
        return STRUCTURE_DEFINITION;
    }

    @Override
    public String[] getStructureDescription(ItemStack stackSize) {
        return description;
    }

    private enum DataBankHatches implements IHatchElement<GT_MetaTileEntity_EM_dataBank> {

        DataStick(GT_MetaTileEntity_Hatch_DataAccess.class) {

            @Override
            public long count(GT_MetaTileEntity_EM_dataBank t) {
                return t.eDataAccessHatches.size();
            }
        },
        OutboundConnector(GT_MetaTileEntity_Hatch_OutputDataItems.class) {

            @Override
            public long count(GT_MetaTileEntity_EM_dataBank t) {
                return t.eStacksDataOutputs.size();
            }
        },
        InboundConnector(GT_MetaTileEntity_Hatch_InputDataItems.class) {

            @Override
            public long count(GT_MetaTileEntity_EM_dataBank t) {
                return t.eDataAccessHatches.size();
            }
        },
        WirelessOutboundConnector(GT_MetaTileEntity_Hatch_WirelessOutputDataItems.class) {

            @Override
            public long count(GT_MetaTileEntity_EM_dataBank t) {
                return t.eWirelessStacksDataOutputs.size();
            }
        };

        private final List<? extends Class<? extends IMetaTileEntity>> mteClasses;

        @SafeVarargs
        DataBankHatches(Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        @Override
        public IGT_HatchAdder<? super GT_MetaTileEntity_EM_dataBank> adder() {
            return GT_MetaTileEntity_EM_dataBank::addDataBankHatchToMachineList;
        }
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
}
