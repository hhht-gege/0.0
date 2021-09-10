package competition.fragment.Anim;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatImageButton;

import com.bigkoo.pickerview.builder.TimePickerBuilder;
import com.bigkoo.pickerview.listener.OnTimeSelectChangeListener;
import com.bigkoo.pickerview.listener.OnTimeSelectListener;
import com.bigkoo.pickerview.view.TimePickerView;
import com.google.android.material.card.MaterialCardView;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import competition.R;
import competition.Utils.API;
import competition.Utils.Constanst;
import competition.Utils.RetrofitManager;
import competition.fragment.Anim.JavaBean.Anim_history_javabean;
import competition.fragment.Anim.JavaBean.warningtemp_request;
import lecho.lib.hellocharts.formatter.LineChartValueFormatter;
import lecho.lib.hellocharts.formatter.SimpleLineChartValueFormatter;
import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Anim_heart_show extends AppCompatActivity implements View.OnClickListener {
    private static String TAG = "Anim_heart_show";
    private TimePickerView pvTime;
    private TextView time_text;
    private TextView time_text2;
    private TextView minute;
    private TextView title;
    private AppCompatImageButton btn_time;
    private String ip;

    private LineChartView lineChart;
    List<PointValue> mPointValues = new ArrayList<PointValue>();
    List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();


    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private MaterialCardView layout;
    private AppCompatImageButton add;
    private AppCompatEditText top;
    private AppCompatEditText end;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_anim_temp_show);
        InitView();
        initTimePicker();
        InitDate();

    }

    public void InitView() {
        title=(TextView) findViewById(R.id.temp_show_title);
        title.setText("心率");
        btn_time = (AppCompatImageButton) findViewById(R.id.select_time_tmp);
        time_text = (TextView) findViewById(R.id.canderview_temp);
        time_text2 = (TextView) findViewById(R.id.temp_time);
        minute = (TextView) findViewById(R.id.temp_minute);
        btn_time.setOnClickListener(this);
        lineChart = (LineChartView) findViewById(R.id.zhexian);
        time = new ArrayList<>();
        data = new ArrayList<>();
    }

    public void InitDate() {
        Date date = new Date(System.currentTimeMillis());
        time_text.setText(getTime(date));
        time_text2.setText(getTime2(date));

        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                minute.setText(getMinute());
            }
        }, 1000, 60000);

        ip = getIntent().getStringExtra("ip");
        getHistory(getTime(date));
        // InitZhexian();
    }

    private void initTimePicker() {//Dialog 模式下，在底部弹出
        pvTime = new TimePickerBuilder(this, new OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                String the_date = getTime(date);
                time_text.setText(the_date);
                getHistory(the_date);
                Log.i("pvTime", "onTimeSelect");

            }
        })
                .setTimeSelectChangeListener(new OnTimeSelectChangeListener() {
                    @Override
                    public void onTimeSelectChanged(Date date) {
                        Log.i("pvTime", "onTimeSelectChanged");
                    }
                })
                .setType(new boolean[]{false, true, true, false, false, false})
                .isDialog(true) //默认设置false ，内部实现将DecorView 作为它的父控件。
                .addOnCancelClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.i("pvTime", "onCancelClickListener");
                    }
                })
                .setItemVisibleCount(3) //若设置偶数，实际值会加1（比如设置6，则最大可见条目为7）
                .setLineSpacingMultiplier(2.0f)
                .isAlphaGradient(true)
                .build();

        Dialog mDialog = pvTime.getDialog();
        if (mDialog != null) {

            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    Gravity.BOTTOM);

            params.leftMargin = 0;
            params.rightMargin = 0;
            pvTime.getDialogContainerLayout().setLayoutParams(params);
            Window dialogWindow = mDialog.getWindow();
            if (dialogWindow != null) {
                dialogWindow.setWindowAnimations(com.bigkoo.pickerview.R.style.picker_view_slide_anim);//修改动画样式
                dialogWindow.setGravity(Gravity.BOTTOM);//改成Bottom,底部显示
                dialogWindow.setDimAmount(0.3f);
            }
        }
    }


    public void temp_back(View view) {
        finish();
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.select_time_tmp && pvTime != null) {
            pvTime.show(v);
        }
    }

    public void warning_temp(View view) {
        builder = new AlertDialog.Builder(this);//创建对话框
        layout = (MaterialCardView) getLayoutInflater().inflate(R.layout.warning_temp, null);//获取自定义布局
        builder.setView(layout);//设置对话框的布局
        dialog = builder.create();//生成最终的对话框
        dialog.show();//显示对话框

        add = (AppCompatImageButton) layout.findViewById(R.id.warning_add);
        top = (AppCompatEditText) layout.findViewById(R.id.warning_temp_top);
        end = (AppCompatEditText) layout.findViewById(R.id.warning_temp_end);
        //设置监听
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Gson gson = new Gson();
                double dtop, dend;
                dtop = Double.parseDouble(top.getText().toString());
                dend = Double.parseDouble(end.getText().toString());
                warningtemp_request request = new warningtemp_request(dtop, dend, getIntent().getIntExtra("petId", -1));
                API api = RetrofitManager.getRetrofit().create(API.class);
                Call<ResponseBody> task = api.warning_temp(Constanst.token, request);
                task.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        Log.d(TAG, "onResponse:" + response.code());
                        try {

                        } catch (Exception e) {
                            e.printStackTrace();
                            Toast.makeText(Anim_heart_show.this, "服务器异常。。。", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Log.d(TAG, "onFailure:" + t.getMessage());
                    }
                });
            }
        });
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
        Log.d("getTime()", "choice date millis: " + date.getTime());
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);
    }

    private String getTime2(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);
    }

    private String getTime3(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("HH:mm");
        return format.format(date);
    }

    private String getMinute() {
        long totalSeconds = System.currentTimeMillis() / 1000;
        long currentMinutes = totalSeconds / 60 % 60; //获取当前分钟数
        long currentHours = totalSeconds / 3600 % 24 + 8; //获取当前小时数
        return String.valueOf(currentHours) + ":" + String.valueOf(currentMinutes);
    }

    public void InitZhexian() {
        getAxisXLables();
        getAxisPoints();
        initLineChart();//初始化

    }

    /**
     * 初始化LineChart的一些设置
     */
    private void initLineChart() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#13C016"));//折线的颜色
        LineChartValueFormatter lineChartValueFormatter = new SimpleLineChartValueFormatter(1);//显示小数点后一位
        line.setFormatter(lineChartValueFormatter);
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(false);//曲线是否平滑
//	    line.setStrokeWidth(3);//线条的粗细，默认是3
        line.setFilled(false);//是否填充曲线的面积
        line.setHasLabels(true);//曲线的数据坐标是否加上备注
