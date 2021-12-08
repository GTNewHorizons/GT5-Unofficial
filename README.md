GT5-Unofficial
===

# detect-conflicts branch

This branch contains a patch to log all gregtech caught recipe duplications. Unlike NEI Custom Diagram's recipe debugger, 
this catch all and every duplication and logs them into a CSV. 

## How to use

On the latest branch, run this command to get the patch on your latest

```bash
git cherrypick 2777b95fe6e133809f721dafdde3a99556248f6a
```

Resolve merge conflicts, if any. Run `gradlew build` as usual, and use the resulting jar to run the full pack.

## Why not just add this into the main branch?

Because the code quality is TERRIBLE, and it adds considerable overhead to the code base. I made this while thinking this would be a throwaway code.  

Merging this back to master would be a disaster, plus I do not want to take on the burden of maintaining this mess.

## CSV format

This is a LOOOOOOOONG line. The first line is a header

### Too many empty fields!

Tweak RecipeDebugConstants.java. Those int constants control how many stacks to print. Be aware, if the recipe has more stacks than you specfied, it will be silently ignored! 

## Precautions

1. This is slow!
2. Make sure your minecraft directory's disk has at least 500MB disk space left.
