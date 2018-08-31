package com.reezx.android.timetable;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class HomeFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    public HomeFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        Bundle bundle = SharedPrefManager.getUserData(getActivity());
        ProfilePicLoader profilePicLoader = new ProfilePicLoader(getActivity(),(ImageView) view.findViewById(R.id.ImageViewHomeFragment));
        String photoURL = bundle.getString(Constants.TEXT_PHOTO_URL);
        if(photoURL != null && !photoURL.equals("0")) {
            profilePicLoader.execute(photoURL);
        }

        ((TextView)view.findViewById(R.id.TextViewHomeFragmentName)).setText(bundle.getString(Constants.TEXT_NAME));
        ((TextView)view.findViewById(R.id.TextViewHomeFragmentEmail)).setText(bundle.getString(Constants.TEXT_EMAIL));

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
          //  throw new RuntimeException(context.toString()
            //        + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
