package ru.prog_edu.movies;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MoviesAdapter extends RecyclerView.Adapter<MoviesAdapter.MoviesHolder> {

    private final ArrayList<Movie> movieItems;

    public interface OnSelectedItemListener{
        void onListItemClick(int selectedMovie);
    }

    private final OnSelectedItemListener mClickHandler;

    public MoviesAdapter(ArrayList<Movie> movieItems, OnSelectedItemListener mClickHandler) {
        this.movieItems = movieItems;
        this.mClickHandler = mClickHandler;
    }

    @NonNull
    @Override
    public MoviesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context;
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.image_item, parent, false);
        return new MoviesHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull MoviesHolder holder, int position) {
        String mainImageUrl = "http://image.tmdb.org/t/p/w185/";
        String imageItemPosition = mainImageUrl+movieItems.get(position).getPosterPath();

        Picasso.get()
                .load(imageItemPosition)
                .into(holder.itemImageView);
    }

    @Override
    public int getItemCount() {
        return movieItems.size();
    }


    public class MoviesHolder extends RecyclerView.ViewHolder  implements View.OnClickListener{
        private final ImageView itemImageView;
        MoviesHolder(View itemView) {

            super(itemView);
            itemImageView = itemView.findViewById(R.id.item_image_view);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int selectedPosition = getAdapterPosition();
            mClickHandler.onListItemClick(selectedPosition);
        }
    }
}
