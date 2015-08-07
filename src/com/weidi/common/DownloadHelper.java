package com.weidi.common;

import android.app.DownloadManager;
import android.net.Uri;

import com.weidi.QApp;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-7 下午3:49:10
 * @Description 1.0
 */
public class DownloadHelper {

	private DownloadManager downloadManager;

	public DownloadHelper() {
		downloadManager = (DownloadManager) QApp.getInstance()
				.getSystemService(QApp.getInstance().DOWNLOAD_SERVICE);
	}
	
	public void download(String url,String filename){
		DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));
		request.setDestinationInExternalPublicDir("Weidi/RecentChat", filename);
		long downloadId = downloadManager.enqueue(request);
		request.allowScanningByMediaScanner();
		
	}
	
}
