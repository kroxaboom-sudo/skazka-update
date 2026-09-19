# Skazka Update

> RU — основной язык · EN — required second language

## RU

Переиспользуемый контур безопасного Android-обновления, вынесенный из Skazka Hub без UI, расписания и production release endpoint.

**Статус:** `0.1.0-preview`.

- `update-core` — проверка update manifest, политика одного доверенного GitHub Releases репозитория, разрешённые transfer-hosts и SHA-256.
- `update-android` — проверка APK по размеру/hash/package/version/minSdk/signing identity и установка через системный PackageInstaller.
- Пользовательское подтверждение установки обязательно.
- Уведомления, фоновые расписания и конкретный feed остаются на уровне приложения.

Проверено на HOSTKEY: core self-test — PASS; `:update-android:assembleDebug` — PASS; `:update-android:lintDebug` — PASS.

## EN

Reusable secure Android update building blocks extracted from Skazka Hub without app UI, scheduling, or a production release endpoint.

**Status:** `0.1.0-preview`.

- `update-core` — update-manifest validation, one trusted GitHub Releases repository policy, allowed transfer hosts, and SHA-256.
- `update-android` — APK size/hash/package/version/minSdk/signing-identity verification and system PackageInstaller handoff.
- User confirmation is mandatory for installation.
- Notifications, background scheduling, and the concrete update feed stay at the app layer.

Verified on HOSTKEY: core self-test — PASS; `:update-android:assembleDebug` — PASS; `:update-android:lintDebug` — PASS.

## Coordinates / Координаты

- `com.kroxaboom.skazka:update-core:0.1.0-preview`
- `com.kroxaboom.skazka:update-android:0.1.0-preview`

## Security boundary / Граница безопасности

Signing keys, release credentials, private endpoints, server-side release tooling, and production configuration are not part of this repository.

See [DEVELOPMENT_RULES.md](DEVELOPMENT_RULES.md).

> A license will be selected before the first stable public release. Until then, publication of the source does not grant reuse rights.
