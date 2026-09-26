Creates a pocket of spacetime that is bigger on the inside using transdimensional engineering. Certified Time Lord regulation compliant.
{gray:{hr}}
Constantly consumes {gold:Hydrogen} and {gold:Helium} when idle and stores them internally
Has 3 types of {blue:Field Generators}, each with {green:9} tiers and varying effects:
{blue:Spacetime Compression}: Unlocks recipes based on tier. {green:-3%} processing time per tier above recipe requirement (multiplicative)
{blue:Time Dilation}: {green:-50%} recipe time (multiplicative), {green:-9.25%} recipe success chance per tier (additive)
{blue:Stabilisation}: {green:+5%} recipe success chance, {green:-5%} recipe yield per tier (additive)
{gray:{hr}}
All stored {gold:Hydrogen} and {gold:Helium} are consumed during a craft. Going over the {gold:Hydrogen} or {gold:Helium} requirement has a penalty on yield and recipe chance
Overflow Ratio = {aqua:(Stored fluid / Recipe requirement) - 1}
Penalty = {aqua:1 - exp(-(30 * Overflow Ratio)^2)}, subtracted from both yield and success chance
Base chance is per recipe. Yield starts at 1, reduced by penalties, multiplies outputs. {gold:Spacetime} is exempt
{gray:{hr}}
Place a {white:Programmed Circuit} in a bus to overclock. EU output is unaffected
If a recipe fails, outputs {aqua:Success chance * 14,400 * (2.0)^(Recipe tier)L} {gold:Spacetime} instead of fluid/item outputs, with EU output unaffected
Place {white:Astral Array Fabricators} into the input bus for parallel processing. Max stored: {green:8,637}. Each successful parallel produces identical output
Parallel Exponent = {aqua:floor(log(8 * Astral Array amount) / log(1.7))}
Parallel = {aqua:2^(Parallel Exponent)}
When running parallel recipes, power changes:
EU input = {aqua:(Base EU input * 4^OC * 2.3^PE * 1.63) / 20.7}
EU output = {aqua:(Base EU output * 2.3^PE) / 20.7}
{white:OC} is the {white:Programmed Circuit} value and {white:PE} is the {white:Parallel Exponent}
Running parallel consumes {gold:Condensed Raw Stellar Plasma Mixture} instead of {gold:Hydrogen} and {gold:Helium}. Required: {aqua:(12.4 / 10^6 * Helium amount * Parallel)}. Each parallel success is independent
{gray:{hr}}
{yellow:Consumes EU directly from your wireless network}
