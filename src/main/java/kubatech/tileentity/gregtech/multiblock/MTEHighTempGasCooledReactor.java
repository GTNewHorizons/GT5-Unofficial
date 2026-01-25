/*
 * spotless:off
 * KubaTech - Gregtech Addon
 * Copyright (C) 2022 - 2025  kuba6000
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this library. If not, see <https://www.gnu.org/licenses/>.
 * spotless:on
 */

package kubatech.tileentity.gregtech.multiblock;

import static com.gtnewhorizon.gtnhlib.util.numberformatting.NumberFormatUtil.formatNumber;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.ofBlock;
import static com.gtnewhorizon.structurelib.structure.StructureUtility.transpose;
import static gregtech.api.enums.GTValues.AuthorKuba;
import static gregtech.api.enums.GTValues.AuthorPxx500;
import static gregtech.api.util.GTStructureUtility.buildHatchAdder;
import static gregtech.api.util.GTUtility.validMTEList;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

import org.jetbrains.annotations.NotNull;

import com.gtnewhorizon.structurelib.alignment.IAlignmentLimits;
import com.gtnewhorizon.structurelib.alignment.constructable.ISurvivalConstructable;
import com.gtnewhorizon.structurelib.structure.IStructureDefinition;
import com.gtnewhorizon.structurelib.structure.ISurvivalBuildEnvironment;
import com.gtnewhorizon.structurelib.structure.StructureDefinition;
import com.gtnewhorizons.modularui.api.drawable.Text;
import com.gtnewhorizons.modularui.api.math.Alignment;
import com.gtnewhorizons.modularui.api.widget.Widget;
import com.gtnewhorizons.modularui.common.widget.DynamicPositionedColumn;
import com.gtnewhorizons.modularui.common.widget.DynamicTextWidget;

import bartworks.common.items.SimpleSubItemClass;
import bartworks.system.material.WerkstoffLoader;
import cpw.mods.fml.common.registry.GameRegistry;
import gregtech.api.GregTechAPI;
import gregtech.api.enums.GTValues;
import gregtech.api.enums.HatchElement;
import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.Textures;
import gregtech.api.enums.TierEU;
import gregtech.api.interfaces.IHatchElement;
import gregtech.api.interfaces.ITexture;
import gregtech.api.interfaces.metatileentity.IMetaTileEntity;
import gregtech.api.interfaces.tileentity.IGregTechTileEntity;
import gregtech.api.metatileentity.implementations.MTEHatch;
import gregtech.api.metatileentity.implementations.MTEHatchInput;
import gregtech.api.metatileentity.implementations.MTEHatchMultiInput;
import gregtech.api.metatileentity.implementations.MTEHatchOutput;
import gregtech.api.recipe.RecipeMap;
import gregtech.api.recipe.check.CheckRecipeResult;
import gregtech.api.recipe.check.CheckRecipeResultRegistry;
import gregtech.api.render.TextureFactory;
import gregtech.api.util.GTLanguageManager;
import gregtech.api.util.GTModHandler;
import gregtech.api.util.GTUtility;
import gregtech.api.util.IGTHatchAdder;
import gregtech.api.util.MultiblockTooltipBuilder;
import gregtech.common.blocks.BlockCasings10;
import gregtech.common.blocks.BlockCasings13;
import gregtech.common.blocks.BlockCasings2;
import gregtech.common.tileentities.machines.IRecipeProcessingAwareHatch;
import gregtech.common.tileentities.machines.MTEHatchInputME;
import gregtech.common.tileentities.machines.MTEHatchOutputME;
import kubatech.api.implementations.KubaTechGTMultiBlockBase;
import kubatech.loaders.HTGRLoader;

