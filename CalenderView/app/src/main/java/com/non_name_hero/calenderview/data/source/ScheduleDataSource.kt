package com.non_name_hero.calenderview.data.source

import androidx.lifecycle.LiveData
import com.non_name_hero.calenderview.data.*
import java.util.*

interface ScheduleDataSource {

    /*UserInfo用コールバッグ*/
    interface GetUserInfoCallback {
        fun onUserInfoLoaded(password: String)
        fun onDataNotAvailable()
    }

    /**
     * User追加時コールバック
     * onUserInfoSaved() -　保存成功時の処理
     * onDataNotAvailable() - 保存失敗時の処理
     */
    interface SaveUserInfoCallback {
        fun onUserInfoSaved(existFlag: Boolean)
        fun onDataNotAvailable()
    }
    
    /**
     * UserInfo変更時コールバック
     * onUserInfoSaved() -　保存成功時の処理
     * onDataNotAvailable() - 保存失敗時の処理
     */
    interface ChangeUserInfoCallback {
        fun onUserInfoSaved()
        fun onDataNotAvailable()
    }

    fun getUserInfo(mailAddress: String, callback: GetUserInfoCallback)
    fun setUserInfo(mailAddress: String, password: String, callback: SaveUserInfoCallback)
    fun changeUserInfo(mailAddress: String, newPassword: String, callback: ChangeUserInfoCallback)

    /*Schedule用コールバック*/
    /*全件取得時のコールバック*/
    interface GetScheduleCallback {
        fun onScheduleLoaded(schedules: List<Schedule>)
        fun onDataNotAvailable()
    }

    /*日付指定スケジュール取得時のコールバック*/
    interface PickUpScheduleCallback {
        fun onScheduleLoaded(schedules: List<Schedule>)
        fun onDataNotAvailable()
    }

    /**
     * Schedule追加時コールバック
     * onScheduleSaved() -　保存成功時の処理
     * onDataNotSaved() - 保存失敗時の処理
     */
    interface SaveScheduleCallback {
        fun onScheduleSaved()
        fun onDataNotSaved()
    }

    fun getSchedule(ScheduleIds: LongArray, callback: GetScheduleCallback)
    fun setSchedule(schedule: Schedule, callback: SaveScheduleCallback)
    fun getAllSchedules(callback: GetScheduleCallback)
    fun removeScheduleByScheduleId(scheduleId: Long)
    fun pickUpSchedules(targetStartDate: Date, targetEndDate: Date, callback: PickUpScheduleCallback)


    /*スケジュールグループ用コールバック*/
    /**
     * ScheduleGroup追加時コールバック
     * onScheduleGroupSaved(primaryKey) -　保存成功時の処理
     * onDataNotSaved() - 保存失敗時の処理
     */
    interface SaveScheduleGroupCallback {
        fun onScheduleGroupSaved(primaryKey: Long)
        fun onDataNotSaved()
    }

    /**
     * ScheduleGroup編集時コールバック
     * onScheduleGroupSaved(updateLine) -　保存成功時の処理
     * onDataNotSaved() - 保存失敗時の処理
     */
    interface UpdateScheduleGroupCallback {
        fun onScheduleGroupSaved(updateLine: Int)
        fun onDataNotSaved()
    }

    /*全件取得時のコールバック*/
    interface GetScheduleGroupsCallback {
        fun onScheduleGroupsLoaded(Groups: List<ScheduleGroup>)
        fun onDataNotAvailable()
    }

    /*1件取得時コールバック*/
    interface GetScheduleGroupCallback {
        fun onScheduleGroupLoaded(group: ScheduleGroup)
    }

    fun insertScheduleGroup(colorNumber: Int, colorCreateTitle: String, textColor: String, color: Int, callback: SaveScheduleGroupCallback)
    fun deleteScheduleGroup(groupId: Int, callback: DeleteCallback)
    fun getScheduleGroup(colorNumber: Int, callback: GetScheduleGroupCallback)
    fun getListScheduleGroup(callback: GetScheduleGroupsCallback)
    fun updateScheduleGroup(groupId: Int, colorNumber: Int, colorCreateTitle: String, textColor: String, color: Int, callback: UpdateScheduleGroupCallback)


    /*CalendarData用コールバッグ*/
    interface LoadHolidayCalendarDataCallback {
        fun onHolidayCalendarDataLoaded(calendarDataList: List<CalendarData>)
        fun onDataNotAvailable()
    }

    interface LoadCalendarDataCallback {
        fun onCalendarDataLoaded(calendarDataList: List<CalendarData>)
        fun onDataNotAvailable()
    }

    fun getHoliday(callback: LoadHolidayCalendarDataCallback)
    fun getCalendarDataList(callback: LoadCalendarDataCallback)


    /*Balance用コールバック*/
    /*全件取得時のコールバック*/
    interface GetBalanceCallback {
        fun onBalanceLoaded(Balances: List<Balance>)
        fun onDataNotAvailable()
    }

    /**
     * Balance追加時コールバック
     * onBalanceSaved() -　保存成功時の処理
     * onDataNotSaved() - 保存失敗時の処理
     */
    interface SaveBalanceCallback {
        fun onBalanceSaved()
        fun onDataNotSaved()
    }

    fun insertBalance(balance: Balance, callback: SaveBalanceCallback)
    fun removeBalanceByBalanceId(balanceId: Long)
    fun getAllBalances(callback: GetBalanceCallback)

    /* BalanceData　*/
    /**
     * BalanceData取得コールバック
     */
    interface GetBalanceDataCallback {
        fun onBalanceDataLoaded(balanceData: List<BalanceData>)
        fun onDataNotAvailable()
    }
    fun getBalanceData(startMonth: Date?, endMonth: Date?, callback: GetBalanceDataCallback)

    /*CategoryData用コールバック*/
    /*全要素取得時コールバック*/
    interface GetCategoriesDataCallback {
        fun onCategoriesDataLoaded(categoryData:List<CategoryData>)
        fun onDataNotAvailable()
    }
    /*1件取得時コールバック*/
    interface GetCategoryDataCallback {
        fun onCategoryDataLoaded(categoryData: CategoryData)
    }

    fun getCategoriesData(categoryId: Int, callback: GetCategoriesDataCallback)
    fun getCategoryData(balanceCategoryId: Int, callback: GetCategoryDataCallback)


    /*Category用コールバック*/
    /*全件取得時コールバック*/
    interface GetCategoryCallback {
        fun onCategoryLoaded(category:List<Category>)
        fun onDataNotAvailable()
    }

    fun getCategory(callback: GetCategoryCallback)


    /*BalanceCategory用コールバック*/
    /**
     * BalanceCategory追加時コールバック
     * onBalanceCategorySaved(primaryKey) -　保存成功時の処理
     * onDataNotSaved() - 保存失敗時の処理
     */
    interface SaveBalanceCategoryCallback {
        fun onBalanceCategorySaved(primaryKey: Long)
        fun onDataNotSaved()
    }

    fun insertBalanceCategory(editFlag: Boolean, balanceCategoryName: String, categoryId: Int, callback: SaveBalanceCategoryCallback)
    fun deleteBalanceCategory(categoryId: Int, balanceCategoryId: Int, callback: DeleteCallback)


    /*共通*/
    /*削除時コールバック*/
    interface DeleteCallback {
        fun onDeleted()
        fun onDataNotDeleted()
    }
}