package com.example.playwithaync

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MainScreen(
    onLaunch: (append: (String) -> Unit) -> Unit,
    onAsync: (append: (String) -> Unit) -> Unit,
    onCancel: (append: (String) -> Unit) -> Unit,
    onDispatchers: (append: (String) -> Unit) -> Unit,
    onStructured: (append: (String) -> Unit) -> Unit
) {
    val logs = remember { mutableStateListOf<String>() }

    fun append(line: String) {
        logs.add(line)
    }

    MaterialTheme {
        Surface(modifier = Modifier.fillMaxSize()) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onLaunch(::append) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("launch")
                    }
                    Button(
                        onClick = { onAsync(::append) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("async")
                    }
                    Button(
                        onClick = { onCancel(::append) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("cancel")
                    }
                }

                // Second row of coroutine actions
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onDispatchers(::append) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("dispatchers")
                    }
                    Button(
                        onClick = { onStructured(::append) },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("structured")
                    }
                    Button(
                        onClick = { logs.clear() },
                        modifier = Modifier.weight(1f)
                    ) {
                        Text("clear")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Output:",
                    style = MaterialTheme.typography.bodyLarge
                )

                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(top = 8.dp)
                ) {
                    items(logs.size) { line ->
                        Text(
                            text = logs[line],
                            modifier = Modifier.padding(vertical = 2.dp),
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }
            }
        }
    }
}