package github.tornaco.android.thanos.start;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.view.LayoutInflater;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.MPPointF;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import github.tornaco.android.thanos.R;
import github.tornaco.android.thanos.core.app.ActivityManager;
import github.tornaco.android.thanos.core.app.ThanosManager;
import github.tornaco.android.thanos.core.util.PkgUtils;
import github.tornaco.android.thanos.databinding.ActivityStartChartBinding;
import github.tornaco.android.thanos.theme.ThemeActivity;
import github.tornaco.android.thanos.util.ActivityUtils;
import lombok.AllArgsConstructor;

public class StartChartActivity extends ThemeActivity implements OnChartValueSelectedListener {
    private ActivityStartChartBinding binding;

    public static void start(Context context) {
        ActivityUtils.startActivity(context, StartChartActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStartChartBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        setupView();
        setData();
    }

    private void setupView() {
        setSupportActionBar(binding.toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        int[] attrs = {android.R.attr.textColorPrimary, android.R.attr.windowBackground};
        TypedArray ta = obtainStyledAttributes(attrs);
        int textColorPrimaryRes = ta.getResourceId(0, R.color.md_red_700);
        int windowBgColorRes = ta.getResourceId(1, R.color.md_white);
        ta.recycle();
        int textColorPrimary = getColor(textColorPrimaryRes);
        int windowBgColor = getColor(windowBgColorRes);

        PieChart chart = binding.chart1;

        chart.getDescription().setEnabled(true);
        chart.getDescription().setText("Powered by thanox");
        chart.getDescription().setTextColor(textColorPrimary);
        chart.setExtraOffsets(5, 10, 5, 5);

        chart.setDragDecelerationFrictionCoef(0.95f);

        chart.setCenterText(generateCenterSpannableText());
        chart.setCenterTextColor(textColorPrimary);
        chart.setDrawCenterText(true);

        chart.setDrawHoleEnabled(true);
        chart.setHoleColor(windowBgColor);

        chart.setTransparentCircleColor(windowBgColor);
        chart.setTransparentCircleAlpha(110);

        chart.setHoleRadius(58f);
        chart.setTransparentCircleRadius(61f);

        chart.setRotationAngle(0);
        // enable rotation of the chart by touch
        chart.setRotationEnabled(true);
        chart.setHighlightPerTapEnabled(true);

        // add a selection listener
        chart.setOnChartValueSelectedListener(this);

        chart.animateY(1400, Easing.EaseInOutQuad);
        // chart.spin(2000, 0, 360);

        Legend l = chart.getLegend();
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP);
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT);
        l.setOrientation(Legend.LegendOrientation.VERTICAL);
        l.setDrawInside(false);
        l.setXEntrySpace(7f);
        l.setYEntrySpace(0f);
        l.setYOffset(0f);

        l.setTextColor(textColorPrimary);

        // entry label styling
        chart.setDrawEntryLabels(false);
        chart.setUsePercentValues(true);
    }

    private SpannableString generateCenterSpannableText() {
        ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
        long times = thanosManager.getActivityManager().getStartRecordsBlockedCount();
        return new SpannableString(String.format("%s times", times));
    }

    private void setData() {
        ThanosManager thanosManager = ThanosManager.from(getApplicationContext());
        if (!thanosManager.isServiceInstalled()) {
            return;
        }
        ActivityManager am = thanosManager.getActivityManager();

        final String[] startPkgs = thanosManager.getActivityManager().getStartRecordBlockedPackages();

        List<StartEntry> startEntries = new ArrayList<>();
        for (String pkg : startPkgs) {
            StartEntry e = new StartEntry(am.getStartRecordBlockedCountByPackageName(pkg), pkg);
            startEntries.add(e);
        }
        Collections.sort(startEntries);

        ArrayList<PieEntry> entries = new ArrayList<>();

        // NOTE: The order of the entries when being added to the entries array determines their position around the center of
        // the chart.
        for (int i = 0; (i < startEntries.size() && i < 24); i++) {
            entries.add(new PieEntry(startEntries.get(i).times,
                    PkgUtils.loadNameByPkgName(this, startEntries.get(i).pkg) + " " + startEntries.get(i).times));
        }

        PieDataSet dataSet = new PieDataSet(entries, "");

        dataSet.setDrawIcons(false);
        dataSet.setSliceSpace(3f);
        dataSet.setIconsOffset(new MPPointF(0, 40));
        dataSet.setSelectionShift(5f);

        // add a lot of colors

        ArrayList<Integer> colors = new ArrayList<>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.MATERIAL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());

        dataSet.setColors(colors);
        //dataSet.setSelectionShift(0f);

        PieChart chart = binding.chart1;

        PieData data = new PieData(dataSet);

        DecimalFormat format = new DecimalFormat("###,###,##0.0");
        data.setValueFormatter(new PercentFormatter(chart) {
            @Override
            public String getFormattedValue(float value) {
                return value >= 8f ? format.format(value) + " %" : "";
            }
        });
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.WHITE);
        chart.setData(data);

        // undo all highlights
        chart.highlightValues(null);

        chart.invalidate();
    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }

    @AllArgsConstructor
    class StartEntry implements Comparable<StartEntry> {
        long times;
        String pkg;

        @Override
        public int compareTo(@NonNull StartEntry o) {
            return -Long.compare(this.times, o.times);
        }
    }
}
