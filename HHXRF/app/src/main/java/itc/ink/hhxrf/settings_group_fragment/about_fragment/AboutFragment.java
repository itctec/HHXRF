package itc.ink.hhxrf.settings_group_fragment.about_fragment;

import android.app.Fragment;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import itc.ink.hhxrf.R;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class AboutFragment extends Fragment {
    private ConstraintLayout instructionItemLayout;
    private ImageView instructionItemArrowIcon;
    private TextView instructionContentItem;
    private ConstraintLayout telItemLayout;
    private ConstraintLayout mailItemLayout;
    private ConstraintLayout instructionDownloadItemLayout;

    private boolean isInstructionItemShow=false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_about, container, false);

        instructionItemLayout=rootView.findViewById(R.id.instruction_Item_Layout);
        instructionItemLayout.setOnClickListener(new InstructionItemLayoutClickListener());
        instructionItemArrowIcon=rootView.findViewById(R.id.instruction_Item_Arrow_Icon);
        instructionContentItem=rootView.findViewById(R.id.instruction_Content_Item);
        telItemLayout=rootView.findViewById(R.id.tel_Item_Layout);
        telItemLayout.setOnClickListener(new TelItemLayoutClickListener());
        mailItemLayout=rootView.findViewById(R.id.mail_Item_Layout);
        mailItemLayout.setOnClickListener(new MailItemLayoutClickListener());
        instructionDownloadItemLayout=rootView.findViewById(R.id.instruction_Download_Item_Layout);
        instructionDownloadItemLayout.setOnClickListener(new InstructionDownloadItemLayoutClickListener());

        return rootView;
    }

    class InstructionItemLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            isInstructionItemShow=!isInstructionItemShow;
            if(isInstructionItemShow){
                instructionItemArrowIcon.setImageResource(R.drawable.vector_drawable_arrow_down);
                instructionContentItem.setVisibility(View.VISIBLE);
            }else{
                instructionItemArrowIcon.setImageResource(R.drawable.vector_drawable_arrow_up);
                instructionContentItem.setVisibility(View.GONE);
            }
        }
    }

    class TelItemLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            Uri data = Uri.parse("tel:13902932001");
            intent.setData(data);
            startActivity(intent);
        }
    }

    class MailItemLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            String[] reciver = new String[] { "contact@gmail.com" };
            String[] mySbuject = new String[] { "主题" };
            String myCc = "cc";
            String mybody = "内容";
            Intent myIntent = new Intent(android.content.Intent.ACTION_SEND);
            myIntent.setType("plain/text");
            myIntent.putExtra(android.content.Intent.EXTRA_EMAIL, reciver);
            myIntent.putExtra(android.content.Intent.EXTRA_CC, myCc);
            myIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, mySbuject);
            myIntent.putExtra(android.content.Intent.EXTRA_TEXT, mybody);
            startActivity(Intent.createChooser(myIntent, "森沙仪器"));
        }
    }

    class InstructionDownloadItemLayoutClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {

        }
    }


}
