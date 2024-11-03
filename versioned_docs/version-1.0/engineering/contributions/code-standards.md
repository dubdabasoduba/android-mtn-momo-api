---
sidebar_position: 1
sidebar_label: Code Standards
---

# Code Standards
---

## Naming Conventions

- When using abbreviations in CamelCase, keep the first letter capitalized and lowercase the remaining letters. For example
    - A "MTN Momo" variable name must be written as `mtnMomo`
    - An "Account Balance" class name must be written as `accountBalance`

### Branch Naming

Create a new branch using the following naming convention:

```vim
issue-number-feature-name
```

For example:

```vim
27-adding-documnetation
```

## Commits

### Atomic commits
We encourage our users to create atomic PRs and commits because they focus on a single, specific change, making the codebase easier to understand, maintain, and troubleshoot. Here are a few reasons why atomic commits are valuable:
* **Clarity and Readability:** Each commit represents a focused change, making it clear what was modified and why. This improves code readability and makes it easier for reviewers to understand the purpose of each change.
* **Easier Debugging:** If issues arise, atomic commits allow for pinpointing exactly when and where a bug was introduced. This is particularly useful in debugging and when using tools like git bisect to trace problematic changes.
* **Simplifies Code Reviews:** Smaller, focused changes make it easier for reviewers to assess the impact of each commit without being overwhelmed by unrelated modifications. This leads to more effective and efficient reviews.
* **Better History Tracking:** Atomic commits create a clean, concise commit history, making it easier to follow the project’s progression over time and understand the rationale behind each change.
* **Facilitates Rollbacks:** In cases where a change needs to be reverted, atomic commits make it possible to roll back individual changes without affecting other, unrelated parts of the code.

:::danger

Non-atomic commits may be subject to change requests.

:::

### Commit Messages
Here are some guidelines when writing a commit message:

1. Separate subject/title from body with a blank line
2. Limit the subject line to 50 characters
3. Capitalize the subject line/Title
4. Do not end the subject line with a period
5. Use hyphens at the beginning of the commit messages in the body
6. Use the imperative mood in the commit messages
7. Wrap the body at 72 characters
8. Use the body to explain what and why, not how

To dive deeper into these guidelines, please view [this article by Chris Beams](https://cbea.ms/git-commit/).

**Sample commit message:**
```vim
Update Library documentation    

- Add and enable Docusaurus
- Add and enable Kdocs
```

## Pull Requests
### Describe Your Pull Request
1. Use the format specified in pull request template for the repository. Populate the stencil completely for maximum verbosity.
   * Tag the actual issue number by replacing #[issue_number] e.g. #42. This closes the issue when your PR is merged.
   * Tag the actual issue author by replacing @[author] e.g. @issue_author. This brings the reporter of the issue into the conversation.
   * Mark the tasks off your checklist by adding an x in the [ ] e.g. [x]. This checks off the boxes in your to-do list. The more boxes you check, the better.
2. Describe your change in detail. Too much detail is better than too little.
3. Describe how you tested your change.
4. Check the Preview tab to make sure the Markdown is correctly rendered and that all tags and references are linked. If not, go back and edit the Markdown.

To dive deeper into these guidelines, please view [Pull Request Guidelines](https://opensource.creativecommons.org/contributing-code/pr-guidelines/).

### Request Review
1. Once your PR is ready, remove the "[WIP]" from the title and/or change it from a draft PR to a regular PR.
2. If a specific reviewer is not assigned automatically, please [request a review](https://help.github.com/en/articles/requesting-a-pull-request-review) from the project maintainer and any other interested parties manually.

### Incorporating feedback
1. If your PR gets a 'Changes requested' review, you will need to address the feedback and update your PR by pushing to the same branch. You don't need to close the PR and open a new one.
2. Be sure to re-request review once you have made changes after a code review.
3. Asking for a re-review makes it clear that you addressed the changes that were requested and that it's waiting on the maintainers instead of the other way round.

:::danger

Non-atomic PRs may be subject to change requests.

:::