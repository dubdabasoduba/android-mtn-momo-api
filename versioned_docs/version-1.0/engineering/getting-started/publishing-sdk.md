---
sidebar_position: 2
sidebar_label: SDK Publishing
---

# SDK Publishing

Publishing the MTN MOMO API SDK is a crucial step in making it available for developers to integrate into their applications. We utilize the [Gradle Maven Publish Plugin](https://vanniktech.github.io/gradle-maven-publish-plugin/) to facilitate the publishing process. This plugin simplifies the configuration and management of publishing artifacts to Maven repositories.

---

## Publishing to Remote Repositories

We currently publish the SDK to two primary repositories:

1. **Sonatype OSS Repository**:
   - **Snapshots Repository**: This repository is used for publishing development versions of the SDK.
     - URL: [SNAPSHOTS](https://s01.oss.sonatype.org/content/repositories/snapshots/io/rekast/momo-api-sdk/)
   - **Releases Repository**: This repository is used for stable, production-ready versions of the SDK.
     - URL: [Releases](https://s01.oss.sonatype.org/content/repositories/releases/io/rekast/momo-api-sdk/)

2. **Maven Central**:
   - This repository is the primary public repository for Java libraries. Note that it does not accept `SNAPSHOT` versions.
     - URL: [Maven Central](https://repo1.maven.org/maven2/io/rekast/momo-api-sdk/)

Remember to do the follwoing when publishing to remote repositiories
1. Update the [version](https://github.com/re-kast/android-mtn-momo-api-sdk/blob/9d70fb3cf954b8626f0facb67d5e00d0652a9305/android/momo-api-sdk/gradle.properties#L4) of the SDK in the `gradle.properties` file when publishing to remote repositories.
2. Select the servers to publish to by uncommenting either of the following lines 

### Publishing Command

To publish and release the SDK to Maven Central, run the following command in your terminal:

```bash
./gradlew publishAndReleaseToMavenCentral --no-configuration-cache --stacktrace
```

- **`--no-configuration-cache`**: This flag disables the configuration cache, which can help avoid issues during the publishing process.
- **`--stacktrace`**: This flag provides a detailed stack trace in case of errors, which can be useful for debugging.

## Publishing Locally

For testing changes locally before publishing to remote repositories, you can publish the SDK artifact to your local Maven repository. This is particularly useful for verifying changes without affecting the remote repositories.

To publish an artifact locally, run the following command:

```bash
./gradlew publishToMavenLocal --no-configuration-cache
```

This command will place the published artifact in your local Maven repository, typically located at `~/.m2/repository/`, allowing you to test the SDK in your local projects.

## Additional Information

- **Versioning**: Ensure that you follow semantic versioning practices when publishing releases. This helps users understand the nature of changes in each version (e.g., major, minor, patch).
- **Documentation**: It is essential to maintain up-to-date documentation for your SDK. Consider using tools like [Dokka](https://kotlinlang.org/docs/dokka/overview.html) to generate documentation from your Kotlin code.

## References

- [Gradle Maven Publish Plugin Documentation](https://vanniktech.github.io/gradle-maven-publish-plugin/)
- [Sonatype OSS Repository Hosting](https://central.sonatype.com/)
- [Maven Central Repository](https://repo1.maven.org/maven2/)
- [Semantic Versioning](https://semver.org/)
- [Dokka Documentation](https://kotlinlang.org/docs/dokka/overview.html)

By following these guidelines, you can effectively publish the MTN MOMO API SDK, ensuring that it is accessible and usable for developers looking to integrate mobile money services into their applications.