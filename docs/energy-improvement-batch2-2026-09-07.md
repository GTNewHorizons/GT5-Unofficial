# Energy improvements: linear cable walks, 2026-09-07

## Scope and decision

The first batch was committed as `bcc7b6b1c6` (`Optimize initial energy transfer work with focused regressions`). This batch implements consolidated-plan item 5 only: replace recursion along degree-two cable runs with an iterative walk. Live performance measurement was not made a prerequisite because this change addresses a directly reproducible stack-depth failure. No TPS/MSPT or CPU-time improvement is claimed.

The source baseline is the first-batch commit. The runtime/build setup remains Temurin 25.0.3, Gradle 9.4.0 and the repository's existing JUnit/Mockito/JVM Downgrader configuration. Supporting reports were backed up again before running Gradle; no clean task was used.

## Change and compatibility

Only `GenerateNodeMap.getNextValidTileEntity` changes in production. It carries the current tile and incoming direction into the next loop iteration instead of making a recursive call. It retains the original direction enumeration, reciprocal-connection check, visited-node check, pipe classification calls, ordered member accumulation and terminal tile/direction results.

The graph builder still chooses the same DFS tree and compresses the same runs. Branch recursion, router recursion, graph clearing, locks, losses, consumer selection and transfer accounting are unchanged. No API, persistent cache, alternate route selection, dynamo optimization or correctness fix was added.

## Baseline and validation

The existing focused energy tests and added stopping-condition tests were run against baseline production before editing the helper. The baseline **throws `StackOverflowError` on the 16,384-cable line**. That was a temporary expected-failure assertion during baseline work; the final test requires successful graph construction instead.

The new long-run fixture uses real `MTECable` and `BaseMetaPipeEntity` objects, with a mocked loaded world. It validates the compressed path's exact members and losses, endpoint/DFS IDs, assigned path references and clearing. A 64-cable control catches ordinary-layout changes. A separate bent-walk test verifies stopping at an external consumer, missing neighbor, branch, missing reciprocal connection and an already-visited node. Existing loop, branch, chunk-border, native receiver, IC2, re-entry and LSC tests remain in the suite.

All **247 unit tests passed**, including 17 focused energy cases. The baseline XML is retained locally in `build/energy-batch2-baseline-results/`; the table below is the durable result record.

| Controlled fixture | Recursive baseline | Iterative result |
|---|---|---|
| 64-cable line: walk stack depth at final neighbor lookup | 62 frames | 1 frame |
| 64-cable line: world tile lookups | 64 | 64 |
| 16,384-cable line | Stack overflow | Builds and clears successfully |
| 16,384-cable line: walk stack depth / world tile lookups | Did not complete | 1 frame / 16,384 lookups |
| Path members, aggregate loss, DFS endpoint and clear state | Short control passes | Both sizes pass |

These are production-code regression counts, not timing measurements. `gradlew.bat test spotlessCheck checkstyleMain checkstyleTest --console=plain` completed successfully: compilation, bytecode downgrading, all unit tests, formatting and both Checkstyle tasks passed. `git diff --check` passed. All eight supporting reports still match their protected backup. Final diff review confirms that only the linear walk changes in production; the remaining additions are tests and this handoff.

## Limits and remaining work

The linear walk remains O(n) in cable count and still performs the same world lookups. This patch removes stack growth with run length; it does not eliminate the compressed path's O(n) member storage or make the wider graph builder/router nonrecursive. Deep branching networks remain outside this patch.

Tests use a mocked loaded world, so they do not establish real chunk-load behavior or whole-server timing. The live benchmark steps recorded in the first-batch handoff remain outstanding, including ordinary active/blocked networks, topology churn, LSC workloads and the deferred AE2 counting scenario. Compare matching source-built artifacts and report allocation/CPU/MSPT distributions before claiming pack-wide gains.

Next: item 6 should begin with production-hatch boundary/callback equivalence tests and representative high-amp workload measurement before choosing a batching fast path. Continue item 7's consumer/protocol/lifecycle coverage alongside that work. No new separate correctness issue was identified during this bounded change.

This batch is left uncommitted for review; nothing was pushed.
