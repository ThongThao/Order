package com.example.order.Screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Text
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.order.ui.theme.OrderTheme
import com.example.order.ui.theme.orange
import com.example.order.ui.theme.red
import com.example.order.viewmodels.UserViewModel

@Composable
fun ProfileScreen(userViewModel: UserViewModel = viewModel(),userId: String?) {
    val user by userViewModel.getUser(userId).observeAsState()
    user?.let {
        OrderTheme {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                Image(
                    modifier = Modifier
                        .fillMaxWidth()
                        .offset(0.dp, (-43).dp),
                    painter = painterResource(id = R.drawable.bg_profile),
                    contentDescription = "Header Background",
                    contentScale = ContentScale.FillWidth
                )

            }

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                ProfileImage(image = it.image.toString())
                it.fullName?.let {
                    Text(
                        text = it,
                        fontSize = 26.sp,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 25.dp)
                    )
                }
                Spacer(modifier = Modifier.height(30.dp))
                Box(
                    modifier = Modifier
                        .height(350.dp)
                        .width(350.dp)
                        .border(width = 1.dp, color = Color.Gray, shape = RoundedCornerShape(10.dp))
                        .background(Color.White, shape = RoundedCornerShape(10.dp))

                )
                {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(87.5.dp)
                            .clickable { /* Xử lý khi nút được nhấn */ },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_edit),
                            tint = orange,
                            contentDescription = null,
                            modifier = Modifier
                                .size(25.dp)
                                .padding(start = 5.dp)
                        )

                        Text(
                            text = "Chỉnh sửa thông tin",
                            fontSize = 20.sp,
                            color = Color.Black,
                        )
                        Spacer(modifier = Modifier.width(45.dp))
                        IconButton(onClick = {
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_forward),
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = 87.5.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.LightGray),

                        ) { }
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(175.dp)
                            .padding(top = 87.5.dp)
                            .clickable { /* Xử lý khi nút được nhấn */ },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_pass),
                            tint = orange,
                            contentDescription = null,
                            modifier = Modifier
                                .size(27.dp)
                        )

                        Text(
                            text = "Đổi mật khẩu",
                            fontSize = 20.sp,
                            color = Color.Black,
                        )
                        Spacer(modifier = Modifier.width(95.dp))
                        IconButton(onClick = {
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_forward),
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }

                    Box(
                        modifier = Modifier
                            .padding(top = 175.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.LightGray)
                    ) {}
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(262.5.dp)
                            .padding(top = 175.dp)
                            .clickable { /* Xử lý khi nút được nhấn */ },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_history),
                            tint = orange,
                            contentDescription = null,
                            modifier = Modifier
                                .size(27.dp)
                        )

                        Text(
                            text = "Lịch sử đơn hàng",
                            fontSize = 20.sp,
                            color = Color.Black,
                        )
                        Spacer(modifier = Modifier.width(55.dp))
                        IconButton(onClick = {
                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_forward),
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                    Box(
                        modifier = Modifier
                            .padding(top = 262.5.dp)
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(Color.LightGray)
                    ) {}
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(350.dp)
                            .padding(top = 262.5.dp)
                            .clickable {

                            },
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Spacer(modifier = Modifier.width(5.dp))
                        Icon(
                            painter = painterResource(id = R.drawable.ic_logout),
                            tint = red,
                            contentDescription = null,
                            modifier = Modifier
                                .size(27.dp)
                        )

                        Text(
                            text = "Đăng xuất",
                            fontSize = 20.sp,
                            color = red,
                        )
                        Spacer(modifier = Modifier.width(116.dp))
                        IconButton(onClick = {


                        }) {
                            Icon(
                                painter = painterResource(id = R.drawable.ic_forward),
                                contentDescription = null,
                                tint = Color.Gray,
                                modifier = Modifier.size(20.dp)
                            )
                        }
                    }
                }

            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .verticalScroll(rememberScrollState())
                    .padding(top = 190.dp, start = 215.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(35.dp)
                        .background(Color.White, shape = CircleShape)

                ) {
                    IconButton(
                        onClick = { /* Xử lý sự kiện khi nút được nhấn */ },
                        modifier = Modifier.align(Alignment.Center),
                    ) {
                        Icon(
                            modifier = Modifier.size(15.dp),
                            painter = painterResource(id = R.drawable.ic_edit),
                            contentDescription = "Edit Icon",
                            tint = Color.Black // Màu của biểu tượng chỉnh sửa
                        )
                    }
                }
            }


        }
    }
}

@Composable
fun ProfileImage(modifier: Modifier = Modifier, image: String) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 100.dp)
    ) {
        val placeholderImage = painterResource(id = R.drawable.ic_profile) // Thay thế bằng ID của ảnh mặc định của bạn

        val imagePainter = if (image.isNullOrEmpty()) {
            placeholderImage
        } else {
            rememberAsyncImagePainter(model = image)
        }
        Surface(
            shape = CircleShape,
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.BottomCenter) // Căn giữa hình ảnh
                .border(3.dp, Color.White, shape = CircleShape)) {
            Image(
                painter = imagePainter,
                contentDescription = null, // Thêm miêu tả nếu cần thiết
                contentScale = ContentScale.Crop,

            )
        }
    }
}