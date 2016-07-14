package com.pcjinrong.pcjr.action;

/**
 * Created by Mario on 2016/7/14.
 */
public class NoLoginExecption extends RuntimeException {

    public NoLoginExecption() {
    }

    public NoLoginExecption(String detailMessage) {
        super(detailMessage);
    }
}
