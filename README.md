# Skazka Update

**RU:** Общий механизм обновления Android-приложений Skazka.

**EN:** Shared update flow for Skazka Android applications.

## Что здесь будет / What belongs here

- проверка доступной версии;
- разбор release metadata;
- передача загрузки в общий download engine;
- проверка целостности и подписи;
- передача APK системному установщику;
- состояние обновления и диагностика.

## Граница / Boundary

Signing keys, release credentials и production release orchestration сюда не попадают. Они остаются в закрытом Skazka Ops и серверном контуре.

## Статус / Status

Миграция началась. Рабочий код переносится небольшими проверяемыми шагами. Пока API не помечен как stable, совместимость между версиями не гарантируется.

Migration has started. Working code is being moved in small, verifiable steps. Until an API is marked stable, compatibility between versions is not guaranteed.
