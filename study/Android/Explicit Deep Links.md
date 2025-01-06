```kotlin
/*  
 * Copyright 2022 The Android Open Source Project * * Licensed under the Apache License, Version 2.0 (the "License"); * you may not use this file except in compliance with the License. * You may obtain a copy of the License at * *     https://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, software * distributed under the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. * See the License for the specific language governing permissions and * limitations under the License. */  
package com.example.compose.rally  
  
import android.Manifest  
import android.app.NotificationChannel  
import android.app.NotificationManager  
import android.content.Context  
import android.content.pm.PackageManager  
import android.os.Build  
import android.os.Bundle  
import androidx.activity.ComponentActivity  
import androidx.activity.compose.setContent  
import androidx.compose.foundation.layout.padding  
import androidx.compose.material.Scaffold  
import androidx.compose.runtime.Composable  
import androidx.compose.runtime.getValue  
import androidx.compose.ui.Modifier  
import androidx.core.app.ActivityCompat  
import androidx.core.app.NotificationCompat  
import androidx.core.app.NotificationManagerCompat  
import androidx.navigation.NavController  
import androidx.navigation.NavDeepLinkBuilder  
import androidx.navigation.NavHostController  
import androidx.navigation.compose.currentBackStackEntryAsState  
import androidx.navigation.compose.rememberNavController  
import com.example.compose.rally.ui.components.RallyTabRow  
import com.example.compose.rally.ui.theme.RallyTheme  
  
/**  
 * This Activity recreates part of the Rally Material Study from * https://material.io/design/material-studies/rally.html */class RallyActivity : ComponentActivity() {  
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        setContent {  
            RallyApp(this)  
        }  
    }  
}  
  
@Composable  
fun RallyApp(context: Context) {  
    RallyTheme {  
        val navController = rememberNavController()  
        val currentBackStack by navController.currentBackStackEntryAsState()  
        val currentDestination = currentBackStack?.destination  
        val currentScreen =  
            rallyTabRowScreens.find { it.route == currentDestination?.route } ?: Accounts  
        Scaffold(  
            topBar = {  
                RallyTabRow(  
                    allScreens = rallyTabRowScreens,  
                    //onTabSelected = { screen -> currentScreen = screen },  
                    onTabSelected = { newScreen ->  
                        navController.navigateSingleTopTo(newScreen.route)  
                    },  
                    currentScreen = currentScreen  
                )  
            }  
        ) { innerPadding ->  
            RallyNavHost(  
                navController = navController,  
                context = context,  
                modifier = Modifier.padding(innerPadding)  
            )  
            /*  
            Box(Modifier.padding(innerPadding)) {                currentScreen.screen()            }             */        }  
    }}  
  
fun createDeepLinkNotification(context: Context,  navController: NavHostController, accountType: String) {  
    // Create a PendingIntent for the deep link  
    val pendingIntent = NavDeepLinkBuilder(context)  
        .setComponentName(RallyActivity::class.java) // Your main activity  
        .setGraph(navController.graph) // Provide a graph ID  
        .setDestination(SingleAccount.routeWithArgs) // Navigate to SingleAccount  
        .setArguments(Bundle().apply {  
            putString(SingleAccount.accountTypeArg, accountType)  
        })  
        .createPendingIntent()  
  
    // Create a notification channel (for Android 8.0+)  
    val channelId = "deep_link_channel"  
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {  
        val channel = NotificationChannel(  
            channelId,  
            "Deep Link Notifications",  
            NotificationManager.IMPORTANCE_DEFAULT  
        )  
        val notificationManager: NotificationManager =  
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager  
        notificationManager.createNotificationChannel(channel)  
    }  
  
    // Build and show the notification  
    val notification = NotificationCompat.Builder(context, channelId)  
        .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with a proper icon  
        .setContentTitle("Account Details")  
        .setContentText("Tap to view account: $accountType")  
        .setContentIntent(pendingIntent) // Attach the PendingIntent  
        .setAutoCancel(true)  
        .build()  
  
    if (ActivityCompat.checkSelfPermission(  
            context,  
            Manifest.permission.POST_NOTIFICATIONS  
        ) != PackageManager.PERMISSION_GRANTED  
    ) {  
        // TODO: Consider calling  
        //    ActivityCompat#requestPermissions  
        // here to request the missing permissions, and then overriding  
        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,        //                                          int[] grantResults)        // to handle the case where the user grants the permission. See the documentation        // for ActivityCompat#requestPermissions for more details.        return  
    }  
    NotificationManagerCompat.from(context).notify(1, notification)  
}  
  
fun RallyNavHostGraphId(navController: NavHostController): Int {  
    // Dynamically return the graph ID from the NavController  
    return navController.graph.id  
}


/*  
 * Copyright 2022 The Android Open Source Project * * Licensed under the Apache License, Version 2.0 (the "License"); * you may not use this file except in compliance with the License. * You may obtain a copy of the License at * *     https://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, software * distributed under the License is distributed on an "AS IS" BASIS, * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. * See the License for the specific language governing permissions and * limitations under the License. */  
package com.example.compose.rally  
  
import androidx.compose.material.icons.Icons  
import androidx.compose.material.icons.filled.AttachMoney  
import androidx.compose.material.icons.filled.Money  
import androidx.compose.material.icons.filled.MoneyOff  
import androidx.compose.material.icons.filled.PieChart  
import androidx.compose.ui.graphics.vector.ImageVector  
import androidx.navigation.NavType  
import androidx.navigation.navArgument  
import androidx.navigation.navDeepLink  
  
/**  
 * Contract for information needed on every Rally navigation destination */interface RallyDestination {  
    val icon: ImageVector  
    val route: String  
}  
  
/**  
 * Rally app navigation destinations */object Overview : RallyDestination {  
    override val icon = Icons.Filled.PieChart  
    override val route = "overview"  
}  
  
object Accounts : RallyDestination {  
    override val icon = Icons.Filled.AttachMoney  
    override val route = "accounts"  
}  
  
object Bills : RallyDestination {  
    override val icon = Icons.Filled.MoneyOff  
    override val route = "bills"  
}  
  
object SingleAccount : RallyDestination {  
    // Added for simplicity, this icon will not in fact be used, as SingleAccount isn't  
    // part of the RallyTabRow selection    override val icon = Icons.Filled.Money  
    override val route = "single_account"  
    const val accountTypeArg = "account_type"  
    val routeWithArgs = "${route}/{${accountTypeArg}}"  
    val arguments = listOf(  
        navArgument(SingleAccount.accountTypeArg) { type = NavType.StringType }  
    )  
    val deepLinks = listOf(  
        navDeepLink { uriPattern = "rally://$route/{$accountTypeArg}"}  
    )  
}  
  
// Screens to be displayed in the top RallyTabRow  
val rallyTabRowScreens = listOf(Overview, Accounts, Bills)



package com.example.compose.rally  
  
import android.content.Context  
import android.util.Log  
import androidx.compose.runtime.Composable  
import androidx.compose.ui.Modifier  
import androidx.navigation.NavGraph.Companion.findStartDestination  
import androidx.navigation.NavHostController  
import androidx.navigation.compose.NavHost  
import androidx.navigation.compose.composable  
import com.example.compose.rally.ui.accounts.AccountsScreen  
import com.example.compose.rally.ui.accounts.SingleAccountScreen  
import com.example.compose.rally.ui.bills.BillsScreen  
import com.example.compose.rally.ui.overview.OverviewScreen  
  
@Composable  
fun RallyNavHost(  
    navController: NavHostController,  
    context: Context,  
    modifier: Modifier = Modifier  
) {  
    NavHost(  
        navController = navController,  
        startDestination = Overview.route,  
        modifier = modifier  
    ) {  
        composable(route = Overview.route) {  
            OverviewScreen(  
                onClickSeeAllAccounts = {  
                    navController.navigateSingleTopTo(Accounts.route)  
                },  
                onClickSeeAllBills = {  
                    createDeepLinkNotification(context = context, navController, accountType = "Checking")  
                    //navController.navigateSingleTopTo(Bills.route)  
                },  
                onAccountClick = { accountType ->  
                    navController.navigateToSingleAccount(accountType)  
                }  
            )  
        }  
        composable(route = Accounts.route) {  
            AccountsScreen(  
                onAccountClick = { accountType ->  
                    navController.navigateToSingleAccount(accountType)  
                }  
            )  
        }  
        composable(route = Bills.route) {  
            BillsScreen()  
        }  
        composable(  
            route = SingleAccount.routeWithArgs,  
            arguments = SingleAccount.arguments,  
            deepLinks = SingleAccount.deepLinks  
        ) { navBackStackEntry ->  
            val accountType =  
                navBackStackEntry.arguments?.getString(SingleAccount.accountTypeArg)  
            SingleAccountScreen(accountType)  
        }  
    }}  
  
fun NavHostController.navigateSingleTopTo(route: String) {  
    val currentRoute = this.currentBackStackEntry?.destination?.route  
    Log.d("Navigation", "Navigating from: $currentRoute to: $route")  
    this.navigate(route) {  
        popUpTo(  
            this@navigateSingleTopTo.graph.findStartDestination().id  
        ) {  
            Log.d(  
                "Navigation",  
                "popUpTo:${this@navigateSingleTopTo.graph.findStartDestination().route.toString()}"  
            )  
            saveState = true  
        }  
        launchSingleTop = true  
        restoreState = true  
    }  
}  
  
private fun NavHostController.navigateToSingleAccount(accountType: String) {  
    this.navigateSingleTopTo("${SingleAccount.route}/$accountType")  
}






```
