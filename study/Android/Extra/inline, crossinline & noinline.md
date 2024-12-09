Documentation related these 3 keywords:
```kotlin
inline
crossinline
noinline
```

### Normal function:

Main.kt
```kotlin
fun main() {
	val list = (1..100).toList()
	list.normalForEach {
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

So the difference we can notice it on the decompiled version of the code, go to Tools>Kotlin>Kotlin Bytecode
So using the normal func word will create also a function in the decompiled version.

Main.decompiled.java
```java
import java.util.Iterator;  
import java.util.List;  
import kotlin.Metadata;  
import kotlin.collections.CollectionsKt;  
import kotlin.jvm.functions.Function1;  
import kotlin.jvm.internal.Intrinsics;  
import kotlin.ranges.IntRange;  
import org.jetbrains.annotations.NotNull;  
  
@Metadata(  
   mv = {1, 9, 0},  
   k = 2,  
   xi = 48,  
   d1 = {"\u0000\u0016\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0000\u001a\u0006\u0010\u0000\u001a\u00020\u0001\u001a*\u0010\u0002\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u00042\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\u00010\u0006¨\u0006\u0007"},  
   d2 = {"main", "", "normalForEach", "T", "", "action", "Lkotlin/Function1;", "app_debug"}  
)  
public final class MainKt {  
   public static final void main() {  
      List list = CollectionsKt.toList((Iterable)(new IntRange(1, 100)));  
      normalForEach(list, (Function1)null.INSTANCE);  
   }  
  
   public static final void normalForEach(@NotNull List $this$normalForEach, @NotNull Function1 action) {  
      Intrinsics.checkNotNullParameter($this$normalForEach, "<this>");  
      Intrinsics.checkNotNullParameter(action, "action");  
      Iterator var2 = $this$normalForEach.iterator();  
  
      while(var2.hasNext()) {  
         Object item = var2.next();  
         action.invoke(item);  
      }  
  
   }  
  
   // $FF: synthetic method  
   public static void main(String[] args) {  
      main();  
   }  
}
```

### Inline
The difference is that the code of the function is actually inlined where the function is called, so it kinds of copy paste it.
Works with coroutines without problems.

Main.kt
```kotlin
fun main() {  
    val list = (1..100).toList()  
    list.inlineForEach {  
        println(it)  
    }  
}

inline fun <T> List<T>.inlineForEach(action: (T) -> Unit) {  
    for (item in this) {  
        action(item)  
    }  
}
```

Main.decompiled.java
```java
import java.util.Iterator;  
import java.util.List;  
import kotlin.Metadata;  
import kotlin.collections.CollectionsKt;  
import kotlin.jvm.functions.Function1;  
import kotlin.jvm.internal.Intrinsics;  
import kotlin.jvm.internal.SourceDebugExtension;  
import kotlin.ranges.IntRange;  
import org.jetbrains.annotations.NotNull;  
  
@Metadata(  
   mv = {1, 9, 0},  
   k = 2,  
   xi = 48,  
   d1 = {"\u0000\u0018\n\u0000\n\u0002\u0010\u0002\n\u0002\b\u0002\n\u0002\u0010 \n\u0000\n\u0002\u0018\u0002\n\u0002\b\u0002\u001a\u0006\u0010\u0000\u001a\u00020\u0001\u001a0\u0010\u0002\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u00042\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\u00010\u0006H\u0086\bø\u0001\u0000\u001a*\u0010\u0007\u001a\u00020\u0001\"\u0004\b\u0000\u0010\u0003*\b\u0012\u0004\u0012\u0002H\u00030\u00042\u0012\u0010\u0005\u001a\u000e\u0012\u0004\u0012\u0002H\u0003\u0012\u0004\u0012\u00020\u00010\u0006\u0082\u0002\u0007\n\u0005\b\u009920\u0001¨\u0006\b"},  
   d2 = {"main", "", "inlineForEach", "T", "", "action", "Lkotlin/Function1;", "normalForEach", "app_debug"}  
)  
@SourceDebugExtension({"SMAP\nMain.kt\nKotlin\n*S Kotlin\n*F\n+ 1 Main.kt\nMainKt\n*L\n1#1,19:1\n15#1,4:20\n*S KotlinDebug\n*F\n+ 1 Main.kt\nMainKt\n*L\n3#1:20,4\n*E\n"})  
public final class MainKt {  
   public static final void main() {  
      List list = CollectionsKt.toList((Iterable)(new IntRange(1, 100)));  
      List $this$inlineForEach$iv = list;  
      int $i$f$inlineForEach = false;  
      Iterator var3 = $this$inlineForEach$iv.iterator();  
  
      while(var3.hasNext()) {  
         Object item$iv = var3.next();  
         int it = ((Number)item$iv).intValue();  
         int var6 = false;  
         System.out.println(it);  
      }  
  
   }  
  
   public static final void normalForEach(@NotNull List $this$normalForEach, @NotNull Function1 action) {  
      Intrinsics.checkNotNullParameter($this$normalForEach, "<this>");  
      Intrinsics.checkNotNullParameter(action, "action");  
      Iterator var2 = $this$normalForEach.iterator();  
  
      while(var2.hasNext()) {  
         Object item = var2.next();  
         action.invoke(item);  
      }  
  
   }  
  
   public static final void inlineForEach(@NotNull List $this$inlineForEach, @NotNull Function1 action) {  
      Intrinsics.checkNotNullParameter($this$inlineForEach, "<this>");  
      Intrinsics.checkNotNullParameter(action, "action");  
      int $i$f$inlineForEach = false;  
      Iterator var3 = $this$inlineForEach.iterator();  
  
      while(var3.hasNext()) {  
         Object item = var3.next();  
         action.invoke(item);  
      }  
  
   }  
  
   // $FF: synthetic method  
   public static void main(String[] args) {  
      main();  
   }  
}
```

There are a lot of inline functions out there for example:
```kotlin
list.map {}
kotlin.math.sqrt()
```

nice usage when at compile time we dont have access to something like class name we can use inline in combination with reified:
```kotlin
fun main() {
	"Hello world".printClassName()
}
inline fun <reified T> T.printClassName(){
	println(T::class.simpleName)
}
```

So for returning is easy to return with the inline function instead of the normal in case of a lambda function parameter.
```kotlin
list.inlinedForEach {
	println(it)
	return
}
```

that would not work in a normal function, but be careful because if a function has a suspend function inside so with Coroutines then migh have problems because the suspend inside can last longer or less so for that we have to use:
```kotlin
inline fun executeAsync(crossinline action: () -> Unit) {
	CoroutineScope(Dispatchers.Default).launch {
	action()
	}
}
```
we can also us noinline

More examples with inline now with variables:
```kotlin
inline val <T> List<T>.lastItem: T  
    get() = get(lastIndex)

println(list.lastItem)
```

now with class:
```kotlin
@JvmInline  
value class Month(val number: Int)
```
