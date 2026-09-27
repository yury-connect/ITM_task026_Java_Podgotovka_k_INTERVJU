# **AOS** (*AbstractOwnableSynchronizer*)

В Java аббревиатура **AOS** расшифровывается как **AbstractOwnableSynchronizer**.

Это базовый класс для синхронизаторов, который **хранит информацию о потоке, владеющем блокировкой** (в эксклюзивном режиме)[](https://developer.aliyun.com/article/1499657#1)[](https://developer.aliyun.com/article/1492740#1). Он является родительским классом для более известного **AQS** (AbstractQueuedSynchronizer)[](https://developer.aliyun.com/article/1499657#1)[](https://developer.aliyun.com/article/1492740#1).

Проще говоря:
	
- **AOS** — это просто хранилище для ссылки на текущего владельца блокировки.
    
- **AQS** — это полноценный фреймворк для создания блокировок и синхронизаторов (*на котором построены `ReentrantLock`, `Semaphore` и другие*)[](https://developer.aliyun.com/article/1281099#1)[](https://developer.aliyun.com/article/867884#1).    

Если на собеседовании вас поправят, сказав «AQS», а не «AOS», не пугайтесь — скорее всего, имелся в виду именно `AbstractQueuedSynchronizer`, так как `AbstractOwnableSynchronizer` используется гораздо реже и упоминается в основном в контексте наследования.

---
