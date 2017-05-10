package com.ingeniatest.gistsproject.adapter;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ingeniatest.gistsproject.FileViewerActivity;
import com.ingeniatest.gistsproject.R;
import com.ingeniatest.gistsproject.model.Gist;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GistAdapterRecyclerView extends RecyclerView.Adapter<GistAdapterRecyclerView.GistViewHolder>{

    private static ArrayList<Gist> gists;
    private int resource;
    private Activity activity;

    public static ArrayList<Gist> getGists() {
        return gists;
    }

    public GistAdapterRecyclerView(ArrayList<Gist> gists, int resource, Activity activity) {
        this.gists = gists;
        this.resource = resource;
        this.activity = activity;
    }

    @Override
    public GistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(resource, parent, false);
        return new GistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(GistViewHolder holder, int position) {

        final Gist gist = gists.get(position);
        final int item = position;
        
        //User Avatar
        if (gist.getOwner() != null){
            if (gist.getOwner().getAvatarUrl() != null) {
                Picasso.with(activity).load(gist.getOwner().getAvatarUrl()).into(holder.userImage);
            }else{
                holder.userImage.setImageDrawable(ContextCompat.getDrawable(activity ,R.drawable.blank_user));
            }
        }else{
            holder.userImage.setImageDrawable(ContextCompat.getDrawable(activity ,R.drawable.blank_user));
        }

        //User Login
        if (gist.getOwner() != null) {
            if ( gist.getOwner().toString() != "" )
                holder.loginCard.setText(gist.getOwner().getLogin().toString());
            else
                holder.loginCard.setText(activity.getString(R.string.login_card));
        }
        else
            holder.loginCard.setText(activity.getString(R.string.login_card));


        //Time Card
        if (gist.getCreatedAt() != null){
            if ( gist.getCreatedAt() != "")
                holder.timeCard.setText(gist.getCreatedAt().toString());
            else
                holder.timeCard.setText(activity.getString(R.string.timecard_card));
         }
        else
            holder.timeCard.setText(activity.getString(R.string.timecard_card));


        // Description
        if (gist.getDescription() != null) {
            if ( gist.getDescription()!=null   )
                holder.descriptionCard.setText(gist.getDescription().toString());
            else
                holder.descriptionCard.setText(activity.getString(R.string.description_card));
        }
        else
            holder.descriptionCard.setText(activity.getString(R.string.description_card));


        //Comments
        if (gist.getComments()!= null) {
            if (gist.getComments().toString() != "")
            holder.numCommentsCard.setText(gist.getComments().toString() +
                    activity.getString(R.string.textcomments_card));
            else
                holder.numCommentsCard.setText(activity.getString(R.string.numbercomments_card) +
                        activity.getString(R.string.textcomments_card));
        }
        else
            holder.numCommentsCard.setText(activity.getString(R.string.numbercomments_card) +
                    activity.getString(R.string.textcomments_card));

        //File Container
        if (gist.getFiles() != null){
            if ( gist.getFiles().getFiles().size() != 0 ){
                holder.fileContainer.setVisibility(View.VISIBLE);
                holder.fileNameCard.setText(gist.getFiles().getFiles().get(0).getFilename());
            }
        }
        else{
            holder.fileContainer.setVisibility(View.INVISIBLE);
        }

        holder.fileContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(activity, FileViewerActivity.class);
                intent.putExtra("Key", item);
                activity.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return gists.size();
    }

    public void upDateData(ArrayList<Gist> inGists){
        gists.clear();
        gists.addAll(inGists);
        notifyDataSetChanged();
    }

    public class GistViewHolder extends RecyclerView.ViewHolder{

        private CircleImageView userImage;
        private TextView loginCard;
        private TextView timeCard;
        private TextView descriptionCard;
        private TextView numCommentsCard;
        private TextView fileNameCard;

        private LinearLayout fileContainer;

        public GistViewHolder(View itemView) {
            super(itemView);

            userImage           = (CircleImageView)itemView.findViewById(R.id.userImageCard);
            loginCard           = (TextView)itemView.findViewById(R.id.loginCard);
            timeCard            = (TextView)itemView.findViewById(R.id.timeCard);
            descriptionCard     = (TextView)itemView.findViewById(R.id.descriptionCard);
            numCommentsCard     = (TextView)itemView.findViewById(R.id.numCommentsCard);
            fileNameCard        = (TextView)itemView.findViewById(R.id.fileName);

            fileContainer       = (LinearLayout)itemView.findViewById(R.id.fileContainer);
        }
    }
}
