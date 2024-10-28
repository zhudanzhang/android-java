package com.example.demo07;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class FruitAdapter extends RecyclerView.Adapter<FruitAdapter.ViewHolder> {
    private List<Fruit> mFruitList;

    static class ViewHolder extends RecyclerView.ViewHolder {
        public View fruitView;
        ImageView fruitImage;
        TextView fruitName;

        public ViewHolder(View view) {
            super(view);
            fruitView = view; // 初始化 fruitView
//            竖向滚动
//            fruitImage = view.findViewById(R.id.fruit_image);
//            fruitName = view.findViewById(R.id.fruit_name);
//            横向滚动
//            fruitImage = view.findViewById(R.id.fruit_image1);
//            fruitName = view.findViewById(R.id.fruit_name1);
//            瀑布流
            fruitImage = view.findViewById(R.id.fruit_image2);
            fruitName = view.findViewById(R.id.fruit_name2);
        }
    }

    public FruitAdapter(List<Fruit> fruitList) {
        mFruitList = fruitList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
//        竖向滚动
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item, parent, false);
        //            横向滚动
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item1, parent, false);

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fruit_item2, parent, false);
//        return new ViewHolder(view);
//     添加点击事件
        final ViewHolder holder = new ViewHolder(view);

        // 为整个子项布局注册点击事件
        holder.fruitView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Fruit fruit = mFruitList.get(position);
                Toast.makeText(v.getContext(), "You clicked view " + fruit.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        // 为ImageView单独注册点击事件
        holder.fruitImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int position = holder.getAdapterPosition();
                Fruit fruit = mFruitList.get(position);
                Toast.makeText(v.getContext(), "You clicked image " + fruit.getName(),
                        Toast.LENGTH_SHORT).show();
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Fruit fruit = mFruitList.get(position);
        holder.fruitImage.setImageResource(fruit.getImageId());
        holder.fruitName.setText(fruit.getName());
    }

    @Override
    public int getItemCount() {
        return mFruitList.size();
    }
}