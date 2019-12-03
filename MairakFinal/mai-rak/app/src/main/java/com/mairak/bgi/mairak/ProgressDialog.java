package com.mairak.bgi.mairak;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.view.Window;



/**
 * 
 * @author
 *
 */
public class ProgressDialog {
	private Dialog progressDialog;
	final GifMovieView gif1;
	
	public ProgressDialog(Activity activity) {
		progressDialog = new Dialog(activity, R.style.AppTheme);
		progressDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
//		progressDialog.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//						WindowManager.LayoutParams.FLAG_FULLSCREEN);
		progressDialog.setCancelable(false);
		progressDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
		progressDialog.setContentView(R.layout.progress);
		gif1 = progressDialog.findViewById(R.id.gif1);
		gif1.setMovieResource(R.drawable.progress);

	}

	public void show() {
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	public void dismiss() {
		if (progressDialog.isShowing()){
			progressDialog.dismiss();
		}
	}
	public boolean isShowing() {
		return progressDialog.isShowing();
	}
}