public class MTEHighTempGasCooledReactor extends KubaTechGTMultiBlockBase<MTEHighTempGasCooledReactor>
    implements ISurvivalConstructable {

    private static final int BASECASINGINDEX = 181;

    private static final String STRUCTURE_PIECE_MAIN = "main";
    private static final IStructureDefinition<MTEHighTempGasCooledReactor> STRUCTURE_DEFINITION = StructureDefinition
        .<MTEHighTempGasCooledReactor>builder()
        .addShape(
            STRUCTURE_PIECE_MAIN,
            transpose( // spotless:off
                new String[][]{
                    {"                             ","                             ","                             ","                             ","                             ","                             ","                             ","                             ","                             ","                 C           ","                CCC          ","               CCChC         ","                CCC          ","                 C           ","                             ","                             ","                             ","                             "},
                    {"                             ","                             ","                             ","                             ","                             ","                             ","                             ","                             ","                             ","                CCC          ","               C   C         ","               C  MC         ","               C   C         ","                CCC          ","                             ","                             ","                             ","                             "},
                    {"                             ","                             ","                             ","                             ","                             ","                             ","                             ","                             ","                             ","                CCC          ","     GGG       C  MC         ","     GfG FFFFFFFN MC         ","     GGG       CN  C         ","                CCC          ","                             ","                             ","                             ","                             "},
                    {"                             ","                             ","                             ","                             ","                             ","                             ","                             ","                             ","     GGG                     ","    GGGGG       CCC          ","   GGEEEGG     CMM C         ","   GGE EGF     CM NC         ","   GGEEEGG     C NNC         ","    GGGGG       CCC          ","     GGG                     ","                             ","                             ","                             "},
                    {"                             ","                             ","                             ","                             ","                             ","                             ","                             ","     GGG                     ","    GEEEG                    ","   GEEHEEG      CCC          ","  GEE   EEG    CNNNC         ","  GEH   HFG    C   C         ","  GEE   EEG    CMMMC         ","   GEEHEEG      CCC          ","    GEEEG                    ","     GGG                     ","                             ","                             "},
                    {"                             ","                             ","                             ","                             ","                             ","                             ","     GGG                     ","   GGEEEGG                   ","  GGE   EGG                  ","  GE  H  EG     CCC      C   "," GE       EG   C MMC    CCC  "," GE H   H EG   CN MC   CCCCC "," GE       EG   CNN C    CCC  ","  GE  H  EG     CCC      C   ","  GGE   EGG                  ","   GGEEEGG                   ","     GGG                     ","                             "},
                    {"                             ","                             ","                             ","                             ","                             ","                             ","     GGG                     ","   GGEEEGG                   ","  GGE   EGG                  ","  GE  H  EG     CCC     CCC  "," GE       EG   CM NC   C   C "," GE H   H EG   CM NC   C  Lw "," GE       EG   CM NC   C  LC ","  GE  H  EG     CCC     CCC  ","  GGE   EGG                  ","   GGEEEGG                   ","     GGG                     ","                             "},
                    {"                             ","                             ","                             ","                             ","                             ","                             ","     GGG                     ","   GGEEEGG               F   ","  GGE   EGG              F   ","  GE  H  EG     CCC     CFC  "," GE       EG   CNN C   C NNC "," GE H   H EG   CN MC   CL  C "," GE       EG   C MMC   CLL C ","  GE  H  EG     CCC     CCC  ","  GGE   EGG                  ","   GGEEEGG                   ","     GGG                     ","                             "},
                    {"                             ","                             ","                             ","                             ","                             ","                             ","     GGG                     ","   GGEEEGG               F   ","  GGE   EGG                  ","  GE  H  EG     CCC     CCC  "," GE       EG   CMMMC   CLLLC "," GE H   H EG   C   C   C  NC "," GE       EG   CNNNC   C NNC ","  GE  H  EG     CCC     CCC  ","  GGE   EGG                  ","   GGEEEGG                   ","     GGG                     ","                             "},
                    {"                             ","                             ","                             ","                             ","                             ","                             ","     GGG                     ","   GGEEEGG               F   ","  GGE   EGG                  ","  GE     EG     CCC     CCC  "," GE       EG   C NNC   CNN C "," GE       EG   CM NC   CN LC "," GE       EG   CMM C   C LLC ","  GE     EG     CCC     CCC  ","  GGE   EGG                  ","   GGEEEGG                   ","     GGG                     ","                             "},
                    {"                             ","                             ","                             ","                             ","                             ","                             ","  PPPPPPPPP                  "," PPPPGGGPPPP             F   "," PPGGEEEGGPP                 "," PPGEE EEGPP   PCCCP    CCC  "," PGEE   EEGP   CN MC   CL NC "," PGE     FGP   CN MC   CL NC "," PGEE   EEGP   CN MC   CL NC "," PPGEE EEGPP   PCCCP    CCC  "," PPGGEEEGGPP                 "," PPPPGGGPPPP                 ","  PPPPPPPPP                  ","                             "},
                    {"                             ","                             ","                             ","                             ","                             ","                             ","  PP     PP                  "," P         P             F   "," P   GDG   P                 ","    GGEGG      PCCCP   PCCCP ","   GGE EGG     CMM C   C LLC ","   GE   EF     cM NNFFFNN Ls ","   GGE EGG     C NNC   CNN C ","    GGEGG      PCCCP   PCCCP "," P   GGG   P                 "," P         P                 ","  PP     PP                  ","                             "},
                    {"                             ","               AAA           ","               AAA           ","               AAA           ","                             ","                             ","  P       P                  "," P         P   eee       F   ","      D                      ","      G        PCCCP   PCCCP ","     GGG       CCCCC   CCCCC ","    GGvGGF     CCCCC   CCCCC ","     GGG       CCCCC   CCCCC ","      G        PCCCP   PCCCP ","                             "," P         P                 ","  P       P                  ","                             "},
                    {"                             ","               A~A           ","               AIA           ","               AAA           ","                             ","                             ","  P       P                  "," P         P FFJJJFFFFFFFF   ","      D      F               ","             F PPPPP   PPPPP ","             F P   P   P   P ","         FFFFF P   P   P   P ","               P   P   P   P ","               PPPPP   PPPPP ","                             "," P         P                 ","  P       P                  ","                             "},
                    {"                             ","               AAA           ","      DDDDDDDDDDAA           ","      D        ADA           ","      D         D            ","      D         D            ","  PP  D  PP     D            "," P    D    P C KDK C  C  C   "," P    D    P                 ","             C P   P   P   P ","                             ","         C C C               ","                             ","               P   P   P   P "," P         P                 "," P         P                 ","  PP     PP                  ","                             "},
                    {"OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO","OOOOOOOOOOOOOOOOOOOOOOOOOOOOO"}
                })) // spotless:on
        .addElement('A', ofBlock(GregTechAPI.sBlockCasings1, 5)) // IV Machine Casing
        .addElement('C', ofBlock(GregTechAPI.sBlockCasings10, 3)) // Pressure Containment Casing
        .addElement('D', ofBlock(GregTechAPI.sBlockCasings13, 0)) // Cable Casing
        .addElement('E', ofBlock(GregTechAPI.sBlockCasings13, 1)) // Graphite Moderator Casing
        .addElement('F', ofBlock(GregTechAPI.sBlockCasings13, 2)) // Insulated Fluid Pipe Casing
        .addElement('G', ofBlock(GregTechAPI.sBlockCasings13, 3)) // Beryllium Integrated Reactor Casing
        .addElement('H', ofBlock(GregTechAPI.sBlockCasings13, 4)) // Refined Graphite Block
        .addElement('I', ofBlock(GregTechAPI.sBlockCasings2, 6)) // Processor Machine Casing
        .addElement('J', ofBlock(GregTechAPI.sBlockCasings2, 10)) // Pump Machine Casing
        .addElement('K', ofBlock(GregTechAPI.sBlockCasings2, 11)) // Motor Machine Casing
        .addElement('L', ofBlock(GregTechAPI.sBlockCasings2, 13)) // Steel Pipe Casing
        .addElement('M', ofBlock(GregTechAPI.sBlockCasings2, 14)) // Titanium Pipe Casing
        .addElement('N', ofBlock(GregTechAPI.sBlockCasings2, 15)) // Tungstensteel Pipe Casing
        .addElement('O', ofBlock(GregTechAPI.sBlockConcretes, 8)) // Light Concrete
        .addElement('P', ofBlock(GregTechAPI.sBlockFrames, 81)) // Tungsten Frame Box
        .addElement(
            'e',
            buildHatchAdder(MTEHighTempGasCooledReactor.class)
                .atLeast(HTGRHatches.HeliumInputHatch, HatchElement.Maintenance, HatchElement.Energy)
                .casingIndex(((BlockCasings2) GregTechAPI.sBlockCasings2).getTextureIndex(11))
                .hint(1)
                .build())
        .addElement(
            'f',
            HatchElement.InputBus.newAny(((BlockCasings13) GregTechAPI.sBlockCasings13).getTextureIndex(3), 2))
        .addElement(
            'v',
            HatchElement.OutputBus.newAny(((BlockCasings13) GregTechAPI.sBlockCasings13).getTextureIndex(3), 3))
        .addElement(
            'c',
            HTGRHatches.CoolantInputHatch.newAny(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(3), 4))
        .addElement(
            'h',
            HTGRHatches.CoolantOutputHatch.newAny(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(3), 5))
        .addElement(
            'w',
            HTGRHatches.WaterInputHatch.newAny(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(3), 6))
        .addElement(
            's',
            HTGRHatches.SteamOutputHatch.newAny(((BlockCasings10) GregTechAPI.sBlockCasings10).getTextureIndex(3), 7))
        .build();

    private static final int HELIUM_NEEDED = 512000;

    private static final int MIN_HELIUM_NEEDED = HELIUM_NEEDED / 10;

    // conversion factor per operation ππππππππππππππππππππππππππππππππππππππππππππππππππππππππ
    private static final double CONVERSION_FACTOR = 0.00141592653589793238462643383279502884197169399375105820974944592307816406286208998628034825342117067982148086513282306647093844609550582231725359408128481117450284102701938521105559644622948954930381964429d;

    private static final long POWER_USAGE = (long) (TierEU.RECIPE_IV * 0.2d); // Minimum power usage
    private static final double POWER_PENALTY_WHEN_MINIMUM_HELIUM = 39d; // When minimum Helium power usage by the pump
                                                                         // will be multiplied by this number+1

    private static final double BASE_PROCESSING_TIME = 2000d; // Minimum operation time
    private static final double SCALING_PROCESSING_TIME = 18000; // Scaling operation time with amount of pellets in
                                                                 // the reactor

    private static final int MAX_CAPACITY = 10000; // Max pellets in reactor
    private static final int MIN_CAPACITY = MAX_CAPACITY / 100; // min 1% reactor fill to start

    private static final double COOLANT_SPEEDUP = 0.07d / 20d; // 7% recipe time per second if supplied with 100% of all
                                                               // needed coolant
    private static final double WATER_SPEEDUP = 0.03d / 20d; // 3% recipe time per second if supplied with 100% of all
                                                             // needed water

    private static final double COOLANT_PER_PELLET = 0.5d; // coolant needed per tick to cool one pellet
    private static final double WATER_PER_PELLET = 0.1d; // water needed per tick to cool one pellet

    private static final double HELIUM_LOST_PER_CYCLE = 0.0005d; // Helium lost per one operation

    private int heliumSupply;
    private double fuelsupply = 0;
    private final HashMap<Materials, Double> mStoredFuels = new HashMap<>();
    private final HashMap<Materials, Double> mStoredBurnedFuels = new HashMap<>();
    private boolean empty;
    private int emptyticksnodiff = 0;
    private int coolanttaking = 0;
    private int watertaking = 0;

    private MTEHatchInput heliumInputHatch;
    private MTEHatchInput coolantInputHatch;
    private MTEHatchOutput coolantOutputHatch;
    private MTEHatchInput waterInputHatch;
    private MTEHatchOutput steamOutputHatch;
    private final ArrayList<MTEHatch> mCustomHatches = new ArrayList<>(5);

    public MTEHighTempGasCooledReactor(int aID, String aName, String aNameRegional) {
        super(aID, aName, aNameRegional);
    }

    private MTEHighTempGasCooledReactor(String aName) {
        super(aName);
    }

    @Override
    public IStructureDefinition<MTEHighTempGasCooledReactor> getStructureDefinition() {
        return STRUCTURE_DEFINITION;
    }

    private boolean addHeliumInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (heliumInputHatch != null) return false;
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            setHatchRecipeMap(hatch);
            heliumInputHatch = hatch;
            mCustomHatches.add(hatch);
            return true;
        }
        return false;
    }

    private boolean addCoolantInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (coolantInputHatch != null) return false;
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            setHatchRecipeMap(hatch);
            coolantInputHatch = hatch;
            mCustomHatches.add(hatch);
            return true;
        }
        return false;
    }

    private boolean addCoolantOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (coolantOutputHatch != null) return false;
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchOutput hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            coolantOutputHatch = hatch;
            mCustomHatches.add(hatch);
            return true;
        }
        return false;
    }

    private boolean addWaterInputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (waterInputHatch != null) return false;
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchInput hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            waterInputHatch = hatch;
            mCustomHatches.add(hatch);
            return true;
        }
        return false;
    }

    private boolean addSteamOutputToMachineList(IGregTechTileEntity aTileEntity, int aBaseCasingIndex) {
        if (steamOutputHatch != null) return false;
        if (aTileEntity == null) return false;
        IMetaTileEntity aMetaTileEntity = aTileEntity.getMetaTileEntity();
        if (aMetaTileEntity == null) return false;
        if (aMetaTileEntity instanceof MTEHatchOutput hatch) {
            hatch.updateTexture(aBaseCasingIndex);
            hatch.updateCraftingIcon(this.getMachineCraftingIcon());
            steamOutputHatch = hatch;
            mCustomHatches.add(hatch);
            return true;
        }
        return false;
    }

    @Override
    public void clearHatches() {
        super.clearHatches();
        heliumInputHatch = null;
        coolantInputHatch = null;
        coolantOutputHatch = null;
        waterInputHatch = null;
        steamOutputHatch = null;
        mCustomHatches.clear();
    }

    @Override
    protected MultiblockTooltipBuilder createTooltip() {
        MultiblockTooltipBuilder tt = new MultiblockTooltipBuilder();
        tt.addMachineType("Breeder Reactor, HTGR")
            .addInfo(
                "Uses up to " + EnumChatFormatting.RED
                    + formatNumber(CONVERSION_FACTOR * 100)
                    + EnumChatFormatting.GRAY
                    + "% of fuel per operation, "
                    + EnumChatFormatting.RED
                    + "10"
                    + EnumChatFormatting.GRAY
                    + "% of this value is flat and "
                    + EnumChatFormatting.RED
                    + "90"
                    + EnumChatFormatting.GRAY
                    + "% dependent on the formula y=1-(1-x)^3 (x is % of pellet fill level)")
            .addInfo(
                "Maintenance problems decrease the efficiency of cooling by " + EnumChatFormatting.RED
                    + "20"
                    + EnumChatFormatting.GRAY
                    + "% for each issue")
            .addInfo(
                "Uses " + EnumChatFormatting.RED
                    + formatNumber(POWER_USAGE)
                    + EnumChatFormatting.GRAY
                    + " EU/t increasing by up to "
                    + EnumChatFormatting.RED
                    + formatNumber(POWER_PENALTY_WHEN_MINIMUM_HELIUM + 1)
                    + EnumChatFormatting.GRAY
                    + " times when lacking Helium Gas")
            .addInfo(
                "Helium gas increases effectiveness of heat exchangers linearly up to " + EnumChatFormatting.RED
                    + "100"
                    + EnumChatFormatting.GRAY
                    + "% at max capacity")
            .addInfo(
                "The Reactor loses " + EnumChatFormatting.RED
                    + formatNumber(HELIUM_LOST_PER_CYCLE * 100)
                    + EnumChatFormatting.GRAY
                    + "% helium per operation and requires at least "
                    + EnumChatFormatting.RED
                    + formatNumber(100 * MIN_HELIUM_NEEDED / HELIUM_NEEDED)
                    + EnumChatFormatting.GRAY
                    + "% helium to start operation")
            .addInfo(
                "One Operation takes longer based on reactor fill level (between " + EnumChatFormatting.RED
                    + formatNumber(BASE_PROCESSING_TIME / 20)
                    + EnumChatFormatting.GRAY
                    + "s and "
                    + EnumChatFormatting.RED
                    + formatNumber((BASE_PROCESSING_TIME + SCALING_PROCESSING_TIME) / 20)
                    + EnumChatFormatting.GRAY
                    + "s)")
            .addInfo(
                "Providing coolant/water/both speeds up recipe by " + EnumChatFormatting.RED
                    + formatNumber(COOLANT_SPEEDUP * 20 * 100)
                    + EnumChatFormatting.GRAY
                    + "%/"
                    + EnumChatFormatting.RED
                    + formatNumber(WATER_SPEEDUP * 20 * 100)
                    + EnumChatFormatting.GRAY
                    + "%/"
                    + EnumChatFormatting.RED
                    + formatNumber(((COOLANT_SPEEDUP + WATER_SPEEDUP) * 20 * 100))
                    + EnumChatFormatting.GRAY
                    + "% total recipe time/second")
            .addInfo(
                "The amount of necessary fluid for maximum bonus speed scales with pellets, " + EnumChatFormatting.RED
                    + formatNumber(COOLANT_PER_PELLET)
                    + EnumChatFormatting.GRAY
                    + " coolant/tick/pellet and "
                    + EnumChatFormatting.RED
                    + formatNumber(WATER_PER_PELLET)
                    + EnumChatFormatting.GRAY
                    + " distilled water/tick/pellet")
            .beginStructureBlock(29, 16, 18, true)
            .addController("Front center")
            .addInputHatch("Top of the Pump - Accepts Helium", 1)
            .addEnergyHatch("Top of the Pump", 1)
            .addMaintenanceHatch("Top of the Pump", 1)
            .addInputBus("Top of the Reactor - Accepts Fuel", 2)
            .addOutputBus("Bottom of the Reactor - Outputs Fuel", 3)
            .addInputHatch("Bottom of the tall Coolant Tower - Accepts Coolant", 4)
            .addOutputHatch("Top of the tall Coolant Tower - Outputs Hot Coolant", 5)
            .addInputHatch("Top of the short Coolant Tower - Accepts Distilled Water", 6)
            .addOutputHatch("Bottom of the short Coolant Tower - Outputs Steam", 7)

            .toolTipFinisher(AuthorKuba, AuthorPxx500);
        return tt;
    }

    @Override
    protected IAlignmentLimits getInitialAlignmentLimits() {
        return (d, r, f) -> d.offsetY == 0 && r.isNotRotated() && f.isNotFlipped();
    }

    @Override
    public void construct(ItemStack stackSize, boolean hintsOnly) {
        this.buildPiece("main", stackSize, hintsOnly, 16, 13, 1);
    }

    @Override
    public int survivalConstruct(ItemStack stackSize, int elementBudget, ISurvivalBuildEnvironment env) {
        if (mMachine) return -1;
        return survivalBuildPiece("main", stackSize, 16, 13, 1, elementBudget, env, false, true);
    }

    @Override
    public boolean checkMachine(IGregTechTileEntity aBaseMetaTileEntity, ItemStack itemStack) {
        return this.checkPiece("main", 16, 13, 1) && this.mMaintenanceHatches.size() == 1
            && !this.mInputBusses.isEmpty()
            && !this.mOutputBusses.isEmpty()
            && !this.mEnergyHatches.isEmpty()
            && this.heliumInputHatch != null
            && this.coolantInputHatch != null
            && this.coolantOutputHatch != null
            && this.waterInputHatch != null
            && this.steamOutputHatch != null;
    }

    @Override
    public void startRecipeProcessing() {
        for (MTEHatch hatch : validMTEList(mCustomHatches)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                aware.startRecipeProcessing();
            }
        }
        super.startRecipeProcessing();
    }

    @Override
    public void endRecipeProcessing() {
        super.endRecipeProcessing();
        for (MTEHatch hatch : validMTEList(mCustomHatches)) {
            if (hatch instanceof IRecipeProcessingAwareHatch aware) {
                setResultIfFailure(aware.endRecipeProcessing(this));
            }
        }
    }

    @Override
    public void updateSlots() {
        super.updateSlots();
        for (MTEHatch hatch : validMTEList(mCustomHatches)) {
            if (hatch instanceof MTEHatchInput input) input.updateSlots();
        }
    }

    private FluidStack getInputFromHatch(MTEHatchInput hatch, FluidStack filter) {
        if (hatch == null || !hatch.isValid()) return null;
        if (hatch instanceof MTEHatchInputME meHatch) {
            for (FluidStack tFluid : meHatch.getStoredFluids()) {
                if (tFluid != null && tFluid.isFluidEqual(filter)) {
                    return tFluid;
                }
            }
        } else if (hatch instanceof MTEHatchMultiInput multiHatch) {
            for (final FluidStack tFluid : multiHatch.getStoredFluid()) {
                if (tFluid != null && tFluid.isFluidEqual(filter)) {
                    return tFluid;
                }
            }
        } else {
            if (hatch.getFillableStack() != null) {
                final FluidStack tStack = hatch.getFillableStack();
                if (tStack != null && tStack.isFluidEqual(filter)) {
                    return tStack;
                }
            }
        }
        return null;
    }

    private boolean addOutputToHatch(MTEHatchOutput hatch, FluidStack fluidStack) {
        if (hatch == null || !hatch.isValid()) return false;
        if (!hatch.canStoreFluid(fluidStack)) return false;

        if (hatch instanceof MTEHatchOutputME tMEHatch) {
            if (!tMEHatch.canFillFluid()) return false;
        }

        int tAmount = hatch.fill(fluidStack, false);
        if (tAmount >= fluidStack.amount) {
            return hatch.fill(fluidStack, true) >= fluidStack.amount;
        } else if (tAmount > 0) {
            fluidStack.amount = fluidStack.amount - hatch.fill(fluidStack, true);
        }
        return false;
    }

    @Override
    public void loadNBTData(NBTTagCompound aNBT) {
        super.loadNBTData(aNBT);
        this.heliumSupply = aNBT.getInteger("HeliumSupply");
        this.fuelsupply = aNBT.getDouble("fuelsupply");
        this.empty = aNBT.getBoolean("EmptyMode");
        this.coolanttaking = aNBT.getInteger("coolanttaking");
        this.watertaking = aNBT.getInteger("watertaking");
        int fuels = aNBT.getInteger("fuels");
        int burnedfuels = aNBT.getInteger("burnedfuels");
        for (int i = 0; i < fuels; i++) {
            String name = aNBT.getString("fuels" + i);
            double amount = aNBT.getDouble("fuelsamount" + i);
            Materials m = Materials.get(name);
            if (m != null) {
                this.mStoredFuels.put(m, amount);
            }
        }
        for (int i = 0; i < burnedfuels; i++) {
            String name = aNBT.getString("burnedfuels" + i);
            double amount = aNBT.getDouble("burnedfuelsamount" + i);
            Materials m = Materials.get(name);
            if (m != null) {
                this.mStoredBurnedFuels.put(m, amount);
            }
        }
    }

    @Override
    public void saveNBTData(NBTTagCompound aNBT) {
        super.saveNBTData(aNBT);
        aNBT.setInteger("HeliumSupply", this.heliumSupply);
        aNBT.setDouble("fuelsupply", this.fuelsupply);
        aNBT.setBoolean("EmptyMode", this.empty);
        aNBT.setInteger("coolanttaking", this.coolanttaking);
        aNBT.setInteger("watertaking", this.watertaking);
        aNBT.setInteger("fuels", this.mStoredFuels.size());
        aNBT.setInteger("burnedfuels", this.mStoredBurnedFuels.size());
        int i = 0;
        for (Map.Entry<Materials, Double> entry : this.mStoredFuels.entrySet()) {
            aNBT.setString("fuels" + i, entry.getKey().mName);
            aNBT.setDouble("fuelsamount" + i, entry.getValue());
            i++;
        }
        i = 0;
        for (Map.Entry<Materials, Double> entry : this.mStoredBurnedFuels.entrySet()) {
            aNBT.setString("burnedfuels" + i, entry.getKey().mName);
            aNBT.setDouble("burnedfuelsamount" + i, entry.getValue());
            i++;
        }
    }

    @Override
    public void onPostTick(IGregTechTileEntity aBaseMetaTileEntity, long aTick) {
        super.onPostTick(aBaseMetaTileEntity, aTick);
        if (aBaseMetaTileEntity.isServerSide() && !this.empty) {
            boolean updateneeded = false;
            if (this.heliumSupply < MTEHighTempGasCooledReactor.HELIUM_NEEDED) {
                this.startRecipeProcessing();
                FluidStack fluidStack = this.getInputFromHatch(heliumInputHatch, Materials.Helium.getGas(1));
                if (fluidStack != null) {
                    int toget = Math
                        .min(MTEHighTempGasCooledReactor.HELIUM_NEEDED - this.heliumSupply, fluidStack.amount);
                    fluidStack.amount -= toget;
                    this.heliumSupply += toget;
                    updateneeded = true;
                }
                this.endRecipeProcessing();
            }
            if (MAX_CAPACITY - this.fuelsupply >= 1) {
                this.startRecipeProcessing();
                for (ItemStack itemStack : this.getStoredInputs()) {
                    if (itemStack == null || itemStack.getItem() != HTGRLoader.HTGR_ITEM) continue;
                    int damage = HTGRLoader.HTGR_ITEM.getDamage(itemStack);
                    if (damage != 3) continue;
                    Materials m = HTGRLoader.HTGR_ITEM.getItemMaterial(itemStack);
                    int toget = (int) Math.min(MAX_CAPACITY - this.fuelsupply, itemStack.stackSize);
                    this.fuelsupply += toget;
                    itemStack.stackSize -= toget;
                    mStoredFuels.merge(m, (double) toget, Double::sum);
                    updateneeded = true;
                }
                this.endRecipeProcessing();
            }
            if (updateneeded) this.updateSlots();
        }
    }

    @Override
    public RecipeMap<?> getRecipeMap() {
        return HTGRLoader.HTGRRecipes;
    }

    @Override
    protected boolean filtersFluid() {
        return false;
    }

    @Override
    public @NotNull CheckRecipeResult checkProcessing() {
        if (this.empty) {
            if (this.heliumSupply > 0 || this.fuelsupply > 0) {
                this.mEfficiency = 10000;
                this.mMaxProgresstime = 100;
                return CheckRecipeResultRegistry.SUCCESSFUL;
            }
            return CheckRecipeResultRegistry.NO_RECIPE;
        }
        if (this.heliumSupply < MIN_HELIUM_NEEDED || this.fuelsupply < MIN_CAPACITY)
            return CheckRecipeResultRegistry.NO_FUEL_FOUND;

        // 1 - Math.pow(1 - x, 3)

        double eff = (0.1d + (1d - GTUtility.powInt(1 - (this.fuelsupply / MAX_CAPACITY), 3)) * 0.9d);

        if (eff <= 0) return CheckRecipeResultRegistry.NO_RECIPE;

        double toReduce = this.fuelsupply * CONVERSION_FACTOR * eff;

        for (Map.Entry<Materials, Double> entry : mStoredFuels.entrySet()) {
            Materials m = entry.getKey();
            double amount = entry.getValue();
            if (amount > 0) {
                double toUse = (amount / fuelsupply) * toReduce;
                mStoredBurnedFuels.merge(m, toUse, Double::sum);
                entry.setValue(amount - toUse);
                this.fuelsupply -= toUse;
            }
        }

        ArrayList<ItemStack> toOutput = new ArrayList<>();

        for (Map.Entry<Materials, Double> entry : mStoredBurnedFuels.entrySet()) {
            if (entry.getValue() >= 1.d) {
                Materials m = entry.getKey();
                double output = Math.floor(entry.getValue());
                ItemStack stack = HTGRLoader.HTGR_ITEM.createBurnedTRISOFuel(m);
                stack.stackSize = (int) output;
                toOutput.add(stack);
            }
        }

        if (this.canOutputAll(toOutput.toArray(new ItemStack[0]))) {
            for (ItemStack itemStack : toOutput) {
                Materials m = HTGRLoader.HTGR_ITEM.getItemMaterial(itemStack);
                if (m != null) {
                    mStoredBurnedFuels.merge(m, (double) -itemStack.stackSize, Double::sum);
                }
            }
            this.mOutputItems = toOutput.toArray(new ItemStack[0]);
        }

        this.coolanttaking = (int) ((COOLANT_PER_PELLET * this.fuelsupply * this.heliumSupply / HELIUM_NEEDED)
            * (1 - (this.getIdealStatus() - this.getRepairStatus()) / 5d));

        this.watertaking = (int) ((WATER_PER_PELLET * this.fuelsupply * this.heliumSupply / HELIUM_NEEDED)
            * (1 - (this.getIdealStatus() - this.getRepairStatus()) / 5d));

        this.mEfficiency = (int) (eff * 10000D);
        this.mEfficiencyIncrease = 0;
        this.lEUt = (long) -(POWER_USAGE + POWER_USAGE
            * (1d - (double) (this.heliumSupply - MIN_HELIUM_NEEDED) / (HELIUM_NEEDED - MIN_HELIUM_NEEDED))
            * POWER_PENALTY_WHEN_MINIMUM_HELIUM);

        this.heliumSupply = (int) ((double) this.heliumSupply * (1d - HELIUM_LOST_PER_CYCLE));

        this.updateSlots();

        this.mMaxProgresstime = (int) BASE_PROCESSING_TIME + (int) (SCALING_PROCESSING_TIME * eff);
        return CheckRecipeResultRegistry.SUCCESSFUL;
    }

    @Override
    protected long getActualEnergyUsage() {
        return -lEUt;
    }

    @Override
    public boolean onRunningTick(ItemStack aStack) {
        super.onRunningTick(aStack);

        if (this.empty) {
            if (this.emptyticksnodiff > 20 && this.emptyticksnodiff % 20 != 0) {
                this.emptyticksnodiff++;
                return true;
            }
            if (this.fuelsupply >= 1d) {
                for (Map.Entry<Materials, Double> entry : mStoredFuels.entrySet()) {
                    if (entry.getValue() >= 1d) {
                        ItemStack fuelStack = HTGRLoader.HTGR_ITEM.createTRISOFuel(entry.getKey());
                        int toOutput = (int) Math.floor(entry.getValue());
                        int didOutput = 0;
                        int outputNow = fuelStack.stackSize = Math.min(toOutput, 64);
                        while (this.addOutputAtomic(fuelStack)) {
                            toOutput -= outputNow;
                            didOutput += outputNow;
                            if (toOutput <= 0) break;
                            outputNow = fuelStack.stackSize = Math.min(toOutput, 64);
                        }
                        entry.setValue(entry.getValue() - didOutput);
                        this.fuelsupply -= didOutput;
                    }
                }
            }
            return true;
        }

        {
            int takecoolant = this.coolanttaking;
            int drainedamount = 0;

            FluidStack tLiquid = getInputFromHatch(coolantInputHatch, GTModHandler.getIC2Coolant(1));
            if (tLiquid != null) {
                int toDrain = Math.min(takecoolant, tLiquid.amount);
                FluidStack drained = coolantInputHatch.drain(toDrain, true);
                drainedamount += drained.amount;
            }

            if (drainedamount > 0) {
                addOutputToHatch(coolantOutputHatch, FluidRegistry.getFluidStack("ic2hotcoolant", drainedamount));

                double eff = drainedamount / ((double) this.coolanttaking);
                double addedTime = (int) (this.mMaxProgresstime * COOLANT_SPEEDUP
                    * eff
                    * this.heliumSupply
                    / HELIUM_NEEDED);
                addedTime *= (1 - (this.getIdealStatus() - this.getRepairStatus()) / 5d);
                if (addedTime > 0) this.mProgresstime += (int) addedTime;
            }
        }
        {
            int takewater = this.watertaking;
            int drainedamount = 0;

            FluidStack tLiquid = getInputFromHatch(waterInputHatch, GTModHandler.getDistilledWater(1));
            if (tLiquid != null) {
                int toDrain = Math.min(takewater, tLiquid.amount);
                FluidStack drained = waterInputHatch.drain(toDrain, true);
                drainedamount += drained.amount;
            }

            if (drainedamount > 0) {
                addOutputToHatch(steamOutputHatch, Materials.Steam.getGas(drainedamount * 160L));

                double eff = drainedamount / ((double) this.watertaking);
                double addedTime = (int) (this.mMaxProgresstime * WATER_SPEEDUP
                    * eff
                    * this.heliumSupply
                    / HELIUM_NEEDED);
                addedTime *= (1 - (this.getIdealStatus() - this.getRepairStatus()) / 5d);
                if (addedTime > 0) this.mProgresstime += (int) addedTime;
            }
        }

        this.updateSlots();

        return true;
    }

    @Override
    public IMetaTileEntity newMetaEntity(IGregTechTileEntity iGregTechTileEntity) {
        return new MTEHighTempGasCooledReactor(this.mName);
    }

    private String getReactorInfoText() {
        StringBuilder sb = new StringBuilder();
        sb.append(EnumChatFormatting.WHITE)
            .append(StatCollector.translateToLocal("kubatech.infodata.htgr.stored_fuel"))
            .append("\n");
        for (Map.Entry<Materials, Double> entry : mStoredFuels.entrySet()) {
            sb.append(
                StatCollector.translateToLocalFormatted(
                    "kubatech.infodata.htgr.stored_fuel_entry",
                    entry.getKey()
                        .getLocalizedNameForItem("%material"),
                    formatNumber(entry.getValue())))
                .append("\n");
        }
        sb.append(EnumChatFormatting.WHITE)
            .append(
                StatCollector.translateToLocalFormatted(
                    "kubatech.infodata.htgr.fuel_supply",
                    formatNumber(this.fuelsupply),
                    formatNumber(MAX_CAPACITY)))
            .append("\n");
        sb.append(EnumChatFormatting.WHITE)
            .append(StatCollector.translateToLocal("kubatech.infodata.htgr.burned_fuel"))
            .append("\n");
        for (Map.Entry<Materials, Double> entry : mStoredBurnedFuels.entrySet()) {
            sb.append(
                StatCollector.translateToLocalFormatted(
                    "kubatech.infodata.htgr.burned_fuel_entry",
                    entry.getKey()
                        .getLocalizedNameForItem("%material"),
                    formatNumber(entry.getValue() * 100d)))
                .append("\n");
        }
        sb.append(EnumChatFormatting.WHITE)
            .append(
                StatCollector
                    .translateToLocalFormatted("kubatech.infodata.htgr.helium_supply", formatNumber(this.heliumSupply)))
            .append("\n");
        sb.append(EnumChatFormatting.WHITE)
            .append(
                StatCollector.translateToLocalFormatted(
                    "kubatech.infodata.htgr.coolant_per_tick",
                    formatNumber(this.coolanttaking)))
            .append("\n");
        sb.append(EnumChatFormatting.WHITE)
            .append(
                StatCollector
                    .translateToLocalFormatted("kubatech.infodata.htgr.water_per_tick", formatNumber(this.watertaking)))
            .append("\n");
        return sb.toString();
    }

    @Override
    public String[] getInfoData() {
        ArrayList<String> ll = new ArrayList<>(Arrays.asList(super.getInfoData()));
        ll.add(
            StatCollector.translateToLocal("kubatech.infodata.running_mode") + " "
                + EnumChatFormatting.GOLD
                + (empty ? StatCollector.translateToLocal("kubatech.infodata.mia.running_mode.output")
                    : StatCollector.translateToLocal("kubatech.infodata.mia.running_mode.operating.normal")));
        ll.addAll(Arrays.asList(getReactorInfoText().split("\n")));
        return ll.toArray(new String[0]);
    }

    @Override
    protected Widget generateCurrentRecipeInfoWidget() {
        DynamicPositionedColumn w = (DynamicPositionedColumn) super.generateCurrentRecipeInfoWidget();

        w.widget(new DynamicTextWidget(() -> {
            String sb = getReactorInfoText();
            return new Text(sb);
        }).setTextAlignment(Alignment.CenterLeft));
        return w;
    }

    @Override
    public ITexture[] getTexture(IGregTechTileEntity aBaseMetaTileEntity, ForgeDirection side,
        ForgeDirection forgeDirection, int aColorIndex, boolean aActive, boolean aRedstone) {
        if (side == forgeDirection) {
            if (aActive) return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(MTEHighTempGasCooledReactor.BASECASINGINDEX),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_ACTIVE_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
            return new ITexture[] {
                Textures.BlockIcons.getCasingTextureForId(MTEHighTempGasCooledReactor.BASECASINGINDEX),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER)
                    .extFacing()
                    .build(),
                TextureFactory.builder()
                    .addIcon(Textures.BlockIcons.OVERLAY_FRONT_HEAT_EXCHANGER_GLOW)
                    .extFacing()
                    .glow()
                    .build() };
        }
        return new ITexture[] {
            Textures.BlockIcons.getCasingTextureForId(MTEHighTempGasCooledReactor.BASECASINGINDEX) };
    }

    @Override
    public void onScrewdriverRightClick(ForgeDirection side, EntityPlayer aPlayer, float aX, float aY, float aZ,
        ItemStack aTool) {
        if (this.mMaxProgresstime > 0) {
            GTUtility.sendChatTrans(aPlayer, "kubatech.chat.forbidden_while_running");
            return;
        }
        this.empty = !this.empty;
        GTUtility.sendChatToPlayer(
            aPlayer,
            "HTGR is now running in " + (this.empty ? "emptying mode." : "normal Operation"));
    }

    @Override
    public boolean supportsVoidProtection() {
        return true;
    }

    private enum HTGRHatches implements IHatchElement<MTEHighTempGasCooledReactor> {

        CoolantInputHatch(MTEHighTempGasCooledReactor::addCoolantInputToMachineList, MTEHatchInput.class) {

            @Override
            public long count(MTEHighTempGasCooledReactor t) {
                if (t.coolantInputHatch == null) return 0;
                return 1;
            }
        },
        HeliumInputHatch(MTEHighTempGasCooledReactor::addHeliumInputToMachineList, MTEHatchInput.class) {

            @Override
            public long count(MTEHighTempGasCooledReactor t) {
                if (t.heliumInputHatch == null) return 0;
                return 1;
            }
        },
        WaterInputHatch(MTEHighTempGasCooledReactor::addWaterInputToMachineList, MTEHatchInput.class) {

            @Override
            public long count(MTEHighTempGasCooledReactor t) {
                if (t.waterInputHatch == null) return 0;
                return 1;
            }
        },
        CoolantOutputHatch(MTEHighTempGasCooledReactor::addCoolantOutputToMachineList, MTEHatchOutput.class) {

            @Override
            public long count(MTEHighTempGasCooledReactor t) {
                if (t.coolantOutputHatch == null) return 0;
                return 1;
            }
        },
        SteamOutputHatch(MTEHighTempGasCooledReactor::addSteamOutputToMachineList, MTEHatchOutput.class) {

            @Override
            public long count(MTEHighTempGasCooledReactor t) {
                if (t.steamOutputHatch == null) return 0;
                return 1;
            }
        },;

        private final List<Class<? extends IMetaTileEntity>> mteClasses;
        private final IGTHatchAdder<MTEHighTempGasCooledReactor> adder;

        @SafeVarargs
        HTGRHatches(IGTHatchAdder<MTEHighTempGasCooledReactor> adder, Class<? extends IMetaTileEntity>... mteClasses) {
            this.mteClasses = Collections.unmodifiableList(Arrays.asList(mteClasses));
            this.adder = adder;
        }

        @Override
        public List<? extends Class<? extends IMetaTileEntity>> mteClasses() {
            return mteClasses;
        }

        @Override
        public IGTHatchAdder<? super MTEHighTempGasCooledReactor> adder() {
            return adder;
        }

    }

    // TODO: Wypierdolić
    @Deprecated
    public static class HTGRMaterials {

        private static class CustomHTGRSimpleSubItemClass extends SimpleSubItemClass {

            HashMap<Integer, String> tooltip = null;

            public CustomHTGRSimpleSubItemClass(HashMap<Integer, String> tooltip, String... tex) {
                super(tex);
                this.tooltip = tooltip;
            }

            @Override
            @SuppressWarnings({ "unchecked", "rawtypes" })
            public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List aList, boolean p_77624_4_) {
                if (this.tooltip.containsKey(this.getDamage(p_77624_1_)))
                    aList.add(this.tooltip.get(this.getDamage(p_77624_1_)));
                // aList.add(StatCollector.translateToLocal("tooltip.bw.high_temp_gas_cooled_reactor.material"));
                aList.add(EnumChatFormatting.DARK_RED + "Deprecated, will be removed in next major update");
                super.addInformation(p_77624_1_, p_77624_2_, aList, p_77624_4_);
            }
        }

        private static class Base_ {

            public String sName;
            public String sEnglish;

            public Base_(String a, String b) {
                this.sName = a;
                this.sEnglish = b;
            }
        }

        public static class Fuel_ {

            public String sName;
            public String sEnglish;
            public ItemStack mainItem;
            public ItemStack secondaryItem;
            public ItemStack[] recycledItems = { GTValues.NI, GTValues.NI, GTValues.NI, GTValues.NI, GTValues.NI,
                GTValues.NI };
            public FluidStack recycledFluid;
            public int[] recycleChances;
            public String tooltip;

            public Fuel_(String sName, String sEnglish, ItemStack mainItem, ItemStack secondaryItem,
                FluidStack recycledFluid, ItemStack[] recycledItems, int[] recycleChances, String tooltip) {
                this.sName = sName;
                this.sEnglish = sEnglish;
                this.mainItem = mainItem;
                this.secondaryItem = secondaryItem;
                this.recycledFluid = recycledFluid;
                System.arraycopy(recycledItems, 0, this.recycledItems, 0, recycledItems.length);
                this.recycleChances = recycleChances;
                this.tooltip = tooltip;
            }
        }

        private static class LangEntry_ {

            public String sName;
            public String sEnglish;

            public LangEntry_(String a, String b) {
                this.sName = a;
                this.sEnglish = b;
            }
        }

        public static final Base_[] sHTGR_Bases = { new Base_("HTGRFuelMixture", "HTGR fuel mixture"),
            new Base_("BISOPebbleCompound", "BISO pebble compound"),
            new Base_("TRISOPebbleCompound", "TRISO pebble compound"), new Base_("TRISOBall", "TRISO ball"),
            new Base_("TRISOPebble", "TRISO pebble"), new Base_("BurnedOutTRISOBall", "Burned out TRISO Ball"),
            new Base_("BurnedOutTRISOPebble", "Burned out TRISO Pebble"), };
        public static final int MATERIALS_PER_FUEL = sHTGR_Bases.length;
        static final int USABLE_FUEL_INDEX = 4;
        static final int BURNED_OUT_FUEL_INDEX = 5;
        public static final Fuel_[] sHTGR_Fuel = { new Fuel_(
            "Thorium",
            "Thorium",
            WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 64),
            Materials.Uranium235.getDust(4),
            GTValues.NF,
            new ItemStack[] { Materials.Silicon.getDust(1), Materials.Graphite.getDust(1), Materials.Carbon.getDust(1),
                Materials.Lutetium.getDust(1), WerkstoffLoader.Thorium232.get(OrePrefixes.dust, 1) },
            new int[] { 9900 / 4, 9900 / 4, 9900 / 4, 9900 / 4, 162 / 4 },
            "Multiplies coolant by 1"),
            new Fuel_(
                "Uranium",
                "Uranium",
                Materials.Uranium.getDust(64),
                Materials.Uranium235.getDust(8),
                FluidRegistry.getFluidStack("krypton", 4),
                new ItemStack[] { Materials.Silicon.getDust(1), Materials.Graphite.getDust(1),
                    Materials.Carbon.getDust(1), Materials.Lead.getDust(1), Materials.Uranium.getDust(1) },
                new int[] { 9900 / 4, 9900 / 4, 9900 / 4, 5000 / 4, 5000 / 4 },
                "Multiplies coolant by 1.5"),
            new Fuel_(
                "Plutonium",
                "Plutonium",
                Materials.Plutonium.getDust(64),
                Materials.Plutonium241.getDust(4),
                FluidRegistry.getFluidStack("xenon", 4),
                new ItemStack[] { Materials.Silicon.getDust(1), Materials.Graphite.getDust(1),
                    Materials.Carbon.getDust(1), Materials.Lead.getDust(1), Materials.Plutonium.getDust(1) },
                new int[] { 9900 / 4, 9900 / 4, 9900 / 4, 5000 / 4, 5000 / 4 },
                "Multiplies coolant by 2"), };
        public static final CustomHTGRSimpleSubItemClass aHTGR_Materials;
        static final ArrayList<LangEntry_> aHTGR_Localizations = new ArrayList<>();

        static {
            String[] sHTGR_Materials = new String[sHTGR_Bases.length * sHTGR_Fuel.length];
            HashMap<Integer, String> tooltip = new HashMap<>();
            int i = 0;
            for (Fuel_ fuel : sHTGR_Fuel) for (Base_ base : sHTGR_Bases) {
                sHTGR_Materials[i] = "HTGR" + base.sName + fuel.sName;
                aHTGR_Localizations.add(
                    new LangEntry_("item." + sHTGR_Materials[i] + ".name", base.sEnglish + " (" + fuel.sEnglish + ")"));
                if ((i + 1) % MATERIALS_PER_FUEL == USABLE_FUEL_INDEX + 1 && fuel.tooltip != null
                    && !fuel.tooltip.isEmpty()) tooltip.put(i, fuel.tooltip);
                i++;
            }
            aHTGR_Materials = new CustomHTGRSimpleSubItemClass(tooltip, sHTGR_Materials);
        }

        public static void registeraTHR_Materials() {
            for (LangEntry_ iName : aHTGR_Localizations)
                GTLanguageManager.addStringLocalization(iName.sName, iName.sEnglish);
            GameRegistry.registerItem(MTEHighTempGasCooledReactor.HTGRMaterials.aHTGR_Materials, "bw.HTGRMaterials");
        }
    }
}
