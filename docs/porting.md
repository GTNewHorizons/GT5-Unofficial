# Porting guidelines

This is a list of steps which should help you on your probably not so easy journey of porting some mod:

### 1. Setting up repository and build system
1. Checkout any information in mod REAMDE/Wiki/Docs to find out if there are any special tasks/configs that need to be applied to the build
2. Fork original repository to preserve commit history
3. Apply build migration as explained in [migration guidelines](migration.md) on your fork

### 2. Refining the fork
Try to get rid of dependencies on concrete jars (usually in the `lib` folder) if any present. This way it will be much easier to change (upgrade/downgrade) your project dependencies, when needed.

Check if they are in maven repository (usually authors put such information in the project readme), if it isn't accessible but project is open source with permissive enough license (e.g., MIT) - you still can publish them yourselves:
   1. Fork the repository
   2. Drop `jitpack.yml` and `.github/workflows/gradle.yml` in project root. You can find this file in this repository root.
   3. Make sure everything builds from console by running `./gradlew clean setupCIWorkspace`
   4. If all is fine/after fixing the errors - make a tag on Github or using console, this should trigger Github build hook and generate a release
   4. Lookup forked repository on `https://jitpack.io/`
   5. Find your release and click "Get it", which should scroll you down to the example of how to add the dependency (make sure you have jitpack repository in mod you are porting)
   6. Checkout build log beside button you clicked to make sure it succeeds

Now when you are sure dependency is available in maven repository - just add it as a normal gradle dependency in `dependencies.gradle`.

If there is not online dependency available, you may upload it as a jar to jitpack, see [jitpack single file publishing thread](https://gist.github.com/jitpack-io/f928a858aa5da08ad9d9662f982da983). Please ensure, that you have the rights to do so!

There may also be a case where mods depend on another mods - then you'll need to port any dependencies first. (Yay, dependency hell! :D)

### 3. Preparing for porting
Try to build the project and see check what types of errors are you getting. Generally, there should be 2 types of errors you encounter:
   - Missing references to packages/classes/methods/fields/parameters. Things get renamed, moved, restructured, removed or even not yet exist. That's the straightforward part - you'll need to adjust references and way things are invoked.
    In case of missing things, you'll either need to implement something that's imitates missing parts or resign from some functionality
   - Build related errors (e.g., something that is a part of the mod in never versions previously was an external library - you'll need to add it as a dependencies)

Fix all build related errors (so build system won't get in your way)

### 4. Porting the mod
After all these preparations nothing should be in the way of porting the mod, the only thing left is the actual code to change, which probably is a most tedious part of this process.

Good approach is to start working with smaller things first, building up your confidence in how the mod works and gradually approaching more complex stuff, here is a general algorithm:
   1. Begin with fixing moved/renamed things by deleting all bad imports and with help of the IDE re-import equivalents if present.
      Intelij IDEA has settings for unambiguous auto-import and import optimization on the fly, which can greatly speedup the process. Just pay attention to what is actually imported.
   2. Remove all nonworking code which is not easily fixable (e.g., class only introduced in newer forge) and provide stubs in its place.
       For example, replace reference to method of non existing class with your method in your class, it can have an empty body and mocked return so the code can compile and run without issues.
       Do not forget to track all things you've stubbed, if you are working on port alone - TODOs should be sufficient (most IDEs have a built in TODO browser).
   3. Build the project and attempt to run it
   4. If there were any critical errors which cause Minecraft to crash or mod to not work - try fixing them first, so you can test your changes
   5. Start fixing small things, ones that you think you have most chances to fix and work your way up
   6. If any there is any feature that is not worth it's time or you simply don't know how to do it - consider dropping it entirely and open an issue in your repository where you'll explain your findings and blockers.
       Maybe somebody with greater knowledge/more time/motivation will try to take bite at it.
9. Fix bugs you've introduced when porting.
    It is uncommon for mods to have lots of workarounds and hidden connections.
    You'll need to test things and check if they work as intended (gl;hf ;p)

### 5. Final words

If after reading this, you are not discouraged and still want to port it - good luck porting it! You'll definitively need it.
