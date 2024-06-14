package com.example.order.Screens

import CartDetail
import CartScreen
import OrderDetail
import SignInScreen
import android.annotation.SuppressLint
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.FabPosition
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.FloatingActionButtonDefaults
import androidx.compose.material.Icon
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.app.R
import com.example.order.navigation.Screen
import com.example.order.ui.theme.metropolisFontFamily
import com.example.order.ui.theme.orange
import com.example.order.ui.theme.placeholderColor
import com.example.order.viewmodels.HomeViewModel
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController

enum class HomeScreens {
    LIST,
    CART,
    HOME,
    PROFILE,
    MORE,
}

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeNavigation(userId: String?) {
    val selectedItem = remember { mutableStateOf(HomeScreens.HOME) }
    val navController = rememberAnimatedNavController()
    Box(modifier = Modifier.fillMaxSize()) {
        //-> Main Container ~90% of the screen
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.93f)
        ) {
            HomeNavHost(navHostController = navController, userId)

            //-> Navigate when the selected item changes..
            navController.popBackStack()
            navController.navigate(route = selectedItem.value.name)
        }

        //-> Bottom Bar ~10% of the screen
        Scaffold(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.12f)
                .align(Alignment.BottomCenter),
            floatingActionButton = {
                FloatingActionButton(
                    modifier = Modifier.size(70.dp),
                    backgroundColor = if (selectedItem.value == HomeScreens.HOME) orange else placeholderColor,
                    onClick = { selectedItem.value = HomeScreens.HOME },
                    elevation = FloatingActionButtonDefaults.elevation(
                        defaultElevation = 4.dp,
                        pressedElevation = 8.dp
                    )
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.ic_home),
                        contentDescription = "Home",
                        tint = Color.White
                    )
                }
            },
            backgroundColor = Color.Transparent,
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,
            bottomBar = {
                BottomAppBar(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.592f),
                    cutoutShape = RoundedCornerShape(50),
                    backgroundColor = Color.White,
                    elevation = 20.dp,
                    contentPadding = PaddingValues(all = 1.dp)
                ) {
                    Row(
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BottomNavigationItem(selected = selectedItem.value == HomeScreens.LIST,
                            onClick = { selectedItem.value = HomeScreens.LIST },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_menu),
                                    contentDescription = "Đơn hàng",
                                    tint = if (selectedItem.value == HomeScreens.LIST) orange else placeholderColor
                                )
                            },
                        )
                        BottomNavigationItem(selected = selectedItem.value == HomeScreens.CART,
                            onClick = { selectedItem.value = HomeScreens.CART },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_cart),
                                    contentDescription = "Thông báo",
                                    tint = if (selectedItem.value == HomeScreens.CART) orange else placeholderColor
                                )
                            },
                            modifier = Modifier.padding(end = 35.dp)
                        )
                        BottomNavigationItem(selected = selectedItem.value == HomeScreens.MORE,
                            onClick = { selectedItem.value = HomeScreens.MORE },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_favorite),
                                    contentDescription = "more",
                                    tint = if (selectedItem.value == HomeScreens.MORE) orange else placeholderColor
                                )
                            },
                            modifier = Modifier.padding(start = 35.dp)
                        )
                        BottomNavigationItem(selected = selectedItem.value == HomeScreens.PROFILE,
                            onClick = { selectedItem.value = HomeScreens.PROFILE },
                            icon = {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_profile),
                                    contentDescription = "profile",
                                    tint = if (selectedItem.value == HomeScreens.PROFILE) orange else placeholderColor
                                )
                            },


                            )


                    }
                }
            }
        ) { }
    }
}

