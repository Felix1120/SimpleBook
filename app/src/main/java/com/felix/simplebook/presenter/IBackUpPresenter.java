package com.felix.simplebook.presenter;

/**
 * Created by chaofei.xue on 2017/12/11.
 */

public interface IBackUpPresenter {
    void databaseToFile(String...path);
    void fileToDatabase(String...path);
    void databaseToExcel(String...path);
    void excelToDatabase(String...path);
}
