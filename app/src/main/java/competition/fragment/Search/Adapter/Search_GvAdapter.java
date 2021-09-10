package competition.fragment.Search.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.List;

import competition.R;

public class Search_GvAdapter extends BaseAdapter {
    private Context context;
    private int mMaxPosition;//根据这个list.size+1 来进行判断
    private List<String> list;

    public  Search_GvAdapter(Context context, List<String> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        mMaxPosition=list.size()+1;
        return mMaxPosition;
    }
    //根据情况可以设置是否需要。。
    public int getMaxPosition(){
        return mMaxPosition;
    }
    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(final int position, View v, ViewGroup parent) {
        ViewHolder vh=null;
        if (v==null){
            vh=new ViewHolder();
            v= LayoutInflater.from(context).inflate(R.layout.item_gd,parent,false);
            vh.img= (ImageView) v.findViewById(R.id.search_item_img);
            vh.demimg= (ImageView) v.findViewById(R.id.search_item_deimg);
            v.setTag(vh);
        }else{
            vh= (ViewHolder) v.getTag();
        }
        if (position==mMaxPosition-1){ //说明要显示
            Glide.with(context).load(R.drawable.add_btn).dontAnimate()
                    .centerCrop().into(vh.img);
            vh.img.setVisibility(View.VISIBLE);
            vh.demimg.setVisibility(View.GONE);
            if (position==6&&mMaxPosition==7){//设置最大6个。那么达到最大，就隐藏。
//                vh.img.setImageResource(R.drawable.id_photo);
                vh.img.setVisibility(View.GONE);
            }
        }else{//设置图片。
            vh.demimg.setVisibility(View.VISIBLE);
            Glide.with(context).load(list.get(position)).into(vh.img);//设置
        }
        //删除
        vh.demimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.remove(position);
                notifyDataSetChanged();
            }
        });
        return v;
    }

    public class ViewHolder{
        public ImageView img,demimg;
    }

}
