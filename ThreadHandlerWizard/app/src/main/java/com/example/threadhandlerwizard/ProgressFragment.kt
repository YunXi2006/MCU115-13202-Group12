package com.example.threadhandlerwizard

import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.google.android.material.progressindicator.CircularProgressIndicator

class ProgressFragment : Fragment(R.layout.frag_progress) {
    private val vm: WorkViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val bar = view.findViewById<CircularProgressIndicator>(R.id.progressBar)
        val percent = view.findViewById<TextView>(R.id.txtPercent)
        val text = view.findViewById<TextView>(R.id.txt)

        vm.status.observe(viewLifecycleOwner) { s ->
            text.text = s
            val preparing = s == WorkViewModel.PREPARING
            bar.isIndeterminate = preparing          // 準備中：一直轉圈
            percent.visibility = if (preparing) View.INVISIBLE else View.VISIBLE
            if (!preparing) bar.progress = vm.progress.value ?: 0
        }
        vm.progress.observe(viewLifecycleOwner) { p ->
            if (!bar.isIndeterminate) bar.setProgressCompat(p, true)
            percent.text = getString(R.string.percent, p)
        }
    }
}
