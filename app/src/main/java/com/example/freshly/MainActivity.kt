package com.example.freshly

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.freshly.ui.HomePageScreen
import com.example.freshly.ui.Phone
import com.example.freshly.ui.theme.CartScreen
import com.example.freshly.ui.theme.CartViewModel
import com.example.freshly.ui.theme.CheckoutPage
import com.example.freshly.ui.theme.EditProfileScreen
import com.example.freshly.ui.theme.FreshlyTheme
import com.example.freshly.ui.theme.InfoPage
import com.example.freshly.ui.theme.LoginScreen
import com.example.freshly.ui.theme.OrderConfirmation
import com.example.freshly.ui.theme.ProductPageScreen
import com.example.freshly.ui.theme.SignUpScreen
import com.example.freshly.ui.theme.TokenManager
import com.example.freshly.ui.theme.UserProfileScreen
import com.example.freshly.ui.theme.UserViewModel
import com.example.freshly.viewmodel.ProductViewModel
import com.example.freshly.ui.theme.ApiClient
import com.example.freshly.ui.theme.ApiService
import java.net.URLDecoder
import java.net.URLEncoder

class MainActivity : ComponentActivity() {
    // Create a custom factory for CartViewModel
    private val cartViewModel: CartViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val apiService = ApiClient.retrofit.create(ApiService::class.java)
                val tokenManager = TokenManager(applicationContext)
                @Suppress("UNCHECKED_CAST")
                return CartViewModel(apiService, tokenManager) as T
            }
        }
    }

    private val userViewModel: UserViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                val tokenManager = TokenManager(applicationContext)
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(tokenManager) as T
            }
        }
    }

    private val productViewModel: ProductViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FreshlyTheme {
                val navController = rememberNavController()

                // Observe userInfo token using collectAsState()
                val userInfo = userViewModel.userInfo.collectAsState().value
                val isLoggedIn = userInfo.token.isNotEmpty()
                val startDestination = if (isLoggedIn) "home" else "phone"

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    NavigationComponent(
                        navController = navController,
                        startDestination = startDestination,
                        modifier = Modifier.padding(innerPadding),
                        cartViewModel = cartViewModel,
                        userViewModel = userViewModel,
                        productViewModel = productViewModel
                    )
                }
            }
        }
    }
}

@Composable
fun NavigationComponent(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier,
    cartViewModel: CartViewModel,
    userViewModel: UserViewModel,
    productViewModel: ProductViewModel
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable("phone") {
            Phone(
                onLoginClick = { navController.navigate("login") },
                onSignUpClick = { navController.navigate("signup") }
            )
        }
        composable("login") {
            LoginScreen(
                userViewModel = userViewModel,
                onLoginSuccess = {
                    navController.navigate("home") {
                        popUpTo("phone") { inclusive = true }
                    }
                }
            )
        }
        composable("signup") {
            SignUpScreen(
                userViewModel = userViewModel,
                onSignUpSuccess = {
                    navController.navigate("home") {
                        popUpTo("phone") { inclusive = true }
                    }
                }
            )
        }
        composable("home") {
            HomePageScreen(
                onCartClick = { navController.navigate("cart") },
                onProductClick = { product ->
                    val encodedName = URLEncoder.encode(product.name, "UTF-8")
                    val encodedDescription = URLEncoder.encode(product.description, "UTF-8")
                    val encodedAllergens = URLEncoder.encode(product.allergens, "UTF-8")
                    val encodedImageUrl = URLEncoder.encode(product.imageUrl, "UTF-8")

                    navController.navigate(
                        "product/${product.id}/$encodedName/${product.price}/$encodedDescription/$encodedAllergens/$encodedImageUrl"
                    )
                },
                onProfileClick = { navController.navigate("userProfile") },
                productViewModel = productViewModel
            )
        }
        composable("cart") {
            CartScreen(
                cartViewModel = cartViewModel,
                onCheckoutClick = { navController.navigate("checkout") }
            )
        }
        composable("product/{productId}/{productName}/{productPrice}/{productDescription}/{productAllergens}/{productImageUrl}") { backStackEntry ->
            val productIdString = backStackEntry.arguments?.getString("productId") ?: "0"
            val productId = productIdString.toIntOrNull() ?: 0
            val productName = backStackEntry.arguments?.getString("productName")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: ""
            val productPriceString = backStackEntry.arguments?.getString("productPrice") ?: "0.0"
            val productPrice = productPriceString.toDoubleOrNull() ?: 0.0
            val productDescription = backStackEntry.arguments?.getString("productDescription")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: ""
            val productAllergens = backStackEntry.arguments?.getString("productAllergens")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: ""
            val productImageUrl = backStackEntry.arguments?.getString("productImageUrl")?.let {
                URLDecoder.decode(it, "UTF-8")
            } ?: ""

            ProductPageScreen(
                productId = productId,
                productName = productName,
                productPrice = productPrice,
                productDescription = productDescription,
                productAllergens = productAllergens,
                productImageUrl = productImageUrl,
                onNavigateBack = { navController.popBackStack() },
                onCartClick = { navController.navigate("cart") },
                cartViewModel = cartViewModel
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
                },
                onEditInfoClick = {
                    navController.navigate("info") // Navigate to info page
                }
            )
        }

        composable("info") {
            InfoPage(
                userViewModel = userViewModel,
                onNavigateBack = { navController.popBackStack() },
                onSignUpComplete = {
                    navController.popBackStack()
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
        composable("userProfile") {
            UserProfileScreen(
                userViewModel = userViewModel,
                onEditProfile = { navController.navigate("editProfile") },
                onLogout = {
                    userViewModel.logout()
                    navController.navigate("phone") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable("editProfile") {
            EditProfileScreen(
                userViewModel = userViewModel,
                onSave = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}
