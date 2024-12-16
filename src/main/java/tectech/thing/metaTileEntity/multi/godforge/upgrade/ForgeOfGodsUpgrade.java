package tectech.thing.metaTileEntity.multi.godforge.upgrade;

import static tectech.thing.metaTileEntity.multi.godforge.upgrade.ForgeOfGodsUpgrade.BGWindowSize.*;
import static tectech.thing.metaTileEntity.multi.godforge.util.MilestoneIcon.*;
import static tectech.thing.metaTileEntity.multi.godforge.util.UpgradeColor.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.List;
import java.util.Set;
import java.util.function.UnaryOperator;

import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

import com.google.common.collect.ImmutableSet;
import com.gtnewhorizons.modularui.api.drawable.UITexture;
import com.gtnewhorizons.modularui.api.math.Pos2d;
import com.gtnewhorizons.modularui.api.math.Size;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.objects.ObjectList;
import tectech.thing.metaTileEntity.multi.godforge.util.MilestoneIcon;
import tectech.thing.metaTileEntity.multi.godforge.util.UpgradeColor;

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
            .windowSize(LARGE)
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
            .prereqs(GISS, FDIM)
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
            .background(BLUE, COMPOSITION)
            .treePos(216, 758));

        END.build(b -> b
            .prereqs(EE)
            .cost(12)
            .background(BLUE, COMPOSITION)
            .windowSize(LARGE)
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
    private MilestoneIcon icon;
    private BGWindowSize windowSize;
    private Pos2d treePos;

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
        this.windowSize = b.windowSize;
        this.treePos = b.treePos;
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
        return extraCost.toArray(new ItemStack[0]);
    }

    public UITexture getBackground() {
        return color.getBackground();
    }

    public UITexture getOverlay() {
        return color.getOverlay();
    }

    public UITexture getSymbol() {
        return icon.getSymbol();
    }

    public float getSymbolWidthRatio() {
        return icon.getWidthRatio();
    }

    public Size getWindowSize() {
        return windowSize.getWindowSize();
    }

    public int getLoreYPos() {
        return windowSize.getLoreY();
    }

    public Pos2d getTreePos() {
        return treePos;
    }

    public String getNameText() {
        return StatCollector.translateToLocal(name);
    }

    public String getShortNameText() {
        return StatCollector.translateToLocal(nameShort);
    }

    public String getBodyText() {
        return StatCollector.translateToLocal(bodyText);
    }

    public String getLoreText() {
        return StatCollector.translateToLocal(loreText);
    }

    public static class Builder {

        // Tree linking
        private ObjectList<ForgeOfGodsUpgrade> prerequisites;
        private boolean requireAllPrerequisites;

        // Cost
        private int shardCost;

        // UI
        private UpgradeColor color = BLUE;
        private MilestoneIcon icon = CHARGE;
        private BGWindowSize windowSize = STANDARD;
        private Pos2d treePos = new Pos2d(0, 0);

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
        public Builder background(UpgradeColor color, MilestoneIcon icon) {
            this.color = color;
            this.icon = icon;
            return this;
        }

        public Builder windowSize(BGWindowSize windowSize) {
            this.windowSize = windowSize;
            return this;
        }

        public Builder treePos(int x, int y) {
            this.treePos = new Pos2d(x, y);
            return this;
        }
    }

    enum BGWindowSize {

        STANDARD(250, 250, 110),
        LARGE(300, 300, 85),

        ;

        private final Size size;
        private final int loreY;

        BGWindowSize(int width, int height, int loreY) {
            this.size = new Size(width, height);
            this.loreY = loreY;
        }

        public Size getWindowSize() {
            return size;
        }

        public int getLoreY() {
            return loreY;
        }
    }
}
