package com.non_name_hero.calenderview.data.source

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.non_name_hero.calenderview.data.*
import com.non_name_hero.calenderview.data.source.ScheduleDataSource.*
import com.non_name_hero.calenderview.utils.PigLeadUtils
import java.util.*

class ScheduleRepository (
        val scheduleDataLocalSource: ScheduleDataSource,
        val mScheduleDataRemoteSource: ScheduleDataSource
) : ScheduleDataSource {
    var mCachedHolidayCalenderData: List<CalendarData>? = null
    var mCachedCalendarData: List<CalendarData>? = null
    var mCachedScheduleMap: Map<String, List<Schedule>>? = null
    var mCacheIsDirty = false
    var mCalendarCacheIsDirty = false
    var mHolidayCacheIsDirty = false
    var cachedBalanceData: LiveData<List<BalanceData>> = MutableLiveData<List<BalanceData>>().apply { this.value = emptyList() }
    var balanceDataCacheIsDirty = false

    override fun changeUserInfo(mailAddress: String, newPassword: String, callback: ChangeUserInfoCallback) {
        mScheduleDataRemoteSource.changeUserInfo(mailAddress, newPassword, callback)
    }

    /*UserInfo*/
    override fun getUserInfo(mailAdress: String, callback: GetUserInfoCallback) {
        mScheduleDataRemoteSource.getUserInfo(mailAdress, callback)
    }

    override fun setUserInfo(mailAdress: String, password: String, callback: SaveUserInfoCallback) {
        mScheduleDataRemoteSource.setUserInfo(mailAdress, password, callback)
    }

    /*Schedule*/
    /**
     * スケジュールIDを指定して情報を取得
     *
     * @param scheduleId 情報を取得したいスケジュールID
     * @param callback   情報取得後の処理
     */
    override fun getSchedule(scheduleId: LongArray, callback: GetScheduleCallback) {
        scheduleDataLocalSource.getSchedule(scheduleId, callback)
    }

    /**
     * 全てのスケジュールデータを取得
     *
     * @param callback
     */
    override fun getAllSchedules(callback: GetScheduleCallback) {
        scheduleDataLocalSource.getAllSchedules(callback)
    }

    /**
     * スケジュール情報をDBに格納する
     *
     * @param schedule 格納するスケジュールオブジェクト
     * @param callback 格納後の処理
     */
    override fun setSchedule(schedule: Schedule, callback: SaveScheduleCallback) {
        scheduleDataLocalSource.setSchedule(schedule, callback)
    }

    /*スケジュール情報をDBから削除する*/
    override fun removeScheduleByScheduleId(scheduleId: Long) {
        scheduleDataLocalSource.removeScheduleByScheduleId(scheduleId)
    }

    override fun pickUpSchedules(targetStartDate: Date, targetEndDate: Date, callback: PickUpScheduleCallback) {
        scheduleDataLocalSource.pickUpSchedules(targetStartDate, targetEndDate, callback)
    }

    fun scheduleCacheClear() {
        mCacheIsDirty = false
        mCachedScheduleMap = null
    }


    /*ScheduleGroup*/
    /**
     * colorNumberを指定してグループ情報を取得
     *
     * @param colorNumber 　カラー番号
     * @param callback    取得後の処理。引数に取得した情報をとる
     */
    override fun getScheduleGroup(colorNumber: Int, callback: GetScheduleGroupCallback) {
        scheduleDataLocalSource.getScheduleGroup(colorNumber, callback)
    }

    /**
     * グループ情報を全権取得
     *
     * @param callback - onScheduleGroupsLoaded(List<ScheduleGroup> scheduleGroups) 情報取得後の処理。引数に全件グループ情報を保持
    </ScheduleGroup> */
    override fun getListScheduleGroup(callback: GetScheduleGroupsCallback) {
        scheduleDataLocalSource.getListScheduleGroup(callback)
    }

    /**
     * グループ情報DBに追加する
     *
     * @param colorNumber       色番号
     * @param colorCreateTitle  色グループ名
     * @param textColor         文字色
     * @param color             背景色
     * @param callback          保存完了後の処理、保存失敗時の処理
     */
    override fun insertScheduleGroup(colorNumber: Int, colorCreateTitle: String, textColor: String, color: Int, callback: SaveScheduleGroupCallback) {
        scheduleDataLocalSource.insertScheduleGroup(colorNumber, colorCreateTitle, textColor, color, callback)
    }

    /**
     * スケジュールグループを更新
     * primaryを合わせる
     * @param groupId           グループID
     * @param colorNumber       色番号
     * @param colorCreateTitle  色グループ名
     * @param textColor         文字色
     * @param color             背景色
     * @param callback          コールバック
     */
    override fun updateScheduleGroup(groupId: Int, colorNumber: Int, colorCreateTitle: String, textColor: String, color: Int, callback: UpdateScheduleGroupCallback) {
        scheduleDataLocalSource.updateScheduleGroup(groupId, colorNumber, colorCreateTitle, textColor, color, callback)
    }

    /**
     * groupIdを指定してグループ情報を削除
     *
     * @param groupId カラー番号
     */
    override fun deleteScheduleGroup(groupId: Int, callback: DeleteCallback) {
        scheduleDataLocalSource.deleteScheduleGroup(groupId, callback)
    }


    /*CalendarData*/
    /**
     * 祝日データを取得
     *
     * @param callback 取得後の処理
     */
    override fun getHoliday(callback: LoadHolidayCalendarDataCallback) {
        if (mCachedHolidayCalenderData == null && mHolidayCacheIsDirty == false) {
            mCachedHolidayCalenderData = ArrayList()
            mScheduleDataRemoteSource.getHoliday(object : LoadHolidayCalendarDataCallback {
                override fun onHolidayCalendarDataLoaded(calendarDataList: List<CalendarData>) {
                    /*キャッシュを保持*/
                    mCachedHolidayCalenderData = calendarDataList
                    callback.onHolidayCalendarDataLoaded(calendarDataList)
                }

                override fun onDataNotAvailable() {
                    callback.onDataNotAvailable()
                }
            })
        } else {
            callback.onHolidayCalendarDataLoaded(mCachedHolidayCalenderData!!)
        }
    }

    fun holidayCacheClear() {
        mHolidayCacheIsDirty = false
        mCachedHolidayCalenderData = null
    }

    /**
     * カレンダー表示データを取得
     * @param callback コールバック
     */
    override fun getCalendarDataList(callback: LoadCalendarDataCallback) {
        if (mCachedCalendarData == null && !mCalendarCacheIsDirty) {
            mCachedCalendarData = ArrayList()
            scheduleDataLocalSource.getCalendarDataList(object : LoadCalendarDataCallback {
                override fun onCalendarDataLoaded(calendarDataList: List<CalendarData>) {
                    mCacheIsDirty = true
                    callback.onCalendarDataLoaded(calendarDataList)
                }

                override fun onDataNotAvailable() {
                    callback.onDataNotAvailable()
                }
            })
        } else {
            mScheduleDataRemoteSource.getCalendarDataList(callback)
        }
    }

    fun calendarCacheClear() {
        mCachedCalendarData = null
        mCalendarCacheIsDirty = false
    }


    /*Balance*/
    override fun getAllBalances(callback: GetBalanceCallback) {
        scheduleDataLocalSource.getAllBalances(callback)
    }


    override fun insertBalance(balance: Balance, callback: SaveBalanceCallback) {
        scheduleDataLocalSource.insertBalance(balance, callback)
    }

    override fun removeBalanceByBalanceId(balanceId: Long) {
        scheduleDataLocalSource.removeBalanceByBalanceId(balanceId)
    }

    /* BalanceData */

    /**
     * カレンダーの表示(家計簿)データを取得
     */
    override fun getBalanceData(startMonth: Date?, endMonth: Date?, callback: GetBalanceDataCallback) {
        if (cachedBalanceData.value?.isEmpty() == true && !balanceDataCacheIsDirty) {
            scheduleDataLocalSource.getBalanceData(
                startMonth,
                endMonth,
                object : GetBalanceDataCallback {
                    override fun onBalanceDataLoaded(balanceLiveData: LiveData<List<BalanceData>>) {
                        balanceDataCacheIsDirty = true
                        callback.onBalanceDataLoaded(balanceLiveData)
                    }

                    override fun onDataNotAvailable() {
                        callback.onDataNotAvailable()
                    }


                })
        } else {
            callback.onBalanceDataLoaded(cachedBalanceData)
        }
    }
    /**
     * カレンダーの表示(家計簿)キャッシュデータを初期化
     */
    fun balanceDataCacheClear() {
        balanceDataCacheIsDirty = false
        cachedBalanceData = MutableLiveData<List<BalanceData>>().apply { this.value = emptyList() }
    }

    /*CategoryData*/
    /**
     * categoryIdを指定してカテゴリデータ情報を全取得
     *
     * @param categoryId 　カテゴリ番号
     * @param callback    取得後の処理。引数に取得した情報をとる
     */
    override fun getCategoriesData(categoryId: Int, callback: GetCategoriesDataCallback) {
        scheduleDataLocalSource.getCategoriesData(categoryId, callback)
    }

    /*カテゴリデータ情報を1件取得*/
    override fun getCategoryData(balanceCategoryId: Int, callback: GetCategoryDataCallback) {
        scheduleDataLocalSource.getCategoryData(balanceCategoryId, callback)
    }


    /*Category*/
    /**
     * 大カテゴリ全件取得
     *
     * @param callback    取得後の処理。引数に取得した情報をとる
     */
    override fun getCategory(callback: GetCategoryCallback) {
        scheduleDataLocalSource.getCategory(callback)
    }


    /*BalanceCategory*/
    /**
     * balanceCategory情報DBに追加する
     *
     * @param editFlag              編集可能フラグ
     * @param balanceCategoryName   バランスカテゴリー名
     * @param categoryId            カテゴリーID
     * @param callback              保存完了後の処理、保存失敗時の処理
     */
    override fun insertBalanceCategory(editFlag: Boolean, balanceCategoryName: String, categoryId: Int, callback: SaveBalanceCategoryCallback) {
        scheduleDataLocalSource.insertBalanceCategory(editFlag, balanceCategoryName, categoryId, callback)
    }

    /**
     * balanceCategoryIdを指定してbalanceCategory情報を削除
     * @param categoryId カテゴリID
     * @param balanceCategoryId サブカテゴリID
     */
    override fun deleteBalanceCategory(categoryId: Int, balanceCategoryId: Int, callback: DeleteCallback) {
        scheduleDataLocalSource.deleteBalanceCategory(categoryId, balanceCategoryId, callback)
    }


    /*singleTon*/
    companion object {
        private var INSTANCE: ScheduleRepository? = null

        /**
         * シングルトン、インスタンスの返却.
         *
         * @param scheduleDataLocalSource  デバイス上のストレージ(ローカルデータソース)
         * @param scheduleDataRemoteSource リモートのデータソース
         * @return the [ScheduleRepository] リポジトリのインスタンス
         */
        @JvmStatic
        fun getInstance(scheduleDataLocalSource: ScheduleDataSource,
                        scheduleDataRemoteSource: ScheduleDataSource): ScheduleRepository {
            if (INSTANCE == null) {
                INSTANCE = ScheduleRepository(scheduleDataLocalSource, scheduleDataRemoteSource)
            }
            return INSTANCE!!
        }

        /**
         * Used to force [.getInstance] to create a new instance
         * next time it's called.
         */
        fun destroyInstance() {
            INSTANCE = null
        }
    }
}

