package competition.fragment.Search.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textview.MaterialTextView;

import java.util.List;

import competition.R;
import competition.fragment.Search.Search;


public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.ViewHolder> {
    private List<Search> searchList;
    private Context searchcontent;
    private SearchAdapter_gv gv;


    public SearchAdapter(List<Search> list,Context context) {
        searchList = list;
        searchcontent=context;
    }


    @NonNull
    @Override
    public SearchAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(searchcontent, R.layout.fragment_search_item, null);
        SearchAdapter.ViewHolder viewHolder = new SearchAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Nullable
    @Override
    public void onBindViewHolder(SearchAdapter.ViewHolder holder, int position) {
        Glide.with(searchcontent).load(searchList.get(position).getPublisherAvatarUrl()).centerCrop().into(holder.userimag);
        holder.username.setText(searchList.get(position).getPublisherUsername());
        holder.time.setText(searchList.get(position).getPublishTime());
        holder.content.setText(searchList.get(position).getContent());
        gv=new SearchAdapter_gv(searchcontent,searchList.get(position).getPicturesUrl());
        holder.gridView.setAdapter(gv);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return searchList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatImageView userimag;
        MaterialTextView content;
        MaterialTextView username;
        MaterialTextView time;
        GridView gridView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
          userimag=(AppCompatImageView) itemView.findViewById(R.id.search_userimag);
          content=(MaterialTextView) itemView.findViewById(R.id.search_content);
          username=(MaterialTextView) itemView.findViewById(R.id.search_username);
          time=(MaterialTextView) itemView.findViewById(R.id.search_time);
          gridView=(GridView) itemView.findViewById(R.id.search_grid);
        }
    }

    public void deleteAnim(int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(searchcontent);
        builder.setTitle("删除提醒");
        builder.setMessage("您确定要删除这条动态吗");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //删除
                searchList.remove(position);
                //更新adapter
                notifyDataSetChanged();
            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void DeleteSearch(int position){

    }


    public interface OnItemClickListener {
        void onClick(int position);
    }
    private SearchAdapter.OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(SearchAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }
}
