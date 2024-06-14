package com.example.admin.screens.Order

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.CheckCircle
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.admin.R
import com.example.admin.screens.Home
import com.example.admin.ui.theme.OrderTheme
import com.example.admin.ui.theme.blue
import com.example.admin.ui.theme.darkblue
import com.example.admin.ui.theme.delete
import com.example.admin.ui.theme.green
import com.example.admin.ui.theme.green2
import com.example.admin.ui.theme.red

class OrderManager : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            OrderTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
OrderScreen(LocalContext.current)
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun OrderScreen(context:Context){
    Column(
        modifier = Modifier
            .fillMaxWidth(),
    ) {
        CenterAlignedTopAppBar(
            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                containerColor = blue,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White,
                actionIconContentColor = Color.White
            ),
            title = {
                Text(
                    text = "Order Manager",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Bold
                )
            },
            navigationIcon = {
                IconButton(onClick = {
                    context.startActivity(Intent(context, Home::class.java))
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "")
                }
            },
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ){
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 5.dp, start = 20.dp, end = 20.dp)
                    .height(55.dp)
            ) {
                androidx.compose.material.Surface(
                    onClick = {
                        context.startActivity(Intent(context, OrderPending::class.java))
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFF3B95F)),
                    elevation = 3.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Rounded.Refresh,
                                contentDescription = "",
                                tint = Color(0xFFF3B95F),
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 5.dp)
                                    .clickable { }
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Processing",
                                color = Color(0xFFFF8911),
                                fontSize = 21.sp
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Rounded.KeyboardArrowRight,
                                contentDescription = "",
                                tint = Color(0xFFF3B95F),
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .clickable { })
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 5.dp, start = 20.dp, end = 20.dp)
                    .height(55.dp)
            ) {
                androidx.compose.material.Surface(
                    onClick = {
                        context.startActivity(Intent(context, OrderShipping::class.java))
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, blue),
                    elevation = 3.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_order1),
                                contentDescription = "",
                                tint = darkblue,
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 5.dp)
                                    .clickable { }
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Shipping",
                                color = darkblue,
                                fontSize = 21.sp
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Rounded.KeyboardArrowRight,
                                contentDescription = "",
                                tint = blue,
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .clickable { })
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 5.dp, start = 20.dp, end = 20.dp)
                    .height(55.dp)
            ) {
                androidx.compose.material.Surface(
                    onClick = {
                        context.startActivity(Intent(context, OrderCancelled::class.java))
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, delete),
                    elevation = 3.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.cancelled),
                                contentDescription = "",
                                tint = red,
                                modifier = Modifier
                                    .size(31.dp)
                                    .padding(start = 10.dp, end = 5.dp)
                                    .clickable { }
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Cancelled",
                                color = red,
                                fontSize = 21.sp
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(imageVector = Icons.Rounded.KeyboardArrowRight,
                                contentDescription = "",
                                tint = delete,
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .clickable { })
                        }
                    }
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp, bottom = 5.dp, start = 20.dp, end = 20.dp)
                    .height(55.dp)
            ) {
                androidx.compose.material.Surface(
                    onClick = {
                        context.startActivity(Intent(context, OrderCompleted::class.java))
                    },
                    shape = RoundedCornerShape(12.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, green2),
                    elevation = 3.dp
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.CheckCircle,
                                contentDescription = "",
                                tint = green2,
                                modifier = Modifier
                                    .padding(start = 10.dp, end = 5.dp)
                                    .clickable { }
                            )
                        }
                        Column(
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .weight(1f)
                        ) {
                            Text(
                                text = "Completed",
                                color = green,
                                fontSize = 21.sp
                            )
                        }
                        Column(
                            modifier = Modifier
                                .fillMaxHeight(),
                            verticalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.KeyboardArrowRight,
                                contentDescription = "",
                                tint = green2,
                                modifier = Modifier
                                    .padding(end = 5.dp)
                                    .clickable { }
                            )
                        }
                    }
                }
            }
        }

    }
}