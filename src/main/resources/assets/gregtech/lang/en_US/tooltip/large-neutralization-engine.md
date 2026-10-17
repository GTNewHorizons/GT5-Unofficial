(Dis)solves all your problems!
{gray:{hr}}
Optionally supply a {aqua:Base} to boost {light_purple:Efficiency}:
{aqua:{var:sodium_hydroxide_rate}/min} of {aqua:Sodium Hydroxide} : {light_purple:{var:sodium_hydroxide_eff}%}
{aqua:{var:potassium_hydroxide_rate}/min} of {aqua:Potassium Hydroxide} : {light_purple:{var:potassium_hydroxide_eff}%}
{aqua:{var:caesium_hydroxide_rate}/min} of {aqua:Caesium Hydroxide} : {light_purple:{var:caesium_hydroxide_eff}%}
{aqua:{var:francium_hydroxide_rate}/min} of {aqua:Francium Hydroxide} : {light_purple:{var:francium_hydroxide_eff}%}
{gray:{hr}}
Produces {dark_green:Toxic Residue} from burning acids
If the {dark_green:Toxic Residue} exceeds {dark_aqua:Capacity}, then the machine {red:Explodes}
Every tick, {dark_green:Toxic Residue} increases by {green:0.05 * Fuel Density (EU/L)^0.8 * Fuel Consumption ({var:fluid_unit}) * rand({var:residue_rand_min}, {var:residue_rand_max})}
Every tick, {dark_green:Toxic Residue} decays by {green:Base Decay * Decay Boost * Toxic Residue^0.08}
{yellow:Residue decays 10x slower when the multiblock is disabled}
{gray:{hr}}
Insert up to {blue:16 Robot Arms} to increase {blue:Decay Boost}:
{white:Decay Boost =} {green:sqrt(Robot Arms) * N^(Robot Arm Tier - 1)} where {green:N=1.2} ({var:tier_iv}§7-) or {green:N=1.4} ({var:tier_luv}§7+)
Every minute, there is a {green:Robot Arms / (45 * (Robot Arm Tier + 1))} chance to void exactly one {blue:Robot Arm}
{gray:{hr}}
The structure has {gold:3 Tiers} which determine {blue:Base Decay} and {dark_aqua:Capacity}:
{gold:T1} : {gold:Strengthened Inanimate Machine Casing} : {blue:{var:tier1_decay}} : {dark_aqua:{var:tier1_capacity}}
{gold:T2} : {gold:Precise Stationary Machine Casing} : {blue:{var:tier2_decay}} : {dark_aqua:{var:tier2_capacity}}
{gold:T3} : {gold:Ultimate Static Machine Casing} : {blue:{var:tier3_decay}} : {dark_aqua:{var:tier3_capacity}}
