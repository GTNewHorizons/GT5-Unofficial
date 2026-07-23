Creates a pocket of spacetime that is bigger on the inside using transdimensional
engineering. Certified Time Lord regulation compliant. This multi uses too much EU
to be handled with conventional means. All EU requirements are handled directly by
your wireless EU network
{hr}
This multiblock will constantly consume hydrogen and helium when it is not running a
recipe once per second. It will store this internally, you can see the totals by
using a scanner. This multi also has three tiered blocks with {red}9{gray} tiers
each. They are as follows and have the associated effects on the multi:
{blue}Spacetime Compression Field Generator:
- The tier of this block determines what recipes can be run. If the multiblocks
  spacetime compression field block exceeds the requirements of the recipe it
  will decrease the processing time by {red}3%{gray} per tier over the requirement (multiplicative)
{blue}Time Dilation Field Generator:
- Decreases the time required for a recipe by {red}50%{gray} per tier of block (multiplicative)
  Decreases the probability of a recipe succeeding by {red}9.25%{gray} per tier (additive)
{blue}Stabilisation Field Generator:
- Increases the probability of a recipe succeeding by {red}5%{gray} per tier (additive)
  Decreases the yield of a recipe by {red}5%{gray} per tier (additive).
  > Low tier stabilisation field generators have a power output penalty
     The power output penalty for using Crude Stabilisation Field Generators is {red}40%
     This penalty decreases by {red}5%{gray} per tier (additive)
{hr}
Going over a recipe requirement on hydrogen or helium has a penalty on yield and recipe chance
All stored hydrogen and helium is consumed during a craft. The associated formulas are:
{green}Overflow ratio = (Stored fluid / Recipe requirement) - 1
{green}Adjustment value = 1 - exp(-(30 * Overflow ratio)^2)
The Adjustment value is then subtracted from the total yield and recipe chance
{hr}
It should be noted that base recipe chance is determined per recipe and yield always starts
at 1 and subtracts depending on penalties. All fluid/item outputs are multiplied by the
yield. Failure fluid is exempt
{hr}
This multiblock can only output to ME output buses/hatches
{hr}
This multiblock can be overclocked by placing a programmed circuit into the input bus
E.g. A circuit of 2 will provide 2 OCs, 16x EU input and 0.25x the time. EU output is unaffected
All outputs are equal. All item and fluid output chances & amounts per recipe are unaffected
{hr}
If a recipe fails the EOH will output {green}Success chance * 14,400 * (2.0)^(Recipe tier){gray}L of molten
SpaceTime instead of fluid/item outputs and output as much EU as a successful recipe
{hr}
This multiblock can perform parallel processing by placing Astral Array Fabricators into the input bus
They are stored internally and can be retrieved via right-clicking the controller with a wire cutter
The maximum amount of stored Astral Arrays is 8,637. Parallel amount is calculated via these formulas:
{green}Parallel exponent = floor(log(8 * Astral Array amount) / log(1.7))
{green}Parallel = 2^(Parallel exponent)
If the EOH is running parallel recipes, the power calculation changes
The power needed for parallel processing is calculated as follows:
{green}total EU = ((EU output - EU input * 1.63) / 20.7) * 2.3^(Parallel exponent)
Furthermore, if parallel recipes are run, the recipes consume Condensed Raw Stellar Plasma Mixture
instead of helium and hydrogen. Overflow penalties still apply
The required amount of fluid to start a recipe is {green}12.4 / 10^6 * Helium amount * Parallel
The success or failure of each parallel is determined independently
{hr}
Animations can be disabled by using a screwdriver on the multiblock
Planet block can be inserted directly by right-clicking the controller with planet block
