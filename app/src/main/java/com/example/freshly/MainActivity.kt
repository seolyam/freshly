package com.example.freshly

import CheckoutPage
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
import com.example.freshly.ui.theme.CartScreen
import com.example.freshly.ui.theme.CartViewModel
import com.example.freshly.ui.theme.EditProfileScreen
import com.example.freshly.ui.theme.FreshlyTheme
import com.example.freshly.ui.theme.HomePageScreen
import com.example.freshly.ui.theme.InfoPage
import com.example.freshly.ui.theme.LoginScreen
import com.example.freshly.ui.theme.OrderConfirmation
import com.example.freshly.ui.theme.ProductPageScreen
import com.example.freshly.ui.theme.SignUpScreen
import com.example.freshly.ui.theme.UserViewModel

class MainActivity : ComponentActivity() {
    // Correctly initialize the ViewModels
    private val cartViewModel: CartViewModel by viewModels()
    private val userViewModel: UserViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreshlyTheme {
                val navController = rememberNavController()
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationComponent(
                        navController = navController,
                        modifier = Modifier.padding(innerPadding),
                        cartViewModel = cartViewModel,
                        userViewModel = userViewModel
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
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel
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
            SignUpScreen(
                userViewModel = userViewModel,
                onSignUpSuccess = { navController.navigate("info") }
            )
        }
        composable("info") {
            InfoPage(
                userViewModel = userViewModel,
                onNavigateBack = { navController.popBackStack() },
                onSignUpComplete = { navController.navigate("home") }
            )
        }
        composable("home") {
            HomePageScreen(
                onCartClick = { navController.navigate("cart") },
                onProductClick = { product ->
                    navController.navigate(
                        "product/${product.name}/${product.price}/${product.description}/${product.allergens}"
                    )
                },
                onProfileClick = { navController.navigate("editProfile") }
            )
        }
        composable("cart") {
            CartScreen(
                cartViewModel = cartViewModel,
                onCheckoutClick = { navController.navigate("checkout") }
            )
        }
        composable("product/{productName}/{productPrice}/{productDescription}/{productAllergens}") { backStackEntry ->
            val productName = backStackEntry.arguments?.getString("productName") ?: ""
            val productPriceString = backStackEntry.arguments?.getString("productPrice") ?: "0.0"
            val productPrice = productPriceString.toDoubleOrNull() ?: 0.0
            val productDescription = backStackEntry.arguments?.getString("productDescription") ?: ""
            val productAllergens = backStackEntry.arguments?.getString("productAllergens") ?: ""

            ProductPageScreen(
                cartViewModel = cartViewModel,
                productName = productName,
                productPrice = productPrice,
                productDescription = productDescription,
                productAllergens = productAllergens,
                onNavigateBack = { navController.popBackStack() },
                onCartClick = { navController.navigate("cart") }
            )
        }
        composable("checkout") {
            CheckoutPage(
                cartViewModel = cartViewModel,
                userViewModel = userViewModel,
                onNavigateBack = { navController.popBackStack() },
                onPlaceOrder = {
                    cartViewModel.clearCart()
                    navController.navigate("orderConfirmation") {
                        popUpTo("home") { inclusive = false }
                    }
                }
            )
        }
        composable("orderConfirmation") {
            OrderConfirmation(
                onNavigateBack = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onTrackOrder = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable("editProfile") {
            EditProfileScreen(
                userViewModel = userViewModel,
                onSave = { /* Implement save functionality */ },
                onNavigateBack = { navController.popBackStack() },
                onLogout = {
                    // Handle logout action
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
    }
}
