# Changelog

## 1.0.0

In this version, the entire internals of the library have been rewritten
to remove the requirement for a native library.

The new implementation is in plain Java and interacts directly with Discord's IPC socket.

**Some of the removed features might be re-added in a future version.**

### Breaking Changes
- Raised minimum Java version to Java 16
- `Core.init` was removed, because it is no longer required
- Removed Managers due to deprecation:
  - `LobbyManager`
  - `NetworkManager`
- Some functions are current not implemented, these throw a `new RuntimeException("not implemented")`:
  - `ActivityManager.registerCommand` because it does not communicate with Discord
  - `ActivityManager.registerSteam` because it does not communicate with Discord
  - `AcitvityManager.acceptRequest` because I cannot figure out how it works
  - `UserManager.getCurrentUserPremiumType` because I don't have Discord Nitro, so I cannot figure out how it works
- `ActivityTimestamps.setStart` not longer resets the end timestamp, use `ActivityTimestamps.clearEnd` for that
- `ActivityTimestamps.setEnd` not longer resets the start timestamp, use `ActivityTimestamps.clearStart` for that

### Big Changes
- Complete rewrite of the internals (see above)
- Support for custom buttons in an activity
  (see `Activity.setActivityButtonsMode`, `Activity.addButton`, and `Activity.removeButton`)
- Support for "progress"-like activities featuring both start and end time
  (see `ActivityTimestamps.setStartAndEnd`)

### Thank You's and Acknowledgements
This big release would not have been possible without the help of various people and I am incredibly thankful for their
contributions to this library!

In rough historical order:
- [@Kevin-OVI](https://github.com/Kevin-OVI) for fixing the DownloadNativeLibrary example before the rewrite
- [@letorbi](https://github.com/letorbi) for massive work towards the rewrite, in particular the Windows support and tons of clean-up and improvements
- [@gravit0](https://github.com/gravit0) for custom button support
- [@Desoroxxx](https://github.com/Desoroxxx) for clean-up and documentation
- [@immails](https://github.com/immails) for Windows-specific fixes
- [@RedTeaDev](https://github.com/RedTeaDev) for implementing the `ActivityJoinEvent`
- [@isabelcoolaf](https://github.com/isabelcoolaf) for "progress" activities and updating part of the library to reflect latest changes by Discord
