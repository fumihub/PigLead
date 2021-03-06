package com.non_name_hero.calenderview.calendar

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.gridlayout.widget.GridLayout
import androidx.lifecycle.Observer
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.CalendarData
import com.non_name_hero.calenderview.databinding.CalendarCellBinding
import com.non_name_hero.calenderview.databinding.CalendarFragmentScreenSlidePageBinding
import com.non_name_hero.calenderview.databinding.ScheduleListBinding
import com.non_name_hero.calenderview.inputForm.InputActivity
import com.non_name_hero.calenderview.inputForm.InputBalanceActivity
import com.non_name_hero.calenderview.utils.DateManager
import com.non_name_hero.calenderview.utils.PigLeadUtils
import java.util.*
import kotlin.collections.HashMap

/**
 * フラグメントにカレンダーを表示させるクラス
 */
class CalendarPageFragment() : Fragment() {

    // TODO 定数はまとめる
    private val NUM_PAGES = 100
    private val DEFAULT_PAGE = NUM_PAGES / 2

    var mProgressMonth: Int = 0

        set(value) {
            field = value - DEFAULT_PAGE
        }

    var mHolidayMap: Map<String, List<CalendarData>> = HashMap()
    var mCalendarMap: Map<String, List<CalendarData>> = HashMap()
    val dateArray: List<Date> = DateManager().days


