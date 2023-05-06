package com.bokchi.mysolelife.contentsList

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bokchi.mysolelife.R
import com.bokchi.mysolelife.utils.FBAuth
import com.bokchi.mysolelife.utils.FBRef
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class ContentListActivity : AppCompatActivity() {

    lateinit var myRef: DatabaseReference

    val bookmarkIdList = mutableListOf<String>() // 북마크 레시피 id값을 담을 리스트

    lateinit var rvAdapter : ContentRVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_content_list)

        val database = Firebase.database
        val items = ArrayList<ContentModel>()
        val itemKeyList = ArrayList<String>()

        rvAdapter = ContentRVAdapter(baseContext, items, itemKeyList, bookmarkIdList)

        val category = intent.getStringExtra("category")
        if(category == "category1") {
            myRef = database.getReference("contents")
        } else if(category == "category2") {
            myRef = database.getReference("contents2")
        }

        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                // dataSnapshot에 들어있는 데이터들을 반복문으로 하나씩 빼오기
                for(dataModel in dataSnapshot.children) {

                    Log.d("ContentListActivity", dataSnapshot.toString())
                    Log.d("ContentListActivity", dataModel.key.toString()) // content id값 찍어보기(key 값)
                    val item = dataModel.getValue(ContentModel::class.java)
                    items.add(item!!)
                    itemKeyList.add(dataModel.key.toString()) // 이렇게 받은 key값을 어댑터로 넘겨줌(북마크 클릭 이벤트에 필요)

                }
                rvAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        myRef.addValueEventListener(postListener)

//         Firebase RealtimeDatabase에 데이터 추가

//        val myRef2 = database.getReference("contents2")
//        for(i in 1..5) {
//            for(k in 0..10) {
//                myRef2.push().setValue(
//                    ContentModel(k.toString(), k.toString(), k.toString())
//                )
//            }
//        }
//        myRef2.push().setValue(
//                ContentModel("title4", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FOtaMq%2Fbtq67OMpk4W%2FH1cd0mda3n2wNWgVL9Dqy0%2Fimg.png", "https://philosopher-chan.tistory.com/1249")
//            )
//        myRef2.push().setValue(
//            ContentModel("title5", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FFtY3t%2Fbtq65q6P4Zr%2FWe64GM8KzHAlGE3xQ2nDjk%2Fimg.png", "https://philosopher-chan.tistory.com/1248")
//        )
//        myRef2.push().setValue(
//            ContentModel("title6", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F8rybA%2Fbtq64mYp1cY%2FVgUEqTkRKgUmoTxlg6mZAK%2Fimg.png", "https://philosopher-chan.tistory.com/1243")
//        )

        val rv : RecyclerView = findViewById(R.id.rv)

//        val items = ArrayList<String>()
//        items.add("a")
//        items.add("b")
//        items.add("c")


//        items.add(ContentModel("간장볶음면 마성의 레시피", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FOtaMq%2Fbtq67OMpk4W%2FH1cd0mda3n2wNWgVL9Dqy0%2Fimg.png", "https://philosopher-chan.tistory.com/1249"))
//        items.add(ContentModel("참치맛나니 초간단레시피", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FFtY3t%2Fbtq65q6P4Zr%2FWe64GM8KzHAlGE3xQ2nDjk%2Fimg.png", "https://philosopher-chan.tistory.com/1248"))
//        items.add(ContentModel("굽네치킨 황금레시피", "https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2F8rybA%2Fbtq64mYp1cY%2FVgUEqTkRKgUmoTxlg6mZAK%2Fimg.png", "https://philosopher-chan.tistory.com/1243"))

        rv.adapter = rvAdapter
        rv.layoutManager = GridLayoutManager(this, 2)

//        rvAdapter.itemClick = object : ContentRVAdapter.ItemClick {
//            override fun onClick(view: View, position: Int) {
//                val intent = Intent(this@ContentListActivity, ContentShowActivity::class.java)
//                intent.putExtra("url", items[position].webUrl)
//                startActivity(intent)
//            }
//        }
        getBookmarkData()
    }

    private fun getBookmarkData() {
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                bookmarkIdList.clear() // clear()하지 않으면 기존의 북마크가 계속 중복되어 쌓임

                for(dataModel in dataSnapshot.children) {

//                    Log.d("getBookmarkData", dataModel.key.toString())
//                    Log.d("getBookmarkData", dataModel.toString())
                    bookmarkIdList.add(dataModel.key.toString())
                }
                Log.d("Bookmark : ", bookmarkIdList.toString()) // 이걸 어댑터로 넘겨줌
                rvAdapter.notifyDataSetChanged() // 동기화
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.w("ContentListActivity", "loadPost:onCancelled", databaseError.toException())
            }
        }
        // 북마크 안에 특정 uid로 된 부분 아래에 있는 값들을
        FBRef.bookmarkRef.child(FBAuth.getUid()).addValueEventListener(postListener)
    }
}