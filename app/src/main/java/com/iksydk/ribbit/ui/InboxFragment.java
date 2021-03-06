package com.iksydk.ribbit.ui;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.iksydk.ribbit.R;
import com.iksydk.ribbit.adapters.MessageAdapter;
import com.iksydk.ribbit.utils.ParseConstants;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Billy on 3/24/2015.
 */
public class InboxFragment extends ListFragment
{
    protected List<ParseObject> mMessages;
    protected SwipeRefreshLayout mSwipeRefreshLayout;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        View rootView = inflater.inflate(R.layout.fragment_inbox,
                container, false);

        mSwipeRefreshLayout = (SwipeRefreshLayout)rootView.findViewById(R.id.swipeRefreshLayout);

        mSwipeRefreshLayout.setOnRefreshListener(getRefreshListener());
        mSwipeRefreshLayout.setColorSchemeColors(R.color.swipeRefresh1, R.color.swipeRefresh2, R.color.swipeRefresh3, R.color.swipeRefresh4);

        showProgress(false);

        return rootView;
    }

    private SwipeRefreshLayout.OnRefreshListener getRefreshListener()
    {
        return new SwipeRefreshLayout.OnRefreshListener()
        {
            @Override
            public void onRefresh()
            {
                refreshInbox();
            }
        };
    }

    @Override
    public void onResume()
    {
        super.onResume();

        showProgress(true);
        refreshInbox();
    }

    private void refreshInbox()
    {
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

                    if(getListView().getAdapter() == null)
                    {
                        MessageAdapter adapter = new MessageAdapter(getListView().getContext(), messages);
                        setListAdapter(adapter);
                    }
                    else
                    {
                        ((MessageAdapter)getListView().getAdapter()).refill(messages);
                    }
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
            Intent intent = new Intent(Intent.ACTION_VIEW, fileUri);
            intent.setDataAndType(fileUri, "video/*");
            startActivity(intent);
        }

        //delete the image
        List<String> ids = message.getList(ParseConstants.KEY_RECIPIENT_IDS);
        if(ids.size() == 1)
        {
            //delete the message
            message.deleteInBackground();
        }
        else
        {
            //remove recipient
            ids.remove(ParseUser.getCurrentUser().getObjectId());
            ArrayList<String> idsToRemove = new ArrayList<>();
            idsToRemove.add(ParseUser.getCurrentUser().getObjectId());

            message.removeAll(ParseConstants.KEY_RECIPIENT_IDS, idsToRemove);
            message.saveInBackground();
        }
    }

    public void showProgress(boolean show)
    {
        if(show)
        {
            mSwipeRefreshLayout.setRefreshing(true);
        }
        else
        {
            mSwipeRefreshLayout.setRefreshing(false);
        }
    }
}
