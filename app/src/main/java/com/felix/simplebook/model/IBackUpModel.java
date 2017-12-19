package com.felix.simplebook.model;

import com.felix.simplebook.callback.ICallBacking;

/**
 * Created by chaofei.xue on 2017/12/11.
 */

public interface IBackUpModel {
    void databaseToFile(ICallBacking<String> callBack, String...path);
    void fileToDatabase(ICallBacking<String> callBack, String...path);
}
