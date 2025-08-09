# Agents Guide: PR Creation Fix

This project uses GitHub CLI for opening pull requests. If `gh pr create` fails with errors like:

- "Head sha can't be blank"
- "Base sha can't be blank"
- "No commits between <base> and <head>"
- "Head ref must be a branch"

use the following checklist.

## Quick Checklist

- Create your branch from the correct base and push it:
  - `git checkout -b <your-branch> 1.21.5`
  - Make changes and commit
  - `git push -u origin <your-branch>`

- Verify there are commits between base and head on the remote:
  - `git fetch origin --prune`
  - `git log --oneline origin/1.21.5..origin/<your-branch>`
    - If this shows nothing, you haven’t pushed or you branched from the wrong base.

- Create the PR with explicit repo/base/head to avoid auto-detection issues:
  - `gh pr create --repo <your-user>/<repo> --base 1.21.5 --head <your-branch> \
      --title "<title>" --body "<body>"`

## Notes

- Ensure `origin` points to your fork and `upstream` to the main repo:
  - `git remote -v`
  - If needed: `git remote set-url origin https://github.com/<your-user>/<repo>`

- If you still get errors, confirm both refs exist on GitHub:
  - `git ls-remote --heads origin 1.21.5 <your-branch>` (should list both)

- For multi-remote setups or when working in a fork, specifying `--repo` helps the CLI pick the correct repository context.

