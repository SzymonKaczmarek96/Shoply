
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.shoply.presentation.components.DropdownMenuConfig
import com.example.shoply.presentation.components.ShoplyBottomBar
import com.example.shoply.presentation.components.ShoplyFab
import com.example.shoply.presentation.components.ShoplyTopBar
import com.example.shoply.presentation.components.snackbar.ShoplySnackbarHost
import com.example.shoply.presentation.screens.homescreen.HomeDestination
import com.example.shoply.presentation.screens.homescreen.homeScreen
import com.example.shoply.presentation.screens.homescreen.navigateToHomeScreen
import com.example.shoply.presentation.screens.productcatalogscreen.ProductCatalogDestination
import com.example.shoply.presentation.screens.productcatalogscreen.navigateToProductCatalogScreen
import com.example.shoply.presentation.screens.productcatalogscreen.productCatalogScreen
import com.example.shoply.presentation.screens.productlistscreen.ProductListDestination
import com.example.shoply.presentation.screens.productlistscreen.navigateToProductListScreen
import com.example.shoply.presentation.screens.productlistscreen.productListScreen
import com.example.shoply.presentation.screens.settingscreen.SettingsDestination
import com.example.shoply.presentation.screens.settingscreen.navigateToSettingsScreen
import com.example.shoply.presentation.screens.settingscreen.settingScreen
import com.myapp.shoply.R

@Composable
fun MainScreen(
    onLogoutClick: () -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isHomeScreen =
        navBackStackEntry?.destination?.hasRoute<HomeDestination>() == true
    val isProductScreen =
        navBackStackEntry?.destination?.hasRoute<ProductCatalogDestination>() == true
    val isSettingsScreen =
        navBackStackEntry?.destination?.hasRoute<SettingsDestination>() == true

    var fabConfig by remember { mutableStateOf(FabConfig()) }
    var dropdownMenuConfig by remember { mutableStateOf(DropdownMenuConfig()) }

    val snackbarHost = remember { SnackbarHostState() }


    Scaffold(
        snackbarHost = {
            ShoplySnackbarHost(
                hostState = snackbarHost,
                modifier = Modifier
            )
        },
        topBar = {
            ShoplyTopBar(
                modifier = Modifier,
                title = stringResource(R.string.app_name),
                isHomeScreen = isHomeScreen,
                onBackButtonClick = { navController.popBackStack() },
                onLogoutClick = { onLogoutClick() },
                dropdownMenuItems = dropdownMenuConfig
            )
        },
        floatingActionButton = {
            if (fabConfig.visible && fabConfig.onClick != null) {
                ShoplyFab(
                    modifier = Modifier,
                    onFabClick = { fabConfig.onClick?.invoke() },
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            ShoplyBottomBar(
                isHomeScreen = isHomeScreen,
                isProductScreen = isProductScreen,
                isSettingsScreen = isSettingsScreen,
                onHomeClick = { navController.navigateToHomeScreen() },
                onProductsClick = { navController.navigateToProductCatalogScreen(null) },
                onSettingsClick = { navController.navigateToSettingsScreen() }
            )
        }
    ) { padding ->
        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = HomeDestination
        ) {
            val cameFromProductList = navController.previousBackStackEntry
                ?.destination?.hasRoute<ProductListDestination>() == true

            homeScreen(
                onFabConfigChange = { fabConfig = it },
                onListClick = {
                    navController.navigateToProductListScreen(it)
                }
            )
            productCatalogScreen(
                onFabConfigChange = { fabConfig = it },
                showSpecialIcon = cameFromProductList,
                onNavigateBack = {
                    navController.popBackStack()
                },
                onMenuConfigChange = { dropdownMenuConfig = it },
            )
            productListScreen(
                onFabConfigChange = { fabConfig = it },
                navigateToProductCatalog = {
                    navController.navigateToProductCatalogScreen(it)
                },
            )
            settingScreen(
                onNavigateToPassword = { } //TODO
            )
        }
    }
}

data class FabConfig(
    val visible: Boolean = false,
    val onClick: (() -> Unit)? = null
)