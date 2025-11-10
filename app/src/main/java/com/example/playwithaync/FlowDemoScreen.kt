package com.example.playwithaync

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.time.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FlowDemoScreen() {
    val viewModel = remember { FlowDemoViewModel() }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Cold vs Hot Flow Example") })
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(24.dp),
        ) {
            // Cold flow display
            Text("Cold Flow Example")
            FlowCollectorSection(
                label = "Cold Flow",
                value = viewModel.coldFlowValue,
                onStart = { viewModel.startColdFlow() }
            )

            Divider()

            // Hot flow display
            Text("Hot Flow Example (StateFlow)")
            FlowCollectorSection(
                label = "Hot Flow",
                value = viewModel.hotFlowValue,
                onStart = { viewModel.startHotFlow() }
            )
        }
    }
}

@Composable
fun FlowCollectorSection(label: String, value: String, onStart: () -> Unit) {
    Column {
        Text(text = "$label Value: $value")
        Button(onClick = onStart) {
            Text("Start $label")
        }
    }
}

class FlowDemoViewModel {
    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())

    // --- Cold Flow ---
    var coldFlowValue by mutableStateOf("-")
        private set

    fun startColdFlow() {
        val coldFlow = flow {
            for (i in 1..5) {
                emit(i)
                delay(500)
            }
        }

        scope.launch {
            coldFlow.collect {
                coldFlowValue = it.toString()
            }
        }
    }


    private val _hotFlow = MutableStateFlow(0)
    val hotFlow = _hotFlow.asStateFlow()

    var hotFlowValue by mutableStateOf("-")
        private set

    init {

        scope.launch {
            var i = 1
            while (true) {
                delay(500)
                _hotFlow.value = i++
            }
        }
    }

    fun startHotFlow() {
        scope.launch {
            hotFlow.collect {
                hotFlowValue = it.toString()
            }
        }
    }
}