    /**
     * onCreateViewは戻り値のビューを表示させる
     *
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return 表示されるView
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        this.mProgressMonth = this.tag?.replace("f", "")?.toInt() ?: 0

        val binding: CalendarFragmentScreenSlidePageBinding
        //DataBinding
        binding = CalendarFragmentScreenSlidePageBinding.inflate(
                inflater,
                container,
                false).apply() {
            this.lifecycleOwner = viewLifecycleOwner
            this.viewmodel = (activity as MainActivity).obtainViewModel()
        }

        binding.viewmodel?.holidaySchedulesMap.apply {
            this?.observe(viewLifecycleOwner, Observer<Map<String, List<CalendarData>>> {
                mHolidayMap = it
                // TODO 本当はスケジュールだけ更新したい
                createCalendar(binding)
            })
        }

        createCalendar(binding)

        binding.executePendingBindings()
        return binding.root
    }


    private fun createCalendar(binding: CalendarFragmentScreenSlidePageBinding) {
        val mDateManager = DateManager().jumpMonth(mProgressMonth)
        val dateArray = mDateManager.days
        val calendarRowCount = mDateManager.weeks
        val calendarColCount = 7
        var calendarCellBinding: CalendarCellBinding
        val inflater = LayoutInflater.from(context)

        binding.calendarGridView.removeAllViewsInLayout()
        binding.calendarGridView.apply {
            this.columnCount = calendarColCount
            this.rowCount = calendarRowCount
        }

        for (rowCount: Int in 0 until calendarRowCount) {

            for (colCount: Int in 0 until calendarColCount) {
                val cellDate = dateArray[(rowCount * 7) + colCount]
                // カレンダーセル生成
                calendarCellBinding = DataBindingUtil.bind(inflater.inflate(R.layout.calendar_cell, binding.calendarGridView, false))
                        ?: throw IllegalStateException()
                //セルのサイズ・位置を指定
                val params = GridLayout.LayoutParams()
                params.width = 0
                params.rowSpec = GridLayout.spec(rowCount, GridLayout.FILL, 1.0f);
                params.columnSpec = GridLayout.spec(colCount, GridLayout.FILL, 1f);
                calendarCellBinding.calendarCell.layoutParams = params
                //日付のみ表示させる
                calendarCellBinding.date = PigLeadUtils.formatD.format(cellDate)
                //当月以外のセルをグレーアウト
                if (mDateManager.isCurrentMonth(cellDate)) {
                    //当日の背景を黄色に
                    if (mDateManager.currentDate == cellDate) {
                        calendarCellBinding.calendarCell.setBackgroundColor(ContextCompat.getColor(context!!, R.color.currentDayColor))
                    } else {
                        calendarCellBinding.calendarCell.setBackgroundColor(Color.WHITE)
                    }
                } else {
                    calendarCellBinding.calendarCell.setBackgroundColor(ContextCompat.getColor(context!!, R.color.notCurrentMonth))
                }

                //祝日、日曜日を赤、土曜日を青に
                setCalendarCellBase(
                        calendarCellBinding,
                        PigLeadUtils.formatYYYYMMDD.format(cellDate),
                        mDateManager.getDayOfWeek(cellDate)
                )

                //スケジュールをセルに追記
                val scheduleList = binding.viewmodel?.calendarDataMap?.value?.get(PigLeadUtils.formatYYYYMMDD.format(cellDate))
                        ?: emptyList()
                //スケジュールを追加
                for (i in 0..3) {
                    if (binding.viewmodel?.calendarDataMap?.value?.containsKey(PigLeadUtils.formatYYYYMMDD.format(cellDate)) == true && i < scheduleList.size) {
                        setScheduleText(scheduleList[i], calendarCellBinding.scheduleList)
                    } else {
                        break
                    }
                }
                // セルタップ時の動作
                calendarCellBinding.root.setOnClickListener { _ ->
                    binding.viewmodel?.setScheduleItem(cellDate)
                    true
                }
                // セル長押し時の動作
                calendarCellBinding.root.setOnLongClickListener { v ->
                    val currentMode = binding?.viewmodel?.currentMode?.value ?: true //
                    val intent = if (!currentMode) Intent(context, InputBalanceActivity::class.java) else Intent(context, InputActivity::class.java)
                    /*TODO スケジュール入力時と家計簿入力時に分ける*/
                    //入力画面に引数で年月日を渡す
                    val year: Int = Integer.valueOf(PigLeadUtils.yearFormat.format(cellDate))
                    val month: Int = Integer.valueOf(PigLeadUtils.monthFormat.format(cellDate))
                    val day: Int = Integer.valueOf(PigLeadUtils.dayFormat.format(cellDate))
                    intent.putExtra("year", year)
                    intent.putExtra("month", month)
                    intent.putExtra("day", day)
                    startActivity(intent)
                    true
                }
                binding.calendarGridView.addView(calendarCellBinding.root)
            }
        }

    }

    /**
     * カレンダーセルベースを作成
     */
    private fun setCalendarCellBase(view: CalendarCellBinding?, date: String, dayOfWeek: Int) {
        view!!.scheduleList.removeAllViews()
        var schedules: List<CalendarData>
        var isHoliday = false
        if (mHolidayMap.containsKey(date)) {
            schedules = mHolidayMap.get(date) ?: emptyList()
            for (schedule in schedules) {
                //祝日の場合
                setScheduleText(schedule, view.scheduleList)
            }
            isHoliday = true
        }


        //日曜日の場合
        if (dayOfWeek == 1 || true == isHoliday) {
            view.dateText.setTextColor(Color.RED)
        } else if (dayOfWeek == 7) {
            view.dateText.setTextColor(Color.BLUE)
        } else {
            view.dateText.setTextColor(Color.BLACK)

        }
    }

    /**
     * スケジュールをセットする
     *
     * @param schedule
     * @param root
     * @return textView
     */
    private fun setScheduleText(schedule: CalendarData, root: ViewGroup) {
        val binding = ScheduleListBinding.inflate(layoutInflater, root, true)
        binding.schedule = schedule
        binding.executePendingBindings()
        //Drawableで背景を指定
        val drawable = GradientDrawable()
        drawable.cornerRadius = 10f
        if (schedule.isHoliday) {
            drawable.setColor(ContextCompat.getColor(context!!, R.color.holidayColor))
        } else {
            drawable.setColor(schedule.groupBackgroundColor)
        }
        binding.root.background = drawable
    }


    fun replaceData(schedules: Map<String, List<CalendarData>>) {
        setCalendarMap(schedules)
    }

    fun replaceHoliday(holidayMap: Map<String, List<CalendarData>>) {
        setHolidayMap(holidayMap)
    }

    private fun setHolidayMap(holidayMap: Map<String, List<CalendarData>>) {
        mHolidayMap = holidayMap
    }

    private fun setCalendarMap(calendarMap: Map<String, List<CalendarData>>) {
        mCalendarMap = calendarMap
    }

}