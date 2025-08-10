This PR improves the NewerNewChunks auto-follow feature.

Changes
- Pause-on-input: while movement/interaction keys are held, Meteor auto-follow pauses and Baritone is cancelled on press; resumes with a short debounce after release.
- Route recalculation: removes periodic 2s goal refresh to reduce churn; selects a new goal only when needed.
- Goal continuation: immediately selects the next target when reaching a goal to keep following the trail.
- Safety: avoids retargeting the player's current chunk; keeps short backtrack cooldown to reduce ping-pong.

Manual Test Steps
- Enable auto-follow in NewerNewChunks with chat logging on.
- Walk/press keys and confirm: Baritone cancels and auto-follow stays paused while keys are held; it resumes shortly after releasing.
- Follow a marked trail and confirm: on reaching each goal chunk, a new goal is selected and pathing continues without stopping.
- Confirm fewer spurious re-paths compared to previous behavior.

Build
- Verified with `./gradlew build -x test`.
