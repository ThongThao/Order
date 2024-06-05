package com.example.admin.screens.User

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.admin.R
import com.example.admin.model.User
import com.example.admin.screens.Home
import com.example.admin.ui.theme.Purple80
import com.example.admin.ui.theme.blue
import com.example.admin.ui.theme.blue2
import com.example.admin.ui.theme.blue3
import com.google.firebase.firestore.FirebaseFirestore

class AllUser : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "UnrememberedMutableState")
    @OptIn(ExperimentalMaterial3Api::class)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            var userList = mutableStateListOf<User>()
            var db: FirebaseFirestore = FirebaseFirestore.getInstance()

            db.collection("users")
                .get()
                .addOnSuccessListener { queryDocumentSnapshot ->
                if (!queryDocumentSnapshot.isEmpty){
                    val list = queryDocumentSnapshot.documents
                    for (d in list){
                        val c: User? = d.toObject(User::class.java)
                        c?.id = d.id
                        Log.e("TAG", "User id is : " + c!!.id)
                        userList.add(c)
                    }
                }else{
                    Toast.makeText(
                        this@AllUser,
                        "No data found in Database",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }.addOnFailureListener {
                Toast.makeText(
                    this@AllUser,
                    "Fail to get the data.",
                    Toast.LENGTH_SHORT
                ).show()
            }
            alluserUI(LocalContext.current, userList)
        }
    }
}
private fun deleteDataFromFirebase(id: String?, context: Context) {


    val db = FirebaseFirestore.getInstance();

    db.collection("users").document(id.toString()).delete().addOnSuccessListener {
        Toast.makeText(context, "Deleted successfully..", Toast.LENGTH_SHORT).show()
        context.startActivity(Intent(context, AllUser::class.java))
    }.addOnFailureListener {

        Toast.makeText(context, "Fail to delete item..", Toast.LENGTH_SHORT).show()
    }

}

@SuppressLint("SuspiciousIndentation")
@OptIn(ExperimentalMaterial3Api::class)
@Composable

fun alluserUI(context: Context, userList: SnapshotStateList<User>){
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Row(
            modifier = Modifier
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            IconButton(onClick = { context.startActivity(Intent(context, Home::class.java)) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_back),
                    contentDescription = "",
                    tint = blue,
                    modifier = Modifier.size(40.dp)
                )
            }
            Spacer(modifier = Modifier.width(55.dp))
            Text(
                text = "All User", style = TextStyle(
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
            )
            Spacer(modifier = Modifier.width(55.dp))
            IconButton(onClick = { }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_add),
                    contentDescription = "",
                    tint = blue,
                    modifier = Modifier.size(40.dp)
                )
            }
        }
        LazyColumn {
            itemsIndexed(userList) {index, item ->
                Surface(
                    color = blue2,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(shape = RoundedCornerShape(8.dp))
                        .padding(10.dp),
                    onClick = {

                    }
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                    ) {

                        Spacer(modifier = Modifier.height(5.dp))
                        Row {
                            Surface(
                                shape = CircleShape,
                                modifier = Modifier.size(width = 80.dp, height = 80.dp)
                            ) {
                                val painter = if ( userList[index]?.image!= null) {
                                    rememberAsyncImagePainter( userList[index]?.image)
                                } else {
                                    painterResource(id = R.drawable.profile)
                                }
                              Image(
                                  painter = painter,
                                  contentDescription = "",
                                  contentScale = ContentScale.Crop,
                              )
                            }
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .weight(2f),
                            ) {
                                Surface(
                                    shape = RoundedCornerShape(24.dp),
                                    modifier = Modifier
                                        .wrapContentSize()
                                        .padding(top = 3.dp),
                                    color = blue3
                                ) {
                                   userList[index]?.role?.let {
                                        Text(
                                            text = it,
                                            fontSize = 12.sp,
                                            style = MaterialTheme.typography.titleSmall,
                                            modifier = Modifier.padding(
                                                vertical = 4.dp,
                                                horizontal = 8.dp
                                            ),
                                            color = Color.Black
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                userList[index]?.fullName?.let {
                                    Text(
                                        text = it,
                                        fontSize =  20.sp,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                }


                                Spacer(modifier = Modifier.height(4.dp))

                            }
                            Column( ) {

                                IconButton(
                                    onClick = {
                                    }
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_edit),
                                        contentDescription = "",
                                        tint = Purple80,
                                        modifier = Modifier.size(30.dp)
                                    )
                                }
                                IconButton(
                                    onClick = { deleteDataFromFirebase(userList[index]?.id, context) },
                                ) {
                                    Icon(
                                        painterResource(id = R.drawable.ic_delete),
                                        contentDescription = "",
                                        tint= Color.Red,
                                        modifier = Modifier.size(30.dp)

                                    )
                                }

                            }
                        }


                    }
                }
            }
        }
    }
}

