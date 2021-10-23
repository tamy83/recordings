package com.yensontam.recordings.recordings.view

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.yensontam.recordings.R
import com.yensontam.recordings.recordings.model.RecordingViewItem
import com.yensontam.recordings.recordings.viewmodel.RecordingsViewModel

class RecordingsListAdapter(private val viewModel: RecordingsViewModel): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

  private var recordings = listOf<RecordingViewItem>()

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
    return ItemViewHolder(
      LayoutInflater
      .from(parent.context)
      .inflate(R.layout.item_recording, parent, false)
    )
  }

  override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
    val viewHolder = holder as ItemViewHolder
    val recordingViewItem = recordings[position]
    viewHolder.fileNameTextView.text = recordingViewItem.name
    viewHolder.durationTextView.text = recordingViewItem.duration
    viewHolder.timeStampTextView.text = recordingViewItem.timeStampString
  }

  override fun getItemCount(): Int {
    return recordings.size
  }

  fun setData(recordingViewItems: List<RecordingViewItem>) {
    recordings = recordingViewItems
    notifyDataSetChanged()
  }

  internal class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
    var fileNameTextView: TextView = itemView.findViewById(R.id.name)
    var durationTextView: TextView = itemView.findViewById(R.id.duration)
    var timeStampTextView: TextView = itemView.findViewById(R.id.timestamp)
  }
}