//		line.setHasLabelsOnlyForSelected(true);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);

        //坐标轴
        Axis axisX = new Axis(); //X轴
        axisX.setHasTiltedLabels(false);  //X轴下面坐标轴字体是斜的显示还是直的，true是斜的显示
//	    axisX.setTextColor(Color.WHITE);  //设置字体颜色
        axisX.setTextColor(Color.parseColor("#000000"));//黑色

//	    axisX.setName("未来几天的天气");  //表格名称
        axisX.setTextSize(11);//设置字体大小
        axisX.setMaxLabelChars(7); //最多几个X轴坐标，意思就是你的缩放让X轴上数据的个数7<=x<=mAxisValues.length
        axisX.setValues(mAxisXValues);  //填充X轴的坐标名称
        data.setAxisXBottom(axisX); //x 轴在底部
//	    data.setAxisXTop(axisX);  //x 轴在顶部
        axisX.setHasLines(true); //x 轴分割线


        Axis axisY = new Axis();  //Y轴
        axisY.setName("单位：℃");//y轴标注
        axisY.setTextSize(11);//设置字体大小
        data.setAxisYLeft(axisY);  //Y轴设置在左边
        axisY.setTextColor(Color.parseColor("#000000"));
        //data.setAxisYRight(axisY);  //y轴设置在右边
        //设置行为属性，支持缩放、滑动以及平移
        lineChart.setInteractive(true);
        lineChart.setZoomType(ZoomType.HORIZONTAL);  //缩放类型，水平
        lineChart.setMaxZoom((float) 15);//缩放比例
        lineChart.setLineChartData(data);
        lineChart.setVisibility(View.VISIBLE);
        /**注d：下面的7，10只是代表一个数字去类比而已
         * 尼玛搞的老子好辛苦！！！见（http://forum.xda-evelopers.com/tools/programming/library-hellocharts-charting-library-t2904456/page2）;
         * 下面几句可以设置X轴数据的显示个数（x轴0-7个数据），当数据点个数小于（29）的时候，缩小到极致hellochart默认的是所有显示。当数据点个数大于（29）的时候，
         * 若不设置axisX.setMaxLabelChars(int count)这句话,则会自动适配X轴所能显示的尽量合适的数据个数。
         * 若设置axisX.setMaxLabelChars(int count)这句话,
         * 33个数据点测试，若 axisX.setMaxLabelChars(10);里面的10大于v.right= 7; 里面的7，则
         刚开始X轴显示7条数据，然后缩放的时候X轴的个数会保证大于7小于10
         若小于v.right= 7;中的7,反正我感觉是这两句都好像失效了的样子 - -!
         * 并且Y轴是根据数据的大小自动设置Y轴上限
         * 若这儿不设置 v.right= 7; 这句话，则图表刚开始就会尽可能的显示所有数据，交互性太差
         */
        Viewport v = new Viewport(lineChart.getMaximumViewport());
        v.left = 0;
        v.right = 7;
        lineChart.setCurrentViewport(v);
    }

    private List<String> time;
    private List<Float> data;

    /**
     * X 轴的显示
     */
    private void getAxisXLables() {
        for (int i = 0; i < time.size(); i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(time.get(i)));
        }
    }

    private void getAxisPoints() {
        for (int i = 0; i < data.size(); i++) {
            mPointValues.add(new PointValue(i, data.get(i)));
        }
    }

    /**
     * 图表的每个点的显示
     */

    public void getHistory(String day) {
        Gson gson = new Gson();
        API api = RetrofitManager.getRetrofit().create(API.class);
        System.out.println(day);
        time.clear();
        data.clear();
        mAxisXValues.clear();
        mPointValues.clear();
        Call<ResponseBody> task = api.collar_history(Constanst.token, ip, day);
        task.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Log.d(TAG, "onResponse: " + response.code());
                try {
                    String json = response.body().string();
                    Log.d(TAG, json);
                    Anim_history_javabean body = gson.fromJson(json, Anim_history_javabean.class);
                    int code = body.getCode();
                    if (code == 200) {
                        int size = body.getData().getCollarMsgs().size();
                        Log.d(TAG, "onResponse: " + size);
                        for (int i = 0; i < body.getData().getCollarMsgs().size(); i++) {
                            time.add(body.getData().getCollarMsgs().get(i).getTime().substring(11, 16));
                            data.add((float) body.getData().getCollarMsgs().get(i).getHeartrate());
                        }
                        if (body.getData().getCollarMsgs().size() == 0) {
                            Toast.makeText(Anim_heart_show.this, day + "没有历史记录", Toast.LENGTH_SHORT).show();
                        }
                        InitZhexian();
                    } else {
                        Toast.makeText(Anim_heart_show.this, body.getMsg(), Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(Anim_heart_show.this, "服务器异常，请稍候再试", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}
