package com.iksydk.ribbit.adapters;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.iksydk.ribbit.R;
import com.iksydk.ribbit.utils.ParseConstants;
import com.parse.ParseObject;
import com.parse.ParseUser;

import java.util.Date;
import java.util.List;

/**
 * Created by Billy on 3/25/2015.
 */
public class UserAdapter extends ArrayAdapter<ParseUser>
{
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
            //holder.iconImageView = (ImageView) convertView.findViewById(R.id.messageIcon);
            holder.nameLabel = (TextView) convertView.findViewById(R.id.nameLabel);

            convertView.setTag(holder);
        }
        else
        {
            holder = (ViewHolder) convertView.getTag();
        }

        ParseUser user = mUsers.get(position);

        /*if(user.getString(ParseConstants.KEY_FILE_TYPE)
                .equals(ParseConstants.TYPE_IMAGE))
        {
            //holder.iconImageView.setImageResource(R.mipmap.ic_picture);
        }
        else
        {
            //holder.iconImageView.setImageResource(R.mipmap.ic_video);
        }*/

        holder.nameLabel.setText(user.getUsername());

        return convertView;
    }

    private static class ViewHolder
    {
        //ImageView iconImageView;
        TextView nameLabel;
    }

    public void refill(List<ParseUser> user)
    {
        mUsers.clear();
        mUsers.addAll(user);
        notifyDataSetChanged();
    }
}
