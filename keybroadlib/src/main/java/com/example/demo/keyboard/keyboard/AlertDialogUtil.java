package com.example.demo.keyboard.keyboard;

import android.app.AlertDialog;
import android.content.Context;
import android.os.Build;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import java.lang.reflect.Method;

public class AlertDialogUtil {
    Context context;
    String contentString = "";
    OnKeyInputLinstener onKeyInputLinstener;

    public OnKeyInputLinstener getOnKeyInputLinstener() {
        return onKeyInputLinstener;
    }

    public void setOnKeyInputLinstener(OnKeyInputLinstener onKeyInputLinstene) {
        this.onKeyInputLinstener = onKeyInputLinstener;
    }

    public AlertDialogUtil(Context context) {
        this.context = context;
    }

    private static AlertDialog mKeyboardDialog;

    //键盘相关操作
    public void showKeyboard() {
        hideSystemSoftKeyboard(context);
        if (mKeyboardDialog != null) {
            mKeyboardDialog.dismiss();
            mKeyboardDialog = null;
        }
        final AlertDialog.Builder builder = new AlertDialog.Builder(context, com.example.demo.keyboard.R.style.BottomDialog);
        final InputLayout inputlayout = new InputLayout(context);
        inputlayout.initKeyboardView(Constants.KEYBOARD_TYPE_QWER);
        mKeyboardDialog = builder.create();
        builder.setCancelable(true);
        mKeyboardDialog.show();
        mKeyboardDialog.setCanceledOnTouchOutside(true);
        //inputlayout.showSoftKeyboard(this, keyBoardType);
        mKeyboardDialog.setContentView(inputlayout);
        Window window = mKeyboardDialog.getWindow();
        if (window != null) {
            window.setGravity(Gravity.BOTTOM);
            window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
            WindowManager.LayoutParams attributes = window.getAttributes();
            //防止点击输入框导致键盘被关闭
            attributes.flags = WindowManager.LayoutParams.FLAG_NOT_TOUCH_MODAL | WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
            attributes.width = ViewGroup.LayoutParams.MATCH_PARENT;
            attributes.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            window.setAttributes(attributes);
        }
        inputlayout.setOnOKOrHiddenKeyClickListener(new OnOKOrHiddenKeyClickListener() {
            @Override
            public boolean onOKKeyClick() {
                mKeyboardDialog.dismiss();
                contentString = "";
                return true;
            }

            @Override
            public boolean onHiddenKeyClick() {
                mKeyboardDialog.dismiss();
                contentString = "";
                return true;
            }

            //获取当前的文本信息
            @Override
            public String onGetDataClick(String str) {
                //添加字符
                contentString += str;
                Toast.makeText(context, str, 1000).show();
                return str;
            }

            //删除字符
            @Override
            public void onDeleteCharest() {
                //删除单个字符
                contentString = contentString.substring(0, contentString.length() - 1);
                Toast.makeText(context, contentString, 1000).show();
            }

            //提交字符信息
            @Override
            public void submitCharest() {
                //提交信息
                //调用接口数据信息更新
                if (onKeyInputLinstener != null) {
                    Toast.makeText(context, contentString, 1000).show();
                    onKeyInputLinstener.onKeyInput(contentString);
                }
            }

            /***
             * 清除所有字符
             */
            @Override
            public void clearAllCharest() {
                contentString = "";
            }
        });
    }

    /**
     * 隐藏系统键盘
     */
    private void hideSystemSoftKeyboard(Context context) {
        int sdkInt = Build.VERSION.SDK_INT;
        if (sdkInt >= 11) {
            try {
                Class<EditText> cls = EditText.class;
                Method setShowSoftInputOnFocus;
                setShowSoftInputOnFocus = cls.getMethod("setShowSoftInputOnFocus", boolean.class);
                setShowSoftInputOnFocus.setAccessible(true);
                setShowSoftInputOnFocus.invoke(this, false);

            } catch (SecurityException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            //setInputType(InputType.TYPE_NULL);
        }
        // 如果软键盘已经显示，则隐藏
//        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
//        if (imm != null) {
//            imm.hideSoftInputFromWindow(getWindowToken(), 0);
//        }
    }



}

