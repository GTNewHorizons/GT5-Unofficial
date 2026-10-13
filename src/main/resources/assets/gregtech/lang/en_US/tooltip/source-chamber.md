Generates particle beams from specific §dsource materials
{lang:gtnhlanth.tt.beaminfo}
{gray:{hr}}
NEI has the list of §dsource materials§7 and their stats
Recipes are all one second long and output a single beam packet
Increasing the power above the §3EU/t Required§7 asymptotically approaches the §dMax Material Energy
{gray:{hr}}
Particle beams have a §5particle type§7, §9energy§7, §2focus§7, and §6rate
§5Type§7 determines the charge, rest mass, and §9Max Particle Energy
§9Energy§7 is the energy of the particle beam in electron volts (eV)
§2Focus§7 is the width of the particle beam
§6Rate§7 is the amount of particles per packet
{gray:{hr}}
§9Output Beam Energy§f = min(§9Max Particle Energy§7, §eaeV§f)
where §eaeV§f = §dMax Material Energy§f * (1 - 1.001^(§9-Energy Ratio§f * (§3EU/t Provided§f - §3EU/t Required§f)))
