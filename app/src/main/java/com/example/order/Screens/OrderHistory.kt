package com.example.order.Screens

import OrderViewModel
import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.DateRange
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.order.model.Order
import com.example.order.ui.theme.Shapes
import com.example.order.ui.theme.blackcart
import com.example.order.ui.theme.grayFont
import com.example.order.ui.theme.green
import com.example.order.ui.theme.menu
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderHistory(
    userId:String?,
    orderViewModel: OrderViewModel = viewModel(),
    navController: NavHostController
) {
    val orders1 by orderViewModel.orders1.collectAsState()
    LaunchedEffect(userId) {
        userId?.let { orderViewModel.fetchOrdersForUser1(it) }
    }

    androidx.compose.material.Scaffold(
        scaffoldState = rememberScaffoldState(),
        floatingActionButtonPosition = FabPosition.Center,
        isFloatingActionButtonDocked = true,
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black,
                    navigationIconContentColor = Color.Black,
                    actionIconContentColor = Color.Black
                ),
                title = {
                    Text(
                        text = "Lịch sử",
                        fontSize = 24.sp,
                        overflow = TextOverflow.Ellipsis,
                        fontWeight = FontWeight.Bold
                    )
                },
                navigationIcon = {
                    Button(
                        onClick = { navController.popBackStack()},
                        contentPadding = PaddingValues(),
                        shape = Shapes.small,
                        colors = ButtonDefaults.buttonColors(
                            backgroundColor = Color.White,
                            contentColor = Color.Black
                        ),
                        //                    elevation = 5.dp,
                        modifier = Modifier
                            .width(38.dp)
                            .height(38.dp)
                    ) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription ="" )
                    }
                },

                )
        },

        content = { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(12.dp)
            ) {
                orders1.let {
                    allOrder1(userId ,order = orders1, navController)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun allOrder1(userId: String?, order: List<Order>, navController: NavHostController){
    val scrollState = rememberLazyListState()
    LazyColumn(
        state = scrollState
    ) {
        items(order) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp, horizontal = 5.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.DateRange,
                        contentDescription = null,
                        modifier = Modifier.height(20.dp)
                    )
                    Spacer(modifier = Modifier.width(5.dp))
                    it.date?. let { date ->
                        val formattedDate = SimpleDateFormat(
                            "dd/MM/yyyy HH:mm",
                            Locale("vi", "VN")
                        ).format(date)
                        Text(
                            text = formattedDate,
                            fontWeight = FontWeight.Medium,
                            fontSize = 15.sp,
                            color = blackcart
                        )
                    }
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                ) {
                    Surface(
                        onClick = {
                            navController.navigate("order_detail/${userId!!}/${it?.id!!}")
                            Log.e("order:","${it.id}")
                        },
                        shape = Shapes.medium,
                        color = Color.White,
                        border = BorderStroke(1.dp, menu),
                        elevation = 1.dp
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .padding(6.dp)
                                    .weight(1f)
                            ) {
                                Text(
                                    text = "${it.restaurant}",
                                    color = Color.DarkGray,
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .padding(bottom = 6.dp)
                                )
                                Text(
                                    text = "x${it.items?.size} món",
                                    color = Color.DarkGray,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    modifier = Modifier
                                        .padding(bottom = 6.dp)
                                )
                                it.total.let {
                                    val formattedPrice = NumberFormat.getCurrencyInstance(
                                        Locale("vi", "VN")
                                    ).format(it)
                                    Text(
                                        text = "Tổng tiền: "+ formattedPrice,
                                        color = Color.DarkGray,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        modifier = Modifier
                                            .padding(bottom = 6.dp)
                                    )
                                }
                                it.status.let {
                                    Text(
                                        text = getStatusText(it),
                                        color = green,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                it.date?. let { date ->
                                    val formattedDate = SimpleDateFormat(
                                        "HH:mm",
                                        Locale.getDefault()
                                    ).format(date)
                                    Text(
                                        text = formattedDate,
                                        color = grayFont,
                                        fontSize = 18.sp,
                                    )
                                }
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxHeight(),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Icon(imageVector = Icons.Rounded.KeyboardArrowRight,
                                    contentDescription = "",
                                    tint = grayFont,
                                    modifier = Modifier
                                        .padding(end = 5.dp)
                                        .clickable { })
                            }
                        }
                    }
                }
            }
        }
    }

}