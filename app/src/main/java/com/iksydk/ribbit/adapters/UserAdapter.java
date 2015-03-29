package com.iksydk.ribbit.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.iksydk.ribbit.R;
import com.iksydk.ribbit.utils.MD5Util;
import com.parse.ParseUser;
import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Billy on 3/25/2015.
 */
public class UserAdapter extends ArrayAdapter<ParseUser>
{
    private static final String TAG = UserAdapter.class.getSimpleName();
    protected Context mContext;
    protected List<ParseUser> mUsers;

    public UserAdapter(Context context, List<ParseUser> users)
    {
        super(context, R.layout.user_item, users);

        mContext = context;
        mUsers = users;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        ViewHolder holder;
        if(convertView == null)
        {
            convertView = LayoutInflater.from(mContext)
                    .inflate(R.layout.user_item, null);
            holder = new ViewHolder();
            holder.userImageView = (ImageView) convertView.findViewById(R.id.userImageView);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.nameLabel);
            holder.checkImageView = (ImageView) convertView.findViewById(R.id.checkImageView);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        ParseUser user = mUsers.get(position);
        String email = user.getEmail().toLowerCase();

        if(email.isEmpty())
        {
            holder.userImageView.setImageResource(R.mipmap.avatar_empty);
        }
        else
        {
            String hash = MD5Util.md5Hex(email);
            String gravatarURL = "http://www.gravatar.com/avatar/" + hash + "?s=204&d=404";
            Log.d(TAG, gravatarURL);

            Picasso.with(getContext())
                    .load(gravatarURL)
                    .placeholder(R.mipmap.avatar_empty)
                    .into(holder.userImageView);

        }

        holder.nameLabel.setText(user.getUsername());

        GridView gridView = (GridView)parent;
        if(gridView.isItemChecked(position))
        {
            holder.checkImageView.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.checkImageView.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

    private static class ViewHolder
    {
        ImageView userImageView;
        ImageView checkImageView;
        TextView nameLabel;
    }

    public void refill(List<ParseUser> user)
    {
        mUsers.clear();
        mUsers.addAll(user);
        notifyDataSetChanged();
    }
}
