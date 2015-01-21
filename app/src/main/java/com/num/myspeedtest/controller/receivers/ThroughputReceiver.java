package com.num.myspeedtest.controller.receivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.widget.TextView;

import com.mobilyzer.MeasurementResult;
import com.mobilyzer.UpdateIntent;
import com.mobilyzer.measurements.TCPThroughputTask;
import com.num.myspeedtest.R;
import com.num.myspeedtest.controller.helpers.Logger;
import com.num.myspeedtest.controller.helpers.ThroughputHelper;
import com.num.myspeedtest.view.activities.ThroughputActivity;

/**
 * Created by joseph on 1/21/15.
 */
public class ThroughputReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
//        ThroughputActivity.getInstace();
//        TextView percentage = (TextView) findViewById(R.id.text_pvalue);
//        Parcelable[] parcels = intent.getParcelableArrayExtra(UpdateIntent.RESULT_PAYLOAD);
//        MeasurementResult[] results;
//        TCPThroughputTask.TCPThroughputDesc desc;
//        Logger.show("Throughput Activity Received");
//        if(parcels != null) {
//            results = new MeasurementResult[parcels.length];
//            for(int i=0; i<results.length; i++) {
//                results[i] = (MeasurementResult) parcels[i];
//                String throughputJSON = results[i].getValues().get("tcp_speed_results");
//                desc = (TCPThroughputTask.TCPThroughputDesc) results[i].getMeasurementDesc();
//                long tp = (long) (desc.calMedianSpeedFromTCPThroughputOutput(throughputJSON));
////                    System.out.println("Throughput Activity: " + desc.dir_up + " tp: " + ThroughputHelper.outputString(tp) + " timeout: " + desc.tcp_timeout_sec + "down: " + desc.data_limit_mb_down + "up: " + desc.data_limit_mb_up);
//                if(!desc.dir_up) {
//                    Logger.show("Throughput Activity Down");
//                    downSpeed.setText(ThroughputHelper.outputString(tp));
//                    isRunningDown = false;
//                }else {
//                    Logger.show("Throughput Activity Up");
//                    upSpeed.setText(ThroughputHelper.outputString(tp));
//                    isRunningUp = false;
//                }
//                if(!isRunningDown && !isRunningUp){
//                    startButtonImage.setImageResource(R.drawable.ic_action_replay);
//                    startButtonTxt.setText("Start");
//                    startButton.setClickable(true);
//                    progressBar.setProgress(100);
//                    percentage.setText("Test Complete");
//                }
//            }
//        }
    }
}