package com.example.joy.tarrgui.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.example.joy.tarr.Session;
import com.example.joy.tarrgui.R;
import com.example.joy.tarrgui.anim.AnimationUtils;
import com.example.joy.tarrgui.extras.Constants;
import com.example.joy.tarrgui.network.VolleySingleton;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by joy on 15-04-2015.
 */
public class AdapterSessionRequest extends
        RecyclerView.Adapter<AdapterSessionRequest.ViewHolderSessionNotify>{


private ArrayList<Session> mListSession = new ArrayList<>();
private LayoutInflater mInflater;
private VolleySingleton mVolleySingleton;
private ImageLoader mImageLoader;
//formatter for parsing the dates in the specified format below
private DateFormat mFormatter = new SimpleDateFormat("yyyy-MM-dd");
//keep track of the previous position for animations where scrolling down requires a different animation compared to scrolling up
private int mPreviousPosition = 0;

public AdapterSessionRequest(Context context){
        mInflater = LayoutInflater.from(context);
        mVolleySingleton = VolleySingleton.getInstance();
        mImageLoader = mVolleySingleton.getImageLoader();
        }

public void setSession(ArrayList<Session> listSession) {
        this.mListSession = listSession;
        //update the adapter to reflect the new set of movies
        notifyDataSetChanged();
        }

@Override
public ViewHolderSessionNotify onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate
        (R.layout.custom_movie_box_office3, parent, false);
        ViewHolderSessionNotify viewHolder = new ViewHolderSessionNotify(view);
        return viewHolder;
        }

@Override
public void onBindViewHolder(ViewHolderSessionNotify holder, int position) {
        Session currentSession = mListSession.get(position);
        //one or more fields of the Movie object may be null since they are fetched from the web
    System.out.println(currentSession.getStr());
    holder.movieTitle.setText(currentSession.getRequestTo());
    holder.sidRequest.setText(currentSession.getStr());

        holder.movieReleaseDate.setText(currentSession.getTopic().get(0));
        holder.sessionRemarks.setText(currentSession.getRemarks());

    if(currentSession.getIsDenied()) {
        holder.imageStatusRequest.setImageResource(R.drawable.ic_highlight_remove_black_18dp);
        //holder.imageStatusNotify.setImageDrawable(R.drawable.ic_highlight_remove_black_18dp);
    }
    else if(currentSession.getIsConfirmed()&& !(currentSession.getIsCompleted())){
        holder.imageStatusRequest.setImageResource(R.drawable.ic_check_circle_black_18dp);
    }
    else if(currentSession.getIsConfirmed()&& (currentSession.getIsCompleted())){
        holder.imageStatusRequest.setImageResource(R.drawable.ic_thumb_up_black_18dp);

    }
    else if(!(currentSession.getIsConfirmed())&& !(currentSession.getIsCompleted())){
        holder.imageStatusRequest.setImageResource(R.drawable.ic_history_black_18dp);

    }

        //retrieved date may be null
        /*Date movieReleaseDate = currentMovie.getReleaseDateTheater();
        if (movieReleaseDate != null) {
            String formattedDate = mFormatter.format(movieReleaseDate);
            holder.movieReleaseDate.setText(formattedDate);
        } else {
            holder.movieReleaseDate.setText(Constants.NA);
        }*/

        float audienceScore = currentSession.getSessionRating();
        if (audienceScore == -1) {
        holder.movieAudienceScore.setRating(0.0F);
        holder.movieAudienceScore.setAlpha(0.5F);
        } else {
        holder.movieAudienceScore.setRating(audienceScore);
        holder.movieAudienceScore.setAlpha(1.0F);
        }

        if (position > mPreviousPosition) {
        AnimationUtils.animateSunblind(holder, true);
//            AnimationUtils.animateSunblind(holder, true);
            //AnimationUtils.animate1(holder, true);
            //AnimationUtils.animate(holder,true);
        } else {
        AnimationUtils.animateSunblind(holder, false);
//            AnimationUtils.animateSunblind(holder, false);
            //AnimationUtils.animate1(holder, false);
            //AnimationUtils.animate(holder, false);
        }
        mPreviousPosition = position;

        /*String urlThumnail = currentUser.getUrlThumbnail();
        loadImages(urlThumnail, holder);
*/
        }


private void loadImages(String urlThumbnail, final ViewHolderSessionNotify holder) {
        if (!urlThumbnail.equals(Constants.NA)) {
        mImageLoader.get(urlThumbnail, new ImageLoader.ImageListener() {
@Override
public void onResponse(ImageLoader.ImageContainer response, boolean isImmediate) {
       // holder.movieThumbnail.setImageBitmap(response.getBitmap());
        }

@Override
public void onErrorResponse(VolleyError error) {

        }
        });
        }
        }

@Override
public int getItemCount() {
        return mListSession.size();
        }


static class ViewHolderSessionNotify extends RecyclerView.ViewHolder {

    //ImageView movieThumbnail;
    TextView movieTitle;
    TextView sidRequest;
    TextView movieReleaseDate;
    TextView sessionRemarks;
    ImageView imageStatusRequest;
    RatingBar movieAudienceScore;

    public ViewHolderSessionNotify(View itemView) {
        super(itemView);
        sidRequest = (TextView) itemView.findViewById(R.id.sidRequest);
        //movieThumbnail = (ImageView) itemView.findViewById(R.id.movieThumbnailRequest);
        movieTitle = (TextView) itemView.findViewById(R.id.movieTitleRequest);
        movieReleaseDate = (TextView) itemView.findViewById(R.id.movieReleaseDateRequest);
        movieAudienceScore = (RatingBar) itemView.findViewById(R.id.movieAudienceScoreRequest);
        sessionRemarks = (TextView) itemView.findViewById(R.id.sremarksRequest);
        imageStatusRequest = (ImageView) itemView.findViewById(R.id.imageStatusRequest);

    }
}
}
