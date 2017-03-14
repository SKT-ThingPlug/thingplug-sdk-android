package com.skt.onem2m_service.ui;

import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.skt.onem2m_service.R;
import com.skt.onem2m_service.data.SensorInfo;
import com.skt.onem2m_service.data.SensorType;
import com.skt.onem2m_service.data.UserInfo;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.TimeSeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * activity for detail sensor info
 *
 * Copyright (C) 2017. SK Telecom, All Rights Reserved.
 * Written 2017, by SK Telecom
 */
public class SensorDetailActivity extends AppCompatActivity {

    private final static String     TAG = SensorDetailActivity.class.getSimpleName();

    private final String[]          COLORS = new String[] {"#e0002a", "#00e02a", "#2a00e0", "#e0e02a", "#2ae0e0", "#e02ae0"};

    private UserInfo                userInfo;
    private SensorInfo              sensorInfo;

    protected GraphicalView         graphView;
    private XYSeries[]              graphDataSerieses;
    private XYMultipleSeriesRenderer    graphRenderer;

    private Timer                   timer = new Timer();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_detail);

        userInfo = UserInfo.getInstance(this);

        Bundle bundle = getIntent().getExtras();
        SensorType sensorType = (SensorType) bundle.get(SensorListActivity.EXTRA_SENSOR_TYPE);

        List<SensorInfo> sensorInfos = SensorListActivity.getSensorInfos();
        for(SensorInfo sensorInfo : sensorInfos) {
            if(sensorInfo.getType() == sensorType) {
                this.sensorInfo = sensorInfo;
                break;
            }
        }

        ActionBar bar = getSupportActionBar();
        bar.setTitle(R.string.actionbar_detail);

        createGraph();

        ImageView itemImageView = (ImageView)findViewById(R.id.item_image);
        TextView itemNameView = (TextView)findViewById(R.id.item_name);
        TextView itemStatus = (TextView)findViewById(R.id.item_status);
        ToggleButton itemEnable = (ToggleButton)findViewById(R.id.item_enable);
        ToggleButton itemActivate = (ToggleButton)findViewById(R.id.item_activate);
        Button itemActuatorRun = (Button)findViewById(R.id.item_actuator_run);

        itemImageView.setImageResource(sensorInfo.getType().getImage());
        itemNameView.setText(sensorInfo.getType().getNickname());
        itemStatus.setText(sensorInfo.toString());
        itemEnable.setVisibility(View.GONE);
        itemActivate.setVisibility(View.GONE);
        itemActuatorRun.setVisibility(View.GONE);

        final Handler handler = new Handler();
        TimerTask task = new TimerTask() {
            public void run() {
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        if (!sensorInfo.isActivated()) { return; }

                        for(int loop1 = 0 ; loop1 < sensorInfo.getType().getValueNumbers() ; loop1++) {
                            graphDataSerieses[loop1].add(graphDataSerieses[loop1].getItemCount(), sensorInfo.getValues()[loop1]);
                        }
                        if(graphDataSerieses[0].getItemCount() >= 100) {
                            graphRenderer.setXAxisMin(graphDataSerieses[0].getItemCount() - 100);
                            graphRenderer.setXAxisMax((int) (graphRenderer.getXAxisMin() + 100));
                        }
                        graphView.invalidate();

                        TextView itemStatus = (TextView)findViewById(R.id.item_status);
                        itemStatus.setText(sensorInfo.toString());
                    }
                });
            }
        };
        timer.schedule(task, 0, userInfo.loadGraphInterval());
    }

    protected void createGraph() {
        graphRenderer = new XYMultipleSeriesRenderer();

        graphRenderer.setChartTitle("Line Graph");
        graphRenderer.setChartTitleTextSize(50);
        graphRenderer.setMargins(new int[] {100, 100, 70, 50});     // top, left, bottom, right

        graphRenderer.setBackgroundColor(Color.WHITE);
        graphRenderer.setMarginsColor(Color.parseColor("#FFFFFF"));
        graphRenderer.setAxesColor(Color.DKGRAY);

        graphRenderer.setXTitle("Time(sec)");
        graphRenderer.setYTitle("Value");
        graphRenderer.setAxisTitleTextSize(30);
        graphRenderer.setLabelsTextSize(25);
        graphRenderer.setYLabelsAlign(Paint.Align.RIGHT);

//        graphRenderer.setPointSize(50);
        graphRenderer.setLegendTextSize(30);

        graphRenderer.setXAxisMin(0);
        graphRenderer.setXAxisMax(100);
        graphRenderer.setZoomEnabled(false, false);

        graphDataSerieses = new XYSeries[sensorInfo.getType().getValueNumbers()];
        XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();;
        for(int loop1 = 0 ; loop1 < sensorInfo.getType().getValueNumbers() ; loop1++) {
            graphDataSerieses[loop1] = new TimeSeries(sensorInfo.getType().getValueInfos()[loop1][0] + "(" + sensorInfo.getType().getValueInfos()[loop1][1] + ")");
            dataset.addSeries(graphDataSerieses[loop1]);

            XYSeriesRenderer seriesRenderer = new XYSeriesRenderer();
            seriesRenderer.setColor(Color.parseColor(COLORS[loop1]));
            seriesRenderer.setPointStyle(PointStyle.POINT);
            seriesRenderer.setLineWidth(3);
            graphRenderer.addSeriesRenderer(seriesRenderer);
        }
        graphView = ChartFactory.getLineChartView(this, dataset, graphRenderer);

        LinearLayout graphArea = (LinearLayout) findViewById(R.id.ll_graph_area);
        graphArea.addView(graphView);
        graphArea.setClickable(false);
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
