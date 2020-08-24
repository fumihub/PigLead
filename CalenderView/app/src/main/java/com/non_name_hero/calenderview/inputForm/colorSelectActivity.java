package com.non_name_hero.calenderview.inputForm;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.non_name_hero.calenderview.R;
import com.non_name_hero.calenderview.calendar.CalendarAdapter;

import java.util.ArrayList;
import java.util.List;

public class colorSelectActivity extends AppCompatActivity {

    private listAdapter mListAdapter;
    private ListView listView;

    private static final int REQUEST_CODE = 1;

    private Button noCategoryButton;
    private Button colorCreateButton;

    private Intent intentOut;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.color_select);
        final Toolbar myToolbar = (Toolbar) findViewById(R.id.colorSelectToolbar);
        setSupportActionBar(myToolbar);

        //ListViewのIDを取得
        listView = findViewById(R.id.listView);

        //リストのアダプターを使用してViewを作成-
        mListAdapter = new listAdapter(getApplicationContext(), this);
        listView.setAdapter(mListAdapter);

        //色作成画面用intent
        intentOut = new Intent(this, colorCreateActivity.class);

        noCategoryButton = findViewById(R.id.noCategoryButton);
        colorCreateButton = findViewById(R.id.colorCreateButton);
/*

        */
/*categoryListのボタンが押されたとき******************//*

        for (Button b :categoryList) {
            b.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //input画面に遷移
                        returnInputActivity(v);
                    }
                });
        }
        */
/************************************************//*

        /*未分類ボタンが押されたとき************************/
        noCategoryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //input画面に遷移
                //色選択遷移用intent
                intentOut = new Intent();
                //ボタンの色番号を遷移先へreturn
                //intentOut.putExtra("ColorNumber", colorNumber);
                setResult(RESULT_OK, intentOut);
                finish();
            }
        });
        /************************************************/

        /*色作成ボタンが押されたとき************************/
        colorCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //色作成画面に遷移
                goColorCreateActivity();
            }
        });
        /************************************************/

    }

    public void goColorCreateActivity() {
        //戻り値を設定して色画面に遷移
        startActivityForResult(intentOut, REQUEST_CODE);
    }

    /*public void returnInputActivity(View v) {

        ColorDrawable backgroundColor = (ColorDrawable) ((Button) v).getBackground();
        int colorId = backgroundColor.getColor();
        //色選択遷移用intent
        intentOut = getIntent();
        //ボタンの色IDを遷移先へreturn
        intentOut.putExtra("ColorId", colorId);
        //色タイトルを遷移先へreturn
        intentOut.putExtra("ColorTitle", ((Button) v).getText().toString());
        //文字色を遷移先へreturn
        intentOut.putExtra("textColor", ((Button) v).getCurrentTextColor());
        //ボタンの色番号を遷移先へreturn
        intentOut.putExtra("ColorNumber", colorNumber);
        setResult(RESULT_OK, intentOut);
        finish();

    }*/

    //Activityから戻り値(色、色タイトル、文字色、色番号)を受け取る処理
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            //colorCreateActivityから戻ってきた場合
            case (REQUEST_CODE):
                if (resultCode == RESULT_OK) {
                    /*ここから*/
                    //TODO　データベース問い合わせてから、Adapter更新
                    /*//色タイトルの受け取り
                    String colorTitle = data.getStringExtra("ColorTitle");
                    //ボタンの色の受け取り
                    int color = data.getIntExtra("Color",0);
                    //文字色の受け取り
                    String textColor = data.getStringExtra("textColor");
                    //色番号
                    colorNumber = data.getIntExtra("ColorNumber", 0);//defaultValue:ColorNumberキーに値が入っていなかった時に返す値

                    *//*ここまでの値をデータベースに保存*//*
                    *//*ColorNumberも保存しているため、SharedPreferencesを使用する必要はない*//*

                    //色ボタン作成
                    Button colorButton = new Button(this);
                    //テキストスタイルを太字に設定
                    SpannableString spanString = new SpannableString(colorTitle);
                    spanString.setSpan(new StyleSpan(Typeface.BOLD), 0, spanString.length(), 0);
                    colorButton.setText(spanString);
                    colorButton.setBackgroundColor(color);
                    if (textColor.equals("黒")) {
                        colorButton.setTextColor(Color.BLACK);
                    }
                    else {
                        colorButton.setTextColor(Color.WHITE);
                    }
                    //ボタンのmarginを設定
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.WRAP_CONTENT);
                    ViewGroup.MarginLayoutParams marginLayoutParams = (ViewGroup.MarginLayoutParams) layoutParams;
                    marginLayoutParams.setMargins(30, 30, 30, 30);//単位はdp
                    colorButton.setLayoutParams(marginLayoutParams);
                    colorButton.setGravity(Gravity.LEFT);
                    //リニアレイアウトに色ボタン追加
                    linearLayout.addView(colorButton, 1);
                    //色ボタンをカテゴリーリストに追加
                    categoryList.add(colorButton);
                    //クリックリスナーの追加
                    for (Button b :categoryList) {
                        b.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //input画面に遷移
                                returnInputActivity(v);
                            }
                        });
                    }*/
                }
                else if (resultCode == RESULT_CANCELED) {
                    //キャンセルボタンを押して戻ってきたときの処理
                }
                else {
                    //その他
                }
                break;
            default:
                break;
        }
    }
}
