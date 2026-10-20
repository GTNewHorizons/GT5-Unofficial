Accelerates particles to high energies and then collides them to generate new particles
Electrically neutral particles are unaffected
Set the {color_electron_volt:Target Beam Energy} (up to {color_electron_volt:{var:max_energy} {TeV}}) and the {light_purple:maximum number of cycles}
Cycles every second
{hr}
{color_mode:Accelerator mode}
Accelerates the beam in the smaller LHC ring
Each cycle increases the {color_electron_volt:Beam Energy} by {color_eu_usage:{EU} consumed in cycle} * {color_electron_volt:{var:ev_per_eu} {eV}} / {gold:Beam Rate}
until the {color_electron_volt:Target Beam Energy} or the {light_purple:maximum number of cycles} is reached
If the {color_electron_volt:Target Beam Energy} is reached, adds {color_formula:{var:rate_gain}} * {gold:Beam Rate} to the current {gold:Beam Rate}
Above a {gold:Beam Rate} of {color_formula:{var:nerf_cutoff}}, adds ({color_formula:{var:rate_gain}} * {gold:Beam Rate})^{dark_red:{var:nerf_power}} instead
{color_eu_usage:Power} cost starts at {color_amperage:1A} of {var:tier} per {gold:Beam Rate} and grows quadratically with the {light_purple:number of completed cycles}
{color_eu_usage:P} = ({color_amperage:1A} {var:tier}) * {gold:R} * ({light_purple:N}+1)^2
Switches to Collider mode automatically when the {light_purple:maximum number of cycles} is reached
{hr}
{color_mode:Collider mode}
Splits the beam into {color_formula:two} beams that go in opposite directions in the larger LHC ring and collide to generate new particles
The {color_electron_volt:Collision Energy} is two times the {color_electron_volt:Beam Energy}
A particle appears in the outputs once the {color_electron_volt:Collision Energy} exceeds its rest mass
{color_name:Filtered Beam Output Hatches} and the Probability Table show the list of possible outputs and let you filter them
{hr}
{color_mode:Booster Fluids}
{color_name:Booster Fluids} raise the {dark_red:scaling exponent} for {gold:Beam Rates} above {color_formula:{var:nerf_cutoff}}
{color_name:No fluid}: {dark_red:{var:nerf_power}}
{color_name:{fluid:quarkgluonplasma}}: {dark_red:{var:nerf_power_qgp}}
{color_name:{fluid:molten.magmatter}}: {dark_red:{var:nerf_power_magmatter}}
With a booster, the {color_electron_volt:Beam Energy} is set to {color_electron_volt:{var:max_energy} {TeV}} at once, and {gold:Beam Rate {L}} of fluid is consumed every cycle
If the supplied {color_name:Booster Fluid} changes, the beam must be re-accelerated from scratch
{hr}
There are four LHC modules, marked by the letter on top of the module:
{aqua:E - Electromagnetism}: Charged Matter Sensor ({aqua:CMS})
{dark_green:W - Weak Interaction}: Advanced Total Lepton Assimilation Snare ({dark_green:ATLAS})
{white:S - Strong Force}: Absolute Lattice Integrated Chromodynamic Encapsulator ({white:ALICE})
{dark_purple:G - Gravity}: Localized Horizon Curvature Binder ({dark_purple:LHCb})
