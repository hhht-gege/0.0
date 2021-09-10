package competition.fragment.Look;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import competition.R;
import competition.databinding.FragmentLookBinding;
import competition.databinding.FragmentLookKindBinding;
import lecho.lib.hellocharts.listener.PieChartOnValueSelectListener;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.PieChartView;

public class KindFragment extends Fragment {
    private FragmentLookKindBinding binding;

    private PieChartView pieChart;     //饼状图View
    private PieChartData data;         //存放数据

    private boolean hasLabels = true;                   //是否有标语
    private boolean hasLabelsOutside = false;           //扇形外面是否有标语
    private boolean hasCenterCircle = true;            //是否有中心圆
    private boolean hasCenterText1 = true;             //是否有中心的文字，大字体
    private boolean hasCenterText2 = false;             //是否有中心的文字2，和第一种文字的大小不一样，小字体，但是好像没有用！
    private boolean isExploded = false;                  //是否是炸开的图像
    private boolean hasLabelForSelected = false;         //选中的扇形显示标语

    private List<Integer> listRange = new ArrayList<Integer>();    //每个扇形范围的数量
    private String[] rangeString = {"猪", "牛", "羊"};//每个范围的标签显示


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding=FragmentLookKindBinding.inflate(inflater,container,false);
        TextView tv_title = binding.tvTitle;
        initView(binding);
        initEvent();
        initData();
        return binding.getRoot();
    }

    private void initView(FragmentLookKindBinding binding) {
        pieChart = (PieChartView) binding.pieChartKind;
    }

    private void initEvent() {
        pieChart.setOnValueTouchListener(new ValueTouchListener());
    }

    private void initData() {
        generateData();
    }


    /**
     * 配置数据
     */
    private void generateData() {
        //数据
        int[] num = {88, 44, 66, 98, 45, 65, 65, 45, 32, 99, 89, 100, 2, 33, 44, 12, 33, 65, 45, 32, 99, 88,};
        int under_60 = 0;
        int be60_80 = 0;
        int be80_90 = 0;

        //判断各个数据的范围
        for (int i = 0; i < num.length; i++) {
            if (num[i] < 60) {
                under_60++;
            } else if (num[i] < 80) {
                be60_80++;
            } else if (num[i] < 90) {
                be80_90++;
            }
        }


        listRange.add(under_60);
        listRange.add(be60_80);
        listRange.add(be80_90);

        int numValues = 3;   //扇形的数量
        int[] colors = {Color.RED, Color.BLUE, Color.YELLOW};   //各个扇形显示的颜色
        List<SliceValue> values = new ArrayList<SliceValue>();
        for (int i = 0; i < numValues; ++i) {
            //判断各个数据范围占360度的多少度
            SliceValue sliceValue = new SliceValue(360f * listRange.get(i) / num.length, colors[i]);
            sliceValue.setLabel("范围 " + rangeString[i] + ":" + listRange.get(i) + "头"); //设置扇形显示的文字
            values.add(sliceValue);
        }

        data = new PieChartData(values);
        data.setHasLabels(hasLabels);
        data.setHasLabelsOnlyForSelected(hasLabelForSelected);
        data.setHasLabelsOutside(hasLabelsOutside);
        data.setHasCenterCircle(hasCenterCircle);

        if (isExploded) {
            data.setSlicesSpacing(24);
        }

        if (hasCenterText1) {
            data.setCenterText1("牲畜种类图");

            // Get roboto-italic font.    //Typeface是用来设置字体的!
            /*Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/huawen.ttf");
            data.setCenterText1Typeface(tf);
*/
            // Get font size from dimens.xml and convert it to sp(library uses sp values).
            data.setCenterText1FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
//                    (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));  //大字体
                    (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));    //小字体
        }

        if (hasCenterText2) {
            data.setCenterText2("学生成绩统计");
            Typeface tf = Typeface.createFromAsset(getActivity().getAssets(), "fonts/huawen.ttf");

            data.setCenterText2Typeface(tf);
            data.setCenterText2FontSize(ChartUtils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                    (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
        }
        pieChart.setPieChartData(data);
    }


    /**
     * 点击监听
     */
    private class ValueTouchListener implements PieChartOnValueSelectListener {

        @Override
        public void onValueSelected(int arcIndex, SliceValue value) {
            showToast(rangeString[arcIndex] + "的数量是：" + listRange.get(arcIndex) + "，占的比重是：" + (value.getValue() / 360f) + "%");
        }

        @Override
        public void onValueDeselected() {

        }

    }

    Toast toast;

    /**
     * 显示对话框
     *
     * @param msg
     */
    private void showToast(String msg) {
        if (toast == null) {
            toast = Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT);
        } else {
            toast.setText(msg);
        }
        toast.show();
    }
}
