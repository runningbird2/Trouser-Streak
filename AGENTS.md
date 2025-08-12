# Repository Guidelines (Agent Workflow)

## Direct Commit Policy
- Default workflow: commit directly to branches on the `origin` fork (e.g., `1.21.5`, feature/fix branches) and push. Do not open PRs by default.
- Upstream (`etianl/Trouser-Streak`): do not open PRs unless explicitly requested. Avoid cross-repo PRs to prevent confusion.
- If coordination is needed, open a PR only on the `origin` repository; never to upstream without confirmation.

## Branching & Pushing
- Update existing branches directly when appropriate (assume, `1.21.5`).
- For isolated work, create a feature branch on origin: `git checkout -b feat/<topic> <base>` and `git push -u origin feat/<topic>`.
- Commit with clear messages: `git add -A && git commit -m "<scope>: <summary>"`.

## Build & Verify
- Build: `./gradlew build -x test` (Java 21). Artifacts: `build/libs/*.jar`.
- Run dev client (optional): `./gradlew runClient`.

## Notes
- Keep changes minimal and focused. Update docs when behavior or workflows change.
- Never push sensitive info. Keep settings and mixins scoped and justified.

