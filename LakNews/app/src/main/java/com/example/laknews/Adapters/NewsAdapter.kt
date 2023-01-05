package com.example.laknews.Adapters

import android.annotation.SuppressLint
import android.print.PrintDocumentAdapter
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.ActionMenuItemView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.laknews.Models.Article
import com.example.laknews.R
import com.bumptech.glide.Glide
import com.example.laknews.Util.Utils
import kotlinx.android.synthetic.main.item_article_preview.view.*
/**
 * An adapter class to populate a recycler view with articles
 *
 * @author Hadi Jalali
 * @since 10/11/2020
 */

class NewsAdapter :RecyclerView.Adapter<NewsAdapter.ArticleViewHolder>() {

    inner class ArticleViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    //DiffUtil is used to only update the new articles to increase the efficiency
    private val differCallback = object:DiffUtil.ItemCallback<Article>(){
        //return true if the new item is equals to the old article
        override fun areItemsTheSame(oldItem: Article, newItem: Article): Boolean {
            //used url because some articles don't have an id
            return oldItem.url == newItem.url
        }

        override fun areContentsTheSame(oldItem: Article, newItem: Article): Boolean {
            return oldItem == newItem
        }
    }
    //check the difference between the adapter and the differCallback that was created above
    val differ = AsyncListDiffer(this,differCallback)
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ArticleViewHolder {
        return ArticleViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_article_preview,parent,false)
        )
    }

    //populates recycler view with appropriate values for each article attribute
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ArticleViewHolder, position: Int) {
        val currentArticle = differ.currentList[position]
        holder.itemView.apply {
            Glide.with(this).load(currentArticle.urlToImage).into(article_image)
            source.text = currentArticle.source?.name
            title.text = currentArticle.title
            desc.text = currentArticle.description
            publishedAt.text = Utils.DateFormat(currentArticle.publishedAt)
            time.text ="\u2020" + Utils.DateToTimeFormat(currentArticle.publishedAt)

            //setting the created clickListener
            setOnClickListener{
                onItemClickListener?.let { it(currentArticle) }
            }
        }
    }
    //next few blocks creates a clickListener to be used on article cards in the application
    override fun getItemCount(): Int {
        return differ.currentList.size
    }
    private var onItemClickListener : ((Article) -> Unit)? = null

    fun setOnItemClickListener(listener:(Article) -> Unit) {
        onItemClickListener = listener
    }
}