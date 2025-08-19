package com.tonyxlab.notemark.data.remote.sync.client

import com.tonyxlab.notemark.data.remote.sync.dto.RemoteNote

interface NotesRemote {
    suspend fun create(body: RemoteNote): RemoteNote
    suspend fun update(body: RemoteNote): RemoteNote
    suspend fun delete(remoteId: String)
    suspend fun getAll(): List<RemoteNote> // full snapshot
}