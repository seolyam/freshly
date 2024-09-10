package com.example.freshly

import CartScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.freshly.ui.Phone
import com.example.freshly.ui.theme.CartViewModel
import com.example.freshly.ui.theme.FreshlyTheme
import com.example.freshly.ui.theme.HomePageScreen
import com.example.freshly.ui.theme.LoginScreen
import com.example.freshly.ui.theme.ProductPage
import com.example.freshly.ui.theme.SignUpScreen

class MainActivity : ComponentActivity() {
    private val cartViewModel: CartViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreshlyTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationComponent(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        cartViewModel = cartViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationComponent(
    navController: NavHostController,
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel
) {
    NavHost(
        navController = navController,
        startDestination = "phone",
        modifier = modifier
    ) {
        composable("phone") {
            Phone(
                onLoginClick = { navController.navigate("login") },
                onSignUpClick = { navController.navigate("signup") }
            )
        }
        composable("login") {
            LoginScreen(onLoginSuccess = { navController.navigate("home") })
        }
        composable("signup") {
            SignUpScreen(onSignUpSuccess = { navController.navigate("home") })
        }
        composable("home") {
            HomePageScreen(
                onCartClick = { navController.navigate("cart") },
                onProductClick = { productName, productPrice, productImage ->
                    navController.navigate("product/$productName/$productPrice/$productImage")
                }
            )
        }
        composable("cart") {
            CartScreen(cartViewModel)
        }
        composable("product/{productName}/{productPrice}/{productImage}") { backStackEntry ->
            val productName = backStackEntry.arguments?.getString("productName") ?: ""
            val productPrice = backStackEntry.arguments?.getString("productPrice") ?: ""
            val productImage = backStackEntry.arguments?.getString("productImage") ?: ""
            ProductPage(
                cartViewModel = cartViewModel,
                productName = productName,
                productPrice = productPrice,
                productImage = productImage
            )
        }
    }
}
