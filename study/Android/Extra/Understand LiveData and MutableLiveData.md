https://www.youtube.com/watch?v=6Jc6-INantQ&ab_channel=PhilippLackner

https://medium.com/@rushabhprajapati20/livedata-vs-mutablestateflow-in-android-kotlin-a-comprehensive-comparison-a186848d410c

**LiveData is the only one that works for java**
# LiveData

LiveData is a part of the Android Jetpack suite of libraries. It represents data that is observable and lifecycle-aware. Being lifecycle-aware means LiveData respects the lifecycle of app components such as Activities, Fragments, and Services, ensuring that these components only receive updates when they’re in an active state.

Benefits of LiveData:

- **Eliminates Manual Lifecycle Management:** No need to manually stop or resume updates based on lifecycle changes.
- **Ensures Your UI Matches Your Data State:** LiveData notifies the observer objects when the lifecycle state changes, so you can update the UI to reflect changes instantly.
- **Reduces Memory Leaks:** Observers are bound to lifecycle objects and get cleaned up when their lifecycle is destroyed.
- **No More Crashes Due to Stopped Activities:** LiveData doesn’t push updates to observers that aren’t in an active state.

# MutableLiveData

MutableLiveData is a subclass of LiveData. It has public methods that allow you to modify the value stored in it. Specifically, you can use `setValue(T value)` for main thread operations or `postValue(T value)` for background thread operations.

**Why We Need LiveData and MutableLiveData:**

> **LiveData**:

- **Data Consistency:** LiveData ensures that the UI always displays the latest data.
- **Lifecycle-awareness:** LiveData automatically manages subscription and unsubscription based on lifecycle states, eliminating common bugs due to lifecycle issues.

> **MutableLiveData:**

- **Data Modification:** There are cases when the data source needs to be modified. For example, when you fetch data from a database or network and want to update your UI, MutableLiveData allows for that modification.

**Example of Integration LiveData and MutableLiveData:**

```kotlin
class UserViewModel : ViewModel() {  
    // MutableLiveData instance (private to prevent unwanted modifications)  
    private val _userName = MutableLiveData<String>()  
    // LiveData instance exposed to the UI  
    val userName: LiveData<String> get() = _userName  
    // Function to update the name  
    fun updateName(name: String) {  
        _userName.value = name  
    }  
}  
  
  
// In Activity or Fragment  
userViewModel.userName.observe(this, Observer { name ->  
    // This lambda is executed whenever the data changes.  
    userNameTextView.text = name  
})
```

**Difference between LiveData and MutableLiveData:**

- **LiveData:** It’s a read-only version. You can’t alter its content directly from where it’s being observed.
- **MutableLiveData:** It’s mutable, meaning you can change its content. It exposes `setValue()` and `postValue()` for this purpose.

**Which is best and when: LiveData or MutableLiveData?**

Both LiveData and MutableLiveData have their use cases:

- **LiveData:** Best for situations where data should be observable but not modifiable by the observer. You typically use LiveData in the UI layer to observe changes.
- **MutableLiveData:** Best for situations where data is expected to change, such as in your ViewModel or data layer. This allows you to encapsulate the data and expose only LiveData to the UI, ensuring the UI can’t make unexpected changes.

**In a typical MVVM architecture:**

- ViewModel contains MutableLiveData because it can modify data based on user actions or network responses.
- View (Activity or Fragment) observes LiveData to reflect changes in the UI, ensuring data is only read and not modified.

By adhering to this separation, you maintain a clean architecture where responsibilities are clearly defined.

-------------

## LiveData vs. StateFlow: The Battle of the Observables?

To our core business of the day now:

Why StateFlow?  
_StateFlow is a new observable data holder class in Android that was introduced in Kotlin 1.3.72. It is designed to be a more powerful and flexible alternative to LiveData._

LiveData vs. StateFlow?  
_LiveData and StateFlow are both observable data holder classes in Android. They are both used to broadcast data changes to subscribers, but they have some key differences.  
_**LiveData**

- LiveData is lifecycle aware, which means that it will automatically stop broadcasting data when the view that is observing it is no longer in the foreground.
- LiveData is a snapshot-based observable, which means that it only emits new values when the data changes. This can be helpful for reducing the number of times that the UI is updated.
- LiveData is a part of the Android framework, so it is tightly coupled to the Android platform.

**StateFlow**

- StateFlow is not lifecycle aware, so you need to manually manage the lifecycle of your subscribers.
- StateFlow is a hot observable, which means that it will emit all new values to all subscribers, even if the value has not changed. This can be helpful for ensuring that all subscribers have the latest data.
- StateFlow is not part of the Android framework, so it is more decoupled from the Android platform.

**However, StateFlow has several advantages over LiveData, including:**

- **Hot emissions:** StateFlow emits all new values to all subscribers, even if the value has not changed. This can be helpful for ensuring that all subscribers have the latest data.
- **Decoupling from the Android platform:** StateFlow is not a part of the Android framework, so it is more decoupled from the Android platform. This makes it easier to use StateFlow in non-Android projects.
- **Better performance**: StateFlow is more performant than LiveData, especially when there are a large number of subscribers.

Here are some reasons why you might want to use StateFlow instead of LiveData:

- You need to ensure that all subscribers have the latest data.
- You are building an app that is not tightly coupled to the Android platform.
- You are building an app that needs to be highly performant.

Example project:

Start by adding the required dependencies in your gradle (app level) as shown:

