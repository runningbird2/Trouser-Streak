Updates based on clarifications:

- Backtrack semantics: backtrack limit now applies relative to the bot’s current forward progress. Each time the bot moves forward, the backtrack window resets to 65 chunks. Backtracking is measured in chunks (16 blocks) along the current heading projection, not by exact path replay.
- Diagonal gap bridging: gap allowance now supports straight-line diagonals in addition to cardinal rays. If no adjacent candidates are present, the pathfinder will attempt 8-way rays up to the configured gap to bridge breaks.
- History window: expanded and retained to reliably detect long backtracks and oscillations; still pruned by time window for efficiency.
- Disconnect behavior: if exploration of branches exhausts and/or the directional backtrack exceeds the limit, Baritone is cancelled and a clean disconnect is performed (configurable).
- AllExceptNew: follows all detected types except "new" (Old, BeingUpdated, OldGeneration, Block/BlockUpdate Exploit) per your guidance.

Notes:
- Backtrack detection uses an apex + heading projection; the apex resets on forward progress so the limit always reflects the last forward advance.
- The heading for backtrack is the chosen branch heading, falling back to trend heading from recent movement when needed.


Adds a HUD element: ChunkFollowStatsHud. Find it under Meteor HUD as 'chunk-follow-stats' and place/scale it as needed.