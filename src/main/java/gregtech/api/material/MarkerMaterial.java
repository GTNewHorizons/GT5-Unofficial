package gregtech.api.material;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.jetbrains.annotations.Nullable;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneType;

/// A minimal [IOreMaterial] that exists only to name an ore-dictionary entry. It carries no composition and no
/// generated items: its whole purpose is to supply the [#getInternalName] that
/// [gregtech.api.objects.ItemData#toString] concatenates onto a prefix to form an ore-dictionary name (e.g.
/// `componentCircuit` + `Resistor`), and to serve as the material identity behind that entry.
///
/// A marker may additionally carry [SubTag]s and name a legacy [Materials] target for smelting, macerating, or
/// arc-smelting, set fluently after construction (e.g. [#smeltingInto]). Both are empty/absent by default, so a
/// marker built with only the constructor behaves exactly as one that names an entry and nothing more --
/// [#contains] returns `false` and every target getter returns `null`. Targets are stored as lazy [Supplier]s
/// rather than resolved [Materials] so a target constructed after this marker still resolves correctly, mirroring
/// `Materials`' own `setSmeltingInto`/`setMaceratingInto`/`setArcSmeltingInto` builder methods.
///
/// Lives in `gregtech.api.material` rather than alongside its construction site because it is an API-level
/// implementer of the core [IOreMaterial] contract, on the same footing as the other material systems that
/// implement it.
///
/// [#toString] returns the internal name so that a marker routed through [OrePrefixes#oreDictName(IOreMaterial)]
/// (`name + material`) stringifies to the exact ore-dictionary name a `Materials`-backed marker produced through
/// [gregtech.api.objects.ItemData#toString].
public final class MarkerMaterial implements IOreMaterial {

    private final String internalName;
    private final String defaultLocalName;
    private final TextureSet textureSet;
    private final short[] rgba;
    private final boolean unifiable;
    private final Set<SubTag> subTags = new LinkedHashSet<>();
    private Supplier<Materials> smeltInto;
    private Supplier<Materials> macerateInto;
    private Supplier<Materials> arcSmeltInto;
    private Supplier<Materials> arcSmeltIntoGas;

    public MarkerMaterial(String internalName, String defaultLocalName, TextureSet textureSet, int argb) {
        this(internalName, defaultLocalName, textureSet, argb, true);
    }

    public MarkerMaterial(String internalName, String defaultLocalName, TextureSet textureSet, int argb,
        boolean unifiable) {
        this.internalName = internalName;
        this.defaultLocalName = defaultLocalName;
        this.textureSet = textureSet;
        this.rgba = new short[] { (short) ((argb >>> 16) & 0xFF), (short) ((argb >>> 8) & 0xFF), (short) (argb & 0xFF),
            (short) ((argb >>> 24) & 0xFF) };
        this.unifiable = unifiable;
    }

    /// Whether an ore-dictionary entry named for this marker participates in unification, mirroring
    /// `Materials#mUnifiable`. Consulted by [gregtech.common.GTProxy#registerOre] when a foreign ore resolves
    /// to this marker.
    public boolean isUnifiable() {
        return unifiable;
    }

    @Override
    public String getInternalName() {
        return internalName;
    }

    @Override
    public String getDefaultLocalName() {
        return defaultLocalName;
    }

    @Override
    public Materials getGTMaterial() {
        return null;
    }

    @Override
    public int getId() {
        return -1;
    }

    @Override
    public short[] getRGBA() {
        return rgba;
    }

    @Override
    public TextureSet getTextureSet() {
        return textureSet;
    }

    @Override
    public List<IStoneType> getValidStones() {
        return StoneType.STONES;
    }

    @Override
    public boolean generatesPrefix(OrePrefixes prefix) {
        return false;
    }

    @Override
    public void addTooltips(List<String> list) {}

    @Override
    public boolean contains(SubTag tag) {
        return subTags.contains(tag);
    }

    @Override
    public MarkerMaterial add(SubTag... tags) {
        if (tags != null) for (SubTag tag : tags) if (tag != null) subTags.add(tag);
        return this;
    }

    @Override
    public boolean remove(SubTag tag) {
        return subTags.remove(tag);
    }

    /// Sets what this marker smelts into.
    public MarkerMaterial smeltingInto(Supplier<Materials> target) {
        smeltInto = target;
        return this;
    }

    /// Sets what this marker macerates into.
    public MarkerMaterial maceratingInto(Supplier<Materials> target) {
        macerateInto = target;
        return this;
    }

    /// Sets what this marker arc smelts into.
    public MarkerMaterial arcSmeltingInto(Supplier<Materials> target) {
        arcSmeltInto = target;
        return this;
    }

    /// Sets what this marker arc smelts into when a specific gas is used.
    public MarkerMaterial arcSmeltingIntoWithGas(Supplier<Materials> gas, Supplier<Materials> target) {
        arcSmeltIntoGas = gas;
        arcSmeltInto = target;
        return this;
    }

    /// The [Materials] this marker smelts into, or `null` if [#smeltingInto] was never called.
    public @Nullable Materials getSmeltInto() {
        return smeltInto == null ? null : smeltInto.get();
    }

    /// The [Materials] this marker macerates into, or `null` if [#maceratingInto] was never called.
    public @Nullable Materials getMacerateInto() {
        return macerateInto == null ? null : macerateInto.get();
    }

    /// The [Materials] this marker arc smelts into, or `null` if neither [#arcSmeltingInto] nor
    /// [#arcSmeltingIntoWithGas] was called.
    public @Nullable Materials getArcSmeltInto() {
        return arcSmeltInto == null ? null : arcSmeltInto.get();
    }

    /// The gas [Materials] this marker's arc smelt target requires, or `null` if [#arcSmeltingIntoWithGas] was
    /// never called.
    public @Nullable Materials getArcSmeltIntoGas() {
        return arcSmeltIntoGas == null ? null : arcSmeltIntoGas.get();
    }

    @Override
    public String toString() {
        return internalName;
    }
}
