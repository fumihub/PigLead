package com.non_name_hero.calenderview.inputForm;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.utils.Injection;

import java.util.Calendar;

public class InputActivity extends AppCompatActivity implements InputContract.View {

    private InputContract.Presenter mInputPresenter;
    private InputContract.View mView;

    private Calendar mStartAtDatetime;
    private Calendar mEndAtDatetime;

    private EditText title;
    private EditText startDate;
    private EditText endDate;
    private EditText startTime;
    private EditText endTime;
    private TextView timeArrow;
    private EditText myBudget;
    private EditText price;
    private TextView place;
    private EditText memo;
    private TextView picture;

    private Button timeButton;
    private Button color1;
    private Button color2;
    private Button detailButton;
    private Button cancelButton;
    private Button doneButton;

    private Intent intentIn;
    private Intent intentOut;
    private String year;
    private String month;
    private String day;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.input_main);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.inputToolbar);
        setSupportActionBar(myToolbar);


        new InputPresenter(this, Injection.provideScheduleRepository(getApplicationContext()));

        //カレンダー初期値用
        intentIn = getIntent();
        year = intentIn.getStringExtra("year");
        month = intentIn.getStringExtra("month");
        day = intentIn.getStringExtra("day");

        //色選択画面遷移用intent
        intentOut = new Intent(this, colorSelectActivity.class);


        /*入力画面表示*********************************************************************/
        //カレンダーセルのボタンが押された場合
        title = findViewById(R.id.title);
        startDate = findViewById(R.id.startDate);
        endDate = findViewById(R.id.endDate);
        timeButton = findViewById(R.id.timeButton);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        timeArrow = findViewById(R.id.timeArrow);
        color1 = findViewById(R.id.colorButton1);
        color2 = findViewById(R.id.colorButton2);
        detailButton = findViewById(R.id.detailButton);
        myBudget = findViewById(R.id.myBudget);
        price = findViewById(R.id.price);
        place = findViewById(R.id.place);
        memo = findViewById(R.id.memo);
        picture = findViewById(R.id.picture);
        cancelButton = findViewById(R.id.cancelButton);
        doneButton = findViewById(R.id.doneButton);

        //最初表示
        title.setVisibility(View.VISIBLE);
        startDate.setVisibility(View.VISIBLE);
        endDate.setVisibility(View.VISIBLE);
        timeButton.setVisibility(View.VISIBLE);
        color1.setVisibility(View.VISIBLE);
        color2.setVisibility(View.VISIBLE);
        detailButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);
        doneButton.setVisibility(View.VISIBLE);
        //最初非表示
        startTime.setVisibility(View.GONE);
        endTime.setVisibility(View.GONE);
        timeArrow.setVisibility(View.GONE);
        myBudget.setVisibility(View.GONE);
        price.setVisibility(View.GONE);
        place.setVisibility(View.GONE);
        memo.setVisibility(View.GONE);
        picture.setVisibility(View.GONE);
        //
        mStartAtDatetime = Calendar.getInstance();
        mStartAtDatetime.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
        mEndAtDatetime = Calendar.getInstance();
        mEndAtDatetime.set(Integer.valueOf(year), Integer.valueOf(month) - 1, Integer.valueOf(day));
