package com.weidi.util;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;

import com.weidi.R;


/**
 *@author  luochangdong  E-mail: 2270333671@qq.com
 *@date 创建时间：2015-6-25 下午4:14:55
 *@Description 1.0
 */
public abstract class XmppLoadThread {

	boolean isHint;
	ProgressDialog mdialog;
	private Context context;
//	private ExecutorService FULL_TASK_EXECUTOR;

	public XmppLoadThread(){
		new AsyncTask<Void, Integer, Object>(){

			@Override
			protected Object doInBackground(Void... params) {
				load();
				return null;
			}
			
			@Override
			protected void onPostExecute(Object result) {
			    result(result);
				super.onPostExecute(result);
			}
		};
	}
	@SuppressLint("NewApi")
	public XmppLoadThread(Context _mcontext) {
		isHint = true;
		context = _mcontext;
//		FULL_TASK_EXECUTOR = (ExecutorService) Executors.newCachedThreadPool();
		new AsyncTask<Void, Integer, Object>() {

			@Override
			protected Object doInBackground(Void... arg0) {
				return load();
			}

			@Override
			protected void onPostExecute(Object result) {
				if (isHint && (mdialog == null || !mdialog.isShowing())) {
					return;
				} else {
					try {
						result(result);
						if (isHint && (mdialog != null && mdialog.isShowing())) {
							mdialog.dismiss();

						}
					} catch (Throwable e) {
						e.printStackTrace();
					}
				}
			}

			@Override
			protected void onPreExecute() {
				if (isHint) {
					try {
						mdialog =  ProgressDialog.show(context, context.getResources().getString(R.string.dialog_title), context
								.getResources().getString(R.string.dialog_load_content));
						mdialog.setCancelable(true);
						mdialog.setContentView(R.layout.dialog_loadding);
						mdialog.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.progress_dialog_style));
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}

		}.execute();
	}

	protected abstract Object load();

	protected abstract void result(Object object);

}