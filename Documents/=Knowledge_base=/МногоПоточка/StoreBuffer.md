# Store Buffer

**Buffer** — это аппаратный буфер в процессоре, который временно хранит операции записи (stores) до того, как они будут записаны в кэш или основную память[](https://dlnext.acm.org/doi/pdf/10.1145/3575757?download=true&__cf_chl_tk=Apy0squlvyoYTlbMEatMVOYtOCCJxSOBtLQKn5STGCM-1785257941-1.0.1.1-wmqQNxaUV0YuQmwQGBi6HDw9W7XvcGRjHYMGsF4ek0w#97#2)[](https://stackoverflow.com/questions/78546285/understanding-the-volatile-modifier-in-the-context-of-x86-architecture-and-the-j/79250566#79250566#4).

### 🔄 Зачем он нужен

Когда ядро выполняет инструкцию записи, оно не ждёт, пока данные дойдут до кэша L1. Оно кладёт их в store buffer и продолжает выполнять следующие инструкции. Это позволяет процессору не простаивать из-за задержек памяти и повышает производительность за счёт **переупорядочивания** операций[](https://stackoverflow.com/questions/78546285/understanding-the-volatile-modifier-in-the-context-of-x86-architecture-and-the-j/79250566#79250566#4)[](https://www.processon.com/editor/view/flow?fromnew=1&isTemp=true&template=true&chartId=5f94e72c7d9c0806f28cf24b&picture=true&isPhoneOrPc=pc&title=JMM%E5%86%85%E5%AD%98%E5%B1%8F%E9%9A%9C).

### ⚠️ Проблема: видимость и reordering

Store buffer — это **локальный** буфер конкретного ядра. Данные в нём ещё **не видны другим ядрам**, пока не будут сброшены (drained) в кэш[](https://stackoverflow.com/questions/78546285/understanding-the-volatile-modifier-in-the-context-of-x86-architecture-and-the-j/79250566#79250566#4). Это создаёт две проблемы:

**Задержка видимости (Visibility Delay):** Поток A записал значение в переменную, но она всё ещё лежит в store buffer. Поток B на другом ядре читает старую копию из своего кэша и не видит изменения[](https://www.processon.com/editor/view/flow?fromnew=1&isTemp=true&template=true&chartId=5f94e72c7d9c0806f28cf24b&picture=true&isPhoneOrPc=pc&title=JMM%E5%86%85%E5%AD%98%E5%B1%8F%E9%9A%9C).

**Переупорядочивание (Reordering):** Процессор может выполнить записи в store buffer не в том порядке, в котором они были в коде. Это нарушает ожидаемый порядок операций[](https://download.microsoft.com/download/a/3/1/a315bac2-8093-45fd-8d04-1a9f899aca53/mdn_0113dg.pdf#18#11)[](https://lkml.org/lkml/2023/8/15/567#9).

### 🛡️ Как с этим борются

**Барьеры памяти (Memory Barriers):** Инструкции вроде `smp_mb()` на x86 или `MemoryBarrier()` в Windows заставляют процессор **сбросить store buffer** и дождаться завершения записи, прежде чем продолжить[](https://learn.microsoft.com/ru-ru/windows/win32/api/winnt/nf-winnt-memorybarrier#1)[](https://kernel.org/pub/linux/kernel/people/paulmck/perfbook/perfbook-eb.2025.12.18a.pdf#150#102). Это гарантирует, что данные станут видимы другим ядрам в правильном порядке.

**`volatile` в Java:** Когда ты пишешь `volatile`-переменную, JVM генерирует инструкцию, которая заставляет процессор сбросить store buffer. Это не «отключает кэш», а заставляет запись **немедленно стать видимой** другим потокам[](https://stackoverflow.com/questions/78546285/understanding-the-volatile-modifier-in-the-context-of-x86-architecture-and-the-j/79250566#79250566#4).

### 🎤 Для собеседования

> _«Store Buffer — это аппаратный буфер, куда процессор складывает операции записи, чтобы не ждать их завершения. Он повышает производительность, но создаёт проблему: данные в буфере ещё не видны другим ядрам. Из-за этого возможны задержки видимости и переупорядочивание. Барьеры памяти (и `volatile` в Java) заставляют процессор сбросить store buffer и гарантировать, что запись стала видимой.»_

---
