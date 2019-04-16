package itc.ink.hhxrf.home_fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import itc.ink.hhxrf.R;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class HomeFragment extends Fragment {
    private ImageView showLeftDrawerBtn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        showLeftDrawerBtn=rootView.findViewById(R.id.home_Fragment_Top_Navigation_Show_Left_Drawer_Btn);

        return rootView;
    }


}
