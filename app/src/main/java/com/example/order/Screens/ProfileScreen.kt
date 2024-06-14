package com.example.order.Screens

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowRight
import androidx.compose.material.icons.rounded.Notifications
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import com.example.app.R
import com.example.order.navigation.Screen
import com.example.order.ui.theme.Shapes
import com.example.order.ui.theme.bg
import com.example.order.ui.theme.blackcart
import com.example.order.ui.theme.grayFont
import com.example.order.ui.theme.menu
import com.example.order.ui.theme.yellow
import com.example.order.viewmodels.UserViewModel
import com.google.firebase.Firebase
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.storage
import java.util.UUID

@OptIn(ExperimentalMaterial3Api::class, ExperimentalMaterialApi::class)
@SuppressLint("UnusedMaterialScaffoldPaddingParameter")
@Composable
fun ProfileScreen(
    userId: String?,
    navController: NavHostController,
    context: Context,
    userViewModel: UserViewModel = viewModel(),

    ) {

    val user by userViewModel.getUser(userId).observeAsState()
    user?.let {
        var imageUri by remember { mutableStateOf<Uri?>(null) }

        // Handle result of activity for result
        val launcher =
            rememberLauncherForActivityResult(contract = ActivityResultContracts.GetContent()) { uri ->
                imageUri = uri
                // Upload image to Firestore and Storage
                imageUri?.let {
                    uploadToStorage(it, context, "profile_image") { imageUrl ->
                        user?.let {
                            val updatedUser = it.copy(
                                image = imageUrl.toString()
                            )
                            userViewModel.updateUser(userId, updatedUser,
                                onSuccess = {
                                    navController.navigate(HomeScreens.PROFILE.name)
                                },
                                onFailure = { e ->
                                }
                            )
                        }
                    }
                }
            }

        // Composable function to handle click on profile image
        fun onProfileImageClicked() {
            // Open file picker to choose an image
            launcher.launch("image/*")

        }

        val scrollState = rememberLazyListState()
        Column(
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
                        androidx.compose.material3.Text(
                            text = "Tài khoản",
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            fontWeight = FontWeight.Bold
                        )
                    },

                    )
            }

            LazyColumn(
                contentPadding = PaddingValues(bottom = 100.dp),
                state = scrollState,
                modifier = Modifier
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
                        ConstraintLayout() {
                            val (topImg, profile) = createRefs()
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(120.dp)
                                    .constrainAs(topImg) {
                                        top.linkTo(parent.top)
                                        start.linkTo(parent.start)
                                    }
                                    .background(
                                        color = menu,
                                        shape = RoundedCornerShape(
                                            bottomEnd = 150.dp,
                                            bottomStart = 150.dp
                                        )
                                    )
                            )

                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceEvenly,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(2.dp)
                                    .constrainAs(profile) {
                                        top.linkTo(topImg.bottom)
                                        bottom.linkTo(topImg.bottom)
                                        start.linkTo(parent.start)
                                        end.linkTo(parent.end)
                                    }
                            ) {
                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier
                                        .padding(top = 30.dp)
                                ) {
                                    ProfileImage(
                                        modifier = Modifier.padding(16.dp),
                                        image = user?.image ?: "", // Pass user's current image URL
                                        onClick = ::onProfileImageClicked // Handle click event
                                    )
                                    Text(
                                        text = "${user!!.fullName}",
                                        color = blackcart,
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(top = 14.dp, bottom = 7.dp)
                                    )
                                    Text(
                                        text = "${user!!.email}",
                                        color = grayFont,
                                        fontSize = 18.sp
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(30.dp))
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 5.dp, start = 25.dp, end = 25.dp)
                                .height(55.dp)
                        ) {
                            androidx.compose.material.Surface(
                                onClick = {
                                    navController.navigate("profile/${user!!.id}")
                                },
                                shape = Shapes.small,
                                color = Color.White,
                                border = BorderStroke(1.dp, menu),
                                elevation = 1.dp
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight(),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(imageVector = Icons.Rounded.Person,
                                            contentDescription = "",
                                            tint = grayFont,
                                            modifier = Modifier
                                                .padding(start = 10.dp, end = 5.dp)
                                                .clickable {
                                                    navController.navigate("profile/${user!!.id}")
                                                }
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .weight(1f)
                                    ) {
                                        Text(
                                            text = "Sửa thông tin",
                                            color = Color.DarkGray,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
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
                                                .clickable { navController.navigate("profile/${user!!.id}") })
                                    }
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 5.dp, start = 25.dp, end = 25.dp)
                                .height(55.dp)
                        ) {
                            androidx.compose.material.Surface(
                                onClick = {
                                          navController.navigate(Screen.OrderHistory.createRoute(
                                              user?.id!!
                                          ))

                                },
                                shape = Shapes.small,
                                color = Color.White,
                                border = BorderStroke(1.dp, menu),
                                elevation = 1.dp
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
                                            painterResource(id = R.drawable.ic_history),
                                            contentDescription = "",
                                            tint = grayFont,
                                            modifier = Modifier
                                                .padding(start = 10.dp, end = 5.dp)
                                                .clickable { }
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .weight(1f)
                                    ) {
                                        Text(
                                            text = "Lịch sử đặt hàng",
                                            color = Color.DarkGray,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 5.dp, start = 25.dp, end = 25.dp)
                                .height(55.dp)
                        ) {
                            androidx.compose.material.Surface(
                                onClick = { /*TODO*/ },
                                shape = Shapes.small,
                                color = Color.White,
                                border = BorderStroke(1.dp, menu),
                                elevation = 1.dp
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight(),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(imageVector = Icons.Rounded.Notifications,
                                            contentDescription = "",
                                            tint = grayFont,
                                            modifier = Modifier
                                                .padding(start = 10.dp, end = 5.dp)
                                                .clickable { }
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .weight(1f)
                                    ) {
                                        Text(
                                            text = "Thông báo",
                                            color = Color.DarkGray,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
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
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 10.dp, bottom = 5.dp, start = 25.dp, end = 25.dp)
                                .height(55.dp)
                        ) {
                            androidx.compose.material.Surface(
                                onClick = { /*TODO*/ },
                                shape = Shapes.small,
                                color = Color.White,
                                border = BorderStroke(1.dp, menu),
                                elevation = 1.dp
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxHeight(),
                                        verticalArrangement = Arrangement.Center
                                    ) {
                                        Icon(imageVector = Icons.Rounded.Settings,
                                            contentDescription = "",
                                            tint = grayFont,
                                            modifier = Modifier
                                                .padding(start = 10.dp, end = 5.dp)
                                                .clickable { }
                                        )
                                    }
                                    Column(
                                        modifier = Modifier
                                            .padding(start = 16.dp)
                                            .weight(1f)
                                    ) {
                                        Text(
                                            text = "Cài đặt",
                                            color = Color.DarkGray,
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.SemiBold
                                        )
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

                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 20.dp, bottom = 5.dp, start = 25.dp, end = 25.dp)
                        ) {
                            Button(
                                onClick = {
                                          navController.navigate(Screen.Login.route)

                                },
                                modifier = Modifier
                                    .height(50.dp),
                                shape = CircleShape,
                                elevation = ButtonDefaults.buttonElevation(
                                    defaultElevation = 5.dp,
                                    pressedElevation = 6.dp
                                ),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFFD32C2C)
                                )
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_logout),
                                    contentDescription = "",
                                    tint = Color.White,
                                    modifier = Modifier
                                        .size(32.dp)
                                        .padding(end = 5.dp)
                                )

                                androidx.compose.material3.Text(
                                    text = "Đăng xuất",
                                    fontWeight = FontWeight.SemiBold,
                                    fontSize = 17.sp
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
@OptIn(ExperimentalMaterialApi::class)
@Composable
fun ProfileImage(
    modifier: Modifier = Modifier,
    image: String,
    onClick: () -> Unit // Add onClick parameter
) {
    val context = LocalContext.current
    val placeholderImage = painterResource(id = R.drawable.ic_profile) // Default image

    val imagePainter = if (image.isNullOrEmpty()) {
        placeholderImage
    } else {
        rememberAsyncImagePainter(image)
    }

    androidx.compose.material.Surface(
        onClick = onClick, // Handle click event
        color = Color.White,
        shape = CircleShape,
        modifier = modifier.size(120.dp),
    ) {
        Image(
            painter = imagePainter,
            contentDescription = "",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .border(
                    border = BorderStroke(3.dp, yellow),
                    shape = CircleShape
                )
                .clickable { onClick() } // Also handle click event
        )
    }
}
fun uploadToStorage(uri: Uri, context: Context, type: String, onImageUploaded: (String) -> Unit) {
    val storage = Firebase.storage
    // Create a storage reference from our app
    val storageRef = storage.reference

    val uniqueImageName = UUID.randomUUID().toString()
    val spaceRef: StorageReference = storageRef.child("$uniqueImageName.jpg")

    val byteArray: ByteArray? = context.contentResolver
        .openInputStream(uri)
        ?.use { it.readBytes() }

    byteArray?.let {
        val uploadTask = spaceRef.putBytes(byteArray)
        uploadTask.addOnFailureListener {
            Toast.makeText(
                context,
                "Upload failed",
                Toast.LENGTH_SHORT
            ).show()
        }.addOnSuccessListener { _ ->
            spaceRef.downloadUrl.addOnSuccessListener { uri ->
                onImageUploaded(uri.toString()) // Pass the URL to the callback function
            }.addOnFailureListener {
                // Handle failures
                Toast.makeText(
                    context,
                    "Failed to retrieve download URL",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}


