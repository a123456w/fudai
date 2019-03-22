package com.qlzx.mylibrary.util;

import android.os.Handler;
import android.widget.TextView;

/**
 * 封装了倒计时 Author:heiyue Email:heiyue623@126.com 2015-3-16下午1:28:13
 */
public class SmsButtonUtil {
	private int time_cons = 60; // 初始的时间
	private int time_temp;
	private String btnText; // 原始的按钮文字
	private String afterText;// 倒计时结束后的文字
	private String countDownText; // 倒计时的时候显示的文字
	private TextView btn;

	public SmsButtonUtil(TextView btn) {
		this.btn = btn;
		btnText = btn.getText().toString();
	}

	/**
	 * 设置倒计时结束后的文字，如不设置，默认恢复原始文字
	 * 
	 * @param text
	 */
	public void setAfterText(String text) {
		this.afterText = text;
	}

	/**
	 * 设置倒计时中的文字使用占位符%d来占数字的位置，如不设置默认使用数字
	 * 
	 * @param text
	 */
	public void setCountDownText(String text) {
		this.countDownText = text;
	}

	/**
	 * 开始倒计时
	 */
	public void startCountDown() {
		time_temp = time_cons;
		handler.post(countDownRunnable);
		// 让按钮不可点击
		btn.setEnabled(false);
	}

	/**
	 * 停止倒计时
	 */
	public void cancelCountDown() {
		handler.removeCallbacks(countDownRunnable);
		btn.setEnabled(true);
		if (afterText != null) {
			btn.setText(afterText);
		} else if (btnText != null) {
			btn.setText(btnText);
		}
	}

	private Handler handler = new Handler();

	private Runnable countDownRunnable = new Runnable() {
		@Override
		public void run() {
			if (time_temp <= 0) {
				cancelCountDown();
				return;
			}
			if (countDownText == null) {
				btn.setText(time_temp + "");
			} else {
				btn.setText(String.format(countDownText, time_temp));
			}
			// btn.setText(time_temp + "");
			time_temp--;
			handler.postDelayed(countDownRunnable, 1000);
		}
	};

}
