package com.felix.simplebook.fragment;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseFragment;
import com.felix.simplebook.callback.IOnClickListener;
import com.felix.simplebook.presenter.BackUpPresenter;
import com.felix.simplebook.presenter.IBackUpPresenter;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.utils.MyToast;
import com.felix.simplebook.utils.SpinnerPopWindow;
import com.felix.simplebook.view.IBackupView;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import butterknife.BindView;

/**
 * Created by chaofei.xue on 2017/12/3.
 */

public class BackupFragment extends BaseFragment implements IBackupView {
    @BindView(R.id.ll_backup_fragment_backup)
    LinearLayout llBackUp;
    @BindView(R.id.et_file_name_fragment_backup)
    EditText etName;
    @BindView(R.id.et_file_path_fragment_backup)
    EditText etBackUpPath;
    @BindView(R.id.btn_backup_fragment_backup)
    Button btnBackUp;
    @BindView(R.id.tv_backup_message_fragment_backup)
    TextView tvBackUpMessage;
    @BindView(R.id.et_backup_type_name_fragment_backup)
    EditText etBackupType;

    @BindView(R.id.ll_restore_fragment_backup)
    LinearLayout llRestore;
    @BindView(R.id.et_file_path_restore_fragment_backup)
    EditText etRestorePath;
    @BindView(R.id.btn_restore_fragment_backup)
    Button btnStore;
    @BindView(R.id.tv_restore_message_fragment_backup)
    TextView tvRestoreMessage;
    @BindView(R.id.et_restore_type_name_fragment_backup)
    EditText etRestoreType;

    @BindView(R.id.rg_fragment_backup)
    RadioGroup radioGroup;
    @BindView(R.id.radio_backup_fragment_backup)
    RadioButton rbBuckUp;
    @BindView(R.id.radio_restore_fragment_backup)
    RadioButton rbRestore;

    private final static int BACKUP_REQUEST = 1000;
    private final static int RESTORE_REQUEST = 2000;

    public final static int TYPE_BACKUP = 1;
    public final static int TYPE_RESTORE = 2;

    private IBackUpPresenter presenter;
    private String backUpPath;
    private String restorePath;

    @Override
    public View initLayout(LayoutInflater inflater, ViewGroup container) {
        View view = inflater.inflate(R.layout.fragment_backup, container, false);
        return view;
    }

