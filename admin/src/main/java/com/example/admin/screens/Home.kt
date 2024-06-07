package com.example.admin.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.admin.R
import com.example.admin.Screens
import com.example.admin.model.Order
import com.example.admin.screens.Category.AllCategory
import com.example.admin.screens.Menu.MenuRes
import com.example.admin.screens.Order.OrderManager
import com.example.admin.screens.Restaurant.AllRestaurant
import com.example.admin.screens.User.AllUser
import com.example.admin.ui.theme.blue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.tasks.await
import java.text.NumberFormat
import java.util.Locale

class Home : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            HomeAdminScreen(LocalContext.current, navController = rememberNavController())
        }
    }
}

@Composable
fun HomeAdminScreen(context: Context, navController: NavHostController) {
    val scrollState = rememberScrollState()
    val db = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()
    val (pendingOrderCount, setPendingOrderCount) = remember { mutableStateOf(0) }
    val (completedOrderCount, setCompletedOrderCount) = remember { mutableStateOf(0) }
    val (totalRevenue, setTotalRevenue) = remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        val pendingOrders = db.collection("orders")
            .whereEqualTo("status", "Processing")
            .get()
            .await()

        val completedOrders = db.collection("orders")
            .whereEqualTo("status", "Delivered")
            .get()
            .await()

        val revenue = completedOrders.documents.sumBy { it.toObject<Order>()?.total ?: 0 }

        setPendingOrderCount(pendingOrders.size())
        setCompletedOrderCount(completedOrders.size())
        setTotalRevenue(revenue)
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(start = 10.dp, end = 10.dp),
        contentAlignment = Alignment.BottomCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Header()
            Spacer(modifier = Modifier.height(30.dp))
            StatusCardLayout(pendingOrderCount, completedOrderCount, totalRevenue)
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                EachCardLayout(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(5.dp)
                        .weight(0.5f),
                    cardName = "Category",
                    cardName1 = "Manager",
                    cardIcon = painterResource(id = R.drawable.ic_category)
                ) {
                    context.startActivity(Intent(context, AllCategory::class.java))
                }

                EachCardLayout(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(5.dp)
                        .weight(0.5f),
                    cardName = "Restaurant",
                    cardName1 = "Manager",
                    cardIcon = painterResource(id = R.drawable.ic_restaurant)
                ) {
                    context.startActivity(Intent(context, AllRestaurant::class.java))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                EachCardLayout(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(5.dp)
                        .weight(0.5f),
                    cardName = "Items Menu",
                    cardName1 = "Manager",
                    cardIcon = painterResource(id = R.drawable.ic_menu)
                ) { context.startActivity(Intent(context, MenuRes::class.java)) }

                EachCardLayout(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(5.dp)
                        .weight(0.5f),
                    cardName = "Order",
                    cardName1 = "Manager",
                    cardIcon = painterResource(id = R.drawable.ic_order)
                ) {
                    context.startActivity(Intent(context, OrderManager::class.java))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                EachCardLayout(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(5.dp)
                        .weight(0.5f),
                    cardName = "Profile",
                    cardName1 = "Manager",
                    cardIcon = painterResource(id = R.drawable.ic_profile)
                ) {

                }
                EachCardLayout(
                    modifier = Modifier
                        .align(Alignment.CenterVertically)
                        .padding(5.dp)
                        .weight(0.5f),
                    cardName = "User",
                    cardName1 = "Manager",
                    cardIcon = painterResource(id = R.drawable.ic_create)
                ) {
                    context.startActivity(Intent(context, AllUser::class.java))
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Card(
                    modifier = Modifier
                        .clickable { navController.navigate(Screens.Login) }
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(1.dp)
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logout),
                            contentDescription = "",
                            tint = Color.Red,
                            modifier = Modifier.size(30.dp)
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
        }

    }

}

@Composable
fun Header() {
    Row(
        modifier = Modifier
            .padding(top = 10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            painter = painterResource(id = R.drawable.logo1),
            contentDescription = "",
            tint = blue,
            modifier = Modifier.size(40.dp)
        )

        Text(
            text = "OrderNow", style = TextStyle(
                fontSize = 30.sp,
                fontWeight = FontWeight.Bold,
            )
        )
        Icon(
            painter = painterResource(id = R.drawable.logo1),
            contentDescription = "",
            tint = blue,
            modifier = Modifier.size(40.dp)
        )
    }
}

@Composable
fun StatusCardLayout(pendingOrderCount: Int, completedOrderCount: Int, totalRevenue: Int) {
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CardColumnContent(
                modifier = Modifier.weight(0.33f),
                icon = painterResource(id = R.drawable.ic_pending),
                centerText = "Pending Order",
                bottomText = pendingOrderCount.toString()
            ) {

            }
            CardColumnContent(
                modifier = Modifier.weight(0.33f),
                icon = painterResource(id = R.drawable.ic_complete),
                centerText = "Completed Order",
                bottomText = completedOrderCount.toString()
            ) {

            }
            CardColumnContent(
                modifier = Modifier.weight(0.33f),
                icon = painterResource(id = R.drawable.ic_money),
                centerText = "Revenue",
                bottomText = formatCurrency(totalRevenue)
            ) {

            }
        }
    }
}

fun formatCurrency(amount: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return format.format(amount)
}


@Composable
fun CardColumnContent(
    modifier: Modifier,
    icon: Painter,
    centerText: String,
    bottomText: String,
    onClick: () -> Unit = {}
) {
    Column(
        modifier = modifier
            .padding(15.dp)
            .width(100.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Icon(
            modifier = Modifier.size(50.dp),
            painter = icon,
            contentDescription = "",
            tint = blue
        )
        Box(modifier = Modifier.height(45.dp)) {
            Text(
                text = centerText, style = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Normal,
                    textAlign = TextAlign.Center
                )
            )
        }
        Spacer(modifier = Modifier.height(5.dp))
        Text(
            text = bottomText, style = TextStyle(
                fontSize = 20.sp,
                fontWeight = FontWeight.SemiBold,
            )
        )
    }
}

@Composable
fun EachCardLayout(
    modifier: Modifier = Modifier,
    cardName: String,
    cardName1: String,
    cardIcon: Painter,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = modifier
            .clickable { onClick() }
            .width(200.dp)
            .height(150.dp),
        shape = RoundedCornerShape(16.dp), elevation = CardDefaults.cardElevation(1.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Icon(
                painter = cardIcon,
                contentDescription = "",
                tint = blue,
                modifier = Modifier.size(30.dp)
            )
            Text(
                text = cardName, style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    color = blue
                )
            )
            Text(
                text = cardName1, style = TextStyle(
                    fontSize = 16.sp,
                    fontWeight = FontWeight.W400,
                    color = blue
                )
            )
        }
    }
}
