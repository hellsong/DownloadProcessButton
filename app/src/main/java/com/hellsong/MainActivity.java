package com.hellsong;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;

import com.helsong.library.ProcessButton;

public class MainActivity extends Activity implements View.OnClickListener {

    private Button mSetNormalButton;
    private Button mSetProcessAndIncrementButton;
    private Button mSetCompleteButton;
    private Button mSetErrorButton;
    private Button mResetButton;

    private ProcessButton mProcessButton;
    private ProcessButton mProcessButton2;
    private ProcessButton mProcessButton3;

    private Handler mHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initView();
    }

    private void initView() {
        mSetNormalButton = (Button) findViewById(R.id.set_normal_button);
        mSetProcessAndIncrementButton = (Button) findViewById(R.id.set_process_button);
        mSetCompleteButton = (Button) findViewById(R.id.set_complete_button);
        mSetErrorButton = (Button) findViewById(R.id.set_error_button);
        mProcessButton = (ProcessButton) findViewById(R.id.process_button);
        mProcessButton2 = (ProcessButton) findViewById(R.id.process_button_2);
        mProcessButton3 = (ProcessButton) findViewById(R.id.process_button_3);
        mResetButton = (Button) findViewById(R.id.reset_button);

        mSetNormalButton.setOnClickListener(this);
        mSetProcessAndIncrementButton.setOnClickListener(this);
        mSetCompleteButton.setOnClickListener(this);
        mSetErrorButton.setOnClickListener(this);
        mResetButton.setOnClickListener(this);
        mProcessButton.setOnClickListener(this);
        mProcessButton2.setOnClickListener(this);
        mProcessButton3.setOnClickListener(this);

        //Setup process button.
////        mProcessButton.setClickable(true);
//        mProcessButton.setDrawHorizontal(false);
//        mProcessButton.setNormalText("normal");
//        mProcessButton.setProcessText("I am Processing!");
//        mProcessButton.setCompleteText("Complete.");
//        mProcessButton.setErrorText("Error!");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.set_normal_button: {
                mProcessButton.setButtonState(ProcessButton.ButtonState.NORMAL);
                mProcessButton2.setButtonState(ProcessButton.ButtonState.NORMAL);
                mProcessButton3.setButtonState(ProcessButton.ButtonState.NORMAL);
                break;
            }
            case R.id.set_process_button: {
                mProcessButton.setButtonState(ProcessButton.ButtonState.PROCESSING);
                mProcessButton2.setButtonState(ProcessButton.ButtonState.PROCESSING);
                mProcessButton3.setButtonState(ProcessButton.ButtonState.PROCESSING);
                break;
            }
            case R.id.set_complete_button: {
                mProcessButton.setButtonState(ProcessButton.ButtonState.COMPLETE);
                mProcessButton2.setButtonState(ProcessButton.ButtonState.COMPLETE);
                mProcessButton3.setButtonState(ProcessButton.ButtonState.COMPLETE);
                break;
            }
            case R.id.set_error_button: {
                mProcessButton.setButtonState(ProcessButton.ButtonState.ERROR);
                mProcessButton2.setButtonState(ProcessButton.ButtonState.ERROR);
                mProcessButton3.setButtonState(ProcessButton.ButtonState.ERROR);
                break;
            }
            case R.id.reset_button: {
                mProcessButton.setButtonState(ProcessButton.ButtonState.NORMAL);
                mProcessButton.setProcess(0);
                mProcessButton2.setButtonState(ProcessButton.ButtonState.NORMAL);
                mProcessButton2.setProcess(0);
                mProcessButton3.setButtonState(ProcessButton.ButtonState.NORMAL);
                mProcessButton3.setProcess(0);
                break;
            }
            case R.id.process_button: {
                mProcessButton.setButtonState(ProcessButton.ButtonState.PROCESSING);
                new DownloadThread(mProcessButton).start();
                mProcessButton.setEnabled(false);
                break;
            }
            case R.id.process_button_2: {
                mProcessButton2.setButtonState(ProcessButton.ButtonState.PROCESSING);
                new DownloadThread(mProcessButton2).start();
                mProcessButton2.setEnabled(false);
                break;
            }
            case R.id.process_button_3: {
                mProcessButton3.setButtonState(ProcessButton.ButtonState.PROCESSING);
                new DownloadThread(mProcessButton3).start();
                mProcessButton3.setEnabled(false);
                break;
            }
        }
    }

    private class DownloadThread extends Thread {
        private ProcessButton mInnerProcessButton;

        public DownloadThread(ProcessButton processButton) {
            mInnerProcessButton = processButton;
        }

        @Override
        public void run() {
            super.run();
            while (mInnerProcessButton.getButtonState() == ProcessButton.ButtonState.PROCESSING && mInnerProcessButton.getCurrentProgress() <= 100) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        mInnerProcessButton.setProcess(mInnerProcessButton.getCurrentProgress() + 5);
                    }
                });
                try {
                    sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mInnerProcessButton.setEnabled(true);
                    if (mInnerProcessButton.getCurrentProgress() >= 100) {
                        mInnerProcessButton.setButtonState(ProcessButton.ButtonState.COMPLETE);
                    }
                }
            });
        }
    }
}
