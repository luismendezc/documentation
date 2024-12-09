https://medium.com/@ramadan123sayed/understanding-collections-in-kotlin-mutable-immutable-arrays-lists-maps-and-sets-d441b851b7f7

# Immutable Collections

Immutable collections cannot be modified after their creation. They are thread-safe and can be shared across different parts of your application without worrying about accidental modifications. Immutable collections in Kotlin include:

- `List`
- `Set`
- `Array`
- `Map`

```kotlin
val fruits: List<String> = listOf("Apple", "Banana", "Cherry")  
val strings = arrayOf("Kotlin", "Android", "Development")  
val numbers: Set<Int> = setOf(1, 2, 3, 4, 5)  
val readOnlyMap: Map<String, String> = mapOf("key1" to "value1", "key2" to "value2")
```

# Mutable Collections

Mutable collections can be modified after their creation. They are flexible and allow for modifications, making them ideal for scenarios where the data set needs to change over time. Mutable collections in Kotlin include:

- `MutableList`
- `MutableSet`
- `MutableMap`

```kotlin
val mutableFruits: MutableList<String> = mutableListOf("Apple", "Banana", "Cherry")  
val mutableNumbers: MutableSet<Int> = mutableSetOf(1, 2, 3, 4, 5)  
val mutableMap: MutableMap<String, String> = mutableMapOf("key1" to "value1", "key2" to "value2")
```

