package com.felix.simplebook.presenter;

import com.felix.simplebook.callback.ICallBacking;
import com.felix.simplebook.fragment.BackupFragment;
import com.felix.simplebook.model.BackUpModel;
import com.felix.simplebook.model.IBackUpModel;
import com.felix.simplebook.view.IBackupView;

/**
 * Created by chaofei.xue on 2017/12/11.
 */

public class BackUpPresenter implements IBackUpPresenter {
    private IBackupView backupView;
    private IBackUpModel backUpModel;

    private StringBuffer stringBuffer;

    public BackUpPresenter(IBackupView backupView) {
        this.backupView = backupView;
        this.backUpModel = new BackUpModel();
        stringBuffer = new StringBuffer();
    }

    @Override
    public void databaseToFile(String...path) {
        if(path[1].length() > 15){
            backupView.showMessage("文件名过长");
            return;
        }
        stringBuffer.delete(0, stringBuffer.length());
        stringBuffer.append("提示：准备备份" + "\n");
        stringBuffer.append("提示：开始备份" + "\n");
        backUpModel.databaseToFile(new ICallBacking<String>() {
            @Override
            public void successful(String value) {
                backupView.showMessage(value);
                stringBuffer.append("提示：" + value + "\n");
                backupView.showInfo(BackupFragment.TYPE_BACKUP, stringBuffer.toString());
            }

            @Override
            public void error(String value) {
                backupView.showMessage(value);
                stringBuffer.append("提示：" + value + "\n");
                backupView.showInfo(BackupFragment.TYPE_BACKUP, stringBuffer.toString());
            }

            @Override
            public void updateInfo(String info) {
                stringBuffer.append("提示：已备份 "+info + "\n");
                backupView.showInfo(BackupFragment.TYPE_BACKUP, stringBuffer.toString());
            }
        }, path);
    }

    @Override
    public void fileToDatabase(String...path) {
        String[] strings = path[0].split("\\.");
        if(!strings[strings.length-1].equals("bu")){
            backupView.showMessage("请选择正确的.bu备份文件");
            return;
        }
        stringBuffer.delete(0, stringBuffer.length());
        stringBuffer.append("提示：准备还原" + "\n");
        stringBuffer.append("提示：开始还原" + "\n");
        backUpModel.fileToDatabase(new ICallBacking<String>() {
            @Override
            public void successful(String value) {
                backupView.showMessage(value);
                stringBuffer.append("提示："+ value + "\n");
                backupView.showInfo(BackupFragment.TYPE_RESTORE, stringBuffer.toString());
            }

            @Override
            public void error(String value) {
                backupView.showMessage(value);
                stringBuffer.append("提示：" + value + "\n");
                backupView.showInfo(BackupFragment.TYPE_RESTORE, stringBuffer.toString());
            }

            @Override
            public void updateInfo(String info) {
                stringBuffer.append("提示："+info + "\n");
                backupView.showInfo(BackupFragment.TYPE_RESTORE, stringBuffer.toString());
            }
        }, path);
    }
}
