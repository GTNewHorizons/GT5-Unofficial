package tectech.thing.metaTileEntity.multi.godforge.upgrade;

import static gregtech.common.gui.modularui.multiblock.godforge.data.Milestones.*;
import static gregtech.common.gui.modularui.multiblock.godforge.data.UpgradeColor.*;
import static tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade.PanelSize.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

import net.minecraft.item.ItemStack;

import com.cleanroommc.modularui.drawable.UITexture;
import com.google.common.collect.ImmutableSet;

import gregtech.common.gui.modularui.multiblock.godforge.data.Milestones;
import gregtech.common.gui.modularui.multiblock.godforge.data.UpgradeColor;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;

public enum ForgeOfGodsUpgrade {

    START,
    IGCC,
    STEM,
    CFCE,
    GISS,
    FDIM,
    SA,
    GPCI,
    REC,
    GEM,
    CTCDD,
    QGPIU,
    SEFCP,
    TCT,
    GGEBE,
    TPTP,
    DOP,
    CNTI,
    EPEC,
    IMKG,
    NDPE,
    POS,
    DOR,
    NGMS,
    SEDS,
    PA,
    CD,
    TSE,
    TBF,
    EE,
    END,

    ;

    public static final ForgeOfGodsUpgrade[] VALUES = values();

    static final Set<ForgeOfGodsUpgrade> SPLIT_UPGRADES;

