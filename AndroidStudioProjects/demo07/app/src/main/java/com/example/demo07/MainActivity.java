package com.example.demo07;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Bundle;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    private List<Fruit> fruitList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initFruits(); // 初始化水果数据

        RecyclerView recyclerView=(RecyclerView)findViewById(R.id.recycler_view);

//        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        //  横向滚动
//        layoutManager.setOrientation(LinearLayoutManager.HORIZONTAL); // 设置为横向布局
        // 瀑布流
        StaggeredGridLayoutManager layoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(layoutManager);

        FruitAdapter adapter = new FruitAdapter(fruitList);
        recyclerView.setAdapter(adapter);
    }
//    横向竖向布局
//    private void initFruits() {
//        for (int i = 0; i < 2; i++) {
//            Fruit apple = new Fruit("Apple", R.drawable.apple_pic);
//            fruitList.add(apple);
//            Fruit banana = new Fruit("Banana", R.drawable.banana_pic);
//            fruitList.add(banana);
//            Fruit orange = new Fruit("Orange", R.drawable.orange_pic);
//            fruitList.add(orange);
//            Fruit watermelon = new Fruit("Watermelon", R.drawable.watermelon_pic);
//            fruitList.add(watermelon);
//            Fruit pear = new Fruit("Pear", R.drawable.pear_pic);
//            fruitList.add(pear);
//            Fruit grape = new Fruit("Grape", R.drawable.grape_pic);
//            fruitList.add(grape);
//            Fruit pineapple = new Fruit("Pineapple", R.drawable.pineapple_pic);
//            fruitList.add(pineapple);
//            Fruit strawberry = new Fruit("Strawberry", R.drawable.strawberry_pic);
//            fruitList.add(strawberry);
//            Fruit cherry = new Fruit("Cherry", R.drawable.cherry_pic);
//            fruitList.add(cherry);
//            Fruit mango = new Fruit("Mango", R.drawable.mango_pic);
//            fruitList.add(mango);
//        }
//    }
//    瀑布流
    private void initFruits() {
        for (int i = 0; i < 2; i++) {
            fruitList.add(new Fruit(getRandomLengthName("Apple"), R.drawable.apple_pic));
            fruitList.add(new Fruit(getRandomLengthName("Banana"), R.drawable.banana_pic));
            fruitList.add(new Fruit(getRandomLengthName("Orange"), R.drawable.orange_pic));
            fruitList.add(new Fruit(getRandomLengthName("Watermelon"), R.drawable.watermelon_pic));
            fruitList.add(new Fruit(getRandomLengthName("Pear"), R.drawable.pear_pic));
            fruitList.add(new Fruit(getRandomLengthName("Grape"), R.drawable.grape_pic));
            fruitList.add(new Fruit(getRandomLengthName("Pineapple"), R.drawable.pineapple_pic));
            fruitList.add(new Fruit(getRandomLengthName("Strawberry"), R.drawable.strawberry_pic));
            fruitList.add(new Fruit(getRandomLengthName("Cherry"), R.drawable.cherry_pic));
            fruitList.add(new Fruit(getRandomLengthName("Mango"), R.drawable.mango_pic));
        }
    }
    private String getRandomLengthName(String name) {
        Random random = new Random();
        int length = random.nextInt(20) + 1;
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < length; i++) {
            builder.append(name);
        }
        return builder.toString();
    }
}