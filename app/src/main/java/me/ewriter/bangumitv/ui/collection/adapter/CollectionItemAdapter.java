package me.ewriter.bangumitv.ui.collection.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import me.ewriter.bangumitv.R;
import me.ewriter.bangumitv.dao.MyCollection;

public class CollectionItemAdapter extends RecyclerView.Adapter<CollectionItemAdapter.MyHolder> {

    List<MyCollection> mList;
    Context mContext;
    onItemClickListener listener;

    public CollectionItemAdapter(Context mContext, List<MyCollection> mList) {
        this.mContext = mContext;
        this.mList = mList;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collection_item, parent, false);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, int position) {
        final MyCollection item = mList.get(position);

        holder.name.setText(item.getNormal_name());

        holder.smallName.setText(item.getSmall_name());
        holder.info.setText(item.getInfo());
//        holder.comment.setText(item.getComment());

        Picasso.with(mContext)
                .load(item.getCover_image_url())
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

    public interface onItemClickListener {
        void onItemClick(View view, MyCollection collection);
    }

    public void setOnItemClickListener(onItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class MyHolder extends RecyclerView.ViewHolder {

        private ImageView image;
        private TextView name;
        private TextView smallName;
        private TextView info;
//        private TextView comment;
        private View item;

        public MyHolder(View itemView) {
            super(itemView);
            item = itemView;
            image = (ImageView) itemView.findViewById(R.id.collection_img);
            name = (TextView) itemView.findViewById(R.id.collection_title);
            smallName = (TextView) itemView.findViewById(R.id.collection_small_title);
            info = (TextView) itemView.findViewById(R.id.collection_info);
//            comment = (TextView) itemView.findViewById(R.id.collection_comment);
        }
    }
}
