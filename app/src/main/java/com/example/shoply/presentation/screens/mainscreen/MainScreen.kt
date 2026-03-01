
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
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
import com.example.shoply.presentation.components.ShoplyBottomBar
import com.example.shoply.presentation.components.ShoplyFab
import com.example.shoply.presentation.components.ShoplyTopBar
import com.example.shoply.presentation.screens.homescreen.HomeDestination
import com.example.shoply.presentation.screens.homescreen.homeScreen
import com.example.shoply.presentation.screens.homescreen.navigateToHomeScreen
import com.example.shoply.presentation.screens.productcatalogscreen.navigateToProductCatalogScreen
import com.example.shoply.presentation.screens.productcatalogscreen.productCatalogScreen
import com.myapp.shoply.R

@Composable
fun MainScreen(
    onLogoutClick: () -> Unit,
) {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val isHomeScreen =
        navBackStackEntry?.destination?.hasRoute<HomeDestination>() == true

    var fabConfig by remember { mutableStateOf(FabConfig()) }


    Scaffold(
        topBar = {
            ShoplyTopBar(
                modifier = Modifier,
                title = stringResource(R.string.app_name),
                isHomeScreen = isHomeScreen,
                onBackButtonClick = { navController.popBackStack() },
                onLogoutClick = { onLogoutClick() },
                onSideMenuClick = {}
            )
        },
        floatingActionButton = {
            if (fabConfig.visible && fabConfig.onClick != null) {
                ShoplyFab(
                    modifier = Modifier,
                    onFabClick = { fabConfig.onClick?.invoke() }
                )
            }
        },
        floatingActionButtonPosition = FabPosition.Center,
        bottomBar = {
            ShoplyBottomBar(
                onHomeClick = { navController.navigateToHomeScreen() },
                onProductsClick = { navController.navigateToProductCatalogScreen() },
                onSettingsClick = {}
            )
        }
    ) { padding ->

        NavHost(
            modifier = Modifier.padding(padding),
            navController = navController,
            startDestination = HomeDestination
        ) {
            homeScreen(
                onFabConfigChange = { fabConfig = it }
            )
            productCatalogScreen(
                onFabConfigChange = { fabConfig = it }
            )
        }


    }
}

data class FabConfig(
    val visible: Boolean = false,
    val onClick: (() -> Unit)? = null
)