(Dis)solves all your problems!
{hr}
Optionally supply a {aqua:Base} to boost {color_mechanic:Efficiency}:
{aqua:{var:sodium_hydroxide_rate}/min} of {aqua:Sodium Hydroxide} : {color_mechanic:{var:sodium_hydroxide_eff}%}
{aqua:{var:potassium_hydroxide_rate}/min} of {aqua:Potassium Hydroxide} : {color_mechanic:{var:potassium_hydroxide_eff}%}
{aqua:{var:caesium_hydroxide_rate}/min} of {aqua:Caesium Hydroxide} : {color_mechanic:{var:caesium_hydroxide_eff}%}
{aqua:{var:francium_hydroxide_rate}/min} of {aqua:Francium Hydroxide} : {color_mechanic:{var:francium_hydroxide_eff}%}
{hr}
Produces {dark_green:Toxic Residue} from burning acids
If the {dark_green:Toxic Residue} exceeds {dark_aqua:Capacity}, then the machine {color_danger:Explodes}
Every tick, {dark_green:Toxic Residue} increases by {green:0.05 * Fuel Density ({EU}/{L})^0.8 * Fuel Consumption ({L}) * rand({var:residue_rand_min}, {var:residue_rand_max})}
Every tick, {dark_green:Toxic Residue} decays by {green:Base Decay * Decay Boost * Toxic Residue^0.08}
{yellow:Residue decays 10x slower when the multiblock is disabled}
{hr}
Insert up to {blue:16 Robot Arms} to increase {blue:Decay Boost}:
{white:Decay Boost =} {green:sqrt(Robot Arms) * N^(Robot Arm Tier - 1)} where {green:N=1.2} ({tier-color:tier_iv:{VN:tier_iv}-}) or {green:N=1.4} ({tier-color:tier_luv:{VN:tier_luv}+})
Every minute, there is a {green:Robot Arms / (45 * (Robot Arm Tier + 1))} chance to void exactly one {blue:Robot Arm}
{hr}
The structure has {color_mode:3 Tiers} which determine {blue:Base Decay} and {dark_aqua:Capacity}:
{color_mode:T1} : {color_mode:Strengthened Inanimate Machine Casing} : {blue:{var:tier1_decay}} : {dark_aqua:{var:tier1_capacity}}
{color_mode:T2} : {color_mode:Precise Stationary Machine Casing} : {blue:{var:tier2_decay}} : {dark_aqua:{var:tier2_capacity}}
{color_mode:T3} : {color_mode:Ultimate Static Machine Casing} : {blue:{var:tier3_decay}} : {dark_aqua:{var:tier3_capacity}}