private fun navItemTextStyle(isSelected: Boolean): TextStyle {
    return TextStyle(
        color = if (isSelected) orange else placeholderColor,
        fontSize = 12.sp,
        fontFamily = metropolisFontFamily,
        fontWeight = FontWeight.Medium
    )
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun HomeNavHost(navHostController: NavHostController, userId: String?) {
    AnimatedNavHost(navController = navHostController, startDestination = HomeScreens.HOME.name) {

        composable(
            route = HomeScreens.HOME.name,
        ) {
            val homeViewModel: HomeViewModel = viewModel()
            HomeScreen(homeViewModel,
                navToRes = {
                    navHostController.navigate(Screen.Restaurant.createRoute(userId ?: ""))
                },
                navToCart={navHostController.navigate(Screen.Cart.createRoute(userId ?: ""))},
                navToChangeAddress = {
                    navHostController.navigate(Screen.ChangeAddress.createRoute(userId ?: ""))
                },
                navController = navHostController,userId = userId)



        }

        composable(
            route = HomeScreens.LIST.name,  ){
           OrderScreen(userId, navController = navHostController)
        }

        composable(
            route = HomeScreens.CART.name,
        ) {
            CartScreen(userId ,navController = navHostController)
        }

        composable(
            route = HomeScreens.PROFILE.name,
        ) {
            ProfileScreen(userId = userId, navController = navHostController, LocalContext.current)
        }
        composable(
            "profile/{userId}",
            arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId") ?: return@composable
            EditProfileScreen(userId,navController=navHostController,LocalContext.current)
        }
        composable(route = Screen.Login.route) {
            SignInScreen(
                navToHome = { user ->
                    navHostController.navigate(Screen.Home.createRoute(user.id ?: ""))
                },
                navToSignUp = {
                    navHostController.navigate(Screen.Register.route)
                }
            )
        }
        composable(
            route = HomeScreens.MORE.name,
        ) {
            userId?.let { it1 -> FavoriteScreen(userId = it1,navController =navHostController) }
        }

        composable(
            route = "MoreDetailsScreen/{content}",
        ) {
            val homeViewModel: HomeViewModel = viewModel()
            HomeScreen(homeViewModel,
                navToRes = {
                    navHostController.navigate(Screen.Restaurant.createRoute(userId ?: ""))
                },
                navToCart={},
                navToChangeAddress = {
                    navHostController.navigate(Screen.ChangeAddress.createRoute(userId ?: ""))
                },
                navController = navHostController,userId = userId)

        }

        composable(route = Screen.Restaurant.route) {
            // Instantiate HomeViewModel using viewModel()
            RestaurantScreen(navController = navHostController, onBackClick = {
                navHostController.navigate(HomeScreens.HOME.name)
            })
        }

        composable("restaurantDetail/{restaurantId}") { backStackEntry ->
            val restaurantId = backStackEntry.arguments?.getString("restaurantId")
            RestaurantDetailScreen(restaurantId, onBackClick = {
                navHostController.navigate(Screen.Restaurant.createRoute(userId ?: ""))
            }, navController = navHostController, userId = userId)
        }

        composable(Screen.ChangeAddress.route) {
            ChangeAddressScreen(onBackClick = {
                navHostController.navigate(HomeScreens.HOME.name)
            })
        }
        composable(route = Screen.Cart.route,
            arguments = listOf(navArgument("userId") { type = NavType.StringType })) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            CartScreen(userId ,navController = navHostController)
        }
        composable(route = "cart_detail/{userId}/{restaurantName}",
            arguments = listOf(navArgument("userId"){ type = NavType.StringType }, navArgument("restaurantName"){ type = NavType.StringType })) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            val restaurantName = backStackEntry.arguments?.getString("restaurantName")
            CartDetail(userId,restaurantName,navController = navHostController)
        }
        composable(
            "order_detail/{userId}/{orderId}",
            arguments = listOf(
                navArgument("userId") { type = NavType.StringType },
                navArgument("orderId") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            val orderId = backStackEntry.arguments?.getString("orderId") ?: return@composable
            OrderDetail(
                navController = navHostController,
                 orderId,
                userId,

            )
        }
        composable(Screen.OrderHistory.route,arguments = listOf(navArgument("userId") { type = NavType.StringType })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            OrderHistory(userId!!,navController = navHostController)
        }
        composable(route = "rating/{userId}/{restaurantName}",
            arguments = listOf(navArgument("userId"){ type = NavType.StringType }, navArgument("restaurantName"){ type = NavType.StringType })) { backStackEntry ->
            val userId = backStackEntry.arguments?.getString("userId")
            val restaurantName = backStackEntry.arguments?.getString("restaurantName")
            RatingAndComment(userId,restaurantName,navController = navHostController)
        }
    }
}
