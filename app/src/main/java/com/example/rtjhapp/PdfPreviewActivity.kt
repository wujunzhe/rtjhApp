package com.example.rtjhapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.rtjhapp.databinding.ActivityPdfRenderBinding
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.util.FitPolicy
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

class PdfPreviewActivity : AppCompatActivity(), OnPageChangeListener, OnLoadCompleteListener {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPdfRenderBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val pdfFileName = "test.pdf"
        binding.pdfView.fromAsset(pdfFileName)
            .defaultPage(0)
            .enableSwipe(true)
            .swipeHorizontal(false)
            .onPageChange(this)
            .onLoad(this)
            .autoSpacing(false)
            .pageFitPolicy(FitPolicy.WIDTH)
            .scrollHandle(DefaultScrollHandle(this))
            .load()


    }

    override fun onPageChanged(page : Int, pageCount : Int) {
        // Handle page change events here
    }

    override fun loadComplete(nbPages : Int) {
        // Handle load complete event here
    }
}