//        /*タイトルEditTextが押されたとき********************/
//        title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //フォーカスを当てる
//                title.setFocusable(true);
//                title.setFocusableInTouchMode(true);
//            }
//        });
//        /**************************************************/

        //初期値を設定
        startDate.setText(month + "/" + day);
        endDate.setText(month + "/" + day);

        /*開始日時EditTextが押されたとき********************/
        startDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //Calendarインスタンスを取得
                final Calendar startCalendar = Calendar.getInstance();
                Intent intentIn = getIntent();
                int year = Integer.valueOf(intentIn.getStringExtra("year"));
                int month = Integer.valueOf(intentIn.getStringExtra("month"));
                int day = Integer.valueOf(intentIn.getStringExtra("day"));
                //DatePickerDialogインスタンスを取得
                DatePickerDialog startDatePickerDialog = new DatePickerDialog(
                        InputActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                startDate.setText(String.format("%02d / %02d", month + 1, dayOfMonth));
                                //Calendarオブジェクトを作成
                                mStartAtDatetime.set(year, month, dayOfMonth);
                            }
                        },
                        year,
                        //monthは0が1月のため-1する必要がある
                        month - 1,
                        day
                );

                //dialogを表示
                startDatePickerDialog.show();

            }
        });
        /**************************************************/

        /*終了日時EditTextが押されたとき********************/
        endDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendarインスタンスを取得
                final Calendar endCalendar = Calendar.getInstance();
                Intent intentIn = getIntent();
                int year = Integer.valueOf(intentIn.getStringExtra("year"));
                int month = Integer.valueOf(intentIn.getStringExtra("month"));
                int day = Integer.valueOf(intentIn.getStringExtra("day"));
                //DatePickerDialogインスタンスを取得
                DatePickerDialog endDatePickerDialog = new DatePickerDialog(
                        InputActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                //setした日付を取得して表示
                                endDate.setText(String.format("%02d / %02d", month + 1, dayOfMonth));
                                mEndAtDatetime.set(year, month, dayOfMonth);
                            }
                        },
                        year,
                        //monthは0が1月のため-1する必要がある
                        month - 1,
                        day
                );

                //dialogを表示
                endDatePickerDialog.show();
            }
        });
        /**************************************************/

        /*時間入力ボタンが押されたとき********************/
        timeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //詳細入力表示に
                timeButton.setVisibility(View.GONE);
                timeArrow.setVisibility(View.VISIBLE);
                startTime.setVisibility(View.VISIBLE);
                endTime.setVisibility(View.VISIBLE);
            }
        });
        /************************************************/

        /*開始時間EditTextが押されたとき********************/
        startTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendarインスタンスを取得
                final Calendar startTimeCalendar = Calendar.getInstance();

                //TimePickerDialogインスタンスを取得
                TimePickerDialog startTimePickerDialog = new TimePickerDialog(
                        InputActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //setした時間を取得して表示
                                startTime.setText(String.format("%02d : %02d", hourOfDay, minute));
                                mStartAtDatetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                mStartAtDatetime.set(Calendar.MINUTE, minute);
                            }
                        },
                        startTimeCalendar.get(Calendar.HOUR_OF_DAY),
                        startTimeCalendar.get(Calendar.MINUTE),
                        true
                );

                //dialogを表示
                startTimePickerDialog.show();

            }
        });
        /**************************************************/

        /*終了時間EditTextが押されたとき********************/
        endTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Calendarインスタンスを取得
                final Calendar endTimeCalendar = Calendar.getInstance();

                //TimePickerDialogインスタンスを取得
                TimePickerDialog endTimePickerDialog = new TimePickerDialog(
                        InputActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                                //setした時間を取得して表示
                                endTime.setText(String.format("%02d : %02d", hourOfDay, minute));
                                //Calendarオブジェクトを作成
                                mEndAtDatetime.set(Calendar.HOUR_OF_DAY, hourOfDay);
                                mEndAtDatetime.set(Calendar.MINUTE, minute);
                            }
                        },
                        endTimeCalendar.get(Calendar.HOUR_OF_DAY),
                        endTimeCalendar.get(Calendar.MINUTE),
                        true
                );

                //dialogを表示
                endTimePickerDialog.show();

            }
        });
        /**************************************************/

        /*表示色ボタンが押されたとき*********************/
        color1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色選択画面へ遷移
                startActivity(intentOut);
            }
        });
        color2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色選択画面へ遷移
                startActivity(intentOut);
            }
        });
        /************************************************/

        /*詳細ボタンが押されたとき************************/
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //詳細入力表示に
                detailButton.setVisibility(View.GONE);
                myBudget.setVisibility(View.VISIBLE);
                price.setVisibility(View.VISIBLE);
                place.setVisibility(View.VISIBLE);
                memo.setVisibility(View.VISIBLE);
                picture.setVisibility(View.VISIBLE);
            }
        });
        /************************************************/

        /*完了ボタンが押されたとき************************/
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //保存処理を実行
                mInputPresenter.saveSchedule(title.getText().toString(), memo.getText().toString(), mStartAtDatetime.getTime(), mEndAtDatetime.getTime(), 0, 0);
                //カレンダー表示画面に遷移
            }
        });
        /************************************************/

        /*キャンセルボタンが押されたとき******************/
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //カレンダー表示画面に遷移
                finish();
            }
        });
        /************************************************/

        /*********************************************************************************/

    }

    @Override
    public void finishInput() {
        finish();
    }

    @Override
    public void setPresenter(InputContract.Presenter presenter) {
        mInputPresenter = presenter;
    }

    /**
     * タッチイベントを取得し、フォーカスエリア外をタッチするとキーボードを閉じる
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        View view = getCurrentFocus();
        if (view != null && (ev.getAction() == MotionEvent.ACTION_UP || ev.getAction() == MotionEvent.ACTION_MOVE) && view instanceof EditText && !view.getClass().getName().startsWith("android.webkit.")) {
            int scrcoords[] = new int[2];
            view.getLocationOnScreen(scrcoords);
            float x = ev.getRawX() + view.getLeft() - scrcoords[0];
            float y = ev.getRawY() + view.getTop() - scrcoords[1];
            if (x < view.getLeft() || x > view.getRight() || y < view.getTop() || y > view.getBottom())
                ((InputMethodManager) this.getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow((this.getWindow().getDecorView().getApplicationWindowToken()), 0);
        }
        return super.dispatchTouchEvent(ev);
    }
}