// Activity KTX for viewModels()  
    implementation "androidx.activity:activity-ktx:1.1.0"  
  
    // Architectural Components  
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"  
  
    // Coroutines  
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9'  
    implementation 'org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9'  
  
    // Coroutine Lifecycle Scopes  
    implementation "androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0"  
    implementation "androidx.lifecycle:lifecycle-runtime-ktx:2.2.0"

We’ll be using binding to access our views, so we’ll add below line of code as well.

buildFeatures { viewBinding true }

Create your xml layout as shown

<?xml version="1.0" encoding="utf-8"?>  
<androidx.constraintlayout.widget.ConstraintLayout xmlns:android="http://schemas.android.com/apk/res/android"  
    xmlns:app="http://schemas.android.com/apk/res-auto"  
    xmlns:tools="http://schemas.android.com/tools"  
    android:layout_width="match_parent"  
    android:layout_height="match_parent"  
    tools:context=".MainActivity">  
  
    <EditText  
        android:id="@+id/userName"  
        android:layout_width="300dp"  
        android:hint="Enter your username"  
        android:layout_height="wrap_content"  
        android:gravity="center"  
        app:layout_constraintBottom_toBottomOf="parent"  
        app:layout_constraintEnd_toEndOf="parent"  
        app:layout_constraintStart_toStartOf="parent"  
        app:layout_constraintTop_toTopOf="parent" />  
  
  
    <EditText  
        android:id="@+id/password"  
        android:layout_width="300dp"  
        android:hint="Enter your password"  
        android:gravity="center"  
        android:layout_height="wrap_content"  
        app:layout_constraintEnd_toEndOf="parent"  
        app:layout_constraintStart_toStartOf="parent"  
        app:layout_constraintTop_toBottomOf="@+id/userName" />  
  
  
    <Button  
        android:id="@+id/submitButton"  
        android:layout_width="300dp"  
        android:layout_height="wrap_content"  
        android:gravity="center"  
        android:text="Submit"  
        android:textStyle="italic"  
        app:layout_constraintEnd_toEndOf="@+id/password"  
        app:layout_constraintStart_toStartOf="@+id/password"  
        app:layout_constraintTop_toBottomOf="@+id/password" />  
  
    <ProgressBar  
        android:id="@+id/progressBar"  
        android:layout_width="wrap_content"  
        android:layout_height="wrap_content"  
        android:visibility="gone"  
        app:layout_constraintBottom_toBottomOf="@+id/submitButton"  
        app:layout_constraintEnd_toEndOf="@+id/password"  
        app:layout_constraintStart_toStartOf="@+id/password"  
        app:layout_constraintTop_toTopOf="@+id/submitButton" />  
  
</androidx.constraintlayout.widget.ConstraintLayout>

You’ll finally need to write your code and business logic: We’ll use MainActivity and MainViewModel in our case. See below,

MainActivity  
NB: Ensure you have the right imports for your project to compile successfully.

class MainActivity : AppCompatActivity() {  
  
    private var _binding: ActivityMainBinding? = null  
  
    private val viewModel: MainViewModel by viewModels()  
    private val binding get() = _binding!!  
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        _binding = ActivityMainBinding.inflate(layoutInflater)  
        setContentView(binding.root)  
  
        login()  
        lifecycleObserver()  
    }  
  
    private fun login() {  
        binding.apply {  
            submitButton.setOnClickListener {  
                viewModel.login(  
                    username = userName.text.toString(),  
                    password = password.text.toString()  
                )  
            }  
        }  
  
    }  
  
    private fun lifecycleObserver() {  
        lifecycleScope.launchWhenStarted {  
            viewModel.loginUiState.collect {  
                when (it) {  
                    is MainViewModel.LoginUiState.Success -> {  
                        Snackbar.make(binding.root, "Successfully logged in", Snackbar.LENGTH_LONG)  
                            .show()  
                        binding.apply {  
                            progressBar.isVisible = false  
                            submitButton.isVisible = true  
                        }  
                    }  
  
                    is MainViewModel.LoginUiState.Error -> {  
                        Snackbar.make(binding.root, it.message, Snackbar.LENGTH_LONG)  
                            .show()  
  
                        binding.apply {  
                            progressBar.isVisible = false  
                            submitButton.isVisible = true  
                        }  
                    }  
  
                    is MainViewModel.LoginUiState.Loading -> {  
                        binding.apply {  
                            progressBar.isVisible = true  
                            submitButton.isVisible = false  
                        }  
  
                    }  
  
                    else -> {}  
                }  
            }  
        }  
    }  
  
  
    override fun onDestroy() {  
        super.onDestroy()  
        _binding = null  
    }  
}

MainViewModel

class MainViewModel : ViewModel(){  
  
    private val _loginUiState = MutableStateFlow<LoginUiState>(LoginUiState.Empty) // Mutable UI State Flow  
    val loginUiState: StateFlow<LoginUiState> = _loginUiState  
  
  
    fun login(username: String, password: String){  
        viewModelScope.launch {  
            _loginUiState.value = LoginUiState.Loading  
  
            delay(2000L)  
            if (username =="Android" && password =="secret"){  
                _loginUiState.value = LoginUiState.Success  
            } else {  
                _loginUiState.value = LoginUiState.Error("Wrong Credentials")  
            }  
        }  
  
    }  
  
  
  
    sealed class LoginUiState{  
        object Success : LoginUiState()  
        object Loading: LoginUiState()  
        object Empty: LoginUiState()  
        data class Error(val message: String): LoginUiState()  
    }  
}




