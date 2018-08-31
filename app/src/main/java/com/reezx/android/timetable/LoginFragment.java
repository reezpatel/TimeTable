package com.reezx.android.timetable;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

public class LoginFragment extends Fragment implements SignInButton.OnClickListener {
    private Context mContext;
    private OnFragmentInteractionListener mListener;

    public LoginFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        SignInButton signInButton = (SignInButton) view.findViewById(R.id.ButtonGoogleSignIn);
        signInButton.setColorScheme(SignInButton.COLOR_AUTO);
        signInButton.setSize(SignInButton.SIZE_WIDE);
        signInButton.setOnClickListener(this);

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            mContext = context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.ButtonGoogleSignIn) {
            GoogleApiClient googleApiClient = null;
            try {
                GoogleSignInOptions googleSignInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                        .requestIdToken("719955137562-tt84ep5iofrot9n97fj7cfut2dfs3uve.apps.googleusercontent.com").requestEmail().requestProfile().requestId().build();
                googleApiClient = new GoogleApiClient.Builder(mContext)
                        .enableAutoManage(getActivity(), new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                Log.d("TAG", "Login Failed");
                            }
                        })
                        .addApi(Auth.GOOGLE_SIGN_IN_API, googleSignInOptions)
                        .build();
            }catch (Exception e) {
                Toast.makeText(mContext,"Please Wait",Toast.LENGTH_SHORT).show();
            }
            if(googleApiClient != null) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
                getActivity().startActivityForResult(signInIntent,Constants.SIGNIN_REQUEST_CODE);
            }else {
                Toast.makeText(mContext,"Please Wait",Toast.LENGTH_SHORT).show();
            }
        }
    }


    public interface OnFragmentInteractionListener {
        void onLoginStatusChange(boolean status);
    }
}
