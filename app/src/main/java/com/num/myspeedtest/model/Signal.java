package com.num.myspeedtest.model;

public class Signal {
    private static String signal = "-1";

    public static String getSignal() {
        return signal;
    }

    public static void setSignal(String signal) {
        Signal.signal = signal;
    }
}
