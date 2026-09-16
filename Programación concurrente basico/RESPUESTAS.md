Preguntas para pensar — Respuestas

Ejercicio 1 — Hola Hilo

Pregunta:¿Qué sucede si llamas a `hilo.run()` en lugar de `hilo.start()`? ¿Cuál es la diferencia?

Respuesta:`start()` le pide a la JVM que cree un hilo de ejecución nuevo (con su propia pila de llamadas) y, cuando ese hilo arranca, invoca automáticamente `run()` dentro de él; por eso el código corre de forma concurrente con el resto del programa. `run()` llamado directamente es solo un método normal: no crea ningún hilo nuevo, se ejecuta de manera síncrona dentro del hilo que hizo la llamada (por ejemplo, `main`). El texto impreso puede verse igual, pero no hay concurrencia real. Además, `start()` solo puede llamarse una vez por hilo (una segunda llamada lanza `IllegalThreadStateException`), mientras que `run()` se puede invocar las veces que se quiera, como cualquier método.


Ejercicio 2 — Hilos con Runnable

Pregunta:¿Cuál es la principal ventaja de usar `Runnable` en lugar de extender `Thread`?

Respuesta:Java no permite herencia múltiple de clases. Si una clase ya extiende `Thread`, no puede extender ninguna otra. Al implementar `Runnable` (una interfaz), la clase sigue libre para extender otra clase y puede implementar varias interfaces a la vez. Además, separa la "tarea a ejecutar" (`Runnable`) del "mecanismo de ejecución" (`Thread`), lo cual es más limpio en el diseño; un mismo `Runnable` puede reutilizarse en varios hilos o enviarse a un `ExecutorService` (pool de hilos), algo que no se logra tan naturalmente extendiendo `Thread` directamente.


Ejercicio 3 — Múltiples Hilos Simples

Pregunta:¿El orden de las salidas siempre es el mismo? ¿Por qué sí o por qué no?

Respuesta:No, el orden no es siempre el mismo. El sistema operativo decide cómo repartir el tiempo de CPU entre los hilos (planificación o *scheduling*), y ese reparto depende de factores como la carga del sistema y el número de núcleos disponibles. Aunque los tres hilos llaman a `Thread.sleep(100)` con el mismo valor, el tiempo real en que cada uno "despierta" varía ligeramente, por lo que pueden intercalarse de forma distinta en cada ejecución. `start()` solo garantiza que el hilo se ejecutará en algún momento, no en qué orden respecto a los demás.


Ejercicio 4 — Compartiendo Datos y `synchronized`

Pregunta:¿Por qué `synchronized` resuelve el problema? ¿Qué es lo que bloquea exactamente?

Respuesta:El problema (condición de carrera) ocurre porque `valor++` no es una operación atómica: internamente son tres pasos (leer, sumar 1, escribir). Si dos hilos entrelazan esos pasos, pueden leer el mismo valor "viejo" antes de que el otro termine de escribir, y se pierde un incremento. `synchronized` hace que el método adquiera el monitor (lock intrínseco) del objeto sobre el que se invoca; un monitor solo puede estar en posesión de un hilo a la vez. Lo que se bloquea exactamente es el acceso concurrente a los métodos sincronizados de esa misma instancia: mientras un hilo está dentro, cualquier otro que intente entrar a un método sincronizado del mismo objeto queda bloqueado hasta que el primero libere el lock. Esto vuelve el incremento efectivamente atómico entre hilos. `synchronized` no bloquea la variable en sí ni otros objetos `Contador`, solo el acceso a las secciones sincronizadas de ese objeto en particular.


Ejercicio 5 — Productor-Consumidor Simple

Pregunta:¿Por qué es importante el bucle `while` en `wait()` en lugar de un `if`?

Respuesta:Cuando un hilo despierta de `wait()`, la especificación de la JVM no garantiza que la condición esperada siga siendo verdadera: puede haber "despertares espurios" o varios hilos notificados a la vez con `notifyAll()` cuando solo uno debería actuar. Por ejemplo, si hay dos consumidores y se hace `notifyAll()`, ambos se despiertan, pero solo uno debería consumir el dato disponible; si se usara `if`, el segundo pasaría igual y consumiría un dato inválido o repetido. Con `while`, cada hilo que despierta vuelve a evaluar la condición antes de continuar: si ya no se cumple, vuelve a llamar a `wait()` en lugar de avanzar incorrectamente. Esto hace el código seguro frente a notificaciones de más y despertares espurios.


Ejercicio 6 — Usando Executors y Future

Pregunta:¿Cuál es la ventaja de usar `ExecutorService` sobre la creación manual de hilos con `new Thread()`?

Respuesta:`ExecutorService` reutiliza un pool de hilos ya creados para ejecutar muchas tareas, evitando el costo de crear y destruir un hilo por cada tarea. Permite limitar cuántos hilos corren en paralelo (por ejemplo, `newFixedThreadPool(3)`), evitando sobrecargar el sistema. Con `Callable<T>` y `Future<T>`, cada tarea puede devolver un resultado (o lanzar una excepción) que se recupera fácilmente con `future.get()`, algo incómodo de lograr con `Runnable` + `Thread` puro. Además ofrece métodos para apagar el pool de forma ordenada (`shutdown()`, `shutdownNow()`, `awaitTermination()`) y separa la lógica de la tarea de la estrategia de ejecución.


Ejercicio 7 — CountDownLatch para Sincronización

Pregunta:¿En qué escenarios `CountDownLatch` sería más útil que `Thread.join()`?

Respuesta:Es más útil cuando no se tienen referencias directas a los hilos (por ejemplo, tareas enviadas a un `ExecutorService`), ya que cualquier tarea puede señalar su finalización llamando a `countDown()` sin que el hilo que espera necesite una referencia a cada una. También sirve para esperar eventos o fases (no necesariamente la muerte completa de un hilo), y para sincronizar el arranque de varios hilos a la vez desde un punto común (patrón "puerta de salida"), algo que `join()` no puede hacer. Además, `latch.await(long, TimeUnit)` permite esperar con un único timeout para el conjunto completo de tareas, en vez de aplicar un timeout hilo por hilo como con `join(long)`. Nota: a diferencia de `join()`, un `CountDownLatch` es de un solo uso (no se puede resetear el contador); para escenarios repetibles se usaría `CyclicBarrier`.
