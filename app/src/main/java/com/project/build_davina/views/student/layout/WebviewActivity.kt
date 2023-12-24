package com.project.build_davina.views.student.layout

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import android.widget.PopupMenu
import androidx.appcompat.app.AppCompatActivity
import com.project.build_davina.R
import com.project.build_davina.databinding.ActivityMhsWebviewBinding
import com.project.build_davina.utils.custom.URLCustom
import com.project.build_davina.utils.helper.SharedPreferences
import com.project.build_davina.views.base.SigninActivity
import com.project.build_davina.views.student.profile.ProfileActivity

class WebviewActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMhsWebviewBinding
    private lateinit var preferences: SharedPreferences

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMhsWebviewBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = "LKPS"
        supportActionBar?.subtitle = "Laporan Kinerja Program Studi"

        preferences = SharedPreferences(this)

        binding.webView.loadUrl(URLCustom.LINK_URL)
        binding.webView.settings.apply {
            javaScriptEnabled = true
            javaScriptCanOpenWindowsAutomatically = true
            setSupportMultipleWindows(true)
        }
        binding.webView.webViewClient = WebViewClient()
        binding.webView.webChromeClient = WebChromeClient()
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.webView.canGoBack()) {
            binding.webView.goBack()
        } else {
            super.onBackPressed()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_options, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mn_option -> {
                val popupMenu = PopupMenu(this, findViewById(R.id.mn_option))
                popupMenu.menuInflater.inflate(R.menu.options_nav_mhs, popupMenu.menu)

                popupMenu.setOnMenuItemClickListener { popupItem ->
                    when (popupItem.itemId) {
                        R.id.nv_profile -> {
                            startActivity(Intent(this, ProfileActivity::class.java))
                            return@setOnMenuItemClickListener true
                        }
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
}