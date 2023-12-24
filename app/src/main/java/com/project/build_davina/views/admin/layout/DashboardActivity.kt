package com.project.build_davina.views.admin.layout

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.project.build_davina.R
import com.project.build_davina.api.viewmodel.DocumentViewModel
import com.project.build_davina.databinding.ActivityAdmDashboardBinding
import com.project.build_davina.utils.helper.SharedPreferences
import com.project.build_davina.views.admin.criteria.CriteriaActivity
import com.project.build_davina.views.admin.document.DocumentActivity
import com.project.build_davina.views.admin.type.TypeActivity
import com.project.build_davina.views.base.SigninActivity

class DashboardActivity : AppCompatActivity() {
    private lateinit var binding: ActivityAdmDashboardBinding
    private lateinit var documentViewModel: DocumentViewModel
    private lateinit var preferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdmDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "Dashboard"
        preferences = SharedPreferences(this)

        documentViewModel = ViewModelProvider(this).get(DocumentViewModel::class.java)
        binding.btnCriteria.setOnClickListener {
            startActivity(Intent(this, CriteriaActivity::class.java))
        }
        binding.btnType.setOnClickListener {
            startActivity(Intent(this, TypeActivity::class.java))
        }
        binding.btnDocument.setOnClickListener {
            startActivity(Intent(this, DocumentActivity::class.java))
        }
        getChart()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mn_option -> {
                val popupMenu = PopupMenu(this, findViewById(R.id.mn_option))
                popupMenu.menuInflater.inflate(R.menu.options_nav_adm, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener { popupItem ->
                    when (popupItem.itemId) {
                        R.id.nv_logout -> {
                            preferences.remove("id")
                            startActivity(Intent(this, SigninActivity::class.java))
                            finishAffinity()
                            return@setOnMenuItemClickListener true
                        }
                        else -> return@setOnMenuItemClickListener false
                    }
                }

                popupMenu.show()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun getChart() {
        documentViewModel.getChart().observe(this, Observer { data ->
            val barEntries = ArrayList<BarEntry>()
            for (i in data.indices) {
                barEntries.add(BarEntry(i.toFloat(), data[i].count.toFloat(), data[i].crated_at))
            }
            setupBarChart(barEntries)
        })
    }

    private fun setupBarChart(barEntries: List<BarEntry>) {
        val dataSet = BarDataSet(barEntries, "Data Set")
        dataSet.setColors(*ColorTemplate.PASTEL_COLORS)
        dataSet.valueTextSize = 12f
        dataSet.valueTextColor = Color.WHITE

        val dataSets = ArrayList<IBarDataSet>()
        dataSets.add(dataSet)

        val data = BarData(dataSets)

        binding.barChart.data = data
        binding.barChart.description.isEnabled = false
        binding.barChart.xAxis.setDrawGridLines(false)
        binding.barChart.xAxis.setDrawAxisLine(true)
        binding.barChart.xAxis.setDrawLabels(true)
        binding.barChart.xAxis.labelCount = barEntries.size

        val labels = ArrayList<String>()
        for (entry in barEntries) {
            labels.add(entry.data as String)
        }
        val xAxis = binding.barChart.xAxis
        xAxis.valueFormatter = IndexAxisValueFormatter(labels)
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.granularity = 1f

        binding.barChart.axisRight.isEnabled = false
        binding.barChart.legend.isEnabled = false
        binding.barChart.animateY(1000, Easing.EaseInOutCubic)

        binding.barChart.invalidate()
    }
}