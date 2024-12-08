package com.example.freshly.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.freshly.models.Product
import com.example.freshly.viewmodel.ProductViewModel
import com.google.accompanist.pager.*

@Composable
fun HomePageScreen(
    modifier: Modifier = Modifier,
    onCartClick: () -> Unit,
    onProductClick: (Product) -> Unit,
    onProfileClick: () -> Unit,
    onMyPurchasesClick: () -> Unit, // Added parameter for "My Purchases"
    productViewModel: ProductViewModel
) {
    val products by productViewModel.products.collectAsState()
    val isLoading by productViewModel.isLoading.collectAsState()
    val errorMessage by productViewModel.errorMessage.collectAsState()
    var searchQuery by remember { mutableStateOf("") }

    // Fetch products only once
    LaunchedEffect(Unit) {
        productViewModel.fetchProducts()
    }

    val filteredProducts = if (searchQuery.isBlank()) {
        products
    } else {
        products.filter { it.name.contains(searchQuery, ignoreCase = true) }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            TopBar(onCartClick)
            SearchSection(
                modifier = Modifier
                    .padding(16.dp)
                    .background(Color.White),
                searchQuery = searchQuery,
                onSearchChange = { query -> searchQuery = query }
            )

            ProductCarouselSection()

            Spacer(modifier = Modifier.height(16.dp))

            when {
                isLoading -> SkeletonLoader()
                errorMessage != null -> Text(
                    text = errorMessage ?: "Unknown error",
                    color = Color.Red,
                    modifier = Modifier.padding(16.dp)
                )
                else -> ProductCategoriesSection(products = filteredProducts, onProductClick = onProductClick)
            }
        }

        BottomNavigationBar(
            modifier = Modifier.align(Alignment.BottomCenter),
            onProfileClick = onProfileClick,
            onMyPurchasesClick = onMyPurchasesClick // Pass the click event handler
        )
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun ProductCarouselSection() {
    val pagerState = rememberPagerState()
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp
    val itemHorizontalMargin = 8.dp
    val itemWidth = screenWidth - itemHorizontalMargin * 2

    // Banner image URLs
    val banners = listOf(
        "https://img.freepik.com/free-psd/tropical-fruit-banner-template_23-2148892583.jpg?semt=ais_hybrid",
        "https://img.freepik.com/free-vector/hand-drawn-supermarket-sale-background_23-2149406388.jpg?semt=ais_hybrid",
        "https://img.freepik.com/premium-psd/healthy-food-vegetable-grocery-social-media-facebook-cover-design-web-banner-template_456977-205.jpg?semt=ais_hybrid"
    )

    HorizontalPager(
        state = pagerState,
        count = banners.size,
        modifier = Modifier
            .fillMaxWidth()
            .height(220.dp),
        verticalAlignment = Alignment.CenterVertically
    ) { page ->
        BannerImage(banners[page], itemWidth, itemHorizontalMargin)
    }
}

@Composable
fun BannerImage(imageUrl: String, itemWidth: Dp, itemHorizontalMargin: Dp) {
    Box(
        modifier = Modifier
            .width(itemWidth)
            .padding(horizontal = itemHorizontalMargin)
            .height(220.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        AsyncImage(
            model = imageUrl,
            contentDescription = "Banner Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}

@Composable
fun ProductImageSection(page: Int, itemWidth: Dp, itemHorizontalMargin: Dp) {
    Box(
        modifier = Modifier
            .width(itemWidth)
            .padding(horizontal = itemHorizontalMargin)
            .height(220.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.Gray),
        contentAlignment = Alignment.Center
    ) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text(
                text = "Page $page",
                color = Color.White,
                fontSize = 24.sp
            )
        }
    }
}

@Composable
fun TopBar(onCartClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Filled.Menu,
            contentDescription = "Menu Icon",
            tint = Color.Black
        )
        Text(
            text = buildAnnotatedString {
                appendWithStyle("Fresh", Color(0xff128819), 24.sp)
                appendWithStyle("ly", Color(0xff6fb103), 24.sp)
            },
            style = TextStyle(fontSize = 24.sp)
        )
        Icon(
            imageVector = Icons.Filled.ShoppingCart,
            contentDescription = "Cart Icon",
            modifier = Modifier.clickable { onCartClick() },
            tint = Color.Black
        )
    }
}

fun buildAnnotatedString(builder: AnnotatedString.Builder.() -> Unit): AnnotatedString {
    return with(AnnotatedString.Builder()) {
        builder()
        toAnnotatedString()
    }
}

fun AnnotatedString.Builder.appendWithStyle(text: String, color: Color, size: androidx.compose.ui.unit.TextUnit) {
    withStyle(
        style = SpanStyle(
            color = color,
            fontSize = size,
            fontWeight = FontWeight.Bold
        )
    ) {
        append(text)
    }
}

@Composable
fun SearchSection(
    modifier: Modifier = Modifier,
    searchQuery: String,
    onSearchChange: (String) -> Unit
) {
    Box(
        modifier = modifier
            .height(48.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .border(1.dp, Color(0xFF141414), RoundedCornerShape(8.dp))
            .clip(RoundedCornerShape(8.dp))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search Icon",
                tint = Color.Black,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))

            BasicTextField(
                value = searchQuery,
                onValueChange = { onSearchChange(it) },
                textStyle = TextStyle(
                    color = Color(0xFF201E1E),
                    fontSize = 14.sp
                ),
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Search an item",
                                color = Color(0xFF807D7D),
                                fontSize = 14.sp
                            )
                        }
                        innerTextField()
                    }
                }
            )
        }
    }
}

