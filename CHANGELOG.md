# Changelog

## 0.6.0

In this version, the entire internals of the library has been rewritten
to remove the requirement for the native library.

The new implementation is in plain Java.

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
  - `UserManager.getCurrentUserPremiumType` because I don't have Discord Nitro, so I cannot test it
