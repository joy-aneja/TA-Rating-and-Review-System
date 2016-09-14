package com.example.joy.tarrgui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.InputStream;
import java.util.Collections;
import java.util.List;

import com.example.joy.tarrgui.R;
import com.example.joy.tarrgui.activities.ActivityMain;
import com.example.joy.tarrgui.pojo.Information;
import com.pkmmte.view.CircularImageView;

/**
 * Created by Windows on 22-12-2014.
 */
public class AdapterDrawer extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Information> data= Collections.emptyList();
    private static final int TYPE_HEADER=0;
    private static final int TYPE_ITEM=1;
    private LayoutInflater inflater;
    private Context context;
    public AdapterDrawer(Context context, List<Information> data){
        this.context=context;
        inflater=LayoutInflater.from(context);
        this.data=data;
    }

    public void delete(int position){
        data.remove(position);
        notifyItemRemoved(position);
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==TYPE_HEADER){
            View view=inflater.inflate(R.layout.drawer_header, parent,false);
            HeaderHolder holder=new HeaderHolder(view);
            return holder;
        }
        else{
            View view=inflater.inflate(R.layout.item_drawer, parent,false);
            ItemHolder holder=new ItemHolder(view);
            return holder;
        }

    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return TYPE_HEADER;
        }
        else {
            return TYPE_ITEM;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if(holder instanceof HeaderHolder ){

        }
        else{
            ItemHolder itemHolder= (ItemHolder) holder;
            Information current=data.get(position-1);
            itemHolder.title.setText(current.title);
            itemHolder.icon.setImageResource(current.iconId);
        }

    }
    @Override
    public int getItemCount() {
        return data.size()+1;
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;
        public ItemHolder(View itemView) {
            super(itemView);
            title= (TextView) itemView.findViewById(R.id.listText);
            icon= (ImageView) itemView.findViewById(R.id.listIcon);
        }
    }
    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView email;
        CircularImageView img;
        public HeaderHolder(View itemView) {
            super(itemView);
            img = (CircularImageView) itemView.findViewById(R.id.profileImage);
            name= (TextView) itemView.findViewById(R.id.name);
            email= (TextView) itemView.findViewById(R.id.email);
            name.setText(ActivityMain.naam);
            email.setText(ActivityMain.epta);
            new LoadProfileImage(img).execute(ActivityMain.picurl);
        }
    }

    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        CircularImageView bmImage;

        public LoadProfileImage(CircularImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                //Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
         /*View v = findViewById(R.id.image_profilepic);
         v.setDrawingCacheEnabled(true);

         Bitmap bitmap = v.getDrawingCache();
         File root = Environment.getExternalStorageDirectory();
         //System.out.println(root.getAbsolutePath()+"/drawable/image.jpg");
         File file = null;
         try
         {
             file = File.createTempFile("image",".jpg",root);
             FileOutputStream ostream = new FileOutputStream(file);
             bitmap.compress(Bitmap.CompressFormat.JPEG, 100, ostream);
             ostream.close();
         }
         catch (Exception e)
         {
             e.printStackTrace();
         }
         System.out.println(file.getAbsolutePath());
*/



        }
    }


}
