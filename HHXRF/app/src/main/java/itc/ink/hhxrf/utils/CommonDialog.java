package itc.ink.hhxrf.utils;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import itc.ink.hhxrf.R;


/**
 * Created by yangwenjiang on 2018/10/19.
 */

public class CommonDialog extends Dialog{
    private TextView dialogTitle;
    private TextView dialogContent;
    private TextView dialogPositiveBtn;
    private TextView dialogNegativeBtn;

    private String content;
    private OnCloseListener listener;
    private String positiveName;
    private String negativeName;
    private String title;

    public CommonDialog(Context context) {
        super(context);
    }

    public CommonDialog(Context context, int themeResId, String content) {
        super(context, themeResId);
        this.content = content;
    }

    public CommonDialog(Context context, String content, OnCloseListener listener) {
        super(context, R.style.DialogTheme);
        this.content = content;
        this.listener = listener;
    }

    protected CommonDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    public CommonDialog setTitle(String title){
        this.title = title;
        return this;
    }

    public CommonDialog setPositiveButton(String name){
        this.positiveName = name;
        return this;
    }

    public CommonDialog setNegativeButton(String name){
        this.negativeName = name;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.common_unit_dialog);
        setCanceledOnTouchOutside(false);

        dialogTitle = findViewById(R.id.dialog_Title);
        dialogTitle.setText(title);
        dialogContent = findViewById(R.id.dialog_Content);
        dialogContent.setHint(content);
        dialogPositiveBtn = findViewById(R.id.dialog_Positive_Btn);
        dialogPositiveBtn.setText(positiveName);
        dialogPositiveBtn.setOnClickListener(new DialogPositiveBtnClickListener());
        dialogNegativeBtn = findViewById(R.id.dialog_Negative_Btn);
        dialogNegativeBtn.setText(negativeName);
        dialogNegativeBtn.setOnClickListener(new DialogNegativeBtnClickListener());
    }

    class DialogPositiveBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(listener != null){
                listener.onClick(CommonDialog.this, dialogContent.getText().toString(),true);
            }
        }
    }

    class DialogNegativeBtnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            if(listener != null){
                listener.onClick(CommonDialog.this, dialogContent.getText().toString(),false);
            }
        }
    }

    public interface OnCloseListener{
        void onClick(Dialog dialog, String contentValue,boolean confirm);
    }
}
