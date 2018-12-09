package me.home.bangumi.ui.calendar.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import me.home.bangumi.R;
import me.home.bangumi.api.ApiManager;
import me.home.bangumi.api.response.BgmListItem;
import me.home.bangumi.dao.BangumiCalendar;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CalendarItemAdapter extends RecyclerView.Adapter<CalendarItemAdapter.MyViewHolder> {

    private List<BangumiCalendar> mList;
    private Context mContext;
    private onCalendarItemListener listener;

    public CalendarItemAdapter(List<BangumiCalendar> mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.calendar_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final BangumiCalendar calendar = mList.get(position);

        if (TextUtils.isEmpty(calendar.getName_cn())) {
            holder.mBangumiTitle.setText(calendar.getName_jp());
        } else {
            holder.mBangumiTitle.setText(calendar.getName_cn());
        }

        String rank = "";
        if (calendar.getRank() != null && calendar.getRank() != 0) {
            rank = calendar.getRank() + "";
        } else {
            rank = "???";
        }

        String average = "";
        if (calendar.getBangumi_average() != null && calendar.getBangumi_average() != 0) {
            average = calendar.getBangumi_average() + "";
        } else {
            average = "???";
        }
        String time = calendar.getTime();
        if (time.equals(""))
            time = "???";
        holder.mBangumiOutline.setText(String.format(mContext.getString(R.string.score_average), average, rank, time));

        if (calendar.getLarge_image() != null) {
//            Picasso.with(mContext)
//                    .load(calendar.getLarge_image())
//                    .placeholder(R.drawable.img_on_load)
//                    .config(Bitmap.Config.RGB_565)
//                    .transform(transformation)
//                    .error(R.drawable.img_on_error)
//                    .into(holder.mBangumiImg);
            HalfTransformation myTransformation = new HalfTransformation();
            Picasso.with(mContext)
                    .load(calendar.getLarge_image())
                    .placeholder(R.drawable.img_on_load)
                    .config(Bitmap.Config.RGB_565)
                    .transform(myTransformation)
                    .error(R.drawable.img_on_error)
                    .into(holder.mBangumiImg);
        } else {
            Picasso.with(mContext)
                    .load(R.drawable.img_on_error)
                    .into(holder.mBangumiImg);
        }

        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onCalendarClick(holder.mBangumiImg, calendar);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView mBangumiImg;
        TextView mBangumiTitle;
        TextView mBangumiOutline;
        View card;

        public MyViewHolder(View itemView) {
            super(itemView);
            card = itemView;
            mBangumiImg = (ImageView) itemView.findViewById(R.id.calendar_bangumi_img);
            mBangumiTitle = (TextView) itemView.findViewById(R.id.calendar_bangumi_title);
            mBangumiOutline = (TextView) itemView.findViewById(R.id.calendar_bangumi_outline);
        }
    }

    public interface onCalendarItemListener {
        void onCalendarClick(View view, BangumiCalendar calendar);
    }

    public void setOnCalendarItemClickListener(onCalendarItemListener listener) {
        this.listener = listener;
    }

    static class HalfTransformation implements Transformation {

        @Override
        public Bitmap transform(Bitmap source) {
            synchronized (HalfTransformation.class) {
                if (source == null)
                    return null;

                int width = source.getWidth() / 2;
                int height = source.getHeight() / 2;
                Bitmap result = Bitmap.createScaledBitmap(source, width, height, false);

                source.recycle();
                return result;
            }
        }

        @Override
        public String key() {
            return "transformation_half";
        }
    }
}
