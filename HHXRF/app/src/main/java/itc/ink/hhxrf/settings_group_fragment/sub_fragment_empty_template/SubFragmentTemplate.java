package itc.ink.hhxrf.settings_group_fragment.sub_fragment_empty_template;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import itc.ink.hhxrf.R;

/**
 * Created by yangwenjiang on 2018/9/19.
 */

public class SubFragmentTemplate extends Fragment {

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_sub_template, container, false);

        return rootView;
    }


}
