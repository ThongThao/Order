package com.example.order.Welcome

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.app.R
import com.example.order.BorderButton
import com.example.order.FilledButton
import com.example.order.model.PageItem
import com.example.order.ui.theme.OrderTheme
import com.example.order.ui.theme.metropolisFontFamily
import com.example.order.ui.theme.orange
import com.example.order.ui.theme.primaryFontColor
import com.example.order.ui.theme.secondaryFontColor
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch


@OptIn( ExperimentalFoundationApi::class, ExperimentalPagerApi::class)
@Composable
fun PageViewScreen(navToWelcom: () -> Unit){
    val pagerState = rememberPagerState()
    val scope = rememberCoroutineScope()
    val list = arrayOf(
        PageItem(
        image = R.drawable.ic_page_1,
        title = "Tìm món ăn bạn yêu thích",
        subTitle = "Khám phá những món ăn ngon nhất từ hơn 1.000 nhà hàng và giao hàng nhanh chóng đến tận nhà bạn"
    ), PageItem(
        image = R.drawable.ic_page_2,
        title = "Giao hàng nhanh",
        subTitle = "Giao đồ ăn nhanh đến tận nhà, văn phòng mọi lúc mọi nơi"
    ), PageItem(
        image = R.drawable.ic_page_3,
        title = "Theo dõi trực tiếp",
        subTitle = "Theo dõi đơn hàng của bạn trên ứng dụng sau khi bạn đặt hàng"
    ))
    OrderTheme {
        HorizontalPager(
            count = list.size,
            state = pagerState
        ) { index ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Image(painter = painterResource(id = list[index].image), contentDescription = null)
                Spacer(modifier = Modifier.height(30.dp))
                Indicator(count = list.size, index = index)
                Spacer(modifier = Modifier.height(35.dp))
                Text(
                    text = list[index].title,
                    style = TextStyle(
                        fontSize = 28.sp,
                        fontFamily = metropolisFontFamily,
                        color = primaryFontColor
                    )
                )
                Spacer(modifier = Modifier.height(33.dp))
                Text(
                    text = list[index].subTitle,
                    style = TextStyle(
                        fontSize = 13.sp,
                        fontFamily = metropolisFontFamily,
                        color = secondaryFontColor,
                        textAlign = TextAlign.Center
                    ),
                    modifier = Modifier.padding(horizontal = 45.dp)
                )
                Spacer(modifier = Modifier.height(40.dp))
                FilledButton(modifier = Modifier.padding(horizontal = 34.dp), text = "Tiếp") {
                    scope.launch {
                        if (index < list.size - 1) {
                            pagerState.animateScrollToPage(index + 1)
                        } else {
                            navToWelcom()

                        }
                    }
                }
                Spacer(modifier = Modifier.height(20.dp))

                BorderButton(modifier = Modifier.padding(horizontal = 34.dp), text = "Bỏ qua", color = secondaryFontColor) {
                    navToWelcom()
                }
                Spacer(modifier = Modifier.height(20.dp))
            }
        }
    }
}
@Composable
fun Indicator(count: Int, index: Int) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(10.dp),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 0 until count) {
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .clip(CircleShape)
                    .background(if (i == index) orange else Color.LightGray)
            )
            Spacer(modifier = Modifier.size(5.dp))
        }
    }
}