@Composable
fun SkeletonLoader() {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(16.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        items(6) {
            Box(
                modifier = Modifier
                    .padding(8.dp)
                    .size(150.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color.LightGray)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(80.dp)
                            .background(Color.Gray)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.6f)
                            .height(10.dp)
                            .background(Color.Gray)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(0.4f)
                            .height(10.dp)
                            .background(Color.Gray)
                    )
                }
            }
        }
    }
}

@Composable
fun ProductCategoriesSection(
    products: List<Product>,
    onProductClick: (Product) -> Unit
) {
    Column(modifier = Modifier.padding(horizontal = 16.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Canvas(modifier = Modifier.size(8.dp)) {
                drawCircle(
                    color = Color.Green,
                    radius = size.minDimension / 2,
                    center = center
                )
            }
            Text(
                text = "PRODUCTS FOR YOU",
                color = Color(0xFF141414),
                style = TextStyle(fontSize = 20.sp),
                modifier = Modifier.padding(start = 8.dp)
            )
        }

        val bottomNavBarHeight = 56.dp

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = bottomNavBarHeight + 16.dp),
            modifier = Modifier
                .padding(top = 8.dp)
                .fillMaxWidth()
        ) {
            items(products) { product ->
                ProductItem(product, onProductClick)
            }
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    onProductClick: (Product) -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(8.dp)
            .clickable { onProductClick(product) }
    ) {
        Box(
            modifier = Modifier
                .size(150.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(Color.LightGray)
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }
        Text(
            text = product.name,
            color = Color(0xFF201E1E),
            fontSize = 14.sp,
            modifier = Modifier.padding(top = 8.dp)
        )
        Text(
            text = "₱${product.price}",
            color = Color(0xFF201E1E),
            fontSize = 14.sp
        )
    }
}

@Composable
fun BottomNavigationBar(
    modifier: Modifier = Modifier,
    onProfileClick: () -> Unit,
    onMyPurchasesClick: () -> Unit
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .background(Color.Black)
            .padding(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                .background(Color.Black)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Filled.Receipt, // Updated to AutoMirrored
                contentDescription = "My Purchases Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onMyPurchasesClick() },
                tint = Color.White
            )
            Icon(
                imageVector = Icons.Filled.ShoppingCart,
                contentDescription = "Cart Icon",
                modifier = Modifier.size(30.dp),
                tint = Color.White
            )
            Icon(
                imageVector = Icons.Filled.Person,
                contentDescription = "Profile Icon",
                modifier = Modifier
                    .size(24.dp)
                    .clickable { onProfileClick() },
                tint = Color.White
            )
        }
    }
}
