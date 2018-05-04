package com.awesomeapp.android.coolnewsapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsViewHolder> {

    private ArrayList<NewsModel> newsList;
    private Context context;

    NewsAdapter(Context context, ArrayList<NewsModel> itemModels) {
        this.context = context;
        this.newsList = itemModels;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View myView = LayoutInflater.from(context).inflate(R.layout.element_layout, parent, false);
        return new NewsViewHolder(myView);
    }

    @Override
    public void onBindViewHolder(@NonNull final NewsViewHolder holder, int position) {

        holder.articleAuthor.setText(newsList.get(position).getAuthorName());
        holder.articleDate.setText(newsList.get(position).getDateOfCreate());
        holder.articleTitle.setText(newsList.get(position).getArticleTitle());
        holder.articleSection.setText(newsList.get(position).getSectionName());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //click
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    public void clearAllData() {
        newsList.clear();
        notifyDataSetChanged();
    }

    public void addAllData(List<NewsModel> news) {
        newsList.clear();
        newsList.addAll(news);
        notifyDataSetChanged();
    }
}
