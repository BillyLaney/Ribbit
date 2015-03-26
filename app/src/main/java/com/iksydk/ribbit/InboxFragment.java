package com.iksydk.ribbit;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.List;

/**
 * Created by Billy on 3/24/2015.
 */
public class InboxFragment extends ListFragment
{
    protected List<ParseObject> mMessages;
    protected ProgressBar mProgressBar;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_inbox,
                container, false);

        mProgressBar = (ProgressBar) rootView.findViewById(R.id.progressBar);
        showProgress(false);
        return rootView;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        showProgress(true);
        ParseQuery<ParseObject> query = new ParseQuery<>(ParseConstants.CLASS_MESSAGE);
        query.whereEqualTo(ParseConstants.KEY_RECIPIENT_IDS, ParseUser.getCurrentUser()
                .getObjectId());
        query.orderByAscending(ParseConstants.KEY_CREATED_AT);

        query.findInBackground(new FindCallback<ParseObject>()
        {
            @Override
            public void done(List<ParseObject> messages, ParseException e)
            {
                showProgress(false);

                if(e == null)
                {
                    mMessages = messages;

                    String[] usernames = new String[mMessages.size()];
                    int i = 0;
                    for(ParseObject message : mMessages)
                    {
                        usernames[i] = message.getString(ParseConstants.KEY_SENDER_NAME);
                        i++;
                    }

                    MessageAdapter adapter = new MessageAdapter(getListView().getContext(), messages);
                    setListAdapter(adapter);
                }
                else
                {

                }
            }
        });
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id)
    {
        super.onListItemClick(l, v, position, id);

        ParseObject message = mMessages.get(position);

        String messageType = message.getString(ParseConstants.KEY_FILE_TYPE);
        ParseFile file = message.getParseFile(ParseConstants.KEY_FILE);
        Uri fileUri = Uri.parse(file.getUrl());
        if(messageType.equals(ParseConstants.TYPE_IMAGE))
        {
            //view image
            Intent intent = new Intent(getActivity(), ViewImageActivity.class);
            intent.setData(fileUri);
            startActivity(intent);
        }
        else
        {
            //view video
        }
    }

    public void showProgress(boolean show)
    {
        if(mProgressBar == null)
        {
            return;
        }
        if(show)
        {
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else
        {
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }
}
