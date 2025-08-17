package com.tonyxlab.notemark.platform.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.tonyxlab.notemark.data.local.datastore.TokenStorage
import com.tonyxlab.notemark.data.workmanager.SyncRequest


import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.core.context.GlobalContext

class BootReceiver: BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED != intent.action) return

        // Don’t block the receiver thread
        val pendingResult = goAsync()
        CoroutineScope(Dispatchers.Default).launch {
            try {
                val koin = GlobalContext.get()

                val syncRequest: SyncRequest = koin.get()

                val interval = TokenStorage.getSyncInterval()
                syncRequest.enqueuePeriodicSync(interval)
            } finally {
                pendingResult.finish()
            }
        }
    }
}