package gregtech.api.material;

import java.util.List;

import gregtech.api.enums.Materials;
import gregtech.api.enums.OrePrefixes;
import gregtech.api.enums.StoneType;
import gregtech.api.enums.SubTag;
import gregtech.api.enums.TextureSet;
import gregtech.api.interfaces.IOreMaterial;
import gregtech.api.interfaces.IStoneType;
import gregtech.api.interfaces.ISubTagContainer;

/// A minimal [IOreMaterial] that exists only to name an ore-dictionary entry. It carries no composition, no
/// [SubTag]s, and no generated items: its whole purpose is to supply the [#getInternalName] that
/// [gregtech.api.objects.ItemData#toString] concatenates onto a prefix to form an ore-dictionary name (e.g.
/// `componentCircuit` + `Resistor`), and to serve as the material identity behind that entry.
///
/// Lives in `gregtech.api.material` rather than alongside its construction site because it is an API-level
/// implementer of the core [IOreMaterial] contract, on the same footing as the other material systems that
/// implement it.
///
/// [#toString] returns the internal name so that a marker routed through [OrePrefixes#get(Object)]'s non-`Materials`
/// branch (`name + material`) stringifies to the exact ore-dictionary name a `Materials`-backed marker produced
/// through [gregtech.api.objects.ItemData#toString].
public final class MarkerMaterial implements IOreMaterial {

    private final String internalName;
    private final String defaultLocalName;
    private final TextureSet textureSet;
    private final short[] rgba;

    public MarkerMaterial(String internalName, String defaultLocalName, TextureSet textureSet, int argb) {
        this.internalName = internalName;
        this.defaultLocalName = defaultLocalName;
        this.textureSet = textureSet;
        this.rgba = new short[] { (short) ((argb >>> 16) & 0xFF), (short) ((argb >>> 8) & 0xFF), (short) (argb & 0xFF),
            (short) ((argb >>> 24) & 0xFF) };
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
        return false;
    }

    @Override
    public ISubTagContainer add(SubTag... tags) {
        return this;
    }

    @Override
    public boolean remove(SubTag tag) {
        return false;
    }

    @Override
    public String toString() {
        return internalName;
    }
}
