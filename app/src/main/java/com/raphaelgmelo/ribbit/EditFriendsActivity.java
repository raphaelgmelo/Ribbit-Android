package com.raphaelgmelo.ribbit;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;


public class EditFriendsActivity extends ActionBarActivity {

    public static final String TAG = EditFriendsActivity.class.getSimpleName();

    protected List<ParseUser> mUsers;
    protected ParseRelation<ParseUser> mFriendsRelation;
    protected ParseUser mCurrentUser;

    @InjectView(android.R.id.list) ListView mListView;
    @InjectView(android.R.id.empty) TextView mEmptyTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_friends);
        ButterKnife.inject(this);

        mListView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

                if (mListView.isItemChecked(position)){
                    //add friend
                    mFriendsRelation.add(mUsers.get(position));
                }
                else{
                    //remove friend
                    mFriendsRelation.remove(mUsers.get(position));
                }
                mCurrentUser.saveInBackground(new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e != null){
                            Log.e(TAG, e.getMessage());
                        }
                    }
                });

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();

        mCurrentUser = ParseUser.getCurrentUser();
        mFriendsRelation = mCurrentUser.getRelation(ParseConstants.KEY_FRIENDS_RELATIONS);

        setProgressBarIndeterminateVisibility(true);

        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.orderByAscending(ParseConstants.KEY_USERNAME);
        query.setLimit(1000);
        query.findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> users, ParseException e) {

                setProgressBarIndeterminateVisibility(false);

                if (e == null){
                    //success
                    mUsers = users;

                    String[] usernames = new String[mUsers.size()];

                    int i = 0;
                    for (ParseUser user : mUsers){
                        usernames[i] = user.getUsername();
                        i++;
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                            EditFriendsActivity.this,
                            android.R.layout.simple_list_item_checked,
                            usernames);

                    mListView.setAdapter(adapter);
                    mListView.setEmptyView(mEmptyTextView);

                    addFriendCheckmarks();

                }
                else{
                    //error
                    Log.e(TAG, e.getMessage());
                    AlertDialog.Builder builder = new AlertDialog.Builder(EditFriendsActivity.this);
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

    private void addFriendCheckmarks() {

        mFriendsRelation.getQuery().findInBackground(new FindCallback<ParseUser>() {
            @Override
            public void done(List<ParseUser> friends, ParseException e) {

                if (e == null){
                    //list returned - look for a match
                    for (int i = 0; i < mUsers.size(); i++){
                        ParseUser user = mUsers.get(i);

                        for (ParseUser friend : friends){
                            if (friend.getObjectId().equals(user.getObjectId())){
                                mListView.setItemChecked(i, true);
                            }
                        }
                    }
                }
                else{
                    Log.e(TAG, e.getMessage());
                }

            }
        });

    }


}