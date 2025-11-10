package com.example.playwithaync

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.lifecycleScope
import com.example.playwithaync.ui.theme.PlayWithAyncTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.coroutines.cancellation.CancellationException


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            PlayWithAyncTheme {
                MainScreen(
                    onLaunch = { messageAppender ->
                        lifecycleScope.launch {
                            messageAppender("Before delay on main thread")
                            delay(1000)
                            messageAppender("After delay — World!")
                        }
                    },
                    onAsync = { messageAppender ->
                        lifecycleScope.launch {
                            val t1 = async(Dispatchers.Default) {
                                delay(800)
                                messageAppender("Task1 finished on ${Thread.currentThread().name}")
                                10
                            }
                            val t2 = async(Dispatchers.Default) {
                                delay(1000)
                                messageAppender("Task2 finished on ${Thread.currentThread().name}")
                                20
                            }
                            val sum = t1.await() + t2.await()
                            messageAppender("Sum = $sum")
                        }
                    },
                    onCancel = { messageAppender ->
                        lifecycleScope.launch {
                            val job = launch {
                                try {
                                    repeat(10) { i ->
                                        delay(300)
                                        messageAppender("Working $i")
                                    }
                                } catch (e: CancellationException) {
                                    messageAppender("Cancelled: ${e.message}")
                                } finally {
                                    messageAppender("Cleanup in finally block")
                                }
                            }

                            // cancel after 1 second
                            delay(1000)
                            messageAppender("Calling job.cancel()")
                            job.cancel()
                            job.join()
                            messageAppender("Job is cancelled")
                        }
                    },
                    onDispatchers = { messageAppender ->
                        lifecycleScope.launch {
                            withContext(Dispatchers.Default) {
                                messageAppender("Default dispatcher on ${Thread.currentThread().name}")
                            }
                            withContext(Dispatchers.IO) {
                                messageAppender("IO dispatcher on ${Thread.currentThread().name}")
                            }
                            withContext(Dispatchers.Unconfined) {
                                messageAppender("Unconfined dispatcher on ${Thread.currentThread().name}")
                            }
                        }
                    },
                    onStructured = { messageAppender ->
                        lifecycleScope.launch {
                            try {
                                val result = coroutineScope {
                                    val one = async { doTask1(messageAppender) }
                                    val two = async { doTask2(messageAppender) }
                                    one.await() + two.await()
                                }
                                messageAppender("Structured result = $result")
                            } catch (e: Exception) {
                                messageAppender("Error: ${e.message}")
                            }
                        }
                    }
                )
            }
        }
    }

    private suspend fun doTask1(messageAppender: (String) -> Unit): Int {
        delay(600)
        messageAppender("doTask1 done")
        return 5
    }

    private suspend fun doTask2(messageAppender: (String) -> Unit): Int {
        delay(900)
        messageAppender("doTask2 done")
        return 7
    }
}

