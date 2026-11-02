Generates particle beams from specific {color_mechanic:source materials}
{lang:gtnhlanth.tt.beaminfo}
{hr}
NEI has the list of {color_mechanic:source materials} and their stats
Recipes are all one second long and output a single beam packet
Increasing the power above the {color_eu_usage:{EU/t} Required} asymptotically approaches the {light_purple:Max Material Energy}
{hr}
Particle beams have a {dark_purple:particle type}, {blue:energy}, {dark_green:focus}, and {gold:rate}
{dark_purple:Type} determines the charge, rest mass, and §9Max Particle Energy
{blue:Energy} is the energy of the particle beam in electron volts (eV)
{dark_green:Focus} is the width of the particle beam
{gold:Rate} is the amount of particles per packet
{hr}
{blue:Output Beam Energy}{color_formula: = min({blue:Max Particle Energy}, {color_note:aeV})}
where {color_note:aeV}{color_formula: = {light_purple:Max Material Energy} * (1 - 1.001^({blue:-Energy Ratio} * ({color_eu_usage:{EU/t} Provided} - {color_eu_usage:{EU/t} Required})))}
