import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

inline val <T> List<T>.lastItem: T
    get() = get(lastIndex)

@JvmInline
value class Month(
    val number: Int,
)

fun main() {
    val list = (1..100).toList()
    list.inlineForEach {
        println(it)
        return // Regresa a la funcion main
    }
    list.normalForEach {
        println(it)
    }
    "Hello world".printClassName()
}

fun <T> List<T>.normalForEach(action: (T) -> Unit) {
    for (item in this) {
        action(item)
    }
}

inline fun <T> List<T>.inlineForEach(action: (T) -> Unit) {
    for (item in this) {
        action(item)
    }
}

inline fun <reified T> T.printClassName() {
    println(T::class.simpleName)
}

inline fun executeAsync(crossinline action: () -> Unit) {
    CoroutineScope(Dispatchers.Default).launch {
        action()
    }
}
