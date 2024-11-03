# MTN MOMO API SDK for Android

[![Create staging repository](https://github.com/dubdabasoduba/android-mtn-momo-api/actions/workflows/main.yml/badge.svg?branch=develop)](https://github.com/dubdabasoduba/android-mtn-momo-api/actions/workflows/main.yml)

## Overview

The MTN MOMO API SDK is an Android library designed to simplify the integration of MTN's Mobile Money (MOMO) services into native Android applications. This SDK provides developers with a seamless way to interact with MTN's mobile payment infrastructure, enabling features such as user authentication, balance inquiries, and transaction processing.

## Key Features

- Easy integration with MTN MOMO API
- Support for various MOMO operations:
  - User information retrieval
  - Account balance checks
  - Payment requests and processing
  - Disbursements and refunds
- Built with modern Android development practices:
  - Kotlin language
  - Coroutines for asynchronous operations
  - Hilt for dependency injection
  - Jetpack Compose for UI (in sample app)
- Comprehensive error handling and logging
- Secure API communication with proper authentication

## Getting Started

### Prerequisites

- Android Studio Arctic Fox or later
- Kotlin 1.9.10 or later
- Minimum SDK version 24
- Compile SDK version 35

### Installation

Add the following to your project's `build.gradle.kts` file:

```
dependencies {
    implementation("io.rekast:momo-api-sdk:0.0.2-SNAPSHOT")
}
```

### Sample `local.properties`

To configure your local environment, create a `local.properties` file in the root of your project with the following content:

```properties
# Local properties for the MTN MOMO API SDK

MOMO_BASE_URL="" ## use https://sandbox.momodeveloper.mtn.com for sandbox and https://momodeveloper.mtn.com for production
MOMO_COLLECTION_PRIMARY_KEY="" ## The collection endpoint/product subscription primary key
MOMO_COLLECTION_SECONDARY_KEY="" ## The collection endpoint/product subscription secondary key
MOMO_REMITTANCE_PRIMARY_KEY="" ## The remittance endpoint/product subscription primary key
MOMO_REMITTANCE_SECONDARY_KEY="" ## The remittance endpoint/product subscription secondary key
MOMO_DISBURSEMENTS_PRIMARY_KEY="" ## The disbursements endpoint/product subscription primary key
MOMO_DISBURSEMENTS_SECONDARY_KEY="" ## The disbursements endpoint/product subscription secondary key
MOMO_API_USER_ID="" ## The sandbox API user ID you. You can use a [UUID generator](https://www.uuidgenerator.net/version4) to create one
MOMO_ENVIRONMENT="" ## API environment, use sandbox for testing and production for production
MOMO_API_VERSION_V1="" ## The API version for v1 endpoints, use v1_0 for sandbox and v1 for production
MOMO_API_VERSION_V2="" ## The API version for v2 endpoints, use v2_0 for sandbox and v2 for production
```

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request. For major changes, please open an issue first to discuss what you would like to change.

1. Fork the project
2. Create your feature branch (`git checkout -b AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin AmazingFeature`)
5. Open a Pull Request

## License

This project is licensed under the Apache License, Version 2.0. See the [LICENSE](LICENSE) file for details.

## Contact

Benjamin Mwalimu Mulyungi - [GitHub](https://github.com/dubdabasoduba)

Project Link: [https://github.com/re-kast/android-mtn-momo-api-sdk](https://github.com/re-kast/android-mtn-momo-api-sdk)

## Acknowledgments

- MTN for providing the MOMO API infrastructure
- The Android development community for various open-source libraries used in this project
- Contributors who have helped improve and maintain this SDK
