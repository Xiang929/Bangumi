package me.home.bangumi.ui.index.adapter;

import android.content.Context;
import android.media.Image;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.siyamed.shapeimageview.RoundedImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import me.home.bangumi.R;
import me.home.bangumi.dao.Index;

public class IndexItemAdapter extends RecyclerView.Adapter<IndexItemAdapter.MyViewHolder> {

    List<Index> mList;
    Context mContext;
    onItemClickListener listener;

    public IndexItemAdapter(List<Index> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @NonNull
    @Override
    public IndexItemAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.index_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final IndexItemAdapter.MyViewHolder holder, int position) {
        final Index item  = mList.get(position);

        holder.name_cn.setText(item.getName_cn());
        holder.name.setText(item.getName());
        holder.info.setText(item.getInfo());

        Picasso.with(mContext)
                .load(item.getImg_url())
                .fit()
                .centerCrop()
                .placeholder(R.drawable.img_on_load)
                .error(R.drawable.img_on_error)
                .into(holder.image);

        holder.item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onItemClick(holder.image, item);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public interface onItemClickListener {
        void onItemClick(View view, Index index);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        private RoundedImageView image;
        private TextView name_cn;
        private TextView name;
        private TextView info;
        private View item;

        public MyViewHolder(View itemView) {
            super(itemView);
            item = itemView;
            image = (RoundedImageView) itemView.findViewById(R.id.item_cover);
            name_cn = (TextView) itemView.findViewById(R.id.item_title);
            name = (TextView) itemView.findViewById(R.id.item_name);
            info = (TextView) itemView.findViewById(R.id.item_info);
        }
    }
}
