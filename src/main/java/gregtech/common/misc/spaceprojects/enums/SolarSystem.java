package gregtech.common.misc.spaceprojects.enums;

import static gregtech.api.enums.Mods.GregTech;
import static gregtech.common.misc.spaceprojects.enums.SpaceBodyType.*;
import static gregtech.common.misc.spaceprojects.enums.StarType.*;

import com.gtnewhorizons.modularui.api.drawable.UITexture;

import gregtech.common.misc.spaceprojects.SpaceProjectManager;
import gregtech.common.misc.spaceprojects.interfaces.ISpaceBody;

/**
 * An enum of all space bodies in the Sol Solar System. Or to be exact the more important ones
 *
 * @author BlueWeabo
 */
public enum SolarSystem implements ISpaceBody {

    Sol(Star, GClass),
    Overworld(Planet),
    Moon(NaturalSatellite),
    Mars(Planet),
    Deimos(NaturalSatellite),
    Phobos(NaturalSatellite),
    Mercury(Planet),
    Venus(Planet),
    Jupiter(GasGiant),
    Io(NaturalSatellite),
    Ganymede(NaturalSatellite),
    Europa(NaturalSatellite),
    Callisto(NaturalSatellite),
    Saturn(GasGiant),
    Mimas(NaturalSatellite),
    Enceladus(NaturalSatellite),
    Tethys(NaturalSatellite),
    Rhea(NaturalSatellite),
    Titan(NaturalSatellite),
    Hyperion(NaturalSatellite),
    Iapetus(NaturalSatellite),
    Phoebe(NaturalSatellite),
    Uranus(IceGiant),
    Miranda(NaturalSatellite),
    Ariel(NaturalSatellite),
    Umbriel(NaturalSatellite),
    Titania(NaturalSatellite),
    Oberon(NaturalSatellite),
    Neptune(IceGiant),
    Proteus(NaturalSatellite),
    Triton(NaturalSatellite),
    Nereid(NaturalSatellite),
    Ceres(DwarfPlanet),
    Pluto(DwarfPlanet),
    Arrokoth(DwarfPlanet),
    MakeMake(DwarfPlanet),
    KuiperBelt(AsteroidBelt),
    NONE(SpaceBodyType.NONE);

    private SpaceBodyType spaceBody;
    private StarType star;
    private UITexture texture;

    SolarSystem(SpaceBodyType aType) {
        this(aType, NotAStar);
    }

    SolarSystem(SpaceBodyType aType, StarType aStarType) {
        star = aStarType;
        spaceBody = aType;
        texture = UITexture.fullImage(GregTech.ID, "solarsystem/" + getName());
        SpaceProjectManager.addLocation(this);
    }

    @Override
    public StarType getStarType() {
        return star;
    }

    @Override
    public SpaceBodyType getType() {
        return spaceBody;
    }

    @Override
    public String getName() {
        return name();
    }

    @Override
    public UITexture getTexture() {
        return texture;
    }

    @Override
    public String getUnlocalizedName() {
        return "gt.solar.system." + getName().toLowerCase();
    }
}
