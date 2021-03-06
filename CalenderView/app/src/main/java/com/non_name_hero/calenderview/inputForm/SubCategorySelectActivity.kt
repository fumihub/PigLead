package com.non_name_hero.calenderview.inputForm

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import com.non_name_hero.calenderview.R
import com.non_name_hero.calenderview.data.BalanceCategory
import com.non_name_hero.calenderview.data.CategoryData
import com.non_name_hero.calenderview.data.ScheduleGroup
import com.non_name_hero.calenderview.data.source.ScheduleDataSource
import com.non_name_hero.calenderview.data.source.ScheduleRepository
import com.non_name_hero.calenderview.utils.Injection
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDeleteDialog
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogBase
import com.non_name_hero.calenderview.utils.dialogUtils.PigLeadDialogFragment
import java.lang.Boolean.TRUE
import java.util.ArrayList

class SubCategorySelectActivity  /*コンストラクタ*/
    : AppCompatActivity(), PigLeadDeleteDialog {

    private lateinit var context: Context                           /*SubCategorySelectActivityのcontext*/


    private lateinit var ListAdapter: SubCategoryListAdapter        /*サブカテゴリーグループリストアダプタ*/

    private lateinit var listView: ListView                         /*サブカテゴリーグループリストビュー*/

    private lateinit var editButton: Button                         /*サブカテゴリーグループ編集ボタン*/
    private lateinit var subcategoryCreateButton: Button            /*サブカテゴリーグループ作成ボタン*/

    private lateinit var repository: ScheduleRepository             /**/

    private var categoryId = 0                                      /*カテゴリID*/

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /*初期設定***************************************/
        context = this
        repository = Injection.provideScheduleRepository(context)
        setContentView(R.layout.sub_category_select)
        val myToolbar = findViewById<View>(R.id.subCategorySelectToolbar) as Toolbar
        editButton = findViewById(R.id.editButton)
        subcategoryCreateButton = findViewById(R.id.subcategoryCreateButton)
        setSupportActionBar(myToolbar)

        /*ListViewのIDを取得*/
        listView = findViewById(R.id.listView)

        /*リストのアダプターを使用してViewを作成*/
        ListAdapter = SubCategoryListAdapter(context, this)
        //削除ダイアログを設定
        ListAdapter.deleteDialog = this
        listView.adapter = ListAdapter

        /*DBから情報を取得*/
        loadCategoriesDataList()
        /************************************************/

        /*編集ボタンが押されたとき***********************/
        editButton.setOnClickListener {
            /*ボタンの文字が「編集」ならば*/
            if (editButton.text.toString() == "編集") {
                /*リストビューに削除ボタン表示*/
                jdgEditMode(java.lang.Boolean.TRUE, "完了")
                /*サブカテゴリー作成ボタン非表示*/
                subcategoryCreateButton.visibility = View.GONE
            } else {
                /*リストビューから削除ボタンを非表示に*/
                jdgEditMode(java.lang.Boolean.FALSE, "編集")
                /*サブカテゴリー作成ボタン表示*/
                subcategoryCreateButton.visibility = View.VISIBLE
            }
        }
        /************************************************/

        /*サブカテゴリー作成ボタンが押されたとき**************/
        subcategoryCreateButton.setOnClickListener {
            /*サブカテゴリー作成ポップアップ表示*/
            val subCategoryEditText: EditText = EditText(this);
            subCategoryEditText.hint = "サブカテゴリー名"
            AlertDialog.Builder(this)
                .setTitle("サブカテゴリー作成")
                .setMessage("サブカテゴリー名を入力してください。")
                .setView(subCategoryEditText)
                .setPositiveButton("保存", DialogInterface.OnClickListener {_, _ ->
                        repository.insertBalanceCategory(
                                BalanceCategory(
                                        TRUE,
                                        subCategoryEditText.text.toString(),
                                        categoryId
                                ),
                                object : ScheduleDataSource.SaveBalanceCategoryCallback {
                                    override fun onBalanceCategorySaved() {
                                        //リストビュー更新
                                        loadCategoriesDataList()
                                    }

                                    override fun onDataNotSaved() {}
                                }
                        )
                })
                .show();
        }
        /************************************************/
    }

    /*TODO SharedPreferenceからViewModelに変更*/
    /*編集モードかを判定する関数********************/
    private fun jdgEditMode(value: Boolean, str: String) {
        /*SharedPreferenceでeditFlagの値を変更*/
        val prefs = getSharedPreferences("input_data", MODE_PRIVATE)
        val editor = prefs.edit()
        /*SharedPreferenceにeditFlagの値を保存*/
        editor.putBoolean("editFlag", value)
        /*非同期処理ならapply()、同期処理ならcommit()*/
        editor.apply()
        listView.adapter = ListAdapter
        /*ボタン文字の切り替え(編集/完了)*/
        editButton.text = str
    }
    /************************************************/