    static {
        // Build upgrade data. Done here due to potential forward references

        // spotless:off

        START.build(b -> b
            .background(BLUE, COMPOSITION)
            .panelSize(LARGE)
            .treePos(126, 56));

        IGCC.build(b -> b
            .prereqs(START)
            .cost(1)
            .background(BLUE, CONVERSION)
            .treePos(126, 116));

        STEM.build(b -> b
            .prereqs(IGCC)
            .cost(1)
            .background(BLUE, CATALYST)
            .treePos(96, 176));

        CFCE.build(b -> b
            .prereqs(IGCC)
            .cost(1)
            .background(BLUE, CATALYST)
            .treePos(156, 176));

        GISS.build(b -> b
            .prereqs(STEM)
            .cost(1)
            .background(BLUE, CHARGE)
            .treePos(66, 236));

        FDIM.build(b -> b
            .prereqs(STEM, CFCE)
            .cost(1)
            .background(BLUE, COMPOSITION)
            .treePos(126, 236));

        SA.build(b -> b
            .prereqs(CFCE)
            .cost(1)
            .background(BLUE, CONVERSION)
            .treePos(186, 236));

        GPCI.build(b -> b
            .prereqs(FDIM)
            .cost(2)
            .background(BLUE, COMPOSITION)
            .treePos(126, 296));

        REC.build(b -> b
            .prereqs(GISS, GPCI)
            .requireAllPrereqs()
            .cost(2)
            .background(RED, CHARGE)
            .treePos(56, 356));

        GEM.build(b -> b
            .prereqs(GPCI)
            .cost(2)
            .background(BLUE, CATALYST)
            .treePos(126, 356));

        CTCDD.build(b -> b
            .prereqs(GPCI, SA)
            .requireAllPrereqs()
            .cost(2)
            .background(RED, CONVERSION)
            .treePos(196, 356));

        QGPIU.build(b -> b
            .prereqs(REC, CTCDD)
            .cost(2)
            .background(BLUE, CATALYST)
            .treePos(126, 416));

        SEFCP.build(b -> b
            .prereqs(QGPIU)
            .cost(3)
            .background(PURPLE, CATALYST)
            .treePos(66, 476));

        TCT.build(b -> b
            .prereqs(QGPIU)
            .cost(3)
            .background(ORANGE, CONVERSION)
            .treePos(126, 476));

        GGEBE.build(b -> b
            .prereqs(QGPIU)
            .cost(3)
            .background(GREEN, CHARGE)
            .treePos(186, 476));

        TPTP.build(b -> b
            .prereqs(GGEBE)
            .cost(4)
            .background(GREEN, CONVERSION)
            .treePos(246, 496));

        DOP.build(b -> b
            .prereqs(CNTI)
            .cost(4)
            .background(PURPLE, CONVERSION)
            .treePos(6, 556));

        CNTI.build(b -> b
            .prereqs(SEFCP)
            .cost(3)
            .background(PURPLE, CHARGE)
            .treePos(66, 536));

        EPEC.build(b -> b
            .prereqs(TCT)
            .cost(3)
            .background(ORANGE, CONVERSION)
            .treePos(126, 536));

        IMKG.build(b -> b
            .prereqs(GGEBE)
            .cost(3)
            .background(GREEN, CHARGE)
            .treePos(186, 536));

        NDPE.build(b -> b
            .prereqs(CNTI)
            .cost(3)
            .background(PURPLE, CHARGE)
            .treePos(66, 596));

        POS.build(b -> b
            .prereqs(EPEC)
            .cost(3)
            .background(ORANGE, CONVERSION)
            .treePos(126, 596));

        DOR.build(b -> b
            .prereqs(IMKG)
            .cost(3)
            .background(GREEN, CONVERSION)
            .treePos(186, 596));

        NGMS.build(b -> b
            .prereqs(NDPE, POS, DOR)
            .cost(4)
            .background(BLUE, CHARGE)
            .treePos(126, 656));

        SEDS.build(b -> b
            .prereqs(NGMS)
            .cost(5)
            .background(BLUE, CONVERSION)
            .treePos(126, 718));

        PA.build(b -> b
            .prereqs(SEDS)
            .cost(6)
            .background(BLUE, CONVERSION)
            .treePos(36, 758));

        CD.build(b -> b
            .prereqs(PA)
            .cost(7)
            .background(BLUE, COMPOSITION)
            .treePos(36, 848));

        TSE.build(b -> b
            .prereqs(CD)
            .cost(8)
            .background(BLUE, CATALYST)
            .treePos(126, 888));

        TBF.build(b -> b
            .prereqs(TSE)
            .cost(9)
            .background(BLUE, CHARGE)
            .treePos(216, 848));

        EE.build(b -> b
            .prereqs(TBF)
            .cost(10)
            .background(BLUE, CONVERSION)
            .treePos(216, 758));

        END.build(b -> b
            .prereqs(EE)
            .cost(12)
            .background(BLUE, COMPOSITION)
            .panelSize(LARGE)
            .treePos(126, 798));

        // spotless:on

        // Build split upgrade set
        SPLIT_UPGRADES = ImmutableSet.of(SEFCP, TCT, GGEBE);

        // Build inverse dependents mapping
        EnumMap<ForgeOfGodsUpgrade, List<ForgeOfGodsUpgrade>> dependencies = new EnumMap<>(ForgeOfGodsUpgrade.class);
        for (ForgeOfGodsUpgrade upgrade : VALUES) {
            for (ForgeOfGodsUpgrade prerequisite : upgrade.prerequisites) {
                dependencies.computeIfAbsent(prerequisite, $ -> new ArrayList<>())
                    .add(upgrade);
            }
        }
        for (var entry : dependencies.entrySet()) {
            ForgeOfGodsUpgrade upgrade = entry.getKey();
            List<ForgeOfGodsUpgrade> deps = entry.getValue();
            if (deps != null) {
                upgrade.dependents = deps.toArray(new ForgeOfGodsUpgrade[0]);
            }
        }
    }

    // Static tree linking
    private ForgeOfGodsUpgrade[] prerequisites;
    private boolean requireAllPrerequisites;

    // Cost
    private int shardCost;
    private final List<ItemStack> extraCost = new ArrayList<>();

    // UI
    private UpgradeColor color;
    private Milestones icon;
    private PanelSize panelSize;
    private int treeX;
    private int treeY;

    // Pre-generated data
    private ForgeOfGodsUpgrade[] dependents = new ForgeOfGodsUpgrade[0];
    private final String name;
    private final String nameShort;
    private final String bodyText;
    private final String loreText;

    ForgeOfGodsUpgrade() {
        this.name = "fog.upgrade.tt." + ordinal();
        this.nameShort = "fog.upgrade.tt.short." + ordinal();
        this.bodyText = "fog.upgrade.text." + ordinal();
        this.loreText = "fog.upgrade.lore." + ordinal();
    }

