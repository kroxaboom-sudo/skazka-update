# Skazka Update

> RU — основной язык · EN — required second language

## RU

Проверка версии, безопасное скачивание, верификация и передача APK системному установщику.

**Текущий статус:** репозиторий создан как целевая граница модуля. Рабочий код переносится из существующих проектов поэтапно, с тестами и без копирования project-specific зависимостей.

**Граница модуля:** client-side update flow; signing keys and release infrastructure remain private.

Перед первым стабильным релизом здесь появятся собственные versioning, тесты, changelog и лицензия. До выбора лицензии публикация кода не означает автоматическое разрешение на его повторное использование.

## EN

Version checks, safe download, verification, and handoff to the Android system installer.

**Current status:** this repository is the target module boundary. Working code is being extracted from existing projects incrementally, with tests and without copying project-specific dependencies.

**Module boundary:** client-side update flow; signing keys and release infrastructure remain private.

Before the first stable release, this repository will get its own versioning, tests, changelog, and license. Until a license is selected, publishing the source does not automatically grant reuse rights.

## Development rules / Правила разработки

See [DEVELOPMENT_RULES.md](DEVELOPMENT_RULES.md).
