package com.qlzx.mylibrary.util;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.manager.SupportRequestManagerFragment;
import com.qlzx.mylibrary.R;
import com.qlzx.mylibrary.widget.SDProgressDialog;
import com.qlzx.mylibrary.widget.payDialog.PayFragment;
import com.qlzx.mylibrary.widget.payDialog.PayPwdView;

import org.greenrobot.eventbus.util.ErrorDialogManager;


public class DialogUtil
{

	private Context mActivity = null;

	private static PayFragment fragment = null ;

	public DialogUtil(Context activity)
	{
		this.mActivity = activity;
	}


	/**
	 * 弹出密码框   使用方法： 1，调用的activity实现PayPwdView.InputCallBack, 然后在实现的方法中写密码输入完成后的操作
	 * msg 是密码框下边的提示，    可以用DialogUtil直接调用这个方法。
	 * @param click
	 * @param msg
	 * @param fragmentManager
	 * @param
	 */

	public static void showPayDialog(PayPwdView.InputCallBack click, String hint,String cash,String msg,FragmentManager fragmentManager){
		Bundle bundle = new Bundle();
		bundle.putString(PayFragment.EXTRA_CONTENT, msg);
		bundle.putString(PayFragment.EXTRA_HINT, hint);
		bundle.putString(PayFragment.EXTRA_CASH, cash);
		fragment = new PayFragment();
		fragment.setArguments(bundle);
		fragment.setPaySuccessCallBack(click);
		fragment.show(fragmentManager ,"Pay");
    }
    public static void dismissPayDialog(){
		if (fragment!=null){
				fragment.disappear();
		}

	}

	public Dialog alert(CharSequence title, CharSequence message)
	{
		Builder builder = new Builder(mActivity);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		Dialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public Dialog alert(int title, int message)
	{

		Builder builder = new Builder(mActivity);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNegativeButton("确定", new DialogInterface.OnClickListener()
		{
			public void onClick(DialogInterface dialog, int which)
			{
				dialog.dismiss();
			}
		});
		Dialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public Dialog confirm(String title, String message, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener)
	{
		Builder builder = new Builder(mActivity);
		builder.setTitle(title);
		builder.setMessage(message);

		try
		{
			builder.setPositiveButton("确定", confirmListener);
		} catch (Exception e)
		{
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
		}
		try
		{
			builder.setNegativeButton("取消", cancelListener);
		} catch (Exception e)
		{
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
		}

		Dialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public Dialog confirm(int title, int message, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener)
	{
		Builder builder = new Builder(mActivity);
		builder.setTitle(title);
		builder.setMessage(message);
		try
		{
			builder.setPositiveButton("确定", confirmListener);
		} catch (Exception e)
		{
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
		}
		try
		{
			builder.setNegativeButton("取消", cancelListener);
		} catch (Exception e)
		{
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
		}

		Dialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	// 弹出自定义的窗体
	public Dialog showView(CharSequence title, View view, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener)
	{
		Builder builder = new Builder(mActivity);
		if (title != "")
			builder.setTitle(title);
		builder.setView(view);

		try
		{
			builder.setPositiveButton("确定", confirmListener);
		} catch (Exception e)
		{
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
		}
		try
		{
			builder.setNegativeButton("取消", cancelListener);
		} catch (Exception e)
		{
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
		}

		Dialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public Dialog showView(int title, View view, DialogInterface.OnClickListener confirmListener, DialogInterface.OnClickListener cancelListener)
	{
		Builder builder = new Builder(mActivity);
		if (title != 0)
			builder.setTitle(title);
		builder.setView(view);

		try
		{
			builder.setPositiveButton("确定", confirmListener);
		} catch (Exception e)
		{
			builder.setPositiveButton("确定", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
		}
		try
		{
			builder.setNegativeButton("取消", cancelListener);
		} catch (Exception e)
		{
			builder.setNegativeButton("取消", new DialogInterface.OnClickListener()
			{

				@Override
				public void onClick(DialogInterface dialog, int which)
				{
					dialog.dismiss();
				}
			});
		}

		Dialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	// 弹出自定义的信息
	public Dialog showMsg(CharSequence title, CharSequence message)
	{
		Builder builder = new Builder(mActivity);
		if (title != "")
			builder.setTitle(title);
		builder.setMessage(message);
		Dialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public Dialog showMsg(int title, int message)
	{
		Builder builder = new Builder(mActivity);
		if (title != 0)
			builder.setTitle(title);
		builder.setMessage(message);
		Dialog dialog = builder.create();
		dialog.show();
		return dialog;
	}

	public Dialog showLoading(String message)
	{
		SDProgressDialog dialog = new SDProgressDialog(mActivity, R.style.dialogBase);
		TextView txt = dialog.getmTxtMsg();
		if (message != null && txt != null)
		{
			txt.setText(message);
		}
		dialog.show();
		return dialog;
	}

}
