package com.felix.simplebook.fragment;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.felix.simplebook.R;
import com.felix.simplebook.base.BaseFragment;
import com.felix.simplebook.presenter.BackUpPresenter;
import com.felix.simplebook.presenter.IBackUpPresenter;
import com.felix.simplebook.utils.MyLog;
import com.felix.simplebook.utils.MyToast;
import com.felix.simplebook.view.IBackupView;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

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

    @BindView(R.id.ll_restore_fragment_backup)
    LinearLayout llRestore;
    @BindView(R.id.et_file_path_restore_fragment_backup)
    EditText etRestorePath;
    @BindView(R.id.btn_restore_fragment_backup)
    Button btnStore;
    @BindView(R.id.tv_restore_message_fragment_backup)
    TextView tvRestoreMessage;

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
                //write data
                presenter.databaseToFile(etBackUpPath.getText().toString(), name);
                btnBackUp.setBackgroundResource(R.drawable.unbtn_ok_shape);
                btnBackUp.setEnabled(false);
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
                        .withFilter(Pattern.compile(".*\\.bu$"))
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
                //read data
                presenter.fileToDatabase(etRestorePath.getText().toString());
                btnStore.setBackgroundResource(R.drawable.unbtn_ok_shape);
                btnStore.setEnabled(false);
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
        }else if(requestCode == RESTORE_REQUEST){
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
}