    @Override
    public void initView() {
        rbBuckUp.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int id) {
                switch (id) {
                    case R.id.radio_backup_fragment_backup:
                        rbBuckUp.setChecked(true);
                        llBackUp.setVisibility(View.VISIBLE);
                        llRestore.setVisibility(View.GONE);
                        break;
                    case R.id.radio_restore_fragment_backup:
                        rbRestore.setChecked(true);
                        llBackUp.setVisibility(View.GONE);
                        llRestore.setVisibility(View.VISIBLE);
                        break;
                }
            }
        });
        presenter = new BackUpPresenter(BackupFragment.this);
        List<String> lists = new ArrayList<>();
        lists.add("默认格式");
        lists.add("Excel格式");
        setSpinner(lists, etBackupType);
        setSpinner(lists, etRestoreType);
    }

    @Override
    public void initData() {
        //NO.1 BackUp Code
        etBackUpPath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withSupportFragment(BackupFragment.this)
                        .withRequestCode(BACKUP_REQUEST)
                        .withHiddenFiles(true)
                        .withFilter(Pattern.compile(".*\\.bcxcdd020f212dcdvfu$"))
                        .withFilterDirectories(false)
                        .start();
            }
        });
        btnBackUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = etName.getText().toString().trim();
                if (name.equals("") || name == null) {
                    MyToast.makeText(getActivity(), "请输入文件名", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etBackUpPath.getText().toString().equals(getResources()
                        .getString(R.string.file_message))) {
                    MyToast.makeText(getActivity(), "请选择保存路径", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etBackupType.getText().toString().equals(getResources()
                        .getString(R.string.default_type))) {
                    //write data default
                    presenter.databaseToFile(etBackUpPath.getText().toString(), name);
                    btnBackUp.setBackgroundResource(R.drawable.unbtn_ok_shape);
                    btnBackUp.setEnabled(false);
                } else {
                    //write data excel
                    presenter.databaseToExcel(etBackUpPath.getText().toString()
                            .trim(), name);
                    btnBackUp.setBackgroundResource(R.drawable.unbtn_ok_shape);
                    btnBackUp.setEnabled(false);
                }
            }
        });

        //NO.2 Restore Code
        etRestorePath.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new MaterialFilePicker()
                        .withSupportFragment(BackupFragment.this)
                        .withRequestCode(RESTORE_REQUEST)
                        .withHiddenFiles(true)
                        .withFilter(Pattern.compile(".*\\.bu$||.*\\.xls$"))
                        .withFilterDirectories(false)
                        .start();
            }
        });

        btnStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (etRestorePath.getText().toString().equals(getResources()
                        .getString(R.string.file_message_2))) {
                    MyToast.makeText(getActivity(), "请先选择需要还原的文件", Toast.LENGTH_SHORT)
                            .show();
                    return;
                }
                if (etRestoreType.getText().toString().equals(getResources()
                        .getString(R.string.default_type))) {
                    //read data default
                    presenter.fileToDatabase(etRestorePath.getText().toString());
                    btnStore.setBackgroundResource(R.drawable.unbtn_ok_shape);
                    btnStore.setEnabled(false);
                } else {
                    //read data excel
                    presenter.excelToDatabase(etRestorePath.getText().toString().trim());
                    btnStore.setBackgroundResource(R.drawable.unbtn_ok_shape);
                    btnStore.setEnabled(false);
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        bind.unbind();
    }

    @Override
    public String initTitle() {
        return mContext.getResources().getString(R.string.backup);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BACKUP_REQUEST) {
            if (data != null) {
                backUpPath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                MyLog.info("data:", backUpPath);
                etBackUpPath.setText(backUpPath);
            }
        } else if (requestCode == RESTORE_REQUEST) {
            if (data != null) {
                restorePath = data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH);
                MyLog.info("data:", restorePath);
                etRestorePath.setText(restorePath);
            }
        }
    }

    @Override
    public void showMessage(final String msg) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyToast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                if (msg.equals("还原完成")) {
                    //刷新数据
                    Intent intent = new Intent("com.felix.simplebook.successful");
                    intent.putExtra("what", "init");
                    mContext.sendBroadcast(intent);
                    //按钮使能
                    btnStore.setEnabled(true);
                    btnStore.setBackgroundResource(R.drawable.btn_ok_shape);
                } else if (msg.equals("还原失败") || msg.equals("备份失败")
                        || msg.equals("备份完成")) {
                    //按钮使能
                    btnStore.setEnabled(true);
                    btnStore.setBackgroundResource(R.drawable.btn_ok_shape);
                    btnBackUp.setEnabled(true);
                    btnBackUp.setBackgroundResource(R.drawable.btn_ok_shape);
                }
            }
        });
    }

    @Override
    public void showInfo(final int type, final String info) {
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (type == TYPE_BACKUP) {
                    tvBackUpMessage.setText(info);
                } else if (type == TYPE_RESTORE) {
                    tvRestoreMessage.setText(info);
                }
            }
        });
    }

    public void setSpinner(List<String> list, final EditText typeEt) {
        final SpinnerPopWindow mSpinnerPopWindow = new SpinnerPopWindow(mContext, list);
        mSpinnerPopWindow.setListener(new ClickListener(typeEt, mSpinnerPopWindow));
        mSpinnerPopWindow.setOnDismissListener(new DismissListener(typeEt));
        typeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSpinnerPopWindow.setWidth(typeEt.getWidth());
                mSpinnerPopWindow.showAsDropDown(typeEt);
                setTextImage(R.drawable.spinner_write_up, typeEt);
            }
        });
    }

    private class DismissListener implements PopupWindow.OnDismissListener {
        private EditText typeEt;

        public DismissListener(EditText typeEt) {
            this.typeEt = typeEt;
        }

        @Override
        public void onDismiss() {
            setTextImage(R.drawable.spinner_write_down, typeEt);
        }
    }

    private class ClickListener implements IOnClickListener {
        EditText typeEt;
        SpinnerPopWindow mSpinnerPopWindow;

        public ClickListener(EditText typeEt, SpinnerPopWindow mSpinnerPopWindow) {
            this.typeEt = typeEt;
            this.mSpinnerPopWindow = mSpinnerPopWindow;
        }

        @Override
        public void onClick(String value) {
            typeEt.setText(value);
            mSpinnerPopWindow.dismiss();
        }
    }

    private void setTextImage(int resId, EditText typeEt) {
        Drawable drawable = getResources().getDrawable(resId);
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        typeEt.setCompoundDrawables(null, null, drawable, null);
    }

}
