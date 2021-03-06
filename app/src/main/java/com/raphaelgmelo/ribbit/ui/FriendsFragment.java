package com.raphaelgmelo.ribbit.ui;

import android.app.AlertDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.raphaelgmelo.ribbit.utils.ParseConstants;
import com.raphaelgmelo.ribbit.R;
import com.raphaelgmelo.ribbit.adapters.UserAdapter;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by raphaelgmelo on 19/04/15.
 */
public class FriendsFragment extends Fragment {

    public static final String TAG = FriendsFragment.class.getSimpleName();

    protected List<ParseUser> mFriends;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;
    @InjectView(R.id.friendsGrid) GridView mGridView;
    @InjectView(android.R.id.empty) TextView mEmptyTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.user_grid, container, false);
        ButterKnife.inject(this, rootView);

        mGridView.setEmptyView(mEmptyTextView);

        return rootView;
    }


    @Override
    public void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATIONS);
        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {

            @Override
            public void done(List<ParseUser> friends, ParseException e) {

                if (e == null) {

                    mFriends = friends;
                    String[] usernames = new String[mFriends.size()];

                    int i = 0;
                    for (ParseUser user : mFriends) {
                        usernames[i] = user.getUsername();
                        i++;
                    }

                    if (mGridView.getAdapter() == null){
                        UserAdapter adapter = new UserAdapter(getActivity(),mFriends);
                        mGridView.setAdapter(adapter);
                    }
                    else{
                        ((UserAdapter)mGridView.getAdapter()).refill(mFriends);
                    }


                }
                else{
                    //error
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                    String title = "Error!";
                    builder.setMessage(e.getMessage())
                            .setTitle(title)
                            .setPositiveButton(android.R.string.ok, null);

                    AlertDialog dialog = builder.create();
                    dialog.show();
                }

            }
        });

    }

}
