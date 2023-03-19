package com.easy.aidlserver

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.os.Process
import android.util.Log

class RemoteService : Service() {
    override fun onBind(intent: Intent?): IBinder {
        Log.d("Service Side", "onBind");
        return binder;
    }

    override fun onUnbind(intent: Intent?): Boolean {
        Log.d("Service Side", "onUnBind")
        return super.onUnbind(intent)
    }

    private val users = mutableListOf<User>()

    private val binder = object : IRemoteAidlInterface.Stub() {
        override fun currPid(): Int {
            return Process.myPid()
        }

        override fun basicTypes(
            anInt: Int,
            aLong: Long,
            aBoolean: Boolean,
            aFloat: Float,
            aDouble: Double,
            aString: String?
        ) {
            Log.d(
                "Service Side",
                "basicTypes anInt:$anInt;aLong:$aLong;aBoolean:$aBoolean;aFloat:$aFloat;aDouble:$aDouble;aString:$aString"
            )
        }

        override fun addUser(user: User?) {
            user?.let {
                users.add(it)
            }
        }

        override fun theFirstUser(): User {
            users.forEach {
                Log.d("Service Side", it.toString())
            }
            return users.lastOrNull() ?: User(name = "Dougie", age = 18)
        }
    }
}