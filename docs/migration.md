# Migration guides

## Generic migration
Migration for the typical mod which doesn't use anything special but Minecraft forge and some library dependencies.
For core plugin, Mixins, shadowing, access transformers, ASM or etc. you'll need to do some extra steps.
If they are missing in this document - we will gladly receive your suggestions/contribution.

1. Copy and replace all files from [template](https://github.com/GTNewHorizons/ExampleMod1.7.10/releases/download/latest-packages/migration.zip) to your repository, but `build.gradle`
2. Copy all repositories from your `build.gradle(.kts)` to `repositories.gradle`
3. Copy all dependencies from your `build.gradle(.kts)` to `dependecies.gradle`
4. replace your `build.gradle(.kts)` with `build.gradle` from template. In case you have written some custom tasks/configurations not present in the template - move them into `addon.gradle`. It will automatically be integrated if present.
5. Adapt `gradle.properties` to your mod
6. Ensure `src/main/resources/mcmod.info` contains `${modId}`, `${modName}`. `${modVersion}` and `${minecraftVersion}`
7. Re-import the project to your IDE (e.g. restart with clean caches in IntelliJ IDEA)
8. Run `./gradlew clean setupDecompWorkspace`

## Mixin configuration
For the reference checkout the [example mixin configuration branch](https://github.com/GTNewHorizons/ExampleMod1.7.10/tree/example-mixins) of the template.

1. Extract mixins package and plugin configuration from `mixins.yourModId.json` to `gradle.properties`
2. Implement MixinPlugin according to example from the reference
3. Remove mixins.mymodid.json
