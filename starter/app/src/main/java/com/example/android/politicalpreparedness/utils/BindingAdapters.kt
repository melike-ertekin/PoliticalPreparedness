package com.example.android.politicalpreparedness.utils

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.databinding.BindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.domain.Election
import com.example.android.politicalpreparedness.domain.Representative
import com.example.android.politicalpreparedness.election.adapter.ElectionListAdapter
import com.example.android.politicalpreparedness.representative.adapter.RepresentativeListAdapter


@BindingAdapter("listData")
fun bindRecyclerView(recyclerView: RecyclerView, data: List<Election>?) {

    Log.d("electionsData", data?.size.toString())
    val adapter = recyclerView.adapter as ElectionListAdapter
    adapter.submitList(data)

}

@BindingAdapter("representativeData")
fun bindRecyclerView2(recyclerView: RecyclerView, data: List<Representative>?) {

    Log.d("representativeData", data?.size.toString())
    val adapter = recyclerView.adapter as RepresentativeListAdapter
    adapter.submitList(data)

}

@BindingAdapter("linkOnClick")
fun bindOnTextClickListener(textView: TextView, url: String?) {

    Log.d("myurl", url.toString())
    textView.setOnClickListener { view: View? ->
        if (url != null && url != "") {
            val context: Context = textView.context
            val intent = Intent(Intent.ACTION_VIEW)
            intent.data = Uri.parse(url)

            context.startActivity(intent)
        }
    }
}

@BindingAdapter("app:loadImage")
fun loadImage(view: ImageView, url: String?) {
    val uri = (url ?: "").toUri().buildUpon().scheme("https").build()

    Glide.with(view).load(uri)
            .placeholder(R.drawable.ic_profile)
            .error(R.drawable.ic_profile)
            .circleCrop()
            .into(view)
}