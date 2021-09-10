package competition.fragment.Anim.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import competition.R;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.RetrofitManager;
import competition.fragment.Anim.Anim;
import competition.fragment.Anim.JavaBean.*;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AnimAdapter extends RecyclerView.Adapter<AnimAdapter.ViewHolder> {
    private List<Anim> animList;
    private Context animcontent;


    public AnimAdapter(List<Anim> list,Context context) {
        animList = list;
       animcontent=context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(animcontent, R.layout.fragment_anim_item, null);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Nullable
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.Tvnick_u.setText(animList.get(position).getPetName());
        holder.Tvkind_u.setText(animList.get(position).getPetBreed());
        Glide.with(animcontent).load(animList.get(position).getPetPortraitPath()).centerCrop().into(holder.image);

        if(animList.get(position).isOvert()==true){
            holder.Tvovert.setText("牲畜名片公开");
        }else{
            holder.Tvovert.setText("牲畜名片不公开");
        }
        holder.delBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteAnim(position);
            }
        });

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
        return animList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView Tvnick_u;
        TextView Tvkind_u;
        TextView Tvovert;
        ImageView image;
        Button delBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Tvnick_u=itemView.findViewById(R.id.anim_item_nick_real);
            Tvkind_u=itemView.findViewById(R.id.anim_item_kind_real);
            Tvovert=itemView.findViewById(R.id.anim_item_isovert);
            image=itemView.findViewById(R.id.anim_item_imag);
            delBtn=itemView.findViewById(R.id.delete_btn);
        }
    }

    public void deleteAnim(int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(animcontent);
        builder.setTitle("删除提醒");
        builder.setMessage("您确定要删除"+animList.get(position).getPetName()+"吗");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //删除
                DeleteAnim(position);

            }
        });
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.show();
    }

    public void DeleteAnim(int position){
        Gson gson = new Gson();
        String token = Constanst.token;
        API api = RetrofitManager.getRetrofit().create(API.class);
        Log.d("AnimAdapter", "Petid->:" + animList.get(position).getPetId());
        Call<ResponseBody> task = api.delete_anim(token,animList.get(position).getPetId());
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d("AnimAdapter", "onResponse:" + response.code());
                try{
                    String json=response.body().string();
                    delete_javabean body=gson.fromJson(json,delete_javabean.class);
                   int code=body.getCode();
                   if(code==200){
                       animList.remove(position);
                       String message = body.getMsg();
                       Toast.makeText(animcontent, message, Toast.LENGTH_SHORT).show();
                       notifyDataSetChanged();
                   }else{
                       String message = body.getMsg();
                       Toast.makeText(animcontent, message, Toast.LENGTH_SHORT).show();
                   }

                }catch (Exception e){
                    e.printStackTrace();
                    Toast.makeText(animcontent, "服务器异常...请稍后再试", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d("AnimAdapter", "onFailure:" + t.getMessage());
            }
        });
    }


    public interface OnItemClickListener {
        void onClick(int position);
    }

    private OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
