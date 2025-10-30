package com.example.playwithaync

import android.util.Log
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.rememberCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

//All types of Side Effects

@Composable
fun MyComposable(userId: String) {
    LaunchedEffect(userId) {
        // Side effect: fetch data from network or DB
        //val user = getUserFromNetwork(userId)
        Log.d("Compose", "Fetched user: ")
    }
}

@Composable
fun MyButton() {
    val scope = rememberCoroutineScope()

    Button(onClick = {
        scope.launch {
            // Side effect: show snackbar or update shared state
            delay(1000)
            Log.d("Compose", "Button clicked!")
        }
    }) {
        Text("Click Me")
    }
}
