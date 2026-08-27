Accelerates particle beams until they produce a photon beam from the relativistic Larmor effect
Electrically neutral §5particles§7 and beams with less than 25 §2Focus§7 are therefore unaffected
{lang:gtnhlanth.tt.beaminfo}
{gray:{hr}}
Requires §f32,000 {var:fluidUnit}/s§7 of §bcoolant§7 to run, returns as §chot coolant
{lang:gtnhlanth.tt.coolant.oxygen}
{lang:gtnhlanth.tt.coolant.nitrogen}
{lang:gtnhlanth.tt.coolant.coolant}
{lang:gtnhlanth.tt.coolant.Scoolant}
{gray:{hr}}
Increasing power increases the §6Output Beam Rate
Preserve §2Beam Focus§7 more effectively with lower temperature §bcoolant
Increasing the tier of the §dAntenna Casings§7 improves the efficiency of the machine
The §9energies§7 in the following formulae must all be in keV
{gray:{hr}}
§9Output Beam Energy§f = §eIR§f * §ePower Scale Factor
where §eIR§f = (§9Input Beam Energy§f)^(1.13 * (§dAntenna Tier§f)^(4/9)) / 40,000,000
and §ePower Scale Factor§f = 1 - 0.15^(§3Total EU/t Provided§f / (30,384 * (§dAntenna Tier§f)^2.5))
{gray:{hr}}
§2Output Beam Focus§f = (§2Input Beam Focus§f + §eMachine Focus§f)/2.5§7 if §2Input Beam Focus§f > §eMachine Focus
§2Output Beam Focus§f = §2Input Beam Focus§f * §eMachine Focus§f/100§7 if §2Input Beam Focus§f <= §eMachine Focus
where §eMachine Focus§f = max(10, min(90, 1.5^(12 - §bCoolant Temperature§f/40)))
{gray:{hr}}
§6Output Beam Rate§f = floor(2.5^(§dAntenna Tier§f) * sqrt(§3Total EU/t Provided§f) * §6Input Beam Rate§f/15,000)
