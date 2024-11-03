---
sidebar_position: 1
sidebar_label: Code Contributions
---

# Code Contributions 
---


## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the project
2. Create your branch (`git checkout -b AmazingFeature`). Read about branch naming [here](./code-standards/#branch-naming)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`). Read about commit messages [here](code-standards/#commit-messages)
4. Push to the branch (`git push origin AmazingFeature`)
5. Open a Pull Request

:::info

Remember to always update the SDK version [**here**](https://github.com/re-kast/android-mtn-momo-api-sdk/blob/a4f42bee8245c884586401ac210f46bac51f1953/android/momo-api-sdk/gradle.properties#L4) if you make changes on any of the SDK files. 
```properties
VERSION_NAME=0.0.2-SNAPSHOT
```

:::


## Code Reviews

Pull Requests (PRs) can only be merged if they meet the following criteria:
* **Approval from at least one reviewer:** Each PR **MUST** receive an approval from at least one designated reviewer before it can be merged.
* **Completion of all items in the PR template:** All checklist items or requirements in the PR template must be marked as completed to ensure readiness.
* **Passing status checks:** All required status checks, such as CI/CD pipelines or other automated checks, must pass successfully.
* **Up-to-date with the develop branch:** The PR branch **MUST** be updated to the latest version of the [**develop branch**](https://github.com/re-kast/android-mtn-momo-api-sdk/tree/develop) to avoid merge conflicts and ensure compatibility.

:::tip[Happy coding!]

We hope you enjoy updating and using this library!

:::