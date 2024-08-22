package com.example.asian.issuethirteen.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("storage")
data class StorageModel(
    @ColumnInfo("storage_name") var storageName: String,
    @ColumnInfo("storage_uri") var storageUri: String,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("storage_id")
    var storageId: Int = 0
}