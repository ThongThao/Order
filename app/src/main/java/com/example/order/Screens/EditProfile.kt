package com.example.order.Screens

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FabPosition
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.Icons.Default
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Phone
import androidx.compose.material.icons.rounded.Place
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.order.ui.theme.Shapes
import com.example.order.ui.theme.bg
import com.example.order.ui.theme.delete
import com.example.order.ui.theme.grayFont
import com.example.order.ui.theme.green
import com.example.order.ui.theme.menu
import com.example.order.viewmodels.UserViewModel
import java.util.Calendar

@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@Composable
fun EditProfileScreen(userId: String?,
                      navController: NavHostController, context: Context,
                      userViewModel: UserViewModel = viewModel(),) {
    val user by userViewModel.getUser(userId).observeAsState()
    user?.let {
        var name by remember { mutableStateOf(user!!.fullName) }
        var address by remember { mutableStateOf(user?.address ?: "") }
        var phone by remember { mutableStateOf(user!!.phoneNumber) }
        var birthdayString by remember { mutableStateOf(user?.birthday?.toString() ?: "") }
        var isFocused by remember { mutableStateOf(false) }
        val calendar = Calendar.getInstance()
        Scaffold(
            scaffoldState = rememberScaffoldState(),
            floatingActionButtonPosition = FabPosition.Center,
            isFloatingActionButtonDocked = true,

            topBar = {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = Color.White)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(70.dp)
                            .padding(horizontal = 15.dp)
                            .background(color = Color.White)
                    ) {
                        CenterAlignedTopAppBar(
                            colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                                containerColor = Color.White,
                                titleContentColor = Color.Black,
                                navigationIconContentColor = Color.Black,
                                actionIconContentColor = Color.Black
                            ),
                            title = {
                                Text(
                                    text = "Thông tin ",
                                    maxLines = 1,
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
                                    Icon(imageVector = Default.ArrowBack, contentDescription ="" )
                                }
                            },
                        )
                    }
                }
            },

            content = {
                val scrollState = rememberLazyListState()

                LazyColumn(
                    state = scrollState,
                    modifier = Modifier
                        .fillMaxSize()
                        .background(color = bg)
                ) {
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxHeight()
                                .fillMaxWidth()
                                .background(color = bg),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Spacer(modifier = Modifier.height(30.dp))
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                                    .height(60.dp)
                            ) {
                                OutlinedTextField(
                                    value = name!!,
                                    onValueChange = { name =it},
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Rounded.Person,
                                            contentDescription = "",
                                            tint = grayFont
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                    singleLine = true,
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        containerColor = Color.White,
                                        focusedBorderColor = menu,
                                        unfocusedBorderColor = menu
                                    ),
                                    label = {
                                        Text(
                                            text = "Tên",
                                            color = grayFont
                                        )
                                    },
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                                    .height(60.dp)
                            ) {
                                OutlinedTextField(
                                    value = phone!!,
                                    onValueChange = {phone=it },
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Rounded.Phone,
                                            contentDescription = "",
                                            tint = grayFont
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                    singleLine = true,
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        containerColor = Color.White,
                                        focusedBorderColor = menu,
                                        unfocusedBorderColor = menu
                                    ),
                                    label = {
                                        Text(
                                            text = "Số điện thoại",
                                            color = grayFont
                                        )
                                    },
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                                    .height(60.dp)
                            ) {
                                OutlinedTextField(
                                    value = address!!,
                                    onValueChange = { address=it},
                                    modifier = Modifier
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp),
                                    leadingIcon = {
                                        Icon(
                                            imageVector = Icons.Rounded.Place,
                                            contentDescription = "",
                                            tint = grayFont
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                    singleLine = true,
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        containerColor = Color.White,
                                        focusedBorderColor = menu,
                                        unfocusedBorderColor = menu
                                    ),
                                    label = {
                                        Text(
                                            text = "Địa chỉ",
                                            color = grayFont
                                        )
                                    },
                                )
                            }
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 10.dp, start = 25.dp, end = 25.dp)
                                    .height(60.dp)
                            ) {
                                OutlinedTextField(
                                    value = birthdayString,
                                    onValueChange = {birthdayString=it },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .onFocusChanged { focusState ->
                                            isFocused = focusState.isFocused
                                        },
                                    shape = RoundedCornerShape(12.dp),
                                    trailingIcon = {
                                        IconButton(
                                            onClick = {
                                                showDatePickerDialog(
                                                    context,
                                                    calendar
                                                ) { year, month, day ->
                                                    val formattedBirthday = "$day/${month + 1}/$year"
                                                    birthdayString = formattedBirthday
                                                }
                                            }
                                        ) {
                                            Icon(
                                                imageVector = Default.DateRange,
                                                contentDescription = "Pick Date",
                                                modifier = Modifier.size(24.dp)
                                            )
                                        }
                                    },
                                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                    singleLine = true,
                                    colors = TextFieldDefaults.outlinedTextFieldColors(
                                        containerColor = Color.White,
                                        focusedBorderColor = menu,
                                        unfocusedBorderColor = menu
                                    ),
                                    label = {
                                        Text(
                                            text = "Ngày sinh",
                                            color = grayFont
                                        )
                                    },
                                )
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(top = 35.dp, bottom = 5.dp, start = 25.dp, end = 25.dp)
                                    .height(60.dp),
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Button(
                                    onClick = {
                                        user?.let {
                                            val updatedUser = it.copy(
                                                fullName = name,
                                                phoneNumber = phone,
                                                address = address,
                                                birthday = birthdayString
                                            )
                                            userViewModel.updateUser(userId, updatedUser,
                                                onSuccess = {
                                                    navController.popBackStack()
                                                },
                                                onFailure = { e ->
                                                }
                                            )
                                        }
                                    },
                                    modifier = Modifier
                                        .height(48.dp),
                                    shape = Shapes.medium,
                                    elevation = androidx.compose.material3.ButtonDefaults.buttonElevation(
                                        defaultElevation = 5.dp,
                                        pressedElevation = 6.dp
                                    ),
                                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                        containerColor = green
                                    )
                                ) {
                                    Text(
                                        text = "Lưu",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }
                                Button(
                                    onClick = {
                                        navController.popBackStack()
                                    },
                                    modifier = Modifier
                                        .height(48.dp),
                                    shape = Shapes.medium,
                                    elevation = androidx.compose.material3.ButtonDefaults.buttonElevation(
                                        defaultElevation = 5.dp,
                                        pressedElevation = 6.dp
                                    ),
                                    colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                        containerColor = delete
                                    )
                                ) {
                                    Text(
                                        text = "Hủy",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 18.sp
                                    )
                                }

                            }
                        }
                    }
                }
            }
        )
    }
}

fun showDatePickerDialog(context: Context, calendar: Calendar, onDateSelected: (Int, Int, Int) -> Unit) {
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    DatePickerDialog(
        context,
        { _, selectedYear, selectedMonth, selectedDay ->
            onDateSelected(selectedYear, selectedMonth, selectedDay)
        },
        year,
        month,
        day
    ).show()
}
