package com.bokchi.mysolelife.contentsList

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bokchi.mysolelife.R
import com.bokchi.mysolelife.utils.FBAuth
import com.bokchi.mysolelife.utils.FBRef
import com.bumptech.glide.Glide

class ContentRVAdapter(val context : Context,
                       val items : ArrayList<ContentModel>,
                       val keyList : ArrayList<String>,
                       val bookmarkIdList : MutableList<String> // 북마크에 있는 id값과 카테고리에 있는 id값을 비교하기 위해
) : RecyclerView.Adapter<ContentRVAdapter.Viewholder>() {

//    interface ItemClick {
//        fun onClick(view : View, position : Int)
//    }
//    var itemClick : ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContentRVAdapter.Viewholder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.content_rv_item, parent, false)
        Log.d("RVAdapter", "key List : " + keyList.toString())
        Log.d("RVAdapter","Bookmark List : " + bookmarkIdList.toString())
        return Viewholder(v)
    }

    override fun onBindViewHolder(holder: ContentRVAdapter.Viewholder, position: Int) {

//        if(itemClick != null) {
//            holder.itemView.setOnClickListener {v ->
//                itemClick?.onClick(v, position)
//            }
//        }
        holder.bindItems(items[position], keyList[position])
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class Viewholder(itemView : View) : RecyclerView.ViewHolder(itemView) {

        fun bindItems(item : ContentModel, key : String) {

            itemView.setOnClickListener {
                val intent = Intent(context, ContentShowActivity::class.java)
                intent.putExtra("url", item.webUrl)
                itemView.context.startActivity(intent)
            }

            val ContentTitle = itemView.findViewById<TextView>(R.id.TextArea)
            val imageViewArea = itemView.findViewById<ImageView>(R.id.imageArea)
            val bookmarkArea = itemView.findViewById<ImageView>(R.id.BookmarkArea)

            if(bookmarkIdList.contains(key)) { // bookmarkIdList가 KeyList에 있는 정보를 가지고 있다면
                bookmarkArea.setImageResource(R.drawable.bookmark_color)
            } else { // 없다면
                bookmarkArea.setImageResource(R.drawable.bookmark_white)
            }

            bookmarkArea.setOnClickListener {   // 북마크를 누르면 실행되는 내용
                Log.d("ContentRVA", FBAuth.getUid())
                Toast.makeText(context, key, Toast.LENGTH_LONG).show()

                // 북마크를 클릭했을 때, 북마크가 있는 경우 -> 북마크 삭제
                if(bookmarkIdList.contains(key)) {

                    FBRef.bookmarkRef
                        .child(FBAuth.getUid())
                        .child(key)
                        .removeValue()

                } else {
                    // 북마크가 없는 경우 -> 북마크 추가
                    FBRef.bookmarkRef
                        .child(FBAuth.getUid())
                        .child(key)
                        .setValue(BookmarkModel(true))
                }

            }

            ContentTitle.text = item.title

            Glide.with(context)
                .load(item.imageUrl)
                .into(imageViewArea)

        }
     }
}