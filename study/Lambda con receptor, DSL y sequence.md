# 🧠 Kotlin - Lambda con receptor, DSL, `sequence` y funciones de extensión

## 🔹 ¿Qué es una lambda con receptor?

Una **lambda con receptor** es una función donde puedes usar `this` de forma implícita para referirte a un objeto sobre el que se ejecuta la lambda.

```kotlin
val saludo: String.() -> Unit = {
    println(this.uppercase())
}

"hola".saludo() // Imprime: HOLA
```

Esto permite escribir bloques más limpios y expresivos, especialmente en builders o configuradores.

##  **🔹 ¿Qué es un DSL?**
Un DSL (Domain Specific Language) es una forma de escribir código personalizado con una sintaxis parecida a un "mini lenguaje", usando lambdas con receptor.

Ejemplo real con apply:

```kotlin
val usuario = Usuario().apply {
    nombre = "Luis"
    edad = 25
}
```

En este bloque, this es el objeto Usuario, y puedes escribir directamente nombre = ... sin repetir usuario.nombre = ....

## **🔹 Diferencias entre funciones estándar con lambdas**

|Función|Usa receptor (`this`)|Usa parámetro (`it`)|Devuelve|
|---|---|---|---|
|`let`|❌ No|✅ Sí|Resultado de la lambda|
|`apply`|✅ Sí|❌ No|El objeto (`this`)|
|`also`|❌ No|✅ Sí|El objeto (`it`)|
|`run`|✅ Sí|❌ No|Resultado de la lambda|
|`with`|✅ Sí (indirecto)|❌ No|Resultado de la lambda|


```kotlin
val usuario = Usuario().apply {
    nombre = "Luis"
    edad = 25
}
```
##  **🔹 ¿Qué hace la función sequence {}?**
sequence {} crea un objeto Sequence<T> que produce los valores uno por uno (evaluación lazy), usando la función yield() para entregar valores.


El bloque de sequence es una lambda con receptor de tipo suspend SequenceScope<T>.() -> Unit.

```kotlin
val seq = sequence {
    yield(1)
    yield(2)
    yield(3)
}

for (i in seq) {
    println("Valor: $i")
}
```

Esto imprime:

Valor: 1
Valor: 2
Valor: 3

Kotlin internamente:
1. Crea un SequenceScope<T>.
2. Ejecuta tu lambda dentro de ese scope.
3. Cada vez que haces yield(x), se entrega un valor al iterador.

## **🔹 Analogía con función DSL estilo Robot**

```kotlin
fun controlarRobot(bloque: Robot.() -> Unit) {
    val robot = Robot()
    robot.bloque()
}

class Robot {
    fun avanzar(pasos: Int) = println("Avanzo $pasos pasos")
    fun girar(grados: Int) = println("Giro $grados grados")
}

controlarRobot {
    avanzar(5)
    girar(90)
}
```
Esto se parece mucho a cómo funciona sequence {}.
En lugar de un Robot, se crea un SequenceScope, y tú llamas a funciones como yield() sobre él.

## **🔹 Diferencia entre lambda con receptor vs. función de extensión**
Ambas usan this, pero son conceptualmente diferentes.

✅ Función de extensión:
```kotlin
fun String.saludar() {
    println("Hola $this")
}


"Luis".saludar() // Llama directamente la función de extensión
```
Se define como parte del lenguaje (como si extendieras la clase).
Es fija y no se pasa como variable.

✅ Lambda con receptor:
```kotlin
val saludo: String.() -> Unit = {
    println("Hola $this")
}

"Luis".saludo() // Se puede guardar y pasar como variable
```

Se puede almacenar, pasar a funciones, combinar.
Es mucho más flexible para crear bloques DSL o configuradores.


✅ Conclusión
Las lambdas con receptor permiten escribir bloques con this implícito.

Los DSLs usan este tipo de lambdas para construir código más limpio (como apply, sequence, buildString).

sequence {} es una función que crea internamente un SequenceScope y ejecuta tu lambda dentro de él.

Las funciones de extensión agregan métodos a tipos existentes, pero no son lambdas ni se pueden pasar como variables.