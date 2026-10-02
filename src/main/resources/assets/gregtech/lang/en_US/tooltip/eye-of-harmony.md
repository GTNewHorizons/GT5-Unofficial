Creates a pocket of spacetime that is bigger on the inside using transdimensional engineering.
Certified Time Lord regulation compliant.
{gold:{hr}}
Comprised of three different Field Generators with {green:9} tiers each:
{blue:Spacetime Compression}: Determines max recipe tier, {green:-3%} processing time per tier above recipe (multiplicative)
{blue:Time Dilation}: {green:-50%} recipe duration per tier (multiplicative), {green:-9.25%} recipe success chance per tier (additive)
{blue:Stabilisation}: {green:+5%} recipe success chance per tier (additive), {green:-5%} yield per tier (additive)
Lower tier stabilisation generators also reduce the total output EU,
starting at {green:-40%} with T1 Crude and increases by {green:+5%} per tier (additive)
{gold:{hr}}
Right-click the controller with a {blue:Planet Block} to insert it and set the recipe
Base recipe success chance {green:(S)} depends on the planet tier
On success, outputs everything listed in NEI. The base yield for fluids starts at {green:100%} before any penalties
On failure, outputs {white:Spacetime =} {green:S * 14,400 * 2^(Planet Tier)} and any output EU
Pity guarantees a success after several consecutive failures but resets on success or recipe change
{gold:{hr}}
Constantly inputs {blue:Hydrogen} and {blue:Helium} into its own internal buffer while idle, and consumes everything on recipe start
Exceeding the minimum required amount for a recipe heavily reduces the recipe success chance and yield
{white:Overflow Ratio =} {green:(Stored Fluid / Min Fluid) - 1}
{white:Penalty% =} {green:1 - exp(-(30 * Overflow Ratio)^2)}
{gold:{hr}}
{yellow:Consumes EU directly from the owner's wireless network}
Set the desired number of overclocks with a {white:Programmed Circuit} in the input bus
Output EU is constant and does not scale with the number of overclocks
{gold:{hr}}
Insert {blue:Astral Array Fabricators (AA)} through an input bus to add parallels (max {blue:8,637})
Each one is rolled independently to determine whether it succeeds or not
{white:Parallel Exponent (PE) =} {green:floor(log(8 * AA) / log(1.7))}
{white:Parallels =} {green:2^(PE)}
{gold:{hr}}
When running parallels, there is no more pity and the power changes:
{white:EU Input =} {green:Base EU Input * 4^OC * 2.3^PE * 1.63 / 20.7}
{white:EU Output =} {green:Base EU Output * 2.3^(PE-1) / 9}
Also consumes {blue:Condensed Raw Stellar Plasma Mixture} instead of {blue:Hydrogen} and {blue:Helium}:
{white:Raw Stellar Plasma =} {green:12.4 / 10^6 * Helium Amount * Parallels}