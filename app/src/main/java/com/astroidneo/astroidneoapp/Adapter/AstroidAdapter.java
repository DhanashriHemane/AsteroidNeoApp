package com.astroidneo.astroidneoapp.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.astroidneo.astroidneoapp.Planet.AstroidPlanet;
import com.astroidneo.astroidneoapp.R;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class AstroidAdapter extends RecyclerView.Adapter<AstroidAdapter.ListHolder>{

    Context context;
    List<AstroidPlanet> mPlanetlist = new ArrayList<>();

    public AstroidAdapter(Context context, List<AstroidPlanet> mPlanetlist) {
        this.context = context;
        this.mPlanetlist = mPlanetlist;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
        View view = inflater.inflate(R.layout.feed_list, viewGroup, false);
        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int position) {

        holder.textViewid.setText(mPlanetlist.get(position).getId());
        holder.textViewspeed.setText(mPlanetlist.get(position).getMagnitude());
        String distance = mPlanetlist.get(position).getDistance();
        DecimalFormat df = new DecimalFormat();
        df.setMaximumFractionDigits(2);
        double d = Double.parseDouble(distance);
        holder.textViewdistance.setText(df.format(d));

    }

    @Override
    public int getItemCount() {
        return mPlanetlist.size();
    }

    public class ListHolder extends RecyclerView.ViewHolder {
        TextView textViewid, textViewspeed, textViewdistance;
        public ListHolder(@NonNull View itemView) {
            super(itemView);

            textViewdistance = (TextView) itemView.findViewById(R.id.tv_distance);
            textViewid = (TextView) itemView.findViewById(R.id.tv_id);
            textViewspeed = (TextView) itemView.findViewById(R.id.tv_speed);

        }
    }

}