    private void build(UnaryOperator<Builder> u) {
        Builder b = u.apply(new Builder());

        this.prerequisites = b.prerequisites != null ? b.prerequisites.toArray(new ForgeOfGodsUpgrade[0])
            : new ForgeOfGodsUpgrade[0];
        this.requireAllPrerequisites = b.requireAllPrerequisites;
        this.shardCost = b.shardCost;
        this.color = b.color;
        this.icon = b.icon;
        this.panelSize = b.panelSize;
        this.treeX = b.treeX;
        this.treeY = b.treeY;
    }

    public void addExtraCost(ItemStack... cost) {
        if (extraCost.size() + cost.length > 12) {
            throw new IllegalArgumentException("Too many inputs for Godforge upgrade cost, cannot be more than 12!");
        }
        extraCost.addAll(Arrays.asList(cost));
    }

    public ForgeOfGodsUpgrade[] getPrerequisites() {
        return prerequisites;
    }

    public boolean requiresAllPrerequisites() {
        return requireAllPrerequisites;
    }

    public ForgeOfGodsUpgrade[] getDependents() {
        return dependents;
    }

    public int getShardCost() {
        return shardCost;
    }

    public boolean hasExtraCost() {
        return !extraCost.isEmpty();
    }

    public ItemStack[] getExtraCost() {
        // Ensure this array always has a size of 12
        ItemStack[] cost = new ItemStack[12];
        for (int i = 0; i < extraCost.size(); i++) {
            cost[i] = extraCost.get(i);
        }
        return cost;
    }

    public ItemStack[] getExtraCostNoNulls() {
        List<ItemStack> cost = new ArrayList<>();
        for (ItemStack singleCost : extraCost) {
            if (singleCost != null) {
                cost.add(singleCost);
            }
        }
        return cost.toArray(new ItemStack[0]);
    }

    public UITexture getBackground() {
        return color.getBackground();
    }

    public UITexture getOverlay() {
        return color.getOverlay();
    }

    public UITexture getSymbol() {
        return icon.getSymbolBackground();
    }

    public float getSymbolWidthRatio() {
        return icon.getSymbolWidthRatio();
    }

    public int getPanelSize() {
        return panelSize.getPanelSize();
    }

    public int getBodySize() {
        return panelSize.getBodySize();
    }

    public int getLoreSize() {
        return panelSize.getLoreSize();
    }

    public int getTreeX() {
        return treeX;
    }

    public int getTreeY() {
        return treeY;
    }

    public String getNameKey() {
        return name;
    }

    public String getShortNameKey() {
        return nameShort;
    }

    public String getBodyKey() {
        return bodyText;
    }

    public String getLoreKey() {
        return loreText;
    }

    public static class Builder {

        // Tree linking
        private ObjectList<ForgeOfGodsUpgrade> prerequisites;
        private boolean requireAllPrerequisites;

        // Cost
        private int shardCost;

        // UI
        private UpgradeColor color = BLUE;
        private Milestones icon = CHARGE;
        private PanelSize panelSize = STANDARD;
        private int treeX;
        private int treeY;

        private Builder() {}

        public Builder prereqs(ForgeOfGodsUpgrade... prereqs) {
            if (this.prerequisites != null) {
                throw new IllegalArgumentException("Cannot repeat calls to ForgeOfGodsUpgrade$Builder#prereqs");
            }
            this.prerequisites = new ObjectArrayList<>(prereqs);
            return this;
        }

        public Builder requireAllPrereqs() {
            this.requireAllPrerequisites = true;
            return this;
        }

        // Cost
        public Builder cost(int shards) {
            this.shardCost = shards;
            return this;
        }

        // UI
        public Builder background(UpgradeColor color, Milestones icon) {
            this.color = color;
            this.icon = icon;
            return this;
        }

        public Builder panelSize(PanelSize panelSize) {
            this.panelSize = panelSize;
            return this;
        }

        public Builder treePos(int x, int y) {
            this.treeX = x;
            this.treeY = y;
            return this;
        }
    }

    public enum PanelSize {

        STANDARD(250, 80, 115),
        LARGE(300, 55, 170);

        private final int panelSize;
        private final int bodySize;
        private final int loreSize;

        PanelSize(int panelSize, int bodySize, int loreSize) {
            this.panelSize = panelSize;
            this.bodySize = bodySize;
            this.loreSize = loreSize;
        }

        public int getPanelSize() {
            return panelSize;
        }

        public int getBodySize() {
            return bodySize;
        }

        public int getLoreSize() {
            return loreSize;
        }
    }
}
