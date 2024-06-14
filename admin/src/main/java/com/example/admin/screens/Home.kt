package com.example.admin.screens

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.admin.R
import com.example.admin.model.Order
import com.example.admin.screens.Category.AllCategory
import com.example.admin.screens.Menu.MenuRes
import com.example.admin.screens.Order.OrderCompleted
import com.example.admin.screens.Order.OrderManager
import com.example.admin.screens.Order.OrderPending
import com.example.admin.screens.Restaurant.AllRestaurant
import com.example.admin.screens.User.AllUser
import com.example.admin.ui.theme.bg
import com.example.admin.ui.theme.blue3
import com.example.admin.ui.theme.blue4
import com.example.admin.ui.theme.blue5
import com.example.admin.ui.theme.darkblue
import com.example.admin.ui.theme.menu
import com.example.admin.ui.theme.menu2
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

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun HomeAdminScreen(context: Context, navController: NavHostController) {
    val scrollState = rememberScrollState()
    val db = FirebaseFirestore.getInstance()
    val scope = rememberCoroutineScope()
    var productCount by remember { mutableStateOf(0) }
    var orderCount by remember { mutableStateOf(0) }
    var totalConfirmed by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        productCount = getPendingCount()
        orderCount = getCompletedCount()
        totalConfirmed = getTotalConfirmed()
    }



    Column(
        modifier = Modifier
            .fillMaxHeight()
            .fillMaxWidth()
            .background(color = bg)
            .padding(bottom = 25.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout {
            val (topImg, profile) = createRefs()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(245.dp)
                    .constrainAs(topImg) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
                    .background(
                        color = blue5,
                        shape = RoundedCornerShape(bottomEnd = 40.dp, bottomStart = 40.dp)
                    )
            )
            Row(
                modifier = Modifier
                    .padding(top = 48.dp, start = 24.dp, end = 24.dp)
                    .fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .height(100.dp)
                        .padding(start = 14.dp)
                        .weight(0.7f),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Center
                ) {
                    androidx.compose.material.Text(
                        text = "Hello",
                        color = Color.White,
                        fontSize = 18.sp
                    )
                    androidx.compose.material.Text(
                        text = "Admin OrderNow",
                        color = Color.White,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 14.dp)
                    )
                }
                Image(
                    painter = painterResource(id = R.drawable.profile),
                    contentDescription = "",
                    modifier = Modifier
                        .height(100.dp)
                        .width(100.dp)
                        .clip(CircleShape)
                        .border(border = BorderStroke(3.dp, menu), shape = CircleShape)
                        .clickable { }
                )
            }

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .shadow(3.dp, shape = RoundedCornerShape(20.dp))
                    .background(
                        color = Color.White,
                        shape = RoundedCornerShape(20.dp)
                    )
                    .constrainAs(profile) {
                        top.linkTo(topImg.bottom)
                        bottom.linkTo(topImg.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Surface(
                    onClick = {
                        context.startActivity(Intent(context, OrderPending::class.java))
                    },
                    color = blue4,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(5.dp)
                            .padding(top = 3.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(45.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                        ) {
                            androidx.compose.material.Icon(
                                painter = painterResource(id = R.drawable.ic_pending),
                                contentDescription = "",
                                tint = blue3,
                                modifier = Modifier
                                    .size(35.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            androidx.compose.material.Text(
                                text = "Pending",
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            androidx.compose.material.Text(
                                text = productCount.toString(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                    }
                }
                Surface(
                    onClick = {
                        context.startActivity(Intent(context, OrderCompleted::class.java))
                    },
                    color = blue4,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(5.dp)
                            .padding(top = 3.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(45.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                        ) {
                            androidx.compose.material.Icon(
                                painter = painterResource(id = R.drawable.ic_complete),
                                contentDescription = "",
                                tint = blue3,
                                modifier = Modifier
                                    .size(35.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            androidx.compose.material.Text(
                                text = "Completed",
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            androidx.compose.material.Text(
                                text =orderCount.toString(),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                    }
                }
                Surface(
                    color = blue4,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .padding(12.dp),
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .padding(5.dp)
                            .padding(top = 3.dp)
                    ) {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .size(45.dp)
                                .background(color = Color.White, shape = RoundedCornerShape(10.dp))
                        ) {
                            androidx.compose.material.Icon(
                                painter = painterResource(id = R.drawable.ic_money),
                                contentDescription = "",
                                tint = blue3,
                                modifier = Modifier
                                    .size(35.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(3.dp))
                        Column(
                            modifier = Modifier
                                .padding(horizontal = 5.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            androidx.compose.material.Text(
                                text = "Revenue",
                                fontSize = 15.sp,
                                color = Color.DarkGray
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            androidx.compose.material.Text(

                                text = formatCurrency(totalConfirmed),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Black
                            )
                        }

                    }
                }
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp, horizontal = 16.dp)
        ) {
            Surface(
                onClick = {
                    context.startActivity(Intent(context, AllCategory::class.java))
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(100.dp)
                    .height(110.dp)
                    .background(
                        color = blue5,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(bottom = 1.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(top = 8.dp)
                            .background(color = menu2, shape = RoundedCornerShape(10.dp))
                    ) {
                        androidx.compose.material.Icon(
                            painter = painterResource(id = R.drawable.ic_category),
                            contentDescription = "",
                            tint = darkblue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 5.dp, bottom = 10.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material.Text(
                            text = "Category Manager",
                            fontSize = 15.sp,
                            color = Color.DarkGray,
                            style = TextStyle(
                                textAlign = TextAlign.Center
                            ),
                            lineHeight = 20.sp,
                        )
                    }
                }
            }
            Surface(
                onClick = {
                    context.startActivity(Intent(context, AllRestaurant::class.java))
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(100.dp)
                    .height(110.dp)
                    .background(
                        color = blue5,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(bottom = 1.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(top = 8.dp)
                            .background(color = menu2, shape = RoundedCornerShape(10.dp))
                    ) {
                        androidx.compose.material.Icon(
                            painter = painterResource(id = R.drawable.ic_restaurant),
                            contentDescription = "",
                            tint = darkblue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 5.dp, bottom = 5.dp, start = 2.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material.Text(
                            text = "Restaurant Manager",
                            fontSize = 15.sp,
                            color = Color.DarkGray,
                            style = TextStyle(
                                textAlign = TextAlign.Center
                            ),
                            lineHeight = 20.sp,
                        )
                    }
                }
            }
            Surface(
                onClick = {
                    context.startActivity(Intent(context,MenuRes::class.java))
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(100.dp)
                    .height(110.dp)
                    .background(
                        color = blue5,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(bottom = 1.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(top = 8.dp)
                            .background(color = menu2, shape = RoundedCornerShape(10.dp))
                    ) {
                        androidx.compose.material.Icon(
                            painter = painterResource(id = R.drawable.ic_menu),
                            contentDescription = "",
                            tint = darkblue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 5.dp, bottom = 5.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material.Text(
                            text = "Menu Manger",
                            fontSize = 15.sp,
                            color = Color.DarkGray,
                            style = TextStyle(
                                textAlign = TextAlign.Center
                            ),
                            lineHeight = 20.sp,
                        )
                    }
                }
            }

        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceEvenly,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 25.dp, horizontal = 16.dp)
        ) {
            Surface(
                onClick = {
                    context.startActivity(Intent(context, OrderManager::class.java))
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(100.dp)
                    .height(110.dp)
                    .background(
                        color = blue5,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(bottom = 1.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(top = 8.dp)
                            .background(color = menu2, shape = RoundedCornerShape(10.dp))
                    ) {
                        androidx.compose.material.Icon(
                            painter = painterResource(id = R.drawable.ic_order1),
                            contentDescription = "",
                            tint = darkblue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 5.dp, bottom = 5.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material.Text(
                            text = "Order Manager",
                            fontSize = 15.sp,
                            color = Color.DarkGray,
                            style = TextStyle(
                                textAlign = TextAlign.Center
                            ),
                            lineHeight = 20.sp,
                        )
                    }
                }
            }

            Surface(
                onClick = {
                    context.startActivity(Intent(context, AllUser::class.java))
                },
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .width(100.dp)
                    .height(110.dp)
                    .background(
                        color = blue5,
                        shape = RoundedCornerShape(10.dp),
                    )
                    .padding(bottom = 1.dp),
                color = Color.White
            ) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(50.dp)
                            .padding(top = 8.dp)
                            .background(color = menu2, shape = RoundedCornerShape(10.dp))
                    ) {
                        androidx.compose.material.Icon(
                            painter = painterResource(id = R.drawable.ic_create),
                            contentDescription = "",
                            tint = darkblue,
                            modifier = Modifier
                                .size(35.dp)
                        )
                    }
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(top = 5.dp, bottom = 5.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        androidx.compose.material.Text(
                            text = "User Manger",
                            fontSize = 15.sp,
                            color = Color.DarkGray,
                            style = TextStyle(
                                textAlign = TextAlign.Center
                            ),
                            lineHeight = 20.sp,
                        )
                    }
                }
            }

        }
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                androidx.compose.material3.Icon(
                    painter = painterResource(id = R.drawable.ic_logout),
                    contentDescription = "",
                    tint = Color.Red,
                    modifier = Modifier.size(30.dp)
                )
            }
        }

    }
}
fun formatCurrency(amount: Int): String {
    val format = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return format.format(amount)
}
suspend fun getPendingCount(): Int {
    val db = FirebaseFirestore.getInstance()
    val userCollection = db.collection("orders").whereEqualTo("status", "Processing")
    val querySnapshot = userCollection.get().await()
    return querySnapshot.size().toInt()
}

suspend fun getCompletedCount(): Int {
    val db = FirebaseFirestore.getInstance()
    val userCollection = db.collection("orders").whereEqualTo("status", "Delivered")
    val querySnapshot = userCollection.get().await()
    return querySnapshot.size().toInt()
}

// Hàm để tính tổng total của các đơn hàng đã xác nhận
suspend fun getTotalConfirmed(): Int {
    val db = FirebaseFirestore.getInstance()
    val orderCollection = db.collection("orders")
    val querySnapshot = orderCollection.whereEqualTo("status", "Delivered").get().await()

    val revenue = querySnapshot.documents.sumBy { it.toObject<Order>()?.total ?: 0 }
    return revenue
}