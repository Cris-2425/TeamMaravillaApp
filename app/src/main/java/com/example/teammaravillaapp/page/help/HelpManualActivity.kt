package com.example.teammaravillaapp.page.help

import android.graphics.Bitmap
import android.graphics.pdf.PdfRenderer
import android.os.Bundle
import android.os.ParcelFileDescriptor
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import androidx.activity.ComponentActivity
import java.io.File

class HelpManualActivity : ComponentActivity() {

    private var pdfRenderer: PdfRenderer? = null
    private var fileDescriptor: ParcelFileDescriptor? = null
    private var currentPage = 0

    private lateinit var imageView: ImageView
    private lateinit var btnPrev: Button
    private lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        actionBar?.hide()

        val file = File(cacheDir, "manual.pdf")

        if (!file.exists()) {
            assets.open("manual/TeamMaravilla_Manual.pdf").use { input ->
                file.outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        }

        fileDescriptor = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY)
        pdfRenderer = PdfRenderer(fileDescriptor!!)

        imageView = ImageView(this).apply {
            adjustViewBounds = true
            scaleType = ImageView.ScaleType.FIT_CENTER
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val scrollView = ScrollView(this).apply {
            setPadding(0, 24, 0, 24)
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                0,
                1f
            )
            addView(imageView)
        }

        btnPrev = Button(this).apply {
            text = "Anterior"
            setOnClickListener {
                if (currentPage > 0) {
                    currentPage--
                    showPage(currentPage)
                }
            }
        }

        btnNext = Button(this).apply {
            text = "Siguiente"
            setOnClickListener {
                if (currentPage < pdfRenderer!!.pageCount - 1) {
                    currentPage++
                    showPage(currentPage)
                }
            }
        }

        val buttonRow = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )

            addView(btnPrev, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
            addView(btnNext, LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT, 1f))
        }

        val rootLayout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )

            addView(scrollView)
            addView(buttonRow)
        }

        setContentView(rootLayout)

        showPage(currentPage)
    }

    private fun showPage(index: Int) {
        val page = pdfRenderer!!.openPage(index)

        val bitmap = Bitmap.createBitmap(
            page.width * 2,
            page.height * 2,
            Bitmap.Config.ARGB_8888
        )

        page.render(bitmap, null, null, PdfRenderer.Page.RENDER_MODE_FOR_DISPLAY)
        page.close()

        imageView.setImageBitmap(bitmap)

        btnPrev.isEnabled = currentPage > 0
        btnNext.isEnabled = currentPage < pdfRenderer!!.pageCount - 1
    }

    override fun onDestroy() {
        pdfRenderer?.close()
        fileDescriptor?.close()
        super.onDestroy()
    }
}