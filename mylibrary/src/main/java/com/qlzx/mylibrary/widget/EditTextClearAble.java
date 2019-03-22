package com.qlzx.mylibrary.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.EditText;

/**
 * 带清空的输入框
 * @author lmm
 *
 */
public class EditTextClearAble extends android.support.v7.widget.AppCompatEditText {
   // private final String TAG = "EditTextClearAble";
    private Drawable dRight;
    private Drawable dLeft;
    private Rect rBounds;

    public EditTextClearAble(Context paramContext) {
            super(paramContext);
            initEditText();
    }

    public EditTextClearAble(Context paramContext, AttributeSet paramAttributeSet) {
            super(paramContext, paramAttributeSet);
            initEditText();
    }

    public EditTextClearAble(Context paramContext, AttributeSet paramAttributeSet,
                             int paramInt) {
            super(paramContext, paramAttributeSet, paramInt);
            initEditText();
    }

    // 初始化edittext 控件
    private void initEditText() {
            setEditTextDrawable();
            addTextChangedListener(new TextWatcher() {        // 对文本内容改变进行监听
                    public void afterTextChanged(Editable paramEditable) {
                    }
					 public void beforeTextChanged(CharSequence paramCharSequence,
                                    int paramInt1, int paramInt2, int paramInt3) {
                    }

                    public void onTextChanged(CharSequence paramCharSequence,
                                    int paramInt1, int paramInt2, int paramInt3) {
                    	EditTextClearAble.this.setEditTextDrawable();
                    }
            });
            
            setOnFocusChangeListener(new OnFocusChangeListener() {
				@Override
				public void onFocusChange(View v, boolean hasFocus) {
					if (hasFocus) {
						setEditTextDrawable();
					}else{
						setCompoundDrawables(dLeft, null, null, null);
					}
					
					//避免嵌套父控件无选中状态
					ViewParent parent = getParent();
					if (parent!=null&&parent instanceof ViewGroup) {
						ViewGroup p = (ViewGroup) parent;
						int count = p.getChildCount();
						if (count==2) {
							p.setSelected(hasFocus);
						}
					}
				}
			});
    }

    // 控制图片的显示
    private void setEditTextDrawable() {
            if (getText().toString().length() == 0){
            	//有错误提示的存在
            	if (!TextUtils.isEmpty(getError())) {
            		setCompoundDrawables(this.dLeft, null, this.dRight, null);
				}else{
					 setCompoundDrawables(this.dLeft, null, null, null);
				}
            } else {
                    setCompoundDrawables(this.dLeft, null, this.dRight, null);
            }
    }

    protected void finalize() throws Throwable {
            super.finalize();
            this.dRight = null;
            this.rBounds = null;
    }
	
	 // 添加触摸事件
//    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
//            if ((this.dRight != null) && (paramMotionEvent.getAction() == 1)) {
//                    this.rBounds = this.dRight.getBounds();
//                    int i = (int) paramMotionEvent.getX();
//                    if (i > getRight() - 3 * this.rBounds.width()) {
////                            setText("");
//                            getEditableText().clear();
////                            paramMotionEvent.setAction(MotionEvent.ACTION_CANCEL);
//                    }
//            }
//            return super.onTouchEvent(paramMotionEvent);
//    }
    // 添加触摸事件
    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
            if ((this.dRight != null) && paramMotionEvent.getAction() == MotionEvent.ACTION_UP) {
                    boolean touchable = paramMotionEvent.getX() > (getWidth() - getTotalPaddingRight())  
                            && (paramMotionEvent.getX() < ((getWidth() - getPaddingRight())));  
                      
                    if (touchable) {  
                    	getEditableText().clear();
                    }  
            }  
            return super.onTouchEvent(paramMotionEvent);
    }

    // 设置显示的图片资源
    public void setCompoundDrawables(Drawable paramDrawable1,
                                     Drawable paramDrawable2, Drawable paramDrawable3,
                                     Drawable paramDrawable4) {
            if (paramDrawable3 != null){
                    this.dRight = paramDrawable3;
            }if (paramDrawable1!=null) {
				this.dLeft = paramDrawable1;
			}
            super.setCompoundDrawables(paramDrawable1, paramDrawable2,
                            paramDrawable3, paramDrawable4);
    }
    
}
