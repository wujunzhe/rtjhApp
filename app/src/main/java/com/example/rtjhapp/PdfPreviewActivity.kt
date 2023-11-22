package com.example.rtjhapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.rtjhapp.databinding.ActivityPdfRenderBinding
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener
import com.github.barteksc.pdfviewer.util.FitPolicy

class PdfPreviewActivity:AppCompatActivity(), OnPageChangeListener, OnLoadCompleteListener {
    override fun onCreate(savedInstanceState : Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityPdfRenderBinding.inflate(LayoutInflater.from(this))
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

        binding.backBtn.setOnClickListener {
            finish()
        }

    }
    override fun onPageChanged(page: Int, pageCount: Int) {
        // Handle page change events here
    }

    override fun loadComplete(nbPages: Int) {
        // Handle load complete event here
    }
}