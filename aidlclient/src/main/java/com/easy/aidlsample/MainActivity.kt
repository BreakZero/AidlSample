package com.easy.aidlsample

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.os.Process
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import com.easy.aidlsample.ui.theme.AidlSampleTheme
import com.easy.aidlserver.IRemoteAidlInterface
import com.easy.aidlserver.User
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    private val _1M = 1024 * 1024
    private var iRemoteService: IRemoteAidlInterface? = null
    private val sConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, service: IBinder?) {
            iRemoteService = IRemoteAidlInterface.Stub.asInterface(service)
            iRemoteService?.let {
                try {
                    val pid = it.currPid()
                    val myPid = Process.myPid()
                    Log.i("Client", "Service Pid: $pid, Client Pid: $myPid")
                    it.basicTypes(1, 2, true, 3.0f, 5.0, "Hello AIDL Server")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            Log.d("Client", "onDisconnect")
            iRemoteService = null
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AidlSampleTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    var btnText by remember {
                        mutableStateOf("Launch Service")
                    }
                    var currUser by remember {
                        mutableStateOf("NULL")
                    }
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center,
                                text = currUser
                            )
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    if ("Launch Service" == btnText) {
                                        val intent = Intent()
                                        intent.component = ComponentName(
                                            "com.easy.aidlserver",
                                            "com.easy.aidlserver.RemoteService"
                                        )
                                        bindService(
                                            intent, sConnection, Context.BIND_AUTO_CREATE
                                        )
                                        btnText = "Unbind Service"
                                    } else {
                                        unbindService(sConnection)
                                        btnText = "Launch Service"
                                    }
                                }) {
                                Text(text = btnText)
                            }

                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    iRemoteService?.addUser(
                                        User(
                                            "Dougie ${Random.nextInt(1, 100)}",
                                            Random.nextInt(18, 24),
                                            byteArray = ByteArray(_1M)
                                        )
                                    )
                                }) {
                                Text(text = "Add User")
                            }
                            Button(
                                modifier = Modifier.fillMaxWidth(),
                                onClick = {
                                    iRemoteService?.theFirstUser()?.let {
                                        currUser = it.toString()
                                    }
                                }
                            ) {
                                Text(text = "GET LATEST USER")
                            }
                        }
                    }
                }
            }
        }
    }
}
