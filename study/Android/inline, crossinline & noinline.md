Documentation related these 3 keywords:
```kts
inline
crossinline
noinline
```

Normal function:
```kts
fun main() {
	val list = (1..100).toList()
	list.nomralForEach {
		println(it)
	}
}

fun <T> List<T>.normalForEach(action: (T) -> Unit) {
	for(item in this) {
		action(item)
	}
}
```

Result:
1
2
3
.
.
.
100
