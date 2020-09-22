package com.mongodb.custommpandroidchart//プロジェクト構成に合わせ変更

import android.content.Context
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.RadioGroup
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.CandleStickChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.Entry
import com.mongodb.custommpandroidchart.chart.*//プロジェクト構成に合わせ変更
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //折れ線グラフ表示(まずはSingle)
        refreshSingleLineChart(this, findViewById(R.id.lineChartExample))
        refreshSingleBarChart(this, findViewById(R.id.barChartExample))
        refreshCandleStickChart(this, findViewById(R.id.candleStickChartExample))
        refreshPieChart(this, findViewById(R.id.pieChartExample1))

        //単線・時系列・複数線・積み上げをラジオボタンで切替
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroupMultiple)
        radioGroup.setOnCheckedChangeListener{ group, checkedId ->
            when(checkedId){
                //単線
                R.id.radioButtonSingle -> {
                    refreshSingleLineChart(this, findViewById(R.id.lineChartExample))
                    refreshSingleBarChart(this, findViewById(R.id.barChartExample))
                    refreshCandleStickChart(this, findViewById(R.id.candleStickChartExample))
                    refreshPieChart(this, findViewById(R.id.pieChartExample1))
                }
                //時系列(円グラフ以外)
                R.id.radioButtonTimeSeries -> {
                    refreshDateLineChart(this, findViewById(R.id.lineChartExample), true)
                    refreshDateSingleBarChart(this, findViewById(R.id.barChartExample))
                    refreshDateCandleStickChart(this, findViewById(R.id.candleStickChartExample))
                }
                //複数線(折れ線、棒のみ)
                R.id.radioButtonMultiple -> {
                    refreshMutipleLineChart(this, findViewById(R.id.lineChartExample))
                    refreshMultipleBarChart(this, findViewById(R.id.barChartExample))
                }
                //積み上げ(棒のみ)
                R.id.radioButtonStack -> {
                    refreshStackBarChart(this, findViewById(R.id.barChartExample))
                }
                //リアル
                else -> {
                    refreshRealLineChart(this, findViewById(R.id.lineChartExample))
                    refreshRealBarChart(this, findViewById(R.id.barChartExample))
                    refreshRealCandleStickChart(this, findViewById(R.id.candleStickChartExample))
                    refreshRealPieChart1(this, findViewById(R.id.pieChartExample1))
                    refreshRealPieChart2(this, findViewById(R.id.pieChartExample2))
                }
            }
        }
    }

    //折れ線グラフの表示(単線)
    private fun refreshSingleLineChart(context: Context, lineChart: LineChart){
        //表示用サンプルデータの作成//
        val x = listOf<Float>(1f, 2f, 3f, 5f, 8f, 13f, 21f, 34f)//X軸データ
        val y = x.map{it*it}//Y軸データ（X軸の2乗）

        //Chartフォーマット
        var lineChartFormat = LineChartFormat()
        //DataSetフォーマット(カテゴリ名のMap)
        var lineDataSetFormat =  mapOf(
            "linear" to LineDataSetFormat(drawValue = false)
        )

        //①Entryにデータ格納(カテゴリ名のMap)
        val allLinesEntries: MutableMap<String, MutableList<Entry>> = mutableMapOf(
            "linear" to makeLineChartData(x, y)
        )

        //②～⑦グラフの作成
        setupLineChart(allLinesEntries, lineChart, lineChartFormat, lineDataSetFormat, context)
    }

    //折れ線グラフの表示(時系列)
    private fun refreshDateLineChart(context: Context, lineChart: LineChart, timeaccuracy: Boolean){
        //////表示用サンプルデータの作成//////
        //X軸データ(時間)
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val x = listOf<Date>(
            sdf.parse("2020/09/01 00:00:00"),
            sdf.parse("2020/09/01 06:00:00"),
            sdf.parse("2020/09/01 12:00:00"),
            sdf.parse("2020/09/01 18:00:00"),
            sdf.parse("2020/09/02 00:00:00"),
            sdf.parse("2020/09/02 06:00:00"),
            sdf.parse("2020/09/02 12:00:00"),
            sdf.parse("2020/09/03 18:00:00"),
        )
        val y = listOf<Float>(1f, 2f, 3f, 5f, 8f, 13f, 21f, 34f)//Y軸データ(数値)

        //Chartフォーマット
        var lineChartFormat = LineChartFormat(
            timeAccuracy = timeaccuracy,
            xAxisDateFormat = SimpleDateFormat("M/d H:mm"),
            toolTipDateFormat = SimpleDateFormat("M/d H:mm"),
            toolTipDirection = "xy"
        )
        //DataSetフォーマット(カテゴリ名のMap)
        var lineDataSetFormat =  mapOf(
            "linear" to LineDataSetFormat()
        )

        //①Entryにデータ格納(カテゴリ名のMap)
        val allLinesEntries: MutableMap<String, MutableList<Entry>> = mutableMapOf(
            "linear" to makeDateLineChartData(x, y, timeaccuracy)
        )

        //②～⑦グラフの作成
        setupLineChart(allLinesEntries, lineChart, lineChartFormat, lineDataSetFormat, context)
    }

    //折れ線グラフの表示(複数線)
    private fun refreshMutipleLineChart(context: Context, lineChart: LineChart){
        //表示用サンプルデータの作成//
        val x = listOf<Float>(1f, 2f, 3f, 5f, 8f, 13f, 21f, 34f)//X軸データ
        val y1 = x.map{it}//Y軸データ1（X軸の1乗）
        val y2 = x.map{it*it}//Y軸データ2（X軸の2乗）

        //Chartフォーマット
        var lineChartFormat = LineChartFormat(
            bgColor = Color.DKGRAY
        )
        //DataSetフォーマット(カテゴリ名のMap)
        var lineDataSetFormat =  mapOf(
            "linear" to LineDataSetFormat(),
            "square" to LineDataSetFormat(drawValue = true)
        )

        //①Entryにデータ格納(カテゴリ名のMap)
        val allLinesEntries: MutableMap<String, MutableList<Entry>> = mutableMapOf(
            "linear" to makeLineChartData(x, y1),
            "square" to makeLineChartData(x, y2)
        )

        //②～⑦グラフの作成
        setupLineChart(allLinesEntries, lineChart, lineChartFormat, lineDataSetFormat, context)
    }

    //棒グラフの表示(単一棒)
    private fun refreshSingleBarChart(context: Context, barChart: BarChart){
        //表示用サンプルデータの作成//
        val x = listOf<Float>(1f, 2f, 3f, 4f, 6f, 7f, 8f, 9f)//X軸データ
        val y = x.map{it*it}//Y軸データ（X軸の2乗）

        //Chartフォーマット
        var barChartFormat = BarChartFormat(
            toolTipDirection = "xy",
            toolTipUnitX = "年目",
            toolTipUnitY = "万円"
        )
        //DataSetフォーマット(カテゴリ名のMap)
        var barDataSetFormat =  mapOf(
            "square" to BarDataSetFormat()
        )

        //①Entryにデータ格納(カテゴリ名のMap)
        val allBarsEntries: MutableMap<String, MutableList<BarEntry>> = mutableMapOf(
            "square" to makeBarChartData(x, y)
        )

        //②～⑦グラフの作成
        setupBarChart(allBarsEntries, barChart, barChartFormat, barDataSetFormat, context)
    }

    //棒グラフの表示(時系列)
    private fun refreshDateSingleBarChart(context: Context, barChart: BarChart){
        //表示用サンプルデータの作成//
        //X軸データ(時間)
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val x = listOf<Date>(
            sdf.parse("2020/09/01 00:00:00"),
            sdf.parse("2020/09/01 06:00:00"),
            sdf.parse("2020/09/01 12:00:00"),
            sdf.parse("2020/09/01 18:00:00"),
            sdf.parse("2020/09/02 00:00:00"),
            sdf.parse("2020/09/02 06:00:00"),
            sdf.parse("2020/09/02 12:00:00"),
            sdf.parse("2020/09/03 18:00:00"),
        )
        val y = listOf<Float>(1f, 2f, 3f, 5f, 8f, 13f, 21f, 34f)//Y軸データ(数値)

        //Chartフォーマット
        var barChartFormat = BarChartFormat()
        //DataSetフォーマット(カテゴリ名のMap)
        var barDataSetFormat =  mapOf(
            "square" to BarDataSetFormat()
        )

        //①Entryにデータ格納(カテゴリ名のMap)
        val allBarsEntries: MutableMap<String, MutableList<BarEntry>> = mutableMapOf(
            "square" to makeDateBarChartData(x, y)
        )

        //②～⑦グラフの作成
        setupBarChart(allBarsEntries, barChart, barChartFormat, barDataSetFormat, context)
    }

    //棒グラフの表示(複数棒)
    private fun refreshMultipleBarChart(context: Context, barChart: BarChart){
        //表示用サンプルデータの作成
        val x = listOf<Float>(1f, 3f, 5f, 7f, 9f, 11f, 13f, 15f)//X軸データ
        val y1 = x.map{it}//Y軸データ1（X軸の1乗）
        val y2 = x.map{it*it}//Y軸データ2（X軸の2乗）

        //Chartフォーマット
        var barChartFormat = BarChartFormat(toolTipDirection = "xy")
        //DataSetフォーマット(カテゴリ名のMap)
        var barDataSetFormat =  mapOf(
            "linear" to BarDataSetFormat(),
            "square" to BarDataSetFormat()
        )

        //①Entryにデータ格納(カテゴリ名のMap)
        val allBarsEntries: MutableMap<String, MutableList<BarEntry>> = mutableMapOf(
            "linear" to makeBarChartData(x, y1),
            "square" to makeBarChartData(x, y2)
        )

        //②～⑦グラフの作成
        setupBarChart(allBarsEntries, barChart, barChartFormat, barDataSetFormat, context)
    }

    //棒グラフの表示(積み上げ)
    private fun refreshStackBarChart(context: Context, barChart: BarChart){
        //表示用サンプルデータの作成//
        val x = listOf<Float>(1f, 2f, 3f, 4f, 6f, 7f, 8f, 9f)//X軸データ
        val y = x.map{ mutableListOf(it, it*it)}//Y軸データ（1項目:X軸の1乗、2項目:Xの2乗）

        //Chartフォーマット
        var barChartFormat = BarChartFormat(toolTipDirection = "xy")
        //DataSetフォーマット(カテゴリ名のMap)
        var barDataSetFormat =  mapOf(
            "stack" to BarDataSetFormat(
                stackLabels = listOf("linear","square")
            )
        )

        //①Entryにデータ格納(カテゴリ名のMap)
        val allBarsEntries: MutableMap<String, MutableList<BarEntry>> = mutableMapOf(
            "stack" to makeStackBarChartData(x, y)
        )

        //②～⑦グラフの作成
        setupBarChart(allBarsEntries, barChart, barChartFormat, barDataSetFormat, context)
    }

    //ローソク足グラフの表示
    private fun refreshCandleStickChart(context: Context, candleStickChart: CandleStickChart){
        //表示用サンプルデータの作成//
        val x = listOf<Float>(2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f)//X軸データ
        val yHigh = x.map{it * 2}//Y軸データ（最大値）
        val yLow = x.map{it}//Y軸データ（最小値）
        val yOpen = x.map{it + 1}//Y軸データ（開始値）
        val yClose = x.map{it + 2}//Y軸データ（終了値）

        //Chartフォーマット
        var candleChartFormat = CandleChartFormat(bgColor = Color.BLACK)
        //DataSetフォーマット(カテゴリ名のMap)
        var candleDataSetFormat = CandleDataSetFormat()

        //①Entryにデータ格納(カテゴリ名のMap)
        val candleEntries = makeCandleChartData(x, yHigh, yLow, yOpen, yClose)

        //②～⑦グラフの作成
        setupCandleStickChart(candleEntries, candleStickChart, candleChartFormat, candleDataSetFormat, context)
    }

    //ローソク足グラフの表示(時系列)
    private fun refreshDateCandleStickChart(context: Context, candleStickChart: CandleStickChart){
        //表示用サンプルデータの作成//
        //X軸データ(時間)
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss")
        val x = listOf<Date>(
            sdf.parse("2020/09/01 00:00:00"),
            sdf.parse("2020/09/01 06:00:00"),
            sdf.parse("2020/09/01 12:00:00"),
            sdf.parse("2020/09/01 18:00:00"),
            sdf.parse("2020/09/02 00:00:00"),
            sdf.parse("2020/09/02 06:00:00"),
            sdf.parse("2020/09/02 12:00:00"),
            sdf.parse("2020/09/03 18:00:00"),
        )
        val ySeed = listOf<Float>(2f, 3f, 4f, 5f, 6f, 7f, 8f, 9f)//Y軸データ生成用
        val yHigh = ySeed.map{it * 2}//Y軸データ（最大値）
        val yLow = ySeed.map{it}//Y軸データ（最小値）
        val yOpen = ySeed.map{it + 1}//Y軸データ（開始値）
        val yClose = ySeed.map{it + 2}//Y軸データ（終了値）

        //Chartフォーマット
        var candleChartFormat = CandleChartFormat(
            xAxisDateFormat = SimpleDateFormat("M/d H:mm"),
            toolTipDateFormat = SimpleDateFormat("M/d H:mm")
        )
        //DataSetフォーマット
        var candleDataSetFormat = CandleDataSetFormat()

        //①Entryにデータ格納
        val candleEntries = makeDateCandleChartData(x, yHigh, yLow, yOpen, yClose)

        //②～⑦グラフの作成
        setupCandleStickChart(candleEntries, candleStickChart, candleChartFormat, candleDataSetFormat, context)
    }

    //円グラフの表示
    private fun refreshPieChart(context: Context, pieChart: PieChart){
        //表示用サンプルデータの作成//
        val dimensions = listOf("A", "B", "C", "D")//分割円の名称(String型)
        val values = listOf(1f, 2f, 3f, 4f)//分割円の大きさ(Float型)

        //Chartフォーマット
        var pieChartFormat = PieChartFormat()
        //DataSetフォーマット
        var pieDataSetFormat = PieDataSetFormat()

        //①Entryにデータ格納
        val pieEntries = makePieChartEntries(dimensions, values)

        //②～⑦グラフの作成
        setupPieChart(pieEntries, pieChart, "PieChart", pieChartFormat, pieDataSetFormat)
    }

    //折れ線グラフの表示(GDP推移)
    private fun refreshRealLineChart(context: Context, lineChart: LineChart){
        //表示用サンプルデータの作成//
        //X軸データ(時間)
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/M")
        val x = listOf<Date>(
            sdf.parse("1990/1"),
            sdf.parse("1995/1"),
            sdf.parse("2000/1"),
            sdf.parse("2005/1"),
            sdf.parse("2010/1"),
            sdf.parse("2015/1"),
            sdf.parse("2018/1")
        )
        val y1 = listOf(6.0f, 7.6f, 10.3f, 13.0f, 15.0f, 18.2f, 20.6f)//Y軸データ1（アメリカ）
        val y2 = listOf(0.4f, 0.7f, 1.2f, 2.3f, 6.1f, 11.2f, 13.4f)//Y軸データ2（中国）
        val y3 = listOf(3.1f, 5.4f, 4.8f, 4.8f, 5.7f, 4.4f, 5.0f)//Y軸データ3（日本）
        val y4 = listOf(1.6f, 2.6f, 1.9f, 2.8f, 3.4f, 3.4f, 4.0f)//Y軸データ4（ドイツ）

        //Chartフォーマット
        var lineChartFormat = LineChartFormat(
            legendTextSize = 10f,
            description = "主要国GDP推移",
            descriptionTextSize = 15f,
            descriptionYOffset = -10f,
            bgColor = Color.DKGRAY,
            xAxisDateFormat = SimpleDateFormat("yyyy年"),
            toolTipDateFormat = SimpleDateFormat("yyyy年"),
            toolTipDirection = "xy",
            toolTipUnitY = "兆ドル"
        )
        //DataSetフォーマット(カテゴリ名のMap)
        var lineDataSetFormat =  mapOf(
            "アメリカ" to LineDataSetFormat(
                lineColor = UNIVERSAL_BLUE,
                lineWidth = 2f
            ),
            "中国" to LineDataSetFormat(
                lineColor = UNIVERSAL_RED,
                lineWidth = 2f
            ),
            "日本" to LineDataSetFormat(
                lineColor = UNIVERSAL_SKYBLUE,
                lineWidth = 2f
            ),
            "ドイツ" to LineDataSetFormat(
                lineColor = Color.LTGRAY,
                lineWidth = 2f
            )
        )

        //①Entryにデータ格納(カテゴリ名のMap)
        val allLinesEntries: MutableMap<String, MutableList<Entry>> = mutableMapOf(
            "アメリカ" to makeDateLineChartData(x, y1, false),
            "中国" to makeDateLineChartData(x, y2, false),
            "日本" to makeDateLineChartData(x, y3, false),
            "ドイツ" to makeDateLineChartData(x, y4, false)
        )

        //②～⑦グラフの作成
        setupLineChart(allLinesEntries, lineChart, lineChartFormat, lineDataSetFormat, context)
    }

    //棒グラフの表示(日本の年齢別人口)
    private fun refreshRealBarChart(context: Context, barChart: BarChart){
        //表示用サンプルデータの作成//
        //X軸データ(時間)
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/M")
        val x = listOf<Date>(
            sdf.parse("1990/1"),
            sdf.parse("1995/1"),
            sdf.parse("2000/1"),
            sdf.parse("2005/1"),
            sdf.parse("2010/1"),
            sdf.parse("2015/1"),
            sdf.parse("2020/1")
        )
        //Y軸データ
        val y = listOf( mutableListOf(2254f, 8614f, 1493f),
            mutableListOf(2003f, 8726f, 1827f),
            mutableListOf(1850f, 8638f, 2204f),
            mutableListOf(1759f, 8442f, 2576f),
            mutableListOf(1684f, 8174f, 2948f),
            mutableListOf(1595f, 7728f, 3386f),
            mutableListOf(1513f, 7481f, 3601f)
        )

        //Chartフォーマット
        var barChartFormat = BarChartFormat(
            legendTextSize = 10f,
            description = "日本の年齢別人口",
            descriptionTextSize = 15f,
            descriptionYOffset = -10f,
            bgColor = Color.rgb(48,48,48),
            toolTipDirection = "xy",
            xAxisDateFormat = SimpleDateFormat("yyyy年"),
            toolTipDateFormat = SimpleDateFormat("yyyy年"),
            toolTipUnitY = "万人"
        )
        //DataSetフォーマット(カテゴリ名のMap)
        var barDataSetFormat =  mapOf(
            "" to BarDataSetFormat(
                stackLabels = listOf("-14","15-64","65-")
            )
        )

        //①Entryにデータ格納(カテゴリ名のMap)
        val allBarsEntries: MutableMap<String, MutableList<BarEntry>> = mutableMapOf(
            "" to makeDateStackBarChartData(x, y)
        )

        //②～⑦グラフの作成
        setupBarChart(allBarsEntries, barChart, barChartFormat, barDataSetFormat, context)
    }

    //ローソク足グラフの表示(日経平均)
    private fun refreshRealCandleStickChart(context: Context, candleStickChart: CandleStickChart){
        //表示用サンプルデータの作成//
        //X軸データ(時間)
        val sdf: SimpleDateFormat = SimpleDateFormat("yyyy/M")
        val x = listOf<Date>(
            sdf.parse("1990/1"),
            sdf.parse("1995/1"),
            sdf.parse("2000/1"),
            sdf.parse("2005/1"),
            sdf.parse("2010/1"),
            sdf.parse("2015/1"),
            sdf.parse("2020/1")
        )
        val yHigh = listOf(38916f, 27147f, 22667f, 16344f, 18262f, 20868f, 24271f)//高値
        val yLow = listOf(12882f, 14309f, 12880f, 7608f, 7055f, 8160f, 14952f)//安値
        val yOpen = listOf(13137f, 23959f, 20243f, 13739f, 16236f, 10314f, 18742f)//始値
        val yClose = listOf(23959f, 20243f, 13739f, 16236f, 10314f, 18742f, 23360f)//終値

        //Chartフォーマット
        var candleChartFormat = CandleChartFormat(
            legendFormat = null,
            description = "日経平均株価推移",
            descriptionTextSize = 15f,
            descriptionYOffset = -10f,
            bgColor = Color.DKGRAY,
            xAxisDateFormat = SimpleDateFormat("yyyy年"),
            toolTipDateFormat = SimpleDateFormat("yyyy年"),
            toolTipDirection = "xy",
            toolTipUnitY = "円(平均)"
        )
        //DataSetフォーマット
        var candleDataSetFormat = CandleDataSetFormat(
            shadowColor = Color.LTGRAY,
            shadowWidth = 3f,
            valueTextSize = 9f,
            valueTextFormatter = "￥%.0f"
        )

        //①Entryにデータ格納
        val candleEntries = makeDateCandleChartData(x, yHigh, yLow, yOpen, yClose)

        //②～⑦グラフの作成
        setupCandleStickChart(candleEntries, candleStickChart, candleChartFormat, candleDataSetFormat, context)
    }

    private fun refreshRealPieChart1(context: Context, pieChart: PieChart){
        //表示用サンプルデータの作成//
        val dimensions = listOf("アメリカ", "日本", "ドイツ", "中国", "その他")//分割円の名称(String型)
        val values = listOf(7.6f, 5.4f, 2.6f, 0.7f, 14.6f)//分割円の大きさ(Float型)

        //Chartフォーマット
        var pieChartFormat = PieChartFormat(
            legendFormat = null,
            description = "世界のGDP(1995年)",
            descriptionTextSize = 15f,
            bgColor = Color.rgb(56,56,56),
            holeColor = Color.rgb(56,56,56),
            holeRadius = 40f,
            transparentCircleRadius = 45f
        )
        //DataSetフォーマット
        var pieDataSetFormat = PieDataSetFormat(
            valueTextColor = Color.WHITE,
            valueTextSize = 10f,
            valueTextFormatter = "%.0f兆ドル",
            colors = listOf(UNIVERSAL_BLUE, UNIVERSAL_SKYBLUE, Color.DKGRAY, UNIVERSAL_RED, UNIVERSAL_BROWN)
        )

        //①Entryにデータ格納
        val pieEntries = makePieChartEntries(dimensions, values)

        //②～⑦グラフの作成
        setupPieChart(pieEntries, pieChart, "", pieChartFormat, pieDataSetFormat)
    }

    private fun refreshRealPieChart2(context: Context, pieChart: PieChart){
        //表示用サンプルデータの作成//
        val dimensions = listOf("アメリカ", "中国", "日本", "ドイツ", "その他")//分割円の名称(String型)
        val values = listOf(20.6f, 13.4f, 5.0f, 4.0f, 42.1f)//分割円の大きさ(Float型)

        //Chartフォーマット
        var pieChartFormat = PieChartFormat(
            legendFormat = null,
            description = "世界のGDP(2018年)",
            descriptionTextSize = 15f,
            bgColor = Color.rgb(48,48,48),
            holeColor = Color.rgb(48,48,48),
            holeRadius = 40f,
            transparentCircleRadius = 45f
        )
        //DataSetフォーマット
        var pieDataSetFormat = PieDataSetFormat(
            valueTextColor = Color.WHITE,
            valueTextSize = 10f,
            valueTextFormatter = "%.0f兆ドル",
            colors = listOf(UNIVERSAL_BLUE, UNIVERSAL_RED, UNIVERSAL_SKYBLUE, Color.DKGRAY, UNIVERSAL_BROWN)
        )

        //①Entryにデータ格納
        val pieEntries = makePieChartEntries(dimensions, values)

        //②～⑦グラフの作成
        setupPieChart(pieEntries, pieChart, "", pieChartFormat, pieDataSetFormat)
    }
}