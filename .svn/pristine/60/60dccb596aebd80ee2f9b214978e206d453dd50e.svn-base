package com.weidi.chat.repository;

import org.jivesoftware.smackx.packet.VCard;

import com.weidi.R;
import com.weidi.common.image.ImageUtil;
import com.weidi.common.image.ImgHandler;
import com.weidi.util.Const;
import com.weidi.util.ImageCache;
import com.weidi.util.XmppUtil;

import android.graphics.Bitmap;
import android.os.Handler;
import android.widget.ImageView;

/**
 * @author luochangdong E-mail: 2270333671@qq.com
 * @date 创建时间：2015-8-23 上午9:55:46
 * @Description 1.0
 */
public class AvatarRepo {
	private static AvatarRepo instance;

	public static AvatarRepo getInstance() {
		if (instance == null) {
			instance = new AvatarRepo();
		}
		return instance;
	}

	public void setAvatarRepo(final String to, final ImageView imgView,
			final Handler mHandler) {
		if (to == null || imgView == null) {
			return;
		}

		new Thread(new Runnable() {

			@Override
			public void run() {
				final Bitmap bitmap1 = ImageCache.getInstance().get(to);

				if (bitmap1 == null) {
					VCard vCard = XmppUtil.getUserInfo(to);
					if (vCard != null) {
						String avatar = vCard.getField("avatar");
						setDefault(imgView, vCard);
						if (avatar != null) {
							final Bitmap bitmap2 = ImageUtil
									.getBitmapFromBase64String(avatar);
							if (bitmap2 != null) {
								mHandler.post(new Runnable() {

									@Override
									public void run() {
										imgView.setImageBitmap(bitmap2);
									}
								});

								ImageCache.getInstance().put(to, bitmap2);
							}
						}
					}
				} else {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							imgView.setImageBitmap(bitmap1);
						}
					});
				}
			}

			private void setDefault(final ImageView imgView, VCard vCard) {
				if (vCard.getField("sex") != null) {
					if (vCard.getField("sex").equals(Const.Male)) {
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								imgView.setImageDrawable(ImgHandler
										.ToCircularBig(R.drawable.icon_male));
							}
						});

					} else {
						mHandler.post(new Runnable() {

							@Override
							public void run() {
								imgView.setImageDrawable(ImgHandler
										.ToCircularBig(R.drawable.icon_female));
							}
						});

					}
				} else {
					mHandler.post(new Runnable() {

						@Override
						public void run() {
							imgView.setImageDrawable(ImgHandler
									.ToCircularBig(R.drawable.icon_male));
						}
					});

				}
			}
		}).start();

	}

}
