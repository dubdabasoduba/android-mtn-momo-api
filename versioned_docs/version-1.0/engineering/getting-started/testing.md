---
sidebar_position: 3
sidebar_label: Code Testing
---

# Code Testing

The MTM Momo Android SDK is designed with a robust testing framework that includes automated style checks, code coverage assessments, unit tests, and user interface/integration tests. These tests are crucial for maintaining code quality and ensuring that new changes do not introduce bugs. All tests are executed through GitHub Actions whenever a pull request is submitted. It is essential that all tests pass successfully for a pull request to be merged without requiring an admin override, thereby maintaining the integrity of the codebase.

---

## Style and Coverage Tests

To maintain a high standard of code quality, we utilize [ktlint](https://github.com/pinterest/ktlint) in combination with [spotless](https://github.com/diffplug/spotless). This setup allows us to enforce strict style checks across the entire codebase. By making these style checks as rigorous as possible, we aim to reduce the amount of bikeshedding—unproductive discussions about code style—during code reviews. This helps developers focus on the functionality and logic of the code rather than its appearance.

For assessing code coverage, we employ [Jacoco](https://www.jacoco.org/jacoco/) to generate detailed coverage reports. These reports are then tracked over time using [Codecov](https://codecov.io), which provides insights into how much of the codebase is being tested. We enforce a minimum coverage percentage for any new code added in a pull request, as well as a minimum reduction in overall coverage percentage change. This ensures that new contributions do not decrease the overall test coverage of the project, thereby maintaining a high standard of reliability.

## Unit Tests

Unit tests are a critical component of our testing strategy, as they allow us to verify the functionality of individual components in isolation. These tests are organized within two main modules: the [momo-api-sdk](https://github.com/re-kast/android-mtn-momo-api-sdk/tree/develop/android/momo-api-sdk/src/test/) and the [sample](https://github.com/re-kast/android-mtn-momo-api-sdk/tree/develop/android/sample/src/test) modules. Developers can run these tests locally to validate their changes before submitting a pull request. Additionally, these unit tests are automatically executed through GitHub Actions whenever a pull request is submitted. It is imperative that all unit tests pass successfully for a pull request to be merged, ensuring that new changes do not break existing functionality.

## User Interface and Integration Tests

In addition to unit tests, we also conduct user interface and integration tests to ensure that the application behaves as expected from the user's perspective. These tests are performed against screen renderings and are defined within the [sample](https://github.com/re-kast/android-mtn-momo-api-sdk/tree/develop/android/momo-api-sdk/src/androidTest) module. Similar to unit tests, these user interface tests can be executed locally and are automatically run through GitHub Actions upon the submission of a pull request. All tests must pass for a pull request to be merged, reinforcing the importance of maintaining a seamless user experience.

Furthermore, we conduct manual tests to cover all functionalities and to validate end-to-end (E2E) user journeys. This includes testing all steps a user may interact with, ensuring that the application performs as intended in real-world scenarios. Manual testing complements our automated testing efforts by providing a thorough examination of the user experience and identifying any potential issues that automated tests may not catch.

:::danger
**Important Reminder**: Pull requests that do not include tests will be subject to change requests during the review process. It is essential to ensure that all new features and bug fixes are accompanied by appropriate tests to maintain the integrity and reliability of the codebase.
:::

By adhering to these testing practices, we strive to deliver a high-quality product that meets the needs of our users while minimizing the risk of introducing bugs or regressions.
