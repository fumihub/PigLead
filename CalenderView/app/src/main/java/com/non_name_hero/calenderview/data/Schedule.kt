package com.non_name_hero.calenderview.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "schedule")
class Schedule(@field:ColumnInfo(name = "schedule_id") @field:PrimaryKey(autoGenerate = true) var scheduleId: Long,
               title: String,
               description: String,
               startAtDatetime: Date,
               endAtDatetime: Date?,
               groupId: Int,
               paymentId: Int) {

    @ColumnInfo(name = "title")
    var title: String?

    @ColumnInfo(name = "description")
    var description: String?

    @ColumnInfo(name = "start_at_datetime")
    private var mStartAtDatetime: Date

    @ColumnInfo(name = "end_at_datetime")
    var endAtDatetime: Date?

    @ColumnInfo(name = "payment_id")
    var paymentId: Int

    @ColumnInfo(name = "group_id", defaultValue = "1")
    var groupId: Int

    @ColumnInfo(name = "start_timestamp")
    var startTimestamp: String? = null

    @ColumnInfo(name = "end_timestamp")
    var endTimestamp: String? = null

    //DBでは管理されないフィールド
    @Ignore
    private var mEditable: Boolean

    @Ignore
    var isHoliday = false

    @Ignore
    constructor(title: String,
                description: String,
                startAtDatetime: Date,
                endAtDatetime: Date?,
                groupId: Int,
                paymentId: Int) : this(0, title, description, startAtDatetime, endAtDatetime, groupId, paymentId) {
    }

    val startAtDatetime: Date?
        get() = mStartAtDatetime

    fun setStartAtDatetime(mStartAtDatetime: Date) {
        this.mStartAtDatetime = mStartAtDatetime
    }

    fun setUneditable() {
        mEditable = false
    }

    init {
        this.title = title
        this.description = description
        mStartAtDatetime = startAtDatetime
        this.endAtDatetime = endAtDatetime
        this.paymentId = paymentId
        this.groupId = groupId
        mEditable = true
    }
}