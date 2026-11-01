Creates a pocket of spacetime that is bigger on the inside using transdimensional engineering.
Certified Time Lord regulation compliant.
{gold:{hr}}
Comprised of three different Field Generators with {green:9} tiers each:
{blue:Spacetime Compression}: Determines max Recipe Tier, {green:-3%} processing time per tier above recipe (multiplicative)
{blue:Time Dilation}: {green:-50%} recipe duration per tier (multiplicative), {green:-9.25%} recipe success chance per tier (additive)
{blue:Stabilisation}: {green:+5%} recipe success chance per tier (additive), {green:-5%} yield per tier (additive)
Lower tier stabilisation generators also reduce the total output {EU},
starting at {green:-40%} with T1 Crude and increases by {green:+5%} per tier (additive)
{gold:{hr}}
Right-click the controller with a {blue:Planet Block} to insert it and set the recipe
Base recipe success chance {green:(S)} depends on the Planet Tier
On success, outputs everything listed in NEI. The base yield for fluids starts at {green:100%} before any penalties
On failure, outputs {green:Spacetime}{color_formula: = S * 14,400 * 2^(Planet Tier)} and any output {EU}
Pity guarantees a success after several consecutive failures but resets on success or recipe change
{gold:{hr}}
Constantly inputs {blue:Hydrogen} and {blue:Helium} into its own internal buffer while idle, and consumes everything on recipe start
Exceeding the minimum required amount for a recipe heavily reduces the recipe success chance and yield
{green:Overflow Ratio}{color_formula: = (Stored Fluid / Min Fluid) - 1}
{green:Penalty%}{color_formula: = 1 - exp(-(30 * Overflow Ratio)^2)}
{gold:{hr}}
{yellow:Consumes {EU} directly from the owner's wireless network}
Set the desired number of overclocks with a {color_name:Programmed Circuit} in the input bus
Output {EU} is constant and does not scale with the number of overclocks
{gold:{hr}}
Insert {blue:Astral Array Fabricators (AA)} through an input bus to add parallels (max {blue:8,637})
Each one is rolled independently to determine whether it succeeds or not
{green:Parallel Exponent (PE)}{color_formula: = floor(log(8 * AA) / log(1.7))}
{green:Parallels}{color_formula: = 2^(PE)}
{gold:{hr}}
When running parallels, there is no more pity and the power changes:
{green:{EU} Input}{color_formula: = Base {EU} Input * 4^OC * 2.3^PE * 1.63 / 20.7}
{green:{EU} Output}{color_formula: = Base {EU} Output * 2.3^(PE-1) / 9}
Also consumes {blue:Condensed Raw Stellar Plasma Mixture} instead of {blue:Hydrogen} and {blue:Helium}:
{green:Raw Stellar Plasma}{color_formula: = 12.4 / 10^6 * Helium Amount * Parallels}
