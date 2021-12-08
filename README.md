GT5-Unofficial
===

# detect-conflicts branch

This branch contains a patch to log all gregtech caught recipe duplications. Unlike NEI Custom Diagram's recipe debugger, 
this catch all and every duplication and logs them into a CSV. 

## How to use

On the latest branch, run these

```bash
git cherrypick 2777b95fe6e133809f721dafdde3a99556248f6a
```

Resolve merge conflicts, if any. Run `gradlew build` as usual, and use the resulting jar to run the full pack.

## Why not just add this into the main branch?

Because the code quality is TERRIBLE, and it adds considerable overhead to the code base. I made this while thinking this would be a throwaway code.  

Merging this back to master would be a disaster, plus I do not want to take on the burden of maintaining this mess.

### CSV format

This is a LOOOOOOOONG line. 
```
RecipeMapIdentifier, EU/t, duration, special value, 
new recipe input stack #A name, new recipe input stack #A meta, new recipe input stack #A size, new recipe input fluid #B name, new recipe input fluid #B amoumnt, new recipe output stack #C name, new recipe output stack #C meta, new recipe output stack #C size, new recipe output fluid #D name, new recipe output fluid #D amoumnt,
old recipe input stack #A name, old recipe input stack #A meta, old recipe input stack #A size, old recipe input fluid #B name, old recipe input fluid #B amoumnt, old recipe output stack #C name, old recipe output stack #C meta, old recipe output stack #C size, old recipe output fluid #D name, old recipe output fluid #D amoumnt,
new recipe adder stack trace with # as delimiter
old recipe adder stack trace with # as delimiter
```

### Precautions

1. This is slow
2. Make sure your minecraft directory's disk has at least 500MB disk space left.
