# introduction

GT_RecipeBuilder is the replacement of GT_Values.RA.addXXXX for constructing and adding recipes.
Compared to the old style, this one utilizes the builder pattern to

1. allow greater flexibility in API.
2. allow us to unify the AssLine recipes and everything else
3. a much nicer syntax than an awfully long line, i.e. more readability

## metadata

this corresponds to the various int/long parameter on the old recipe adder interface.
See implosion compressor recipe map for a example on its usage.
the extension also made it possible to use more complicated value, however there is no such use case yet.

# Coding Conventions

## Metadata identifiers

1. naming uses snake case, e.g. `my_metadata_identifier`
2.

## complicated recipe adder

1. If one invocation of recipe adder would add multiple recipe to same recipe map, give that recipe map a recipeEmitter
2. If one invocation of recipe adder would conditionally add recipe, define a new IGT_RecipeMap in GT_RecipeConstants
3. If one invocation of recipe adder would add recipe to multiple recipe map,
   1. If all recipe maps involved receive recipe only via this type of adding, use the chaining mechanism offered by GT_RecipeMap, i.e. addDownstream().

      e.g.sMultiblockElectrolyzerRecipes and sElectrolyzerRecipes
   2. Otherwise, define a new IGT_RecipeMap in GT_RecipeConstants.
4. If the target isn't a real recipe map (e.g. AssLine stuff), define a new IGT_RecipeMap in GT_RecipeConstants.

## Downstream in an addon

This assumes you need to generate recipe into your own recipe map from a parent recipe map.

## deep copy or not

There is no need to do deep copy EXCEPT you are downstream.
If you do modify the values in a downstream recipe map, call IGT_RecipeMap.deepCopyInput() before adding yourself as a downstream.

## setRecipeSpecialHandler or setRecipeEmitterSingle

Prefer setRecipeSpecialHandler, unless it would throw exception on builder.build().

## Special Value and Special Item

These are considered legacy. IGT_RecipeMap should avoid using these and use the more readable metadata system.

## Use recipe builder or add() directly inside IGT_RecipeMap.doAdd()?

You SHOULD use the recipe builder and delegate further processing to the doAdd() on that recipe map. e.g. UniversalDistillation
However, there are situations that you need to bypass those logic. Then add() is a valid choice.

## Reassign builder variable

No. Just like StringBuilder, you should not do this. Builder is guaranteed to return itself, not a copy.

Reassigning wouldn't break anything though. This is a coding convention to help the code to stay organized.
