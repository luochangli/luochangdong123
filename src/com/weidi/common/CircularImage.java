package com.weidi.common;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;

import com.weidi.R;
import com.weidi.common.image.MaskedImage;

public class CircularImage extends MaskedImage {  
    public CircularImage(Context paramContext) {  
        super(paramContext);  
        setImageResource(R.drawable.default_useravatar);
    }  
  
    public CircularImage(Context paramContext, AttributeSet paramAttributeSet) {  
        super(paramContext, paramAttributeSet); 
        setImageResource(R.drawable.default_useravatar);
    }  
  
    public CircularImage(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {  
        super(paramContext, paramAttributeSet, paramInt);  
        setImageResource(R.drawable.default_useravatar);
    }  
    
//    public void setBitmap(String username) {
//    	Bitmap bitmap = XmppConnection.getInstance().getUserImage(username);
//		if (bitmap!=null) {
//			setImageBitmap(ImgHandler.ToCircularBig(bitmap));
//		}
//		else {
//			setImageResource(R.drawable.default_icon);
//		}
//	}
    
  
    public Bitmap createMask() {  
        int i = getWidth();  
        int j = getHeight();  
        Bitmap.Config localConfig = Bitmap.Config.ARGB_8888;  
        Bitmap localBitmap = Bitmap.createBitmap(i, j, localConfig);  
        Canvas localCanvas = new Canvas(localBitmap);  
        Paint localPaint = new Paint(1);  
        localPaint.setColor(-16777216);  
        float f1 = getWidth();  
        float f2 = getHeight();  
        RectF localRectF = new RectF(0.0F, 0.0F, f1, f2);  
        localCanvas.drawOval(localRectF, localPaint);  
        return localBitmap;  
    }  
}
