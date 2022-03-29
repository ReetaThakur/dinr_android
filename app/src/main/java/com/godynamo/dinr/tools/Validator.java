package com.godynamo.dinr.tools;

/**
 * Created by dankovassev on 2015-02-11.
 */

import android.text.InputFilter;
import android.text.TextWatcher;

interface Validator extends TextWatcher, InputFilter {

    public String getValue();
    boolean isValid();
    boolean hasFullLength();

}