---
sidebar_position: 2
sidebar_label: SDK Publishing
---

# SDK Publishing

Publishing the MTN MOMO API SDK is a crucial step in making it available for developers to integrate into their applications. We utilize the [Gradle Maven Publish Plugin](https://vanniktech.github.io/gradle-maven-publish-plugin/) to facilitate the publishing process. This plugin simplifies the configuration and management of publishing artifacts to Maven repositories, ensuring a smooth and efficient workflow.

---

## Publishing to Remote Repositories

We currently publish the SDK to two primary repositories:

1. **Sonatype OSS Repository**:
   - **Snapshots Repository**: This repository is used for publishing development versions of the SDK, allowing developers to test new features before they are officially released.
     - URL: [Snapshots](https://s01.oss.sonatype.org/content/repositories/snapshots/io/rekast/momo-api-sdk/)
   - **Releases Repository**: This repository is used for stable, production-ready versions of the SDK, ensuring that developers have access to reliable and tested versions.
     - URL: [Releases](https://s01.oss.sonatype.org/content/repositories/releases/io/rekast/momo-api-sdk/)

2. **Maven Central**:
   - This repository is the primary public repository for Java libraries. It is widely used and trusted by developers. Note that it does not accept `SNAPSHOT` versions, so ensure that you are publishing stable releases.
     - URL: [Maven Central](https://repo1.maven.org/maven2/io/rekast/momo-api-sdk/)

### Steps for Publishing to Remote Repositories

When publishing to remote repositories, remember to follow these steps:

1. **Update the SDK Version**:
   - Update the [version](https://github.com/re-kast/android-mtn-momo-api-sdk/blob/9d70fb3cf954b8626f0facb67d5e00d0652a9305/android/momo-api-sdk/gradle.properties#L4) of the SDK in the `gradle.properties` file. This ensures that the correct version is published.
   :::info
   ```properties
   VERSION_NAME=0.0.2-SNAPSHOT
   ```
   :::

2. **Select the Publishing Server**:
   - Choose the server to publish to by uncommenting either of the following [lines](https://github.com/re-kast/android-mtn-momo-api-sdk/blob/de966147f9a240b2bdf0611663a6fd5b05cf21ae/android/momo-api-sdk/gradle.properties#L24-L25):
   :::info
   ```properties
   #SONATYPE_HOST=S01
   #SONATYPE_HOST=CENTRAL_PORTAL
   ```
   :::

### Publishing Command

To publish and release the SDK to Maven Central, run the following command in your terminal:

```bash
./gradlew publishAndReleaseToMavenCentral --no-configuration-cache --stacktrace
```

- **`--no-configuration-cache`**: This flag disables the configuration cache, which can help avoid issues during the publishing process, especially if there are changes in the build configuration.
- **`--stacktrace`**: This flag provides a detailed stack trace in case of errors, which can be useful for debugging and identifying issues during the publishing process.

## Publishing Locally

For testing changes locally before publishing to remote repositories, you can publish the SDK artifact to your local Maven repository. This is particularly useful for verifying changes without affecting the remote repositories.

To publish an artifact locally, run the following command:

```bash
./gradlew publishToMavenLocal --no-configuration-cache
```

This command will place the published artifact in your local Maven repository, typically located at `~/.m2/repository/`, allowing you to test the SDK in your local projects.

## Additional Information

- **Versioning**: Ensure that you follow [semantic versioning](https://semver.org/) practices when publishing releases. This helps users understand the nature of changes in each version (e.g., major, minor, patch). For example, increment the major version for breaking changes, the minor version for new features, and the patch version for bug fixes.

- **Documentation**: It is essential to maintain up-to-date documentation for your SDK. Consider using tools like [Dokka](https://kotlinlang.org/docs/dokka/overview.html) to generate documentation from your Kotlin code. Well-documented SDKs improve usability and help developers integrate your library more effectively.

- **Changelog**: Maintain a changelog to document changes between versions. This can be a simple markdown file that outlines new features, bug fixes, and any breaking changes. This practice enhances transparency and helps users understand what to expect in each release.

## References

- [Gradle Maven Publish Plugin Documentation](https://vanniktech.github.io/gradle-maven-publish-plugin/)
- [Sonatype OSS Repository Hosting](https://central.sonatype.com/)
- [Maven Central Repository](https://repo1.maven.org/maven2/)
- [Semantic Versioning](https://semver.org/)
- [Dokka Documentation](https://kotlinlang.org/docs/dokka/overview.html)
- [Creating a Changelog](https://keepachangelog.com/en/1.0.0/)

By following these guidelines, you can effectively publish the MTN MOMO API SDK, ensuring that it is accessible and usable for developers looking to integrate mobile money services into their applications.