//    /*サブカテゴリー作成画面遷移関数*****************************/
//    private fun goSubCategoryCreateActivity() {
//        /*サブカテゴリー作成画面用intent*/
//        val intentOut = Intent(this, SubCategoryCreateActivity::class.java)
//        /*カテゴリーIDを引数でサブカテゴリー画面に渡す*/
//        intentOut.putExtra("categoryID", /*TODO カテゴリーID*/)
//        /*戻り値を設定してサブカテゴリー作成画面に遷移*/
//        startActivityForResult(intentOut, REQUEST_CODE)
//    }
//    /************************************************/

//    /*Activityから戻ってきてからList内容を更新する関数*/
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        when (requestCode) {
//            REQUEST_CODE -> when (resultCode) {
//                RESULT_OK -> {
//                    /*DB問い合わせて更新*/
//                    loadCategoriesDataList()
//                }
//                RESULT_CANCELED -> {
//                    /*キャンセルボタンを押して戻ってきたときの処理*/
//                }
//                else -> {
//                    /*その他*/
//                }
//            }
//            else -> {
//            }
//        }
//    }
//    /************************************************/

    /*List内容を更新する関数************************/
    private fun loadCategoriesDataList() {
        /*表示するカテゴリIDを取得*/
        val intentIn = intent
        categoryId = intentIn.getIntExtra("CategoryID", 21)
        repository.getCategoriesData(categoryId,object : ScheduleDataSource.GetCategoriesDataCallback {
            override fun onCategoriesDataLoaded(CategoryData: List<CategoryData>) {
                //取得後の処理
                ListAdapter.setList(CategoryData)
            }

            override fun onDataNotAvailable() {}
        })
    }
    /************************************************/

    /**
     * 削除用ダイアログを設定
     *
     * @param callback ダイアログのボタン押下時の処理
     * @return dialog DialogFragmentのオブジェクト
     */
    override fun getDeleteDialog(scheduleGroup: ScheduleGroup?, callback: PigLeadDialogBase.DialogCallback): PigLeadDialogFragment? {
        /*表示させるメッセージの定義*/
        val dialogMessages = ArrayList(listOf(*resources.getStringArray(R.array.delete_schedule_group_dialog_massage)))
        /*表示メッセージに削除対象の名前を挿入*/
        dialogMessages[0] = String.format(dialogMessages[0], scheduleGroup?.groupName)
        val positiveBtnMessage = getString(R.string.delete_schedule_group_positive)
        val negativeBtnMessage = getString(R.string.delete_schedule_group_negative)
        /*AlertDialogを設定*/
        val dialog = PigLeadDialogFragment(context)
        dialog.setDialogMessage(dialogMessages)
                .setPositiveBtnMessage(positiveBtnMessage)
                .setNegativeBtnMessage(negativeBtnMessage)
                .setPositiveClickListener { dialog, which -> callback.onClickPositiveBtn() }
                .setNegativeClickListener { dialog, which -> callback.onClickNegativeBtn() }
        return dialog
    }
    /************************************************/

    /*削除警告ダイアログ表示関数*********************/
    override fun showPigLeadDiaLog(dialog: PigLeadDialogFragment?) {
        /*AlertDialogを表示*/
        dialog?.show(supportFragmentManager, DELETE_DIALOG_TAG)
    }
    /************************************************/

    /*定数定義***************************************/
    companion object {
        private const val REQUEST_CODE = 1
        private const val DELETE_DIALOG_TAG = "DELETE_DIALOG"
    }
    /************************************************/
}