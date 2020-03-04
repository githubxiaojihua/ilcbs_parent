package com.xiaojihua.erp.job;

import com.xiaojihua.utils.UtilFuns;

import java.text.ParseException;
import java.util.Date;

public class TestJob {
    public void showTime() throws ParseException {
        System.out.println(UtilFuns.dateTimeFormat(new Date(),"HH:mm:ss"));
    }
}
