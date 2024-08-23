package com.example.asian.issuethirteen.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity("storage")
data class StorageModel(
    @ColumnInfo("storage_name") var storageName: String,
    @ColumnInfo("storage_uri") var storageUri: String,
    @ColumnInfo("real_id") var realId: Long,
) {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo("storage_id")
    var storageId: Int = 0

    @Ignore
    var isSelected = false
}