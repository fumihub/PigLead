package com.non_name_hero.calenderview.calendar;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.inputForm.InputActivity;
import com.non_name_hero.calenderview.utils.DateManager;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

//フラグメントにカレンダーのビューを表示させる
public class CalendarPageFragment extends Fragment {

    private Intent intent;
    private Date mTargetDate;
    private CalendarAdapter mCalendarAdapter;
    private GridView calendarGridView;
    private int mProgressMonth;
    private DateManager mDateManager;
    private List<Date> dateArray;

    //コンストラクタ
    public CalendarPageFragment(int progressMonth){
        dateArray = mDateManager.getDays();
        mProgressMonth = progressMonth;
    }

    //onCreateViewは戻り値のビューを表示させる
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //表示させるViewを指定
        ViewGroup rootView = (ViewGroup) inflater.inflate(
                R.layout.calendar_fragment_screen_slide_page, container, false);
        //カレンダのIDを取得
        calendarGridView = rootView.findViewById(R.id.calendarGridView);


        //カレンダーのアダプターを使用してViewを作成-
        mCalendarAdapter = new CalendarAdapter(getContext());
        calendarGridView.setAdapter(mCalendarAdapter);


        mCalendarAdapter.setJumpMonth(mProgressMonth);

        //クリックリスナー
        calendarGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * GridViewのクリックリスナー
             * @param parent 表示されているAdapterViewのインスタンス参照
             * @param view 選択されたItemのViewインスタンス参照
             * @param position GridView内でのポジション
             * @param id Adapter内メソッドgetItemIdで設定した値　ここではpositionをそのまま返してる
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //年月日のフォーマット
                SimpleDateFormat yearFormat = new SimpleDateFormat("yyyy", Locale.US);
                SimpleDateFormat monthFormat = new SimpleDateFormat("MM", Locale.US);
                SimpleDateFormat dayFormat = new SimpleDateFormat("dd", Locale.US);
                //選択されたセルのViewIdを取得
                TextView selectedDateText =(TextView) view.findViewById(R.id.dateText);
                //トーストメッセージ作成
                String message = selectedDateText.getText().toString() + "日が選択されました。";
                //トーストを表示
                Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
                //入力画面に遷移
                intent = new Intent(getContext(), InputActivity.class);
                //入力画面に引数で年月日を渡す
                intent.putExtra("year", yearFormat.format(dateArray.get(position)));
                intent.putExtra("month", monthFormat.format(dateArray.get(position)));
                intent.putExtra("day", dayFormat.format(dateArray.get(position)));
                startActivity(intent);
            }
        });
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }
}