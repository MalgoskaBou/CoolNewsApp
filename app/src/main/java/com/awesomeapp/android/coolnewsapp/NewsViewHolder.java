package com.awesomeapp.android.coolnewsapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class NewsViewHolder extends RecyclerView.ViewHolder {

    TextView articleTitle;
    TextView articleSection;
    TextView articleAuthor;
    TextView articleDate;

    NewsViewHolder(View itemView) {
        super(itemView);
        articleTitle = itemView.findViewById(R.id.articleTitleTxt);
        articleSection = itemView.findViewById(R.id.articleSectionNameTxt);
        articleAuthor = itemView.findViewById(R.id.articleAuthorTxt);
        articleDate = itemView.findViewById(R.id.articleDateTxt);

    }
}

