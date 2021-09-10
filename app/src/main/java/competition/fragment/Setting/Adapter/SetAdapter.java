package competition.fragment.Setting.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textview.MaterialTextView;
import com.google.gson.Gson;

import java.util.List;

import competition.R;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.RetrofitManager;
import competition.fragment.Setting.Javabean.bindcollar_javabean;
import competition.fragment.Setting.Javabean.bindcollar_request;
import competition.fragment.Setting.Set_bind;
import competition.fragment.Setting.Setitem;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SetAdapter extends RecyclerView.Adapter<SetAdapter.ViewHolder> {

    private List<Setitem> setList;
    private Context setcontent;


    public SetAdapter(List<Setitem> list,Context context) {
        setList = list;
        setcontent=context;
    }


    @NonNull
    @Override
    public SetAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = View.inflate(setcontent, R.layout.fragment_set_item, parent);
        View view = LayoutInflater.from(setcontent).inflate(R.layout.fragment_set_item,parent,false);
        SetAdapter.ViewHolder viewHolder = new SetAdapter.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.Tvid.setText("项圈ip:"+setList.get(position).getCollarId());
        if(setList.get(position).getUrl()!="")
        Glide.with(setcontent).load(setList.get(position).getUrl()).centerCrop().into(holder.image);
        if(setList.get(position).getPetId()>0){
            holder.Tvlink.setText("已连接到牲畜:"+setList.get(position).getName());
        }else{
            holder.Tvlink.setText("未连接到牲畜...");
        }
        holder.selectBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(setcontent, Set_bind.class);
                intent.putExtra("ip",setList.get(position).getCollarId());
                setcontent.startActivity(intent);
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

        //长按删除
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                deleteSet(position);
                return true;
            }
        });
    }



    @Override
    public int getItemCount() {
        return setList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        MaterialTextView Tvid;
        MaterialTextView Tvlink;
        ShapeableImageView image;
        AppCompatButton selectBtn;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            Tvid=(MaterialTextView) itemView.findViewById(R.id.set_item_id_real);
            Tvlink=(MaterialTextView) itemView.findViewById(R.id.set_item_islink);
            image=(ShapeableImageView) itemView.findViewById(R.id.set_item_imag);
            selectBtn=(AppCompatButton) itemView.findViewById(R.id.select_btn);
        }
    }

    public void deleteSet(int position){
        AlertDialog.Builder builder=new AlertDialog.Builder(setcontent);
        builder.setTitle("删除提醒");
        builder.setMessage("您确定要删除"+setList.get(position).getPetId()+"吗");
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //删除
                setList.remove(position);
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

    //item点击接听
    public interface OnItemClickListener {
        void onClick(int position);
    }
    private SetAdapter.OnItemClickListener listener;

    //第二步， 写一个公共的方法
    public void setOnItemClickListener(SetAdapter.OnItemClickListener listener) {
        this.listener = listener;
    }


}
