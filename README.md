# Numerical Methods

Небольшой веб-проект по численным методам: Scala backend считает методы, React frontend дает удобный интерфейс для ввода данных и просмотра результатов.

## Что внутри

Проект лежит в папке `numerical-methods` и состоит из двух частей:

- `backend` - Scala 3 + Akka HTTP API.
- `frontend` - React + Vite клиент.

Реализованные разделы:

- Лаб 1: решение СЛАУ методом простых итераций (Якоби).
- Лаб 2: нелинейные уравнения и системы.
- Лаб 3: численное интегрирование.
- Лаб 4: аппроксимация.
- Лаб 5: интерполяция.
- Лаб 6: численное решение ОДУ.

## Запуск

Backend:

```bash
cd numerical-methods/backend
sbt run
```

API поднимается на:

```text
http://localhost:8080/api/v1
```

Frontend:

```bash
cd numerical-methods/frontend
npm install
npm run dev
```

Приложение открывается на:

```text
http://localhost:5173
```

## Проверки

Backend-тесты:

```bash
cd numerical-methods/backend
sbt test
```

Сборка frontend:

```bash
cd numerical-methods/frontend
npm run build
```

## Структура

```text
numerical-methods/
  backend/
    src/main/scala/routes/      # HTTP routes
    src/main/scala/solver/      # реализации численных методов
    src/test/scala/solver/      # регрессионные тесты
  frontend/
    src/pages/                  # страницы лабораторных
    src/components/             # формы, графики, панели результатов
    src/api/                    # клиент API и адаптеры ответов
```

