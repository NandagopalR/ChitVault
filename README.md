# ChitVault

ChitVault is a Jetpack Compose Android starter app scaffolded with MVVM, Dagger Hilt, Room, and Firebase integrations.

## Included setup

- Android Gradle Plugin `9.1.0`
- Kotlin `2.2.20`
- Jetpack Compose UI with Material 3
- Dagger Hilt dependency injection
- Room local caching for person records
- Firebase Analytics, Crashlytics, Cloud Messaging, Storage, and Realtime Database dependencies

## Firebase data shape

The app currently expects Firebase Realtime Database nodes like this:

```json
{
  "users": {
    "uid-1": {
      "username": "John",
      "mail": "john@example.com",
      "age": 30,
      "isAmountCredited": false,
      "creditedDate": "",
      "creditedAmount": 5000.0
    }
  },
  "persons": {
    "person-1": {
      "username": "Asha",
      "mail": "asha@example.com",
      "age": 27,
      "isAmountCredited": true,
      "creditedDate": "2026-04-08",
      "creditedAmount": 12500.0
    }
  }
}
```

## Important notes

- Add your `google-services.json` file under `/Users/nandagopal/Documents/New project/app/google-services.json` before building.
- Firebase dependencies are wired, but the project cannot build successfully until that config file is present.
- `creditedAmount` was added to support the Home card requirement.
