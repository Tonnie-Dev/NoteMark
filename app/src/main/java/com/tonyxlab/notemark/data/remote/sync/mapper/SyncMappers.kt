package com.tonyxlab.notemark.data.remote.sync.mapper

import com.tonyxlab.notemark.data.remote.sync.dto.UploadItem
import com.tonyxlab.notemark.data.remote.sync.entity.SyncRecord

fun SyncRecord.toUploadItem(): UploadItem =
    UploadItem(
            localId = id.toString(),
            operation.name,
            payload = payload,
            clientTimestamp = timestamp
    )


